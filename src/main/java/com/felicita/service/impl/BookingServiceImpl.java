package com.felicita.service.impl;

import com.felicita.email.BookingEmailHelperService;
import com.felicita.exception.*;
import com.felicita.filter.helper.BookingHelperService;
import com.felicita.model.dto.*;
import com.felicita.model.enums.*;
import com.felicita.model.request.BookingCancelledRequest;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.model.request.bookings.BookingDataRequest;
import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.bookings.BookingAllDetailsResponse;
import com.felicita.model.response.bookings.BookingWithParamsResponse;
import com.felicita.model.response.bookings.BookingsBasicDetails;
import com.felicita.model.response.bookings.BookingsRequestParamsResponse;
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

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_BOOKING_DETAILS;

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
            }else{
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

            String bookingReference = commonService.createBooingReference();

            Long bookingId = bookingRepository.createBooking(insertBookingRequest,bookingReference, userId);
            bookingRepository.addParticipantsToBooking(bookingId, insertBookingRequest.getParticipants(), userId);
            bookingRepository.addAccommodationsToBooking(bookingId, insertBookingRequest.getAccommodations(), userId);
            bookingRepository.addTransportationsToBooking(bookingId, insertBookingRequest.getTransportations(), userId);
            bookingRepository.addActivitiesToBooking(bookingId, insertBookingRequest.getActivities(), userId);
            bookingRepository.addDocumentsToBooking(bookingId, insertBookingRequest.getDocuments(), userId);
            bookingRepository.addInsuranceToBooking(bookingId, insertBookingRequest.getBookingInsurance(), userId);
            bookingRepository.addNotesToBooking(bookingId, insertBookingRequest.getBookingNotes(), userId);
            bookingRepository.addPriceBreakdownToBooking(bookingId, insertBookingRequest.getPriceBreakDowns(), userId);
            bookingRepository.addBookingInvoiceToBooking(bookingId, insertBookingRequest.getBookingInvoice(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.BOOKING_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Booking Created")
                    .message("A new booking '" + bookingReference + "' has been created.")
                    .actionUrl(VIEW_BOOKING_DETAILS + "/" + bookingId)
                    .actionText("View Booking")
                    .icon("CalendarCheck")
                    .color("#10B981")
                    .metadata(Map.of(
                            "bookingId", bookingId,
                            "bookingReference", bookingReference,
                            "packageScheduleId", insertBookingRequest.getPackageScheduleId(),
                            "totalPersons", insertBookingRequest.getTotalPersons(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.BOOKING_CREATE.name())
                    .sourceModule(SourceModule.BOOKING.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (bookingId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.TOUR_CREATED.name(), supervisorUserIds);
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
                    new InsertResponse(),
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

}
