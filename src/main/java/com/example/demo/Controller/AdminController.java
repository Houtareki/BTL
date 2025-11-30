package com.example.demo.Controller;

import com.example.demo.Model.CommonModel.UserForm;
import com.example.demo.Model.RequestModel.MovieUpdateRequestDTO;
import com.example.demo.Respository.CommentRepo;
import com.example.demo.Respository.MovieRepo;
import com.example.demo.Respository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Movie;
import com.example.demo.Entity.User;
import com.example.demo.Model.CommonModel.Role;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Model.ResponseModel.MovieDetailResponseDTO;
import com.example.demo.Service.MovieServiceAdmin;
import com.example.demo.Service.UserService;

import java.util.HashMap;
import java.util.Map;

@Controller 
@RequestMapping("/api/admin/movies") 
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private MovieServiceAdmin movieServiceAdmin; 

    @GetMapping("/get-page")
	public String getMovieManagerPage() {
		return "AdminMovie";
	}

    @GetMapping
    public ResponseEntity<?> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "movieId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            org.springframework.data.domain.Page<Movie> moviesPage =
                movieServiceAdmin.getAllMoviesWithPaging(page, size, sortBy, sortDirection);

            return ResponseEntity.ok(moviesPage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Không thể lấy danh sách phim: " + e.getMessage(), null)
            );
        }
    }
  
    // Thêm phim mới (legacy - chỉ thông tin cơ bản)
    @PostMapping("/basic")
    public ResponseEntity<?> addNewMovie(@RequestBody Movie movie) {
        try {
            Movie newMovie = movieServiceAdmin.addMovie(movie); 
            CustomData<Movie> data = new CustomData<>(newMovie);
            CustomResponse<Movie> response = new CustomResponse<>("Success", "Thêm phim mới thành công!", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new CustomResponse<>("Error", "Thêm phim thất bại: " + e.getMessage(), null)
            );
        }
    }

    // Thêm phim mới kèm actors và genres
    @PostMapping
    public ResponseEntity<?> addNewMovieWithRelationships(@RequestBody MovieUpdateRequestDTO createRequest) {
        try {
            Movie newMovie = movieServiceAdmin.addMovieWithRelationships(createRequest); 
            CustomData<Movie> data = new CustomData<>(newMovie);
            CustomResponse<Movie> response = new CustomResponse<>("Success", "Thêm phim mới thành công!", data);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(
                new CustomResponse<>("Error", e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new CustomResponse<>("Error", "Thêm phim thất bại: " + e.getMessage(), null)
            );
        }
    }
  
    // Sửa thông tin phim kèm actors và genres
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable("id") int id, @RequestBody MovieUpdateRequestDTO updateRequest) {
        try {
            Movie updatedMovie = movieServiceAdmin.updateMovieWithRelationships(id, updateRequest); 
            CustomData<Movie> data = new CustomData<>(updatedMovie);
            CustomResponse<Movie> response = new CustomResponse<>("Success", "Cập nhật phim thành công!", data);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) { 
            return ResponseEntity.status(404).body(
                new CustomResponse<>("Error", e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new CustomResponse<>("Error", "Cập nhật phim thất bại: " + e.getMessage(), null)
            );
        }
    }
  
    // Xóa phim    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") int id) {
        try {
            movieServiceAdmin.deleteMovie(id); 
            return ResponseEntity.ok(
                new CustomResponse<>("Success", "Xóa phim thành công!", null)
            );
        } catch (RuntimeException e) { 
             return ResponseEntity.status(404).body(
                new CustomResponse<>("Error", e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new CustomResponse<>("Error", "Xóa phim thất bại: " + e.getMessage(), null)
            );
        }
    }
    
    // Lấy chi tiết phim kèm episodes, actors và genres
    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getMovieDetail(@PathVariable("id") int id) {
        try {
            MovieDetailResponseDTO movieDetail = movieServiceAdmin.getMovieDetail(id);
            CustomData<MovieDetailResponseDTO> data = new CustomData<>(movieDetail);
            CustomResponse<MovieDetailResponseDTO> response = new CustomResponse<>(
                "Success", 
                "Lấy thông tin chi tiết phim thành công!", 
                data
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                new CustomResponse<>("Error", e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Không thể lấy thông tin phim: " + e.getMessage(), null)
            );
        }
    }

    
}