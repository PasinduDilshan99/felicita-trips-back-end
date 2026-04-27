package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Long loggedUserId;
    private String notificationType;
    private String priority;
    private String title;
    private String message;
    private String actionUrl;
    private String actionText;
    private String icon;
    private String color;
    private Map<String, Object> metadata;
    private Boolean isArchived;
    private Boolean isDeleted;
    private Long assignedTo;
    private String assignedUsername;
    private String targetRole;
    private String sourceModule;
    private LocalDateTime expiresAt;
    private Long createdBy;
    private Boolean isRead;
    private LocalDateTime readAt;
}
