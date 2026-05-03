package com.felicita.comparator.impl;

import com.felicita.comparator.DestinationComparator;
import com.felicita.model.dto.*;
import com.felicita.model.other.ActivityUpdateDetails;
import com.felicita.model.other.DestinationCategoryUpdateComparisonResult;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.request.DestinationCategoryUpdateRequest;
import com.felicita.model.request.DestinationUpdateRequest;
import com.felicita.model.response.DestinationCategoryDetailsResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DestinationComparatorImpl implements DestinationComparator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationComparatorImpl.class);

    @Override
    public DestinationUpdateComparisonResult compareUpdates(
            DestinationUpdateRequest updateRequest,
            DestinationResponseDto existingDestination) {

        LOGGER.info("Starting comparison between update request and existing destination with ID: {}",
                existingDestination.getDestinationId());

        DestinationUpdateComparisonResult result = new DestinationUpdateComparisonResult();

        // 1. Compare basic destination details
        compareBasicDetails(updateRequest, existingDestination, result);

        // 2. Compare images (removed and added)
        compareImages(updateRequest, existingDestination, result);

        // 3. Compare activities (removed, added, and modified)
        compareActivities(updateRequest, existingDestination, result);

        // 4. Compare destination categories (removed and added)
        compareDestinationCategories(updateRequest, existingDestination, result);

        LOGGER.info("Comparison completed. Updates found: {}", result.hasAnyUpdates());

        return result;
    }

    @Override
    public DestinationCategoryUpdateComparisonResult compareDestinationCategoryUpdates(
            DestinationCategoryDetailsResponseDto existing,
            DestinationCategoryUpdateRequest request
    ) {

        List<DestinationCategoryUpdateComparisonResult.FieldChange> fieldChanges = new ArrayList<>();

        List<DestinationCategoryUpdateComparisonResult.ImageChange> imageChanges = new ArrayList<>();

        List<DestinationCategoryUpdateComparisonResult.DestinationChange> destinationChanges = new ArrayList<>();

        // -------------------------
        // CATEGORY FIELD CHANGES
        // -------------------------

        if (!Objects.equals(existing.getCategory(), request.getCategory())) {
            fieldChanges.add(
                    new DestinationCategoryUpdateComparisonResult.FieldChange(
                            "category",
                            existing.getCategory(),
                            request.getCategory(),
                            true
                    )
            );
        }

        if (!Objects.equals(existing.getCategoryDescription(), request.getDescription())) {
            fieldChanges.add(
                    new DestinationCategoryUpdateComparisonResult.FieldChange(
                            "description",
                            existing.getCategoryDescription(),
                            request.getDescription(),
                            true
                    )
            );
        }

        if (!Objects.equals(existing.getCategoryStatus(), request.getStatus())) {
            fieldChanges.add(
                    new DestinationCategoryUpdateComparisonResult.FieldChange(
                            "status",
                            existing.getCategoryStatus(),
                            request.getStatus(),
                            true
                    )
            );
        }

        if (!Objects.equals(existing.getColor(), request.getColor())) {
            fieldChanges.add(
                    new DestinationCategoryUpdateComparisonResult.FieldChange(
                            "color",
                            existing.getColor(),
                            request.getColor(),
                            true
                    )
            );
        }

        if (!Objects.equals(existing.getHoverColor(), request.getHoverColor())) {
            fieldChanges.add(
                    new DestinationCategoryUpdateComparisonResult.FieldChange(
                            "hoverColor",
                            existing.getHoverColor(),
                            request.getHoverColor(),
                            true
                    )
            );
        }

        // -------------------------
        // IMAGE REMOVES
        // -------------------------

        if (request.getRemoveImageIds() != null) {
            request.getRemoveImageIds().forEach(id ->
                    imageChanges.add(
                            new DestinationCategoryUpdateComparisonResult.ImageChange(
                                    id,
                                    "REMOVE",
                                    null,
                                    null
                            )
                    )
            );
        }

        // -------------------------
        // IMAGE UPDATES
        // -------------------------

        if (request.getUpdateImages() != null) {
            request.getUpdateImages().forEach(img ->
                    imageChanges.add(
                            new DestinationCategoryUpdateComparisonResult.ImageChange(
                                    img.getImageId(),
                                    "UPDATE",
                                    "OLD_DATA_NOT_LOADED_HERE",
                                    img.getImageUrl()
                            )
                    )
            );
        }

        // -------------------------
        // NEW IMAGES
        // -------------------------

        if (request.getNewImages() != null) {
            request.getNewImages().forEach(img ->
                    imageChanges.add(
                            new DestinationCategoryUpdateComparisonResult.ImageChange(
                                    null,
                                    "ADD",
                                    null,
                                    img.getImageUrl()
                            )
                    )
            );
        }

        // -------------------------
        // RETURN RESULT
        // -------------------------

        return DestinationCategoryUpdateComparisonResult.builder()
                .categoryId(request.getCategoryId())
                .fieldChanges(fieldChanges)
                .imageChanges(imageChanges)
                .destinationChanges(List.of()) // optional future enhancement
                .build();
    }

    private void compareBasicDetails(DestinationUpdateRequest updateRequest,
                                     DestinationResponseDto existing,
                                     DestinationUpdateComparisonResult result) {

        if (updateRequest.getName() != null && !updateRequest.getName().equals(existing.getDestinationName())) {
            result.addUpdatedField("destinationName", existing.getDestinationName(), updateRequest.getName());
            LOGGER.debug("Field 'destinationName' will be updated from '{}' to '{}'",
                    existing.getDestinationName(), updateRequest.getName());
        }

        if (updateRequest.getDescription() != null && !updateRequest.getDescription().equals(existing.getDestinationDescription())) {
            result.addUpdatedField("destinationDescription", existing.getDestinationDescription(), updateRequest.getDescription());
            LOGGER.debug("Field 'destinationDescription' will be updated");
        }

        if (updateRequest.getLocation() != null && !updateRequest.getLocation().equals(existing.getLocation())) {
            result.addUpdatedField("location", existing.getLocation(), updateRequest.getLocation());
            LOGGER.debug("Field 'location' will be updated from '{}' to '{}'",
                    existing.getLocation(), updateRequest.getLocation());
        }

        if (updateRequest.getLatitude() != null && !updateRequest.getLatitude().equals(existing.getLatitude())) {
            result.addUpdatedField("latitude", existing.getLatitude(), updateRequest.getLatitude());
            LOGGER.debug("Field 'latitude' will be updated from '{}' to '{}'",
                    existing.getLatitude(), updateRequest.getLatitude());
        }

        if (updateRequest.getLongitude() != null && !updateRequest.getLongitude().equals(existing.getLongitude())) {
            result.addUpdatedField("longitude", existing.getLongitude(), updateRequest.getLongitude());
            LOGGER.debug("Field 'longitude' will be updated from '{}' to '{}'",
                    existing.getLongitude(), updateRequest.getLongitude());
        }

        if (updateRequest.getExtraPrice() != null) {
            // Add to result if you have this field in your DTO
            result.addUpdatedField("extraPrice", null, updateRequest.getExtraPrice());
        }

        if (updateRequest.getExtraPriceNote() != null) {
            result.addUpdatedField("extraPriceNote", null, updateRequest.getExtraPriceNote());
        }

        if (updateRequest.getStatus() != null && !updateRequest.getStatus().equals(existing.getStatusName())) {
            result.addUpdatedField("status", existing.getStatusName(), updateRequest.getStatus());
            LOGGER.debug("Field 'status' will be updated from '{}' to '{}'",
                    existing.getStatusName(), updateRequest.getStatus());
        }
    }

    private void compareImages(DestinationUpdateRequest updateRequest,
                               DestinationResponseDto existing,
                               DestinationUpdateComparisonResult result) {

        // Check removed images
        if (updateRequest.getRemoveImages() != null && !updateRequest.getRemoveImages().isEmpty()) {
            List<DestionationImageResponseDto> removedImages = existing.getImages().stream()
                    .filter(img -> updateRequest.getRemoveImages().contains((long) img.getImageId()))
                    .collect(Collectors.toList());

            for (DestionationImageResponseDto image : removedImages) {
                result.addRemovedImage(image);
                LOGGER.debug("Image with ID {} will be removed", image.getImageId());
            }
        }

        // Check added images - FIX: Use DestinationInsertRequest.Image instead
        if (updateRequest.getNewImages() != null && !updateRequest.getNewImages().isEmpty()) {
            for (com.felicita.model.request.DestinationInsertRequest.Image newImage : updateRequest.getNewImages()) {
                result.addAddedImage(newImage);
                LOGGER.debug("New image '{}' will be added", newImage.getName());
            }
        }
    }

    private void compareActivities(DestinationUpdateRequest updateRequest,
                                   DestinationResponseDto existing,
                                   DestinationUpdateComparisonResult result) {

        // Check removed activities
        if (updateRequest.getRemoveActivities() != null && !updateRequest.getRemoveActivities().isEmpty()) {
            List<DestinationActivityResponseDto> removedActivities = existing.getActivities().stream()
                    .filter(activity -> updateRequest.getRemoveActivities().contains((long) activity.getActivityId()))
                    .collect(Collectors.toList());

            for (DestinationActivityResponseDto activity : removedActivities) {
                result.addRemovedActivity(activity);
                LOGGER.debug("Activity '{}' with ID {} will be removed",
                        activity.getActivityName(), activity.getActivityId());
            }
        }

        // Check added or modified activities
        if (updateRequest.getNewActivities() != null && !updateRequest.getNewActivities().isEmpty()) {
            for (DestinationUpdateRequest.Activity updatedActivity : updateRequest.getNewActivities()) {
                // Check if this activity already exists (by name)
                DestinationActivityResponseDto existingActivity = existing.getActivities().stream()
                        .filter(act -> act.getActivityName().equals(updatedActivity.getName()))
                        .findFirst()
                        .orElse(null);

                if (existingActivity == null) {
                    // This is a new activity
                    result.addAddedActivity(updatedActivity);
                    LOGGER.debug("New activity '{}' will be added", updatedActivity.getName());
                } else {
                    // This is an update to existing activity - compare fields
                    compareActivityFields(existingActivity, updatedActivity, result);
                }
            }
        }
    }

    private void compareActivityFields(DestinationActivityResponseDto existing,
                                       DestinationUpdateRequest.Activity updated,
                                       DestinationUpdateComparisonResult result) {

        ActivityUpdateDetails activityUpdate = new ActivityUpdateDetails();
        activityUpdate.setActivityId(existing.getActivityId());
        activityUpdate.setActivityName(existing.getActivityName());
        boolean hasUpdates = false;

        if (updated.getDescription() != null && !updated.getDescription().equals(existing.getActivityDescription())) {
            activityUpdate.addUpdatedField("description", existing.getActivityDescription(), updated.getDescription());
            hasUpdates = true;
        }

        if (updated.getDurationHover() != null && !updated.getDurationHover().equals(existing.getDurationHours())) {
            activityUpdate.addUpdatedField("durationHours", existing.getDurationHours(), updated.getDurationHover());
            hasUpdates = true;
        }

        if (updated.getAvailableFrom() != null && !updated.getAvailableFrom().toString().equals(existing.getAvailableFrom())) {
            activityUpdate.addUpdatedField("availableFrom", existing.getAvailableFrom(), updated.getAvailableFrom().toString());
            hasUpdates = true;
        }

        if (updated.getAvailableTo() != null && !updated.getAvailableTo().toString().equals(existing.getAvailableTo())) {
            activityUpdate.addUpdatedField("availableTo", existing.getAvailableTo(), updated.getAvailableTo().toString());
            hasUpdates = true;
        }

        if (updated.getPriceLocal() != null && !updated.getPriceLocal().equals(existing.getPriceLocal())) {
            activityUpdate.addUpdatedField("priceLocal", existing.getPriceLocal(), updated.getPriceLocal());
            hasUpdates = true;
        }

        if (updated.getPriceForeigners() != null && !updated.getPriceForeigners().equals(existing.getPriceForeigners())) {
            activityUpdate.addUpdatedField("priceForeigners", existing.getPriceForeigners(), updated.getPriceForeigners());
            hasUpdates = true;
        }

        if (updated.getMinParticipate() != null && !updated.getMinParticipate().equals(existing.getMinParticipate())) {
            activityUpdate.addUpdatedField("minParticipate", existing.getMinParticipate(), updated.getMinParticipate());
            hasUpdates = true;
        }

        if (updated.getMaxParticipate() != null && !updated.getMaxParticipate().equals(existing.getMaxParticipate())) {
            activityUpdate.addUpdatedField("maxParticipate", existing.getMaxParticipate(), updated.getMaxParticipate());
            hasUpdates = true;
        }

        // Check activity category updates
        compareActivityCategories(updated, activityUpdate);

        // Check activity images
        compareActivityImages(updated, activityUpdate);

        if (hasUpdates || activityUpdate.hasUpdates()) {
            result.addModifiedActivity(activityUpdate);
            LOGGER.debug("Activity '{}' with ID {} will be modified",
                    existing.getActivityName(), existing.getActivityId());
        }
    }

    private void compareActivityCategories(DestinationUpdateRequest.Activity updated,
                                           ActivityUpdateDetails activityUpdate) {
        if (updated.getAddActivityCategoriesId() != null && !updated.getAddActivityCategoriesId().isEmpty()) {
            activityUpdate.setAddedCategoryIds(updated.getAddActivityCategoriesId());
            LOGGER.debug("Activity '{}' will have {} categories added",
                    activityUpdate.getActivityName(),
                    updated.getAddActivityCategoriesId().size());
        }

        if (updated.getRemoveActivityCategoriesId() != null && !updated.getRemoveActivityCategoriesId().isEmpty()) {
            activityUpdate.setRemovedCategoryIds(updated.getRemoveActivityCategoriesId());
            LOGGER.debug("Activity '{}' will have {} categories removed",
                    activityUpdate.getActivityName(),
                    updated.getRemoveActivityCategoriesId().size());
        }
    }

    private void compareActivityImages(DestinationUpdateRequest.Activity updated,
                                       ActivityUpdateDetails activityUpdate) {
        if (updated.getActivityImages() != null && !updated.getActivityImages().isEmpty()) {
            for (DestinationUpdateRequest.Image image : updated.getActivityImages()) {
                activityUpdate.addAddedImage(image);
                LOGGER.debug("Activity '{}' will have new image '{}' added",
                        activityUpdate.getActivityName(), image.getName());
            }
        }
    }

    private void compareDestinationCategories(DestinationUpdateRequest updateRequest,
                                              DestinationResponseDto existing,
                                              DestinationUpdateComparisonResult result) {

        // Check removed categories
        if (updateRequest.getRemovedestinationCategoriesIdList() != null &&
                !updateRequest.getRemovedestinationCategoriesIdList().isEmpty()) {

            List<DestinationCategoryDetailsDto> removedCategories = existing.getDestinationCategoryDetailsDtos().stream()
                    .filter(cat -> updateRequest.getRemovedestinationCategoriesIdList().contains(cat.getId()))
                    .collect(Collectors.toList());

            for (DestinationCategoryDetailsDto category : removedCategories) {
                result.addRemovedCategory(category);
                LOGGER.debug("Category '{}' with ID {} will be removed from destination",
                        category.getName(), category.getId());
            }
        }

        // Check added categories
        if (updateRequest.getAdddestinationCategoriesIdList() != null &&
                !updateRequest.getAdddestinationCategoriesIdList().isEmpty()) {
            result.setAddedCategoryIds(updateRequest.getAdddestinationCategoriesIdList());
            LOGGER.debug("{} new categories will be added to destination",
                    updateRequest.getAdddestinationCategoriesIdList().size());
        }
    }
}