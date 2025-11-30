package com.example.demo.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Director;
import com.example.demo.Entity.MovieDirector;
import com.example.demo.Model.ComposeIdModel.MovieDirectorId;

import jakarta.transaction.Transactional;

@Repository
public interface MovieDirectorRepo extends JpaRepository<MovieDirector, MovieDirectorId> {
    @Transactional
    @Query(value = "select d.* from movie_directors md left join directors d on md.director_id = d.director_id where md.movie_id = :movieId; ", nativeQuery = true)
    List<Director> findDirectorsByMovieId(@Param(value = "movieId") int movieId);

    void deleteByMovieId(int movieId);
}
