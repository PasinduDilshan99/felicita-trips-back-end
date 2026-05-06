package com.felicita.email.impl;

import com.felicita.email.TourEmailHelperService;
import com.felicita.model.other.TourComparisonResult;
import com.felicita.model.request.*;
import com.felicita.model.response.TourAllDetailsResponse;
import com.felicita.model.response.TourExtrasResponse;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TourEmailHelperServiceImpl implements TourEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourEmailHelperServiceImpl.class);

    @Override
    public String buildTourCreateSuccessfullSubject(TourInsertRequest tourInsertRequest, Long tourId, User loggedUser) {
        return String.format("[Felicita Trips] Tour Created — %s",
                tourInsertRequest.getName() != null ? tourInsertRequest.getName() : "Unknown");
    }

    @Override
    public String buildTourCreateSuccessfullBody(TourInsertRequest tourInsertRequest, Long tourId, User loggedUser) {
        String imagesHtml = buildTourImagesHtml(tourInsertRequest.getImages());
        String inclusionsHtml = buildTourInclusionsHtml(tourInsertRequest.getInclusions());
        String exclusionsHtml = buildTourExclusionsHtml(tourInsertRequest.getExclusions());
        String conditionsHtml = buildTourConditionsHtml(tourInsertRequest.getConditions());
        String travelTipsHtml = buildTourTravelTipsHtml(tourInsertRequest.getTravelTips());
        String tourTypesHtml = buildTourTypesHtml(tourInsertRequest.getTourTypes());
        String tourCategoriesHtml = buildTourCategoriesHtml(tourInsertRequest.getTourCategories());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Tour Created</title>" +
                "<style>" +
                "body{margin:0;padding:0;background:#f0f7f7;font-family:Georgia,serif;}" +
                ".wrapper{max-width:740px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,128,128,0.10);}" +
                ".header{background:linear-gradient(135deg,#0e7c7b 0%,#1a9e9e 50%,#2bbfbf 100%);padding:36px 40px 28px;text-align:center;}" +
                ".header img{height:52px;margin-bottom:16px;}" +
                ".header h1{color:#ffffff;font-size:22px;margin:0;font-weight:normal;letter-spacing:0.5px;}" +
                ".header p.tagline{color:rgba(255,255,255,0.80);font-size:12px;margin:6px 0 0;letter-spacing:2px;text-transform:uppercase;}" +
                ".badge{display:inline-block;background:rgba(255,255,255,0.18);color:#ffffff;border:1px solid rgba(255,255,255,0.40);border-radius:20px;padding:4px 16px;font-size:12px;margin-top:14px;letter-spacing:1px;}" +
                ".content{padding:36px 40px;}" +
                ".section-title{font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#0e7c7b;margin:24px 0 14px 0;font-family:Arial,sans-serif;}" +
                ".section-title:first-of-type{margin-top:0;}" +
                ".info-card{background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".info-row{display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;}" +
                ".info-row:last-child{border-bottom:none;padding-bottom:0;}" +
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:140px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".tour-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".destination-card{background:#f9fdfd;border:1px solid #c8e8e8;border-radius:8px;padding:12px 16px;margin-bottom:12px;}" +
                ".destination-card .dest-title{font-weight:bold;color:#0e7c7b;margin-bottom:8px;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".coordinates{font-family:monospace;font-size:13px;background:#f0f7f7;padding:2px 8px;border-radius:4px;display:inline-block;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Tour Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#128652; New Tour Package Added</span>" +
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

                "<p class='section-title'>Tour Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Tour ID</span><span class='info-value'>#" + tourId + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Tour Name</span><span class='info-value'><span class='tour-icon'>🚌</span>" + escapeHtml(tourInsertRequest.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (tourInsertRequest.getDescription() != null ? escapeHtml(tourInsertRequest.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(tourInsertRequest.getStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Duration</span><span class='info-value'>" + (tourInsertRequest.getDuration() != null ? tourInsertRequest.getDuration() + " days" : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Season</span><span class='info-value'>#" + (tourInsertRequest.getSeason() != null ? tourInsertRequest.getSeason() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Start Location</span><span class='info-value'>" + escapeHtml(tourInsertRequest.getStartLocation()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>End Location</span><span class='info-value'>" + escapeHtml(tourInsertRequest.getEndLocation()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Coordinates</span><span class='info-value'><span class='coordinates'>" +
                (tourInsertRequest.getLatitude() != null ? tourInsertRequest.getLatitude() : "—") + ", " +
                (tourInsertRequest.getLongitude() != null ? tourInsertRequest.getLongitude() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Assign To</span><span class='info-value'>#" + (tourInsertRequest.getAssignTo() != null ? tourInsertRequest.getAssignTo() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Assign Message</span><span class='info-value'>" + (tourInsertRequest.getAssignMessage() != null ? escapeHtml(tourInsertRequest.getAssignMessage()) : "—") + "</span></div>" +
                "</div>" +

                (tourTypesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Tour Types</p>" +
                                "<div class='info-card'>" + tourTypesHtml + "</div>") +

                (tourCategoriesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Tour Categories</p>" +
                                "<div class='info-card'>" + tourCategoriesHtml + "</div>") +

                (inclusionsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Inclusions</p>" +
                                "<div class='info-card'>" + inclusionsHtml + "</div>") +

                (exclusionsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Exclusions</p>" +
                                "<div class='info-card'>" + exclusionsHtml + "</div>") +

                (conditionsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Terms & Conditions</p>" +
                                "<div class='info-card'>" + conditionsHtml + "</div>") +

                (travelTipsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Travel Tips</p>" +
                                "<div class='info-card'>" + travelTipsHtml + "</div>") +

                (imagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Images (" + (tourInsertRequest.getImages() != null ? tourInsertRequest.getImages().size() : 0) + ")</p>" +
                                imagesHtml) +

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
    public String buildTourUpdateSuccessfullSubject(User loggedUser, Long tourId) {
        return String.format("[Felicita Trips] Tour Updated — #%d by %s %s",
                tourId,
                loggedUser.getFirstName() != null ? loggedUser.getFirstName() : "",
                loggedUser.getLastName() != null ? loggedUser.getLastName() : "");
    }

    @Override
    public String buildTourUpdateSuccessfullBody(User loggedUser, Long tourId, TourComparisonResult comparisonResult) {
        String basicFieldsHtml = buildTourBasicFieldsHtml(comparisonResult.getBasicDetailsChanges());
        String tourTypesHtml = buildTourTypeChangesHtml(comparisonResult);
        String tourCategoriesHtml = buildTourCategoryChangesHtml(comparisonResult);
        String destinationsHtml = buildTourDestinationChangesHtml(comparisonResult);
        String imagesHtml = buildTourImageChangesHtml(comparisonResult);
        String inclusionsHtml = buildTourInclusionChangesHtml(comparisonResult);
        String exclusionsHtml = buildTourExclusionChangesHtml(comparisonResult);
        String conditionsHtml = buildTourConditionChangesHtml(comparisonResult);
        String travelTipsHtml = buildTourTravelTipChangesHtml(comparisonResult);

        boolean hasAnyUpdates = comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Tour Updated</title>" +
                "<style>" +
                "body{margin:0;padding:0;background:#f0f7f7;font-family:Georgia,serif;}" +
                ".wrapper{max-width:740px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,128,128,0.10);}" +
                ".header{background:linear-gradient(135deg,#0e7c7b 0%,#1a9e9e 50%,#2bbfbf 100%);padding:36px 40px 28px;text-align:center;}" +
                ".header img{height:52px;margin-bottom:16px;}" +
                ".header h1{color:#ffffff;font-size:22px;margin:0;font-weight:normal;letter-spacing:0.5px;}" +
                ".header p.tagline{color:rgba(255,255,255,0.80);font-size:12px;margin:6px 0 0;letter-spacing:2px;text-transform:uppercase;}" +
                ".badge{display:inline-block;background:rgba(255,255,255,0.18);color:#ffffff;border:1px solid rgba(255,255,255,0.40);border-radius:20px;padding:4px 16px;font-size:12px;margin-top:14px;letter-spacing:1px;}" +
                ".content{padding:36px 40px;}" +
                ".section-title{font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#0e7c7b;margin:24px 0 14px 0;font-family:Arial,sans-serif;}" +
                ".section-title:first-of-type{margin-top:0;}" +
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
                ".tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".tag-added{background:#d4f4e8;border-color:#90d0c0;color:#1a6b40;}" +
                ".tag-removed{background:#fdecea;border-color:#e0b0b0;color:#a33;text-decoration:line-through;}" +
                ".tag-updated{background:#fff8e7;border-color:#ffd700;color:#b8860b;}" +
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
                "<h1>Tour Updated</h1>" +
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

                "<p class='section-title'>Tour ID</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'>" +
                "<span class='info-label'>Tour ID</span>" +
                "<span class='info-value' style='font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;'>#" + tourId + "</span>" +
                "</div>" +
                "</div>" +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this tour.</div>") +

                (basicFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + basicFieldsHtml + "</tbody>" +
                                "</td><br/>") +

                (tourTypesHtml.isEmpty() ? "" : tourTypesHtml) +
                (tourCategoriesHtml.isEmpty() ? "" : tourCategoriesHtml) +
                (destinationsHtml.isEmpty() ? "" : destinationsHtml) +
                (imagesHtml.isEmpty() ? "" : imagesHtml) +
                (inclusionsHtml.isEmpty() ? "" : inclusionsHtml) +
                (exclusionsHtml.isEmpty() ? "" : exclusionsHtml) +
                (conditionsHtml.isEmpty() ? "" : conditionsHtml) +
                (travelTipsHtml.isEmpty() ? "" : travelTipsHtml) +

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
    public String buildTourTerminateSuccessfullSubject(User loggedUser, TourAllDetailsResponse tourDetails) {
        return String.format("[Felicita Trips] Tour Terminated — %s",
                tourDetails.getTourName() != null ? tourDetails.getTourName() : "Unknown");
    }

    @Override
    public String buildTourTerminateSuccessfullBody(User loggedUser, TourAllDetailsResponse tourDetails) {
        String tourTypesHtml = buildTerminateTourTypesHtml(tourDetails.getTourTypeDtos());
        String tourCategoriesHtml = buildTerminateTourCategoriesHtml(tourDetails.getTourCategoryDto());
        String inclusionsHtml = buildTerminateTourInclusionsHtml(tourDetails.getInclusions());
        String exclusionsHtml = buildTerminateTourExclusionsHtml(tourDetails.getExclusions());
        String conditionsHtml = buildTerminateTourConditionsHtml(tourDetails.getConditions());
        String travelTipsHtml = buildTerminateTourTravelTipsHtml(tourDetails.getTravelTips());
        String imagesHtml = buildTerminateTourImagesHtml(tourDetails.getImages());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Tour Terminated</title>" +
                "<style>" +
                "body{margin:0;padding:0;background:#f0f7f7;font-family:Georgia,serif;}" +
                ".wrapper{max-width:740px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,128,128,0.10);}" +
                ".header{background:linear-gradient(135deg,#8B0000 0%,#b22222 50%,#cd5c5c 100%);padding:36px 40px 28px;text-align:center;}" +
                ".header img{height:52px;margin-bottom:16px;}" +
                ".header h1{color:#ffffff;font-size:22px;margin:0;font-weight:normal;letter-spacing:0.5px;}" +
                ".header p.tagline{color:rgba(255,255,255,0.80);font-size:12px;margin:6px 0 0;letter-spacing:2px;text-transform:uppercase;}" +
                ".badge{display:inline-block;background:rgba(255,255,255,0.18);color:#ffffff;border:1px solid rgba(255,255,255,0.40);border-radius:20px;padding:4px 16px;font-size:12px;margin-top:14px;letter-spacing:1px;}" +
                ".content{padding:36px 40px;}" +
                ".section-title{font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#8B0000;margin:24px 0 14px 0;font-family:Arial,sans-serif;}" +
                ".section-title:first-of-type{margin-top:0;}" +
                ".info-card{background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".info-row{display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;}" +
                ".info-row:last-child{border-bottom:none;padding-bottom:0;}" +
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:140px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".terminate-warning{background:#fdecea;border:1px solid #e0b0b0;border-radius:8px;padding:16px 20px;margin-bottom:24px;text-align:center;}" +
                ".terminate-warning p{color:#a33;font-family:Arial,sans-serif;font-size:13px;margin:0;}" +
                ".terminate-warning .warning-icon{font-size:24px;margin-bottom:8px;display:block;}" +
                ".status-pill-terminated{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;background:#fdecea;color:#a33;font-weight:bold;}" +
                ".tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#8B0000,#cd5c5c);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".coordinates{font-family:monospace;font-size:13px;background:#f0f7f7;padding:2px 8px;border-radius:4px;display:inline-block;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Tour Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This tour package has been terminated</strong> and is no longer available for booking.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + loggedUser.getEmail() + " &nbsp;|&nbsp; " + loggedUser.getUsername() + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Tour Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Tour ID</span><span class='info-value'>#" + tourDetails.getTourId() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Tour Name</span><span class='info-value'>" + escapeHtml(tourDetails.getTourName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (tourDetails.getTourDescription() != null ? escapeHtml(tourDetails.getTourDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(tourDetails.getStatusName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'><span class='status-pill-terminated'>TERMINATED</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Duration</span><span class='info-value'>" + (tourDetails.getDuration() != null ? tourDetails.getDuration() + " days" : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Start Location</span><span class='info-value'>" + escapeHtml(tourDetails.getStartLocation()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>End Location</span><span class='info-value'>" + escapeHtml(tourDetails.getEndLocation()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Coordinates</span><span class='info-value'><span class='coordinates'>" +
                (tourDetails.getLatitude() != null ? tourDetails.getLatitude() : "—") + ", " +
                (tourDetails.getLongitude() != null ? tourDetails.getLongitude() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Season</span><span class='info-value'>" + escapeHtml(tourDetails.getSeasonName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Assigned To</span><span class='info-value'>" + escapeHtml(tourDetails.getAssignToName()) + " (#" + (tourDetails.getAssignTo() != null ? tourDetails.getAssignTo() : "—") + ")</span></div>" +
                "<div class='info-row'><span class='info-label'>Assign Message</span><span class='info-value'>" + (tourDetails.getAssignMessage() != null ? escapeHtml(tourDetails.getAssignMessage()) : "—") + "</span></div>" +
                "</div>" +

                (tourTypesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Tour Types</p>" +
                                "<div class='info-card'>" + tourTypesHtml + "</div>") +

                (tourCategoriesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Tour Categories</p>" +
                                "<div class='info-card'>" + tourCategoriesHtml + "</div>") +

                (inclusionsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Inclusions</p>" +
                                "<div class='info-card'>" + inclusionsHtml + "</div>") +

                (exclusionsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Exclusions</p>" +
                                "<div class='info-card'>" + exclusionsHtml + "</div>") +

                (conditionsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Terms & Conditions</p>" +
                                "<div class='info-card'>" + conditionsHtml + "</div>") +

                (travelTipsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Travel Tips</p>" +
                                "<div class='info-card'>" + travelTipsHtml + "</div>") +

                (imagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Images (" + (tourDetails.getImages() != null ? tourDetails.getImages().size() : 0) + ")</p>" +
                                imagesHtml) +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Admin Portal</p>" +
                "<p>This is an automated notification from the Felicita Trips admin system.</p>" +
                "<p>Please do not reply to this email.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

// Helper methods for Tour email

    private String buildTourTypesHtml(List<Long> tourTypeIds) {
        if (tourTypeIds == null || tourTypeIds.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No tour types assigned</span>";
        }
        StringBuilder sb = new StringBuilder("<div style='display:flex;flex-wrap:wrap;gap:8px;'>");
        for (Long typeId : tourTypeIds) {
            sb.append("<span class='tag'>Tour Type ID: ").append(typeId).append("</span>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String buildTourCategoriesHtml(List<Long> tourCategoryIds) {
        if (tourCategoryIds == null || tourCategoryIds.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No tour categories assigned</span>";
        }
        StringBuilder sb = new StringBuilder("<div style='display:flex;flex-wrap:wrap;gap:8px;'>");
        for (Long catId : tourCategoryIds) {
            sb.append("<span class='tag'>Tour Category ID: ").append(catId).append("</span>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String buildTourImagesHtml(List<TourImageInsertRequest> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
        int i = 1;
        for (TourImageInsertRequest img : images) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(img.getImageName())).append("</td>")
                    .append("<td>").append(img.getImageDescription() != null ? escapeHtml(img.getImageDescription()) : "—").append("</td>")
                    .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildTourDestinationsHtml(List<TourDestinationInsertRequest> destinations) {
        if (destinations == null || destinations.isEmpty()) {
            return "<div class='info-card'><span style='color:#6b8e8e;font-style:italic;'>No destinations added</span></div>";
        }
        StringBuilder sb = new StringBuilder();
        for (TourDestinationInsertRequest dest : destinations) {
            sb.append("<div class='destination-card'>")
                    .append("<div class='dest-title'>Day ").append(dest.getDayNumber() != null ? dest.getDayNumber() : "—").append("</div>")
                    .append("<div class='info-row'><span class='info-label'>Destination ID</span><span class='info-value'>#").append(dest.getDestinationId()).append("</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Activity ID</span><span class='info-value'>#").append(dest.getActivityId() != null ? dest.getActivityId() : "—").append("</span></div>")
                    .append("</div>");
        }
        return sb.toString();
    }

    private String buildTourInclusionsHtml(List<TourInclusionInsertRequest> inclusions) {
        if (inclusions == null || inclusions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No inclusions specified</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (TourInclusionInsertRequest inc : inclusions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>")
                    .append(escapeHtml(inc.getInclusionText()))
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(inc.getDisplayOrder() != null ? inc.getDisplayOrder() : "—").append("]</span>")
                    .append(" ").append(buildStatusPill(inc.getStatus()))
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildTourExclusionsHtml(List<TourExclusionInsertRequest> exclusions) {
        if (exclusions == null || exclusions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No exclusions specified</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (TourExclusionInsertRequest exc : exclusions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>")
                    .append(escapeHtml(exc.getExclusionText()))
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(exc.getDisplayOrder() != null ? exc.getDisplayOrder() : "—").append("]</span>")
                    .append(" ").append(buildStatusPill(exc.getStatus()))
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildTourConditionsHtml(List<TourConditionInsertRequest> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No conditions specified</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (TourConditionInsertRequest cond : conditions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>")
                    .append(escapeHtml(cond.getConditionText()))
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(cond.getDisplayOrder() != null ? cond.getDisplayOrder() : "—").append("]</span>")
                    .append(" ").append(buildStatusPill(cond.getStatus()))
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildTourTravelTipsHtml(List<TourTravelTipInsertRequest> travelTips) {
        if (travelTips == null || travelTips.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No travel tips specified</span>";
        }
        StringBuilder sb = new StringBuilder();
        for (TourTravelTipInsertRequest tip : travelTips) {
            sb.append("<div style='margin-bottom:12px;padding:10px;background:#f9fdfd;border-radius:6px;'>")
                    .append("<strong>📌 ").append(escapeHtml(tip.getTipTitle())).append("</strong>")
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(tip.getDisplayOrder() != null ? tip.getDisplayOrder() : "—").append("]</span>")
                    .append("<p style='margin:5px 0 0 0;font-size:13px;color:#555;'>").append(escapeHtml(tip.getTipDescription())).append("</p>")
                    .append(" ").append(buildStatusPill(tip.getStatus()))
                    .append("</div>");
        }
        return sb.toString();
    }

    private String buildTourBasicFieldsHtml(List<TourComparisonResult.FieldChange> basicFieldChanges) {
        if (basicFieldChanges == null || basicFieldChanges.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (TourComparisonResult.FieldChange field : basicFieldChanges) {
            sb.append("<tr>")
                    .append("<td><strong>").append(formatFieldName(field.getFieldName())).append("</strong></td>")
                    .append("<td><span class='old-value'>").append(formatValueForDisplay(field.getOldValue())).append("</span></td>")
                    .append("<td><span class='new-value'>").append(formatValueForDisplay(field.getNewValue())).append("</span></td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildTourTypeChangesHtml(TourComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getTourTypeIdsToRemove() != null && !comparisonResult.getTourTypeIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Tour Types</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long typeId : comparisonResult.getTourTypeIdsToRemove()) {
                sb.append("<span class='tag tag-removed'>Tour Type ID: ").append(typeId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getTourTypeIdsToAdd() != null && !comparisonResult.getTourTypeIdsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Tour Types</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long typeId : comparisonResult.getTourTypeIdsToAdd()) {
                sb.append("<span class='tag tag-added'>Tour Type ID: ").append(typeId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getTourTypesToUpdate() != null && !comparisonResult.getTourTypesToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Tour Types</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (TourComparisonResult.TourTypeChange type : comparisonResult.getTourTypesToUpdate()) {
                sb.append("<span class='tag tag-updated'>")
                        .append("Tour Type ID: ").append(type.getTourTypeId())
                        .append(" | IsPrimary: ").append(type.getIsPrimary())
                        .append(" | Status: ").append(type.getStatus())
                        .append("</span>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildTourCategoryChangesHtml(TourComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getTourCategoryIdsToRemove() != null && !comparisonResult.getTourCategoryIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Tour Categories</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long catId : comparisonResult.getTourCategoryIdsToRemove()) {
                sb.append("<span class='tag tag-removed'>Tour Category ID: ").append(catId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getTourCategoryIdsToAdd() != null && !comparisonResult.getTourCategoryIdsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Tour Categories</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long catId : comparisonResult.getTourCategoryIdsToAdd()) {
                sb.append("<span class='tag tag-added'>Tour Category ID: ").append(catId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getTourCategoriesToUpdate() != null && !comparisonResult.getTourCategoriesToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Tour Categories</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (TourComparisonResult.TourCategoryChange cat : comparisonResult.getTourCategoriesToUpdate()) {
                sb.append("<span class='tag tag-updated'>")
                        .append("Tour Category ID: ").append(cat.getTourCategoryId())
                        .append(" | IsPrimary: ").append(cat.getIsPrimary())
                        .append(" | Status: ").append(cat.getStatus())
                        .append("</span>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildTourDestinationChangesHtml(TourComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getDestinationIdsToRemove() != null && !comparisonResult.getDestinationIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Destinations</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long destId : comparisonResult.getDestinationIdsToRemove()) {
                sb.append("<span class='tag tag-removed'>Destination ID: ").append(destId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getDestinationsToAdd() != null && !comparisonResult.getDestinationsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Destinations</p>")
                    .append("<div style='margin-bottom:20px;'>");
            for (TourComparisonResult.TourDestinationChange dest : comparisonResult.getDestinationsToAdd()) {
                sb.append("<div class='destination-card' style='margin-bottom:8px;background:#d4f4e8;'>")
                        .append("<div class='dest-title'>Day ").append(dest.getDayNumber() != null ? dest.getDayNumber() : "—").append("</div>")
                        .append("<div><span class='info-label'>Destination ID:</span> #").append(dest.getDestinationId()).append("</div>")
                        .append("<div><span class='info-label'>Activity ID:</span> #").append(dest.getActivityId() != null ? dest.getActivityId() : "—").append("</div>")
                        .append("</div>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getDestinationsToUpdate() != null && !comparisonResult.getDestinationsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Destinations</p>")
                    .append("<div style='margin-bottom:20px;'>");
            for (TourComparisonResult.TourDestinationChange dest : comparisonResult.getDestinationsToUpdate()) {
                sb.append("<div class='destination-card' style='margin-bottom:8px;background:#fff8e7;border-color:#ffd700;'>")
                        .append("<div class='dest-title'>Day ").append(dest.getDayNumber() != null ? dest.getDayNumber() : "—").append("</div>")
                        .append("<div><span class='info-label'>Tour Destination ID:</span> #").append(dest.getTourDestinationId()).append("</div>")
                        .append("<div><span class='info-label'>Destination ID:</span> #").append(dest.getDestinationId()).append("</div>")
                        .append("<div><span class='info-label'>Activity ID:</span> #").append(dest.getActivityId() != null ? dest.getActivityId() : "—").append("</div>")
                        .append("<div><span class='info-label'>Status:</span> ").append(buildStatusPill(dest.getStatus())).append("</div>")
                        .append("</div>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildTourImageChangesHtml(TourComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getImageIdsToRemove() != null && !comparisonResult.getImageIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Images</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long imgId : comparisonResult.getImageIdsToRemove()) {
                sb.append("<span class='tag tag-removed'>Image ID: ").append(imgId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getImagesToAdd() != null && !comparisonResult.getImagesToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Images</p>")
                    .append("<table class='info-table'><thead><tr><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
            for (TourComparisonResult.TourImageChange img : comparisonResult.getImagesToAdd()) {
                sb.append("<tr>")
                        .append("<td>").append(escapeHtml(img.getImageName())).append("</td>")
                        .append("<td>").append(img.getImageDescription() != null ? escapeHtml(img.getImageDescription()) : "—").append("</td>")
                        .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                        .append("<td>").append(img.getImageUrl() != null ? "<a href='" + img.getImageUrl() + "' target='_blank'>🔗 View</a>" : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        if (comparisonResult.getImagesToUpdate() != null && !comparisonResult.getImagesToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Images</p>")
                    .append("<table class='info-table'><thead><td><th>Image ID</th><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
            for (TourComparisonResult.TourImageChange img : comparisonResult.getImagesToUpdate()) {
                sb.append("<tr>")
                        .append("<td>").append(img.getImageId() != null ? img.getImageId() : "—").append("</td>")
                        .append("<td>").append(escapeHtml(img.getImageName())).append("</td>")
                        .append("<td>").append(img.getImageDescription() != null ? escapeHtml(img.getImageDescription()) : "—").append("</td>")
                        .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                        .append("<td>").append(img.getImageUrl() != null ? "<a href='" + img.getImageUrl() + "' target='_blank'>🔗 View</a>" : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        return sb.toString();
    }

    private String buildTourInclusionChangesHtml(TourComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getInclusionIdsToRemove() != null && !comparisonResult.getInclusionIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Inclusions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (Long incId : comparisonResult.getInclusionIdsToRemove()) {
                sb.append("<li class='tag-removed' style='margin-bottom:5px;'>Inclusion ID: ").append(incId).append("</li>");
            }
            sb.append("</ul>");
        }

        if (comparisonResult.getInclusionsToAdd() != null && !comparisonResult.getInclusionsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Inclusions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (TourComparisonResult.TourInclusionChange inc : comparisonResult.getInclusionsToAdd()) {
                sb.append("<li style='margin-bottom:5px;color:#1a6b40;'>")
                        .append(escapeHtml(inc.getInclusionText()))
                        .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(inc.getDisplayOrder()).append("]</span>")
                        .append("</li>");
            }
            sb.append("</ul>");
        }

        if (comparisonResult.getInclusionsToUpdate() != null && !comparisonResult.getInclusionsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Inclusions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (TourComparisonResult.TourInclusionChange inc : comparisonResult.getInclusionsToUpdate()) {
                sb.append("<li style='margin-bottom:5px;background:#fff8e7;padding:5px;border-radius:4px;'>")
                        .append("<strong>Inclusion ID:</strong> ").append(inc.getInclusionId()).append("<br/>")
                        .append("<strong>Text:</strong> ").append(escapeHtml(inc.getInclusionText())).append("<br/>")
                        .append("<strong>Display Order:</strong> ").append(inc.getDisplayOrder()).append("<br/>")
                        .append("<strong>Status:</strong> ").append(buildStatusPill(inc.getStatus()))
                        .append("</li>");
            }
            sb.append("</ul>");
        }

        return sb.toString();
    }

    private String buildTourExclusionChangesHtml(TourComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getExclusionIdsToRemove() != null && !comparisonResult.getExclusionIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Exclusions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (Long excId : comparisonResult.getExclusionIdsToRemove()) {
                sb.append("<li class='tag-removed' style='margin-bottom:5px;'>Exclusion ID: ").append(excId).append("</li>");
            }
            sb.append("</ul>");
        }

        if (comparisonResult.getExclusionsToAdd() != null && !comparisonResult.getExclusionsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Exclusions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (TourComparisonResult.TourExclusionChange exc : comparisonResult.getExclusionsToAdd()) {
                sb.append("<li style='margin-bottom:5px;color:#1a6b40;'>")
                        .append(escapeHtml(exc.getExclusionText()))
                        .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(exc.getDisplayOrder()).append("]</span>")
                        .append("</li>");
            }
            sb.append("</ul>");
        }

        if (comparisonResult.getExclusionsToUpdate() != null && !comparisonResult.getExclusionsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Exclusions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (TourComparisonResult.TourExclusionChange exc : comparisonResult.getExclusionsToUpdate()) {
                sb.append("<li style='margin-bottom:5px;background:#fff8e7;padding:5px;border-radius:4px;'>")
                        .append("<strong>Exclusion ID:</strong> ").append(exc.getExclusionId()).append("<br/>")
                        .append("<strong>Text:</strong> ").append(escapeHtml(exc.getExclusionText())).append("<br/>")
                        .append("<strong>Display Order:</strong> ").append(exc.getDisplayOrder()).append("<br/>")
                        .append("<strong>Status:</strong> ").append(buildStatusPill(exc.getStatus()))
                        .append("</li>");
            }
            sb.append("</ul>");
        }

        return sb.toString();
    }

    private String buildTourConditionChangesHtml(TourComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getConditionIdsToRemove() != null && !comparisonResult.getConditionIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Conditions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (Long condId : comparisonResult.getConditionIdsToRemove()) {
                sb.append("<li class='tag-removed' style='margin-bottom:5px;'>Condition ID: ").append(condId).append("</li>");
            }
            sb.append("</ul>");
        }

        if (comparisonResult.getConditionsToAdd() != null && !comparisonResult.getConditionsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Conditions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (TourComparisonResult.TourConditionChange cond : comparisonResult.getConditionsToAdd()) {
                sb.append("<li style='margin-bottom:5px;color:#1a6b40;'>")
                        .append(escapeHtml(cond.getConditionText()))
                        .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(cond.getDisplayOrder()).append("]</span>")
                        .append("</li>");
            }
            sb.append("</ul>");
        }

        if (comparisonResult.getConditionsToUpdate() != null && !comparisonResult.getConditionsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Conditions</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (TourComparisonResult.TourConditionChange cond : comparisonResult.getConditionsToUpdate()) {
                sb.append("<li style='margin-bottom:5px;background:#fff8e7;padding:5px;border-radius:4px;'>")
                        .append("<strong>Condition ID:</strong> ").append(cond.getConditionId()).append("<br/>")
                        .append("<strong>Text:</strong> ").append(escapeHtml(cond.getConditionText())).append("<br/>")
                        .append("<strong>Display Order:</strong> ").append(cond.getDisplayOrder()).append("<br/>")
                        .append("<strong>Status:</strong> ").append(buildStatusPill(cond.getStatus()))
                        .append("</li>");
            }
            sb.append("</ul>");
        }

        return sb.toString();
    }

    private String buildTourTravelTipChangesHtml(TourComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getTravelTipIdsToRemove() != null && !comparisonResult.getTravelTipIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Travel Tips</p>")
                    .append("<ul style='margin:0 0 20px 20px;'>");
            for (Long tipId : comparisonResult.getTravelTipIdsToRemove()) {
                sb.append("<li class='tag-removed' style='margin-bottom:5px;'>Travel Tip ID: ").append(tipId).append("</li>");
            }
            sb.append("</ul>");
        }

        if (comparisonResult.getTravelTipsToAdd() != null && !comparisonResult.getTravelTipsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Travel Tips</p>")
                    .append("<div style='margin-bottom:20px;'>");
            for (TourComparisonResult.TourTravelTipChange tip : comparisonResult.getTravelTipsToAdd()) {
                sb.append("<div style='margin-bottom:12px;padding:10px;background:#d4f4e8;border-radius:6px;'>")
                        .append("<strong>📌 ").append(escapeHtml(tip.getTipTitle())).append("</strong>")
                        .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(tip.getDisplayOrder()).append("]</span>")
                        .append("<p style='margin:5px 0 0 0;font-size:13px;color:#555;'>").append(escapeHtml(tip.getTipDescription())).append("</p>")
                        .append("</div>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getTravelTipsToUpdate() != null && !comparisonResult.getTravelTipsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Travel Tips</p>")
                    .append("<div style='margin-bottom:20px;'>");
            for (TourComparisonResult.TourTravelTipChange tip : comparisonResult.getTravelTipsToUpdate()) {
                sb.append("<div style='margin-bottom:12px;padding:10px;background:#fff8e7;border-color:#ffd700;border-radius:6px;'>")
                        .append("<strong>Travel Tip ID:</strong> ").append(tip.getTravelTipId()).append("<br/>")
                        .append("<strong>📌 Title:</strong> ").append(escapeHtml(tip.getTipTitle())).append("<br/>")
                        .append("<strong>Description:</strong> ").append(escapeHtml(tip.getTipDescription())).append("<br/>")
                        .append("<strong>Display Order:</strong> ").append(tip.getDisplayOrder()).append("<br/>")
                        .append("<strong>Status:</strong> ").append(buildStatusPill(tip.getStatus()))
                        .append("</div>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildTerminateTourTypesHtml(List<com.felicita.model.dto.TourTypeDto> tourTypes) {
        if (tourTypes == null || tourTypes.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No tour types assigned</span>";
        }
        StringBuilder sb = new StringBuilder("<div style='display:flex;flex-wrap:wrap;gap:8px;'>");
        for (com.felicita.model.dto.TourTypeDto type : tourTypes) {
            sb.append("<span class='tag'>").append(escapeHtml(type.getTourTypeName())).append("</span>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String buildTerminateTourCategoriesHtml(List<com.felicita.model.dto.TourCategoryDto> categories) {
        if (categories == null || categories.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No tour categories assigned</span>";
        }
        StringBuilder sb = new StringBuilder("<div style='display:flex;flex-wrap:wrap;gap:8px;'>");
        for (com.felicita.model.dto.TourCategoryDto cat : categories) {
            sb.append("<span class='tag'>").append(escapeHtml(cat.getTourCategoryName())).append("</span>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String buildTerminateTourInclusionsHtml(List<com.felicita.model.response.TourExtrasResponse.TourInclusion> inclusions) {
        if (inclusions == null || inclusions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No inclusions</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (com.felicita.model.response.TourExtrasResponse.TourInclusion inc : inclusions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>").append(escapeHtml(inc.getDescription())).append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildTerminateTourExclusionsHtml(List<TourExtrasResponse.TourExclusion> exclusions) {
        if (exclusions == null || exclusions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No exclusions</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (com.felicita.model.response.TourExtrasResponse.TourExclusion exc : exclusions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>").append(escapeHtml(exc.getDescription())).append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildTerminateTourConditionsHtml(List<com.felicita.model.response.TourExtrasResponse.TourCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No conditions</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (com.felicita.model.response.TourExtrasResponse.TourCondition cond : conditions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>").append(escapeHtml(cond.getDescription())).append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildTerminateTourTravelTipsHtml(List<com.felicita.model.response.TourExtrasResponse.TourTravelTip> travelTips) {
        if (travelTips == null || travelTips.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No travel tips</span>";
        }
        StringBuilder sb = new StringBuilder();
        for (com.felicita.model.response.TourExtrasResponse.TourTravelTip tip : travelTips) {
            sb.append("<div style='margin-bottom:12px;padding:10px;background:#f9fdfd;border-radius:6px;'>")
                    .append("<strong>📌 ").append(escapeHtml(tip.getDescription())).append("</strong>")
                    .append("<p style='margin:5px 0 0 0;font-size:13px;color:#555;'>").append(escapeHtml(tip.getDescription())).append("</p>")
                    .append("</div>");
        }
        return sb.toString();
    }

    private String buildTerminateTourImagesHtml(List<com.felicita.model.dto.TourImageResponseDto> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><td><th>#</th><th>Name</th><th>Description</th><th>Preview</th></tr></thead><tbody>");
        int i = 1;
        for (com.felicita.model.dto.TourImageResponseDto img : images) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(img.getImageName())).append("</td>")
                    .append("<td>").append(img.getImageDescription() != null ? escapeHtml(img.getImageDescription()) : "—").append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    // Missing helper methods - add these to your TourEmailHelperServiceImpl class

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
        if (value instanceof java.math.BigDecimal) {
            return "$" + value.toString();
        }
        if (value instanceof java.time.LocalTime) {
            return value.toString();
        }
        if (value instanceof java.time.LocalDate) {
            return value.toString();
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
