package com.felicita.controller;

import com.felicita.model.request.*;
import com.felicita.model.request.bookings.BookingDataRequest;
import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.bookings.BookingAllDetailsResponse;
import com.felicita.model.response.bookings.BookingWithParamsResponse;
import com.felicita.model.response.bookings.BookingsRequestParamsResponse;
import com.felicita.model.response.statistics.BookingAssignStatisticsResponse;
import com.felicita.model.response.statistics.BookingHistoryStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatusStatisticsResponse;
import com.felicita.service.BookingService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/booking")
public class BookingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(path = "/pending")
    public ResponseEntity<CommonResponse<List<PendingToursResponse>>> getPendingBookingToursDetailsById() {
        LOGGER.info("{} Start execute get pending booking tours details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PendingToursResponse>> response = bookingService.getPendingBookingToursDetailsById();
        LOGGER.info("{} End execute get pending booking tours details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/cancelled-pending")
    public ResponseEntity<CommonResponse<UpdateResponse>> cancelledPendingBooking(@RequestBody BookingCancelledRequest bookingCancelledRequest) {
        LOGGER.info("{} Start execute cancelled pending booking {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = bookingService.cancelledPendingBooking(bookingCancelledRequest);
        LOGGER.info("{} End execute cancelled pending booking {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/completed")
    public ResponseEntity<CommonResponse<List<CompleteToursResponse>>> getCompletedBookingToursDetailsById() {
        LOGGER.info("{} Start execute get completed booking tours details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<CompleteToursResponse>> response = bookingService.getCompletedBookingToursDetailsById();
        LOGGER.info("{} End execute get completed booking tours details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/upcoming")
    public ResponseEntity<CommonResponse<List<UpcomingToursResponse>>> getUpcomingBookingToursDetailsById() {
        LOGGER.info("{} Start execute get upcoming booking tours details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<UpcomingToursResponse>> response = bookingService.getUpcomingBookingToursDetailsById();
        LOGGER.info("{} End execute get upcoming booking tours details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/requested")
    public ResponseEntity<CommonResponse<List<RequestedToursResponse>>> getRequstedToursDetailsById() {
        LOGGER.info("{} Start execute get requested booking tours details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<RequestedToursResponse>> response = bookingService.getRequstedToursDetailsById();
        LOGGER.info("{} End execute get requested booking tours details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/cancelled")
    public ResponseEntity<CommonResponse<List<CancelledToursResponse>>> getCancelledToursDetailsById() {
        LOGGER.info("{} Start execute get cancelled booking tours details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<CancelledToursResponse>> response = bookingService.getCancelledToursDetailsById();
        LOGGER.info("{} End execute get cancelled booking tours details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/book-tour-filter")
    public ResponseEntity<CommonResponse<List<BookingFilterResponse>>> getBookingFilter() {
        LOGGER.info("{} Start execute get filters in the booking {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<BookingFilterResponse>> response = bookingService.getBookingFilter();
        LOGGER.info("{} End execute get filters in the booking {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/booked-tours")
    public ResponseEntity<CommonResponse<List<UserBookingSummaryResponse>>> getBookedTours() {
        LOGGER.info("{} Start execute get booked tours {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<UserBookingSummaryResponse>> response = bookingService.getBookedTours();
        LOGGER.info("{} End execute get booked tours {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/book-tour")
    public ResponseEntity<CommonResponse<BookInsertResponse>> bookingTour(@RequestBody BookingRequest bookingRequest) {
        LOGGER.info("{} Start execute booking a tour {}", Constant.DOTS, Constant.DOTS);
        LOGGER.info("Booking request: {}", bookingRequest);
        CommonResponse<BookInsertResponse> response = bookingService.bookingTour(bookingRequest);
        LOGGER.info("{} End execute booking a tour {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/tour-book-inquiry")
    public ResponseEntity<CommonResponse<InsertResponse>> tourBookingInquiry(@RequestBody TourBookingInquiryRequest tourBookingInquiryRequest) {
        LOGGER.info("{} Start execute booking a tour inquiry {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = bookingService.tourBookingInquiry(tourBookingInquiryRequest);
        LOGGER.info("{} End execute booking a tour inquiry {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/book-receipt/{bookingId}")
    public ResponseEntity<CommonResponse<PrintReceiptForBookingResponse>> createReceiptForBooking(@PathVariable Long bookingId) {
        LOGGER.info("{} Start execute create receipt for booking a tour {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PrintReceiptForBookingResponse> response = bookingService.createReceiptForBooking(bookingId);
        LOGGER.info("{} End execute create receipt for booking a tour {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // admin
    @GetMapping(path = "/booking-statistics")
    public ResponseEntity<CommonResponse<BookingStatisticsResponse>> getBookingStatistics() {
        LOGGER.info("{} Start execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingStatisticsResponse> response = bookingService.getBookingStatistics();
        LOGGER.info("{} End execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/booking-status-statistics")
    public ResponseEntity<CommonResponse<BookingStatusStatisticsResponse>> getBookingStatusStatistics() {
        LOGGER.info("{} Start execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingStatusStatisticsResponse> response = bookingService.getBookingStatusStatistics();
        LOGGER.info("{} End execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/booking-assign-statistics")
    public ResponseEntity<CommonResponse<BookingAssignStatisticsResponse>> getBookingAssignStatistics() {
        LOGGER.info("{} Start execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingAssignStatisticsResponse> response = bookingService.getBookingAssignStatistics();
        LOGGER.info("{} End execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/booking-history-statistics")
    public ResponseEntity<CommonResponse<BookingHistoryStatisticsResponse>> getBookingHistoryStatistics() {
        LOGGER.info("{} Start execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingHistoryStatisticsResponse> response = bookingService.getBookingHistoryStatistics();
        LOGGER.info("{} End execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/bookings")
    public ResponseEntity<CommonResponse<BookingWithParamsResponse>> getBookingsWithParams(@RequestBody BookingDataRequest bookingDataRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingWithParamsResponse> response = bookingService.getBookingsWithParams(bookingDataRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/bookings-request-params")
    public ResponseEntity<CommonResponse<BookingsRequestParamsResponse>> getBookingsParamsData() {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingsRequestParamsResponse> response = bookingService.getBookingsParamsData();
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/booking-all-details")
    public ResponseEntity<CommonResponse<BookingAllDetailsResponse>> getBookingAllDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingAllDetailsResponse> response = bookingService.getBookingAllDetailsById(commonIdRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create-booking")
    public ResponseEntity<CommonResponse<InsertResponse>> createBooking(@RequestBody InsertBookingRequest insertBookingRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = bookingService.createBooking(insertBookingRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
