package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Actor;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Respository.ActorRepo;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
@CrossOrigin(origins = "*")
public class ActorController {

    @Autowired
    private ActorRepo actorRepo;

    @GetMapping
    public ResponseEntity<?> getAllActors() {
        try {
            List<Actor> actors = actorRepo.findAll();
            CustomData<List<Actor>> data = new CustomData<>(actors);
            CustomResponse<List<Actor>> response = new CustomResponse<>(
                "Success",
                "Lấy danh sách diễn viên thành công!",
                data
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Không thể lấy danh sách diễn viên: " + e.getMessage(), null)
            );
        }
    }
}
