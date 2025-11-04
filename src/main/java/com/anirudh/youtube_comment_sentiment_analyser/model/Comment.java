package com.anirudh.youtube_comment_sentiment_analyser.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id", nullable = false)
    private String videoId;

    private String author;

    @Column(name = "comment_text", nullable = false, columnDefinition = "TEXT")
    private String commentText;

    @Column(name = "sentiment", columnDefinition = "VARCHAR(10) CHECK (sentiment IN ('POSITIVE', 'NEGATIVE', 'NEUTRAL'))")
    private String sentiment;

    private Integer likes;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "analysed_at")
    private LocalDateTime analysedAt = LocalDateTime.now();

    // No-args constructor required by Hibernate
    public Comment() {}

    public Comment(String videoId, String author, String commentText, String sentiment, Integer likes, LocalDateTime publishedAt) {
        this.videoId = videoId;
        this.author = author;
        this.commentText = commentText;
        this.sentiment = sentiment;
        this.likes = likes != null ? likes : 0;
        this.publishedAt = publishedAt;
        this.analysedAt = LocalDateTime.now();
    }
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	public void setAnalysedAt(LocalDateTime now) {
		this.analysedAt = now;
		
	}
	public String getCommentText() {
		return this.commentText;
	}

	public String getVideoId() {
		return this.videoId;
	}
}
