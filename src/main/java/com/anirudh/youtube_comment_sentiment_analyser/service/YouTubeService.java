package com.anirudh.youtube_comment_sentiment_analyser.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.anirudh.youtube_comment_sentiment_analyser.model.Comment;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;

@Service
public class YouTubeService {
	@Value("${youtube.api.key}")
	private final String apiKey;
	private SentimentService sentimentService;
	
	public YouTubeService(@Value("${youtube.api.key}") String apiKey) {
		this.apiKey = apiKey;
		this.sentimentService = new SentimentService();
	}
	
	@SuppressWarnings("finally")
	public List<Comment> fetchComments(String videoID) {
		List<Comment> comments = new ArrayList<Comment>();
		JsonFactory factory = JacksonFactory.getDefaultInstance();
		
		HttpTransport transport = null;
		int exceptions = 0;
		try {
			transport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException e) {
			System.out.println(e.getMessage());
			exceptions++;
			
		} catch (IOException e) {
			e.getMessage();
			exceptions++;
		}
		finally {
			if (exceptions > 0) {
				return comments;
			}
		}
		try {
			YouTube youtube = new YouTube.Builder(transport, factory, request -> {}).setApplicationName("Youtube Sentiment Analyser").build();
			YouTube.CommentThreads.List request = youtube.commentThreads().list("snippet")
					.setVideoId(videoID);
			CommentThreadListResponse response = request.setKey(this.apiKey)
					.setTextFormat("plaintext")
					.setOrder("relevance")
					.setMaxResults(50L)
					.execute();
			for (CommentThread thread : response.getItems()) {
                CommentSnippet snippet = thread.getSnippet().getTopLevelComment().getSnippet();
                Comment c = new Comment(videoID, 
                		snippet.getAuthorDisplayName(), 
                		snippet.getTextDisplay(), 
                		this.sentimentService.analyseSentiment(snippet.getTextDisplay()),
                	    snippet.getLikeCount() != null ? snippet.getLikeCount().intValue() : 0,
                		Instant.ofEpochMilli(snippet.getPublishedAt().getValue()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                comments.add(c);
            }
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			return comments;
		}
	}	
}
