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
public class DestinationStatisticsResponse {

    private DestinationDetails destinationDetails;
    private WishDetails wishDetails;
    private List<CategoryDetails> categoryDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DestinationDetails{
        private Integer totalDestinationCount;
        private Integer activeDestinations;
        private Integer inActiveDestinations;
        private Integer hiddenDestinations;
        private Integer recentlyUpdateDestinations;
        private Integer recentlyAddedDestinations;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WishDetails{
        private Integer wishListCount;
        private Integer notWishListCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryDetails{
        private Long categoryId;
        private String categoryName;
        private Integer count;
    }
}
