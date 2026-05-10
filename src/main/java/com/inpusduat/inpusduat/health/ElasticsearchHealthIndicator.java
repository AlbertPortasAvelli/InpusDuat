package com.inpusduat.inpusduat.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticsearchHealthIndicator implements HealthIndicator {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Health health() {
        try {
            boolean exists = elasticsearchTemplate.indexOps(
                elasticsearchTemplate.getIndexCoordinatesFor(
                    com.inpusduat.inpusduat.search.MediaDocument.class
                )
            ).exists();
            return Health.up().withDetail("index", "media").withDetail("exists", exists).build();
        } catch (Exception e) {
            return Health.down().withDetail("error", e.getMessage()).build();
        }
    }
}