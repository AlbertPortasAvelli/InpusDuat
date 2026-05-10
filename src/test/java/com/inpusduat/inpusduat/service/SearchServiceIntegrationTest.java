// src/test/java/com/inpusduat/inpusduat/service/SearchServiceIntegrationTest.java
package com.inpusduat.inpusduat.service;

import com.inpusduat.inpusduat.dto.media.MediaSearchResponse;
import com.inpusduat.inpusduat.search.ElasticsearchIndexService;
import com.inpusduat.inpusduat.domain.Media;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SearchServiceIntegrationTest {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ElasticsearchIndexService elasticsearchIndexService;

    @BeforeEach
    void setUp() {
        // Index a sample document
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
        List<MediaSearchResponse> results = searchService.search("cor", null, null, null);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getTitle()).contains("cor");
    }

    @Test
    void searchByLanguage_filtersCorrectly() {
        List<MediaSearchResponse> results = searchService.search(null, null, "ca", null);
        assertThat(results).allMatch(r -> "ca".equals(r.getLanguage()));
    }

    @Test
    void searchByType_filtersCorrectly() {
        List<MediaSearchResponse> results = searchService.search(null, "SERIES", null, null);
        results.forEach(r -> assertThat(r.getType()).isEqualTo("SERIES"));
    }
}