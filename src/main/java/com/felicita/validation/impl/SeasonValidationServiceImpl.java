package com.felicita.validation.impl;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.seasons.SeasonUpdateRequest;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.SeasonValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeasonValidationServiceImpl implements SeasonValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeasonValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public SeasonValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateCommonIdRequest(CommonIdRequest seasonTerminateIdRequest) {

    }

    @Override
    public void validateSeasonUpdateRequest(SeasonUpdateRequest seasonUpdateRequest) {

    }
}
