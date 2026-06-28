package com.felicita.model.response.bookings.unassign;

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
public class UnassignBookingsRequestParamsResponse {

    private List<String> bookingRefences;
    private List<BookingStatusIdAndNameResponse> bookingStatuses;
    private List<TourIdAndNameResponse> tours;
    private List<PackageIdAndNamesResponse> packages;
    private List<PackageScheduleIdAndNameResponse> packageSchedules;
    private List<EmployeeIdAndNameResponse> assignedUsers;

}