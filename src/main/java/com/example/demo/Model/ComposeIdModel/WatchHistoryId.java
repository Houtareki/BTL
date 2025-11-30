package com.example.demo.Model.ComposeIdModel;

import java.io.Serializable;
import java.util.Objects;

public class WatchHistoryId implements Serializable {
    private Integer userId;
    private Integer movieId;

    public WatchHistoryId() {
    }

    public WatchHistoryId(Integer userId, Integer movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof WatchHistoryId)) return false;
        WatchHistoryId other = (WatchHistoryId) o;
        return Objects.equals(this.userId, other.getUserId()) && Objects.equals(this.movieId, other.getMovieId());
    }

    @Override 
    public int hashCode() {
        return Objects.hash(this.userId, this.movieId);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    
}
