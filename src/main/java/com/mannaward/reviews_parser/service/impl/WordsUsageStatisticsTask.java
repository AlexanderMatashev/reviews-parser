package com.mannaward.reviews_parser.service.impl;

import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.service.Task;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;


public class WordsUsageStatisticsTask extends Task {

    public static final String WORD_MATCH_PATTERN = "[a-zA-Z]{4,}"; // so the numbers, random characters, and short words will not be counted

    public WordsUsageStatisticsTask(Set<Review> reviews) {
        super(reviews);
    }

    @Override
    public void doTask() {
        wordCount(getReviews());
    }

    private void wordCount(Set<Review> reviews) {
        long start = Instant.now().toEpochMilli();
        ConcurrentHashMap<String, LongAdder> wordsCounterMap = new ConcurrentHashMap<>();
        reviews.parallelStream()
                .map(review -> review.getText().split("[\\s-:,()!?.]"))
                .flatMap(Arrays::stream)
                .parallel()
                .filter(w -> w.matches(WORD_MATCH_PATTERN))
                .map(String::toLowerCase)
                .forEach(word -> {
                    if (!wordsCounterMap.containsKey(word))
                        wordsCounterMap.put(word, new LongAdder());
                    wordsCounterMap.get(word).increment();
                });

        LinkedHashMap result =
                wordsCounterMap
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, LongAdder>comparingByValue(getComparator()).reversed())
                        .limit(1000)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new));

        long end = Instant.now().toEpochMilli();
        System.out.println(String.format("\tCompleted in %d milliseconds", (end - start)));
    }
}
