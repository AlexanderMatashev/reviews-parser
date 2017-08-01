package com.mannaward.reviews_parser.api;

import java.util.concurrent.*;

/**
 * Mock for Google translation API
 */
public class TranslatorAPI {

    private static TranslatorAPI translatorAPI;
    private ExecutorService executor;

    private TranslatorAPI(){
        executor = Executors.newFixedThreadPool(100);
    }

    public static TranslatorAPI getInstance(){
        if(translatorAPI == null) {
            translatorAPI = new TranslatorAPI();
        }
        return translatorAPI;
    }


    public String translate(String text) throws ExecutionException, InterruptedException {
        Future<String> future = executor.submit(() -> {
            if(text.length() > 1000) {
                System.out.println("!!!! too long text::" + text); // to make sure that I didn't exceed the limit
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return text;
        });
        return future.get();
    }
}
