package com.example.demo.Respository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Director;

import java.util.Optional;

@Repository
public interface DirectorRepo extends JpaRepository<Director,Integer> {

    @Transactional
    @Query(value = "SELECT * FROM directors", nativeQuery = true)
    Page<Director> getAllDirectors(Pageable pageable);

    @Transactional
    @Query(value = "SELECT * FROM directors WHERE name LIKE concat('%', :keyword, '%')", nativeQuery = true)
    Page<Director> searchDirectorsByName(
            @Param("keyword") String keyword,
            Pageable pageable);

    Optional<Director> findByName(String name);
}
