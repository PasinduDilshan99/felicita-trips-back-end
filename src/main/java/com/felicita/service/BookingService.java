package com.felicita.service;

import com.felicita.model.request.BookingCancelledRequest;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.model.response.*;
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
}
