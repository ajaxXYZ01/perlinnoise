package perlinnoise;

public class vec2 {

    private static final float EPSILON = 1e-6f;;
    public float x, y;

    public vec2() {
        x = y = 0;
    }
    public vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // --- Operations ---

    public static void add(vec2 u, vec2 v, vec2 res) {
        res.x = u.x + v.x;
        res.y = u.y + v.y;
    }

    public static void sub(vec2 u, vec2 v, vec2 res) {
        res.x = u.x - v.x;
        res.y = u.y - v.y;
    }

    public static float dot(vec2 u, vec2 v) {
        return u.x*v.x + u.y*v.y;
    }

    // --- Chainable Operations ---

    public vec2 add(vec2 u) {
        x += u.x;
        y += u.y;
        return this;
    }

    public vec2 sub(vec2 u) {
        x -= u.x;
        y -= u.y;
        return this;
    }

    public float dot(vec2 u) {
        return x*u.x + y*u.y;
    }

    // --- Transformations ---

    public vec2 scale(float sx, float sy) {
        x *= sx;
        y *= sy;
        return this;
    }

    public vec2 scale(float s) {
        x *= s;
        y *= s;
        return this;
    }

    public static void rotate(vec2 v, float sin_angle, float cos_angle) {
        float x = v.x;
        float y = v.y;

        v.x = x*cos_angle - y*sin_angle;
        v.y = x*sin_angle + y*cos_angle;
    }

    public static void translate(vec2 v, float dx, float dy) {
        v.x += dx;
        v.y += dy;
    }

    // --- Utils ---
    
    public float len() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public float lenSquared() {
        return x*x + y*y;
    }

    public static boolean normalize(vec2 v) {
        float len = (float) Math.sqrt(v.x*v.x + v.y*v.y);

        if (len < EPSILON)
            return false;

        v.x /= len;
        v.y /= len;

        return true;
    }

    // Chainable
    public vec2 normalized() {

        float len = len();

        if (len < EPSILON)
            return null;
        
        x /= len;
        y /= len;

        return this;
    }

    public vec2 perpendicular() {
        float temp = x;
        x = -y;
        y = temp;
        return this;
    }

    public vec2 copy() {
        return new vec2(x, y);
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + ">";
    }
}