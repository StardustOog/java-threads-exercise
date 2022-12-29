package dev.stardustoog.image;

public class ThreadCountException extends RuntimeException{

    public ThreadCountException() {
        super("Inappropriate thread count!");
    }

}
