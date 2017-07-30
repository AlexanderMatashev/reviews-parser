package com.mannaward.reviews_parser.service.impl;

import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.service.Task;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class GroupByReviewsCountTask extends Task {

    public GroupByReviewsCountTask(Set<Review> reviews) {
        super(reviews);
    }

    @Override
    public void doTask() {
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

        LinkedHashMap result =
                reviewsByProfileCounterMap
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, LongAdder>comparingByValue(getComparator()).reversed())
                        .limit(1000)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new));

        long end = Instant.now().toEpochMilli();
        System.out.println(String.format("\tCompleted in %d milliseconds", (end - start)));
    }

}
