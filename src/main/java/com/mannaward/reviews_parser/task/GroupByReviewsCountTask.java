package com.mannaward.reviews_parser.task;

import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.task.Task;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class GroupByReviewsCountTask extends Task {

    public GroupByReviewsCountTask(Set<Review> reviews) {
        super(reviews);
    }

    @Override
    public void run() {
        long start = Instant.now().toEpochMilli();
        ConcurrentHashMap<String, LongAdder> reviewsByProfileCounterMap = new ConcurrentHashMap<>();
        getReviews().parallelStream()
                .map(Review::getProfileName)
                .parallel()
                .forEach(profileName -> {
                    if (!reviewsByProfileCounterMap.containsKey(profileName))
                        reviewsByProfileCounterMap.put(profileName, new LongAdder());
                    reviewsByProfileCounterMap.get(profileName).increment();
                });

        LinkedHashMap result = getOrderedResult(reviewsByProfileCounterMap, 1000);
        printResult(result);
        long end = Instant.now().toEpochMilli();
        System.out.println(String.format("\tCompleted in %d milliseconds", (end - start)));
    }

}
