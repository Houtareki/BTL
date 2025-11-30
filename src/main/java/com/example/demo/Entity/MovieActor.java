package com.example.demo.Entity;

import com.example.demo.Model.ComposeIdModel.MovieActorId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(MovieActorId.class)
@Table(name = "movie_actors")
public class MovieActor {
    
    @Id
    @Column(name = "movie_id")
    private Integer movieId;

    @Id
    @Column(name = "actor_id")
    private Integer actorId;

    public MovieActor() {
    }

    public MovieActor(Integer movieId, Integer actorId) {
        this.movieId = movieId;
        this.actorId = actorId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    
}
