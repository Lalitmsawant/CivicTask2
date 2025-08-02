package util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ValidationUtils {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * Validate uploaded image file
     */
    public static boolean isValidImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        // Check content type
        String contentType = file.getContentType();
        return contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }

    /**
     * Get file extension from filename
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }

        return filename.substring(lastDotIndex);
    }

    /**
     * Generate unique filename
     */
    public static String generateUniqueFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return System.currentTimeMillis() + "_" + Math.random() + extension;
    }

}
