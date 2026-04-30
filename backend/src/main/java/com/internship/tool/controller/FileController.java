package com.internship.tool.controller;

import com.internship.tool.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * All file endpoints require a valid JWT.
 *
 * Pattern for future PUBLIC endpoints (e.g. /auth/login, /auth/register):
 *   @Operation(security = @SecurityRequirement(name = ""))
 * That empty-string name overrides the class-level security requirement so
 * Swagger UI renders those methods without the padlock.
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File Attachments", description = "Upload and download file attachments")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Upload a file",
               description = "Accepts pdf, jpg, jpeg, or png files up to 10 MB. Returns the stored UUID-based filename.")
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file) {

        String storedFilename = fileService.storeFile(file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "filename", storedFilename,
                        "message",  "File uploaded successfully"
                ));
    }

    @Operation(summary = "Download a file",
               description = "Streams the stored file by its UUID-based filename with the correct Content-Type.")
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource resource = fileService.loadFile(filename);

        MediaType mediaType = MediaTypeFactory
                .getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        String safeFilename = org.springframework.util.StringUtils.cleanPath(
                resource.getFilename() != null ? resource.getFilename() : filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + safeFilename + "\"")
                .contentType(mediaType)
                .body(resource);
    }
}
