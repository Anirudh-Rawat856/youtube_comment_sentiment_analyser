package com.anirudh.youtube_comment_sentiment_analyser.repository;

import com.anirudh.youtube_comment_sentiment_analyser.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVideoId(String videoId);
}
