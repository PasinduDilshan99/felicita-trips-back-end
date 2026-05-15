package com.felicita.validation;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.TourInsertRequest;
import com.felicita.model.request.TourUpdateRequest;
import com.felicita.model.request.tour.category.TourCategoryInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryUpdateRequest;
import com.felicita.model.response.TourTerminateRequest;

public interface TourValidationService {
    void validateTerminateTourRequest(TourTerminateRequest tourTerminateRequest);

    void validateTourInsertRequest(TourInsertRequest tourInsertRequest);

    void validateTourUpdateRequest(TourUpdateRequest tourUpdateRequest);

    void validateCommonIdRequest(CommonIdRequest commonIdRequest);

    void vaidateTourCategoryUpdateRequest(TourCategoryUpdateRequest tourCategoryUpdateRequest);

    void validateTourCategoryInsertRequest(TourCategoryInsertRequest tourCategoryInsertRequest);
}
