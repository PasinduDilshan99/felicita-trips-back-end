package com.felicita.validation;

import com.felicita.model.request.*;
import com.felicita.model.request.activity.category.ActivityCategoryInsertRequest;
import com.felicita.model.request.activity.category.ActivityCategoryUpdateRequest;
import com.felicita.model.request.activity.schedule.ActivityScheduleUpdateRequest;

public interface ActivityValidationService {
    void validateTerminateActivityRequest(ActivityTerminateRequest activityTerminateRequest);

    void validateActivityInsertRequest(ActivityInsertRequest activityInsertRequest);

    void validateActivityUpdateRequest(ActivityUpdateRequest activityUpdateRequest);

    void validateActivityScheduleInsertRequest(ActivityScheduleInsertRequest activityScheduleInsertRequest);

    void validateActivityScheduleUpdateRequest(ActivityScheduleUpdateRequest activityScheduleUpdateRequest);

    void validateActivityScheduleTerminateRequest(CommonIdRequest commonIdRequest);

    void validateCommonIdRequest(CommonIdRequest commonIdRequest);

    void validateActivityCategoryInsertRequest(ActivityCategoryInsertRequest activityCategoryInsertRequest);

    void validateActivityCategoryUpdateRequest(ActivityCategoryUpdateRequest activityCategoryUpdateRequest);
}
