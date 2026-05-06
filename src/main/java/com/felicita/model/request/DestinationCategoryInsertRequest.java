package com.felicita.model.request;

import com.felicita.model.dto.InsertDestinationCategoryImagesRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DestinationCategoryInsertRequest {

    private String category;
    private String description;
    private String status;
    private String color;
    private String hoverColor;
    private List<InsertDestinationCategoryImagesRequestDto> images;

}