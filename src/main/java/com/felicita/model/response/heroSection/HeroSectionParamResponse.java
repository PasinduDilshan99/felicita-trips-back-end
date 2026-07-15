package com.felicita.model.response.heroSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeroSectionParamResponse {
    private Integer count;
    private List<HeroSectionBasicResponse> heroSectionBasicResponses;
}
