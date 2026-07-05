package com.felicita.validation;

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
import com.felicita.model.request.bookings.unassign.UnassignBookingRequest;

public interface BookingValidationService {
    void validateBookingRequest(BookingRequest bookingRequest);

    void validateTourBookingInquiryRequest(TourBookingInquiryRequest tourBookingInquiryRequest);

    void validateBookingCancelledRequest(BookingCancelledRequest bookingCancelledRequest);

    void validateBookingDataRequest(BookingDataRequest bookingDataRequest);

    void validateInsertBookingRequest(InsertBookingRequest insertBookingRequest);

    void validateUpdateBookingRequest(UpdateBookingRequest updateBookingRequest);

    void validateCommonIdRequest(CommonIdRequest commonIdRequest);

    void validateUpdateBookingStatusRequest(UpdateBookingStatusRequest updateBookingStatusRequest);

    void validateUpdateBookingsStatusesRequest(UpdateBookingsStatusesRequest updateBookingsStatusesRequest);

    void validateInsertBookingsStatusesRequest(InsertBookingsStatusesRequest insertBookingsStatusesRequest);

    void validateAssignBookingRequest(AssignBookingRequest assignBookingRequest);

    void validateUnassignBookingRequest(UnassignBookingRequest unassignBookingRequest);
}
