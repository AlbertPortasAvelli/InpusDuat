package com.inpusduat.inpusduat.service;

import com.inpusduat.inpusduat.domain.Media;
import com.inpusduat.inpusduat.domain.User;
import com.inpusduat.inpusduat.domain.UserMedia;
import com.inpusduat.inpusduat.dto.usermedia.UserMediaRequest;
import com.inpusduat.inpusduat.dto.usermedia.UserMediaResponse;
import com.inpusduat.inpusduat.dto.usermedia.UserMediaUpdateRequest;
import com.inpusduat.inpusduat.exception.DuplicateResourceException;
import com.inpusduat.inpusduat.exception.ResourceNotFoundException;
import com.inpusduat.inpusduat.exception.UnauthorizedException;
import com.inpusduat.inpusduat.repository.MediaRepository;
import com.inpusduat.inpusduat.repository.UserMediaRepository;
import com.inpusduat.inpusduat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMediaService {

    private final UserMediaRepository userMediaRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    public UserMediaResponse addMedia(UserMediaRequest request) {
        User user = getCurrentUser();

        if (userMediaRepository.existsByUserIdAndMediaId(user.getId(), request.getMediaId())) {
            throw new DuplicateResourceException("Media already in your list");
        }

        Media media = mediaRepository.findById(request.getMediaId())
            .orElseThrow(() -> new ResourceNotFoundException("Media not found"));

        UserMedia userMedia = UserMedia.builder()
            .user(user)
            .media(media)
            .status(request.getStatus())
            .rating(request.getRating())
            .notes(request.getNotes())
            .watchedAt(request.getStatus() == UserMedia.Status.WATCHED ? LocalDateTime.now() : null)
            .build();

        return toResponse(userMediaRepository.save(userMedia));
    }

    public Page<UserMediaResponse> getMyList(Pageable pageable) {
    User user = getCurrentUser();
    return userMediaRepository.findByUserId(user.getId(), pageable) // ← Page version already in repo
            .map(this::toResponse);
    }

    public UserMediaResponse updateMedia(Long mediaId, UserMediaUpdateRequest request) {
        User user = getCurrentUser();
        UserMedia userMedia = userMediaRepository.findByUserIdAndMediaId(user.getId(), mediaId)
            .orElseThrow(() -> new ResourceNotFoundException("Entry not found in your list"));

        if (request.getStatus() != null) {
            userMedia.setStatus(request.getStatus());
            if (request.getStatus() == UserMedia.Status.WATCHED && userMedia.getWatchedAt() == null) {
                userMedia.setWatchedAt(LocalDateTime.now());
            }
        }
        if (request.getRating() != null) userMedia.setRating(request.getRating());
        if (request.getNotes() != null) userMedia.setNotes(request.getNotes());

        return toResponse(userMediaRepository.save(userMedia));
    }

    public void removeMedia(Long mediaId) {
        User user = getCurrentUser();
        UserMedia userMedia = userMediaRepository.findByUserIdAndMediaId(user.getId(), mediaId)
            .orElseThrow(() -> new ResourceNotFoundException("Entry not found in your list"));
        userMediaRepository.delete(userMedia);
    }

    private UserMediaResponse toResponse(UserMedia um) {
        return UserMediaResponse.builder()
            .id(um.getId())
            .mediaId(um.getMedia().getId())
            .mediaTitle(um.getMedia().getTitle())
            .status(um.getStatus())
            .rating(um.getRating())
            .notes(um.getNotes())
            .watchedAt(um.getWatchedAt())
            .build();
    }
}