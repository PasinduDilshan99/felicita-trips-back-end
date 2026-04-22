package com.felicita.service.impl;

import com.felicita.model.request.ChatBotRequest;
import com.felicita.model.request.CreateInquiryRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.service.EmailHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class EmailHelperServiceImpl implements EmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailHelperServiceImpl.class);

    private static final String COMPANY_NAME = "Felicita Trips";
    private static final String COMPANY_EMAIL = "felicitatrips@gmail.com";
    private static final String COMPANY_PHONE = "+94701774488";

    @Override
    public String buildAdminTourBookingSubject(TourBookingInquiryRequest request) {
        return "New Tour Booking Inquiry - " + request.getName() +
                " (Tour ID: " + request.getTourId() + ")";
    }

    @Override
    public String buildAdminTourBookingBody(TourBookingInquiryRequest request, Long inquiryId) {
        StringBuilder body = new StringBuilder();

        String accentDark = "#0D4E4A";
        String brandLight = "#F0FDFA";
        String brandBorder = "#CCFBF1";
        String brandText = "#0F766E";
        String alertBg = "#FFFBEB";
        String alertBorder = "#FDE68A";
        String alertText = "#78350F";

        body.append("<!DOCTYPE html>\n<html>\n<head>\n");
        body.append("<meta charset='UTF-8'>\n");
        body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        body.append("<style>\n");
        body.append("* { box-sizing: border-box; margin: 0; padding: 0; }\n");
        body.append("body { font-family: Arial, sans-serif; background: #F4F4F0; color: #1A1A1A; }\n");
        body.append(".wrapper { padding: 40px 20px; }\n");
        body.append(".card { max-width: 620px; margin: 0 auto; background: #fff; border-radius: 12px; overflow: hidden; border: 1px solid #E5E5E0; }\n");

        // Header
        body.append(".header { background: " + accentDark + "; padding: 38px 36px 52px; position: relative; }\n");
        body.append(".header-arc { position: absolute; bottom: -1px; left: 0; right: 0; height: 26px; background: #fff; border-radius: 50% 50% 0 0 / 100% 100% 0 0; }\n");
        body.append(".logo-row { display: flex; align-items: center; gap: 9px; margin-bottom: 26px; }\n");
        body.append(".logo-mark { width: 30px; height: 30px; background: #14B8A6; border-radius: 7px; display: flex; align-items: center; justify-content: center; font-size: 15px; }\n");
        body.append(".logo-name { font-size: 11px; font-weight: 700; letter-spacing: 0.12em; text-transform: uppercase; color: rgba(255,255,255,0.85); }\n");
        body.append(".badge { display: inline-flex; align-items: center; gap: 6px; background: rgba(20,184,166,0.18); border: 1px solid rgba(20,184,166,0.38); color: #5EEAD4; font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; padding: 4px 11px; border-radius: 20px; margin-bottom: 14px; }\n");
        body.append(".badge-dot { display: inline-block; width: 6px; height: 6px; background: #14B8A6; border-radius: 50%; }\n");
        body.append(".header h1 { color: #fff; font-size: 23px; font-weight: 400; margin-bottom: 5px; font-family: Georgia, serif; letter-spacing: -0.02em; }\n");
        body.append(".header p { color: rgba(255,255,255,0.5); font-size: 13px; }\n");

        // Body
        body.append(".body { padding: 32px 36px 28px; background: #fff; }\n");
        body.append(".id-pill { display: flex; align-items: center; background: " + brandLight + "; border: 1px solid " + brandBorder + "; border-radius: 6px; padding: 10px 16px; margin-bottom: 24px; }\n");
        body.append(".id-pill .lbl { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: " + brandText + "; }\n");
        body.append(".id-pill .val { font-size: 14px; font-weight: 700; color: " + accentDark + "; margin-left: auto; letter-spacing: 0.05em; }\n");
        body.append(".sec-header { display: flex; align-items: center; gap: 9px; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #EBEBEB; }\n");
        body.append(".sec-icon { width: 27px; height: 27px; background: " + brandLight + "; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 13px; }\n");
        body.append(".sec-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; }\n");
        body.append(".meta-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 20px; }\n");
        body.append(".meta-item { background: #F8F8F6; border-radius: 8px; padding: 13px 15px; }\n");
        body.append(".meta-lbl { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 3px; }\n");
        body.append(".meta-val { font-size: 13px; color: #1A1A1A; font-weight: 500; }\n");
        body.append(".pkg-box { background: #F8F8F6; border-radius: 8px; padding: 16px 18px; display: flex; align-items: center; gap: 14px; }\n");
        body.append(".pkg-icon { width: 38px; height: 38px; background: " + accentDark + "; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 18px; flex-shrink: 0; }\n");
        body.append(".pkg-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 3px; }\n");
        body.append(".pkg-name { font-size: 14px; font-weight: 600; color: #1A1A1A; }\n");
        body.append(".pkg-sub { font-size: 12px; color: #777; margin-top: 2px; }\n");
        body.append(".divider { height: 1px; background: #EBEBEB; margin: 24px 0; }\n");
        body.append(".alert { background: " + alertBg + "; border: 1px solid " + alertBorder + "; border-radius: 8px; padding: 15px 18px; display: flex; align-items: flex-start; gap: 10px; }\n");
        body.append(".alert-dot { width: 8px; height: 8px; background: #F59E0B; border-radius: 50%; flex-shrink: 0; margin-top: 4px; }\n");
        body.append(".alert-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #92400E; margin-bottom: 3px; }\n");
        body.append(".alert-desc { font-size: 13px; color: " + alertText + "; }\n");

        // Footer
        body.append(".footer { background: #F8F8F6; border-top: 1px solid #EBEBEB; padding: 20px 36px; display: flex; align-items: center; justify-content: space-between; }\n");
        body.append(".ftr-name { font-size: 13px; font-weight: 700; color: #1A1A1A; }\n");
        body.append(".ftr-role { font-size: 11px; color: #999; }\n");
        body.append(".ftr-note { font-size: 11px; color: #AAA; font-style: italic; text-align: right; max-width: 160px; line-height: 1.5; font-family: Georgia, serif; }\n");
        body.append("</style>\n</head>\n<body>\n");

        body.append("<div class='wrapper'><div class='card'>\n");

        // ── Header ──────────────────────────────────────────────────────
        body.append("<div class='header'>\n");
        body.append("<div class='logo-row'><div class='logo-mark'>&#9992;</div>");
        body.append("<span class='logo-name'>").append(escapeHtml(COMPANY_NAME)).append("</span></div>\n");
        body.append("<div class='badge'><span class='badge-dot'></span> New Booking</div>\n");
        body.append("<h1>Tour Booking Inquiry</h1>\n");
        body.append("<p>A new booking inquiry requires your attention</p>\n");
        body.append("<div class='header-arc'></div>\n");
        body.append("</div>\n");

        // ── Body ────────────────────────────────────────────────────────
        body.append("<div class='body'>\n");

        // Inquiry ID pill
        body.append("<div class='id-pill'>\n");
        body.append("<span class='lbl'>Inquiry Reference</span>\n");
        body.append("<span class='val'>#INQ-").append(String.format("%05d", inquiryId)).append("</span>\n");
        body.append("</div>\n");

        // Customer details
        body.append("<div class='sec-header'><div class='sec-icon'>&#128100;</div><span class='sec-title'>Customer Details</span></div>\n");
        body.append("<div class='meta-grid'>\n");
        body.append(metaItem("Full Name", escapeHtml(request.getName())));
        body.append(metaItem("Email Address", escapeHtml(request.getEmail())));
        body.append(metaItem("Contact Number", escapeHtml(request.getContactNumber())));
        body.append(metaItem("Country", escapeHtml(request.getCountry())));
        body.append("</div>\n");

        body.append("<div class='divider'></div>\n");

        // Tour & Package
        body.append("<div class='sec-header'><div class='sec-icon'>&#127968;</div><span class='sec-title'>Tour &amp; Package</span></div>\n");
        body.append("<div class='meta-grid' style='margin-bottom:16px;'>\n");
        body.append(metaItem("Tour ID", escapeHtml(String.valueOf(request.getTourId()))));
        body.append(metaItem("Package ID", escapeHtml(String.valueOf(request.getPackageId()))));
        body.append("</div>\n");
        body.append("<div class='pkg-box'>\n");
        body.append("<div class='pkg-icon'>&#127956;</div>\n");
        body.append("<div>\n");
        body.append("<div class='pkg-label'>Selected Tour</div>\n");
        body.append("<div class='pkg-name'>").append(escapeHtml(request.getTourName())).append("</div>\n");
        body.append("<div class='pkg-sub'>Package: ").append(escapeHtml(request.getPackageName())).append("</div>\n");
        body.append("</div></div>\n");

        body.append("<div class='divider'></div>\n");

        // Alert
        body.append("<div class='alert'>\n");
        body.append("<div class='alert-dot'></div>\n");
        body.append("<div><div class='alert-title'>Action Required</div>\n");
        body.append("<div class='alert-desc'>Please contact the customer as soon as possible to confirm their booking.</div></div>\n");
        body.append("</div>\n");

        body.append("</div>\n"); // end .body

        // ── Footer ──────────────────────────────────────────────────────
        body.append("<div class='footer'>\n");
        body.append("<div><div class='ftr-name'>").append(escapeHtml(COMPANY_NAME)).append("</div>");
        body.append("<div class='ftr-role'>Premium Sri Lanka Travel</div></div>\n");
        body.append("<div class='ftr-note'>Automated notification — do not reply directly</div>\n");
        body.append("</div>\n");

        body.append("</div></div>\n</body>\n</html>");
        return body.toString();
    }

    @Override
    public String buildCustomerTourBookingSubject() {
        return "Your Tour Inquiry Has Been Received";
    }

    @Override
    public String buildCustomerTourBookingBody(TourBookingInquiryRequest request) {
        StringBuilder body = new StringBuilder();

        String accentDark = "#0D4E4A";
        String brandLight = "#F0FDFA";
        String brandBorder = "#CCFBF1";
        String brandText = "#0F766E";

        body.append("<!DOCTYPE html>\n<html>\n<head>\n");
        body.append("<meta charset='UTF-8'>\n");
        body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        body.append("<style>\n");
        body.append("* { box-sizing: border-box; margin: 0; padding: 0; }\n");
        body.append("body { font-family: Arial, sans-serif; background: #F4F4F0; color: #1A1A1A; }\n");
        body.append(".wrapper { padding: 40px 20px; }\n");
        body.append(".card { max-width: 620px; margin: 0 auto; background: #fff; border-radius: 12px; overflow: hidden; border: 1px solid #E5E5E0; }\n");

        // Header
        body.append(".header { background: " + accentDark + "; padding: 38px 36px 52px; position: relative; }\n");
        body.append(".header-arc { position: absolute; bottom: -1px; left: 0; right: 0; height: 26px; background: #fff; border-radius: 50% 50% 0 0 / 100% 100% 0 0; }\n");
        body.append(".logo-row { display: flex; align-items: center; gap: 9px; margin-bottom: 26px; }\n");
        body.append(".logo-mark { width: 30px; height: 30px; background: #14B8A6; border-radius: 7px; display: flex; align-items: center; justify-content: center; font-size: 15px; }\n");
        body.append(".logo-name { font-size: 11px; font-weight: 700; letter-spacing: 0.12em; text-transform: uppercase; color: rgba(255,255,255,0.85); }\n");
        body.append(".badge { display: inline-flex; align-items: center; gap: 6px; background: rgba(20,184,166,0.18); border: 1px solid rgba(20,184,166,0.38); color: #5EEAD4; font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; padding: 4px 11px; border-radius: 20px; margin-bottom: 14px; }\n");
        body.append(".badge-dot { display: inline-block; width: 6px; height: 6px; background: #14B8A6; border-radius: 50%; }\n");
        body.append(".header h1 { color: #fff; font-size: 23px; font-weight: 400; margin-bottom: 5px; font-family: Georgia, serif; letter-spacing: -0.02em; }\n");
        body.append(".header p { color: rgba(255,255,255,0.5); font-size: 13px; }\n");

        // Body
        body.append(".body { padding: 32px 36px 28px; background: #fff; }\n");
        body.append(".greeting { font-size: 15px; color: #1A1A1A; margin-bottom: 10px; }\n");
        body.append(".greeting strong { color: " + accentDark + "; }\n");
        body.append(".intro { font-size: 14px; color: #555; line-height: 1.7; margin-bottom: 26px; }\n");
        body.append(".sec-header { display: flex; align-items: center; gap: 9px; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #EBEBEB; }\n");
        body.append(".sec-icon { width: 27px; height: 27px; background: " + brandLight + "; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 13px; }\n");
        body.append(".sec-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; }\n");
        body.append(".summary-card { border: 1px solid #EBEBEB; border-radius: 10px; overflow: hidden; margin-bottom: 24px; }\n");
        body.append(".summary-row { display: flex; align-items: center; padding: 14px 18px; gap: 14px; }\n");
        body.append(".summary-row + .summary-row { border-top: 1px solid #EBEBEB; }\n");
        body.append(".s-icon { width: 32px; height: 32px; background: " + brandLight + "; border-radius: 7px; display: flex; align-items: center; justify-content: center; font-size: 15px; flex-shrink: 0; }\n");
        body.append(".s-lbl { font-size: 11px; color: #999; margin-bottom: 2px; }\n");
        body.append(".s-val { font-size: 13px; font-weight: 600; color: #1A1A1A; }\n");
        body.append(".divider { height: 1px; background: #EBEBEB; margin: 24px 0; }\n");
        body.append(".steps-lbl { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 12px; }\n");
        body.append(".step { display: flex; align-items: flex-start; gap: 12px; margin-bottom: 12px; }\n");
        body.append(".step-num { width: 24px; height: 24px; background: " + brandLight + "; border: 1px solid " + brandBorder + "; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 11px; font-weight: 700; color: " + brandText + "; flex-shrink: 0; margin-top: 1px; }\n");
        body.append(".step-title { font-size: 13px; font-weight: 600; color: #1A1A1A; margin-bottom: 1px; }\n");
        body.append(".step-desc { font-size: 12px; color: #777; line-height: 1.5; }\n");
        body.append(".step-line { width: 1px; height: 8px; background: " + brandBorder + "; margin: 1px 0 1px 11px; }\n");
        body.append(".cta { text-align: center; margin-top: 24px; }\n");
        body.append(".btn { display: inline-block; background: " + accentDark + "; color: #fff; font-size: 13px; font-weight: 600; letter-spacing: 0.04em; padding: 12px 28px; border-radius: 6px; text-decoration: none; }\n");
        body.append(".btn-sub { font-size: 11px; color: #AAA; margin-top: 7px; }\n");

        // Footer
        body.append(".footer { background: #F8F8F6; border-top: 1px solid #EBEBEB; padding: 20px 36px; display: flex; align-items: center; justify-content: space-between; }\n");
        body.append(".ftr-name { font-size: 13px; font-weight: 700; color: #1A1A1A; }\n");
        body.append(".ftr-role { font-size: 11px; color: #999; }\n");
        body.append(".ftr-note { font-size: 11px; color: #AAA; font-style: italic; text-align: right; max-width: 160px; line-height: 1.5; font-family: Georgia, serif; }\n");
        body.append("</style>\n</head>\n<body>\n");

        body.append("<div class='wrapper'><div class='card'>\n");

        // ── Header ──────────────────────────────────────────────────────
        body.append("<div class='header'>\n");
        body.append("<div class='logo-row'><div class='logo-mark'>&#9992;</div>");
        body.append("<span class='logo-name'>").append(escapeHtml(COMPANY_NAME)).append("</span></div>\n");
        body.append("<div class='badge'><span class='badge-dot'></span> Booking Received</div>\n");
        body.append("<h1>We've Got Your Request</h1>\n");
        body.append("<p>Your tour booking inquiry is confirmed</p>\n");
        body.append("<div class='header-arc'></div>\n");
        body.append("</div>\n");

        // ── Body ────────────────────────────────────────────────────────
        body.append("<div class='body'>\n");
        body.append("<p class='greeting'>Dear <strong>").append(escapeHtml(request.getName())).append("</strong>,</p>\n");
        body.append("<p class='intro'>Thank you for your interest in our tours. We've successfully received your booking inquiry ")
                .append("and our team will review your request and get back to you shortly.</p>\n");

        // Summary
        body.append("<div class='sec-header'><div class='sec-icon'>&#127956;</div><span class='sec-title'>Your Booking Summary</span></div>\n");
        body.append("<div class='summary-card'>\n");
        body.append(summaryRow("&#127758;", "Tour / Destination", escapeHtml(request.getTourName())));
        body.append(summaryRow("&#128197;", "Selected Package", escapeHtml(request.getPackageName())));
        body.append("</div>\n");

        body.append("<div class='divider'></div>\n");

        // Next steps
        body.append("<div class='steps-lbl'>What happens next</div>\n");
        body.append(buildStep("1", "Team review", "Our specialists will review your inquiry and tour availability."));
        body.append("<div class='step-line'></div>\n");
        body.append(buildStep("2", "Personalised proposal", "We'll prepare a tailored quote and itinerary for your selected package."));
        body.append("<div class='step-line'></div>\n");
        body.append(buildStep("3", "We'll be in touch", "Expect a detailed response from us within 24 hours."));

        // CTA
        body.append("<div class='cta'>\n");
        body.append("<a href='https://felicitatrips.com' class='btn'>Explore More Tours</a>\n");
        body.append("<div class='btn-sub'>felicitatrips.com</div>\n");
        body.append("</div>\n");

        body.append("</div>\n"); // end .body

        // ── Footer ──────────────────────────────────────────────────────
        body.append("<div class='footer'>\n");
        body.append("<div><div class='ftr-name'>").append(escapeHtml(COMPANY_NAME)).append(" Team</div>");
        body.append("<div class='ftr-role'>Premium Sri Lanka Travel</div></div>\n");
        body.append("<div class='ftr-note'>Creating unforgettable Sri Lanka experiences</div>\n");
        body.append("</div>\n");

        body.append("</div></div>\n</body>\n</html>");
        return body.toString();
    }

