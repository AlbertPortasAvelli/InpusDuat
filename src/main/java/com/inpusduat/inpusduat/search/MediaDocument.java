// src/main/java/com/inpusduat/inpusduat/search/MediaDocument.java
package com.inpusduat.inpusduat.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "media")
public class MediaDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String originalTitle;

    @Field(type = FieldType.Keyword)
    private String type;        // SERIES, MOVIE

    @Field(type = FieldType.Keyword)
    private String genre;

    @Field(type = FieldType.Keyword)
    private String language;

    @Field(type = FieldType.Integer)
    private Integer releaseYear;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String synopsis;

    @Field(type = FieldType.Keyword)
    private String posterUrl;
}