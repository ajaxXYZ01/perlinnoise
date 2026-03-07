public class Noise {

    // Grid size
    private int gridWidth, gridHeight;
    // Cached gradients vectors
    private vec2[][] gradients;

    public Noise(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        gradients = new vec2[gridWidth + 1][gridHeight + 1];
        generateGradients();
    }

    // Generating Randomly rotated vectors
    private void generateGradients() {
        for (int x = 0; x <= gridWidth; x++) {
            for (int y = 0; y <= gridHeight; y++) {
                double angle = Math.random() * Math.PI * 2;
                gradients[x][y] = new vec2((float) Math.cos(angle), (float) Math.sin(angle));
            }
        }
    }

    /* This is a 4x4 grid size example
     *      1         2         3         4
     * +---------+---------+---------+---------+
     * |         |         |         |         |
     * |         |         |         |         | 1
     * |         |         |         |         |
     * +---------+---------D---------C---------+
     * |         |         |         |         |
     * |         |         |    P    |         | 2
     * |         |         |         |         |
     * +---------+---------A---------B---------+
     * |         |         |         |         |
     * |         |         |         |         | 3
     * |         |         |         |         |
     * +---------+---------+---------+---------+
     * |         |         |         |         |
     * |         |         |         |         | 4
     * |         |         |         |         |
     * +---------+---------+---------+---------+
     * 
     * P is in 'noise coordinates'
     * If P is initally in different space (e.g. world space, pixel space)
     * it needs to be converted to noise space (formula to convert depends on the space type)
     * 
     * A is the local bottom-left grid coordinate of P (depends on P)
     * A = ((int) Math.floor(x(P)), (int) Math.floor(y(P)))
     * 
     * B = A + 1 gives the very left grid cooridinnate of A
     * 
     * 
     */


    public float sample(float x, float y) {

        /*  P = (x, y)
         *
         *  D(x0, y1) +---------+ C(x1, y1)
         *            |         |
         *            |    P    |
         *            |         |
         *  A(x0, y0) +---------+ B(x1, y0)
         */

        // bottom-left
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);

        // top-right
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        // Direction vectors of (P) w.r.t (A) ranges from 0 - 1 (for both x and y)
        float local_x = x - x0;
        float local_y = y - y0;

        // Direction vectors of (P) w.r.t the cornors
        vec2 d00 = new vec2(local_x    , local_y    ); // AP
        vec2 d10 = new vec2(local_x - 1, local_y    ); // BP
        vec2 d01 = new vec2(local_x    , local_y - 1); // CP
        vec2 d11 = new vec2(local_x - 1, local_y - 1); // DP

        // Gradient vectors | randomly rotated vectors
        vec2 g00 = gradients[x0][y0];
        vec2 g10 = gradients[x1][y0];
        vec2 g01 = gradients[x0][y1];
        vec2 g11 = gradients[x1][y1];

        // how much is P in direction of the cornors
        float n00 = vec2.dot(g00, d00);
        float n10 = vec2.dot(g10, d10);
        float n01 = vec2.dot(g01, d01);
        float n11 = vec2.dot(g11, d11);

        local_x = fade(local_x);
        local_y = fade(local_y);

        /*
         * interp_x1
         * +====>----+
         * |         |
         * |  value  |
         * |         |
         * +====>----+
         * interp_x0
         * 
         * Interpolating the depth values [Horizontally]
         */
        float interp_x0 = lerp(n00, n10, local_x);
        float interp_x1 = lerp(n01, n11, local_x);

        /*
         * interp_x1
         * +====>----+
         * |    |    |
         * |    v    |
         * |  value  |
         * |    ^    |
         * |    |    |
         * +====>----+
         * interp_x0
         * 
         * Interpolating interp_x0 and  interp_x1 [Vertically]
         */
        float value = lerp(interp_x0, interp_x1, local_y);
        
        return value; // this is the perlin noise value ranges from [-1, 1]
    }

    /*
     * 6x^5 - 15x^4 + 10t^3
     *
     * A Polynomial with derivative's 0 (slope parallel to x-axis) at t = 0 and t = 1
     * 
     * Nothing Special, just used to smoothly transition between
     * the value's near cornor's, instead of linear transition.
     * 
     * This fixes artifacts
     */
    private float fade(float t) {
        return t*t*t*(t*(6*t - 15) + 10);
    }
    
    // Linear interpolation
    public static float lerp(float a, float b, float t) {
        return (b - a) * t + a;
    }

    // ----------------------------------------------------------------
    // Setters
    // ----------------------------------------------------------------

    public void rotateGradientVectors(float delta_angle) {

        float sin_delta_angle = (float) Math.sin(delta_angle);
        float cos_delta_angle = (float) Math.cos(delta_angle);

        for (int x = 0; x <= gridWidth; x++) {
            for (int y = 0; y <= gridHeight; y++) {
                vec2.rotate(gradients[x][y], sin_delta_angle, cos_delta_angle);
            }
        }
    }

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------

    int getGridWidth()  { return gridWidth;  }
    int getGridHeight() { return gridHeight; }
}
