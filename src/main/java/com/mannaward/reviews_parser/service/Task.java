package com.mannaward.reviews_parser.service;

import com.mannaward.reviews_parser.model.Review;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

public abstract class Task {

    private Set<Review> reviews;

    public Task(Set<Review> reviews) {
        this.reviews = reviews;
    }

    protected Set<Review> getReviews() {
        return reviews;
    }

    protected Comparator<LongAdder> getComparator(){
        return (o1, o2) -> Long.valueOf(o1.longValue()).compareTo(o2.longValue());
    }

    public abstract void doTask();

}
