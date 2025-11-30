package com.example.demo.Entity;

import java.time.LocalDate;

import com.example.demo.Model.ComposeIdModel.FavoriteId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(FavoriteId.class)
@Table(name = "favorites")
public class UserFavoritedMovie {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "movie_id")
    private Integer movieId;

    @Column(name = "added_at")
    private LocalDate addedAt;

    public UserFavoritedMovie() {
    }

    public UserFavoritedMovie(Integer userId, Integer movieId) {
        this.userId = userId;
        this.movieId = movieId;
        this.addedAt = LocalDate.now();
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

    public LocalDate getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDate addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "Favorite [userId=" + userId + ", movieId=" + movieId + ", addedAt=" + addedAt + "]";
    }

    
    
}
