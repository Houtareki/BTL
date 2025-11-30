package com.example.demo.Model.ResponseModel;

import com.example.demo.Entity.Movie;
import com.example.demo.Entity.Episode;
import com.example.demo.Entity.Actor;
import com.example.demo.Entity.Genre;

import java.util.List;

public class MovieDetailResponseDTO {
    
    // Thông tin phim cơ bản
    private int movieId;
    private String title;
    private String description;
    private String releaseYear;
    private String posterUrl;
    private String thumbUrl;
    private String trailerUrl;
    private String status;
    private String country;
    private String language;
    private int views;
    
    // Danh sách episodes
    private List<EpisodeDTO> episodes;
    
    // Danh sách actors
    private List<ActorDTO> actors;
    
    // Danh sách genres
    private List<GenreDTO> genres;
    
    // Constructor mặc định
    public MovieDetailResponseDTO() {}
    
    // Constructor từ Movie entity
    public MovieDetailResponseDTO(Movie movie) {
        this.movieId = movie.getMovieId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.releaseYear = movie.getReleaseYear();
        this.posterUrl = movie.getPosterUrl();
        this.thumbUrl = movie.getThumbUrl();
        this.trailerUrl = movie.getTrailerUrl();
        this.status = movie.getMovieStatus() != null ? movie.getMovieStatus().toString() : "SHOWING";
        this.country = movie.getCountry();
        this.language = movie.getLanguage();
        this.views = movie.getViews();
    }
    
    // Constructor đầy đủ với episodes, actors, genres
    public MovieDetailResponseDTO(Movie movie, List<EpisodeDTO> episodes, List<ActorDTO> actors, List<GenreDTO> genres) {
        this(movie); // Gọi constructor trên để set các field cơ bản
        this.episodes = episodes;
        this.actors = actors;
        this.genres = genres;
    }
    
    // Inner class cho Episode
    public static class EpisodeDTO {
        private int episodeId;
        private String name;
        private String videoUrl;
        
        public EpisodeDTO() {}
        
        public EpisodeDTO(Episode episode) {
            this.episodeId = episode.getEpisodeId();
            this.name = episode.getName();
            this.videoUrl = episode.getVideoUrl();
        }
        
        // Getters and Setters
        public int getEpisodeId() { return episodeId; }
        public void setEpisodeId(int episodeId) { this.episodeId = episodeId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVideoUrl() { return videoUrl; }
        public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    }
    
    // Inner class cho Actor
    public static class ActorDTO {
        private int actorId;
        private String name;
        
        public ActorDTO() {}
        
        public ActorDTO(Actor actor) {
            this.actorId = actor.getActorId();
            this.name = actor.getName();
        }
        
        // Getters and Setters
        public int getActorId() { return actorId; }
        public void setActorId(int actorId) { this.actorId = actorId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    // Inner class cho Genre
    public static class GenreDTO {
        private int genreId;
        private String name;
        
        public GenreDTO() {}
        
        public GenreDTO(Genre genre) {
            this.genreId = genre.getGenreId();
            this.name = genre.getName();
        }
        
        // Getters and Setters
        public int getGenreId() { return genreId; }
        public void setGenreId(int genreId) { this.genreId = genreId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    // Getters and Setters cho MovieDetailResponseDTO
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getReleaseYear() { return releaseYear; }
    public void setReleaseYear(String releaseYear) { this.releaseYear = releaseYear; }
    
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    
    public String getThumbUrl() { return thumbUrl; }
    public void setThumbUrl(String thumbUrl) { this.thumbUrl = thumbUrl; }
    
    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    
    public List<EpisodeDTO> getEpisodes() { return episodes; }
    public void setEpisodes(List<EpisodeDTO> episodes) { this.episodes = episodes; }
    
    public List<ActorDTO> getActors() { return actors; }
    public void setActors(List<ActorDTO> actors) { this.actors = actors; }
    
    public List<GenreDTO> getGenres() { return genres; }
    public void setGenres(List<GenreDTO> genres) { this.genres = genres; }
}
