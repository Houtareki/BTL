package com.example.demo.Respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Genre;

import jakarta.transaction.Transactional;

@Repository
public interface GenreRepo extends JpaRepository<Genre, Integer>{

    @Transactional
    @Query(value = "select * from genres", nativeQuery = true)
    List<Genre> getAllGenres();

    @Transactional
    @Query(value = "SELECT * FROM genres WHERE name like concat('%', :keyword, '%')", nativeQuery = true)
    Page<Genre> searchGenresByName(
            @Param("keyword") String keyword,
            Pageable pageable);

    Optional<Genre> findByName(String name);

    @Transactional
    @Query(value = "select * from genres", nativeQuery = true)
    Page<Genre> getAllGenre(Pageable pageable);
}
