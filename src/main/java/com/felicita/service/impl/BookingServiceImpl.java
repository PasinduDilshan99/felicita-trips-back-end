package com.felicita.service.impl;

import com.felicita.email.BookingEmailHelperService;
import com.felicita.exception.*;
import com.felicita.filter.helper.BookingHelperService;
import com.felicita.model.dto.*;
import com.felicita.model.enums.*;
import com.felicita.model.other.*;
import com.felicita.model.request.BookingCancelledRequest;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.model.request.bookings.BookingDataRequest;
import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.model.request.bookings.UpdateBookingRequest;
import com.felicita.model.request.bookings.UpdateBookingStatusRequest;
import com.felicita.model.request.bookings.history.BookingHistoryDataRequest;
import com.felicita.model.request.bookings.status.InsertBookingsStatusesRequest;
import com.felicita.model.request.bookings.status.UpdateBookingsStatusesRequest;
import com.felicita.model.request.bookings.unassign.AssignBookingRequest;
import com.felicita.model.request.bookings.unassign.UnassignBookingDataRequest;
import com.felicita.model.request.bookings.unassign.UnassignBookingRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.bookings.*;
import com.felicita.model.response.bookings.history.BookingHisotryWithParamsResponse;
import com.felicita.model.response.bookings.history.BookingHistoryBasicDetailsResponse;
import com.felicita.model.response.bookings.history.BookingHistoryDetailsResponse;
import com.felicita.model.response.bookings.history.BookingsHistoryRequestParamsResponse;
import com.felicita.model.response.bookings.status.BookingStatusBasicDetailsResponse;
import com.felicita.model.response.bookings.status.BookingStatusDetailsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingBasicDetailsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingWithParamsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingsRequestParamsResponse;
import com.felicita.model.response.common.BookingIdAndReferenceResponse;
import com.felicita.model.response.statistics.BookingAssignStatisticsResponse;
import com.felicita.model.response.statistics.BookingHistoryStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatusStatisticsResponse;
import com.felicita.repository.BookingRepository;
import com.felicita.security.model.User;
import com.felicita.service.*;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.BookingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_BOOKING_DETAILS;
import static com.felicita.util.FrontEndUrls.VIEW_BOOKING_STATUS_DETAILS;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final CommonService commonService;
    private final BookingValidationService bookingValidationService;
    private final BookingHelperService bookingHelperService;
    private final PackageService packageService;
    private final VehicleService vehicleService;
    private final EmailService emailService;
    private final EmailHelperService emailHelperService;
    private final BookingEmailHelperService bookingEmailHelperService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, CommonService commonService, BookingValidationService bookingValidationService, BookingHelperService bookingHelperService, PackageService packageService, VehicleService vehicleService, EmailService emailService, EmailHelperService emailHelperService, BookingEmailHelperService bookingEmailHelperService) {
        this.bookingRepository = bookingRepository;
        this.commonService = commonService;
        this.bookingValidationService = bookingValidationService;
        this.bookingHelperService = bookingHelperService;
        this.packageService = packageService;
        this.vehicleService = vehicleService;
        this.emailService = emailService;
        this.emailHelperService = emailHelperService;
        this.bookingEmailHelperService = bookingEmailHelperService;
    }

    @Override
    public CommonResponse<List<CompleteToursResponse>> getCompletedBookingToursDetailsById() {
        LOGGER.info("Start fetching all completed booking tours details from repository");
        try {
            Long userId = commonService.getUserIdBySecurityContext();
            List<CompleteToursResponse> completeToursResponses = bookingRepository.getCompletedBookingToursDetailsById(userId);

            if (completeToursResponses.isEmpty()) {
                LOGGER.warn("No completed booking tours details found in database");
                throw new DataNotFoundErrorExceptionHandler("No completed booking tours details found");
            }

            LOGGER.info("Fetched {} completed booking tours details successfully", completeToursResponses.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    completeToursResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    new ArrayList<>(),
                    Instant.now());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching completed booking tours details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch completed booking tours details from database");
        } finally {
            LOGGER.info("End fetching all completed booking tours details from repository");
        }
    }

    @Override
    public CommonResponse<List<UpcomingToursResponse>> getUpcomingBookingToursDetailsById() {
        LOGGER.info("Start fetching all upcoming booking tours details from repository");
        try {
            Long userId = commonService.getUserIdBySecurityContext();
            List<UpcomingToursResponse> upcomingToursResponses = bookingRepository.getUpcomingBookingToursDetailsById(userId);

            if (upcomingToursResponses.isEmpty()) {
                LOGGER.warn("No upcoming booking tours details found in database");
                throw new DataNotFoundErrorExceptionHandler("No upcoming booking tours details found");
            }

            LOGGER.info("Fetched {} upcoming booking tours details successfully", upcomingToursResponses.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    upcomingToursResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    new ArrayList<>(),
                    Instant.now());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching upcoming booking tours details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch upcoming booking tours details from database");
        } finally {
            LOGGER.info("End fetching all upcoming booking tours details from repository");
        }
    }

    @Override
    public CommonResponse<List<RequestedToursResponse>> getRequstedToursDetailsById() {
        LOGGER.info("Start fetching all requested  booking tours details from repository");
        try {
            Long userId = commonService.getUserIdBySecurityContext();
            List<RequestedToursResponse> requestedToursResponses = bookingRepository.getRequstedToursDetailsById(userId);

            if (requestedToursResponses.isEmpty()) {
                LOGGER.warn("No requested booking tours details found in database");
                throw new DataNotFoundErrorExceptionHandler("No requested booking tours details found");
            }

            LOGGER.info("Fetched {} requested booking tours details successfully", requestedToursResponses.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    requestedToursResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    new ArrayList<>(),
                    Instant.now());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching requested booking tours details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch requested booking tours details from database");
        } finally {
            LOGGER.info("End fetching all requested booking tours details from repository");
        }
    }

    @Override
    public CommonResponse<List<CancelledToursResponse>> getCancelledToursDetailsById() {
        LOGGER.info("Start fetching all cancelled booking tours details from repository");
        try {
            Long userId = commonService.getUserIdBySecurityContext();
            List<CancelledToursResponse> cancelledToursResponses = bookingRepository.getCancelledToursDetailsById(userId);

            if (cancelledToursResponses.isEmpty()) {
                LOGGER.warn("No cancelled booking tours details found in database");
                throw new DataNotFoundErrorExceptionHandler("No cancelled booking tours details found");
            }

            LOGGER.info("Fetched {} cancelled booking tours details successfully", cancelledToursResponses.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    cancelledToursResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    new ArrayList<>(),
                    Instant.now());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching cancelled booking tours details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch cancelled booking tours details from database");
        } finally {
            LOGGER.info("End fetching all cancelled booking tours details from repository");
        }
    }

    @Override
    public CommonResponse<BookInsertResponse> bookingTour(BookingRequest bookingRequest) {
        try {
            PackageBasicDetailsDto packageBasicDetailsDto = packageService.getPackageBasicDetailsByScheduleId(bookingRequest.getPackageScheduleId());
            LocalDate tourStartDate = packageBasicDetailsDto.getAssumeStartDate().toLocalDate();
            List<PackageActivityPriceDto> packageActivityPriceDto = packageService.getPackageActivityPriceByScheduleId(bookingRequest.getPackageScheduleId());
            List<PackageDestinationExtraPriceDto> packageDestinationExtraPriceDto = packageService.getPackageDestinationExtraPriceByScheduleId(bookingRequest.getPackageScheduleId());
            List<PackageDayAccommodationPriceDto> packageDayAccommodationPriceDto = packageService.getPackageDayAccommodationPriceByScheduleId(bookingRequest.getPackageScheduleId());
            BookInsertResponse bookInsertResponse = new BookInsertResponse();

            Double tourTotalAccommodtionAmountPerPerson = bookingHelperService.calculateTotalAccomodationAmountPerPerson(packageDayAccommodationPriceDto);
            Double tourActivitiesAmountPerPerson = bookingHelperService.calculateTotalActivityAmountPerPerson(packageActivityPriceDto);
            Double tourDestinationExtraAmountPerPerson = bookingHelperService.calculateTotalDestinationExtraAmountPerPerson(packageDestinationExtraPriceDto);

            bookingValidationService.validateBookingRequest(bookingRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            //
            InsertBookingRequestDto insertBookingRequestDto = new InsertBookingRequestDto();
            insertBookingRequestDto.setBookingReference(bookingHelperService.generateUniqueBookingReferance());
            insertBookingRequestDto.setUserId(userId);
            insertBookingRequestDto.setPackageScheduleId(bookingRequest.getPackageScheduleId());
            insertBookingRequestDto.setTotalPersons(bookingRequest.getParticipants().size());

//            Double totalAmount = bookingHelperService.calculateTotalAmount(bookingRequest, packageBasicDetailsDto);
            Double totalAmount = (tourTotalAccommodtionAmountPerPerson + tourActivitiesAmountPerPerson + tourDestinationExtraAmountPerPerson) * bookingRequest.getParticipants().size();
//            Double discountAmount = bookingHelperService.calculateDiscountAmount(bookingRequest, packageBasicDetailsDto);
            Double discountAmount = (totalAmount * packageBasicDetailsDto.getDiscountPercentage()) / 100;
//            Double taxAmount = bookingHelperService.calculateTaxAmount(bookingRequest, packageBasicDetailsDto);
            Double taxAmount = (totalAmount * 18) / 100;
            Double insuranceAmount = 0.0;

            if (bookingRequest.getInsuranceRequired() == true) {
                insuranceAmount = bookingHelperService.calculateInsuranceAmount(bookingRequest, packageBasicDetailsDto);
            }
            insertBookingRequestDto.setTotalAmount(totalAmount);
            insertBookingRequestDto.setDiscountAmount(discountAmount);
            insertBookingRequestDto.setTaxAmount(taxAmount);
            insertBookingRequestDto.setInsuranceAmount(insuranceAmount);

            Double finalAmount = totalAmount - discountAmount + taxAmount + insuranceAmount;

            insertBookingRequestDto.setFinalAmount(finalAmount);

            insertBookingRequestDto.setBookingDate(java.time.LocalDate.now());
            insertBookingRequestDto.setTravelStartDate(packageBasicDetailsDto.getAssumeStartDate());
            insertBookingRequestDto.setTravelEndDate(packageBasicDetailsDto.getAssumeEndDate());
            insertBookingRequestDto.setBookingStatus(BookingStatus.PENDING.name());
            insertBookingRequestDto.setSpecialRequirements(bookingRequest.getSpecialRequirements());
            insertBookingRequestDto.setDietaryRestrictions(bookingRequest.getDietaryRestrictions());
            insertBookingRequestDto.setInsuranceRequired(bookingRequest.getInsuranceRequired());

            //
            Long bookingId = bookingRepository.bookingTourBasicDetails(insertBookingRequestDto);
            bookInsertResponse.setBookingId(bookingId);

            // insert transport - airport
            bookingRepository.bookingAirportTransportation(bookingId, bookingRequest.getTransport(), userId);

            // insert transport vehicel
            for (PackageDayAccommodationPriceDto p : packageDayAccommodationPriceDto) {
                VehicleBasicDetailsDto vehicleBasicDetailsDto = vehicleService.getVehicleBasicDetailsById(p.getVehicleId());
                LocalDate date = tourStartDate.plusDays(p.getDayNumber() - 1);
                bookingRepository.bookingTransportation(bookingId, vehicleBasicDetailsDto, date, userId);

                // insert booking itinerary
                bookingRepository.insertBookingItinerary(bookingId, p, date, userId);
            }

            // insert activities
            for (PackageActivityPriceDto a : packageActivityPriceDto) {
                int totalParticipants = bookingRequest.getParticipants().size();
                bookingRepository.insertBookingPriceBreakdown(bookingId, a, totalParticipants, userId);

                // insert booking activities
                bookingRepository.insertBookingActivities(bookingId, a, totalParticipants, userId);
            }

            // insert participants
            for (BookingRequest.Participant participant : bookingRequest.getParticipants()) {
                bookingRepository.insertBookingParticipant(bookingId, participant, userId);
            }

            // insert booking notes
            for (BookingRequest.BookingNote note : bookingRequest.getBookingNotes()) {
                bookingRepository.insertBookingNote(bookingId, note, userId);
            }

            // booking accommodation not done

            // booking invoice
            LocalDate invoiceDate = java.time.LocalDate.now();
            LocalDate invoiceDueDate = invoiceDate.plusDays(7);
            String invoiceNumber = bookingHelperService.generateUniqueBookingInvoiceReference(bookingId);
            bookingRepository.insertBookingInvoice(bookingId, invoiceNumber, invoiceDate, invoiceDueDate, totalAmount, taxAmount, discountAmount, finalAmount, bookingRequest.getInvoices(), userId);


            return
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                            CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                            bookInsertResponse,
                            Instant.now()
                    );
        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert booking request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());

        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<PrintReceiptForBookingResponse> createReceiptForBooking(Long bookingId) {
        try {
            BookingBasicDetailsDto bookingBasicDetailsDto = bookingRepository.getBookingBasicDetailsByBookingId(bookingId);
            List<BookingActivityDto> bookingActivityDtos = bookingRepository.getBookingActivityByBookingId(bookingId);
            List<BookingParticipantDto> bookingParticipantDtos = bookingRepository.getBookingParticipantByBookingId(bookingId);
            List<PackageDestinationExtraPriceDto> packageDestinationExtraPriceDto = packageService.getPackageDestinationExtraPriceByScheduleId(bookingBasicDetailsDto.getPackageScheduleId());
            List<PackageDayAccommodationPriceDto> packageDayAccommodationPriceDto = packageService.getPackageDayAccommodationPriceByScheduleId(bookingBasicDetailsDto.getPackageScheduleId());
            PrintReceiptForBookingResponse response = bookingHelperService.createReceiptForBooking(
                    bookingBasicDetailsDto,
                    bookingActivityDtos,
                    bookingParticipantDtos,
                    packageDestinationExtraPriceDto,
                    packageDayAccommodationPriceDto);
            LOGGER.info("bookingBasicDetailsDto {}", bookingBasicDetailsDto);
            LOGGER.info("bookingActivityDtos {}", bookingActivityDtos);
            LOGGER.info("bookingParticipantDtos {}", bookingParticipantDtos);

            return (
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            response,
                            Instant.now()
                    )
            );

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching cancelled booking tours details: {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching cancelled booking tours details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch cancelled booking tours details from database");
        } finally {
            LOGGER.info("End fetching all cancelled booking tours details from repository");
        }
    }

    @Override
    public CommonResponse<List<BookingFilterResponse>> getBookingFilter() {
        LOGGER.info("Start fetching booking filters from repository");
        try {
            List<BookingFilterResponse> bookingFilterResponses = bookingRepository.getBookingFilter();

            if (bookingFilterResponses.isEmpty()) {
                LOGGER.warn("No requested booking filters found in database");
                throw new DataNotFoundErrorExceptionHandler("No requested booking tours details found");
            }

            LOGGER.info("Fetched {} requested booking filters details successfully", bookingFilterResponses.size());
            return (
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            bookingFilterResponses,
                            Instant.now()
                    )
            );

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching requested booking filters details: {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching requested booking filters details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch requested booking filetrs details from database");
        } finally {
            LOGGER.info("End fetching all requested booking filters details from repository");
        }
    }

    @Override
    public CommonResponse<List<UserBookingSummaryResponse>> getBookedTours() {
        LOGGER.info("Start fetching booked tours from repository");
        try {
            Long userId = commonService.getUserIdBySecurityContext();
            List<UserBookingSummaryResponse> userBookingSummaryResponses = bookingRepository.getBookedTours(userId);

            if (userBookingSummaryResponses.isEmpty()) {
                LOGGER.warn("No requested booked tours found in database");
                throw new DataNotFoundErrorExceptionHandler("No requested booked tours details found");
            }

            LOGGER.info("Fetched {} booked tours details successfully", userBookingSummaryResponses.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    userBookingSummaryResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching booked tours filters details: {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booked tours details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booked tours from database");
        } finally {
            LOGGER.info("End fetching booked tours from repository");
        }
    }

    @Override
    public CommonResponse<List<PendingToursResponse>> getPendingBookingToursDetailsById() {
        LOGGER.info("Start fetching all pending booking tours details from repository");
        try {
            Long userId = commonService.getUserIdBySecurityContext();
            List<PendingToursResponse> pendingToursResponses = bookingRepository.getPendingBookingToursDetailsById(userId);

            if (pendingToursResponses.isEmpty()) {
                LOGGER.warn("No pending booking tours details found in database");
                throw new DataNotFoundErrorExceptionHandler("No pending booking tours details found");
            }

            LOGGER.info("Fetched {} pending booking tours details successfully", pendingToursResponses.size());
            return (
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            pendingToursResponses,
                            Instant.now()
                    )
            );

        } catch (DataNotFoundErrorExceptionHandler e) {
            return (
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            new ArrayList<>(),
                            Instant.now()
                    )
            );
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching pending booking tours details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch pending booking tours details from database");
        } finally {
            LOGGER.info("End fetching all pending booking tours details from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> tourBookingInquiry(TourBookingInquiryRequest tourBookingInquiryRequest) {
        LOGGER.info("Start insert tour booking inquiry request.");
        try {
            Long userId = commonService.getUserIdBySecurityContextWithOutException();
            bookingValidationService.validateTourBookingInquiryRequest(tourBookingInquiryRequest);
            if (userId != null) {
                String bookingReference = bookingHelperService.generateUniqueBookingReferance();
                bookingRepository.insertBookingInquiryToBookings(tourBookingInquiryRequest, userId, bookingReference);
            }
            Long tourBookingInquiryId = bookingRepository.insertTourBookingInquiry(tourBookingInquiryRequest, userId);

            if (tourBookingInquiryId == null || tourBookingInquiryId <= 0) {
                throw new InsertFailedErrorExceptionHandler("Failed to insert tour booking inquiry");
            } else {
                String adminSubject = emailHelperService.buildAdminTourBookingSubject(tourBookingInquiryRequest);
                String adminBody = emailHelperService.buildAdminTourBookingBody(tourBookingInquiryRequest, tourBookingInquiryId);

                emailService.sendFromDev(
                        "felicitatrips@gmail.com",
                        adminSubject,
                        adminBody
                );

                if (tourBookingInquiryRequest.getEmail() != null && !tourBookingInquiryRequest.getEmail().isEmpty()) {

                    String customerSubject = emailHelperService.buildCustomerTourBookingSubject();
                    String customerBody = emailHelperService.buildCustomerTourBookingBody(tourBookingInquiryRequest);

                    emailService.sendFromMain(
                            tourBookingInquiryRequest.getEmail(),
                            customerSubject,
                            customerBody
                    );
                }
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully insert tour booking inquiry."),
                    Instant.now());

        } catch (InsertFailedErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while insert booking tours inquiry : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to insert booking tours inquiry to database");
        } finally {
            LOGGER.info("End insert booking tours inquiry to repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> cancelledPendingBooking(BookingCancelledRequest bookingCancelledRequest) {
        LOGGER.info("Start cancelled tour booking inquiry request.");
        try {
            Long userId = commonService.getUserIdBySecurityContext();
            bookingValidationService.validateBookingCancelledRequest(bookingCancelledRequest);
            bookingRepository.cancelledPendingBooking(bookingCancelledRequest, userId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully cancelled tour booking inquiry.", bookingCancelledRequest.getBookingId()),
                    Instant.now());

        } catch (UpdateFailedErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while cancelled booking tours inquiry : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to cancelled booking tours inquiry to database");
        } finally {
            LOGGER.info("End cancelled booking tours inquiry to repository");
        }
    }

    @Override
    public CommonResponse<BookingStatisticsResponse> getBookingStatistics() {
        LOGGER.info("Start fetching booking statistics from repository");
        try {
            BookingStatisticsResponse bookingStatisticsResponse = new BookingStatisticsResponse();

            BookingStatisticsResponse.Summary summary = bookingRepository.getBookingSummaryStatistics();
            bookingStatisticsResponse.setSummary(summary);

            List<BookingStatisticsResponse.MonthlyBookingTrend> monthlyBookingTrends = bookingRepository.getMonthlyBookingTrendsStatistics();
            bookingStatisticsResponse.setMonthlyBookingTrends(monthlyBookingTrends);

            List<BookingStatisticsResponse.MonthlyRevenueTrend> monthlyRevenueTrends = bookingRepository.getMonthlyRevenueTrendsStatistics();
            bookingStatisticsResponse.setMonthlyRevenueTrends(monthlyRevenueTrends);

            List<BookingStatisticsResponse.BookingStatusDistribution> bookingStatusDistributions = bookingRepository.getBookingStatusDistributionsStatistics();
            bookingStatisticsResponse.setBookingStatusDistributions(bookingStatusDistributions);

            List<BookingStatisticsResponse.BookingFunnel> bookingFunnels = bookingRepository.getBookingFunnelsStatistics();
            bookingStatisticsResponse.setBookingFunnels(bookingFunnels);

            List<BookingStatisticsResponse.TopTour> topTours = bookingRepository.getTopToursStatistics();
            bookingStatisticsResponse.setTopTours(topTours);

            List<BookingStatisticsResponse.PopularActivity> popularActivities = bookingRepository.getPopularActivitiesStatistics();
            bookingStatisticsResponse.setPopularActivities(popularActivities);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking statistics from database");
        } finally {
            LOGGER.info("End fetching booking statistics from repository");
        }
    }

    @Override
    public CommonResponse<BookingStatusStatisticsResponse> getBookingStatusStatistics() {
        LOGGER.info("Start fetching booking status statistics from repository");
        try {
            BookingStatusStatisticsResponse bookingStatusStatisticsResponse = new BookingStatusStatisticsResponse();

            BookingStatusStatisticsResponse.Summary summary = bookingRepository.getBookingStatusSummaryStatistics();
            bookingStatusStatisticsResponse.setSummary(summary);

            List<BookingStatusStatisticsResponse.StatusDistribution> statusDistributions = bookingRepository.getStatusDistributionsStatistics();
            bookingStatusStatisticsResponse.setStatusDistributions(statusDistributions);

            List<BookingStatusStatisticsResponse.StatusFunnel> statusFunnels = bookingRepository.getStatusFunnelsStatistics();
            bookingStatusStatisticsResponse.setStatusFunnels(statusFunnels);

            List<BookingStatusStatisticsResponse.StatusTrend> statusTrends = bookingRepository.getStatusTrendsStatistics();
            bookingStatusStatisticsResponse.setStatusTrends(statusTrends);

            List<BookingStatusStatisticsResponse.DropOffStatistics> dropOffStatistics = bookingRepository.getDropOffStatisticsStatistics();
            bookingStatusStatisticsResponse.setDropOffStatistics(dropOffStatistics);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingStatusStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking status statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking status statistics from database");
        } finally {
            LOGGER.info("End fetching booking status statistics from repository");
        }
    }

    @Override
    public CommonResponse<BookingAssignStatisticsResponse> getBookingAssignStatistics() {
        LOGGER.info("Start fetching booking assignment statistics from repository");
        try {
            BookingAssignStatisticsResponse bookingAssignStatisticsResponse = new BookingAssignStatisticsResponse();

            // Summary
            BookingAssignStatisticsResponse.Summary summary = bookingRepository.getBookingAssignSummaryStatistics();
            bookingAssignStatisticsResponse.setSummary(summary);

            // Employee Workloads
            List<BookingAssignStatisticsResponse.EmployeeWorkload> employeeWorkloads = bookingRepository.getEmployeeWorkloadsStatistics();
            bookingAssignStatisticsResponse.setEmployeeWorkloads(employeeWorkloads);

            // Employee Revenues
            List<BookingAssignStatisticsResponse.EmployeeRevenue> employeeRevenues = bookingRepository.getEmployeeRevenuesStatistics();
            bookingAssignStatisticsResponse.setEmployeeRevenues(employeeRevenues);

            // Department Distributions
            List<BookingAssignStatisticsResponse.DepartmentDistribution> departmentDistributions = bookingRepository.getDepartmentDistributionsStatistics();
            bookingAssignStatisticsResponse.setDepartmentDistributions(departmentDistributions);

            // Designation Distributions
            List<BookingAssignStatisticsResponse.DesignationDistribution> designationDistributions = bookingRepository.getDesignationDistributionsStatistics();
            bookingAssignStatisticsResponse.setDesignationDistributions(designationDistributions);

            // Monthly Assignment Trends
            List<BookingAssignStatisticsResponse.MonthlyAssignmentTrend> monthlyAssignmentTrends = bookingRepository.getMonthlyAssignmentTrendsStatistics();
            bookingAssignStatisticsResponse.setMonthlyAssignmentTrends(monthlyAssignmentTrends);

            // Assignment Status Distributions
            List<BookingAssignStatisticsResponse.AssignmentStatusDistribution> assignmentStatusDistributions = bookingRepository.getAssignmentStatusDistributionsStatistics();
            bookingAssignStatisticsResponse.setAssignmentStatusDistributions(assignmentStatusDistributions);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingAssignStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking assignment statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking assignment statistics from database");
        } finally {
            LOGGER.info("End fetching booking assignment statistics from repository");
        }
    }

    @Override
    public CommonResponse<BookingHistoryStatisticsResponse> getBookingHistoryStatistics() {
        LOGGER.info("Start fetching booking history statistics from repository");
        try {
            BookingHistoryStatisticsResponse bookingHistoryStatisticsResponse = new BookingHistoryStatisticsResponse();

            // Summary
            BookingHistoryStatisticsResponse.Summary summary = bookingRepository.getBookingHistorySummaryStatistics();
            bookingHistoryStatisticsResponse.setSummary(summary);

            // Booking Growth Trends
            List<BookingHistoryStatisticsResponse.BookingGrowthTrend> bookingGrowthTrends = bookingRepository.getBookingGrowthTrendsStatistics();
            bookingHistoryStatisticsResponse.setBookingGrowthTrends(bookingGrowthTrends);

            // Revenue Growth Trends
            List<BookingHistoryStatisticsResponse.RevenueGrowthTrend> revenueGrowthTrends = bookingRepository.getRevenueGrowthTrendsStatistics();
            bookingHistoryStatisticsResponse.setRevenueGrowthTrends(revenueGrowthTrends);

            // Booking Status Histories
            List<BookingHistoryStatisticsResponse.BookingStatusHistory> bookingStatusHistories = bookingRepository.getBookingStatusHistoriesStatistics();
            bookingHistoryStatisticsResponse.setBookingStatusHistories(bookingStatusHistories);

            // Cancellation Trends
            List<BookingHistoryStatisticsResponse.CancellationTrend> cancellationTrends = bookingRepository.getCancellationTrendsStatistics();
            bookingHistoryStatisticsResponse.setCancellationTrends(cancellationTrends);

            // Historical Top Tours
            List<BookingHistoryStatisticsResponse.HistoricalTopTour> historicalTopTours = bookingRepository.getHistoricalTopToursStatistics();
            bookingHistoryStatisticsResponse.setHistoricalTopTours(historicalTopTours);

            // Customer Return Statistics
            List<BookingHistoryStatisticsResponse.CustomerReturnStatistics> customerReturnStatistics = bookingRepository.getCustomerReturnStatisticsStatistics();
            bookingHistoryStatisticsResponse.setCustomerReturnStatistics(customerReturnStatistics);

            // Peak Booking Periods
            List<BookingHistoryStatisticsResponse.PeakBookingPeriod> peakBookingPeriods = bookingRepository.getPeakBookingPeriodsStatistics();
            bookingHistoryStatisticsResponse.setPeakBookingPeriods(peakBookingPeriods);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingHistoryStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking history statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking history statistics from database");
        } finally {
            LOGGER.info("End fetching booking history statistics from repository");
        }
    }

    @Override
    public CommonResponse<BookingWithParamsResponse> getBookingsWithParams(BookingDataRequest bookingDataRequest) {
        LOGGER.info("Start fetching bookings with params from repository");
        try {
            bookingValidationService.validateBookingDataRequest(bookingDataRequest);

            BookingWithParamsResponse bookingWithParams = new BookingWithParamsResponse();

            List<BookingsBasicDetails> bookingsBasicDetails = bookingRepository.getBookingBasicDetailsForParams(bookingDataRequest);
            Integer bookingCountForParms = bookingRepository.getBookingCountForParams(bookingDataRequest);

            bookingWithParams.setBookingsBasicDetails(bookingsBasicDetails);
            bookingWithParams.setBookingCount(bookingCountForParms);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingWithParams,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching bookings with params: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch bookings with params from database");
        } finally {
            LOGGER.info("End fetching bookings with params from repository");
        }
    }

    @Override
    public CommonResponse<BookingsRequestParamsResponse> getBookingsParamsData() {
        LOGGER.info("Start fetching bookings params data from repository");
        try {
            BookingsRequestParamsResponse bookingsParamsData = bookingRepository.getBookingsParamsData();

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingsParamsData,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching bookings params data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch bookings params data from database");
        } finally {
            LOGGER.info("End fetching bookings params data from repository");
        }
    }

    @Override
    public CommonResponse<BookingAllDetailsResponse> getBookingAllDetailsById(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start fetching booking all details by id from repository");
        try {
            Long bookingId = commonIdRequest.getId();

            BookingAllDetailsResponse bookingAllDetails = new BookingAllDetailsResponse();

            // 1. Booking Information
            BookingAllDetailsResponse.BookingInformation bookingInformation = bookingRepository.getBookingInformationById(bookingId);
            bookingAllDetails.setBookingInformation(bookingInformation);
            LOGGER.info("Booking information: {}", bookingInformation);

            // 2. Customer Information
            BookingAllDetailsResponse.CustomerInformation customerInformation = bookingRepository.getCustomerInformationByBookingId(bookingId);
            bookingAllDetails.setCustomerInformation(customerInformation);
            LOGGER.info("Customer information: {}", customerInformation);

            // 3. Tour Information
            BookingAllDetailsResponse.TourInformation tourInformation = bookingRepository.getTourInformationByBookingId(bookingId);
            bookingAllDetails.setTourInformation(tourInformation);
            LOGGER.info("Tour information: {}", tourInformation);

            // 4. Package Information
            BookingAllDetailsResponse.PackageInformation packageInformation = bookingRepository.getPackageInformationByBookingId(bookingId);
            bookingAllDetails.setPackageInformation(packageInformation);
            LOGGER.info("Package information: {}", packageInformation);

            // 5. Booking Status Information
            BookingAllDetailsResponse.BookingStatusInformation bookingStatusInformation = bookingRepository.getBookingStatusInformationByBookingId(bookingId);
            bookingAllDetails.setBookingStatusInformation(bookingStatusInformation);
            LOGGER.info("Booking status information: {}", bookingStatusInformation);

            // 6. Assignment Information
            BookingAllDetailsResponse.AssignmentInformation assignmentInformation = bookingRepository.getAssignmentInformationByBookingId(bookingId);
            bookingAllDetails.setAssignmentInformation(assignmentInformation);
            LOGGER.info("Assignment information: {}", assignmentInformation);

            // 7. Cancellation Information
            BookingAllDetailsResponse.CancellationInformation cancellationInformation = bookingRepository.getCancellationInformationByBookingId(bookingId);
            bookingAllDetails.setCancellationInformation(cancellationInformation);
            LOGGER.info("Cancellation information: {}", cancellationInformation);

            // 8. Participants List
            List<BookingAllDetailsResponse.ParticipantInformation> participants = bookingRepository.getParticipantsByBookingId(bookingId);
            bookingAllDetails.setParticipants(participants);
            LOGGER.info("Participants: {}", participants);

            // 9. Accommodations List
            List<BookingAllDetailsResponse.AccommodationInformation> accommodations = bookingRepository.getAccommodationsByBookingId(bookingId);
            bookingAllDetails.setAccommodations(accommodations);
            LOGGER.info("Accommodations: {}", accommodations);

            // 10. Transportations List
            List<BookingAllDetailsResponse.TransportationInformation> transportations = bookingRepository.getTransportationsByBookingId(bookingId);
            bookingAllDetails.setTransportations(transportations);
            LOGGER.info("Transportations: {}", transportations);

            // 11. Activities List
            List<BookingAllDetailsResponse.ActivityInformation> activities = bookingRepository.getActivitiesByBookingId(bookingId);
            bookingAllDetails.setActivities(activities);
            LOGGER.info("Activities: {}", activities);

            List<BookingAllDetailsResponse.BookingDocuments> bookingDocuments = bookingRepository.getBookingDocumentsByBookingId(bookingId);
            bookingAllDetails.setDocuments(bookingDocuments);
            LOGGER.info("Booking Documents: {}", bookingDocuments);

            BookingAllDetailsResponse.BookingInsurance bookingInsurance = bookingRepository.getBookingInsuranceByBookingId(bookingId);
            bookingAllDetails.setBookingInsurance(bookingInsurance);
            LOGGER.info("Booking Insurance: {}", bookingInsurance);

            List<BookingAllDetailsResponse.BookingItinerary> bookingItineraries = bookingRepository.getBookingItineraryByBookingId(bookingId);
            bookingAllDetails.setBookingItineraries(bookingItineraries);
            LOGGER.info("Booking Itineraries: {}", bookingItineraries);

            List<BookingAllDetailsResponse.BookingNote> bookingNotes = bookingRepository.getBookingNoteByBookingId(bookingId);
            bookingAllDetails.setBookingNotes(bookingNotes);
            LOGGER.info("Booking Notes: {}", bookingNotes);

            List<BookingAllDetailsResponse.BookingPriceBreakDown> bookingPriceBreakDowns = bookingRepository.getBookingPriceBreakDownByBookingId(bookingId);
            bookingAllDetails.setPriceBreakDowns(bookingPriceBreakDowns);
            LOGGER.info("Booking Price Breakdowns: {}", bookingPriceBreakDowns);

            BookingAllDetailsResponse.BookingInvoice bookingInvoice = bookingRepository.getBookingInvoiceByBookingId(bookingId);
            bookingAllDetails.setBookingInvoice(bookingInvoice);
            LOGGER.info("Booking Invoice: {}", bookingInvoice);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingAllDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching booking all details: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking all details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking all details from database");
        } finally {
            LOGGER.info("End fetching booking all details from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> createBooking(InsertBookingRequest insertBookingRequest) {
        LOGGER.info("Start creating booking from repository");
        try {
            bookingValidationService.validateInsertBookingRequest(insertBookingRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            String bookingReference = commonService.createBooingReference(userId);

            Long bookingId = bookingRepository.createBooking(insertBookingRequest, bookingReference, userId);
            bookingRepository.addParticipantsToBooking(bookingId, insertBookingRequest.getParticipants(), userId);
            bookingRepository.addAccommodationsToBooking(bookingId, insertBookingRequest.getAccommodations(), userId);
            bookingRepository.addTransportationsToBooking(bookingId, insertBookingRequest.getTransportations(), userId);
            bookingRepository.addActivitiesToBooking(bookingId, insertBookingRequest.getActivities(), userId);
            bookingRepository.addDocumentsToBooking(bookingId, insertBookingRequest.getDocuments(), userId);
            bookingRepository.addInsuranceToBooking(bookingId, insertBookingRequest.getBookingInsurance(), userId);
            bookingRepository.addItinerariesToBooking(bookingId, insertBookingRequest.getBookingItineraries(), userId);
            bookingRepository.addNotesToBooking(bookingId, insertBookingRequest.getBookingNotes(), userId);
            bookingRepository.addPriceBreakdownToBooking(bookingId, insertBookingRequest.getPriceBreakDowns(), userId);
            String invoiceReference = commonService.createBookingInvoiceReference(bookingId, userId);
            bookingRepository.addBookingInvoiceToBooking(bookingId, invoiceReference, insertBookingRequest.getBookingInvoice(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            LOGGER.info("Supervisor Details: {}", supervisorDetails);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            LOGGER.info("AAA");
            if (!supervisorUserIds.isEmpty()) {
                supervisorUserIds.add(userId);
            }
            LOGGER.info("BBB");

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("bookingId", bookingId);
            metadata.put("bookingReference", bookingReference);
            metadata.put("packageScheduleId", insertBookingRequest.getPackageScheduleId());
            metadata.put("totalPersons", insertBookingRequest.getTotalPersons());
            metadata.put("createdBy", userId);


            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKING_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Booking Created")
                    .message("A new booking '" + bookingReference + "' has been created.")
                    .actionUrl(VIEW_BOOKING_DETAILS + "/" + bookingId)
                    .actionText("View Booking")
                    .icon("CalendarCheck")
                    .color("#10B981")
                    .metadata(metadata)
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKING_CREATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();
            LOGGER.info("CCC");
            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            LOGGER.info("DDD");
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);
            LOGGER.info("EEE");
            if (bookingId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKING_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = bookingEmailHelperService.buildBookingCreateSuccessfullBody(insertBookingRequest, bookingId, loggedUser);
                String subject = bookingEmailHelperService.buildBookingCreateSuccessfullSubject(insertBookingRequest, bookingId, loggedUser);
//                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse(""),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating booking: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to create booking in database");
        } finally {
            LOGGER.info("End creating booking from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateBooking(UpdateBookingRequest updateBookingRequest) {
        LOGGER.info("Start updating booking from repository");
        try {
            bookingValidationService.validateUpdateBookingRequest(updateBookingRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();
            Long bookingId = updateBookingRequest.getBookingId();

            BookingAllDetailsResponse previousBookingDetails = getBookingAllDetailsById(new CommonIdRequest(updateBookingRequest.getBookingId())).getData();

            bookingRepository.updateBookingBasicInformation(updateBookingRequest, userId);

            bookingRepository.addParticipantsToBooking(bookingId, updateBookingRequest.getAddParticipants(), userId);
            bookingRepository.removeParticipantsFromBooking(bookingId, updateBookingRequest.getRemoveParticipants(), userId);
            bookingRepository.updateParticipantsOfBooking(bookingId, updateBookingRequest.getUpdateParticipants(), userId);

            bookingRepository.addAccommodationsToBooking(bookingId, updateBookingRequest.getAddAccommodations(), userId);
            bookingRepository.removeAccommodationsFromBooking(bookingId, updateBookingRequest.getRemoveAccommodations(), userId);
            bookingRepository.updateAccommodationsOfBooking(bookingId, updateBookingRequest.getUpdateAccommodations(), userId);

            bookingRepository.addTransportationsToBooking(bookingId, updateBookingRequest.getAddTransportations(), userId);
            bookingRepository.removeTransportationsFromBooking(bookingId, updateBookingRequest.getRemoveTransportations(), userId);
            bookingRepository.updateTransportationsOfBooking(bookingId, updateBookingRequest.getUpdateTransportations(), userId);

            bookingRepository.addActivitiesToBooking(bookingId, updateBookingRequest.getAddActivities(), userId);
            bookingRepository.removeActivitiesFromBooking(bookingId, updateBookingRequest.getRemoveActivities(), userId);
            bookingRepository.updateActivitiesOfBooking(bookingId, updateBookingRequest.getUpdateActivities(), userId);

            bookingRepository.addDocumentsToBooking(bookingId, updateBookingRequest.getAddDocuments(), userId);
            bookingRepository.removeDocumentsFromBooking(bookingId, updateBookingRequest.getRemoveDocuments(), userId);
            bookingRepository.updateDocumentsOfBooking(bookingId, updateBookingRequest.getUpdateDocuments(), userId);

            bookingRepository.addInsuranceToBooking(bookingId, updateBookingRequest.getAddBookingInsurance(), userId);
            bookingRepository.updateInsuranceOfBooking(bookingId, updateBookingRequest.getUpdateBookingInsurance(), userId);
            bookingRepository.removeInsuranceFromBooking(bookingId, updateBookingRequest.getRemoveBookingInsurance(), userId);

            bookingRepository.addItinerariesToBooking(bookingId, updateBookingRequest.getAddBookingItineraries(), userId);
            bookingRepository.removeItinerariesFromBooking(bookingId, updateBookingRequest.getRemoveBookingItineraries(), userId);
            bookingRepository.updateItinerariesOfBooking(bookingId, updateBookingRequest.getUpdateBookingItineraries(), userId);

            bookingRepository.addNotesToBooking(bookingId, updateBookingRequest.getAddBookingNotes(), userId);
            bookingRepository.removeNotesFromBooking(bookingId, updateBookingRequest.getRemoveBookingNotes(), userId);
            bookingRepository.updateNotesOfBooking(bookingId, updateBookingRequest.getUpdateBookingNotes(), userId);

            bookingRepository.addPriceBreakdownToBooking(bookingId, updateBookingRequest.getAddPriceBreakDowns(), userId);
            bookingRepository.removePriceBreakdownFromBooking(bookingId, updateBookingRequest.getRemovePriceBreakDowns(), userId);
            bookingRepository.updatePriceBreakdownOfBooking(bookingId, updateBookingRequest.getUpdatePriceBreakDowns(), userId);

            if (updateBookingRequest.getAddBookingInvoice() != null) {
                String invoiceReference = commonService.createBookingInvoiceReference(bookingId, userId);
                bookingRepository.addBookingInvoiceToBooking(bookingId, invoiceReference, updateBookingRequest.getAddBookingInvoice(), userId);
            }
            bookingRepository.updateBookingInvoiceOfBooking(bookingId, updateBookingRequest.getUpdateBookingInvoice(), userId);
            bookingRepository.removeBookingInvoiceFromBooking(bookingId, updateBookingRequest.getRemoveBookingInvoice(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKING_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Booking Updated")
                    .message("The booking '" + previousBookingDetails.getBookingInformation().getBookingReference() + "' has been updated.")
                    .actionUrl(VIEW_BOOKING_DETAILS + "/" + updateBookingRequest.getBookingId())
                    .actionText("View Booking")
                    .icon("CalendarClock")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "bookingId", updateBookingRequest.getBookingId(),
                            "bookingReference", previousBookingDetails.getBookingInformation().getBookingReference(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKING_UPDATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            BookingComparisonResult comparisonResult = compareBookingsUpdates(
                    updateBookingRequest,
                    previousBookingDetails
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKING_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = bookingEmailHelperService.buildBookingUpdateSuccessfullSubject(loggedUser, updateBookingRequest.getBookingId());
            String body = bookingEmailHelperService.buildBookingUpdateSuccessfullBody(loggedUser, updateBookingRequest.getBookingId(), comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse(),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating booking: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update booking in database");
        } finally {
            LOGGER.info("End updating booking from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateBooking(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start terminating booking by id from repository");
        try {
            bookingValidationService.validateCommonIdRequest(commonIdRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();
            Long bookingId = commonIdRequest.getId();
            BookingAllDetailsResponse bookingDetails = getBookingAllDetailsById(commonIdRequest).getData();

            bookingRepository.updateBookingStatus(new UpdateBookingStatusRequest(bookingId, BookingStatus.CANCELLED.name()), userId);

            List<Long> participants = Optional.ofNullable(bookingDetails.getParticipants())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(BookingAllDetailsResponse.ParticipantInformation::getParticipantId)
                    .toList();
            bookingRepository.removeParticipantsFromBooking(bookingId, participants, userId);

            List<Long> accommodations = Optional.ofNullable(bookingDetails.getAccommodations())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(BookingAllDetailsResponse.AccommodationInformation::getAccommodationId)
                    .toList();
            bookingRepository.removeAccommodationsFromBooking(bookingId, accommodations, userId);

            List<Long> transportations = Optional.ofNullable(bookingDetails.getTransportations())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(BookingAllDetailsResponse.TransportationInformation::getTransportationId)
                    .toList();
            bookingRepository.removeTransportationsFromBooking(bookingId, transportations, userId);

            List<Long> activities = Optional.ofNullable(bookingDetails.getActivities())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(BookingAllDetailsResponse.ActivityInformation::getBookingActivityId)
                    .toList();
            bookingRepository.removeActivitiesFromBooking(bookingId, activities, userId);

            List<Long> documents = Optional.ofNullable(bookingDetails.getDocuments())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(BookingAllDetailsResponse.BookingDocuments::getDocumentId)
                    .toList();
            bookingRepository.removeDocumentsFromBooking(bookingId, documents, userId);

            Long bookingInsurance = Optional.ofNullable(bookingDetails.getBookingInsurance())
                    .map(BookingAllDetailsResponse.BookingInsurance::getInsuranceId)
                    .orElse(null);
            bookingRepository.removeInsuranceFromBooking(bookingId, bookingInsurance, userId);

            List<Long> bookingItineraries = Optional.ofNullable(bookingDetails.getBookingItineraries())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(BookingAllDetailsResponse.BookingItinerary::getItineraryId)
                    .toList();
            bookingRepository.removeItinerariesFromBooking(bookingId, bookingItineraries, userId);

            List<Long> bookingNotes = Optional.ofNullable(bookingDetails.getBookingNotes())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(BookingAllDetailsResponse.BookingNote::getNoteId)
                    .toList();
            bookingRepository.removeNotesFromBooking(bookingId, bookingNotes, userId);

            List<Long> priceBreakDowns = Optional.ofNullable(bookingDetails.getPriceBreakDowns())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(BookingAllDetailsResponse.BookingPriceBreakDown::getPriceBreakDownId)
                    .toList();
            bookingRepository.removePriceBreakdownFromBooking(bookingId, priceBreakDowns, userId);

            Long bookingInvoice = Optional.ofNullable(bookingDetails.getBookingInvoice())
                    .map(BookingAllDetailsResponse.BookingInvoice::getInvoiceId)
                    .orElse(null);
            bookingRepository.removeBookingInvoiceFromBooking(bookingId, bookingInvoice, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKING_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Booking Cancelled")
                    .message("The booking '" + bookingDetails.getBookingInformation().getBookingReference() + "' has been cancelled.")
                    .actionUrl(VIEW_BOOKING_DETAILS + "/" + bookingDetails.getBookingInformation().getBookingId())
                    .actionText("View Booking")
                    .icon("CalendarX")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "bookingId", bookingDetails.getBookingInformation().getBookingId(),
                            "bookingReference", bookingDetails.getBookingInformation().getBookingReference(),
                            "status", bookingDetails.getBookingStatusInformation().getBookingStatusName(),
                            "cancelledBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKING_TERMINATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKING_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = bookingEmailHelperService.buildBookingTerminateSuccessfullSubject(loggedUser, bookingDetails);
            String body = bookingEmailHelperService.buildBookingTerminateSuccessfullBody(loggedUser, bookingDetails);

//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse(),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating booking: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate booking in database");
        } finally {
            LOGGER.info("End terminating booking from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateBookingStatus(UpdateBookingStatusRequest updateBookingStatusRequest) {
        LOGGER.info("Start updating booking status from repository");
        try {
            bookingValidationService.validateUpdateBookingStatusRequest(updateBookingStatusRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            BookingsBasicDetails bookingsBasicDetails = getBookingBasicDetails(new CommonIdRequest(updateBookingStatusRequest.getBookingId())).getData();

            bookingRepository.updateBookingStatus(updateBookingStatusRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKING_STATUS_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Booking Status Updated")
                    .message("The booking '" + bookingsBasicDetails.getBookingReference() + "' status has been updated to " + updateBookingStatusRequest.getBookingStatus() + ".")
                    .actionUrl(VIEW_BOOKING_DETAILS + "/" + bookingsBasicDetails.getBookingId())
                    .actionText("View Booking")
                    .icon("RefreshCcw")
                    .color("#F59E0B")
                    .metadata(Map.of(
                            "bookingId", bookingsBasicDetails.getBookingId(),
                            "bookingReference", bookingsBasicDetails.getBookingReference(),
                            "oldStatus", bookingsBasicDetails.getBookingStatusName(),
                            "newStatus", updateBookingStatusRequest.getBookingStatus(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKING_STATUS_UPDATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            BookingStatusComparisonResult comparisonResult = compareBookingStatusUpdates(
                    bookingsBasicDetails,
                    updateBookingStatusRequest
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKING_STATUS_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = bookingEmailHelperService.buildBookingStatusUpdateSuccessfullSubject(loggedUser, updateBookingStatusRequest.getBookingId(), bookingsBasicDetails);
            String body = bookingEmailHelperService.buildBookingStatusUpdateSuccessfullBody(loggedUser, updateBookingStatusRequest.getBookingId(), comparisonResult, bookingsBasicDetails);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse(),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating booking status: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update booking status in database");
        } finally {
            LOGGER.info("End updating booking status from repository");
        }
    }

    private BookingStatusComparisonResult compareBookingStatusUpdates(
            BookingsBasicDetails bookingsBasicDetails,
            UpdateBookingStatusRequest updateBookingStatusRequest) {

        BookingStatusComparisonResult.BookingStatusComparisonResultBuilder resultBuilder =
                BookingStatusComparisonResult.builder();

        List<String> changes = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        boolean hasChanges = false;

        // Get current status details
        Long oldStatusId = bookingsBasicDetails.getBookingStatusId();
        String oldStatusName = bookingsBasicDetails.getBookingStatusName();

        // Parse new status
        String newStatusStr = updateBookingStatusRequest.getBookingStatus();
        Long newStatusId = getStatusIdByName(newStatusStr);
        String newStatusName = newStatusStr;

        // Check if status is changing
        if (oldStatusId != null && newStatusId != null && !oldStatusId.equals(newStatusId)) {
            changes.add(String.format("Booking status changed from '%s' (%d) to '%s' (%d)",
                    oldStatusName, oldStatusId, newStatusName, newStatusId));
            hasChanges = true;

            // Add warnings based on status transitions
            addStatusChangeWarnings(warnings, oldStatusId, newStatusId);
        } else if (oldStatusId != null && oldStatusId.equals(newStatusId)) {
            changes.add("Booking status remains unchanged: " + oldStatusName);
        }

        // Build the result
        return resultBuilder
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .oldStatusId(oldStatusId)
                .oldStatusName(oldStatusName)
                .newStatusId(newStatusId)
                .newStatusName(newStatusName)
                .changedBy("System") // You can pass user here if needed
                .changeTimestamp(new Date().toString())
                .build();
    }

    // Helper method to get status ID by name
    private Long getStatusIdByName(String statusName) {
        if (statusName == null) return null;

        switch (statusName.toUpperCase()) {
            case "NEW_INQUIRY":
                return 1L;
            case "PENDING":
                return 2L;
            case "CONTACTED":
                return 3L;
            case "QUOTATION_SENT":
                return 4L;
            case "NEGOTIATION":
                return 5L;
            case "CONFIRMED":
                return 6L;
            case "PAYMENT_PENDING":
                return 7L;
            case "BOOKED":
                return 8L;
            case "COMPLETED":
                return 9L;
            case "CANCELLED":
                return 10L;
            case "REJECTED":
                return 11L;
            case "EXPIRED":
                return 12L;
            default:
                return null;
        }
    }

    // Helper method to add warnings for status transitions
    private void addStatusChangeWarnings(List<String> warnings, Long oldStatusId, Long newStatusId) {
        // Cancellation warnings
        if (newStatusId == 10L) { // CANCELLED
            warnings.add("Warning: Booking is being cancelled!");
            if (oldStatusId == 8L) { // BOOKED
                warnings.add("Alert: Cancelling a BOOKED booking may require refund processing");
            }
            if (oldStatusId == 9L) { // COMPLETED
                warnings.add("Alert: Cancelling a COMPLETED booking requires special approval");
            }
        }

        // Confirmation warnings
        if (newStatusId == 6L && oldStatusId != 6L) { // CONFIRMED
            warnings.add("Note: Booking is being confirmed");
            if (oldStatusId == 5L) { // NEGOTIATION
                warnings.add("Info: Booking confirmed after negotiation");
            }
        }

        // Completion warning
        if (newStatusId == 9L) { // COMPLETED
            warnings.add("Note: Booking marked as completed");
        }

        // Rejection warning
        if (newStatusId == 11L) { // REJECTED
            warnings.add("Warning: Booking is being rejected");
        }

        // Expiry warning
        if (newStatusId == 12L) { // EXPIRED
            warnings.add("Note: Booking has expired");
        }

        // Payment related warnings
        if (newStatusId == 7L) { // PAYMENT_PENDING
            warnings.add("Note: Booking is pending payment");
        }

        if (newStatusId == 8L && oldStatusId == 7L) { // BOOKED from PAYMENT_PENDING
            warnings.add("Info: Payment received, booking confirmed as BOOKED");
        }
    }

    @Override
    public CommonResponse<BookingsBasicDetails> getBookingBasicDetails(CommonIdRequest bookingId) {
        LOGGER.info("Start fetching booking basic details by id from repository");
        try {
            BookingsBasicDetails bookingsBasicDetails = bookingRepository.getBookingBasicDetails(bookingId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingsBasicDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking basic details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking basic details from database");
        } finally {
            LOGGER.info("End fetching booking basic details from repository");
        }
    }

    @Override
    public CommonResponse<List<BookingStatusBasicDetailsResponse>> getBookingsStatuses() {
        LOGGER.info("Start fetching booking statuses from repository");
        try {
            List<BookingStatusBasicDetailsResponse> bookingStatuses = bookingRepository.getBookingsStatuses();

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingStatuses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking statuses: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking statuses from database");
        } finally {
            LOGGER.info("End fetching booking statuses from repository");
        }
    }

    @Override
    public CommonResponse<BookingStatusBasicDetailsResponse> getBookingsStatusesBasicDetailsById(CommonIdRequest bookingId) {
        LOGGER.info("Start fetching booking status basic details by id from repository");
        try {
            BookingStatusBasicDetailsResponse bookingStatusBasicDetails = bookingRepository.getBookingsStatusesBasicDetailsById(bookingId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingStatusBasicDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking status basic details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking status basic details from database");
        } finally {
            LOGGER.info("End fetching booking status basic details from repository");
        }
    }

    @Override
    public CommonResponse<BookingStatusDetailsResponse> getBookingsStatusesAllDetailsById(CommonIdRequest bookingId) {
        LOGGER.info("Start fetching booking status all details by id from repository");
        try {
            BookingStatusDetailsResponse bookingStatusAllDetails = bookingRepository.getBookingsStatusesAllDetailsById(bookingId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingStatusAllDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking status all details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking status all details from database");
        } finally {
            LOGGER.info("End fetching booking status all details from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> createBookingsStatuses(InsertBookingsStatusesRequest insertBookingsStatusesRequest) {
        LOGGER.info("Start creating booking status from repository");
        try {
            bookingValidationService.validateInsertBookingsStatusesRequest(insertBookingsStatusesRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long bookingStatusId = bookingRepository.createBookingsStatuses(insertBookingsStatusesRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKINGS_STATUSES_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Booking Status Created")
                    .message("A new booking status '" + insertBookingsStatusesRequest.getStatusName() + "' has been created.")
                    .actionUrl(VIEW_BOOKING_STATUS_DETAILS + "/" + bookingStatusId)
                    .actionText("View Booking Status")
                    .icon("TagPlus")
                    .color("#10B981")
                    .metadata(Map.of(
                            "bookingStatusId", bookingStatusId,
                            "statusName", insertBookingsStatusesRequest.getStatusName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKINGS_STATUSES_CREATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (bookingStatusId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKINGS_STATUSES_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = bookingEmailHelperService.buildBookingsStatusesCreateSuccessfullBody(insertBookingsStatusesRequest, bookingStatusId, loggedUser);
                String subject = bookingEmailHelperService.buildBookingsStatusesCreateSuccessfullSubject(insertBookingsStatusesRequest, bookingStatusId, loggedUser);
//                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse(),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating booking status: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to create booking status in database");
        } finally {
            LOGGER.info("End creating booking status from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateBookingsStatuses(UpdateBookingsStatusesRequest updateBookingsStatusesRequest) {
        LOGGER.info("Start updating booking status from repository");
        try {
            bookingValidationService.validateUpdateBookingsStatusesRequest(updateBookingsStatusesRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            BookingStatusBasicDetailsResponse previousBookingStatusDetails = getBookingsStatusesBasicDetailsById(new CommonIdRequest(updateBookingsStatusesRequest.getStatusId())).getData();

            bookingRepository.updateBookingsStatuses(updateBookingsStatusesRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKINGS_STATUSES_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Booking Status Updated")
                    .message("The booking status '" + updateBookingsStatusesRequest.getStatusName() + "' has been updated.")
                    .actionUrl(VIEW_BOOKING_STATUS_DETAILS + "/" + updateBookingsStatusesRequest.getStatusId())
                    .actionText("View Booking Status")
                    .icon("Tag")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "bookingStatusId", updateBookingsStatusesRequest.getStatusId(),
                            "statusName", updateBookingsStatusesRequest.getStatusName(),
                            "status", updateBookingsStatusesRequest.getStatus(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKING_STATUS_UPDATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            BookingsStatusesComparisonResult comparisonResult = compareBookingsStatusesUpdates(
                    updateBookingsStatusesRequest,
                    previousBookingStatusDetails
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKINGS_STATUSES_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = bookingEmailHelperService.buildBookingsStatusesUpdateSuccessfullSubject(loggedUser, updateBookingsStatusesRequest.getStatusId());
            String body = bookingEmailHelperService.buildBookingsStatusesUpdateSuccessfullBody(loggedUser, updateBookingsStatusesRequest.getStatusId(), comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse(),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating booking status: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update booking status in database");
        } finally {
            LOGGER.info("End updating booking status from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateBookingsStatuses(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start terminating booking status by id from repository");
        try {
            bookingValidationService.validateCommonIdRequest(commonIdRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            BookingStatusDetailsResponse bookingStatusResponse = getBookingsStatusesAllDetailsById(commonIdRequest).getData();
            bookingRepository.terminateBookingsStatuses(commonIdRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKINGS_STATUSES_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Booking Status Terminated")
                    .message("The booking status '" + bookingStatusResponse.getStatusName() + "' has been terminated.")
                    .actionUrl(VIEW_BOOKING_STATUS_DETAILS + "/" + bookingStatusResponse.getStatusId())
                    .actionText("View Booking Status")
                    .icon("TagX")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "bookingStatusId", bookingStatusResponse.getStatusId(),
                            "statusName", bookingStatusResponse.getStatusName(),
                            "status", bookingStatusResponse.getStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKINGS_STATUSES_TERMINATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKINGS_STATUSES_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = bookingEmailHelperService.buildBookingsStatusesTerminateSuccessfullSubject(loggedUser, bookingStatusResponse);
            String body = bookingEmailHelperService.buildBookingsStatusesTerminateSuccessfullBody(loggedUser, bookingStatusResponse);

//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse(),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating booking status: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate booking status in database");
        } finally {
            LOGGER.info("End terminating booking status from repository");
        }
    }

    @Override
    public CommonResponse<BookingBillResponse> getBookingBillingDetails(CommonIdRequest bookingId) {
        LOGGER.info("Start fetching booking billing details by id from repository");
        try {
            Long id = bookingId.getId();

            BookingBillResponse bookingBillResponse = new BookingBillResponse();

            // 1. Basic Booking Information
            BookingBillResponse.BookingBasicInfo basicInfo = bookingRepository.getBookingBasicInfoForBill(id);
            bookingBillResponse.setBookingId(basicInfo.getBookingId());
            bookingBillResponse.setBookingReference(basicInfo.getBookingReference());
            bookingBillResponse.setBookingDate(basicInfo.getBookingDate());

            // 2. Customer Information
            BookingBillResponse.Customer customer = bookingRepository.getCustomerForBill(id);
            bookingBillResponse.setCustomer(customer);

            // 3. Tour Details
            BookingBillResponse.TourDetails tourDetails = bookingRepository.getTourDetailsForBill(id);
            bookingBillResponse.setTour(tourDetails);

            // 4. Package Details (if applicable)
            BookingBillResponse.PackageDetails packageDetails = bookingRepository.getPackageDetailsForBill(id);
            bookingBillResponse.setPackageDetails(packageDetails);

            // 5. Participants
            List<BookingBillResponse.Participant> participants = bookingRepository.getParticipantsForBill(id);
            bookingBillResponse.setParticipants(participants);

            // 6. Price Breakdown
            List<BookingBillResponse.PriceItem> priceBreakdown = bookingRepository.getPriceBreakdownForBill(id);
            bookingBillResponse.setPriceBreakdown(priceBreakdown);

            // 7. Billing Summary
            BookingBillResponse.BillingSummary billingSummary = bookingRepository.getBillingSummaryForBill(id);
            bookingBillResponse.setBillingSummary(billingSummary);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingBillResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking billing details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking billing details from database");
        } finally {
            LOGGER.info("End fetching booking billing details from repository");
        }
    }

    @Override
    public CommonResponse<List<BookingIdAndReferenceResponse>> getBookingIdAndReferences() {
        LOGGER.info("Start fetching booking id and references from repository");
        try {
            List<BookingIdAndReferenceResponse> bookingIdAndReferences = bookingRepository.getBookingIdAndReferences(BookingAssignStatus.ALL.name());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingIdAndReferences,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking id and references: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking id and references from database");
        } finally {
            LOGGER.info("End fetching booking id and references from repository");
        }
    }

    @Override
    public CommonResponse<UnassignBookingWithParamsResponse> getUnassignBookingsWithParams(UnassignBookingDataRequest unassignBookingDataRequest) {
        LOGGER.info("Start fetching unassign bookings with params from repository");
        try {
            UnassignBookingWithParamsResponse unassignBookingWithParams = new UnassignBookingWithParamsResponse();
            List<UnassignBookingBasicDetailsResponse> unassignBookingBasicDetailsResponseList = bookingRepository.getUnassignBookingBasicDetails(unassignBookingDataRequest);
            Integer count = bookingRepository.getUnassignBookingBasicDetailsCount(unassignBookingDataRequest);

            unassignBookingWithParams.setUnassignBookingBasicDetailsResponses(unassignBookingBasicDetailsResponseList);
            unassignBookingWithParams.setUnassignBookingCount(count);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    unassignBookingWithParams,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching unassign bookings with params: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch unassign bookings with params from database");
        } finally {
            LOGGER.info("End fetching unassign bookings with params from repository");
        }
    }

    @Override
    public CommonResponse<UnassignBookingsRequestParamsResponse> getUnassignBookingsParamsData() {
        LOGGER.info("Start fetching unassign bookings params data from repository");
        try {
            UnassignBookingsRequestParamsResponse unassignBookingsParamsData = new UnassignBookingsRequestParamsResponse();

            unassignBookingsParamsData.setBookingRefences(bookingRepository.getUnassignBookingReferences());
            unassignBookingsParamsData.setBookingStatuses(commonService.getBookingStatusesIdAndNameResponses());
            unassignBookingsParamsData.setTours(commonService.getTourIdAndNameResponses());
            unassignBookingsParamsData.setPackages(commonService.getPacakgeIdAndNameResponses());
            unassignBookingsParamsData.setPackageSchedules(commonService.getPackageScheduleIdAndNameResponses());
            unassignBookingsParamsData.setAssignedUsers(commonService.getTourAssignUserIdAndNameResponses());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    unassignBookingsParamsData,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching unassign bookings params data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch unassign bookings params data from database");
        } finally {
            LOGGER.info("End fetching unassign bookings params data from repository");
        }
    }

    @Override
    public CommonResponse<List<BookingIdAndReferenceResponse>> getUnassignBookingList() {
        LOGGER.info("Start fetching unassign booking list from repository");
        try {
            List<BookingIdAndReferenceResponse> unassignBookingList = bookingRepository.getBookingIdAndReferences(BookingAssignStatus.ALL.name());

            if (unassignBookingList == null || unassignBookingList.isEmpty()) {
                return new CommonResponse<>(
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                        "No unassigned bookings found",
                        List.of(),
                        Instant.now());
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    unassignBookingList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching unassign booking list: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch unassign booking list from database");
        } finally {
            LOGGER.info("End fetching unassign booking list from repository");
        }
    }

    @Override
    public CommonResponse<List<BookingIdAndReferenceResponse>> getassignBookingList() {
        LOGGER.info("Start fetching assigned booking list from repository");
        try {
            List<BookingIdAndReferenceResponse> assignedBookingList =  bookingRepository.getBookingIdAndReferences(BookingAssignStatus.ALL.name());

            if (assignedBookingList == null || assignedBookingList.isEmpty()) {
                return new CommonResponse<>(
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                        "No assigned bookings found",
                        List.of(),
                        Instant.now());
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    assignedBookingList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching assigned booking list: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch assigned booking list from database");
        } finally {
            LOGGER.info("End fetching assigned booking list from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateUnassignBookingToAssign(AssignBookingRequest assignBookingRequest) {
        LOGGER.info("Start updating unassign booking to assign from repository");
        try {
            bookingValidationService.validateAssignBookingRequest(assignBookingRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            BookingsBasicDetails bookingsBasicDetails = getBookingBasicDetails(new CommonIdRequest(assignBookingRequest.getBookingId())).getData();

            bookingRepository.updateUnassignBookingToAssign(assignBookingRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKING_ASSIGNED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Booking Assigned")
                    .message("Booking '" + bookingsBasicDetails.getBookingReference() + "' has been assigned.")
                    .actionUrl(VIEW_BOOKING_DETAILS + "/" + bookingsBasicDetails.getBookingId())
                    .actionText("View Booking")
                    .icon("UserCheck")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "bookingId", bookingsBasicDetails.getBookingId(),
                            "bookingReference", bookingsBasicDetails.getBookingReference(),
                            "assignedTo", assignBookingRequest.getAssignUsername(),
                            "assignedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKING_ASSIGN.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            AssignBookingComparisonResult comparisonResult = compareAssignBookingUpdates(
                    assignBookingRequest,
                    bookingsBasicDetails
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKING_ASSIGNED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = bookingEmailHelperService.buildAssignBookingSuccessfullSubject(loggedUser, assignBookingRequest.getBookingId());
            String body = bookingEmailHelperService.buildAssignBookingSuccessfullBody(loggedUser, assignBookingRequest.getBookingId(), comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("update", assignBookingRequest.getBookingId()),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating unassign booking to assign: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update unassign booking to assign in database");
        } finally {
            LOGGER.info("End updating unassign booking to assign from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateUnassignBooking(UnassignBookingRequest unassignBookingRequest) {
        LOGGER.info("Start unassigning booking from repository");
        try {
            bookingValidationService.validateUnassignBookingRequest(unassignBookingRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            BookingsBasicDetails bookingsBasicDetails = getBookingBasicDetails(new CommonIdRequest(unassignBookingRequest.getBookingId())).getData();

            bookingRepository.updateUnassignBooking(unassignBookingRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKING_ASSIGN_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Booking Assignment Updated")
                    .message("The booking '" + bookingsBasicDetails.getBookingReference() + "' assignment has been updated.")
                    .actionUrl(VIEW_BOOKING_DETAILS + "/" + bookingsBasicDetails.getBookingId())
                    .actionText("View Booking")
                    .icon("UserCog")
                    .color("#F59E0B")
                    .metadata(Map.of(
                            "bookingId", bookingsBasicDetails.getBookingId(),
                            "bookingReference", bookingsBasicDetails.getBookingReference(),
                            "previousAssignedTo", bookingsBasicDetails.getAssignedEmployeeName(),
                            "newAssignedTo", unassignBookingRequest.getAssignUsername(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKING_ASSIGN_UPDATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            AssignBookingUpdateComparisonResult comparisonResult = compareUpdateAssignBookingUpdates(
                    unassignBookingRequest,
                    bookingsBasicDetails
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.BOOKING_ASSIGN_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = bookingEmailHelperService.buildAssignBookingUpdateSuccessfullSubject(loggedUser, unassignBookingRequest.getBookingId());
            String body = bookingEmailHelperService.buildAssignBookingUpdateSuccessfullBody(loggedUser, unassignBookingRequest.getBookingId(), comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("",unassignBookingRequest.getBookingId()),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while unassigning booking: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to unassign booking in database");
        } finally {
            LOGGER.info("End unassigning booking from repository");
        }
    }

    @Override
    public CommonResponse<BookingHisotryWithParamsResponse> getBookingHistoryByRequestParam(BookingHistoryDataRequest bookingHistoryDataRequest) {
        LOGGER.info("Start fetching booking history with params from repository");
        try {
            BookingHisotryWithParamsResponse bookingHistoryWithParams = new BookingHisotryWithParamsResponse();

            List<BookingHistoryBasicDetailsResponse> bookingHistoryBasicDetailsResponses = bookingRepository.getBookingHistoryBasicDetails(bookingHistoryDataRequest);
            Integer count = bookingRepository.getBookingHistoryBasicDetailsCount(bookingHistoryDataRequest);
            bookingHistoryWithParams.setBookingHistoryBasicDetailsResponses(bookingHistoryBasicDetailsResponses);
            bookingHistoryWithParams.setBookingHistoryCount(count);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingHistoryWithParams,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking history with params: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking history with params from database");
        } finally {
            LOGGER.info("End fetching booking history with params from repository");
        }
    }

    @Override
    public CommonResponse<BookingsHistoryRequestParamsResponse> getBookingsHistoryParamsData() {
        LOGGER.info("Start fetching booking history params data from repository");
        try {
            BookingsHistoryRequestParamsResponse bookingsHistoryParamsData = new BookingsHistoryRequestParamsResponse();

            bookingsHistoryParamsData.setBookingRefences(bookingRepository.getUnassignBookingReferences());
            bookingsHistoryParamsData.setBookingStatuses(commonService.getBookingStatusesIdAndNameResponses());
            bookingsHistoryParamsData.setTours(commonService.getTourIdAndNameResponses());
            bookingsHistoryParamsData.setPackages(commonService.getPacakgeIdAndNameResponses());
            bookingsHistoryParamsData.setAssignedEmployees(commonService.getTourAssignUserIdAndNameResponses());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingsHistoryParamsData,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking history params data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking history params data from database");
        } finally {
            LOGGER.info("End fetching booking history params data from repository");
        }
    }

    @Override
    public CommonResponse<BookingHistoryDetailsResponse> getBookingHistoryDetailsById(CommonIdRequest bookingId) {
        LOGGER.info("Start fetching booking history details by id from repository");
        try {
            BookingHistoryDetailsResponse bookingHistoryDetails = new BookingHistoryDetailsResponse();

            BookingsBasicDetails bookingsBasicDetails = getBookingBasicDetails(new CommonIdRequest(bookingId.getId())).getData();
            bookingHistoryDetails.setBookingsBasicDetails(bookingsBasicDetails);

            List<BookingHistoryDetailsResponse.BookingActivityHistory> bookingActivityHistories = bookingRepository.getBookingActivityHistory(bookingId.getId());
            bookingHistoryDetails.setBookingActivityHistories(bookingActivityHistories);

            List<BookingHistoryDetailsResponse.BookingStatusHistory> bookingStatusHistories = bookingRepository.getBookingStatusHistory(bookingId.getId());
            bookingHistoryDetails.setBookingStatusHistories(bookingStatusHistories);

            List<BookingHistoryDetailsResponse.BookingAssignmentHistory> bookingAssignmentHistories = bookingRepository.getBookingAssignmentHistory(bookingId.getId());
            bookingHistoryDetails.setBookingAssignmentHistories(bookingAssignmentHistories);

            List<BookingHistoryDetailsResponse.BookingPaymentHistory> bookingPaymentHistories = bookingRepository.getBookingPaymentHistory(bookingId.getId());
            bookingHistoryDetails.setBookingPaymentHistories(bookingPaymentHistories);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookingHistoryDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching booking history details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booking history details from database");
        } finally {
            LOGGER.info("End fetching booking history details from repository");
        }
    }

    private AssignBookingUpdateComparisonResult compareUpdateAssignBookingUpdates(
            UnassignBookingRequest unassignBookingRequest,
            BookingsBasicDetails bookingsBasicDetails) {

        AssignBookingUpdateComparisonResult.AssignBookingUpdateComparisonResultBuilder resultBuilder =
                AssignBookingUpdateComparisonResult.builder();

        List<AssignBookingUpdateComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        List<String> changes = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        boolean hasChanges = false;

        // Get current assignment details
        Long oldEmployeeId = bookingsBasicDetails.getAssignedEmployeeId();
        String oldEmployeeName = bookingsBasicDetails.getAssignedEmployeeName();
        String oldMessage = bookingsBasicDetails.getAssignMessage();

        // Get new assignment details from request
        Long newEmployeeId = unassignBookingRequest.getAssignTo();
        String newEmployeeName = unassignBookingRequest.getAssignUsername();
        String newMessage = unassignBookingRequest.getAssignMessage();

        // Check if this is an unassignment (assignTo is null or 0)
        boolean isUnassignment = (newEmployeeId == null || newEmployeeId == 0L);

        // Compare assigned employee
        if (!Objects.equals(oldEmployeeId, newEmployeeId)) {
            String oldName = oldEmployeeName != null ? oldEmployeeName : "Unassigned";
            String newName = newEmployeeName != null ? newEmployeeName : "Unassigned";

            if (oldEmployeeId == null && newEmployeeId != null && !isUnassignment) {
                // New assignment
                changes.add(String.format("Booking assigned to: %s (ID: %d)", newName, newEmployeeId));
                warnings.add("Info: Booking has been assigned to an employee");
            } else if (oldEmployeeId != null && (newEmployeeId == null || isUnassignment)) {
                // Unassignment
                changes.add(String.format("Booking unassigned from: %s (ID: %d)", oldName, oldEmployeeId));
                warnings.add("Warning: Booking is being unassigned from employee");
            } else if (oldEmployeeId != null && newEmployeeId != null && !isUnassignment) {
                // Reassignment
                changes.add(String.format("Assignment changed from '%s' (ID: %d) to '%s' (ID: %d)",
                        oldName, oldEmployeeId, newName, newEmployeeId));
                warnings.add("Note: Booking is being reassigned to another employee");
            }

            fieldChanges.add(new AssignBookingUpdateComparisonResult.FieldChange(
                    "assignedEmployee",
                    oldEmployeeId,
                    newEmployeeId,
                    "Assigned Employee"));
            hasChanges = true;
        }

        // Compare assign message
        if (!Objects.equals(oldMessage, newMessage)) {
            String oldMsg = oldMessage != null ? oldMessage : "null";
            String newMsg = newMessage != null ? newMessage : "null";
            changes.add(String.format("Assignment message changed from '%s' to '%s'", oldMsg, newMsg));

            fieldChanges.add(new AssignBookingUpdateComparisonResult.FieldChange(
                    "assignMessage",
                    oldMessage,
                    newMessage,
                    "Assignment Message"));
            hasChanges = true;
        }

        // Additional validations and warnings
        if (isUnassignment) {
            // Unassignment validations
            if (oldEmployeeId == null) {
                warnings.add("Warning: Booking is already unassigned. No action needed.");
            }
            if (newMessage != null && !newMessage.trim().isEmpty()) {
                warnings.add("Note: Unassignment message provided: " + newMessage);
            }
        } else {
            // Assignment validations
            if (newEmployeeId != null && (newEmployeeName == null || newEmployeeName.trim().isEmpty())) {
                warnings.add("Warning: Employee name is missing for assignment");
            }

            if (newEmployeeId != null && newEmployeeId.equals(oldEmployeeId)) {
                warnings.add("Note: Assigning to the same employee already assigned");
            }
        }

        if (newMessage != null && newMessage.length() > 500) {
            warnings.add("Warning: Assignment message is very long (>500 characters)");
        }

        // Check if booking ID matches
        if (unassignBookingRequest.getBookingId() != null &&
                bookingsBasicDetails.getBookingId() != null &&
                !unassignBookingRequest.getBookingId().equals(bookingsBasicDetails.getBookingId())) {
            warnings.add("Warning: Booking ID mismatch between request and details");
        }

        // Check if no changes were made
        if (!hasChanges) {
            changes.add("No changes detected in booking assignment");
            if (oldEmployeeId != null) {
                changes.add("Booking is currently assigned to: " + oldEmployeeName);
            } else {
                changes.add("Booking is currently unassigned");
            }
        }

        // Build the result
        return resultBuilder
                .fieldChanges(fieldChanges)
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .oldAssignedEmployeeId(oldEmployeeId)
                .oldAssignedEmployeeName(oldEmployeeName)
                .newAssignedEmployeeId(isUnassignment ? null : newEmployeeId)
                .newAssignedEmployeeName(isUnassignment ? null : newEmployeeName)
                .oldAssignMessage(oldMessage)
                .newAssignMessage(newMessage)
                .changedBy("System") // You can pass logged user here if needed
                .changeTimestamp(new Date().toString())
                .build();
    }

    private AssignBookingComparisonResult compareAssignBookingUpdates(
            AssignBookingRequest assignBookingRequest,
            BookingsBasicDetails bookingsBasicDetails) {

        AssignBookingComparisonResult.AssignBookingComparisonResultBuilder resultBuilder =
                AssignBookingComparisonResult.builder();

        List<AssignBookingComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        List<String> changes = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        boolean hasChanges = false;

        // Get current assignment details
        Long oldEmployeeId = bookingsBasicDetails.getAssignedEmployeeId();
        String oldEmployeeName = bookingsBasicDetails.getAssignedEmployeeName();
        String oldMessage = bookingsBasicDetails.getAssignMessage();

        // Get new assignment details
        Long newEmployeeId = assignBookingRequest.getAssignTo();
        String newEmployeeName = assignBookingRequest.getAssignUsername();
        String newMessage = assignBookingRequest.getAssignMessage();

        // Compare assigned employee
        if (!Objects.equals(oldEmployeeId, newEmployeeId)) {
            String oldName = oldEmployeeName != null ? oldEmployeeName : "Unassigned";
            String newName = newEmployeeName != null ? newEmployeeName : "Unassigned";

            if (oldEmployeeId == null && newEmployeeId != null) {
                changes.add(String.format("Booking assigned to: %s (ID: %d)", newName, newEmployeeId));
            } else if (oldEmployeeId != null && newEmployeeId == null) {
                changes.add(String.format("Booking unassigned from: %s (ID: %d)", oldName, oldEmployeeId));
            } else {
                changes.add(String.format("Assignment changed from '%s' (ID: %d) to '%s' (ID: %d)",
                        oldName, oldEmployeeId, newName, newEmployeeId));
            }

            fieldChanges.add(new AssignBookingComparisonResult.FieldChange(
                    "assignedEmployee",
                    oldEmployeeId,
                    newEmployeeId,
                    "Assigned Employee"));
            hasChanges = true;

            // Add warnings for assignment changes
            if (newEmployeeId != null && oldEmployeeId == null) {
                warnings.add("Info: Booking has been assigned to an employee");
            } else if (newEmployeeId == null && oldEmployeeId != null) {
                warnings.add("Warning: Booking is being unassigned from employee");
            } else if (newEmployeeId != null && oldEmployeeId != null) {
                warnings.add("Note: Booking is being reassigned to another employee");
            }
        }

        // Compare assign message
        if (!Objects.equals(oldMessage, newMessage)) {
            String oldMsg = oldMessage != null ? oldMessage : "null";
            String newMsg = newMessage != null ? newMessage : "null";
            changes.add(String.format("Assignment message changed from '%s' to '%s'", oldMsg, newMsg));

            fieldChanges.add(new AssignBookingComparisonResult.FieldChange(
                    "assignMessage",
                    oldMessage,
                    newMessage,
                    "Assignment Message"));
            hasChanges = true;
        }

        // Additional validations
        if (newEmployeeId == null && (newMessage == null || newMessage.trim().isEmpty())) {
            warnings.add("Warning: Booking is being unassigned without any message");
        }

        if (newEmployeeId != null && (newEmployeeName == null || newEmployeeName.trim().isEmpty())) {
            warnings.add("Warning: Employee name is missing for assignment");
        }

        if (newMessage != null && newMessage.length() > 500) {
            warnings.add("Warning: Assignment message is very long (>500 characters)");
        }

        // Check if no changes were made
        if (!hasChanges) {
            changes.add("No changes detected in booking assignment");
        }

        // Build the result
        return resultBuilder
                .fieldChanges(fieldChanges)
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .oldAssignedEmployeeId(oldEmployeeId)
                .oldAssignedEmployeeName(oldEmployeeName)
                .newAssignedEmployeeId(newEmployeeId)
                .newAssignedEmployeeName(newEmployeeName)
                .oldAssignMessage(oldMessage)
                .newAssignMessage(newMessage)
                .changedBy("System") // You can pass logged user here if needed
                .changeTimestamp(new Date().toString())
                .build();
    }

    private BookingComparisonResult compareBookingsUpdates(
            UpdateBookingRequest updateBookingRequest,
            BookingAllDetailsResponse previousBookingDetails) {

        BookingComparisonResult.BookingComparisonResultBuilder resultBuilder =
                BookingComparisonResult.builder();

        List<BookingComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        List<String> changes = new ArrayList<>();
        boolean hasChanges = false;
        List<String> warnings = new ArrayList<>();

        boolean isFinancialCalculationValid = true;
        BigDecimal calculatedFinalAmount = null;
        BigDecimal differenceAmount = null;
        boolean isDateRangeValid = true;
        Integer daysBetweenTravelDates = null;

        BookingAllDetailsResponse.BookingInformation previousBookingInfo =
                previousBookingDetails.getBookingInformation();

        // Compare basic booking information
        if (previousBookingInfo != null) {
            // Compare bookingDate
            if (updateBookingRequest.getBookingDate() != null &&
                    previousBookingInfo.getBookingDate() != null &&
                    !updateBookingRequest.getBookingDate().equals(previousBookingInfo.getBookingDate())) {
                changes.add(String.format("Booking Date changed from %s to %s",
                        previousBookingInfo.getBookingDate(),
                        updateBookingRequest.getBookingDate()));
                fieldChanges.add(new BookingComparisonResult.FieldChange(
                        "bookingDate",
                        previousBookingInfo.getBookingDate(),
                        updateBookingRequest.getBookingDate(),
                        "Booking Date"));
                hasChanges = true;
            }

            // Compare travelStartDate
            if (updateBookingRequest.getTravelStartDate() != null &&
                    previousBookingInfo.getTravelStartDate() != null &&
                    !updateBookingRequest.getTravelStartDate().equals(previousBookingInfo.getTravelStartDate())) {
                changes.add(String.format("Travel Start Date changed from %s to %s",
                        previousBookingInfo.getTravelStartDate(),
                        updateBookingRequest.getTravelStartDate()));
                fieldChanges.add(new BookingComparisonResult.FieldChange(
                        "travelStartDate",
                        previousBookingInfo.getTravelStartDate(),
                        updateBookingRequest.getTravelStartDate(),
                        "Travel Start Date"));
                hasChanges = true;
            }

            // Compare travelEndDate
            if (updateBookingRequest.getTravelEndDate() != null &&
                    previousBookingInfo.getTravelEndDate() != null &&
                    !updateBookingRequest.getTravelEndDate().equals(previousBookingInfo.getTravelEndDate())) {
                changes.add(String.format("Travel End Date changed from %s to %s",
                        previousBookingInfo.getTravelEndDate(),
                        updateBookingRequest.getTravelEndDate()));
                fieldChanges.add(new BookingComparisonResult.FieldChange(
                        "travelEndDate",
                        previousBookingInfo.getTravelEndDate(),
                        updateBookingRequest.getTravelEndDate(),
                        "Travel End Date"));
                hasChanges = true;
            }

            // Validate date range
            if (updateBookingRequest.getTravelStartDate() != null &&
                    updateBookingRequest.getTravelEndDate() != null) {

                LocalDate startDate = updateBookingRequest.getTravelStartDate();
                LocalDate endDate = updateBookingRequest.getTravelEndDate();

                if (endDate.isBefore(startDate)) {
                    isDateRangeValid = false;
                    warnings.add("Error: Travel end date is before travel start date!");
                } else {
                    daysBetweenTravelDates = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
                    if (daysBetweenTravelDates > 365) {
                        warnings.add(String.format("Warning: Travel spans more than a year (%d days)", daysBetweenTravelDates));
                    }
                }
            }

            // Compare totalPersons
            if (updateBookingRequest.getTotalPersons() != null &&
                    previousBookingInfo.getTotalPersons() != null &&
                    !updateBookingRequest.getTotalPersons().equals(previousBookingInfo.getTotalPersons())) {
                changes.add(String.format("Total Persons changed from %d to %d",
                        previousBookingInfo.getTotalPersons(),
                        updateBookingRequest.getTotalPersons()));
                fieldChanges.add(new BookingComparisonResult.FieldChange(
                        "totalPersons",
                        previousBookingInfo.getTotalPersons(),
                        updateBookingRequest.getTotalPersons(),
                        "Total Persons"));
                hasChanges = true;
            }

            // Compare financial fields
            compareFinancialFields(updateBookingRequest, previousBookingInfo, changes, fieldChanges, hasChanges, warnings);
        }

        // Compare customerId
        if (updateBookingRequest.getCustomerId() != null &&
                previousBookingDetails.getCustomerInformation() != null &&
                previousBookingDetails.getCustomerInformation().getUserId() != null &&
                !updateBookingRequest.getCustomerId().equals(previousBookingDetails.getCustomerInformation().getUserId())) {
            changes.add(String.format("Customer changed from ID %d to %d",
                    previousBookingDetails.getCustomerInformation().getUserId(),
                    updateBookingRequest.getCustomerId()));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "customerId",
                    previousBookingDetails.getCustomerInformation().getUserId(),
                    updateBookingRequest.getCustomerId(),
                    "Customer"));
            hasChanges = true;
            warnings.add("Warning: Changing customer may affect booking history and communications");
        }

        // Compare tourId
        if (updateBookingRequest.getTourId() != null &&
                previousBookingDetails.getTourInformation() != null &&
                previousBookingDetails.getTourInformation().getTourId() != null &&
                !updateBookingRequest.getTourId().equals(previousBookingDetails.getTourInformation().getTourId())) {
            changes.add(String.format("Tour changed from ID %d to %d",
                    previousBookingDetails.getTourInformation().getTourId(),
                    updateBookingRequest.getTourId()));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "tourId",
                    previousBookingDetails.getTourInformation().getTourId(),
                    updateBookingRequest.getTourId(),
                    "Tour"));
            hasChanges = true;
            warnings.add("Warning: Changing tour may affect pricing and availability");
        }

        // Compare packageId
        if (updateBookingRequest.getPackageId() != null &&
                previousBookingDetails.getPackageInformation() != null &&
                previousBookingDetails.getPackageInformation().getPackageId() != null &&
                !updateBookingRequest.getPackageId().equals(previousBookingDetails.getPackageInformation().getPackageId())) {
            changes.add(String.format("Package changed from ID %d to %d",
                    previousBookingDetails.getPackageInformation().getPackageId(),
                    updateBookingRequest.getPackageId()));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "packageId",
                    previousBookingDetails.getPackageInformation().getPackageId(),
                    updateBookingRequest.getPackageId(),
                    "Package"));
            hasChanges = true;
            warnings.add("Warning: Changing package may affect pricing and inclusions");
        }

        // Compare packageScheduleId
        if (!Objects.equals(updateBookingRequest.getPackageScheduleId(),
                previousBookingDetails.getPackageInformation() != null ?
                        previousBookingDetails.getPackageInformation().getPackageId() : null)) {
            // Note: Package schedule ID might not be directly in the response
            // Adjust based on your actual data structure
        }

        // Compare bookingStatusId
        Long oldStatusId = previousBookingDetails.getBookingStatusInformation() != null ?
                previousBookingDetails.getBookingStatusInformation().getBookingStatusId() : null;
        Long newStatusId = updateBookingRequest.getBookingStatusId();

        if (oldStatusId != null && newStatusId != null && !oldStatusId.equals(newStatusId)) {
            String oldStatusName = previousBookingDetails.getBookingStatusInformation() != null ?
                    previousBookingDetails.getBookingStatusInformation().getBookingStatusName() : "Unknown";
            String newStatusName = getStatusNameById(newStatusId);
            changes.add(String.format("Booking Status changed from '%s' (ID: %d) to '%s' (ID: %d)",
                    oldStatusName, oldStatusId, newStatusName, newStatusId));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "bookingStatusId",
                    oldStatusId,
                    newStatusId,
                    "Booking Status"));
            hasChanges = true;

            // Status change warnings
            if (newStatusId == 3L) { // Assuming 3 = CANCELLED
                warnings.add("Warning: Booking is being cancelled!");
            } else if (newStatusId == 4L) { // Assuming 4 = COMPLETED
                warnings.add("Note: Booking marked as completed");
            } else if (newStatusId == 2L && oldStatusId == 1L) { // 1 = CONFIRMED, 2 = PENDING
                warnings.add("Warning: Booking status changed from CONFIRMED to PENDING");
            }
        }

        // Compare specialRequirements
        if (!Objects.equals(updateBookingRequest.getSpecialRequirements(),
                previousBookingInfo != null ? previousBookingInfo.getSpecialRequirements() : null)) {
            String oldReq = previousBookingInfo != null && previousBookingInfo.getSpecialRequirements() != null ?
                    previousBookingInfo.getSpecialRequirements() : "null";
            String newReq = updateBookingRequest.getSpecialRequirements() != null ?
                    updateBookingRequest.getSpecialRequirements() : "null";
            changes.add(String.format("Special Requirements changed from '%s' to '%s'", oldReq, newReq));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "specialRequirements",
                    previousBookingInfo != null ? previousBookingInfo.getSpecialRequirements() : null,
                    updateBookingRequest.getSpecialRequirements(),
                    "Special Requirements"));
            hasChanges = true;
        }

        // Compare dietaryRestrictions
        if (!Objects.equals(updateBookingRequest.getDietaryRestrictions(),
                previousBookingInfo != null ? previousBookingInfo.getDietaryRestrictions() : null)) {
            String oldDiet = previousBookingInfo != null && previousBookingInfo.getDietaryRestrictions() != null ?
                    previousBookingInfo.getDietaryRestrictions() : "null";
            String newDiet = updateBookingRequest.getDietaryRestrictions() != null ?
                    updateBookingRequest.getDietaryRestrictions() : "null";
            changes.add(String.format("Dietary Restrictions changed from '%s' to '%s'", oldDiet, newDiet));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "dietaryRestrictions",
                    previousBookingInfo != null ? previousBookingInfo.getDietaryRestrictions() : null,
                    updateBookingRequest.getDietaryRestrictions(),
                    "Dietary Restrictions"));
            hasChanges = true;
        }

        // Compare insuranceRequired
        if (updateBookingRequest.getInsuranceRequired() != null &&
                previousBookingInfo != null &&
                previousBookingInfo.getInsuranceRequired() != null &&
                !updateBookingRequest.getInsuranceRequired().equals(previousBookingInfo.getInsuranceRequired())) {
            changes.add(String.format("Insurance Required changed from %s to %s",
                    previousBookingInfo.getInsuranceRequired() ? "Yes" : "No",
                    updateBookingRequest.getInsuranceRequired() ? "Yes" : "No"));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "insuranceRequired",
                    previousBookingInfo.getInsuranceRequired(),
                    updateBookingRequest.getInsuranceRequired(),
                    "Insurance Required"));
            hasChanges = true;
        }

        // Compare assignTo
        if (!Objects.equals(updateBookingRequest.getAssignTo(),
                previousBookingDetails.getAssignmentInformation() != null ?
                        previousBookingDetails.getAssignmentInformation().getEmployeeId() : null)) {
            Long oldAssignTo = previousBookingDetails.getAssignmentInformation() != null ?
                    previousBookingDetails.getAssignmentInformation().getEmployeeId() : null;
            Long newAssignTo = updateBookingRequest.getAssignTo();
            String oldAssignName = previousBookingDetails.getAssignmentInformation() != null ?
                    previousBookingDetails.getAssignmentInformation().getEmployeeName() : "Unassigned";
            changes.add(String.format("Assignment changed from '%s' (ID: %d) to ID: %d",
                    oldAssignName, oldAssignTo != null ? oldAssignTo : 0, newAssignTo != null ? newAssignTo : 0));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "assignTo",
                    oldAssignTo,
                    newAssignTo,
                    "Assigned To"));
            hasChanges = true;
        }

        // Compare assignMessage
        if (!Objects.equals(updateBookingRequest.getAssignMessage(),
                previousBookingDetails.getAssignmentInformation() != null ?
                        previousBookingDetails.getAssignmentInformation().getAssignMessage() : null)) {
            String oldMsg = previousBookingDetails.getAssignmentInformation() != null &&
                    previousBookingDetails.getAssignmentInformation().getAssignMessage() != null ?
                    previousBookingDetails.getAssignmentInformation().getAssignMessage() : "null";
            String newMsg = updateBookingRequest.getAssignMessage() != null ?
                    updateBookingRequest.getAssignMessage() : "null";
            changes.add(String.format("Assignment Message changed from '%s' to '%s'", oldMsg, newMsg));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "assignMessage",
                    previousBookingDetails.getAssignmentInformation() != null ?
                            previousBookingDetails.getAssignmentInformation().getAssignMessage() : null,
                    updateBookingRequest.getAssignMessage(),
                    "Assignment Message"));
            hasChanges = true;
        }

        // Handle Participants
        handleParticipants(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Accommodations
        handleAccommodations(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Transportations
        handleTransportations(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Activities
        handleActivities(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Documents
        handleDocuments(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Insurance
        handleInsurance(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Itineraries
        handleItineraries(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Notes
        handleNotes(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Price Breakdowns
        handlePriceBreakDowns(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Handle Invoice
        handleInvoice(updateBookingRequest, previousBookingDetails, changes, fieldChanges, warnings, hasChanges);

        // Check if any changes were made
        if (!hasChanges) {
            changes.add("No changes detected in booking");
        }

        // Get status names
        String oldStatusName = previousBookingDetails.getBookingStatusInformation() != null ?
                previousBookingDetails.getBookingStatusInformation().getBookingStatusName() : null;
        String newStatusName = getStatusNameById(updateBookingRequest.getBookingStatusId());

        // Build the result
        return resultBuilder
                .fieldChanges(fieldChanges)
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .isFinancialCalculationValid(isFinancialCalculationValid)
                .calculatedFinalAmount(calculatedFinalAmount)
                .differenceAmount(differenceAmount)
                .isDateRangeValid(isDateRangeValid)
                .daysBetweenTravelDates(daysBetweenTravelDates)
                .oldBookingStatusId(oldStatusId)
                .newBookingStatusId(newStatusId)
                .oldBookingStatusName(oldStatusName)
                .newBookingStatusName(newStatusName)
                .changedBy("System")
                .changedByUserId(null)
                .changeTimestamp(new Date().toString())
                .build();
    }

// Helper methods for handling collections

    private void compareFinancialFields(UpdateBookingRequest request,
                                        BookingAllDetailsResponse.BookingInformation previousInfo,
                                        List<String> changes,
                                        List<BookingComparisonResult.FieldChange> fieldChanges,
                                        boolean hasChanges,
                                        List<String> warnings) {

        // Compare totalAmount
        if (request.getTotalAmount() != null && previousInfo.getTotalAmount() != null &&
                request.getTotalAmount().compareTo(previousInfo.getTotalAmount()) != 0) {
            changes.add(String.format("Total Amount changed from %.2f to %.2f",
                    previousInfo.getTotalAmount(), request.getTotalAmount()));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "totalAmount", previousInfo.getTotalAmount(), request.getTotalAmount(), "Total Amount"));
            hasChanges = true;
        }

        // Compare discountAmount
        if (request.getDiscountAmount() != null && previousInfo.getDiscountAmount() != null &&
                request.getDiscountAmount().compareTo(previousInfo.getDiscountAmount()) != 0) {
            changes.add(String.format("Discount Amount changed from %.2f to %.2f",
                    previousInfo.getDiscountAmount(), request.getDiscountAmount()));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "discountAmount", previousInfo.getDiscountAmount(), request.getDiscountAmount(), "Discount Amount"));
            hasChanges = true;
        }

        // Compare taxAmount
        if (request.getTaxAmount() != null && previousInfo.getTaxAmount() != null &&
                request.getTaxAmount().compareTo(previousInfo.getTaxAmount()) != 0) {
            changes.add(String.format("Tax Amount changed from %.2f to %.2f",
                    previousInfo.getTaxAmount(), request.getTaxAmount()));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "taxAmount", previousInfo.getTaxAmount(), request.getTaxAmount(), "Tax Amount"));
            hasChanges = true;
        }

        // Compare insuranceAmount
        if (request.getInsuranceAmount() != null && previousInfo.getInsuranceAmount() != null &&
                request.getInsuranceAmount().compareTo(previousInfo.getInsuranceAmount()) != 0) {
            changes.add(String.format("Insurance Amount changed from %.2f to %.2f",
                    previousInfo.getInsuranceAmount(), request.getInsuranceAmount()));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "insuranceAmount", previousInfo.getInsuranceAmount(), request.getInsuranceAmount(), "Insurance Amount"));
            hasChanges = true;
        }

        // Compare finalAmount
        if (request.getFinalAmount() != null && previousInfo.getFinalAmount() != null &&
                request.getFinalAmount().compareTo(previousInfo.getFinalAmount()) != 0) {
            changes.add(String.format("Final Amount changed from %.2f to %.2f",
                    previousInfo.getFinalAmount(), request.getFinalAmount()));
            fieldChanges.add(new BookingComparisonResult.FieldChange(
                    "finalAmount", previousInfo.getFinalAmount(), request.getFinalAmount(), "Final Amount"));
            hasChanges = true;
        }

        // Validate financial calculation
        if (request.getTotalAmount() != null && request.getDiscountAmount() != null &&
                request.getTaxAmount() != null && request.getInsuranceAmount() != null &&
                request.getFinalAmount() != null) {
            BigDecimal calculated = request.getTotalAmount()
                    .subtract(request.getDiscountAmount())
                    .add(request.getTaxAmount())
                    .add(request.getInsuranceAmount());

            BigDecimal diff = request.getFinalAmount().subtract(calculated).abs();
            if (diff.compareTo(new BigDecimal("0.01")) > 0) {
                warnings.add(String.format("Warning: Financial calculation mismatch! Calculated: %.2f, Provided: %.2f (Diff: %.2f)",
                        calculated, request.getFinalAmount(), diff));
            }
        }
    }

    private void handleParticipants(UpdateBookingRequest request,
                                    BookingAllDetailsResponse previousDetails,
                                    List<String> changes,
                                    List<BookingComparisonResult.FieldChange> fieldChanges,
                                    List<String> warnings,
                                    boolean hasChanges) {

        // Add participants
        if (request.getAddParticipants() != null && !request.getAddParticipants().isEmpty()) {
            for (InsertBookingRequest.Participant participant : request.getAddParticipants()) {
                BookingComparisonResult.ParticipantChange change =
                        BookingComparisonResult.ParticipantChange.builder()
                                .firstName(participant.getFirstName())
                                .lastName(participant.getLastName())
                                .dateOfBirth(participant.getDateOfBirth())
                                .email(participant.getEmail())
                                .mobileNumber(participant.getMobileNumber())
                                .passportNumber(participant.getPassportNumber())
                                .build();
                // Add to result
                changes.add(String.format("Participant to add: %s %s",
                        participant.getFirstName(), participant.getLastName()));
            }
        }

        // Remove participants
        if (request.getRemoveParticipants() != null && !request.getRemoveParticipants().isEmpty()) {
            changes.add(String.format("Participants to remove IDs: %s", request.getRemoveParticipants()));
        }

        // Update participants
        if (request.getUpdateParticipants() != null && !request.getUpdateParticipants().isEmpty()) {
            for (UpdateBookingRequest.UpdateParticipant update : request.getUpdateParticipants()) {
                // Find existing participant
                if (previousDetails.getParticipants() != null) {
                    previousDetails.getParticipants().stream()
                            .filter(p -> p.getParticipantId().equals(update.getParticipantId()))
                            .findFirst()
                            .ifPresent(existing -> {
                                if (!Objects.equals(existing.getFirstName(), update.getFirstName()) ||
                                        !Objects.equals(existing.getLastName(), update.getLastName())) {
                                    changes.add(String.format("Participant ID %d updated: %s %s -> %s %s",
                                            update.getParticipantId(),
                                            existing.getFirstName(), existing.getLastName(),
                                            update.getFirstName(), update.getLastName()));
                                }
                            });
                }
            }
        }
    }

    private void handleAccommodations(UpdateBookingRequest request,
                                      BookingAllDetailsResponse previousDetails,
                                      List<String> changes,
                                      List<BookingComparisonResult.FieldChange> fieldChanges,
                                      List<String> warnings,
                                      boolean hasChanges) {

        // Add accommodations
        if (request.getAddAccommodations() != null && !request.getAddAccommodations().isEmpty()) {
            for (InsertBookingRequest.Accommodation accommodation : request.getAddAccommodations()) {
                changes.add(String.format("Accommodation to add: %s (%s - %s)",
                        accommodation.getHotelId(), accommodation.getCheckInDate(), accommodation.getCheckOutDate()));
            }
        }

        // Remove accommodations
        if (request.getRemoveAccommodations() != null && !request.getRemoveAccommodations().isEmpty()) {
            changes.add(String.format("Accommodations to remove IDs: %s", request.getRemoveAccommodations()));
        }
    }

    private void handleTransportations(UpdateBookingRequest request,
                                       BookingAllDetailsResponse previousDetails,
                                       List<String> changes,
                                       List<BookingComparisonResult.FieldChange> fieldChanges,
                                       List<String> warnings,
                                       boolean hasChanges) {

        // Add transportations
        if (request.getAddTransportations() != null && !request.getAddTransportations().isEmpty()) {
            for (InsertBookingRequest.Transportation transportation : request.getAddTransportations()) {
                changes.add(String.format("Transportation to add: %s - %s (%s -> %s)",
                        transportation.getTransportType(), transportation.getCarrierName(),
                        transportation.getDepartureLocation(), transportation.getArrivalLocation()));
            }
        }

        // Remove transportations
        if (request.getRemoveTransportations() != null && !request.getRemoveTransportations().isEmpty()) {
            changes.add(String.format("Transportations to remove IDs: %s", request.getRemoveTransportations()));
        }
    }

    private void handleActivities(UpdateBookingRequest request,
                                  BookingAllDetailsResponse previousDetails,
                                  List<String> changes,
                                  List<BookingComparisonResult.FieldChange> fieldChanges,
                                  List<String> warnings,
                                  boolean hasChanges) {

        // Add activities
        if (request.getAddActivities() != null && !request.getAddActivities().isEmpty()) {
            for (InsertBookingRequest.Activity activity : request.getAddActivities()) {
                changes.add(String.format("Activity to add: ID %d on %s",
                        activity.getActivityId(), activity.getActivityDate()));
            }
        }

        // Remove activities
        if (request.getRemoveActivities() != null && !request.getRemoveActivities().isEmpty()) {
            changes.add(String.format("Activities to remove IDs: %s", request.getRemoveActivities()));
        }
    }

    private void handleDocuments(UpdateBookingRequest request,
                                 BookingAllDetailsResponse previousDetails,
                                 List<String> changes,
                                 List<BookingComparisonResult.FieldChange> fieldChanges,
                                 List<String> warnings,
                                 boolean hasChanges) {

        // Add documents
        if (request.getAddDocuments() != null && !request.getAddDocuments().isEmpty()) {
            for (InsertBookingRequest.BookingDocuments document : request.getAddDocuments()) {
                changes.add(String.format("Document to add: %s (%s)",
                        document.getDocumentName(), document.getDocumentType()));
            }
        }

        // Remove documents
        if (request.getRemoveDocuments() != null && !request.getRemoveDocuments().isEmpty()) {
            changes.add(String.format("Documents to remove IDs: %s", request.getRemoveDocuments()));
        }
    }

    private void handleInsurance(UpdateBookingRequest request,
                                 BookingAllDetailsResponse previousDetails,
                                 List<String> changes,
                                 List<BookingComparisonResult.FieldChange> fieldChanges,
                                 List<String> warnings,
                                 boolean hasChanges) {

        // Add insurance
        if (request.getAddBookingInsurance() != null) {
            InsertBookingRequest.BookingInsurance insurance = request.getAddBookingInsurance();
            changes.add(String.format("Insurance to add: %s (Policy: %s)",
                    insurance.getInsuranceProvider(), insurance.getPolicyNumber()));
        }

        // Remove insurance
        if (request.getRemoveBookingInsurance() != null) {
            changes.add(String.format("Insurance to remove ID: %d", request.getRemoveBookingInsurance()));
        }
    }

    private void handleItineraries(UpdateBookingRequest request,
                                   BookingAllDetailsResponse previousDetails,
                                   List<String> changes,
                                   List<BookingComparisonResult.FieldChange> fieldChanges,
                                   List<String> warnings,
                                   boolean hasChanges) {

        // Add itineraries
        if (request.getAddBookingItineraries() != null && !request.getAddBookingItineraries().isEmpty()) {
            for (InsertBookingRequest.BookingItinerary itinerary : request.getAddBookingItineraries()) {
                changes.add(String.format("Itinerary to add: Day %d - %s",
                        itinerary.getDayNumber(), itinerary.getTitle()));
            }
        }

        // Remove itineraries
        if (request.getRemoveBookingItineraries() != null && !request.getRemoveBookingItineraries().isEmpty()) {
            changes.add(String.format("Itineraries to remove IDs: %s", request.getRemoveBookingItineraries()));
        }
    }

    private void handleNotes(UpdateBookingRequest request,
                             BookingAllDetailsResponse previousDetails,
                             List<String> changes,
                             List<BookingComparisonResult.FieldChange> fieldChanges,
                             List<String> warnings,
                             boolean hasChanges) {

        // Add notes
        if (request.getAddBookingNotes() != null && !request.getAddBookingNotes().isEmpty()) {
            for (InsertBookingRequest.BookingNote note : request.getAddBookingNotes()) {
                changes.add(String.format("Note to add: %s - %s",
                        note.getNoteType(), note.getNoteText().substring(0, Math.min(50, note.getNoteText().length()))));
            }
        }

        // Remove notes
        if (request.getRemoveBookingNotes() != null && !request.getRemoveBookingNotes().isEmpty()) {
            changes.add(String.format("Notes to remove IDs: %s", request.getRemoveBookingNotes()));
        }
    }

    private void handlePriceBreakDowns(UpdateBookingRequest request,
                                       BookingAllDetailsResponse previousDetails,
                                       List<String> changes,
                                       List<BookingComparisonResult.FieldChange> fieldChanges,
                                       List<String> warnings,
                                       boolean hasChanges) {

        // Add price breakdowns
        if (request.getAddPriceBreakDowns() != null && !request.getAddPriceBreakDowns().isEmpty()) {
            for (InsertBookingRequest.BookingPriceBreakDown pb : request.getAddPriceBreakDowns()) {
                changes.add(String.format("Price Breakdown to add: %s - %.2f",
                        pb.getItemName(), pb.getTotalPrice()));
            }
        }

        // Remove price breakdowns
        if (request.getRemovePriceBreakDowns() != null && !request.getRemovePriceBreakDowns().isEmpty()) {
            changes.add(String.format("Price Breakdowns to remove IDs: %s", request.getRemovePriceBreakDowns()));
        }
    }

    private void handleInvoice(UpdateBookingRequest request,
                               BookingAllDetailsResponse previousDetails,
                               List<String> changes,
                               List<BookingComparisonResult.FieldChange> fieldChanges,
                               List<String> warnings,
                               boolean hasChanges) {

        // Add invoice
        if (request.getAddBookingInvoice() != null) {
            InsertBookingRequest.BookingInvoice invoice = request.getAddBookingInvoice();
            changes.add(String.format("Invoice to add: Total %.2f, Due %s",
                    invoice.getTotalAmount(), invoice.getDueDate()));
        }

        // Remove invoice
        if (request.getRemoveBookingInvoice() != null) {
            changes.add(String.format("Invoice to remove ID: %d", request.getRemoveBookingInvoice()));
        }
    }

    private String getStatusNameById(Long statusId) {
        if (statusId == null) return "Unknown";
        switch (statusId.intValue()) {
            case 1:
                return "PENDING";
            case 2:
                return "CONFIRMED";
            case 3:
                return "CANCELLED";
            case 4:
                return "COMPLETED";
            case 5:
                return "IN PROGRESS";
            default:
                return "UNKNOWN";
        }
    }

    private BookingsStatusesComparisonResult compareBookingsStatusesUpdates(
            UpdateBookingsStatusesRequest updateBookingsStatusesRequest,
            BookingStatusBasicDetailsResponse previousBookingStatusDetails) {

        BookingsStatusesComparisonResult.BookingsStatusesComparisonResultBuilder resultBuilder =
                BookingsStatusesComparisonResult.builder();

        List<BookingsStatusesComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        List<String> changes = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        boolean hasChanges = false;

        // Compare statusName
        if (updateBookingsStatusesRequest.getStatusName() != null &&
                previousBookingStatusDetails.getStatusName() != null &&
                !updateBookingsStatusesRequest.getStatusName().equals(previousBookingStatusDetails.getStatusName())) {
            changes.add(String.format("Status Name changed from '%s' to '%s'",
                    previousBookingStatusDetails.getStatusName(),
                    updateBookingsStatusesRequest.getStatusName()));
            fieldChanges.add(new BookingsStatusesComparisonResult.FieldChange(
                    "statusName",
                    previousBookingStatusDetails.getStatusName(),
                    updateBookingsStatusesRequest.getStatusName(),
                    "Status Name"));
            hasChanges = true;
        }

        // Compare description
        if (!Objects.equals(updateBookingsStatusesRequest.getDescription(),
                previousBookingStatusDetails.getDescription())) {
            String oldDesc = previousBookingStatusDetails.getDescription() != null ?
                    previousBookingStatusDetails.getDescription() : "null";
            String newDesc = updateBookingsStatusesRequest.getDescription() != null ?
                    updateBookingsStatusesRequest.getDescription() : "null";
            changes.add(String.format("Description changed from '%s' to '%s'", oldDesc, newDesc));
            fieldChanges.add(new BookingsStatusesComparisonResult.FieldChange(
                    "description",
                    previousBookingStatusDetails.getDescription(),
                    updateBookingsStatusesRequest.getDescription(),
                    "Description"));
            hasChanges = true;
        }

        // Compare status (active/inactive)
        String oldStatus = previousBookingStatusDetails.getStatus();
        String newStatus = updateBookingsStatusesRequest.getStatus();
        if (oldStatus != null && newStatus != null && !oldStatus.equals(newStatus)) {
            changes.add(String.format("Status changed from '%s' to '%s'", oldStatus, newStatus));
            fieldChanges.add(new BookingsStatusesComparisonResult.FieldChange(
                    "status",
                    oldStatus,
                    newStatus,
                    "Status"));
            hasChanges = true;

            // Add warnings for status deactivation
            if ("INACTIVE".equals(newStatus) && "ACTIVE".equals(oldStatus)) {
                warnings.add("Warning: This booking status is being deactivated. It will no longer be available for selection.");
                warnings.add("Note: Ensure no active bookings are using this status before deactivating.");
            } else if ("ACTIVE".equals(newStatus) && "INACTIVE".equals(oldStatus)) {
                warnings.add("Info: This booking status is being reactivated.");
            }
        }

        // Check if statusId is being changed (should not happen, but validate)
        if (updateBookingsStatusesRequest.getStatusId() != null &&
                previousBookingStatusDetails.getStatusId() != null &&
                !updateBookingsStatusesRequest.getStatusId().equals(previousBookingStatusDetails.getStatusId())) {
            warnings.add("Warning: Status ID is being changed! This is not recommended.");
            fieldChanges.add(new BookingsStatusesComparisonResult.FieldChange(
                    "statusId",
                    previousBookingStatusDetails.getStatusId(),
                    updateBookingsStatusesRequest.getStatusId(),
                    "Status ID"));
            hasChanges = true;
        }

        // Additional validations
        if (updateBookingsStatusesRequest.getStatusName() != null &&
                updateBookingsStatusesRequest.getStatusName().trim().isEmpty()) {
            warnings.add("Warning: Status name is empty");
        }

        if (updateBookingsStatusesRequest.getDescription() != null &&
                updateBookingsStatusesRequest.getDescription().length() > 500) {
            warnings.add("Warning: Description is very long (>500 characters)");
        }

        // Check if no changes were made
        if (!hasChanges) {
            changes.add("No changes detected in booking status");
        }

        // Build the result
        return resultBuilder
                .fieldChanges(fieldChanges)
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy("System")
                .changedByUserId(null)
                .changeTimestamp(new Date().toString())
                .build();
    }
}
