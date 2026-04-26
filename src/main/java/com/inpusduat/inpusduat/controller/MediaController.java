package com.inpusduat.inpusduat.controller;

import com.inpusduat.inpusduat.dto.media.MediaRequest;
import com.inpusduat.inpusduat.dto.media.MediaResponse;
import com.inpusduat.inpusduat.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "media", description = "Series and movies catalogue management")
public class MediaController {

    private final MediaService mediaService;

    @GetMapping
    @Operation(summary = "List all media paginated")
    public ResponseEntity<Page<MediaResponse>> findAll(
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(mediaService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a media entry by ID")
    public ResponseEntity<MediaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mediaService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new media entry — ADMIN only")
    public ResponseEntity<MediaResponse> create(
            @Valid @RequestBody MediaRequest request) {
        // TODO: obtener userId del token JWT cuando implementemos seguridad
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mediaService.create(request, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a media entry — ADMIN only")
    public ResponseEntity<MediaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MediaRequest request) {
        return ResponseEntity.ok(mediaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a media entry — ADMIN only")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mediaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}