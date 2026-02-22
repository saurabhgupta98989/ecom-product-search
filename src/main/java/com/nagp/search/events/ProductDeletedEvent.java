package com.nagp.search.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDeletedEvent {
    private String id;
}
