package com.example.demo.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Comment;

import jakarta.transaction.Transactional;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {

    @Transactional
    @Query(value = "select * from comments where movie_id = :movieId", nativeQuery = true)
    List<Comment> getAllCommentsByMovieId(@Param(value = "movieId") int movieId);


    @Transactional
    @Modifying
    @Query(value = "insert into comments (user_id, movie_id, content) values (:userId, :movieId, :content);", nativeQuery = true)
    void addComment(@Param(value = "userId") int userId, @Param(value = "movieId") int movieId, @Param(value = "content") String content);

    void deleteByMovieId(int movieId);
}
