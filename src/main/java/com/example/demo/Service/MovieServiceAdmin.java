package com.example.demo.Service;

import com.example.demo.Entity.Movie;
import com.example.demo.Respository.MovieRepo;
import com.example.demo.Entity.Episode;
import com.example.demo.Entity.Actor;
import com.example.demo.Entity.Genre;
import com.example.demo.Entity.MovieActor;
import com.example.demo.Entity.MovieGenre;
import com.example.demo.Model.CommonModel.MovieStatus;
import com.example.demo.Model.RequestModel.MovieUpdateRequestDTO;
import com.example.demo.Model.ResponseModel.MovieDetailResponseDTO;
import com.example.demo.Respository.MovieRepo; 
import com.example.demo.Respository.EpisodeRepo;
import com.example.demo.Respository.MovieActorRepo;
import com.example.demo.Respository.MovieGenreRepo;
import com.example.demo.Respository.ActorRepo;
import com.example.demo.Respository.GenreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors; 

@Service
public class MovieServiceAdmin {

    @Autowired
    private MovieRepo movieRepo;
    
    @Autowired
    private EpisodeRepo episodeRepo;
    
    @Autowired
    private MovieActorRepo movieActorRepo;
    
    @Autowired
    private MovieGenreRepo movieGenreRepo;
    
    @Autowired
    private ActorRepo actorRepo;
    
    @Autowired
    private GenreRepo genreRepo;

    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    public Page<Movie> getAllMoviesWithPaging(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return movieRepo.findAll(pageable);
    }

    //Thêm phim mới (cho Admin)

    public Movie addMovie(Movie movie) {
        movie.setViews(0);

        return movieRepo.save(movie);
    }

    // Thêm phim mới kèm actors và genres (cho Admin)
    @Transactional
    public Movie addMovieWithRelationships(MovieUpdateRequestDTO createRequest) {
        // Tạo Movie object mới
        Movie newMovie = new Movie();
        newMovie.setTitle(createRequest.getTitle());
        newMovie.setDescription(createRequest.getDescription());
        newMovie.setReleaseYear(createRequest.getReleaseYear());
        newMovie.setPosterUrl(createRequest.getPosterUrl());
        newMovie.setCountry(createRequest.getCountry());
        newMovie.setLanguage(createRequest.getLanguage());
        newMovie.setTrailerUrl(createRequest.getTrailerUrl());
        newMovie.setThumbUrl(createRequest.getThumbUrl());
        newMovie.setViews(0);
        
        // Set MovieStatus, mặc định là showing nếu không có
        if (createRequest.getMovieStatus() != null && !createRequest.getMovieStatus().isEmpty()) {
            newMovie.setMovieStatus(MovieStatus.valueOf(createRequest.getMovieStatus()));
        } else {
            newMovie.setMovieStatus(MovieStatus.showing);
        }

        // Lưu phim mới
        Movie savedMovie = movieRepo.save(newMovie);

        // Xử lý actors: thêm actors mới
        if (createRequest.getActorIds() != null && !createRequest.getActorIds().isEmpty()) {
            for (Integer actorId : createRequest.getActorIds()) {
                // Kiểm tra actor có tồn tại không
                if (!actorRepo.existsById(actorId)) {
                    throw new RuntimeException("Không tìm thấy diễn viên với id: " + actorId);
                }
                
                MovieActor movieActor = new MovieActor(savedMovie.getMovieId(), actorId);
                movieActorRepo.save(movieActor);
            }
        }

        // Xử lý genres: thêm genres mới
        if (createRequest.getGenreIds() != null && !createRequest.getGenreIds().isEmpty()) {
            for (Integer genreId : createRequest.getGenreIds()) {
                // Kiểm tra genre có tồn tại không
                if (!genreRepo.existsById(genreId)) {
                    throw new RuntimeException("Không tìm thấy thể loại với id: " + genreId);
                }
                
                MovieGenre movieGenre = new MovieGenre(savedMovie.getMovieId(), genreId);
                movieGenreRepo.save(movieGenre);
            }
        }

        return savedMovie;
    }
    // Sửa thông tin phim (cho Admin)

