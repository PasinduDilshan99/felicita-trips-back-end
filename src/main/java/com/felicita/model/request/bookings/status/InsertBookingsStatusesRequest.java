package com.felicita.model.request.bookings.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertBookingsStatusesRequest {

    private String statusName;
    private String description;
    private String status;
}