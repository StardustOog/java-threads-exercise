package dev.stardustoog.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ProcessImageToTxt implements Callable<List<String>> {

    private int startRowNum;
    private int endRowNum;
    private BufferedImage bufferedImage;

    public ProcessImageToTxt(int startRowNum, int endRowNum, BufferedImage bufferedImage) {
        this.startRowNum = startRowNum;
        this.endRowNum = endRowNum;
        this.bufferedImage = bufferedImage;
    }

    @Override
    public List<String> call() {

        int width = bufferedImage.getWidth();
        List<String> rowChars = new ArrayList<>();

        for(int i = startRowNum; i <= endRowNum; i++) {

            for (int k = 0; k < width; k++) {

                Color c = new Color(bufferedImage.getRGB(k, i));
                int avg = (c.getBlue() + c.getRed() + c.getGreen()) / 3;

                if (avg >= 128) {
                    rowChars.add(".");
                } else {
                    rowChars.add(";");
                }

            }

        }
        return rowChars;
    }

}
