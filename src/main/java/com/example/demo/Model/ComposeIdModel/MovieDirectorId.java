package com.example.demo.Model.ComposeIdModel;

import java.io.Serializable;
import java.util.Objects;


public class MovieDirectorId implements Serializable{

    private Integer movieId;
    private Integer directorId;

    public MovieDirectorId() {
    }

    public MovieDirectorId(Integer movieId, Integer directorId) {
        this.movieId = movieId;
        this.directorId = directorId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof MovieActorId)) return false;
        MovieDirectorId other = (MovieDirectorId) o;
        return Objects.equals(this.movieId, other.getMovieId()) && Objects.equals(this.directorId, other.getDirectorId());
    }

    @Override 
    public int hashCode() {
        return Objects.hash(this.movieId, this.directorId);
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Integer directorId) {
        this.directorId = directorId;
    }

    
}
