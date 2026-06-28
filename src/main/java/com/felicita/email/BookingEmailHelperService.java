package com.felicita.email;

import com.felicita.model.other.*;
import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.model.request.bookings.status.InsertBookingsStatusesRequest;
import com.felicita.model.response.bookings.BookingAllDetailsResponse;
import com.felicita.model.response.bookings.BookingsBasicDetails;
import com.felicita.model.response.bookings.status.BookingStatusDetailsResponse;
import com.felicita.security.model.User;

public interface BookingEmailHelperService {
    String buildBookingCreateSuccessfullBody(InsertBookingRequest insertBookingRequest, Long bookingId, User loggedUser);

    String buildBookingCreateSuccessfullSubject(InsertBookingRequest insertBookingRequest, Long bookingId, User loggedUser);

    String buildBookingUpdateSuccessfullSubject(User loggedUser, Long bookingId);

    String buildBookingUpdateSuccessfullBody(User loggedUser, Long bookingId, BookingComparisonResult comparisonResult);

    String buildBookingStatusUpdateSuccessfullSubject(User loggedUser, Long bookingId, BookingsBasicDetails bookingsBasicDetails);

    String buildBookingStatusUpdateSuccessfullBody(User loggedUser, Long bookingId, BookingStatusComparisonResult comparisonResult, BookingsBasicDetails bookingsBasicDetails);

    String buildBookingTerminateSuccessfullSubject(User loggedUser, BookingAllDetailsResponse bookingDetails);

    String buildBookingTerminateSuccessfullBody(User loggedUser, BookingAllDetailsResponse bookingDetails);

    String buildBookingsStatusesCreateSuccessfullBody(InsertBookingsStatusesRequest insertBookingsStatusesRequest, Long bookingStatusId, User loggedUser);

    String buildBookingsStatusesCreateSuccessfullSubject(InsertBookingsStatusesRequest insertBookingsStatusesRequest, Long bookingStatusId, User loggedUser);

    String buildBookingsStatusesUpdateSuccessfullSubject(User loggedUser, Long statusId);

    String buildBookingsStatusesUpdateSuccessfullBody(User loggedUser, Long statusId, BookingsStatusesComparisonResult comparisonResult);

    String buildBookingsStatusesTerminateSuccessfullSubject(User loggedUser, BookingStatusDetailsResponse bookingStatusResponse);

    String buildBookingsStatusesTerminateSuccessfullBody(User loggedUser, BookingStatusDetailsResponse bookingStatusResponse);

    String buildAssignBookingSuccessfullSubject(User loggedUser, Long bookingId);

    String buildAssignBookingSuccessfullBody(User loggedUser, Long bookingId, AssignBookingComparisonResult comparisonResult);

    String buildAssignBookingUpdateSuccessfullSubject(User loggedUser, Long bookingId);

    String buildAssignBookingUpdateSuccessfullBody(User loggedUser, Long bookingId, AssignBookingUpdateComparisonResult comparisonResult);
}
