package com.felicita.model.other;

import com.felicita.model.dto.DestinationActivityResponseDto;
import com.felicita.model.dto.DestinationCategoryDetailsDto;
import com.felicita.model.dto.DestionationImageResponseDto;
import com.felicita.model.request.DestinationInsertRequest;
import com.felicita.model.request.DestinationUpdateRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DestinationUpdateComparisonResult {
    private List<FieldUpdate> updatedFields = new ArrayList<>();
    private List<DestionationImageResponseDto> removedImages = new ArrayList<>();
    private List<DestinationInsertRequest.Image> addedImages = new ArrayList<>(); // Changed to DestinationInsertRequest.Image
    private List<DestinationActivityResponseDto> removedActivities = new ArrayList<>();
    private List<DestinationUpdateRequest.Activity> addedActivities = new ArrayList<>();
    private List<ActivityUpdateDetails> modifiedActivities = new ArrayList<>();
    private List<DestinationCategoryDetailsDto> removedCategories = new ArrayList<>();
    private List<Long> addedCategoryIds = new ArrayList<>();

    public void addUpdatedField(String fieldName, Object oldValue, Object newValue) {
        updatedFields.add(new FieldUpdate(fieldName, oldValue, newValue));
    }

    public void addRemovedImage(DestionationImageResponseDto image) {
        removedImages.add(image);
    }

    public void addAddedImage(DestinationInsertRequest.Image image) { // Changed parameter type
        addedImages.add(image);
    }

    public void addRemovedActivity(DestinationActivityResponseDto activity) {
        removedActivities.add(activity);
    }

    public void addAddedActivity(DestinationUpdateRequest.Activity activity) {
        addedActivities.add(activity);
    }

    public void addModifiedActivity(ActivityUpdateDetails activity) {
        modifiedActivities.add(activity);
    }

    public void addRemovedCategory(DestinationCategoryDetailsDto category) {
        removedCategories.add(category);
    }

    public void setAddedCategoryIds(List<Long> categoryIds) {
        this.addedCategoryIds = categoryIds;
    }

    public boolean hasAnyUpdates() {
        return !updatedFields.isEmpty() ||
                !removedImages.isEmpty() ||
                !addedImages.isEmpty() ||
                !removedActivities.isEmpty() ||
                !addedActivities.isEmpty() ||
                !modifiedActivities.isEmpty() ||
                !removedCategories.isEmpty() ||
                !addedCategoryIds.isEmpty();
    }
}