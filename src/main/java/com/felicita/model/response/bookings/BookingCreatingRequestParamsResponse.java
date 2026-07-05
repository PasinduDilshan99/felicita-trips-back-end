package com.felicita.model.response.bookings;

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
public class BookingCreatingRequestParamsResponse {
    private List<IdAndNameResponse> customerList;
    private List<TourIdAndNameResponse> tourList;
    private List<IdAndNameResponse> packageList;
    private List<PackageIdScheduleIdAndScheduleNameResponse> packageScheduleList;
    private List<BookingStatusIdAndNameResponse> bookingStatuses;
    private List<EmployeeIdAndNameResponse> assignEmployeeList;
    private List<IdAndNameResponse> genders;
    private List<IdAndNameResponse> countries;
    private List<IdAndNameResponse> statusList;
    private List<IdAndNameResponse> hotelList;
    private List<String> roomTypes;
    private List<IdAndNameResponse> vehicleList;
    private List<String> transportTypes;
    private List<IdAndNameResponse> activityList;
    private List<ActivityIdScheduleIdAndScheduleNameResponse> activityScheduleList;
    private List<String> documentTypes;
    private List<String> mimeTypes;
    private List<String> insuranceProviders;
    private List<String> coverageType;
    private List<String> includedMeals;
    private List<String> noteTypes;
    private List<String> priceBreakDownType;

}
