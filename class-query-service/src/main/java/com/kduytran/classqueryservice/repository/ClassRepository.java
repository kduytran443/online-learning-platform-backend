package com.kduytran.classqueryservice.repository;

import com.kduytran.classqueryservice.document.ClassDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends ElasticsearchRepository<ClassDocument, String> {
    long countByStatus(String status);
}
