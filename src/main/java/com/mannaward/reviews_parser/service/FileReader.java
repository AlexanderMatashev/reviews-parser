package com.mannaward.reviews_parser.service;

import com.mannaward.reviews_parser.model.Review;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class FileReader {

    private static final String DELIMITER = ",";

    public static Set<Review> readFile(Path filePath) throws Exception {
        long start = System.currentTimeMillis();
        Set<Review> reviews = Files.readAllLines(filePath)
                .parallelStream()
                .map(line -> CsvParser.parse(line, DELIMITER))
                .parallel()
                .collect(Collectors.toSet());
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("File read in " + duration + "ms");
        return reviews;
    }





}
