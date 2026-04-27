package com.felicita.validation;

import com.felicita.model.request.*;

public interface DestinationValidationService {
    void validateDestinationInsertRequest(DestinationInsertRequest destinationInsertRequest);

    void validateTerminateDestinationRequest(DestinationTerminateRequest destinationTerminateRequest);

    void validateDestinationUpdateRequest(DestinationUpdateRequest destinationUpdateRequest);

    void validateDestinationCategoryInsertRequest(DestinationCategoryInsertRequest destinationCategoryInsertRequest);

    void validateDestinationCategoryUpdateRequest(DestinationCategoryUpdateRequest destinationCategoryUpdateRequest);

    void validateDestinationCategoryTerminateRequest(DestinationCategoryTerminateRequest destinationCategoryTerminateRequest);
}
