package com.example.demo.Respository;

import com.example.demo.Model.CommonModel.EpisodeAndMovieTitle;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Episode;

import java.util.List;
import java.util.Optional;


@Repository
public interface EpisodeRepo extends JpaRepository<Episode,Integer>{
    Optional<Episode> findByEpisodeId(int episodeId);
    List<Episode> findAllEpisodeByMovieId(int movieId);

    @Modifying
    @Transactional
    @Query("UPDATE Episode e SET e.movieId = NULL WHERE e.movieId = :movieId")
    void unlinkEpisodesFromMovie(@Param("movieId") int movieId);

    @Transactional
    @Query(value = "SELECT * FROM episodes WHERE movie_id = :movieId", nativeQuery = true)
    List<Episode> findByMovieId(@Param("movieId") int movieId);

    @Transactional
    @Query(value = "SELECT * FROM episodes WHERE name LIKE concat('%', :keyword, '%')", nativeQuery = true)
    Page<Episode> searchByName(
            @Param("keyword") String keyword,
            Pageable pageable);

    @Transactional
    @Query(value = "SELECT * FROM episodes WHERE movie_id = -1", nativeQuery = true)
    Page<Episode> findUnlinkedEpisodes(Pageable pageable);

    @Transactional
    @Query(value = "SELECT " +
            "ep.episode_id AS episodeId, " +
            "mo.title AS movieTitle, " +
            "ep.name AS episodeName, " +
            "ep.video_url AS videoUrl, " +
            "ep.movie_id AS movieId " +
            "FROM episodes ep " +
            "LEFT JOIN movies mo ON ep.movie_id = mo.movie_id " + // Dùng LEFT JOIN
            "WHERE ep.name LIKE concat('%', :keyword, '%')",

            countQuery = "SELECT count(ep.episode_id) " + // Thêm countQuery để phân trang
                    "FROM episodes ep " +
                    "LEFT JOIN movies mo ON ep.movie_id = mo.movie_id " +
                    "WHERE ep.name LIKE %:keyword%",
    nativeQuery = true)
    Page<EpisodeAndMovieTitle> searchByNameWithMovieTitle(
            @Param("keyword") String keyword,
            Pageable pageable);

    List<Episode> findByName(String keyword);

}
