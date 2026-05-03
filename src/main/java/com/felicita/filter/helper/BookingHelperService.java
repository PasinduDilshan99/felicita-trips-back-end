package com.felicita.filter.helper;

import com.felicita.model.dto.*;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.response.PrintReceiptForBookingResponse;

import java.util.List;

public interface BookingHelperService{

    String generateUniqueBookingReferance();

    Double calculateTotalAmount(BookingRequest bookingRequest, PackageBasicDetailsDto packageBasicDetailsDto);

    Double calculateDiscountAmount(BookingRequest bookingRequest, PackageBasicDetailsDto packageBasicDetailsDto);

    Double calculateTaxAmount(BookingRequest bookingRequest, PackageBasicDetailsDto packageBasicDetailsDto);

    Double calculateInsuranceAmount(BookingRequest bookingRequest, PackageBasicDetailsDto packageBasicDetailsDto);

    String generateUniqueBookingInvoiceReference(Long bookingId);

    Double calculateTotalAccomodationAmountPerPerson(List<PackageDayAccommodationPriceDto> packageDayAccommodationPriceDto);

    Double calculateTotalActivityAmountPerPerson(List<PackageActivityPriceDto> packageActivityPriceDto);

    Double calculateTotalDestinationExtraAmountPerPerson(List<PackageDestinationExtraPriceDto> packageDestinationExtraPriceDto);

    PrintReceiptForBookingResponse createReceiptForBooking(BookingBasicDetailsDto bookingBasicDetailsDto, List<BookingActivityDto> bookingActivityDtos, List<BookingParticipantDto> bookingParticipantDtos, List<PackageDestinationExtraPriceDto> packageDestinationExtraPriceDto, List<PackageDayAccommodationPriceDto> packageDayAccommodationPriceDto);
}
