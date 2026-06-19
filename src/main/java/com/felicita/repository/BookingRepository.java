package com.felicita.repository;

import com.felicita.model.dto.*;
import com.felicita.model.request.BookingCancelledRequest;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.BookingAssignStatisticsResponse;
import com.felicita.model.response.statistics.BookingHistoryStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatisticsResponse;
import com.felicita.model.response.statistics.BookingStatusStatisticsResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository {
    List<CompleteToursResponse> getCompletedBookingToursDetailsById(Long userId);

    List<UpcomingToursResponse> getUpcomingBookingToursDetailsById(Long userId);

    List<RequestedToursResponse> getRequstedToursDetailsById(Long userId);

    List<CancelledToursResponse> getCancelledToursDetailsById(Long userId);

    Long bookingTourBasicDetails(InsertBookingRequestDto insertBookingRequestDto);

    void bookingTransportation(Long bookingId, VehicleBasicDetailsDto vehicleBasicDetailsDto, LocalDate date, Long userId);

    void insertBookingPriceBreakdown(Long bookingId, PackageActivityPriceDto a, int totalParticipants,Long userId);

    void insertBookingParticipant(Long bookingId, BookingRequest.Participant participant, Long userId);

    void insertBookingNote(Long bookingId, BookingRequest.BookingNote note, Long userId);

    void insertBookingItinerary(Long bookingId, PackageDayAccommodationPriceDto p, LocalDate date, Long userId);

    void insertBookingActivities(Long bookingId, PackageActivityPriceDto a, int totalParticipants, Long userId);

    void insertBookingInvoice(Long bookingId, String invoiceNumber, LocalDate invoiceDate, LocalDate invoiceDueDate, Double totalAmount, Double taxAmount, Double discountAmount, Double finalAmount,BookingRequest.BookingInvoice invoices, Long userId);

    BookingBasicDetailsDto getBookingBasicDetailsByBookingId(Long bookingId);

    List<BookingActivityDto> getBookingActivityByBookingId(Long bookingId);

    List<BookingParticipantDto> getBookingParticipantByBookingId(Long bookingId);

    List<BookingFilterResponse> getBookingFilter();

    List<UserBookingSummaryResponse> getBookedTours(Long userId);

    void bookingAirportTransportation(Long bookingId, BookingRequest.Transport transport, Long userId);

    List<PendingToursResponse> getPendingBookingToursDetailsById(Long userId);

    Long insertTourBookingInquiry(TourBookingInquiryRequest tourBookingInquiryRequest, Long userId);

    void insertBookingInquiryToBookings(TourBookingInquiryRequest tourBookingInquiryRequest, Long userId, String bookingReference);

    void cancelledPendingBooking(BookingCancelledRequest bookingCancelledRequest, Long userId);

    BookingStatisticsResponse.Summary getBookingSummaryStatistics();

    List<BookingStatisticsResponse.MonthlyBookingTrend> getMonthlyBookingTrendsStatistics();

    List<BookingStatisticsResponse.MonthlyRevenueTrend> getMonthlyRevenueTrendsStatistics();

    List<BookingStatisticsResponse.BookingStatusDistribution> getBookingStatusDistributionsStatistics();

    List<BookingStatisticsResponse.BookingFunnel> getBookingFunnelsStatistics();

    List<BookingStatisticsResponse.TopTour> getTopToursStatistics();

    List<BookingStatisticsResponse.PopularActivity> getPopularActivitiesStatistics();

    BookingStatusStatisticsResponse.Summary getBookingStatusSummaryStatistics();

    List<BookingStatusStatisticsResponse.StatusDistribution> getStatusDistributionsStatistics();

    List<BookingStatusStatisticsResponse.StatusFunnel> getStatusFunnelsStatistics();

    List<BookingStatusStatisticsResponse.StatusTrend> getStatusTrendsStatistics();

    List<BookingStatusStatisticsResponse.DropOffStatistics> getDropOffStatisticsStatistics();

    BookingAssignStatisticsResponse.Summary getBookingAssignSummaryStatistics();

    List<BookingAssignStatisticsResponse.EmployeeWorkload> getEmployeeWorkloadsStatistics();

    List<BookingAssignStatisticsResponse.EmployeeRevenue> getEmployeeRevenuesStatistics();

    List<BookingAssignStatisticsResponse.DepartmentDistribution> getDepartmentDistributionsStatistics();

    List<BookingAssignStatisticsResponse.DesignationDistribution> getDesignationDistributionsStatistics();

    List<BookingAssignStatisticsResponse.MonthlyAssignmentTrend> getMonthlyAssignmentTrendsStatistics();

    List<BookingAssignStatisticsResponse.AssignmentStatusDistribution> getAssignmentStatusDistributionsStatistics();

    BookingHistoryStatisticsResponse.Summary getBookingHistorySummaryStatistics();

    List<BookingHistoryStatisticsResponse.BookingGrowthTrend> getBookingGrowthTrendsStatistics();

    List<BookingHistoryStatisticsResponse.RevenueGrowthTrend> getRevenueGrowthTrendsStatistics();

    List<BookingHistoryStatisticsResponse.BookingStatusHistory> getBookingStatusHistoriesStatistics();

    List<BookingHistoryStatisticsResponse.CancellationTrend> getCancellationTrendsStatistics();

    List<BookingHistoryStatisticsResponse.HistoricalTopTour> getHistoricalTopToursStatistics();

    List<BookingHistoryStatisticsResponse.CustomerReturnStatistics> getCustomerReturnStatisticsStatistics();

    List<BookingHistoryStatisticsResponse.PeakBookingPeriod> getPeakBookingPeriodsStatistics();
}
