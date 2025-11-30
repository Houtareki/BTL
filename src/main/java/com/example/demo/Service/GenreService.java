package com.example.demo.Service;

import com.example.demo.Entity.Genre;
import com.example.demo.Respository.GenreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenreService {

    @Autowired
    private GenreRepo genreRepo;

    private static final int PAGE_SIZE = 10;

    public Page<Genre> getAllGenre(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "genre_id"));
        return genreRepo.getAllGenre(pageable);
    }

    public Page<Genre> searchGenre(String keyword, int pageNo) {
        Pageable pageable =  PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "genre_id"));
        return genreRepo.searchGenresByName(keyword, pageable);
    }

    public Optional<Genre> getGenreById(int id) {
        return genreRepo.findById(id);
    }

    public Genre addGenre(Genre genre) {
        if (genreRepo.findByName(genre.getName()).isPresent()) {
            return null;
        }

        return genreRepo.save(genre);
    }

    public Genre updateGenre(int id, Genre genre) {
        Optional<Genre> existingGenre = genreRepo.findByName(genre.getName());
        if (existingGenre.isEmpty()) {
            return null;
        }

        Optional<Genre> duplicateName = genreRepo.findByName(genre.getName());
        if (duplicateName.isPresent() && duplicateName.get().getGenreId() != id) {
            return null;
        }

        Genre updatedGenre = existingGenre.get();
        updatedGenre.setName(genre.getName());
        return genreRepo.save(updatedGenre);
    }

    public boolean deleteGenre(int id) {
        if (!genreRepo.existsById(id)) {
            return false;
        }

        try {
            genreRepo.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
