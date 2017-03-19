package com.balatamilmani.exception;

public class FileServiceFileNotFoundException extends RuntimeException {

    public FileServiceFileNotFoundException(String message) {
        super(message);
    }

    public FileServiceFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}