package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import util.ValidationUtils;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {
    @Value("${app.file.upload-dir}")
    private String uploadDir;

    /**
     * Store uploaded file and return the file path
     */
    public String storeFile(MultipartFile file) throws IOException {
        if (!ValidationUtils.isValidImageFile(file)) {
            throw new IllegalArgumentException("Invalid file type or size");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String filename = ValidationUtils.generateUniqueFilename(originalFilename);

        // Store file
        Path targetLocation = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    /**
     * Delete file from storage
     */
    public void deleteFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        Files.deleteIfExists(filePath);
    }

    /**
     * Get file path for serving
     */
    public Path getFilePath(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }

}
