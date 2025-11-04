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
import com.google.api.services.youtube.model.VideoListResponse;

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
	public YouTube youtubeBuilder() {
		JsonFactory factory = JacksonFactory.getDefaultInstance();
		HttpTransport transport = null;
		try {
			transport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.getMessage();
		}
		finally {
			YouTube youtube = new YouTube.Builder(transport, factory, request -> {}).setApplicationName("Youtube Sentiment Analyser").build();
			return youtube;
		}
	}
		
	@SuppressWarnings("finally")
	public List<Comment> fetchComments(String videoID) {
		List<Comment> comments = new ArrayList<Comment>();
		try {
			YouTube youtube = youtubeBuilder();
			YouTube.CommentThreads.List request = youtube.commentThreads().list("snippet")
					.setVideoId(videoID);
			CommentThreadListResponse response = request.setKey(this.apiKey)
					.setTextFormat("plaintext")
					.setOrder("relevance")
					.setPrettyPrint(true)
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
	
	@SuppressWarnings("finally")
	public String fetchTitle(String videoID) {
		String title = "";
		try {
			YouTube youtube = youtubeBuilder();
			YouTube.Videos.List requestVideo = youtube.videos()
					.list("snippet")
					.setId(videoID)
					.setKey(this.apiKey);
			VideoListResponse responseVideo = requestVideo.execute();
			if (responseVideo.getItems().isEmpty()) {
		        return "Unknown Title";
		    }
		    title = responseVideo.getItems().get(0).getSnippet().getTitle();	
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			return title;
		}
			
	}
}
