package com.felicita.email;

import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.other.ActivitiesComparisonResult;
import com.felicita.model.request.ActivityInsertRequest;
import com.felicita.security.model.User;

public interface ActivityEmailHelperService {
    String buildActivityCreateSuccessfullBody(ActivityInsertRequest activityInsertRequest, Long activityId, User loggedUser);

    String buildActivityCreateSuccessfullSubject(ActivityInsertRequest activityInsertRequest, Long activityId, User loggedUser);

    String buildActivityUpdateSuccessfullSubject(User loggedUser, Long activityId);

    String buildActivityUpdateSuccessfullBody(User loggedUser, Long activityId, ActivitiesComparisonResult comparisonResult);

    String buildActivityTerminateSuccessfullSubject(User loggedUser, ActivityResponseDto activityResponseDto);

    String buildActivityTerminateSuccessfullBody(User loggedUser, ActivityResponseDto activityResponseDto);
}
