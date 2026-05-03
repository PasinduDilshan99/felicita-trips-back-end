package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NotificationInsertRequestDto {

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
    private String targetRole;
    private String sourceModule;
    private LocalDateTime expiresAt;
    private Long createdBy;
}