package com.example.demo.Service;

import org.springframework.data.domain.Page;

import com.example.demo.Entity.Movie;

/**
 * Service Interface - ABSTRACTION
 */
public interface IMovieSearchService {

	Page<Movie> searchMovies(String keyword, Integer genreId, String country, Integer releaseYear, String sortBy,
			int page, int size);
}