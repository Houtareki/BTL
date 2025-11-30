package com.example.demo.Entity;

import com.example.demo.Model.ComposeIdModel.MovieDirectorId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(MovieDirectorId.class)
@Table(name = "movie_directors")
public class MovieDirector {

    @Id
    @Column(name = "movie_id")
    private Integer movieId;

    @Id
    @Column(name = "director_id")
    private Integer directorId;

    public MovieDirector() {
    }

    public MovieDirector(Integer movieId, Integer directorId) {
        this.movieId = movieId;
        this.directorId = directorId;
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
