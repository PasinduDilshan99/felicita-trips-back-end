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
public class DestinationCategoryUpdateRequest {

    private Long categoryId;

    private String category;
    private String description;
    private String status;
    private String color;
    private String hoverColor;

    private List<Long> removeImageIds;

    private List<UpdateImage> updateImages;

    private List<InsertDestinationCategoryImagesRequestDto> newImages;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateImage {

        private Long imageId;
        private String name;
        private String description;
        private String imageUrl;
        private String status;
    }

}