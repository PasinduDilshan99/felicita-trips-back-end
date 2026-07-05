package com.felicita.controller;

import com.felicita.model.request.*;
import com.felicita.model.request.bookings.BookingDataRequest;
import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.model.request.bookings.UpdateBookingRequest;
import com.felicita.model.request.bookings.UpdateBookingStatusRequest;
import com.felicita.model.request.bookings.history.BookingHistoryDataRequest;
import com.felicita.model.request.bookings.status.InsertBookingsStatusesRequest;
import com.felicita.model.request.bookings.status.UpdateBookingsStatusesRequest;
import com.felicita.model.request.bookings.unassign.AssignBookingRequest;
import com.felicita.model.request.bookings.unassign.UnassignBookingRequest;
import com.felicita.model.request.bookings.unassign.UnassignBookingDataRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.bookings.*;
import com.felicita.model.response.bookings.history.BookingHisotryWithParamsResponse;
import com.felicita.model.response.bookings.history.BookingHistoryDetailsResponse;
import com.felicita.model.response.bookings.history.BookingsHistoryRequestParamsResponse;
import com.felicita.model.response.bookings.status.BookingStatusBasicDetailsResponse;
import com.felicita.model.response.bookings.status.BookingStatusDetailsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingWithParamsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingsRequestParamsResponse;
import com.felicita.model.response.common.BookingIdAndReferenceResponse;
import com.felicita.model.response.common.BookingStatusIdAndNameResponse;
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
        LOGGER.info(bookingDataRequest.toString());
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

    @PostMapping(path = "/booking-basic-details")
    public ResponseEntity<CommonResponse<BookingsBasicDetails>> getBookingBasicDetails(@RequestBody CommonIdRequest bookingId) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingsBasicDetails> response = bookingService.getBookingBasicDetails(bookingId);
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

    @PostMapping(path = "/create-booking-params")
    public ResponseEntity<CommonResponse<BookingCreatingRequestParamsResponse>> getCreateBookingParams(@RequestBody CommonIdRequest tourId) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingCreatingRequestParamsResponse> response = bookingService.getCreateBookingParams(tourId);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-booking")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateBooking(@RequestBody UpdateBookingRequest updateBookingRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = bookingService.updateBooking(updateBookingRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-booking-status")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateBookingStatus(@RequestBody UpdateBookingStatusRequest updateBookingStatusRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = bookingService.updateBookingStatus(updateBookingStatusRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-booking")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateBooking(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = bookingService.terminateBooking(commonIdRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/booking-id-and-references")
    public ResponseEntity<CommonResponse<List<BookingIdAndReferenceResponse>>> getBookingIdAndReferences() {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<BookingIdAndReferenceResponse>> response = bookingService.getBookingIdAndReferences();
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // booking statuses
    @GetMapping(path = "/bookings-statuses")
    public ResponseEntity<CommonResponse<List<BookingStatusBasicDetailsResponse>>> getBookingsStatuses() {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<BookingStatusBasicDetailsResponse>> response = bookingService.getBookingsStatuses();
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/bookings-statuses-id-and-names")
    public ResponseEntity<CommonResponse<List<BookingStatusIdAndNameResponse>>> getBookingsStatusesIdAndNames() {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<BookingStatusIdAndNameResponse>> response = bookingService.getBookingsStatusesIdAndNames();
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/bookings-statuses-basic-details")
    public ResponseEntity<CommonResponse<BookingStatusBasicDetailsResponse>> getBookingsStatusesBasicDetailsById(@RequestBody CommonIdRequest bookingId) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingStatusBasicDetailsResponse> response = bookingService.getBookingsStatusesBasicDetailsById(bookingId);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/bookings-statuses-all-details")
    public ResponseEntity<CommonResponse<BookingStatusDetailsResponse>> getBookingsStatusesAllDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingStatusDetailsResponse> response = bookingService.getBookingsStatusesAllDetailsById(commonIdRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create-bookings-statuses")
    public ResponseEntity<CommonResponse<InsertResponse>> createBookingsStatuses(@RequestBody InsertBookingsStatusesRequest insertBookingsStatusesRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = bookingService.createBookingsStatuses(insertBookingsStatusesRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-bookings-statuses")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateBookingsStatuses(@RequestBody UpdateBookingsStatusesRequest updateBookingsStatusesRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = bookingService.updateBookingsStatuses(updateBookingsStatusesRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-bookings-statuses")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateBookingsStatuses(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = bookingService.terminateBookingsStatuses(commonIdRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // unassign bookings
    @PostMapping(path = "/unassign-bookings")
    public ResponseEntity<CommonResponse<UnassignBookingWithParamsResponse>> getUnassignBookingsWithParams(@RequestBody UnassignBookingDataRequest unassignBookingDataRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UnassignBookingWithParamsResponse> response = bookingService.getUnassignBookingsWithParams(unassignBookingDataRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/unassign-bookings-request-params")
    public ResponseEntity<CommonResponse<UnassignBookingsRequestParamsResponse>> getUnassignBookingsParamsData() {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UnassignBookingsRequestParamsResponse> response = bookingService.getUnassignBookingsParamsData();
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/unassign-booking-list")
    public ResponseEntity<CommonResponse<List<BookingIdAndReferenceResponse>>> getUnassignBookingList() {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<BookingIdAndReferenceResponse>> response = bookingService.getUnassignBookingList();
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/assigned-unassign-booking")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateUnassignBookingToAssign(@RequestBody AssignBookingRequest assignBookingRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = bookingService.updateUnassignBookingToAssign(assignBookingRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/assign-booking-list")
    public ResponseEntity<CommonResponse<List<BookingIdAndReferenceResponse>>> getassignBookingList() {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<BookingIdAndReferenceResponse>> response = bookingService.getassignBookingList();
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-unassign-booking")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateUnassignBooking(@RequestBody UnassignBookingRequest unassignBookingRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = bookingService.updateUnassignBooking(unassignBookingRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // billings
    @PostMapping(path = "/booking-billing-details")
    public ResponseEntity<CommonResponse<BookingBillResponse>> getBookingBillingDetails(@RequestBody CommonIdRequest bookingId) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingBillResponse> response = bookingService.getBookingBillingDetails(bookingId);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // booking history
    @PostMapping(path = "/bookings-history")
    public ResponseEntity<CommonResponse<BookingHisotryWithParamsResponse>> getBookingHistoryByRequestParam(@RequestBody BookingHistoryDataRequest bookingHistoryDataRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingHisotryWithParamsResponse> response = bookingService.getBookingHistoryByRequestParam(bookingHistoryDataRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/bookings-history-request-params")
    public ResponseEntity<CommonResponse<BookingsHistoryRequestParamsResponse>> getBookingsHistoryParamsData() {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingsHistoryRequestParamsResponse> response = bookingService.getBookingsHistoryParamsData();
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/bookings-history-details")
    public ResponseEntity<CommonResponse<BookingHistoryDetailsResponse>> getBookingHistoryDetailsById(@RequestBody CommonIdRequest bookingId) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<BookingHistoryDetailsResponse> response = bookingService.getBookingHistoryDetailsById(bookingId);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
