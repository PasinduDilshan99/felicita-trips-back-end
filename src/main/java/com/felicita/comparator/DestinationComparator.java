package com.felicita.comparator;

import com.felicita.model.dto.DestinationResponseDto;
import com.felicita.model.other.DestinationCategoryUpdateComparisonResult;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.request.DestinationCategoryUpdateRequest;
import com.felicita.model.request.DestinationUpdateRequest;
import com.felicita.model.response.DestinationCategoryDetailsResponseDto;

public interface DestinationComparator {
    DestinationUpdateComparisonResult compareUpdates(
            DestinationUpdateRequest updateRequest,
            DestinationResponseDto existingDestination);

    DestinationCategoryUpdateComparisonResult compareDestinationCategoryUpdates(DestinationCategoryDetailsResponseDto destinationCategoryDetailsResponseDto, DestinationCategoryUpdateRequest destinationCategoryUpdateRequest);
}
