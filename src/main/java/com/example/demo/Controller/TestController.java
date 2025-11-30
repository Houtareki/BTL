package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.demo.Entity.Actor;
import com.example.demo.Entity.Director;
import com.example.demo.Entity.Episode;
import com.example.demo.Entity.Genre;
import com.example.demo.Entity.Movie;
import com.example.demo.Entity.User;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Respository.ActorRepo;
import com.example.demo.Respository.DirectorRepo;
import com.example.demo.Respository.EpisodeRepo;
import com.example.demo.Respository.FavoriteRepo;
import com.example.demo.Respository.GenreRepo;
import com.example.demo.Respository.MovieActorRepo;
import com.example.demo.Respository.MovieDirectorRepo;
import com.example.demo.Respository.MovieGenreRepo;
import com.example.demo.Respository.MovieRepo;
import com.example.demo.Respository.UserRepo;
import com.example.demo.Respository.WatchHistoryRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TestController {

    @Autowired
    private EpisodeRepo episodeRepo;
    
    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private ActorRepo actorRepo;

    @Autowired
    private DirectorRepo directorRepo;

    @Autowired
    private FavoriteRepo favoriteRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private MovieActorRepo movieActorRepo;

    @Autowired
    private MovieDirectorRepo movieDirectorRepo;

    @Autowired
    private MovieGenreRepo movieGenreRepo;

    @Autowired
    private WatchHistoryRepo watchHistoryRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/get-all-episode")
    public ResponseEntity<?> getAllEpisode() {
        Map<String,String> response = new HashMap<>();
        List<Episode> episodeList = new ArrayList<>();
        try {
            episodeList = this.episodeRepo.findAll();
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Episodes found!");
        response.put("Data", episodeList.toString());
        return ResponseEntity.ok(response);
        
    }
    
    @GetMapping("/get-all-movie")
    public ResponseEntity<?> getAllMovie() {
        Map<String,String> response = new HashMap<>();
        List<Movie> episodeList = new ArrayList<>();
        try {
            episodeList = this.movieRepo.findAll();
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Movies found!");
        response.put("Data", episodeList.toString());
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/get-all-actor")
    public ResponseEntity<?> getAllActor() {
        Map<String,String> response = new HashMap<>();
        List<Actor> episodeList = new ArrayList<>();
        try {
            episodeList = this.actorRepo.findAll();
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Actors found!");
        response.put("Data", episodeList.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-director")
    public ResponseEntity<?> getAllDirector() {
        Map<String,String> response = new HashMap<>();
        List<Director> episodeList = new ArrayList<>();
        try {
            episodeList = this.directorRepo.findAll();
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Actors found!");
        response.put("Data", episodeList.toString());
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/get-all-favorited-movies-by-userId")
    public ResponseEntity<?> getAllFavoritedMovieByUserId(@RequestParam(name = "userId") int userId) {
        Map<String,String> response = new HashMap<>();
        List<Movie> movieList = new ArrayList<>();
        try {
            movieList = favoriteRepo.findAllFavoriedMovieByUerId(userId);
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Actors found!");
        response.put("Data", movieList.toString());
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/get-all-genre")
    public ResponseEntity<?> getAllGenre() {
        Map<String,String> response = new HashMap<>();
        List<Genre> movieList = new ArrayList<>();
        try {
            movieList = genreRepo.findAll();
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Actors found!");
        response.put("Data", movieList.toString());
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/get-all-actors-by-movieId")
    public ResponseEntity<?> getAllActorByMovieId(@RequestParam(name = "movieId") int movieId) {
        Map<String,String> response = new HashMap<>();
        List<Actor> movieList = new ArrayList<>();
        try {
            movieList = movieActorRepo.findActorsByMovieId(movieId);
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Actors found!");
        response.put("Data", movieList.toString());
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/get-all-directors-by-movieId")
    public ResponseEntity<?> getAllDirectorByMovieId(@RequestParam(name = "movieId") int movieId) {
        Map<String,String> response = new HashMap<>();
        List<Director> movieList = new ArrayList<>();
        try {
            movieList = movieDirectorRepo.findDirectorsByMovieId(movieId);
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Actors found!");
        response.put("Data", movieList.toString());
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/get-all-genres-by-movieId")
    public ResponseEntity<?> getAllGenreByMovieId(@RequestParam(name = "movieId") int movieId) {
        Map<String,String> response = new HashMap<>();
        List<Genre> movieList = new ArrayList<>();
        try {
            movieList = movieGenreRepo.findGenresByMovieId(movieId);
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Actors found!");
        response.put("Data", movieList.toString());
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/get-all-watched-movies-by-userId")
    public ResponseEntity<?> getAllWatchedMoviesByMovieId(@RequestParam(name = "userId") int userId) {
        Map<String,String> response = new HashMap<>();
        List<Movie> movieList = new ArrayList<>();
        try {
            movieList = watchHistoryRepo.findWatchedMovieByUserId(userId);
        } catch (Exception e) {
            response.put("Status","Error");
            response.put("Message","Somethins went wrong! Try later!");
            return ResponseEntity.ok(response);
        }
        response.put("Status","Success");
        response.put("Message","Actors found!");
        response.put("Data", movieList.toString());
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/test")
    public ResponseEntity<?> getMethodName() {
        CustomData<List<Actor>> data = new CustomData<>(actorRepo.findAll());
        CustomResponse<List<Actor>> response = new CustomResponse<>("success", "thanh cong oi", data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUser() {
        CustomData<List<User>> data = new CustomData<>(userRepo.findAll());
        CustomResponse<List<User>> response = new CustomResponse<>("success", "thanh cong oi", data);

        return ResponseEntity.ok(response);
    }
    
}
