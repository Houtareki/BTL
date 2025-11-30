package com.example.demo.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Genre;
import com.example.demo.Entity.MovieGenre;
import com.example.demo.Model.ComposeIdModel.MovieGenreId;

import jakarta.transaction.Transactional;

@Repository
public interface MovieGenreRepo extends JpaRepository<MovieGenre,MovieGenreId>{

    @Transactional
    @Query(value = "select g.* from movie_genres mg left join genres g on mg.genre_id = g.genre_id where mg.movie_id = :movieId", nativeQuery = true)
    List<Genre> findGenresByMovieId(@Param(value = "movieId") int movieId);

    void deleteByMovieId(int movieId);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM movie_genres WHERE movie_id = :movieId", nativeQuery = true)
    void deleteAllByMovieId(@Param("movieId") int movieId);
}
