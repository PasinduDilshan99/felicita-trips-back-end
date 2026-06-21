package com.felicita.validation;

import com.felicita.model.request.BookingCancelledRequest;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.model.request.bookings.BookingDataRequest;
import com.felicita.model.request.bookings.InsertBookingRequest;

public interface BookingValidationService {
    void validateBookingRequest(BookingRequest bookingRequest);

    void validateTourBookingInquiryRequest(TourBookingInquiryRequest tourBookingInquiryRequest);

    void validateBookingCancelledRequest(BookingCancelledRequest bookingCancelledRequest);

    void validateBookingDataRequest(BookingDataRequest bookingDataRequest);

    void validateInsertBookingRequest(InsertBookingRequest insertBookingRequest);
}
