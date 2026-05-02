package com.felicita.filter.helper.impl;

import com.felicita.filter.helper.BookingHelperService;
import com.felicita.model.dto.*;
import com.felicita.model.request.BookingRequest;
import com.felicita.model.response.PrintReceiptForBookingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BookingHelperServiceImpl implements BookingHelperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingHelperServiceImpl.class);


    private final Double FIXED_INSURANCE_AMOUNT_PER_PERSON = 5000.00;


    @Override
    public String generateUniqueBookingReferance() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "BK-" + timestamp + "-" + random;
    }

    @Override
    public Double calculateTotalAmount(BookingRequest bookingRequest, PackageBasicDetailsDto packageBasicDetailsDto) {
        int size = bookingRequest.getParticipants().size();
        return packageBasicDetailsDto.getPricePerPerson() * size;
    }

    @Override
    public Double calculateDiscountAmount(BookingRequest bookingRequest, PackageBasicDetailsDto packageBasicDetailsDto) {
        Double totalAmount = calculateTotalAmount(bookingRequest, packageBasicDetailsDto);
        return (totalAmount * packageBasicDetailsDto.getDiscountPercentage()) / 100;
    }

    @Override
    public Double calculateTaxAmount(BookingRequest bookingRequest, PackageBasicDetailsDto packageBasicDetailsDto) {
        Double totalAmount = calculateTotalAmount(bookingRequest, packageBasicDetailsDto);
        return (totalAmount * 18) / 100;
    }

    @Override
    public Double calculateInsuranceAmount(BookingRequest bookingRequest, PackageBasicDetailsDto packageBasicDetailsDto) {
        return FIXED_INSURANCE_AMOUNT_PER_PERSON * bookingRequest.getParticipants().size();
    }

    @Override
    public String generateUniqueBookingInvoiceReference(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }

        String timeCode = Long.toString(System.currentTimeMillis(), 36).toUpperCase();
        return "INV" + timeCode + bookingId;
    }

    @Override
    public Double calculateTotalAccomodationAmountPerPerson(List<PackageDayAccommodationPriceDto> packageDayAccommodationPriceDto) {
        Double totalAmount = 0.0;
        for (PackageDayAccommodationPriceDto p: packageDayAccommodationPriceDto){
            double price = p.getPrice() * (100.0 + p.getServiceCharge()) / 100;
            double discount = price * p.getDiscount() / 100;
            double tax = price * p.getTax() / 100;
            double extraCharge = p.getExtraCharge();
            double transportPrice = p.getTransportPrice();
            double total = price - discount + tax + extraCharge + transportPrice;
            totalAmount += total;
        }
        return totalAmount;
    }

    @Override
    public Double calculateTotalActivityAmountPerPerson(List<PackageActivityPriceDto> packageActivityPriceDto) {
        Double totalAmount = 0.0;
        for(PackageActivityPriceDto p : packageActivityPriceDto){
            totalAmount += p.getPriceForeigners();
        }
        return totalAmount;
    }

    @Override
    public Double calculateTotalDestinationExtraAmountPerPerson(List<PackageDestinationExtraPriceDto> packageDestinationExtraPriceDto) {
        Double totalAmount = 0.0;
        for(PackageDestinationExtraPriceDto p : packageDestinationExtraPriceDto){
            totalAmount += p.getExtraPrice();
        }
        return totalAmount;
    }

    @Override
    public PrintReceiptForBookingResponse createReceiptForBooking(
            BookingBasicDetailsDto bookingBasicDetailsDto,
            List<BookingActivityDto> bookingActivityDtos,
            List<BookingParticipantDto> bookingParticipantDtos,
            List<PackageDestinationExtraPriceDto> packageDestinationExtraPriceDto,
            List<PackageDayAccommodationPriceDto> packageDayAccommodationPriceDto) {

        // Map participants
        List<PrintReceiptForBookingResponse.ParticipentDetails> participantDetails = bookingParticipantDtos.stream()
                .map(p -> PrintReceiptForBookingResponse.ParticipentDetails.builder()
                        .bookingId(p.getBookingId())
                        .firstName(p.getFirstName())
                        .lastName(p.getLastName())
                        .dateOfBirth(p.getDateOfBirth())
                        .gender(p.getGender())
                        .passportNumber(p.getPassportNumber())
                        .nationality(p.getNationality())
                        .email(p.getEmail())
                        .mobileNumber(p.getMobileNumber())
                        .medicalConditions(p.getMedicalConditions())
                        .allergies(p.getAllergies())
                        .build())
                .toList();

        // Map activities
        List<PrintReceiptForBookingResponse.ActivityDetails> activityDetailsList = bookingActivityDtos.stream()
                .map(a -> PrintReceiptForBookingResponse.ActivityDetails.builder()
                        .activityName(a.getName())
                        .activityDescription(a.getDescription())
                        .numberOfParticipants(a.getNumberOfParticipants())
                        .pricePerPerson(a.getPricePerPerson())
                        .totalPrice(a.getTotalPrice())
                        .build())
                .toList();

        // Map destinations
        List<PrintReceiptForBookingResponse.DestiantionDetails> destiantionDetails = packageDestinationExtraPriceDto.stream()
                .map(d -> PrintReceiptForBookingResponse.DestiantionDetails.builder()
                        .extraPrice(d.getExtraPrice())
                        .extraPriceNote(d.getExtraPriceNote())
                        .destinationName(d.getDestinationName())
                        .destinationDescription(d.getDestinationDescription())
                        .build())
                .toList();

        // Map accommodations
        List<PrintReceiptForBookingResponse.AccommodationDetails> accommodationDetailsList = packageDayAccommodationPriceDto.stream()
                .map(a -> PrintReceiptForBookingResponse.AccommodationDetails.builder()
                        .dayNumber(a.getDayNumber())
                        .hotelName(a.getHotelName())
                        .transportPrice(a.getTransportPrice())
                        .price(a.getPrice())
                        .discount(a.getDiscount())
                        .serviceCharge(a.getServiceCharge())
                        .tax(a.getTax())
                        .extraCharge(a.getExtraCharge())
                        .extraChargeNote(a.getExtraChargeNote())
                        .build())
                .toList();

        // Build the final response
        return PrintReceiptForBookingResponse.builder()
                .bookingId(bookingBasicDetailsDto.getBookingId())
                .bookingReference(bookingBasicDetailsDto.getBookingReference())
                .invoiceNumber(bookingBasicDetailsDto.getInvoiceNumber())
                .invoiceDate(bookingBasicDetailsDto.getInvoiceDate())
                .dueDate(bookingBasicDetailsDto.getDueDate())
                .subtotal(bookingBasicDetailsDto.getSubtotal())
                .taxAmount(bookingBasicDetailsDto.getTaxAmount())
                .discountAmount(bookingBasicDetailsDto.getDiscountAmount())
                .insuranceAmount(bookingBasicDetailsDto.getInsuranceAmount())
                .packagePrice(bookingBasicDetailsDto.getPackagePrice())
                .totalAmount(bookingBasicDetailsDto.getTotalAmount())
                .amountPaid(bookingBasicDetailsDto.getAmountPaid())
                .balanceDue(bookingBasicDetailsDto.getBalanceDue())
                .billingFullName(bookingBasicDetailsDto.getBillingFullName())
                .billingAddress(bookingBasicDetailsDto.getBillingAddress())
                .billingEmail(bookingBasicDetailsDto.getBillingEmail())
                .billingPhone(bookingBasicDetailsDto.getBillingPhone())
                .packageName(bookingBasicDetailsDto.getPackageName())
                .packageScheduleId(bookingBasicDetailsDto.getPackageScheduleId())
                .assumeStartDate(bookingBasicDetailsDto.getAssumeStartDate())
                .assumeEndDate(bookingBasicDetailsDto.getAssumeEndDate())
                .tourName(bookingBasicDetailsDto.getTourName())
                .tourDescription(bookingBasicDetailsDto.getTourDescription())
                .finalAmount(bookingBasicDetailsDto.getFinalAmount())
                .bookingDate(bookingBasicDetailsDto.getBookingDate())
                .bookingStatus(bookingBasicDetailsDto.getBookingStatus())
                .participentDetails(participantDetails)
                .activityDetailsList(activityDetailsList)
                .destiantionDetails(destiantionDetails)
                .accommodationDetailsList(accommodationDetailsList)
                .build();
    }


}
