package com.example.demo.Model.CommonModel;

public class CommentForm {
    private String content;
    private int userId;
    private int movieId;

    public CommentForm(String content, int userId, int movieId) {
        this.content = content;
        this.userId = userId;
        this.movieId = movieId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

}
