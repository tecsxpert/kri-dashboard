package com.internship.tool.service;

import com.internship.tool.exception.BadRequestException;
import com.internship.tool.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png");
    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024; // 10 MB

    @Value("${app.uploads.dir:uploads}")
    private String uploadsDir;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        uploadPath = Paths.get(uploadsDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
            log.info("Upload directory ready: {}", uploadPath);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not create upload directory: " + uploadPath, ex);
        }
    }

    // ------------------------------------------------------------------ //
    //  Public API                                                          //
    // ------------------------------------------------------------------ //

    /**
     * Validates, renames, and stores an uploaded file.
     *
     * @return the stored filename (UUID + original extension)
     */
    public String storeFile(MultipartFile file) {
        // 1. Null / empty check
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("No file provided or file is empty");
        }

        // 2. Explicit size check (defence-in-depth beyond spring.servlet.multipart config)
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException(
                    "File exceeds maximum allowed size of 10 MB (received: " + file.getSize() + " bytes)");
        }

        // 3. Validate and sanitise original filename to prevent path traversal
        String rawName = file.getOriginalFilename();
        if (rawName == null || rawName.isBlank()) {
            throw new BadRequestException("Invalid file name");
        }
        String originalName = StringUtils.cleanPath(rawName);

        if (originalName.contains("..")) {
            throw new BadRequestException("Invalid filename — path traversal attempt detected");
        }

        // 4. Extension validation (case-insensitive)
        String extension = extractExtension(originalName);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BadRequestException(
                    "File type '." + extension + "' is not allowed. Accepted: " + ALLOWED_EXTENSIONS);
        }

        // 5. Generate unique filename and resolve target path inside upload directory
        String storedFilename = UUID.randomUUID() + "." + extension.toLowerCase();
        Path targetPath = uploadPath.resolve(storedFilename).normalize();

        // 6. Verify the resolved path is still inside the upload directory (second layer)
        if (!targetPath.startsWith(uploadPath)) {
            throw new BadRequestException("Invalid filename — path traversal attempt detected");
        }

        // 7. Write to disk
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File stored: {} (size: {} bytes)", storedFilename, file.getSize());
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to store file: " + storedFilename, ex);
        }

        return storedFilename;
    }

    /**
     * Loads a stored file as a Spring {@link Resource} for streaming to the client.
     *
     * @throws ResourceNotFoundException if the file does not exist or is not readable
     */
    public Resource loadFile(String filename) {
        try {
            // Sanitise and resolve; prevent traversal beyond upload root
            Path filePath = uploadPath.resolve(StringUtils.cleanPath(filename)).normalize();

            if (!filePath.startsWith(uploadPath)) {
                throw new ResourceNotFoundException("File not found: " + filename);
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File not found: " + filename);
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found: " + filename);
        }
    }

    // ------------------------------------------------------------------ //
    //  Private helpers                                                     //
    // ------------------------------------------------------------------ //

    private String extractExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            throw new BadRequestException("File has no valid extension");
        }
        return filename.substring(dotIndex + 1);
    }
}
