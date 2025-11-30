package com.example.demo.Respository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Actor;

import java.util.Optional;

@Repository
public interface ActorRepo extends JpaRepository<Actor,Integer>{

    @Transactional
    @Query(value = "SELECT * FROM actors", nativeQuery = true)
    Page<Actor> getAllActors(Pageable pageable);

    @Transactional
    @Query(value = "SELECT * FROM actors WHERE name LIKE concat('%', :keyword, '%')", nativeQuery = true)
    Page<Actor> searchActorsByName(
            @Param("keyword") String keyword,
            Pageable pageable);

    Optional<Actor> findByName(String name);
}
