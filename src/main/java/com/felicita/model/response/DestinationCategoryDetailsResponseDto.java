package com.felicita.model.response;

import com.felicita.model.dto.CategoryDestinationResponseDto;
import com.felicita.model.dto.DestinationsCategoryImageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DestinationCategoryDetailsResponseDto {
    private int categoryId;
    private String category;
    private String categoryDescription;
    private String categoryStatus;
    private String color;
    private String hoverColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DestinationsCategoryImageResponseDto> images;

    private List<CategoryDestinationResponseDto> destinations;
}
