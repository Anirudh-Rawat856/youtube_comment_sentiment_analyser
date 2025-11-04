-- ============================================
-- YouTube Comment Sentiment Analyser - Database Schema
-- ============================================

-- Drop existing tables (optional, for dev resets)
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS videos;

-- ============================================
-- Table: videos
-- Stores basic video information and aggregated sentiment stats
-- ============================================
CREATE TABLE videos (
    id SERIAL PRIMARY KEY,
    video_id VARCHAR(50) UNIQUE NOT NULL,          -- YouTube video ID
    title VARCHAR(255),
    total_comments INT DEFAULT 0,                  
    positive_count INT DEFAULT 0,
    neutral_count INT DEFAULT 0,
    negative_count INT DEFAULT 0,
    last_analysed TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Table: comments
-- Stores individual comments with sentiment classification
-- ============================================
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    video_id VARCHAR(50) NOT NULL,                 -- YouTube video ID (FK-like)
    author VARCHAR(255),
    comment_text TEXT NOT NULL,
    sentiment VARCHAR(10) CHECK (sentiment IN ('POSITIVE', 'NEGATIVE', 'NEUTRAL')),
    likes INT DEFAULT 0,
    published_at TIMESTAMP,
    analysed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_video FOREIGN KEY (video_id)
        REFERENCES videos(video_id)
        ON DELETE CASCADE
);

-- ============================================
-- Optional: Indexes for performance
-- ============================================
CREATE INDEX idx_video_id ON comments(video_id);
CREATE INDEX idx_sentiment ON comments(sentiment);
CREATE INDEX idx_published_at ON comments(published_at);
