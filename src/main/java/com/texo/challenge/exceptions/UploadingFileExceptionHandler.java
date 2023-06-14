package com.texo.challenge.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
class UploadingFileExceptionHandler {

    @ExceptionHandler({FileException.class, FileNotFoundException.class})
    public ResponseEntity<String> handleUploadingFileException(final FileException exception) {
        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
