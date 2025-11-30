package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Genre;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Respository.GenreRepo;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@CrossOrigin(origins = "*")
public class GenreController {

    @Autowired
    private GenreRepo genreRepo;

    @GetMapping
    public ResponseEntity<?> getAllGenres() {
        try {
            List<Genre> genres = genreRepo.findAll();
            CustomData<List<Genre>> data = new CustomData<>(genres);
            CustomResponse<List<Genre>> response = new CustomResponse<>(
                "Success",
                "Lấy danh sách thể loại thành công!",
                data
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Không thể lấy danh sách thể loại: " + e.getMessage(), null)
            );
        }
    }
}
