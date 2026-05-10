package com.inpusduat.inpusduat.controller;

import com.inpusduat.inpusduat.dto.usermedia.UserMediaRequest;
import com.inpusduat.inpusduat.dto.usermedia.UserMediaResponse;
import com.inpusduat.inpusduat.dto.usermedia.UserMediaUpdateRequest;
import com.inpusduat.inpusduat.service.UserMediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/v1/user-media")
@RequiredArgsConstructor
public class UserMediaController {

    private final UserMediaService userMediaService;

    @PostMapping
    public ResponseEntity<UserMediaResponse> addMedia(@Valid @RequestBody UserMediaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userMediaService.addMedia(request));
    }

    @GetMapping
    public ResponseEntity<Page<UserMediaResponse>> getMyList(
            @PageableDefault(size = 20) Pageable pageable) {  // ← add this
        return ResponseEntity.ok(userMediaService.getMyList(pageable));
    }

    @PutMapping("/{mediaId}")
    public ResponseEntity<UserMediaResponse> updateMedia(
            @PathVariable Long mediaId,
            @Valid @RequestBody UserMediaUpdateRequest request) {
        return ResponseEntity.ok(userMediaService.updateMedia(mediaId, request));
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> removeMedia(@PathVariable Long mediaId) {
        userMediaService.removeMedia(mediaId);
        return ResponseEntity.noContent().build();
    }
}