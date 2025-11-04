package com.anirudh.youtube_comment_sentiment_analyser.controller;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anirudh.youtube_comment_sentiment_analyser.model.Comment;
import com.anirudh.youtube_comment_sentiment_analyser.model.Video;
import com.anirudh.youtube_comment_sentiment_analyser.repository.CommentRepository;
import com.anirudh.youtube_comment_sentiment_analyser.repository.VideoRepository;
import com.anirudh.youtube_comment_sentiment_analyser.service.SentimentService;
import com.anirudh.youtube_comment_sentiment_analyser.service.YouTubeService;

@RestController
@RequestMapping("/api/sentiment")
public class SentimentController {

    private final YouTubeService youtubeService;
    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;

    @Autowired
    public SentimentController(YouTubeService youtubeService,
                               SentimentService sentimentService,
                               CommentRepository commentRepository,
                               VideoRepository videoRepository) {
        this.youtubeService = youtubeService;
        this.commentRepository = commentRepository;
        this.videoRepository = videoRepository;
    }

    @SuppressWarnings({ "unchecked"})
	@GetMapping("/{videoId}")
    public Map<String, Object> analyse(@PathVariable String videoId) {
        List<Comment> comments = this.youtubeService.fetchComments(videoId);
        String title = this.youtubeService.fetchTitle(videoId);
        long positive = 0, neutral = 0, negative = 0;
        Video video = this.videoRepository.findByVideoId(videoId).orElse(new Video());
        video.setVideoId(videoId);
        video.setTitle(title);
        this.videoRepository.save(video);
        this.videoRepository.flush();
        for (Comment comment : comments) {
            String sentiment = comment.getSentiment();
            comment.setAnalysedAt(LocalDateTime.now());
             if (comment.getVideoId().equals(null) || comment.getCommentText().equals(null)) {
            	 continue;
             }
            this.commentRepository.save(comment);
            switch (sentiment) {
                case "POSITIVE" -> positive++;
                case "NEGATIVE" -> negative++;
                default -> neutral++;
            }
        }
        
        video.setTotalComments(comments.size());
        video.setPositiveCount((int) positive);
        video.setNegativeCount((int) negative);
        video.setNeutralCount((int) neutral);
        video.setLastAnalysed(LocalDateTime.now());
        System.out.println(video.getVideoId());
        this.videoRepository.saveAndFlush(video);
        
        Map<String, Object> result = new HashMap<>();
        result.put("videoId", videoId);
        result.put("title", title);
        result.put("totalComments", comments.size());
        result.put("positive", positive);
        result.put("neutral", neutral);
        result.put("negative", negative);
        Random random = new Random();
        int start = random.nextInt(0, comments.size() > 5 ? comments.size() - 5 : comments.size());
        result.put("sample", comments.subList(start, Math.min(start + 5, comments.size())));
        try {
        	return result;
        } catch (Exception e) {
            e.printStackTrace();
            return (Map<String, Object>) ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
