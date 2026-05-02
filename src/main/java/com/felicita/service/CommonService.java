package com.felicita.service;


import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.request.ReadNotificationInsertRequest;
import com.felicita.model.response.*;
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
}
