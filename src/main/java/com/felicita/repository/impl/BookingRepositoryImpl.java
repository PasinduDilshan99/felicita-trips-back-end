package com.felicita.repository.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.InsertFailedErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.dto.*;
import com.felicita.model.request.BookingCancelledRequest;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.BookingAssignStatisticsResponse;
import com.felicita.model.response.statistics.BookingHistoryStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatusStatisticsResponse;
import com.felicita.queries.ActivitiesQueries;
import com.felicita.queries.BookingQueries;
import com.felicita.queries.TourQueries;
import com.felicita.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CompleteToursResponse> getCompletedBookingToursDetailsById(Long userId) {
        String GET_COMPLETE_BOOKING_DETAILS_BY_ID = BookingQueries.GET_COMPLETE_BOOKING_DETAILS_BY_ID;
        String GET_COMPLETE_BOOKING_PARTICIPANTS = BookingQueries.GET_COMPLETE_BOOKING_PARTICIPANTS;
        String GET_COMPLETE_BOOKING_ACTIVITIES = BookingQueries.GET_COMPLETE_BOOKING_ACTIVITIES;
        String GET_COMPLETE_BOOKING_PAYMENTS = BookingQueries.GET_COMPLETE_BOOKING_PAYMENTS;
        String GET_COMPLETE_BOOKING_DOCUMENTS = BookingQueries.GET_COMPLETE_BOOKING_DOCUMENTS;

        try {
            LOGGER.info("Executing query to fetch completed booking tours for user ID: {}", userId);

            // Create a map to store bookings by ID
            Map<Long, CompleteToursResponse> bookingMap = new LinkedHashMap<>();

            // 1. Fetch main booking details
            jdbcTemplate.query(GET_COMPLETE_BOOKING_DETAILS_BY_ID, new Object[]{userId}, (rs) -> {
                Long bookingId = rs.getLong("booking_id");

                CompleteToursResponse booking = CompleteToursResponse.builder()
                        .bookingId(bookingId)
                        .bookingReference(rs.getString("booking_reference"))
                        .bookingDate(rs.getDate("booking_date").toLocalDate())
                        .travelStartDate(rs.getDate("travel_start_date").toLocalDate())
                        .travelEndDate(rs.getDate("travel_end_date").toLocalDate())
                        .totalPersons(rs.getInt("total_persons"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .insuranceAmount(rs.getBigDecimal("insurance_amount"))
                        .finalAmount(rs.getBigDecimal("final_amount"))
                        .bookingStatus(rs.getString("booking_status"))
                        .cancellationReason(rs.getString("cancellation_reason"))
                        .cancellationDate(rs.getTimestamp("cancellation_date") != null ?
                                rs.getTimestamp("cancellation_date").toLocalDateTime() : null)
                        .refundAmount(rs.getBigDecimal("refund_amount"))
                        .tourId(rs.getLong("tour_id"))
                        .tourName(rs.getString("tour_name"))
                        .tourDescription(rs.getString("tour_description"))
                        .tourDuration(rs.getInt("tour_duration"))
                        .startLocation(rs.getString("start_location"))
                        .endLocation(rs.getString("end_location"))
//                        .tourType(rs.getString("tour_type"))
//                        .tourCategory(rs.getString("tour_category"))
                        .packageName(rs.getString("package_name"))
                        .packageDescription(rs.getString("package_description"))
                        .packageTotalPrice(rs.getBigDecimal("package_total_price"))
                        .discountPercentage(rs.getBigDecimal("discount_percentage"))
                        .packagePricePerPerson(rs.getBigDecimal("package_price_per_person"))
                        .packageScheduleName(rs.getString("package_schedule_name"))
                        .assumeStartDate(rs.getDate("assume_start_date") != null ?
                                rs.getDate("assume_start_date").toLocalDate() : null)
                        .assumeEndDate(rs.getDate("assume_end_date") != null ?
                                rs.getDate("assume_end_date").toLocalDate() : null)
                        .username(rs.getString("username"))
                        .userFullName(rs.getString("user_full_name"))
                        .email(rs.getString("email"))
                        .mobileNumber1(rs.getString("mobile_number1"))
                        .actualDurationDays(rs.getInt("actual_duration_days"))
                        .completionTime(rs.getString("completion_time"))
                        .participants(new ArrayList<>())
                        .activities(new ArrayList<>())
                        .payments(new ArrayList<>())
                        .documents(new ArrayList<>())
                        .build();

                bookingMap.put(bookingId, booking);
            });

            // 2. Fetch participants for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_COMPLETE_BOOKING_PARTICIPANTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    CompleteToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        CompleteToursResponse.Participant participant = CompleteToursResponse.Participant.builder()
                                .bookingId(bookingId)
                                .firstName(rs.getString("first_name"))
                                .lastName(rs.getString("last_name"))
                                .dateOfBirth(rs.getDate("date_of_birth") != null ?
                                        rs.getDate("date_of_birth").toLocalDate() : null)
                                .age(rs.getInt("age"))
                                .gender(rs.getString("gender"))
                                .passportNumber(rs.getString("passport_number"))
                                .nationality(rs.getString("nationality"))
                                .email(rs.getString("email"))
                                .mobileNumber(rs.getString("mobile_number"))
                                .emergencyContactName(rs.getString("emergency_contact_name"))
                                .emergencyContactPhone(rs.getString("emergency_contact_phone"))
                                .emergencyContactRelationship(rs.getString("emergency_contact_relationship"))
                                .medicalConditions(rs.getString("medical_conditions"))
                                .allergies(rs.getString("allergies"))
                                .specialAssistanceRequired(rs.getBoolean("special_assistance_required"))
                                .assistanceDetails(rs.getString("assistance_details"))
                                .roomSharingWithFirstName(rs.getString("room_sharing_with_first_name"))
                                .roomSharingWithLastName(rs.getString("room_sharing_with_last_name"))
                                .build();

                        booking.getParticipants().add(participant);
                    }
                });
            }

            // 3. Fetch activities for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_COMPLETE_BOOKING_ACTIVITIES, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    CompleteToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        CompleteToursResponse.Activity activity = CompleteToursResponse.Activity.builder()
                                .bookingId(bookingId)
                                .activityName(rs.getString("activity_name"))
                                .activityDescription(rs.getString("activity_description"))
