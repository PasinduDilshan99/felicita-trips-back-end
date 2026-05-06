package com.felicita.email.impl;

import com.felicita.email.PackageEmailHelperService;
import com.felicita.model.dto.PackageFeatureResponseDto;
import com.felicita.model.dto.PackageImageResponseDto;
import com.felicita.model.dto.PackageResponseDto;
import com.felicita.model.other.PackageComparisonResult;
import com.felicita.model.request.*;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PackageEmailHelperServiceImpl implements PackageEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageEmailHelperServiceImpl.class);

    @Override
    public String buildPackageCreateSuccessfullSubject(PackageInsertRequest packageInsertRequest, Long packageId, User loggedUser) {
        return String.format("[Felicita Trips] Package Created — %s",
                packageInsertRequest.getName() != null ? packageInsertRequest.getName() : "Unknown");
    }

    @Override
    public String buildPackageCreateSuccessfullBody(PackageInsertRequest packageInsertRequest, Long packageId, User loggedUser) {
        String imagesHtml = buildPackageImagesHtml(packageInsertRequest.getImages());
        String dayAccommodationsHtml = buildPackageDayAccommodationsHtml(packageInsertRequest.getDayAccommodations());
        String inclusionsHtml = buildPackageInclusionsHtml(packageInsertRequest.getInclusions());
        String featuresHtml = buildPackageFeaturesHtml(packageInsertRequest.getAddFeatures());
        String exclusionsHtml = buildPackageExclusionsHtml(packageInsertRequest.getExclusions());
        String conditionsHtml = buildPackageConditionsHtml(packageInsertRequest.getConditions());
        String travelTipsHtml = buildPackageTravelTipsHtml(packageInsertRequest.getTravelTips());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Package Created</title>" +
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
                ".package-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".price{font-weight:bold;color:#1a6b40;}" +
                ".discount{color:#a33;text-decoration:line-through;}" +
                ".tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".accommodation-card{background:#f9fdfd;border:1px solid #c8e8e8;border-radius:8px;padding:12px 16px;margin-bottom:12px;}" +
                ".accommodation-card .day-title{font-weight:bold;color:#0e7c7b;margin-bottom:8px;}" +
                ".meal-badge{display:inline-block;padding:2px 8px;border-radius:12px;font-size:11px;margin:2px;}" +
                ".meal-yes{background:#d4f4e8;color:#1a6b40;}" +
                ".meal-no{background:#fdecea;color:#a33;}" +
                ".feature-card{background:linear-gradient(135deg,#fff8e7 0%,#fff4db 100%);border:2px solid #ffd700;border-radius:8px;padding:12px 16px;margin-bottom:12px;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Package Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#127880; New Package Added</span>" +
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

                "<p class='section-title'>Package Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Package ID</span><span class='info-value'>#" + packageId + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Package Name</span><span class='info-value'><span class='package-icon'>📦</span>" + escapeHtml(packageInsertRequest.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Package Type</span><span class='info-value'>#" + (packageInsertRequest.getPackageType() != null ? packageInsertRequest.getPackageType() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Tour ID</span><span class='info-value'>#" + (packageInsertRequest.getTourId() != null ? packageInsertRequest.getTourId() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (packageInsertRequest.getDescription() != null ? escapeHtml(packageInsertRequest.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(packageInsertRequest.getStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Period</span><span class='info-value'>" +
                (packageInsertRequest.getStartDate() != null ? packageInsertRequest.getStartDate().toString() : "—") + " to " +
                (packageInsertRequest.getEndDate() != null ? packageInsertRequest.getEndDate().toString() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Original Price</span><span class='info-value'><span class='discount'>$" + (packageInsertRequest.getTotalPrice() != null ? packageInsertRequest.getTotalPrice() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Discount</span><span class='info-value'>" + (packageInsertRequest.getDiscountPercentage() != null ? packageInsertRequest.getDiscountPercentage() + "%" : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Final Price</span><span class='info-value'><span class='price'>$" + calculateFinalPrice(packageInsertRequest.getTotalPrice(), packageInsertRequest.getDiscountPercentage()) + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Price Per Person</span><span class='info-value'><span class='price'>$" + (packageInsertRequest.getPricePerPerson() != null ? packageInsertRequest.getPricePerPerson() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Participants</span><span class='info-value'>Min: " + (packageInsertRequest.getMinPersonCount() != null ? packageInsertRequest.getMinPersonCount() : "—") +
                " | Max: " + (packageInsertRequest.getMaxPersonCount() != null ? packageInsertRequest.getMaxPersonCount() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Colors</span><span class='info-value'><span style='display:inline-block;width:20px;height:20px;background:" + (packageInsertRequest.getColor() != null ? packageInsertRequest.getColor() : "#ccc") + ";border-radius:4px;margin-right:5px;'></span> " +
                (packageInsertRequest.getColor() != null ? packageInsertRequest.getColor() : "—") +
                " | Hover: " + (packageInsertRequest.getHoverColor() != null ? packageInsertRequest.getHoverColor() : "—") + "</span></div>" +
                "</div>" +

                (featuresHtml.isEmpty() ? "" :
                        "<p class='section-title'>Features (" + (packageInsertRequest.getAddFeatures() != null ? packageInsertRequest.getAddFeatures().size() : 0) + ")</p>" +
                                featuresHtml) +

                (dayAccommodationsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Day-wise Accommodations</p>" +
                                dayAccommodationsHtml) +

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
                        "<p class='section-title'>Images (" + (packageInsertRequest.getImages() != null ? packageInsertRequest.getImages().size() : 0) + ")</p>" +
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
    public String buildPackageUpdateSuccessfullSubject(User loggedUser, String name) {
        return String.format("[Felicita Trips] Package Updated — %s by %s %s",
                name != null ? name : "Unknown",
                loggedUser.getFirstName() != null ? loggedUser.getFirstName() : "",
                loggedUser.getLastName() != null ? loggedUser.getLastName() : "");
    }

    @Override
    public String buildPackageUpdateSuccessfullBody(User loggedUser, PackageComparisonResult comparisonResult) {
        String basicFieldsHtml = buildPackageBasicFieldsHtml(comparisonResult.getBasicDetailsChanges());
        String featuresHtml = buildPackageFeatureChangesHtml(comparisonResult);
        String dayAccommodationsHtml = buildPackageDayAccommodationChangesHtml(comparisonResult);
        String imagesHtml = buildPackageImageChangesHtml(comparisonResult);
        String inclusionsHtml = buildPackageInclusionChangesHtml(comparisonResult);
        String exclusionsHtml = buildPackageExclusionChangesHtml(comparisonResult);
        String conditionsHtml = buildPackageConditionChangesHtml(comparisonResult);
        String travelTipsHtml = buildPackageTravelTipChangesHtml(comparisonResult);

        boolean hasAnyUpdates = comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Package Updated</title>" +
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
                ".accommodation-card{background:#f9fdfd;border:1px solid #c8e8e8;border-radius:8px;padding:12px 16px;margin-bottom:12px;}" +
                ".meal-badge{display:inline-block;padding:2px 8px;border-radius:12px;font-size:11px;margin:2px;}" +
                ".meal-yes{background:#d4f4e8;color:#1a6b40;}" +
                ".meal-no{background:#fdecea;color:#a33;}" +
                ".feature-card{background:linear-gradient(135deg,#fff8e7 0%,#fff4db 100%);border:2px solid #ffd700;border-radius:8px;padding:12px 16px;margin-bottom:12px;}" +
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
                "<h1>Package Updated</h1>" +
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

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this package.</div>") +

                (basicFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + basicFieldsHtml + "</tbody>" +
                                "</table><br/>") +

                (featuresHtml.isEmpty() ? "" : featuresHtml) +
                (dayAccommodationsHtml.isEmpty() ? "" : dayAccommodationsHtml) +
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
    public String buildPackageTerminateSuccessfullSubject(User loggedUser, PackageResponseDto packageResponseDto) {
        return String.format("[Felicita Trips] Package Terminated — %s",
                packageResponseDto.getPackageName() != null ? packageResponseDto.getPackageName() : "Unknown");
    }

    @Override
    public String buildPackageTerminateSuccessfullBody(User loggedUser, PackageResponseDto packageResponseDto) {
        String featuresHtml = buildTerminatePackageFeaturesHtml(packageResponseDto.getFeatures());
        String imagesHtml = buildTerminatePackageImagesHtml(packageResponseDto.getImages());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Package Terminated</title>" +
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
                ".price{font-weight:bold;color:#1a6b40;}" +
                ".discount{color:#a33;text-decoration:line-through;}" +
                ".tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".feature-card{background:#f9fdfd;border:1px solid #c8e8e8;border-radius:8px;padding:12px 16px;margin-bottom:12px;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#8B0000,#cd5c5c);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Package Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This package has been terminated</strong> and is no longer available for booking.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + loggedUser.getEmail() + " &nbsp;|&nbsp; " + loggedUser.getUsername() + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Package Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Package ID</span><span class='info-value'>#" + packageResponseDto.getPackageId() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Package Name</span><span class='info-value'>" + escapeHtml(packageResponseDto.getPackageName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Package Type</span><span class='info-value'>" + escapeHtml(packageResponseDto.getPackageTypeName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Tour</span><span class='info-value'>" + escapeHtml(packageResponseDto.getTourName()) + " (#" + packageResponseDto.getTourId() + ")</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (packageResponseDto.getPackageDescription() != null ? escapeHtml(packageResponseDto.getPackageDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(packageResponseDto.getPackageStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'><span class='status-pill-terminated'>TERMINATED</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Period</span><span class='info-value'>" +
                (packageResponseDto.getStartDate() != null ? packageResponseDto.getStartDate().toString() : "—") + " to " +
                (packageResponseDto.getEndDate() != null ? packageResponseDto.getEndDate().toString() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Original Price</span><span class='info-value'><span class='discount'>$" + (packageResponseDto.getTotalPrice() != null ? packageResponseDto.getTotalPrice() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Discount</span><span class='info-value'>" + (packageResponseDto.getDiscountPercentage() != null ? packageResponseDto.getDiscountPercentage() + "%" : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Final Price</span><span class='info-value'><span class='price'>$" + calculateFinalPrice(packageResponseDto.getTotalPrice(), packageResponseDto.getDiscountPercentage()) + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Price Per Person</span><span class='info-value'><span class='price'>$" + (packageResponseDto.getPricePerPerson() != null ? packageResponseDto.getPricePerPerson() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Participants</span><span class='info-value'>Min: " + (packageResponseDto.getMinPersonCount() != null ? packageResponseDto.getMinPersonCount() : "—") +
                " | Max: " + (packageResponseDto.getMaxPersonCount() != null ? packageResponseDto.getMaxPersonCount() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Colors</span><span class='info-value'><span style='display:inline-block;width:20px;height:20px;background:" + (packageResponseDto.getColor() != null ? packageResponseDto.getColor() : "#ccc") + ";border-radius:4px;margin-right:5px;'></span> " +
                (packageResponseDto.getColor() != null ? packageResponseDto.getColor() : "—") +
                " | Hover: " + (packageResponseDto.getHoverColor() != null ? packageResponseDto.getHoverColor() : "—") + "</span></div>" +
                "</div>" +

                (featuresHtml.isEmpty() ? "" :
                        "<p class='section-title'>Features (" + (packageResponseDto.getFeatures() != null ? packageResponseDto.getFeatures().size() : 0) + ")</p>" +
                                featuresHtml) +

                (imagesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Images (" + (packageResponseDto.getImages() != null ? packageResponseDto.getImages().size() : 0) + ")</p>" +
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

// Helper methods for Package email

    private String calculateFinalPrice(BigDecimal totalPrice, BigDecimal discountPercentage) {
        if (totalPrice == null) return "—";
        if (discountPercentage == null) return totalPrice.toString();
        BigDecimal discount = totalPrice.multiply(discountPercentage.divide(new BigDecimal(100)));
        return totalPrice.subtract(discount).toString();
    }

    private String buildPackageImagesHtml(List<PackageImageInsertRequest> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Description</th><th>Status</th><th>Preview</th></tr></thead><tbody>");
        int i = 1;
        for (PackageImageInsertRequest img : images) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(img.getName())).append("</td>")
                    .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                    .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                    .append("<td>").append(img.getImageUrl() != null ?
                            "<a href='" + img.getImageUrl() + "' style='color:#0e7c7b;text-decoration:none;' target='_blank'>🔗 View</a>" : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody><table>");
        return sb.toString();
    }

    private String buildPackageDayAccommodationsHtml(List<PackageDayAccommodationInsertRequest> dayAccommodations) {
        if (dayAccommodations == null || dayAccommodations.isEmpty()) {
            return "<div class='info-card'><span style='color:#6b8e8e;font-style:italic;'>No day accommodations added</span></div>";
        }
        StringBuilder sb = new StringBuilder();
        for (PackageDayAccommodationInsertRequest acc : dayAccommodations) {
            sb.append("<div class='accommodation-card'>")
                    .append("<div class='day-title'>Day ").append(acc.getDayNumber() != null ? acc.getDayNumber() : "—").append("</div>")
                    .append("<div><span class='info-label'>Breakfast:</span> <span class='meal-badge ").append(Boolean.TRUE.equals(acc.getBreakfast()) ? "meal-yes" : "meal-no").append("'>").append(Boolean.TRUE.equals(acc.getBreakfast()) ? "✓ Included" : "✗ Not Included").append("</span>")
                    .append(Boolean.TRUE.equals(acc.getBreakfast()) && acc.getBreakfastDescription() != null ? " <span style='font-size:12px;color:#6b8e8e;'>(" + escapeHtml(acc.getBreakfastDescription()) + ")</span>" : "")
                    .append("</div>")
                    .append("<div><span class='info-label'>Lunch:</span> <span class='meal-badge ").append(Boolean.TRUE.equals(acc.getLunch()) ? "meal-yes" : "meal-no").append("'>").append(Boolean.TRUE.equals(acc.getLunch()) ? "✓ Included" : "✗ Not Included").append("</span>")
                    .append(Boolean.TRUE.equals(acc.getLunch()) && acc.getLunchDescription() != null ? " <span style='font-size:12px;color:#6b8e8e;'>(" + escapeHtml(acc.getLunchDescription()) + ")</span>" : "")
                    .append("</div>")
                    .append("<div><span class='info-label'>Dinner:</span> <span class='meal-badge ").append(Boolean.TRUE.equals(acc.getDinner()) ? "meal-yes" : "meal-no").append("'>").append(Boolean.TRUE.equals(acc.getDinner()) ? "✓ Included" : "✗ Not Included").append("</span>")
                    .append(Boolean.TRUE.equals(acc.getDinner()) && acc.getDinnerDescription() != null ? " <span style='font-size:12px;color:#6b8e8e;'>(" + escapeHtml(acc.getDinnerDescription()) + ")</span>" : "")
                    .append("</div>")
                    .append("<div><span class='info-label'>Morning Tea:</span> <span class='meal-badge ").append(Boolean.TRUE.equals(acc.getMorningTea()) ? "meal-yes" : "meal-no").append("'>").append(Boolean.TRUE.equals(acc.getMorningTea()) ? "✓ Included" : "✗ Not Included").append("</span>")
                    .append(Boolean.TRUE.equals(acc.getMorningTea()) && acc.getMorningTeaDescription() != null ? " <span style='font-size:12px;color:#6b8e8e;'>(" + escapeHtml(acc.getMorningTeaDescription()) + ")</span>" : "")
                    .append("</div>")
                    .append("<div><span class='info-label'>Evening Tea:</span> <span class='meal-badge ").append(Boolean.TRUE.equals(acc.getEveningTea()) ? "meal-yes" : "meal-no").append("'>").append(Boolean.TRUE.equals(acc.getEveningTea()) ? "✓ Included" : "✗ Not Included").append("</span>")
                    .append(Boolean.TRUE.equals(acc.getEveningTea()) && acc.getEveningTeaDescription() != null ? " <span style='font-size:12px;color:#6b8e8e;'>(" + escapeHtml(acc.getEveningTeaDescription()) + ")</span>" : "")
                    .append("</div>")
                    .append("<div><span class='info-label'>Snacks:</span> <span class='meal-badge ").append(Boolean.TRUE.equals(acc.getSnacks()) ? "meal-yes" : "meal-no").append("'>").append(Boolean.TRUE.equals(acc.getSnacks()) ? "✓ Included" : "✗ Not Included").append("</span>")
                    .append(Boolean.TRUE.equals(acc.getSnacks()) && acc.getSnackNote() != null ? " <span style='font-size:12px;color:#6b8e8e;'>(" + escapeHtml(acc.getSnackNote()) + ")</span>" : "")
                    .append("</div>")
                    .append("<div><span class='info-label'>Hotel ID:</span> #").append(acc.getHotelId() != null ? acc.getHotelId() : "—").append("</div>")
                    .append("<div><span class='info-label'>Transport ID:</span> #").append(acc.getTransportId() != null ? acc.getTransportId() : "—").append("</div>")
                    .append("<div><span class='info-label'>Other Notes:</span> ").append(acc.getOtherNotes() != null ? escapeHtml(acc.getOtherNotes()) : "—").append("</div>")
                    .append("</div>");
        }
        return sb.toString();
    }

    private String buildPackageInclusionsHtml(List<PackageInclusionInsertRequest> inclusions) {
        if (inclusions == null || inclusions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No inclusions specified</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (PackageInclusionInsertRequest inc : inclusions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>")
                    .append(escapeHtml(inc.getInclusionText()))
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(inc.getDisplayOrder() != null ? inc.getDisplayOrder() : "—").append("]</span>")
                    .append(" ").append(buildStatusPill(inc.getStatus()))
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildPackageFeaturesHtml(List<PackageFeaturesInsertRequest> features) {
        if (features == null || features.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No features specified</span>";
        }
        StringBuilder sb = new StringBuilder();
        for (PackageFeaturesInsertRequest feature : features) {
            sb.append("<div class='feature-card'>")
                    .append("<strong>✨ ").append(escapeHtml(feature.getFeatureName())).append("</strong>")
                    .append(": <strong style='color:" + (feature.getColor() != null ? feature.getColor() : "#1a6b40") + ";'>").append(escapeHtml(feature.getFeatureValue())).append("</strong>")
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Hover: ").append(feature.getHoverColor() != null ? feature.getHoverColor() : "—").append("]</span>")
                    .append("<p style='margin:5px 0 0 0;font-size:13px;color:#555;'>").append(feature.getFeatureDescription() != null ? escapeHtml(feature.getFeatureDescription()) : "—").append("</p>")
                    .append(feature.getSpecialNote() != null ? "<p style='margin:5px 0 0 0;font-size:12px;color:#a33;font-style:italic;'>📝 " + escapeHtml(feature.getSpecialNote()) + "</p>" : "")
                    .append(" ").append(buildStatusPill(feature.getStatus()))
                    .append("</div>");
        }
        return sb.toString();
    }

    private String buildPackageExclusionsHtml(List<PackageExclusionInsertRequest> exclusions) {
        if (exclusions == null || exclusions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No exclusions specified</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (PackageExclusionInsertRequest exc : exclusions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>")
                    .append(escapeHtml(exc.getExclusionText()))
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(exc.getDisplayOrder() != null ? exc.getDisplayOrder() : "—").append("]</span>")
                    .append(" ").append(buildStatusPill(exc.getStatus()))
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildPackageConditionsHtml(List<PackageConditionInsertRequest> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No conditions specified</span>";
        }
        StringBuilder sb = new StringBuilder("<ul style='margin:0;padding-left:20px;'>");
        for (PackageConditionInsertRequest cond : conditions) {
            sb.append("<li style='margin-bottom:8px;color:#1a3333;'>")
                    .append(escapeHtml(cond.getConditionText()))
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(cond.getDisplayOrder() != null ? cond.getDisplayOrder() : "—").append("]</span>")
                    .append(" ").append(buildStatusPill(cond.getStatus()))
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String buildPackageTravelTipsHtml(List<PackageTravelTipInsertRequest> travelTips) {
        if (travelTips == null || travelTips.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No travel tips specified</span>";
        }
        StringBuilder sb = new StringBuilder();
        for (PackageTravelTipInsertRequest tip : travelTips) {
            sb.append("<div style='margin-bottom:12px;padding:10px;background:#f9fdfd;border-radius:6px;'>")
                    .append("<strong>📌 ").append(escapeHtml(tip.getTipTitle())).append("</strong>")
                    .append(" <span style='font-size:11px;color:#6b8e8e;'>[Order: ").append(tip.getDisplayOrder() != null ? tip.getDisplayOrder() : "—").append("]</span>")
                    .append("<p style='margin:5px 0 0 0;font-size:13px;color:#555;'>").append(escapeHtml(tip.getTipDescription())).append("</p>")
                    .append(" ").append(buildStatusPill(tip.getStatus()))
                    .append("</div>");
        }
        return sb.toString();
    }

    private String buildPackageBasicFieldsHtml(List<PackageComparisonResult.FieldChange> basicFieldChanges) {
        if (basicFieldChanges == null || basicFieldChanges.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (PackageComparisonResult.FieldChange field : basicFieldChanges) {
            sb.append("<tr>")
                    .append("<td><strong>").append(formatFieldName(field.getFieldName())).append("</strong></td>")
                    .append("<td><span class='old-value'>").append(formatValueForDisplay(field.getOldValue())).append("</span></td>")
                    .append("<td><span class='new-value'>").append(formatValueForDisplay(field.getNewValue())).append("</span></td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildPackageFeatureChangesHtml(PackageComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getFeatureIdsToRemove() != null && !comparisonResult.getFeatureIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Features</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long featureId : comparisonResult.getFeatureIdsToRemove()) {
                sb.append("<span class='tag tag-removed'>Feature ID: ").append(featureId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getFeaturesToAdd() != null && !comparisonResult.getFeaturesToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Features</p>")
                    .append("<div style='margin-bottom:20px;'>");
            for (PackageComparisonResult.PackageFeatureChange feature : comparisonResult.getFeaturesToAdd()) {
                sb.append("<div class='feature-card' style='background:#d4f4e8;'>")
                        .append("<strong>✨ ").append(escapeHtml(feature.getFeatureName())).append("</strong>")
                        .append(": <strong style='color:" + (feature.getColor() != null ? feature.getColor() : "#1a6b40") + ";'>").append(escapeHtml(feature.getFeatureValue())).append("</strong>")
                        .append("<p style='margin:5px 0 0 0;font-size:13px;'>").append(feature.getFeatureDescription() != null ? escapeHtml(feature.getFeatureDescription()) : "—").append("</p>")
                        .append("</div>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getFeaturesToUpdate() != null && !comparisonResult.getFeaturesToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Features</p>")
                    .append("<div style='margin-bottom:20px;'>");
            for (PackageComparisonResult.PackageFeatureChange feature : comparisonResult.getFeaturesToUpdate()) {
                sb.append("<div class='feature-card' style='background:#fff8e7;border-color:#ffd700;'>")
                        .append("<strong>Feature ID:</strong> ").append(feature.getFeatureId()).append("<br/>")
                        .append("<strong>✨ Name:</strong> ").append(escapeHtml(feature.getFeatureName())).append("<br/>")
                        .append("<strong>Value:</strong> ").append(escapeHtml(feature.getFeatureValue())).append("<br/>")
                        .append("<strong>Description:</strong> ").append(feature.getFeatureDescription() != null ? escapeHtml(feature.getFeatureDescription()) : "—").append("<br/>")
                        .append("<strong>Status:</strong> ").append(buildStatusPill(feature.getStatus()))
                        .append("</div>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildPackageDayAccommodationChangesHtml(PackageComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.getDayAccommodationIdsToRemove() != null && !comparisonResult.getDayAccommodationIdsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Day Accommodations</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long accId : comparisonResult.getDayAccommodationIdsToRemove()) {
                sb.append("<span class='tag tag-removed'>Accommodation ID: ").append(accId).append("</span>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getDayAccommodationsToAdd() != null && !comparisonResult.getDayAccommodationsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Day Accommodations</p>")
                    .append("<div style='margin-bottom:20px;'>");
            for (PackageComparisonResult.PackageDayAccommodationChange acc : comparisonResult.getDayAccommodationsToAdd()) {
                sb.append("<div class='accommodation-card' style='background:#d4f4e8;'>")
                        .append("<div class='day-title'>Day ").append(acc.getDayNumber()).append("</div>")
                        .append("<div>Breakfast: ").append(Boolean.TRUE.equals(acc.getBreakfast()) ? "✓" : "✗").append("</div>")
                        .append("<div>Lunch: ").append(Boolean.TRUE.equals(acc.getLunch()) ? "✓" : "✗").append("</div>")
                        .append("<div>Dinner: ").append(Boolean.TRUE.equals(acc.getDinner()) ? "✓" : "✗").append("</div>")
                        .append("<div>Hotel ID: #").append(acc.getHotelId() != null ? acc.getHotelId() : "—").append("</div>")
                        .append("</div>");
            }
            sb.append("</div>");
        }

        if (comparisonResult.getDayAccommodationsToUpdate() != null && !comparisonResult.getDayAccommodationsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Day Accommodations</p>")
                    .append("<div style='margin-bottom:20px;'>");
            for (PackageComparisonResult.PackageDayAccommodationChange acc : comparisonResult.getDayAccommodationsToUpdate()) {
                sb.append("<div class='accommodation-card' style='background:#fff8e7;border-color:#ffd700;'>")
                        .append("<div class='day-title'>Day ").append(acc.getDayNumber()).append("</div>")
                        .append("<div><strong>Accommodation ID:</strong> ").append(acc.getPackageDayAccommodationId()).append("</div>")
                        .append("<div><strong>Breakfast:</strong> ").append(Boolean.TRUE.equals(acc.getBreakfast()) ? "✓" : "✗").append("</div>")
                        .append("<div><strong>Lunch:</strong> ").append(Boolean.TRUE.equals(acc.getLunch()) ? "✓" : "✗").append("</div>")
                        .append("<div><strong>Dinner:</strong> ").append(Boolean.TRUE.equals(acc.getDinner()) ? "✓" : "✗").append("</div>")
                        .append("<div><strong>Hotel ID:</strong> #").append(acc.getHotelId() != null ? acc.getHotelId() : "—").append("</div>")
                        .append("<div><strong>Transport ID:</strong> #").append(acc.getTransportId() != null ? acc.getTransportId() : "—").append("</div>")
                        .append("</div>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildPackageImageChangesHtml(PackageComparisonResult comparisonResult) {
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
            for (PackageComparisonResult.PackageImageChange img : comparisonResult.getImagesToAdd()) {
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
            for (PackageComparisonResult.PackageImageChange img : comparisonResult.getImagesToUpdate()) {
                sb.append("<tr>")
                        .append("<td>").append(img.getImageId() != null ? img.getImageId() : "—").append("</td>")
                        .append("<td>").append(escapeHtml(img.getName())).append("</td>")
                        .append("<td>").append(img.getDescription() != null ? escapeHtml(img.getDescription()) : "—").append("</td>")
                        .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                        .append("<td>").append(img.getImageUrl() != null ? "<a href='" + img.getImageUrl() + "' target='_blank'>🔗 View</a>" : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        return sb.toString();
    }

    private String buildPackageInclusionChangesHtml(PackageComparisonResult comparisonResult) {
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
            for (PackageComparisonResult.PackageInclusionChange inc : comparisonResult.getInclusionsToAdd()) {
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
            for (PackageComparisonResult.PackageInclusionChange inc : comparisonResult.getInclusionsToUpdate()) {
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

    private String buildPackageExclusionChangesHtml(PackageComparisonResult comparisonResult) {
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
            for (PackageComparisonResult.PackageExclusionChange exc : comparisonResult.getExclusionsToAdd()) {
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
            for (PackageComparisonResult.PackageExclusionChange exc : comparisonResult.getExclusionsToUpdate()) {
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

    private String buildPackageConditionChangesHtml(PackageComparisonResult comparisonResult) {
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
            for (PackageComparisonResult.PackageConditionChange cond : comparisonResult.getConditionsToAdd()) {
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
            for (PackageComparisonResult.PackageConditionChange cond : comparisonResult.getConditionsToUpdate()) {
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

    private String buildPackageTravelTipChangesHtml(PackageComparisonResult comparisonResult) {
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
            for (PackageComparisonResult.PackageTravelTipChange tip : comparisonResult.getTravelTipsToAdd()) {
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
            for (PackageComparisonResult.PackageTravelTipChange tip : comparisonResult.getTravelTipsToUpdate()) {
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

    private String buildTerminatePackageFeaturesHtml(List<PackageFeatureResponseDto> features) {
        if (features == null || features.isEmpty()) {
            return "<div class='info-card'><span style='color:#6b8e8e;font-style:italic;'>No features</span></div>";
        }
        StringBuilder sb = new StringBuilder();
        for (PackageFeatureResponseDto feature : features) {
            sb.append("<div class='feature-card'>")
                    .append("<strong>✨ ").append(escapeHtml(feature.getFeatureName())).append("</strong>")
                    .append(": <strong style='color:" + (feature.getColor() != null ? feature.getColor() : "#1a6b40") + ";'>").append(escapeHtml(feature.getFeatureValue())).append("</strong>")
                    .append("<p style='margin:5px 0 0 0;font-size:13px;color:#555;'>").append(feature.getFeatureDescription() != null ? escapeHtml(feature.getFeatureDescription()) : "—").append("</p>")
                    .append(feature.getSpecialNote() != null ? "<p style='margin:5px 0 0 0;font-size:12px;color:#a33;font-style:italic;'>📝 " + escapeHtml(feature.getSpecialNote()) + "</p>" : "")
                    .append("</div>");
        }
        return sb.toString();
    }

    private String buildTerminatePackageImagesHtml(List<PackageImageResponseDto> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Description</th><th>Preview</th></tr></thead><tbody>");
        int i = 1;
        for (PackageImageResponseDto img : images) {
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

// Common helper methods

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
        if (value instanceof java.time.LocalDate) {
            return value.toString();
        }
        if (value instanceof java.time.LocalDateTime) {
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
