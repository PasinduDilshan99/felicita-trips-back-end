package com.felicita.validation;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.seasons.SeasonInsertRequest;
import com.felicita.model.request.seasons.SeasonUpdateRequest;

public interface SeasonValidationService {
    static void validateSeasonInsertRequest(SeasonInsertRequest seasonInsertRequest) {
    }

    void validateCommonIdRequest(CommonIdRequest seasonTerminateIdRequest);

    void validateSeasonUpdateRequest(SeasonUpdateRequest seasonUpdateRequest);
}