//                                .activityCategory(rs.getString("activity_category"))
                                .activityDate(rs.getDate("activity_date") != null ?
                                        rs.getDate("activity_date").toLocalDate() : null)
                                .startTime(rs.getString("start_time"))
                                .endTime(rs.getString("end_time"))
                                .numberOfParticipants(rs.getInt("number_of_participants"))
                                .pricePerPerson(rs.getBigDecimal("price_per_person"))
                                .totalPrice(rs.getBigDecimal("total_price"))
                                .destinationName(rs.getString("destination_name"))
                                .durationHours(rs.getBigDecimal("duration_hours"))
                                .priceLocal(rs.getBigDecimal("price_local"))
                                .priceForeigners(rs.getBigDecimal("price_foreigners"))
                                .activityStatus(rs.getString("activity_status"))
                                .build();

                        booking.getActivities().add(activity);
                    }
                });
            }

            // 4. Fetch payments for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_COMPLETE_BOOKING_PAYMENTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    CompleteToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        CompleteToursResponse.Payment payment = CompleteToursResponse.Payment.builder()
                                .bookingId(bookingId)
                                .paymentReference(rs.getString("payment_reference"))
                                .amount(rs.getBigDecimal("amount"))
                                .paymentMethod(rs.getString("payment_method"))
                                .paymentStatus(rs.getString("payment_status"))
                                .installmentNumber(rs.getInt("installment_number"))
                                .totalInstallments(rs.getInt("total_installments"))
                                .paymentDate(rs.getTimestamp("payment_date") != null ?
                                        rs.getTimestamp("payment_date").toLocalDateTime() : null)
                                .dueDate(rs.getDate("due_date") != null ?
                                        rs.getDate("due_date").toLocalDate() : null)
                                .transactionId(rs.getString("transaction_id"))
                                .invoiceNumber(rs.getString("invoice_number"))
                                .invoiceDate(rs.getDate("invoice_date") != null ?
                                        rs.getDate("invoice_date").toLocalDate() : null)
                                .invoiceTotal(rs.getBigDecimal("invoice_total"))
                                .amountPaid(rs.getBigDecimal("amount_paid"))
                                .balanceDue(rs.getBigDecimal("balance_due"))
                                .build();

                        booking.getPayments().add(payment);
                    }
                });
            }

            // 5. Fetch documents for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_COMPLETE_BOOKING_DOCUMENTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    CompleteToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        CompleteToursResponse.Document document = CompleteToursResponse.Document.builder()
                                .bookingId(bookingId)
                                .documentType(rs.getString("document_type"))
                                .documentName(rs.getString("document_name"))
                                .documentUrl(rs.getString("document_url"))
                                .fileSize(rs.getLong("file_size"))
                                .build();

                        booking.getDocuments().add(document);
                    }
                });
            }

            LOGGER.info("Successfully fetched {} completed booking tours for user ID: {}",
                    bookingMap.size(), userId);

            return new ArrayList<>(bookingMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching completed booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch completed booking tours from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching completed booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching completed booking tours");
        }
    }

    @Override
    public List<UpcomingToursResponse> getUpcomingBookingToursDetailsById(Long userId) {
        String GET_UPCOMING_BOOKING_DETAILS_BY_ID = BookingQueries.GET_UPCOMING_BOOKING_DETAILS_BY_ID;
        String GET_UPCOMING_BOOKING_PARTICIPANTS = BookingQueries.GET_UPCOMING_BOOKING_PARTICIPANTS;
        String GET_UPCOMING_BOOKING_ACTIVITIES = BookingQueries.GET_UPCOMING_BOOKING_ACTIVITIES;
        String GET_UPCOMING_BOOKING_PAYMENTS = BookingQueries.GET_UPCOMING_BOOKING_PAYMENTS;
        String GET_UPCOMING_BOOKING_DOCUMENTS = BookingQueries.GET_UPCOMING_BOOKING_DOCUMENTS;

        try {
            LOGGER.info("Executing query to fetch upcoming booking tours for user ID: {}", userId);

            // Create a map to store bookings by ID
            Map<Long, UpcomingToursResponse> bookingMap = new LinkedHashMap<>();

            // 1. Fetch main booking details
            jdbcTemplate.query(GET_UPCOMING_BOOKING_DETAILS_BY_ID, new Object[]{userId}, (rs) -> {
                Long bookingId = rs.getLong("booking_id");

                UpcomingToursResponse booking = UpcomingToursResponse.builder()
                        .bookingId(bookingId)
                        .bookingReference(rs.getString("booking_reference"))
                        .bookingDate(rs.getDate("booking_date").toLocalDate())
                        .travelStartDate(rs.getDate("travel_start_date").toLocalDate())
                        .travelEndDate(rs.getDate("travel_end_date").toLocalDate())
                        .totalPersons(rs.getInt("total_persons"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .assignTo(rs.getLong("assign_to"))
                        .assignToName(rs.getString("assign_to_name"))
                        .insuranceAmount(rs.getBigDecimal("insurance_amount"))
                        .finalAmount(rs.getBigDecimal("final_amount"))
                        .bookingStatus(rs.getString("booking_status"))
                        .cancellationReason(rs.getString("cancellation_reason"))
                        .tourId(rs.getLong("tour_id"))
                        .tourName(rs.getString("tour_name"))
                        .tourDescription(rs.getString("tour_description"))
                        .tourDuration(rs.getInt("tour_duration"))
                        .startLocation(rs.getString("start_location"))
                        .endLocation(rs.getString("end_location"))
//                        .tourType(rs.getString("tour_type"))
//                        .tourCategory(rs.getString("tour_category"))
                        .packageName(rs.getString("package_name"))
                        .packageDescription(rs.getString("package_description"))
                        .packageTotalPrice(rs.getBigDecimal("package_total_price"))
                        .discountPercentage(rs.getBigDecimal("discount_percentage"))
                        .packagePricePerPerson(rs.getBigDecimal("package_price_per_person"))
                        .packageScheduleName(rs.getString("package_schedule_name"))
                        .assumeStartDate(rs.getDate("assume_start_date") != null ?
                                rs.getDate("assume_start_date").toLocalDate() : null)
                        .assumeEndDate(rs.getDate("assume_end_date") != null ?
                                rs.getDate("assume_end_date").toLocalDate() : null)
                        .username(rs.getString("username"))
                        .userFullName(rs.getString("user_full_name"))
                        .email(rs.getString("email"))
                        .mobileNumber1(rs.getString("mobile_number1"))
                        .daysUntilTravel(rs.getLong("days_until_travel"))
                        .travelUrgency(rs.getString("travel_urgency"))
                        .countdown(rs.getString("countdown"))
                        .participants(new ArrayList<>())
                        .activities(new ArrayList<>())
                        .payments(new ArrayList<>())
                        .documents(new ArrayList<>())
                        .build();

                bookingMap.put(bookingId, booking);
            });

            // 2. Fetch participants for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_UPCOMING_BOOKING_PARTICIPANTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    UpcomingToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        UpcomingToursResponse.Participant participant = UpcomingToursResponse.Participant.builder()
                                .bookingId(bookingId)
                                .firstName(rs.getString("first_name"))
                                .lastName(rs.getString("last_name"))
                                .dateOfBirth(rs.getDate("date_of_birth") != null ?
                                        rs.getDate("date_of_birth").toLocalDate() : null)
                                .age(rs.getInt("age"))
                                .gender(rs.getString("gender"))
                                .passportNumber(rs.getString("passport_number"))
                                .passportProvided(rs.getString("passport_number") != null)
                                .nationality(rs.getString("nationality"))
                                .email(rs.getString("email"))
                                .mobileNumber(rs.getString("mobile_number"))
                                .emergencyContactName(rs.getString("emergency_contact_name"))
                                .emergencyContactPhone(rs.getString("emergency_contact_phone"))
                                .emergencyContactRelationship(rs.getString("emergency_contact_relationship"))
                                .medicalConditions(rs.getString("medical_conditions"))
                                .allergies(rs.getString("allergies"))
                                .specialAssistanceRequired(rs.getBoolean("special_assistance_required"))
                                .assistanceDetails(rs.getString("assistance_details"))
                                .roomSharingWithFirstName(rs.getString("room_sharing_with_first_name"))
                                .roomSharingWithLastName(rs.getString("room_sharing_with_last_name"))
                                .participantReadiness(
                                        (rs.getString("passport_number") != null && rs.getString("medical_conditions") != null)
                                                ? "READY" : "PENDING_DOCS"
                                )
                                .build();

                        booking.getParticipants().add(participant);
                    }
                });
            }

            // 3. Fetch activities for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_UPCOMING_BOOKING_ACTIVITIES, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    UpcomingToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        UpcomingToursResponse.Activity activity = UpcomingToursResponse.Activity.builder()
                                .bookingId(bookingId)
                                .activityName(rs.getString("activity_name"))
                                .activityDescription(rs.getString("activity_description"))
//                                .activityCategory(rs.getString("activity_category"))
                                .activityDate(rs.getDate("activity_date") != null ?
                                        rs.getDate("activity_date").toLocalDate() : null)
                                .startTime(rs.getString("start_time"))
                                .endTime(rs.getString("end_time"))
                                .numberOfParticipants(rs.getInt("number_of_participants"))
                                .pricePerPerson(rs.getBigDecimal("price_per_person"))
                                .totalPrice(rs.getBigDecimal("total_price"))
                                .destinationName(rs.getString("destination_name"))
                                .durationHours(rs.getBigDecimal("duration_hours"))
                                .priceLocal(rs.getBigDecimal("price_local"))
                                .priceForeigners(rs.getBigDecimal("price_foreigners"))
                                .daysUntilActivity(rs.getLong("days_until_activity"))
                                .activityTiming(rs.getString("activity_timing"))
                                .build();

                        booking.getActivities().add(activity);
                    }
                });
            }

            // 4. Fetch payments for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_UPCOMING_BOOKING_PAYMENTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    UpcomingToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        UpcomingToursResponse.Payment payment = UpcomingToursResponse.Payment.builder()
                                .bookingId(bookingId)
                                .paymentReference(rs.getString("payment_reference"))
                                .amount(rs.getBigDecimal("amount"))
                                .paymentMethod(rs.getString("payment_method"))
                                .paymentStatus(rs.getString("payment_status"))
                                .installmentNumber(rs.getInt("installment_number"))
                                .totalInstallments(rs.getInt("total_installments"))
                                .paymentDate(rs.getTimestamp("payment_date") != null ?
                                        rs.getTimestamp("payment_date").toLocalDateTime() : null)
                                .dueDate(rs.getDate("due_date") != null ?
                                        rs.getDate("due_date").toLocalDate() : null)
                                .transactionId(rs.getString("transaction_id"))
                                .invoiceNumber(rs.getString("invoice_number"))
                                .invoiceDate(rs.getDate("invoice_date") != null ?
                                        rs.getDate("invoice_date").toLocalDate() : null)
                                .invoiceTotal(rs.getBigDecimal("invoice_total"))
                                .amountPaid(rs.getBigDecimal("amount_paid"))
                                .balanceDue(rs.getBigDecimal("balance_due"))
                                .paymentUrgency(rs.getString("payment_urgency"))
                                .build();

                        booking.getPayments().add(payment);
                    }
                });
            }

            // 5. Fetch documents for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_UPCOMING_BOOKING_DOCUMENTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    UpcomingToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        UpcomingToursResponse.Document document = UpcomingToursResponse.Document.builder()
                                .bookingId(bookingId)
                                .documentType(rs.getString("document_type"))
                                .documentName(rs.getString("document_name"))
                                .documentUrl(rs.getString("document_url"))
                                .fileSize(rs.getLong("file_size"))
                                .documentCategory(rs.getString("document_category"))
                                .build();

                        booking.getDocuments().add(document);
                    }
                });
            }

            LOGGER.info("Successfully fetched {} upcoming booking tours for user ID: {}",
                    bookingMap.size(), userId);

            return new ArrayList<>(bookingMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching upcoming booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch upcoming booking tours from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching upcoming booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching upcoming booking tours");
        }
    }

    @Override
    public List<RequestedToursResponse> getRequstedToursDetailsById(Long userId) {
        String GET_REQUESTED_BOOKING_DETAILS_BY_ID = BookingQueries.GET_REQUESTED_BOOKING_DETAILS_BY_ID;
        String GET_REQUESTED_BOOKING_PARTICIPANTS = BookingQueries.GET_REQUESTED_BOOKING_PARTICIPANTS;
        String GET_REQUESTED_BOOKING_ACTIVITIES = BookingQueries.GET_REQUESTED_BOOKING_ACTIVITIES;
        String GET_REQUESTED_BOOKING_PAYMENTS = BookingQueries.GET_REQUESTED_BOOKING_PAYMENTS;
        String GET_REQUESTED_BOOKING_DOCUMENTS = BookingQueries.GET_REQUESTED_BOOKING_DOCUMENTS;

        try {
            LOGGER.info("Executing query to fetch requested booking tours for user ID: {}", userId);

            // Create a map to store bookings by ID
            Map<Long, RequestedToursResponse> bookingMap = new LinkedHashMap<>();

            // 1. Fetch main booking details
            jdbcTemplate.query(GET_REQUESTED_BOOKING_DETAILS_BY_ID, new Object[]{userId}, (rs) -> {
                Long bookingId = rs.getLong("booking_id");

                RequestedToursResponse booking = RequestedToursResponse.builder()
                        .bookingId(bookingId)
                        .bookingReference(rs.getString("booking_reference"))
                        .bookingDate(rs.getDate("booking_date").toLocalDate())
                        .travelStartDate(
                                rs.getDate("travel_start_date") != null
                                        ? rs.getDate("travel_start_date").toLocalDate()
                                        : null
                        )
                        .travelEndDate(
                                rs.getDate("travel_start_date") != null
                                        ? rs.getDate("travel_end_date").toLocalDate()
                                        : null
                        )
                        .totalPersons(rs.getInt("total_persons"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .insuranceAmount(rs.getBigDecimal("insurance_amount"))
                        .finalAmount(rs.getBigDecimal("final_amount"))
                        .bookingStatus(rs.getString("booking_status"))
                        .cancellationReason(rs.getString("cancellation_reason"))
                        .cancellationDate(rs.getTimestamp("cancellation_date") != null ?
                                rs.getTimestamp("cancellation_date").toLocalDateTime() : null)
                        .refundAmount(rs.getBigDecimal("refund_amount"))
                        .tourId(rs.getLong("tour_id"))
                        .tourName(rs.getString("tour_name"))
                        .tourDescription(rs.getString("tour_description"))
                        .tourDuration(rs.getInt("tour_duration"))
                        .startLocation(rs.getString("start_location"))
                        .endLocation(rs.getString("end_location"))
//                        .tourType(rs.getString("tour_type"))
//                        .tourCategory(rs.getString("tour_category"))
                        .packageName(rs.getString("package_name"))
                        .assignTo(rs.getLong("assign_to"))
                        .assignToName(rs.getString("assign_to_name"))
                        .packageDescription(rs.getString("package_description"))
                        .packageTotalPrice(rs.getBigDecimal("package_total_price"))
                        .discountPercentage(rs.getBigDecimal("discount_percentage"))
                        .packagePricePerPerson(rs.getBigDecimal("package_price_per_person"))
                        .packageScheduleName(rs.getString("package_schedule_name"))
                        .assumeStartDate(rs.getDate("assume_start_date") != null ?
                                rs.getDate("assume_start_date").toLocalDate() : null)
                        .assumeEndDate(rs.getDate("assume_end_date") != null ?
                                rs.getDate("assume_end_date").toLocalDate() : null)
                        .username(rs.getString("username"))
                        .userFullName(rs.getString("user_full_name"))
                        .email(rs.getString("email"))
                        .mobileNumber1(rs.getString("mobile_number1"))
                        .requestStatus(rs.getString("request_status"))
                        .approvalStatus(rs.getString("approval_status"))
                        .daysUntilTravel(rs.getLong("days_until_travel"))
                        .requestUrgency(rs.getString("request_urgency"))
                        .requestAge(rs.getString("request_age"))
                        .participants(new ArrayList<>())
                        .activities(new ArrayList<>())
                        .payments(new ArrayList<>())
                        .documents(new ArrayList<>())
                        .build();

                bookingMap.put(bookingId, booking);
            });

            // 2. Fetch participants for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_REQUESTED_BOOKING_PARTICIPANTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    RequestedToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        RequestedToursResponse.Participant participant = RequestedToursResponse.Participant.builder()
                                .bookingId(bookingId)
                                .firstName(rs.getString("first_name"))
                                .lastName(rs.getString("last_name"))
                                .dateOfBirth(rs.getDate("date_of_birth") != null ?
                                        rs.getDate("date_of_birth").toLocalDate() : null)
                                .age(rs.getInt("age"))
                                .gender(rs.getString("gender"))
                                .passportNumber(rs.getString("passport_number"))
                                .nationality(rs.getString("nationality"))
                                .email(rs.getString("email"))
                                .mobileNumber(rs.getString("mobile_number"))
                                .emergencyContactName(rs.getString("emergency_contact_name"))
                                .emergencyContactPhone(rs.getString("emergency_contact_phone"))
                                .emergencyContactRelationship(rs.getString("emergency_contact_relationship"))
                                .medicalConditions(rs.getString("medical_conditions"))
                                .allergies(rs.getString("allergies"))
                                .specialAssistanceRequired(rs.getBoolean("special_assistance_required"))
                                .assistanceDetails(rs.getString("assistance_details"))
                                .roomSharingWithFirstName(rs.getString("room_sharing_with_first_name"))
                                .roomSharingWithLastName(rs.getString("room_sharing_with_last_name"))
                                .documentStatus(
                                        rs.getString("passport_number") != null ? "COMPLETE" : "PENDING"
                                )
                                .build();

                        booking.getParticipants().add(participant);
                    }
                });
            }

            // 3. Fetch activities for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_REQUESTED_BOOKING_ACTIVITIES, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    RequestedToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        RequestedToursResponse.Activity activity = RequestedToursResponse.Activity.builder()
                                .bookingId(bookingId)
                                .activityName(rs.getString("activity_name"))
                                .activityDescription(rs.getString("activity_description"))
