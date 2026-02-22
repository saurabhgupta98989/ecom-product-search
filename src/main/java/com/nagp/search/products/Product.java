package com.nagp.search.products;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class Product {

    private String id;
    private String name;
    private String description;
    private List<String> tags;
    private String brand;
    private String category;
    private List<String> audience;
    private List<String> colors;
    private List<String> sizes;
    private List<ProductImage> images;
    private Price price;
    private Inventory inventory;
    private Instant createdAt = Instant.now();
}
