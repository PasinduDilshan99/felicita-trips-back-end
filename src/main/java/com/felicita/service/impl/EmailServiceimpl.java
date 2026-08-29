package com.felicita.service.impl;

import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.request.email.HotelRatesRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.InsertResponse;
import com.felicita.service.EmailService;
import com.felicita.util.CommonResponseMessages;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Properties;

@Service
public class EmailServiceimpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceimpl.class);

    @Autowired
    @Qualifier("devMailSender")
    private JavaMailSender devMailSender;

    @Autowired
    @Qualifier("mainMailSender")
    private JavaMailSender mainMailSender;

    @Autowired
    @Qualifier("infoMailSender")
    private JavaMailSender infoMailSender;

    // Add these for IMAP store access
    private static final String IMAP_HOST = "mail.privateemail.com";
    private static final String IMAP_PORT = "993";

    @Override
    public void sendFromDev(String to, String subject, String body) {
        sendHtmlEmail(devMailSender, to, subject, body);
    }

    @Override
    public void sendFromDev(String to, List<String> cc, String subject, String body) {
        sendHtmlEmail(devMailSender, to, cc, subject, body);
    }

    @Override
    public void sendFromAdmin(String to, String subject, String body) {
        sendHtmlEmail(infoMailSender, to, subject, body);
    }

    @Override
    public void sendFromInfo(List<String> to, List<String> cc, String subject) {
        sendInfoHtmlEmail(infoMailSender, to, cc, subject);
    }

    /**
     * Send HTML email using the Info mail sender with multiple recipients
     * This sends separate emails to each recipient in the 'to' list
     *
     * @param infoMailSender The info mail sender instance
     * @param to List of recipient email addresses (each receives a separate email)
     * @param cc List of CC recipient email addresses (can be null or empty)
     * @param subject Email subject
     */
    private void sendInfoHtmlEmail(JavaMailSender infoMailSender,
                                   List<String> to,
                                   List<String> cc,
                                   String subject) {
        try {
            if (to == null || to.isEmpty()) {
                throw new IllegalArgumentException("Recipient list (to) cannot be null or empty");
            }

            // Loop through each recipient and send separate email
            for (String recipient : to) {
                sendSingleEmail(infoMailSender, recipient, cc, subject);
            }

            LOGGER.info("Info HTML emails sent successfully to {} recipients: {}", to.size(), to);

        } catch (MessagingException e) {
            LOGGER.error("Failed to send Info HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send Info HTML email", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error sending Info HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Unexpected error sending Info HTML email", e);
        }
    }

    /**
     * Send a single email to one recipient
     */
    private void sendSingleEmail(JavaMailSender infoMailSender,
                                 String to,
                                 List<String> cc,
                                 String subject) throws MessagingException {

        MimeMessage message = infoMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set sender info
        String fromEmail = "info@felicitatrips.com";
        helper.setFrom(fromEmail);

        // Set single recipient
        helper.setTo(to);

        // Set CC recipients if provided
        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.toArray(new String[0]));
        }

        // Set subject
        helper.setSubject(subject);

        // Build the email body with the new structure
        String body = buildEmailBody();

        // Build the full email content with signature
        String fullBody = buildEmailWithSignature(body);
        helper.setText(fullBody, true); // true = HTML content

        // Send the email
        infoMailSender.send(message);

        // Optional: Save a copy to Sent folder
        saveToSentFolder(infoMailSender, message, fromEmail);

        LOGGER.debug("Email sent to: {}", to);
    }

    /**
     * Builds the email body with the specified structure
     */
    private String buildEmailBody() {
        return "<p style='font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #333333;'>" +
                "Dear Team,<br><br>" +
                "Greetings from Felicita Trips!<br><br>" +
                "We are a newly established travel company specializing in inbound tourism in Sri Lanka. " +
                "We have already received several tour inquiries and have recommended your property to our potential guests.<br><br>" +
                "In this regard, we would appreciate it if you could share your travel agent rates for the Summer and Winter seasons.<br><br>" +
                "Should you require any further details or documents from our end, please feel free to reach out to us.<br><br>" +
                "We look forward to hearing from you and hope to build a good business relationship with your team.<br><br>" +
                "Best regards,<br>" +
                "</p>";
    }

    /**
     * Builds the complete email content with signature appended
     */
    private String buildEmailWithSignature(String body) {
        String signature = getEmailSignature();

        // Combine body and signature with proper spacing
        return body +
                "<div style='margin-top: 30px;'>" +
                signature +
                "</div>";
    }

    /**
     * Returns the email signature HTML
     */
    private String getEmailSignature() {
        String logoUrl = "https://res.cloudinary.com/dtzrivqye/image/upload/v1775493945/gi5x2y4vwaplhkwchp0p.png";

        return "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"font-family: Arial, Helvetica, sans-serif; border-collapse: collapse;\">" +
                "  <tr>" +
                "    <!-- Logo -->" +
                "    <td style=\"padding-right:18px; vertical-align:middle;\" width=\"90\">" +
                "      <img src=\"" + logoUrl + "\" width=\"80\" height=\"80\" alt=\"Felicita Trips\" style=\"display:block; border:0;\">" +
                "    </td>" +
                "    <!-- Vertical divider -->" +
                "    <td style=\"border-left:2px solid #4f8f80; padding-left:18px; vertical-align:middle;\">" +
                "      <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"font-family: Arial, Helvetica, sans-serif; border-collapse: collapse;\">" +
                "        <tr>" +
                "          <td style=\"font-size:19px; font-weight:bold; color:#1a1a1a; padding-bottom:2px; font-family: Arial, Helvetica, sans-serif;\">" +
                "            Dilshan Dimbulana" +
                "          </td>" +
                "        </tr>" +
                "        <tr>" +
                "          <td style=\"font-size:13px; color:#4f8f80; font-weight:bold; padding-bottom:8px; font-family: Arial, Helvetica, sans-serif;\">" +
                "            Proprietor, Felicita Trips" +
                "          </td>" +
                "        </tr>" +
                "        <tr>" +
                "          <td style=\"font-size:12.5px; color:#333333; padding-bottom:3px; font-family: Arial, Helvetica, sans-serif;\">" +
                "            <span style=\"color:#4f8f80; font-weight:bold;\">M:</span>" +
                "            <a href=\"https://wa.me/94701774488\" style=\"color:#333333; text-decoration:none; font-family: Arial, Helvetica, sans-serif;\">+94 70 177 4488</a>" +
                "          </td>" +
                "        </tr>" +
                "        <tr>" +
                "          <td style=\"font-size:12.5px; color:#333333; padding-bottom:3px; font-family: Arial, Helvetica, sans-serif;\">" +
                "            <span style=\"color:#4f8f80; font-weight:bold;\">E:</span>" +
                "            <a href=\"mailto:info@felicitatrips.com\" style=\"color:#333333; text-decoration:none; font-family: Arial, Helvetica, sans-serif;\">info@felicitatrips.com</a>" +
                "          </td>" +
                "        </tr>" +
                "        <tr>" +
                "          <td style=\"font-size:12.5px; color:#333333; padding-bottom:8px; font-family: Arial, Helvetica, sans-serif;\">" +
                "            <span style=\"color:#4f8f80; font-weight:bold;\">W:</span>" +
                "            <a href=\"https://www.felicitatrips.com\" style=\"color:#333333; text-decoration:none; font-family: Arial, Helvetica, sans-serif;\">www.felicitatrips.com</a>" +
                "          </td>" +
                "        </tr>" +
                "        <tr>" +
                "          <td style=\"font-size:11px; color:#888888; padding-top:4px; font-family: Arial, Helvetica, sans-serif;\">" +
                "            Colombo, Sri Lanka" +
                "          </td>" +
                "        </tr>" +
                "      </table>" +
                "    </td>" +
                "  </tr>" +
                "</table>";
    }

    @Override
    public void sendFromMain(String to, String subject, String body) {
        sendHtmlEmail(mainMailSender, to, subject, body);
    }

    @Override
    public void sendPlainTextFromDev(String to, String subject, String body) {
        sendPlainTextEmail(devMailSender, to, subject, body);
    }

    @Override
    public void sendPlainTextFromMain(String to, String subject, String body) {
        sendPlainTextEmail(mainMailSender, to, subject, body);
    }

    @Override
    public CommonResponse<InsertResponse> requestHotelRates(HotelRatesRequest hotelRatesRequest) {
        LOGGER.info("Start requesting hotel rates");
        try {
            sendFromInfo(hotelRatesRequest.getTo(), hotelRatesRequest.getCc(), hotelRatesRequest.getSubject());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Hotel rates requested successfully"),
                    Instant.now());

        } catch (Exception e) {
            LOGGER.error("Error occurred while requesting hotel rates: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to request hotel rates");
        } finally {
            LOGGER.info("End requesting hotel rates");
        }
    }

    private String getSenderEmail(JavaMailSender sender) {
        if (sender == devMailSender) {
            return "dev.felicitatrips@gmail.com";
        } else if (sender == mainMailSender) {
            return "felicitatrips@gmail.com";
        } else if (sender == infoMailSender) {
            return "info@felicitatrips.com";
        }
        return "info@felicitatrips.com";
    }

    /**
     * Save a copy of the sent email to the Sent folder
     */
    private void saveToSentFolder(JavaMailSender sender, MimeMessage message, String fromEmail) {
        try {
            // Only save for private email (info@felicitatrips.com)
            if (!fromEmail.equals("info@felicitatrips.com")) {
                return;
            }

            // Create IMAP session to save to Sent folder
            Properties props = new Properties();
            props.put("mail.imap.host", IMAP_HOST);
            props.put("mail.imap.port", IMAP_PORT);
            props.put("mail.imap.ssl.enable", "true");
            props.put("mail.imap.ssl.trust", IMAP_HOST);
            props.put("mail.store.protocol", "imaps");

            Session imapSession = Session.getInstance(props);
            Store store = imapSession.getStore("imaps");

            try {
                // Connect to IMAP
                store.connect(IMAP_HOST, 993, fromEmail, "Nisa@#0727");

                // Open Sent folder
                Folder sentFolder = store.getFolder("Sent");
                if (sentFolder == null) {
                    sentFolder = store.getFolder("INBOX.Sent");
                }
                if (sentFolder == null) {
                    sentFolder = store.getFolder("[Gmail]/Sent Mail");
                }
                if (sentFolder == null) {
                    LOGGER.warn("Could not find Sent folder, skipping save");
                    return;
                }

                sentFolder.open(Folder.READ_WRITE);

                // Append the message to Sent folder
                sentFolder.appendMessages(new Message[]{message});
                sentFolder.close(false);

                LOGGER.info("Email saved to Sent folder");

            } catch (MessagingException e) {
                LOGGER.error("Failed to save to Sent folder: {}", e.getMessage());
                // Don't throw exception here - email was already sent
            } finally {
                if (store != null && store.isConnected()) {
                    try {
                        store.close();
                    } catch (MessagingException e) {
                        LOGGER.warn("Error closing IMAP store: {}", e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.warn("Failed to save email to Sent folder: {}", e.getMessage());
            // Don't throw - email was already sent successfully
        }
    }

    private void sendHtmlEmail(JavaMailSender sender, String to, String subject, String htmlBody) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String fromEmail = getSenderEmail(sender);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            sender.send(message);
            LOGGER.info("HTML email sent successfully to: {}", to);

            // Save to Sent folder
            saveToSentFolder(sender, message, fromEmail);

        } catch (Exception e) {
            LOGGER.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    private void sendHtmlEmail(JavaMailSender sender,
                               String to,
                               List<String> cc,
                               String subject,
                               String htmlBody) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String fromEmail = getSenderEmail(sender);
            helper.setFrom(fromEmail);
            helper.setTo(to);

            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(new String[0]));
            }

            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            sender.send(message);
            LOGGER.info("HTML email sent successfully to: {}", to);

            // Save to Sent folder
            saveToSentFolder(sender, message, fromEmail);

        } catch (Exception e) {
            LOGGER.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    private void sendPlainTextEmail(JavaMailSender sender, String to, String subject, String textBody) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            String fromEmail = getSenderEmail(sender);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textBody, false);

            sender.send(message);
            LOGGER.info("Plain text email sent successfully to: {}", to);

            // Save to Sent folder
            saveToSentFolder(sender, message, fromEmail);

        } catch (Exception e) {
            LOGGER.error("Failed to send plain text email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send plain text email", e);
        }
    }
}