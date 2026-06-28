package com.felicita.service;

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
import com.felicita.model.response.bookings.history.BookingHistoryDetailsResponse;
import com.felicita.model.response.bookings.history.BookingsHistoryRequestParamsResponse;
import com.felicita.model.response.bookings.status.BookingStatusBasicDetailsResponse;
import com.felicita.model.response.bookings.status.BookingStatusDetailsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingWithParamsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingsRequestParamsResponse;
import com.felicita.model.response.common.BookingIdAndReferenceResponse;
import com.felicita.model.response.statistics.BookingAssignStatisticsResponse;
import com.felicita.model.response.statistics.BookingHistoryStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatusStatisticsResponse;

import java.util.List;

public interface BookingService {
    CommonResponse<List<CompleteToursResponse>> getCompletedBookingToursDetailsById();

    CommonResponse<List<UpcomingToursResponse>> getUpcomingBookingToursDetailsById();

    CommonResponse<List<RequestedToursResponse>> getRequstedToursDetailsById();

    CommonResponse<List<CancelledToursResponse>> getCancelledToursDetailsById();

    CommonResponse<BookInsertResponse> bookingTour(BookingRequest bookingRequest);

    CommonResponse<PrintReceiptForBookingResponse> createReceiptForBooking(Long bookingId);

    CommonResponse<List<BookingFilterResponse>> getBookingFilter();

    CommonResponse<List<UserBookingSummaryResponse>> getBookedTours();

    CommonResponse<List<PendingToursResponse>> getPendingBookingToursDetailsById();

    CommonResponse<InsertResponse> tourBookingInquiry(TourBookingInquiryRequest tourBookingInquiryRequest);

    CommonResponse<UpdateResponse> cancelledPendingBooking(BookingCancelledRequest bookingCancelledRequest);

    CommonResponse<BookingStatisticsResponse> getBookingStatistics();

    CommonResponse<BookingStatusStatisticsResponse> getBookingStatusStatistics();

    CommonResponse<BookingAssignStatisticsResponse> getBookingAssignStatistics();

    CommonResponse<BookingHistoryStatisticsResponse> getBookingHistoryStatistics();

    CommonResponse<BookingWithParamsResponse> getBookingsWithParams(BookingDataRequest bookingDataRequest);

    CommonResponse<BookingsRequestParamsResponse> getBookingsParamsData();

    CommonResponse<BookingAllDetailsResponse> getBookingAllDetailsById(CommonIdRequest commonIdRequest);

    CommonResponse<InsertResponse> createBooking(InsertBookingRequest insertBookingRequest);

    CommonResponse<UpdateResponse> updateBooking(UpdateBookingRequest updateBookingRequest);

    CommonResponse<TerminateResponse> terminateBooking(CommonIdRequest commonIdRequest);

    CommonResponse<UpdateResponse> updateBookingStatus(UpdateBookingStatusRequest updateBookingStatusRequest);

    CommonResponse<BookingsBasicDetails> getBookingBasicDetails(CommonIdRequest bookingId);

    CommonResponse<List<BookingStatusBasicDetailsResponse>> getBookingsStatuses();

    CommonResponse<BookingStatusBasicDetailsResponse> getBookingsStatusesBasicDetailsById(CommonIdRequest bookingId);

    CommonResponse<BookingStatusDetailsResponse> getBookingsStatusesAllDetailsById(CommonIdRequest commonIdRequest);

    CommonResponse<InsertResponse> createBookingsStatuses(InsertBookingsStatusesRequest insertBookingsStatusesRequest);

    CommonResponse<UpdateResponse> updateBookingsStatuses(UpdateBookingsStatusesRequest updateBookingsStatusesRequest);

    CommonResponse<TerminateResponse> terminateBookingsStatuses(CommonIdRequest commonIdRequest);

    CommonResponse<BookingBillResponse> getBookingBillingDetails(CommonIdRequest bookingId);

    CommonResponse<List<BookingIdAndReferenceResponse>> getBookingIdAndReferences();

    CommonResponse<UnassignBookingWithParamsResponse> getUnassignBookingsWithParams(UnassignBookingDataRequest unassignBookingDataRequest);

    CommonResponse<UnassignBookingsRequestParamsResponse> getUnassignBookingsParamsData();

    CommonResponse<List<BookingIdAndReferenceResponse>> getUnassignBookingList();

    CommonResponse<List<BookingIdAndReferenceResponse>> getassignBookingList();

    CommonResponse<UpdateResponse> updateUnassignBookingToAssign(AssignBookingRequest assignBookingRequest);

    CommonResponse<UpdateResponse> updateUnassignBooking(UnassignBookingRequest unassignBookingRequest);

    CommonResponse<BookingHisotryWithParamsResponse> getBookingHistoryByRequestParam(BookingHistoryDataRequest bookingHistoryDataRequest);

    CommonResponse<BookingsHistoryRequestParamsResponse> getBookingsHistoryParamsData();

    CommonResponse<BookingHistoryDetailsResponse> getBookingHistoryDetailsById(CommonIdRequest bookingId);
}
