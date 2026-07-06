package com.felicita.validation.impl;

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
import com.felicita.service.CommonService;
import com.felicita.validation.BookingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingValidationServiceImpl implements BookingValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingValidationServiceImpl.class);

    private final CommonService commonService;

    @Autowired
    public BookingValidationServiceImpl(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public void validateBookingRequest(BookingRequest bookingRequest) {

    }

    @Override
    public void validateTourBookingInquiryRequest(TourBookingInquiryRequest tourBookingInquiryRequest) {

    }

    @Override
    public void validateBookingCancelledRequest(BookingCancelledRequest bookingCancelledRequest) {

    }

    @Override
    public void validateBookingDataRequest(BookingDataRequest bookingDataRequest) {

    }

    @Override
    public void validateInsertBookingRequest(InsertBookingRequest insertBookingRequest) {

    }

    @Override
    public void validateUpdateBookingRequest(UpdateBookingRequest updateBookingRequest) {

    }

    @Override
    public void validateCommonIdRequest(CommonIdRequest commonIdRequest) {

    }

    @Override
    public void validateUpdateBookingStatusRequest(UpdateBookingStatusRequest updateBookingStatusRequest) {

    }

    @Override
    public void validateUpdateBookingsStatusesRequest(UpdateBookingsStatusesRequest updateBookingsStatusesRequest) {

    }

    @Override
    public void validateInsertBookingsStatusesRequest(InsertBookingsStatusesRequest insertBookingsStatusesRequest) {

    }

    @Override
    public void validateAssignBookingRequest(AssignBookingRequest assignBookingRequest) {

    }

    @Override
    public void validateUnassignBookingRequest(UnassignBookingRequest unassignBookingRequest) {

    }
}
