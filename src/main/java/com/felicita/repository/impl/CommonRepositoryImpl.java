package com.felicita.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.ReadNotificationInsertRequest;
import com.felicita.model.response.AllCategoriesResponse;
import com.felicita.model.response.NotificationResponse;
import com.felicita.model.response.TourForTerminateResponse;
import com.felicita.model.response.UnReadNotificationCountResponse;
import com.felicita.model.response.common.*;
import com.felicita.queries.CommonQueries;
import com.felicita.queries.TourQueries;
import com.felicita.repository.CommonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CommonRepositoryImpl implements CommonRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommonRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AllCategoriesResponse.ActivityCategory> getAllActivityCategories() {

        String GET_ALL_ACTIVITY_CATEGORIES = CommonQueries.GET_ALL_ACTIVITY_CATEGORIES;

        try {

            Map<Long, AllCategoriesResponse.ActivityCategory> categoryMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_ALL_ACTIVITY_CATEGORIES, rs -> {

                Long categoryId = rs.getLong("activity_category_id");

                AllCategoriesResponse.ActivityCategory category =
                        categoryMap.get(categoryId);

                if (category == null) {

                    category = AllCategoriesResponse.ActivityCategory.builder()
                            .activityCategoryId(categoryId)
                            .activityCategoryName(rs.getString("activity_category_name"))
                            .activityCategoryDescription(rs.getString("activity_category_description"))
                            .activityCategoryColor(rs.getString("activity_category_color"))
                            .activityCategoryHoverColor(rs.getString("activity_category_hover_color"))
                            .activityCategoryImages(new ArrayList<>())
                            .build();

                    categoryMap.put(categoryId, category);
                }

                Long imageId = rs.getLong("image_id");

                if (!rs.wasNull()) {
                    AllCategoriesResponse.Images image =
                            AllCategoriesResponse.Images.builder()
                                    .imageId(imageId)
                                    .imageName(rs.getString("image_name"))
                                    .imageDescription(rs.getString("image_description"))
                                    .imageUrl(rs.getString("image_url"))
                                    .build();

                    category.getActivityCategoryImages().add(image);
                }
            });

            return new ArrayList<>(categoryMap.values());

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch activity categories: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity categories");
        }
    }

    @Override
    public List<AllCategoriesResponse.DestinationCategory> getAllDestinationCategories() {

        try {

            Map<Long, AllCategoriesResponse.DestinationCategory> categoryMap = new LinkedHashMap<>();

            jdbcTemplate.query(CommonQueries.GET_ALL_DESTINATION_CATEGORIES, rs -> {

                Long id = rs.getLong("destination_category_id");

                AllCategoriesResponse.DestinationCategory category = categoryMap.get(id);

                if (category == null) {
                    category = AllCategoriesResponse.DestinationCategory.builder()
                            .destinationCategoryId(id)
                            .destinationCategoryName(rs.getString("destination_category_name"))
                            .destinationCategoryDescription(rs.getString("destination_category_description"))
                            .destinationCategoryColor(rs.getString("destination_category_color"))
                            .destinationCategoryHoverColor(rs.getString("destination_category_hover_color"))
                            .destinationCategoryImages(new ArrayList<>())
                            .build();

                    categoryMap.put(id, category);
                }

                Long imageId = rs.getLong("image_id");

                if (!rs.wasNull()) {
                    AllCategoriesResponse.Images image =
                            AllCategoriesResponse.Images.builder()
                                    .imageId(imageId)
                                    .imageName(rs.getString("image_name"))
                                    .imageDescription(rs.getString("image_description"))
                                    .imageUrl(rs.getString("image_url"))
                                    .build();

                    category.getDestinationCategoryImages().add(image);
                }
            });

            return new ArrayList<>(categoryMap.values());

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch destination categories: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination categories");
        }
    }

    @Override
    public List<AllCategoriesResponse.PackageCategory> getAllPackageCategories() {

        try {

            Map<Long, AllCategoriesResponse.PackageCategory> categoryMap = new LinkedHashMap<>();

            jdbcTemplate.query(CommonQueries.GET_ALL_PACKAGE_TYPES, rs -> {

                Long id = rs.getLong("package_category_id");

                AllCategoriesResponse.PackageCategory category = categoryMap.get(id);

                if (category == null) {
                    category = AllCategoriesResponse.PackageCategory.builder()
                            .packageCategoryId(id)
                            .packageCategoryName(rs.getString("package_category_name"))
                            .packageCategoryDescription(rs.getString("package_category_description"))
                            .packageCategoryColor(rs.getString("package_category_color"))
                            .packageCategoryHoverColor(rs.getString("package_category_hover_color"))
                            .packageCategoryImages(new ArrayList<>())
                            .build();

                    categoryMap.put(id, category);
                }

                Long imageId = rs.getLong("image_id");

                if (!rs.wasNull()) {
                    AllCategoriesResponse.Images image =
                            AllCategoriesResponse.Images.builder()
                                    .imageId(imageId)
                                    .imageName(rs.getString("image_name"))
                                    .imageDescription(rs.getString("image_description"))
                                    .imageUrl(rs.getString("image_url"))
                                    .build();

                    category.getPackageCategoryImages().add(image);
                }
            });

            return new ArrayList<>(categoryMap.values());

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch package categories: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package categories");
        }
    }

    @Override
    public List<AllCategoriesResponse.TourCategory> getAllTourCategories() {

        try {

            return jdbcTemplate.query(
                    CommonQueries.GET_ALL_TOUR_CATEGORIES,
                    (rs, rowNum) -> AllCategoriesResponse.TourCategory.builder()
                            .tourCategoryId(rs.getLong("tour_category_id"))
                            .tourCategoryName(rs.getString("tour_category_name"))
                            .tourCategoryDescription(rs.getString("tour_category_description"))
                            .tourCategoryColor(rs.getString("tour_category_color"))
                            .tourCategoryHoverColor(rs.getString("tour_category_hover_color"))
                            .build()
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch tour categories: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour categories");
        }
    }

    @Override
    public List<AllCategoriesResponse.TourType> getAllTourTypes() {

        try {

            return jdbcTemplate.query(
                    CommonQueries.GET_ALL_TOUR_TYPES,
                    (rs, rowNum) -> AllCategoriesResponse.TourType.builder()
                            .tourTypeId(rs.getLong("tour_type_id"))
                            .tourTypeName(rs.getString("tour_type_name"))
                            .tourTypeDescription(rs.getString("tour_type_description"))
                            .tourTypeColor(rs.getString("tour_type_color"))
                            .tourTypeHoverColor(rs.getString("tour_type_hover_color"))
                            .build()
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch tour types: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour types");
        }
    }

    @Override
    public List<AllCategoriesResponse.Seasons> getAllSeasons() {
        try {
            Map<Long, AllCategoriesResponse.Seasons> seasonsMap = new LinkedHashMap<>();

            jdbcTemplate.query(CommonQueries.GET_ALL_SEASONS, rs -> {
                Long seasonId = rs.getLong("season_id");
                AllCategoriesResponse.Seasons season = seasonsMap.get(seasonId);
                if (season == null) {
                    season = AllCategoriesResponse.Seasons.builder()
                            .seasonId(seasonId)
                            .seasonName(rs.getString("season_name"))
                            .seasonStandardName(rs.getString("season_standard_name"))
                            .seasonDescription(rs.getString("season_description"))
                            .startMonth(rs.getInt("start_month"))
                            .endMonth(rs.getInt("end_month"))
                            .isPeak(rs.getBoolean("is_peak"))
                            .seasonImages(new ArrayList<>())
                            .build();

                    seasonsMap.put(seasonId, season);
                }

                Long imageId = rs.getLong("image_id");

                if (!rs.wasNull()) {
                    AllCategoriesResponse.Images image =
                            AllCategoriesResponse.Images.builder()
                                    .imageId(imageId)
                                    .imageUrl(rs.getString("image_url"))
                                    .imageName(rs.getString("image_name"))
                                    .imageDescription(rs.getString("image_description"))
                                    .build();

                    season.getSeasonImages().add(image);
                }
            });

            return new ArrayList<>(seasonsMap.values());

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch seasons: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch seasons");
        }
    }

    @Override
    public List<SupervisorBasicDetailsDto> getSupervisorBasicDetailsByUserId(Long userId) {

        try {

            return jdbcTemplate.query(
                    CommonQueries.GET_SUPERVISOR_EMAILS_BY_USER_ID,
                    new Object[]{userId},
                    (rs, rowNum) ->
                            SupervisorBasicDetailsDto.builder()
                                    .userId(rs.getLong("user_id"))
                                    .username(rs.getString("username"))
                                    .email(rs.getString("email"))
                                    .build()
            );

        } catch (Exception e) {

            LOGGER.error("Failed to get supervisor details", e);

            throw new InternalServerErrorExceptionHandler(
                    "Failed to get supervisor details"
            );
        }
    }

    @Override
    public Long createNotification(NotificationInsertRequestDto dto) {

        LOGGER.info(dto.toString());

        Long typeId = jdbcTemplate.queryForObject(
                "SELECT id FROM notification_types WHERE name = ?",
                Long.class,
                dto.getNotificationType()
        );

        Long priorityId = jdbcTemplate.queryForObject(
                "SELECT id FROM notification_priority WHERE name = ?",
                Long.class,
                dto.getPriority()
        );

        Long roleId = null;
        if (dto.getTargetRole() != null) {
            roleId = jdbcTemplate.queryForObject(
                    "SELECT id FROM privileges WHERE name = ?",
                    Long.class,
                    dto.getTargetRole()
            );
        }

        final Long finalRoleId = roleId;

        String sql = """
                    INSERT INTO notifications (
                        notification_type_id,
                        priority_id,
                        title,
                        message,
                        action_url,
                        action_text,
                        icon,
                        color,
                        metadata,
                        is_archived,
                        is_deleted,
                        assigned_to,
                        target_role_id,
                        source_module,
                        expires_at,
                        created_by
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, typeId);
            ps.setLong(2, priorityId);
            ps.setString(3, dto.getTitle());
            ps.setString(4, dto.getMessage());
            ps.setString(5, dto.getActionUrl());
            ps.setString(6, dto.getActionText());
            ps.setString(7, dto.getIcon());
            ps.setString(8, dto.getColor());

            try {
                ps.setString(9, dto.getMetadata() != null ? new ObjectMapper().writeValueAsString(dto.getMetadata()) : null);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            ps.setBoolean(10, dto.getIsArchived() != null && dto.getIsArchived());
            ps.setBoolean(11, dto.getIsDeleted() != null && dto.getIsDeleted());

            if (dto.getAssignedTo() != null) {
                ps.setLong(12, dto.getAssignedTo());
            } else {
                ps.setNull(12, Types.INTEGER);
            }

            if (finalRoleId != null) {
                ps.setLong(13, finalRoleId);
            } else {
                ps.setNull(13, Types.INTEGER);
            }

            ps.setString(14, dto.getSourceModule());

            if (dto.getExpiresAt() != null) {
                ps.setTimestamp(15, Timestamp.valueOf(dto.getExpiresAt()));
            } else {
                ps.setNull(15, Types.TIMESTAMP);
            }

            ps.setLong(16, dto.getCreatedBy());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }

    @Override
    public void createNotificationRecipients(Long notificationId, List<Long> supervisorUserIds) {

        String sql = """
                    INSERT INTO notification_recipients (
                        notification_id,
                        user_id,
                        is_read,
                        read_at
                    )
                    VALUES (?, ?, 0, NULL)
                """;

        jdbcTemplate.batchUpdate(sql, supervisorUserIds, supervisorUserIds.size(),
                (ps, userId) -> {
                    ps.setLong(1, notificationId);
                    ps.setLong(2, userId);
                }
        );
    }

    @Override
    public List<NotificationResponse> getNotificationForLoggedUser(Long userId) {

        String sql = """
                    SELECT 
                        n.id AS notification_id,
                        n.notification_type_id,
                        nt.name AS notification_type,
                        np.name AS priority,
                        n.title,
                        n.message,
                        n.action_url,
                        n.action_text,
                        n.icon,
                        n.color,
                        n.metadata,
                        n.is_archived,
                        n.is_deleted,
                        n.assigned_to,
                        u.username,
                        rt.name AS target_role,
                        n.source_module,
                        n.expires_at,
                        n.created_by,
                        nr.is_read,
                        nr.read_at
                    FROM notifications n
                    INNER JOIN notification_recipients nr 
                        ON nr.notification_id = n.id
                    INNER JOIN notification_types nt 
                        ON nt.id = n.notification_type_id
                    INNER JOIN notification_priority np 
                        ON np.id = n.priority_id
                    LEFT JOIN roles rt 
                        ON rt.id = n.target_role_id
                    LEFT JOIn user u
                        ON u.user_id = n.assigned_to
                    WHERE nr.user_id = ?
                      AND n.is_deleted = 0
                    ORDER BY n.created_at DESC
                """;

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {

            Map<String, Object> metadata = null;
            try {
                String json = rs.getString("metadata");
                if (json != null) {
                    metadata = new ObjectMapper().readValue(json, Map.class);
                }
            } catch (Exception e) {
                metadata = null;
            }

            return NotificationResponse.builder()
                    .loggedUserId(userId)
                    .notificationType(rs.getString("notification_type"))
                    .priority(rs.getString("priority"))
                    .title(rs.getString("title"))
                    .message(rs.getString("message"))
                    .actionUrl(rs.getString("action_url"))
                    .actionText(rs.getString("action_text"))
                    .icon(rs.getString("icon"))
                    .color(rs.getString("color"))
                    .metadata(metadata)
                    .isArchived(rs.getBoolean("is_archived"))
                    .isDeleted(rs.getBoolean("is_deleted"))
                    .assignedTo(rs.getObject("assigned_to") != null ? rs.getLong("assigned_to") : null)
                    .assignedUsername(rs.getString("username"))
                    .targetRole(rs.getString("target_role"))
                    .sourceModule(rs.getString("source_module"))
                    .expiresAt(rs.getTimestamp("expires_at") != null
                            ? rs.getTimestamp("expires_at").toLocalDateTime()
                            : null)
                    .createdBy(rs.getObject("created_by") != null ? rs.getLong("created_by") : null)
                    .isRead(rs.getBoolean("is_read"))
                    .readAt(rs.getTimestamp("read_at") != null
                            ? rs.getTimestamp("read_at").toLocalDateTime()
                            : null)
                    .build();
        });
    }

    @Override
    public void readNotification(ReadNotificationInsertRequest request, Long userId) {

        String sql = """
        UPDATE notification_recipients
        SET is_read = 1,
            read_at = NOW()
        WHERE notification_id = ?
          AND user_id = ?
    """;

        jdbcTemplate.update(sql,
                request.getNotificationId(),
                userId
        );
    }

    @Override
    public UnReadNotificationCountResponse getAllUnReadNotifications(Long userId) {

        String sql = """
        SELECT COUNT(*) 
        FROM notification_recipients
        WHERE user_id = ?
          AND is_read = 0
    """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        return UnReadNotificationCountResponse.builder()
                .count(count)
                .build();
    }

    @Override
    public void readAllUnreadNotifications(Long userId) {

        String sql = """
        UPDATE notification_recipients
        SET is_read = 1,
            read_at = NOW()
        WHERE user_id = ?
          AND is_read = 0
    """;

        jdbcTemplate.update(sql, userId);
    }

    @Override
    public List<String> getSupervisorEmailsWhichEnableNotificationForGiven(
            String name,
            List<Long> supervisorUserIds) {

        if (supervisorUserIds == null || supervisorUserIds.isEmpty()) {
            return List.of();
        }

        String inSql = supervisorUserIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql = """
        SELECT DISTINCT u.email
        FROM user u
        INNER JOIN notification_settings ns 
            ON ns.user_id = u.user_id
        INNER JOIN notification_types nt 
            ON nt.id = ns.notification_type_id
        WHERE nt.name = ?
          AND ns.is_email_enabled = 1
          AND u.user_id IN (%s)
          AND u.email IS NOT NULL
    """.formatted(inSql);

        List<Object> params = new ArrayList<>();
        params.add(name);
        params.addAll(supervisorUserIds);

        return jdbcTemplate.queryForList(sql, params.toArray(), String.class);
    }

    @Override
    public boolean existsByEmployeeCode(String employeeCode) {
        try {
            String query = """
                SELECT COUNT(*)
                FROM employees
                WHERE employee_code = ?
                """;

            Integer count = jdbcTemplate.queryForObject(
                    query,
                    Integer.class,
                    employeeCode
            );
            return count != null && count > 0;

        } catch (Exception ex) {
            LOGGER.error("Error while checking employee code existence", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to check employee code existence"
            );
        }
    }

    @Override
    public List<ActivityIdAndNameResponse> getActivityIdAndNameResponses() {

        try {

            return jdbcTemplate.query(
                    CommonQueries.GET_ACTIVITY_ID_AND_NAME,
                    (rs, rowNum) -> ActivityIdAndNameResponse.builder()
                            .activityId(rs.getLong("activity_id"))
                            .activityName(rs.getString("activity_name"))
                            .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching activity id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activity id and names from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching activity id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching activity id and names"
            );
        }
    }

    @Override
    public List<TourIdAndNameResponse> getTourIdAndNameResponses() {

        String sql = """
        SELECT tour_id, name
        FROM tour
        WHERE status = 1
        ORDER BY name ASC
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                TourIdAndNameResponse.builder()
                        .tourId(rs.getLong("tour_id"))
                        .tourName(rs.getString("name"))
                        .build()
        );
    }
    @Override
    public List<DestinationIdAndNameResponse> getDestinationIdAndNameResponses() {

        try {

            return jdbcTemplate.query(
                    CommonQueries.GET_DESTINATION_ID_AND_NAME,
                    (rs, rowNum) -> DestinationIdAndNameResponse.builder()
                            .destinationId(rs.getLong("destination_id"))
                            .destinationName(rs.getString("destination_name"))
                            .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching destination id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch destination id and names from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching destination id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching destination id and names"
            );
        }
    }

    @Override
    public List<TourScheduleIdAndNameResponse> getTourScheduleIdAndNameResponses() {

        try {

            return jdbcTemplate.query(
                    CommonQueries.GET_TOUR_SCHEDULE_ID_AND_NAME,
                    (rs, rowNum) -> TourScheduleIdAndNameResponse.builder()
                            .tourScheduleId(rs.getLong("tour_schedule_id"))
                            .tourScheduleName(rs.getString("tour_schedule_name"))
                            .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching tour schedule id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch tour schedule id and names from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching tour schedule id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching tour schedule id and names"
            );
        }
    }

    @Override
    public List<PackageScheduleIdAndNameResponse> getPackageScheduleIdAndNameResponses() {

        try {

            return jdbcTemplate.query(
                    CommonQueries.GET_PACKAGE_SCHEDULE_ID_AND_NAME,
                    (rs, rowNum) -> PackageScheduleIdAndNameResponse.builder()
                            .packageScheduleId(rs.getLong("package_schedule_id"))
                            .packageScheduleName(rs.getString("package_schedule_name"))
                            .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching package schedule id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch package schedule id and names from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching package schedule id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching package schedule id and names"
            );
        }
    }

    @Override
    public List<SeasonIdAndNameResponse> getSeasonIdAndNameResponses() {

        try {

            return jdbcTemplate.query(
                    CommonQueries.GET_SEASON_ID_AND_NAME,
                    (rs, rowNum) -> SeasonIdAndNameResponse.builder()
                            .seasonId(rs.getLong("season_id"))
                            .seasonName(rs.getString("season"))
                            .build()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching season id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch season id and names from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching season id and names: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching season id and names"
            );
        }
    }


}
