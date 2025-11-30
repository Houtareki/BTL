package com.example.demo.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Episode;
import com.example.demo.Entity.Genre;
import com.example.demo.Entity.Movie;
import com.example.demo.Model.CommonModel.CommentForm;
import com.example.demo.Model.ResponseModel.CommentData;
import com.example.demo.Model.ResponseModel.MovieInteractionData;

@Service
public interface MovieService {

    List<Movie> getBannerMovies();

    Movie getMovieByMovieId(int movieId);

    List<Episode> findAllEpisodeByMovieId(int movieId);

    Movie findMovieById(int movieId);

    List<Movie> getShowingMovies(int userId);

    MovieInteractionData getMovieInteractionData(int userId, int movieId);

    List<Movie> getSuggestedMovies(int userId);

    List<Genre> getAllGenres(int userId);

    List<String> getAllCountries(int userId);

    List<Integer> getAllYears(int userId);

    List<CommentData> getAllCommentsByMovieId(int userId, int movieId);

    Comment addComment(CommentForm commentForm);

    Movie updateMovieViews(int userId, int movieId);

    List<Movie> getAllMoviesByGenreId(int userId, int genreId);

    List<Movie> getAllMoviesByCountry(int userId, String country);

    List<Movie> getLikedMoviesByUserId(int userId);

    List<Movie> getSavedMoviesByUserId(int userId);

    List<Movie> getWatchedMoviesByUserId(int userId);

    //----------------------------------------------------------------

    Page<Movie> getAllMovies(int pageNo);
    Page<Movie> searchMoviesByTitle(String title, int pageNo);

    Movie getMovieById(int movieId);
    Movie saveMovie(Movie movie);
    Movie updateMovie(int movieId, Movie movieDetails);
    void deleteMovie(int movieId);
}
