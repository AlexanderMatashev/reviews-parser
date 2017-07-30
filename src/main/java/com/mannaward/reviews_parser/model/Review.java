package com.mannaward.reviews_parser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Review {

    private String id;

    private String productId;

    private String userId;

    private String profileName;

    private String helpfulnessNumerator;

    private String helpfulnessDenominator;

    private String score;

    private String time;

    private String summary;

    private String text;
}
