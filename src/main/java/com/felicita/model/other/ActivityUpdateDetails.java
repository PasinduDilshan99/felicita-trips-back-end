package com.felicita.model.other;

import com.felicita.model.request.DestinationUpdateRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActivityUpdateDetails {
    private int activityId;
    private String activityName;
    private List<FieldUpdate> updatedFields = new ArrayList<>();
    private List<Long> addedCategoryIds = new ArrayList<>();
    private List<Long> removedCategoryIds = new ArrayList<>();
    private List<DestinationUpdateRequest.Image> addedImages = new ArrayList<>();

    public void addUpdatedField(String fieldName, Object oldValue, Object newValue) {
        updatedFields.add(new FieldUpdate(fieldName, oldValue, newValue));
    }

    public void addAddedImage(DestinationUpdateRequest.Image image) {
        addedImages.add(image);
    }

    public boolean hasUpdates() {
        return !updatedFields.isEmpty() ||
                !addedCategoryIds.isEmpty() ||
                !removedCategoryIds.isEmpty() ||
                !addedImages.isEmpty();
    }
}