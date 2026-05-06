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
public class TourScheduleStatisticsResponse {

    private Summary summary;
    private List<ScheduleTimeline> scheduleTimeline;
    private List<DurationDistribution> durationDistribution;
    private List<ScheduleExecutionPerformance> executionPerformance;
    private List<ScheduleRatingOverview> ratingOverview;
    private List<ParticipationTrend> participationTrend;

    // =========================
    // 1. SUMMARY KPI
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Integer totalSchedules;
        private Integer completedSchedules;
        private Double averageRating;
        private Double utilizationRate;
    }

    // =========================
    // 2. SCHEDULE TIMELINE
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleTimeline {

        private String scheduleDate;
        private Integer totalSchedules;
    }

    // =========================
    // 3. DURATION DISTRIBUTION
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DurationDistribution {

        private Integer durationStart;
        private Integer durationEnd;
        private Integer totalSchedules;
    }

    // =========================
    // 4. EXECUTION PERFORMANCE
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleExecutionPerformance {

        private Long scheduleId;
        private String scheduleName;
        private Integer completedInstances;
    }

    // =========================
    // 5. RATING OVERVIEW
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleRatingOverview {

        private Long scheduleId;
        private String scheduleName;
        private Double averageRating;
        private Integer totalReviews;
    }

    // =========================
    // 6. PARTICIPATION TREND
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ParticipationTrend {

        private Long scheduleId;
        private String scheduleName;
        private Integer totalParticipants;
    }
}