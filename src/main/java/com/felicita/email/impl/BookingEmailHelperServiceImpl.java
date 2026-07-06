package com.felicita.email.impl;

import com.felicita.email.BookingEmailHelperService;
import com.felicita.model.other.*;
import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.model.request.bookings.status.InsertBookingsStatusesRequest;
import com.felicita.model.response.bookings.BookingAllDetailsResponse;
import com.felicita.model.response.bookings.BookingsBasicDetails;
import com.felicita.model.response.bookings.status.BookingStatusDetailsResponse;
import com.felicita.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingEmailHelperServiceImpl implements BookingEmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingEmailHelperServiceImpl.class);

    @Override
    public String buildBookingCreateSuccessfullSubject(InsertBookingRequest insertBookingRequest, Long bookingId, User loggedUser) {
        return String.format("[Felicita Trips] Booking Created — #%d", bookingId);
    }

    @Override
    public String buildBookingUpdateSuccessfullSubject(User loggedUser, Long bookingId) {
        return String.format("[Felicita Trips] Booking Updated — #%d", bookingId);
    }

    @Override
    public String buildBookingUpdateSuccessfullBody(User loggedUser, Long bookingId, BookingComparisonResult comparisonResult) {
        String basicFieldsHtml = buildBookingBasicFieldsHtml(comparisonResult.getFieldChanges());
        String statusChangeHtml = buildBookingStatusChangeHtml(comparisonResult);
        String participantsHtml = buildBookingParticipantsUpdateHtml(comparisonResult);
        String accommodationsHtml = buildBookingAccommodationsUpdateHtml(comparisonResult);
        String transportationsHtml = buildBookingTransportationsUpdateHtml(comparisonResult);
        String activitiesHtml = buildBookingActivitiesUpdateHtml(comparisonResult);
        String insuranceHtml = buildBookingInsuranceUpdateHtml(comparisonResult);
        String invoiceHtml = buildBookingInvoiceUpdateHtml(comparisonResult);
        boolean hasAnyUpdates = comparisonResult != null && comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Updated</title>" +
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
                ".price{font-weight:bold;color:#1a6b40;}" +
                ".add-tag{color:#1a6b40;font-weight:bold;}" +
                ".remove-tag{color:#a33;font-weight:bold;}" +
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
                "<h1>Booking Updated</h1>" +
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

                "<p class='section-title'>Booking Information</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Booking ID</span><span class='info-value'><span class='reference-id'>#" + bookingId + "</span></span></div>" +
                "</div>" +

                (comparisonResult != null && !comparisonResult.getWarnings().isEmpty() ?
                        "<div class='warnings-box'>" +
                                "<span class='warning-icon'>⚠️</span>" +
                                "<p><strong>Warnings:</strong> " + String.join("; ", comparisonResult.getWarnings()) + "</p>" +
                                "</div>" : "") +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this booking.</div>") +

                (basicFieldsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Updated Fields</p>" +
                                "<table class='updated-fields-table'>" +
                                "<thead><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr></thead>" +
                                "<tbody>" + basicFieldsHtml + "</tbody>" +
                                "</table><br/>") +

                (statusChangeHtml.isEmpty() ? "" : statusChangeHtml) +

                (comparisonResult != null && (!comparisonResult.isFinancialCalculationValid() || !comparisonResult.isDateRangeValid()) ?
                        "<p class='section-title'>Validation Information</p>" +
                                "<div class='validation-box'>" +
                                "<div class='validation-title'>Booking Validation Results</div>" +

                                (comparisonResult.getDaysBetweenTravelDates() != null ?
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Days Between Travel Dates:</span>" +
                                                "<span class='validation-value'>" + comparisonResult.getDaysBetweenTravelDates() + " days</span>" +
                                                "</div>" : "") +

                                (comparisonResult.getCalculatedFinalAmount() != null ?
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Calculated Final Amount:</span>" +
                                                "<span class='validation-value'>$" + comparisonResult.getCalculatedFinalAmount() + "</span>" +
                                                "</div>" : "") +

                                (comparisonResult.getDifferenceAmount() != null ?
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Difference Amount:</span>" +
                                                "<span class='validation-value'>$" + comparisonResult.getDifferenceAmount() + "</span>" +
                                                "</div>" : "") +

                                (!comparisonResult.isDateRangeValid() ?
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Date Range Status:</span>" +
                                                "<span class='validation-value validation-error'>⚠️ Invalid - End date must be after start date</span>" +
                                                "</div>" :
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Date Range Status:</span>" +
                                                "<span class='validation-value validation-success'>✓ Valid</span>" +
                                                "</div>") +

                                (!comparisonResult.isFinancialCalculationValid() ?
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Financial Calculation:</span>" +
                                                "<span class='validation-value validation-warning'>⚠️ Warning - Financial totals do not match</span>" +
                                                "</div>" :
                                        "<div class='validation-item'>" +
                                                "<span class='validation-label'>Financial Calculation:</span>" +
                                                "<span class='validation-value validation-success'>✓ Valid</span>" +
                                                "</div>") +

                                "</div>" : "") +

                (participantsHtml.isEmpty() ? "" : participantsHtml) +
                (accommodationsHtml.isEmpty() ? "" : accommodationsHtml) +
                (transportationsHtml.isEmpty() ? "" : transportationsHtml) +
                (activitiesHtml.isEmpty() ? "" : activitiesHtml) +
                (insuranceHtml.isEmpty() ? "" : insuranceHtml) +
                (invoiceHtml.isEmpty() ? "" : invoiceHtml) +

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
    public String buildBookingStatusUpdateSuccessfullSubject(User loggedUser, Long bookingId, BookingsBasicDetails bookingsBasicDetails) {
        return String.format("[Felicita Trips] Booking Status Updated — #%d", bookingId);
    }

    @Override
    public String buildBookingStatusUpdateSuccessfullBody(User loggedUser, Long bookingId, BookingStatusComparisonResult comparisonResult, BookingsBasicDetails bookingsBasicDetails) {
        String statusChangeHtml = buildBookingStatusChangeDetailsHtml(comparisonResult);
        String bookingDetailsHtml = buildBookingStatusBasicDetailsHtml(bookingsBasicDetails);
        boolean hasAnyUpdates = comparisonResult != null && comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Status Updated</title>" +
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
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-new-inquiry{background:#e3f2fd;color:#0d47a1;}" +
                ".status-pending{background:#fff3e0;color:#e65100;}" +
                ".status-contacted{background:#e8f5e9;color:#1b5e20;}" +
                ".status-quotation-sent{background:#f3e5f5;color:#4a148c;}" +
                ".status-negotiation{background:#fff8e1;color:#f57f17;}" +
                ".status-confirmed{background:#e0f7fa;color:#00695c;}" +
                ".status-payment-pending{background:#fff9c4;color:#f57f17;}" +
                ".status-booked{background:#c8e6c9;color:#1b5e20;}" +
                ".status-completed{background:#b2dfdb;color:#004d40;}" +
                ".status-cancelled{background:#ffcdd2;color:#b71c1c;}" +
                ".status-rejected{background:#fce4ec;color:#880e4f;}" +
                ".status-expired{background:#e0e0e0;color:#424242;}" +
                ".status-unknown{background:#f5f5f5;color:#616161;}" +
                ".status-change-box{background:#f5fbfb;border:2px solid #0e7c7b;border-radius:8px;padding:20px 24px;margin-bottom:24px;text-align:center;}" +
                ".status-change-box .arrow-icon{font-size:32px;color:#0e7c7b;margin:0 16px;}" +
                ".status-old{font-size:18px;color:#a33;}" +
                ".status-new{font-size:18px;color:#1a6b40;font-weight:bold;}" +
                ".status-label{font-size:12px;color:#6b8e8e;text-transform:uppercase;letter-spacing:1px;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".reference-id{font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;}" +
                ".warnings-box{background:#fff8e7;border-left:3px solid #ffd700;padding:12px 16px;margin-bottom:24px;border-radius:6px;}" +
                ".warnings-box p{margin:0;font-size:13px;color:#6b4c00;}" +
                ".warning-icon{font-size:16px;margin-right:8px;}" +
                ".price{font-weight:bold;color:#1a6b40;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Booking Status Updated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#128736; Status Update</span>" +
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

                "<p class='section-title'>Booking Information</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Booking ID</span><span class='info-value'><span class='reference-id'>#" + bookingId + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Booking Reference</span><span class='info-value'>" + (bookingsBasicDetails.getBookingReference() != null ? escapeHtml(bookingsBasicDetails.getBookingReference()) : "—") + "</span></div>" +
                "</div>" +

                (comparisonResult != null && !comparisonResult.getWarnings().isEmpty() ?
                        "<div class='warnings-box'>" +
                                "<span class='warning-icon'>⚠️</span>" +
                                "<p><strong>Warnings:</strong> " + String.join("; ", comparisonResult.getWarnings()) + "</p>" +
                                "</div>" : "") +

                (hasAnyUpdates ? statusChangeHtml : "<div class='info-card'><div class='info-row'><span class='info-label'>Status</span><span class='info-value'>No status change detected</span></div></div>") +

                (bookingDetailsHtml.isEmpty() ? "" : bookingDetailsHtml) +

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
    public String buildBookingTerminateSuccessfullSubject(User loggedUser, BookingAllDetailsResponse bookingDetails) {
        String bookingRef = bookingDetails.getBookingInformation() != null &&
                bookingDetails.getBookingInformation().getBookingReference() != null ?
                bookingDetails.getBookingInformation().getBookingReference() :
                String.valueOf(bookingDetails.getBookingInformation().getBookingId());
        return String.format("[Felicita Trips] Booking Terminated — %s", bookingRef);
    }

    @Override
    public String buildBookingTerminateSuccessfullBody(User loggedUser, BookingAllDetailsResponse bookingDetails) {
        String bookingInfoHtml = buildTerminateBookingInfoHtml(bookingDetails);
        String customerInfoHtml = buildTerminateCustomerInfoHtml(bookingDetails);
        String tourPackageInfoHtml = buildTerminateTourPackageInfoHtml(bookingDetails);
        String participantsHtml = buildTerminateBookingParticipantsHtml(bookingDetails.getParticipants());
        String accommodationsHtml = buildTerminateBookingAccommodationsHtml(bookingDetails.getAccommodations());
        String transportationsHtml = buildTerminateBookingTransportationsHtml(bookingDetails.getTransportations());
        String activitiesHtml = buildTerminateBookingActivitiesHtml(bookingDetails.getActivities());
        String invoiceHtml = buildTerminateBookingInvoiceHtml(bookingDetails.getBookingInvoice());
        String insuranceHtml = buildTerminateBookingInsuranceHtml(bookingDetails.getBookingInsurance());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Terminated</title>" +
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
                ".booking-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
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
                ".price{font-weight:bold;color:#1a6b40;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Booking Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This booking has been terminated</strong> and is no longer active in the system.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + escapeHtml(loggedUser.getEmail()) + " &nbsp;|&nbsp; " + escapeHtml(loggedUser.getUsername()) + "</div>" +
                "</div>" +
                "</div>" +

                bookingInfoHtml +
                customerInfoHtml +
                tourPackageInfoHtml +

                (participantsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Participants (" + (bookingDetails.getParticipants() != null ? bookingDetails.getParticipants().size() : 0) + ")</p>" +
                                participantsHtml) +

                (accommodationsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Accommodations (" + (bookingDetails.getAccommodations() != null ? bookingDetails.getAccommodations().size() : 0) + ")</p>" +
                                accommodationsHtml) +

                (transportationsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Transportations (" + (bookingDetails.getTransportations() != null ? bookingDetails.getTransportations().size() : 0) + ")</p>" +
                                transportationsHtml) +

                (activitiesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Activities (" + (bookingDetails.getActivities() != null ? bookingDetails.getActivities().size() : 0) + ")</p>" +
                                activitiesHtml) +

                (insuranceHtml.isEmpty() ? "" : insuranceHtml) +
                (invoiceHtml.isEmpty() ? "" : invoiceHtml) +

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
    public String buildBookingsStatusesCreateSuccessfullSubject(InsertBookingsStatusesRequest insertBookingsStatusesRequest, Long bookingStatusId, User loggedUser) {
        return String.format("[Felicita Trips] Booking Status Created — %s",
                insertBookingsStatusesRequest.getStatusName() != null ?
                        insertBookingsStatusesRequest.getStatusName() : "Unknown Status");
    }

    @Override
    public String buildBookingsStatusesUpdateSuccessfullSubject(User loggedUser, Long statusId) {
        return String.format("[Felicita Trips] Booking Status Updated — #%d", statusId);
    }

    @Override
    public String buildBookingsStatusesUpdateSuccessfullBody(User loggedUser, Long statusId, BookingsStatusesComparisonResult comparisonResult) {
        String basicFieldsHtml = buildBookingsStatusesBasicFieldsHtml(comparisonResult.getFieldChanges());
        boolean hasAnyUpdates = comparisonResult != null && comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Status Updated</title>" +
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
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Booking Status Updated</h1>" +
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

                "<p class='section-title'>Status Information</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Status ID</span><span class='info-value'><span class='reference-id'>#" + statusId + "</span></span></div>" +
                "</div>" +

                (comparisonResult != null && !comparisonResult.getWarnings().isEmpty() ?
                        "<div class='warnings-box'>" +
                                "<span class='warning-icon'>⚠️</span>" +
                                "<p><strong>Warnings:</strong> " + String.join("; ", comparisonResult.getWarnings()) + "</p>" +
                                "</div>" : "") +

                (hasAnyUpdates ? "" : "<div class='no-updates'>No changes were made to this booking status.</div>") +

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
                                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(comparisonResult.getOldStatus()) + "</span></div>" +
                                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'>" + buildStatusPill(comparisonResult.getNewStatus()) + "</span></div>" +
                                "</div>" : "") +

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
    public String buildBookingsStatusesTerminateSuccessfullSubject(User loggedUser, BookingStatusDetailsResponse bookingStatusResponse) {
        return String.format("[Felicita Trips] Booking Status Terminated — %s",
                bookingStatusResponse.getStatusName() != null ?
                        bookingStatusResponse.getStatusName() : "Unknown Status");
    }

    @Override
    public String buildBookingsStatusesTerminateSuccessfullBody(User loggedUser, BookingStatusDetailsResponse bookingStatusResponse) {
        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Status Terminated</title>" +
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
                ".status-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".status-pending{background:#fff8e7;color:#b8860b;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#8B0000,#cd5c5c);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".reference-id{font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;}" +
                ".statistics-box{background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;padding:16px 20px;margin-top:12px;}" +
                ".statistics-row{display:flex;justify-content:space-around;flex-wrap:wrap;gap:12px;}" +
                ".stat-item{text-align:center;padding:8px 16px;}" +
                ".stat-number{font-size:20px;font-weight:bold;color:#0e7c7b;}" +
                ".stat-label{font-size:11px;color:#6b8e8e;text-transform:uppercase;letter-spacing:1px;}" +
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
                "<h1>Booking Status Terminated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#10060; Termination Completed</span>" +
                "</div>" +

                "<div class='content'>" +

                "<div class='terminate-warning'>" +
                "<span class='warning-icon'>⚠️</span>" +
                "<p><strong>This booking status has been terminated</strong> and is no longer available for use in the system.</p>" +
                "</div>" +

                "<p class='section-title'>Terminated By</p>" +
                "<div class='actor-row'>" +
                "<div class='actor-avatar'>" + getInitials(loggedUser) + "</div>" +
                "<div class='actor-info'>" +
                "<div class='actor-name'>" + getFullName(loggedUser) + "</div>" +
                "<div class='actor-meta'>" + escapeHtml(loggedUser.getEmail()) + " &nbsp;|&nbsp; " + escapeHtml(loggedUser.getUsername()) + "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Status Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Status ID</span><span class='info-value'><span class='reference-id'>#" + bookingStatusResponse.getStatusId() + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Status Name</span><span class='info-value'><span class='status-icon'>🏷️</span>" + escapeHtml(bookingStatusResponse.getStatusName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (bookingStatusResponse.getDescription() != null ? escapeHtml(bookingStatusResponse.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + buildStatusPill(bookingStatusResponse.getStatus()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'><span class='status-pill-terminated'>TERMINATED</span></span></div>" +
                "</div>" +

                "<p class='section-title'>Usage Statistics</p>" +
                "<div class='info-card'>" +
                "<div class='statistics-box'>" +
                "<div class='statistics-row'>" +
                "<div class='stat-item'>" +
                "<div class='stat-number'>" + (bookingStatusResponse.getTotalBookingsUsingThisStatus() != null ? bookingStatusResponse.getTotalBookingsUsingThisStatus() : "0") + "</div>" +
                "<div class='stat-label'>Total Bookings</div>" +
                "</div>" +
                "<div class='stat-item'>" +
                "<div class='stat-number'>" + (bookingStatusResponse.getActiveBookingsCount() != null ? bookingStatusResponse.getActiveBookingsCount() : "0") + "</div>" +
                "<div class='stat-label'>Active Bookings</div>" +
                "</div>" +
                "<div class='stat-item'>" +
                "<div class='stat-number'>" + (bookingStatusResponse.getCompletedBookingsCount() != null ? bookingStatusResponse.getCompletedBookingsCount() : "0") + "</div>" +
                "<div class='stat-label'>Completed</div>" +
                "</div>" +
                "<div class='stat-item'>" +
                "<div class='stat-number'>" + (bookingStatusResponse.getCancelledBookingsCount() != null ? bookingStatusResponse.getCancelledBookingsCount() : "0") + "</div>" +
                "<div class='stat-label'>Cancelled</div>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>" +

                "<p class='section-title'>Audit Information</p>" +
                "<div class='info-metadata'>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Created By</span><span class='info-metadata-value'>" +
                (bookingStatusResponse.getCreatedBy() != null ? "User #" + bookingStatusResponse.getCreatedBy() : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Created At</span><span class='info-metadata-value'>" +
                (bookingStatusResponse.getCreatedAt() != null ? formatLocalDateTime(bookingStatusResponse.getCreatedAt()) : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Last Updated By</span><span class='info-metadata-value'>" +
                (bookingStatusResponse.getUpdatedBy() != null ? "User #" + bookingStatusResponse.getUpdatedBy() : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Last Updated At</span><span class='info-metadata-value'>" +
                (bookingStatusResponse.getUpdatedAt() != null ? formatLocalDateTime(bookingStatusResponse.getUpdatedAt()) : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Terminated By</span><span class='info-metadata-value'>" +
                (bookingStatusResponse.getTerminatedBy() != null ? "User #" + bookingStatusResponse.getTerminatedBy() : "—") +
                "</span></div>" +
                "<div class='info-metadata-row'><span class='info-metadata-label'>Terminated At</span><span class='info-metadata-value'>" +
                (bookingStatusResponse.getTerminatedAt() != null ? formatLocalDateTime(bookingStatusResponse.getTerminatedAt()) : "—") +
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
    public String buildAssignBookingSuccessfullSubject(User loggedUser, Long bookingId) {
        return String.format("[Felicita Trips] Booking Assigned — #%d", bookingId);
    }

    @Override
    public String buildAssignBookingSuccessfullBody(User loggedUser, Long bookingId, AssignBookingComparisonResult comparisonResult) {
        String assignmentChangesHtml = buildAssignmentChangesHtml(comparisonResult);
        boolean hasAnyUpdates = comparisonResult != null && comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Assignment Updated</title>" +
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
                ".assignment-box{background:#f5fbfb;border:2px solid #0e7c7b;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".assignment-box .arrow-icon{font-size:28px;color:#0e7c7b;margin:0 12px;}" +
                ".assignment-old{font-size:16px;color:#a33;}" +
                ".assignment-new{font-size:16px;color:#1a6b40;font-weight:bold;}" +
                ".assignment-label{font-size:11px;color:#6b8e8e;text-transform:uppercase;letter-spacing:1px;margin-bottom:4px;}" +
                ".assignment-message-box{background:#fff8e7;border-left:3px solid #ffd700;padding:12px 16px;margin-top:12px;border-radius:6px;}" +
                ".assignment-message-box p{margin:0;font-size:13px;color:#6b4c00;font-style:italic;}" +
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
                ".employee-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:13px;color:#0e7c7b;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Booking Assignment Updated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#128736; Assignment Updated</span>" +
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

                "<p class='section-title'>Booking Information</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Booking ID</span><span class='info-value'><span class='reference-id'>#" + bookingId + "</span></span></div>" +
                "</div>" +

                (comparisonResult != null && !comparisonResult.getWarnings().isEmpty() ?
                        "<div class='warnings-box'>" +
                                "<span class='warning-icon'>⚠️</span>" +
                                "<p><strong>Warnings:</strong> " + String.join("; ", comparisonResult.getWarnings()) + "</p>" +
                                "</div>" : "") +

                (hasAnyUpdates ? assignmentChangesHtml : "<div class='no-updates'>No assignment changes were made to this booking.</div>") +

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
    public String buildAssignBookingUpdateSuccessfullSubject(User loggedUser, Long bookingId) {
        return String.format("[Felicita Trips] Booking Assignment Updated — #%d", bookingId);
    }

    @Override
    public String buildAssignBookingUpdateSuccessfullBody(User loggedUser, Long bookingId, AssignBookingUpdateComparisonResult comparisonResult) {
        String assignmentChangesHtml = buildAssignmentUpdateChangesHtml(comparisonResult);
        boolean hasAnyUpdates = comparisonResult != null && comparisonResult.isHasChanges();

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Assignment Updated</title>" +
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
                ".assignment-box{background:#f5fbfb;border:2px solid #0e7c7b;border-radius:8px;padding:20px 24px;margin-bottom:24px;}" +
                ".assignment-box .arrow-icon{font-size:28px;color:#0e7c7b;margin:0 12px;}" +
                ".assignment-old{font-size:16px;color:#a33;}" +
                ".assignment-new{font-size:16px;color:#1a6b40;font-weight:bold;}" +
                ".assignment-label{font-size:11px;color:#6b8e8e;text-transform:uppercase;letter-spacing:1px;margin-bottom:4px;}" +
                ".assignment-message-box{background:#fff8e7;border-left:3px solid #ffd700;padding:12px 16px;margin-top:12px;border-radius:6px;}" +
                ".assignment-message-box p{margin:0;font-size:13px;color:#6b4c00;font-style:italic;}" +
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
                ".employee-tag{display:inline-block;padding:4px 12px;background:#e8f5f5;border:1px solid #c8e8e8;border-radius:16px;font-size:13px;color:#0e7c7b;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Booking Assignment Updated</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#9998; Assignment Update</span>" +
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

                "<p class='section-title'>Booking Information</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Booking ID</span><span class='info-value'><span class='reference-id'>#" + bookingId + "</span></span></div>" +
                "</div>" +

                (comparisonResult != null && !comparisonResult.getWarnings().isEmpty() ?
                        "<div class='warnings-box'>" +
                                "<span class='warning-icon'>⚠️</span>" +
                                "<p><strong>Warnings:</strong> " + String.join("; ", comparisonResult.getWarnings()) + "</p>" +
                                "</div>" : "") +

                (hasAnyUpdates ? assignmentChangesHtml : "<div class='no-updates'>No assignment changes were made to this booking.</div>") +

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
    public String buildBookingsStatusesCreateSuccessfullBody(InsertBookingsStatusesRequest insertBookingsStatusesRequest, Long bookingStatusId, User loggedUser) {
        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Status Created</title>" +
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
                ".status-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
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
                "<h1>Booking Status Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#127912; New Status Added</span>" +
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

                "<p class='section-title'>Status Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Status ID</span><span class='info-value'><span class='reference-id'>#" + bookingStatusId + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Status Name</span><span class='info-value'><span class='status-icon'>🏷️</span>" + escapeHtml(insertBookingsStatusesRequest.getStatusName()) + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + (insertBookingsStatusesRequest.getDescription() != null ? escapeHtml(insertBookingsStatusesRequest.getDescription()) : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + buildStatusPill(insertBookingsStatusesRequest.getStatus()) + "</span></div>" +
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

    private String buildAssignmentUpdateChangesHtml(AssignBookingUpdateComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        if (comparisonResult.getOldAssignedEmployeeId() == null && comparisonResult.getNewAssignedEmployeeId() == null) {
            return "<div class='no-updates'>No assignment changes detected.</div>";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Assignment Changes</p>")
                .append("<div class='assignment-box'>");

        // Employee assignment change
        if (comparisonResult.getOldAssignedEmployeeId() != null || comparisonResult.getNewAssignedEmployeeId() != null) {
            sb.append("<div style='display:flex;align-items:center;justify-content:center;flex-wrap:wrap;'>");

            // Old employee
            sb.append("<div style='text-align:center;'>")
                    .append("<div class='assignment-label'>Previous Employee</div>")
                    .append("<div class='assignment-old'>");
            if (comparisonResult.getOldAssignedEmployeeId() != null) {
                sb.append("<span class='employee-tag'>")
                        .append(comparisonResult.getOldAssignedEmployeeName() != null ?
                                escapeHtml(comparisonResult.getOldAssignedEmployeeName()) :
                                "Employee #" + comparisonResult.getOldAssignedEmployeeId())
                        .append("</span>");
            } else {
                sb.append("<span style='color:#6b8e8e;font-style:italic;'>Not Assigned</span>");
            }
            sb.append("</div></div>");

            // Arrow
            sb.append("<span class='arrow-icon'>➜</span>");

            // New employee
            sb.append("<div style='text-align:center;'>")
                    .append("<div class='assignment-label'>New Employee</div>")
                    .append("<div class='assignment-new'>");
            if (comparisonResult.getNewAssignedEmployeeId() != null) {
                sb.append("<span class='employee-tag' style='background:#d4f4e8;border-color:#90d0c0;'>")
                        .append(comparisonResult.getNewAssignedEmployeeName() != null ?
                                escapeHtml(comparisonResult.getNewAssignedEmployeeName()) :
                                "Employee #" + comparisonResult.getNewAssignedEmployeeId())
                        .append("</span>");
            } else {
                sb.append("<span style='color:#6b8e8e;font-style:italic;'>Not Assigned</span>");
            }
            sb.append("</div></div>");

            sb.append("</div>");
        }

        // Assignment message change
        if (comparisonResult.getOldAssignMessage() != null || comparisonResult.getNewAssignMessage() != null) {
            if ((comparisonResult.getOldAssignMessage() != null && comparisonResult.getNewAssignMessage() != null &&
                    !comparisonResult.getOldAssignMessage().equals(comparisonResult.getNewAssignMessage())) ||
                    (comparisonResult.getOldAssignMessage() == null && comparisonResult.getNewAssignMessage() != null) ||
                    (comparisonResult.getOldAssignMessage() != null && comparisonResult.getNewAssignMessage() == null)) {

                sb.append("<div style='margin-top:16px;padding-top:16px;border-top:1px solid #e0f0f0;'>")
                        .append("<div class='assignment-label'>Assignment Message</div>")
                        .append("<div style='display:flex;flex-direction:column;gap:8px;'>");

                if (comparisonResult.getOldAssignMessage() != null) {
                    sb.append("<div>")
                            .append("<span style='font-size:12px;color:#6b8e8e;'>Old: </span>")
                            .append("<span class='old-value'>").append(escapeHtml(comparisonResult.getOldAssignMessage())).append("</span>")
                            .append("</div>");
                }
                if (comparisonResult.getNewAssignMessage() != null) {
                    sb.append("<div>")
                            .append("<span style='font-size:12px;color:#6b8e8e;'>New: </span>")
                            .append("<span class='new-value'>").append(escapeHtml(comparisonResult.getNewAssignMessage())).append("</span>")
                            .append("</div>");
                }
                if (comparisonResult.getOldAssignMessage() == null && comparisonResult.getNewAssignMessage() != null) {
                    sb.append("<div class='assignment-message-box' style='margin-top:4px;'>")
                            .append("<p>📝 ").append(escapeHtml(comparisonResult.getNewAssignMessage())).append("</p>")
                            .append("</div>");
                }

                sb.append("</div></div>");
            }
        }

        // Field changes (if any other fields were updated)
        if (comparisonResult.getFieldChanges() != null && !comparisonResult.getFieldChanges().isEmpty()) {
            sb.append("<div style='margin-top:16px;padding-top:16px;border-top:1px solid #e0f0f0;'>")
                    .append("<div class='assignment-label'>Other Changes</div>")
                    .append("<table class='updated-fields-table'>")
                    .append("<thead><tr><th>Field</th><th>Old Value</th><th>New Value</th></tr></thead><tbody>");

            for (AssignBookingUpdateComparisonResult.FieldChange field : comparisonResult.getFieldChanges()) {
                String displayOldValue = formatValueForDisplay(field.getOldValue());
                String displayNewValue = formatValueForDisplay(field.getNewValue());
                sb.append("<tr>")
                        .append("<td><strong>").append(formatFieldName(field.getFieldName())).append("</strong></td>")
                        .append("<td><span class='old-value'>").append(displayOldValue).append("</span></td>")
                        .append("<td><span class='new-value'>").append(displayNewValue).append("</span></td>")
                        .append("</tr>");
            }

            sb.append("</tbody></table></div>");
        }

        sb.append("</div>");
        return sb.toString();
    }

    private String buildAssignmentChangesHtml(AssignBookingComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        if (comparisonResult.getOldAssignedEmployeeId() == null && comparisonResult.getNewAssignedEmployeeId() == null) {
            return "<div class='no-updates'>No assignment changes detected.</div>";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Assignment Changes</p>")
                .append("<div class='assignment-box'>");

        // Employee assignment change
        if (comparisonResult.getOldAssignedEmployeeId() != null || comparisonResult.getNewAssignedEmployeeId() != null) {
            sb.append("<div style='display:flex;align-items:center;justify-content:center;flex-wrap:wrap;'>");

            // Old employee
            sb.append("<div style='text-align:center;'>")
                    .append("<div class='assignment-label'>Previous Employee</div>")
                    .append("<div class='assignment-old'>");
            if (comparisonResult.getOldAssignedEmployeeId() != null) {
                sb.append("<span class='employee-tag'>")
                        .append(comparisonResult.getOldAssignedEmployeeName() != null ?
                                escapeHtml(comparisonResult.getOldAssignedEmployeeName()) :
                                "Employee #" + comparisonResult.getOldAssignedEmployeeId())
                        .append("</span>");
            } else {
                sb.append("<span style='color:#6b8e8e;font-style:italic;'>Not Assigned</span>");
            }
            sb.append("</div></div>");

            // Arrow
            sb.append("<span class='arrow-icon'>➜</span>");

            // New employee
            sb.append("<div style='text-align:center;'>")
                    .append("<div class='assignment-label'>New Employee</div>")
                    .append("<div class='assignment-new'>");
            if (comparisonResult.getNewAssignedEmployeeId() != null) {
                sb.append("<span class='employee-tag' style='background:#d4f4e8;border-color:#90d0c0;'>")
                        .append(comparisonResult.getNewAssignedEmployeeName() != null ?
                                escapeHtml(comparisonResult.getNewAssignedEmployeeName()) :
                                "Employee #" + comparisonResult.getNewAssignedEmployeeId())
                        .append("</span>");
            } else {
                sb.append("<span style='color:#6b8e8e;font-style:italic;'>Not Assigned</span>");
            }
            sb.append("</div></div>");

            sb.append("</div>");
        }

        // Assignment message change
        if (comparisonResult.getOldAssignMessage() != null || comparisonResult.getNewAssignMessage() != null) {
            if ((comparisonResult.getOldAssignMessage() != null && comparisonResult.getNewAssignMessage() != null &&
                    !comparisonResult.getOldAssignMessage().equals(comparisonResult.getNewAssignMessage())) ||
                    (comparisonResult.getOldAssignMessage() == null && comparisonResult.getNewAssignMessage() != null) ||
                    (comparisonResult.getOldAssignMessage() != null && comparisonResult.getNewAssignMessage() == null)) {

                sb.append("<div style='margin-top:16px;padding-top:16px;border-top:1px solid #e0f0f0;'>")
                        .append("<div class='assignment-label'>Assignment Message</div>")
                        .append("<div style='display:flex;flex-direction:column;gap:8px;'>");

                if (comparisonResult.getOldAssignMessage() != null) {
                    sb.append("<div>")
                            .append("<span style='font-size:12px;color:#6b8e8e;'>Old: </span>")
                            .append("<span class='old-value'>").append(escapeHtml(comparisonResult.getOldAssignMessage())).append("</span>")
                            .append("</div>");
                }
                if (comparisonResult.getNewAssignMessage() != null) {
                    sb.append("<div>")
                            .append("<span style='font-size:12px;color:#6b8e8e;'>New: </span>")
                            .append("<span class='new-value'>").append(escapeHtml(comparisonResult.getNewAssignMessage())).append("</span>")
                            .append("</div>");
                }
                if (comparisonResult.getOldAssignMessage() == null && comparisonResult.getNewAssignMessage() != null) {
                    sb.append("<div class='assignment-message-box' style='margin-top:4px;'>")
                            .append("<p>📝 ").append(escapeHtml(comparisonResult.getNewAssignMessage())).append("</p>")
                            .append("</div>");
                }

                sb.append("</div></div>");
            }
        }

        sb.append("</div>");
        return sb.toString();
    }

    private String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "—";
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    private String buildBookingsStatusesBasicFieldsHtml(List<BookingsStatusesComparisonResult.FieldChange> fieldChanges) {
        if (fieldChanges == null || fieldChanges.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (BookingsStatusesComparisonResult.FieldChange field : fieldChanges) {
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

    private String buildStatusPill(String status) {
        if (status == null) {
            return "<span class='status-pill status-inactive'>INACTIVE</span>";
        }
        if ("ACTIVE".equalsIgnoreCase(status) || "1".equals(status)) {
            return "<span class='status-pill status-active'>" + status + "</span>";
        } else if ("PENDING".equalsIgnoreCase(status) || "2".equals(status)) {
            return "<span class='status-pill status-pending'>" + status + "</span>";
        } else {
            return "<span class='status-pill status-inactive'>" + status + "</span>";
        }
    }

    private String buildTerminateBookingInfoHtml(BookingAllDetailsResponse bookingDetails) {
        if (bookingDetails == null || bookingDetails.getBookingInformation() == null) return "";

        BookingAllDetailsResponse.BookingInformation info = bookingDetails.getBookingInformation();
        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Booking Details</p>")
                .append("<div class='info-card'>")
                .append("<div class='info-row'><span class='info-label'>Booking ID</span><span class='info-value'><span class='reference-id'>#" + info.getBookingId() + "</span></span></div>")
                .append("<div class='info-row'><span class='info-label'>Booking Reference</span><span class='info-value'>" + (info.getBookingReference() != null ? escapeHtml(info.getBookingReference()) : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Booking Date</span><span class='info-value'>" + (info.getBookingDate() != null ? info.getBookingDate().toString() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Travel Period</span><span class='info-value'>" +
                        (info.getTravelStartDate() != null ? info.getTravelStartDate().toString() : "—") +
                        " → " +
                        (info.getTravelEndDate() != null ? info.getTravelEndDate().toString() : "—") +
                        "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Total Persons</span><span class='info-value'>" + (info.getTotalPersons() != null ? info.getTotalPersons() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Insurance Required</span><span class='info-value'>" + (Boolean.TRUE.equals(info.getInsuranceRequired()) ? "Yes" : "No") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Total Amount</span><span class='info-value'><span class='price'>$" + (info.getTotalAmount() != null ? info.getTotalAmount() : "—") + "</span></span></div>")
                .append("<div class='info-row'><span class='info-label'>Discount</span><span class='info-value'>$" + (info.getDiscountAmount() != null ? info.getDiscountAmount() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Tax</span><span class='info-value'>$" + (info.getTaxAmount() != null ? info.getTaxAmount() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Insurance Amount</span><span class='info-value'>$" + (info.getInsuranceAmount() != null ? info.getInsuranceAmount() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Final Amount</span><span class='info-value'><span class='price'>$" + (info.getFinalAmount() != null ? info.getFinalAmount() : "—") + "</span></span></div>");

        if (info.getSpecialRequirements() != null && !info.getSpecialRequirements().isEmpty()) {
            sb.append("<div class='info-row'><span class='info-label'>Special Requirements</span><span class='info-value'>" + escapeHtml(info.getSpecialRequirements()) + "</span></div>");
        }
        if (info.getDietaryRestrictions() != null && !info.getDietaryRestrictions().isEmpty()) {
            sb.append("<div class='info-row'><span class='info-label'>Dietary Restrictions</span><span class='info-value'>" + escapeHtml(info.getDietaryRestrictions()) + "</span></div>");
        }

        // Booking Status
        if (bookingDetails.getBookingStatusInformation() != null) {
            BookingAllDetailsResponse.BookingStatusInformation statusInfo = bookingDetails.getBookingStatusInformation();
            sb.append("<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>" + getBookingStatusPill(statusInfo.getBookingStatusId(), statusInfo.getBookingStatusName()) + "</span></div>");
        }
        sb.append("<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'><span class='status-pill-terminated'>TERMINATED</span></span></div>");

        sb.append("</div>");
        return sb.toString();
    }

    private String buildTerminateCustomerInfoHtml(BookingAllDetailsResponse bookingDetails) {
        if (bookingDetails == null || bookingDetails.getCustomerInformation() == null) return "";

        BookingAllDetailsResponse.CustomerInformation customer = bookingDetails.getCustomerInformation();
        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Customer Information</p>")
                .append("<div class='info-card'>")
                .append("<div class='info-row'><span class='info-label'>Customer Name</span><span class='info-value'>" + (customer.getFullName() != null ? escapeHtml(customer.getFullName()) : escapeHtml(customer.getFirstName() + " " + customer.getLastName())) + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Username</span><span class='info-value'>" + (customer.getUsername() != null ? escapeHtml(customer.getUsername()) : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Email</span><span class='info-value'>" + (customer.getEmail() != null ? escapeHtml(customer.getEmail()) : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Mobile</span><span class='info-value'>" + (customer.getMobileNumber() != null ? escapeHtml(customer.getMobileNumber()) : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Passport</span><span class='info-value'>" + (customer.getPassportNumber() != null ? escapeHtml(customer.getPassportNumber()) : "—") + "</span></div>")
                .append("</div>");
        return sb.toString();
    }

    private String buildTerminateTourPackageInfoHtml(BookingAllDetailsResponse bookingDetails) {
        StringBuilder sb = new StringBuilder();

        // Tour Information
        if (bookingDetails.getTourInformation() != null) {
            BookingAllDetailsResponse.TourInformation tour = bookingDetails.getTourInformation();
            sb.append("<p class='section-title'>Tour Information</p>")
                    .append("<div class='info-card'>")
                    .append("<div class='info-row'><span class='info-label'>Tour ID</span><span class='info-value'>#" + tour.getTourId() + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Tour Name</span><span class='info-value'>" + escapeHtml(tour.getTourName()) + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Duration</span><span class='info-value'>" + (tour.getDuration() != null ? tour.getDuration() + " days" : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Start Location</span><span class='info-value'>" + (tour.getStartLocation() != null ? escapeHtml(tour.getStartLocation()) : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>End Location</span><span class='info-value'>" + (tour.getEndLocation() != null ? escapeHtml(tour.getEndLocation()) : "—") + "</span></div>");
            if (tour.getTourDescription() != null) {
                sb.append("<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + escapeHtml(tour.getTourDescription()) + "</span></div>");
            }
            sb.append("</div>");
        }

        // Package Information
        if (bookingDetails.getPackageInformation() != null) {
            BookingAllDetailsResponse.PackageInformation pkg = bookingDetails.getPackageInformation();
            sb.append("<p class='section-title'>Package Information</p>")
                    .append("<div class='info-card'>")
                    .append("<div class='info-row'><span class='info-label'>Package ID</span><span class='info-value'>#" + pkg.getPackageId() + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Package Name</span><span class='info-value'>" + escapeHtml(pkg.getPackageName()) + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Total Price</span><span class='info-value'><span class='price'>$" + (pkg.getPackageTotalPrice() != null ? pkg.getPackageTotalPrice() : "—") + "</span></span></div>")
                    .append("<div class='info-row'><span class='info-label'>Price Per Person</span><span class='info-value'>$" + (pkg.getPricePerPerson() != null ? pkg.getPricePerPerson() : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Discount Percentage</span><span class='info-value'>" + (pkg.getDiscountPercentage() != null ? pkg.getDiscountPercentage() + "%" : "—") + "</span></div>");
            if (pkg.getPackageDescription() != null) {
                sb.append("<div class='info-row'><span class='info-label'>Description</span><span class='info-value'>" + escapeHtml(pkg.getPackageDescription()) + "</span></div>");
            }
            sb.append("</div>");
        }

        // Assignment Information
        if (bookingDetails.getAssignmentInformation() != null) {
            BookingAllDetailsResponse.AssignmentInformation assignment = bookingDetails.getAssignmentInformation();
            sb.append("<p class='section-title'>Assignment Information</p>")
                    .append("<div class='info-card'>")
                    .append("<div class='info-row'><span class='info-label'>Assigned To</span><span class='info-value'>" + (assignment.getEmployeeName() != null ? escapeHtml(assignment.getEmployeeName()) : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Department</span><span class='info-value'>" + (assignment.getDepartmentName() != null ? escapeHtml(assignment.getDepartmentName()) : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Designation</span><span class='info-value'>" + (assignment.getDesignationName() != null ? escapeHtml(assignment.getDesignationName()) : "—") + "</span></div>");
            if (assignment.getAssignMessage() != null) {
                sb.append("<div class='info-row'><span class='info-label'>Assignment Message</span><span class='info-value'>" + escapeHtml(assignment.getAssignMessage()) + "</span></div>");
            }
            sb.append("</div>");
        }

        // Cancellation Information
        if (bookingDetails.getCancellationInformation() != null) {
            BookingAllDetailsResponse.CancellationInformation cancellation = bookingDetails.getCancellationInformation();
            sb.append("<p class='section-title'>Cancellation Information</p>")
                    .append("<div class='info-card'>")
                    .append("<div class='info-row'><span class='info-label'>Cancellation Date</span><span class='info-value'>" + (cancellation.getCancellationDate() != null ? cancellation.getCancellationDate().toString() : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Refund Amount</span><span class='info-value'>$" + (cancellation.getRefundAmount() != null ? cancellation.getRefundAmount() : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Refund Status</span><span class='info-value'>" + (cancellation.getRefundStatus() != null ? escapeHtml(cancellation.getRefundStatus()) : "—") + "</span></div>");
            if (cancellation.getCancellationReason() != null) {
                sb.append("<div class='info-row'><span class='info-label'>Cancellation Reason</span><span class='info-value'>" + escapeHtml(cancellation.getCancellationReason()) + "</span></div>");
            }
            if (cancellation.getCancellationNotes() != null) {
                sb.append("<div class='info-row'><span class='info-label'>Cancellation Notes</span><span class='info-value'>" + escapeHtml(cancellation.getCancellationNotes()) + "</span></div>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildTerminateBookingParticipantsHtml(List<BookingAllDetailsResponse.ParticipantInformation> participants) {
        if (participants == null || participants.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Date of Birth</th><th>Gender</th><th>Nationality</th><th>Passport</th><th>Email</th><th>Mobile</th></tr></thead><tbody>");
        int i = 1;
        for (BookingAllDetailsResponse.ParticipantInformation p : participants) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(p.getFullName() != null ? escapeHtml(p.getFullName()) : escapeHtml(p.getFirstName() + " " + p.getLastName())).append("</td>")
                    .append("<td>").append(p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "—").append("</td>")
                    .append("<td>").append(p.getGender() != null ? escapeHtml(p.getGender()) : "—").append("</td>")
                    .append("<td>").append(p.getNationality() != null ? escapeHtml(p.getNationality()) : "—").append("</td>")
                    .append("<td>").append(p.getPassportNumber() != null ? escapeHtml(p.getPassportNumber()) : "—").append("</td>")
                    .append("<td>").append(p.getEmail() != null ? escapeHtml(p.getEmail()) : "—").append("</td>")
                    .append("<td>").append(p.getMobileNumber() != null ? escapeHtml(p.getMobileNumber()) : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildTerminateBookingAccommodationsHtml(List<BookingAllDetailsResponse.AccommodationInformation> accommodations) {
        if (accommodations == null || accommodations.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Hotel</th><th>Room Type</th><th>Room Number</th><th>Check In</th><th>Check Out</th><th>Confirmation #</th></tr></thead><tbody>");
        int i = 1;
        for (BookingAllDetailsResponse.AccommodationInformation a : accommodations) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(a.getHotelName())).append("</td>")
                    .append("<td>").append(escapeHtml(a.getRoomType())).append("</td>")
                    .append("<td>").append(a.getRoomNumber() != null ? escapeHtml(a.getRoomNumber()) : "—").append("</td>")
                    .append("<td>").append(a.getCheckInDate() != null ? a.getCheckInDate().toString() : "—").append("</td>")
                    .append("<td>").append(a.getCheckOutDate() != null ? a.getCheckOutDate().toString() : "—").append("</td>")
                    .append("<td>").append(a.getConfirmationNumber() != null ? escapeHtml(a.getConfirmationNumber()) : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildTerminateBookingTransportationsHtml(List<BookingAllDetailsResponse.TransportationInformation> transportations) {
        if (transportations == null || transportations.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Type</th><th>Carrier</th><th>Departure</th><th>Arrival</th><th>Reference #</th></tr></thead><tbody>");
        int i = 1;
        for (BookingAllDetailsResponse.TransportationInformation t : transportations) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(t.getTransportType())).append("</td>")
                    .append("<td>").append(t.getCarrierName() != null ? escapeHtml(t.getCarrierName()) : "—").append("</td>")
                    .append("<td>").append((t.getDepartureLocation() != null ? escapeHtml(t.getDepartureLocation()) + " " : "") +
                            (t.getDepartureDate() != null ? t.getDepartureDate().toString() : "") +
                            (t.getDepartureTime() != null ? " " + t.getDepartureTime().toString() : "")).append("</td>")
                    .append("<td>").append((t.getArrivalLocation() != null ? escapeHtml(t.getArrivalLocation()) + " " : "") +
                            (t.getArrivalDate() != null ? t.getArrivalDate().toString() : "") +
                            (t.getArrivalTime() != null ? " " + t.getArrivalTime().toString() : "")).append("</td>")
                    .append("<td>").append(t.getReferenceNumber() != null ? escapeHtml(t.getReferenceNumber()) : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildTerminateBookingActivitiesHtml(List<BookingAllDetailsResponse.ActivityInformation> activities) {
        if (activities == null || activities.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Activity</th><th>Date</th><th>Start</th><th>End</th><th>Participants</th><th>Total Price</th></tr></thead><tbody>");
        int i = 1;
        for (BookingAllDetailsResponse.ActivityInformation a : activities) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(a.getActivityName())).append("</td>")
                    .append("<td>").append(a.getActivityDate() != null ? a.getActivityDate().toString() : "—").append("</td>")
                    .append("<td>").append(a.getStartTime() != null ? a.getStartTime().toString() : "—").append("</td>")
                    .append("<td>").append(a.getEndTime() != null ? a.getEndTime().toString() : "—").append("</td>")
                    .append("<td>").append(a.getNumberOfParticipants() != null ? a.getNumberOfParticipants() : "—").append("</td>")
                    .append("<td>").append(a.getTotalPrice() != null ? "$" + a.getTotalPrice() : "—").append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildTerminateBookingInsuranceHtml(BookingAllDetailsResponse.BookingInsurance insurance) {
        if (insurance == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Insurance Details</p>")
                .append("<div class='info-card'>")
                .append("<div class='info-row'><span class='info-label'>Provider</span><span class='info-value'>" + escapeHtml(insurance.getInsuranceProvider()) + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Policy Number</span><span class='info-value'>" + escapeHtml(insurance.getPolicyNumber()) + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Coverage Type</span><span class='info-value'>" + escapeHtml(insurance.getCoverageType()) + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Premium Amount</span><span class='info-value'>$" + (insurance.getPremiumAmount() != null ? insurance.getPremiumAmount() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Policy Period</span><span class='info-value'>" +
                        (insurance.getPolicyStartDate() != null ? insurance.getPolicyStartDate().toString() : "—") +
                        " → " +
                        (insurance.getPolicyEndDate() != null ? insurance.getPolicyEndDate().toString() : "—") +
                        "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Status</span><span class='info-value'>" + (insurance.getStatus() != null ? insurance.getStatus() : "—") + "</span></div>")
                .append("</div>");
        return sb.toString();
    }

    private String buildTerminateBookingInvoiceHtml(BookingAllDetailsResponse.BookingInvoice invoice) {
        if (invoice == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Invoice Details</p>")
                .append("<div class='info-card'>")
                .append("<div class='info-row'><span class='info-label'>Due Date</span><span class='info-value'>" + (invoice.getDueDate() != null ? invoice.getDueDate().toString() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Billing Name</span><span class='info-value'>" + (invoice.getBillingFullName() != null ? escapeHtml(invoice.getBillingFullName()) : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Billing Email</span><span class='info-value'>" + (invoice.getBillingEmail() != null ? escapeHtml(invoice.getBillingEmail()) : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Billing Phone</span><span class='info-value'>" + (invoice.getBillingPhone() != null ? escapeHtml(invoice.getBillingPhone()) : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Sub Total</span><span class='info-value'><span class='price'>$" + (invoice.getSubTotal() != null ? invoice.getSubTotal() : "—") + "</span></span></div>")
                .append("<div class='info-row'><span class='info-label'>Tax Amount</span><span class='info-value'>$" + (invoice.getTaxAmount() != null ? invoice.getTaxAmount() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Discount</span><span class='info-value'>$" + (invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Insurance Amount</span><span class='info-value'>$" + (invoice.getInsuranceAmount() != null ? invoice.getInsuranceAmount() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Total Amount</span><span class='info-value'><span class='price'>$" + (invoice.getTotalAmount() != null ? invoice.getTotalAmount() : "—") + "</span></span></div>")
                .append("<div class='info-row'><span class='info-label'>Amount Paid</span><span class='info-value'>$" + (invoice.getAmountPaid() != null ? invoice.getAmountPaid() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Balance Due</span><span class='info-value'><span class='price'>$" + (invoice.getBalanceDue() != null ? invoice.getBalanceDue() : "—") + "</span></span></div>")
                .append("<div class='info-row'><span class='info-label'>Invoice Status</span><span class='info-value'>" + (invoice.getStatus() != null ? invoice.getStatus() : "—") + "</span></div>")
                .append("</div>");
        return sb.toString();
    }

    private String buildBookingStatusChangeDetailsHtml(BookingStatusComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        if (comparisonResult.getOldStatusId() == null && comparisonResult.getNewStatusId() == null) return "";
        if (comparisonResult.getOldStatusId() != null && comparisonResult.getOldStatusId().equals(comparisonResult.getNewStatusId())) {
            return "<div class='info-card'><div class='info-row'><span class='info-label'>Status</span><span class='info-value'>No change detected</span></div></div>";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Status Change</p>")
                .append("<div class='status-change-box'>")
                .append("<div style='display:flex;align-items:center;justify-content:center;flex-wrap:wrap;'>")

                // Old status
                .append("<div>")
                .append("<div class='status-label'>Previous Status</div>")
                .append("<div class='status-old'>")
                .append(getBookingStatusPill(comparisonResult.getOldStatusId(), comparisonResult.getOldStatusName()))
                .append("</div>")
                .append("</div>")

                // Arrow
                .append("<span class='arrow-icon'>➜</span>")

                // New status
                .append("<div>")
                .append("<div class='status-label'>New Status</div>")
                .append("<div class='status-new'>")
                .append(getBookingStatusPill(comparisonResult.getNewStatusId(), comparisonResult.getNewStatusName()))
                .append("</div>")
                .append("</div>")

                .append("</div>")
                .append("</div>");

        return sb.toString();
    }

    private String getBookingStatusPill(Long statusId, String statusName) {
        if (statusId == null || statusName == null) {
            return "<span class='status-pill status-unknown'>UNKNOWN</span>";
        }

        String cssClass = "status-unknown";
        String displayName = statusName;

        switch (statusName.toUpperCase()) {
            case "NEW_INQUIRY":
                cssClass = "status-new-inquiry";
                break;
            case "PENDING":
                cssClass = "status-pending";
                break;
            case "CONTACTED":
                cssClass = "status-contacted";
                break;
            case "QUOTATION_SENT":
                cssClass = "status-quotation-sent";
                break;
            case "NEGOTIATION":
                cssClass = "status-negotiation";
                break;
            case "CONFIRMED":
                cssClass = "status-confirmed";
                break;
            case "PAYMENT_PENDING":
                cssClass = "status-payment-pending";
                break;
            case "BOOKED":
                cssClass = "status-booked";
                break;
            case "COMPLETED":
                cssClass = "status-completed";
                break;
            case "CANCELLED":
                cssClass = "status-cancelled";
                break;
            case "REJECTED":
                cssClass = "status-rejected";
                break;
            case "EXPIRED":
                cssClass = "status-expired";
                break;
            default:
                cssClass = "status-unknown";
                break;
        }

        return "<span class='status-pill " + cssClass + "'>" + displayName + "</span>";
    }

    private String buildBookingStatusBasicDetailsHtml(BookingsBasicDetails bookingsBasicDetails) {
        if (bookingsBasicDetails == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Booking Details</p>")
                .append("<div class='info-card'>")
                .append("<div class='info-row'><span class='info-label'>Customer</span><span class='info-value'>" + escapeHtml(bookingsBasicDetails.getCustomerName()) + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Email</span><span class='info-value'>" + escapeHtml(bookingsBasicDetails.getEmail()) + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Mobile</span><span class='info-value'>" + escapeHtml(bookingsBasicDetails.getMobileNumber()) + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Tour</span><span class='info-value'>" + escapeHtml(bookingsBasicDetails.getTourName()) + " (ID: #" + bookingsBasicDetails.getTourId() + ")</span></div>")
                .append("<div class='info-row'><span class='info-label'>Package</span><span class='info-value'>" + (bookingsBasicDetails.getPackageName() != null ? escapeHtml(bookingsBasicDetails.getPackageName()) : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Travel Period</span><span class='info-value'>" +
                        (bookingsBasicDetails.getTravelStartDate() != null ? bookingsBasicDetails.getTravelStartDate().toString() : "—") +
                        " → " +
                        (bookingsBasicDetails.getTravelEndDate() != null ? bookingsBasicDetails.getTravelEndDate().toString() : "—") +
                        "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Total Persons</span><span class='info-value'>" + (bookingsBasicDetails.getTotalPersons() != null ? bookingsBasicDetails.getTotalPersons() : "—") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Total Amount</span><span class='info-value'><span class='price'>$" + (bookingsBasicDetails.getTotalAmount() != null ? bookingsBasicDetails.getTotalAmount() : "—") + "</span></span></div>")
                .append("<div class='info-row'><span class='info-label'>Final Amount</span><span class='info-value'><span class='price'>$" + (bookingsBasicDetails.getFinalAmount() != null ? bookingsBasicDetails.getFinalAmount() : "—") + "</span></span></div>")
                .append("<div class='info-row'><span class='info-label'>Insurance Required</span><span class='info-value'>" + (Boolean.TRUE.equals(bookingsBasicDetails.getInsuranceRequired()) ? "Yes" : "No") + "</span></div>")
                .append("<div class='info-row'><span class='info-label'>Assigned Employee</span><span class='info-value'>" + (bookingsBasicDetails.getAssignedEmployeeName() != null ? escapeHtml(bookingsBasicDetails.getAssignedEmployeeName()) : "—") + "</span></div>");

        if (bookingsBasicDetails.getSpecialRequirements() != null && !bookingsBasicDetails.getSpecialRequirements().isEmpty()) {
            sb.append("<div class='info-row'><span class='info-label'>Special Requirements</span><span class='info-value'>" + escapeHtml(bookingsBasicDetails.getSpecialRequirements()) + "</span></div>");
        }
        if (bookingsBasicDetails.getDietaryRestrictions() != null && !bookingsBasicDetails.getDietaryRestrictions().isEmpty()) {
            sb.append("<div class='info-row'><span class='info-label'>Dietary Restrictions</span><span class='info-value'>" + escapeHtml(bookingsBasicDetails.getDietaryRestrictions()) + "</span></div>");
        }
        if (bookingsBasicDetails.getCancellationDate() != null) {
            sb.append("<div class='info-row'><span class='info-label'>Cancellation Date</span><span class='info-value'>" + bookingsBasicDetails.getCancellationDate().toString() + "</span></div>");
        }
        if (bookingsBasicDetails.getRefundAmount() != null) {
            sb.append("<div class='info-row'><span class='info-label'>Refund Amount</span><span class='info-value'>$" + bookingsBasicDetails.getRefundAmount() + "</span></div>");
        }

        sb.append("</div>");
        return sb.toString();
    }

    @Override
    public String buildBookingCreateSuccessfullBody(InsertBookingRequest insertBookingRequest, Long bookingId, User loggedUser) {
        String participantsHtml = buildBookingParticipantsHtml(insertBookingRequest.getParticipants());
        String accommodationsHtml = buildBookingAccommodationsHtml(insertBookingRequest.getAccommodations());
        String transportationsHtml = buildBookingTransportationsHtml(insertBookingRequest.getTransportations());
        String activitiesHtml = buildBookingActivitiesHtml(insertBookingRequest.getActivities());
        String invoiceHtml = buildBookingInvoiceHtml(insertBookingRequest.getBookingInvoice());

        return "<!DOCTYPE html>" +
                "<html lang='en'><head><meta charset='UTF-8'/><meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>Booking Created</title>" +
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
                ".booking-icon{display:inline-block;font-size:20px;margin-right:8px;vertical-align:middle;}" +
                ".status-pill{display:inline-block;padding:2px 12px;border-radius:12px;font-size:12px;font-family:Arial,sans-serif;}" +
                ".status-active{background:#d4f4e8;color:#1a6b40;}" +
                ".status-inactive{background:#fdecea;color:#a33;}" +
                ".info-table{width:100%;border-collapse:collapse;margin-top:8px;}" +
                ".info-table th{background:#e8f5f5;color:#0e7c7b;font-family:Arial,sans-serif;font-size:11px;letter-spacing:1px;text-transform:uppercase;padding:10px 12px;text-align:left;border:1px solid #c8e8e8;}" +
                ".info-table td{padding:10px 12px;border:1px solid #e0f0f0;font-size:13px;color:#2a4444;font-family:Arial,sans-serif;vertical-align:top;}" +
                ".info-table tr:nth-child(even) td{background:#f9fdfd;}" +
                ".actor-row{display:flex;align-items:center;gap:12px;padding:16px 20px;background:#f5fbfb;border:1px solid #c8e8e8;border-radius:8px;margin-bottom:24px;}" +
                ".actor-avatar{width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,#0e7c7b,#2bbfbf);display:flex;align-items:center;justify-content:center;color:#fff;font-size:16px;font-weight:bold;font-family:Arial,sans-serif;flex-shrink:0;}" +
                ".actor-info{font-family:Arial,sans-serif;}" +
                ".actor-name{font-size:14px;color:#1a3333;font-weight:bold;}" +
                ".actor-meta{font-size:12px;color:#6b8e8e;margin-top:2px;}" +
                ".reference-id{font-family:monospace;background:#f0f7f7;padding:2px 8px;border-radius:4px;font-size:13px;}" +
                ".price{font-weight:bold;color:#1a6b40;}" +
                ".footer{background:#e8f5f5;border-top:2px solid #c8e8e8;padding:24px 40px;text-align:center;}" +
                ".footer p{font-family:Arial,sans-serif;font-size:11px;color:#6b8e8e;margin:4px 0;line-height:1.6;}" +
                ".footer .brand{font-size:13px;color:#0e7c7b;font-weight:bold;margin-bottom:6px;}" +
                "</style></head><body>" +
                "<div class='wrapper'>" +
                "<div class='header'>" +
                "<img src='https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png' alt='Felicita Trips'/>" +
                "<h1>Booking Created</h1>" +
                "<p class='tagline'>See More! Feel More! Live More!</p>" +
                "<span class='badge'>&#128736; New Booking Added</span>" +
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

                "<p class='section-title'>Booking Details</p>" +
                "<div class='info-card'>" +
                "<div class='info-row'><span class='info-label'>Booking ID</span><span class='info-value'><span class='reference-id'>#" + bookingId + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Customer ID</span><span class='info-value'>#" + (insertBookingRequest.getCustomerId() != null ? insertBookingRequest.getCustomerId() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Tour ID</span><span class='info-value'>" + (insertBookingRequest.getTourId() != null ? "#" + insertBookingRequest.getTourId() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Package ID</span><span class='info-value'>" + (insertBookingRequest.getPackageId() != null ? "#" + insertBookingRequest.getPackageId() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Package Schedule ID</span><span class='info-value'>" + (insertBookingRequest.getPackageScheduleId() != null ? "#" + insertBookingRequest.getPackageScheduleId() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Booking Date</span><span class='info-value'>" + (insertBookingRequest.getBookingDate() != null ? insertBookingRequest.getBookingDate().toString() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Travel Period</span><span class='info-value'>" +
                (insertBookingRequest.getTravelStartDate() != null ? insertBookingRequest.getTravelStartDate().toString() : "—") +
                " → " +
                (insertBookingRequest.getTravelEndDate() != null ? insertBookingRequest.getTravelEndDate().toString() : "—") +
                "</span></div>" +
                "<div class='info-row'><span class='info-label'>Total Persons</span><span class='info-value'>" + (insertBookingRequest.getTotalPersons() != null ? insertBookingRequest.getTotalPersons() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Insurance Required</span><span class='info-value'>" + (Boolean.TRUE.equals(insertBookingRequest.getInsuranceRequired()) ? "Yes" : "No") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Booking Status</span><span class='info-value'>" + getBookingStatusName(insertBookingRequest.getBookingStatusId()) + "</span></div>" +
                (insertBookingRequest.getSpecialRequirements() != null && !insertBookingRequest.getSpecialRequirements().isEmpty() ?
                        "<div class='info-row'><span class='info-label'>Special Requirements</span><span class='info-value'>" + escapeHtml(insertBookingRequest.getSpecialRequirements()) + "</span></div>" : "") +
                (insertBookingRequest.getDietaryRestrictions() != null && !insertBookingRequest.getDietaryRestrictions().isEmpty() ?
                        "<div class='info-row'><span class='info-label'>Dietary Restrictions</span><span class='info-value'>" + escapeHtml(insertBookingRequest.getDietaryRestrictions()) + "</span></div>" : "") +
                "<div class='info-row'><span class='info-label'>Total Amount</span><span class='info-value'><span class='price'>$" + (insertBookingRequest.getTotalAmount() != null ? insertBookingRequest.getTotalAmount() : "—") + "</span></span></div>" +
                "<div class='info-row'><span class='info-label'>Discount</span><span class='info-value'>$" + (insertBookingRequest.getDiscountAmount() != null ? insertBookingRequest.getDiscountAmount() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Tax</span><span class='info-value'>$" + (insertBookingRequest.getTaxAmount() != null ? insertBookingRequest.getTaxAmount() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Insurance Amount</span><span class='info-value'>$" + (insertBookingRequest.getInsuranceAmount() != null ? insertBookingRequest.getInsuranceAmount() : "—") + "</span></div>" +
                "<div class='info-row'><span class='info-label'>Final Amount</span><span class='info-value'><span class='price'>$" + (insertBookingRequest.getFinalAmount() != null ? insertBookingRequest.getFinalAmount() : "—") + "</span></span></div>" +
                "</div>" +

                (participantsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Participants (" + (insertBookingRequest.getParticipants() != null ? insertBookingRequest.getParticipants().size() : 0) + ")</p>" +
                                participantsHtml) +

                (accommodationsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Accommodations (" + (insertBookingRequest.getAccommodations() != null ? insertBookingRequest.getAccommodations().size() : 0) + ")</p>" +
                                accommodationsHtml) +

                (transportationsHtml.isEmpty() ? "" :
                        "<p class='section-title'>Transportations (" + (insertBookingRequest.getTransportations() != null ? insertBookingRequest.getTransportations().size() : 0) + ")</p>" +
                                transportationsHtml) +

                (activitiesHtml.isEmpty() ? "" :
                        "<p class='section-title'>Activities (" + (insertBookingRequest.getActivities() != null ? insertBookingRequest.getActivities().size() : 0) + ")</p>" +
                                activitiesHtml) +

                (invoiceHtml.isEmpty() ? "" :
                        "<p class='section-title'>Invoice Details</p>" +
                                invoiceHtml) +

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

    private String getStatusName(Integer status) {
        if (status == null) return "—";
        if (status == 1) return "Active";
        if (status == 2) return "Inactive";
        if (status == 3) return "Terminated";
        return "Unknown";
    }

    private String getBookingStatusName(Long statusId) {
        if (statusId == null) return "—";
        if (statusId == 1) return "NEW_INQUIRY";
        if (statusId == 2) return "PENDING";
        if (statusId == 3) return "CONTACTED";
        if (statusId == 4) return "QUOTATION_SENT";
        if (statusId == 5) return "NEGOTIATION";
        if (statusId == 6) return "CONFIRMED";
        if (statusId == 7) return "PAYMENT_PENDING";
        if (statusId == 8) return "BOOKED";
        if (statusId == 9) return "COMPLETED";
        if (statusId == 10) return "CANCELLED";
        if (statusId == 11) return "REJECTED";
        if (statusId == 12) return "EXPIRED";
        return "UNKNOWN";
    }

    private String buildBookingParticipantsHtml(List<InsertBookingRequest.Participant> participants) {
        if (participants == null || participants.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Date of Birth</th><th>Passport</th><th>Email</th><th>Mobile</th><th>Status</th></tr></thead><tbody>");
        int i = 1;
        for (InsertBookingRequest.Participant p : participants) {
            String fullName = (p.getFirstName() != null ? p.getFirstName() : "") +
                    (p.getLastName() != null ? " " + p.getLastName() : "");
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(escapeHtml(fullName.trim())).append("</td>")
                    .append("<td>").append(p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "—").append("</td>")
                    .append("<td>").append(p.getPassportNumber() != null ? escapeHtml(p.getPassportNumber()) : "—").append("</td>")
                    .append("<td>").append(p.getEmail() != null ? escapeHtml(p.getEmail()) : "—").append("</td>")
                    .append("<td>").append(p.getMobileNumber() != null ? escapeHtml(p.getMobileNumber()) : "—").append("</td>")
                    .append("<td>").append(getStatusName(p.getStatus() != null ? p.getStatus().intValue() : null)).append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildBookingAccommodationsHtml(List<InsertBookingRequest.Accommodation> accommodations) {
        if (accommodations == null || accommodations.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Hotel ID</th><th>Room Type</th><th>Room Number</th><th>Check In</th><th>Check Out</th><th>Confirmation #</th><th>Status</th></tr></thead><tbody>");
        int i = 1;
        for (InsertBookingRequest.Accommodation a : accommodations) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(a.getHotelId() != null ? escapeHtml(a.getHotelId()) : "—").append("</td>")
                    .append("<td>").append(a.getRoomType() != null ? escapeHtml(a.getRoomType()) : "—").append("</td>")
                    .append("<td>").append(a.getRoomNumber() != null ? escapeHtml(a.getRoomNumber()) : "—").append("</td>")
                    .append("<td>").append(a.getCheckInDate() != null ? a.getCheckInDate().toString() : "—").append("</td>")
                    .append("<td>").append(a.getCheckOutDate() != null ? a.getCheckOutDate().toString() : "—").append("</td>")
                    .append("<td>").append(a.getConfirmationNumber() != null ? escapeHtml(a.getConfirmationNumber()) : "—").append("</td>")
                    .append("<td>").append(getStatusName(a.getStatus() != null ? a.getStatus().intValue() : null)).append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildBookingTransportationsHtml(List<InsertBookingRequest.Transportation> transportations) {
        if (transportations == null || transportations.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Type</th><th>Carrier</th><th>Departure</th><th>Arrival</th><th>Reference #</th><th>Status</th></tr></thead><tbody>");
        int i = 1;
        for (InsertBookingRequest.Transportation t : transportations) {
            String departureInfo = "";
            if (t.getDepartureLocation() != null) departureInfo += escapeHtml(t.getDepartureLocation());
            if (t.getDepartureDate() != null) departureInfo += " " + t.getDepartureDate().toString();
            if (t.getDepartureTime() != null) departureInfo += " " + t.getDepartureTime().toString();
            if (departureInfo.isEmpty()) departureInfo = "—";

            String arrivalInfo = "";
            if (t.getArrivalLocation() != null) arrivalInfo += escapeHtml(t.getArrivalLocation());
            if (t.getArrivalDate() != null) arrivalInfo += " " + t.getArrivalDate().toString();
            if (t.getArrivalTime() != null) arrivalInfo += " " + t.getArrivalTime().toString();
            if (arrivalInfo.isEmpty()) arrivalInfo = "—";

            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(t.getTransportType() != null ? escapeHtml(t.getTransportType()) : "—").append("</td>")
                    .append("<td>").append(t.getCarrierName() != null ? escapeHtml(t.getCarrierName()) : "—").append("</td>")
                    .append("<td>").append(departureInfo).append("</td>")
                    .append("<td>").append(arrivalInfo).append("</td>")
                    .append("<td>").append(t.getReferenceNumber() != null ? escapeHtml(t.getReferenceNumber()) : "—").append("</td>")
                    .append("<td>").append(getStatusName(t.getStatus() != null ? t.getStatus().intValue() : null)).append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildBookingActivitiesHtml(List<InsertBookingRequest.Activity> activities) {
        if (activities == null || activities.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='info-table'><thead><tr><th>#</th><th>Activity ID</th><th>Schedule ID</th><th>Date</th><th>Start</th><th>End</th><th>Participants</th><th>Price/Person</th><th>Total Price</th><th>Status</th></tr></thead><tbody>");
        int i = 1;
        for (InsertBookingRequest.Activity a : activities) {
            sb.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(a.getActivityId() != null ? "#" + a.getActivityId() : "—").append("</td>")
                    .append("<td>").append(a.getActivityScheduleId() != null ? "#" + a.getActivityScheduleId() : "—").append("</td>")
                    .append("<td>").append(a.getActivityDate() != null ? a.getActivityDate().toString() : "—").append("</td>")
                    .append("<td>").append(a.getStartTime() != null ? a.getStartTime().toString() : "—").append("</td>")
                    .append("<td>").append(a.getEndTime() != null ? a.getEndTime().toString() : "—").append("</td>")
                    .append("<td>").append(a.getNumberOfParticipants() != null ? a.getNumberOfParticipants() : "—").append("</td>")
                    .append("<td>").append(a.getPricePerPerson() != null ? "$" + a.getPricePerPerson() : "—").append("</td>")
                    .append("<td>").append(a.getTotalPrice() != null ? "$" + a.getTotalPrice() : "—").append("</td>")
                    .append("<td>").append(getStatusName(a.getStatus() != null ? a.getStatus().intValue() : null)).append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private String buildBookingInvoiceHtml(InsertBookingRequest.BookingInvoice invoice) {
        if (invoice == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='info-card'>");
        sb.append("<div class='info-row'><span class='info-label'>Due Date</span><span class='info-value'>" + (invoice.getDueDate() != null ? invoice.getDueDate().toString() : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Billing Name</span><span class='info-value'>" + (invoice.getBillingFullName() != null ? escapeHtml(invoice.getBillingFullName()) : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Billing Email</span><span class='info-value'>" + (invoice.getBillingEmail() != null ? escapeHtml(invoice.getBillingEmail()) : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Billing Phone</span><span class='info-value'>" + (invoice.getBillingPhone() != null ? escapeHtml(invoice.getBillingPhone()) : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Billing Address</span><span class='info-value'>" + (invoice.getBillingAddress() != null ? escapeHtml(invoice.getBillingAddress()) : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Sub Total</span><span class='info-value'><span class='price'>$" + (invoice.getSubTotal() != null ? invoice.getSubTotal() : "—") + "</span></span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Tax Amount</span><span class='info-value'>$" + (invoice.getTaxAmount() != null ? invoice.getTaxAmount() : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Discount</span><span class='info-value'>$" + (invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Insurance Amount</span><span class='info-value'>$" + (invoice.getInsuranceAmount() != null ? invoice.getInsuranceAmount() : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Total Amount</span><span class='info-value'><span class='price'>$" + (invoice.getTotalAmount() != null ? invoice.getTotalAmount() : "—") + "</span></span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Amount Paid</span><span class='info-value'>$" + (invoice.getAmountPaid() != null ? invoice.getAmountPaid() : "—") + "</span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Balance Due</span><span class='info-value'><span class='price'>$" + (invoice.getBalanceDue() != null ? invoice.getBalanceDue() : "—") + "</span></span></div>");
        sb.append("<div class='info-row'><span class='info-label'>Invoice Status</span><span class='info-value'>" + getStatusName(invoice.getStatus() != null ? invoice.getStatus().intValue() : null) + "</span></div>");
        sb.append("</div>");
        return sb.toString();
    }

    private String buildBookingBasicFieldsHtml(List<BookingComparisonResult.FieldChange> fieldChanges) {
        if (fieldChanges == null || fieldChanges.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (BookingComparisonResult.FieldChange field : fieldChanges) {
            String displayOldValue = formatValueForDisplay(field.getOldValue());
            String displayNewValue = formatValueForDisplay(field.getNewValue());

            // Special handling for amount fields
            if (field.getFieldName() != null &&
                    (field.getFieldName().contains("Amount") || field.getFieldName().contains("Price") ||
                            field.getFieldName().contains("Total") || field.getFieldName().contains("Final"))) {
                sb.append("<tr>")
                        .append("<td><strong>").append(formatFieldName(field.getFieldName())).append("</strong></td>")
                        .append("<td><span class='old-value'>$").append(displayOldValue).append("</span></td>")
                        .append("<td><span class='new-value'>$").append(displayNewValue).append("</span></td>")
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

    private String buildBookingStatusChangeHtml(BookingComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        if (comparisonResult.getOldBookingStatusId() == null && comparisonResult.getNewBookingStatusId() == null) return "";
        if (comparisonResult.getOldBookingStatusId() != null &&
                comparisonResult.getOldBookingStatusId().equals(comparisonResult.getNewBookingStatusId())) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("<p class='section-title'>Status Change</p>")
                .append("<div class='info-card'>")
                .append("<div class='info-row'><span class='info-label'>Previous Status</span><span class='info-value'>")
                .append(comparisonResult.getOldBookingStatusName() != null ? comparisonResult.getOldBookingStatusName() : "—")
                .append("</span></div>")
                .append("<div class='info-row'><span class='info-label'>New Status</span><span class='info-value'>")
                .append(comparisonResult.getNewBookingStatusName() != null ? comparisonResult.getNewBookingStatusName() : "—")
                .append("</span></div>")
                .append("</div>");
        return sb.toString();
    }

    private String buildBookingParticipantsUpdateHtml(BookingComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        StringBuilder sb = new StringBuilder();

        // Participants to add
        if (comparisonResult.getParticipantsToAdd() != null && !comparisonResult.getParticipantsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Participants</p>")
                    .append("<table class='info-table'><thead><tr><th>#</th><th>Name</th><th>Date of Birth</th><th>Email</th><th>Mobile</th><th>Passport</th></tr></thead><tbody>");
            int i = 1;
            for (BookingComparisonResult.ParticipantChange p : comparisonResult.getParticipantsToAdd()) {
                String fullName = (p.getFirstName() != null ? p.getFirstName() : "") +
                        (p.getLastName() != null ? " " + p.getLastName() : "");
                sb.append("<tr>")
                        .append("<td>").append(i++).append("</td>")
                        .append("<td><span class='add-tag'>+ </span>").append(escapeHtml(fullName.trim())).append("</td>")
                        .append("<td>").append(p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "—").append("</td>")
                        .append("<td>").append(p.getEmail() != null ? escapeHtml(p.getEmail()) : "—").append("</td>")
                        .append("<td>").append(p.getMobileNumber() != null ? escapeHtml(p.getMobileNumber()) : "—").append("</td>")
                        .append("<td>").append(p.getPassportNumber() != null ? escapeHtml(p.getPassportNumber()) : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        // Participants to remove
        if (comparisonResult.getParticipantsToRemove() != null && !comparisonResult.getParticipantsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Participants</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long participantId : comparisonResult.getParticipantsToRemove()) {
                sb.append("<span class='remove-tag'>Participant ID: #").append(participantId).append("</span>");
            }
            sb.append("</div>");
        }

        // Participants to update
        if (comparisonResult.getParticipantsToUpdate() != null && !comparisonResult.getParticipantsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Participants</p>")
                    .append("<table class='info-table'><thead><tr><th>ID</th><th>Field</th><th>Old Value</th><th>New Value</th></tr></thead><tbody>");
            for (BookingComparisonResult.ParticipantUpdateChange p : comparisonResult.getParticipantsToUpdate()) {
                if (p.getOldFirstName() != null && p.getFirstName() != null && !p.getOldFirstName().equals(p.getFirstName())) {
                    sb.append("<tr><td>").append(p.getParticipantId()).append("</td><td>First Name</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(p.getOldFirstName())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(p.getFirstName())).append("</span></td></tr>");
                }
                if (p.getOldLastName() != null && p.getLastName() != null && !p.getOldLastName().equals(p.getLastName())) {
                    sb.append("<tr><td>").append(p.getParticipantId()).append("</td><td>Last Name</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(p.getOldLastName())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(p.getLastName())).append("</span></td></tr>");
                }
                if (p.getOldEmail() != null && p.getEmail() != null && !p.getOldEmail().equals(p.getEmail())) {
                    sb.append("<tr><td>").append(p.getParticipantId()).append("</td><td>Email</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(p.getOldEmail())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(p.getEmail())).append("</span></td></tr>");
                }
                if (p.getOldMobileNumber() != null && p.getMobileNumber() != null && !p.getOldMobileNumber().equals(p.getMobileNumber())) {
                    sb.append("<tr><td>").append(p.getParticipantId()).append("</td><td>Mobile</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(p.getOldMobileNumber())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(p.getMobileNumber())).append("</span></td></tr>");
                }
            }
            sb.append("</tbody></table><br/>");
        }

        return sb.toString();
    }

    private String buildBookingAccommodationsUpdateHtml(BookingComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        StringBuilder sb = new StringBuilder();

        // Accommodations to add
        if (comparisonResult.getAccommodationsToAdd() != null && !comparisonResult.getAccommodationsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Accommodations</p>")
                    .append("<table class='info-table'><thead><tr><th>#</th><th>Hotel</th><th>Room Type</th><th>Check In</th><th>Check Out</th></tr></thead><tbody>");
            int i = 1;
            for (BookingComparisonResult.AccommodationChange a : comparisonResult.getAccommodationsToAdd()) {
                sb.append("<tr>")
                        .append("<td>").append(i++).append("</td>")
                        .append("<td><span class='add-tag'>+ </span>").append(escapeHtml(a.getHotelName())).append("</td>")
                        .append("<td>").append(escapeHtml(a.getRoomType())).append("</td>")
                        .append("<td>").append(a.getCheckInDate() != null ? a.getCheckInDate().toString() : "—").append("</td>")
                        .append("<td>").append(a.getCheckOutDate() != null ? a.getCheckOutDate().toString() : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        // Accommodations to remove
        if (comparisonResult.getAccommodationsToRemove() != null && !comparisonResult.getAccommodationsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Accommodations</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long accommodationId : comparisonResult.getAccommodationsToRemove()) {
                sb.append("<span class='remove-tag'>Accommodation ID: #").append(accommodationId).append("</span>");
            }
            sb.append("</div>");
        }

        // Accommodations to update
        if (comparisonResult.getAccommodationsToUpdate() != null && !comparisonResult.getAccommodationsToUpdate().isEmpty()) {
            sb.append("<p class='section-title'>Updated Accommodations</p>")
                    .append("<table class='info-table'><thead><tr><th>ID</th><th>Field</th><th>Old Value</th><th>New Value</th></tr></thead><tbody>");
            for (BookingComparisonResult.AccommodationUpdateChange a : comparisonResult.getAccommodationsToUpdate()) {
                if (a.getOldHotelName() != null && a.getHotelName() != null && !a.getOldHotelName().equals(a.getHotelName())) {
                    sb.append("<tr><td>").append(a.getAccommodationId()).append("</td><td>Hotel</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(a.getOldHotelName())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(a.getHotelName())).append("</span></td></tr>");
                }
                if (a.getOldRoomType() != null && a.getRoomType() != null && !a.getOldRoomType().equals(a.getRoomType())) {
                    sb.append("<tr><td>").append(a.getAccommodationId()).append("</td><td>Room Type</td>")
                            .append("<td><span class='old-value'>").append(escapeHtml(a.getOldRoomType())).append("</span></td>")
                            .append("<td><span class='new-value'>").append(escapeHtml(a.getRoomType())).append("</span></td></tr>");
                }
            }
            sb.append("</tbody></table><br/>");
        }

        return sb.toString();
    }

    private String buildBookingTransportationsUpdateHtml(BookingComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        StringBuilder sb = new StringBuilder();

        // Transportations to add
        if (comparisonResult.getTransportationsToAdd() != null && !comparisonResult.getTransportationsToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Transportations</p>")
                    .append("<table class='info-table'><thead><tr><th>#</th><th>Type</th><th>Carrier</th><th>Departure</th><th>Arrival</th></tr></thead><tbody>");
            int i = 1;
            for (BookingComparisonResult.TransportationChange t : comparisonResult.getTransportationsToAdd()) {
                sb.append("<tr>")
                        .append("<td>").append(i++).append("</td>")
                        .append("<td><span class='add-tag'>+ </span>").append(escapeHtml(t.getTransportType())).append("</td>")
                        .append("<td>").append(escapeHtml(t.getCarrierName())).append("</td>")
                        .append("<td>").append(escapeHtml(t.getDepartureLocation())).append(" ").append(t.getDepartureDate() != null ? t.getDepartureDate().toString() : "").append("</td>")
                        .append("<td>").append(escapeHtml(t.getArrivalLocation())).append(" ").append(t.getArrivalDate() != null ? t.getArrivalDate().toString() : "").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        // Transportations to remove
        if (comparisonResult.getTransportationsToRemove() != null && !comparisonResult.getTransportationsToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Transportations</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long transportationId : comparisonResult.getTransportationsToRemove()) {
                sb.append("<span class='remove-tag'>Transportation ID: #").append(transportationId).append("</span>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildBookingActivitiesUpdateHtml(BookingComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        StringBuilder sb = new StringBuilder();

        // Activities to add
        if (comparisonResult.getActivitiesToAdd() != null && !comparisonResult.getActivitiesToAdd().isEmpty()) {
            sb.append("<p class='section-title'>Added Activities</p>")
                    .append("<table class='info-table'><thead><tr><th>#</th><th>Activity</th><th>Date</th><th>Participants</th><th>Total Price</th></tr></thead><tbody>");
            int i = 1;
            for (BookingComparisonResult.ActivityChange a : comparisonResult.getActivitiesToAdd()) {
                sb.append("<tr>")
                        .append("<td>").append(i++).append("</td>")
                        .append("<td><span class='add-tag'>+ </span>").append(escapeHtml(a.getActivityName())).append("</td>")
                        .append("<td>").append(a.getActivityDate() != null ? a.getActivityDate().toString() : "—").append("</td>")
                        .append("<td>").append(a.getNumberOfParticipants() != null ? a.getNumberOfParticipants() : "—").append("</td>")
                        .append("<td>$").append(a.getTotalPrice() != null ? a.getTotalPrice() : "—").append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        // Activities to remove
        if (comparisonResult.getActivitiesToRemove() != null && !comparisonResult.getActivitiesToRemove().isEmpty()) {
            sb.append("<p class='section-title'>Removed Activities</p>")
                    .append("<div style='display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px;'>");
            for (Long activityId : comparisonResult.getActivitiesToRemove()) {
                sb.append("<span class='remove-tag'>Activity ID: #").append(activityId).append("</span>");
            }
            sb.append("</div>");
        }

        return sb.toString();
    }

    private String buildBookingInsuranceUpdateHtml(BookingComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        StringBuilder sb = new StringBuilder();

        // Insurance to add
        if (comparisonResult.getInsuranceToAdd() != null) {
            BookingComparisonResult.InsuranceChange insurance = comparisonResult.getInsuranceToAdd();
            sb.append("<p class='section-title'>Insurance Added</p>")
                    .append("<div class='info-card'>")
                    .append("<div class='info-row'><span class='info-label'>Provider</span><span class='info-value'><span class='add-tag'>+ </span>" + escapeHtml(insurance.getInsuranceProvider()) + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Policy Number</span><span class='info-value'>" + escapeHtml(insurance.getPolicyNumber()) + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Coverage Type</span><span class='info-value'>" + escapeHtml(insurance.getCoverageType()) + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Premium Amount</span><span class='info-value'>$" + (insurance.getPremiumAmount() != null ? insurance.getPremiumAmount() : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Policy Period</span><span class='info-value'>" +
                            (insurance.getPolicyStartDate() != null ? insurance.getPolicyStartDate().toString() : "—") +
                            " → " +
                            (insurance.getPolicyEndDate() != null ? insurance.getPolicyEndDate().toString() : "—") +
                            "</span></div>")
                    .append("</div>");
        }

        // Insurance to remove
        if (comparisonResult.getInsuranceToRemove() != null) {
            sb.append("<p class='section-title'>Insurance Removed</p>")
                    .append("<div class='info-card'>")
                    .append("<div class='info-row'><span class='info-label'>Insurance ID</span><span class='info-value'><span class='remove-tag'>Insurance ID: #" + comparisonResult.getInsuranceToRemove() + "</span></span></div>")
                    .append("</div>");
        }

        // Insurance to update
        if (comparisonResult.getInsuranceToUpdate() != null) {
            BookingComparisonResult.InsuranceUpdateChange insurance = comparisonResult.getInsuranceToUpdate();
            sb.append("<p class='section-title'>Insurance Updated</p>")
                    .append("<table class='info-table'><thead><tr><th>Field</th><th>Old Value</th><th>New Value</th></tr></thead><tbody>");

            if (insurance.getOldInsuranceProvider() != null && insurance.getInsuranceProvider() != null &&
                    !insurance.getOldInsuranceProvider().equals(insurance.getInsuranceProvider())) {
                sb.append("<tr><td>Provider</td>")
                        .append("<td><span class='old-value'>").append(escapeHtml(insurance.getOldInsuranceProvider())).append("</span></td>")
                        .append("<td><span class='new-value'>").append(escapeHtml(insurance.getInsuranceProvider())).append("</span></td></tr>");
            }
            if (insurance.getOldPolicyNumber() != null && insurance.getPolicyNumber() != null &&
                    !insurance.getOldPolicyNumber().equals(insurance.getPolicyNumber())) {
                sb.append("<tr><td>Policy Number</td>")
                        .append("<td><span class='old-value'>").append(escapeHtml(insurance.getOldPolicyNumber())).append("</span></td>")
                        .append("<td><span class='new-value'>").append(escapeHtml(insurance.getPolicyNumber())).append("</span></td></tr>");
            }
            if (insurance.getOldPremiumAmount() != null && insurance.getPremiumAmount() != null &&
                    !insurance.getOldPremiumAmount().equals(insurance.getPremiumAmount())) {
                sb.append("<tr><td>Premium Amount</td>")
                        .append("<td><span class='old-value'>$").append(insurance.getOldPremiumAmount()).append("</span></td>")
                        .append("<td><span class='new-value'>$").append(insurance.getPremiumAmount()).append("</span></td></tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        return sb.toString();
    }

    private String buildBookingInvoiceUpdateHtml(BookingComparisonResult comparisonResult) {
        if (comparisonResult == null) return "";
        StringBuilder sb = new StringBuilder();

        // Invoice to add
        if (comparisonResult.getInvoiceToAdd() != null) {
            BookingComparisonResult.InvoiceChange invoice = comparisonResult.getInvoiceToAdd();
            sb.append("<p class='section-title'>Invoice Added</p>")
                    .append("<div class='info-card'>")
                    .append("<div class='info-row'><span class='info-label'>Billing Name</span><span class='info-value'><span class='add-tag'>+ </span>" + escapeHtml(invoice.getBillingFullName()) + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Billing Email</span><span class='info-value'>" + escapeHtml(invoice.getBillingEmail()) + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Sub Total</span><span class='info-value'>$" + (invoice.getSubTotal() != null ? invoice.getSubTotal() : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Tax Amount</span><span class='info-value'>$" + (invoice.getTaxAmount() != null ? invoice.getTaxAmount() : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Total Amount</span><span class='info-value'>$" + (invoice.getTotalAmount() != null ? invoice.getTotalAmount() : "—") + "</span></div>")
                    .append("<div class='info-row'><span class='info-label'>Balance Due</span><span class='info-value'>$" + (invoice.getBalanceDue() != null ? invoice.getBalanceDue() : "—") + "</span></div>")
                    .append("</div>");
        }

        // Invoice to remove
        if (comparisonResult.getInvoiceToRemove() != null) {
            sb.append("<p class='section-title'>Invoice Removed</p>")
                    .append("<div class='info-card'>")
                    .append("<div class='info-row'><span class='info-label'>Invoice ID</span><span class='info-value'><span class='remove-tag'>Invoice ID: #" + comparisonResult.getInvoiceToRemove() + "</span></span></div>")
                    .append("</div>");
        }

        // Invoice to update
        if (comparisonResult.getInvoiceToUpdate() != null) {
            BookingComparisonResult.InvoiceUpdateChange invoice = comparisonResult.getInvoiceToUpdate();
            sb.append("<p class='section-title'>Invoice Updated</p>")
                    .append("<table class='info-table'><thead><tr><th>Field</th><th>Old Value</th><th>New Value</th></tr></thead><tbody>");

            if (invoice.getOldSubTotal() != null && invoice.getSubTotal() != null &&
                    !invoice.getOldSubTotal().equals(invoice.getSubTotal())) {
                sb.append("<tr><td>Sub Total</td>")
                        .append("<td><span class='old-value'>$").append(invoice.getOldSubTotal()).append("</span></td>")
                        .append("<td><span class='new-value'>$").append(invoice.getSubTotal()).append("</span></td></tr>");
            }
            if (invoice.getOldTotalAmount() != null && invoice.getTotalAmount() != null &&
                    !invoice.getOldTotalAmount().equals(invoice.getTotalAmount())) {
                sb.append("<tr><td>Total Amount</td>")
                        .append("<td><span class='old-value'>$").append(invoice.getOldTotalAmount()).append("</span></td>")
                        .append("<td><span class='new-value'>$").append(invoice.getTotalAmount()).append("</span></td></tr>");
            }
            if (invoice.getOldBalanceDue() != null && invoice.getBalanceDue() != null &&
                    !invoice.getOldBalanceDue().equals(invoice.getBalanceDue())) {
                sb.append("<tr><td>Balance Due</td>")
                        .append("<td><span class='old-value'>$").append(invoice.getOldBalanceDue()).append("</span></td>")
                        .append("<td><span class='new-value'>$").append(invoice.getBalanceDue()).append("</span></td></tr>");
            }
            if (invoice.getOldBillingFullName() != null && invoice.getBillingFullName() != null &&
                    !invoice.getOldBillingFullName().equals(invoice.getBillingFullName())) {
                sb.append("<tr><td>Billing Name</td>")
                        .append("<td><span class='old-value'>").append(escapeHtml(invoice.getOldBillingFullName())).append("</span></td>")
                        .append("<td><span class='new-value'>").append(escapeHtml(invoice.getBillingFullName())).append("</span></td></tr>");
            }
            sb.append("</tbody></table><br/>");
        }

        return sb.toString();
    }

    private String formatValueForDisplay(Object value) {
        if (value == null) return "—";
        if (value instanceof Boolean) {
            return (Boolean) value ? "Yes" : "No";
        }
        if (value instanceof BigDecimal) {
            return value.toString();
        }
        if (value instanceof LocalDate) {
            return ((LocalDate) value).toString();
        }
        if (value instanceof LocalTime) {
            return ((LocalTime) value).toString();
        }
        if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        }
        return escapeHtml(value.toString());
    }

    private String formatFieldName(String fieldName) {
        if (fieldName == null) return "";
        String readable = fieldName.replaceAll("([A-Z])", " $1").toLowerCase();
        readable = readable.substring(0, 1).toUpperCase() + readable.substring(1);

        // Special case replacements for better readability
        readable = readable.replace("Booking status id", "Booking Status");
        readable = readable.replace("Total persons", "Total Persons");
        readable = readable.replace("Insurance required", "Insurance Required");
        readable = readable.replace("Special requirements", "Special Requirements");
        readable = readable.replace("Dietary restrictions", "Dietary Restrictions");
        readable = readable.replace("Total amount", "Total Amount");
        readable = readable.replace("Discount amount", "Discount");
        readable = readable.replace("Tax amount", "Tax");
        readable = readable.replace("Insurance amount", "Insurance Amount");
        readable = readable.replace("Final amount", "Final Amount");

        return readable;
    }
}
