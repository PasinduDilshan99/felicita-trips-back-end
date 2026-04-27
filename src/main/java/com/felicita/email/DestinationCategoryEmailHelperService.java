package com.felicita.email;

import com.felicita.model.other.DestinationCategoryUpdateComparisonResult;
import com.felicita.model.request.DestinationCategoryInsertRequest;
import com.felicita.model.request.DestinationCategoryUpdateRequest;
import com.felicita.model.response.DestinationCategoryDetailsResponseDto;
import com.felicita.security.model.User;

public interface DestinationCategoryEmailHelperService {
    String buildDestinationCategoryCreateSuccessfullBody(DestinationCategoryInsertRequest destinationCategoryInsertRequest, User loggedUser);

    String buildDestinationCategoryCreateSuccessfullSubject(DestinationCategoryInsertRequest destinationCategoryInsertRequest, User loggedUser);

    String buildDestinationCategoryUpdateSuccessfullSubject(DestinationCategoryUpdateRequest destinationCategoryUpdateRequest, User loggedUser);

    String buildDestinationCategoryUpdateSuccessfullBody(User loggedUser, DestinationCategoryUpdateComparisonResult comparisonResult);

    String buildDestinationCategoryTerminateSuccessfullSubject(User loggeduser, DestinationCategoryDetailsResponseDto destinationCategoryDetailsResponseDto);

    String buildDestinationCategoryTerminateSuccessfullBody(User loggeduser, DestinationCategoryDetailsResponseDto destinationCategoryDetailsResponseDto);
}
