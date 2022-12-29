package dev.stardustoog;


import dev.stardustoog.filescoppier.FilesCoppier;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        ImageReaderWriter imageReaderWriter = new ImageReaderWriter();
//        imageReaderWriter.readAndWrite();
        Scanner scanner = new Scanner(System.in);
        FilesCoppier coppier = new FilesCoppier();
        System.out.print("Please enter source dir name: ");
        String sourceName = scanner.nextLine();
        System.out.print("Please enter dest dir name: ");
        String destName = scanner.nextLine();
        coppier.filesCopy(sourceName, destName);
    }
}
