package com.felicita.email.impl;

import com.felicita.email.EmployeeEmailHelperService;
import com.felicita.model.dto.UserBasicDetailsDto;
import com.felicita.model.dto.WelcomeEmployeeDto;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmployeeEmailHelperServiceImpl implements EmployeeEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeEmailHelperServiceImpl.class);

    @Override
    public String buildEmployeeCreateSuccessfullSubject(WelcomeEmployeeDto welcomeEmployeeDto, User loggedUser) {
        return String.format("[Felicita Trips] Employee Created — %s (%s)",
                loggedUser.getFirstName() != null ? loggedUser.getFirstName() : "",
                welcomeEmployeeDto.getEmployeeCode() != null ? welcomeEmployeeDto.getEmployeeCode() : "Unknown");
    }

    @Override
    public String buildEmployeeCreateSuccessfullBody(WelcomeEmployeeDto welcomeEmployeeDto, User loggedUser) {
        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Employee Created</title>" +
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
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:150px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".employee-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".highlight{background:#e8f5f5;padding:2px 6px;border-radius:4px;font-weight:bold;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Employee Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#128104; New Employee Added</span>" +
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

                "<p class='section-title'>Employee Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Employee Code</span><span class='info-value'><span class='employee-icon'>👤</span><span class='highlight'>" + escapeHtml(welcomeEmployeeDto.getEmployeeCode()) + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Employee Type</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getEmployeeType()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Department</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getDepartmentName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Designation</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getDesignation()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Employment Type</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getEmploymentType()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Supervisor</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getSupervisorName()) + " (" + escapeHtml(welcomeEmployeeDto.getSupervisorId()) + ")</span></div>" +
                "<div class='info-row'><span class='info-label'>Reporting Manager</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getReportingManagerName()) + " (" + escapeHtml(welcomeEmployeeDto.getReportingManagerID()) + ")</span></div>" +
                "<div class='info-row'><span class='info-label'>Hiring Date</span><span class='info-value'>" + (welcomeEmployeeDto.getHiringDate() != null ? welcomeEmployeeDto.getHiringDate().toString() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Probation Period</span><span class='info-value'>" + (welcomeEmployeeDto.getProbationMonths() != null ? welcomeEmployeeDto.getProbationMonths() + " month(s)" : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Probation End Date</span><span class='info-value'>" + (welcomeEmployeeDto.getProbationEndDate() != null ? welcomeEmployeeDto.getProbationEndDate().toString() : "—") + "</span></div>" +
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
    public String buildEmployeeCreateSuccessfullSubjectForEmployee(UserBasicDetailsDto userBasicDetailsDto, WelcomeEmployeeDto welcomeEmployeeDto, User loggedUser) {
        return String.format("[Felicita Trips] Welcome to Felicita Trips, %s!",
                userBasicDetailsDto.getFirstName() != null ? userBasicDetailsDto.getFirstName() : "Employee");
    }

    @Override
    public String buildEmployeeCreateSuccessfullBodyForEmployee(UserBasicDetailsDto userBasicDetailsDto, WelcomeEmployeeDto welcomeEmployeeDto, User loggedUser) {
        String tempPassword = "Welcome@123"; // This should be generated dynamically in real implementation

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Welcome to Felicita Trips</title>" +
                "<style>" +
                "body{margin:0;padding:0;background:#f0f7f7;font-family:Georgia,serif;}" +
                ".wrapper{max-width:640px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,128,128,0.10);}" +
                ".header{background:linear-gradient(135deg,#0e7c7b 0%,#1a9e9e 50%,#2bbfbf 100%);padding:36px 40px 28px;text-align:center;}" +
                ".header img{height:52px;margin-bottom:16px;}" +
                ".header h1{color:#ffffff;font-size:22px;margin:0;font-weight:normal;letter-spacing:0.5px;}" +
                ".header p.tagline{color:rgba(255,255,255,0.80);font-size:12px;margin:6px 0 0;letter-spacing:2px;text-transform:uppercase;}" +
                ".welcome-badge{display:inline-block;background:rgba(255,255,255,0.18);color:#ffffff;border:1px solid rgba(255,255,255,0.40);border-radius:20px;padding:4px 16px;font-size:12px;margin-top:14px;letter-spacing:1px;}" +
                ".content{padding:36px 40px;}" +
                ".section-title{font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#0e7c7b;margin:0 0 14px;font-family:Arial,sans-serif;}" +
                ".info-card{background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".info-row{display:flex;align-items:flex-start;padding:8px 0;border-bottom:1px solid #e0f0f0;}" +
                ".info-row:last-child{border-bottom:none;padding-bottom:0;}" +
                ".info-label{font-family:Arial,sans-serif;font-size:12px;color:#6b8e8e;min-width:150px;padding-top:2px;}" +
                ".info-value{font-size:14px;color:#1a3333;font-family:Arial,sans-serif;word-break:break-all;}" +
                ".welcome-message{background:linear-gradient(135deg,#fff8e7 0%,#fff4db 100%);border:2px solid #ffd700;border-radius:8px;padding:24px;margin-bottom:24px;text-align:center;}" +
                ".welcome-message h2{color:#0e7c7b;margin:0 0 10px 0;font-size:20px;}" +
                ".welcome-message p{color:#1a3333;font-size:14px;margin:5px 0;}" +
                ".credentials-box{background:#e8f5f5;border:1px solid #c8e8e8;border-radius:8px;padding:20px;margin:20px 0;text-align:center;}" +
                ".credentials-box .label{font-size:11px;color:#6b8e8e;letter-spacing:1px;text-transform:uppercase;margin-bottom:8px;}" +
                ".credentials-box .value{font-size:16px;color:#0e7c7b;font-weight:bold;font-family:monospace;background:#ffffff;padding:8px 12px;border-radius:6px;display:inline-block;margin:5px 0;}" +
                ".warning-note{background:#fdecea;border-left:4px solid #a33;padding:12px 16px;margin:20px 0;font-size:12px;color:#a33;font-family:Arial,sans-serif;}" +
                ".button{display:inline-block;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);color:#ffffff;text-decoration:none;padding:12px 24px;border-radius:6px;font-family:Arial,sans-serif;font-size:14px;font-weight:bold;margin-top:15px;}" +
                ".employee-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".highlight{background:#e8f5f5;padding:2px 6px;border-radius:4px;font-weight:bold;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Welcome Aboard!</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='welcome-badge'>&#127881; New Employee Onboarded</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='welcome-message'>" +
                "<h2>Dear " + escapeHtml(userBasicDetailsDto.getFirstName()) + " " + escapeHtml(userBasicDetailsDto.getLastName()) + ",</h2>" +
                "<p>We are thrilled to welcome you to the <strong>Felicita Trips</strong> family!</p>" +
                "<p>Your journey with us begins today, and we're excited to have you on board.</p>" +
                "</div>" +

                "<p class='section-title'>Your Account Credentials</p>" +
                "<div class='credentials-box'>" +
                "<div class='label'>Username / Email</div>" +
                "<div class='value'>" + escapeHtml(userBasicDetailsDto.getEmail()) + "</div>" +
                "<div class='label' style='margin-top:15px;'>Temporary Password</div>" +
                "<div class='value'>" + tempPassword + "</div>" +
                "<a href='https://felicita-trips.com/login' class='button'>Login to Your Account</a>" +
                "</div>" +

                "<div class='warning-note'>" +
                "<strong>⚠️ Important:</strong> Please change your password after your first login. For security reasons, do not share your credentials with anyone." +
                "</div>" +

                "<p class='section-title'>Your Employment Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Employee Code</span><span class='info-value'><span class='employee-icon'>👤</span><span class='highlight'>" + escapeHtml(welcomeEmployeeDto.getEmployeeCode()) + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Employee Type</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getEmployeeType()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Department</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getDepartmentName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Designation</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getDesignation()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Employment Type</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getEmploymentType()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Supervisor</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getSupervisorName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Reporting Manager</span><span class='info-value'>" + escapeHtml(welcomeEmployeeDto.getReportingManagerName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Hiring Date</span><span class='info-value'>" + (welcomeEmployeeDto.getHiringDate() != null ? welcomeEmployeeDto.getHiringDate().toString() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Probation Period</span><span class='info-value'>" + (welcomeEmployeeDto.getProbationMonths() != null ? welcomeEmployeeDto.getProbationMonths() + " month(s)" : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Probation End Date</span><span class='info-value'>" + (welcomeEmployeeDto.getProbationEndDate() != null ? welcomeEmployeeDto.getProbationEndDate().toString() : "—") + "</span></div>" +
                "</div>" +

                "<p class='section-title'>Getting Started</p>" +
                "<div class='info-card'>" +
                "<p style='margin:0 0 10px 0;color:#1a3333;'>📧 <strong>Email:</strong> " + escapeHtml(userBasicDetailsDto.getEmail()) + "</p>" +
                "<p style='margin:0 0 10px 0;color:#1a3333;'>📱 <strong>Mobile:</strong> " + escapeHtml(userBasicDetailsDto.getMobileNumber()) + "</p>" +
                "<p style='margin:0;color:#1a3333;'>🆔 <strong>NIC:</strong> " + escapeHtml(userBasicDetailsDto.getNic()) + "</p>" +
                "</div>" +

                "</div>" +

                "<div class='footer'>" +
                "<p class='brand'>Felicita Trips — Employee Portal</p>" +
                "<p>This is an automated welcome email from the Felicita Trips HR system.</p>" +
                "<p>For any assistance, please contact the HR department.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

// Helper methods

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
