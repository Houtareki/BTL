package com.example.demo.Model.ComposeIdModel;

import java.io.Serializable;
import java.util.Objects;

public class FavoriteId implements Serializable {
    private Integer userId;
    private Integer movieId;

    public FavoriteId() {
    }

    public FavoriteId(Integer userId, Integer movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(! (o instanceof FavoriteId) ) return false;
        FavoriteId other = (FavoriteId) o;
        return Objects.equals(userId, other.getUserId()) && Objects.equals(movieId, other.getMovieId());
    }

    @Override 
    public int hashCode() {
        return Objects.hash(userId, movieId);
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
