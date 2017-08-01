package com.mannaward.reviews_parser.task;


import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.task.Task;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class GroupByProductIdCountTask extends Task {

    public GroupByProductIdCountTask(Set<Review> reviews) {
        super(reviews);
    }

    @Override
    public void run() {
        long start = Instant.now().toEpochMilli();
        ConcurrentHashMap<String, LongAdder> reviewsByProductCounterMap = new ConcurrentHashMap<>();
        getReviews().parallelStream()
                .map(Review::getProductId)
                .parallel()
                .forEach(profileName -> {
                    if (!reviewsByProductCounterMap.containsKey(profileName))
                        reviewsByProductCounterMap.put(profileName, new LongAdder());
                    reviewsByProductCounterMap.get(profileName).increment();
                });

        LinkedHashMap result = getOrderedResult(reviewsByProductCounterMap, 1000);
        printResult(result);

        long end = Instant.now().toEpochMilli();
        System.out.println(String.format("\tCompleted in %d milliseconds", (end - start)));
    }

}
