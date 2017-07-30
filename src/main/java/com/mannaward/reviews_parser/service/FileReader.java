package com.mannaward.reviews_parser.service;

import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.service.impl.CsvParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class FileReader {

    public static final String DELIMETER = ",";

    public static Set<Review> readFile(Path filePath) throws Exception {
        long start = System.currentTimeMillis();
        Set<Review> reviews = Files.readAllLines(filePath)
                .parallelStream()
                .map(line -> CsvParser.parse(line, DELIMETER))
                .parallel()
                .collect(Collectors.toSet());
        long end = System.currentTimeMillis();
        long duration = end - start;
        return reviews;
    }





}
