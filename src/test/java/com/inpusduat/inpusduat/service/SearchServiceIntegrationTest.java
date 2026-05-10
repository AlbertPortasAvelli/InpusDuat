package com.inpusduat.inpusduat.service;

import com.inpusduat.inpusduat.dto.media.MediaSearchResponse;
import com.inpusduat.inpusduat.search.ElasticsearchIndexService;
import com.inpusduat.inpusduat.domain.Media;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SearchServiceIntegrationTest {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ElasticsearchIndexService elasticsearchIndexService;

    @BeforeEach
    void setUp() {
        Media media = new Media();
        media.setId(999L);
        media.setTitle("El cor de la ciutat");
        media.setOriginalTitle("El cor de la ciutat");
        media.setLanguage("ca");
        media.setGenre("Drama");
        media.setReleaseYear(2000);
        media.setSynopsis("Sèrie catalana de llarga durada");
        elasticsearchIndexService.indexMedia(media);
    }

    @Test
    void searchByQuery_returnsResults() {
        Page<MediaSearchResponse> results = searchService.search("cor", null, null, null, PageRequest.of(0, 20));
        assertThat(results.getContent()).isNotEmpty();
        assertThat(results.getContent().get(0).getTitle()).contains("cor");
    }

    @Test
    void searchByLanguage_filtersCorrectly() {
        Page<MediaSearchResponse> results = searchService.search(null, null, "ca", null, PageRequest.of(0, 20));
        assertThat(results.getContent()).allMatch(r -> "ca".equals(r.getLanguage()));
    }

    @Test
    void searchByType_filtersCorrectly() {
        Page<MediaSearchResponse> results = searchService.search(null, "SERIES", null, null, PageRequest.of(0, 20));
        results.getContent().forEach(r -> assertThat(r.getType()).isEqualTo("SERIES"));
    }
}