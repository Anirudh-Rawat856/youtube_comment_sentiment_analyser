package com.anirudh.youtube_comment_sentiment_analyser.repository;

import com.anirudh.youtube_comment_sentiment_analyser.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByVideoId(String videoId);
}