    public Movie updateMovie(int id, Movie movieDetails) {

        Movie existingMovie = movieRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy phim với id: " + id));

        existingMovie.setTitle(movieDetails.getTitle());
        existingMovie.setDescription(movieDetails.getDescription());
        existingMovie.setReleaseYear(movieDetails.getReleaseYear());
        existingMovie.setPosterUrl(movieDetails.getPosterUrl());
        existingMovie.setMovieStatus(movieDetails.getMovieStatus());
        existingMovie.setCountry(movieDetails.getCountry());
        existingMovie.setLanguage(movieDetails.getLanguage());
        existingMovie.setTrailerUrl(movieDetails.getTrailerUrl());
        existingMovie.setThumbUrl(movieDetails.getThumbUrl());

        return movieRepo.save(existingMovie);
    }
    
    // Sửa thông tin phim kèm actors và genres (cho Admin)
    @Transactional
    public Movie updateMovieWithRelationships(int id, MovieUpdateRequestDTO updateRequest) {
        // Tìm phim
        Movie existingMovie = movieRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy phim với id: " + id));

        // Cập nhật thông tin cơ bản của phim
        existingMovie.setTitle(updateRequest.getTitle());
        existingMovie.setDescription(updateRequest.getDescription());
        existingMovie.setReleaseYear(updateRequest.getReleaseYear());
        existingMovie.setPosterUrl(updateRequest.getPosterUrl());
        
        // Convert String to MovieStatus enum
        if (updateRequest.getMovieStatus() != null && !updateRequest.getMovieStatus().isEmpty()) {
            existingMovie.setMovieStatus(MovieStatus.valueOf(updateRequest.getMovieStatus()));
        }
        
        existingMovie.setCountry(updateRequest.getCountry());
        existingMovie.setLanguage(updateRequest.getLanguage());
        existingMovie.setTrailerUrl(updateRequest.getTrailerUrl());
        existingMovie.setThumbUrl(updateRequest.getThumbUrl());

        // Lưu thông tin phim
        Movie savedMovie = movieRepo.save(existingMovie);

        // Xử lý actors: xóa tất cả actors cũ và thêm actors mới
        if (updateRequest.getActorIds() != null) {
            movieActorRepo.deleteAllByMovieId(id);
            
            for (Integer actorId : updateRequest.getActorIds()) {
                // Kiểm tra actor có tồn tại không
                if (!actorRepo.existsById(actorId)) {
                    throw new RuntimeException("Không tìm thấy diễn viên với id: " + actorId);
                }
                
                MovieActor movieActor = new MovieActor(id, actorId);
                movieActorRepo.save(movieActor);
            }
        }

        // Xử lý genres: xóa tất cả genres cũ và thêm genres mới
        if (updateRequest.getGenreIds() != null) {
            movieGenreRepo.deleteAllByMovieId(id);
            
            for (Integer genreId : updateRequest.getGenreIds()) {
                // Kiểm tra genre có tồn tại không
                if (!genreRepo.existsById(genreId)) {
                    throw new RuntimeException("Không tìm thấy thể loại với id: " + genreId);
                }
                
                MovieGenre movieGenre = new MovieGenre(id, genreId);
                movieGenreRepo.save(movieGenre);
            }
        }

        return savedMovie;
    }
    // Xóa phim (cho Admin)

    public void deleteMovie(int id) {

        if (!movieRepo.existsById(id)) {
            throw new RuntimeException("Lỗi: Không tìm thấy phim với id: " + id);
        }

        movieRepo.deleteById(id);
    }
    
    // Lấy chi tiết phim kèm episodes, actors và genres
    public MovieDetailResponseDTO getMovieDetail(int movieId) {
        // Lấy thông tin phim
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim với id: " + movieId));
        
        // Lấy danh sách episodes
        List<Episode> episodes = episodeRepo.findAllEpisodeByMovieId(movieId);
        List<MovieDetailResponseDTO.EpisodeDTO> episodeDTOs = episodes.stream()
                .map(MovieDetailResponseDTO.EpisodeDTO::new)
                .collect(Collectors.toList());
        
        // Lấy danh sách actors
        List<Actor> actors = movieActorRepo.findActorsByMovieId(movieId);
        List<MovieDetailResponseDTO.ActorDTO> actorDTOs = actors.stream()
                .map(MovieDetailResponseDTO.ActorDTO::new)
                .collect(Collectors.toList());
        
        // Lấy danh sách genres
        List<Genre> genres = movieGenreRepo.findGenresByMovieId(movieId);
        List<MovieDetailResponseDTO.GenreDTO> genreDTOs = genres.stream()
                .map(MovieDetailResponseDTO.GenreDTO::new)
                .collect(Collectors.toList());
        
        // Tạo và trả về DTO
        return new MovieDetailResponseDTO(movie, episodeDTOs, actorDTOs, genreDTOs);
    }
}
