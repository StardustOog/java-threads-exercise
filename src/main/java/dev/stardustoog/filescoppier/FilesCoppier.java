package dev.stardustoog.filescoppier;

import java.io.File;

public class FilesCoppier {


    public void filesCopy(String sourceDirName, String destDirName) {
        File sourceFolder = new File(sourceDirName);

        File destFolder = new File(destDirName);
        System.out.println(destDirName);
        Thread thread = new Thread(new FileCoppierRunnable(sourceFolder, destFolder, ""));
        thread.start();
    }
}
