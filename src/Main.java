import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

    Main() {

        Noise noise = new Noise(256,256);
        int width  = 512;
        int height = 512;
        
        JFrame window = new JFrame("This is a Perlin Noise Texture");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        BufferedImage image_texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        float scale = 1 / 32f;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float noise_x = x / ((float) width)  * noise.getGridWidth()  * scale;
                float noise_y = y / ((float) height) * noise.getGridHeight() * scale;
                
                float value = noise.sampleFractal(noise_x, noise_y, 16);
                int color = (int)((value + 1) * 0.5f * 255);

                image_texture.setRGB(x, y, new Color(color, color, color).getRGB());
            }
        }

        // writePNG(image_texture, "noise_render_" + System.currentTimeMillis() + ".png");

        JPanel canvas = new JPanel() {
            protected void paintComponent(Graphics gfx) {
                super.paintComponent(gfx);
                gfx.drawImage(image_texture, 0, 0, null);
            }
        };

        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setBackground(Color.RED);

        window.add(canvas);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static void main(String args[]) {        
        new Main();
    }

    public static void writePNG(BufferedImage image, String fileName) {
        File outputFile = new File("renders\\" + fileName);
        try {
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}