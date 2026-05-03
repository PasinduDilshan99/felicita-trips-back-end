package com.felicita.email.impl;

import com.felicita.email.ActivityEmailHelperService;
import com.felicita.model.dto.ActivityCategoryDto;
import com.felicita.model.dto.ActivityImageDto;
import com.felicita.model.dto.ActivityRequirementDto;
import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.other.ActivitiesComparisonResult;
import com.felicita.model.request.ActivityImageInsertRequest;
import com.felicita.model.request.ActivityInsertRequest;
import com.felicita.model.request.ActivityRequirementInsertRequest;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityEmailHelperServiceImpl implements ActivityEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityEmailHelperServiceImpl.class);

    @Override
    public String buildActivityCreateSuccessfullSubject(ActivityInsertRequest activityInsertRequest, Long activityId, User loggedUser) {
        return String.format("[Felicita Trips] Activity Created — %s",
                activityInsertRequest.getName() != null ? activityInsertRequest.getName() : "Unknown");
    }

    @Override
    public String buildActivityCreateSuccessfullBody(ActivityInsertRequest activityInsertRequest, Long activityId, User loggedUser) {
        String categoriesHtml = buildActivityCategoriesHtml(activityInsertRequest.getCategories());
        String imagesHtml = buildActivityImagesHtml(activityInsertRequest.getImages());
        String requirementsHtml = buildActivityRequirementsHtml(activityInsertRequest.getRequirements());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Activity Created</title>" +
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
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:140px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".activity-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".category-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".category-primary{background:#d4f4e8;border-color:#90d0c0;color:#1a6b40;font-weight:bold;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".requirement-tag{display:inline-block;padding:6px 12px;background:#e8f5f5;border-left:3px solid #0e7c7b;margin:4px;font-size:12px;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".price{font-weight:bold;color:#1a6b40;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Activity Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#127939; New Activity Added</span>" +
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

                "<p class='section-title'>Activity Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Activity ID</span><span class='info-value'>#" + activityId + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Activity Name</span><span class='info-value'><span class='activity-icon'>🏃</span>" + escapeHtml(activityInsertRequest.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Destination ID</span><span class='info-value'>#" + activityInsertRequest.getDestinationId() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (activityInsertRequest.getDescription() != null ? escapeHtml(activityInsertRequest.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(activityInsertRequest.getStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Duration</span><span class='info-value'>" + (activityInsertRequest.getDurationHours() != null ? activityInsertRequest.getDurationHours() + " hours" : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Available Time</span><span class='info-value'>" +
                (activityInsertRequest.getAvailableFrom() != null ? activityInsertRequest.getAvailableFrom().toString() : "—") + " - " +
                (activityInsertRequest.getAvailableTo() != null ? activityInsertRequest.getAvailableTo().toString() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Price (Local)</span><span class='info-value'><span class='price'>$" + (activityInsertRequest.getPriceLocal() != null ? activityInsertRequest.getPriceLocal() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Price (Foreigners)</span><span class='info-value'><span class='price'>$" + (activityInsertRequest.getPriceForeigners() != null ? activityInsertRequest.getPriceForeigners() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Participants</span><span class='info-value'>Min: " + (activityInsertRequest.getMinParticipate() != null ? activityInsertRequest.getMinParticipate() : "—") +
                " | Max: " + (activityInsertRequest.getMaxParticipate() != null ? activityInsertRequest.getMaxParticipate() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Season ID</span><span class='info-value'>#" + (activityInsertRequest.getSeasonId() != null ? activityInsertRequest.getSeasonId() : "—") + "</span></div>" +
                "</div>" +

                (categoriesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Categories</p>" +
                                "<div class='info-card'>" + categoriesHtml + "</div>") +

                (imagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Images (" + (activityInsertRequest.getImages() != null ? activityInsertRequest.getImages().size() : 0) + ")</p>" +
                                imagesHtml) +

                (requirementsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Requirements (" + (activityInsertRequest.getRequirements() != null ? activityInsertRequest.getRequirements().size() : 0) + ")</p>" +
                                requirementsHtml) +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Admin Portal</p>" +
                "<p>This is an automated notification from the Felicita Trips admin system.</p>" +
                "<p>Please do not reply to this email.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

    @Override
    public String buildActivityUpdateSuccessfullSubject(User loggedUser, Long activityId) {
        return String.format("[Felicita Trips] Activity Updated — #%d by %s %s",
                activityId,
                loggedUser.getFirstName() != null ? loggedUser.getFirstName() : "",
                loggedUser.getLastName() != null ? loggedUser.getLastName() : "");
    }

    @Override
    public String buildActivityUpdateSuccessfullBody(User loggedUser, Long activityId, ActivitiesComparisonResult comparisonResult) {
        String basicFieldsHtml = buildActivityBasicFieldsHtml(comparisonResult.getBasicFieldChanges());
        String categoriesHtml = buildActivityCategoryChangesHtml(comparisonResult);
        String imagesHtml = buildActivityImageChangesHtml(comparisonResult);
        String requirementsHtml = buildActivityRequirementChangesHtml(comparisonResult);

        boolean hasAnyUpdates = comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Activity Updated</title>" +
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
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:140px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".updated-fields-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".updated-fields-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".updated-fields-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".updated-fields-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".old-value{color:#a33;text-decoration:line-through;background:#fdecea;padding:2px 6px;border-radius:4px;display:inline-block;}" +
                ".new-value{color:#1a6b40;background:#d4f4e8;padding:2px 6px;border-radius:4px;display:inline-block;}" +
                ".category-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".category-tag-added{background:#d4f4e8;border-color:#90d0c0;color:#1a6b40;}" +
                ".category-tag-removed{background:#fdecea;border-color:#e0b0b0;color:#a33;text-decoration:line-through;}" +
                ".category-tag-updated{background:#fff8e7;border-color:#ffd700;color:#b8860b;}" +
                ".category-primary{font-weight:bold;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".no-updates{text-align:center;color:#6b8e8e;font-style:italic;padding:24px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Activity Updated</h1>" +
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

                "<p class='section-title'>Activity ID</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'>" +
                "<span class='info-label'>Activity ID</span>" +
                "<span class='info-value' style='font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;'>#" + activityId + "</span>" +
                "</div>" +
                "</div>" +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this activity.</div>") +

                (basicFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + basicFieldsHtml + "</tbody>" +
                                "</table><br/>") +

                (categoriesHtml.isEmpty() ? "" : categoriesHtml) +
                (imagesHtml.isEmpty() ? "" : imagesHtml) +
                (requirementsHtml.isEmpty() ? "" : requirementsHtml) +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Admin Portal</p>" +
                "<p>This is an automated notification from the Felicita Trips admin system.</p>" +
                "<p>Please do not reply to this email.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

    @Override
    public String buildActivityTerminateSuccessfullSubject(User loggedUser, ActivityResponseDto activityResponseDto) {
        return String.format("[Felicita Trips] Activity Terminated — %s",
                activityResponseDto.getName() != null ? activityResponseDto.getName() : "Unknown");
    }

    @Override
    public String buildActivityTerminateSuccessfullBody(User loggedUser, ActivityResponseDto activityResponseDto) {
        String categoriesHtml = buildTerminateActivityCategoriesHtml(activityResponseDto.getActivityCategoryDtos());
        String imagesHtml = buildTerminateActivityImagesHtml(activityResponseDto.getImages());
        String requirementsHtml = buildTerminateActivityRequirementsHtml(activityResponseDto.getRequirements());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Activity Terminated</title>" +
                "<style>" +
                "body{margin:0;padding:0;background:#f0f7f7;font-family:Georgia,serif;}" +
                ".wrapper{max-width:640px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,128,128,0.10);}" +
                ".header{background:linear-gradient(135deg,#8B0000 0%,#b22222 50%,#cd5c5c 100%);padding:36px 40px 28px;text-align:center;}" +
                ".header img{height:52px;margin-bottom:16px;}" +
                ".header h1{color:#ffffff;font-size:22px;margin:0;font-weight:normal;letter-spacing:0.5px;}" +
                ".header p.tagline{color:rgba(255,255,255,0.80);font-size:12px;margin:6px 0 0;letter-spacing:2px;text-transform:uppercase;}" +
                ".badge{display:inline-block;background:rgba(255,255,255,0.18);color:#ffffff;border:1px solid rgba(255,255,255,0.40);border-radius:20px;padding:4px 16px;font-size:12px;margin-top:14px;letter-spacing:1px;}" +
                ".content{padding:36px 40px;}" +
                ".section-title{font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#8B0000;margin:0 0 14px;font-family:Arial,sans-serif;}" +
                ".info-card{background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".info-row{display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;}" +
                ".info-row:last-child{border-bottom:none;padding-bottom:0;}" +
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:140px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".terminate-warning{background:#fdecea;border:1px solid #e0b0b0;border-radius:8px;padding:16px 20px;margin-bottom:24px;text-align:center;}" +
                ".terminate-warning p{color:#a33;font-family:Arial,sans-serif;font-size:13px;margin:0;}" +
                ".terminate-warning .warning-icon{font-size:24px;margin-bottom:8px;display:block;}" +
                ".status-pill-terminated{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;background:#fdecea;color:#a33;font-weight:bold;}" +
                ".category-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".category-primary{background:#d4f4e8;border-color:#90d0c0;color:#1a6b40;font-weight:bold;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#8B0000,#cd5c5c);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".price{font-weight:bold;color:#1a6b40;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Activity Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This activity has been terminated</strong> and is no longer available for booking or display.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + loggedUser.getEmail() + " &nbsp;|&nbsp; " + loggedUser.getUsername() + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Activity Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Activity ID</span><span class='info-value'>#" + activityResponseDto.getId() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Activity Name</span><span class='info-value'>" + escapeHtml(activityResponseDto.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Destination</span><span class='info-value'>" + escapeHtml(activityResponseDto.getDestinationName()) + " (#" + activityResponseDto.getDestinationId() + ")</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (activityResponseDto.getDescription() != null ? escapeHtml(activityResponseDto.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(activityResponseDto.getStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'><span class='status-pill-terminated'>TERMINATED</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Duration</span><span class='info-value'>" + (activityResponseDto.getDurationHours() != null ? activityResponseDto.getDurationHours() + " hours" : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Available Time</span><span class='info-value'>" +
                (activityResponseDto.getAvailableFrom() != null ? activityResponseDto.getAvailableFrom().toString() : "—") + " - " +
                (activityResponseDto.getAvailableTo() != null ? activityResponseDto.getAvailableTo().toString() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Price (Local)</span><span class='info-value'><span class='price'>$" + (activityResponseDto.getPriceLocal() != null ? activityResponseDto.getPriceLocal() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Price (Foreigners)</span><span class='info-value'><span class='price'>$" + (activityResponseDto.getPriceForeigners() != null ? activityResponseDto.getPriceForeigners() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Participants</span><span class='info-value'>Min: " + (activityResponseDto.getMinParticipate() != null ? activityResponseDto.getMinParticipate() : "—") +
                " | Max: " + (activityResponseDto.getMaxParticipate() != null ? activityResponseDto.getMaxParticipate() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Season</span><span class='info-value'>" + (activityResponseDto.getSeason() != null ? escapeHtml(activityResponseDto.getSeason()) : "—") + "</span></div>" +
                "</div>" +

                (categoriesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Categories</p>" +
                                "<div class='info-card'>" + categoriesHtml + "</div>") +

                (imagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Images (" + (activityResponseDto.getImages() != null ? activityResponseDto.getImages().size() : 0) + ")</p>" +
                                imagesHtml) +

                (requirementsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Requirements (" + (activityResponseDto.getRequirements() != null ? activityResponseDto.getRequirements().size() : 0) + ")</p>" +
                                requirementsHtml) +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Admin Portal</p>" +
                "<p>This is an automated notification from the Felicita Trips admin system.</p>" +
                "<p>Please do not reply to this email.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

// Helper methods for Activity email

    private String buildActivityCategoriesHtml(List<ActivityInsertRequest.Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No categories assigned</span>";
        }
        StringBuilder sb = new StringBuilder("<div style='display:flex;flex-wrap:wrap;gap:8px;'>");
        for (ActivityInsertRequest.Category category : categories) {
            String primaryClass = Boolean.TRUE.equals(category.getIsPrimary()) ? " category-primary" : "";
            sb.append("<span class='category-tag" + primaryClass + "'>")
                    .append("Category ID: ").append(category.getCategoryId())
                    .append(Boolean.TRUE.equals(category.getIsPrimary()) ? " ⭐" : "")
                    .append("</span>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String buildActivityImagesHtml(List<ActivityImageInsertRequest> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
        int i = 1;
        for (ActivityImageInsertRequest img : images) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(img.getName())).append("</td>")
                    .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                    .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildActivityRequirementsHtml(List<ActivityRequirementInsertRequest> requirements) {
        if (requirements == null || requirements.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Value</th><th>Description</th><th>Status</th></tr></thead><tbody>");
        int i = 1;
        for (ActivityRequirementInsertRequest req : requirements) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(req.getName())).append("</td>")
                    .append("<td style='color:" + (req.getColor() != null ? req.getColor() : "#1a3333") + ";font-weight:bold;'>").append(escapeHtml(req.getValue())).append("</td>")
                    .append("<td>").append(req.getDescription() != null ? escapeHtml(req.getDescription()) : "—").append("</td>")
                    .append("<td>").append(buildStatusPill(req.getStatus())).append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildActivityBasicFieldsHtml(List<ActivitiesComparisonResult.FieldChange> basicFieldChanges) {
        if (basicFieldChanges == null || basicFieldChanges.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (ActivitiesComparisonResult.FieldChange field : basicFieldChanges) {
            sb.append("<tr>")
                    .append("<td><strong>").append(formatFieldName(field.getFieldName())).append("</strong></td>")
                    .append("<td><span class='old-value'>").append(formatValueForDisplay(field.getOldValue())).append("</span></td>")
                    .append("<td><span class='new-value'>").append(formatValueForDisplay(field.getNewValue())).append("</span></td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildActivityCategoryChangesHtml(ActivitiesComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getCategoryIdsToRemove() != null && !comparisonResult.getCategoryIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Categories</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long catId : comparisonResult.getCategoryIdsToRemove()) {
                sb.append("<span class='category-tag category-tag-removed'>Category ID: ").append(catId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getCategoriesToAdd() != null && !comparisonResult.getCategoriesToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Categories</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (ActivitiesComparisonResult.CategoryChange cat : comparisonResult.getCategoriesToAdd()) {
                sb.append("<span class='category-tag category-tag-added'>")
                        .append("Category ID: ").append(cat.getCategoryId())
                        .append(Boolean.TRUE.equals(cat.getIsPrimary()) ? " ⭐ (Primary)" : "")
                        .append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getCategoriesToUpdate() != null && !comparisonResult.getCategoriesToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Categories</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (ActivitiesComparisonResult.CategoryChange cat : comparisonResult.getCategoriesToUpdate()) {
                sb.append("<span class='category-tag category-tag-updated'>")
                        .append("Category ID: ").append(cat.getCategoryId())
                        .append(Boolean.TRUE.equals(cat.getIsPrimary()) ? " ⭐ (Primary)" : "")
                        .append(" | Status: ").append(cat.getStatus())
                        .append("</span>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildActivityImageChangesHtml(ActivitiesComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getImageIdsToRemove() != null && !comparisonResult.getImageIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Images</p>")
                    .append("<table class='info-table'><thead><tr><th>Image ID</th><th>Name</th><th>Preview</th></tr></thead><tbody>");
            for (Long imgId : comparisonResult.getImageIdsToRemove()) {
                sb.append("<tr><td>").append(imgId).append("</td><td colspan='2'><span class='old-value'>Image ID: ").append(imgId).append("</span></td></tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        if (comparisonResult.getImagesToAdd() != null && !comparisonResult.getImagesToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Images</p>")
                    .append("<table class='info-table'><thead><tr><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
            for (ActivitiesComparisonResult.ImageChange img : comparisonResult.getImagesToAdd()) {
                sb.append("<tr>")
                        .append("<td>").append(escapeHtml(img.getName())).append("</td>")
                        .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                        .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                        .append("<td>").append(img.getImageUrl() != null ? "<a href='" + img.getImageUrl() + "' target='_blank'>🔗 View</a>" : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        if (comparisonResult.getImagesToUpdate() != null && !comparisonResult.getImagesToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Images</p>")
                    .append("<table class='info-table'><thead><tr><th>Image ID</th><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
            for (ActivitiesComparisonResult.ImageChange img : comparisonResult.getImagesToUpdate()) {
                sb.append("<tr>")
                        .append("<td>").append(img.getImageId() != null ? img.getImageId() : "—").append("</td>")
                        .append("<td>").append(escapeHtml(img.getName())).append("</td>")
                        .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                        .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                        .append("<td>").append(img.getImageUrl() != null ? "<a href='" + img.getImageUrl() + "' target='_blank'>🔗 View</a>" : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody><table><br/>");
        }

        return sb.toString();
    }

    private String buildActivityRequirementChangesHtml(ActivitiesComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getRequirementIdsToRemove() != null && !comparisonResult.getRequirementIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Requirements</p>")
                    .append("<table class='info-table'><thead><tr><th>Requirement ID</th><th>Name</th><th>Value</th></tr></thead><tbody>");
            for (Long reqId : comparisonResult.getRequirementIdsToRemove()) {
                sb.append("<tr><td>").append(reqId).append("</td><td colspan='2'><span class='old-value'>Requirement ID: ").append(reqId).append("</span></td></tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        if (comparisonResult.getRequirementsToAdd() != null && !comparisonResult.getRequirementsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Requirements</p>")
                    .append("<table class='info-table'><thead><tr><th>Name</th><th>Value</th><th>Description</th><th>Color</th><th>Status</th></tr></thead><tbody>");
            for (ActivitiesComparisonResult.RequirementChange req : comparisonResult.getRequirementsToAdd()) {
                sb.append("<tr>")
                        .append("<td>").append(escapeHtml(req.getName())).append("</td>")
                        .append("<td style='color:" + (req.getColor() != null ? req.getColor() : "#1a3333") + ";font-weight:bold;'>").append(escapeHtml(req.getValue())).append("</td>")
                        .append("<td>").append(req.getDescription() != null ? escapeHtml(req.getDescription()) : "—").append("</td>")
                        .append("<td>").append(req.getColor() != null ? req.getColor() : "—").append("</td>")
                        .append("<td>").append(buildStatusPill(req.getStatus())).append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></td><br/>");
        }

        if (comparisonResult.getRequirementsToUpdate() != null && !comparisonResult.getRequirementsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Requirements</p>")
                    .append("<table class='info-table'><thead><tr><th>Requirement ID</th><th>Name</th><th>Value</th><th>Description</th><th>Color</th><th>Status</th></tr></thead><tbody>");
            for (ActivitiesComparisonResult.RequirementChange req : comparisonResult.getRequirementsToUpdate()) {
                sb.append("<tr>")
                        .append("<td>").append(req.getRequirementId() != null ? req.getRequirementId() : "—").append("</td>")
                        .append("<td>").append(escapeHtml(req.getName())).append("</td>")
                        .append("<td style='color:" + (req.getColor() != null ? req.getColor() : "#1a3333") + ";font-weight:bold;'>").append(escapeHtml(req.getValue())).append("</td>")
                        .append("<td>").append(req.getDescription() != null ? escapeHtml(req.getDescription()) : "—").append("</td>")
                        .append("<td>").append(req.getColor() != null ? req.getColor() : "—").append("</td>")
                        .append("<td>").append(buildStatusPill(req.getStatus())).append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        return sb.toString();
    }

    private String buildTerminateActivityCategoriesHtml(List<ActivityCategoryDto> categories) {
        if (categories == null || categories.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No categories assigned</span>";
        }
        StringBuilder sb = new StringBuilder("<div style='display:flex;flex-wrap:wrap;gap:8px;'>");
        for (ActivityCategoryDto category : categories) {
            String primaryClass = Boolean.TRUE.equals(category.getIsPrimary()) ? " category-primary" : "";
            sb.append("<span class='category-tag" + primaryClass + "'>")
                    .append(escapeHtml(category.getName()))
                    .append(Boolean.TRUE.equals(category.getIsPrimary()) ? " ⭐" : "")
                    .append("</span>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String buildTerminateActivityImagesHtml(List<ActivityImageDto> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Description</th><th>Preview</th></tr></thead><tbody>");
        int i = 1;
        for (ActivityImageDto img : images) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(img.getName())).append("</td>")
                    .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildTerminateActivityRequirementsHtml(List<ActivityRequirementDto> requirements) {
        if (requirements == null || requirements.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Value</th><th>Description</th></tr></thead><tbody>");
        int i = 1;
        for (ActivityRequirementDto req : requirements) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(req.getName())).append("</td>")
                    .append("<td style='color:" + (req.getColor() != null ? req.getColor() : "#1a3333") + ";font-weight:bold;'>").append(escapeHtml(req.getValue())).append("</td>")
                    .append("<td>").append(req.getDescription() != null ? escapeHtml(req.getDescription()) : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String formatFieldName(String fieldName) {
        if (fieldName == null) return "";
        String readable = fieldName.replaceAll("([A-Z])", " $1").toLowerCase();
        return readable.substring(0, 1).toUpperCase() + readable.substring(1);
    }

    private String formatValueForDisplay(Object value) {
        if (value == null) return "—";
        if (value instanceof Boolean) {
            return (Boolean) value ? "Yes" : "No";
        }
        if (value instanceof java.math.BigDecimal) {
            return "$" + value.toString();
        }
        if (value instanceof java.time.LocalTime) {
            return value.toString();
        }
        return escapeHtml(value.toString());
    }

    private String getInitials(User user) {
        String initials = "";
        if (user.getFirstName() != null && !user.getFirstName().isEmpty())
            initials += user.getFirstName().charAt(0);
        if (user.getLastName() != null && !user.getLastName().isEmpty())
            initials += user.getLastName().charAt(0);
        return initials.toUpperCase();
    }

    private String getFullName(User user) {
        StringBuilder sb = new StringBuilder();
        if (user.getFirstName() != null) sb.append(user.getFirstName()).append(" ");
        if (user.getMiddleName() != null) sb.append(user.getMiddleName()).append(" ");
        if (user.getLastName() != null) sb.append(user.getLastName());
        return sb.toString().trim();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String buildStatusPill(String status) {
        if (status == null) {
            return "<span class='status-pill status-inactive'>INACTIVE</span>";
        }
        if ("ACTIVE".equalsIgnoreCase(status)) {
            return "<span class='status-pill status-active'>" + status + "</span>";
        } else {
            return "<span class='status-pill status-inactive'>" + status + "</span>";
        }
    }
}
