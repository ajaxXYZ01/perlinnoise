package perlinnoise;

public class Noise {
    private int gridWidth, gridHeight;
    public vec2[][] gradients;

    public Noise(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        gradients = new vec2[gridWidth + 1][gridHeight + 1];
        GenerateGradients();
    }

    private void GenerateGradients() {
        for (int x = 0; x <= gridWidth; x++) {
            for (int y = 0; y <= gridWidth; y++) {
                double angle = Math.random() * Math.PI * 2;
                gradients[x][y] = new vec2((float) Math.cos(angle), (float) Math.sin(angle));
            }
        }
    }
}
