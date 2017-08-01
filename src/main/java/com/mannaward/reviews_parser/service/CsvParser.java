package com.mannaward.reviews_parser.service;

import com.mannaward.reviews_parser.model.Review;

public class CsvParser {

    public static Review parse(String csvRow, String delimiter) { // TODO improve mapping to remove dependency on fields order
        String[] fieldsArr = csvRow.split(delimiter);
        Review review = new Review();
        review.setId(fieldsArr[0]);
        review.setProductId(fieldsArr[1]);
        review.setUserId(fieldsArr[2]);
        review.setProfileName(fieldsArr[3]);
        review.setHelpfulnessNumerator(fieldsArr[4]);
        review.setHelpfulnessDenominator(fieldsArr[5]);
        review.setScore(fieldsArr[6]);
        review.setTime(fieldsArr[7]);
        review.setSummary(fieldsArr[8]);
        review.setText(fieldsArr[9]);
        return review;
    }

}