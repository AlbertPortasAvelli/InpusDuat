// src/main/java/com/inpusduat/inpusduat/search/ElasticsearchIndexService.java
package com.inpusduat.inpusduat.search;

import com.inpusduat.inpusduat.domain.Media;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchIndexService {

    private final MediaSearchRepository mediaSearchRepository;

    public void indexMedia(Media media) {
        MediaDocument doc = toDocument(media);
        mediaSearchRepository.save(doc);
        log.info("Indexed media id={} title={}", media.getId(), media.getTitle());
    }

    public void deleteMedia(Long mediaId) {
        mediaSearchRepository.deleteById(mediaId);
        log.info("Deleted media from index id={}", mediaId);
    }

    private MediaDocument toDocument(Media media) {
        return MediaDocument.builder()
                .id(media.getId())
                .title(media.getTitle())
                .originalTitle(media.getOriginalTitle())
                .type(media.getType() != null ? media.getType().name() : null)
                .genre(media.getGenre())
                .language(media.getLanguage())
                .releaseYear(media.getReleaseYear())
                .synopsis(media.getSynopsis())
                .posterUrl(media.getPosterUrl())
                .build();
    }
}