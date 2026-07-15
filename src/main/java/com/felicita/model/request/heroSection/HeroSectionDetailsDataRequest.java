package com.felicita.model.request.heroSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeroSectionDetailsDataRequest {
    private String heroSectionType;
    private Long heroSectionId;
}
