package com.felicita.email.impl;

import com.felicita.email.HeroSectionEmailHelperService;
import com.felicita.model.other.HeroSectionComparisonResult;
import com.felicita.model.request.heroSection.HeroSectionInsertRequest;
import com.felicita.model.response.heroSection.HeroSectionDetailsResponse;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HeroSectionEmailHelperServiceImpl implements HeroSectionEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeroSectionEmailHelperServiceImpl.class);

    @Override
    public String buildHeroSectionCreateSuccessfullSubject(HeroSectionInsertRequest heroSectionInsertRequest, Long heroSectionId, User loggedUser) {
        return String.format("[Felicita Trips] Hero Section Created — %s",
                heroSectionInsertRequest.getName() != null ?
                        heroSectionInsertRequest.getName() : "Unknown Hero");
    }

    @Override
    public String buildHeroSectionUpdateSuccessfullSubject(User loggedUser, Long heroSectionId) {
        return String.format("[Felicita Trips] Hero Section Updated — #%d", heroSectionId);
    }

    @Override
    public String buildHeroSectionUpdateSuccessfullBody(User loggedUser, Long heroSectionId, HeroSectionComparisonResult comparisonResult) {
        String basicFieldsHtml = buildHeroSectionBasicFieldsHtml(comparisonResult.getFieldChanges());
        String orderChangeHtml = buildHeroSectionOrderChangeHtml(comparisonResult);
        boolean hasAnyUpdates = comparisonResult != null && comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Hero Section Updated</title>" +
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
                ".order-change-box{background:#f5fbfb;border:2px solid #0e7c7b;border-radius:8px;padding:16px 20px;margin-bottom:24px;text-align:center;}" +
                ".order-change-box .arrow-icon{font-size:24px;color:#0e7c7b;margin:0 12px;}" +
                ".order-old{font-size:18px;color:#a33;font-weight:bold;}" +
                ".order-new{font-size:18px;color:#1a6b40;font-weight:bold;}" +
                ".order-label{font-size:11px;color:#6b8e8e;text-transform:uppercase;letter-spacing:1px;}" +
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
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Hero Section Updated</h1>" +
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

                "<p class='section-title'>Hero Section Information</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Hero ID</span><span class='info-value'><span class='reference-id'>#" + heroSectionId + "</span></span></div>" +
                "</div>" +

                (comparisonResult != null && !comparisonResult.getWarnings().isEmpty() ?
                        "<div class='warnings-box'>" +
                                "<span class='warning-icon'>⚠️</span>" +
                                "<p><strong>Warnings:</strong> " + String.join("; ", comparisonResult.getWarnings()) + "</p>" +
                                "</div>" : "") +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this hero section.</div>") +

                (basicFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + basicFieldsHtml + "</tbody>" +
                                "</table><br/>") +

                (comparisonResult != null && comparisonResult.getOldStatusId() != null && comparisonResult.getNewStatusId() != null &&
                        !comparisonResult.getOldStatusId().equals(comparisonResult.getNewStatusId()) ?
                        "<p class='section-title'>Status Change</p>" +
                                "<div class='info-card'>" +
                                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + getStatusName(comparisonResult.getOldStatusId()) + "</span></div>" +
                                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'>" + getStatusName(comparisonResult.getNewStatusId()) + "</span></div>" +
                                "</div>" : "") +

                (orderChangeHtml.isEmpty() ? "" : orderChangeHtml) +

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

    @Override
    public String buildHeroSectionTerminateSuccessfullSubject(User loggedUser, HeroSectionDetailsResponse heroSectionData) {
        return String.format("[Felicita Trips] Hero Section Terminated — %s",
                heroSectionData.getName() != null ?
                        heroSectionData.getName() : "Unknown Hero");
    }

    @Override
    public String buildHeroSectionTerminateSuccessfullBody(User loggedUser, HeroSectionDetailsResponse heroSectionData, String heroSectionType) {
        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Hero Section Terminated</title>" +
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
                ".hero-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".type-badge{display:inline-block;padding:2px 10px;border-radius:12px;font-size:11px;font-weight:bold;text-transform:uppercase;}" +
                ".type-badge-hero{background:#e8f5f5;color:#0e7c7b;}" +
                ".type-badge-banner{background:#fff8e7;color:#b8860b;}" +
                ".type-badge-featured{background:#f3e5f5;color:#4a148c;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#8B0000,#cd5c5c);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".reference-id{font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;}" +
                ".image-preview{max-width:100px;max-height:60px;border-radius:4px;border:1px solid #c8e8e8;}" +
                ".button-preview{display:inline-block;padding:6px 12px;background:#0e7c7b;color:#ffffff;border-radius:4px;font-size:12px;text-decoration:none;}" +
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
                "<h1>Hero Section Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This hero section has been terminated</strong> and is no longer available for display on the website.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + escapeHtml(loggedUser.getEmail()) + " &nbsp;|&nbsp; " + escapeHtml(loggedUser.getUsername()) + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Hero Section Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Hero ID</span><span class='info-value'><span class='reference-id'>#" + heroSectionData.getId() + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Name</span><span class='info-value'><span class='hero-icon'>🌟</span>" + escapeHtml(heroSectionData.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Type</span><span class='info-value'>" + getHeroTypeBadge(heroSectionType) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Order</span><span class='info-value'>" + (heroSectionData.getOrder() != null ? heroSectionData.getOrder() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + getStatusName(heroSectionData.getStatusId()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'><span class='status-pill-terminated'>TERMINATED</span></span></div>" +
                (heroSectionData.getImageUrl() != null && !heroSectionData.getImageUrl().isEmpty() ?
                        "<div class='info-row'><span class='info-label'>Image</span><span class='info-value'><img src='" + heroSectionData.getImageUrl() + "' class='image-preview' alt='Hero Image'/><br/><a href='" + heroSectionData.getImageUrl() + "' target='_blank' style='color:#0e7c7b;font-size:12px;'>View Full Image</a></span></div>" : "") +
                "</div>" +

                "<p class='section-title'>Content Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Title</span><span class='info-value'>" + (heroSectionData.getTitle() != null ? escapeHtml(heroSectionData.getTitle()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Subtitle</span><span class='info-value'>" + (heroSectionData.getSubtitle() != null ? escapeHtml(heroSectionData.getSubtitle()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (heroSectionData.getDescription() != null ? escapeHtml(heroSectionData.getDescription()) : "—") + "</span></div>" +
                "</div>" +

                (heroSectionData.getPrimaryButtonText() != null || heroSectionData.getSecondaryButtonText() != null ?
                        "<p class='section-title'>Action Buttons</p>" +
                                "<div class='info-card'>" +
                                (heroSectionData.getPrimaryButtonText() != null ?
                                        "<div class='info-row'><span class='info-label'>Primary Button</span><span class='info-value'><span class='button-preview'>" + escapeHtml(heroSectionData.getPrimaryButtonText()) + "</span> → " + (heroSectionData.getPrimaryButtonLink() != null ? escapeHtml(heroSectionData.getPrimaryButtonLink()) : "—") + "</span></div>" : "") +
                                (heroSectionData.getSecondaryButtonText() != null ?
                                        "<div class='info-row'><span class='info-label'>Secondary Button</span><span class='info-value'><span style='display:inline-block;padding:6px 12px;background:#f5fbfb;border:1px solid #0e7c7b;color:#0e7c7b;border-radius:4px;font-size:12px;'>" + escapeHtml(heroSectionData.getSecondaryButtonText()) + "</span> → " + (heroSectionData.getSecondaryButtonLink() != null ? escapeHtml(heroSectionData.getSecondaryButtonLink()) : "—") + "</span></div>" : "") +
                                "</div>" : "") +

                "<p class='section-title'>Audit Information</p>" +
                "<div class='info-metadata'>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Created By</span><span class='info-metadata-value'>" +
                (heroSectionData.getCreatedBy() != null ? "User #" + heroSectionData.getCreatedBy() : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Created At</span><span class='info-metadata-value'>" +
                (heroSectionData.getCreatedAt() != null ? formatLocalDateTime(heroSectionData.getCreatedAt()) : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Last Updated By</span><span class='info-metadata-value'>" +
                (heroSectionData.getUpdatedBy() != null ? "User #" + heroSectionData.getUpdatedBy() : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Last Updated At</span><span class='info-metadata-value'>" +
                (heroSectionData.getUpdatedAt() != null ? formatLocalDateTime(heroSectionData.getUpdatedAt()) : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Terminated By</span><span class='info-metadata-value'>" +
                (heroSectionData.getTerminatedBy() != null ? "User #" + heroSectionData.getTerminatedBy() : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Terminated At</span><span class='info-metadata-value'>" +
                (heroSectionData.getTerminatedAt() != null ? formatLocalDateTime(heroSectionData.getTerminatedAt()) : "—") +
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
    public String buildHeroSectionCreateSuccessfullBody(HeroSectionInsertRequest heroSectionInsertRequest, Long heroSectionId, User loggedUser) {
        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Hero Section Created</title>" +
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
                ".hero-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".status-pending{background:#fff8e7;color:#b8860b;}" +
                ".button-preview{display:inline-block;padding:6px 12px;background:#0e7c7b;color:#ffffff;border-radius:4px;font-size:12px;text-decoration:none;}" +
                ".image-preview{max-width:100px;max-height:60px;border-radius:4px;border:1px solid #c8e8e8;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".reference-id{font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;font-size:13px;}" +
                ".type-badge{display:inline-block;padding:2px 10px;border-radius:12px;font-size:11px;font-weight:bold;text-transform:uppercase;}" +
                ".type-badge-hero{background:#e8f5f5;color:#0e7c7b;}" +
                ".type-badge-banner{background:#fff8e7;color:#b8860b;}" +
                ".type-badge-featured{background:#f3e5f5;color:#4a148c;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Hero Section Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#127912; New Hero Section Added</span>" +
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

                "<p class='section-title'>Hero Section Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Hero ID</span><span class='info-value'><span class='reference-id'>#" + heroSectionId + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Name</span><span class='info-value'><span class='hero-icon'>🌟</span>" + escapeHtml(heroSectionInsertRequest.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Type</span><span class='info-value'>" + getHeroTypeBadge(heroSectionInsertRequest.getHeroSectionType()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Order</span><span class='info-value'>" + (heroSectionInsertRequest.getOrder() != null ? heroSectionInsertRequest.getOrder() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + getStatusName(heroSectionInsertRequest.getStatusId()) + "</span></div>" +
                (heroSectionInsertRequest.getImageUrl() != null && !heroSectionInsertRequest.getImageUrl().isEmpty() ?
                        "<div class='info-row'><span class='info-label'>Image</span><span class='info-value'><img src='" + heroSectionInsertRequest.getImageUrl() + "' class='image-preview' alt='Hero Image'/><br/><a href='" + heroSectionInsertRequest.getImageUrl() + "' target='_blank' style='color:#0e7c7b;font-size:12px;'>View Full Image</a></span></div>" : "") +
                "</div>" +

                "<p class='section-title'>Content Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Title</span><span class='info-value'>" + (heroSectionInsertRequest.getTitle() != null ? escapeHtml(heroSectionInsertRequest.getTitle()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Subtitle</span><span class='info-value'>" + (heroSectionInsertRequest.getSubtitle() != null ? escapeHtml(heroSectionInsertRequest.getSubtitle()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (heroSectionInsertRequest.getDescription() != null ? escapeHtml(heroSectionInsertRequest.getDescription()) : "—") + "</span></div>" +
                "</div>" +

                (heroSectionInsertRequest.getPrimaryButtonText() != null || heroSectionInsertRequest.getSecondaryButtonText() != null ?
                        "<p class='section-title'>Action Buttons</p>" +
                                "<div class='info-card'>" +
                                (heroSectionInsertRequest.getPrimaryButtonText() != null ?
                                        "<div class='info-row'><span class='info-label'>Primary Button</span><span class='info-value'><span class='button-preview'>" + escapeHtml(heroSectionInsertRequest.getPrimaryButtonText()) + "</span> → " + (heroSectionInsertRequest.getPrimaryButtonLink() != null ? escapeHtml(heroSectionInsertRequest.getPrimaryButtonLink()) : "—") + "</span></div>" : "") +
                                (heroSectionInsertRequest.getSecondaryButtonText() != null ?
                                        "<div class='info-row'><span class='info-label'>Secondary Button</span><span class='info-value'><span style='display:inline-block;padding:6px 12px;background:#f5fbfb;border:1px solid #0e7c7b;color:#0e7c7b;border-radius:4px;font-size:12px;'>" + escapeHtml(heroSectionInsertRequest.getSecondaryButtonText()) + "</span> → " + (heroSectionInsertRequest.getSecondaryButtonLink() != null ? escapeHtml(heroSectionInsertRequest.getSecondaryButtonLink()) : "—") + "</span></div>" : "") +
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

    private String getStatusName(Long statusId) {
        if (statusId == null) return "—";
        if (statusId == 1) return "<span class='status-pill status-active'>ACTIVE</span>";
        if (statusId == 2) return "<span class='status-pill status-pending'>PENDING</span>";
        if (statusId == 3) return "<span class='status-pill status-inactive'>INACTIVE</span>";
        return "UNKNOWN";
    }

    private String buildHeroSectionBasicFieldsHtml(List<HeroSectionComparisonResult.FieldChange> fieldChanges) {
        if (fieldChanges == null || fieldChanges.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (HeroSectionComparisonResult.FieldChange field : fieldChanges) {
            String displayOldValue = formatValueForDisplay(field.getOldValue());
            String displayNewValue = formatValueForDisplay(field.getNewValue());

            // Special handling for image URL field
            if ("imageUrl".equalsIgnoreCase(field.getFieldName()) ||
                    "image_url".equalsIgnoreCase(field.getFieldName())) {
                sb.append("<tr>")
                        .append("<td><strong>Image</strong></td>")
                        .append("<td>")
                        .append("<span class='old-value'>")
                        .append(displayOldValue != null && !displayOldValue.equals("—") ?
                                "<img src='" + displayOldValue + "' style='max-width:80px;max-height:40px;border-radius:4px;border:1px solid #c8e8e8;margin-bottom:4px;display:block;'/><br/>" + displayOldValue : "—")
                        .append("</span>")
                        .append("</td>")
                        .append("<td>")
                        .append("<span class='new-value'>")
                        .append(displayNewValue != null && !displayNewValue.equals("—") ?
                                "<img src='" + displayNewValue + "' style='max-width:80px;max-height:40px;border-radius:4px;border:1px solid #c8e8e8;margin-bottom:4px;display:block;'/><br/>" + displayNewValue : "—")
                        .append("</span>")
                        .append("</td>")
                        .append("</tr>");
            } else {
                sb.append("<tr>")
                        .append("<td><strong>").append(formatFieldName(field.getFieldName())).append("</strong></td>")
                        .append("<td><span class='old-value'>").append(displayOldValue).append("</span></td>")
                        .append("<td><span class='new-value'>").append(displayNewValue).append("</span></td>")
                        .append("</tr>");
            }
        }
        return sb.toString();
    }

    private String formatValueForDisplay(Object value) {
        if (value == null) return "—";
        if (value instanceof Boolean) {
            return (Boolean) value ? "Yes" : "No";
        }
        if (value instanceof java.math.BigDecimal) {
            return "$" + value.toString();
        }
        if (value instanceof java.time.LocalDate) {
            return ((java.time.LocalDate) value).toString();
        }
        if (value instanceof java.time.LocalDateTime) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
            return ((java.time.LocalDateTime) value).format(formatter);
        }
        if (value instanceof java.util.Date) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            return sdf.format((java.util.Date) value);
        }
        if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        }
        return escapeHtml(value.toString());
    }

    private String buildHeroSectionOrderChangeHtml(HeroSectionComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        if (comparisonResult.getOldOrder() == null && comparisonResult.getNewOrder() == null) return "";
        if (comparisonResult.getOldOrder() != null &&
                comparisonResult.getOldOrder().equals(comparisonResult.getNewOrder())) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Order Change</p>")
                .append("<div class='order-change-box'>")
                .append("<div style='display:flex;align-items:center;justify-content:center;flex-wrap:wrap;'>")

                // Old order
                .append("<div>")
                .append("<div class='order-label'>Previous Order</div>")
                .append("<div class='order-old'>")
                .append(comparisonResult.getOldOrder() != null ? comparisonResult.getOldOrder() : "—")
                .append("</div>")
                .append("</div>")

                // Arrow
                .append("<span class='arrow-icon'>➜</span>")

                // New order
                .append("<div>")
                .append("<div class='order-label'>New Order</div>")
                .append("<div class='order-new'>")
                .append(comparisonResult.getNewOrder() != null ? comparisonResult.getNewOrder() : "—")
                .append("</div>")
                .append("</div>")

                .append("</div>")
                .append("</div>");

        return sb.toString();
    }

    private String formatFieldName(String fieldName) {
        if (fieldName == null) return "";
        String readable = fieldName.replaceAll("([A-Z])", " $1").toLowerCase();
        readable = readable.substring(0, 1).toUpperCase() + readable.substring(1);

        // Special case replacements for better readability
        readable = readable.replace("Hero section type", "Type");
        readable = readable.replace("Image url", "Image");
        readable = readable.replace("Primary button text", "Primary Button Text");
        readable = readable.replace("Primary button link", "Primary Button Link");
        readable = readable.replace("Secondary button text", "Secondary Button Text");
        readable = readable.replace("Secondary button link", "Secondary Button Link");
        readable = readable.replace("Status id", "Status");
        readable = readable.replace("Order", "Display Order");

        return readable;
    }

    private String formatLocalDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return "—";
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }


    private String getHeroTypeBadge(String type) {
        if (type == null) return "—";

        String cssClass = "type-badge-hero";
        String displayName = type;

        switch (type.toUpperCase()) {
            case "HOME":
                cssClass = "type-badge-hero";
                displayName = "Home";
                break;
            case "ABOUT_US":
                cssClass = "type-badge-about";
                displayName = "About Us";
                break;
            case "ACTIVITY":
                cssClass = "type-badge-activity";
                displayName = "Activity";
                break;
            case "BLOG":
                cssClass = "type-badge-blog";
                displayName = "Blog";
                break;
            case "CONTACT_US":
                cssClass = "type-badge-contact";
                displayName = "Contact Us";
                break;
            case "DESTINATION":
                cssClass = "type-badge-destination";
                displayName = "Destination";
                break;
            case "FAQ":
                cssClass = "type-badge-faq";
                displayName = "FAQ";
                break;
            case "PACKAGE":
                cssClass = "type-badge-package";
                displayName = "Package";
                break;
            case "SEASONS":
                cssClass = "type-badge-seasons";
                displayName = "Seasons";
                break;
            case "TOUR":
                cssClass = "type-badge-tour";
                displayName = "Tour";
                break;
            case "VEHICLE":
                cssClass = "type-badge-vehicle";
                displayName = "Vehicle";
                break;
            case "VEHICLE_SPECIFICATION":
                cssClass = "type-badge-vehicle-spec";
                displayName = "Vehicle Specification";
                break;
            case "VEHICLE_TYPES":
                cssClass = "type-badge-vehicle-types";
                displayName = "Vehicle Types";
                break;
            default:
                cssClass = "type-badge-hero";
                displayName = type;
                break;
        }

        return "<span class='type-badge " + cssClass + "'>" + displayName + "</span>";
    }
}
