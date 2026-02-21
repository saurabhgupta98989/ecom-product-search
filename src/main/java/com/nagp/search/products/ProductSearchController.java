package com.nagp.search.products;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/search")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService service;

    @GetMapping
    public Page<ProductSearch> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String audience,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Float minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizePage,
            @RequestParam(defaultValue = "salesCount") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return service.search(
                search, brand, category, audience, color, size,
                minPrice, maxPrice, minRating,
                page, sizePage, sortBy, sortDir
        );
    }

    @PostMapping
    public ProductSearch create(@RequestBody ProductSearch product) {
        return service.create(product);
    }

    @PostMapping("/bulk")
    public void bulkCreate(@RequestBody List<ProductSearch> products) {
        service.bulkCreate(products);
    }

    @PatchMapping("/{id}")
    public void patchUpdate(
            @PathVariable String id,
            @RequestBody Map<String,Object> updates
    ) {
        service.patchUpdate(id, updates);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

}
