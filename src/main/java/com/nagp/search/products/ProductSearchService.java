package com.nagp.search.products;

import java.util.List;
import java.util.Map;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchOperations operations;
    private final ProductSearchRepository repository;

    public Page<ProductSearch> search(
            String text,
            String brand,
            String category,
            String audience,
            String color,
            String size,
            Double minPrice,
            Double maxPrice,
            Float minRating,
            int page,
            int sizePage,
            String sortBy,
            String sortDir
    ) {

        Pageable pageable = PageRequest.of(page, sizePage);
        BoolQuery.Builder bool = new BoolQuery.Builder();
        if (text != null && !text.isBlank()) {
            bool.must(m -> m.multiMatch(mm -> mm
                    .query(text)
                            .fields("name^5","description","tags")
                            .fuzziness("AUTO")
                            .operator(Operator.And)
            ));
        } else {
            bool.must(m -> m.matchAll(ma -> ma));
        }

        // ---------------- FILTERS ----------------
        addTermFilter(bool, "audience", audience);
        addTermFilter(bool, "brand", brand);
        addTermFilter(bool, "category", category);
        addTermFilter(bool, "colors", color);
        addTermFilter(bool, "availableSizes", size);
        //  addTermFilter(bool, "tags", tag);

        // ---------------- PRICE RANGE ----------------
        if (minPrice != null || maxPrice != null) {
            bool.filter(f -> f.range(r -> r
                    .number(n -> {
                        n.field("price");
                        if (minPrice != null) n.gte(minPrice);
                        if (maxPrice != null) n.lte(maxPrice);
                        return n;
                    })
            ));
        }

        // ---------------- RATING FILTER ----------------
        if (minRating != null) {
            bool.filter(f -> f.range(r -> r
                    .number(n -> n
                            .field("rating")
                            .gte(minRating.doubleValue())
                    )));
        }

        // ---------------- SORT ----------------

        List<String> allowedSortFields =
                List.of("price","rating","salesCount","createdAt","popularityScore");
        if (!allowedSortFields.contains(sortBy))
            sortBy = "salesCount";
        Sort sort = Sort.by(
                "asc".equalsIgnoreCase(sortDir)
                        ? Sort.Order.asc(sortBy)
                        : Sort.Order.desc(sortBy)
        );

        // ---------------- QUERY BUILD ----------------

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(bool.build()))
                .withPageable(pageable)
                .withSort(sort)
                .build();

        SearchHits<ProductSearch> hits =
                operations.search(query, ProductSearch.class);

        List<ProductSearch> result =
                hits.stream().map(SearchHit::getContent).toList();

        return new PageImpl<>(result, pageable, hits.getTotalHits());
    }


    public ProductSearch create(ProductSearch product) {
        return repository.save(product);
    }

    public void bulkCreate(List<ProductSearch> products) {
        repository.saveAll(products);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public void patchUpdate(String id, Map<String,Object> updates) {
        Document document = Document.from(updates);
        UpdateQuery query = UpdateQuery.builder(id)
                .withDocument(document)
                .build();
        operations.update(query, IndexCoordinates.of("product_search"));
    }

    private void addTermFilter(BoolQuery.Builder bool, String field, String value) {
        if (value != null && !value.isBlank()) {
            bool.filter(f -> f.term(t -> t
                    .field(field)
                    .value(value.toLowerCase())));
        }
    }
}
