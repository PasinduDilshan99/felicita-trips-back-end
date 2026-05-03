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
public class ActivityStatisticsResponse {

    private ActivityDetails activityDetails;
    private WishDetails wishDetails;
    private List<CategoryDetails> categoryDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityDetails{
        private Integer totalActivitiesCount;
        private Integer activeActivities;
        private Integer inActiveActivities;
        private Integer hiddenActivities;
        private Integer recentlyUpdateActivities;
        private Integer recentlyAddedActivities;
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