// ── Private helpers ─────────────────────────────────────────────────────────

    private String summaryRow(String icon, String label, String value) {
        return "<div class='summary-row'>\n" +
                "<div class='s-icon'>" + icon + "</div>\n" +
                "<div><div class='s-lbl'>" + label + "</div>" +
                "<div class='s-val'>" + value + "</div></div>\n" +
                "</div>\n";
    }

    @Override
    public String buildInquirySubject(CreateInquiryRequest request) {
        return "New Inquiry from " + request.getName() +
                " - " + request.getPreferredDestination();
    }

    public String buildChatBotInquirySubject(ChatBotRequest request) {
        String flowType = request.getPreferences().getFlowType();
        String flowTypeDisplay = getFlowTypeDisplayName(flowType);

        return String.format("[Felicita Trips] New %s Inquiry from %s",
                flowTypeDisplay, request.getName());
    }

    public String buildChatBotInquiryEmailBody(ChatBotRequest request) {
        StringBuilder body = new StringBuilder();

        String accentColor = "#0D4E4A";
        String brandColor = "#14B8A6";
        String brandLight = "#F0FDFA";
        String brandBorder = "#CCFBF1";
        String brandText = "#0F766E";
        String alertBg = "#FFFBEB";
        String alertBorder = "#FDE68A";
        String alertText = "#78350F";
        String alertTitleColor = "#92400E";

        body.append("<!DOCTYPE html>\n<html>\n<head>\n<meta charset='UTF-8'>\n");
        body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        body.append("<style>\n");
        body.append("* { box-sizing: border-box; margin: 0; padding: 0; }\n");
        body.append("body { font-family: Arial, sans-serif; background: #F4F4F0; color: #1A1A1A; }\n");
        body.append(".wrapper { padding: 40px 20px; background: #F4F4F0; }\n");
        body.append(".card { max-width: 620px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; border: 1px solid #E5E5E0; }\n");

        // Header styles
        body.append(".header { background: " + accentColor + "; padding: 40px 36px 32px; position: relative; }\n");
        body.append(".header-accent { position: absolute; top: 0; right: 0; width: 140px; height: 140px; background: rgba(255,255,255,0.04); border-radius: 0 0 0 140px; }\n");
        body.append(".logo { display: flex; align-items: center; gap: 10px; margin-bottom: 28px; }\n");
        body.append(".logo-mark { width: 32px; height: 32px; background: " + brandColor + "; border-radius: 8px; display: flex; align-items: center; justify-content: center; }\n");
        body.append(".logo-name { color: rgba(255,255,255,0.9); font-size: 12px; letter-spacing: 0.12em; text-transform: uppercase; font-weight: 700; }\n");
        body.append(".badge { display: inline-flex; align-items: center; gap: 6px; background: rgba(20,184,166,0.18); border: 1px solid rgba(20,184,166,0.38); color: #5EEAD4; font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; padding: 5px 12px; border-radius: 20px; margin-bottom: 16px; }\n");
        body.append(".badge-dot { display: inline-block; width: 6px; height: 6px; background: " + brandColor + "; border-radius: 50%; }\n");
        body.append(".header h1 { color: #ffffff; font-size: 26px; font-weight: 400; margin-bottom: 6px; font-family: Georgia, serif; letter-spacing: -0.02em; }\n");
        body.append(".header p { color: rgba(255,255,255,0.5); font-size: 13px; }\n");

        // Body styles
        body.append(".body { padding: 32px 36px; background: #ffffff; }\n");
        body.append(".section-header { display: flex; align-items: center; gap: 10px; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #EBEBEB; }\n");
        body.append(".section-icon { width: 28px; height: 28px; background: " + brandLight + "; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 13px; }\n");
        body.append(".section-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #888; }\n");
        body.append(".meta-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 28px; }\n");
        body.append(".meta-item { background: #F8F8F6; border-radius: 8px; padding: 14px 16px; }\n");
        body.append(".meta-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 4px; }\n");
        body.append(".meta-value { font-size: 13px; color: #1A1A1A; font-weight: 500; }\n");
        body.append(".divider { height: 1px; background: #EBEBEB; margin: 24px 0; }\n");

        // Preferences / Tags
        body.append(".pref-group { margin-bottom: 16px; }\n");
        body.append(".pref-label { font-size: 12px; color: #666; margin-bottom: 7px; }\n");
        body.append(".tags { display: flex; flex-wrap: wrap; gap: 6px; }\n");
        body.append(".tag { font-size: 12px; background: " + brandLight + "; color: " + brandText + "; border: 1px solid " + brandBorder + "; padding: 4px 11px; border-radius: 20px; font-weight: 500; }\n");

        // Alert
        body.append(".alert { background: " + alertBg + "; border: 1px solid " + alertBorder + "; border-radius: 8px; padding: 16px 20px; }\n");
        body.append(".alert-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: " + alertTitleColor + "; margin-bottom: 4px; }\n");
        body.append(".alert-desc { font-size: 13px; color: " + alertText + "; }\n");

        // Footer
        body.append(".footer { background: #F8F8F6; padding: 20px 36px; display: flex; align-items: center; justify-content: space-between; border-top: 1px solid #EBEBEB; }\n");
        body.append(".footer-brand { font-size: 12px; color: #888; }\n");
        body.append(".footer-brand strong { color: #1A1A1A; font-weight: 700; display: block; margin-bottom: 2px; font-size: 13px; }\n");
        body.append(".footer-note { font-size: 11px; color: #AAA; text-align: right; max-width: 180px; line-height: 1.5; }\n");
        body.append("</style>\n</head>\n<body>\n");

        body.append("<div class='wrapper'><div class='card'>\n");

        // ── Header ────────────────────────────────────────────────
        body.append("<div class='header'>\n");
        body.append("<div class='header-accent'></div>\n");
        body.append("<div class='logo'>\n");
        body.append("<div class='logo-mark'>&#9992;</div>\n");
        body.append("<span class='logo-name'>").append(escapeHtml(COMPANY_NAME)).append("</span>\n");
        body.append("</div>\n");
        body.append("<div class='badge'><span class='badge-dot'></span> New Inquiry</div>\n");
        body.append("<h1>Travel Inquiry Received</h1>\n");
        body.append("<p>A new customer inquiry requires your attention</p>\n");
        body.append("</div>\n");

        // ── Body ──────────────────────────────────────────────────
        body.append("<div class='body'>\n");

        // Customer Details
        body.append("<div class='section-header'>\n");
        body.append("<div class='section-icon'>&#128100;</div>\n");
        body.append("<span class='section-title'>Customer Details</span>\n");
        body.append("</div>\n");
        body.append("<div class='meta-grid'>\n");
        body.append(metaItem("Full Name", escapeHtml(request.getName())));
        body.append(metaItem("Email Address", escapeHtml(request.getEmail())));
        body.append(metaItem("Phone Number", escapeHtml(request.getPhone())));
        body.append(metaItem("Inquiry Date", getCurrentDateTime()));
        body.append("</div>\n");

        body.append("<div class='divider'></div>\n");

        // Preferences Section
        Map<String, List<String>> selections = request.getPreferences().getSelections();
        String flowType = request.getPreferences().getFlowType();

        switch (flowType) {
            case "tours":
                body.append("<div class='section-header'>\n");
                body.append("<div class='section-icon'>&#9992;</div>\n");
                body.append("<span class='section-title'>Tour Package Preferences</span>\n");
                body.append("</div>\n");
                appendTagGroup(body, selections, "category", "Tour Categories");
                appendTagGroup(body, selections, "duration", "Preferred Duration");
                appendTagGroup(body, selections, "tourType", "Tour Types");
                appendTagGroup(body, selections, "season", "Preferred Seasons");
                appendTagGroup(body, selections, "budget", "Budget Range");
                break;

            case "activities":
                body.append("<div class='section-header'>\n");
                body.append("<div class='section-icon'>&#127914;</div>\n");
                body.append("<span class='section-title'>Activities &amp; Experiences Preferences</span>\n");
                body.append("</div>\n");
                appendTagGroup(body, selections, "category", "Activity Types");
                appendTagGroup(body, selections, "season", "Preferred Seasons");
                appendTagGroup(body, selections, "duration", "Preferred Duration");
                break;

            case "destinations":
                body.append("<div class='section-header'>\n");
                body.append("<div class='section-icon'>&#127965;</div>\n");
                body.append("<span class='section-title'>Destination Preferences</span>\n");
                body.append("</div>\n");
                appendTagGroup(body, selections, "category", "Destination Types");
                appendTagGroup(body, selections, "location", "Preferred Locations");
                appendTagGroup(body, selections, "rating", "Minimum Ratings");
                break;

            default:
                body.append("<div class='section-header'>\n");
                body.append("<div class='section-icon'>&#128203;</div>\n");
                body.append("<span class='section-title'>General Inquiry</span>\n");
                body.append("</div>\n");
                break;
        }

        body.append("<div class='divider'></div>\n");

        // Action Alert
        body.append("<div class='alert'>\n");
        body.append("<div class='alert-title'>&#9888; Action Required</div>\n");
        body.append("<div class='alert-desc'>Please respond to this customer within 24 hours to maintain service standards.</div>\n");
        body.append("</div>\n");

        body.append("</div>\n"); // end .body

        // ── Footer ────────────────────────────────────────────────
        body.append("<div class='footer'>\n");
        body.append("<div class='footer-brand'><strong>").append(escapeHtml(COMPANY_NAME)).append("</strong>Premium Sri Lanka Travel Experiences</div>\n");
        body.append("<div class='footer-note'>This is an automated notification. Please do not reply to this email.</div>\n");
        body.append("</div>\n");

        body.append("</div></div>\n</body>\n</html>");

        return body.toString();
    }

    private String metaItem(String label, String value) {
        return "<div class='meta-item'>\n" +
                "<div class='meta-label'>" + label + "</div>\n" +
                "<div class='meta-value'>" + value + "</div>\n" +
                "</div>\n";
    }

    private void appendTagGroup(StringBuilder body, Map<String, List<String>> selections,
                                String key, String label) {
        List<String> items = selections.get(key);
        if (items == null || items.isEmpty()) return;

        body.append("<div class='pref-group'>\n");
        body.append("<div class='pref-label'>").append(escapeHtml(label)).append("</div>\n");
        body.append("<div class='tags'>\n");
        for (String item : items) {
            body.append("<span class='tag'>").append(escapeHtml(item)).append("</span>\n");
        }
        body.append("</div>\n</div>\n");
    }

    public String buildCustomerConfirmationEmailBody(ChatBotRequest request) {
        StringBuilder body = new StringBuilder();

        String accentDark = "#0D4E4A";
        String brandColor = "#14B8A6";
        String brandLight = "#F0FDFA";
        String brandBorder = "#CCFBF1";
        String brandText = "#0F766E";

        body.append("<!DOCTYPE html>\n<html>\n<head>\n");
        body.append("<meta charset='UTF-8'>\n");
        body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        body.append("<style>\n");
        body.append("* { box-sizing: border-box; margin: 0; padding: 0; }\n");
        body.append("body { font-family: Arial, sans-serif; background: #F4F4F0; color: #1A1A1A; }\n");
        body.append(".wrapper { padding: 40px 20px; }\n");
        body.append(".card { max-width: 580px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; border: 1px solid #E5E5E0; }\n");

        // Header
        body.append(".header { background: " + accentDark + "; padding: 44px 36px 56px; position: relative; text-align: center; }\n");
        body.append(".header-arc { position: absolute; bottom: -1px; left: 0; right: 0; height: 28px; background: #ffffff; border-radius: 50% 50% 0 0 / 100% 100% 0 0; }\n");
        body.append(".check-ring { width: 56px; height: 56px; background: rgba(20,184,166,0.18); border: 2px solid rgba(20,184,166,0.45); border-radius: 50%; margin: 0 auto 20px; display: flex; align-items: center; justify-content: center; }\n");
        body.append(".check-icon { font-size: 22px; }\n");
        body.append(".header h1 { color: #ffffff; font-size: 24px; font-weight: 400; margin-bottom: 8px; font-family: Georgia, serif; letter-spacing: -0.02em; }\n");
        body.append(".header p { color: rgba(255,255,255,0.5); font-size: 13px; }\n");

        // Body
        body.append(".body { padding: 36px 36px 28px; background: #ffffff; }\n");
        body.append(".greeting { font-size: 15px; color: #1A1A1A; margin-bottom: 12px; }\n");
        body.append(".greeting strong { color: " + accentDark + "; }\n");
        body.append(".intro { font-size: 14px; color: #555; line-height: 1.7; margin-bottom: 28px; }\n");

        // Steps
        body.append(".steps-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 14px; }\n");
        body.append(".step { display: flex; align-items: flex-start; gap: 14px; margin-bottom: 14px; }\n");
        body.append(".step-num { width: 26px; height: 26px; background: " + brandLight + "; border: 1px solid " + brandBorder + "; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 11px; font-weight: 700; color: " + brandText + "; flex-shrink: 0; margin-top: 1px; }\n");
        body.append(".step-title { font-size: 13px; font-weight: 600; color: #1A1A1A; margin-bottom: 2px; }\n");
        body.append(".step-desc { font-size: 12px; color: #777; line-height: 1.5; }\n");
        body.append(".step-connector { width: 1px; height: 10px; background: " + brandBorder + "; margin: 2px 0 2px 12px; }\n");

        // Divider
        body.append(".divider { height: 1px; background: #EBEBEB; margin: 28px 0; }\n");

        // Contact
        body.append(".contact-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 12px; }\n");
        body.append(".contact-row { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }\n");
        body.append(".contact-icon { width: 28px; height: 28px; background: #F8F8F6; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 14px; flex-shrink: 0; }\n");
        body.append(".contact-text { font-size: 13px; color: #1A1A1A; }\n");
        body.append(".contact-sub { font-size: 12px; color: #999; }\n");

        // CTA
        body.append(".cta { text-align: center; margin-top: 28px; }\n");
        body.append(".btn { display: inline-block; background: " + accentDark + "; color: #ffffff; font-size: 13px; font-weight: 600; letter-spacing: 0.04em; padding: 12px 28px; border-radius: 6px; text-decoration: none; }\n");
        body.append(".btn-sub { font-size: 11px; color: #AAA; margin-top: 8px; }\n");

        // Footer
        body.append(".footer { background: #F8F8F6; border-top: 1px solid #EBEBEB; padding: 22px 36px; display: flex; align-items: center; justify-content: space-between; }\n");
        body.append(".footer-name { font-size: 13px; font-weight: 700; color: #1A1A1A; }\n");
        body.append(".footer-role { font-size: 12px; color: #999; }\n");
        body.append(".footer-tag { font-size: 12px; color: #AAA; font-style: italic; text-align: right; max-width: 160px; line-height: 1.5; font-family: Georgia, serif; }\n");
        body.append("</style>\n</head>\n<body>\n");

        body.append("<div class='wrapper'><div class='card'>\n");

        // ── Header ──────────────────────────────────────────────────────
        body.append("<div class='header'>\n");
        body.append("<div class='check-ring'><span class='check-icon'>&#10003;</span></div>\n");
        body.append("<h1>Inquiry Requested</h1>\n");
        body.append("<p>We've received your request and are on it</p>\n");
        body.append("<div class='header-arc'></div>\n");
        body.append("</div>\n");

        // ── Body ────────────────────────────────────────────────────────
        body.append("<div class='body'>\n");
        body.append("<p class='greeting'>Dear <strong>").append(escapeHtml(request.getName())).append("</strong>,</p>\n");
        body.append("<p class='intro'>Thank you for reaching out to <strong style='color:#1A1A1A;'>Felicita Trips</strong>. ")
                .append("Your travel inquiry has been received and our team is already reviewing your preferences ")
                .append("to craft the perfect Sri Lanka experience for you.</p>\n");

        // Steps
        body.append("<div class='steps-label'>What happens next</div>\n");
        body.append(buildStep("1", "Specialist review",
                "A dedicated travel specialist will carefully review your preferences and requirements."));
        body.append("<div class='step-connector'></div>\n");
        body.append(buildStep("2", "Personalised recommendations",
                "We'll prepare curated tour options tailored specifically to your selections."));
        body.append("<div class='step-connector'></div>\n");
        body.append(buildStep("3", "Response within 24 hours",
                "You'll hear from us with a detailed proposal and next steps."));

        body.append("<div class='divider'></div>\n");

        // Contact
        body.append("<div class='contact-label'>Need help sooner?</div>\n");
        body.append("<div class='contact-row'>\n");
        body.append("<div class='contact-icon'>&#128222;</div>\n");
        body.append("<span class='contact-text'>").append(escapeHtml(COMPANY_PHONE))
                .append(" <span class='contact-sub'>24/7 Support</span></span>\n");
        body.append("</div>\n");
        body.append("<div class='contact-row'>\n");
        body.append("<div class='contact-icon'>&#9993;</div>\n");
        body.append("<span class='contact-text'>").append(escapeHtml(COMPANY_EMAIL)).append("</span>\n");
        body.append("</div>\n");

        // CTA
        body.append("<div class='cta'>\n");
        body.append("<a href='https://felicitatrips.com/sri-lankan-tours' class='btn'>Explore Our Tours</a>\n");
        body.append("<div class='btn-sub'>felicitatrips.com</div>\n");
        body.append("</div>\n");

        body.append("</div>\n"); // end .body

        // ── Footer ──────────────────────────────────────────────────────
        body.append("<div class='footer'>\n");
        body.append("<div><div class='footer-name'>").append(escapeHtml(COMPANY_NAME)).append(" Team</div>\n");
        body.append("<div class='footer-role'>Premium Sri Lanka Travel</div></div>\n");
        body.append("<div class='footer-tag'>Creating unforgettable Sri Lanka experiences</div>\n");
        body.append("</div>\n");

        body.append("</div></div>\n</body>\n</html>");

        return body.toString();
    }

