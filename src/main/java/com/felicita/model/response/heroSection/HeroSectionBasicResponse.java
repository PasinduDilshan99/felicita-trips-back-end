package com.felicita.model.response.heroSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeroSectionBasicResponse {
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
}
