package com.felicita.validation.impl;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.TourInsertRequest;
import com.felicita.model.request.TourUpdateRequest;
import com.felicita.model.request.tour.category.TourCategoryInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryUpdateRequest;
import com.felicita.model.request.tour.schedule.TourScheduleDataRequest;
import com.felicita.model.request.tour.schedule.TourScheduleInsertRequest;
import com.felicita.model.request.tour.schedule.TourScheduleUpdateRequest;
import com.felicita.model.request.tour.type.TourTypeInsertRequest;
import com.felicita.model.request.tour.type.TourTypeUpdateRequest;
import com.felicita.model.response.TourTerminateRequest;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.TourValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourValidationServiceImpl implements TourValidationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TourValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public TourValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateTerminateTourRequest(TourTerminateRequest tourTerminateRequest) {

    }

    @Override
    public void validateTourInsertRequest(TourInsertRequest tourInsertRequest) {

    }

    @Override
    public void validateTourUpdateRequest(TourUpdateRequest tourUpdateRequest) {

    }

    @Override
    public void validateCommonIdRequest(CommonIdRequest commonIdRequest) {

    }

    @Override
    public void vaidateTourCategoryUpdateRequest(TourCategoryUpdateRequest tourCategoryUpdateRequest) {

    }

    @Override
    public void validateTourCategoryInsertRequest(TourCategoryInsertRequest tourCategoryInsertRequest) {

    }

    @Override
    public void validateTourTypeInsertRequest(TourTypeInsertRequest tourTypeInsertRequest) {

    }

    @Override
    public void vaidateTourTypeUpdateRequest(TourTypeUpdateRequest tourTypeUpdateRequest) {

    }

    @Override
    public void validateTourScheduleDataRequest(TourScheduleDataRequest tourScheduleDataRequest) {

    }

    @Override
    public void validateTourScheduleInsertRequest(TourScheduleInsertRequest tourScheduleInsertRequest) {

    }

    @Override
    public void validateTourScheduleUpdateRequest(TourScheduleUpdateRequest tourScheduleUpdateRequest) {

    }
}
