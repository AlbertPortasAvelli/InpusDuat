package com.inpusduat.inpusduat.service;

import com.inpusduat.inpusduat.domain.Media;
import com.inpusduat.inpusduat.domain.User;
import com.inpusduat.inpusduat.dto.media.MediaRequest;
import com.inpusduat.inpusduat.dto.media.MediaResponse;
import com.inpusduat.inpusduat.exception.ResourceNotFoundException;
import com.inpusduat.inpusduat.repository.MediaRepository;
import com.inpusduat.inpusduat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<MediaResponse> findAll(Pageable pageable) {
        log.info("Fetching all media - page: {}, size: {}", 
            pageable.getPageNumber(), pageable.getPageSize());
        return mediaRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public MediaResponse findById(Long id) {
        log.info("Fetching media with id: {}", id);
        return mediaRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Media", id));
    }

    @Transactional
    public MediaResponse create(MediaRequest request, Long userId) {
        log.info("Creating media: {} by user: {}", request.getTitle(), userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Media media = Media.builder()
                .title(request.getTitle())
                .originalTitle(request.getOriginalTitle())
                .type(request.getType())
                .genre(request.getGenre())
                .language(request.getLanguage())
                .releaseYear(request.getReleaseYear())
                .synopsis(request.getSynopsis())
                .posterUrl(request.getPosterUrl())
                .createdBy(user)
                .build();

        Media saved = mediaRepository.save(media);
        log.info("Media created successfully with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public MediaResponse update(Long id, MediaRequest request) {
        log.info("Updating media with id: {}", id);
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media", id));

        media.setTitle(request.getTitle());
        media.setOriginalTitle(request.getOriginalTitle());
        media.setType(request.getType());
        media.setGenre(request.getGenre());
        media.setLanguage(request.getLanguage());
        media.setReleaseYear(request.getReleaseYear());
        media.setSynopsis(request.getSynopsis());
        media.setPosterUrl(request.getPosterUrl());

        Media saved = mediaRepository.save(media);
        log.info("Media updated successfully with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting media with id: {}", id);
        if (!mediaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Media", id);
        }
        mediaRepository.deleteById(id);
        log.info("Media deleted successfully with id: {}", id);
    }

    private MediaResponse toResponse(Media media) {
        return MediaResponse.builder()
                .id(media.getId())
                .title(media.getTitle())
                .originalTitle(media.getOriginalTitle())
                .type(media.getType())
                .genre(media.getGenre())
                .language(media.getLanguage())
                .releaseYear(media.getReleaseYear())
                .synopsis(media.getSynopsis())
                .posterUrl(media.getPosterUrl())
                .createdBy(media.getCreatedBy().getId())
                .createdAt(media.getCreatedAt())
                .updatedAt(media.getUpdatedAt())
                .build();
    }
}