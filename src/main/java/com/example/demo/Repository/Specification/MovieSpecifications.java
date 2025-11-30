package com.example.demo.Repository.Specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.Entity.Movie;
import com.example.demo.Entity.MovieGenre;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

/**
 * SPECIFICATION PATTERN Build dynamic queries
 */
public class MovieSpecifications {

	public static Specification<Movie> buildSpec(String keyword, Integer genreId, String country, Integer year) {

		Specification<Movie> spec = (hasKeyword(keyword)).and(hasGenre(genreId)).and(hasCountry(country))
				.and(hasYear(year));

		return spec;
	}

	private static Specification<Movie> hasKeyword(String keyword) {
		return (root, query, cb) -> {
			if (keyword == null || keyword.trim().isEmpty()) {
				return cb.conjunction();
			}

			String pattern = "%" + keyword.toLowerCase() + "%";
			return cb.or(cb.like(cb.lower(root.get("title")), pattern),
					cb.like(cb.lower(root.get("description")), pattern));
		};
	}

	private static Specification<Movie> hasGenre(Integer genreId) {
		return (root, query, cb) -> {
			if (genreId == null || genreId <= 0) {
				return cb.conjunction();
			}

			query.distinct(true);

			Subquery<Integer> subquery = query.subquery(Integer.class);
			Root<MovieGenre> mgRoot = subquery.from(MovieGenre.class);

			subquery.select(mgRoot.get("movieId")).where(cb.equal(mgRoot.get("genreId"), genreId));

			return cb.in(root.get("movieId")).value(subquery);
		};
	}

	private static Specification<Movie> hasCountry(String country) {
		return (root, query, cb) -> {
			if (country == null || country.trim().isEmpty()) {
				return cb.conjunction();
			}
			return cb.equal(cb.lower(root.get("country")), country.toLowerCase());
		};
	}

	private static Specification<Movie> hasYear(Integer year) {
		return (root, query, cb) -> {
			if (year == null) {
				return cb.conjunction();
			}
			return cb.equal(root.get("releaseYear"), String.valueOf(year));
		};
	}
}