// ── Private helper ──────────────────────────────────────────────────────────

    private String buildStep(String number, String title, String description) {
        return "<div class='step'>\n" +
                "<div class='step-num'>" + number + "</div>\n" +
                "<div>\n" +
                "<div class='step-title'>" + escapeHtml(title) + "</div>\n" +
                "<div class='step-desc'>" + escapeHtml(description) + "</div>\n" +
                "</div>\n" +
                "</div>\n";
    }

    @Override
    public String buildInquiryEmailBody(CreateInquiryRequest request) {
        StringBuilder body = new StringBuilder();

        String accentDark = "#0D4E4A";
        String brandColor = "#14B8A6";
        String brandLight = "#F0FDFA";
        String brandBorder = "#CCFBF1";
        String brandText = "#0F766E";
        String alertBg = "#FFFBEB";
        String alertBorder = "#FDE68A";
        String alertText = "#78350F";
        String alertTitleColor = "#92400E";

        body.append("<!DOCTYPE html>\n<html>\n<head>\n");
        body.append("<meta charset='UTF-8'>\n");
        body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        body.append("<style>\n");
        body.append("* { box-sizing: border-box; margin: 0; padding: 0; }\n");
        body.append("body { font-family: Arial, sans-serif; background: #F4F4F0; color: #1A1A1A; }\n");
        body.append(".wrapper { padding: 40px 20px; background: #F4F4F0; }\n");
        body.append(".card { max-width: 620px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; border: 1px solid #E5E5E0; }\n");

        // Header styles
        body.append(".header { background: " + accentDark + "; padding: 40px 36px 32px; position: relative; }\n");
        body.append(".header-accent { position: absolute; top: 0; right: 0; width: 140px; height: 140px; background: rgba(255,255,255,0.04); border-radius: 0 0 0 140px; }\n");
        body.append(".logo { display: flex; align-items: center; gap: 10px; margin-bottom: 28px; }\n");
        body.append(".logo-mark { width: 32px; height: 32px; background: " + brandColor + "; border-radius: 8px; display: flex; align-items: center; justify-content: center; }\n");
        body.append(".logo-name { color: rgba(255,255,255,0.9); font-size: 12px; letter-spacing: 0.12em; text-transform: uppercase; font-weight: 700; }\n");
        body.append(".badge { display: inline-flex; align-items: center; gap: 6px; background: rgba(20,184,166,0.18); border: 1px solid rgba(20,184,166,0.38); color: #5EEAD4; font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; padding: 5px 12px; border-radius: 20px; margin-bottom: 16px; }\n");
        body.append(".badge-dot { display: inline-block; width: 6px; height: 6px; background: " + brandColor + "; border-radius: 50%; }\n");
        body.append(".header h1 { color: #ffffff; font-size: 26px; font-weight: 400; margin-bottom: 6px; font-family: Georgia, serif; letter-spacing: -0.02em; }\n");
        body.append(".header p { color: rgba(255,255,255,0.5); font-size: 13px; }\n");

        // Body styles
        body.append(".body { padding: 32px 36px; background: #ffffff; }\n");
        body.append(".section-header { display: flex; align-items: center; gap: 10px; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #EBEBEB; }\n");
        body.append(".section-icon { width: 28px; height: 28px; background: " + brandLight + "; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 13px; }\n");
        body.append(".section-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #888; }\n");
        body.append(".meta-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 20px; }\n");
        body.append(".meta-item { background: #F8F8F6; border-radius: 8px; padding: 14px 16px; }\n");
        body.append(".meta-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 4px; }\n");
        body.append(".meta-value { font-size: 13px; color: #1A1A1A; font-weight: 500; }\n");
        body.append(".full-width { grid-column: 1 / -1; }\n");
        body.append(".message-box { background: #F8F8F6; border-radius: 8px; padding: 16px; margin-top: 10px; }\n");
        body.append(".message-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 8px; }\n");
        body.append(".message-content { font-size: 13px; color: #1A1A1A; line-height: 1.6; }\n");
        body.append(".divider { height: 1px; background: #EBEBEB; margin: 24px 0; }\n");

        // Alert
        body.append(".alert { background: " + alertBg + "; border: 1px solid " + alertBorder + "; border-radius: 8px; padding: 16px 20px; }\n");
        body.append(".alert-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: " + alertTitleColor + "; margin-bottom: 4px; }\n");
        body.append(".alert-desc { font-size: 13px; color: " + alertText + "; }\n");

        // Footer
        body.append(".footer { background: #F8F8F6; padding: 20px 36px; display: flex; align-items: center; justify-content: space-between; border-top: 1px solid #EBEBEB; }\n");
        body.append(".footer-brand { font-size: 12px; color: #888; }\n");
        body.append(".footer-brand strong { color: #1A1A1A; font-weight: 700; display: block; margin-bottom: 2px; font-size: 13px; }\n");
        body.append(".footer-note { font-size: 11px; color: #AAA; text-align: right; max-width: 180px; line-height: 1.5; }\n");
        body.append("</style>\n</head>\n<body>\n");

        body.append("<div class='wrapper'><div class='card'>\n");

        // Header
        body.append("<div class='header'>\n");
        body.append("<div class='header-accent'></div>\n");
        body.append("<div class='logo'>\n");
        body.append("<div class='logo-mark'>&#9992;</div>\n");
        body.append("<span class='logo-name'>").append(escapeHtml(COMPANY_NAME)).append("</span>\n");
        body.append("</div>\n");
        body.append("<div class='badge'><span class='badge-dot'></span> New Inquiry</div>\n");
        body.append("<h1>Travel Inquiry Received</h1>\n");
        body.append("<p>A new travel inquiry requires your attention</p>\n");
        body.append("</div>\n");

        // Body
        body.append("<div class='body'>\n");

        // Customer Details
        body.append("<div class='section-header'>\n");
        body.append("<div class='section-icon'>&#128100;</div>\n");
        body.append("<span class='section-title'>Customer Details</span>\n");
        body.append("</div>\n");
        body.append("<div class='meta-grid'>\n");
        body.append(metaItem("Full Name", escapeHtml(request.getName())));
        body.append(metaItem("Email Address", escapeHtml(request.getEmail())));
        body.append(metaItem("Phone Number", escapeHtml(request.getPhoneNumber())));
        body.append(metaItem("Country", escapeHtml(request.getCountry())));
        body.append(metaItem("Preferred Contact", escapeHtml(request.getPreferredContactMethod())));
        body.append(metaItem("Inquiry Date", getCurrentDateTime()));
        body.append("</div>\n");

        body.append("<div class='divider'></div>\n");

        // Travel Details
        body.append("<div class='section-header'>\n");
        body.append("<div class='section-icon'>&#127956;</div>\n");
        body.append("<span class='section-title'>Travel Details</span>\n");
        body.append("</div>\n");
        body.append("<div class='meta-grid'>\n");
        body.append(metaItem("Preferred Destination", escapeHtml(request.getPreferredDestination())));
        body.append(metaItem("Number of Adults", String.valueOf(request.getAdults())));
        body.append(metaItem("Number of Kids", String.valueOf(request.getKids())));
        body.append(metaItem("Arrival Date", escapeHtml(request.getArrivalDate())));
        body.append(metaItem("Departure Date", escapeHtml(request.getDepartureDate())));
        body.append("</div>\n");

        // Message
        if (request.getMessage() != null && !request.getMessage().trim().isEmpty()) {
            body.append("<div class='message-box'>\n");
            body.append("<div class='message-label'>Customer Message</div>\n");
            body.append("<div class='message-content'>").append(escapeHtml(request.getMessage())).append("</div>\n");
            body.append("</div>\n");
        }

        body.append("<div class='divider'></div>\n");

        // Action Alert
        body.append("<div class='alert'>\n");
        body.append("<div class='alert-title'>&#9888; Action Required</div>\n");
        body.append("<div class='alert-desc'>Please respond to this customer within 24 hours to maintain service standards.</div>\n");
        body.append("</div>\n");

        body.append("</div>\n");

        // Footer
        body.append("<div class='footer'>\n");
        body.append("<div class='footer-brand'><strong>").append(escapeHtml(COMPANY_NAME)).append("</strong>Premium Sri Lanka Travel Experiences</div>\n");
        body.append("<div class='footer-note'>This is an automated notification. Please do not reply to this email.</div>\n");
        body.append("</div>\n");

        body.append("</div></div>\n</body>\n</html>");

        return body.toString();
    }

    @Override
    public String buildCustomerInquiryEmailBody(CreateInquiryRequest request) {
        StringBuilder body = new StringBuilder();

        String accentDark = "#0D4E4A";
        String brandColor = "#14B8A6";
        String brandLight = "#F0FDFA";
        String brandBorder = "#CCFBF1";
        String brandText = "#0F766E";

        body.append("<!DOCTYPE html>\n<html>\n<head>\n");
        body.append("<meta charset='UTF-8'>\n");
        body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        body.append("<style>\n");
        body.append("* { box-sizing: border-box; margin: 0; padding: 0; }\n");
        body.append("body { font-family: Arial, sans-serif; background: #F4F4F0; color: #1A1A1A; }\n");
        body.append(".wrapper { padding: 40px 20px; }\n");
        body.append(".card { max-width: 580px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; border: 1px solid #E5E5E0; }\n");

        // Header
        body.append(".header { background: " + accentDark + "; padding: 44px 36px 56px; position: relative; text-align: center; }\n");
        body.append(".header-arc { position: absolute; bottom: -1px; left: 0; right: 0; height: 28px; background: #ffffff; border-radius: 50% 50% 0 0 / 100% 100% 0 0; }\n");
        body.append(".check-ring { width: 56px; height: 56px; background: rgba(20,184,166,0.18); border: 2px solid rgba(20,184,166,0.45); border-radius: 50%; margin: 0 auto 20px; display: flex; align-items: center; justify-content: center; }\n");
        body.append(".check-icon { font-size: 22px; }\n");
        body.append(".header h1 { color: #ffffff; font-size: 24px; font-weight: 400; margin-bottom: 8px; font-family: Georgia, serif; letter-spacing: -0.02em; }\n");
        body.append(".header p { color: rgba(255,255,255,0.5); font-size: 13px; }\n");

        // Body
        body.append(".body { padding: 36px 36px 28px; background: #ffffff; }\n");
        body.append(".greeting { font-size: 15px; color: #1A1A1A; margin-bottom: 12px; }\n");
        body.append(".greeting strong { color: " + accentDark + "; }\n");
        body.append(".intro { font-size: 14px; color: #555; line-height: 1.7; margin-bottom: 28px; }\n");

        // Summary Card
        body.append(".summary-card { border: 1px solid #EBEBEB; border-radius: 10px; overflow: hidden; margin-bottom: 28px; }\n");
        body.append(".summary-header { background: " + brandLight + "; padding: 12px 18px; border-bottom: 1px solid #EBEBEB; }\n");
        body.append(".summary-header h3 { font-size: 13px; font-weight: 600; color: " + accentDark + "; }\n");
        body.append(".summary-row { display: flex; align-items: center; padding: 12px 18px; gap: 14px; }\n");
        body.append(".summary-row + .summary-row { border-top: 1px solid #EBEBEB; }\n");
        body.append(".s-icon { width: 32px; height: 32px; background: " + brandLight + "; border-radius: 7px; display: flex; align-items: center; justify-content: center; font-size: 15px; flex-shrink: 0; }\n");
        body.append(".s-lbl { font-size: 11px; color: #999; margin-bottom: 2px; }\n");
        body.append(".s-val { font-size: 13px; font-weight: 600; color: #1A1A1A; }\n");

        // Steps
        body.append(".steps-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 14px; }\n");
        body.append(".step { display: flex; align-items: flex-start; gap: 14px; margin-bottom: 14px; }\n");
        body.append(".step-num { width: 26px; height: 26px; background: " + brandLight + "; border: 1px solid " + brandBorder + "; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 11px; font-weight: 700; color: " + brandText + "; flex-shrink: 0; margin-top: 1px; }\n");
        body.append(".step-title { font-size: 13px; font-weight: 600; color: #1A1A1A; margin-bottom: 2px; }\n");
        body.append(".step-desc { font-size: 12px; color: #777; line-height: 1.5; }\n");
        body.append(".step-connector { width: 1px; height: 10px; background: " + brandBorder + "; margin: 2px 0 2px 12px; }\n");

        // Divider
        body.append(".divider { height: 1px; background: #EBEBEB; margin: 28px 0; }\n");

        // Contact
        body.append(".contact-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 12px; }\n");
        body.append(".contact-row { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }\n");
        body.append(".contact-icon { width: 28px; height: 28px; background: #F8F8F6; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 14px; flex-shrink: 0; }\n");
        body.append(".contact-text { font-size: 13px; color: #1A1A1A; }\n");
        body.append(".contact-sub { font-size: 12px; color: #999; }\n");

        // CTA
        body.append(".cta { text-align: center; margin-top: 28px; }\n");
        body.append(".btn { display: inline-block; background: " + accentDark + "; color: #ffffff; font-size: 13px; font-weight: 600; letter-spacing: 0.04em; padding: 12px 28px; border-radius: 6px; text-decoration: none; }\n");
        body.append(".btn-sub { font-size: 11px; color: #AAA; margin-top: 8px; }\n");

        // Footer
        body.append(".footer { background: #F8F8F6; border-top: 1px solid #EBEBEB; padding: 22px 36px; display: flex; align-items: center; justify-content: space-between; }\n");
        body.append(".footer-name { font-size: 13px; font-weight: 700; color: #1A1A1A; }\n");
        body.append(".footer-role { font-size: 12px; color: #999; }\n");
        body.append(".footer-tag { font-size: 12px; color: #AAA; font-style: italic; text-align: right; max-width: 160px; line-height: 1.5; font-family: Georgia, serif; }\n");
        body.append("</style>\n</head>\n<body>\n");

        body.append("<div class='wrapper'><div class='card'>\n");

        // Header
        body.append("<div class='header'>\n");
        body.append("<div class='check-ring'><span class='check-icon'>&#10003;</span></div>\n");
        body.append("<h1>Inquiry Received!</h1>\n");
        body.append("<p>We've received your travel inquiry and will respond shortly</p>\n");
        body.append("<div class='header-arc'></div>\n");
        body.append("</div>\n");

        // Body
        body.append("<div class='body'>\n");
        body.append("<p class='greeting'>Dear <strong>").append(escapeHtml(request.getName())).append("</strong>,</p>\n");
        body.append("<p class='intro'>Thank you for reaching out to <strong style='color:#1A1A1A;'>Felicita Trips</strong>. ")
                .append("We have successfully received your travel inquiry to <strong>")
                .append(escapeHtml(request.getPreferredDestination())).append("</strong>. ")
                .append("Our team is already reviewing your requirements to craft the perfect Sri Lanka experience for you.</p>\n");

        // Summary
        body.append("<div class='summary-card'>\n");
        body.append("<div class='summary-header'><h3>Your Inquiry Summary</h3></div>\n");
        body.append(summaryRow("&#127758;", "Destination", escapeHtml(request.getPreferredDestination())));
        body.append(summaryRow("&#128101;", "Travelers", request.getAdults() + " Adults" + (request.getKids() > 0 ? ", " + request.getKids() + " Kids" : "")));
        body.append(summaryRow("&#128197;", "Travel Dates", escapeHtml(request.getArrivalDate()) + " to " + escapeHtml(request.getDepartureDate())));
        body.append("</div>\n");

        // Steps
        body.append("<div class='steps-label'>What happens next</div>\n");
        body.append(buildStep("1", "Inquiry Review",
                "Our travel specialists will carefully review your travel preferences and requirements."));
        body.append("<div class='step-connector'></div>\n");
        body.append(buildStep("2", "Personalised Proposal",
                "We'll prepare a tailored itinerary and quote based on your selected destination and travel dates."));
        body.append("<div class='step-connector'></div>\n");
        body.append(buildStep("3", "Response Within 24 Hours",
                "You'll hear from us with a detailed proposal and next steps for your Sri Lanka adventure."));

        body.append("<div class='divider'></div>\n");

        // Contact
        body.append("<div class='contact-label'>Need assistance sooner?</div>\n");
        body.append("<div class='contact-row'>\n");
        body.append("<div class='contact-icon'>&#128222;</div>\n");
        body.append("<span class='contact-text'>").append(escapeHtml(COMPANY_PHONE))
                .append(" <span class='contact-sub'>24/7 Support</span></span>\n");
        body.append("</div>\n");
        body.append("<div class='contact-row'>\n");
        body.append("<div class='contact-icon'>&#9993;</div>\n");
        body.append("<span class='contact-text'>").append(escapeHtml(COMPANY_EMAIL)).append("</span>\n");
        body.append("</div>\n");

        // CTA
        body.append("<div class='cta'>\n");
        body.append("<a href='https://felicitatrips.com/sri-lankan-tours' class='btn'>Explore Our Tours</a>\n");
        body.append("<div class='btn-sub'>felicitatrips.com</div>\n");
        body.append("</div>\n");

        body.append("</div>\n");

        // Footer
        body.append("<div class='footer'>\n");
        body.append("<div><div class='footer-name'>").append(escapeHtml(COMPANY_NAME)).append(" Team</div>\n");
        body.append("<div class='footer-role'>Premium Sri Lanka Travel</div></div>\n");
        body.append("<div class='footer-tag'>Creating unforgettable Sri Lanka experiences</div>\n");
        body.append("</div>\n");

        body.append("</div></div>\n</body>\n</html>");

        return body.toString();
    }


    @Override
    public String buildCustomerInquirySubject(CreateInquiryRequest request) {
        return "We've received your inquiry to " + request.getPreferredDestination() + " - Felicita Trips";
    }


    private void appendSelectionsHtml(StringBuilder body, Map<String, List<String>> selections,
                                      String key, String label) {
        if (selections.containsKey(key) && selections.get(key) != null && !selections.get(key).isEmpty()) {
            body.append("<div style='margin-top: 15px;'>\n");
            body.append("<strong>").append(label).append(":</strong>\n");
            body.append("<ul>\n");
            for (String item : selections.get(key)) {
                body.append("<li>").append(escapeHtml(item)).append("</li>\n");
            }
            body.append("</ul>\n");
            body.append("</div>\n");
        }
    }

    private String getFlowTypeDisplayName(String flowType) {
        if ("tours".equals(flowType)) {
            return "Tour Package";
        } else if ("activities".equals(flowType)) {
            return "Activities & Experiences";
        } else if ("destinations".equals(flowType)) {
            return "Destinations";
        }
        return "General";
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
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
