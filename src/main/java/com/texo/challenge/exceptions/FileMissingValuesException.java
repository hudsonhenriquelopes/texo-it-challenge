package com.texo.challenge.exceptions;

public class FileMissingValuesException extends FileException {

    public FileMissingValuesException(String message, Object... params) {
        super(message, params);
    }
}
