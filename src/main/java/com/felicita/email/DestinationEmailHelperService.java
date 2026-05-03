package com.felicita.email;

import com.felicita.model.dto.DestinationResponseDto;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.request.DestinationInsertRequest;
import com.felicita.model.request.TrendingDestinationInsertRequest;
import com.felicita.model.request.TrendingDestinationTerminateRequest;
import com.felicita.security.model.User;

import java.util.List;

public interface DestinationEmailHelperService {
    String buildDestinationCreateSuccessfullBody(DestinationInsertRequest destinationInsertRequest, List<String> destinationCategories, User loggedUser);

    String buildDestinationCreateSuccessfullSubject(DestinationInsertRequest destinationInsertRequest, User loggedUser);

    String buildDestinationUpdateSuccessfullSubject(User loggedUser);

    String buildDestinationUpdateSuccessfullBody(User loggedUser, Long destinationId, DestinationUpdateComparisonResult comparisonResult);

    String buildDestinationTerminateSuccessfullSubject(User loggedUser, DestinationResponseDto destinationDetailsById);

    String buildDestinationTerminateSuccessfullBody(User loggedUser, DestinationResponseDto destinationDetailsById);

    String buildTrendingDestinationCreateSuccessfullBody(TrendingDestinationInsertRequest trendingDestinationInsertRequest, User loggedUser);

    String buildTrendingDestinationCreateSuccessfullSubject(TrendingDestinationInsertRequest trendingDestinationInsertRequest, User loggedUser);

    String buildTrendingDestinationTerminateSuccessfullSubject(User loggedUser, TrendingDestinationTerminateRequest trendingDestinationTerminateRequest);

    String buildTrendingDestinationTerminateSuccessfullBody(User loggedUser, TrendingDestinationTerminateRequest trendingDestinationTerminateRequest);
}
