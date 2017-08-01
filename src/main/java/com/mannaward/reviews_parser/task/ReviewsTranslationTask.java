package com.mannaward.reviews_parser.task;


import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.api.TranslatorAPI;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ReviewsTranslationTask extends Task {

    private int MAX_BLOCK_SIZE = 1000;
    private TranslatorAPI translatorAPI = TranslatorAPI.getInstance();


    public ReviewsTranslationTask(Set<Review> reviews) {
        super(reviews);
    }


    /**
     * What we can do here to optimize requests amount sent to Google Translate API - is to group few small reviews
     * into bigger one to reach 1000 symbols restriction.
     */
    @Override
    public void run() {
        Set<String> texts = new HashSet<>();
        getReviews().parallelStream().forEach(review -> texts.add(addIdToText(review)));
        Set<String> translatedReviews = texts.stream().map(str -> {
            try {
                return translatorAPI.translate(str);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());
        translatedReviews.size();
    }

    private String removeGarbageFromText(String str){
        return str.replaceAll("<\\s*br\\s>","")
                .replaceAll("</\\s*br\\s>","")
                .replaceAll("<\\s*br\\s/>","")
                .replaceAll("\"","")
                .replaceAll("\\{}","");
    }

    private String addIdToText(Review review) {
        return "{" + review.getId() + " " + removeGarbageFromText(review.getText()) + "}";
    }
}
