package com.example.demo.Entity;

import java.time.LocalDate;

import com.example.demo.Model.ComposeIdModel.SaveId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(SaveId.class)
@Table(name = "saves")
public class UserSavedMovie {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "movie_id")
    private Integer movieId;

    @Column(name = "")
    private LocalDate addedAt;

    public UserSavedMovie() {
    }

    public UserSavedMovie(Integer userId, Integer movieId, LocalDate addedAt) {
        this.userId = userId;
        this.movieId = movieId;
        this.addedAt = addedAt;
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

    
}
