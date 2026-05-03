package com.felicita.email.impl;

import com.felicita.email.DestinationCategoryEmailHelperService;
import com.felicita.model.dto.InsertDestinationCategoryImagesRequestDto;
import com.felicita.model.other.DestinationCategoryUpdateComparisonResult;
import com.felicita.model.request.DestinationCategoryInsertRequest;
import com.felicita.model.request.DestinationCategoryUpdateRequest;
import com.felicita.model.response.DestinationCategoryDetailsResponseDto;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationCategoryEmailHelperServiceImpl implements DestinationCategoryEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationCategoryEmailHelperServiceImpl.class);

    // ============================================================
// EMAIL SUBJECTS
// ============================================================

    @Override
    public String buildDestinationCategoryCreateSuccessfullSubject(
            DestinationCategoryInsertRequest destinationCategoryInsertRequest,
            User loggedUser) {
        return String.format("[Felicita Trips] Destination Category Created — %s",
                destinationCategoryInsertRequest.getCategory());
    }

    @Override
    public String buildDestinationCategoryUpdateSuccessfullSubject(
            DestinationCategoryUpdateRequest destinationCategoryUpdateRequest,
            User loggedUser) {
        return String.format("[Felicita Trips] Destination Category Updated — ID #%d",
                destinationCategoryUpdateRequest.getCategoryId());
    }

    @Override
    public String buildDestinationCategoryTerminateSuccessfullSubject(
            User loggeduser,
            DestinationCategoryDetailsResponseDto destinationCategoryDetailsResponseDto) {
        return String.format("[Felicita Trips] Destination Category Terminated — %s",
                destinationCategoryDetailsResponseDto.getCategory());
    }


