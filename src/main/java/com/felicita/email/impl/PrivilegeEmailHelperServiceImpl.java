package com.felicita.email.impl;

import com.felicita.email.PrivilegeEmailHelperService;
import com.felicita.model.other.PrivilegeUpdateComparisonResult;
import com.felicita.model.request.PrivilegeInsertRequest;
import com.felicita.model.response.PrivilegeResponse;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeEmailHelperServiceImpl implements PrivilegeEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeEmailHelperServiceImpl.class);

    @Override
    public String buildPrivilegeCreateSuccessfullSubject(PrivilegeInsertRequest privilegeInsertRequest, Long privilegeId, User loggedUser) {
        return String.format("[Felicita Trips] Privilege Created — %s",
                privilegeInsertRequest.getName() != null ? privilegeInsertRequest.getName() : "Unknown");
    }

    @Override
    public String buildPrivilegeCreateSuccessfullBody(PrivilegeInsertRequest privilegeInsertRequest, Long privilegeId, User loggedUser) {
        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Privilege Created</title>" +
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
                ".privilege-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
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
                "<h1>Privilege Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#128273; New Privilege Added</span>" +
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

                "<p class='section-title'>Privilege Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Privilege ID</span><span class='info-value'>#" + privilegeId + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Privilege Name</span><span class='info-value'><span class='privilege-icon'>🔑</span>" + escapeHtml(privilegeInsertRequest.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (privilegeInsertRequest.getDescription() != null ? escapeHtml(privilegeInsertRequest.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(privilegeInsertRequest.getStatus()) + "</span></div>" +
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
    public String buildPrivilegeUpdateSuccessfullSubject(User loggedUser) {
        return String.format("[Felicita Trips] Privilege Updated — %s %s",
                loggedUser.getFirstName() != null ? loggedUser.getFirstName() : "",
                loggedUser.getLastName() != null ? loggedUser.getLastName() : "");
    }

    @Override
    public String buildPrivilegeUpdateSuccessfullBody(User loggedUser, PrivilegeUpdateComparisonResult comparisonResult) {
        boolean hasAnyUpdates = comparisonResult.isHasChanges();

        String updatedFieldsHtml = buildPrivilegeUpdatedFieldsHtml(comparisonResult);

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Privilege Updated</title>" +
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
                ".updated-fields-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".updated-fields-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".updated-fields-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".updated-fields-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".old-value{color:#a33;text-decoration:line-through;background:#fdecea;padding:2px 6px;border-radius:4px;display:inline-block;}" +
                ".new-value{color:#1a6b40;background:#d4f4e8;padding:2px 6px;border-radius:4px;display:inline-block;}" +
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
                "<h1>Privilege Updated</h1>" +
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

                "<p class='section-title'>Privilege ID</p>" +
                "<div class='info-card'>" +
                "<div class='info-row' style='display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;'>" +
                "<span class='info-label' style='font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:130px;'>Privilege ID</span>" +
                "<span class='info-value' style='font-size:14px;color:#1a3333;font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;'>#" + comparisonResult.getPrivilegeId() + "</span>" +
                "</div>" +
                "</div>" +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this privilege.</div>") +

                (updatedFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + updatedFieldsHtml + "</tbody>" +
                                "</table>") +

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
    public String buildPrivilegeTerminateSuccessfullSubject(User loggedUser, PrivilegeResponse privilegeResponse) {
        return String.format("[Felicita Trips] Privilege Terminated — %s",
                privilegeResponse.getPrivilegeName() != null ? privilegeResponse.getPrivilegeName() : "Unknown");
    }

    @Override
    public String buildPrivilegeTerminateSuccessfullBody(User loggedUser, PrivilegeResponse privilegeResponse) {
        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Privilege Terminated</title>" +
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
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:130px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".terminate-warning{background:#fdecea;border:1px solid #e0b0b0;border-radius:8px;padding:16px 20px;margin-bottom:24px;text-align:center;}" +
                ".terminate-warning p{color:#a33;font-family:Arial,sans-serif;font-size:13px;margin:0;}" +
                ".terminate-warning .warning-icon{font-size:24px;margin-bottom:8px;display:block;}" +
                ".status-pill-terminated{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;background:#fdecea;color:#a33;font-weight:bold;}" +
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
                "<h1>Privilege Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This privilege has been terminated</strong> and is no longer available for assignment to users or roles.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + loggedUser.getEmail() + " &nbsp;|&nbsp; " + loggedUser.getUsername() + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Privilege Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Privilege ID</span><span class='info-value'>#" + privilegeResponse.getPrivilegeId() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Privilege Name</span><span class='info-value'>" + escapeHtml(privilegeResponse.getPrivilegeName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (privilegeResponse.getPrivilegeDescription() != null ? escapeHtml(privilegeResponse.getPrivilegeDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(privilegeResponse.getPrivilegeStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'><span class='status-pill-terminated'>TERMINATED</span></span></div>" +
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

// Helper methods for Privilege email

    private String buildPrivilegeUpdatedFieldsHtml(PrivilegeUpdateComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.isNameChanged()) {
            sb.append("<tr>")
                    .append("<td><strong>Privilege Name</strong></td>")
                    .append("<td><span class='old-value'>").append(escapeHtml(comparisonResult.getOldName())).append("</span></td>")
                    .append("<td><span class='new-value'>").append(escapeHtml(comparisonResult.getNewName())).append("</span></td>")
                    .append("</tr>");
        }

        if (comparisonResult.isDescriptionChanged()) {
            sb.append("<tr>")
                    .append("<td><strong>Description</strong></td>")
                    .append("<td><span class='old-value'>").append(comparisonResult.getOldDescription() != null ? escapeHtml(comparisonResult.getOldDescription()) : "—").append("</span></td>")
                    .append("<td><span class='new-value'>").append(comparisonResult.getNewDescription() != null ? escapeHtml(comparisonResult.getNewDescription()) : "—").append("</span></td>")
                    .append("</tr>");
        }

        if (comparisonResult.isStatusChanged()) {
            sb.append("<tr>")
                    .append("<td><strong>Status</strong></td>")
                    .append("<td><span class='old-value'>").append(comparisonResult.getOldStatus()).append("</span></td>")
                    .append("<td><span class='new-value'>").append(comparisonResult.getNewStatus()).append("</span></td>")
                    .append("</tr>");
        }

        return sb.toString();
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
        if ("ACTIVE".equalsIgnoreCase(status)) {
            return "<span class='status-pill status-active'>" + status + "</span>";
        } else {
            return "<span class='status-pill status-inactive'>" + status + "</span>";
        }
    }
}
