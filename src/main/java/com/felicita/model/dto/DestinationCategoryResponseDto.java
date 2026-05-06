package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationCategoryResponseDto {
    private int categoryId;
    private String category;
    private String categoryDescription;
    private String categoryStatus;
    private String color;
    private String hoverColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DestinationsCategoryImageResponseDto> images;
}
