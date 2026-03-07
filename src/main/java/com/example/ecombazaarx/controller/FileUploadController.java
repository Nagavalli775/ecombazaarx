package com.example.ecombazaarx.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FileUploadController {

    private final String uploadDir = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {

        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());

            String imageUrl = "http://localhost:9090/uploads/" + fileName;

            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed");
        }
    }
}
