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
public class ActivityScheduleStatisticsResponse {

    private List<ActivityParticipationTrend> participationTrends;
    private List<PopularActivity> popularActivities;
    private List<ActivityRatingOverview> activityRatings;
    private List<ScheduleTimeline> scheduleTimelines;
    private List<ActivityStatusDistribution> statusDistributions;
    private Summary summary;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityParticipationTrend {
        private String activityDate;
        private Integer totalParticipants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PopularActivity {
        private Long activityId;
        private String activityName;
        private Integer totalParticipants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityRatingOverview {
        private Long activityId;
        private String activityName;
        private Double averageRating;
        private Integer totalReviews;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleTimeline {
        private Long scheduleId;
        private String scheduleName;
        private String activityName;
        private String assumeStartDate;
        private String assumeEndDate;
        private Double durationHoursStart;
        private Double durationHoursEnd;
        private String specialNote;
        private Integer status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityStatusDistribution {
        private String statusName;
        private Integer totalCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {
        private Integer totalActivities;
        private Integer totalActiveSchedules;
        private Integer totalParticipants;
        private Double overallAverageRating;
    }

}