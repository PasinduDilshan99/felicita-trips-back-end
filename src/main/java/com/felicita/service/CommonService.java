package com.felicita.service;


import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.request.ReadNotificationInsertRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.common.*;
import com.felicita.model.response.common.ActivityIdAndNameResponse;
import com.felicita.security.model.User;

import java.util.List;

public interface CommonService {
    
    Long getUserIdBySecurityContext();

    Long getUserIdBySecurityContextWithOutException();

    String generateRandomOtp();

    String getUserEmailBySecurityContext();

    CommonResponse<AllCategoriesResponse> getAllCategories();

    List<SupervisorBasicDetailsDto> getSupervisorBasicDetailsByUserId(Long userId);

    User getLoggedUser();

    Long createNotification(NotificationInsertRequestDto notificationInsertRequestDto);

    void createNotificationRecipients(Long notificationId, List<Long> supervisorUserIds);

    CommonResponse<List<NotificationResponse>> getNotificationForLoggedUser();

    CommonResponse<UpdateResponse> readNotification(ReadNotificationInsertRequest notificationInsertRequest);

    CommonResponse<UnReadNotificationCountResponse> getAllUnReadNotifications();

    CommonResponse<UpdateResponse> readAllUnreadNotifications();

    List<String> getSupervisorEmailsWhichEnableNotificationForGiven(String name, List<Long> supervisorUserIds);

    List<Long> extractSupervisorUserIds(List<SupervisorBasicDetailsDto> supervisorDetails);

    String createEmployeeUniqueEmployeeCode();

    List<ActivityIdAndNameResponse> getActivityIdAndNameResponses();

    List<DestinationIdAndNameResponse> getDestinationIdAndNameResponses();

    List<TourScheduleIdAndNameResponse> getTourScheduleIdAndNameResponses();

    List<PackageScheduleIdAndNameResponse> getPackageScheduleIdAndNameResponses();

    List<SeasonIdAndNameResponse> getSeasonIdAndNameResponses();

    List<TourIdAndNameResponse> getTourIdAndNameResponses();

    List<PackageIdAndNamesResponse> getPacakgeIdAndNameResponses();

    List<ActivityScheduleIdAndNameResponse> getActivityScheduleIdAndNames();

    String createBooingReference(Long userId);

    String createBookingInvoiceReference(Long bookingId,Long userId);

    List<BookingStatusIdAndNameResponse> getBookingStatusesIdAndNameResponses();

    List<EmployeeIdAndNameResponse> getTourAssignUserIdAndNameResponses();

    List<IdAndNameResponse> getCustomerIdAndNameResponses();

    List<IdAndNameResponse> getPacakgeIdAndNameResponsesByTourId(Long id);

    List<PackageIdScheduleIdAndScheduleNameResponse> getPacakgeScheduleIdAndNameResponsesByTourId(Long id);

    List<IdAndNameResponse> getGenderIdAndNameResponses();

    List<IdAndNameResponse> getCountryIdAndNameResponses();

    List<IdAndNameResponse> getStatusIdAndNameResponses();

    List<IdAndNameResponse> getHotelIdAndNameResponses();

    List<IdAndNameResponse> getVehicleIdAndRegisterNumberResponses();

    List<IdAndNameResponse> getActivityIdAndNameResponsesByTourId(Long id);

    List<ActivityIdScheduleIdAndScheduleNameResponse> getActivityScheduleIdAndNameResponsesByTourId(Long id);
}
