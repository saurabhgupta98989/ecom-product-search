package com.nagp.search.events;

import com.nagp.search.products.Inventory;
import com.nagp.search.products.Price;
import com.nagp.search.products.ProductImage;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data
@Builder
public class ProductCreatedEvent  {
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
