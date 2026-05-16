package com.felicita.email;

import com.felicita.model.other.TourCategoryComparisonResult;
import com.felicita.model.other.TourComparisonResult;
import com.felicita.model.other.TourScheduleComparisonResult;
import com.felicita.model.other.TourTypeComparisonResult;
import com.felicita.model.request.TourInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryUpdateRequest;
import com.felicita.model.request.tour.schedule.TourScheduleInsertRequest;
import com.felicita.model.request.tour.schedule.TourScheduleUpdateRequest;
import com.felicita.model.request.tour.type.TourTypeInsertRequest;
import com.felicita.model.request.tour.type.TourTypeUpdateRequest;
import com.felicita.model.response.TourAllDetailsResponse;
import com.felicita.model.response.tour.category.TourCategoryBasicDetailsResponse;
import com.felicita.model.response.tour.schedule.TourScheduleBasicDetailsResponse;
import com.felicita.model.response.tour.type.TourTypeBasicDetailsResponse;
import com.felicita.security.model.User;

public interface TourEmailHelperService {
    String buildTourCreateSuccessfullBody(TourInsertRequest tourInsertRequest, Long tourId, User loggedUser);

    String buildTourCreateSuccessfullSubject(TourInsertRequest tourInsertRequest, Long tourId, User loggedUser);

    String buildTourUpdateSuccessfullSubject(User loggedUser, Long tourId);

    String buildTourUpdateSuccessfullBody(User loggedUser, Long tourId, TourComparisonResult comparisonResult);

    String buildTourTerminateSuccessfullSubject(User loggedUser, TourAllDetailsResponse tourDetails);

    String buildTourTerminateSuccessfullBody(User loggedUser, TourAllDetailsResponse tourDetails);

    String buildTourCategoryTerminateSuccessfullSubject(User loggedUser, TourCategoryBasicDetailsResponse tourCategoryResponse);

    String buildTourCategoryTerminateSuccessfullBody(User loggedUser, TourCategoryBasicDetailsResponse tourCategoryResponse);

    String buildTourCategoryCreateSuccessfullBody(Long tourCategoryId, TourCategoryInsertRequest tourCategoryInsertRequest, User loggedUser);

    String buildTourCategoryCreateSuccessfullSubject(Long tourCategoryId, TourCategoryInsertRequest tourCategoryInsertRequest, User loggedUser);

    String buildTourCategoryUpdateSuccessfullSubject(User loggedUser, TourCategoryUpdateRequest tourCategoryUpdateRequest);

    String buildTourCategoryUpdateSuccessfullBody(User loggedUser, TourCategoryUpdateRequest tourCategoryUpdateRequest, TourCategoryComparisonResult comparisonResult);

    String buildTourTypeTerminateSuccessfullSubject(User loggedUser, TourTypeBasicDetailsResponse tourTypeResponse);

    String buildTourTypeTerminateSuccessfullBody(User loggedUser, TourTypeBasicDetailsResponse tourTypeResponse);

    String buildTourTypeCreateSuccessfullBody(Long tourTypeId, TourTypeInsertRequest tourTypeInsertRequest, User loggedUser);

    String buildTourTypeCreateSuccessfullSubject(Long tourTypeId, TourTypeInsertRequest tourTypeInsertRequest, User loggedUser);

    String buildTourTypeUpdateSuccessfullSubject(User loggedUser, TourTypeUpdateRequest tourTypeUpdateRequest);

    String buildTourTypeUpdateSuccessfullBody(User loggedUser, TourTypeUpdateRequest tourTypeUpdateRequest, TourTypeComparisonResult comparisonResult);

    String buildTourScheduleCreateSuccessfullBody(Long tourScheduleId, TourScheduleInsertRequest tourScheduleInsertRequest, User loggedUser);

    String buildTourScheduleCreateSuccessfullSubject(Long tourScheduleId, TourScheduleInsertRequest tourScheduleInsertRequest, User loggedUser);

    String buildTourScheduleUpdateSuccessfullSubject(User loggedUser, TourScheduleUpdateRequest tourScheduleUpdateRequest);

    String buildTourScheduleUpdateSuccessfullBody(User loggedUser, TourScheduleUpdateRequest tourScheduleUpdateRequest, TourScheduleComparisonResult comparisonResult);

    String buildTourScheduleTerminateSuccessfullSubject(User loggedUser, TourScheduleBasicDetailsResponse tourScheduleResponse);

    String buildTourScheduleTerminateSuccessfullBody(User loggedUser, TourScheduleBasicDetailsResponse tourScheduleResponse);
}
