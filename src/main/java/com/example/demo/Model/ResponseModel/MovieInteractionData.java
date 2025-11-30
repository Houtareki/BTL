package com.example.demo.Model.ResponseModel;

public class MovieInteractionData {
    private int userId;
    private int movieId;

    private int totalLikes;
    private int totalSaves;
    private int totalView;

    private boolean isUserLikedThis;
    private boolean isUserSavedThis;

    public MovieInteractionData(int userId, int movieId, int totalLikes, int totalSaves, int totalView,
            boolean isUserLikedThis, boolean isUserSavedThis) {
        this.userId = userId;
        this.movieId = movieId;
        this.totalLikes = totalLikes;
        this.totalSaves = totalSaves;
        this.totalView = totalView;
        this.isUserLikedThis = isUserLikedThis;
        this.isUserSavedThis = isUserSavedThis;
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

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalSaves() {
        return totalSaves;
    }

    public void setTotalSaves(int totalSaves) {
        this.totalSaves = totalSaves;
    }

    public int getTotalView() {
        return totalView;
    }

    public void setTotalView(int totalView) {
        this.totalView = totalView;
    }

    public boolean isUserLikedThis() {
        return isUserLikedThis;
    }

    public void setUserLikedThis(boolean isUserLikedThis) {
        this.isUserLikedThis = isUserLikedThis;
    }

    public boolean isUserSavedThis() {
        return isUserSavedThis;
    }

    public void setUserSavedThis(boolean isUserSavedThis) {
        this.isUserSavedThis = isUserSavedThis;
    }

}
