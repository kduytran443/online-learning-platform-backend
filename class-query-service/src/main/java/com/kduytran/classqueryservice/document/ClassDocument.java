package com.kduytran.classqueryservice.document;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "books")
@Getter @Setter
public class ClassDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String image;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Keyword)
    private String categoryId;

    @Field(type = FieldType.Keyword)
    private String ownerType;

    @Field(type = FieldType.Keyword)
    private String ownerId;

    @Field(type = FieldType.Text)
    private String ownerName;
}
