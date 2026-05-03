package com.felicita.email.impl;

import com.felicita.email.TourEmailHelperService;
import com.felicita.model.other.TourComparisonResult;
import com.felicita.model.request.TourInsertRequest;
import com.felicita.model.response.TourAllDetailsResponse;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class TourEmailHelperServiceImpl implements TourEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourEmailHelperServiceImpl.class);

    @Override
    public String buildTourCreateSuccessfullBody(TourInsertRequest tourInsertRequest, Long tourId, User loggedUser) {
        return "";
    }

    @Override
    public String buildTourCreateSuccessfullSubject(TourInsertRequest tourInsertRequest, Long tourId, User loggedUser) {
        return "";
    }

    @Override
    public String buildTourUpdateSuccessfullSubject(User loggedUser, Long tourId) {
        return "";
    }

    @Override
    public String buildTourUpdateSuccessfullBody(User loggedUser, Long tourId, TourComparisonResult comparisonResult) {
        return "";
    }

    @Override
    public String buildTourTerminateSuccessfullSubject(User loggedUser, TourAllDetailsResponse tourDetails) {
        return "";
    }

    @Override
    public String buildTourTerminateSuccessfullBody(User loggedUser, TourAllDetailsResponse tourDetails) {
        return "";
    }
}