// ============================================================
// EMAIL BODIES
// ============================================================

    @Override
    public String buildDestinationCategoryCreateSuccessfullBody(
            DestinationCategoryInsertRequest req,
            User loggedUser) {

        String imageRows = buildCreateImageRows(req.getImages());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Destination Category Created</title>" +
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
                ".color-swatch{display:inline-block;width:16px;height:16px;border-radius:4px;vertical-align:middle;margin-right:6px;border:1px solid rgba(0,0,0,0.12);}" +
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
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                ".divider{height:1px;background:linear-gradient(90deg,transparent,#c8e8e8,transparent);margin:24px 0;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Destination Category Created</h1>" +
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

                "<p class='section-title'>Category Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Category Name</span><span class='info-value'>" + req.getCategory() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + req.getDescription() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(req.getStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Color</span><span class='info-value'><span class='color-swatch' style='background:" + req.getColor() + "'></span>" + req.getColor() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Hover Color</span><span class='info-value'><span class='color-swatch' style='background:" + req.getHoverColor() + "'></span>" + req.getHoverColor() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Images Added</span><span class='info-value'>" + (req.getImages() != null ? req.getImages().size() : 0) + " image(s)</span></div>" +
                "</div>" +

                (imageRows.isEmpty() ? "" :
                        "<p class='section-title'>Images</p>" +
                                "<table class='images-table'>" +
                                "<thead><tr><th>#</th><th>Name</th><th>Description</th><th>Status</th></tr></thead>" +
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

    @Override
    public String buildDestinationCategoryUpdateSuccessfullBody(
            User loggedUser,
            DestinationCategoryUpdateComparisonResult comparisonResult) {

        String fieldRows = buildUpdateFieldRows(comparisonResult.getFieldChanges());
        String imageRows = buildUpdateImageRows(comparisonResult.getImageChanges());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Destination Category Updated</title>" +
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
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".changes-table{width:100%;border-collapse:collapse;margin-bottom:24px;}" +
                ".changes-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".changes-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".changes-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".old-val{color:#a33333;text-decoration:line-through;font-size:12px;}" +
                ".new-val{color:#1a6b40;font-size:12px;font-weight:bold;}" +
                ".arrow{color:#0e7c7b;padding:0 4px;}" +
                ".tag-add{display:inline-block;padding:2px 10px;border-radius:12px;font-size:11px;background:#d4f4e8;color:#1a6b40;font-family:Arial,sans-serif;font-weight:bold;}" +
                ".tag-remove{display:inline-block;padding:2px 10px;border-radius:12px;font-size:11px;background:#fdecea;color:#a33;font-family:Arial,sans-serif;font-weight:bold;}" +
                ".tag-update{display:inline-block;padding:2px 10px;border-radius:12px;font-size:11px;background:#fff3d4;color:#7a5800;font-family:Arial,sans-serif;font-weight:bold;}" +
                ".no-changes{font-family:Arial,sans-serif;font-size:13px;color:#6b8e8e;font-style:italic;padding:12px;background:#f9fdfd;border:1px solid #e0f0f0;border-radius:6px;margin-bottom:24px;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Destination Category Updated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#9998; Successfully Updated — ID #" + comparisonResult.getCategoryId() + "</span>" +
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

                "<p class='section-title'>Field Changes</p>" +
                (fieldRows.isEmpty()
                        ? "<div class='no-changes'>No field changes detected.</div>"
                        : "<table class='changes-table'>" +
                        "<thead><tr><th>Field</th><th>Previous Value</th><th>New Value</th></tr></thead>" +
                        "<tbody>" + fieldRows + "</tbody>" +
                        "</table>"
                ) +

                "<p class='section-title'>Image Changes</p>" +
                (imageRows.isEmpty()
                        ? "<div class='no-changes'>No image changes detected.</div>"
                        : "<table class='changes-table'>" +
                        "<thead><tr><th>Image ID</th><th>Change</th><th>Details</th></tr></thead>" +
                        "<tbody>" + imageRows + "</tbody>" +
                        "</table>"
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

    @Override
    public String buildDestinationCategoryTerminateSuccessfullBody(
            User loggeduser,
            DestinationCategoryDetailsResponseDto dto) {

        int imageCount = dto.getImages() != null ? dto.getImages().size() : 0;
        int destCount  = dto.getDestinations() != null ? dto.getDestinations().size() : 0;

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Destination Category Terminated</title>" +
                "<style>" +
                "body{margin:0;padding:0;background:#f7f0f0;font-family:Georgia,serif;}" +
                ".wrapper{max-width:640px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(128,0,0,0.08);}" +
                ".header{background:linear-gradient(135deg,#0e7c7b 0%,#1a9e9e 60%,#2bbfbf 100%);padding:36px 40px 28px;text-align:center;}" +
                ".header img{height:52px;margin-bottom:16px;}" +
                ".header h1{color:#ffffff;font-size:22px;margin:0;font-weight:normal;letter-spacing:0.5px;}" +
                ".header p.tagline{color:rgba(255,255,255,0.80);font-size:12px;margin:6px 0 0;letter-spacing:2px;text-transform:uppercase;}" +
                ".badge-terminate{display:inline-block;background:rgba(220,50,50,0.22);color:#ffe0e0;border:1px solid rgba(255,150,150,0.50);border-radius:20px;padding:4px 16px;font-size:12px;margin-top:14px;letter-spacing:1px;}" +
                ".content{padding:36px 40px;}" +
                ".section-title{font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#0e7c7b;margin:0 0 14px;font-family:Arial,sans-serif;}" +
                ".warning-banner{background:#fff5f5;border:1px solid #f5c0c0;border-left:4px solid #c0392b;border-radius:6px;padding:14px 18px;margin-bottom:24px;font-family:Arial,sans-serif;font-size:13px;color:#7a2222;line-height:1.6;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".info-card{background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".info-row{display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;}" +
                ".info-row:last-child{border-bottom:none;padding-bottom:0;}" +
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:150px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-terminated{background:#fdecea;color:#a33;}" +
                ".stats-row{display:flex;gap:16px;margin-bottom:24px;}" +
                ".stat-box{flex:1;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:16px;text-align:center;}" +
                ".stat-num{font-size:28px;color:#0e7c7b;font-family:Arial,sans-serif;font-weight:bold;}" +
                ".stat-label{font-size:11px;color:#6b8e8e;font-family:Arial,sans-serif;text-transform:uppercase;letter-spacing:1px;margin-top:4px;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Destination Category Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge-terminate'>&#9888; Terminated</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='warning-banner'>" +
                "<strong>Important Notice:</strong> The destination category <strong>&quot;" + dto.getCategory() + "&quot;</strong> " +
                "has been permanently terminated by an admin. This action has been logged for audit purposes." +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggeduser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggeduser) + "</div>" +
                "<div class='actor-meta'>" + loggeduser.getEmail() + " &nbsp;|&nbsp; " + loggeduser.getUsername() + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Category Snapshot</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Category ID</span><span class='info-value'>#" + dto.getCategoryId() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Category Name</span><span class='info-value'>" + dto.getCategory() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + dto.getCategoryDescription() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status at Termination</span><span class='info-value'><span class='status-pill status-terminated'>TERMINATED</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Created At</span><span class='info-value'>" + (dto.getCreatedAt() != null ? dto.getCreatedAt().toString().replace("T", " ") : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Last Updated At</span><span class='info-value'>" + (dto.getUpdatedAt() != null ? dto.getUpdatedAt().toString().replace("T", " ") : "—") + "</span></div>" +
                "</div>" +

                "<p class='section-title'>Affected Records</p>" +
                "<div class='stats-row'>" +
                "<div class='stat-box'><div class='stat-num'>" + imageCount + "</div><div class='stat-label'>Images</div></div>" +
                "<div class='stat-box'><div class='stat-num'>" + destCount + "</div><div class='stat-label'>Destinations</div></div>" +
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


// ============================================================
// PRIVATE HELPERS  (add these to the same class)
// ============================================================

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

    private String buildCreateImageRows(List<InsertDestinationCategoryImagesRequestDto> images) {
        if (images == null || images.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (InsertDestinationCategoryImagesRequestDto img : images) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(img.getName() != null ? img.getName() : "—").append("</td>")
                    .append("<td>").append(img.getDescription() != null ? img.getDescription() : "—").append("</td>")
                    .append("<td>").append(buildStatusPill(img.getStatus())).append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String buildUpdateFieldRows(List<DestinationCategoryUpdateComparisonResult.FieldChange> changes) {
        if (changes == null || changes.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (DestinationCategoryUpdateComparisonResult.FieldChange fc : changes) {
            if (Boolean.TRUE.equals(fc.getChanged())) {
                sb.append("<tr>")
                        .append("<td style='font-family:Arial,sans-serif;font-size:13px;color:#2a4444;'>").append(fc.getFieldName()).append("</td>")
                        .append("<td><span class='old-val'>").append(fc.getOldValue() != null ? fc.getOldValue() : "—").append("</span></td>")
                        .append("<td><span class='new-val'>").append(fc.getNewValue() != null ? fc.getNewValue() : "—").append("</span></td>")
                        .append("</tr>");
            }
        }
        return sb.toString();
    }

    private String buildUpdateImageRows(List<DestinationCategoryUpdateComparisonResult.ImageChange> changes) {
        if (changes == null || changes.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (DestinationCategoryUpdateComparisonResult.ImageChange ic : changes) {
            String tagHtml;
            String details;
            if ("ADD".equalsIgnoreCase(ic.getChangeType())) {
                tagHtml = "<span class='tag-add'>ADD</span>";
                details = ic.getNewValue() != null ? ic.getNewValue() : "—";
            } else if ("REMOVE".equalsIgnoreCase(ic.getChangeType())) {
                tagHtml = "<span class='tag-remove'>REMOVE</span>";
                details = ic.getOldValue() != null ? ic.getOldValue() : "—";
            } else {
                tagHtml = "<span class='tag-update'>UPDATE</span>";
                details = "<span class='old-val'>" + (ic.getOldValue() != null ? ic.getOldValue() : "—") + "</span>" +
                        "<span class='arrow'> &#8594; </span>" +
                        "<span class='new-val'>" + (ic.getNewValue() != null ? ic.getNewValue() : "—") + "</span>";
            }
            sb.append("<tr>")
                    .append("<td style='font-family:Arial,sans-serif;font-size:13px;'>#").append(ic.getImageId()).append("</td>")
                    .append("<td>").append(tagHtml).append("</td>")
                    .append("<td>").append(details).append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }
}
