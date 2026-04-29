package com.felicita.email.impl;

import com.felicita.email.DestinationEmailHelperService;
import com.felicita.model.dto.DestinationActivityResponseDto;
import com.felicita.model.dto.DestinationCategoryDetailsDto;
import com.felicita.model.dto.DestionationImageResponseDto;
import com.felicita.model.other.ActivityUpdateDetails;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.other.FieldUpdate;
import com.felicita.model.request.DestinationInsertRequest;
import com.felicita.model.request.DestinationUpdateRequest;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationEmailHelperServiceImpl implements DestinationEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationEmailHelperServiceImpl.class);

    @Override
    public String buildDestinationCreateSuccessfullSubject(DestinationInsertRequest destinationInsertRequest, User loggedUser) {
        return String.format("[Felicita Trips] Destination Created — %s",
                destinationInsertRequest.getName());
    }

    @Override
    public String buildDestinationUpdateSuccessfullSubject(User loggedUser) {
        return String.format("[Felicita Trips] Destination Updated — %s %s",
                loggedUser.getFirstName() != null ? loggedUser.getFirstName() : "",
                loggedUser.getLastName() != null ? loggedUser.getLastName() : "");
    }

    @Override
    public String buildDestinationUpdateSuccessfullBody(User loggedUser, Long destinationId, DestinationUpdateComparisonResult comparisonResult) {
        String updatedFieldsHtml = buildUpdatedFieldsHtml(comparisonResult.getUpdatedFields());
        String removedImagesHtml = buildRemovedImagesHtml(comparisonResult.getRemovedImages());
        String addedImagesHtml = buildAddedImagesHtml(comparisonResult.getAddedImages());
        String removedActivitiesHtml = buildRemovedActivitiesHtml(comparisonResult.getRemovedActivities());
        String addedActivitiesHtml = buildAddedActivitiesHtml(comparisonResult.getAddedActivities());
        String modifiedActivitiesHtml = buildModifiedActivitiesHtml(comparisonResult.getModifiedActivities());
        String removedCategoriesHtml = buildRemovedCategoriesHtml(comparisonResult.getRemovedCategories());
        String addedCategoriesHtml = buildAddedCategoriesHtml(comparisonResult.getAddedCategoryIds());

        boolean hasAnyUpdates = comparisonResult.hasAnyUpdates();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Destination Updated</title>" +
                "<style>" +
                "body{margin:0;padding:0;background:#f0f7f7;font-family:Georgia,serif;}" +
                ".wrapper{max-width:640px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,128,128,0.10);}" +
                ".header{background:linear-gradient(135deg,#0e7c7b 0%,#1a9e9e 50%,#2bbfbf 100%);padding:36px 40px 28px;text-align:center;}" +
                ".header img{height:52px;margin-bottom:16px;}" +
                ".header h1{color:#ffffff;font-size:22px;margin:0;font-weight:normal;letter-spacing:0.5px;}" +
                ".header p.tagline{color:rgba(255,255,255,0.80);font-size:12px;margin:6px 0 0;letter-spacing:2px;text-transform:uppercase;}" +
                ".badge{display:inline-block;background:rgba(255,255,255,0.18);color:#ffffff;border:1px solid rgba(255,255,255,0.40);border-radius:20px;padding:4px 16px;font-size:12px;margin-top:14px;letter-spacing:1px;}" +
                ".content{padding:36px 40px;}" +
                ".section-title{font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#0e7c7b;margin:0 0 14px;font-family:Arial,sans-serif;}" +
                ".info-card{background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".updated-fields-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".updated-fields-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".updated-fields-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".updated-fields-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".old-value{color:#a33;text-decoration:line-through;background:#fdecea;padding:2px 6px;border-radius:4px;display:inline-block;}" +
                ".new-value{color:#1a6b40;background:#d4f4e8;padding:2px 6px;border-radius:4px;display:inline-block;}" +
                ".images-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".images-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".images-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".images-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".activities-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".activities-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".activities-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".categories-container{display:flex;flex-wrap:wrap;gap:8px;margin-top:4px;}" +
                ".category-tag{display:inline-block;padding:2px 8px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:12px;font-size:11px;color:#0e7c7b;font-family:Arial,sans-serif;}" +
                ".category-tag-removed{background:#fdecea;border-color:#e0b0b0;color:#a33;text-decoration:line-through;}" +
                ".category-tag-added{background:#d4f4e8;border-color:#90d0c0;color:#1a6b40;}" +
                ".no-updates{text-align:center;color:#6b8e8e;font-style:italic;padding:24px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Destination Updated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#9998; Update Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<p class='section-title'>Updated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + loggedUser.getEmail() + " &nbsp;|&nbsp; " + loggedUser.getUsername() + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Destination ID</p>" +
                "<div class='info-card'>" +
                "<div class='info-row' style='display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;'>" +
                "<span class='info-label' style='font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:130px;'>Destination ID</span>" +
                "<span class='info-value' style='font-size:14px;color:#1a3333;font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;'>#" + destinationId + "</span>" +
                "</div>" +
                "</div>" +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this destination.</div>") +

                (updatedFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + updatedFieldsHtml + "</tbody>" +
                                "</table><br/>") +

                (removedCategoriesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Removed Categories</p>" +
                                "<div class='categories-container'>" + removedCategoriesHtml + "</div><br/>") +

                (addedCategoriesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Added Categories</p>" +
                                "<div class='categories-container'>" + addedCategoriesHtml + "</div><br/>") +

                (removedImagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Removed Images</p>" +
                                "<table class='images-table'>" +
                                "<thead><tr><th>#</th><th>Name</th><th>Description</th><th>Preview</th></tr></thead>" +
                                "<tbody>" + removedImagesHtml + "</tbody>" +
                                "</table><br/>") +

                (addedImagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Added Images</p>" +
                                "<table class='images-table'>" +
                                "<thead><tr><th>#</th><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead>" +
                                "<tbody>" + addedImagesHtml + "</tbody>" +
                                "</table><br/>") +

                (removedActivitiesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Removed Activities</p>" +
                                "<table class='activities-table'>" +
                                "<thead><tr><th>ID</th><th>Name</th><th>Description</th></tr></thead>" +
                                "<tbody>" + removedActivitiesHtml + "</tbody>" +
                                "</table><br/>") +

                (addedActivitiesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Added Activities</p>" +
                                "<table class='activities-table'>" +
                                "<thead><tr><th>Name</th><th>Description</th><th>Price (Local)</th><th>Price (Foreigners)</th></tr></thead>" +
                                "<tbody>" + addedActivitiesHtml + "</tbody>" +
                                "</table><br/>") +

                (modifiedActivitiesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Modified Activities</p>" +
                                modifiedActivitiesHtml) +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Admin Portal</p>" +
                "<p>This is an automated notification from the Felicita Trips admin system.</p>" +
                "<p>Please do not reply to this email.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

// Helper methods for the update email

    private String buildUpdatedFieldsHtml(List<FieldUpdate> updatedFields) {
        if (updatedFields == null || updatedFields.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (FieldUpdate field : updatedFields) {
            sb.append("<tr>")
                    .append("<td><strong>").append(formatFieldName(field.getFieldName())).append("</strong></td>")
                    .append("<td><span class='old-value'>").append(formatValueForDisplay(field.getOldValue())).append("</span></td>")
                    .append("<td><span class='new-value'>").append(formatValueForDisplay(field.getNewValue())).append("</span></td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildRemovedImagesHtml(List<DestionationImageResponseDto> removedImages) {
        if (removedImages == null || removedImages.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (DestionationImageResponseDto img : removedImages) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(img.getImageName())).append("</td>")
                    .append("<td>").append(img.getImageDescription() != null ? escapeHtml(img.getImageDescription()) : "—").append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildAddedImagesHtml(List<DestinationInsertRequest.Image> addedImages) {
        if (addedImages == null || addedImages.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (DestinationInsertRequest.Image img : addedImages) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(img.getName() != null ? escapeHtml(img.getName()) : "—").append("</td>")
                    .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                    .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildRemovedActivitiesHtml(List<DestinationActivityResponseDto> removedActivities) {
        if (removedActivities == null || removedActivities.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (DestinationActivityResponseDto activity : removedActivities) {
            sb.append("<tr>")
                    .append("<td>").append(activity.getActivityId()).append("</td>")
                    .append("<td>").append(escapeHtml(activity.getActivityName())).append("</td>")
                    .append("<td>").append(activity.getActivityDescription() != null ? escapeHtml(activity.getActivityDescription()) : "—").append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildAddedActivitiesHtml(List<DestinationUpdateRequest.Activity> addedActivities) {
        if (addedActivities == null || addedActivities.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (DestinationUpdateRequest.Activity activity : addedActivities) {
            sb.append("<tr>")
                    .append("<td>").append(escapeHtml(activity.getName())).append("</td>")
                    .append("<td>").append(activity.getDescription() != null ? escapeHtml(activity.getDescription()) : "—").append("</td>")
                    .append("<td>").append(activity.getPriceLocal() != null ? "$" + activity.getPriceLocal() : "—").append("</td>")
                    .append("<td>").append(activity.getPriceForeigners() != null ? "$" + activity.getPriceForeigners() : "—").append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildModifiedActivitiesHtml(List<ActivityUpdateDetails> modifiedActivities) {
        if (modifiedActivities == null || modifiedActivities.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (ActivityUpdateDetails activity : modifiedActivities) {
            sb.append("<div class='info-card' style='margin-bottom:16px;'>")
                    .append("<div class='info-row'><span class='info-label'>Activity ID</span><span class='info-value'>#").append(activity.getActivityId()).append("</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Activity Name</span><span class='info-value'>").append(escapeHtml(activity.getActivityName())).append("</span></div>");

            if (activity.getUpdatedFields() != null && !activity.getUpdatedFields().isEmpty()) {
                sb.append("<div class='info-row'><span class='info-label'>Updated Fields</span><span class='info-value'>")
                        .append("<table style='width:100%;margin-top:8px;border-collapse:collapse;'>")
                        .append("<tr style='background:#e8f5f5;'><th style='padding:6px;text-align:left;font-size:11px;'>Field</th><th style='padding:6px;text-align:left;font-size:11px;'>Old</th><th style='padding:6px;text-align:left;font-size:11px;'>New</th></tr>");
                for (FieldUpdate field : activity.getUpdatedFields()) {
                    sb.append("<tr>")
                            .append("<td style='padding:6px;border-bottom:1px solid #e0f0f0;font-size:12px;'>").append(formatFieldName(field.getFieldName())).append("</td>")
                            .append("<td style='padding:6px;border-bottom:1px solid #e0f0f0;'><span class='old-value' style='font-size:11px;'>").append(formatValueForDisplay(field.getOldValue())).append("</span></td>")
                            .append("<td style='padding:6px;border-bottom:1px solid #e0f0f0;'><span class='new-value' style='font-size:11px;'>").append(formatValueForDisplay(field.getNewValue())).append("</span></td>")
                            .append("</tr>");
                }
                sb.append("</table></span></div>");
            }

            if (activity.getRemovedCategoryIds() != null && !activity.getRemovedCategoryIds().isEmpty()) {
                sb.append("<div class='info-row'><span class='info-label'>Removed Categories</span><span class='info-value'>")
                        .append("<div class='categories-container'>");
                for (Long catId : activity.getRemovedCategoryIds()) {
                    sb.append("<span class='category-tag category-tag-removed'>Category ID: ").append(catId).append("</span>");
                }
                sb.append("</div></span></div>");
            }

            if (activity.getAddedCategoryIds() != null && !activity.getAddedCategoryIds().isEmpty()) {
                sb.append("<div class='info-row'><span class='info-label'>Added Categories</span><span class='info-value'>")
                        .append("<div class='categories-container'>");
                for (Long catId : activity.getAddedCategoryIds()) {
                    sb.append("<span class='category-tag category-tag-added'>Category ID: ").append(catId).append("</span>");
                }
                sb.append("</div></span></div>");
            }

            if (activity.getAddedImages() != null && !activity.getAddedImages().isEmpty()) {
                sb.append("<div class='info-row'><span class='info-label'>Added Images</span><span class='info-value'>")
                        .append("<table style='width:100%;margin-top:8px;border-collapse:collapse;'><tr><th style='padding:6px;text-align:left;font-size:11px;'>Name</th><th style='padding:6px;text-align:left;font-size:11px;'>Preview</th></tr>");
                for (DestinationUpdateRequest.Image img : activity.getAddedImages()) {
                    sb.append("<tr>")
                            .append("<td style='padding:6px;border-bottom:1px solid #e0f0f0;font-size:12px;'>").append(escapeHtml(img.getName())).append("</td>")
                            .append("<td style='padding:6px;border-bottom:1px solid #e0f0f0;'>").append(img.getImageUrl() != null ? "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                            .append("</tr>");
                }
                sb.append("</table></span></div>");
            }

            sb.append("</div>");
        }
        return sb.toString();
    }

    private String buildRemovedCategoriesHtml(List<DestinationCategoryDetailsDto> removedCategories) {
        if (removedCategories == null || removedCategories.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (DestinationCategoryDetailsDto category : removedCategories) {
            sb.append("<span class='category-tag category-tag-removed'>")
                    .append(escapeHtml(category.getName()))
                    .append("</span>");
        }
        return sb.toString();
    }

    private String buildAddedCategoriesHtml(List<Long> addedCategoryIds) {
        if (addedCategoryIds == null || addedCategoryIds.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Long catId : addedCategoryIds) {
            sb.append("<span class='category-tag category-tag-added'>")
                    .append("Category ID: ").append(catId)
                    .append("</span>");
        }
        return sb.toString();
    }

    private String formatFieldName(String fieldName) {
        if (fieldName == null) return "";
        // Convert camelCase to readable format
        String readable = fieldName.replaceAll("([A-Z])", " $1").toLowerCase();
        return readable.substring(0, 1).toUpperCase() + readable.substring(1);
    }

    private String formatValueForDisplay(Object value) {
        if (value == null) return "—";
        if (value instanceof Boolean) {
            return (Boolean) value ? "Yes" : "No";
        }
        if (value instanceof Double || value instanceof Integer || value instanceof Long) {
            return value.toString();
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty()) return "—";
            return escapeHtml(str);
        }
        return escapeHtml(value.toString());
    }

    @Override
    public String buildDestinationCreateSuccessfullBody(DestinationInsertRequest destinationInsertRequest,
                                                        List<String> destinationCategories, User loggedUser) {

        String imageRows = buildDestinationImageRows(destinationInsertRequest.getImages());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Destination Created</title>" +
                "<style>" +
                "body{margin:0;padding:0;background:#f0f7f7;font-family:Georgia,serif;}" +
                ".wrapper{max-width:640px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,128,128,0.10);}" +
                ".header{background:linear-gradient(135deg,#0e7c7b 0%,#1a9e9e 50%,#2bbfbf 100%);padding:36px 40px 28px;text-align:center;}" +
                ".header img{height:52px;margin-bottom:16px;}" +
                ".header h1{color:#ffffff;font-size:22px;margin:0;font-weight:normal;letter-spacing:0.5px;}" +
                ".header p.tagline{color:rgba(255,255,255,0.80);font-size:12px;margin:6px 0 0;letter-spacing:2px;text-transform:uppercase;}" +
                ".badge{display:inline-block;background:rgba(255,255,255,0.18);color:#ffffff;border:1px solid rgba(255,255,255,0.40);border-radius:20px;padding:4px 16px;font-size:12px;margin-top:14px;letter-spacing:1px;}" +
                ".content{padding:36px 40px;}" +
                ".section-title{font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#0e7c7b;margin:0 0 14px;font-family:Arial,sans-serif;}" +
                ".info-card{background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".info-row{display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;}" +
                ".info-row:last-child{border-bottom:none;padding-bottom:0;}" +
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:130px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".images-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".images-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".images-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".images-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".categories-container{display:flex;flex-wrap:wrap;gap:8px;margin-top:4px;}" +
                ".category-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;}" +
                ".coordinates{font-family:monospace;font-size:13px;background:#f0f7f7;padding:2px 8px;border-radius:4px;display:inline-block;}" +
                ".extra-price{font-weight:bold;color:#1a6b40;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Destination Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10003; Successfully Created</span>" +
                "</div>" +

                "<div class='content'>" +

                "<p class='section-title'>Created By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + loggedUser.getEmail() + " &nbsp;|&nbsp; " + loggedUser.getUsername() + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Destination Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Destination Name</span><span class='info-value'>" + escapeHtml(destinationInsertRequest.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + escapeHtml(destinationInsertRequest.getDescription()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(destinationInsertRequest.getStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Location</span><span class='info-value'>" + escapeHtml(destinationInsertRequest.getLocation()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Coordinates</span><span class='info-value'><span class='coordinates'>" +
                (destinationInsertRequest.getLatitude() != null ? destinationInsertRequest.getLatitude() : "—") + ", " +
                (destinationInsertRequest.getLongitude() != null ? destinationInsertRequest.getLongitude() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Extra Price</span><span class='info-value'>" +
                (destinationInsertRequest.getExtraPrice() != null ? "<span class='extra-price'>$" + destinationInsertRequest.getExtraPrice() + "</span>" : "—") + "</span></div>" +
                (destinationInsertRequest.getExtraPriceNote() != null ?
                        "<div class='info-row'><span class='info-label'>Extra Price Note</span><span class='info-value'>" + escapeHtml(destinationInsertRequest.getExtraPriceNote()) + "</span></div>" : "") +
                "<div class='info-row'><span class='info-label'>Categories</span><span class='info-value'>" + buildCategoriesHtml(destinationCategories) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Images Added</span><span class='info-value'>" + (destinationInsertRequest.getImages() != null ? destinationInsertRequest.getImages().size() : 0) + " image(s)</span></div>" +
                "</div>" +

                (imageRows.isEmpty() ? "" :
                        "<p class='section-title'>Images</p>" +
                                "<table class='images-table'>" +
                                "<thead><tr><th>#</th><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead>" +
                                "<tbody>" + imageRows + "</tbody>" +
                                "</table><br/>"
                ) +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Admin Portal</p>" +
                "<p>This is an automated notification from the Felicita Trips admin system.</p>" +
                "<p>Please do not reply to this email.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

    // Add these private helper methods to the DestinationCategoryEmailHelperServiceImpl class

    private String buildDestinationImageRows(List<DestinationInsertRequest.Image> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (DestinationInsertRequest.Image img : images) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(img.getName() != null ? escapeHtml(img.getName()) : "—").append("</td>")
                    .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                    .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildCategoriesHtml(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No categories assigned</span>";
        }
        StringBuilder sb = new StringBuilder("<div class='categories-container'>");
        for (String category : categories) {
            sb.append("<span class='category-tag'>").append(escapeHtml(category)).append("</span>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String getFullName(User user) {
        StringBuilder sb = new StringBuilder();
        if (user.getFirstName()  != null) sb.append(user.getFirstName()).append(" ");
        if (user.getMiddleName() != null) sb.append(user.getMiddleName()).append(" ");
        if (user.getLastName()   != null) sb.append(user.getLastName());
        return sb.toString().trim();
    }

    private String getInitials(User user) {
        String initials = "";
        if (user.getFirstName() != null && !user.getFirstName().isEmpty())
            initials += user.getFirstName().charAt(0);
        if (user.getLastName() != null && !user.getLastName().isEmpty())
            initials += user.getLastName().charAt(0);
        return initials.toUpperCase();
    }

    private String buildStatusPill(String status) {
        if ("ACTIVE".equalsIgnoreCase(status)) {
            return "<span style='display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;background:#d4f4e8;color:#1a6b40;'>" + status + "</span>";
        } else {
            return "<span style='display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;background:#fdecea;color:#a33;'>" + status + "</span>";
        }
    }

}
