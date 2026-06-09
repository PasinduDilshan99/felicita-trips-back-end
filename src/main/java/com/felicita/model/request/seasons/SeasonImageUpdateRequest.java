package com.felicita.model.request.seasons;

import lombok.Data;

@Data
public class SeasonImageUpdateRequest {

    private Integer id;

    private Integer seasonId;

    private String name;
    private String description;
    private String imageUrl;

    private Integer status;
}