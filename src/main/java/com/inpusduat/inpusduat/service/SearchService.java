// src/main/java/com/inpusduat/inpusduat/service/SearchService.java
package com.inpusduat.inpusduat.service;

import com.inpusduat.inpusduat.dto.media.MediaSearchResponse;
import com.inpusduat.inpusduat.search.MediaDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<MediaSearchResponse> search(String q, String type, String language, Integer releaseYear) {

        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // Full-text on title, originalTitle, synopsis
        if (q != null && !q.isBlank()) {
            boolQuery.must(Query.of(qb -> qb.multiMatch(mm -> mm
                    .query(q)
                    .fields("title^3", "originalTitle^2", "synopsis")
            )));
        } else {
            boolQuery.must(Query.of(qb -> qb.matchAll(ma -> ma)));
        }

        // Keyword filters
        if (type != null && !type.isBlank()) {
            boolQuery.filter(Query.of(qb -> qb.term(t -> t.field("type").value(type.toUpperCase()))));
        }
        if (language != null && !language.isBlank()) {
            boolQuery.filter(Query.of(qb -> qb.term(t -> t.field("language").value(language))));
        }
        if (releaseYear != null) {
            boolQuery.filter(Query.of(qb -> qb.term(t -> t.field("releaseYear").value(releaseYear))));
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(Query.of(q2 -> q2.bool(boolQuery.build())))
                .withMaxResults(50)
                .build();

        SearchHits<MediaDocument> hits = elasticsearchOperations.search(query, MediaDocument.class);

        return hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(this::toResponse)
                .toList();
    }

    private MediaSearchResponse toResponse(MediaDocument doc) {
        return MediaSearchResponse.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .originalTitle(doc.getOriginalTitle())
                .type(doc.getType())
                .genre(doc.getGenre())
                .language(doc.getLanguage())
                .releaseYear(doc.getReleaseYear())
                .synopsis(doc.getSynopsis())
                .posterUrl(doc.getPosterUrl())
                .build();
    }
}