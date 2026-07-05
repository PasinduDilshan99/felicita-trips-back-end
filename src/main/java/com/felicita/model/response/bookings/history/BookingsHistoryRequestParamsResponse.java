package com.felicita.model.response.bookings.history;

import com.felicita.model.response.common.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingsHistoryRequestParamsResponse {
    private List<String> bookingRefences;
    private List<BookingStatusIdAndNameResponse> bookingStatuses;
    private List<TourIdAndNameResponse> tours;
    private List<PackageIdAndNamesResponse> packages;
    private List<EmployeeIdAndNameResponse> assignedEmployees;

}
