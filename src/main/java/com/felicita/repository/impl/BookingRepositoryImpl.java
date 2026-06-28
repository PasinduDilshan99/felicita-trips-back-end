package com.felicita.repository.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InsertFailedErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.dto.*;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.BookingCancelledRequest;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.model.request.bookings.BookingDataRequest;
import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.model.request.bookings.UpdateBookingRequest;
import com.felicita.model.request.bookings.UpdateBookingStatusRequest;
import com.felicita.model.request.bookings.history.*;
import com.felicita.model.request.bookings.status.InsertBookingsStatusesRequest;
import com.felicita.model.request.bookings.status.UpdateBookingsStatusesRequest;
import com.felicita.model.request.bookings.unassign.AssignBookingRequest;
import com.felicita.model.request.bookings.unassign.UnassignBookingDataRequest;
import com.felicita.model.request.bookings.unassign.UnassignBookingRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.bookings.BookingAllDetailsResponse;
import com.felicita.model.response.bookings.BookingBillResponse;
import com.felicita.model.response.bookings.BookingsBasicDetails;
import com.felicita.model.response.bookings.BookingsRequestParamsResponse;
import com.felicita.model.response.bookings.history.BookingHistoryBasicDetailsResponse;
import com.felicita.model.response.bookings.history.BookingHistoryDetailsResponse;
import com.felicita.model.response.bookings.status.BookingStatusBasicDetailsResponse;
import com.felicita.model.response.bookings.status.BookingStatusDetailsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingBasicDetailsResponse;
import com.felicita.model.response.common.BookingIdAndReferenceResponse;
import com.felicita.model.response.statistics.BookingAssignStatisticsResponse;
import com.felicita.model.response.statistics.BookingHistoryStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatusStatisticsResponse;
import com.felicita.queries.ActivitiesQueries;
import com.felicita.queries.BookingQueries;
import com.felicita.queries.TourQueries;
import com.felicita.repository.BookingRepository;
import com.felicita.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;

    @Autowired
    public BookingRepositoryImpl(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    private static final Long TERMINATED_STATUS = 3L;

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

    @Override
    public List<BookingsBasicDetails> getBookingBasicDetailsForParams(
            BookingDataRequest request) {

        try {

            StringBuilder sql = new StringBuilder(
                    BookingQueries.GET_BOOKING_BASIC_DETAILS_FOR_PARAMS);

            List<Object> params = new ArrayList<>();

            if (request.getName() != null
                    && !request.getName().isBlank()) {

                sql.append("""
                        AND (
                            LOWER(t.name) LIKE ?
                            OR LOWER(CONCAT(
                                    COALESCE(u.first_name,''),
                                    ' ',
                                    COALESCE(u.last_name,'')
                                )) LIKE ?
                        )
                        """);

                String search =
                        "%" + request.getName().toLowerCase() + "%";

                params.add(search);
                params.add(search);
            }

            if (request.getBookingReference() != null
                    && !request.getBookingReference().isBlank()) {

                sql.append("""
                        AND LOWER(b.booking_reference)
                        LIKE ?
                        """);

                params.add(
                        "%" +
                                request.getBookingReference().toLowerCase() +
                                "%");
            }

            if (request.getMinPrice() != null) {
                sql.append("""
                        AND b.final_amount >= ?
                        """);

                params.add(request.getMinPrice());
            }

            if (request.getMaxPrice() != null) {
                sql.append("""
                        AND b.final_amount <= ?
                        """);

                params.add(request.getMaxPrice());
            }

            if (request.getDiscountAmount() != null) {
                sql.append("""
                        AND b.discount_amount >= ?
                        """);

                params.add(request.getDiscountAmount());
            }

            if (request.getTravelStartDate() != null) {
                sql.append("""
                        AND b.travel_start_date >= ?
                        """);

                params.add(request.getTravelStartDate());
            }

            if (request.getTravelEndDate() != null) {
                sql.append("""
                        AND b.travel_end_date <= ?
                        """);

                params.add(request.getTravelEndDate());
            }

            if (request.getBookingFrom() != null) {
                sql.append("""
                        AND b.booking_date >= ?
                        """);

                params.add(request.getBookingFrom());
            }

            if (request.getBookingTo() != null) {
                sql.append("""
                        AND b.booking_date <= ?
                        """);

                params.add(request.getBookingTo());
            }

            if (request.getBookingStatusId() != null) {
                sql.append("""
                        AND b.booking_status_id = ?
                        """);

                params.add(request.getBookingStatusId());
            }

            if (request.getTourId() != null) {
                sql.append("""
                        AND b.tour_id = ?
                        """);

                params.add(request.getTourId());
            }

            if (request.getPackageId() != null) {
                sql.append("""
                        AND b.package_id = ?
                        """);

                params.add(request.getPackageId());
            }

            if (request.getAssignTo() != null) {
                sql.append("""
                        AND b.assign_to = ?
                        """);

                params.add(request.getAssignTo());
            }

            String sortBy =
                    request.getSortBy() != null
                            ? request.getSortBy()
                            : "b.created_at";

            String sortDirection =
                    "ASC".equalsIgnoreCase(
                            request.getSortDirection())
                            ? "ASC"
                            : "DESC";

            sql.append("""
                            ORDER BY
                            """)
                    .append(sortBy)
                    .append(" ")
                    .append(sortDirection);

            if (request.getPageSize() != null
                    && request.getPageNumber() != null) {

                sql.append("""
                        LIMIT ?
                        OFFSET ?
                        """);

                params.add(request.getPageSize());

                params.add(
                        request.getPageNumber()
                                * request.getPageSize());
            }

            return jdbcTemplate.query(
                    sql.toString(),
                    params.toArray(),
                    (rs, rowNum) ->
                            BookingsBasicDetails.builder()
                                    .bookingId(
                                            rs.getLong("booking_id"))
                                    .bookingReference(
                                            rs.getString(
                                                    "booking_reference"))
                                    .bookingDate(
                                            rs.getDate("booking_date") != null
                                                    ? rs.getDate("booking_date")
                                                    .toLocalDate()
                                                    : null)
                                    .travelStartDate(
                                            rs.getDate(
                                                    "travel_start_date") != null
                                                    ? rs.getDate(
                                                            "travel_start_date")
                                                    .toLocalDate()
                                                    : null)
                                    .travelEndDate(
                                            rs.getDate(
                                                    "travel_end_date") != null
                                                    ? rs.getDate(
                                                            "travel_end_date")
                                                    .toLocalDate()
                                                    : null)
                                    .userId(
                                            rs.getLong("user_id"))
                                    .username(
                                            rs.getString(
                                                    "username"))
                                    .customerName(
                                            rs.getString(
                                                    "customer_name"))
                                    .email(
                                            rs.getString("email"))
                                    .mobileNumber(
                                            rs.getString(
                                                    "mobile_number1"))
                                    .tourId(
                                            rs.getLong("tour_id"))
                                    .tourName(
                                            rs.getString(
                                                    "tour_name"))
                                    .tourDuration(
                                            rs.getInt(
                                                    "tour_duration"))
                                    .startLocation(
                                            rs.getString(
                                                    "start_location"))
                                    .endLocation(
                                            rs.getString(
                                                    "end_location"))
                                    .packageId(
                                            rs.getLong("package_id"))
                                    .packageName(
                                            rs.getString(
                                                    "package_name"))
                                    .totalPersons(
                                            rs.getInt(
                                                    "total_persons"))
                                    .totalAmount(
                                            rs.getBigDecimal(
                                                    "total_amount"))
                                    .discountAmount(
                                            rs.getBigDecimal(
                                                    "discount_amount"))
                                    .taxAmount(
                                            rs.getBigDecimal(
                                                    "tax_amount"))
                                    .insuranceAmount(
                                            rs.getBigDecimal(
                                                    "insurance_amount"))
                                    .finalAmount(
                                            rs.getBigDecimal(
                                                    "final_amount"))
                                    .insuranceRequired(
                                            rs.getBoolean(
                                                    "insurance_required"))
                                    .bookingStatusId(
                                            rs.getLong(
                                                    "booking_status_id"))
                                    .bookingStatusName(
                                            rs.getString(
                                                    "booking_status_name"))
                                    .assignedEmployeeId(
                                            rs.getObject(
                                                    "assigned_employee_id",
                                                    Long.class))
                                    .assignedEmployeeName(
                                            rs.getString(
                                                    "assigned_employee_name"))
                                    .assignMessage(
                                            rs.getString(
                                                    "assign_message"))
                                    .cancellationDate(
                                            rs.getDate(
                                                    "cancellation_date") != null
                                                    ? rs.getDate(
                                                            "cancellation_date")
                                                    .toLocalDate()
                                                    : null)
                                    .refundAmount(
                                            rs.getBigDecimal(
                                                    "refund_amount"))
                                    .specialRequirements(
                                            rs.getString(
                                                    "special_requirements"))
                                    .dietaryRestrictions(
                                            rs.getString(
                                                    "dietary_restrictions"))
                                    .build());

        } catch (Exception ex) {

            LOGGER.error(
                    "Error fetching booking details",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking details");
        }
    }

    @Override
    public Integer getBookingCountForParams(
            BookingDataRequest request) {

        try {

            StringBuilder sql = new StringBuilder(
                    BookingQueries.GET_BOOKING_COUNT_FOR_PARAMS);

            List<Object> params = new ArrayList<>();

            if (request.getName() != null
                    && !request.getName().isBlank()) {

                sql.append("""
                        AND (
                            LOWER(t.name) LIKE ?
                            OR LOWER(CONCAT(
                                    COALESCE(u.first_name,''),
                                    ' ',
                                    COALESCE(u.last_name,'')
                                )) LIKE ?
                        )
                        """);

                String search =
                        "%" + request.getName().toLowerCase() + "%";

                params.add(search);
                params.add(search);
            }

            if (request.getBookingReference() != null
                    && !request.getBookingReference().isBlank()) {

                sql.append("""
                        AND LOWER(b.booking_reference)
                        LIKE ?
                        """);

                params.add(
                        "%" +
                                request.getBookingReference().toLowerCase() +
                                "%");
            }

            if (request.getMinPrice() != null) {
                sql.append(" AND b.final_amount >= ?");
                params.add(request.getMinPrice());
            }

            if (request.getMaxPrice() != null) {
                sql.append(" AND b.final_amount <= ?");
                params.add(request.getMaxPrice());
            }

            if (request.getDiscountAmount() != null) {
                sql.append(" AND b.discount_amount >= ?");
                params.add(request.getDiscountAmount());
            }

            if (request.getTravelStartDate() != null) {
                sql.append(" AND b.travel_start_date >= ?");
                params.add(request.getTravelStartDate());
            }

            if (request.getTravelEndDate() != null) {
                sql.append(" AND b.travel_end_date <= ?");
                params.add(request.getTravelEndDate());
            }

            if (request.getBookingFrom() != null) {
                sql.append(" AND b.booking_date >= ?");
                params.add(request.getBookingFrom());
            }

            if (request.getBookingTo() != null) {
                sql.append(" AND b.booking_date <= ?");
                params.add(request.getBookingTo());
            }

            if (request.getBookingStatusId() != null) {
                sql.append(" AND b.booking_status_id = ?");
                params.add(request.getBookingStatusId());
            }

            if (request.getTourId() != null) {
                sql.append(" AND b.tour_id = ?");
                params.add(request.getTourId());
            }

            if (request.getPackageId() != null) {
                sql.append(" AND b.package_id = ?");
                params.add(request.getPackageId());
            }

            if (request.getAssignTo() != null) {
                sql.append(" AND b.assign_to = ?");
                params.add(request.getAssignTo());
            }

            Integer count =
                    jdbcTemplate.queryForObject(
                            sql.toString(),
                            params.toArray(),
                            Integer.class);

            return count == null ? 0 : count;

        } catch (Exception ex) {

            LOGGER.error(
                    "Error fetching booking count",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking count");
        }
    }

    @Override
    public BookingsRequestParamsResponse getBookingsParamsData() {

        try {

            LOGGER.info("Fetching booking request params data");

            BookingsRequestParamsResponse response =
                    jdbcTemplate.queryForObject(
                            BookingQueries.GET_BOOKINGS_REQUEST_PARAMS,
                            (rs, rowNum) ->
                                    BookingsRequestParamsResponse.builder()
                                            .minPrice(
                                                    rs.getLong("min_price"))
                                            .maxPrice(
                                                    rs.getLong("max_price"))
                                            .minDiscountAmount(
                                                    rs.getDouble(
                                                            "min_discount_amount"))
                                            .maxDiscountAmount(
                                                    rs.getDouble(
                                                            "max_discount_amount"))
                                            .minBookingDate(
                                                    rs.getDate(
                                                            "min_booking_date") != null
                                                            ? rs.getDate(
                                                                    "min_booking_date")
                                                            .toLocalDate()
                                                            : null)
                                            .maxBookingDate(
                                                    rs.getDate(
                                                            "max_booking_date") != null
                                                            ? rs.getDate(
                                                                    "max_booking_date")
                                                            .toLocalDate()
                                                            : null)
                                            .minTravelStartDate(
                                                    rs.getDate(
                                                            "min_travel_start_date") != null
                                                            ? rs.getDate(
                                                                    "min_travel_start_date")
                                                            .toLocalDate()
                                                            : null)
                                            .maxTravelStartDate(
                                                    rs.getDate(
                                                            "max_travel_start_date") != null
                                                            ? rs.getDate(
                                                                    "max_travel_start_date")
                                                            .toLocalDate()
                                                            : null)
                                            .build());

            if (response == null) {
                return BookingsRequestParamsResponse.builder().build();
            }

            response.setBookingStatuses(
                    getBookingParamStatuses());

            response.setTours(
                    getBookingParamTours());

            response.setPackages(
                    getBookingParamPackages());

            response.setAssignEmployees(
                    getBookingParamAssignEmployees());

            return response;

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching booking request params data",
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking request params data");
        }
    }

    @Override
    public BookingAllDetailsResponse.BookingInformation getBookingInformationById(
            Long bookingId) {

        try {

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_BOOKING_INFORMATION_BY_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse.BookingInformation.builder()
                                    .bookingId(
                                            rs.getLong("booking_id"))
                                    .bookingReference(
                                            rs.getString("booking_reference"))
                                    .bookingDate(
                                            rs.getDate("booking_date") != null
                                                    ? rs.getDate("booking_date").toLocalDate()
                                                    : null)
                                    .travelStartDate(
                                            rs.getDate("travel_start_date") != null
                                                    ? rs.getDate("travel_start_date").toLocalDate()
                                                    : null)
                                    .travelEndDate(
                                            rs.getDate("travel_end_date") != null
                                                    ? rs.getDate("travel_end_date").toLocalDate()
                                                    : null)
                                    .totalPersons(
                                            rs.getObject(
                                                    "total_persons",
                                                    Integer.class))
                                    .totalAmount(
                                            rs.getBigDecimal("total_amount"))
                                    .discountAmount(
                                            rs.getBigDecimal("discount_amount"))
                                    .taxAmount(
                                            rs.getBigDecimal("tax_amount"))
                                    .insuranceAmount(
                                            rs.getBigDecimal("insurance_amount"))
                                    .finalAmount(
                                            rs.getBigDecimal("final_amount"))
                                    .insuranceRequired(
                                            rs.getBoolean("insurance_required"))
                                    .specialRequirements(
                                            rs.getString("special_requirements"))
                                    .dietaryRestrictions(
                                            rs.getString("dietary_restrictions"))
                                    .build());

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Error fetching booking information. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking information");
        }
    }

    @Override
    public BookingAllDetailsResponse.CustomerInformation
    getCustomerInformationByBookingId(
            Long bookingId) {

        try {

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_CUSTOMER_INFORMATION_BY_BOOKING_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse.CustomerInformation.builder()
                                    .userId(
                                            rs.getLong("user_id"))
                                    .username(
                                            rs.getString("username"))
                                    .firstName(
                                            rs.getString("first_name"))
                                    .lastName(
                                            rs.getString("last_name"))
                                    .fullName(
                                            rs.getString("full_name"))
                                    .email(
                                            rs.getString("email"))
                                    .mobileNumber(
                                            rs.getString("mobile_number1"))
                                    .passportNumber(
                                            rs.getString("passport_number"))
                                    .build());

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Error fetching customer information. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch customer information");
        }
    }

    @Override
    public BookingAllDetailsResponse.TourInformation
    getTourInformationByBookingId(
            Long bookingId) {

        try {

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_TOUR_INFORMATION_BY_BOOKING_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse.TourInformation.builder()
                                    .tourId(
                                            rs.getLong("tour_id"))
                                    .tourName(
                                            rs.getString("tour_name"))
                                    .tourDescription(
                                            rs.getString("tour_description"))
                                    .duration(
                                            rs.getObject(
                                                    "duration",
                                                    Integer.class))
                                    .startLocation(
                                            rs.getString("start_location"))
                                    .endLocation(
                                            rs.getString("end_location"))
                                    .latitude(
                                            rs.getObject(
                                                    "latitude",
                                                    Double.class))
                                    .longitude(
                                            rs.getObject(
                                                    "longitude",
                                                    Double.class))
                                    .build());

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Error fetching tour information. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch tour information");
        }
    }

    @Override
    public BookingAllDetailsResponse.PackageInformation
    getPackageInformationByBookingId(
            Long bookingId) {

        try {

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_PACKAGE_INFORMATION_BY_BOOKING_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse.PackageInformation.builder()
                                    .packageId(
                                            rs.getLong("package_id"))
                                    .packageName(
                                            rs.getString("package_name"))
                                    .packageDescription(
                                            rs.getString("package_description"))
                                    .packageTotalPrice(
                                            rs.getBigDecimal("total_price"))
                                    .pricePerPerson(
                                            rs.getBigDecimal("price_per_person"))
                                    .discountPercentage(
                                            rs.getBigDecimal(
                                                    "discount_percentage"))
                                    .build());

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Error fetching package information. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch package information");
        }
    }

    @Override
    public BookingAllDetailsResponse.BookingStatusInformation
    getBookingStatusInformationByBookingId(
            Long bookingId) {

        try {

            return jdbcTemplate.queryForObject(
                    BookingQueries.GET_BOOKING_STATUS_INFORMATION_BY_BOOKING_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse.BookingStatusInformation.builder()
                                    .bookingStatusId(
                                            rs.getLong("id"))
                                    .bookingStatusName(
                                            rs.getString("name"))
                                    .bookingStatusDescription(
                                            rs.getString("description"))
                                    .build());

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Error fetching booking status information. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booking status information");
        }
    }

    @Override
    public BookingAllDetailsResponse.AssignmentInformation
    getAssignmentInformationByBookingId(
            Long bookingId) {

        try {

            List<BookingAllDetailsResponse.AssignmentInformation> assignments =
                    jdbcTemplate.query(
                            BookingQueries.GET_ASSIGNMENT_INFORMATION_BY_BOOKING_ID,
                            new Object[]{bookingId},
                            (rs, rowNum) ->
                                    BookingAllDetailsResponse.AssignmentInformation.builder()
                                            .employeeId(
                                                    rs.getLong(
                                                            "employee_id"))
                                            .employeeUserId(
                                                    rs.getLong(
                                                            "user_id"))
                                            .employeeCode(
                                                    rs.getString(
                                                            "employee_code"))
                                            .employeeName(
                                                    rs.getString(
                                                            "employee_name"))
                                            .departmentName(
                                                    rs.getString(
                                                            "department_name"))
                                            .designationName(
                                                    rs.getString(
                                                            "designation_name"))
                                            .assignMessage(
                                                    rs.getString(
                                                            "assign_message"))
                                            .build());

            return assignments.isEmpty()
                    ? null
                    : assignments.get(0);

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Error fetching assignment information. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch assignment information");
        }
    }

    @Override
    public List<BookingAllDetailsResponse.ParticipantInformation>
    getParticipantsByBookingId(Long bookingId) {

        try {

            return jdbcTemplate.query(
                    BookingQueries.GET_PARTICIPANTS_BY_BOOKING_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse.ParticipantInformation.builder()
                                    .participantId(
                                            rs.getLong("id"))
                                    .firstName(
                                            rs.getString("first_name"))
                                    .lastName(
                                            rs.getString("last_name"))
                                    .fullName(
                                            rs.getString("full_name"))
                                    .dateOfBirth(
                                            rs.getDate("date_of_birth") != null
                                                    ? rs.getDate("date_of_birth")
                                                    .toLocalDate()
                                                    : null)
                                    .gender(
                                            rs.getString("gender_name"))
                                    .nationality(
                                            rs.getString("country_name"))
                                    .passportNumber(
                                            rs.getString("passport_number"))
                                    .email(
                                            rs.getString("email"))
                                    .mobileNumber(
                                            rs.getString("mobile_number"))
                                    .emergencyContactName(
                                            rs.getString(
                                                    "emergency_contact_name"))
                                    .emergencyContactPhone(
                                            rs.getString(
                                                    "emergency_contact_phone"))
                                    .emergencyContactRelationship(
                                            rs.getString(
                                                    "emergency_contact_relationship"))
                                    .medicalConditions(
                                            rs.getString(
                                                    "medical_conditions"))
                                    .allergies(
                                            rs.getString("allergies"))
                                    .specialAssistanceRequired(
                                            rs.getBoolean(
                                                    "special_assistance_required"))
                                    .assistanceDetails(
                                            rs.getString(
                                                    "assistance_details"))
                                    .build());

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching participants. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch participants");
        }
    }

    @Override
    public BookingAllDetailsResponse.CancellationInformation
    getCancellationInformationByBookingId(
            Long bookingId) {

        try {

            List<BookingAllDetailsResponse.CancellationInformation>
                    cancellations =
                    jdbcTemplate.query(
                            BookingQueries
                                    .GET_CANCELLATION_INFORMATION_BY_BOOKING_ID,
                            new Object[]{bookingId},
                            (rs, rowNum) ->
                                    BookingAllDetailsResponse
                                            .CancellationInformation
                                            .builder()
                                            .cancellationDate(
                                                    rs.getDate(
                                                            "cancellation_date")
                                                            != null
                                                            ? rs.getDate(
                                                                    "cancellation_date")
                                                            .toLocalDate()
                                                            : null)
                                            .cancellationReason(
                                                    rs.getString(
                                                            "name"))
                                            .cancellationNotes(
                                                    rs.getString(
                                                            "cancellation_notes"))
                                            .refundAmount(
                                                    rs.getBigDecimal(
                                                            "refund_amount"))
                                            .refundStatus(
                                                    rs.getString(
                                                            "refund_status"))
                                            .build());

            return cancellations.isEmpty()
                    ? null
                    : cancellations.get(0);

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching cancellation information. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch cancellation information");
        }
    }

    @Override
    public List<BookingAllDetailsResponse.AccommodationInformation>
    getAccommodationsByBookingId(
            Long bookingId) {

        try {

            return jdbcTemplate.query(
                    BookingQueries.GET_ACCOMMODATIONS_BY_BOOKING_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse
                                    .AccommodationInformation
                                    .builder()
                                    .accommodationId(
                                            rs.getLong("id"))
                                    .hotelName(
                                            rs.getString("hotel_name"))
                                    .roomType(
                                            rs.getString("room_type"))
                                    .roomNumber(
                                            rs.getString("room_number"))
                                    .confirmationNumber(
                                            rs.getString(
                                                    "confirmation_number"))
                                    .checkInDate(
                                            rs.getDate(
                                                    "check_in_date") != null
                                                    ? rs.getDate(
                                                            "check_in_date")
                                                    .toLocalDate()
                                                    : null)
                                    .checkOutDate(
                                            rs.getDate(
                                                    "check_out_date") != null
                                                    ? rs.getDate(
                                                            "check_out_date")
                                                    .toLocalDate()
                                                    : null)
                                    .build());

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching accommodations. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch accommodations");
        }
    }

    @Override
    public List<BookingAllDetailsResponse.TransportationInformation>
    getTransportationsByBookingId(
            Long bookingId) {

        try {

            return jdbcTemplate.query(
                    BookingQueries.GET_TRANSPORTATIONS_BY_BOOKING_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse
                                    .TransportationInformation
                                    .builder()
                                    .transportationId(
                                            rs.getLong("id"))
                                    .transportType(
                                            rs.getString(
                                                    "transport_type"))
                                    .departureDate(
                                            rs.getDate(
                                                    "departure_date") != null
                                                    ? rs.getDate(
                                                            "departure_date")
                                                    .toLocalDate()
                                                    : null)
                                    .departureTime(
                                            rs.getTime(
                                                    "departure_time") != null
                                                    ? rs.getTime(
                                                            "departure_time")
                                                    .toLocalTime()
                                                    : null)
                                    .arrivalDate(
                                            rs.getDate(
                                                    "arrival_date") != null
                                                    ? rs.getDate(
                                                            "arrival_date")
                                                    .toLocalDate()
                                                    : null)
                                    .arrivalTime(
                                            rs.getTime(
                                                    "arrival_time") != null
                                                    ? rs.getTime(
                                                            "arrival_time")
                                                    .toLocalTime()
                                                    : null)
                                    .departureLocation(
                                            rs.getString(
                                                    "departure_location"))
                                    .arrivalLocation(
                                            rs.getString(
                                                    "arrival_location"))
                                    .carrierName(
                                            rs.getString(
                                                    "carrier_name"))
                                    .referenceNumber(
                                            rs.getString(
                                                    "reference_number"))
                                    .seatNumbers(
                                            rs.getString(
                                                    "seat_numbers"))
                                    .vehicleNumber(
                                            rs.getString(
                                                    "registration_number"))
                                    .build());

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching transportations. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch transportations");
        }
    }

    @Override
    public List<BookingAllDetailsResponse.ActivityInformation>
    getActivitiesByBookingId(
            Long bookingId) {

        try {

            return jdbcTemplate.query(
                    BookingQueries.GET_ACTIVITIES_BY_BOOKING_ID,
                    new Object[]{bookingId},
                    (rs, rowNum) ->
                            BookingAllDetailsResponse
                                    .ActivityInformation
                                    .builder()
                                    .bookingActivityId(
                                            rs.getLong("id"))
                                    .activityId(
                                            rs.getLong(
                                                    "activity_id"))
                                    .activityName(
                                            rs.getString(
                                                    "activity_name"))
                                    .activityDate(
                                            rs.getDate(
                                                    "activity_date") != null
                                                    ? rs.getDate(
                                                            "activity_date")
                                                    .toLocalDate()
                                                    : null)
                                    .startTime(
                                            rs.getTime(
                                                    "start_time") != null
                                                    ? rs.getTime(
                                                            "start_time")
                                                    .toLocalTime()
                                                    : null)
                                    .endTime(
                                            rs.getTime(
                                                    "end_time") != null
                                                    ? rs.getTime(
                                                            "end_time")
                                                    .toLocalTime()
                                                    : null)
                                    .numberOfParticipants(
                                            rs.getObject(
                                                    "number_of_participants",
                                                    Integer.class))
                                    .pricePerPerson(
                                            rs.getBigDecimal(
                                                    "price_per_person"))
                                    .totalPrice(
                                            rs.getBigDecimal(
                                                    "total_price"))
                                    .status(
                                            rs.getString(
                                                    "status_name"))
                                    .build());

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Error fetching activities. BookingId: {}",
                    bookingId,
                    ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activities");
        }
    }

    @Override
    public Long createBooking(
            InsertBookingRequest request,
            String bookingReference,
            Long userId) {

        try {

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {

                PreparedStatement ps =
                        connection.prepareStatement(
                                BookingQueries.INSERT_BOOKING,
                                Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, bookingReference);
                ps.setObject(2, request.getCustomerId());
                ps.setObject(3, request.getPackageScheduleId());
                ps.setObject(4, request.getTotalPersons());
                ps.setBigDecimal(5, request.getTotalAmount());
                ps.setBigDecimal(6, request.getDiscountAmount());
                ps.setBigDecimal(7, request.getTaxAmount());
                ps.setBigDecimal(8, request.getInsuranceAmount());
                ps.setBigDecimal(9, request.getFinalAmount());
                ps.setObject(10, request.getBookingDate());
                ps.setObject(11, request.getTravelStartDate());
                ps.setObject(12, request.getTravelEndDate());
                ps.setObject(13, request.getBookingStatusId());
                ps.setString(14, request.getSpecialRequirements());
                ps.setString(15, request.getDietaryRestrictions());
                ps.setBoolean(
                        16,
                        Boolean.TRUE.equals(
                                request.getInsuranceRequired()));
                ps.setObject(17, userId);
                ps.setObject(18, request.getTourId());
                ps.setObject(19, request.getPackageId());
                ps.setObject(20, request.getAssignTo());
                ps.setString(21, request.getAssignMessage());

                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                throw new InsertFailedErrorExceptionHandler(
                        "Failed to create booking");
            }

            return keyHolder.getKey().longValue();

        } catch (Exception ex) {

            LOGGER.error(
                    "Error creating booking",
                    ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to create booking");
        }
    }

    @Override
    public void addParticipantsToBooking(
            Long bookingId,
            List<InsertBookingRequest.Participant> participants,
            Long userId) {

        if (participants == null || participants.isEmpty()) {
            return;
        }

        try {

            for (InsertBookingRequest.Participant p : participants) {

                jdbcTemplate.update(
                        BookingQueries.ADD_BOOKING_PARTICIPANT,
                        bookingId,
                        p.getFirstName(),
                        p.getLastName(),
                        p.getDateOfBirth(),
                        p.getGenderId(),
                        p.getPassportNumber(),
                        p.getNationalityCountryId(),
                        p.getEmail(),
                        p.getMobileNumber(),
                        p.getEmergencyContactName(),
                        p.getEmergencyContactPhone(),
                        p.getEmergencyContactRelationship(),
                        p.getMedicalConditions(),
                        p.getAllergies(),
                        p.getSpecialAssistanceRequired(),
                        p.getAssistanceDetails(),
                        p.getRoomSharingWith(),
                        p.getStatus(),
                        userId
                );
            }

        } catch (Exception ex) {

            LOGGER.error(
                    "Error adding participants. BookingId: {}",
                    bookingId,
                    ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking participants");
        }
    }

    @Override
    public void addAccommodationsToBooking(
            Long bookingId,
            List<InsertBookingRequest.Accommodation> accommodations,
            Long userId) {

        if (accommodations == null || accommodations.isEmpty()) {
            return;
        }

        try {

            for (InsertBookingRequest.Accommodation a : accommodations) {

                jdbcTemplate.update(
                        BookingQueries.ADD_BOOKING_ACCOMMODATION,
                        bookingId,
                        a.getCheckInDate(),
                        a.getCheckOutDate(),
                        a.getHotelId(),
                        a.getRoomType(),
                        a.getRoomNumber(),
                        a.getConfirmationNumber(),
                        a.getStatus(),
                        userId
                );
            }

        } catch (Exception ex) {

            LOGGER.error("Error adding accommodations. BookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking accommodations");
        }
    }

    @Override
    public void addTransportationsToBooking(
            Long bookingId,
            List<InsertBookingRequest.Transportation> transportations,
            Long userId) {

        if (transportations == null || transportations.isEmpty()) {
            return;
        }

        try {

            for (InsertBookingRequest.Transportation t : transportations) {

                jdbcTemplate.update(
                        BookingQueries.ADD_BOOKING_TRANSPORTATION,
                        bookingId,
                        t.getTransportType(),
                        t.getVehicleId(),
                        t.getDepartureDate(),
                        t.getDepartureTime(),
                        t.getArrivalDate(),
                        t.getArrivalTime(),
                        t.getDepartureLocation(),
                        t.getArrivalLocation(),
                        t.getCarrierName(),
                        t.getReferenceNumber(),
                        t.getSeatNumbers(),
                        t.getStatus(),
                        userId
                );
            }

        } catch (Exception ex) {

            LOGGER.error("Error adding transportations. BookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking transportations");
        }
    }

    @Override
    public void addActivitiesToBooking(
            Long bookingId,
            List<InsertBookingRequest.Activity> activities,
            Long userId) {

        if (activities == null || activities.isEmpty()) {
            return;
        }

        try {

            for (InsertBookingRequest.Activity a : activities) {

                jdbcTemplate.update(
                        BookingQueries.ADD_BOOKING_ACTIVITY,
                        bookingId,
                        a.getActivityId(),
                        a.getActivityScheduleId(),
                        a.getActivityDate(),
                        a.getStartTime(),
                        a.getEndTime(),
                        a.getNumberOfParticipants(),
                        a.getPricePerPerson(),
                        a.getTotalPrice(),
                        a.getStatus(),
                        userId
                );
            }

        } catch (Exception ex) {

            LOGGER.error("Error adding activities. BookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking activities");
        }
    }

    @Override
    public void addDocumentsToBooking(
            Long bookingId,
            List<InsertBookingRequest.BookingDocuments> documents,
            Long userId) {

        if (documents == null || documents.isEmpty()) {
            return;
        }

        try {

            for (InsertBookingRequest.BookingDocuments d : documents) {

                jdbcTemplate.update(
                        BookingQueries.ADD_BOOKING_DOCUMENT,
                        bookingId,
                        d.getDocumentType(),
                        d.getDocumentName(),
                        d.getDocumentUrl(),
                        d.getFileSize(),
                        d.getMimiType(),
                        d.getStatus(),
                        userId
                );
            }

        } catch (Exception ex) {

            LOGGER.error("Error adding documents. BookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking documents");
        }
    }

    @Override
    public void addInsuranceToBooking(
            Long bookingId,
            InsertBookingRequest.BookingInsurance insurance,
            Long userId) {

        if (insurance == null) {
            return;
        }

        try {

            jdbcTemplate.update(
                    BookingQueries.ADD_BOOKING_INSURANCE,
                    bookingId,
                    insurance.getInsuranceProvider(),
                    insurance.getPolicyNumber(),
                    insurance.getCoverageType(),
                    insurance.getPremiumAmount(),
                    insurance.getCoverageDetails(),
                    insurance.getPolicyStartDate(),
                    insurance.getPolicyEndDate(),
                    insurance.getStatus(),
                    userId
            );

        } catch (Exception ex) {

            LOGGER.error("Error adding insurance. BookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking insurance");
        }
    }

    @Override
    public void addNotesToBooking(
            Long bookingId,
            List<InsertBookingRequest.BookingNote> notes,
            Long userId) {

        if (notes == null || notes.isEmpty()) {
            return;
        }

        try {

            for (InsertBookingRequest.BookingNote n : notes) {

                jdbcTemplate.update(
                        BookingQueries.ADD_BOOKING_NOTE,
                        bookingId,
                        n.getNoteType(),
                        n.getNoteText(),
                        n.getIsImportant(),
                        n.getFollowUpDate(),
                        n.getFollowUpComplete(),
                        n.getStatus(),
                        userId
                );
            }

        } catch (Exception ex) {

            LOGGER.error("Error adding notes. BookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking notes");
        }
    }

    @Override
    public void addPriceBreakdownToBooking(
            Long bookingId,
            List<InsertBookingRequest.BookingPriceBreakDown> priceBreakDowns,
            Long userId) {

        if (priceBreakDowns == null || priceBreakDowns.isEmpty()) {
            return;
        }

        try {

            for (InsertBookingRequest.BookingPriceBreakDown p : priceBreakDowns) {

                jdbcTemplate.update(
                        BookingQueries.ADD_BOOKING_PRICE_BREAKDOWN,
                        bookingId,
                        p.getItemType(),
                        p.getItemName(),
                        p.getItemDescription(),
                        p.getQuantity(),
                        p.getUnitPrice(),
                        p.getTotalPrice(),
                        p.getStatus(),
                        userId
                );
            }

        } catch (Exception ex) {

            LOGGER.error("Error adding price breakdown. BookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save price breakdown");
        }
    }

    @Override
    public void addBookingInvoiceToBooking(
            Long bookingId,
            String invoiceReference,
            InsertBookingRequest.BookingInvoice invoice,
            Long userId) {

        if (invoice == null) {
            return;
        }

        try {

            jdbcTemplate.update(
                    BookingQueries.ADD_BOOKING_INVOICE,
                    bookingId,
                    invoiceReference,
                    LocalDate.now(),
                    invoice.getDueDate(),
                    invoice.getSubTotal(),
                    invoice.getTaxAmount(),
                    invoice.getDiscountAmount(),
                    invoice.getTotalAmount(),
                    invoice.getAmountPaid(),
                    invoice.getBalanceDue(),
                    invoice.getBillingFullName(),
                    invoice.getBillingAddress(),
                    invoice.getBillingEmail(),
                    invoice.getBillingPhone(),
                    invoice.getStatus(),
                    invoice.getInsuranceAmount(),
                    userId
            );

        } catch (Exception ex) {

            LOGGER.error("Error adding invoice. BookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking invoice");
        }
    }

    @Override
    public void addItinerariesToBooking(
            Long bookingId,
            List<InsertBookingRequest.BookingItinerary> bookingItineraries,
            Long userId) {

        if (bookingItineraries == null || bookingItineraries.isEmpty()) {
            return;
        }

        String sql = BookingQueries.ADD_BOOKING_ITINERARY;

        try {

            for (InsertBookingRequest.BookingItinerary i : bookingItineraries) {

                jdbcTemplate.update(
                        sql,
                        bookingId,
                        i.getDayNumber(),
                        i.getItineraryDate(),
                        i.getTitle(),
                        i.getDescription(),
                        i.getStartTime(),
                        i.getEndTime(),
                        i.getLocation(),
                        i.getIncludedMeals(),
                        i.getStatus() != null ? i.getStatus() : 1,
                        userId,
                        userId
                );
            }

        } catch (Exception ex) {

            LOGGER.error("Error adding itineraries for bookingId: {}", bookingId, ex);

            throw new InsertFailedErrorExceptionHandler(
                    "Failed to save booking itineraries");
        }
    }

    @Override
    public List<BookingAllDetailsResponse.BookingDocuments> getBookingDocumentsByBookingId(Long bookingId) {

        String sql = """
                    SELECT bd.document_name, bd.document_type, bd.document_url,
                           bd.file_size, bd.mime_type, cs.name AS status
                    FROM booking_documents bd
                    LEFT JOIN common_status cs ON bd.status = cs.id
                    WHERE bd.booking_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        BookingAllDetailsResponse.BookingDocuments.builder()
                                .documentName(rs.getString("document_name"))
                                .documentType(rs.getString("document_type"))
                                .documentUrl(rs.getString("document_url"))
                                .fileSize(rs.getDouble("file_size"))
                                .mimiType(rs.getString("mime_type"))
                                .status(rs.getString("status"))
                                .build(),
                bookingId
        );
    }

    @Override
    public BookingAllDetailsResponse.BookingInsurance getBookingInsuranceByBookingId(Long bookingId) {

        String sql = """
                    SELECT bi.insurance_provider, bi.policy_number, bi.coverage_type,
                           bi.coverage_details, bi.premium_amount,
                           bi.policy_start_date, bi.policy_end_date,
                           cs.name AS status
                    FROM booking_insurance bi
                    LEFT JOIN common_status cs ON bi.status_id = cs.id
                    WHERE bi.booking_id = ?
                    LIMIT 1
                """;

        return jdbcTemplate.query(sql, rs -> {
            if (!rs.next()) return null;

            return BookingAllDetailsResponse.BookingInsurance.builder()
                    .insuranceProvider(rs.getString("insurance_provider"))
                    .policyNumber(rs.getString("policy_number"))
                    .coverageType(rs.getString("coverage_type"))
                    .coverageDetails(rs.getString("coverage_details"))
                    .premiumAmount(rs.getDouble("premium_amount"))
                    .policyStartDate(rs.getDate("policy_start_date") != null ?
                            rs.getDate("policy_start_date").toLocalDate() : null)
                    .policyEndDate(rs.getDate("policy_end_date") != null ?
                            rs.getDate("policy_end_date").toLocalDate() : null)
                    .status(rs.getString("status"))
                    .build();
        }, bookingId);
    }

    @Override
    public List<BookingAllDetailsResponse.BookingItinerary> getBookingItineraryByBookingId(Long bookingId) {

        String sql = """
                    SELECT bi.day_number, bi.itinerary_date, bi.title, bi.description,
                           bi.start_time, bi.end_time, bi.location, bi.included_meals,
                           cs.name AS status
                    FROM booking_itinerary bi
                    LEFT JOIN common_status cs ON bi.status_id = cs.id
                    WHERE bi.booking_id = ?
                    ORDER BY bi.day_number ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        BookingAllDetailsResponse.BookingItinerary.builder()
                                .dayNumber(rs.getInt("day_number"))
                                .itineraryDate(rs.getDate("itinerary_date").toLocalDate())
                                .title(rs.getString("title"))
                                .description(rs.getString("description"))
                                .startTime(rs.getTime("start_time") != null ?
                                        rs.getTime("start_time").toLocalTime() : null)
                                .endTime(rs.getTime("end_time") != null ?
                                        rs.getTime("end_time").toLocalTime() : null)
                                .location(rs.getString("location"))
                                .includedMeals(rs.getString("included_meals"))
                                .status(rs.getString("status"))
                                .build(),
                bookingId
        );
    }

    @Override
    public List<BookingAllDetailsResponse.BookingNote> getBookingNoteByBookingId(Long bookingId) {

        String sql = """
                    SELECT note_type, note_text, is_important,
                           follow_up_date, follow_up_completed,
                           cs.name AS status
                    FROM booking_notes bn
                    LEFT JOIN common_status cs ON bn.status_id = cs.id
                    WHERE bn.booking_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        BookingAllDetailsResponse.BookingNote.builder()
                                .noteType(rs.getString("note_type"))
                                .noteText(rs.getString("note_text"))
                                .isImportant(rs.getBoolean("is_important"))
                                .followUpDate(rs.getDate("follow_up_date") != null ?
                                        rs.getDate("follow_up_date").toLocalDate() : null)
                                .followUpComplete(rs.getBoolean("follow_up_completed"))
                                .status(rs.getString("status"))
                                .build(),
                bookingId
        );
    }

    @Override
    public List<BookingAllDetailsResponse.BookingPriceBreakDown> getBookingPriceBreakDownByBookingId(Long bookingId) {

        String sql = """
                    SELECT item_type, item_name, item_description,
                           quantity, unit_price, total_price,
                           cs.name AS status
                    FROM booking_price_breakdown bp
                    LEFT JOIN common_status cs ON bp.status_id = cs.id
                    WHERE bp.booking_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        BookingAllDetailsResponse.BookingPriceBreakDown.builder()
                                .itemType(rs.getString("item_type"))
                                .itemName(rs.getString("item_name"))
                                .itemDescription(rs.getString("item_description"))
                                .quantity(rs.getInt("quantity"))
                                .unitPrice(rs.getDouble("unit_price"))
                                .totalPrice(rs.getDouble("total_price"))
                                .status(rs.getString("status"))
                                .build(),
                bookingId
        );
    }

    @Override
    public BookingAllDetailsResponse.BookingInvoice getBookingInvoiceByBookingId(Long bookingId) {

        String sql = """
                    SELECT due_date, subtotal, tax_amount, total_amount,
                           discount_amount, insurance_amount, amount_paid,
                           balance_due, billing_full_name, billing_address,
                           billing_email, billing_phone,
                           cs.name AS status
                    FROM booking_invoices bi
                    LEFT JOIN common_status cs ON bi.status = cs.id
                    WHERE bi.booking_id = ?
                    LIMIT 1
                """;

        return jdbcTemplate.query(sql, rs -> {
            if (!rs.next()) return null;

            return BookingAllDetailsResponse.BookingInvoice.builder()
                    .dueDate(rs.getDate("due_date").toLocalDate())
                    .subTotal(rs.getDouble("subtotal"))
                    .taxAmount(rs.getDouble("tax_amount"))
                    .totalAmount(rs.getDouble("total_amount"))
                    .discountAmount(rs.getDouble("discount_amount"))
                    .insuranceAmount(rs.getDouble("insurance_amount"))
                    .amountPaid(rs.getDouble("amount_paid"))
                    .balanceDue(rs.getDouble("balance_due"))
                    .billingFullName(rs.getString("billing_full_name"))
                    .billingAddress(rs.getString("billing_address"))
                    .billingEmail(rs.getString("billing_email"))
                    .billingPhone(rs.getString("billing_phone"))
                    .status(rs.getString("status"))
                    .build();
        }, bookingId);
    }

    @Override
    public void updateBookingBasicInformation(UpdateBookingRequest request, Long userId) {

        jdbcTemplate.update(
                BookingQueries.UPDATE_BOOKING_BASIC_INFORMATION,
                request.getCustomerId(),
                request.getTourId(),
                request.getPackageId(),
                request.getPackageScheduleId(),
                request.getBookingDate(),
                request.getTravelStartDate(),
                request.getTravelEndDate(),
                request.getTotalPersons(),
                request.getTotalAmount(),
                request.getDiscountAmount(),
                request.getTaxAmount(),
                request.getInsuranceAmount(),
                request.getFinalAmount(),
                request.getInsuranceRequired(),
                request.getBookingStatusId(),
                request.getSpecialRequirements(),
                request.getDietaryRestrictions(),
                request.getAssignTo(),
                request.getAssignMessage(),
                userId,
                request.getBookingId()
        );
    }

    @Override
    public void removeParticipantsFromBooking(Long bookingId,
                                              List<Long> removeParticipants,
                                              Long userId) {

        if (CollectionUtils.isEmpty(removeParticipants)) {
            return;
        }

        for (Long participantId : removeParticipants) {
            jdbcTemplate.update(
                    BookingQueries.TERMINATE_BOOKING_PARTICIPANT,
                    TERMINATED_STATUS,
                    userId,
                    participantId,
                    bookingId
            );
        }
    }

    @Override
    public void updateParticipantsOfBooking(
            Long bookingId,
            List<UpdateBookingRequest.UpdateParticipant> participants,
            Long userId) {

        if (CollectionUtils.isEmpty(participants)) {
            return;
        }

        for (UpdateBookingRequest.UpdateParticipant participant : participants) {

            jdbcTemplate.update(
                    BookingQueries.UPDATE_BOOKING_PARTICIPANT,
                    participant.getFirstName(),
                    participant.getLastName(),
                    participant.getDateOfBirth(),
                    participant.getGenderId(),
                    participant.getPassportNumber(),
                    participant.getNationalityCountryId(),
                    participant.getEmail(),
                    participant.getMobileNumber(),
                    participant.getEmergencyContactName(),
                    participant.getEmergencyContactPhone(),
                    participant.getEmergencyContactRelationship(),
                    participant.getMedicalConditions(),
                    participant.getAllergies(),
                    participant.getSpecialAssistanceRequired(),
                    participant.getAssistanceDetails(),
                    participant.getRoomSharingWith(),
                    participant.getStatus(),
                    userId,
                    participant.getParticipantId(),
                    bookingId
            );
        }
    }

    @Override
    public void removeAccommodationsFromBooking(
            Long bookingId,
            List<Long> removeAccommodations,
            Long userId) {

        if (CollectionUtils.isEmpty(removeAccommodations)) {
            return;
        }

        for (Long accommodationId : removeAccommodations) {

            jdbcTemplate.update(
                    BookingQueries.TERMINATE_BOOKING_ACCOMMODATION,
                    TERMINATED_STATUS,
                    userId,
                    accommodationId,
                    bookingId
            );
        }
    }

    @Override
    public void updateAccommodationsOfBooking(
            Long bookingId,
            List<UpdateBookingRequest.UpdateAccommodation> accommodations,
            Long userId) {

        if (CollectionUtils.isEmpty(accommodations)) {
            return;
        }

        for (UpdateBookingRequest.UpdateAccommodation accommodation : accommodations) {

            jdbcTemplate.update(
                    BookingQueries.UPDATE_BOOKING_ACCOMMODATION,
                    accommodation.getCheckInDate(),
                    accommodation.getCheckOutDate(),
                    accommodation.getHotelId(),
                    accommodation.getRoomType(),
                    accommodation.getRoomNumber(),
                    accommodation.getConfirmationNumber(),
                    accommodation.getStatus(),
                    userId,
                    accommodation.getAccommodationId(),
                    bookingId
            );
        }
    }

    @Override
    public void removeTransportationsFromBooking(
            Long bookingId,
            List<Long> removeTransportations,
            Long userId) {

        if (CollectionUtils.isEmpty(removeTransportations)) {
            return;
        }

        for (Long transportationId : removeTransportations) {

            jdbcTemplate.update(
                    BookingQueries.TERMINATE_BOOKING_TRANSPORTATION,
                    TERMINATED_STATUS,
                    userId,
                    transportationId,
                    bookingId
            );
        }
    }

    @Override
    public void updateTransportationsOfBooking(
            Long bookingId,
            List<UpdateBookingRequest.UpdateTransportation> transportations,
            Long userId) {

        if (CollectionUtils.isEmpty(transportations)) {
            return;
        }

        for (UpdateBookingRequest.UpdateTransportation transportation : transportations) {

            jdbcTemplate.update(
                    BookingQueries.UPDATE_BOOKING_TRANSPORTATION,
                    transportation.getTransportType(),
                    transportation.getVehicleId(),
                    transportation.getDepartureDate(),
                    transportation.getDepartureTime(),
                    transportation.getArrivalDate(),
                    transportation.getArrivalTime(),
                    transportation.getDepartureLocation(),
                    transportation.getArrivalLocation(),
                    transportation.getCarrierName(),
                    transportation.getReferenceNumber(),
                    transportation.getSeatNumbers(),
                    transportation.getStatus(),
                    userId,
                    transportation.getTransportationId(),
                    bookingId
            );
        }
    }

    @Override
    public void removeActivitiesFromBooking(Long bookingId,
                                            List<Long> removeActivities,
                                            Long userId) {

        if (CollectionUtils.isEmpty(removeActivities)) {
            return;
        }

        for (Long activityId : removeActivities) {

            jdbcTemplate.update(
                    BookingQueries.TERMINATE_BOOKING_ACTIVITY,
                    TERMINATED_STATUS,
                    userId,
                    activityId,
                    bookingId
            );
        }
    }

    @Override
    public void updateActivitiesOfBooking(
            Long bookingId,
            List<UpdateBookingRequest.UpdateActivity> updateActivities,
            Long userId) {

        if (CollectionUtils.isEmpty(updateActivities)) {
            return;
        }

        for (UpdateBookingRequest.UpdateActivity activity : updateActivities) {

            jdbcTemplate.update(
                    BookingQueries.UPDATE_BOOKING_ACTIVITY,
                    activity.getActivityId(),
                    activity.getActivityScheduleId(),
                    activity.getActivityDate(),
                    activity.getStartTime(),
                    activity.getEndTime(),
                    activity.getNumberOfParticipants(),
                    activity.getPricePerPerson(),
                    activity.getTotalPrice(),
                    activity.getStatus(),
                    userId,
                    activity.getBookingActivityId(),
                    bookingId
            );
        }
    }

    @Override
    public void removeDocumentsFromBooking(Long bookingId,
                                           List<Long> removeDocuments,
                                           Long userId) {

        if (CollectionUtils.isEmpty(removeDocuments)) {
            return;
        }

        for (Long documentId : removeDocuments) {

            jdbcTemplate.update(
                    BookingQueries.TERMINATE_BOOKING_DOCUMENT,
                    TERMINATED_STATUS,
                    userId,
                    documentId,
                    bookingId
            );
        }
    }

    @Override
    public void updateDocumentsOfBooking(
            Long bookingId,
            List<UpdateBookingRequest.UpdateBookingDocuments> updateDocuments,
            Long userId) {

        if (CollectionUtils.isEmpty(updateDocuments)) {
            return;
        }

        for (UpdateBookingRequest.UpdateBookingDocuments document : updateDocuments) {

            jdbcTemplate.update(
                    BookingQueries.UPDATE_BOOKING_DOCUMENT,
                    document.getDocumentName(),
                    document.getDocumentType(),
                    document.getDocumentUrl(),
                    document.getFileSize(),
                    document.getMimiType(),
                    document.getStatus(),
                    userId,
                    document.getDocumentId(),
                    bookingId
            );
        }
    }

    @Override
    public void updateInsuranceOfBooking(
            Long bookingId,
            UpdateBookingRequest.UpdateBookingInsurance insurance,
            Long userId) {

        if (insurance == null) {
            return;
        }

        jdbcTemplate.update(
                BookingQueries.UPDATE_BOOKING_INSURANCE,
                insurance.getInsuranceProvider(),
                insurance.getPolicyNumber(),
                insurance.getCoverageType(),
                insurance.getPremiumAmount(),
                insurance.getCoverageDetails(),
                insurance.getPolicyStartDate(),
                insurance.getPolicyEndDate(),
                insurance.getStatus(),
                userId,
                insurance.getInsuranceId(),
                bookingId
        );
    }

    @Override
    public void removeInsuranceFromBooking(Long bookingId,
                                           Long removeBookingInsurance,
                                           Long userId) {

        if (removeBookingInsurance == null) {
            return;
        }

        jdbcTemplate.update(
                BookingQueries.TERMINATE_BOOKING_INSURANCE,
                TERMINATED_STATUS,
                userId,
                removeBookingInsurance,
                bookingId
        );
    }

    @Override
    public void removeItinerariesFromBooking(Long bookingId,
                                             List<Long> removeBookingItineraries,
                                             Long userId) {

        if (CollectionUtils.isEmpty(removeBookingItineraries)) {
            return;
        }

        for (Long itineraryId : removeBookingItineraries) {

            jdbcTemplate.update(
                    BookingQueries.TERMINATE_BOOKING_ITINERARY,
                    TERMINATED_STATUS,
                    userId,
                    itineraryId,
                    bookingId
            );
        }
    }

    @Override
    public void updateItinerariesOfBooking(
            Long bookingId,
            List<UpdateBookingRequest.UpdateBookingItinerary> updateBookingItineraries,
            Long userId) {

        if (CollectionUtils.isEmpty(updateBookingItineraries)) {
            return;
        }

        for (UpdateBookingRequest.UpdateBookingItinerary itinerary : updateBookingItineraries) {

            jdbcTemplate.update(
                    BookingQueries.UPDATE_BOOKING_ITINERARY,
                    itinerary.getDayNumber(),
                    itinerary.getItineraryDate(),
                    itinerary.getTitle(),
                    itinerary.getDescription(),
                    itinerary.getStartTime(),
                    itinerary.getEndTime(),
                    itinerary.getLocation(),
                    itinerary.getIncludedMeals(),
                    itinerary.getStatus(),
                    userId,
                    itinerary.getItineraryId(),
                    bookingId
            );
        }
    }

    @Override
    public void removeNotesFromBooking(Long bookingId,
                                       List<Long> removeBookingNotes,
                                       Long userId) {

        if (CollectionUtils.isEmpty(removeBookingNotes)) {
            return;
        }

        for (Long noteId : removeBookingNotes) {

            jdbcTemplate.update(
                    BookingQueries.TERMINATE_BOOKING_NOTE,
                    TERMINATED_STATUS,
                    userId,
                    noteId,
                    bookingId
            );
        }
    }

    @Override
    public void updateNotesOfBooking(
            Long bookingId,
            List<UpdateBookingRequest.UpdateBookingNote> updateBookingNotes,
            Long userId) {

        if (CollectionUtils.isEmpty(updateBookingNotes)) {
            return;
        }

        for (UpdateBookingRequest.UpdateBookingNote note : updateBookingNotes) {

            jdbcTemplate.update(
                    BookingQueries.UPDATE_BOOKING_NOTE,
                    note.getNoteType(),
                    note.getNoteText(),
                    note.getIsImportant(),
                    note.getFollowUpDate(),
                    note.getFollowUpComplete(),
                    note.getStatus(),
                    userId,
                    note.getNoteId(),
                    bookingId
            );
        }
    }

    @Override
    public void removePriceBreakdownFromBooking(Long bookingId,
                                                List<Long> removePriceBreakDowns,
                                                Long userId) {

        if (CollectionUtils.isEmpty(removePriceBreakDowns)) {
            return;
        }

        for (Long priceBreakDownId : removePriceBreakDowns) {

            jdbcTemplate.update(
                    BookingQueries.TERMINATE_BOOKING_PRICE_BREAKDOWN,
                    TERMINATED_STATUS,
                    userId,
                    priceBreakDownId,
                    bookingId
            );
        }
    }

    @Override
    public void updatePriceBreakdownOfBooking(
            Long bookingId,
            List<UpdateBookingRequest.UpdateBookingPriceBreakDown> updatePriceBreakDowns,
            Long userId) {

        if (CollectionUtils.isEmpty(updatePriceBreakDowns)) {
            return;
        }

        for (UpdateBookingRequest.UpdateBookingPriceBreakDown breakdown : updatePriceBreakDowns) {

            jdbcTemplate.update(
                    BookingQueries.UPDATE_BOOKING_PRICE_BREAKDOWN,
                    breakdown.getItemType(),
                    breakdown.getItemName(),
                    breakdown.getItemDescription(),
                    breakdown.getQuantity(),
                    breakdown.getUnitPrice(),
                    breakdown.getTotalPrice(),
                    breakdown.getStatus(),
                    userId,
                    breakdown.getPriceBreakDownId(),
                    bookingId
            );
        }
    }

    @Override
    public void updateBookingInvoiceOfBooking(
            Long bookingId,
            UpdateBookingRequest.UpdateBookingInvoice invoice,
            Long userId) {

        if (invoice == null) {
            return;
        }

        jdbcTemplate.update(
                BookingQueries.UPDATE_BOOKING_INVOICE,
                invoice.getDueDate(),
                invoice.getSubTotal(),
                invoice.getTaxAmount(),
                invoice.getTotalAmount(),
                invoice.getDiscountAmount(),
                invoice.getInsuranceAmount(),
                invoice.getAmountPaid(),
                invoice.getBalanceDue(),
                invoice.getBillingFullName(),
                invoice.getBillingAddress(),
                invoice.getBillingEmail(),
                invoice.getBillingPhone(),
                invoice.getStatus(),
                userId,
                invoice.getInvoiceId(),
                bookingId
        );
    }

    @Override
    public void removeBookingInvoiceFromBooking(Long bookingId,
                                                Long removeBookingInvoice,
                                                Long userId) {

        if (removeBookingInvoice == null) {
            return;
        }

        jdbcTemplate.update(
                BookingQueries.TERMINATE_BOOKING_INVOICE,
                TERMINATED_STATUS,
                userId,
                removeBookingInvoice,
                bookingId
        );
    }

    @Override
    public BookingsBasicDetails getBookingBasicDetails(CommonIdRequest bookingId) {
        try {

            String sql = BookingQueries.GET_BOOKING_BASIC_DETAILS_FOR_BOOKING_ID;

            List<Object> params = new ArrayList<>();
            params.add(bookingId.getId());
            List<BookingsBasicDetails> result = jdbcTemplate.query(
                    sql,
                    params.toArray(),
                    (rs, rowNum) ->
                            BookingsBasicDetails.builder()
                                    .bookingId(rs.getLong("booking_id"))
                                    .bookingReference(rs.getString("booking_reference"))
                                    .bookingDate(
                                            rs.getDate("booking_date") != null
                                                    ? rs.getDate("booking_date").toLocalDate()
                                                    : null)
                                    .travelStartDate(
                                            rs.getDate("travel_start_date") != null
                                                    ? rs.getDate("travel_start_date").toLocalDate()
                                                    : null)
                                    .travelEndDate(
                                            rs.getDate("travel_end_date") != null
                                                    ? rs.getDate("travel_end_date").toLocalDate()
                                                    : null)

                                    .userId(rs.getLong("user_id"))
                                    .username(rs.getString("username"))
                                    .customerName(rs.getString("customer_name"))
                                    .email(rs.getString("email"))
                                    .mobileNumber(rs.getString("mobile_number1"))

                                    .tourId(rs.getLong("tour_id"))
                                    .tourName(rs.getString("tour_name"))
                                    .tourDuration(rs.getInt("tour_duration"))
                                    .startLocation(rs.getString("start_location"))
                                    .endLocation(rs.getString("end_location"))

                                    .packageId(rs.getLong("package_id"))
                                    .packageName(rs.getString("package_name"))

                                    .totalPersons(rs.getInt("total_persons"))
                                    .totalAmount(rs.getBigDecimal("total_amount"))
                                    .discountAmount(rs.getBigDecimal("discount_amount"))
                                    .taxAmount(rs.getBigDecimal("tax_amount"))
                                    .insuranceAmount(rs.getBigDecimal("insurance_amount"))
                                    .finalAmount(rs.getBigDecimal("final_amount"))

                                    .insuranceRequired(rs.getObject("insurance_required", Boolean.class))
                                    .bookingStatusId(rs.getObject("booking_status_id", Long.class))
                                    .bookingStatusName(rs.getString("booking_status_name"))

                                    .assignedEmployeeId(rs.getObject("assigned_employee_id", Long.class))
                                    .assignedEmployeeName(rs.getString("assigned_employee_name"))
                                    .assignMessage(rs.getString("assign_message"))

                                    .cancellationDate(
                                            rs.getDate("cancellation_date") != null
                                                    ? rs.getDate("cancellation_date").toLocalDate()
                                                    : null)

                                    .refundAmount(rs.getBigDecimal("refund_amount"))
                                    .specialRequirements(rs.getString("special_requirements"))
                                    .dietaryRestrictions(rs.getString("dietary_restrictions"))

                                    .build()
            );

            return result.isEmpty() ? null : result.get(0);

        } catch (Exception ex) {
            LOGGER.error("Error fetching booking details", ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch booking details");
        }
    }

    @Override
    public void updateBookingStatus(UpdateBookingStatusRequest request, Long userId) {

        Long statusId = jdbcTemplate.queryForObject(
                "SELECT id FROM booking_status WHERE name = ?",
                Long.class,
                request.getBookingStatus()
        );

        if (statusId == null) {
            throw new IllegalArgumentException("Invalid booking status: " + request.getBookingStatus());
        }

        jdbcTemplate.update(
                BookingQueries.UPDATE_BOOKING_STATUS,
                statusId,
                userId,
                request.getBookingId()
        );
    }

    @Override
    public List<BookingStatusBasicDetailsResponse> getBookingsStatuses() {

        String sql = """
                    SELECT 
                        bs.id AS status_id,
                        bs.name AS status_name,
                        bs.description,
                        cs.name AS status
                    FROM booking_status bs
                    LEFT JOIN common_status cs
                    ON cs.id = bs.status
                    ORDER BY bs.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                BookingStatusBasicDetailsResponse.builder()
                        .statusId(rs.getLong("status_id"))
                        .statusName(rs.getString("status_name"))
                        .description(rs.getString("description"))
                        .status(rs.getString("status"))
                        .build()
        );
    }

    @Override
    public BookingStatusBasicDetailsResponse getBookingsStatusesBasicDetailsById(CommonIdRequest request) {

        String sql = """
                    SELECT 
                        bs.id AS status_id,
                        bs.name AS status_name,
                        bs.description,
                        cs.name AS status
                    FROM booking_status bs
                    LEFT JOIN common_status cs
                    ON cs.id = bs.status
                    WHERE bs.id = ?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{request.getId()},
                (rs, rowNum) ->
                        BookingStatusBasicDetailsResponse.builder()
                                .statusId(rs.getLong("status_id"))
                                .statusName(rs.getString("status_name"))
                                .description(rs.getString("description"))
                                .status(rs.getString("status"))
                                .build()
        );
    }

    @Override
    public BookingStatusDetailsResponse getBookingsStatusesAllDetailsById(CommonIdRequest request) {

        String sql = """
                    SELECT 
                        bs.id AS status_id,
                        bs.name AS status_name,
                        bs.description,
                        cs.name AS status,
                
                        bs.created_at,
                        bs.created_by,
                        bs.updated_at,
                        bs.updated_by,
                        bs.terminated_at,
                        bs.terminated_by,
                
                        COUNT(b.booking_id) AS total_bookings,
                
                        SUM(CASE WHEN b.booking_status_id = bs.id THEN 1 ELSE 0 END) AS active_bookings_count
                
                    FROM booking_status bs
                    LEFT JOIN bookings b ON b.booking_status_id = bs.id
                    LEFT JOIN common_status cs ON cs.id = bs.status
                    WHERE bs.id = ?
                    GROUP BY bs.id
                """;

        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{request.getId()},
                (rs, rowNum) ->
                        BookingStatusDetailsResponse.builder()
                                .statusId(rs.getLong("status_id"))
                                .statusName(rs.getString("status_name"))
                                .description(rs.getString("description"))
                                .status(rs.getString("status"))

                                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                                .createdBy(rs.getLong("created_by"))

                                .updatedAt(rs.getTimestamp("updated_at") != null
                                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                                        : null)

                                .updatedBy(rs.getObject("updated_by") != null
                                        ? rs.getLong("updated_by")
                                        : null)

                                .terminatedAt(rs.getTimestamp("terminated_at") != null
                                        ? rs.getTimestamp("terminated_at").toLocalDateTime()
                                        : null)

                                .terminatedBy(rs.getObject("terminated_by") != null
                                        ? rs.getLong("terminated_by")
                                        : null)

                                .totalBookingsUsingThisStatus(rs.getInt("total_bookings"))
                                .activeBookingsCount(rs.getInt("active_bookings_count"))
                                .build()
        );
    }

    @Override
    public Long createBookingsStatuses(InsertBookingsStatusesRequest request, Long userId) {

        Long statusId = statusRepository.getStatusIdByName(request.getStatus());

        String sql = """
                INSERT INTO booking_status (
                    name,
                    description,
                    status,
                    created_at,
                    created_by,
                    updated_at,
                    updated_by
                )
                VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, request.getStatusName());
            ps.setString(2, request.getDescription());
            ps.setLong(3, statusId);
            ps.setLong(4, userId);
            ps.setLong(5, userId);

            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null
                ? keyHolder.getKey().longValue()
                : 0L;
    }

    @Override
    public void updateBookingsStatuses(UpdateBookingsStatusesRequest request, Long userId) {

        Long statusId = statusRepository.getStatusIdByName(request.getStatus());

        String sql = """
                    UPDATE booking_status
                    SET
                        name = ?,
                        description = ?,
                        status = ?,
                        updated_at = CURRENT_TIMESTAMP,
                        updated_by = ?
                    WHERE id = ?
                    AND terminated_at IS NULL
                """;


        jdbcTemplate.update(
                sql,
                request.getStatusName(),
                request.getDescription(),
                statusId,
                userId,
                request.getStatusId()
        );
    }

    @Override
    public void terminateBookingsStatuses(CommonIdRequest commonIdRequest, Long userId) {

        String sql = """
                    UPDATE booking_status
                    SET
                        status = ?,
                        terminated_at = CURRENT_TIMESTAMP,
                        terminated_by = ?
                    WHERE id = ?
                    AND terminated_at IS NULL
                """;
        Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.name());

        jdbcTemplate.update(
                sql,
                statusId,
                userId,
                commonIdRequest.getId()
        );
    }

    @Override
    public BookingBillResponse.BookingBasicInfo getBookingBasicInfoForBill(Long id) {

        String sql = """
                SELECT
                    booking_id,
                    booking_reference,
                    DATE(created_at) AS booking_date
                FROM bookings
                WHERE booking_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                rs -> rs.next()
                        ? BookingBillResponse.BookingBasicInfo.builder()
                        .bookingId(rs.getInt("booking_id"))
                        .bookingReference(rs.getString("booking_reference"))
                        .bookingDate(rs.getDate("booking_date").toLocalDate())
                        .build()
                        : null
        );
    }

    @Override
    public BookingBillResponse.Customer getCustomerForBill(Long id) {

        String sql = """
                SELECT
                    u.user_id,
                    CONCAT(u.first_name,' ',u.last_name) AS full_name,
                    u.email,
                    u.mobile_number1
                FROM bookings b
                INNER JOIN user u
                    ON b.user_id = u.user_id
                WHERE b.booking_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                rs -> rs.next()
                        ? BookingBillResponse.Customer.builder()
                        .userId(rs.getInt("user_id"))
                        .fullName(rs.getString("full_name"))
                        .email(rs.getString("email"))
                        .mobileNumber(rs.getString("mobile_number1"))
                        .build()
                        : null
        );
    }

    @Override
    public BookingBillResponse.TourDetails getTourDetailsForBill(Long id) {

        String sql = """
                SELECT
                    t.tour_id,
                    t.name AS tour_name,
                    t.duration,
                    t.start_location,
                    t.end_location,
                    ps.assume_start_date,
                    ps.assume_end_date,
                    b.total_persons
                FROM bookings b
                LEFT JOIN tour t
                    ON b.tour_id = t.tour_id
                LEFT JOIN package_schedule ps
                    ON b.package_schedule_id = ps.id
                WHERE b.booking_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                rs -> rs.next()
                        ? BookingBillResponse.TourDetails.builder()
                        .tourId(rs.getInt("tour_id"))
                        .tourName(rs.getString("tour_name"))
                        .duration(rs.getInt("duration"))
                        .startLocation(rs.getString("start_location"))
                        .endLocation(rs.getString("end_location"))
                        .travelStartDate(
                                rs.getDate("assume_start_date") != null
                                        ? rs.getDate("assume_start_date").toLocalDate()
                                        : null
                        )
                        .travelEndDate(
                                rs.getDate("assume_end_date") != null
                                        ? rs.getDate("assume_end_date").toLocalDate()
                                        : null
                        )
                        .totalPersons(rs.getInt("total_persons"))
                        .build()
                        : null
        );
    }

    @Override
    public BookingBillResponse.PackageDetails getPackageDetailsForBill(Long id) {

        String sql = """
                SELECT
                    p.package_id,
                    p.name AS package_name,
                    ps.name AS schedule_name
                FROM bookings b
                LEFT JOIN package_schedule ps
                    ON b.package_schedule_id = ps.id
                LEFT JOIN packages p
                    ON ps.package_id = p.package_id
                WHERE b.booking_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                rs -> rs.next()
                        ? BookingBillResponse.PackageDetails.builder()
                        .packageId(rs.getInt("package_id"))
                        .packageName(rs.getString("package_name"))
                        .scheduleName(rs.getString("schedule_name"))
                        .build()
                        : null
        );
    }

    @Override
    public List<BookingBillResponse.Participant> getParticipantsForBill(Long id) {

        String sql = """
                SELECT
                    first_name,
                    last_name,
                    passport_number
                FROM booking_participants
                WHERE booking_id = ?
                  AND terminated_at IS NULL
                ORDER BY id
                """;

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                (rs, rowNum) -> BookingBillResponse.Participant.builder()
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .passportNumber(rs.getString("passport_number"))
                        .build()
        );
    }

    @Override
    public List<BookingBillResponse.PriceItem> getPriceBreakdownForBill(Long id) {

        String sql = """
                SELECT
                    item_type,
                    item_name,
                    quantity,
                    unit_price,
                    total_price
                FROM booking_price_breakdown
                WHERE booking_id = ?
                ORDER BY id
                """;

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                (rs, rowNum) ->
                        BookingBillResponse.PriceItem.builder()
                                .itemType(rs.getString("item_type"))
                                .itemName(rs.getString("item_name"))
                                .quantity(rs.getInt("quantity"))
                                .unitPrice(rs.getBigDecimal("unit_price"))
                                .totalPrice(rs.getBigDecimal("total_price"))
                                .build()
        );
    }

    @Override
    public BookingBillResponse.BillingSummary getBillingSummaryForBill(Long id) {

        String sql = """
                SELECT
                    total_amount,
                    discount_amount,
                    tax_amount,
                    insurance_amount,
                    final_amount,
                    paid_amount,
                    due_amount
                FROM bookings
                WHERE booking_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                rs -> rs.next()
                        ? BookingBillResponse.BillingSummary.builder()
                        .subtotal(rs.getBigDecimal("total_amount"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .insuranceAmount(rs.getBigDecimal("insurance_amount"))
                        .finalAmount(rs.getBigDecimal("final_amount"))
                        .paidAmount(rs.getBigDecimal("paid_amount"))
                        .dueAmount(rs.getBigDecimal("due_amount"))
                        .build()
                        : null
        );
    }

    @Override
    public List<BookingIdAndReferenceResponse> getBookingIdAndReferences(String assignStatus) {

        StringBuilder sql = new StringBuilder("""
                    SELECT
                        booking_id,
                        booking_reference
                    FROM bookings
                    WHERE 1=1
                """);

        if ("ASSIGNED".equalsIgnoreCase(assignStatus)) {
            sql.append(" AND assign_to IS NOT NULL ");
        } else if ("UNASSIGNED".equalsIgnoreCase(assignStatus)) {
            sql.append(" AND assign_to IS NULL ");
        }
        // ALL -> no extra condition

        sql.append(" ORDER BY booking_id DESC ");

        return jdbcTemplate.query(
                sql.toString(),
                (rs, rowNum) -> BookingIdAndReferenceResponse.builder()
                        .bookingId(rs.getLong("booking_id"))
                        .bookingReference(rs.getString("booking_reference"))
                        .build()
        );
    }

    @Override
    public List<UnassignBookingBasicDetailsResponse> getUnassignBookingBasicDetails(
            UnassignBookingDataRequest request) {

        try {

            StringBuilder sql = new StringBuilder(
                    BookingQueries.GET_UNASSIGN_BOOKING_BASIC_DETAILS);

            List<Object> params = new ArrayList<>();

            appendFilters(sql, params, request);

            sql.append(" ORDER BY ")
                    .append(request.getSortBy() == null ? "b.booking_id" : request.getSortBy())
                    .append(" ")
                    .append(request.getSortDirection() == null ? "DESC" : request.getSortDirection());

            sql.append(" LIMIT ? OFFSET ?");

            params.add(request.getPageSize());
            params.add(request.getPageNumber() * request.getPageSize());

            return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) ->

                    UnassignBookingBasicDetailsResponse.builder()

                            .booking(UnassignBookingBasicDetailsResponse.Booking.builder()
                                    .bookingId(rs.getLong("booking_id"))
                                    .bookingReference(rs.getString("booking_reference"))
                                    .bookingDate(getLocalDate(rs, "booking_date"))
                                    .travelStartDate(getLocalDate(rs, "travel_start_date"))
                                    .travelEndDate(getLocalDate(rs, "travel_end_date"))
                                    .totalPersons(rs.getInt("total_persons"))
                                    .build())

                            .customer(UnassignBookingBasicDetailsResponse.Customer.builder()
                                    .userId(rs.getLong("user_id"))
                                    .firstName(rs.getString("first_name"))
                                    .lastName(rs.getString("last_name"))
                                    .email(rs.getString("email"))
                                    .mobileNumber(rs.getString("mobile_number1"))
                                    .nic(rs.getString("nic"))
                                    .passportNumber(rs.getString("passport_number"))
                                    .build())

                            .tour(UnassignBookingBasicDetailsResponse.Tour.builder()
                                    .tourId(rs.getLong("tour_id"))
                                    .tourName(rs.getString("tour_name"))
                                    .description(rs.getString("description"))
                                    .duration(rs.getInt("duration"))
                                    .startLocation(rs.getString("start_location"))
                                    .endLocation(rs.getString("end_location"))
                                    .build())

                            .packageDetails(UnassignBookingBasicDetailsResponse.PackageDetails.builder()
                                    .packageId(rs.getLong("package_id"))
                                    .packageName(rs.getString("package_name"))
                                    .totalPrice(rs.getBigDecimal("total_price"))
                                    .pricePerPerson(rs.getBigDecimal("price_per_person"))
                                    .minPersonCount(rs.getInt("min_person_count"))
                                    .maxPersonCount(rs.getInt("max_person_count"))
                                    .build())

                            .schedule(UnassignBookingBasicDetailsResponse.Schedule.builder()
                                    .packageScheduleId(rs.getLong("package_schedule_id"))
                                    .scheduleName(rs.getString("schedule_name"))
                                    .assumeStartDate(getLocalDate(rs, "assume_start_date"))
                                    .assumeEndDate(getLocalDate(rs, "assume_end_date"))
                                    .build())

                            .financial(UnassignBookingBasicDetailsResponse.Financial.builder()
                                    .totalAmount(rs.getBigDecimal("total_amount"))
                                    .discountAmount(rs.getBigDecimal("discount_amount"))
                                    .taxAmount(rs.getBigDecimal("tax_amount"))
                                    .insuranceAmount(rs.getBigDecimal("insurance_amount"))
                                    .finalAmount(rs.getBigDecimal("final_amount"))
                                    .paidAmount(rs.getBigDecimal("paid_amount"))
                                    .dueAmount(rs.getBigDecimal("due_amount"))
                                    .build())

                            .assignment(UnassignBookingBasicDetailsResponse.Assignment.builder()
                                    .assignedTo(rs.getLong("assign_to"))
                                    .assignedUser(rs.getString("assigned_user"))
                                    .assignMessage(rs.getString("assign_message"))
                                    .build())

                            .status(UnassignBookingBasicDetailsResponse.Status.builder()
                                    .bookingStatusId(rs.getInt("booking_status_id"))
                                    .bookingStatus(rs.getString("booking_status"))
                                    .build())

                            .build());
        } catch (Exception ex) {

            LOGGER.error("Error getting unassign bookings.", ex);

            throw new DataNotFoundErrorExceptionHandler("Failed to retrieve unassign bookings.");
        }
    }

    @Override
    public Integer getUnassignBookingBasicDetailsCount(
            UnassignBookingDataRequest request) {

        try {

            StringBuilder sql = new StringBuilder(
                    BookingQueries.COUNT_UNASSIGN_BOOKING_BASIC_DETAILS);

            List<Object> params = new ArrayList<>();

            appendFilters(sql, params, request);

            return jdbcTemplate.queryForObject(
                    sql.toString(),
                    params.toArray(),
                    Integer.class);

        } catch (Exception ex) {

            LOGGER.error("Error counting unassign bookings.", ex);

            throw new DataNotFoundErrorExceptionHandler(
                    "Failed to retrieve booking count.");
        }
    }

    @Override
    public List<String> getUnassignBookingReferences() {

        try {

            return jdbcTemplate.query(
                    BookingQueries.GET_UNASSIGN_BOOKING_REFERENCES,
                    (rs, rowNum) -> rs.getString("booking_reference")
            );

        } catch (Exception ex) {

            LOGGER.error("Error fetching booking references.", ex);

            throw new InternalServerErrorExceptionHandler(
                    "Failed to retrieve booking references.");
        }
    }

    @Override
    public void updateUnassignBookingToAssign(AssignBookingRequest assignBookingRequest, Long userId) {

        String sql = """
                    UPDATE bookings
                    SET
                        assign_to = ?,
                        assign_message = ?,
                        updated_by = ?,
                        updated_at = CURRENT_TIMESTAMP
                    WHERE booking_id = ?
                      AND assign_to IS NULL
                """;

        int updatedRows = jdbcTemplate.update(
                sql,
                assignBookingRequest.getAssignTo(),
                assignBookingRequest.getAssignMessage(),
                userId,
                assignBookingRequest.getBookingId()
        );

        if (updatedRows == 0) {
            throw new RuntimeException("Booking is already assigned or does not exist");
        }
    }

    @Override
    public void updateUnassignBooking(UnassignBookingRequest unassignBookingRequest, Long userId) {

        String sql = """
                    UPDATE bookings
                    SET
                        assign_to = ?,
                        assign_message = ?,
                        updated_by = ?,
                        updated_at = CURRENT_TIMESTAMP
                    WHERE booking_id = ?
                """;

        int updatedRows = jdbcTemplate.update(
                sql,
                unassignBookingRequest.getAssignTo(),
                unassignBookingRequest.getAssignMessage(),
                userId,
                unassignBookingRequest.getBookingId()
        );

        if (updatedRows == 0) {
            throw new RuntimeException("Booking not found");
        }
    }

    @Override
    public List<BookingHistoryBasicDetailsResponse> getBookingHistoryBasicDetails(
            BookingHistoryDataRequest req) {

        StringBuilder sql = new StringBuilder("""
                    SELECT
                        b.booking_id,
                        b.booking_reference,
                        b.total_persons,
                        b.booking_date,
                        b.travel_start_date,
                        b.travel_end_date,
                        b.final_amount,
                        b.paid_amount,
                        b.due_amount,
                        b.refund_amount,
                
                        bs.id AS status_id,
                        bs.name AS status_name,
                
                        t.tour_id,
                        t.name AS tour_name,
                
                        p.package_id,
                        p.name AS package_name,
                
                        u.user_id,
                        CONCAT(u.first_name, ' ', u.last_name) AS customer_name,
                
                        eu.id AS employee_id,
                        e_user.first_name AS employee_first_name,
                        e_user.last_name AS employee_last_name
                
                    FROM bookings b
                    LEFT JOIN booking_status bs ON b.booking_status_id = bs.id
                    LEFT JOIN tour t ON b.tour_id = t.tour_id
                    LEFT JOIN packages p ON b.package_id = p.package_id
                    LEFT JOIN user u ON b.user_id = u.user_id
                
                    LEFT JOIN employees eu ON b.assign_to = eu.id
                    LEFT JOIN user e_user ON eu.user_id = e_user.user_id
                
                    WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        // filters
        if (req.getBookingReference() != null) {
            sql.append(" AND b.booking_reference LIKE ? ");
            params.add("%" + req.getBookingReference() + "%");
        }

        if (req.getTourId() != null) {
            sql.append(" AND b.tour_id = ? ");
            params.add(req.getTourId());
        }

        if (req.getPackageId() != null) {
            sql.append(" AND b.package_id = ? ");
            params.add(req.getPackageId());
        }

        if (req.getBookingStatusId() != null) {
            sql.append(" AND b.booking_status_id = ? ");
            params.add(req.getBookingStatusId());
        }

        if (req.getBookingFrom() != null && req.getBookingTo() != null) {
            sql.append(" AND b.booking_date BETWEEN ? AND ? ");
            params.add(req.getBookingFrom());
            params.add(req.getBookingTo());
        }

        if (req.getTravelStartDate() != null && req.getTravelEndDate() != null) {
            sql.append(" AND b.travel_start_date BETWEEN ? AND ? ");
            params.add(req.getTravelStartDate());
            params.add(req.getTravelEndDate());
        }

        if (req.getMinPrice() != null) {
            sql.append(" AND b.final_amount >= ? ");
            params.add(req.getMinPrice());
        }

        if (req.getMaxPrice() != null) {
            sql.append(" AND b.final_amount <= ? ");
            params.add(req.getMaxPrice());
        }

        // sorting
        String sortBy = (req.getSortBy() != null) ? req.getSortBy() : "b.booking_id";
        String sortDir = (req.getSortDirection() != null) ? req.getSortDirection() : "DESC";

        sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortDir);

        // pagination
        int pageSize = req.getPageSize() != null ? req.getPageSize() : 10;
        int pageNumber = req.getPageNumber() != null ? req.getPageNumber() : 0;

        int offset = pageNumber * pageSize;

        sql.append(" LIMIT ? OFFSET ? ");
        params.add(pageSize);
        params.add(offset);

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> BookingHistoryBasicDetailsResponse.builder()
                        .bookingId(rs.getLong("booking_id"))
                        .bookingReference(rs.getString("booking_reference"))
                        .customerName(rs.getString("customer_name"))
                        .tourName(rs.getString("tour_name"))
                        .packageName(rs.getString("package_name"))
                        .totalPersons(rs.getInt("total_persons"))
                        .bookingDate(rs.getDate("booking_date").toLocalDate())
                        .travelStartDate(rs.getDate("travel_start_date").toLocalDate())
                        .travelEndDate(rs.getDate("travel_end_date").toLocalDate())
                        .finalAmount(rs.getBigDecimal("final_amount"))
                        .paidAmount(rs.getBigDecimal("paid_amount"))
                        .dueAmount(rs.getBigDecimal("due_amount"))
                        .refundAmount(rs.getBigDecimal("refund_amount"))
                        .bookingStatus(rs.getString("status_name"))
                        .assignedEmployee(
                                rs.getString("employee_first_name") + " " + rs.getString("employee_last_name")
                        )
                        .build()
        );
    }

    @Override
    public Integer getBookingHistoryBasicDetailsCount(
            BookingHistoryDataRequest req) {

        StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(*)
                    FROM bookings b
                    WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        if (req.getBookingReference() != null) {
            sql.append(" AND b.booking_reference LIKE ? ");
            params.add("%" + req.getBookingReference() + "%");
        }

        if (req.getTourId() != null) {
            sql.append(" AND b.tour_id = ? ");
            params.add(req.getTourId());
        }

        if (req.getPackageId() != null) {
            sql.append(" AND b.package_id = ? ");
            params.add(req.getPackageId());
        }

        if (req.getBookingStatusId() != null) {
            sql.append(" AND b.booking_status_id = ? ");
            params.add(req.getBookingStatusId());
        }

        if (req.getBookingFrom() != null && req.getBookingTo() != null) {
            sql.append(" AND b.booking_date BETWEEN ? AND ? ");
            params.add(req.getBookingFrom());
            params.add(req.getBookingTo());
        }

        if (req.getTravelStartDate() != null && req.getTravelEndDate() != null) {
            sql.append(" AND b.travel_start_date BETWEEN ? AND ? ");
            params.add(req.getTravelStartDate());
            params.add(req.getTravelEndDate());
        }

        if (req.getMinPrice() != null) {
            sql.append(" AND b.final_amount >= ? ");
            params.add(req.getMinPrice());
        }

        if (req.getMaxPrice() != null) {
            sql.append(" AND b.final_amount <= ? ");
            params.add(req.getMaxPrice());
        }

        return jdbcTemplate.queryForObject(
                sql.toString(),
                params.toArray(),
                Integer.class
        );
    }

    @Override
    public List<BookingHistoryDetailsResponse.BookingActivityHistory> getBookingActivityHistory(Long id) {

        String sql = """
                    SELECT
                        bah.activity_type,
                        bah.description,
                        bah.updated_at,
                        u.username AS updated_by
                    FROM booking_activity_history bah
                    LEFT JOIN user u ON bah.updated_by = u.user_id
                    WHERE bah.booking_id = ?
                    ORDER BY bah.updated_at DESC
                """;

        return jdbcTemplate.query(sql, new Object[]{id},
                (rs, rowNum) -> BookingHistoryDetailsResponse.BookingActivityHistory.builder()
                        .activityType(rs.getString("activity_type"))
                        .description(rs.getString("description"))
                        .updatedBy(rs.getString("updated_by"))
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .build()
        );
    }

    @Override
    public List<BookingHistoryDetailsResponse.BookingStatusHistory> getBookingStatusHistory(Long id) {

        String sql = """
                    SELECT
                        bsh.previous_status_id,
                        ps.name AS previous_status,
                        bsh.new_status_id,
                        ns.name AS new_status,
                        bsh.updated_at,
                        u.username AS updated_by
                    FROM booking_status_history bsh
                    LEFT JOIN booking_status ps ON bsh.previous_status_id = ps.id
                    LEFT JOIN booking_status ns ON bsh.new_status_id = ns.id
                    LEFT JOIN user u ON bsh.updated_by = u.user_id
                    WHERE bsh.booking_id = ?
                    ORDER BY bsh.updated_at DESC
                """;

        return jdbcTemplate.query(sql, new Object[]{id},
                (rs, rowNum) -> BookingHistoryDetailsResponse.BookingStatusHistory.builder()
                        .previousStatus(rs.getString("previous_status"))
                        .newStatus(rs.getString("new_status"))
                        .updatedBy(rs.getString("updated_by"))
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .build()
        );
    }

    @Override
    public List<BookingHistoryDetailsResponse.BookingAssignmentHistory> getBookingAssignmentHistory(Long id) {

        String sql = """
                    SELECT
                        bah.previous_assign_employee,
                        pu.username AS previous_employee,
                        bah.new_assign_employee,
                        nu.username AS new_employee,
                        bah.updated_at,
                        u.username AS updated_by
                    FROM booking_assign_history bah
                    LEFT JOIN user pu ON bah.previous_assign_employee = pu.user_id
                    LEFT JOIN user nu ON bah.new_assign_employee = nu.user_id
                    LEFT JOIN user u ON bah.updated_by = u.user_id
                    WHERE bah.booking_id = ?
                    ORDER BY bah.updated_at DESC
                """;

        return jdbcTemplate.query(sql, new Object[]{id},
                (rs, rowNum) -> BookingHistoryDetailsResponse.BookingAssignmentHistory.builder()
                        .previousEmployee(rs.getString("previous_employee"))
                        .newEmployee(rs.getString("new_employee"))
                        .updatedBy(rs.getString("updated_by"))
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .build()
        );
    }

    @Override
    public List<BookingHistoryDetailsResponse.BookingPaymentHistory> getBookingPaymentHistory(Long id) {

        String sql = """
                    SELECT
                        bph.previous_paid_amount,
                        bph.new_paid_amount,
                        bph.previous_due_amount,
                        bph.new_due_amount,
                        bph.previous_refund_amount,
                        bph.new_refund_amount,
                        bph.payment_reference,
                        bph.remarks,
                        bph.updated_at,
                        u.username AS updated_by
                    FROM booking_payment_history bph
                    LEFT JOIN user u ON bph.updated_by = u.user_id
                    WHERE bph.booking_id = ?
                    ORDER BY bph.updated_at DESC
                """;

        return jdbcTemplate.query(sql, new Object[]{id},
                (rs, rowNum) -> BookingHistoryDetailsResponse.BookingPaymentHistory.builder()
                        .previousPaidAmount(rs.getBigDecimal("previous_paid_amount"))
                        .newPaidAmount(rs.getBigDecimal("new_paid_amount"))
                        .previousDueAmount(rs.getBigDecimal("previous_due_amount"))
                        .newDueAmount(rs.getBigDecimal("new_due_amount"))
                        .previousRefundAmount(rs.getBigDecimal("previous_refund_amount"))
                        .newRefundAmount(rs.getBigDecimal("new_refund_amount"))
                        .paymentReference(rs.getString("payment_reference"))
                        .remarks(rs.getString("remarks"))
                        .updatedBy(rs.getString("updated_by"))
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .build()
        );
    }

    @Override
    public void addRecordForBookingActivityHistory(
            BookingActivityHistoryInsertRequest req,
            Long userId) {

        String sql = """
        INSERT INTO booking_activity_history
        (booking_id, activity_type, description, updated_at, updated_by)
        VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)
    """;

        jdbcTemplate.update(
                sql,
                req.getBookingId(),
                req.getActivityType(),
                req.getDescription(),
                userId
        );
    }

    @Override
    public void addRecordForBookingAssignHistory(
            BookingAssignHistoryInsertRequest req,
            Long userId) {

        String sql = """
        INSERT INTO booking_assign_history
        (booking_id, previous_assign_employee, new_assign_employee, updated_at, updated_by)
        VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)
    """;

        jdbcTemplate.update(
                sql,
                req.getBookingId(),
                req.getPreviousAssignEmployee(),
                req.getNewAssignEmployee(),
                userId
        );
    }

    @Override
    public void addRecordForBookingPaymentHistory(
            BookingPaymentHistoryInsertRequest req,
            Long userId) {

        String sql = """
        INSERT INTO booking_payment_history
        (booking_id,
         previous_paid_amount,
         new_paid_amount,
         previous_due_amount,
         new_due_amount,
         previous_refund_amount,
         new_refund_amount,
         payment_reference,
         remarks,
         updated_at,
         updated_by)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)
    """;

        jdbcTemplate.update(
                sql,
                req.getBookingId(),
                req.getPreviousPaidAmount(),
                req.getNewPaidAmount(),
                req.getPreviousDueAmount(),
                req.getNewDueAmount(),
                req.getPreviousRefundAmount(),
                req.getNewRefundAmount(),
                req.getPaymentReference(),
                req.getRemarks(),
                userId
        );
    }

    @Override
    public void addRecordForBookingStatusHistory(
            BookingStatusHistoryInsertRequest req,
            Long userId) {

        String sql = """
        INSERT INTO booking_status_history
        (booking_id, previous_status_id, new_status_id, updated_at, updated_by)
        VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)
    """;

        jdbcTemplate.update(
                sql,
                req.getBookingId(),
                req.getPreviousStatus(),
                req.getNewStatus(),
                userId
        );
    }

    private LocalDate getLocalDate(ResultSet rs, String column) throws SQLException {
        Date date = rs.getDate(column);
        return date != null ? date.toLocalDate() : null;
    }

    private void appendFilters(StringBuilder sql,
                               List<Object> params,
                               UnassignBookingDataRequest request) {

        if (StringUtils.hasText(request.getBookingReference())) {
            sql.append(" AND b.booking_reference LIKE ?");
            params.add("%" + request.getBookingReference() + "%");
        }

        if (StringUtils.hasText(request.getCustomerName())) {
            sql.append("""
                        AND CONCAT(
                            COALESCE(u.first_name,''),' ',
                            COALESCE(u.last_name,'')
                        ) LIKE ?
                    """);
            params.add("%" + request.getCustomerName() + "%");
        }

        if (request.getBookingStatusId() != null) {
            sql.append(" AND b.booking_status_id = ?");
            params.add(request.getBookingStatusId());
        }

        if (request.getTourId() != null) {
            sql.append(" AND b.tour_id = ?");
            params.add(request.getTourId());
        }

        if (request.getPackageId() != null) {
            sql.append(" AND b.package_id = ?");
            params.add(request.getPackageId());
        }

        if (request.getPackageScheduleId() != null) {
            sql.append(" AND b.package_schedule_id = ?");
            params.add(request.getPackageScheduleId());
        }

        if (request.getAssignTo() != null) {
            sql.append(" AND b.assign_to = ?");
            params.add(request.getAssignTo());
        }

        if (StringUtils.hasText(request.getEmail())) {
            sql.append(" AND u.email LIKE ?");
            params.add("%" + request.getEmail() + "%");
        }

        if (StringUtils.hasText(request.getMobileNumber())) {
            sql.append(" AND u.mobile_number1 LIKE ?");
            params.add("%" + request.getMobileNumber() + "%");
        }

        if (request.getBookingDateFrom() != null) {
            sql.append(" AND b.booking_date >= ?");
            params.add(request.getBookingDateFrom());
        }

        if (request.getBookingDateTo() != null) {
            sql.append(" AND b.booking_date <= ?");
            params.add(request.getBookingDateTo());
        }

        if (request.getTravelStartDateFrom() != null) {
            sql.append(" AND b.travel_start_date >= ?");
            params.add(request.getTravelStartDateFrom());
        }

        if (request.getTravelStartDateTo() != null) {
            sql.append(" AND b.travel_start_date <= ?");
            params.add(request.getTravelStartDateTo());
        }
    }

    private List<BookingsRequestParamsResponse.IdAndName>
    getBookingParamStatuses() {

        return jdbcTemplate.query(
                BookingQueries.GET_BOOKING_PARAM_STATUSES,
                (rs, rowNum) ->
                        BookingsRequestParamsResponse.IdAndName
                                .builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .build());
    }

    private List<BookingsRequestParamsResponse.IdAndName>
    getBookingParamTours() {

        return jdbcTemplate.query(
                BookingQueries.GET_BOOKING_PARAM_TOURS,
                (rs, rowNum) ->
                        BookingsRequestParamsResponse.IdAndName
                                .builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .build());
    }

    private List<BookingsRequestParamsResponse.IdAndName>
    getBookingParamPackages() {

        return jdbcTemplate.query(
                BookingQueries.GET_BOOKING_PARAM_PACKAGES,
                (rs, rowNum) ->
                        BookingsRequestParamsResponse.IdAndName
                                .builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .build());
    }

    private List<BookingsRequestParamsResponse.IdAndName>
    getBookingParamAssignEmployees() {

        return jdbcTemplate.query(
                BookingQueries.GET_BOOKING_PARAM_ASSIGN_EMPLOYEES,
                (rs, rowNum) ->
                        BookingsRequestParamsResponse.IdAndName
                                .builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .build());
    }


}
