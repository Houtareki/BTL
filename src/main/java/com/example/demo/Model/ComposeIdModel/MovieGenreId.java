package com.example.demo.Model.ComposeIdModel;

import java.io.Serializable;
import java.util.Objects;

public class MovieGenreId implements Serializable {

    private Integer movieId;
    private Integer genreId;

    public MovieGenreId() {}

    public MovieGenreId(Integer movieId, Integer genreId) {
        this.movieId = movieId;
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof MovieGenreId)) return false;
        MovieGenreId other = (MovieGenreId) o;
        return Objects.equals(this.movieId, other.getMovieId()) && Objects.equals(this.genreId, other.getGenreId());
    }

    @Override 
    public int hashCode() {
        return Objects.hash(this.movieId, this.genreId);
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    
}
