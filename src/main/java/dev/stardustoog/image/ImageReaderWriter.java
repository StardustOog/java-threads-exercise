package dev.stardustoog.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageReaderWriter {

    public void readAndWrite() {
        Scanner scanner = new Scanner(System.in);
        String imagePath = getFileName(scanner);
        OutputType outputType = outputType(scanner);
        System.out.print("Please enter the count of threads you want to use: ");
        int threadCount = scanner.nextInt();
        processImage(imagePath, outputType, threadCount);
    }

    private void processImage(String imagePath, OutputType outputType, int threadCount) {
        BufferedImage image = null;
        try {
            //BufferedImage isn't autocloseable so I didn't use try-with-resources
            //but if I wanted to the solution would be that I'd create wrapper class for BufferedImage
            //which would implement autocloseable
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(threadCount > image.getHeight() || threadCount <= 0) throw new ThreadCountException();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        if(outputType == OutputType.TXT_TYPE) {
            try {
                processImageToTXT(image, executorService, image.getHeight()/threadCount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            processImageToIMG(image, executorService, threadCount);
        }
    }

    /*
    17

    3
    0-2 3-5 6-8 9-11 12-14 15-17
     */

    private void processImageToTXT(BufferedImage img, ExecutorService executorService, int countForThread) throws IOException {
        try(FileWriter fileWriter = new FileWriter(UUID.randomUUID() + ".txt"))
        {
            int height = img.getHeight();
            List< List<String> > imageRows = new ArrayList<>();
            for (int i = 0; i < countForThread * (height/countForThread); i += countForThread) {
                imageRows.add(rowsToTxt(i, i + countForThread - 1, img, executorService));
            }
            imageRows.add(rowsToTxt(countForThread * (height/countForThread) + height%countForThread,
                    height - 1, img, executorService));
            //writing into file
            for(int i = 0; i < imageRows.size(); i++) {
                for (int k = 0; k < imageRows.get(i).size(); k++) {
                    fileWriter.write(imageRows.get(i).get(k));
                }
            }
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<String> rowsToTxt(int startingRow, int endingRow, BufferedImage image, ExecutorService executorService) throws ExecutionException, InterruptedException {
        ProcessImageToTxt processImageToTxt = new ProcessImageToTxt(startingRow, endingRow, image);
        Future<List<String>> result = executorService.submit(processImageToTxt);
        return result.get();
    }


    private void processImageToIMG(BufferedImage img, ExecutorService executorService, int countForThread) {
        int height = img.getHeight();
        try
        {
            // File isn't autocloseable or else I'd use try-with-resources
            for (int i = 0; i < countForThread * (height/countForThread); i += countForThread) {
                ProcessImageToImg processImageToImg = new ProcessImageToImg(i, i + countForThread - 1, img);
                executorService.submit(processImageToImg);
            }
            ProcessImageToImg processImageToImg =
                    new ProcessImageToImg(countForThread * (height/countForThread) + height%countForThread,
                    height - 1, img);
            executorService.submit(processImageToImg);
            File file = new File(UUID.randomUUID() + ".jpeg");
            ImageIO.write(img, "jpg", file);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    private String getFileName(Scanner scanner) {
        System.out.print("Please enter image path: ");
        String imagePath = scanner.nextLine();
        String reg = ".*" + ".jpeg" + "$";
        while(!imagePath.matches(reg)) {
            System.out.print("Your input format wasn't jpeg, please enter jpeg file: ");
            imagePath = scanner.nextLine();
        }
        return imagePath;
    }

    private OutputType outputType(Scanner scanner) {
        System.out.print("\nPlease enter write file type (TXT or IMAGE): ");
        String writeFileType = scanner.nextLine();
        System.out.println();
        while(!writeFileType.equals("TXT") && !writeFileType.equals("IMAGE")) {
            System.out.print("message type should be TXT or IMAGE: ");
            writeFileType = scanner.nextLine();
        }
        return writeFileType.equals("TXT")? OutputType.TXT_TYPE : OutputType.IMAGE_TYPE;
    }


}
