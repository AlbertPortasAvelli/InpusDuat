// src/main/java/com/inpusduat/inpusduat/search/MediaSearchRepository.java
package com.inpusduat.inpusduat.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaSearchRepository extends ElasticsearchRepository<MediaDocument, Long> {
    // Basic CRUD + findAll inherited.
    // Full-text queries handled in SearchService via ElasticsearchOperations.
}