package com.felicita.validation;

import com.felicita.model.request.common.IdWithTypeRequest;
import com.felicita.model.request.heroSection.HeroSectionInsertRequest;
import com.felicita.model.request.heroSection.HeroSectionTypeRequest;
import com.felicita.model.request.heroSection.HeroSectionUpdateRequest;

public interface HeroSectionValidationService {
    void validateHeroSectionInsertRequest(HeroSectionInsertRequest heroSectionInsertRequest);

    void validateHeroSectionUpdateRequest(HeroSectionUpdateRequest heroSectionUpdateRequest);

    void validateIdWithTypeRequest(IdWithTypeRequest idWithTypeRequest);

    void validateHeroSectionTypeRequest(HeroSectionTypeRequest heroSectionTypeRequest);
}
