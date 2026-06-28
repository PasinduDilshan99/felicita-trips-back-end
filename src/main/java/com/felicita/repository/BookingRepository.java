package com.felicita.repository;

import com.felicita.model.dto.*;
import com.felicita.model.request.BookingCancelledRequest;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.model.request.bookings.BookingDataRequest;
import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.model.request.bookings.UpdateBookingRequest;
import com.felicita.model.request.bookings.UpdateBookingStatusRequest;
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
import com.felicita.model.response.bookings.status.BookingStatusBasicDetailsResponse;
import com.felicita.model.response.bookings.status.BookingStatusDetailsResponse;
import com.felicita.model.response.bookings.unassign.UnassignBookingBasicDetailsResponse;
import com.felicita.model.response.common.BookingIdAndReferenceResponse;
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

    List<BookingsBasicDetails> getBookingBasicDetailsForParams(BookingDataRequest bookingDataRequest);

    Integer getBookingCountForParams(BookingDataRequest bookingDataRequest);

    BookingsRequestParamsResponse getBookingsParamsData();

    BookingAllDetailsResponse.BookingInformation getBookingInformationById(Long bookingId);

    BookingAllDetailsResponse.CustomerInformation getCustomerInformationByBookingId(Long bookingId);

    BookingAllDetailsResponse.TourInformation getTourInformationByBookingId(Long bookingId);

    BookingAllDetailsResponse.PackageInformation getPackageInformationByBookingId(Long bookingId);

    BookingAllDetailsResponse.BookingStatusInformation getBookingStatusInformationByBookingId(Long bookingId);

    BookingAllDetailsResponse.AssignmentInformation getAssignmentInformationByBookingId(Long bookingId);

    List<BookingAllDetailsResponse.ParticipantInformation> getParticipantsByBookingId(Long bookingId);

    BookingAllDetailsResponse.CancellationInformation getCancellationInformationByBookingId(Long bookingId);

    List<BookingAllDetailsResponse.AccommodationInformation> getAccommodationsByBookingId(Long bookingId);

    List<BookingAllDetailsResponse.TransportationInformation> getTransportationsByBookingId(Long bookingId);

    List<BookingAllDetailsResponse.ActivityInformation> getActivitiesByBookingId(Long bookingId);

    Long createBooking(InsertBookingRequest insertBookingRequest, String bookingReference, Long userId);

    void addParticipantsToBooking(Long bookingId, List<InsertBookingRequest.Participant> participants, Long userId);

    void addAccommodationsToBooking(Long bookingId, List<InsertBookingRequest.Accommodation> accommodations, Long userId);

    void addTransportationsToBooking(Long bookingId, List<InsertBookingRequest.Transportation> transportations, Long userId);

    void addActivitiesToBooking(Long bookingId, List<InsertBookingRequest.Activity> activities, Long userId);

    void addDocumentsToBooking(Long bookingId, List<InsertBookingRequest.BookingDocuments> documents, Long userId);

    void addInsuranceToBooking(Long bookingId, InsertBookingRequest.BookingInsurance bookingInsurance, Long userId);

    void addNotesToBooking(Long bookingId, List<InsertBookingRequest.BookingNote> bookingNotes, Long userId);

    void addPriceBreakdownToBooking(Long bookingId, List<InsertBookingRequest.BookingPriceBreakDown> priceBreakDowns, Long userId);

    void addBookingInvoiceToBooking(Long bookingId,String invoiceReference, InsertBookingRequest.BookingInvoice bookingInvoice, Long userId);

    void addItinerariesToBooking(Long bookingId, List<InsertBookingRequest.BookingItinerary> bookingItineraries, Long userId);

    List<BookingAllDetailsResponse.BookingDocuments> getBookingDocumentsByBookingId(Long bookingId);

    BookingAllDetailsResponse.BookingInsurance getBookingInsuranceByBookingId(Long bookingId);

    List<BookingAllDetailsResponse.BookingItinerary> getBookingItineraryByBookingId(Long bookingId);

    List<BookingAllDetailsResponse.BookingNote> getBookingNoteByBookingId(Long bookingId);

    List<BookingAllDetailsResponse.BookingPriceBreakDown> getBookingPriceBreakDownByBookingId(Long bookingId);

    BookingAllDetailsResponse.BookingInvoice getBookingInvoiceByBookingId(Long bookingId);

    void updateBookingBasicInformation(UpdateBookingRequest updateBookingRequest, Long userId);

    void removeParticipantsFromBooking(Long bookingId, List<Long> removeParticipants, Long userId);

    void updateParticipantsOfBooking(Long bookingId, List<UpdateBookingRequest.UpdateParticipant> updateParticipants, Long userId);

    void removeAccommodationsFromBooking(Long bookingId, List<Long> removeAccommodations, Long userId);

    void updateAccommodationsOfBooking(Long bookingId, List<UpdateBookingRequest.UpdateAccommodation> updateAccommodations, Long userId);

    void removeTransportationsFromBooking(Long bookingId, List<Long> removeTransportations, Long userId);

    void updateTransportationsOfBooking(Long bookingId, List<UpdateBookingRequest.UpdateTransportation> updateTransportations, Long userId);

    void removeActivitiesFromBooking(Long bookingId, List<Long> removeActivities, Long userId);

    void updateActivitiesOfBooking(Long bookingId, List<UpdateBookingRequest.UpdateActivity> updateActivities, Long userId);

    void removeDocumentsFromBooking(Long bookingId, List<Long> removeDocuments, Long userId);

    void updateDocumentsOfBooking(Long bookingId, List<UpdateBookingRequest.UpdateBookingDocuments> updateDocuments, Long userId);

    void updateInsuranceOfBooking(Long bookingId, UpdateBookingRequest.UpdateBookingInsurance updateBookingInsurance, Long userId);

    void removeInsuranceFromBooking(Long bookingId, Long removeBookingInsurance, Long userId);

    void removeItinerariesFromBooking(Long bookingId, List<Long> removeBookingItineraries, Long userId);

    void updateItinerariesOfBooking(Long bookingId, List<UpdateBookingRequest.UpdateBookingItinerary> updateBookingItineraries, Long userId);

    void removeNotesFromBooking(Long bookingId, List<Long> removeBookingNotes, Long userId);

    void updateNotesOfBooking(Long bookingId, List<UpdateBookingRequest.UpdateBookingNote> updateBookingNotes, Long userId);

    void removePriceBreakdownFromBooking(Long bookingId, List<Long> removePriceBreakDowns, Long userId);

    void updatePriceBreakdownOfBooking(Long bookingId, List<UpdateBookingRequest.UpdateBookingPriceBreakDown> updatePriceBreakDowns, Long userId);

    void updateBookingInvoiceOfBooking(Long bookingId, UpdateBookingRequest.UpdateBookingInvoice updateBookingInvoice, Long userId);

    void removeBookingInvoiceFromBooking(Long bookingId, Long removeBookingInvoice, Long userId);

    BookingsBasicDetails getBookingBasicDetails(CommonIdRequest bookingId);

    void updateBookingStatus(UpdateBookingStatusRequest updateBookingStatusRequest, Long userId);

    List<BookingStatusBasicDetailsResponse> getBookingsStatuses();

    BookingStatusBasicDetailsResponse getBookingsStatusesBasicDetailsById(CommonIdRequest bookingId);

    BookingStatusDetailsResponse getBookingsStatusesAllDetailsById(CommonIdRequest bookingId);

    Long createBookingsStatuses(InsertBookingsStatusesRequest insertBookingsStatusesRequest, Long userId);

    void updateBookingsStatuses(UpdateBookingsStatusesRequest updateBookingsStatusesRequest, Long userId);

    void terminateBookingsStatuses(CommonIdRequest commonIdRequest, Long userId);

    BookingBillResponse.BookingBasicInfo getBookingBasicInfoForBill(Long id);

    BookingBillResponse.Customer getCustomerForBill(Long id);

    BookingBillResponse.TourDetails getTourDetailsForBill(Long id);

    BookingBillResponse.PackageDetails getPackageDetailsForBill(Long id);

    List<BookingBillResponse.Participant> getParticipantsForBill(Long id);

    List<BookingBillResponse.PriceItem> getPriceBreakdownForBill(Long id);

    BookingBillResponse.BillingSummary getBillingSummaryForBill(Long id);

    List<BookingIdAndReferenceResponse> getBookingIdAndReferences(String assignStatus);

    List<UnassignBookingBasicDetailsResponse> getUnassignBookingBasicDetails(UnassignBookingDataRequest unassignBookingDataRequest);

    Integer getUnassignBookingBasicDetailsCount(UnassignBookingDataRequest unassignBookingDataRequest);

    List<String> getUnassignBookingReferences();

    void updateUnassignBookingToAssign(AssignBookingRequest assignBookingRequest, Long userId);

    void updateUnassignBooking(UnassignBookingRequest unassignBookingRequest, Long userId);
}
