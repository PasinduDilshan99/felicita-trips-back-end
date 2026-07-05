package com.felicita.email;

import com.felicita.model.other.SeasonUpdateComparisonResult;
import com.felicita.model.request.seasons.SeasonInsertRequest;
import com.felicita.model.response.seasons.SeasonAllDetailsResponse;
import com.felicita.security.model.User;

public interface SeasonEmailHelperService {
    String buildSeasonTerminateSuccessfullSubject(User loggedUser, SeasonAllDetailsResponse seasonResponse);

    String buildSeasonTerminateSuccessfullBody(User loggedUser, SeasonAllDetailsResponse seasonResponse);

    String buildSeasonCreateSuccessfullBody(SeasonInsertRequest seasonInsertRequest, Long seasonId, User loggedUser);

    String buildSeasonCreateSuccessfullSubject(SeasonInsertRequest seasonInsertRequest, Long seasonId, User loggedUser);

    String buildSeasonUpdateSuccessfullSubject(User loggedUser, Long id);

    String buildSeasonUpdateSuccessfullBody(User loggedUser, Long id, SeasonUpdateComparisonResult comparisonResult);
}
