package com.felicita.email;

import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.request.DestinationInsertRequest;
import com.felicita.security.model.User;

import java.util.List;

public interface DestinationEmailHelperService {
    String buildDestinationCreateSuccessfullBody(DestinationInsertRequest destinationInsertRequest, List<String> destinationCategories, User loggedUser);

    String buildDestinationCreateSuccessfullSubject(DestinationInsertRequest destinationInsertRequest, User loggedUser);

    String buildDestinationUpdateSuccessfullSubject(User loggedUser);

    String buildDestinationUpdateSuccessfullBody(User loggedUser, Long destinationId, DestinationUpdateComparisonResult comparisonResult);
}
