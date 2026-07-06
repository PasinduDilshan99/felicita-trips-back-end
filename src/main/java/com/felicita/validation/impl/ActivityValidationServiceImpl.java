package com.felicita.validation.impl;

import com.felicita.model.request.*;
import com.felicita.model.request.activity.category.ActivityCategoryInsertRequest;
import com.felicita.model.request.activity.category.ActivityCategoryUpdateRequest;
import com.felicita.model.request.activity.schedule.ActivityScheduleUpdateRequest;
import com.felicita.validation.ActivityValidationService;
import com.felicita.validation.CommonValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityValidationServiceImpl implements ActivityValidationService {

    private static final Logger LOGGEr = LoggerFactory.getLogger(ActivityValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public ActivityValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateTerminateActivityRequest(ActivityTerminateRequest activityTerminateRequest) {

    }

    @Override
    public void validateActivityInsertRequest(ActivityInsertRequest activityInsertRequest) {

    }

    @Override
    public void validateActivityUpdateRequest(ActivityUpdateRequest activityUpdateRequest) {

    }

    @Override
    public void validateActivityScheduleInsertRequest(ActivityScheduleInsertRequest activityScheduleInsertRequest) {

    }

    @Override
    public void validateActivityScheduleUpdateRequest(ActivityScheduleUpdateRequest activityScheduleUpdateRequest) {

    }

    @Override
    public void validateActivityScheduleTerminateRequest(CommonIdRequest commonIdRequest) {

    }

    @Override
    public void validateCommonIdRequest(CommonIdRequest commonIdRequest) {

    }

    @Override
    public void validateActivityCategoryInsertRequest(ActivityCategoryInsertRequest activityCategoryInsertRequest) {

    }

    @Override
    public void validateActivityCategoryUpdateRequest(ActivityCategoryUpdateRequest activityCategoryUpdateRequest) {

    }
}
