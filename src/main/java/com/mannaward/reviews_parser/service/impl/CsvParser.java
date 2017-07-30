package com.mannaward.reviews_parser.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;

public class CsvParser {

    public void parse(String csvData) throws IOException {
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);
        for (CSVRecord csvRecord : parser) {

        }
    }



}
