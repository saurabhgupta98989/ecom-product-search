package com.nagp.search.events;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ProductUpdatedEvent {
    private String id;
    private Map<String,Object> updatedFields;
}
