package com.felicita.validation.impl;

import com.felicita.model.request.*;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.DestinationValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DestinationValidationServiceImpl implements DestinationValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public DestinationValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateDestinationInsertRequest(DestinationInsertRequest destinationInsertRequest) {

    }

    @Override
    public void validateTerminateDestinationRequest(DestinationTerminateRequest destinationTerminateRequest) {

    }

    @Override
    public void validateDestinationUpdateRequest(DestinationUpdateRequest destinationUpdateRequest) {

    }

    @Override
    public void validateDestinationCategoryInsertRequest(DestinationCategoryInsertRequest destinationCategoryInsertRequest) {

    }

    @Override
    public void validateDestinationCategoryUpdateRequest(DestinationCategoryUpdateRequest destinationCategoryUpdateRequest) {

    }

    @Override
    public void validateDestinationCategoryTerminateRequest(DestinationCategoryTerminateRequest destinationCategoryTerminateRequest) {

    }
}
