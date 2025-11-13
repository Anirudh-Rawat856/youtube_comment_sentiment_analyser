# Sentiment Analysis of YouTube Comments

A full-stack application that uses a sentiment analysis model and the YouTube Data API to analyse the **sentiment of comments on a YouTube Video**.  
Developed utilising **Spring Boot**, **PostgreSQL**, **Chart.js**, and **Docker**.

---

## Characteristics

- Retrieves comments by ID from any YouTube video
- Analyses sentiment (positive, neutral, and negative) and shows a pie chart visualisation in real time.
- All comments and analysis are saved to PostgreSQL. The dashboard is a straightforward HTML + Chart interface.J.S.

---

## Tech Stack

| Component | Technology |
|------------|-------------|
| Backend | Spring Boot (Java) |
| Database | PostgreSQL |
| Frontend | HTML, Chart.js |
| Deployment | Docker Compose |
| API | YouTube Data API v3 |

---

## ⚙️ Setup Instructions

1) Clone the Repository

2️) Create a .env File
Add your secrets here:

YOUTUBE_API_KEY=YOUR_API_KEY

POSTGRES_PASSWORD=YOUR_DB_PASSWORD

3️) Start the Application

docker compose up --build

Your app will be live at
http://localhost:8080/dashboard.html

Example - Enter a YouTube Video ID (e.g. kCc8FmEb1nY) in the dashboard.

You’ll see:

A pie chart with the heading displaying the title of the video and the contents showing sentiment breakdown.

A table of five sample comments with their total likes and sentiment tags.

Environment Variables

YOUTUBE_API_KEY	API - key from Google Cloud

POSTGRES_PASSWORD	- PostgreSQL database password

SPRING_DATASOURCE_URL	(optional) - custom DB URL

API Endpoints:
Method	Endpoint	Description

GET	/api/sentiment/{videoId} - Analyze comments for a video

GET	/dashboard.html	- Display the analysis dashboard
AWS EC2 / DigitalOcean via Docker Compose

