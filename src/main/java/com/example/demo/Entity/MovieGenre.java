package com.example.demo.Entity;

import com.example.demo.Model.ComposeIdModel.MovieGenreId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(MovieGenreId.class)
@Table(name = "movie_genres")
public class MovieGenre {

    @Id
    @Column(name = "movie_id")
    private Integer movieId;

    @Id
    @Column(name = "genre_id")
    private Integer genreId;

    public MovieGenre() {
    }

    public MovieGenre(Integer movieId, Integer genreId) {
        this.movieId = movieId;
        this.genreId = genreId;
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
