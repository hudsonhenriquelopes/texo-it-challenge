package com.texo.challenge.utils;

import com.texo.challenge.exceptions.FileException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileServiceTest {
    private static final String RESOURCE_PATH = "./src/test/resources/";

    @Test
    void testReadMoviesSuccess() {
        String filePath = new File(RESOURCE_PATH + "movielist.csv").getAbsolutePath();
        assertFalse(ReadFileService.readMovies(filePath).isEmpty());
    }

    @Test
    void testReadMoviesFailWhenInvalidPath() {
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies("https://"));
        assertEquals("Invalid path. This should be a local CSV file path.", exception.getMessage());
    }

    @Test
    void testReadMoviesFailWhenFileDoesNotExist() {
        String filePath = new File(RESOURCE_PATH + "invalid.csv").getAbsolutePath();
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies(filePath));
        assertEquals(String.format("File does not exist. (Path: \"%s\")", filePath), exception.getMessage());
    }

    @Test
    void testReadMoviesFailsWhenFileIsEmpty() {
        String filePath = new File(RESOURCE_PATH + "empty.csv").getAbsolutePath();
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies(filePath));
        assertEquals("File has no header or content.", exception.getMessage());
    }

    @Test
    void testReadMoviesFailsWhenHasNoHeader() {
        String filePath = new File(RESOURCE_PATH + "noheader.csv").getAbsolutePath();
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies(filePath));
        assertEquals("File has no header.", exception.getMessage());
    }

    @Test
    void testReadMoviesFailsWhenMissingColumnsOnHeader() {
        String filePath = new File(RESOURCE_PATH + "missingcolumnsonheader.csv").getAbsolutePath();
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies(filePath));
        assertEquals("File is missing column on header: producers.", exception.getMessage());
    }

    @Test
    void testReadMoviesFailsWhenHasOnlyHeader() {
        String filePath = new File(RESOURCE_PATH + "onlyheader.csv").getAbsolutePath();
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies(filePath));
        assertEquals("File has only the header.", exception.getMessage());
    }

    @Test
    void testReadMoviesFailsWhenMissingValuesInLine() {
        String filePath = new File(RESOURCE_PATH + "missingvaluesinline.csv").getAbsolutePath();
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies(filePath));
        assertEquals("There are not enough values in the line. (Required at least 4 columns)", exception.getMessage());
    }

    @Test
    void testReadMoviesFailsWhenMissingProducer() {
        String filePath = new File(RESOURCE_PATH + "missingproducer.csv").getAbsolutePath();
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies(filePath));
        assertEquals("There are movies where producers have not being informed.", exception.getMessage());
    }

    @Test
    void testReadMoviesFailsWhenInvalidYear() {
        String filePath = new File(RESOURCE_PATH + "invalidyear.csv").getAbsolutePath();
        FileException exception = assertThrows(FileException.class, () -> ReadFileService.readMovies(filePath));
        assertEquals("Year '1980s' is invalid.", exception.getMessage());
    }
}