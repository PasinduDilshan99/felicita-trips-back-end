package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DestinationCategoriesStatisticsResponse {
    private DestinationCategoriesDetails destinationCategoriesDetails;
    private List<CategoryUsedDetails> categoryUsedDetails;
    private List<CategoriesImagesCount> categoriesImagesCounts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DestinationCategoriesDetails{
        private Integer totalDestinationCategoriesCount;
        private Integer activeDestinationsCategories;
        private Integer inActiveDestinationsCategories;
        private Integer terminateDestinationsCategories;
        private Integer recentlyUpdateDestinationsCategories;
        private Integer recentlyAddedDestinationsCategories;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoriesImagesCount{
        private Long categoryId;
        private String categoryName;
        private Integer imagesCount;
        private String color;
        private String hoverColor;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryUsedDetails{
        private Long categoryId;
        private String categoryName;
        private Integer count;
        private String color;
        private String hoverColor;
    }
}
