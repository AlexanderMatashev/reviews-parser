package com.mannaward.reviews_parser.core;

import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.service.FileReader;
import com.mannaward.reviews_parser.service.impl.GroupByProductIdCountTask;
import com.mannaward.reviews_parser.service.impl.GroupByReviewsCountTask;
import com.mannaward.reviews_parser.service.impl.WordsUsageStatisticsTask;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("/home/alex/Work/apps/reviews.csv");  // TODO read from arguments
        Set<Review> reviews = FileReader.readFile(path);
        new WordsUsageStatisticsTask(reviews).doTask();
        new GroupByProductIdCountTask(reviews).doTask();
        new GroupByReviewsCountTask(reviews).doTask();
    }

}
