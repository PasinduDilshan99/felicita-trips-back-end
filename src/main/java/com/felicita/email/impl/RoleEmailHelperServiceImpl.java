package com.felicita.email.impl;

import com.felicita.email.RoleEmailHelperService;
import com.felicita.model.other.RoleUpdateComparisonResult;
import com.felicita.model.request.RoleInsertRequest;
import com.felicita.model.response.RoleResponse;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleEmailHelperServiceImpl implements RoleEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleEmailHelperServiceImpl.class);

    @Override
    public String buildRoleCreateSuccessfullSubject(RoleInsertRequest roleInsertRequest, Long roleId, User loggedUser) {
        return String.format("[Felicita Trips] Role Created — %s",
                roleInsertRequest.getName() != null ? roleInsertRequest.getName() : "Unknown");
    }

    @Override
    public String buildRoleCreateSuccessfullBody(RoleInsertRequest roleInsertRequest, Long roleId, User loggedUser) {
        String privilegesHtml = buildRolePrivilegesHtml(roleInsertRequest.getPrivilegesIds());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Role Created</title>" +
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
                ".role-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".privilege-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".privileges-container{display:flex;flex-wrap:wrap;gap:8px;margin-top:4px;}" +
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
                "<h1>Role Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#128100; New Role Added</span>" +
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

                "<p class='section-title'>Role Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Role ID</span><span class='info-value'>#" + roleId + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Role Name</span><span class='info-value'><span class='role-icon'>👥</span>" + escapeHtml(roleInsertRequest.getName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (roleInsertRequest.getDescription() != null ? escapeHtml(roleInsertRequest.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(roleInsertRequest.getStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Privileges</span><span class='info-value'>" + privilegesHtml + "</span></div>" +
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
    public String buildRoleUpdateSuccessfullSubject(User loggedUser) {
        return String.format("[Felicita Trips] Role Updated — %s %s",
                loggedUser.getFirstName() != null ? loggedUser.getFirstName() : "",
                loggedUser.getLastName() != null ? loggedUser.getLastName() : "");
    }

    @Override
    public String buildRoleUpdateSuccessfullBody(User loggedUser, RoleUpdateComparisonResult comparisonResult) {
        boolean hasAnyUpdates = comparisonResult.isHasChanges();

        String updatedFieldsHtml = buildRoleUpdatedFieldsHtml(comparisonResult);
        String addedPrivilegesHtml = buildAddedPrivilegesHtml(comparisonResult.getAddedPrivilegeIds());
        String removedPrivilegesHtml = buildRemovedPrivilegesHtml(comparisonResult.getRemovedPrivilegeIds());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Role Updated</title>" +
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
                ".privilege-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:12px;color:#0e7c7b;font-family:Arial,sans-serif;margin:4px;}" +
                ".privilege-tag-added{background:#d4f4e8;border-color:#90d0c0;color:#1a6b40;}" +
                ".privilege-tag-removed{background:#fdecea;border-color:#e0b0b0;color:#a33;text-decoration:line-through;}" +
                ".privileges-container{display:flex;flex-wrap:wrap;gap:8px;margin-top:4px;}" +
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
                "<h1>Role Updated</h1>" +
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

                "<p class='section-title'>Role ID</p>" +
                "<div class='info-card'>" +
                "<div class='info-row' style='display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;'>" +
                "<span class='info-label' style='font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:130px;'>Role ID</span>" +
                "<span class='info-value' style='font-size:14px;color:#1a3333;font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;'>#" + comparisonResult.getRoleId() + "</span>" +
                "</div>" +
                "</div>" +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this role.</div>") +

                (updatedFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + updatedFieldsHtml + "</tbody>" +
                                "</table><br/>") +

                (addedPrivilegesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Added Privileges</p>" +
                                "<div class='privileges-container'>" + addedPrivilegesHtml + "</div><br/>") +

                (removedPrivilegesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Removed Privileges</p>" +
                                "<div class='privileges-container'>" + removedPrivilegesHtml + "</div>") +

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
    public String buildRoleTerminateSuccessfullSubject(User loggedUser, RoleResponse roleResponse) {
        return String.format("[Felicita Trips] Role Terminated — %s",
                roleResponse.getRoleName() != null ? roleResponse.getRoleName() : "Unknown");
    }

    @Override
    public String buildRoleTerminateSuccessfullBody(User loggedUser, RoleResponse roleResponse) {
        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Role Terminated</title>" +
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
                "<h1>Role Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This role has been terminated</strong> and is no longer available for assignment to users.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + loggedUser.getEmail() + " &nbsp;|&nbsp; " + loggedUser.getUsername() + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Role Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Role ID</span><span class='info-value'>#" + roleResponse.getRoleId() + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Role Name</span><span class='info-value'>" + escapeHtml(roleResponse.getRoleName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (roleResponse.getRoleDescription() != null ? escapeHtml(roleResponse.getRoleDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(roleResponse.getRoleStatus()) + "</span></div>" +
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

// Helper methods for Role email

    private String buildRolePrivilegesHtml(List<Long> privilegeIds) {
        if (privilegeIds == null || privilegeIds.isEmpty()) {
            return "<span style='color:#6b8e8e;font-style:italic;'>No privileges assigned</span>";
        }
        StringBuilder sb = new StringBuilder("<div class='privileges-container'>");
        for (Long privilegeId : privilegeIds) {
            sb.append("<span class='privilege-tag'>Privilege ID: ").append(privilegeId).append("</span>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String buildRoleUpdatedFieldsHtml(RoleUpdateComparisonResult comparisonResult) {
        StringBuilder sb = new StringBuilder();

        if (comparisonResult.isNameChanged()) {
            sb.append("<tr>")
                    .append("<td><strong>Role Name</strong></td>")
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

    private String buildAddedPrivilegesHtml(List<Long> addedPrivilegeIds) {
        if (addedPrivilegeIds == null || addedPrivilegeIds.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Long privilegeId : addedPrivilegeIds) {
            sb.append("<span class='privilege-tag privilege-tag-added'>+ Privilege ID: ").append(privilegeId).append("</span>");
        }
        return sb.toString();
    }

    private String buildRemovedPrivilegesHtml(List<Long> removedPrivilegeIds) {
        if (removedPrivilegeIds == null || removedPrivilegeIds.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Long privilegeId : removedPrivilegeIds) {
            sb.append("<span class='privilege-tag privilege-tag-removed'>- Privilege ID: ").append(privilegeId).append("</span>");
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
