package com.example.demo.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Movie;
import com.example.demo.Entity.WatchHistory;
import com.example.demo.Model.ComposeIdModel.WatchHistoryId;

import jakarta.transaction.Transactional;

@Repository
public interface WatchHistoryRepo extends JpaRepository<WatchHistory, WatchHistoryId>{

    @Transactional
    @Query(value = "select m.* from watch_history wh left join movies m on wh.movie_id = m.movie_id where wh.user_id = :userId", nativeQuery = true)
    List<Movie> findWatchedMovieByUserId(@Param(value = "userId") int userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO watch_history (user_id, movie_id) VALUES (:userId, :movieId) ON DUPLICATE KEY UPDATE user_id = :userId", nativeQuery = true)
    void addToWatchHistory(@Param("userId") int userId, @Param("movieId") int movieId);

    @Transactional
    @Query(value = "SELECT COUNT(*) FROM watch_history WHERE user_id = :userId AND movie_id = :movieId", nativeQuery = true)
    int checkIfWatched(@Param("userId") int userId, @Param("movieId") int movieId);
}
