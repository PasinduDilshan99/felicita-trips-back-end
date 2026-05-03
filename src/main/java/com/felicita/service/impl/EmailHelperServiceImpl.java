package com.felicita.service.impl;

import com.felicita.model.dto.DestinationActivityResponseDto;
import com.felicita.model.dto.DestinationCategoryDetailsDto;
import com.felicita.model.dto.DestinationResponseDto;
import com.felicita.model.dto.DestionationImageResponseDto;
import com.felicita.model.other.ActivityUpdateDetails;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.other.FieldUpdate;
import com.felicita.model.request.*;
import com.felicita.security.model.User;
import com.felicita.service.EmailHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EmailHelperServiceImpl implements EmailHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailHelperServiceImpl.class);

    private static final String COMPANY_NAME = "Felicita Trips";
    private static final String COMPANY_EMAIL = "felicitatrips@gmail.com";
    private static final String COMPANY_PHONE = "+94701774488";
    private static final String WEBSITE_URL = "https://felicitatrips.com";

    // Color constants
    private static final class Colors {
        static final String ACCENT_DARK = "#0D4E4A";
        static final String BRAND_COLOR = "#14B8A6";
        static final String BRAND_LIGHT = "#F0FDFA";
        static final String BRAND_BORDER = "#CCFBF1";
        static final String BRAND_TEXT = "#0F766E";

        static final String ALERT_BG = "#FFFBEB";
        static final String ALERT_BORDER = "#FDE68A";
        static final String ALERT_TEXT = "#78350F";
        static final String ALERT_TITLE_COLOR = "#92400E";

        static final String UPDATE_BG = "#EFF6FF";
        static final String UPDATE_BORDER = "#BFDBFE";
        static final String UPDATE_TEXT = "#1E40AF";

        static final String REMOVE_BG = "#FEF2F2";
        static final String REMOVE_BORDER = "#FEE2E2";
        static final String REMOVE_TEXT = "#991B1B";

        static final String ADD_BG = "#F0FDF4";
        static final String ADD_BORDER = "#DCFCE7";
        static final String ADD_TEXT = "#166534";

        static final String MODIFY_BG = "#FFFBEB";
        static final String MODIFY_BORDER = "#FDE68A";
        static final String MODIFY_TEXT = "#92400E";

        static final String SUCCESS_BG = "#F0FDF4";
        static final String SUCCESS_BORDER = "#86EFAC";
        static final String SUCCESS_TEXT = "#166534";

        static final String INFO_BG = "#EFF6FF";
        static final String INFO_BORDER = "#BFDBFE";
        static final String INFO_TEXT = "#1E40AF";
    }

    // ==================== TOUR BOOKING EMAILS ====================

    @Override
    public String buildAdminTourBookingSubject(TourBookingInquiryRequest request) {
        return String.format("New Tour Booking Inquiry - %s (Tour ID: %d)",
                request.getName(), request.getTourId());
    }

    @Override
    public String buildAdminTourBookingBody(TourBookingInquiryRequest request, Long inquiryId) {
        EmailBuilder builder = new EmailBuilder()
                .withSubject("New Booking", "Tour Booking Inquiry", "A new booking inquiry requires your attention")
                .withInquiryId(inquiryId)
                .addSection("Customer Details", buildCustomerDetailsGrid(request))
                .addSection("Tour & Package", buildTourPackageDetails(request))
                .addAlert("Action Required", "Please contact the customer as soon as possible to confirm their booking.");

        return builder.build();
    }

    @Override
    public String buildCustomerTourBookingSubject() {
        return "Your Tour Inquiry Has Been Received";
    }

    @Override
    public String buildCustomerTourBookingBody(TourBookingInquiryRequest request) {
        EmailBuilder builder = new EmailBuilder()
                .withSubject("Booking Received", "We've Got Your Request", "Your tour booking inquiry is confirmed")
                .withGreeting(request.getName())
                .withIntro("Thank you for your interest in our tours. We've successfully received your booking inquiry " +
                        "and our team will review your request and get back to you shortly.")
                .addSummaryCard(buildTourBookingSummary(request))
                .addSteps(new String[][]{
                        {"1", "Team review", "Our specialists will review your inquiry and tour availability."},
                        {"2", "Personalised proposal", "We'll prepare a tailored quote and itinerary for your selected package."},
                        {"3", "We'll be in touch", "Expect a detailed response from us within 24 hours."}
                })
                .addCta(WEBSITE_URL, "Explore More Tours");

        return builder.build();
    }

    // ==================== GENERAL INQUIRY EMAILS ====================

    @Override
    public String buildInquirySubject(CreateInquiryRequest request) {
        return String.format("New Inquiry from %s - %s", request.getName(), request.getPreferredDestination());
    }

    @Override
    public String buildInquiryEmailBody(CreateInquiryRequest request) {
        EmailBuilder builder = new EmailBuilder()
                .withSubject("New Inquiry", "Travel Inquiry Received", "A new travel inquiry requires your attention")
                .addSection("Customer Details", buildCustomerDetailsGrid(request))
                .addSection("Travel Details", buildTravelDetailsGrid(request))
                .addMessageIfPresent(request.getMessage())
                .addAlert("Action Required", "Please respond to this customer within 24 hours to maintain service standards.");

        return builder.build();
    }

    @Override
    public String buildCustomerInquirySubject(CreateInquiryRequest request) {
        return String.format("We've received your inquiry to %s - Felicita Trips", request.getPreferredDestination());
    }

    @Override
    public String buildCustomerInquiryEmailBody(CreateInquiryRequest request) {
        EmailBuilder builder = new EmailBuilder()
                .withSubject("Inquiry Received!", "Inquiry Received!", "We've received your travel inquiry and will respond shortly")
                .withGreeting(request.getName())
                .withIntro(String.format("Thank you for reaching out to <strong>Felicita Trips</strong>. " +
                                "We have successfully received your travel inquiry to <strong>%s</strong>. " +
                                "Our team is already reviewing your requirements to craft the perfect Sri Lanka experience for you.",
                        escapeHtml(request.getPreferredDestination())))
                .addSummaryCard(buildInquirySummary(request))
                .addSteps(new String[][]{
                        {"1", "Inquiry Review", "Our travel specialists will carefully review your travel preferences and requirements."},
                        {"2", "Personalised Proposal", "We'll prepare a tailored itinerary and quote based on your selected destination and travel dates."},
                        {"3", "Response Within 24 Hours", "You'll hear from us with a detailed proposal and next steps for your Sri Lanka adventure."}
                })
                .addContactInfo()
                .addCta(WEBSITE_URL + "/sri-lankan-tours", "Explore Our Tours");

        return builder.build();
    }

    // ==================== CHATBOT INQUIRY EMAILS ====================

    @Override
    public String buildChatBotInquirySubject(ChatBotRequest request) {
        String flowTypeDisplay = getFlowTypeDisplayName(request.getPreferences().getFlowType());
        return String.format("[Felicita Trips] New %s Inquiry from %s", flowTypeDisplay, request.getName());
    }

    @Override
    public String buildChatBotInquiryEmailBody(ChatBotRequest request) {
        EmailBuilder builder = new EmailBuilder()
                .withSubject("New Inquiry", "Travel Inquiry Received", "A new customer inquiry requires your attention")
                .addSection("Customer Details", buildChatBotCustomerDetails(request))
                .addSection("Preferences", buildPreferenceSections(request.getPreferences().getSelections(), request.getPreferences().getFlowType()))
                .addAlert("Action Required", "Please respond to this customer within 24 hours to maintain service standards.");

        return builder.build();
    }

    @Override
    public String buildCustomerConfirmationEmailBody(ChatBotRequest request) {
        EmailBuilder builder = new EmailBuilder()
                .withSubject("Inquiry Requested", "Inquiry Requested", "We've received your request and are on it")
                .withGreeting(request.getName())
                .withIntro("Thank you for reaching out to <strong>Felicita Trips</strong>. " +
                        "Your travel inquiry has been received and our team is already reviewing your preferences " +
                        "to craft the perfect Sri Lanka experience for you.")
                .addSteps(new String[][]{
                        {"1", "Specialist review", "A dedicated travel specialist will carefully review your preferences and requirements."},
                        {"2", "Personalised recommendations", "We'll prepare curated tour options tailored specifically to your selections."},
                        {"3", "Response within 24 hours", "You'll hear from us with a detailed proposal and next steps."}
                })
                .addContactInfo()
                .addCta(WEBSITE_URL + "/sri-lankan-tours", "Explore Our Tours");

        return builder.build();
    }

    // ==================== DESTINATION MANAGEMENT EMAILS ====================

    @Override
    public String buildDestinationCreateSuccessfullSubject(DestinationInsertRequest request, User loggedUser) {
        return String.format("[Felicita Trips] New Destination Created: %s by %s %s",
                escapeHtml(request.getName()), escapeHtml(loggedUser.getFirstName()), escapeHtml(loggedUser.getLastName()));
    }

    @Override
    public String buildDestinationCreateSuccessfullBody(DestinationInsertRequest request,
                                                        List<String> destinationCategories, User loggedUser) {
        EmailBuilder builder = new EmailBuilder()
                .withSubject("Destination Created", "New Destination Added", "A new destination has been successfully added to the system")
                .addSuccessAlert("Creation Successful", "The destination has been successfully created and is now available in the system.")
                .addSection("Destination Details", buildDestinationDetailsGrid(request))
                .addLocationInfo(request)
                .addCategoriesIfPresent(destinationCategories)
                .addDescriptionIfPresent(request.getDescription())
                .addExtraPriceIfPresent(request.getExtraPrice(), request.getExtraPriceNote())
                .addImagesIfPresent(request.getImages())
                .addNotificationInfo(loggedUser, "Creation");

        return builder.build();
    }

    @Override
    public String buildDestinationUpdateSuccessfullSubject(User loggedUser) {
        return String.format("[Felicita Trips] Destination Update Completed by %s %s",
                escapeHtml(loggedUser.getFirstName()), escapeHtml(loggedUser.getLastName()));
    }

    @Override
    public String buildDestinationUpdateSuccessfullBody(User loggedUser, Long destinationId,
                                                        DestinationUpdateComparisonResult comparisonResult) {
        DestinationUpdateBuilder builder = new DestinationUpdateBuilder()
                .withDestinationId(destinationId)
                .withComparisonResult(comparisonResult)
                .withUser(loggedUser);

        return builder.build();
    }

    @Override
    public String buildDestinationTerminateSuccessfullSubject(User loggedUser, DestinationResponseDto destinationDetailsById) {
        return String.format("[Felicita Trips] Destination Terminated: %s by %s %s",
                escapeHtml(destinationDetailsById.getDestinationName()),
                escapeHtml(loggedUser.getFirstName()),
                escapeHtml(loggedUser.getLastName()));
    }

    @Override
    public String buildDestinationTerminateSuccessfullBody(User loggedUser, DestinationResponseDto destinationDetailsById) {
        EmailBuilder builder = new EmailBuilder()
                .withTerminationStyle(true)
                .withSubject("Destination Terminated", "Destination Termination Notice",
                        "A destination has been terminated and removed from the system")
                .addAlert("⚠️ Action Required", "This destination has been terminated and is no longer active in the system.")
                .addSection("Terminated Destination Details", buildTerminatedDestinationDetails(destinationDetailsById))
                .addNotificationInfo(loggedUser, "Termination");

        return builder.build();
    }

    // Helper method for terminated destination details
    private String buildTerminatedDestinationDetails(DestinationResponseDto destination) {
        StringBuilder sb = new StringBuilder();

        // Basic details grid
        sb.append("<div class='meta-grid'>\n");
        sb.append(metaItem("Destination ID", String.valueOf(destination.getDestinationId())));
        sb.append(metaItem("Destination Name", escapeHtml(destination.getDestinationName())));
        sb.append(metaItem("Location", escapeHtml(destination.getLocation() != null ? destination.getLocation() : "N/A")));
        sb.append(metaItem("Status", getTerminatedStatusBadge(destination.getStatusName())));
        sb.append("</div>\n");

        // Coordinates if available
        if (destination.getLatitude() != null && destination.getLongitude() != null) {
            sb.append("<div class='location-box termination-location'>\n");
            sb.append("<div class='section-header' style='margin-bottom:12px;'>\n");
            sb.append("<div class='section-icon'>&#128205;</div>\n");
            sb.append("<span class='section-title'>Location Coordinates</span>\n");
            sb.append("</div>\n");
            sb.append(String.format("""
                <div class='location-row'>
                    <span class='location-label'>Latitude:</span>
                    <span class='location-value'>%.6f</span>
                </div>
                <div class='location-row'>
                    <span class='location-label'>Longitude:</span>
                    <span class='location-value'>%.6f</span>
                </div>
                """, destination.getLatitude(), destination.getLongitude()));
            sb.append("</div>\n");
        }

        // Description
        if (destination.getDestinationDescription() != null && !destination.getDestinationDescription().trim().isEmpty()) {
            sb.append("<div class='description-box termination-description'>\n");
            sb.append("<div class='description-label'>Destination Description</div>\n");
            sb.append("<div class='description-content'>").append(escapeHtml(destination.getDestinationDescription())).append("</div>\n");
            sb.append("</div>\n");
        }

        // Categories
        if (destination.getDestinationCategoryDetailsDtos() != null && !destination.getDestinationCategoryDetailsDtos().isEmpty()) {
            sb.append("<div class='categories-box termination-categories'>\n");
            sb.append("<div class='categories-label'>Associated Categories (").append(destination.getDestinationCategoryDetailsDtos().size()).append(")</div>\n");
            sb.append("<div class='tags'>\n");
            for (DestinationCategoryDetailsDto category : destination.getDestinationCategoryDetailsDtos()) {
                String categoryDisplay = category.getIsPrimary() != null && category.getIsPrimary()
                        ? escapeHtml(category.getName()) + " <span style='font-size:9px;'>(Primary)</span>"
                        : escapeHtml(category.getName());
                sb.append(String.format("<span class='tag termination-tag'>%s</span>\n", categoryDisplay));
            }
            sb.append("</div>\n");
            sb.append("</div>\n");
        }

        // Termination warning box
        sb.append("<div class='termination-warning'>\n");
        sb.append("<div class='warning-icon'>&#9888;</div>\n");
        sb.append("<div class='warning-content'>\n");
        sb.append("<div class='warning-title'>Destination Terminated</div>\n");
        sb.append("<div class='warning-message'>This destination has been permanently terminated and is no longer available for bookings or new inquiries.</div>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    private String getTerminatedStatusBadge(String status) {
        return "<span style='display:inline-block; background:#FEE2E2; color:#991B1B; padding:2px 8px; border-radius:4px; font-size:11px; font-weight:600;'>TERMINATED</span>";
    }

    // ==================== HELPER METHODS ====================

    private String getFlowTypeDisplayName(String flowType) {
        if ("tours".equals(flowType)) return "Tour Package";
        if ("activities".equals(flowType)) return "Activities & Experiences";
        if ("destinations".equals(flowType)) return "Destinations";
        return "General";
    }

    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String formatFieldName(String fieldName) {
        if (fieldName == null) return "";

        return switch (fieldName.toLowerCase()) {
            case "name" -> "Destination Name";
            case "description" -> "Description";
            case "status" -> "Status";
            case "location" -> "Location";
            case "latitude" -> "Latitude";
            case "longitude" -> "Longitude";
            case "extraprice" -> "Extra Price";
            case "extrapricenote" -> "Extra Price Note";
            default -> {
                String formatted = fieldName.replaceAll("([a-z])([A-Z])", "$1 $2");
                yield formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
            }
        };
    }

    private String formatValueForDisplay(Object value) {
        if (value == null) return "<em>Not set</em>";
        if (value instanceof Double d) {
            return d == 0.0 ? "0.00" : String.format("LKR %,.2f", d);
        }
        if (value instanceof Boolean b) return b ? "Yes" : "No";
        String strValue = value.toString();
        return strValue.trim().isEmpty() ? "<em>Empty</em>" : escapeHtml(strValue);
    }

    // ==================== HTML BUILDING COMPONENTS ====================

    private String metaItem(String label, String value) {
        return String.format("""
                <div class='meta-item'>
                    <div class='meta-label'>%s</div>
                    <div class='meta-value'>%s</div>
                </div>
                """, label, value);
    }

    private String summaryRow(String icon, String label, String value) {
        return String.format("""
                <div class='summary-row'>
                    <div class='s-icon'>%s</div>
                    <div>
                        <div class='s-lbl'>%s</div>
                        <div class='s-val'>%s</div>
                    </div>
                </div>
                """, icon, label, value);
    }

    private String buildStep(String number, String title, String description) {
        return String.format("""
                <div class='step'>
                    <div class='step-num'>%s</div>
                    <div>
                        <div class='step-title'>%s</div>
                        <div class='step-desc'>%s</div>
                    </div>
                </div>
                """, number, escapeHtml(title), escapeHtml(description));
    }

    // ==================== INNER BUILDER CLASSES ====================

    /**
     * Main email builder for standard emails
     */
    private class EmailBuilder {
        private final StringBuilder html = new StringBuilder();
        private String badgeText = "";
        private String title = "";
        private String subtitle = "";
        private Long inquiryId;
        private String greetingName;
        private String introText;
        private final StringBuilder sections = new StringBuilder();
        private final StringBuilder steps = new StringBuilder();
        private String alertTitle;
        private String alertMessage;
        private String successTitle;
        private String successMessage;
        private String summaryCard;
        private String contactInfo;
        private String ctaUrl;
        private String ctaText;
        private String notificationInfo;
        private boolean isTerminationEmail = false;

        EmailBuilder withSubject(String badgeText, String title, String subtitle) {
            this.badgeText = badgeText;
            this.title = title;
            this.subtitle = subtitle;
            return this;
        }

        EmailBuilder withInquiryId(Long id) {
            this.inquiryId = id;
            return this;
        }

        EmailBuilder withGreeting(String name) {
            this.greetingName = name;
            return this;
        }

        EmailBuilder withIntro(String intro) {
            this.introText = intro;
            return this;
        }

        EmailBuilder addSection(String title, String content) {
            sections.append(String.format("""
                    <div class='section-header'>
                        <div class='section-icon'>%s</div>
                        <span class='section-title'>%s</span>
                    </div>
                    %s
                    """, getSectionIcon(title), title, content));
            return this;
        }

        EmailBuilder addSummaryCard(String card) {
            this.summaryCard = card;
            return this;
        }

        EmailBuilder addSteps(String[][] stepsArray) {
            steps.append("<div class='steps-label'>What happens next</div>");
            for (int i = 0; i < stepsArray.length; i++) {
                steps.append(buildStep(stepsArray[i][0], stepsArray[i][1], stepsArray[i][2]));
                if (i < stepsArray.length - 1) {
                    steps.append("<div class='step-connector'></div>");
                }
            }
            return this;
        }

        EmailBuilder addAlert(String title, String message) {
            this.alertTitle = title;
            this.alertMessage = message;
            return this;
        }

        EmailBuilder addSuccessAlert(String title, String message) {
            this.successTitle = title;
            this.successMessage = message;
            return this;
        }

        EmailBuilder addContactInfo() {
            this.contactInfo = buildContactInfo();
            return this;
        }

        EmailBuilder addCta(String url, String text) {
            this.ctaUrl = url;
            this.ctaText = text;
            return this;
        }

        EmailBuilder addMessageIfPresent(String message) {
            if (message != null && !message.trim().isEmpty()) {
                sections.append(String.format("""
                        <div class='message-box'>
                            <div class='message-label'>Customer Message</div>
                            <div class='message-content'>%s</div>
                        </div>
                        """, escapeHtml(message)));
            }
            return this;
        }

        EmailBuilder withTerminationStyle(boolean isTermination) {
            this.isTerminationEmail = isTermination;
            return this;
        }

        EmailBuilder addLocationInfo(DestinationInsertRequest request) {
            if (request.getLocation() != null || (request.getLatitude() != null && request.getLongitude() != null)) {
                StringBuilder locationHtml = new StringBuilder();
                locationHtml.append("<div class='location-box'>");
                locationHtml.append("<div class='section-header' style='margin-bottom:12px;'>");
                locationHtml.append("<div class='section-icon'>&#128205;</div>");
                locationHtml.append("<span class='section-title'>Location Information</span>");
                locationHtml.append("</div>");

                if (request.getLocation() != null) {
                    locationHtml.append(String.format("""
                            <div class='location-row'>
                                <span class='location-label'>Address/Location:</span>
                                <span class='location-value'>%s</span>
                            </div>
                            """, escapeHtml(request.getLocation())));
                }

                if (request.getLatitude() != null && request.getLongitude() != null) {
                    locationHtml.append(String.format("""
                            <div class='location-row'>
                                <span class='location-label'>Coordinates:</span>
                                <span class='location-value'>%.6f, %.6f</span>
                            </div>
                            """, request.getLatitude(), request.getLongitude()));
                }
                locationHtml.append("</div>");
                sections.append(locationHtml);
            }
            return this;
        }

        EmailBuilder addCategoriesIfPresent(List<String> categories) {
            if (categories != null && !categories.isEmpty()) {
                StringBuilder categoriesHtml = new StringBuilder();
                categoriesHtml.append("<div class='categories-box'>");
                categoriesHtml.append("<div class='categories-label'>Destination Categories</div>");
                categoriesHtml.append("<div class='tags'>");
                for (String category : categories) {
                    categoriesHtml.append(String.format("<span class='tag'>%s</span>", escapeHtml(category)));
                }
                categoriesHtml.append("</div></div>");
                sections.append(categoriesHtml);
            }
            return this;
        }

        EmailBuilder addDescriptionIfPresent(String description) {
            if (description != null && !description.trim().isEmpty()) {
                sections.append(String.format("""
                        <div class='description-box'>
                            <div class='description-label'>Description</div>
                            <div class='description-content'>%s</div>
                        </div>
                        """, escapeHtml(description)));
            }
            return this;
        }

        EmailBuilder addExtraPriceIfPresent(Double price, String note) {
            if (price != null && price > 0) {
                sections.append(String.format("""
                        <div class='price-box'>
                            <div class='price-label'>Extra Charges</div>
                            <div class='price-amount'>LKR %,.2f</div>
                            %s
                        </div>
                        """, price, note != null ? String.format("<div class='price-note'>%s</div>", escapeHtml(note)) : ""));
            }
            return this;
        }

        EmailBuilder addImagesIfPresent(List<DestinationInsertRequest.Image> images) {
            if (images != null && !images.isEmpty()) {
                StringBuilder imagesHtml = new StringBuilder();
                imagesHtml.append("<div class='images-box'>");
                imagesHtml.append(String.format("<div class='images-label'>Uploaded Images (%d)</div>", images.size()));
                imagesHtml.append("<div class='images-grid'>");
                for (DestinationInsertRequest.Image image : images) {
                    imagesHtml.append(String.format("""
                            <div class='image-item'>
                                <div class='image-placeholder'>&#128444;</div>
                                <div class='image-name'>%s</div>
                            </div>
                            """, escapeHtml(image.getName())));
                }
                imagesHtml.append("</div></div>");
                sections.append(imagesHtml);
            }
            return this;
        }

        EmailBuilder addNotificationInfo(User user, String actionType) {
            this.notificationInfo = buildNotificationInfo(user, actionType);
            return this;
        }

        String build() {
            return buildHtmlTemplate();
        }

        private String buildHtmlTemplate() {
            StringBuilder body = new StringBuilder();

            body.append("<!DOCTYPE html>\n<html>\n<head>\n");
            body.append("<meta charset='UTF-8'>\n");
            body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
            body.append("<style>\n");
            body.append(getCommonStyles());
            if (isTerminationEmail) {
                body.append(getTerminationSpecificStyles());
            }
            body.append("</style>\n</head>\n<body>\n");
            body.append("<div class='wrapper'><div class='card'>\n");

            // Header
            body.append(buildHeader(badgeText, title, subtitle));

            // Body
            body.append("<div class='body'>\n");

            if (inquiryId != null) {
                body.append(buildInquiryIdPill(inquiryId));
            }

            if (greetingName != null) {
                body.append(String.format("<p class='greeting'>Dear <strong>%s</strong>,</p>\n", escapeHtml(greetingName)));
            }

            if (introText != null) {
                body.append(String.format("<p class='intro'>%s</p>\n", introText));
            }

            if (successTitle != null) {
                body.append(buildSuccessAlert(successTitle, successMessage));
            }

            body.append(sections);

            if (summaryCard != null) {
                body.append(summaryCard);
            }

            if (steps.length() > 0) {
                body.append(steps);
            }

            if (contactInfo != null) {
                body.append(contactInfo);
            }

            if (alertTitle != null) {
                body.append(buildAlert(alertTitle, alertMessage));
            }

            if (notificationInfo != null) {
                body.append(notificationInfo);
            }

            if (ctaUrl != null) {
                body.append(buildCta(ctaUrl, ctaText));
            }

            body.append("</div>\n");

            // Footer
            body.append(buildFooter());

            body.append("</div></div>\n</body>\n</html>");
            return body.toString();
        }

        private String getSectionIcon(String title) {
            return switch (title) {
                case "Customer Details" -> "&#128100;";
                case "Travel Details", "Tour & Package" -> "&#127956;";
                case "Preferences", "Terminated Destination Details" -> "&#128203;";
                default -> "&#128203;";
            };
        }
    }

    /**
     * Builder for destination update emails (more complex structure)
     */
    private class DestinationUpdateBuilder {
        private Long destinationId;
        private DestinationUpdateComparisonResult comparisonResult;
        private User user;
        private final StringBuilder body = new StringBuilder();

        DestinationUpdateBuilder withDestinationId(Long id) {
            this.destinationId = id;
            return this;
        }

        DestinationUpdateBuilder withComparisonResult(DestinationUpdateComparisonResult result) {
            this.comparisonResult = result;
            return this;
        }

        DestinationUpdateBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        String build() {
            buildHtmlStart();
            buildHeader("Destination Updated", "Destination Update Summary", "A destination has been successfully updated in the system");
            buildBody();
            buildFooter();
            buildHtmlEnd();
            return body.toString();
        }

        private void buildHtmlStart() {
            body.append("<!DOCTYPE html>\n<html>\n<head>\n");
            body.append("<meta charset='UTF-8'>\n");
            body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
            body.append("<style>\n");
            body.append(getCommonStyles());
            body.append(getUpdateSpecificStyles());
            body.append("</style>\n</head>\n<body>\n");
            body.append("<div class='wrapper'><div class='card'>\n");
        }

        private void buildHtmlEnd() {
            body.append("</div></div>\n</body>\n</html>");
        }

        private void buildHeader(String badgeText, String title, String subtitle) {
            body.append(String.format("""
                    <div class='header'>
                        <div class='header-accent'></div>
                        <div class='logo'>
                            <div class='logo-mark'>&#9992;</div>
                            <span class='logo-name'>%s</span>
                        </div>
                        <div class='badge'><span class='badge-dot'></span> %s</div>
                        <h1>%s</h1>
                        <p>%s</p>
                    </div>
                    """, COMPANY_NAME, badgeText, title, subtitle));
        }

        private void buildBody() {
            body.append("<div class='body'>\n");

            // Summary Card
            body.append(String.format("""
                    <div class='summary-card'>
                        <div class='summary-title'>&#128195; Update Summary</div>
                        <div class='summary-stats'>
                            <div class='stat'><strong>Destination ID:</strong> #%d</div>
                            <div class='stat'><strong>Total Updates:</strong> %d</div>
                        </div>
                    </div>
                    """, destinationId, getTotalUpdateCount(comparisonResult)));

            // Field Updates
            if (comparisonResult.getUpdatedFields() != null && !comparisonResult.getUpdatedFields().isEmpty()) {
                buildFieldUpdatesSection();
            }

            // Category Changes
            buildCategoryChangesSection();

            // Image Changes
            buildImageChangesSection();

            // Activity Changes
            buildActivityChangesSection();

            // Notification Info
            body.append(buildNotificationInfo(user, "Update"));

            body.append("</div>\n");
        }

        private void buildFieldUpdatesSection() {
            body.append("""
                    <div class='update-card'>
                        <div class='update-header'>
                            <div class='update-icon'>&#128221;</div>
                            <span class='update-title'>Field Changes</span>
                        </div>
                    """);

            for (FieldUpdate field : comparisonResult.getUpdatedFields()) {
                body.append(String.format("""
                                <div class='field-row'>
                                    <div class='field-name'>%s</div>
                                    <div class='field-values'>
                                        <span class='old-value'>%s</span>
                                        <span class='arrow'>→</span>
                                        <span class='new-value'>%s</span>
                                    </div>
                                </div>
                                """, formatFieldName(field.getFieldName()),
                        formatValueForDisplay(field.getOldValue()),
                        formatValueForDisplay(field.getNewValue())));
            }
            body.append("</div>\n");
        }

        private void buildCategoryChangesSection() {
            boolean hasCategoryChanges = (comparisonResult.getRemovedCategories() != null && !comparisonResult.getRemovedCategories().isEmpty()) ||
                    (comparisonResult.getAddedCategoryIds() != null && !comparisonResult.getAddedCategoryIds().isEmpty());

            if (hasCategoryChanges) {
                body.append("""
                        <div class='list-box'>
                            <div class='list-header'>
                                <div class='list-icon' style='background:#EFF6FF;'>&#127968;</div>
                                <span class='list-title' style='color:#1E40AF;'>Category Changes</span>
                            </div>
                        """);

                if (comparisonResult.getRemovedCategories() != null) {
                    for (DestinationCategoryDetailsDto category : comparisonResult.getRemovedCategories()) {
                        body.append(String.format("""
                                <div class='list-item'>
                                    <div class='list-bullet' style='background:#EF4444;'></div>
                                    <span style='color:#991B1B;'>Removed: </span>
                                    <span>%s</span>
                                </div>
                                """, escapeHtml(category.getName())));
                    }
                }

                if (comparisonResult.getAddedCategoryIds() != null) {
                    for (Long categoryId : comparisonResult.getAddedCategoryIds()) {
                        body.append(String.format("""
                                <div class='list-item'>
                                    <div class='list-bullet' style='background:#22C55E;'></div>
                                    <span style='color:#166534;'>Added: </span>
                                    <span>Category ID: %d</span>
                                </div>
                                """, categoryId));
                    }
                }
                body.append("</div>\n");
            }
        }

        private void buildImageChangesSection() {
            boolean hasImageChanges = (comparisonResult.getRemovedImages() != null && !comparisonResult.getRemovedImages().isEmpty()) ||
                    (comparisonResult.getAddedImages() != null && !comparisonResult.getAddedImages().isEmpty());

            if (hasImageChanges) {
                body.append("""
                        <div class='list-box'>
                            <div class='list-header'>
                                <div class='list-icon' style='background:#F0FDF4;'>&#128444;</div>
                                <span class='list-title' style='color:#166534;'>Image Changes</span>
                            </div>
                        """);

                if (comparisonResult.getRemovedImages() != null) {
                    for (DestionationImageResponseDto image : comparisonResult.getRemovedImages()) {
                        body.append(String.format("""
                                <div class='list-item'>
                                    <div class='list-bullet' style='background:#EF4444;'></div>
                                    <span style='color:#991B1B;'>Removed: </span>
                                    <span>%s</span>
                                </div>
                                """, escapeHtml(image.getImageName())));
                    }
                }

                if (comparisonResult.getAddedImages() != null) {
                    for (DestinationInsertRequest.Image image : comparisonResult.getAddedImages()) {
                        body.append(String.format("""
                                <div class='list-item'>
                                    <div class='list-bullet' style='background:#22C55E;'></div>
                                    <span style='color:#166534;'>Added: </span>
                                    <span>%s</span>
                                </div>
                                """, escapeHtml(image.getName())));
                    }
                }
                body.append("</div>\n");
            }
        }

        private void buildActivityChangesSection() {
            boolean hasActivityChanges = (comparisonResult.getRemovedActivities() != null && !comparisonResult.getRemovedActivities().isEmpty()) ||
                    (comparisonResult.getAddedActivities() != null && !comparisonResult.getAddedActivities().isEmpty()) ||
                    (comparisonResult.getModifiedActivities() != null && !comparisonResult.getModifiedActivities().isEmpty());

            if (!hasActivityChanges) return;

            body.append("""
                    <div class='section-header'>
                        <div class='section-icon'>&#127914;</div>
                        <span class='section-title'>Activity Changes</span>
                    </div>
                    """);

            // Removed Activities
            if (comparisonResult.getRemovedActivities() != null && !comparisonResult.getRemovedActivities().isEmpty()) {
                body.append(String.format("""
                        <div class='list-box' style='background:%s;'>
                            <div class='list-header'>
                                <div class='list-icon' style='background:#FEE2E2;'>&#10006;</div>
                                <span class='list-title' style='color:%s;'>Removed Activities</span>
                            </div>
                        """, Colors.REMOVE_BG, Colors.REMOVE_TEXT));

                for (DestinationActivityResponseDto activity : comparisonResult.getRemovedActivities()) {
                    body.append(String.format("""
                            <div class='list-item'>
                                <div class='list-bullet' style='background:#EF4444;'></div>
                                <span>%s</span>
                            </div>
                            """, escapeHtml(activity.getActivityName())));
                }
                body.append("</div>\n");
            }

            // Added Activities
            if (comparisonResult.getAddedActivities() != null && !comparisonResult.getAddedActivities().isEmpty()) {
                body.append(String.format("""
                        <div class='list-box' style='background:%s;'>
                            <div class='list-header'>
                                <div class='list-icon' style='background:#DCFCE7;'>&#10003;</div>
                                <span class='list-title' style='color:%s;'>Added Activities</span>
                            </div>
                        """, Colors.ADD_BG, Colors.ADD_TEXT));

                for (DestinationUpdateRequest.Activity activity : comparisonResult.getAddedActivities()) {
                    body.append(String.format("""
                            <div class='list-item'>
                                <div class='list-bullet' style='background:#22C55E;'></div>
                                <span>%s</span>
                            </div>
                            """, escapeHtml(activity.getName())));
                }
                body.append("</div>\n");
            }

            // Modified Activities
            if (comparisonResult.getModifiedActivities() != null && !comparisonResult.getModifiedActivities().isEmpty()) {
                for (ActivityUpdateDetails activity : comparisonResult.getModifiedActivities()) {
                    buildModifiedActivityCard(activity);
                }
            }
        }

        private void buildModifiedActivityCard(ActivityUpdateDetails activity) {
            body.append(String.format("""
                    <div class='activity-card'>
                        <div class='activity-name'>✏️ %s</div>
                    """, escapeHtml(activity.getActivityName())));

            if (activity.getUpdatedFields() != null) {
                for (FieldUpdate field : activity.getUpdatedFields()) {
                    body.append(String.format("""
                                    <div class='sub-field'>
                                        <span class='sub-field-name'>%s:</span>
                                        <span class='old-value' style='margin-right:8px;'>%s</span>
                                        <span class='arrow'>→</span>
                                        <span class='new-value' style='margin-left:8px;'>%s</span>
                                    </div>
                                    """, formatFieldName(field.getFieldName()),
                            formatValueForDisplay(field.getOldValue()),
                            formatValueForDisplay(field.getNewValue())));
                }
            }

            // Category changes within activity
            if ((activity.getAddedCategoryIds() != null && !activity.getAddedCategoryIds().isEmpty()) ||
                    (activity.getRemovedCategoryIds() != null && !activity.getRemovedCategoryIds().isEmpty())) {
                body.append("<div class='sub-field'><span class='sub-field-name'>Categories:</span>");
                if (activity.getRemovedCategoryIds() != null && !activity.getRemovedCategoryIds().isEmpty()) {
                    body.append(String.format("<span class='old-value'>Removed: %d</span>", activity.getRemovedCategoryIds().size()));
                }
                if (activity.getAddedCategoryIds() != null && !activity.getAddedCategoryIds().isEmpty()) {
                    body.append(String.format("<span class='new-value'>Added: %d</span>", activity.getAddedCategoryIds().size()));
                }
                body.append("</div>\n");
            }

            // Image changes within activity
            if (activity.getAddedImages() != null && !activity.getAddedImages().isEmpty()) {
                body.append(String.format("""
                        <div class='sub-field'>
                            <span class='sub-field-name'>Added Images:</span>
                            <span class='new-value'>+%d new image(s)</span>
                        </div>
                        """, activity.getAddedImages().size()));
            }

            body.append("</div>\n");
        }

        private void buildFooter() {
            body.append(String.format("""
                    <div class='footer'>
                        <div class='footer-brand'><strong>%s</strong>Premium Sri Lanka Travel Experiences</div>
                        <div class='footer-note'>This is an automated notification. Please review the changes made to the destination.</div>
                    </div>
                    """, COMPANY_NAME));
        }
    }

    // ==================== STYLES ====================

    private String getCommonStyles() {
        return
                "* { box-sizing: border-box; margin: 0; padding: 0; }\n" +
                        "body { font-family: Arial, sans-serif; background: #F4F4F0; color: #1A1A1A; }\n" +
                        ".wrapper { padding: 40px 20px; background: #F4F4F0; }\n" +
                        ".card { max-width: 760px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; border: 1px solid #E5E5E0; }\n" +
                        "\n" +
                        "/* Header styles */\n" +
                        ".header { background: " + Colors.ACCENT_DARK + "; padding: 40px 36px 32px; position: relative; }\n" +
                        ".header-accent { position: absolute; top: 0; right: 0; width: 140px; height: 140px; background: rgba(255,255,255,0.04); border-radius: 0 0 0 140px; }\n" +
                        ".logo { display: flex; align-items: center; gap: 10px; margin-bottom: 28px; }\n" +
                        ".logo-mark { width: 32px; height: 32px; background: " + Colors.BRAND_COLOR + "; border-radius: 8px; display: flex; align-items: center; justify-content: center; }\n" +
                        ".logo-name { color: rgba(255,255,255,0.9); font-size: 12px; letter-spacing: 0.12em; text-transform: uppercase; font-weight: 700; }\n" +
                        ".badge { display: inline-flex; align-items: center; gap: 6px; background: rgba(20,184,166,0.18); border: 1px solid rgba(20,184,166,0.38); color: #5EEAD4; font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; padding: 5px 12px; border-radius: 20px; margin-bottom: 16px; }\n" +
                        ".badge-dot { display: inline-block; width: 6px; height: 6px; background: " + Colors.BRAND_COLOR + "; border-radius: 50%; }\n" +
                        ".header h1 { color: #ffffff; font-size: 26px; font-weight: 400; margin-bottom: 6px; font-family: Georgia, serif; letter-spacing: -0.02em; }\n" +
                        ".header p { color: rgba(255,255,255,0.5); font-size: 13px; }\n" +
                        "\n" +
                        "/* Body styles */\n" +
                        ".body { padding: 32px 36px; background: #ffffff; }\n" +
                        ".section-header { display: flex; align-items: center; gap: 10px; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #EBEBEB; }\n" +
                        ".section-icon { width: 28px; height: 28px; background: " + Colors.BRAND_LIGHT + "; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 13px; }\n" +
                        ".section-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #888; }\n" +
                        ".meta-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 20px; }\n" +
                        ".meta-item { background: #F8F8F6; border-radius: 8px; padding: 14px 16px; }\n" +
                        ".meta-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 4px; }\n" +
                        ".meta-value { font-size: 13px; color: #1A1A1A; font-weight: 500; }\n" +
                        ".divider { height: 1px; background: #EBEBEB; margin: 24px 0; }\n" +
                        ".greeting { font-size: 15px; color: #1A1A1A; margin-bottom: 12px; }\n" +
                        ".greeting strong { color: " + Colors.ACCENT_DARK + "; }\n" +
                        ".intro { font-size: 14px; color: #555; line-height: 1.7; margin-bottom: 28px; }\n" +
                        "\n" +
                        "/* Steps */\n" +
                        ".steps-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #999; margin-bottom: 14px; }\n" +
                        ".step { display: flex; align-items: flex-start; gap: 14px; margin-bottom: 14px; }\n" +
                        ".step-num { width: 26px; height: 26px; background: " + Colors.BRAND_LIGHT + "; border: 1px solid " + Colors.BRAND_BORDER + "; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 11px; font-weight: 700; color: " + Colors.BRAND_TEXT + "; flex-shrink: 0; margin-top: 1px; }\n" +
                        ".step-title { font-size: 13px; font-weight: 600; color: #1A1A1A; margin-bottom: 2px; }\n" +
                        ".step-desc { font-size: 12px; color: #777; line-height: 1.5; }\n" +
                        ".step-connector { width: 1px; height: 10px; background: " + Colors.BRAND_BORDER + "; margin: 2px 0 2px 12px; }\n" +
                        "\n" +
                        "/* Footer */\n" +
                        ".footer { background: #F8F8F6; padding: 20px 36px; display: flex; align-items: center; justify-content: space-between; border-top: 1px solid #EBEBEB; }\n" +
                        ".footer-brand { font-size: 12px; color: #888; }\n" +
                        ".footer-brand strong { color: #1A1A1A; font-weight: 700; display: block; margin-bottom: 2px; font-size: 13px; }\n" +
                        ".footer-note { font-size: 11px; color: #AAA; text-align: right; max-width: 180px; line-height: 1.5; }\n" +
                        "\n" +
                        "/* Alert */\n" +
                        ".alert { background: " + Colors.ALERT_BG + "; border: 1px solid " + Colors.ALERT_BORDER + "; border-radius: 8px; padding: 16px 20px; }\n" +
                        ".alert-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: " + Colors.ALERT_TITLE_COLOR + "; margin-bottom: 4px; }\n" +
                        ".alert-desc { font-size: 13px; color: " + Colors.ALERT_TEXT + "; }\n" +
                        "\n" +
                        "/* Contact */\n" +
                        ".contact-row { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }\n" +
                        ".contact-icon { width: 28px; height: 28px; background: #F8F8F6; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 14px; flex-shrink: 0; }\n" +
                        ".contact-text { font-size: 13px; color: #1A1A1A; }\n" +
                        ".contact-sub { font-size: 12px; color: #999; }\n" +
                        "\n" +
                        "/* CTA */\n" +
                        ".cta { text-align: center; margin-top: 28px; }\n" +
                        ".btn { display: inline-block; background: " + Colors.ACCENT_DARK + "; color: #ffffff; font-size: 13px; font-weight: 600; letter-spacing: 0.04em; padding: 12px 28px; border-radius: 6px; text-decoration: none; }\n" +
                        ".btn-sub { font-size: 11px; color: #AAA; margin-top: 8px; }\n";
    }

    private String getUpdateSpecificStyles() {
        return
                ".summary-card { background: " + Colors.UPDATE_BG + "; border: 1px solid " + Colors.UPDATE_BORDER + "; border-radius: 8px; padding: 16px 20px; margin-bottom: 24px; }\n" +
                        ".summary-title { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: " + Colors.UPDATE_TEXT + "; margin-bottom: 8px; }\n" +
                        ".summary-stats { display: flex; gap: 20px; flex-wrap: wrap; margin-top: 8px; }\n" +
                        ".stat { font-size: 13px; }\n" +
                        ".stat strong { color: " + Colors.ACCENT_DARK + "; }\n" +
                        "\n" +
                        ".update-card { background: #F8F8F6; border-radius: 8px; padding: 16px; margin-bottom: 24px; }\n" +
                        ".update-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }\n" +
                        ".update-icon { width: 24px; height: 24px; background: " + Colors.BRAND_LIGHT + "; border-radius: 4px; display: flex; align-items: center; justify-content: center; font-size: 12px; }\n" +
                        ".update-title { font-size: 11px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: #666; }\n" +
                        ".field-row { display: flex; align-items: baseline; padding: 8px 0; border-bottom: 1px solid #EBEBEB; }\n" +
                        ".field-row:last-child { border-bottom: none; }\n" +
                        ".field-name { width: 140px; font-size: 12px; font-weight: 600; color: #1A1A1A; }\n" +
                        ".field-values { flex: 1; display: flex; gap: 20px; }\n" +
                        ".old-value { font-size: 12px; color: #991B1B; background: #FEE2E2; padding: 2px 8px; border-radius: 4px; }\n" +
                        ".new-value { font-size: 12px; color: #166534; background: #DCFCE7; padding: 2px 8px; border-radius: 4px; }\n" +
                        ".arrow { color: #999; font-size: 12px; }\n" +
                        "\n" +
                        ".list-box { background: #F8F8F6; border-radius: 8px; padding: 16px; margin-bottom: 24px; }\n" +
                        ".list-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }\n" +
                        ".list-icon { width: 24px; height: 24px; border-radius: 4px; display: flex; align-items: center; justify-content: center; font-size: 12px; }\n" +
                        ".list-title { font-size: 11px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; }\n" +
                        ".list-item { display: flex; align-items: center; gap: 10px; padding: 6px 0; border-bottom: 1px solid #EBEBEB; font-size: 13px; }\n" +
                        ".list-item:last-child { border-bottom: none; }\n" +
                        ".list-bullet { width: 6px; height: 6px; border-radius: 50%; flex-shrink: 0; }\n" +
                        "\n" +
                        ".activity-card { background: #F8F8F6; border-radius: 8px; padding: 16px; margin-bottom: 16px; }\n" +
                        ".activity-name { font-size: 14px; font-weight: 600; color: " + Colors.ACCENT_DARK + "; margin-bottom: 12px; }\n" +
                        ".sub-field { margin-left: 20px; padding: 6px 0; border-bottom: 1px solid #EBEBEB; }\n" +
                        ".sub-field:last-child { border-bottom: none; }\n" +
                        ".sub-field-name { font-size: 11px; color: #666; min-width: 120px; display: inline-block; }\n" +
                        "\n" +
                        ".info-box { background: " + Colors.INFO_BG + "; border: 1px solid " + Colors.INFO_BORDER + "; border-radius: 8px; padding: 16px; margin-top: 24px; }\n" +
                        ".info-label { font-size: 10px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: " + Colors.INFO_TEXT + "; margin-bottom: 8px; }\n" +
                        ".info-row { display: flex; gap: 12px; margin-bottom: 6px; font-size: 13px; }\n" +
                        ".info-row strong { color: " + Colors.INFO_TEXT + "; min-width: 100px; }\n";
    }

    private String getTerminationSpecificStyles() {
        return
                "/* Termination specific styles */\n" +
                        ".termination-description { background: #FEF2F2; border: 1px solid #FECACA; }\n" +
                        ".termination-categories { background: #FEF2F2; border: 1px solid #FECACA; }\n" +
                        ".termination-location { background: #FEF2F2; border: 1px solid #FECACA; }\n" +
                        ".termination-tag { background: #FEE2E2; color: #991B1B; border-color: #FECACA; }\n" +
                        "\n" +
                        ".termination-warning { \n" +
                        "    background: #FEF2F2; \n" +
                        "    border: 2px solid #DC2626; \n" +
                        "    border-radius: 8px; \n" +
                        "    padding: 20px; \n" +
                        "    margin-top: 24px;\n" +
                        "    display: flex;\n" +
                        "    align-items: center;\n" +
                        "    gap: 16px;\n" +
                        "}\n" +
                        ".warning-icon {\n" +
                        "    font-size: 32px;\n" +
                        "    flex-shrink: 0;\n" +
                        "}\n" +
                        ".warning-content {\n" +
                        "    flex: 1;\n" +
                        "}\n" +
                        ".warning-title {\n" +
                        "    font-size: 14px;\n" +
                        "    font-weight: 700;\n" +
                        "    color: #991B1B;\n" +
                        "    margin-bottom: 4px;\n" +
                        "}\n" +
                        ".warning-message {\n" +
                        "    font-size: 13px;\n" +
                        "    color: #7F1D1D;\n" +
                        "    line-height: 1.5;\n" +
                        "}\n";
    }

    // ==================== DATA BUILDING METHODS ====================

    private String buildCustomerDetailsGrid(TourBookingInquiryRequest request) {
        return String.format("""
                        <div class='meta-grid'>
                            %s
                            %s
                            %s
                            %s
                        </div>
                        """,
                metaItem("Full Name", escapeHtml(request.getName())),
                metaItem("Email Address", escapeHtml(request.getEmail())),
                metaItem("Contact Number", escapeHtml(request.getContactNumber())),
                metaItem("Country", escapeHtml(request.getCountry())));
    }

    private String buildTourPackageDetails(TourBookingInquiryRequest request) {
        return String.format("""
                        <div class='meta-grid' style='margin-bottom:16px;'>
                            %s
                            %s
                        </div>
                        <div class='pkg-box'>
                            <div class='pkg-icon'>&#127956;</div>
                            <div>
                                <div class='pkg-label'>Selected Tour</div>
                                <div class='pkg-name'>%s</div>
                                <div class='pkg-sub'>Package: %s</div>
                            </div>
                        </div>
                        """,
                metaItem("Tour ID", String.valueOf(request.getTourId())),
                metaItem("Package ID", String.valueOf(request.getPackageId())),
                escapeHtml(request.getTourName()),
                escapeHtml(request.getPackageName()));
    }

    private String buildCustomerDetailsGrid(CreateInquiryRequest request) {
        return String.format("""
                        <div class='meta-grid'>
                            %s
                            %s
                            %s
                            %s
                            %s
                            %s
                        </div>
                        """,
                metaItem("Full Name", escapeHtml(request.getName())),
                metaItem("Email Address", escapeHtml(request.getEmail())),
                metaItem("Phone Number", escapeHtml(request.getPhoneNumber())),
                metaItem("Country", escapeHtml(request.getCountry())),
                metaItem("Preferred Contact", escapeHtml(request.getPreferredContactMethod())),
                metaItem("Inquiry Date", getCurrentDateTime()));
    }

    private String buildTravelDetailsGrid(CreateInquiryRequest request) {
        return String.format("""
                        <div class='meta-grid'>
                            %s
                            %s
                            %s
                            %s
                            %s
                        </div>
                        """,
                metaItem("Preferred Destination", escapeHtml(request.getPreferredDestination())),
                metaItem("Number of Adults", String.valueOf(request.getAdults())),
                metaItem("Number of Kids", String.valueOf(request.getKids())),
                metaItem("Arrival Date", escapeHtml(request.getArrivalDate())),
                metaItem("Departure Date", escapeHtml(request.getDepartureDate())));
    }

    private String buildChatBotCustomerDetails(ChatBotRequest request) {
        return String.format("""
                        <div class='meta-grid'>
                            %s
                            %s
                            %s
                            %s
                        </div>
                        """,
                metaItem("Full Name", escapeHtml(request.getName())),
                metaItem("Email Address", escapeHtml(request.getEmail())),
                metaItem("Phone Number", escapeHtml(request.getPhone())),
                metaItem("Inquiry Date", getCurrentDateTime()));
    }

    private String buildPreferenceSections(Map<String, List<String>> selections, String flowType) {
        StringBuilder sb = new StringBuilder();

        String sectionTitle = switch (flowType) {
            case "tours" -> "Tour Package Preferences";
            case "activities" -> "Activities & Experiences Preferences";
            case "destinations" -> "Destination Preferences";
            default -> "Preferences";
        };

        sb.append(String.format("""
                <div class='section-header'>
                    <div class='section-icon'>&#9992;</div>
                    <span class='section-title'>%s</span>
                </div>
                """, sectionTitle));

        switch (flowType) {
            case "tours":
                appendTagGroup(sb, selections, "category", "Tour Categories");
                appendTagGroup(sb, selections, "duration", "Preferred Duration");
                appendTagGroup(sb, selections, "tourType", "Tour Types");
                appendTagGroup(sb, selections, "season", "Preferred Seasons");
                appendTagGroup(sb, selections, "budget", "Budget Range");
                break;
            case "activities":
                appendTagGroup(sb, selections, "category", "Activity Types");
                appendTagGroup(sb, selections, "season", "Preferred Seasons");
                appendTagGroup(sb, selections, "duration", "Preferred Duration");
                break;
            case "destinations":
                appendTagGroup(sb, selections, "category", "Destination Types");
                appendTagGroup(sb, selections, "location", "Preferred Locations");
                appendTagGroup(sb, selections, "rating", "Minimum Ratings");
                break;
        }

        return sb.toString();
    }

    private void appendTagGroup(StringBuilder sb, Map<String, List<String>> selections, String key, String label) {
        List<String> items = selections.get(key);
        if (items == null || items.isEmpty()) return;

        sb.append("<div class='pref-group'>\n");
        sb.append(String.format("<div class='pref-label'>%s</div>\n", escapeHtml(label)));
        sb.append("<div class='tags'>\n");
        for (String item : items) {
            sb.append(String.format("<span class='tag'>%s</span>\n", escapeHtml(item)));
        }
        sb.append("</div>\n</div>\n");
    }

    private String buildTourBookingSummary(TourBookingInquiryRequest request) {
        return String.format("""
                        <div class='summary-card'>
                            <div class='summary-header'><h3>Your Booking Summary</h3></div>
                            %s
                            %s
                        </div>
                        """,
                summaryRow("&#127758;", "Tour / Destination", escapeHtml(request.getTourName())),
                summaryRow("&#128197;", "Selected Package", escapeHtml(request.getPackageName())));
    }

    private String buildInquirySummary(CreateInquiryRequest request) {
        return String.format("""
                        <div class='summary-card'>
                            <div class='summary-header'><h3>Your Inquiry Summary</h3></div>
                            %s
                            %s
                            %s
                        </div>
                        """,
                summaryRow("&#127758;", "Destination", escapeHtml(request.getPreferredDestination())),
                summaryRow("&#128101;", "Travelers", request.getAdults() + " Adults" + (request.getKids() > 0 ? ", " + request.getKids() + " Kids" : "")),
                summaryRow("&#128197;", "Travel Dates", escapeHtml(request.getArrivalDate()) + " to " + escapeHtml(request.getDepartureDate())));
    }

    private String buildDestinationDetailsGrid(DestinationInsertRequest request) {
        return String.format("""
                        <div class='meta-grid'>
                            %s
                            %s
                        </div>
                        """,
                metaItem("Destination Name", escapeHtml(request.getName())),
                metaItem("Status", getStatusBadge(request.getStatus())));
    }

    private String buildContactInfo() {
        return String.format("""
                <div class='contact-label'>Need help sooner?</div>
                <div class='contact-row'>
                    <div class='contact-icon'>&#128222;</div>
                    <span class='contact-text'>%s <span class='contact-sub'>24/7 Support</span></span>
                </div>
                <div class='contact-row'>
                    <div class='contact-icon'>&#9993;</div>
                    <span class='contact-text'>%s</span>
                </div>
                """, COMPANY_PHONE, COMPANY_EMAIL);
    }

    private String buildNotificationInfo(User user, String actionType) {
        return String.format("""
                        <div class='info-box'>
                            <div class='info-label'>%s Information</div>
                            <div class='info-row'><strong>%s By:</strong> <span>%s %s (%s)</span></div>
                            <div class='info-row'><strong>Employee ID:</strong> <span>%s</span></div>
                            <div class='info-row'><strong>CC Recipients:</strong> <span>Supervisors / Management</span></div>
                            <div class='info-row'><strong>%s Time:</strong> <span>%s</span></div>
                        </div>
                        """,
                actionType,
                actionType,
                escapeHtml(user.getFirstName()),
                escapeHtml(user.getLastName()),
                escapeHtml(user.getEmail()),
                user.getId() != null ? user.getId() : "N/A",
                actionType,
                getCurrentDateTime());
    }

    private String buildHeader(String badgeText, String title, String subtitle) {
        return String.format("""
                <div class='header'>
                    <div class='header-accent'></div>
                    <div class='logo'>
                        <div class='logo-mark'>&#9992;</div>
                        <span class='logo-name'>%s</span>
                    </div>
                    <div class='badge'><span class='badge-dot'></span> %s</div>
                    <h1>%s</h1>
                    <p>%s</p>
                </div>
                """, COMPANY_NAME, badgeText, title, subtitle);
    }

    private String buildInquiryIdPill(Long inquiryId) {
        return String.format("""
                <div class='id-pill'>
                    <span class='lbl'>Inquiry Reference</span>
                    <span class='val'>#INQ-%05d</span>
                </div>
                """, inquiryId);
    }

    private String buildAlert(String title, String message) {
        return String.format("""
                <div class='alert'>
                    <div class='alert-dot'></div>
                    <div>
                        <div class='alert-title'>%s</div>
                        <div class='alert-desc'>%s</div>
                    </div>
                </div>
                """, title, message);
    }

    private String buildSuccessAlert(String title, String message) {
        return String.format("""
                <div class='success-alert'>
                    <div class='success-title'>&#10004; %s</div>
                    <div class='success-desc'>%s</div>
                </div>
                """, title, message);
    }

    private String buildCta(String url, String text) {
        return String.format("""
                <div class='cta'>
                    <a href='%s' class='btn'>%s</a>
                    <div class='btn-sub'>felicitatrips.com</div>
                </div>
                """, url, text);
    }

    private String buildFooter() {
        return String.format("""
                <div class='footer'>
                    <div class='footer-brand'><strong>%s</strong>Premium Sri Lanka Travel Experiences</div>
                    <div class='footer-note'>This is an automated notification. Please do not reply to this email.</div>
                </div>
                """, COMPANY_NAME);
    }

    private String getStatusBadge(String status) {
        if (status == null) return "N/A";
        return switch (status.toLowerCase()) {
            case "active", "published" ->
                    "<span style='display:inline-block; background:#DCFCE7; color:#166534; padding:2px 8px; border-radius:4px; font-size:11px; font-weight:600;'>ACTIVE</span>";
            case "inactive", "draft" ->
                    "<span style='display:inline-block; background:#FEE2E2; color:#991B1B; padding:2px 8px; border-radius:4px; font-size:11px; font-weight:600;'>INACTIVE</span>";
            default ->
                    String.format("<span style='display:inline-block; background:#FEF3C7; color:#92400E; padding:2px 8px; border-radius:4px; font-size:11px; font-weight:600;'>%s</span>", escapeHtml(status.toUpperCase()));
        };
    }

    private int getTotalUpdateCount(DestinationUpdateComparisonResult result) {
        int count = 0;
        if (result.getUpdatedFields() != null) count += result.getUpdatedFields().size();
        if (result.getRemovedImages() != null) count += result.getRemovedImages().size();
        if (result.getAddedImages() != null) count += result.getAddedImages().size();
        if (result.getRemovedActivities() != null) count += result.getRemovedActivities().size();
        if (result.getAddedActivities() != null) count += result.getAddedActivities().size();
        if (result.getModifiedActivities() != null) count += result.getModifiedActivities().size();
        if (result.getRemovedCategories() != null) count += result.getRemovedCategories().size();
        if (result.getAddedCategoryIds() != null) count += result.getAddedCategoryIds().size();
        return count;
    }
}