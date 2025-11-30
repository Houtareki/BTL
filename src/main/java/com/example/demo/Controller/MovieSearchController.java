
// src/main/java/com/example/demo/Controller/MovieSearchController.java
package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Movie;
import com.example.demo.Model.RequestModel.MovieSearchRequestDTO;
import com.example.demo.Model.ResponseModel.MovieSearchResponseDTO;
import com.example.demo.Service.IMovieSearchService;

/**
 * REST Controller URL: /movies/api/search
 */
@RestController
@RequestMapping("/movies/api")
@CrossOrigin(origins = "*")
public class MovieSearchController {

	private final IMovieSearchService searchService;

	@Autowired
	public MovieSearchController(IMovieSearchService searchService) {
		this.searchService = searchService;
	}

	@GetMapping("/search")
	public ResponseEntity<MovieSearchResponseDTO> searchMovies(@ModelAttribute MovieSearchRequestDTO req) {
		try {
			Page<Movie> result = searchService.searchMovies(req.getKeyword(), req.getGenreId(), req.getCountry(),
					req.getReleaseYear(), req.getSortBy(), req.getPage(), req.getSize());

			return ResponseEntity.ok(MovieSearchResponseDTO.of(result, req.getSortBy()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}
