package com.felicita.model.request;

import lombok.Data;

@Data
public class TrendingDestinationTerminateRequest {
    private Long destinationId;
    private String destinationName;
    private String status;
}
