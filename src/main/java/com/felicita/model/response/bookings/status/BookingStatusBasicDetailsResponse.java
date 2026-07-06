package com.felicita.model.response.bookings.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingStatusBasicDetailsResponse {

    private Long statusId;
    private String statusName;
    private String description;
    private String status;
}