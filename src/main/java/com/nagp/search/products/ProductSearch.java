package com.nagp.search.products;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Builder
@Data
@Document(indexName = "product_search")
public class ProductSearch {

    @Id
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "standard"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Text)
    private List<String> tags;       // party, ethnic, casual, sports

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private List<String> audience;

    @Field(type = FieldType.Keyword)
    private List<String> colors;

    @Field(type = FieldType.Keyword)
    private List<String> availableSizes;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Float)
    private float rating;

    @Field(type = FieldType.Integer)
    private int salesCount;

    @Field(type = FieldType.Boolean)
    private Boolean inStock;

    @Field(type = FieldType.Nested)
    private List<ProductImage> images;

    @Field(type = FieldType.Integer)
    private Integer popularityScore;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Keyword)
    private String slug;
}