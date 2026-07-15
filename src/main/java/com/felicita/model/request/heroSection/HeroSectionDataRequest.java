package com.felicita.model.request.heroSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeroSectionDataRequest {
    private String name;
    private String heroSectionType;
    private String title;
    private String subTitle;
    private String description;
    private String primaryButtonText;
    private String secondaryButtonText;
    private String status;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}
