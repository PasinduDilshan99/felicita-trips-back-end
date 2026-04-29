package com.felicita.repository;

import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.request.ReadNotificationInsertRequest;
import com.felicita.model.response.AllCategoriesResponse;
import com.felicita.model.response.NotificationResponse;
import com.felicita.model.response.UnReadNotificationCountResponse;

import java.util.List;

public interface CommonRepository {
    List<AllCategoriesResponse.ActivityCategory> getAllActivityCategories();

    List<AllCategoriesResponse.DestinationCategory> getAllDestinationCategories();

    List<AllCategoriesResponse.TourCategory> getAllTourCategories();

    List<AllCategoriesResponse.PackageCategory> getAllPackageCategories();

    List<AllCategoriesResponse.TourType> getAllTourTypes();

    List<AllCategoriesResponse.Seasons> getAllSeasons();

    List<SupervisorBasicDetailsDto> getSupervisorBasicDetailsByUserId(Long userId);

    Long createNotification(NotificationInsertRequestDto dto);

    void createNotificationRecipients(Long notificationId, List<Long> supervisorUserIds);

    List<NotificationResponse> getNotificationForLoggedUser(Long userId);

    void readNotification(ReadNotificationInsertRequest notificationInsertRequest, Long userId);

    UnReadNotificationCountResponse getAllUnReadNotifications(Long userId);

    void readAllUnreadNotifications(Long userId);

    List<String> getSupervisorEmailsWhichEnableNotificationForGiven(String name, List<Long> supervisorUserIds);
}
