package com.felicita.model.response.bookings.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingStatusDetailsResponse {


    private Long statusId;
    private String statusName;
    private String description;
    private String status;

    private Integer totalBookingsUsingThisStatus;
    private Integer activeBookingsCount;
    private Integer completedBookingsCount;
    private Integer cancelledBookingsCount;

    private LocalDateTime createdAt;
    private Long createdBy;

    private LocalDateTime updatedAt;
    private Long updatedBy;

    private LocalDateTime terminatedAt;
    private Long terminatedBy;

}