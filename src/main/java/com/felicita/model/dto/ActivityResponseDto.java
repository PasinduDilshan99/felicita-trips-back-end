package com.felicita.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponseDto {
    private Long id;

    @JsonProperty("destination_id")
    private Integer destinationId;

    private String destinationName;
    private String name;
    private String description;

    @JsonProperty("activities_category")
    private List<ActivityCategoryDto> activityCategoryDtos;

    @JsonProperty("duration_hours")
    private BigDecimal durationHours;

    @JsonProperty("available_from")
    private Time availableFrom;

    @JsonProperty("available_to")
    private Time availableTo;

    @JsonProperty("price_local")
    private BigDecimal priceLocal;

    @JsonProperty("price_foreigners")
    private BigDecimal priceForeigners;

    @JsonProperty("min_participate")
    private Integer minParticipate;

    @JsonProperty("max_participate")
    private Integer maxParticipate;

    private Long seasonId;
    private String season;
    private String status;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    // Related data
    private List<ActivityScheduleDto> schedules;
    private List<ActivityRequirementDto> requirements;
    private List<ActivityImageDto> images;

    private boolean wish = false;
}
