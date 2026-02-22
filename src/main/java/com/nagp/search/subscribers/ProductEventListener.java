package com.nagp.search.subscribers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagp.search.events.EventEnvelope;
import com.nagp.search.events.ProductCreatedEvent;
import com.nagp.search.products.Inventory;
import com.nagp.search.products.Price;
import com.nagp.search.products.ProductSearch;
import com.nagp.search.products.ProductSearchService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEventListener {

    private final ObjectMapper mapper;
    private final ProductSearchService searchService;

    @SqsListener("ecom-product-search-service-queue")
    public void listen(String message) {
        try {
            SqsNotification notification =
                    mapper.readValue(message, SqsNotification.class);
            EventEnvelope<ProductCreatedEvent> event =
                    mapper.readValue(notification.getMessage(),
                            new TypeReference<EventEnvelope<ProductCreatedEvent>>() {});
            if ("PRODUCT_CREATED".equals(event.getEventType())) {
                ProductCreatedEvent product= event.getData();
                Inventory inventory= product.getInventory();
                Price price=product.getPrice();
                ProductSearch productSearch=ProductSearch.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .tags((product.getTags()))
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .audience(product.getAudience())
                        .colors(product.getColors())
                        .images(product.getImages())
                        .price(price.getDiscountPrice())
                        .availableSizes(inventory.getAvailableSizes())
                        .inStock(inventory.isInStock())
                        .build();
                searchService.create(productSearch);
            }

        } catch (Exception e) {
            log.error("Failed to process message", e);
            throw new RuntimeException(e);
        }
    }
}