//                                .activityCategory(rs.getString("activity_category"))
                                .activityDate(rs.getDate("activity_date") != null ?
                                        rs.getDate("activity_date").toLocalDate() : null)
                                .startTime(rs.getString("start_time"))
                                .endTime(rs.getString("end_time"))
                                .numberOfParticipants(rs.getInt("number_of_participants"))
                                .pricePerPerson(rs.getBigDecimal("price_per_person"))
                                .totalPrice(rs.getBigDecimal("total_price"))
                                .destinationName(rs.getString("destination_name"))
                                .durationHours(rs.getBigDecimal("duration_hours"))
                                .priceLocal(rs.getBigDecimal("price_local"))
                                .priceForeigners(rs.getBigDecimal("price_foreigners"))
                                .activityStatus(rs.getString("activity_status"))
                                .availabilityStatus(rs.getString("availability_status"))
                                .build();

                        booking.getActivities().add(activity);
                    }
                });
            }

            // 4. Fetch payments for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_REQUESTED_BOOKING_PAYMENTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    RequestedToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        RequestedToursResponse.Payment payment = RequestedToursResponse.Payment.builder()
                                .bookingId(bookingId)
                                .paymentReference(rs.getString("payment_reference"))
                                .amount(rs.getBigDecimal("amount"))
                                .paymentMethod(rs.getString("payment_method"))
                                .paymentStatus(rs.getString("payment_status"))
                                .installmentNumber(rs.getInt("installment_number"))
                                .totalInstallments(rs.getInt("total_installments"))
                                .paymentDate(rs.getTimestamp("payment_date") != null ?
                                        rs.getTimestamp("payment_date").toLocalDateTime() : null)
                                .dueDate(rs.getDate("due_date") != null ?
                                        rs.getDate("due_date").toLocalDate() : null)
                                .transactionId(rs.getString("transaction_id"))
                                .invoiceNumber(rs.getString("invoice_number"))
                                .invoiceDate(rs.getDate("invoice_date") != null ?
                                        rs.getDate("invoice_date").toLocalDate() : null)
                                .invoiceTotal(rs.getBigDecimal("invoice_total"))
                                .amountPaid(rs.getBigDecimal("amount_paid"))
                                .balanceDue(rs.getBigDecimal("balance_due"))
                                .paymentPriority(rs.getString("payment_priority"))
                                .depositRequired(rs.getBoolean("deposit_required"))
                                .depositAmount(rs.getBigDecimal("deposit_amount"))
                                .build();

                        booking.getPayments().add(payment);
                    }
                });
            }

            // 5. Fetch documents for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_REQUESTED_BOOKING_DOCUMENTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    RequestedToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        RequestedToursResponse.Document document = RequestedToursResponse.Document.builder()
                                .bookingId(bookingId)
                                .documentType(rs.getString("document_type"))
                                .documentName(rs.getString("document_name"))
                                .documentUrl(rs.getString("document_url"))
                                .fileSize(rs.getLong("file_size"))
                                .documentStatus(rs.getString("document_status"))
                                .requiredForApproval(rs.getBoolean("required_for_approval"))
                                .build();

                        booking.getDocuments().add(document);
                    }
                });
            }

            LOGGER.info("Successfully fetched {} requested booking tours for user ID: {}",
                    bookingMap.size(), userId);

            return new ArrayList<>(bookingMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching requested booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch requested booking tours from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching requested booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching requested booking tours");
        }
    }

    @Override
    public List<CancelledToursResponse> getCancelledToursDetailsById(Long userId) {
        String GET_CANCELLED_BOOKING_DETAILS_BY_ID = BookingQueries.GET_CANCELLED_BOOKING_DETAILS_BY_ID;
        String GET_CANCELLED_BOOKING_PARTICIPANTS = BookingQueries.GET_CANCELLED_BOOKING_PARTICIPANTS;
        String GET_CANCELLED_BOOKING_ACTIVITIES = BookingQueries.GET_CANCELLED_BOOKING_ACTIVITIES;
        String GET_CANCELLED_BOOKING_PAYMENTS = BookingQueries.GET_CANCELLED_BOOKING_PAYMENTS;
        String GET_CANCELLED_BOOKING_DOCUMENTS = BookingQueries.GET_CANCELLED_BOOKING_DOCUMENTS;

        try {
            LOGGER.info("Executing query to fetch cancelled booking tours for user ID: {}", userId);

            // Create a map to store bookings by ID
            Map<Long, CancelledToursResponse> bookingMap = new LinkedHashMap<>();

            // 1. Fetch main booking details
            jdbcTemplate.query(GET_CANCELLED_BOOKING_DETAILS_BY_ID, new Object[]{userId}, (rs) -> {
                Long bookingId = rs.getLong("booking_id");

                CancelledToursResponse booking = CancelledToursResponse.builder()
                        .bookingId(bookingId)
                        .bookingReference(rs.getString("booking_reference"))
                        .bookingDate(rs.getDate("booking_date").toLocalDate())
                        .travelStartDate(rs.getDate("travel_start_date").toLocalDate())
                        .travelEndDate(rs.getDate("travel_end_date").toLocalDate())
                        .totalPersons(rs.getInt("total_persons"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .insuranceAmount(rs.getBigDecimal("insurance_amount"))
                        .finalAmount(rs.getBigDecimal("final_amount"))
                        .bookingStatus(rs.getString("booking_status"))
                        .cancellationReason(rs.getString("cancellation_reason"))
                        .cancellationDate(rs.getTimestamp("cancellation_date") != null ?
                                rs.getTimestamp("cancellation_date").toLocalDateTime() : null)
                        .refundAmount(rs.getBigDecimal("refund_amount"))
                        .refundStatus(rs.getString("refund_status"))
                        .refundedAmount(rs.getBigDecimal("refunded_amount"))
                        .refundProcessedDate(rs.getTimestamp("refund_processed_date") != null ?
                                rs.getTimestamp("refund_processed_date").toLocalDateTime() : null)
                        .tourId(rs.getLong("tour_id"))
                        .tourName(rs.getString("tour_name"))
                        .tourDescription(rs.getString("tour_description"))
                        .tourDuration(rs.getInt("tour_duration"))
                        .startLocation(rs.getString("start_location"))
                        .endLocation(rs.getString("end_location"))
//                        .tourType(rs.getString("tour_type"))
//                        .tourCategory(rs.getString("tour_category"))
                        .packageName(rs.getString("package_name"))
                        .packageDescription(rs.getString("package_description"))
                        .packageTotalPrice(rs.getBigDecimal("package_total_price"))
                        .discountPercentage(rs.getBigDecimal("discount_percentage"))
                        .packagePricePerPerson(rs.getBigDecimal("package_price_per_person"))
                        .packageScheduleName(rs.getString("package_schedule_name"))
                        .assumeStartDate(rs.getDate("assume_start_date") != null ?
                                rs.getDate("assume_start_date").toLocalDate() : null)
                        .assumeEndDate(rs.getDate("assume_end_date") != null ?
                                rs.getDate("assume_end_date").toLocalDate() : null)
                        .username(rs.getString("username"))
                        .userFullName(rs.getString("user_full_name"))
                        .email(rs.getString("email"))
                        .mobileNumber1(rs.getString("mobile_number1"))
                        .cancellationStage(rs.getString("cancellation_stage"))
                        .daysBeforeTravel(rs.getLong("days_before_travel"))
                        .cancellationFee(rs.getBigDecimal("cancellation_fee"))
                        .cancellationPenaltyPercentage(rs.getBigDecimal("cancellation_penalty_percentage"))
                        .cancellationNotes(rs.getString("cancellation_notes"))
                        .participants(new ArrayList<>())
                        .activities(new ArrayList<>())
                        .payments(new ArrayList<>())
                        .documents(new ArrayList<>())
                        .build();

                bookingMap.put(bookingId, booking);
            });

            // 2. Fetch participants for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_CANCELLED_BOOKING_PARTICIPANTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    CancelledToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        CancelledToursResponse.Participant participant = CancelledToursResponse.Participant.builder()
                                .bookingId(bookingId)
                                .firstName(rs.getString("first_name"))
                                .lastName(rs.getString("last_name"))
                                .dateOfBirth(rs.getDate("date_of_birth") != null ?
                                        rs.getDate("date_of_birth").toLocalDate() : null)
                                .age(rs.getInt("age"))
                                .gender(rs.getString("gender"))
                                .passportNumber(rs.getString("passport_number"))
                                .nationality(rs.getString("nationality"))
                                .email(rs.getString("email"))
                                .mobileNumber(rs.getString("mobile_number"))
                                .emergencyContactName(rs.getString("emergency_contact_name"))
                                .emergencyContactPhone(rs.getString("emergency_contact_phone"))
                                .emergencyContactRelationship(rs.getString("emergency_contact_relationship"))
                                .medicalConditions(rs.getString("medical_conditions"))
                                .allergies(rs.getString("allergies"))
                                .specialAssistanceRequired(rs.getBoolean("special_assistance_required"))
                                .assistanceDetails(rs.getString("assistance_details"))
                                .roomSharingWithFirstName(rs.getString("room_sharing_with_first_name"))
                                .roomSharingWithLastName(rs.getString("room_sharing_with_last_name"))
                                .refundIssued(rs.getBoolean("refund_issued"))
                                .participantRefundAmount(rs.getBigDecimal("participant_refund_amount"))
                                .build();

                        booking.getParticipants().add(participant);
                    }
                });
            }

            // 3. Fetch activities for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_CANCELLED_BOOKING_ACTIVITIES, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    CancelledToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        CancelledToursResponse.Activity activity = CancelledToursResponse.Activity.builder()
                                .bookingId(bookingId)
                                .activityName(rs.getString("activity_name"))
                                .activityDescription(rs.getString("activity_description"))
