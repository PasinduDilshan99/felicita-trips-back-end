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
public class SeasonStatisticsResponse {

    private List<SeasonActivityCount> seasonActivityCounts;

    private List<SeasonTourCount> seasonTourCounts;

    private List<SeasonPopularity> seasonPopularities;

    private List<PeakSeasonDistribution> peakSeasonDistributions;

    private List<SeasonWeatherOverview> seasonWeatherOverviews;

    private Summary summary;

    // 1. Season Wise Activity Count
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SeasonActivityCount {

        private Long seasonId;
        private String seasonName;
        private Integer totalActivities;
    }

    // 2. Season Wise Tour Count
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SeasonTourCount {

        private Long seasonId;
        private String seasonName;
        private Integer totalTours;
    }

    // 3. Season Popularity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SeasonPopularity {

        private Long seasonId;
        private String seasonName;
        private Integer totalActivities;
        private Integer totalTours;
        private Integer totalUsage;
    }

    // 4. Peak vs Non-Peak Distribution
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PeakSeasonDistribution {

        private String seasonName;
        private Boolean isPeak;
        private Integer activityCount;
        private Integer tourCount;
    }

    // 5. Weather Overview
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SeasonWeatherOverview {

        private Long seasonId;
        private String seasonName;
        private Integer temperatureMin;
        private Integer temperatureMax;
        private String rainfallPattern;
        private String weatherSummary;
    }

    // Summary KPIs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Integer totalSeasons;
        private Integer totalActivities;
        private Integer totalTours;
        private String mostUsedSeason;
        private String peakSeason;
    }
}