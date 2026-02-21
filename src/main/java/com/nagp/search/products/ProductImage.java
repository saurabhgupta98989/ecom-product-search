package com.nagp.search.products;

import lombok.Data;

@Data
public class ProductImage {
    private String url;
    private String type;
    private Integer order;
    private Boolean primary;
}