//                                .activityCategory(rs.getString("activity_category"))
                                .activityDate(rs.getDate("activity_date") != null ?
                                        rs.getDate("activity_date").toLocalDate() : null)
                                .startTime(rs.getString("start_time"))
                                .endTime(rs.getString("end_time"))
                                .numberOfParticipants(rs.getInt("number_of_participants"))
                                .pricePerPerson(rs.getBigDecimal("price_per_person"))
                                .totalPrice(rs.getBigDecimal("total_price"))
                                .destinationName(rs.getString("destination_name"))
                                .durationHours(rs.getBigDecimal("duration_hours"))
                                .priceLocal(rs.getBigDecimal("price_local"))
                                .priceForeigners(rs.getBigDecimal("price_foreigners"))
                                .activityStatus(rs.getString("activity_status"))
                                .activityRefundable(rs.getBoolean("activity_refundable"))
                                .activityRefundAmount(rs.getBigDecimal("activity_refund_amount"))
                                .build();

                        booking.getActivities().add(activity);
                    }
                });
            }

            // 4. Fetch payments for these bookings
            // 4. Fetch payments for these bookings (UPDATED MAPPING)
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_CANCELLED_BOOKING_PAYMENTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    CancelledToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        CancelledToursResponse.Payment payment = CancelledToursResponse.Payment.builder()
                                .bookingId(bookingId)
                                .paymentReference(rs.getString("payment_reference"))
                                .amount(rs.getBigDecimal("amount"))
                                .paymentMethod(rs.getString("payment_method"))
                                .paymentStatus(rs.getString("payment_status"))
                                .installmentNumber(rs.getInt("installment_number"))
                                .totalInstallments(rs.getInt("total_installments"))
                                .paymentDate(rs.getTimestamp("payment_date") != null ?
                                        rs.getTimestamp("payment_date").toLocalDateTime() : null)
                                .dueDate(rs.getDate("due_date") != null ?
                                        rs.getDate("due_date").toLocalDate() : null)
                                .transactionId(rs.getString("transaction_id"))
                                .invoiceNumber(rs.getString("invoice_number"))
                                .invoiceDate(rs.getDate("invoice_date") != null ?
                                        rs.getDate("invoice_date").toLocalDate() : null)
                                .invoiceTotal(rs.getBigDecimal("invoice_total"))
                                .amountPaid(rs.getBigDecimal("amount_paid"))
                                .balanceDue(rs.getBigDecimal("balance_due"))
                                .refundReference(rs.getString("refund_reference"))
                                .refundAmount(rs.getBigDecimal("refund_amount"))
                                .refundDate(rs.getTimestamp("refund_date") != null ?
                                        rs.getTimestamp("refund_date").toLocalDateTime() : null)
                                .refundStatus(rs.getString("refund_status"))  // FIXED: Now matches query
                                .paymentPriority(rs.getString("payment_priority"))
                                .depositRequired(rs.getBoolean("deposit_required"))
                                .depositAmount(rs.getBigDecimal("deposit_amount"))
                                .refundStatusInfo(rs.getString("refund_status_info"))
                                .paymentMethodId(rs.getLong("payment_method_id"))
                                .paymentStatusId(rs.getLong("payment_status_id"))
                                .refundStatusId(rs.getLong("refund_status_id"))
                                .build();

                        booking.getPayments().add(payment);
                    }
                });
            }

            // 5. Fetch documents for these bookings
            if (!bookingMap.isEmpty()) {
                jdbcTemplate.query(GET_CANCELLED_BOOKING_DOCUMENTS, new Object[]{userId}, (rs) -> {
                    Long bookingId = rs.getLong("booking_id");
                    CancelledToursResponse booking = bookingMap.get(bookingId);

                    if (booking != null) {
                        CancelledToursResponse.Document document = CancelledToursResponse.Document.builder()
                                .bookingId(bookingId)
                                .documentType(rs.getString("document_type"))
                                .documentName(rs.getString("document_name"))
                                .documentUrl(rs.getString("document_url"))
                                .fileSize(rs.getLong("file_size"))
                                .documentStatus(rs.getString("document_status"))
                                .cancellationRelated(rs.getBoolean("cancellation_related"))
                                .build();

                        booking.getDocuments().add(document);
                    }
                });
            }

            LOGGER.info("Successfully fetched {} cancelled booking tours for user ID: {}",
                    bookingMap.size(), userId);

            return new ArrayList<>(bookingMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching cancelled booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch cancelled booking tours from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching cancelled booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching cancelled booking tours");
        }
    }

    @Override
    public Long bookingTourBasicDetails(InsertBookingRequestDto dto) {
        String QUERY = BookingQueries.INSERT_BOOKING_BASIC_DETAILS;

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        QUERY,
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, dto.getBookingReference());
                ps.setLong(2, dto.getUserId());
                ps.setLong(3, dto.getPackageScheduleId());

                ps.setInt(4, dto.getTotalPersons());
                ps.setDouble(5, dto.getTotalAmount());
                ps.setDouble(6, dto.getDiscountAmount() != null ? dto.getDiscountAmount() : 0.0);
                ps.setDouble(7, dto.getTaxAmount() != null ? dto.getTaxAmount() : 0.0);
                ps.setDouble(8, dto.getInsuranceAmount() != null ? dto.getInsuranceAmount() : 0.0);
                ps.setDouble(9, dto.getFinalAmount());

                ps.setDate(10, Date.valueOf(dto.getBookingDate()));
                ps.setDate(11, dto.getTravelStartDate());
                ps.setDate(12, dto.getTravelEndDate());

                // booking_status_id (must be resolved beforehand)
                ps.setInt(13, 1);

                ps.setString(14, dto.getSpecialRequirements());
                ps.setString(15, dto.getDietaryRestrictions());
                ps.setBoolean(16, Boolean.TRUE.equals(dto.getInsuranceRequired()));

                // created_by (usually userId)
                ps.setLong(17, dto.getUserId());

                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                throw new InternalServerErrorExceptionHandler("Failed to retrieve generated booking ID");
            }

            return keyHolder.getKey().longValue();

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while inserting booking basic details", ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to insert booking basic details into database"
            );
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while inserting booking basic details", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while inserting booking basic details"
            );
        }
    }

    @Override
    public void bookingTransportation(Long bookingId, VehicleBasicDetailsDto vehicleBasicDetailsDto, LocalDate departureDate, Long userId) {
        String QUERY = BookingQueries.INSERT_BOOKING_TRANSPORTATION;
        try {
            jdbcTemplate.update(QUERY,
                    bookingId,
                    vehicleBasicDetailsDto.getVehicleType(),      // transport_type
                    departureDate,                               // departure_date
                    vehicleBasicDetailsDto.getVehicleMake() + " " + vehicleBasicDetailsDto.getVehicleModel(), // carrier_name
                    vehicleBasicDetailsDto.getVehicleNumber(),   // reference_number
                    userId                                            // created_by (replace with actual user ID)
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while inserting booking transportation for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Database error while inserting booking transportation");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while inserting booking transportation for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while inserting booking transportation");
        }
    }

    @Override
    public void insertBookingPriceBreakdown(Long bookingId, PackageActivityPriceDto activity, int totalParticipants, Long userId) {
        String QUERY = BookingQueries.INSERT_BOOKING_PRICE_BREAKDOWN;

        try {
            Double totalPrice = activity.getPriceForeigners() != null
                    ? activity.getPriceForeigners() * totalParticipants
                    : 0.0;

            jdbcTemplate.update(
                    QUERY,
                    bookingId,
                    "ACTIVITY",
                    activity.getName(),
                    activity.getDescription(),
                    totalParticipants,
                    activity.getPriceForeigners(),
                    totalPrice,
                    userId
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while inserting booking price breakdown for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Database error while inserting booking price breakdown");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while inserting booking price breakdown for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while inserting booking price breakdown");
        }
    }

    @Override
    public void insertBookingParticipant(Long bookingId, BookingRequest.Participant participant, Long userId) {
        String QUERY = BookingQueries.INSERT_BOOKING_PARTICIPANT;

        try {
            jdbcTemplate.update(
                    QUERY,
                    bookingId,
                    participant.getFirstName(),
                    participant.getLastName(),
                    participant.getDateOfBirth(),
                    participant.getGender(),             // subquery maps to gender_id
                    participant.getPassportNumber(),
                    participant.getCountry(),            // subquery maps to country_id
                    participant.getEmail(),
                    participant.getMobileNumber(),
                    participant.getEmergencyContactName(),
                    participant.getEmergencyContactPhone(),
                    participant.getEmergencyContactRelationship(),
                    participant.getMedicalConditions(),
                    participant.getAllergies(),
                    participant.getSpecialAssistanceRequired() != null ? participant.getSpecialAssistanceRequired() : false,
                    participant.getAssistantDetails(),
                    null,                                // room_sharing_with (can set later if needed)
                    userId
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while inserting booking participant for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Database error while inserting booking participant");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while inserting booking participant for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while inserting booking participant");
        }
    }

    @Override
    public void insertBookingNote(Long bookingId, BookingRequest.BookingNote note, Long userId) {
        String QUERY = BookingQueries.INSERT_BOOKING_NOTE;

        try {
            jdbcTemplate.update(
                    QUERY,
                    bookingId,
                    note.getNoteType(),
                    note.getNoteText(),
                    userId
            );

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Database error while inserting booking note for bookingId {}",
                    bookingId,
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Database error while inserting booking note"
            );
        } catch (Exception ex) {
            LOGGER.error(
                    "Unexpected error while inserting booking note for bookingId {}",
                    bookingId,
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error while inserting booking note"
            );
        }
    }

    @Override
    public void insertBookingItinerary(
            Long bookingId,
            PackageDayAccommodationPriceDto p,
            LocalDate date,
            Long userId
    ) {
        String QUERY = BookingQueries.INSERT_BOOKING_ITINERARY;

        try {
            jdbcTemplate.update(
                    QUERY,
                    bookingId,
                    p.getDayNumber(),
                    date,
                    p.getTourName(),
                    p.getTourDescription(),
                    null, // start_time
                    null, // end_time
                    p.getHotelName(),
                    null, // included_meals
                    userId
            );

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Database error while inserting booking itinerary for bookingId {} day {}",
                    bookingId,
                    p.getDayNumber(),
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Database error while inserting booking itinerary"
            );
        } catch (Exception ex) {
            LOGGER.error(
                    "Unexpected error while inserting booking itinerary for bookingId {} day {}",
                    bookingId,
                    p.getDayNumber(),
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error while inserting booking itinerary"
            );
        }
    }

    @Override
    public void insertBookingActivities(
            Long bookingId,
            PackageActivityPriceDto a,
            int totalParticipants,
            Long userId
    ) {
        String QUERY = BookingQueries.INSERT_BOOKING_ACTIVITIES;

        try {
            Double pricePerPerson = a.getPriceForeigners() != null
                    ? a.getPriceForeigners()
                    : 0.0;

            Double totalPrice = pricePerPerson * totalParticipants;

            jdbcTemplate.update(
                    QUERY,
                    bookingId,
                    a.getActivityId(),
                    null, // activity_schedule_id
                    null, // activity_date
                    null, // start_time
                    null, // end_time
                    totalParticipants,
                    pricePerPerson,
                    totalPrice,
                    1, // status ID (adjust if needed)
                    userId
            );

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Database error while inserting booking activity for bookingId {} activityId {}",
                    bookingId,
                    a.getActivityId(),
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Database error while inserting booking activity"
            );
        } catch (Exception ex) {
            LOGGER.error(
                    "Unexpected error while inserting booking activity for bookingId {} activityId {}",
                    bookingId,
                    a.getActivityId(),
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error while inserting booking activity"
            );
        }
    }

    @Override
    public void insertBookingInvoice(
            Long bookingId,
            String invoiceNumber,
            LocalDate invoiceDate,
            LocalDate invoiceDueDate,
            Double totalAmount,
            Double taxAmount,
            Double discountAmount,
            Double finalAmount,
            BookingRequest.BookingInvoice invoice,
            Long userId
    ) {
        String QUERY = BookingQueries.INSERT_BOOKING_INVOICE;

        try {
            double safeTax = taxAmount != null ? taxAmount : 0.0;
            double safeDiscount = discountAmount != null ? discountAmount : 0.0;
            double amountPaid = 0.0;
            double balanceDue = totalAmount;

            jdbcTemplate.update(
                    QUERY,
                    bookingId,
                    invoiceNumber,
                    invoiceDate,
                    invoiceDueDate,
                    finalAmount,
                    safeTax,
                    safeDiscount,
                    totalAmount,
                    amountPaid,
                    balanceDue,
                    invoice.getBillingFullName(),
                    invoice.getBillingAddress(),
                    invoice.getBillingEmail(),
                    invoice.getBillingPhone(),
                    1, // status ID
                    userId
            );

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Database error while inserting booking invoice for bookingId {} invoiceNumber {}",
                    bookingId,
                    invoiceNumber,
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Database error while inserting booking invoice"
            );
        } catch (Exception ex) {
            LOGGER.error(
                    "Unexpected error while inserting booking invoice for bookingId {} invoiceNumber {}",
                    bookingId,
                    invoiceNumber,
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error while inserting booking invoice"
            );
        }
    }

    @Override
    public BookingBasicDetailsDto getBookingBasicDetailsByBookingId(Long bookingId) {
        String QUERY = BookingQueries.GET_BOOKING_BASIC_DETAILS_BY_BOOKING_ID;

        try {
            return jdbcTemplate.queryForObject(
                    QUERY,
                    new Object[]{bookingId},
                    (rs, rowNum) -> BookingBasicDetailsDto.builder()
                            .bookingId(rs.getLong("booking_id"))
                            .bookingReference(rs.getString("booking_reference"))

                            // Invoice fields
                            .invoiceNumber(rs.getString("invoice_number"))
                            .invoiceDate(rs.getDate("invoice_date") != null ? rs.getDate("invoice_date").toLocalDate() : null)
                            .dueDate(rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null)
                            .subtotal(rs.getObject("subtotal", Double.class))
                            .taxAmount(rs.getObject("tax_amount", Double.class))
                            .discountAmount(rs.getObject("discount_amount", Double.class))
                            .insuranceAmount(rs.getObject("insurance_amount", Double.class))
                            .packagePrice(rs.getObject("package_price", Double.class))
                            .totalAmount(rs.getObject("total_amount", Double.class))
                            .amountPaid(rs.getObject("amount_paid", Double.class))
                            .balanceDue(rs.getObject("balance_due", Double.class))

                            // Billing info
                            .billingFullName(rs.getString("billing_full_name"))
                            .billingAddress(rs.getString("billing_address"))
                            .billingEmail(rs.getString("billing_email"))
                            .billingPhone(rs.getString("billing_phone"))

                            // Package info
                            .packageName(rs.getString("package_name"))
                            .packageScheduleId(rs.getLong("package_schedule_id"))
                            .assumeStartDate(rs.getDate("assume_start_date") != null ? rs.getDate("assume_start_date").toLocalDate() : null)
                            .assumeEndDate(rs.getDate("assume_end_date") != null ? rs.getDate("assume_end_date").toLocalDate() : null)

                            // Tour info
                            .tourName(rs.getString("tour_name"))
                            .tourDescription(rs.getString("tour_description"))

                            // Booking info
                            .finalAmount(rs.getObject("final_amount", Double.class))
                            .bookingDate(rs.getDate("booking_date") != null ? rs.getDate("booking_date").toLocalDate() : null)
                            .bookingStatus(rs.getString("booking_status"))

                            .build()
            );

        } catch (EmptyResultDataAccessException ex) {
            LOGGER.warn("No booking found for ID: {}", bookingId);
            return null; // or throw a custom DataNotFound exception
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching booking details for ID {}: {}", bookingId, ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching booking details");
        }
    }

    @Override
    public List<BookingActivityDto> getBookingActivityByBookingId(Long bookingId) {
        String QUERY = BookingQueries.GET_BOOKING_ACTIVITIES_BY_BOOKING_ID;

        try {
            return jdbcTemplate.query(
                    QUERY,
                    new Object[]{bookingId},
                    (rs, rowNum) -> BookingActivityDto.builder()
                            .bookingId(rs.getLong("booking_id"))
                            .activityId(rs.getLong("activity_id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .numberOfParticipants(rs.getObject("number_of_participants", Integer.class))
                            .pricePerPerson(rs.getObject("price_per_person", Double.class))
                            .totalPrice(rs.getObject("total_price", Double.class))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching booking activities for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Database error while fetching booking activities");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching booking activities for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching booking activities");
        }
    }

    @Override
    public List<BookingParticipantDto> getBookingParticipantByBookingId(Long bookingId) {
        String QUERY = BookingQueries.GET_BOOKING_PARTICIPANTS_BY_BOOKING_ID;

        try {
            return jdbcTemplate.query(
                    QUERY,
                    new Object[]{bookingId},
                    (rs, rowNum) -> BookingParticipantDto.builder()
                            .bookingId(rs.getLong("booking_id"))
                            .firstName(rs.getString("first_name"))
                            .lastName(rs.getString("last_name"))
                            .dateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null)
                            .gender(rs.getString("name"))  // gender name
                            .passportNumber(rs.getString("passport_number"))
                            .nationality(rs.getString(10)) // country name, column index 10 (or use alias)
                            .email(rs.getString("email"))
                            .mobileNumber(rs.getString("mobile_number"))
                            .medicalConditions(rs.getString("medical_conditions"))
                            .allergies(rs.getString("allergies"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching booking participants for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Database error while fetching booking participants");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching booking participants for bookingId {}", bookingId, ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching booking participants");
        }
    }

    @Override
    public List<BookingFilterResponse> getBookingFilter() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(BookingQueries.GET_BOOKING_FILTER);

        // Use a map to group by tour
        Map<Long, BookingFilterResponse> tourMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            Long tourId = ((Number) row.get("tour_id")).longValue();
            BookingFilterResponse tour = tourMap.getOrDefault(tourId,
                    BookingFilterResponse.builder()
                            .tourId(tourId)
                            .tourName((String) row.get("tour_name"))
                            .tourDescription((String) row.get("tour_description"))
                            .packageDetails(new ArrayList<>())
                            .build()
            );

            // Package
            Long packageId = row.get("package_id") != null ? ((Number) row.get("package_id")).longValue() : null;
            if (packageId != null) {
                BookingFilterResponse.PackageDetails packageDetails = tour.getPackageDetails().stream()
                        .filter(p -> p.getPackageId().equals(packageId))
                        .findFirst()
                        .orElseGet(() -> {
                            BookingFilterResponse.PackageDetails p = BookingFilterResponse.PackageDetails.builder()
                                    .packageId(packageId)
                                    .packageName((String) row.get("package_name"))
                                    .packageDescription((String) row.get("package_description"))
                                    .packageSchedulesDetails(new ArrayList<>())
                                    .build();
                            tour.getPackageDetails().add(p);
                            return p;
                        });

                // Package Schedule
                Long packageScheduleId = row.get("package_schedule_id") != null ? ((Number) row.get("package_schedule_id")).longValue() : null;
                if (packageScheduleId != null) {
                    BookingFilterResponse.PackageSchedulesDetails schedule = BookingFilterResponse.PackageSchedulesDetails.builder()
                            .packageScheduleId(packageScheduleId)
                            .packageScheduleName((String) row.get("package_schedule_name"))
                            .packageScheduleDescription((String) row.get("package_schedule_description"))
                            .startDate((Date) row.get("package_schedule_start_date"))
                            .endDate((Date) row.get("package_schedule_end_date"))
                            .build();
                    packageDetails.getPackageSchedulesDetails().add(schedule);
                }
            }

            tourMap.put(tourId, tour);
        }

        return new ArrayList<>(tourMap.values());
    }

    @Override
    public List<UserBookingSummaryResponse> getBookedTours(Long userId) {
        String sql = BookingQueries.GET_BOOKED_TOURS_BY_USER_ID;
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                UserBookingSummaryResponse.builder()
                        .bookingId(rs.getLong("booking_id"))
                        .bookingReference(rs.getString("booking_reference"))
                        .bookingInvoiceNumber(rs.getString("invoice_number"))
                        .packageName(rs.getString("package_name"))
                        .packageScheduleName(rs.getString("package_schedule_name"))
                        .tourName(rs.getString("tour_name"))
                        .build()
        );
    }

    @Override
    public void bookingAirportTransportation(
            Long bookingId,
            BookingRequest.Transport transport,
            Long userId) {
        String sql = BookingQueries.INSERT_BOOKING_AIRPORT_TRANSPORTATION;

        try {
            jdbcTemplate.update(
                    sql,
                    bookingId,
                    "FLIGHT",
                    transport.getDepartureDate(),
                    transport.getDepartureTime(),
                    transport.getArrivalDate(),
                    transport.getArrivalTime(),
                    transport.getDepartureLocation(),
                    transport.getArrivalLocation(),
                    userId
            );
        } catch (Exception ex) {
            LOGGER.error("Error while inserting airport transportation for bookingId: {}", bookingId, ex);
            throw new RuntimeException("Failed to save airport transportation details", ex);
        }
    }

    @Override
    public List<PendingToursResponse> getPendingBookingToursDetailsById(Long userId) {
        String GET_PENDING_BOOKING_DETAILS_BY_ID = BookingQueries.GET_PENDING_BOOKING_DETAILS_BY_ID;

        try {
            LOGGER.info("Executing query to fetch pending booking tours for user ID: {}", userId);

            // Create a map to store bookings by ID
            Map<Long, PendingToursResponse> bookingMap = new LinkedHashMap<>();

            // 1. Fetch main booking details
            jdbcTemplate.query(GET_PENDING_BOOKING_DETAILS_BY_ID, new Object[]{userId}, (rs) -> {
                Long bookingId = rs.getLong("booking_id");

                PendingToursResponse booking = PendingToursResponse.builder()
                        .bookingId(bookingId)
                        .bookingReference(rs.getString("booking_reference"))
                        .bookingDate(rs.getDate("booking_date").toLocalDate())
                        .bookingStatus(rs.getString("booking_status"))
                        .tourId(rs.getLong("tour_id"))
                        .tourName(rs.getString("tour_name"))
                        .tourDescription(rs.getString("tour_description"))
                        .tourDuration(rs.getInt("tour_duration"))
                        .startLocation(rs.getString("start_location"))
                        .endLocation(rs.getString("end_location"))
//                        .tourType(rs.getString("tour_type"))
//                        .tourCategory(rs.getString("tour_category"))
                        .packageName(rs.getString("package_name"))
                        .packageDescription(rs.getString("package_description"))
                        .packageTotalPrice(rs.getBigDecimal("package_total_price"))
                        .discountPercentage(rs.getBigDecimal("discount_percentage"))
                        .packagePricePerPerson(rs.getBigDecimal("package_price_per_person"))
                        .username(rs.getString("username"))
                        .userFullName(rs.getString("user_full_name"))
                        .email(rs.getString("email"))
                        .mobileNumber1(rs.getString("mobile_number1"))
                        .build();

                bookingMap.put(bookingId, booking);
            });
            LOGGER.info("Successfully fetched {} pending booking tours for user ID: {}",
                    bookingMap.size(), userId);

            return new ArrayList<>(bookingMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching pending booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch pending booking tours from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching pending booking tours for user {}: {}",
                    userId, ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching pending booking tours");
        }
    }

    @Override
    public Long insertTourBookingInquiry(TourBookingInquiryRequest request, Long userId) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        BookingQueries.INSERT_TOUR_BOOKING_INQUIRY,
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setLong(1, request.getTourId());
                if (request.getPackageId() != null) {
                    ps.setLong(2, request.getPackageId());
                } else {
                    ps.setNull(2, Types.BIGINT);
                }
                ps.setString(3, "PENDING_INQUIRY");

                if (userId != null) {
                    ps.setLong(4, userId);
                } else {
                    ps.setNull(4, Types.BIGINT);
                }
                ps.setString(5, request.getName());
                ps.setString(6, request.getEmail());
                ps.setString(7, request.getContactNumber());
                ps.setString(8, request.getCountry());
                if (userId != null) {
                    ps.setLong(9, userId);
                } else {
                    ps.setNull(9, Types.BIGINT);
                }
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                throw new InsertFailedErrorExceptionHandler("Failed to generate inquiry ID");
            }
            return keyHolder.getKey().longValue();
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting tour booking inquiry", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to insert tour booking inquiry", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert tour booking inquiry");
        }
    }

    @Override
    public void insertBookingInquiryToBookings(
            TourBookingInquiryRequest request,
            Long userId,
            String bookingReference) {

        try {

            jdbcTemplate.update(
                    BookingQueries.INSERT_BOOKING_INQUIRY_TO_BOOKINGS,
                    bookingReference,
                    userId,
                    request.getTourId(),
                    request.getPackageId(),
                    "PENDING_INQUIRY",
                    userId
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while inserting booking from inquiry. bookingReference={}", bookingReference, ex);
            throw new InternalServerErrorExceptionHandler("Database error while inserting booking");

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while inserting booking from inquiry. bookingReference={}", bookingReference, ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while inserting booking");
        }
    }

    @Override
    public void cancelledPendingBooking(BookingCancelledRequest bookingCancelledRequest, Long userId) {
        try {
            String updatedStatus = "PENDING_INQUIRY";
            if (bookingCancelledRequest.getBookingStatus().equalsIgnoreCase("PENDING_INQUIRY")) {
                updatedStatus = "PENDING_CANCELLED";
            } else if (bookingCancelledRequest.getBookingStatus().equalsIgnoreCase("PENDING")
                    || bookingCancelledRequest.getBookingStatus().equalsIgnoreCase("IN_PROGRESS")
                    || bookingCancelledRequest.getBookingStatus().equalsIgnoreCase("CONFIRMED")
                    || bookingCancelledRequest.getBookingStatus().equalsIgnoreCase("BOOKING_COMPLETED")
                    || bookingCancelledRequest.getBookingStatus().equalsIgnoreCase("PAID")
            ) {
                updatedStatus = "CANCELLED";
            }
            jdbcTemplate.update(
                    BookingQueries.CANCELLED_BOOKING_PENDING_REQUEST,
                    updatedStatus,
                    userId,
                    bookingCancelledRequest.getBookingId()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while cancelling booking from inquiry. booking id = {}", bookingCancelledRequest.getBookingId(), ex);
            throw new InternalServerErrorExceptionHandler("Database error while cancelling booking");

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while cancelling booking from inquiry. booking id = {}", bookingCancelledRequest.getBookingId(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while cancelling booking");
        }
    }

    @Override
    public BookingStatisticsResponse.Summary getBookingSummaryStatistics() {

        try {
            LOGGER.info("Fetching booking summary statistics");

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_BOOKING_SUMMARY_STATISTICS,
                    (rs, rowNum) ->
                            BookingStatisticsResponse.Summary.builder()
                                    .totalBookings(rs.getLong("total_bookings"))
                                    .totalRevenue(rs.getBigDecimal("total_revenue"))
                                    .activeBookings(rs.getLong("active_bookings"))
                                    .cancelledBookings(rs.getLong("cancelled_bookings"))
                                    .totalTravellers(rs.getLong("total_travellers"))
                                    .averageBookingValue(
                                            rs.getBigDecimal("average_booking_value"))
                                    .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Error fetching booking summary statistics", ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking summary statistics");
        }
    }

    @Override
    public List<BookingStatisticsResponse.MonthlyBookingTrend>
    getMonthlyBookingTrendsStatistics() {

        try {
            LOGGER.info("Fetching monthly booking trends");

            return jdbcTemplate.query(
                    BookingQueries.GET_MONTHLY_BOOKING_TRENDS,
                    (rs, rowNum) ->
                            BookingStatisticsResponse.MonthlyBookingTrend.builder()
                                    .year(rs.getInt("year"))
                                    .month(rs.getInt("month"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Error fetching monthly booking trends", ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch monthly booking trends");
        }
    }

    @Override
    public List<BookingStatisticsResponse.MonthlyRevenueTrend>
    getMonthlyRevenueTrendsStatistics() {

        try {

            LOGGER.info("Fetching monthly revenue trends");

            return jdbcTemplate.query(
                    BookingQueries.GET_MONTHLY_REVENUE_TRENDS,
                    (rs, rowNum) ->
                            BookingStatisticsResponse.MonthlyRevenueTrend.builder()
                                    .year(rs.getInt("year"))
                                    .month(rs.getInt("month"))
                                    .totalRevenue(
                                            rs.getBigDecimal("total_revenue"))
                                    .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Error fetching monthly revenue trends", ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch monthly revenue trends");
        }
    }

    @Override
    public List<BookingStatisticsResponse.BookingStatusDistribution>
    getBookingStatusDistributionsStatistics() {

        try {

            LOGGER.info("Fetching booking status distributions");

            return jdbcTemplate.query(
                    BookingQueries.GET_BOOKING_STATUS_DISTRIBUTION,
                    (rs, rowNum) ->
                            BookingStatisticsResponse
                                    .BookingStatusDistribution
                                    .builder()
                                    .bookingStatusId(
                                            rs.getLong("booking_status_id"))
                                    .bookingStatusName(
                                            rs.getString("booking_status_name"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .percentage(
                                            rs.getBigDecimal("percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Error fetching booking status distribution", ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking status distribution");
        }
    }

    @Override
    public List<BookingStatisticsResponse.BookingFunnel>
    getBookingFunnelsStatistics() {

        try {

            LOGGER.info("Fetching booking funnel statistics");

            return jdbcTemplate.query(
                    BookingQueries.GET_BOOKING_FUNNEL,
                    (rs, rowNum) ->
                            BookingStatisticsResponse.BookingFunnel.builder()
                                    .stepOrder(rs.getInt("step_order"))
                                    .bookingStatusName(
                                            rs.getString("booking_status_name"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .conversionPercentage(
                                            rs.getBigDecimal(
                                                    "conversion_percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Error fetching booking funnel statistics", ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking funnel statistics");
        }
    }

    @Override
    public List<BookingStatisticsResponse.TopTour>
    getTopToursStatistics() {

        try {

            LOGGER.info("Fetching top tours statistics");

            return jdbcTemplate.query(
                    BookingQueries.GET_TOP_TOURS,
                    (rs, rowNum) ->
                            BookingStatisticsResponse.TopTour.builder()
                                    .tourId(rs.getLong("tour_id"))
                                    .tourName(rs.getString("tour_name"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .totalParticipants(
                                            rs.getLong(
                                                    "total_participants"))
                                    .totalRevenue(
                                            rs.getBigDecimal(
                                                    "total_revenue"))
                                    .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Error fetching top tours statistics", ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch top tours statistics");
        }
    }

    @Override
    public List<BookingStatisticsResponse.PopularActivity>
    getPopularActivitiesStatistics() {

        try {

            LOGGER.info("Fetching popular activities statistics");

            return jdbcTemplate.query(
                    BookingQueries.GET_POPULAR_ACTIVITIES,
                    (rs, rowNum) ->
                            BookingStatisticsResponse.PopularActivity.builder()
                                    .activityId(
                                            rs.getLong("activity_id"))
                                    .activityName(
                                            rs.getString("activity_name"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .totalParticipants(
                                            rs.getLong(
                                                    "total_participants"))
                                    .totalRevenue(
                                            rs.getBigDecimal(
                                                    "total_revenue"))
                                    .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Error fetching popular activities statistics", ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch popular activities statistics");
        }
    }
    @Override
    public BookingStatusStatisticsResponse.Summary
    getBookingStatusSummaryStatistics() {

        try {

            LOGGER.info("Fetching booking status summary statistics");

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_BOOKING_STATUS_SUMMARY_STATISTICS,
                    (rs, rowNum) ->
                            BookingStatusStatisticsResponse.Summary.builder()
                                    .totalStatuses(
                                            rs.getLong("total_statuses"))
                                    .activeStatuses(
                                            rs.getLong("active_statuses"))
                                    .mostUsedStatus(
                                            rs.getString("most_used_status"))
                                    .mostUsedStatusCount(
                                            rs.getLong("most_used_status_count"))
                                    .inquiryToBookedPercentage(
                                            rs.getBigDecimal(
                                                    "inquiry_to_booked_percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Error while fetching booking status summary statistics",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking status summary statistics");
        }
    }

    @Override
    public List<BookingStatusStatisticsResponse.StatusDistribution>
    getStatusDistributionsStatistics() {

        try {

            LOGGER.info("Fetching status distributions");

            return jdbcTemplate.query(
                    BookingQueries.GET_STATUS_DISTRIBUTIONS,
                    (rs, rowNum) ->
                            BookingStatusStatisticsResponse
                                    .StatusDistribution
                                    .builder()
                                    .bookingStatusId(
                                            rs.getLong(
                                                    "booking_status_id"))
                                    .bookingStatusName(
                                            rs.getString(
                                                    "booking_status_name"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .percentage(
                                            rs.getBigDecimal(
                                                    "percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error while fetching status distributions",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch status distributions");
        }
    }

    @Override
    public List<BookingStatusStatisticsResponse.StatusFunnel>
    getStatusFunnelsStatistics() {

        try {

            LOGGER.info("Fetching booking status funnel statistics");

            return jdbcTemplate.query(
                    BookingQueries.GET_STATUS_FUNNELS,
                    (rs, rowNum) ->
                            BookingStatusStatisticsResponse
                                    .StatusFunnel
                                    .builder()
                                    .stepOrder(
                                            rs.getInt("step_order"))
                                    .bookingStatusName(
                                            rs.getString(
                                                    "booking_status_name"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .conversionPercentage(
                                            rs.getBigDecimal(
                                                    "conversion_percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error while fetching booking status funnels",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking status funnels");
        }
    }

    @Override
    public List<BookingStatusStatisticsResponse.StatusTrend>
    getStatusTrendsStatistics() {

        try {

            LOGGER.info("Fetching booking status trends");

            return jdbcTemplate.query(
                    BookingQueries.GET_STATUS_TRENDS,
                    (rs, rowNum) ->
                            BookingStatusStatisticsResponse
                                    .StatusTrend
                                    .builder()
                                    .year(
                                            rs.getInt("year"))
                                    .month(
                                            rs.getInt("month"))
                                    .bookingStatusId(
                                            rs.getLong(
                                                    "booking_status_id"))
                                    .bookingStatusName(
                                            rs.getString(
                                                    "booking_status_name"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error while fetching booking status trends",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking status trends");
        }
    }


    @Override
    public List<BookingStatusStatisticsResponse.DropOffStatistics>
    getDropOffStatisticsStatistics() {

        try {

            LOGGER.info("Fetching booking drop-off statistics");

            return jdbcTemplate.query(
                    BookingQueries.GET_DROP_OFF_STATISTICS,
                    (rs, rowNum) ->
                            BookingStatusStatisticsResponse
                                    .DropOffStatistics
                                    .builder()
                                    .bookingStatusName(
                                            rs.getString(
                                                    "booking_status_name"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .percentage(
                                            rs.getBigDecimal(
                                                    "percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error while fetching booking drop-off statistics",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking drop-off statistics");
        }
    }

    @Override
    public BookingAssignStatisticsResponse.Summary
    getBookingAssignSummaryStatistics() {

        try {

            LOGGER.info(
                    "Fetching booking assignment summary statistics");

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_BOOKING_ASSIGN_SUMMARY_STATISTICS,
                    (rs, rowNum) ->
                            BookingAssignStatisticsResponse
                                    .Summary
                                    .builder()
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .assignedBookings(
                                            rs.getLong(
                                                    "assigned_bookings"))
                                    .unassignedBookings(
                                            rs.getLong(
                                                    "unassigned_bookings"))
                                    .totalAssignedEmployees(
                                            rs.getLong(
                                                    "total_assigned_employees"))
                                    .averageBookingsPerEmployee(
                                            rs.getBigDecimal(
                                                    "average_bookings_per_employee"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching booking assignment summary statistics",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking assignment summary statistics");
        }
    }

    @Override
    public List<BookingAssignStatisticsResponse.EmployeeWorkload>
    getEmployeeWorkloadsStatistics() {

        try {

            LOGGER.info("Fetching employee workloads");

            return jdbcTemplate.query(
                    BookingQueries.GET_EMPLOYEE_WORKLOADS,
                    (rs, rowNum) ->
                            BookingAssignStatisticsResponse
                                    .EmployeeWorkload
                                    .builder()
                                    .employeeId(
                                            rs.getLong(
                                                    "employee_id"))
                                    .userId(
                                            rs.getLong(
                                                    "user_id"))
                                    .employeeName(
                                            rs.getString(
                                                    "employee_name"))
                                    .designationName(
                                            rs.getString(
                                                    "designation_name"))
                                    .departmentName(
                                            rs.getString(
                                                    "department_name"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching employee workloads",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch employee workloads");
        }
    }

    @Override
    public List<BookingAssignStatisticsResponse.EmployeeRevenue>
    getEmployeeRevenuesStatistics() {

        try {

            LOGGER.info(
                    "Fetching employee revenues statistics");

            return jdbcTemplate.query(
                    BookingQueries.GET_EMPLOYEE_REVENUES,
                    (rs, rowNum) ->
                            BookingAssignStatisticsResponse
                                    .EmployeeRevenue
                                    .builder()
                                    .employeeId(
                                            rs.getLong(
                                                    "employee_id"))
                                    .userId(
                                            rs.getLong(
                                                    "user_id"))
                                    .employeeName(
                                            rs.getString(
                                                    "employee_name"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .totalRevenue(
                                            rs.getBigDecimal(
                                                    "total_revenue"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching employee revenues statistics",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch employee revenues statistics");
        }
    }

    @Override
    public List<BookingAssignStatisticsResponse.DepartmentDistribution>
    getDepartmentDistributionsStatistics() {

        try {

            LOGGER.info(
                    "Fetching department distributions");

            return jdbcTemplate.query(
                    BookingQueries.GET_DEPARTMENT_DISTRIBUTIONS,
                    (rs, rowNum) ->
                            BookingAssignStatisticsResponse
                                    .DepartmentDistribution
                                    .builder()
                                    .departmentId(
                                            rs.getLong(
                                                    "department_id"))
                                    .departmentName(
                                            rs.getString(
                                                    "department_name"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .percentage(
                                            rs.getBigDecimal(
                                                    "percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching department distributions",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch department distributions");
        }
    }

    @Override
    public List<BookingAssignStatisticsResponse.DesignationDistribution>
    getDesignationDistributionsStatistics() {

        try {

            LOGGER.info(
                    "Fetching designation distributions");

            return jdbcTemplate.query(
                    BookingQueries.GET_DESIGNATION_DISTRIBUTIONS,
                    (rs, rowNum) ->
                            BookingAssignStatisticsResponse
                                    .DesignationDistribution
                                    .builder()
                                    .designationId(
                                            rs.getLong(
                                                    "designation_id"))
                                    .designationName(
                                            rs.getString(
                                                    "designation_name"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .percentage(
                                            rs.getBigDecimal(
                                                    "percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching designation distributions",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch designation distributions");
        }
    }

    @Override
    public List<BookingAssignStatisticsResponse.MonthlyAssignmentTrend>
    getMonthlyAssignmentTrendsStatistics() {

        try {

            LOGGER.info(
                    "Fetching monthly assignment trends");

            return jdbcTemplate.query(
                    BookingQueries.GET_MONTHLY_ASSIGNMENT_TRENDS,
                    (rs, rowNum) ->
                            BookingAssignStatisticsResponse
                                    .MonthlyAssignmentTrend
                                    .builder()
                                    .year(
                                            rs.getInt("year"))
                                    .month(
                                            rs.getInt("month"))
                                    .totalAssignedBookings(
                                            rs.getLong(
                                                    "total_assigned_bookings"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching monthly assignment trends",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch monthly assignment trends");
        }
    }

    @Override
    public List<BookingAssignStatisticsResponse
            .AssignmentStatusDistribution>
    getAssignmentStatusDistributionsStatistics() {

        try {

            LOGGER.info(
                    "Fetching assignment status distributions");

            return jdbcTemplate.query(
                    BookingQueries
                            .GET_ASSIGNMENT_STATUS_DISTRIBUTIONS,
                    (rs, rowNum) ->
                            BookingAssignStatisticsResponse
                                    .AssignmentStatusDistribution
                                    .builder()
                                    .assignmentType(
                                            rs.getString(
                                                    "assignment_type"))
                                    .totalBookings(
                                            rs.getLong(
                                                    "total_bookings"))
                                    .percentage(
                                            rs.getBigDecimal(
                                                    "percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching assignment status distributions",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch assignment status distributions");
        }
    }
    @Override
    public BookingHistoryStatisticsResponse.Summary
    getBookingHistorySummaryStatistics() {

        try {

            LOGGER.info(
                    "Fetching booking history summary statistics");

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_BOOKING_HISTORY_SUMMARY_STATISTICS,
                    (rs, rowNum) ->
                            BookingHistoryStatisticsResponse.Summary.builder()
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .totalRevenue(
                                            rs.getBigDecimal("total_revenue"))
                                    .firstBookingDate(
                                            rs.getDate("first_booking_date") != null
                                                    ? rs.getDate("first_booking_date").toLocalDate()
                                                    : null)
                                    .latestBookingDate(
                                            rs.getDate("latest_booking_date") != null
                                                    ? rs.getDate("latest_booking_date").toLocalDate()
                                                    : null)
                                    .averageMonthlyBookings(
                                            rs.getBigDecimal("average_monthly_bookings"))
                                    .averageMonthlyRevenue(
                                            rs.getBigDecimal("average_monthly_revenue"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching booking history summary statistics",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking history summary statistics");
        }
    }


    @Override
    public List<BookingHistoryStatisticsResponse.BookingGrowthTrend>
    getBookingGrowthTrendsStatistics() {

        try {

            LOGGER.info("Fetching booking growth trends");

            return jdbcTemplate.query(
                    BookingQueries.GET_BOOKING_GROWTH_TRENDS,
                    (rs, rowNum) ->
                            BookingHistoryStatisticsResponse.BookingGrowthTrend
                                    .builder()
                                    .year(rs.getInt("year"))
                                    .month(rs.getInt("month"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching booking growth trends",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking growth trends");
        }
    }


    @Override
    public List<BookingHistoryStatisticsResponse.RevenueGrowthTrend>
    getRevenueGrowthTrendsStatistics() {

        try {

            LOGGER.info("Fetching revenue growth trends");

            return jdbcTemplate.query(
                    BookingQueries.GET_REVENUE_GROWTH_TRENDS,
                    (rs, rowNum) ->
                            BookingHistoryStatisticsResponse.RevenueGrowthTrend
                                    .builder()
                                    .year(rs.getInt("year"))
                                    .month(rs.getInt("month"))
                                    .totalRevenue(
                                            rs.getBigDecimal("total_revenue"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching revenue growth trends",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch revenue growth trends");
        }
    }


    @Override
    public List<BookingHistoryStatisticsResponse.BookingStatusHistory>
    getBookingStatusHistoriesStatistics() {

        try {

            LOGGER.info(
                    "Fetching booking status histories");

            return jdbcTemplate.query(
                    BookingQueries.GET_BOOKING_STATUS_HISTORIES,
                    (rs, rowNum) ->
                            BookingHistoryStatisticsResponse.BookingStatusHistory
                                    .builder()
                                    .year(rs.getInt("year"))
                                    .month(rs.getInt("month"))
                                    .bookingStatusId(
                                            rs.getLong("booking_status_id"))
                                    .bookingStatusName(
                                            rs.getString("booking_status_name"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching booking status histories",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking status histories");
        }
    }


    @Override
    public List<BookingHistoryStatisticsResponse.CancellationTrend>
    getCancellationTrendsStatistics() {

        try {

            LOGGER.info(
                    "Fetching cancellation trends");

            return jdbcTemplate.query(
                    BookingQueries.GET_CANCELLATION_TRENDS,
                    (rs, rowNum) ->
                            BookingHistoryStatisticsResponse.CancellationTrend
                                    .builder()
                                    .year(rs.getInt("year"))
                                    .month(rs.getInt("month"))
                                    .totalCancelledBookings(
                                            rs.getLong(
                                                    "total_cancelled_bookings"))
                                    .cancellationRate(
                                            rs.getBigDecimal(
                                                    "cancellation_rate"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching cancellation trends",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch cancellation trends");
        }
    }


    @Override
    public List<BookingHistoryStatisticsResponse.HistoricalTopTour>
    getHistoricalTopToursStatistics() {

        try {

            LOGGER.info(
                    "Fetching historical top tours");

            return jdbcTemplate.query(
                    BookingQueries.GET_HISTORICAL_TOP_TOURS,
                    (rs, rowNum) ->
                            BookingHistoryStatisticsResponse.HistoricalTopTour
                                    .builder()
                                    .tourId(
                                            rs.getLong("tour_id"))
                                    .tourName(
                                            rs.getString("tour_name"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .totalParticipants(
                                            rs.getLong("total_participants"))
                                    .totalRevenue(
                                            rs.getBigDecimal("total_revenue"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching historical top tours",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch historical top tours");
        }
    }


    @Override
    public List<BookingHistoryStatisticsResponse.CustomerReturnStatistics>
    getCustomerReturnStatisticsStatistics() {

        try {

            LOGGER.info(
                    "Fetching customer return statistics");

            return jdbcTemplate.query(
                    BookingQueries.GET_CUSTOMER_RETURN_STATISTICS,
                    (rs, rowNum) ->
                            BookingHistoryStatisticsResponse
                                    .CustomerReturnStatistics
                                    .builder()
                                    .customerType(
                                            rs.getString(
                                                    "customer_type"))
                                    .totalCustomers(
                                            rs.getLong(
                                                    "total_customers"))
                                    .percentage(
                                            rs.getBigDecimal(
                                                    "percentage"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching customer return statistics",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch customer return statistics");
        }
    }


    @Override
    public List<BookingHistoryStatisticsResponse.PeakBookingPeriod>
    getPeakBookingPeriodsStatistics() {

        try {

            LOGGER.info(
                    "Fetching peak booking periods");

            return jdbcTemplate.query(
                    BookingQueries.GET_PEAK_BOOKING_PERIODS,
                    (rs, rowNum) ->
                            BookingHistoryStatisticsResponse.PeakBookingPeriod
                                    .builder()
                                    .month(rs.getInt("month"))
                                    .monthName(
                                            rs.getString("month_name"))
                                    .totalBookings(
                                            rs.getLong("total_bookings"))
                                    .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching peak booking periods",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch peak booking periods");
        }
    }


}
