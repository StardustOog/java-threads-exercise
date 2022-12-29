package dev.stardustoog.filescoppier;

import java.io.*;

public class FileCoppierRunnable implements Runnable {
    private File sourceDir;
    private File destDir;
    private String nameBeginning;

    public FileCoppierRunnable(File sourceDir, File destDir, String nameBeginning) {
        this.sourceDir = sourceDir;
        this.destDir = destDir;
        this.nameBeginning = nameBeginning;
    }

    @Override
    public void run() {
        for(File file:sourceDir.listFiles()) {
            if(file.isFile()) {
                try {
                    copyFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String dirName = getName(file);
                Thread thread = new Thread(new FileCoppierRunnable(file, destDir,
                        nameBeginning + dirName + "_"));
                thread.start();
            }
        }

    }

    private String getName(File file) {
        String name = file.getPath();
        int lastIndex = name.lastIndexOf('/');
        String fileName = name.substring(lastIndex + 1);
        System.out.println(fileName);
        return fileName;
    }

    private void copyFile(File file) throws IOException {
        String fileName = getName(file);
        File file1 = new File(destDir.getAbsolutePath() + "/" + nameBeginning + fileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(file1);
            int l;
            byte[] chunk = new byte[1024];
            while ((l = inputStream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, l);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

}
