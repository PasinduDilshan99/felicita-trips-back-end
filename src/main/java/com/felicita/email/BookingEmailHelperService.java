package com.felicita.email;

import com.felicita.model.request.bookings.InsertBookingRequest;
import com.felicita.security.model.User;

public interface BookingEmailHelperService {
    String buildBookingCreateSuccessfullBody(InsertBookingRequest insertBookingRequest, Long bookingId, User loggedUser);

    String buildBookingCreateSuccessfullSubject(InsertBookingRequest insertBookingRequest, Long bookingId, User loggedUser);
}
