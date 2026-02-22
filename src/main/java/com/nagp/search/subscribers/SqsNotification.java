package com.nagp.search.subscribers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SqsNotification {

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("MessageId")
    private String messageId;
}
