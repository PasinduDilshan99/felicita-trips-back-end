package com.felicita.model.request.seasons;

import lombok.Data;

@Data
public class SeasonImageInsertRequest {
    private String name;
    private String description;
    private String imageUrl;
    private String status;
}