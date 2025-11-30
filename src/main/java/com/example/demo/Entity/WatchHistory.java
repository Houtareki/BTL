package com.example.demo.Entity;

import com.example.demo.Model.ComposeIdModel.WatchHistoryId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(WatchHistoryId.class)
@Table(name = "watch_history")
public class WatchHistory {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "movie_id")
    private Integer movieId;

    public WatchHistory() {
    }

    public WatchHistory(Integer userId, Integer movieId) {
        this.userId = userId;
        this.movieId = movieId;
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
