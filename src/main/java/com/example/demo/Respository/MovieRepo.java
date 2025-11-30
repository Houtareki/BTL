package com.example.demo.Respository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Movie;

import jakarta.transaction.Transactional;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {

    Movie findByMovieId(int movieId);

    @Transactional
    @Query(value = "select distinct * from movies order by release_date desc limit 10", nativeQuery = true)
    List<Movie> getBannerMovies();

    @Transactional
    @Query(value = "select distinct * from movies where status='showing' limit 10", nativeQuery = true)
    List<Movie> getShowingMovies();

    @Transactional
    @Query(value = "select views from movies where movie_id = :movieId", nativeQuery = true)
    int findAllViewsByMovieId(@Param(value = "movieId") int movieId);

    @Transactional
    @Query(value = "select * from movies order by views limit 10", nativeQuery = true)
    List<Movie> getSuggestedMovies();

    @Transactional
    @Query(value = "select distinct country from movies", nativeQuery = true)
    List<String> getAllCountries();

    @Transactional
    @Query(value = "select m.* from movies m left join movie_genres mg on m.movie_id = mg.movie_id where mg.genre_id = :genreId", nativeQuery = true)
    List<Movie> getAllMoviesByGenre(@Param(value = "genreId") int genreId);

    @Transactional
    @Query(value = "select * from movies where country = :country", nativeQuery = true)
    List<Movie> getAllMoviesByCountry(@Param(value = "country") String country);

    @Transactional
    @Modifying
    @Query(value = "update movies set views = views+1 where movie_id = :movieId", nativeQuery = true)
    void updateMovieViews(@Param(value = "movieId") int movieId);

    @Transactional
    @Query(value = "SELECT DISTINCT CAST(release_date AS UNSIGNED) FROM movies WHERE release_date IS NOT NULL ORDER BY CAST(release_date AS UNSIGNED) DESC", nativeQuery = true)
    List<Integer> getAllYears();

    // Thêm vào src/main/java/com/example/demo/Respository/MovieRepo.java

    @Transactional
    @Query(value = """
    SELECT m.* FROM movies m
    WHERE (SELECT COUNT(*) FROM episodes e WHERE e.movie_id = m.movie_id) > 1
    ORDER BY m.views DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Movie> findTopSeriesMoviesByTotalViews(@Param("limit") int limit);

    @Transactional
    @Query(value = """
    SELECT m.* FROM movies m
    WHERE (SELECT COUNT(*) FROM episodes e WHERE e.movie_id = m.movie_id) <= 1
    ORDER BY m.views DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Movie> findTopFullMoviesByViews(@Param("limit") int limit);

    /**
     * Tìm phim theo title (case-insensitive)
     */
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Movie> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    /**
     * Tìm phim theo quốc gia
     */
    Page<Movie> findByCountry(String country, Pageable pageable);

    /**
     * Tìm phim theo năm phát hành
     */
    Page<Movie> findByReleaseYear(String releaseYear, Pageable pageable);

    /**
     * Lấy top N phim xem nhiều nhất
     */
    @Query("SELECT m FROM Movie m ORDER BY m.views DESC")
    Page<Movie> findTopViewedMovies(Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(views), 0) FROM movies", nativeQuery = true)
    Long sumTotalViews();

}
