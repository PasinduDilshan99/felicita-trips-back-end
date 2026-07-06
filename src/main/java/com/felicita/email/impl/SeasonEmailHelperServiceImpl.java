package com.felicita.email.impl;

import com.felicita.email.SeasonEmailHelperService;
import com.felicita.model.other.SeasonUpdateComparisonResult;
import com.felicita.model.request.seasons.SeasonImageInsertRequest;
import com.felicita.model.request.seasons.SeasonInsertRequest;
import com.felicita.model.response.seasons.SeasonAllDetailsResponse;
import com.felicita.model.response.seasons.SeasonImageResponse;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class SeasonEmailHelperServiceImpl implements SeasonEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeasonEmailHelperServiceImpl.class);

    @Override
    public String buildSeasonTerminateSuccessfullSubject(User loggedUser, SeasonAllDetailsResponse seasonResponse) {
        return String.format("[Felicita Trips] Season Terminated — %s",
                seasonResponse.getName() != null ?
                        seasonResponse.getName() : "Unknown Season");
    }

    @Override
    public String buildSeasonTerminateSuccessfullBody(User loggedUser, SeasonAllDetailsResponse seasonResponse) {
        String imagesHtml = buildTerminateSeasonImagesHtml(seasonResponse.getSeasonImages());
        String activitiesHtml = buildTerminateSeasonActivitiesHtml(seasonResponse.getActivities());
        String toursHtml = buildTerminateSeasonToursHtml(seasonResponse.getTours());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Season Terminated</title>" +
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
                ".season-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".peak-badge{display:inline-block;padding:2px 8px;border-radius:12px;font-size:11px;background:#ffd700;color:#6b4c00;margin-left:8px;}" +
                ".weather-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;margin:4px;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#8B0000,#cd5c5c);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".reference-id{font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".activity-tag{display:inline-block;padding:6px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;margin:4px;}" +
                ".tour-tag{display:inline-block;padding:6px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;margin:4px;}" +
                ".info-metadata{background:#f9fdfd;border:1px solid #c8e8e8;border-radius:8px;padding:16px 20px;margin-top:16px;}" +
                ".info-metadata-row{display:flex;align-items:center;padding:4px 0;font-size:12px;}" +
                ".info-metadata-label{font-family:Arial,sans-serif;color:#6b8e8e;min-width:100px;}" +
                ".info-metadata-value{font-family:Arial,sans-serif;color:#1a3333;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Season Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This season has been terminated</strong> and is no longer available for assignment to activities or tours.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + escapeHtml(loggedUser.getEmail()) + " &nbsp;|&nbsp; " + escapeHtml(loggedUser.getUsername()) + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Season Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Season ID</span><span class='info-value'><span class='reference-id'>#" + seasonResponse.getId() + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Season Name</span><span class='info-value'><span class='season-icon'>🌸</span>" + escapeHtml(seasonResponse.getName()) +
                (Boolean.TRUE.equals(seasonResponse.getIsPeak()) ? "<span class='peak-badge'>Peak Season</span>" : "") +
                "</span></div>" +
                "<div class='info-row'><span class='info-label'>Standard Name</span><span class='info-value'>" + (seasonResponse.getStandardName() != null ? escapeHtml(seasonResponse.getStandardName()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Local Name</span><span class='info-value'>" + (seasonResponse.getLocalName() != null ? escapeHtml(seasonResponse.getLocalName()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Season Period</span><span class='info-value'>" + getMonthName(seasonResponse.getStartMonth()) + " - " + getMonthName(seasonResponse.getEndMonth()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Weather</span><span class='info-value'>" +
                "<span class='weather-tag'>🌡️ " + (seasonResponse.getTemperatureMin() != null ? seasonResponse.getTemperatureMin() : "—") + "°C - " + (seasonResponse.getTemperatureMax() != null ? seasonResponse.getTemperatureMax() : "—") + "°C</span>" +
                "<span class='weather-tag'>💧 " + (seasonResponse.getMonsoonType() != null ? escapeHtml(seasonResponse.getMonsoonType()) : "—") + "</span>" +
                "<span class='weather-tag'>☔ " + (seasonResponse.getRainfallPattern() != null ? escapeHtml(seasonResponse.getRainfallPattern()) : "—") + "</span>" +
                "</span></div>" +
                "<div class='info-row'><span class='info-label'>Weather Summary</span><span class='info-value'>" + (seasonResponse.getWeatherSummary() != null ? escapeHtml(seasonResponse.getWeatherSummary()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (seasonResponse.getDescription() != null ? escapeHtml(seasonResponse.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Display Order</span><span class='info-value'>" + (seasonResponse.getDisplayOrder() != null ? seasonResponse.getDisplayOrder() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(String.valueOf(seasonResponse.getStatus())) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'><span class='status-pill-terminated'>TERMINATED</span></span></div>" +
                "</div>" +

                (imagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Season Images (" + (seasonResponse.getSeasonImages() != null ? seasonResponse.getSeasonImages().size() : 0) + ")</p>" +
                                imagesHtml) +

                (activitiesHtml.isEmpty() ? "" : activitiesHtml) +
                (toursHtml.isEmpty() ? "" : toursHtml) +

                "<p class='section-title'>Audit Information</p>" +
                "<div class='info-metadata'>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Created By</span><span class='info-metadata-value'>" +
                (seasonResponse.getCreatedBy() != null ? "User #" + seasonResponse.getCreatedBy() : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Created At</span><span class='info-metadata-value'>" +
                (seasonResponse.getCreatedAt() != null ? formatLocalDateTime(seasonResponse.getCreatedAt()) : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Last Updated By</span><span class='info-metadata-value'>" +
                (seasonResponse.getUpdatedBy() != null ? "User #" + seasonResponse.getUpdatedBy() : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Last Updated At</span><span class='info-metadata-value'>" +
                (seasonResponse.getUpdatedAt() != null ? formatLocalDateTime(seasonResponse.getUpdatedAt()) : "—") +
                "</span></div>" +
                "</div>" +

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
    public String buildSeasonCreateSuccessfullSubject(SeasonInsertRequest seasonInsertRequest, Long seasonId, User loggedUser) {
        return String.format("[Felicita Trips] Season Created — %s",
                seasonInsertRequest.getName() != null ?
                        seasonInsertRequest.getName() : "Unknown Season");
    }

    @Override
    public String buildSeasonUpdateSuccessfullSubject(User loggedUser, Long id) {
        return String.format("[Felicita Trips] Season Updated — Season #%d", id);
    }

    @Override
    public String buildSeasonUpdateSuccessfullBody(User loggedUser, Long id, SeasonUpdateComparisonResult comparisonResult) {
        String basicFieldsHtml = buildSeasonBasicFieldsHtml(comparisonResult.getFieldChanges());
        String activitiesHtml = buildSeasonActivitiesChangesHtml(comparisonResult);
        String toursHtml = buildSeasonToursChangesHtml(comparisonResult);
        String imagesHtml = buildSeasonImageChangesHtml(comparisonResult);
        boolean hasAnyUpdates = comparisonResult != null && comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Season Updated</title>" +
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
                ".info-row:last-child{border-bottom:none;}" +
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:140px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".updated-fields-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".updated-fields-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".updated-fields-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".updated-fields-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".old-value{color:#a33;text-decoration:line-through;background:#fdecea;padding:2px 6px;border-radius:4px;display:inline-block;}" +
                ".new-value{color:#1a6b40;background:#d4f4e8;padding:2px 6px;border-radius:4px;display:inline-block;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".status-pending{background:#fff8e7;color:#b8860b;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".no-updates{text-align:center;color:#6b8e8e;font-style:italic;padding:24px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;}" +
                ".reference-id{font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;}" +
                ".warnings-box{background:#fff8e7;border-left:3px solid #ffd700;padding:12px 16px;margin-bottom:24px;border-radius:6px;}" +
                ".warnings-box p{margin:0;font-size:13px;color:#6b4c00;}" +
                ".warning-icon{font-size:16px;margin-right:8px;}" +
                ".validation-box{background:#e8f5f5;border:1px solid #c8e8e8;border-radius:8px;padding:16px 20px;margin-bottom:24px;}" +
                ".validation-title{font-size:12px;font-weight:bold;color:#0e7c7b;margin-bottom:8px;font-family:Arial,sans-serif;}" +
                ".validation-item{display:flex;align-items:center;gap:12px;padding:4px 0;font-size:13px;}" +
                ".validation-label{color:#6b8e8e;min-width:140px;}" +
                ".validation-value{color:#1a3333;}" +
                ".validation-warning{color:#b8860b;}" +
                ".validation-error{color:#a33;}" +
                ".validation-success{color:#1a6b40;}" +
                ".activity-tag{display:inline-block;padding:6px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;margin:4px;}" +
                ".activity-tag-added{background:#d4f4e8;border-color:#90d0c0;color:#1a6b40;}" +
                ".activity-tag-removed{background:#fdecea;border-color:#e0b0b0;color:#a33;text-decoration:line-through;}" +
                ".tour-tag{display:inline-block;padding:6px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;margin:4px;}" +
                ".tour-tag-added{background:#d4f4e8;border-color:#90d0c0;color:#1a6b40;}" +
                ".tour-tag-removed{background:#fdecea;border-color:#e0b0b0;color:#a33;text-decoration:line-through;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Season Updated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#9998; Update Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<p class='section-title'>Updated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + escapeHtml(loggedUser.getEmail()) + " &nbsp;|&nbsp; " + escapeHtml(loggedUser.getUsername()) + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Season Information</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Season ID</span><span class='info-value'><span class='reference-id'>#" + id + "</span></span></div>" +
                "</div>" +

                (comparisonResult != null && !comparisonResult.getWarnings().isEmpty() ?
                        "<div class='warnings-box'>" +
                                "<span class='warning-icon'>⚠️</span>" +
                                "<p><strong>Warnings:</strong> " + String.join("; ", comparisonResult.getWarnings()) + "</p>" +
                                "</div>" : "") +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this season.</div>") +

                (basicFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + basicFieldsHtml + "</tbody>" +
                                "</table><br/>") +

                (comparisonResult != null && comparisonResult.getOldStatus() != null && comparisonResult.getNewStatus() != null &&
                        !comparisonResult.getOldStatus().equals(comparisonResult.getNewStatus()) ?
                        "<p class='section-title'>Status Change</p>" +
                                "<div class='info-card'>" +
                                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(String.valueOf(comparisonResult.getOldStatus())) + "</span></div>" +
                                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'>" + buildStatusPill(String.valueOf(comparisonResult.getNewStatus())) + "</span></div>" +
                                "</div>" : "") +

                (comparisonResult != null && (!comparisonResult.isMonthRangeValid() || !comparisonResult.isTemperatureValid()) ?
                        "<p class='section-title'>Validation Information</p>" +
                                "<div class='validation-box'>" +
                                "<div class='validation-title'>Season Validation Results</div>" +

                                (comparisonResult.getMonthsSpan() != null ?
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Months Span:</span>" +
                                                "<span class='validation-value'>" + comparisonResult.getMonthsSpan() + " months</span>" +
                                                "</div>" : "") +

                                (!comparisonResult.isMonthRangeValid() ?
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Month Range Status:</span>" +
                                                "<span class='validation-value validation-warning'>⚠️ Warning - End month should be after start month</span>" +
                                                "</div>" :
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Month Range Status:</span>" +
                                                "<span class='validation-value validation-success'>✓ Valid</span>" +
                                                "</div>") +

                                (!comparisonResult.isTemperatureValid() ?
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Temperature Range Status:</span>" +
                                                "<span class='validation-value validation-warning'>⚠️ Warning - Max temperature should be greater than min temperature</span>" +
                                                "</div>" :
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Temperature Range Status:</span>" +
                                                "<span class='validation-value validation-success'>✓ Valid</span>" +
                                                "</div>") +

                                "</div>" : "") +

                (activitiesHtml.isEmpty() ? "" : activitiesHtml) +
                (toursHtml.isEmpty() ? "" : toursHtml) +
                (imagesHtml.isEmpty() ? "" : imagesHtml) +

                (comparisonResult != null && comparisonResult.getChangeTimestamp() != null ?
                        "<div class='info-card'>" +
                                "<div class='info-row'><span class='info-label'>Change Timestamp</span><span class='info-value'>" + comparisonResult.getChangeTimestamp() + "</span></div>" +
                                "</div>" : "") +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Admin Portal</p>" +
                "<p>This is an automated notification from the Felicita Trips admin system.</p>" +
                "<p>Please do not reply to this email.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

    private String buildSeasonBasicFieldsHtml(List<SeasonUpdateComparisonResult.FieldChange> fieldChanges) {
        if (fieldChanges == null || fieldChanges.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (SeasonUpdateComparisonResult.FieldChange field : fieldChanges) {
            String displayOldValue = formatValueForDisplay(field.getOldValue());
            String displayNewValue = formatValueForDisplay(field.getNewValue());

            sb.append("<tr>")
                    .append("<td><strong>").append(formatFieldName(field.getFieldName())).append("</strong></td>")
                    .append("<td><span class='old-value'>").append(displayOldValue).append("</span></td>")
                    .append("<td><span class='new-value'>").append(displayNewValue).append("</span></td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String formatValueForDisplay(Object value) {
        if (value == null) return "—";
        if (value instanceof Boolean) {
            return (Boolean) value ? "Yes" : "No";
        }
        if (value instanceof Integer) {
            // Handle status integer values
            Integer intValue = (Integer) value;
            if (intValue == 1) return "Active";
            if (intValue == 0) return "Inactive";
            if (intValue == 2) return "Pending";
            return String.valueOf(intValue);
        }
        if (value instanceof java.math.BigDecimal) {
            return "$" + value.toString();
        }
        if (value instanceof java.time.LocalDate) {
            return formatLocalDate((java.time.LocalDate) value);
        }
        if (value instanceof java.time.LocalDateTime) {
            return formatLocalDateTime((java.time.LocalDateTime) value);
        }
        if (value instanceof java.util.Date) {
            return formatDateTime((java.util.Date) value);
        }
        if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        }
        return escapeHtml(value.toString());
    }

    private String formatLocalDate(java.time.LocalDate date) {
        if (date == null) return "—";
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return date.format(formatter);
    }

    private String formatDateTime(java.util.Date date) {
        if (date == null) return "—";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        return sdf.format(date);
    }

    private String formatFieldName(String fieldName) {
        if (fieldName == null) return "";
        // Convert camelCase to readable format
        String readable = fieldName.replaceAll("([A-Z])", " $1").toLowerCase();
        readable = readable.substring(0, 1).toUpperCase() + readable.substring(1);

        // Special case replacements for better readability
        readable = readable.replace("Is peak", "Peak Season");
        readable = readable.replace("Start month", "Start Month");
        readable = readable.replace("End month", "End Month");
        readable = readable.replace("Monsoon type", "Monsoon Type");
        readable = readable.replace("Weather summary", "Weather Summary");
        readable = readable.replace("Rainfall pattern", "Rainfall Pattern");
        readable = readable.replace("Display order", "Display Order");
        readable = readable.replace("Standard name", "Standard Name");
        readable = readable.replace("Local name", "Local Name");
        readable = readable.replace("Temperature min", "Minimum Temperature");
        readable = readable.replace("Temperature max", "Maximum Temperature");

        return readable;
    }

    private String buildSeasonActivitiesChangesHtml(SeasonUpdateComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getActivitiesToRemove() != null && !comparisonResult.getActivitiesToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Activities</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long activityId : comparisonResult.getActivitiesToRemove()) {
                sb.append("<span class='activity-tag activity-tag-removed'>Activity ID: #").append(activityId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getActivitiesToAdd() != null && !comparisonResult.getActivitiesToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Activities</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long activityId : comparisonResult.getActivitiesToAdd()) {
                sb.append("<span class='activity-tag activity-tag-added'>Activity ID: #").append(activityId).append("</span>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildSeasonToursChangesHtml(SeasonUpdateComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getToursToRemove() != null && !comparisonResult.getToursToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Tours</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long tourId : comparisonResult.getToursToRemove()) {
                sb.append("<span class='tour-tag tour-tag-removed'>Tour ID: #").append(tourId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getToursToAdd() != null && !comparisonResult.getToursToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Tours</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long tourId : comparisonResult.getToursToAdd()) {
                sb.append("<span class='tour-tag tour-tag-added'>Tour ID: #").append(tourId).append("</span>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildSeasonImageChangesHtml(SeasonUpdateComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getImagesToRemove() != null && !comparisonResult.getImagesToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Images</p>")
                    .append("<table class='info-table'><thead><tr><th>Image ID</th><th>Name</th><th>Preview</th></tr></thead><tbody>");
            for (Long imgId : comparisonResult.getImagesToRemove()) {
                sb.append("tr<td>").append(imgId).append("</td><td colspan='2'><span class='old-value'>Image ID: ").append(imgId).append("</span></td></tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        if (comparisonResult.getImagesToAdd() != null && !comparisonResult.getImagesToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Images</p>")
                    .append("<table class='info-table'><thead><tr><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
            for (SeasonUpdateComparisonResult.ImageChange img : comparisonResult.getImagesToAdd()) {
                sb.append("<tr>")
                        .append("<td>").append(escapeHtml(img.getName())).append("</td>")
                        .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                        .append("<td>").append(buildStatusPill(String.valueOf(img.getStatus()))).append("</td>")
                        .append("<td>").append(img.getImageUrl() != null ? "<a href='" + img.getImageUrl() + "' target='_blank'>🔗 View</a>" : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        if (comparisonResult.getImagesToUpdate() != null && !comparisonResult.getImagesToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Images</p>")
                    .append("<table class='info-table'><thead><tr><th>Image ID</th><th>Field</th><th>Old Value</th><th>New Value</th></tr></thead><tbody>");
            for (SeasonUpdateComparisonResult.ImageUpdateChange img : comparisonResult.getImagesToUpdate()) {
                // Name change
                if (img.getOldName() != null && img.getNewName() != null && !img.getOldName().equals(img.getNewName())) {
                    sb.append("<tr>")
                            .append("<td>").append(img.getImageId()).append("</td>")
                            .append("<td>Name</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(img.getOldName())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(img.getNewName())).append("</span></td>")
                            .append("</tr>");
                }
                // Description change
                if (img.getOldDescription() != null && img.getNewDescription() != null && !img.getOldDescription().equals(img.getNewDescription())) {
                    sb.append("<tr>")
                            .append("<td>").append(img.getImageId()).append("</td>")
                            .append("<td>Description</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(img.getOldDescription())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(img.getNewDescription())).append("</span></td>")
                            .append("</tr>");
                }
                // URL change
                if (img.getOldImageUrl() != null && img.getNewImageUrl() != null && !img.getOldImageUrl().equals(img.getNewImageUrl())) {
                    sb.append("<tr>")
                            .append("<td>").append(img.getImageId()).append("</td>")
                            .append("<td>Image URL</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(img.getOldImageUrl())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(img.getNewImageUrl())).append("</span></td>")
                            .append("</tr>");
                }
                // Status change
                if (img.getOldStatus() != null && img.getNewStatus() != null && !img.getOldStatus().equals(img.getNewStatus())) {
                    sb.append("<tr>")
                            .append("<td>").append(img.getImageId()).append("</td>")
                            .append("<td>Status</td>")
                            .append("<td><span class='old-value'>").append(buildStatusPill(String.valueOf(img.getOldStatus()))).append("</span></td>")
                            .append("<td><span class='new-value'>").append(buildStatusPill(String.valueOf(img.getNewStatus()))).append("</span></td>")
                            .append("</tr>");
                }
            }
            sb.append("</tbody></table><br/>");
        }

        return sb.toString();
    }

    @Override
    public String buildSeasonCreateSuccessfullBody(SeasonInsertRequest seasonInsertRequest, Long seasonId, User loggedUser) {
        String imagesHtml = buildCreateSeasonImagesHtml(seasonInsertRequest.getImageInsertRequests());
        String activitiesHtml = buildCreateSeasonActivitiesHtml(seasonInsertRequest.getInsertActivitiesIds());
        String toursHtml = buildCreateSeasonToursHtml(seasonInsertRequest.getInsertTourIds());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Season Created</title>" +
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
                ".season-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".peak-badge{display:inline-block;padding:2px 8px;border-radius:12px;font-size:11px;background:#ffd700;color:#6b4c00;margin-left:8px;}" +
                ".weather-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;margin:4px;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".activity-tag{display:inline-block;padding:6px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;margin:4px;}" +
                ".tour-tag{display:inline-block;padding:6px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;margin:4px;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".reference-id{font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;font-size:13px;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Season Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#127912; New Season Added</span>" +
                "</div>" +

                "<div class='content'>" +

                "<p class='section-title'>Created By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + escapeHtml(loggedUser.getEmail()) + " &nbsp;|&nbsp; " + escapeHtml(loggedUser.getUsername()) + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Season Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Season ID</span><span class='info-value'><span class='reference-id'>#" + seasonId + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Season Name</span><span class='info-value'><span class='season-icon'>🌸</span>" + escapeHtml(seasonInsertRequest.getName()) +
                (Boolean.TRUE.equals(seasonInsertRequest.getIsPeak()) ? "<span class='peak-badge'>Peak Season</span>" : "") +
                "</span></div>" +
                "<div class='info-row'><span class='info-label'>Standard Name</span><span class='info-value'>" + (seasonInsertRequest.getStandardName() != null ? escapeHtml(seasonInsertRequest.getStandardName()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Local Name</span><span class='info-value'>" + (seasonInsertRequest.getLocalName() != null ? escapeHtml(seasonInsertRequest.getLocalName()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Season Period</span><span class='info-value'>" + getMonthName(seasonInsertRequest.getStartMonth()) + " - " + getMonthName(seasonInsertRequest.getEndMonth()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Weather</span><span class='info-value'>" +
                "<span class='weather-tag'>🌡️ " + (seasonInsertRequest.getTemperatureMin() != null ? seasonInsertRequest.getTemperatureMin() : "—") + "°C - " + (seasonInsertRequest.getTemperatureMax() != null ? seasonInsertRequest.getTemperatureMax() : "—") + "°C</span>" +
                "<span class='weather-tag'>💧 " + (seasonInsertRequest.getMonsoonType() != null ? escapeHtml(seasonInsertRequest.getMonsoonType()) : "—") + "</span>" +
                "<span class='weather-tag'>☔ " + (seasonInsertRequest.getRainfallPattern() != null ? escapeHtml(seasonInsertRequest.getRainfallPattern()) : "—") + "</span>" +
                "</span></div>" +
                "<div class='info-row'><span class='info-label'>Weather Summary</span><span class='info-value'>" + (seasonInsertRequest.getWeatherSummary() != null ? escapeHtml(seasonInsertRequest.getWeatherSummary()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (seasonInsertRequest.getDescription() != null ? escapeHtml(seasonInsertRequest.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Display Order</span><span class='info-value'>" + (seasonInsertRequest.getDisplayOrder() != null ? seasonInsertRequest.getDisplayOrder() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(seasonInsertRequest.getStatus()) + "</span></div>" +
                "</div>" +

                (imagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Images (" + (seasonInsertRequest.getImageInsertRequests() != null ? seasonInsertRequest.getImageInsertRequests().size() : 0) + ")</p>" +
                                imagesHtml) +

                (activitiesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Linked Activities (" + (seasonInsertRequest.getInsertActivitiesIds() != null ? seasonInsertRequest.getInsertActivitiesIds().size() : 0) + ")</p>" +
                                "<div class='info-card'>" +
                                "<div style='display:flex;flex-wrap:wrap;gap:8px;'>" +
                                activitiesHtml +
                                "</div>" +
                                "</div>") +

                (toursHtml.isEmpty() ? "" :
                        "<p class='section-title'>Linked Tours (" + (seasonInsertRequest.getInsertTourIds() != null ? seasonInsertRequest.getInsertTourIds().size() : 0) + ")</p>" +
                                "<div class='info-card'>" +
                                "<div style='display:flex;flex-wrap:wrap;gap:8px;'>" +
                                toursHtml +
                                "</div>" +
                                "</div>") +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Admin Portal</p>" +
                "<p>This is an automated notification from the Felicita Trips admin system.</p>" +
                "<p>Please do not reply to this email.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

    private String buildCreateSeasonImagesHtml(List<SeasonImageInsertRequest> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
        int i = 1;
        for (SeasonImageInsertRequest img : images) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(img.getName())).append("</td>")
                    .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                    .append("<td>").append(buildStatusPill(String.valueOf(img.getStatus()))).append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildCreateSeasonActivitiesHtml(List<Long> activityIds) {
        if (activityIds == null || activityIds.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Long activityId : activityIds) {
            sb.append("<span class='activity-tag'>Activity ID: #").append(activityId).append("</span>");
        }
        return sb.toString();
    }

    private String buildCreateSeasonToursHtml(List<Long> tourIds) {
        if (tourIds == null || tourIds.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Long tourId : tourIds) {
            sb.append("<span class='tour-tag'>Tour ID: #").append(tourId).append("</span>");
        }
        return sb.toString();
    }

    private String buildTerminateSeasonImagesHtml(List<SeasonImageResponse> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Description</th><th>Preview</th></tr></thead><tbody>");
        int i = 1;
        for (SeasonImageResponse img : images) {
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

    private String buildTerminateSeasonActivitiesHtml(List<SeasonAllDetailsResponse.SeasonActivity> activities) {
        if (activities == null || activities.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Associated Activities (").append(activities.size()).append(")</p>")
                .append("<div class='info-card'><div style='display:flex;flex-wrap:wrap;gap:8px;'>");
        for (SeasonAllDetailsResponse.SeasonActivity activity : activities) {
            sb.append("<span class='activity-tag'>")
                    .append("Activity: #").append(activity.getActivityId())
                    .append(" - ").append(escapeHtml(activity.getActivityName()))
                    .append("</span>");
        }
        sb.append("</div></div>");
        return sb.toString();
    }

    private String buildTerminateSeasonToursHtml(List<SeasonAllDetailsResponse.SeasonTour> tours) {
        if (tours == null || tours.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Associated Tours (").append(tours.size()).append(")</p>")
                .append("<div class='info-card'><div style='display:flex;flex-wrap:wrap;gap:8px;'>");
        for (SeasonAllDetailsResponse.SeasonTour tour : tours) {
            sb.append("<span class='tour-tag'>")
                    .append("Tour: #").append(tour.getTourId())
                    .append(" - ").append(escapeHtml(tour.getTourName()))
                    .append("</span>");
        }
        sb.append("</div></div>");
        return sb.toString();
    }

    private String getMonthName(Integer month) {
        if (month == null || month < 1 || month > 12) return "—";
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return months[month - 1];
    }

    private String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "—";
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    private String buildStatusPill(String status) {
        if (status == null) {
            return "<span class='status-pill status-inactive'>INACTIVE</span>";
        }
        if ("ACTIVE".equalsIgnoreCase(status) || "1".equals(status)) {
            return "<span class='status-pill status-active'>ACTIVE</span>";
        } else if ("PENDING".equalsIgnoreCase(status) || "2".equals(status)) {
            return "<span class='status-pill status-pending'>PENDING</span>";
        } else {
            return "<span class='status-pill status-inactive'>INACTIVE</span>";
        }
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

}
