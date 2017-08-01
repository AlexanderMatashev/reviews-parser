package com.mannaward.reviews_parser.task;

import com.mannaward.reviews_parser.model.Review;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public abstract class Task implements Runnable {

    private Set<Review> reviews;

    public Task(Set<Review> reviews) {
        this.reviews = reviews;
    }

    protected Set<Review> getReviews() {
        return reviews;
    }

    private Comparator<LongAdder> getComparator() {
        return (o1, o2) -> Long.valueOf(o1.longValue()).compareTo(o2.longValue());
    }

    protected LinkedHashMap getOrderedResult(ConcurrentHashMap<String, LongAdder> mixedMap, int count) {
        return
                mixedMap
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, LongAdder>comparingByValue(getComparator()).reversed())
                        .limit(count)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new));
    }

    protected void printResult(LinkedHashMap result){
        result.entrySet().forEach(entry -> System.out.println("\t"+ ((Map.Entry)entry).getKey() + "    " +((Map.Entry)entry).getValue()));
    }

}
