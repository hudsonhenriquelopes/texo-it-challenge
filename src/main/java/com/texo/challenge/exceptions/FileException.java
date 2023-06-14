package com.texo.challenge.exceptions;

public class FileException extends RuntimeException {
    protected FileException(String message) {
        super(message);
    }

    public FileException(String message, Object... params) {
        this(String.format(message, params));
    }
}

