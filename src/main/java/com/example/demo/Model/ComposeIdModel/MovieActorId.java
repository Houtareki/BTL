package com.example.demo.Model.ComposeIdModel;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MovieActorId implements Serializable{

    private Integer movieId;
    private Integer actorId;

    public MovieActorId(Integer movieId, Integer actorId) {
        this.movieId = movieId;
        this.actorId = actorId;
    }

    public MovieActorId() {}

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof MovieActorId)) return false;
        MovieActorId other = (MovieActorId) o;
        return Objects.equals(this.movieId, other.getMovieId()) && Objects.equals(this.actorId, other.getActorId());
    }

    @Override 
    public int hashCode() {
        return Objects.hash(this.movieId, this.actorId);
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
