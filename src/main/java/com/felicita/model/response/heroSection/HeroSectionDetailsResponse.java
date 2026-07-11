package com.felicita.model.response.heroSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeroSectionDetailsResponse {

    private Long id;
    private String name;
    private String imageUrl;
    private String title;
    private String subtitle;
    private String description;

    private String primaryButtonText;
    private String primaryButtonLink;

    private String secondaryButtonText;
    private String secondaryButtonLink;

    private Long statusId;
    private String status;

    private Integer order;

    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByUsername;

    private LocalDateTime updatedAt;
    private Long updatedBy;
    private String updatedByUsername;

    private LocalDateTime terminatedAt;
    private Long terminatedBy;
    private String terminatedByUsername;
}