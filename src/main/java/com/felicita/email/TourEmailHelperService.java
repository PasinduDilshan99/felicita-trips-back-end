package com.felicita.email;

import com.felicita.model.other.TourComparisonResult;
import com.felicita.model.request.TourInsertRequest;
import com.felicita.model.response.TourAllDetailsResponse;
import com.felicita.security.model.User;

public interface TourEmailHelperService {
    String buildTourCreateSuccessfullBody(TourInsertRequest tourInsertRequest, Long tourId, User loggedUser);

    String buildTourCreateSuccessfullSubject(TourInsertRequest tourInsertRequest, Long tourId, User loggedUser);

    String buildTourUpdateSuccessfullSubject(User loggedUser, Long tourId);

    String buildTourUpdateSuccessfullBody(User loggedUser, Long tourId, TourComparisonResult comparisonResult);

    String buildTourTerminateSuccessfullSubject(User loggedUser, TourAllDetailsResponse tourDetails);

    String buildTourTerminateSuccessfullBody(User loggedUser, TourAllDetailsResponse tourDetails);
}
