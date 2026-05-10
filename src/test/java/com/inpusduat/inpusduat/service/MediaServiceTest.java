package com.inpusduat.inpusduat.service;

import com.inpusduat.inpusduat.domain.Media;
import com.inpusduat.inpusduat.domain.Role;
import com.inpusduat.inpusduat.domain.User;
import com.inpusduat.inpusduat.dto.media.MediaRequest;
import com.inpusduat.inpusduat.dto.media.MediaResponse;
import com.inpusduat.inpusduat.exception.ResourceNotFoundException;
import com.inpusduat.inpusduat.repository.MediaRepository;
import com.inpusduat.inpusduat.repository.UserRepository;
import com.inpusduat.inpusduat.search.ElasticsearchIndexService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private ElasticsearchIndexService elasticsearchIndexService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MediaService mediaService;

    private User testUser;
    private Media testMedia;
    private MediaRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hash")
                .role(Role.ADMIN)
                .active(true)
                .build();

        testMedia = Media.builder()
                .id(1L)
                .title("Plats bruts")
                .type(Media.MediaType.SERIES)
                .language("Catalan")
                .createdBy(testUser)
                .build();

        testRequest = new MediaRequest();
        testRequest.setTitle("Plats bruts");
        testRequest.setType(Media.MediaType.SERIES);
        testRequest.setLanguage("Catalan");
    }

    @Test
    void findAll_shouldReturnPageOfMedia() {
        Page<Media> page = new PageImpl<>(List.of(testMedia));
        when(mediaRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<MediaResponse> result = mediaService.findAll(PageRequest.of(0, 20));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Plats bruts");
        verify(mediaRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void findById_shouldReturnMedia_whenExists() {
        when(mediaRepository.findById(1L)).thenReturn(Optional.of(testMedia));

        MediaResponse result = mediaService.findById(1L);

        assertThat(result.getTitle()).isEqualTo("Plats bruts");
        assertThat(result.getType()).isEqualTo(Media.MediaType.SERIES);
    }

    @Test
    void findById_shouldThrowException_whenNotExists() {
        when(mediaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mediaService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_shouldSaveAndReturnMedia() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(mediaRepository.save(any(Media.class))).thenReturn(testMedia);

        MediaResponse result = mediaService.create(testRequest, 1L);

        assertThat(result.getTitle()).isEqualTo("Plats bruts");
        verify(mediaRepository, times(1)).save(any(Media.class));
    }

    @Test
    void create_shouldThrowException_whenUserNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mediaService.create(testRequest, 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(mediaRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteMedia_whenExists() {
        when(mediaRepository.existsById(1L)).thenReturn(true);

        mediaService.delete(1L);

        verify(mediaRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenNotExists() {
        when(mediaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> mediaService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(mediaRepository, never()).deleteById(any());
    }
}