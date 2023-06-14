package com.texo.challenge.utils;

import com.texo.challenge.exceptions.FileException;
import com.texo.challenge.exceptions.FileMissingColumnsException;
import com.texo.challenge.exceptions.FileMissingValuesException;
import com.texo.challenge.models.MovieDto;
import lombok.experimental.UtilityClass;
import org.aspectj.util.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ReadFileService {

    private final List<String> DEFAULT_HEADER = List.of("year", "title", "studios", "producers", "winner");

    public Set<MovieDto> readMovies(String filePath) {
        checkFilePathValid(filePath);

        File file = new File(filePath);
        checkFileExists(file);

        List<String> lines = FileUtil.readAsLines(file);
        checkFileContent(lines);

        return parseLines(lines);
    }

    private static Set<MovieDto> parseLines(List<String> lines) {
        return lines.stream().map(line -> line.split(";", 5)).map(ReadFileService::parseToMovieDto).collect(Collectors.toSet());
    }

    private static void checkFilePathValid(String filePath) {
        if (filePath.toLowerCase().startsWith("http") || !filePath.toLowerCase().endsWith(".csv")) {
            throw new FileException("Invalid path. This should be a local CSV file path.");
        }
    }

    private void checkFileExists(File file) {
        if (!file.exists()) {
            throw new FileException("File does not exist. (Path: \"%s\")", file.getAbsolutePath());
        }
    }

    private static void checkFileContent(List<String> lines) {
        checkLines(lines, "File has no header or content.");

        String[] header = lines.get(0).trim().split(";");
        if (DEFAULT_HEADER.size() != header.length) {
            throw new FileException("File has no header.");
        }
        for (int i = 0; i < DEFAULT_HEADER.size(); i++) {
            if (!DEFAULT_HEADER.get(i).equalsIgnoreCase(header[i])) {
                throw new FileException("File is missing column on header: %s.", DEFAULT_HEADER.get(i));
            }
        }

        lines.remove(0);
        checkLines(lines, "File has only the header.");
    }

    private static void checkLines(List<String> lines, String message) {
        if (lines.isEmpty()) {
            throw new FileException(message);
        }
    }

    private MovieDto parseToMovieDto(String[] columns) throws FileMissingColumnsException, FileMissingValuesException {
        if (columns.length != 5) {
            throw new FileMissingColumnsException("There are not enough values in the line. (Required at least 4 columns)");
        }
        int year;
        try {
            year = Integer.parseInt(columns[0]);
        } catch (NumberFormatException e) {
            throw new FileMissingValuesException("Year '%s' is invalid.", columns[0]);
        }
        return new MovieDto(year, columns[1], columns[2], getProducerNames(columns[3]), "yes".equalsIgnoreCase(columns[4]));
    }

    private Set<String> getProducerNames(String producerColumn) throws FileMissingValuesException {
        Set<String> producers = new HashSet<>();
        if (producerColumn.isBlank()) {
            throw new FileMissingValuesException("There are movies where producers have not being informed.");
        }
        String[] names = producerColumn.trim().split(", ");
        int last = names.length - 1;
        if (last > 0) {
            producers.addAll(Arrays.asList(names).subList(0, last));
        }
        names = names[last].trim().split(" and ");
        producers.add(names[0]);
        if (names.length > 1) {
            producers.add(names[1]);
        }
        return producers;
    }
}

