package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.Respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Episode;
import com.example.demo.Entity.Genre;
import com.example.demo.Entity.Movie;
import com.example.demo.Entity.User;
import com.example.demo.Model.CommonModel.CommentForm;
import com.example.demo.Model.ResponseModel.CommentData;
import com.example.demo.Model.ResponseModel.MovieInteractionData;

@Service
public class MovieServiceIMP implements MovieService {


    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private EpisodeRepo episMovieRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FavoriteRepo favoriteRepo;

    @Autowired
    private SaveRepo saveRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private WatchHistoryRepo watchHistoryRepo;
    @Autowired
    private MovieActorRepo movieActorRepo;
    @Autowired
    private MovieDirectorRepo movieDirectorRepo;
    @Autowired
    private MovieGenreRepo movieGenreRepo;
    @Autowired
    private EpisodeRepo episodeRepo;




    public List<Movie> getBannerMovies() {
        return movieRepo.getBannerMovies();
    }

    public Movie getMovieByMovieId(int movieId) {
        return movieRepo.findById(movieId).orElse(null);
    }

    public List<Episode> findAllEpisodeByMovieId(int movieId) {
        return episMovieRepo.findAllEpisodeByMovieId(movieId);
    }

    public Movie findMovieById(int movieId) {
        return movieRepo.findById(movieId).orElse(null);
    }

    public MovieInteractionData getMovieInteractionData(int userId, int movieId) {
        MovieInteractionData res = new MovieInteractionData(userId, movieId, 0, 0, 0, false, false);
        User user = this.userRepo.findByUserId(userId);
        Movie movie = this.movieRepo.findByMovieId(movieId);
        if (user == null || movie == null) {
            return null;
        }

        res.setTotalLikes(favoriteRepo.findTotalLikesByMovieId(movieId));
        res.setTotalSaves(saveRepo.findTotalSavesByMovieId(movieId));
        res.setTotalView(movieRepo.findAllViewsByMovieId(movieId));
        res.setUserLikedThis(favoriteRepo.isMovieFavoritedByUser(userId, movieId) != 0);
        res.setUserSavedThis(saveRepo.isMovieSavedByUser(userId, movieId) != 0);
        return res;
    }

    public List<Movie> getShowingMovies(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if (user != null) {
            return this.movieRepo.getShowingMovies();
        }
        return null;
    }

    public List<Movie> getSuggestedMovies(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if (user != null) {
            return this.movieRepo.getSuggestedMovies();
        }
        return null;
    }

    public List<Genre> getAllGenres(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if (user != null) {
            return this.genreRepo.getAllGenres();
        }
        return null;
    }

    public List<String> getAllCountries(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if (user != null) {
            return this.movieRepo.getAllCountries();
        }
        return null;
    }

    public List<Integer> getAllYears(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if (user != null) {
            List<Integer> years = this.movieRepo.getAllYears();
            return (years != null && !years.isEmpty()) ? years : null;
        }
        return null;
    }

    public List<CommentData> getAllCommentsByMovieId(int userId, int movieId) {
        User user = this.userRepo.findByUserId(userId);
        Movie movie = this.movieRepo.findByMovieId(movieId);
        if (user != null && movie != null) {
            List<Comment> comments = this.commentRepo.getAllCommentsByMovieId(movieId);
            List<CommentData> commentDatas = new ArrayList<>();
            for (Comment comment : comments) {
                commentDatas.add(new CommentData(comment.getCommentId(), comment.getUserId(), comment.getMovieId(),
                        comment.getContent(), comment.getCreatedAt(),
                        this.userRepo.findByUserId(comment.getUserId()).getUsername()));
            }
            return commentDatas;
        }
        return null;
    }

    public Comment addComment(CommentForm commentForm) {
        int userId = commentForm.getUserId();
        int movieId = commentForm.getMovieId();
        String content = commentForm.getContent();
        User user = userRepo.findByUserId(userId);
        Movie movie = movieRepo.findByMovieId(movieId);
        if (user != null && movie != null && content != null && !content.isEmpty()) {
            commentRepo.addComment(userId, movieId, content);
            return new Comment();
        }
        return null;
    }

