package com.kduytran.classqueryservice.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.util.ObjectBuilder;
import com.kduytran.classqueryservice.constant.IndexConstant;
import com.kduytran.classqueryservice.converter.ClassConverter;
import com.kduytran.classqueryservice.document.ClassDocument;
import com.kduytran.classqueryservice.dto.ClassDTO;
import com.kduytran.classqueryservice.dto.PaginationResponseDTO;
import com.kduytran.classqueryservice.dto.SearchRequestDTO;
import com.kduytran.classqueryservice.exception.ResourceNotFoundException;
import com.kduytran.classqueryservice.repository.ClassRepository;
import com.kduytran.classqueryservice.service.IClassService;
import jakarta.json.JsonException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClassServiceImpl implements IClassService {

    private final ClassRepository classRepository;
    private final ModelMapper modelMapper;
    private final ElasticsearchClient esClient;

    /**
     * Creates a new class based on the provided DTO.
     *
     * @param dto The DTO containing class information.
     * @return The UUID of the newly created class.
     */
    @Override
    public UUID create(ClassDTO dto) {
        ClassDocument document = classRepository.save(modelMapper.map(dto, ClassDocument.class));
        return UUID.fromString(document.getId());
    }

    @Override
    public void createBulk(ClassDTO[] dtos) {
        Iterable<ClassDocument> documentIterable = Arrays.stream(dtos)
                .map(dto -> modelMapper.map(dto, ClassDocument.class)).collect(Collectors.toList());
        classRepository.saveAll(documentIterable);
    }

    /**
     * Updates an existing class based on the provided DTO.
     *
     * @param dto The DTO containing updated class information.
     */
    @Override
    public void update(String id, ClassDTO dto) {
        ClassDocument document = classRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        document = ClassConverter.convert(dto, document);
        classRepository.save(document);
    }

    /**
     * Deletes a class based on the provided DTO.
     *
     * @param id The string class id to be deleted.
     */
    @Override
    public void delete(String id) {
        ClassDocument document = classRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        document.setStatus("D");
        classRepository.save(document);
    }



    /**
     * Searches for classes based on the provided search request DTO and returns a paginated response.
     *
     * @param requestDTO The DTO containing search criteria and pagination details.
     * @return A {@link PaginationResponseDTO} containing a list of {@link ClassDTO} objects.
     */
    @Override
    public PaginationResponseDTO<ClassDTO> search(SearchRequestDTO requestDTO) {
        long allLiveItems = classRepository.countByStatus("L");

        final int from = (requestDTO.getPage() - 1) <= 0 ? 0 : (requestDTO.getPage() - 1) * requestDTO.getSize();
        int totalPages = (int) Math.ceil((double) allLiveItems / requestDTO.getSize());

        Query byStatus = MatchQuery.of(
                m -> m.field("status")
                        .query("L")
        )._toQuery();

        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder()
                .index(IndexConstant.CLASSES_INDEX)
                .query(q -> q
                        .bool(b -> {
                            // Status must be 'L'
                            b.must(byStatus);

                            // Handle "multiMatchQuery" if field size is larger than 0
                            if (requestDTO.getFields() != null && requestDTO.getFields().size() > 0) {
                                MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(
                                        builder -> builder
                                                .fields(requestDTO.getFields())
                                                .operator(Operator.And)
                                                .query(requestDTO.getSearchTerm())
                                );
                                b.must(mq -> mq.multiMatch(multiMatchQuery));
                            }
                            return b;
                        })
                )
                .sort(so -> {
                    if (!"averageRating".equals(requestDTO.getSortBy())) {
                        so.field(FieldSort.of(
                                f -> f.field(requestDTO.getSortBy()).order(requestDTO.getSortOrder())
                        ));
                    }
                    // Sort by averageRating
                    so.field(FieldSort.of(
                            f -> f.field("averageRating").order(requestDTO.getSortOrder())
                    ));
                    so.field(FieldSort.of(
                            f -> f.field("numberOfReviews").order(requestDTO.getSortOrder())
                    ));
                    return so;
                })
                .from(from)
                .size(requestDTO.getSize());

        if (requestDTO.hasSourceIncludes()) {
            searchRequestBuilder.source(src -> src
                    .filter(f -> f
                            .includes(requestDTO.getSourceIncludes())
                    )
            );
        } else if (requestDTO.hasSourceExcludes()) {
            searchRequestBuilder.source(src -> src
                    .filter(f -> f
                            .excludes(requestDTO.getSourceExcludes())
                    )
            );
        } else if (requestDTO.hasSourceIncludesAndExcludes()) {
            searchRequestBuilder.source(src -> src
                    .filter(f -> f
                            .includes(requestDTO.getSourceIncludes())
                            .excludes(requestDTO.getSourceExcludes())
                    )
            );
        }

        SearchResponse<ClassDocument> response;
        try {
            response = esClient.search(
                    searchRequestBuilder.build(),
                    ClassDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Hit<ClassDocument>> hits = response.hits().hits();
        List<ClassDTO> classDTOS = ClassConverter.convert(hits.stream().map(Hit::source).collect(Collectors.toList()));

        return new PaginationResponseDTO<>(
            requestDTO.getPage(),
                requestDTO.getSize(),
                allLiveItems,
                totalPages,
                classDTOS
        );
    }

    @Override
    public List<ClassDTO> getAllLiveStatus() {
        return ClassConverter.convert(classRepository.findAll());
    }

}
