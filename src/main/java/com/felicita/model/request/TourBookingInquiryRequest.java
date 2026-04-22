package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourBookingInquiryRequest {
    private Long tourId;
    private String tourName;
    private Long packageId;
    private String packageName;
    private String name;
    private String email;
    private String contactNumber;
    private String country;
}
