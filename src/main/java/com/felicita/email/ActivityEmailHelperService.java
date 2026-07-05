package com.felicita.email;

import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.dto.activity.schedule.ActivityScheduleBasicDetailsDTO;
import com.felicita.model.other.ActivitiesCategoryComparisonResult;
import com.felicita.model.other.ActivitiesComparisonResult;
import com.felicita.model.other.ActivitiesScheduleComparisonResult;
import com.felicita.model.request.ActivityInsertRequest;
import com.felicita.model.request.ActivityScheduleInsertRequest;
import com.felicita.model.request.activity.category.ActivityCategoryInsertRequest;
import com.felicita.model.request.activity.category.ActivityCategoryUpdateRequest;
import com.felicita.model.request.activity.schedule.ActivityScheduleUpdateRequest;
import com.felicita.model.response.activity.category.ActivityCategoryDetailsResponse;
import com.felicita.security.model.User;

public interface ActivityEmailHelperService {
    String buildActivityCreateSuccessfullBody(ActivityInsertRequest activityInsertRequest, Long activityId, User loggedUser);

    String buildActivityCreateSuccessfullSubject(ActivityInsertRequest activityInsertRequest, Long activityId, User loggedUser);

    String buildActivityUpdateSuccessfullSubject(User loggedUser, Long activityId);

    String buildActivityUpdateSuccessfullBody(User loggedUser, Long activityId, ActivitiesComparisonResult comparisonResult);

    String buildActivityTerminateSuccessfullSubject(User loggedUser, ActivityResponseDto activityResponseDto);

    String buildActivityTerminateSuccessfullBody(User loggedUser, ActivityResponseDto activityResponseDto);

    String buildActivityScheduleCreateSuccessfullBody(Long activityScheduleId, ActivityScheduleInsertRequest activityScheduleInsertRequest, User loggedUser);

    String buildActivityScheduleCreateSuccessfullSubject(Long activityScheduleId, ActivityScheduleInsertRequest activityScheduleInsertRequest, User loggedUser);

    String buildActivityScheduleUpdateSuccessfullSubject(User loggedUser, ActivityScheduleUpdateRequest activityScheduleUpdateRequest);

    String buildActivityScheduleUpdateSuccessfullBody(User loggedUser, ActivityScheduleUpdateRequest activityScheduleUpdateRequest, ActivitiesScheduleComparisonResult comparisonResult);

    String buildActivityScheduleTerminateSuccessfullSubject(User loggedUser, ActivityScheduleBasicDetailsDTO activityScheduleResponse);

    String buildActivityScheduleTerminateSuccessfullBody(User loggedUser, ActivityScheduleBasicDetailsDTO activityScheduleResponse);

    String buildActivityCategoryTerminateSuccessfullSubject(User loggedUser, ActivityCategoryDetailsResponse activityCategoryResponse);

    String buildActivityCategoryTerminateSuccessfullBody(User loggedUser, ActivityCategoryDetailsResponse activityCategoryResponse);

    String buildActivityCategoryCreateSuccessfullBody(Long activityCategoryId, ActivityCategoryInsertRequest activityCategoryInsertRequest, User loggedUser);

    String buildActivityCategoryCreateSuccessfullSubject(Long activityCategoryId, ActivityCategoryInsertRequest activityCategoryInsertRequest, User loggedUser);

    String buildActivityCategoryUpdateSuccessfullSubject(User loggedUser, ActivityCategoryUpdateRequest activityCategoryUpdateRequest);

    String buildActivityCategoryUpdateSuccessfullBody(User loggedUser, ActivityCategoryUpdateRequest activityCategoryUpdateRequest, ActivitiesCategoryComparisonResult comparisonResult);
}