    @Override
    public Movie updateMovieViews(int userId, int movieId) {
        User user = userRepo.findByUserId(userId);
        Movie movie = movieRepo.findByMovieId(movieId);
        if(user!=null && movie!=null) {
            // Cập nhật lượt xem
            this.movieRepo.updateMovieViews(movieId);

            // Thêm vào lịch sử xem (nếu chưa có)
            int alreadyWatched = this.watchHistoryRepo.checkIfWatched(userId, movieId);
            if(alreadyWatched == 0) {
                this.watchHistoryRepo.addToWatchHistory(userId, movieId);
            }
            return movie;
        }
        return null;
    }

    public List<Movie> getAllMoviesByGenreId(int userId, int genreId) {
        User user = userRepo.findByUserId(userId);
        if (user != null) {
            List<Movie> movies = movieRepo.getAllMoviesByGenre(genreId);
            if (movies != null && !movies.isEmpty()) {
                return movies;
            }
            return null;
        }
        return null;
    }

    public List<Movie> getAllMoviesByCountry(int userId, String country) {
        User user = userRepo.findByUserId(userId);
        if (user != null) {
            List<Movie> movies = movieRepo.getAllMoviesByCountry(country);
            if (movies != null && !movies.isEmpty()) {
                return movies;
            }
            return null;
        }
        return null;
    }

    @Override
    public List<Movie> getLikedMoviesByUserId(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if(user!=null) {
            return this.favoriteRepo.findAllFavoriedMovieByUerId(userId);
        }
        return null;
    }

    @Override
    public List<Movie> getSavedMoviesByUserId(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if(user!=null) {
            return this.saveRepo.findAllSavedMovieByUserId(userId);
        }
        return null;
    }

    @Override
    public List<Movie> getWatchedMoviesByUserId(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if(user!=null) {
            return this.watchHistoryRepo.findWatchedMovieByUserId(userId);
        }
        return null;
    }

    //---------------------------------------------------------------------
    private static final int PAGE_SIZE = 10;

    @Override
    public Page<Movie> getAllMovies(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "movieId"));
        return movieRepo.findAll(pageable);
    }

    @Override
    public Page<Movie> searchMoviesByTitle(String title, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "movieId"));
        return movieRepo.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Movie getMovieById(int movieId) {
        return movieRepo.findById(movieId).orElse(null);
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepo.save(movie);
    }

    @Override
    public Movie updateMovie(int movieId, Movie movieDetails) {
        Optional<Movie> movie = movieRepo.findById(movieId);
        if (movie.isEmpty()) {
            return null;
        }

        Movie existingMovie = movie.get();

        existingMovie.setTitle(movieDetails.getTitle());
        existingMovie.setDescription(movieDetails.getDescription());
        existingMovie.setReleaseYear(movieDetails.getReleaseYear());
        existingMovie.setPosterUrl(movieDetails.getPosterUrl());
        existingMovie.setThumbUrl(movieDetails.getThumbUrl());
        existingMovie.setTrailerUrl(movieDetails.getTrailerUrl());
        existingMovie.setCountry(movieDetails.getCountry());
        existingMovie.setLanguage(movieDetails.getLanguage());
        existingMovie.setMovieStatus(movieDetails.getMovieStatus());
        existingMovie.setViews(movieDetails.getViews());

        return movieRepo.save(existingMovie);
    }

    @Override
    public void deleteMovie(int movieId) {
        movieActorRepo.deleteByMovieId(movieId);
        movieDirectorRepo.deleteByMovieId(movieId);
        movieGenreRepo.deleteByMovieId(movieId);
        episodeRepo.unlinkEpisodesFromMovie(movieId);
        commentRepo.deleteByMovieId(movieId);

        movieRepo.deleteById(movieId);
    }

}
