package com.example.khub.error.exceptions;

public class ImageNotFoundException extends RuntimeException {
    public static String MESSAGE = "Image with id %s not found";

    public ImageNotFoundException(String message) {
        super(message);
    }

}
