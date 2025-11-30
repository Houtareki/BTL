package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    // Đường dẫn thư mục lưu file upload - upload vào resources để lưu trữ lâu dài
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/posters/";
    private static final String VIDEO_UPLOAD_DIR = "src/main/resources/static/uploads/videos/";

    @PostMapping("/poster")
    public ResponseEntity<?> uploadPoster(@RequestParam("file") MultipartFile file) {
        try {
            // Kiểm tra file có rỗng không
            if (file.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "Error");
                errorResponse.put("message", "Vui lòng chọn file để upload");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Kiểm tra loại file (chỉ cho phép ảnh)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "Error");
                errorResponse.put("message", "Chỉ cho phép upload file ảnh");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Tạo thư mục nếu chưa tồn tại
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Tạo tên file unique để tránh trùng lặp
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Lưu file vào thư mục
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Tạo URL để truy cập file
            String fileUrl = "/uploads/posters/" + uniqueFilename;

            // Tạo response đơn giản hơn
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", "Success");
            responseMap.put("message", "Upload ảnh thành công!");
            responseMap.put("url", fileUrl);
            responseMap.put("filename", uniqueFilename);

            return ResponseEntity.ok(responseMap);

        } catch (IOException e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "Error");
            errorResponse.put("message", "Lỗi khi upload file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @DeleteMapping("/poster")
    public ResponseEntity<?> deletePoster(@RequestParam("filename") String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            Files.deleteIfExists(filePath);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "Success");
            successResponse.put("message", "Xóa ảnh thành công!");
            return ResponseEntity.ok(successResponse);
        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "Error");
            errorResponse.put("message", "Lỗi khi xóa file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            // Kiểm tra file có rỗng không
            if (file.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "Error");
                errorResponse.put("message", "Vui lòng chọn file để upload");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Kiểm tra loại file (chỉ cho phép video)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "Error");
                errorResponse.put("message", "Chỉ cho phép upload file video");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Kiểm tra kích thước file (max 500MB)
            long maxSize = 500L * 1024 * 1024; // 500MB
            if (file.getSize() > maxSize) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "Error");
                errorResponse.put("message", "Kích thước video không được vượt quá 500MB");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Tạo thư mục nếu chưa tồn tại
            File uploadDir = new File(VIDEO_UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Tạo tên file unique để tránh trùng lặp
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Lưu file vào thư mục
            Path filePath = Paths.get(VIDEO_UPLOAD_DIR + uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Tạo URL để truy cập file
            String fileUrl = "/uploads/videos/" + uniqueFilename;

            // Tạo response
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", "Success");
            responseMap.put("message", "Upload video thành công!");
            responseMap.put("url", fileUrl);
            responseMap.put("filename", uniqueFilename);

            return ResponseEntity.ok(responseMap);

        } catch (IOException e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "Error");
            errorResponse.put("message", "Lỗi khi upload file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @DeleteMapping("/video")
    public ResponseEntity<?> deleteVideo(@RequestParam("filename") String filename) {
        try {
            Path filePath = Paths.get(VIDEO_UPLOAD_DIR + filename);
            Files.deleteIfExists(filePath);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "Success");
            successResponse.put("message", "Xóa video thành công!");
            return ResponseEntity.ok(successResponse);
        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "Error");
            errorResponse.put("message", "Lỗi khi xóa file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
