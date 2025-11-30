package com.example.demo.Respository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.UserFavoritedMovie;
import com.example.demo.Model.ComposeIdModel.FavoriteId;
import com.example.demo.Entity.Movie;

import jakarta.transaction.Transactional;

@Repository
public interface FavoriteRepo extends JpaRepository<UserFavoritedMovie,FavoriteId> {

    @Transactional
    @Query(value = "select m.* from favorites f left join movies m on f.movie_id = m.movie_id where f.user_id = :userId", nativeQuery = true)
    List<Movie> findAllFavoriedMovieByUerId(@Param("userId") int userId);

    @Transactional
    @Query(value = "select count(movie_id) from favorites f left join users u on f.user_id = u.user_id where u.user_id = :userId and movie_id = :movieId", nativeQuery = true)
    int isMovieFavoritedByUser(@Param(value = "userId") int userId, @Param(value = "movieId") int movieId);

    @Transactional
    @Query(value = "select count(*) from favorites f left join movies m on f.movie_id = m.movie_id where f.movie_id = :movieId", nativeQuery = true)
    int findTotalLikesByMovieId(@Param(value = "movieId") int movieId);

    @Transactional
    @Modifying
    @Query(value = "insert into favorites (user_id, movie_id, added_at) values (:userId,:movieId,:addedAt)", nativeQuery = true)
    void addToRepo(@Param(value = "userId") int userId, @Param(value = "movieId") int movieId, @Param(value = "addedAt") LocalDateTime addedAt);

    @Transactional
    @Modifying
    @Query(value = "delete from favorites where user_id=:userId and movie_id = :movieId", nativeQuery = true)
    void removeFromRepo(@Param(value = "userId") int userId, @Param(value = "movieId") int movieId);
}
