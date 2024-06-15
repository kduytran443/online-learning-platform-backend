package com.kduytran.classqueryservice.document;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "books")
@Getter @Setter
public class CategoryDocument {
    @Id
    private String id;
    private String name;
    private String code;
    private String status;
    private String parentCategoryId;
}
