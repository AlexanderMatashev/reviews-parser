package com.mannaward.reviews_parser.task;

import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.task.Task;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;


public class WordsUsageStatisticsTask extends Task {

    public static final String WORD_MATCH_PATTERN = "[a-zA-Z]{4,}"; // so the numbers, random characters, and short words will not be counted

    public WordsUsageStatisticsTask(Set<Review> reviews) {
        super(reviews);
    }

    @Override
    public void run() {
        long start = Instant.now().toEpochMilli();
        ConcurrentHashMap<String, LongAdder> wordsCounterMap = new ConcurrentHashMap<>();
        getReviews().parallelStream()
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

        LinkedHashMap result = getOrderedResult(wordsCounterMap, 1000);
        printResult(result);

        long end = Instant.now().toEpochMilli();
        System.out.println(String.format("\tCompleted in %d milliseconds", (end - start)));
    }

}
