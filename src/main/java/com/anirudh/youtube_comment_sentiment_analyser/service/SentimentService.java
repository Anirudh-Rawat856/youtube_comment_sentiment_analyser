package com.anirudh.youtube_comment_sentiment_analyser.service;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SentimentService {
    private final StanfordCoreNLP pipeline;

    public SentimentService() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public String analyseSentiment(String text) {
        if (text == null || text.isEmpty()) return "NEUTRAL";

        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        int totalScore = 0;
        int sentences = 0;

        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            switch (sentiment.toLowerCase()) {
                case "positive": totalScore += 1; break;
                case "very positive": totalScore += 2; break;
                case "negative": totalScore -= 1; break;
                case "very negative": totalScore -= 2; break;
            }
            sentences++;
        }

        double avg = sentences > 0 ? (double) totalScore / sentences : 0;
        if (avg > 0.5) return "POSITIVE";
        if (avg < -0.5) return "NEGATIVE";
        return "NEUTRAL";
    }
}
