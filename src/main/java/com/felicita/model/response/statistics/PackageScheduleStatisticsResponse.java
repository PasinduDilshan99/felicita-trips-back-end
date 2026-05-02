package com.felicita.model.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageScheduleStatisticsResponse {

    private Summary summary;
    private List<ScheduleTimeline> scheduleTimelines;
    private List<ScheduleStatusDistribution> scheduleStatusDistributions;
    private List<DurationDistribution> durationDistributions;
    private List<ScheduleParticipationPerformance> scheduleParticipationPerformances;
    private List<ScheduleRatingOverview> scheduleRatingOverviews;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Long totalSchedules;
        private Long activeSchedules;
        private BigDecimal averageScheduleRating;
        private Long totalParticipants;
        private BigDecimal averageDuration;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleTimeline {

        private String timeline;
        private Integer totalSchedules;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DurationDistribution {

        private Long scheduleId;
        private String scheduleName;
        private Integer durationStart;
        private Integer durationEnd;
        private BigDecimal averageDuration;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleParticipationPerformance {

        private Long scheduleId;
        private String scheduleName;
        private Integer totalParticipants;
        private BigDecimal averageParticipants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleRatingOverview {

        private Long scheduleId;
        private String scheduleName;
        private BigDecimal averageRating;
        private Integer totalReviews;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleStatusDistribution {

        private Integer statusId;
        private Integer totalSchedules;
    }
}