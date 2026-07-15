package com.felicita.model.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeroSectionStatisticsResponse {

    private Summary summary;
    private List<StatusStatistics> statusStatistics;
    private List<MonthlyStatistics> monthlyStatistics;
    private List<ActivityStatistics> activityStatistics;
    private List<TopEditorStatistics> topEditorStatistics;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Integer totalHeroSections;
        private Integer activeHeroSections;
        private Integer inactiveHeroSections;
        private Integer terminatedHeroSections;
        private Integer createdThisMonth;
        private Integer updatedThisMonth;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StatusStatistics {

        private Long statusId;
        private String status;
        private Integer count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MonthlyStatistics {

        private Integer year;
        private Integer month;
        private String monthName;
        private Integer count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityStatistics {

        private Integer year;
        private Integer month;
        private String monthName;
        private Integer createdCount;
        private Integer updatedCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TopEditorStatistics {

        private Long userId;
        private String username;
        private Integer updateCount;
    }
}