package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Movie;
import com.example.demo.Model.CommonModel.SortType;
import com.example.demo.Repository.Specification.MovieSpecifications;
import com.example.demo.Respository.MovieRepo;
import com.example.demo.Service.Strategy.MovieSortStrategy;
import com.example.demo.Service.Strategy.SortStrategyFactory;

/**
 * Service Implementation ORCHESTRATION - Kết hợp Strategy + Specification +
 * Repository
 */
@Service
@Transactional
public class MovieSearchServiceImpl implements IMovieSearchService {

	private final MovieRepo movieRepository;
	private final SortStrategyFactory sortFactory;

	@Autowired
	public MovieSearchServiceImpl(MovieRepo movieRepository, SortStrategyFactory sortFactory) {
		this.movieRepository = movieRepository;
		this.sortFactory = sortFactory;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Movie> searchMovies(String keyword, Integer genreId, String country, Integer releaseYear, String sortBy,
			int page, int size) {

		// STEP 1: Build Specification
		Specification<Movie> spec = MovieSpecifications.buildSpec(keyword, genreId, country, releaseYear);

		// STEP 2: Get Sort Strategy
		SortType sortType = SortType.fromString(sortBy);
		MovieSortStrategy strategy = sortFactory.getStrategy(sortType);

		// STEP 3: Create Pageable
		Pageable pageable = PageRequest.of(page, size, strategy.getSort());

		// STEP 4: Execute Query
		return movieRepository.findAll(spec, pageable);
	}
}