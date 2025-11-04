package com.anirudh.youtube_comment_sentiment_analyser.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name="videos")
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;
	
	@Column(name = "video_id", unique = true, nullable = false)
	private String videoId;
	
	@Column(name = "title", unique = false)
	private String title;
	
	private Integer totalComments;
    private Integer positiveCount;
    private Integer neutralCount;
    private Integer negativeCount;
    
    @Column(name = "last_analysed")
    private LocalDateTime lastAnalysed;
	
    public Video(String videoId, String title, Integer totalComments, Integer positiveCount, Integer neutralCount, Integer negativeCount, LocalDateTime lastAnalysed) {
    	this.videoId = videoId;
    	this.title = title;
    	this.totalComments = totalComments;
    	this.positiveCount = positiveCount;
    	this.neutralCount = neutralCount;
    	this.negativeCount = negativeCount;
    	this.lastAnalysed = lastAnalysed;
    }

	public Video() {
		
	}

	public Integer getPositiveCount() {
		return positiveCount;
	}

	public void setPositiveCount(Integer positiveCount) {
		this.positiveCount = positiveCount;
	}

	public Integer getTotalComments() {
		return totalComments;
	}

	public void setTotalComments(Integer totalComments) {
		this.totalComments = totalComments;
	}

	public Integer getNeutralCount() {
		return neutralCount;
	}

	public void setNeutralCount(Integer neutralCount) {
		this.neutralCount = neutralCount;
	}

	public Integer getNegativeCount() {
		return negativeCount;
	}

	public void setNegativeCount(Integer negativeCount) {
		this.negativeCount = negativeCount;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public void setLastAnalysed(LocalDateTime now) {
		this.lastAnalysed = now;
		
	}

	public String getVideoId() {
		return this.videoId;
	}
}
