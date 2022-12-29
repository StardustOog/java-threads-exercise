package dev.stardustoog.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ProcessImageToImg implements Runnable {

    private int startingRow;
    private int endingRow;
    private BufferedImage bufferedImage;

    public ProcessImageToImg(int startingRow, int endingRow, BufferedImage bufferedImage) {
        this.startingRow = startingRow;
        this.endingRow = endingRow;
        this.bufferedImage = bufferedImage;
    }

    @Override
    public void run() {
        for(int i = startingRow; i <= endingRow; i++) {
            for(int k = 0; k < bufferedImage.getWidth(); k++) {
                Color c = new Color(bufferedImage.getRGB(k, i));
                int avg = (c.getBlue() + c.getRed() + c.getGreen()) / 3;
                if (avg >= 128) {
                    c = new Color(255, 255, 255);
                    bufferedImage.setRGB(k, i, c.getRGB());
                } else {
                    bufferedImage.setRGB(k, i, 0);
                }
            }
        }
    }
}
