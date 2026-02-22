package com.nagp.search.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventEnvelope<T> {
    private String eventId;
    private String eventType;
    private String source;
    private long timestamp;
    private T data;
}
