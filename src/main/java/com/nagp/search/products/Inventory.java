package com.nagp.search.products;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Inventory {
    private Map<String,Integer> stockBySize;
    private boolean inStock;
    private List<String> availableSizes;
}
