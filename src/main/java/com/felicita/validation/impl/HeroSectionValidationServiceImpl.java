package com.felicita.validation.impl;

import com.felicita.model.request.common.IdWithTypeRequest;
import com.felicita.model.request.heroSection.HeroSectionInsertRequest;
import com.felicita.model.request.heroSection.HeroSectionTypeRequest;
import com.felicita.model.request.heroSection.HeroSectionUpdateRequest;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.HeroSectionValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeroSectionValidationServiceImpl implements HeroSectionValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeroSectionValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public HeroSectionValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateHeroSectionInsertRequest(HeroSectionInsertRequest heroSectionInsertRequest) {

    }

    @Override
    public void validateHeroSectionUpdateRequest(HeroSectionUpdateRequest heroSectionUpdateRequest) {

    }

    @Override
    public void validateIdWithTypeRequest(IdWithTypeRequest idWithTypeRequest) {

    }

    @Override
    public void validateHeroSectionTypeRequest(HeroSectionTypeRequest heroSectionTypeRequest) {

    }
}
