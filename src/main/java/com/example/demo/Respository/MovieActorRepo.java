package com.example.demo.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Actor;
import com.example.demo.Entity.MovieActor;
import com.example.demo.Model.ComposeIdModel.MovieActorId;

import jakarta.transaction.Transactional;

@Repository
public interface MovieActorRepo extends JpaRepository<MovieActor, MovieActorId>{
    @Transactional
    @Query(value = "select a.* from movie_actors mc left join actors a on mc.actor_id = a.actor_id where mc.movie_id = :movieId;", nativeQuery = true)
    List<Actor> findActorsByMovieId(@Param(value = "movieId") int movieId);

    void deleteByMovieId(int movieId);
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM movie_actors WHERE movie_id = :movieId", nativeQuery = true)
    void deleteAllByMovieId(@Param("movieId") int movieId);
}
