package com.felicita.validation;

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

public interface TourValidationService {
    void validateTerminateTourRequest(TourTerminateRequest tourTerminateRequest);

    void validateTourInsertRequest(TourInsertRequest tourInsertRequest);

    void validateTourUpdateRequest(TourUpdateRequest tourUpdateRequest);

    void validateCommonIdRequest(CommonIdRequest commonIdRequest);

    void vaidateTourCategoryUpdateRequest(TourCategoryUpdateRequest tourCategoryUpdateRequest);

    void validateTourCategoryInsertRequest(TourCategoryInsertRequest tourCategoryInsertRequest);

    void validateTourTypeInsertRequest(TourTypeInsertRequest tourTypeInsertRequest);

    void vaidateTourTypeUpdateRequest(TourTypeUpdateRequest tourTypeUpdateRequest);

    void validateTourScheduleDataRequest(TourScheduleDataRequest tourScheduleDataRequest);

    void validateTourScheduleInsertRequest(TourScheduleInsertRequest tourScheduleInsertRequest);

    void validateTourScheduleUpdateRequest(TourScheduleUpdateRequest tourScheduleUpdateRequest);
}
