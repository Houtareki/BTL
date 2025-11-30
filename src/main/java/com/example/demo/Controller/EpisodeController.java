package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Episode;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Respository.EpisodeRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/episodes")
@CrossOrigin(origins = "*")
public class EpisodeController {

    @Autowired
    private EpisodeRepo episodeRepo;

    // Lấy tất cả episodes của một movie
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getEpisodesByMovieId(@PathVariable("movieId") int movieId) {
        try {
            List<Episode> episodes = episodeRepo.findAllEpisodeByMovieId(movieId);
            return ResponseEntity.ok(episodes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Không thể lấy danh sách tập phim: " + e.getMessage(), null)
            );
        }
    }

    // Lấy một episode theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEpisodeById(@PathVariable("id") int id) {
        try {
            Optional<Episode> episode = episodeRepo.findByEpisodeId(id);
            if (episode.isPresent()) {
                return ResponseEntity.ok(episode.get());
            } else {
                return ResponseEntity.status(404).body(
                    new CustomResponse<>("Error", "Không tìm thấy tập phim với ID: " + id, null)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Lỗi khi lấy thông tin tập phim: " + e.getMessage(), null)
            );
        }
    }

    // Thêm episode mới
    @PostMapping
    public ResponseEntity<?> createEpisode(@RequestBody Episode episode) {
        try {
            Episode savedEpisode = episodeRepo.save(episode);
            CustomData<Episode> data = new CustomData<>(savedEpisode);
            CustomResponse<Episode> response = new CustomResponse<>("Success", "Thêm tập phim thành công!", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new CustomResponse<>("Error", "Thêm tập phim thất bại: " + e.getMessage(), null)
            );
        }
    }

    // Cập nhật episode
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEpisode(@PathVariable("id") int id, @RequestBody Episode episodeDetails) {
        try {
            Optional<Episode> existingEpisode = episodeRepo.findByEpisodeId(id);
            
            if (!existingEpisode.isPresent()) {
                return ResponseEntity.status(404).body(
                    new CustomResponse<>("Error", "Không tìm thấy tập phim với ID: " + id, null)
                );
            }

            Episode episode = existingEpisode.get();
            episode.setName(episodeDetails.getName());
            episode.setVideoUrl(episodeDetails.getVideoUrl());
            
            Episode updatedEpisode = episodeRepo.save(episode);
            CustomData<Episode> data = new CustomData<>(updatedEpisode);
            CustomResponse<Episode> response = new CustomResponse<>("Success", "Cập nhật tập phim thành công!", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new CustomResponse<>("Error", "Cập nhật tập phim thất bại: " + e.getMessage(), null)
            );
        }
    }

    // Xóa episode
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEpisode(@PathVariable("id") int id) {
        try {
            Optional<Episode> episode = episodeRepo.findByEpisodeId(id);
            
            if (!episode.isPresent()) {
                return ResponseEntity.status(404).body(
                    new CustomResponse<>("Error", "Không tìm thấy tập phim với ID: " + id, null)
                );
            }

            episodeRepo.deleteById(id);
            return ResponseEntity.ok(
                new CustomResponse<>("Success", "Xóa tập phim thành công!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Xóa tập phim thất bại: " + e.getMessage(), null)
            );
        }
    }

    // Xóa tất cả episodes của một movie
    @DeleteMapping("/movie/{movieId}")
    public ResponseEntity<?> deleteEpisodesByMovieId(@PathVariable("movieId") int movieId) {
        try {
            List<Episode> episodes = episodeRepo.findAllEpisodeByMovieId(movieId);
            episodeRepo.deleteAll(episodes);
            return ResponseEntity.ok(
                new CustomResponse<>("Success", "Xóa tất cả tập phim của movie thành công!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Xóa tập phim thất bại: " + e.getMessage(), null)
            );
        }
    }
}
