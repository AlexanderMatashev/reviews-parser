package com.mannaward.reviews_parser.core;

import com.mannaward.reviews_parser.model.Review;
import com.mannaward.reviews_parser.service.FileReader;
import com.mannaward.reviews_parser.task.GroupByProductIdCountTask;
import com.mannaward.reviews_parser.task.GroupByReviewsCountTask;
import com.mannaward.reviews_parser.task.ReviewsTranslationTask;
import com.mannaward.reviews_parser.task.WordsUsageStatisticsTask;
import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {
        CommandLine cmd = getCmd(args);
        String strPath = cmd.getOptionValue("source");
        Path path = Paths.get(strPath); // TODO handle invalid path
        Set<Review> reviews = FileReader.readFile(path);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(new WordsUsageStatisticsTask(reviews));
        executor.execute(new GroupByProductIdCountTask(reviews));
        executor.execute(new GroupByReviewsCountTask(reviews));

        Boolean translate = Boolean.valueOf(cmd.getOptionValue("translate"));
        if(translate) {
            executor.execute(new ReviewsTranslationTask(reviews));
        }
    }

    private static Options getOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Show help");
        options.addOption(Option.builder().longOpt("source").required().argName("source").hasArg().desc("Path to source file").type(String.class).build());
        options.addOption(Option.builder().longOpt("translate").argName("translate").hasArg().desc("Translate reviews").type(Boolean.class).build());
        return options;
    }

    private static CommandLine getCmd(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(getOptions() , args);
        if(commandLine.hasOption("h")){
            help();
        }
        return commandLine;
    }

    private static void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Main", getOptions());
        System.exit(0);
    }
}
