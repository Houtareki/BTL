package com.example.demo.Respository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.UserSavedMovie;
import com.example.demo.Model.ComposeIdModel.SaveId;

import jakarta.transaction.Transactional;

@Repository
public interface SaveRepo extends JpaRepository<UserSavedMovie, SaveId>{

    @Transactional
    @Query(value = "select count(movie_id) from saves s left join users u on s.user_id = u.user_id where u.user_id = :userId and movie_id = :movieId", nativeQuery = true)
    int isMovieSavedByUser(@Param(value = "userId") int userId, @Param(value = "movieId") int movieId);

    @Transactional
    @Query(value = "select count(*) from saves s left join movies m on s.movie_id = m.movie_id where s.movie_id = :movieId", nativeQuery = true)
    int findTotalSavesByMovieId(@Param(value = "movieId") int movieId);

    @Transactional
    @Modifying
    @Query(value = "insert into saves (user_id, movie_id, added_at) values (:userId,:movieId,:addedAt)", nativeQuery = true)
    int addToRepo(@Param(value = "userId") int userId, @Param(value = "movieId") int movieId, @Param(value = "addedAt") LocalDateTime addedAt);

    @Transactional
    @Modifying
    @Query(value = "delete from saves where user_id=:userId and movie_id = :movieId", nativeQuery = true)
    int removeFromRepo(@Param(value = "userId") int userId, @Param(value = "movieId") int movieId);

    @Transactional
    @Query(value = "select m.* from saves s left join movies m on s.movie_id = m.movie_id where s.user_id = :userId", nativeQuery = true)
    List<Movie> findAllSavedMovieByUserId(@Param("userId") int userId);
}
