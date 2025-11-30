package com.example.demo.Model.ResponseModel;

import java.time.LocalDateTime;

public class CommentData {
    private int commentId;
    private int userId;
    private int movieId;
    private String content;
    private LocalDateTime createdAt;
    private String username;

    public CommentData(int commentId, int userId, int movieId, String content, LocalDateTime createdAt,
            String username) {
        this.commentId = commentId;
        this.userId = userId;
        this.movieId = movieId;
        this.content = content;
        this.createdAt = createdAt;
        this.username = username;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
