package com.felicita.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felicita.exception.*;
import com.felicita.model.dto.*;
import com.felicita.model.dto.activity.schedule.ActivityScheduleBasicDetailsDTO;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.*;
import com.felicita.model.request.activity.category.ActivityCategoryImageRequest;
import com.felicita.model.request.activity.category.ActivityCategoryImageUpdateRequest;
import com.felicita.model.request.activity.category.ActivityCategoryInsertRequest;
import com.felicita.model.request.activity.category.ActivityCategoryUpdateRequest;
import com.felicita.model.request.activity.schedule.ActivityScheduleUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.activity.category.ActivityCategoryDetailsResponse;
import com.felicita.model.response.statistics.ActivityCategoriesStatisticsResponse;
import com.felicita.model.response.statistics.ActivityScheduleStatisticsResponse;
import com.felicita.queries.ActivitiesQueries;
import com.felicita.repository.ActivitiesRepository;
import com.felicita.repository.StatusRepository;
import com.felicita.util.Sortings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ActivitiesRepositoryImpl implements ActivitiesRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiesRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final StatusRepository statusRepository;

    @Autowired
    public ActivitiesRepositoryImpl(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<ActivityResponseDto> getAllActivities() {
        String GET_ALL_ACTIVITIES = ActivitiesQueries.GET_ALL_ACTIVITIES;

        return jdbcTemplate.query(GET_ALL_ACTIVITIES, new ActivityRowMapper());
    }

    @Override
    public List<ActivityCategoryResponseDto> getAllActivityCategories() {
        String GET_ALL_ACTIVITY_CATEGORIES = ActivitiesQueries.GET_ALL_ACTIVITY_CATEGORIES;

        try {
            LOGGER.info("Executing query to fetch all activity categories.");

            // Use a LinkedHashMap to maintain insertion order and group images under categories
            Map<Integer, ActivityCategoryResponseDto> categoryMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_ALL_ACTIVITY_CATEGORIES, (rs) -> {
                int categoryId = rs.getInt("category_id");

                ActivityCategoryResponseDto category = categoryMap.get(categoryId);
                if (category == null) {
                    category = new ActivityCategoryResponseDto();
                    category.setCategoryId(categoryId);
                    category.setCategoryName(rs.getString("category_name"));
                    category.setCategoryDescription(rs.getString("category_description"));
                    category.setCategoryStatus(rs.getString("category_status"));
                    category.setCreatedAt(rs.getTimestamp("category_created_at"));
                    category.setCreatedBy(rs.getObject("category_created_by", Integer.class));
                    category.setUpdatedAt(rs.getTimestamp("category_updated_at"));
                    category.setUpdatedBy(rs.getObject("category_updated_by", Integer.class));
                    category.setTerminatedAt(rs.getTimestamp("category_terminated_at"));
                    category.setTerminatedBy(rs.getObject("category_terminated_by", Integer.class));
                    category.setNumberOfActivities(rs.getInt("activities_count"));
                    category.setColor(rs.getString("color"));
                    category.setHoverColor(rs.getString("hover_color"));
                    category.setImages(new ArrayList<>());

                    categoryMap.put(categoryId, category);
                }

                int imageId = rs.getInt("image_id");
                if (!rs.wasNull()) {
                    ActivityCategoryImageResponseDto image = new ActivityCategoryImageResponseDto();
                    image.setImageId(imageId);
                    image.setImageName(rs.getString("image_name"));
                    image.setImageDescription(rs.getString("image_description"));
                    image.setImageUrl(rs.getString("image_url"));
                    image.setImageStatus(rs.getString("image_status"));
                    image.setCreatedAt(rs.getTimestamp("image_created_at"));
                    image.setCreatedBy(rs.getObject("image_created_by", Integer.class));
                    image.setUpdatedAt(rs.getTimestamp("image_updated_at"));
                    image.setUpdatedBy(rs.getObject("image_updated_by", Integer.class));
                    image.setTerminatedAt(rs.getTimestamp("image_terminated_at"));
                    image.setTerminatedBy(rs.getObject("image_terminated_by", Integer.class));

                    category.getImages().add(image);
                }
            });

            return new ArrayList<>(categoryMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity categories: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity categories from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity categories: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity categories");
        }
    }

    @Override
    public List<ActivityReviewDetailsResponse> getActivityReviewDetailsById(Long activityId) {
        String GET_ACTIVITY_REVIEW_DETAILS_BY_ID = ActivitiesQueries.GET_ACTIVITY_REVIEW_DETAILS_BY_ID;

        try {
            LOGGER.info("Executing query to fetch activity review details for activityId : {}", activityId);

            // Map to aggregate nested data
            Map<Long, ActivityReviewDetailsResponse> reviewMap = new LinkedHashMap<>();

            jdbcTemplate.query(
                    GET_ACTIVITY_REVIEW_DETAILS_BY_ID,
                    new Object[]{activityId},
                    rs -> {
                        Long reviewId = rs.getLong("review_id");
                        ActivityReviewDetailsResponse review = reviewMap.get(reviewId);

                        if (review == null) {
                            review = ActivityReviewDetailsResponse.builder()
                                    .reviewId(reviewId)
                                    .activityScheduleId(rs.getLong("activity_schedule_id"))
                                    .activityId(rs.getLong("activity_id"))
                                    .activityName(rs.getString("activity_name"))
                                    .reviewName(rs.getString("review_name"))
                                    .review(rs.getString("review"))
                                    .rating(rs.getBigDecimal("rating"))
                                    .description(rs.getString("description"))
                                    .reviewStatus(rs.getString("review_status"))
                                    .numberOfParticipate(rs.getInt("number_of_participate"))
                                    .reviewCreatedBy(rs.getLong("review_created_by"))
                                    .reviewCreatedAt(rs.getTimestamp("review_created_at").toLocalDateTime())
                                    .reviewUpdatedBy(rs.getObject("review_updated_by") != null ? rs.getLong("review_updated_by") : null)
                                    .reviewUpdatedAt(rs.getTimestamp("review_updated_at") != null ? rs.getTimestamp("review_updated_at").toLocalDateTime() : null)
                                    .images(new ArrayList<>())
                                    .reactions(new ArrayList<>())
                                    .comments(new ArrayList<>())
                                    .build();
                            reviewMap.put(reviewId, review);
                        }

                        // Images
                        Long imageId = rs.getObject("image_id", Long.class);
                        if (imageId != null && review.getImages().stream().noneMatch(i -> i.getImageId().equals(imageId))) {
                            review.getImages().add(ActivityReviewDetailsResponse.ReviewImage.builder()
                                    .imageId(imageId)
                                    .imageName(rs.getString("image_name"))
                                    .imageDescription(rs.getString("image_description"))
                                    .imageUrl(rs.getString("image_url"))
                                    .imageStatus(rs.getString("image_status"))
                                    .imageCreatedBy(rs.getLong("image_created_by"))
                                    .imageCreatedAt(rs.getTimestamp("image_created_at").toLocalDateTime())
                                    .build());
                        }

                        // Review Reactions
                        Long reviewReactionId = rs.getObject("review_reaction_id", Long.class);
                        if (reviewReactionId != null && review.getReactions().stream().noneMatch(r -> r.getReviewReactionId().equals(reviewReactionId))) {
                            review.getReactions().add(ActivityReviewDetailsResponse.ReviewReaction.builder()
                                    .reviewReactionId(reviewReactionId)
                                    .reactionReviewId(rs.getLong("reaction_review_id"))
                                    .userId(rs.getLong("reaction_user_id"))
                                    .userName(rs.getString("reaction_user_name"))
                                    .reactionType(rs.getString("reaction_type"))
                                    .reviewReactionStatus(rs.getString("review_reaction_status"))
                                    .reactionCreatedAt(rs.getTimestamp("reaction_created_at").toLocalDateTime())
                                    .build());
                        }

                        // Comments
                        Long commentId = rs.getObject("comment_id", Long.class);
                        ActivityReviewDetailsResponse.Comment comment = null;
                        if (commentId != null) {
                            comment = review.getComments().stream()
                                    .filter(c -> c.getCommentId().equals(commentId))
                                    .findFirst()
                                    .orElse(null);
                            if (comment == null) {
                                comment = ActivityReviewDetailsResponse.Comment.builder()
                                        .commentId(commentId)
                                        .commentReviewId(rs.getLong("comment_review_id"))
                                        .userId(rs.getLong("comment_user_id"))
                                        .userName(rs.getString("comment_user_name"))
                                        .parentCommentId(rs.getObject("parent_comment_id") != null ? rs.getLong("parent_comment_id") : null)
                                        .comment(rs.getString("comment"))
                                        .commentStatus(rs.getString("comment_status"))
                                        .commentCreatedAt(rs.getTimestamp("comment_created_at").toLocalDateTime())
                                        .commentCreatedBy(rs.getLong("comment_created_by"))
                                        .commentReactions(new ArrayList<>())
                                        .build();
                                review.getComments().add(comment);
                            }
                        }

                        // Comment Reactions
                        if (comment != null) {
                            Long commentReactionId = rs.getObject("comment_reaction_id", Long.class);
                            if (commentReactionId != null && comment.getCommentReactions().stream().noneMatch(cr -> cr.getCommentReactionId().equals(commentReactionId))) {
                                comment.getCommentReactions().add(ActivityReviewDetailsResponse.Comment.CommentReaction.builder()
                                        .commentReactionId(commentReactionId)
                                        .commentReactionCommentId(rs.getLong("comment_reaction_comment_id"))
                                        .userId(rs.getLong("comment_reaction_user_id"))
                                        .userName(rs.getString("comment_reaction_user_name"))
                                        .commentReactionType(rs.getString("comment_reaction_type"))
                                        .commentReactionStatus(rs.getString("comment_reaction_status"))
                                        .commentReactionCreatedBy(rs.getLong("comment_reaction_created_by"))
                                        .commentReactionCreatedAt(rs.getTimestamp("comment_reaction_created_at").toLocalDateTime())
                                        .build());
                            }
                        }
                    }
            );

            return new ArrayList<>(reviewMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity reviews for activityId={}: {}", activityId, ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity reviews from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity reviews for activityId={}: {}", activityId, ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity reviews");
        }
    }


    @Override
    public List<ActivityReviewDetailsResponse> getAllActivityReviewDetails() {
        String GET_ACTIVITY_REVIEW_DETAILS = ActivitiesQueries.GET_ACTIVITY_REVIEW_DETAILS;

        try {
            LOGGER.info("Executing query to fetch all activity review details.");

            // Map to store reviews by reviewId for aggregating nested lists
            Map<Long, ActivityReviewDetailsResponse> reviewMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_ACTIVITY_REVIEW_DETAILS, rs -> {
                Long reviewId = rs.getLong("review_id");
                ActivityReviewDetailsResponse review = reviewMap.get(reviewId);

                if (review == null) {
                    review = ActivityReviewDetailsResponse.builder()
                            .reviewId(reviewId)
                            .activityScheduleId(rs.getLong("activity_schedule_id"))
                            .activityId(rs.getLong("activity_id"))
                            .activityName(rs.getString("activity_name"))
                            .reviewName(rs.getString("review_name"))
                            .review(rs.getString("review"))
                            .rating(rs.getBigDecimal("rating"))
                            .description(rs.getString("description"))
                            .reviewStatus(rs.getString("review_status"))
                            .numberOfParticipate(rs.getInt("number_of_participate"))
                            .reviewCreatedBy(rs.getLong("review_created_by"))
                            .reviewCreatedAt(rs.getTimestamp("review_created_at").toLocalDateTime())
                            .reviewUpdatedBy(rs.getObject("review_updated_by") != null ? rs.getLong("review_updated_by") : null)
                            .reviewUpdatedAt(rs.getTimestamp("review_updated_at") != null ? rs.getTimestamp("review_updated_at").toLocalDateTime() : null)
                            .images(new ArrayList<>())
                            .reactions(new ArrayList<>())
                            .comments(new ArrayList<>())
                            .build();
                    reviewMap.put(reviewId, review);
                }

                // Process Review Image
                Long imageId = rs.getObject("image_id", Long.class);
                if (imageId != null && review.getImages().stream().noneMatch(i -> i.getImageId().equals(imageId))) {
                    review.getImages().add(ActivityReviewDetailsResponse.ReviewImage.builder()
                            .imageId(imageId)
                            .imageName(rs.getString("image_name"))
                            .imageDescription(rs.getString("image_description"))
                            .imageUrl(rs.getString("image_url"))
                            .imageStatus(rs.getString("image_status"))
                            .imageCreatedBy(rs.getLong("image_created_by"))
                            .imageCreatedAt(rs.getTimestamp("image_created_at").toLocalDateTime())
                            .build());
                }

                // Process Review Reaction
                Long reviewReactionId = rs.getObject("review_reaction_id", Long.class);
                if (reviewReactionId != null && review.getReactions().stream().noneMatch(r -> r.getReviewReactionId().equals(reviewReactionId))) {
                    review.getReactions().add(ActivityReviewDetailsResponse.ReviewReaction.builder()
                            .reviewReactionId(reviewReactionId)
                            .reactionReviewId(rs.getLong("reaction_review_id"))
                            .userId(rs.getLong("reaction_user_id"))
                            .userName(rs.getString("reaction_user_name"))
                            .reactionType(rs.getString("reaction_type"))
                            .reviewReactionStatus(rs.getString("review_reaction_status"))
                            .reactionCreatedAt(rs.getTimestamp("reaction_created_at").toLocalDateTime())
                            .build());
                }

                // Process Comment
                Long commentId = rs.getObject("comment_id", Long.class);
                ActivityReviewDetailsResponse.Comment comment = null;
                if (commentId != null) {
                    comment = review.getComments().stream()
                            .filter(c -> c.getCommentId().equals(commentId))
                            .findFirst()
                            .orElse(null);
                    if (comment == null) {
                        comment = ActivityReviewDetailsResponse.Comment.builder()
                                .commentId(commentId)
                                .commentReviewId(rs.getLong("comment_review_id"))
                                .userId(rs.getLong("comment_user_id"))
                                .userName(rs.getString("comment_user_name"))
                                .parentCommentId(rs.getObject("parent_comment_id") != null ? rs.getLong("parent_comment_id") : null)
                                .comment(rs.getString("comment"))
                                .commentStatus(rs.getString("comment_status"))
                                .commentCreatedAt(rs.getTimestamp("comment_created_at").toLocalDateTime())
                                .commentCreatedBy(rs.getLong("comment_created_by"))
                                .commentReactions(new ArrayList<>())
                                .build();
                        review.getComments().add(comment);
                    }
                }

                // Process Comment Reaction
                if (comment != null) {
                    Long commentReactionId = rs.getObject("comment_reaction_id", Long.class);
                    if (commentReactionId != null && comment.getCommentReactions().stream().noneMatch(cr -> cr.getCommentReactionId().equals(commentReactionId))) {
                        comment.getCommentReactions().add(ActivityReviewDetailsResponse.Comment.CommentReaction.builder()
                                .commentReactionId(commentReactionId)
                                .commentReactionCommentId(rs.getLong("comment_reaction_comment_id"))
                                .userId(rs.getLong("comment_reaction_user_id"))
                                .userName(rs.getString("comment_reaction_user_name"))
                                .commentReactionType(rs.getString("comment_reaction_type"))
                                .commentReactionStatus(rs.getString("comment_reaction_status"))
                                .commentReactionCreatedBy(rs.getLong("comment_reaction_created_by"))
                                .commentReactionCreatedAt(rs.getTimestamp("comment_reaction_created_at").toLocalDateTime())
                                .build());
                    }
                }
            });

            return new ArrayList<>(reviewMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity reviews: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity reviews from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity reviews: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity reviews");
        }
    }

    @Override
    public ActivityResponseDto getActivityById(Long activityId) {

        try {
            return jdbcTemplate.queryForObject(
                    ActivitiesQueries.GET_ACTIVITY_DETAILS_BY_ID,
                    new Object[]{activityId},
                    (rs, rowNum) -> {

                        ActivityResponseDto activity = new ActivityResponseDto();

                        // Basic fields
                        activity.setId(rs.getLong("id"));
                        activity.setDestinationId(rs.getInt("destination_id"));
                        activity.setName(rs.getString("name"));
                        activity.setDescription(rs.getString("description"));
                        activity.setDurationHours(rs.getBigDecimal("duration_hours"));
                        activity.setAvailableFrom(rs.getTime("available_from"));
                        activity.setAvailableTo(rs.getTime("available_to"));
                        activity.setPriceLocal(rs.getBigDecimal("price_local"));
                        activity.setPriceForeigners(rs.getBigDecimal("price_foreigners"));
                        activity.setMinParticipate(rs.getInt("min_participate"));
                        activity.setMaxParticipate(rs.getInt("max_participate"));
                        activity.setSeasonId(rs.getLong("seasonId"));
                        activity.setSeason(rs.getString("season"));
                        activity.setStatus(rs.getString("status_name"));
                        activity.setCreatedAt(rs.getTimestamp("created_at"));
                        activity.setUpdatedAt(rs.getTimestamp("updated_at"));

                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            // Categories
                            String categoriesJson = rs.getString("categories");
                            activity.setActivityCategoryDtos(
                                    categoriesJson != null
                                            ? mapper.readValue(categoriesJson,
                                            new TypeReference<List<ActivityCategoryDto>>() {
                                            })
                                            : List.of()
                            );

                            // Schedules
                            String schedulesJson = rs.getString("schedules");
                            activity.setSchedules(
                                    schedulesJson != null
                                            ? mapper.readValue(schedulesJson,
                                            new TypeReference<List<ActivityScheduleDto>>() {
                                            })
                                            : List.of()
                            );

                            // Requirements
                            String requirementsJson = rs.getString("requirements");
                            activity.setRequirements(
                                    requirementsJson != null
                                            ? mapper.readValue(requirementsJson,
                                            new TypeReference<List<ActivityRequirementDto>>() {
                                            })
                                            : List.of()
                            );

                            // Images
                            String imagesJson = rs.getString("images");
                            activity.setImages(
                                    imagesJson != null
                                            ? mapper.readValue(imagesJson,
                                            new TypeReference<List<ActivityImageDto>>() {
                                            })
                                            : List.of()
                            );

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error parsing JSON fields", e);
                        }

                        return activity;
                    }
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity details");
        }
    }

    @Override
    public List<ActivityHistoryDetailsResponse> getActivityHistoryDetailsById(Long activityId) {
        String GET_ACTIVITY_HISTORY_DETAILS_BY_ID = ActivitiesQueries.GET_ACTIVITY_HISTORY_DETAILS_BY_ID;

        try {
            LOGGER.info("Fetching activity history details for activityId : {} ", activityId);

            Map<Long, ActivityHistoryDetailsResponse> historyMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_ACTIVITY_HISTORY_DETAILS_BY_ID, new Object[]{activityId}, rs -> {
                Long historyId = rs.getLong("history_id");
                ActivityHistoryDetailsResponse response = historyMap.get(historyId);

                if (response == null) {
                    response = ActivityHistoryDetailsResponse.builder()
                            .historyId(historyId)
                            .activity(ActivityHistoryDetailsResponse.ActivityInfo.builder()
                                    .activityId(rs.getLong("activity_id"))
                                    .activityName(rs.getString("activity_name"))
                                    .activityDescription(rs.getString("activity_description"))
                                    .durationHours(rs.getInt("duration_hours"))
                                    .availableFrom(getLocalDateTime(rs, "available_from"))
                                    .availableTo(getLocalDateTime(rs, "available_to"))
                                    .priceLocal(rs.getDouble("price_local"))
                                    .priceForeigners(rs.getDouble("price_foreigners"))
                                    .minParticipate(rs.getInt("min_participate"))
                                    .maxParticipate(rs.getInt("max_participate"))
                                    .season(rs.getString("season"))
                                    .destination(ActivityHistoryDetailsResponse.DestinationInfo.builder()
                                            .destinationId(rs.getString("destination_id"))
                                            .destinationName(rs.getString("destination_name"))
                                            .destinationDescription(rs.getString("destination_description"))
                                            .destinationLocation(rs.getString("destination_location"))
                                            .latitude(getDouble(rs, "latitude"))
                                            .longitude(getDouble(rs, "longitude"))
                                            .build())
                                    .build())
                            .schedule(ActivityHistoryDetailsResponse.ScheduleInfo.builder()
                                    .scheduleId(rs.getLong("schedule_id"))
                                    .scheduleName(rs.getString("schedule_name"))
                                    .scheduleDescription(rs.getString("schedule_description"))
                                    .assumeStartDate(getLocalDateTime(rs, "assume_start_date"))
                                    .assumeEndDate(getLocalDateTime(rs, "assume_end_date"))
                                    .durationHoursStart(rs.getInt("duration_hours_start"))
                                    .durationHoursEnd(rs.getInt("duration_hours_end"))
                                    .specialNote(rs.getString("schedule_special_note"))
                                    .build())
                            .history(ActivityHistoryDetailsResponse.HistoryInfo.builder()
                                    .historyName(rs.getString("history_name"))
                                    .historyDescription(rs.getString("history_description"))
                                    .numberOfParticipate(rs.getInt("number_of_participate"))
                                    .activityStart(getLocalDateTime(rs, "activity_start"))
                                    .activityEnd(getLocalDateTime(rs, "activity_end"))
                                    .rating(rs.getDouble("rating"))
                                    .specialNote(rs.getString("history_special_note"))
                                    .statusName(rs.getString("history_status_name"))
                                    .createdByUsername(rs.getString("history_created_by_username"))
                                    .updatedByUsername(rs.getString("history_updated_by_username"))
                                    .terminatedByUsername(rs.getString("history_terminated_by_username"))
                                    .createdAt(getLocalDateTime(rs, "history_created_at"))
                                    .updatedAt(getLocalDateTime(rs, "history_updated_at"))
                                    .terminatedAt(getLocalDateTime(rs, "history_terminated_at"))
                                    .build())
                            .images(new ArrayList<>())
                            .build();
                    ObjectMapper mapper = new ObjectMapper();

                    String categoriesJson = rs.getString("activity_categories");
                    List<String> categories;

                    try {
                        categories = categoriesJson != null
                                ? mapper.readValue(categoriesJson, new TypeReference<List<String>>() {
                        })
                                : List.of();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error parsing activity categories JSON", e);
                    }

//                    response.getActivity().setActivityCategory(categories);

                    historyMap.put(historyId, response);
                }


                // Add image if exists
                Long imageId = rs.getLong("image_id");
                if (imageId != 0) {
                    ActivityHistoryDetailsResponse.ImageInfo image = ActivityHistoryDetailsResponse.ImageInfo.builder()
                            .imageId(imageId)
                            .imageName(rs.getString("image_name"))
                            .imageDescription(rs.getString("image_description"))
                            .imageUrl(rs.getString("image_url"))
                            .statusName(rs.getString("image_status_name"))
                            .createdByUsername(rs.getString("image_created_by_username"))
                            .updatedByUsername(rs.getString("image_updated_by_username"))
                            .terminatedByUsername(rs.getString("image_terminated_by_username"))
                            .createdAt(getLocalDateTime(rs, "image_created_at"))
                            .updatedAt(getLocalDateTime(rs, "image_updated_at"))
                            .terminatedAt(getLocalDateTime(rs, "image_terminated_at"))
                            .build();

                    response.getImages().add(image);
                }
            });

            return new ArrayList<>(historyMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity details");
        }
    }

    @Override
    public List<ActivityHistoryDetailsResponse> getAllActivityHistoryDetails() {

        try {

            Map<Long, ActivityHistoryDetailsResponse> historyMap = new LinkedHashMap<>();

            jdbcTemplate.query(ActivitiesQueries.GET_ACTIVITY_HISTORY_DETAILS, rs -> {

                Long historyId = rs.getLong("history_id");
                ActivityHistoryDetailsResponse response = historyMap.get(historyId);

                if (response == null) {

                    response = ActivityHistoryDetailsResponse.builder()
                            .historyId(historyId)
                            .activity(
                                    ActivityHistoryDetailsResponse.ActivityInfo.builder()
                                            .activityId(rs.getLong("activity_id"))
                                            .activityName(rs.getString("activity_name"))
                                            .activityDescription(rs.getString("activity_description"))
                                            .durationHours(rs.getInt("duration_hours"))
                                            .availableFrom(getLocalDateTime(rs, "available_from"))
                                            .availableTo(getLocalDateTime(rs, "available_to"))
                                            .priceLocal(rs.getDouble("price_local"))
                                            .priceForeigners(rs.getDouble("price_foreigners"))
                                            .minParticipate(rs.getInt("min_participate"))
                                            .maxParticipate(rs.getInt("max_participate"))
                                            .season(rs.getString("season"))
                                            .destination(
                                                    ActivityHistoryDetailsResponse.DestinationInfo.builder()
                                                            .destinationId(rs.getString("destination_id"))
                                                            .destinationName(rs.getString("destination_name"))
                                                            .destinationDescription(rs.getString("destination_description"))
                                                            .destinationLocation(rs.getString("destination_location"))
                                                            .latitude(getDouble(rs, "latitude"))
                                                            .longitude(getDouble(rs, "longitude"))
                                                            .build()
                                            )
                                            .build()
                            )
                            .schedule(
                                    ActivityHistoryDetailsResponse.ScheduleInfo.builder()
                                            .scheduleId(rs.getLong("schedule_id"))
                                            .scheduleName(rs.getString("schedule_name"))
                                            .scheduleDescription(rs.getString("schedule_description"))
                                            .assumeStartDate(getLocalDateTime(rs, "assume_start_date"))
                                            .assumeEndDate(getLocalDateTime(rs, "assume_end_date"))
                                            .durationHoursStart(rs.getInt("duration_hours_start"))
                                            .durationHoursEnd(rs.getInt("duration_hours_end"))
                                            .specialNote(rs.getString("schedule_special_note"))
                                            .build()
                            )
                            .history(
                                    ActivityHistoryDetailsResponse.HistoryInfo.builder()
                                            .historyName(rs.getString("history_name"))
                                            .historyDescription(rs.getString("history_description"))
                                            .numberOfParticipate(rs.getInt("number_of_participate"))
                                            .activityStart(getLocalDateTime(rs, "activity_start"))
                                            .activityEnd(getLocalDateTime(rs, "activity_end"))
                                            .rating(rs.getDouble("rating"))
                                            .specialNote(rs.getString("history_special_note"))
                                            .statusName(rs.getString("history_status_name"))
                                            .createdByUsername(rs.getString("history_created_by_username"))
                                            .updatedByUsername(rs.getString("history_updated_by_username"))
                                            .terminatedByUsername(rs.getString("history_terminated_by_username"))
                                            .createdAt(getLocalDateTime(rs, "history_created_at"))
                                            .updatedAt(getLocalDateTime(rs, "history_updated_at"))
                                            .terminatedAt(getLocalDateTime(rs, "history_terminated_at"))
                                            .build()
                            )
                            .images(new ArrayList<>())
                            .build();

                    ObjectMapper mapper = new ObjectMapper();

                    String categoriesJson = rs.getString("activity_categories");

                    try {
                        List<ActivityCategoryDto> categories =
                                categoriesJson != null
                                        ? mapper.readValue(
                                        categoriesJson,
                                        new TypeReference<List<ActivityCategoryDto>>() {
                                        }
                                )
                                        : List.of();

                        response.getActivity().setActivityCategoryDtos(categories);

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error parsing activity categories JSON", e);
                    }

                    historyMap.put(historyId, response);
                }

                // Add image
                Long imageId = rs.getLong("image_id");
                if (!rs.wasNull()) {
                    response.getImages().add(
                            ActivityHistoryDetailsResponse.ImageInfo.builder()
                                    .imageId(imageId)
                                    .imageName(rs.getString("image_name"))
                                    .imageDescription(rs.getString("image_description"))
                                    .imageUrl(rs.getString("image_url"))
                                    .statusName(rs.getString("image_status_name"))
                                    .createdByUsername(rs.getString("image_created_by_username"))
                                    .updatedByUsername(rs.getString("image_updated_by_username"))
                                    .terminatedByUsername(rs.getString("image_terminated_by_username"))
                                    .createdAt(getLocalDateTime(rs, "image_created_at"))
                                    .updatedAt(getLocalDateTime(rs, "image_updated_at"))
                                    .terminatedAt(getLocalDateTime(rs, "image_terminated_at"))
                                    .build()
                    );
                }

            });

            return new ArrayList<>(historyMap.values());

        } catch (Exception ex) {
            throw new RuntimeException("Error fetching activity history details", ex);
        }
    }

    @Override
    public List<ActivityHistoryImageResponse> getAllActivityHistoryImages() {
        String GET_ACTIVITY_HISTORY_IMAGES = ActivitiesQueries.GET_ACTIVITY_HISTORY_IMAGES;

        try {
            LOGGER.info("Executing query to fetch all activity history images.");

            List<ActivityHistoryImageResponse> result = new ArrayList<>();

            jdbcTemplate.query(GET_ACTIVITY_HISTORY_IMAGES, rs -> {

                ActivityHistoryImageResponse.Activity activity = ActivityHistoryImageResponse.Activity.builder()
                        .activityId(rs.getLong("activity_id"))
                        .activityName(rs.getString("activity_name"))
                        .activityDescription(rs.getString("activity_description"))
                        .activityCategory(rs.getString("activity_category"))
                        .durationHours(rs.getInt("duration_hours"))
                        .priceLocal(rs.getDouble("price_local"))
                        .priceForeigners(rs.getDouble("price_foreigners"))
                        .minParticipate(rs.getInt("min_participate"))
                        .maxParticipate(rs.getInt("max_participate"))
                        .build();

                ActivityHistoryImageResponse.Schedule schedule = ActivityHistoryImageResponse.Schedule.builder()
                        .scheduleId(rs.getLong("schedule_id"))
                        .scheduleName(rs.getString("schedule_name"))
                        .scheduleDescription(rs.getString("schedule_description"))
                        .assumeStartDate(getLocalDateTime(rs, "assume_start_date"))
                        .assumeEndDate(getLocalDateTime(rs, "assume_end_date"))
                        .durationHoursStart(rs.getInt("duration_hours_start"))
                        .durationHoursEnd(rs.getInt("duration_hours_end"))
                        .scheduleSpecialNote(rs.getString("schedule_special_note"))
                        .build();

                ActivityHistoryImageResponse.History history = ActivityHistoryImageResponse.History.builder()
                        .historyId(rs.getLong("history_id"))
                        .historyName(rs.getString("history_name"))
                        .historyDescription(rs.getString("history_description"))
                        .numberOfParticipate(rs.getInt("number_of_participate"))
                        .activityStart(getLocalDateTime(rs, "activity_start"))
                        .activityEnd(getLocalDateTime(rs, "activity_end"))
                        .rating(rs.getDouble("rating"))
                        .historySpecialNote(rs.getString("history_special_note"))
                        .historyStatusName(rs.getString("history_status_name"))
                        .build();

                ActivityHistoryImageResponse image = ActivityHistoryImageResponse.builder()
                        .imageId(rs.getLong("image_id"))
                        .imageName(rs.getString("image_name"))
                        .imageDescription(rs.getString("image_description"))
                        .imageUrl(rs.getString("image_url"))
                        .imageStatusName(rs.getString("image_status_name"))
                        .imageCreatedByUsername(rs.getString("image_created_by_username"))
                        .imageUpdatedByUsername(rs.getString("image_updated_by_username"))
                        .imageTerminatedByUsername(rs.getString("image_terminated_by_username"))
                        .imageCreatedAt(getLocalDateTime(rs, "image_created_at"))
                        .imageUpdatedAt(getLocalDateTime(rs, "image_updated_at"))
                        .imageTerminatedAt(getLocalDateTime(rs, "image_terminated_at"))
                        .activity(activity)
                        .schedule(schedule)
                        .history(history)
                        .build();

                result.add(image);
            });

            return result;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity history images: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity history images from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity history images: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity history images");
        }
    }

    @Override
    public List<ActivityHistoryImageResponse> getActivityHistoryImagesById(Long activityId) {
        String GET_ACTIVITY_HISTORY_IMAGES_BY_ID = ActivitiesQueries.GET_ACTIVITY_HISTORY_IMAGES_BY_ID;

        try {
            LOGGER.info("Fetching activity history images for activityId: {}", activityId);

            List<ActivityHistoryImageResponse> result = new ArrayList<>();

            jdbcTemplate.query(GET_ACTIVITY_HISTORY_IMAGES_BY_ID, new Object[]{activityId}, rs -> {

                ActivityHistoryImageResponse.Activity activity = ActivityHistoryImageResponse.Activity.builder()
                        .activityId(rs.getLong("activity_id"))
                        .activityName(rs.getString("activity_name"))
                        .activityDescription(rs.getString("activity_description"))
                        .activityCategory(rs.getString("activity_category"))
                        .durationHours(rs.getInt("duration_hours"))
                        .priceLocal(rs.getDouble("price_local"))
                        .priceForeigners(rs.getDouble("price_foreigners"))
                        .minParticipate(rs.getInt("min_participate"))
                        .maxParticipate(rs.getInt("max_participate"))
                        .build();

                ActivityHistoryImageResponse.Schedule schedule = ActivityHistoryImageResponse.Schedule.builder()
                        .scheduleId(rs.getLong("schedule_id"))
                        .scheduleName(rs.getString("schedule_name"))
                        .scheduleDescription(rs.getString("schedule_description"))
                        .assumeStartDate(getLocalDateTime(rs, "assume_start_date"))
                        .assumeEndDate(getLocalDateTime(rs, "assume_end_date"))
                        .durationHoursStart(rs.getInt("duration_hours_start"))
                        .durationHoursEnd(rs.getInt("duration_hours_end"))
                        .scheduleSpecialNote(rs.getString("schedule_special_note"))
                        .build();

                ActivityHistoryImageResponse.History history = ActivityHistoryImageResponse.History.builder()
                        .historyId(rs.getLong("history_id"))
                        .historyName(rs.getString("history_name"))
                        .historyDescription(rs.getString("history_description"))
                        .numberOfParticipate(rs.getInt("number_of_participate"))
                        .activityStart(getLocalDateTime(rs, "activity_start"))
                        .activityEnd(getLocalDateTime(rs, "activity_end"))
                        .rating(rs.getDouble("rating"))
                        .historySpecialNote(rs.getString("history_special_note"))
                        .historyStatusName(rs.getString("history_status_name"))
                        .build();

                ActivityHistoryImageResponse image = ActivityHistoryImageResponse.builder()
                        .imageId(rs.getLong("image_id"))
                        .imageName(rs.getString("image_name"))
                        .imageDescription(rs.getString("image_description"))
                        .imageUrl(rs.getString("image_url"))
                        .imageStatusName(rs.getString("image_status_name"))
                        .imageCreatedByUsername(rs.getString("image_created_by_username"))
                        .imageUpdatedByUsername(rs.getString("image_updated_by_username"))
                        .imageTerminatedByUsername(rs.getString("image_terminated_by_username"))
                        .imageCreatedAt(getLocalDateTime(rs, "image_created_at"))
                        .imageUpdatedAt(getLocalDateTime(rs, "image_updated_at"))
                        .imageTerminatedAt(getLocalDateTime(rs, "image_terminated_at"))
                        .activity(activity)
                        .schedule(schedule)
                        .history(history)
                        .build();

                result.add(image);
            });

            return result;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity history images: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity history images from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity history images: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity history images");
        }
    }

    private static final List<String> ALLOWED_ACTIVITY_SORT_COLUMNS = List.of(
            "name",
            "price_local",
            "duration_hours",
            "created_at",
            "available_from",
            "available_to"
    );

    @Override
    public ActivityWithParamsResponse getActivitiesWithParams(ActivityDataRequest activityDataRequest) {
        LOGGER.info(activityDataRequest.toString());
        try {
            LOGGER.info("Executing query to fetch all activity categories...");

            String sortBy = activityDataRequest.getSortBy();
            String sortDirection = activityDataRequest.getSortDirection();

            if (sortBy == null || !ALLOWED_ACTIVITY_SORT_COLUMNS.contains(sortBy)) {
                sortBy = "created_at";
            }

            if (sortDirection == null ||
                    (!sortDirection.equalsIgnoreCase("ASC")
                            && !sortDirection.equalsIgnoreCase("DESC"))) {
                sortDirection = "ASC";
            }

            String paginatedQuery = ActivitiesQueries.GET_ACTIVITY_IDS_WITH_FILTERS
                    + " ORDER BY a." + sortBy + " " + sortDirection
                    + " LIMIT ? OFFSET ?";

            int offset = (activityDataRequest.getPageNumber() - 1) * activityDataRequest.getPageSize();
            List<Long> activitiesIds = jdbcTemplate.query(
                    paginatedQuery,
                    new Object[]{
                            activityDataRequest.getName(), activityDataRequest.getName(),
                            activityDataRequest.getMinPrice(), activityDataRequest.getMinPrice(),
                            activityDataRequest.getMaxPrice(), activityDataRequest.getMaxPrice(),
                            activityDataRequest.getDuration(), activityDataRequest.getDuration(),
                            activityDataRequest.getSeason(), activityDataRequest.getSeason(),          // FIXED
                            activityDataRequest.getStatus(), activityDataRequest.getStatus(),          // FIXED
                            activityDataRequest.getActivityCategory(), activityDataRequest.getActivityCategory(), // MOVED HERE
                            activityDataRequest.getPageSize(),
                            offset
                    },
                    (rs, rowNum) -> rs.getLong("id")
            );
            Integer totalCount = jdbcTemplate.queryForObject(
                    ActivitiesQueries.GET_ACTIVITY_COUNT_WITH_FILTERS,
                    new Object[]{
                            activityDataRequest.getName(), activityDataRequest.getName(),
                            activityDataRequest.getMinPrice(), activityDataRequest.getMinPrice(),
                            activityDataRequest.getMaxPrice(), activityDataRequest.getMaxPrice(),
                            activityDataRequest.getDuration(), activityDataRequest.getDuration(),
                            activityDataRequest.getSeason(), activityDataRequest.getSeason(),        // ✅ season
                            activityDataRequest.getStatus(), activityDataRequest.getStatus(),        // ✅ status
                            activityDataRequest.getActivityCategory(), activityDataRequest.getActivityCategory() // ✅ category
                    },
                    Integer.class
            );


            if (activitiesIds.isEmpty()) {
                return null;
            }


            String inSql = String.join(",", Collections.nCopies(activitiesIds.size(), "?"));
            String sql = String.format(
                    ActivitiesQueries.GET_ACTIVITIES_BY_IDS
                            + " ORDER BY FIELD(a.id, %s)",
                    inSql,
                    inSql
            );

            List<Object> params = new ArrayList<>(activitiesIds);
            params.addAll(activitiesIds);

            List<ActivityResponseDto> result = jdbcTemplate.query(
                    sql,
                    params.toArray(),
                    new ActivityRowMapper()
            );

            return new ActivityWithParamsResponse(totalCount, result);

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity categories: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity categories from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity categories: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity categories");
        }
    }

    @Override
    public List<ActivityForTerminateResponse> getActivitiesForTerminate() {
        String GET_ACTIVE_ACTIVITIES_FOR_TERMINATE = ActivitiesQueries.GET_ACTIVE_ACTIVITIES_FOR_TERMINATE;

        try {
            return jdbcTemplate.query(
                    GET_ACTIVE_ACTIVITIES_FOR_TERMINATE,
                    new Object[]{CommonStatus.ACTIVE.toString()},
                    (rs, rowNum) -> ActivityForTerminateResponse.builder()
                            .activityId(rs.getLong("id"))
                            .activityName(rs.getString("name"))
                            .build()
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch activities for terminate: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activities for terminate: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activities for terminate");
        }
    }

    @Override
    public void terminateActivity(ActivityTerminateRequest activityTerminateRequest, Long userId) {
        String ACTIVITY_TERMINATE = ActivitiesQueries.ACTIVITY_TERMINATE;
        try {
            jdbcTemplate.update(ACTIVITY_TERMINATE, new Object[]{CommonStatus.TERMINATED.toString(), userId, activityTerminateRequest.getActivityId()});
        } catch (DataAccessException tfe) {
            LOGGER.error(tfe.toString());
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to terminate activity : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate activity");
        }
    }

    @Override
    public Long insertActivityDetails(ActivityInsertRequest activityInsertRequest, Long userId) {
        String INSERT_ACTIVITY_BASIC_DETAILS = ActivitiesQueries.INSERT_ACTIVITY_BASIC_DETAILS;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            Long statusId = statusRepository.getStatusIdByName(activityInsertRequest.getStatus());

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        INSERT_ACTIVITY_BASIC_DETAILS,
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setLong(1, activityInsertRequest.getDestinationId());
                ps.setString(2, activityInsertRequest.getName());
                ps.setString(3, activityInsertRequest.getDescription());
                ps.setBigDecimal(4, activityInsertRequest.getDurationHours());
                ps.setObject(5, activityInsertRequest.getAvailableFrom());
                ps.setObject(6, activityInsertRequest.getAvailableTo());
                ps.setBigDecimal(7, activityInsertRequest.getPriceLocal());
                ps.setBigDecimal(8, activityInsertRequest.getPriceForeigners());
                ps.setInt(9, activityInsertRequest.getMinParticipate());
                ps.setInt(10, activityInsertRequest.getMaxParticipate());
                ps.setLong(11, activityInsertRequest.getSeasonId());
                ps.setLong(12, statusId);
                ps.setLong(13, userId);

                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                throw new InsertFailedErrorExceptionHandler("Failed to generate activity ID");
            }

            return keyHolder.getKey().longValue();

        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting activity", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to insert activity", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert activity");
        }
    }

    @Override
    public void insertActivityImages(
            Long activityId,
            List<ActivityImageInsertRequest> images,
            Long userId) {

        if (images == null || images.isEmpty()) {
            return;
        }

        String INSERT_ACTIVITY_IMAGE = ActivitiesQueries.INSERT_ACTIVITY_IMAGE;

        try {
            jdbcTemplate.batchUpdate(
                    INSERT_ACTIVITY_IMAGE,
                    images,
                    images.size(),
                    (ps, image) -> {
                        ps.setLong(1, activityId);
                        ps.setString(2, image.getName());
                        ps.setString(3, image.getDescription());
                        ps.setString(4, image.getImageUrl());
                        ps.setString(5, image.getStatus());
                        ps.setLong(6, userId);
                    }
            );

        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting activity images", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to insert activity images", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert activity images");
        }
    }

    @Override
    public void insertActivityRequirements(
            Long activityId,
            List<ActivityRequirementInsertRequest> requirements,
            Long userId) {

        if (requirements == null || requirements.isEmpty()) {
            return;
        }

        String INSERT_ACTIVITY_REQUIREMENTS = ActivitiesQueries.INSERT_ACTIVITY_REQUIREMENTS;

        try {
            jdbcTemplate.batchUpdate(
                    INSERT_ACTIVITY_REQUIREMENTS,
                    requirements,
                    requirements.size(),
                    (ps, req) -> {
                        ps.setLong(1, activityId);
                        ps.setString(2, req.getName());
                        ps.setString(3, req.getValue());
                        ps.setString(4, req.getDescription());
                        ps.setString(5, req.getStatus());
                        ps.setString(6, req.getColor());
                        ps.setLong(7, userId);
                    }
            );

        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting activity requirements", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to insert activity requirements", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert activity requirements");
        }
    }

    @Override
    public void updateBasicActivityDetails(ActivityUpdateRequest request, Long userId) {

        String sql = ActivitiesQueries.UPDATE_BASIC_ACTIVITY_DETAILS;

        try {

            Long statusId = statusRepository.getStatusIdByName(request.getStatus());

            jdbcTemplate.update(sql,
                    request.getDestinationId(),
                    request.getName(),
                    request.getDescription(),
                    request.getDurationHours(),
                    request.getAvailableFrom() != null
                            ? Time.valueOf(request.getAvailableFrom())
                            : null,
                    request.getAvailableTo() != null
                            ? Time.valueOf(request.getAvailableTo())
                            : null,
                    request.getPriceLocal(),
                    request.getPriceForeigners(),
                    request.getMinParticipate(),
                    request.getMaxParticipate(),
                    request.getSeasonId(),
                    statusId,
                    userId,
                    request.getActivityId()
            );

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating activity", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to update activity", e);
            throw new InternalServerErrorExceptionHandler("Failed to update activity");
        }
    }

    @Override
    public void removeActivityImages(List<Long> removeImagesIds, Long userId) {
        try {
            jdbcTemplate.batchUpdate(
                    ActivitiesQueries.ACTIVITY_IMAGES_REMOVE,
                    removeImagesIds,
                    removeImagesIds.size(),
                    (ps, imageId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
                        ps.setLong(2, userId);
                        ps.setLong(3, imageId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove activity images", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove activity images : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove activity images");
        }
    }

    @Override
    public void removeRequirements(List<Long> removeRequirementsIds, Long userId) {
        try {
            jdbcTemplate.batchUpdate(
                    ActivitiesQueries.ACTIVITY_REQUIREMENTS_REMOVE,
                    removeRequirementsIds,
                    removeRequirementsIds.size(),
                    (ps, requirementId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
                        ps.setLong(2, userId);
                        ps.setLong(3, requirementId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove requirements", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove requirements : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove requirements");
        }
    }

    @Override
    public void updateActivityImages(Long activityId,
                                     List<ActivityImageUpdateRequest> updatedImages,
                                     Long userId) {

        if (updatedImages == null || updatedImages.isEmpty()) {
            return;
        }

        try {
            for (ActivityImageUpdateRequest image : updatedImages) {

                jdbcTemplate.update(
                        ActivitiesQueries.UPDATE_ACTIVITY_IMAGE,
                        image.getName(),
                        image.getDescription(),
                        image.getImageUrl(),
                        image.getStatus(),
                        userId,
                        image.getImageId(),
                        activityId
                );
            }

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating activity images", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update activity images", e);
            throw new InternalServerErrorExceptionHandler("Failed to update activity images");
        }
    }

    @Override
    public void updateActivityRequirements(Long activityId,
                                           List<ActivityRequirementsUpdateRequest> updatedRequirements,
                                           Long userId) {

        if (updatedRequirements == null || updatedRequirements.isEmpty()) {
            return;
        }

        try {
            for (ActivityRequirementsUpdateRequest req : updatedRequirements) {

                jdbcTemplate.update(
                        ActivitiesQueries.UPDATE_ACTIVITY_REQUIREMENT,
                        req.getName(),
                        req.getValue(),
                        req.getDescription(),
                        req.getColor(),
                        req.getStatus(),
                        userId,
                        req.getRequirementId(),
                        activityId
                );
            }

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating activity requirements", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update activity requirements", e);
            throw new InternalServerErrorExceptionHandler("Failed to update activity requirements");
        }
    }

    @Override
    public ActivityStatisticsResponse.ActivityDetails getActivityDetailsStatistics() {
        try {
            LOGGER.info("Executing query to fetch activities details statistics.");

            return jdbcTemplate.queryForObject(
                    ActivitiesQueries.GET_ACTIVITY_DETAILS_STATISTICS,
                    (rs, rowNum) -> ActivityStatisticsResponse.ActivityDetails.builder()
                            .totalActivitiesCount(rs.getInt("totalActivityCount"))
                            .activeActivities(rs.getInt("activeActivities"))
                            .inActiveActivities(rs.getInt("inActiveActivities"))
                            .hiddenActivities(rs.getInt("hiddenActivities"))
                            .recentlyUpdateActivities(rs.getInt("recentlyUpdatedActivities"))
                            .recentlyAddedActivities(rs.getInt("recentlyAddedActivities"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activities details statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activities details statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activities details statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activities details statistics");
        }
    }

    @Override
    public ActivityStatisticsResponse.WishDetails getActivityWishStatistics() {
        try {
            LOGGER.info("Executing query to fetch activities wish statistics.");

            return jdbcTemplate.queryForObject(
                    ActivitiesQueries.GET_ACTIVITY_WISH_STATISTICS,
                    (rs, rowNum) -> ActivityStatisticsResponse.WishDetails.builder()
                            .wishListCount(rs.getInt("wishListCount"))
                            .notWishListCount(rs.getInt("notWishListCount"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activities wish statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activities wish statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activities wish statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activities wish statistics");
        }
    }

    @Override
    public List<ActivityStatisticsResponse.CategoryDetails> getActivityCategoryStatistics() {
        try {
            LOGGER.info("Executing query to fetch activities category statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_ACTIVITY_CATEGORY_STATISTICS,
                    (rs, rowNum) -> ActivityStatisticsResponse.CategoryDetails.builder()
                            .categoryId(rs.getLong("category_id"))
                            .categoryName(rs.getString("category_name"))
                            .count(rs.getInt("activity_count"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activities category statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activities category statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activities category statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activities category statistics");
        }
    }

    @Override
    public ActivityScheduleStatisticsResponse.Summary getActivitySchduleSummeryStatsitics() {

        try {
            LOGGER.info("Executing query to fetch activity schedule summary statistics.");

            return jdbcTemplate.queryForObject(
                    ActivitiesQueries.GET_ACTIVITY_SCHEDULE_SUMMARY_STATISTICS,
                    (rs, rowNum) -> ActivityScheduleStatisticsResponse.Summary.builder()
                            .totalActivities(rs.getInt("total_activities"))
                            .totalActiveSchedules(rs.getInt("active_schedules"))
                            .totalParticipants(rs.getInt("total_participants"))
                            .overallAverageRating(rs.getDouble("overall_average_rating"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity schedule summary statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity schedule summary statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity schedule summary statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity schedule summary statistics");
        }
    }

    @Override
    public List<ActivityScheduleStatisticsResponse.ActivityParticipationTrend> getActivityParticipationTrendsStatsitics() {

        try {
            LOGGER.info("Executing query to fetch activity participation trend statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_ACTIVITY_PARTICIPATION_TREND_STATISTICS,
                    (rs, rowNum) -> ActivityScheduleStatisticsResponse.ActivityParticipationTrend.builder()
                            .activityDate(rs.getString("activity_date"))
                            .totalParticipants(rs.getInt("total_participants"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity participation trend statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity participation trend statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity participation trend statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity participation trend statistics");
        }
    }

    @Override
    public List<ActivityScheduleStatisticsResponse.ActivityRatingOverview> getActivityRatingOverviewStatsitics() {

        try {
            LOGGER.info("Executing query to fetch activity rating overview statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_ACTIVITY_RATING_OVERVIEW_STATISTICS,
                    (rs, rowNum) -> ActivityScheduleStatisticsResponse.ActivityRatingOverview.builder()
                            .activityId(rs.getLong("activity_id"))
                            .activityName(rs.getString("activity_name"))
                            .averageRating(rs.getDouble("average_rating"))
                            .totalReviews(rs.getInt("total_reviews"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity rating overview statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity rating overview statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity rating overview statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity rating overview statistics");
        }
    }

    @Override
    public List<ActivityScheduleStatisticsResponse.PopularActivity> getPopularActivitiesStatsitics() {

        try {
            LOGGER.info("Executing query to fetch popular activities statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_POPULAR_ACTIVITIES_STATISTICS,
                    (rs, rowNum) -> ActivityScheduleStatisticsResponse.PopularActivity.builder()
                            .activityId(rs.getLong("activity_id"))
                            .activityName(rs.getString("activity_name"))
                            .totalParticipants(rs.getInt("total_participants"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching popular activities statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch popular activities statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching popular activities statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching popular activities statistics");
        }
    }

    @Override
    public List<ActivityScheduleStatisticsResponse.ScheduleTimeline> getScheduleTimelineStatsitics() {

        try {
            LOGGER.info("Executing query to fetch schedule timeline statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_SCHEDULE_TIMELINE_STATISTICS,
                    (rs, rowNum) -> ActivityScheduleStatisticsResponse.ScheduleTimeline.builder()
                            .scheduleId(rs.getLong("schedule_id"))
                            .scheduleName(rs.getString("schedule_name"))
                            .activityName(rs.getString("activity_name"))
                            .assumeStartDate(rs.getString("assume_start_date"))
                            .assumeEndDate(rs.getString("assume_end_date"))
                            .durationHoursStart(rs.getDouble("duration_hours_start"))
                            .durationHoursEnd(rs.getDouble("duration_hours_end"))
                            .specialNote(rs.getString("special_note"))
                            .status(rs.getInt("status"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching schedule timeline statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch schedule timeline statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching schedule timeline statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching schedule timeline statistics");
        }
    }

    @Override
    public List<ActivityScheduleStatisticsResponse.ActivityStatusDistribution> getActivityStatusDistributionStatsitics() {

        try {
            LOGGER.info("Executing query to fetch activity status distribution statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_ACTIVITY_STATUS_DISTRIBUTION_STATISTICS,
                    (rs, rowNum) -> ActivityScheduleStatisticsResponse.ActivityStatusDistribution.builder()
                            .statusName(rs.getString("status_name"))
                            .totalCount(rs.getInt("total_count"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity status distribution statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch activity status distribution statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity status distribution statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching activity status distribution statistics");
        }
    }

    @Override
    public ActivityCategoriesStatisticsResponse.Summary getActivitySummeryStatistics() {

        try {
            LOGGER.info("Fetching activity category summary statistics.");

            return jdbcTemplate.queryForObject(
                    ActivitiesQueries.GET_ACTIVITY_CATEGORY_SUMMARY_STATISTICS,
                    (rs, rowNum) -> ActivityCategoriesStatisticsResponse.Summary.builder()
                            .totalCategories(rs.getInt("total_categories"))
                            .totalActivities(rs.getInt("total_activities"))
                            .mostUsedCategory(rs.getString("most_used_category"))
                            .overallAverageRating(rs.getDouble("overall_average_rating"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching category summary: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch category summary statistics");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching category summary: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching category summary");
        }
    }

    @Override
    public List<ActivityCategoriesStatisticsResponse.CategoryActivityCount> getCategoryActivityCountStatistics() {

        try {
            LOGGER.info("Fetching category activity count statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_CATEGORY_ACTIVITY_COUNT_STATISTICS,
                    (rs, rowNum) -> ActivityCategoriesStatisticsResponse.CategoryActivityCount.builder()
                            .categoryId(rs.getLong("category_id"))
                            .categoryName(rs.getString("category_name"))
                            .totalActivities(rs.getInt("total_activities"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching category activity count: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch category activity count statistics");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching category activity count: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching category activity count");
        }
    }

    @Override
    public List<ActivityCategoriesStatisticsResponse.CategoryParticipationPerformance> getCategoryParticipationPerformanceStatistics() {

        try {
            LOGGER.info("Fetching category participation performance statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_CATEGORY_PARTICIPATION_PERFORMANCE_STATISTICS,
                    (rs, rowNum) -> ActivityCategoriesStatisticsResponse.CategoryParticipationPerformance.builder()
                            .categoryId(rs.getLong("category_id"))
                            .categoryName(rs.getString("category_name"))
                            .totalParticipants(rs.getInt("total_participants"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching category participation performance: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch category participation performance statistics");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching category participation performance: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching category participation performance");
        }
    }

    @Override
    public List<ActivityCategoriesStatisticsResponse.CategoryRatingOverview> getCategoryRatingOverviewStatistics() {

        try {
            LOGGER.info("Fetching category rating overview statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_CATEGORY_RATING_OVERVIEW_STATISTICS,
                    (rs, rowNum) -> ActivityCategoriesStatisticsResponse.CategoryRatingOverview.builder()
                            .categoryId(rs.getLong("category_id"))
                            .categoryName(rs.getString("category_name"))
                            .averageRating(rs.getDouble("average_rating"))
                            .totalReviews(rs.getInt("total_reviews"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching category rating overview: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch category rating overview statistics");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching category rating overview: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching category rating overview");
        }
    }

    @Override
    public List<ActivityCategoriesStatisticsResponse.CategoryDistribution> getCategoryDistributionStatistics() {

        try {
            LOGGER.info("Fetching category distribution statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_CATEGORY_DISTRIBUTION_STATISTICS,
                    (rs, rowNum) -> ActivityCategoriesStatisticsResponse.CategoryDistribution.builder()
                            .categoryName(rs.getString("category_name"))
                            .activityCount(rs.getInt("activity_count"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching category distribution: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch category distribution statistics");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching category distribution: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching category distribution");
        }
    }

    @Override
    public List<ActivityCategoriesStatisticsResponse.CategoryPrimarySecondaryUsage> getCategoryPrimarySecondaryUsageStatistics() {

        try {
            LOGGER.info("Fetching category primary vs secondary usage statistics.");

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_CATEGORY_PRIMARY_SECONDARY_USAGE_STATISTICS,
                    (rs, rowNum) -> ActivityCategoriesStatisticsResponse.CategoryPrimarySecondaryUsage.builder()
                            .categoryName(rs.getString("category_name"))
                            .primaryCount(rs.getInt("primary_count"))
                            .secondaryCount(rs.getInt("secondary_count"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching category primary/secondary usage: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch category primary/secondary usage statistics");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching category primary/secondary usage: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching category primary/secondary usage statistics");
        }
    }

    @Override
    public void insertActivityCategories(
            Long activityId,
            List<ActivityInsertRequest.Category> categories,
            Long userId) {

        try {
            LOGGER.info("Inserting activity categories for activityId: {}", activityId);
            if (categories == null || categories.isEmpty()) {
                LOGGER.warn("No categories provided for activityId: {}", activityId);
                return;
            }

            for (ActivityInsertRequest.Category category : categories) {
                Long statusId = statusRepository.getStatusIdByName(category.getStatus());
                if (statusId == null) {
                    LOGGER.error("Invalid status name: {}", category.getStatus());
                    throw new IllegalArgumentException(
                            "Invalid status: " + category.getStatus()
                    );
                }
                int rowsAffected = jdbcTemplate.update(
                        ActivitiesQueries.INSERT_ACTIVITY_CATEGORY_MAP,
                        activityId,
                        category.getCategoryId(),
                        Boolean.TRUE.equals(category.getIsPrimary()),
                        statusId,
                        userId,
                        userId
                );
                LOGGER.info(
                        "Inserted activity category mapping. activityId: {}, categoryId: {}, rowsAffected: {}",
                        activityId,
                        category.getCategoryId(),
                        rowsAffected
                );
            }

            LOGGER.info(
                    "Successfully inserted {} category mappings for activityId: {}",
                    categories.size(),
                    activityId
            );

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Database error while inserting activity categories for activityId: {} - {}",
                    activityId,
                    ex.getMessage(),
                    ex
            );
            throw new DataAccessErrorExceptionHandler(
                    "Failed to insert activity categories"
            );

        } catch (Exception ex) {
            LOGGER.error(
                    "Unexpected error while inserting activity categories for activityId: {} - {}",
                    activityId,
                    ex.getMessage(),
                    ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while inserting activity categories"
            );
        }
    }

    @Override
    public void removeActivityCategories(List<Long> removeCategoryIds, Long userId) {

        try {

            LOGGER.info("Removing activity categories. CategoryMapIds: {}", removeCategoryIds);
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.name());
            if (removeCategoryIds == null || removeCategoryIds.isEmpty()) {
                LOGGER.warn("No activity category ids provided for removal.");
                return;
            }

            for (Long categoryMapId : removeCategoryIds) {

                int rowsAffected = jdbcTemplate.update(
                        ActivitiesQueries.REMOVE_ACTIVITY_CATEGORIES,
                        statusId,
                        userId,
                        userId,
                        categoryMapId
                );

                LOGGER.info(
                        "Removed activity category mapping. categoryMapId: {}, rowsAffected: {}",
                        categoryMapId,
                        rowsAffected
                );
            }

            LOGGER.info(
                    "Successfully removed {} activity category mappings.",
                    removeCategoryIds.size()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while removing activity categories: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to remove activity categories"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while removing activity categories: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while removing activity categories"
            );
        }
    }

    @Override
    public void updateActivityCategories(
            Long activityId,
            List<ActivityInsertRequest.Category> updatedCategories,
            Long userId) {
        try {
            LOGGER.info("Updating activity categories for activityId: {}", activityId);
            if (updatedCategories == null || updatedCategories.isEmpty()) {
                LOGGER.warn("No activity categories provided for update. activityId: {}", activityId);
                return;
            }
            for (ActivityInsertRequest.Category category : updatedCategories) {
                Long statusId = statusRepository.getStatusIdByName(category.getStatus());
                if (statusId == null) {
                    LOGGER.error("Invalid status name: {}", category.getStatus());
                    throw new IllegalArgumentException(
                            "Invalid status: " + category.getStatus()
                    );
                }
                int rowsAffected = jdbcTemplate.update(
                        ActivitiesQueries.UPDATE_ACTIVITY_CATEGORIES,
                        Boolean.TRUE.equals(category.getIsPrimary()),
                        statusId,
                        userId,
                        activityId,
                        category.getCategoryId()
                );

                LOGGER.info(
                        "Updated activity category mapping. activityId: {}, categoryId: {}, rowsAffected: {}",
                        activityId,
                        category.getCategoryId(),
                        rowsAffected
                );
            }

            LOGGER.info(
                    "Successfully updated {} activity category mappings for activityId: {}",
                    updatedCategories.size(),
                    activityId
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while updating activity categories for activityId: {} - {}",
                    activityId,
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to update activity categories"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while updating activity categories for activityId: {} - {}",
                    activityId,
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while updating activity categories"
            );
        }
    }

    @Override
    public void termianteActivityCategories(Long activityId, Long userId) {

        try {

            LOGGER.info("Terminating activity categories for activityId: {}", activityId);
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.name());
            int rowsAffected = jdbcTemplate.update(
                    ActivitiesQueries.TERMINATE_ACTIVITY_CATEGORIES,
                    statusId,
                    userId,
                    userId,
                    activityId
            );
            LOGGER.info(
                    "Successfully terminated activity categories for activityId: {}, rowsAffected: {}",
                    activityId,
                    rowsAffected
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while terminating activity categories for activityId: {} - {}",
                    activityId,
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to terminate activity categories"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while terminating activity categories for activityId: {} - {}",
                    activityId,
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while terminating activity categories"
            );
        }
    }

    @Override
    public List<ActivityBasicDetailsResponse> getActivityByDestinationId(
            ActivitiesByDestinationId activitiesByDestinationId) {

        try {

            LOGGER.info("Fetching activities for destinationId: {}",
                    activitiesByDestinationId.getDestinationId());

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_ACTIVITIES_BY_DESTINATION_ID,
                    rs -> {

                        Map<Long, ActivityBasicDetailsResponse> activityMap = new LinkedHashMap<>();

                        while (rs.next()) {

                            Long activityId = rs.getLong("activity_id");

                            ActivityBasicDetailsResponse activity =
                                    activityMap.get(activityId);

                            if (activity == null) {

                                activity = ActivityBasicDetailsResponse.builder()
                                        .activityId(activityId)
                                        .destinationId(rs.getLong("destination_id"))
                                        .name(rs.getString("name"))
                                        .description(rs.getString("description"))
                                        .durationHours(rs.getBigDecimal("duration_hours"))
                                        .availableFrom(rs.getTime("available_from") != null
                                                ? rs.getTime("available_from").toLocalTime()
                                                : null)
                                        .availableTo(rs.getTime("available_to") != null
                                                ? rs.getTime("available_to").toLocalTime()
                                                : null)
                                        .priceLocal(rs.getBigDecimal("price_local"))
                                        .priceForeigners(rs.getBigDecimal("price_foreigners"))
                                        .minParticipate(rs.getInt("min_participate"))
                                        .maxParticipate(rs.getInt("max_participate"))
                                        .season(rs.getString("season"))
                                        .seasonId(rs.getLong("season_id"))
                                        .statusId(rs.getLong("status_id"))
                                        .categories(new ArrayList<>())
                                        .images(new ArrayList<>())
                                        .build();

                                activityMap.put(activityId, activity);
                            }

                            // Categories
                            Long categoryId = rs.getLong("category_id");
                            if (categoryId != 0) {

                                ActivityBasicDetailsResponse.Category category =
                                        ActivityBasicDetailsResponse.Category.builder()
                                                .categoryId(categoryId)
                                                .categoryName(rs.getString("category_name"))
                                                .isPrimary(rs.getBoolean("is_primary"))
                                                .build();

                                activity.getCategories().add(category);
                            }

                            // Images
                            Long imageId = rs.getLong("image_id");
                            if (imageId != 0) {

                                ActivityBasicDetailsResponse.Image image =
                                        ActivityBasicDetailsResponse.Image.builder()
                                                .imageId(imageId)
                                                .name(rs.getString("image_name"))
                                                .description(rs.getString("image_description"))
                                                .imageUrl(rs.getString("image_url"))
                                                .build();

                                activity.getImages().add(image);
                            }
                        }

                        return new ArrayList<>(activityMap.values());
                    },
                    activitiesByDestinationId.getDestinationId()
            );

        } catch (DataAccessException ex) {

            LOGGER.error("Database error while fetching activities by destinationId: {}",
                    activitiesByDestinationId.getDestinationId(), ex);

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activities by destination"
            );

        } catch (Exception ex) {

            LOGGER.error("Unexpected error while fetching activities by destinationId: {}",
                    activitiesByDestinationId.getDestinationId(), ex);

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching activities"
            );
        }
    }

    @Override
    public ActivityScheduleWithParamsResponse getActivitiesScheduleWithParams(
            ActivityScheduleDataRequest request) {

        try {

            StringBuilder query = new StringBuilder(
                    ActivitiesQueries.BASE_ACTIVITY_SCHEDULE_QUERY);

            StringBuilder countQuery = new StringBuilder(
                    ActivitiesQueries.COUNT_ACTIVITY_SCHEDULE_QUERY);

            List<Object> params = new ArrayList<>();
            List<Object> countParams = new ArrayList<>();

            // =========================================
            // FILTERS
            // =========================================

            if (request.getName() != null && !request.getName().isBlank()) {

                String search = "%" + request.getName().trim() + "%";

                query.append("""
                        AND (
                            a.name LIKE ?
                            OR s.name LIKE ?
                        )
                        """);

                countQuery.append("""
                        AND (
                            a.name LIKE ?
                            OR s.name LIKE ?
                        )
                        """);

                params.add(search);
                params.add(search);

                countParams.add(search);
                countParams.add(search);
            }

            if (request.getActivityId() != null) {

                query.append(" AND a.id = ? ");
                countQuery.append(" AND a.id = ? ");

                params.add(request.getActivityId());
                countParams.add(request.getActivityId());
            }

            if (request.getDestinationId() != null) {

                query.append(" AND a.destination_id = ? ");
                countQuery.append(" AND a.destination_id = ? ");

                params.add(request.getDestinationId());
                countParams.add(request.getDestinationId());
            }

            if (request.getPackageScheduleId() != null) {

                query.append(" AND s.package_schedule_id = ? ");
                countQuery.append(" AND s.package_schedule_id = ? ");

                params.add(request.getPackageScheduleId());
                countParams.add(request.getPackageScheduleId());
            }

            if (request.getTourScheduleId() != null) {

                query.append(" AND s.tour_schedule_id = ? ");
                countQuery.append(" AND s.tour_schedule_id = ? ");

                params.add(request.getTourScheduleId());
                countParams.add(request.getTourScheduleId());
            }

            if (request.getSeasonId() != null) {

                query.append(" AND a.season_id LIKE ? ");
                countQuery.append(" AND a.season_id LIKE ? ");

                params.add(request.getSeasonId());
                countParams.add(request.getSeasonId());
            }

            if (request.getStatus() != null && !request.getStatus().isBlank()) {

                Long statusId = statusRepository.getStatusIdByName(request.getStatus());

                if (statusId != null) {

                    query.append(" AND a.status = ? ");
                    countQuery.append(" AND a.status = ? ");

                    params.add(statusId);
                    countParams.add(statusId);
                }
            }

            if (request.getFromDate() != null) {

                query.append(" AND s.assume_start_date >= ? ");
                countQuery.append(" AND s.assume_start_date >= ? ");

                params.add(request.getFromDate());
                countParams.add(request.getFromDate());
            }

            if (request.getToDate() != null) {

                query.append(" AND s.assume_end_date <= ? ");
                countQuery.append(" AND s.assume_end_date <= ? ");

                params.add(request.getToDate());
                countParams.add(request.getToDate());
            }

            if (request.getActivityCategoryId() != null) {

                query.append("""
                        AND EXISTS (
                            SELECT 1
                            FROM activity_category_map acm
                            WHERE acm.activity_id = a.id
                            AND acm.category_id = ?
                        )
                        """);

                countQuery.append("""
                        AND EXISTS (
                            SELECT 1
                            FROM activity_category_map acm
                            WHERE acm.activity_id = a.id
                            AND acm.category_id = ?
                        )
                        """);

                params.add(request.getActivityCategoryId());
                countParams.add(request.getActivityCategoryId());
            }

            // =========================================
            // SORTING
            // =========================================

            String sortColumn = mapSortColumn(request.getSortBy());

            String sortDirection = "DESC";

            if ("ASC".equalsIgnoreCase(request.getSortDirection())) {
                sortDirection = "ASC";
            }

            query.append(" ORDER BY ")
                    .append(sortColumn)
                    .append(" ")
                    .append(sortDirection);

            // =========================================
            // PAGINATION
            // =========================================

            int pageSize = request.getPageSize() > 0
                    ? request.getPageSize()
                    : 10;

            int pageNumber = request.getPageNumber() > 0
                    ? request.getPageNumber()
                    : 0;

            int offset = pageNumber * pageSize;

            query.append(" LIMIT ? OFFSET ? ");

            params.add(pageSize);
            params.add(offset);

            // =========================================
            // COUNT
            // =========================================

            Integer totalCount = jdbcTemplate.queryForObject(
                    countQuery.toString(),
                    countParams.toArray(),
                    Integer.class
            );

            // =========================================
            // MAIN DATA
            // =========================================

            List<ActivityScheduleResponseDto> result =
                    jdbcTemplate.query(
                            query.toString(),
                            params.toArray(),
                            (rs, rowNum) -> {

                                Long activityId = rs.getLong("activity_id");

                                return ActivityScheduleResponseDto.builder()
                                        .activityId(activityId)
                                        .scheduleId(rs.getLong("schedule_id"))
                                        .destinationId(rs.getLong("destination_id"))
                                        .destinationName(rs.getString("destination_name"))
                                        .activityName(rs.getString("activity_name"))
                                        .activityScheduleName(rs.getString("activity_schedule_name"))
                                        .description(rs.getString("description"))
                                        .durationHours(rs.getBigDecimal("duration_hours"))
                                        .availableFrom(rs.getTime("available_from"))
                                        .availableTo(rs.getTime("available_to"))
                                        .priceLocal(rs.getBigDecimal("price_local"))
                                        .priceForeigners(rs.getBigDecimal("price_foreigners"))
                                        .minParticipate(rs.getInt("min_participate"))
                                        .maxParticipate(rs.getInt("max_participate"))
                                        .seasonId(rs.getLong("season_id"))
                                        .season(rs.getString("season"))
                                        .status(rs.getString("status"))
                                        .createdAt(rs.getTimestamp("created_at"))
                                        .updatedAt(rs.getTimestamp("updated_at"))

                                        .scheduleAssumeStartDate(
                                                rs.getString("assume_start_date"))

                                        .scheduleAssumeEndDate(
                                                rs.getString("assume_end_date"))

                                        .scheduleDurationHoursStart(
                                                rs.getBigDecimal("duration_hours_start"))

                                        .scheduleDurationHoursEnd(
                                                rs.getBigDecimal("duration_hours_end"))

                                        .scheduleSpecialNote(
                                                rs.getString("special_note"))

                                        .scheduleDescription(
                                                rs.getString("schedule_description"))

                                        .scheduleStatus(
                                                rs.getString("schedule_status"))

                                        .activityCategoryDtos(
                                                getCategoriesByActivityId(activityId))

                                        .images(
                                                getImagesByActivityId(activityId))

                                        .build();
                            });

            return new ActivityScheduleWithParamsResponse(
                    totalCount != null ? totalCount : 0,
                    result
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching activity schedules: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activity schedules from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching activity schedules: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching activity schedules"
            );
        }
    }

    @Override
    public List<String> getDistinctActivityDurations() {

        try {

            return jdbcTemplate.query(
                    ActivitiesQueries.GET_DISTINCT_ACTIVITY_DURATIONS,
                    (rs, rowNum) -> rs.getBigDecimal("duration_hours")
                            .stripTrailingZeros()
                            .toPlainString()
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching distinct activity durations: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch distinct activity durations from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching distinct activity durations: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching distinct activity durations"
            );
        }
    }

    @Override
    public ActivityScheduleDetailsResponse getActivityScheduleDetailsById(
            CommonIdRequest activityScheduleId) {

        try {

            ActivityScheduleDetailsResponse response = jdbcTemplate.queryForObject(
                    ActivitiesQueries.GET_ACTIVITY_SCHEDULE_DETAILS_BY_ID,
                    new Object[]{activityScheduleId.getId()},
                    (rs, rowNum) -> ActivityScheduleDetailsResponse.builder()

                            // =====================================================
                            // ACTIVITY SCHEDULE
                            // =====================================================

                            .activityScheduleId(
                                    rs.getLong("activity_schedule_id"))

                            .activityScheduleName(
                                    rs.getString("activity_schedule_name"))

                            .scheduleAssumeStartDate(
                                    rs.getString("assume_start_date"))

                            .scheduleAssumeEndDate(
                                    rs.getString("assume_end_date"))

                            .scheduleDurationHoursStart(
                                    rs.getBigDecimal("duration_hours_start"))

                            .scheduleDurationHoursEnd(
                                    rs.getBigDecimal("duration_hours_end"))

                            .scheduleSpecialNote(
                                    rs.getString("special_note"))

                            .scheduleDescription(
                                    rs.getString("schedule_description"))

                            .scheduleStatus(
                                    rs.getString("schedule_status"))

                            .scheduleCreatedAt(
                                    rs.getTimestamp("schedule_created_at"))

                            .scheduleUpdatedAt(
                                    rs.getTimestamp("schedule_updated_at"))

                            // =====================================================
                            // ACTIVITY
                            // =====================================================

                            .activityId(
                                    rs.getLong("activity_id"))

                            .activityName(
                                    rs.getString("activity_name"))

                            .activityDescription(
                                    rs.getString("activity_description"))

                            .durationHours(
                                    rs.getBigDecimal("duration_hours"))

                            .availableFrom(
                                    rs.getTime("available_from"))

                            .availableTo(
                                    rs.getTime("available_to"))

                            .priceLocal(
                                    rs.getBigDecimal("price_local"))

                            .priceForeigners(
                                    rs.getBigDecimal("price_foreigners"))

                            .minParticipate(
                                    rs.getInt("min_participate"))

                            .maxParticipate(
                                    rs.getInt("max_participate"))

                            .seasonId(
                                    rs.getLong("season_id"))

                            .season(
                                    rs.getString("season"))

                            .activityStatus(
                                    rs.getString("activity_status"))

                            .activityCreatedAt(
                                    rs.getTimestamp("activity_created_at"))

                            .activityUpdatedAt(
                                    rs.getTimestamp("activity_updated_at"))

                            // =====================================================
                            // DESTINATION
                            // =====================================================

                            .destinationId(
                                    rs.getLong("destination_id"))

                            .destinationName(
                                    rs.getString("destination_name"))

                            // =====================================================
                            // TOUR
                            // =====================================================

                            .tourId(
                                    rs.getLong("tour_id"))

                            .tourName(
                                    rs.getString("tour_name"))

                            .tourDescription(
                                    rs.getString("tour_description"))

                            .tourDuration(
                                    rs.getInt("tour_duration"))

                            .startLocation(
                                    rs.getString("start_location"))

                            .endLocation(
                                    rs.getString("end_location"))

                            .tourStatus(
                                    rs.getString("tour_status"))

                            // =====================================================
                            // TOUR SCHEDULE
                            // =====================================================

                            .tourScheduleId(
                                    rs.getLong("tour_schedule_id"))

                            .tourScheduleName(
                                    rs.getString("tour_schedule_name"))

                            .tourScheduleStartDate(
                                    rs.getString("tour_schedule_start_date"))

                            .tourScheduleEndDate(
                                    rs.getString("tour_schedule_end_date"))

                            .tourScheduleDurationStart(
                                    rs.getInt("tour_schedule_duration_start"))

                            .tourScheduleDurationEnd(
                                    rs.getInt("tour_schedule_duration_end"))

                            .tourScheduleStatus(
                                    rs.getString("tour_schedule_status"))

                            // =====================================================
                            // PACKAGE
                            // =====================================================

                            .packageId(
                                    rs.getLong("package_id"))

                            .packageName(
                                    rs.getString("package_name"))

                            .packageDescription(
                                    rs.getString("package_description"))

                            .totalPrice(
                                    rs.getBigDecimal("total_price"))

                            .discountPercentage(
                                    rs.getBigDecimal("discount_percentage"))

                            .pricePerPerson(
                                    rs.getBigDecimal("price_per_person"))

                            .minPersonCount(
                                    rs.getInt("min_person_count"))

                            .maxPersonCount(
                                    rs.getInt("max_person_count"))

                            .packageStatus(
                                    rs.getString("package_status"))

                            // =====================================================
                            // PACKAGE SCHEDULE
                            // =====================================================

                            .packageScheduleId(
                                    rs.getLong("package_schedule_id"))

                            .packageScheduleName(
                                    rs.getString("package_schedule_name"))

                            .packageScheduleStartDate(
                                    rs.getString("package_schedule_start_date"))

                            .packageScheduleEndDate(
                                    rs.getString("package_schedule_end_date"))

                            .packageScheduleDurationStart(
                                    rs.getInt("package_schedule_duration_start"))

                            .packageScheduleDurationEnd(
                                    rs.getInt("package_schedule_duration_end"))

                            .packageScheduleStatus(
                                    rs.getString("package_schedule_status"))

                            // =====================================================
                            // EXTRA DETAILS
                            // =====================================================

                            .activityCategoryDtos(
                                    getCategoriesByActivityId(
                                            rs.getLong("activity_id")
                                    ))

                            .images(
                                    getImagesByActivityId(
                                            rs.getLong("activity_id")
                                    ))

                            .build()
            );

            return response;

        } catch (EmptyResultDataAccessException ex) {

            LOGGER.error(
                    "No activity schedule found for id: {}",
                    activityScheduleId.getId(),
                    ex
            );

            throw new DataNotFoundErrorExceptionHandler(
                    "Activity schedule not found"
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching activity schedule details: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activity schedule details from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching activity schedule details: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching activity schedule details"
            );
        }
    }

    @Override
    public Long createActivitySchedule(
            ActivityScheduleInsertRequest request,
            Long userId) {

        try {

            // =====================================================
            // STATUS
            // =====================================================

            Long statusId = statusRepository.getStatusIdByName(
                    request.getStatus()
            );

            if (statusId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Invalid status : " + request.getStatus()
                );
            }

            // =====================================================
            // INSERT
            // =====================================================

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {

                PreparedStatement ps = connection.prepareStatement(
                        ActivitiesQueries.INSERT_ACTIVITY_SCHEDULE,
                        Statement.RETURN_GENERATED_KEYS
                );

                // =====================================================
                // BASIC DETAILS
                // =====================================================

                ps.setString(
                        1,
                        request.getActivityScheduleName()
                );

                ps.setLong(
                        2,
                        request.getActivityId()
                );

                // =====================================================
                // ASSUME START DATE
                // =====================================================

                if (request.getAssumeStartDate() != null) {

                    ps.setDate(
                            3,
                            new java.sql.Date(
                                    request.getAssumeStartDate().getTime()
                            )
                    );

                } else {

                    ps.setNull(3, Types.DATE);
                }

                // =====================================================
                // ASSUME END DATE
                // =====================================================

                if (request.getAssumeEndDate() != null) {

                    ps.setDate(
                            4,
                            new java.sql.Date(
                                    request.getAssumeEndDate().getTime()
                            )
                    );

                } else {

                    ps.setNull(4, Types.DATE);
                }

                // =====================================================
                // DURATION START
                // =====================================================

                if (request.getDurationHoursStart() != null) {

                    ps.setBigDecimal(
                            5,
                            BigDecimal.valueOf(
                                    request.getDurationHoursStart()
                            )
                    );

                } else {

                    ps.setNull(5, Types.DECIMAL);
                }

                // =====================================================
                // DURATION END
                // =====================================================

                if (request.getDurationHoursEnd() != null) {

                    ps.setBigDecimal(
                            6,
                            BigDecimal.valueOf(
                                    request.getDurationHoursEnd()
                            )
                    );

                } else {

                    ps.setNull(6, Types.DECIMAL);
                }

                // =====================================================
                // SPECIAL NOTE
                // =====================================================

                if (request.getSpecialNotes() != null) {

                    ps.setString(
                            7,
                            request.getSpecialNotes()
                    );

                } else {

                    ps.setNull(7, Types.VARCHAR);
                }

                // =====================================================
                // DESCRIPTION
                // =====================================================

                if (request.getDescription() != null) {

                    ps.setString(
                            8,
                            request.getDescription()
                    );

                } else {

                    ps.setNull(8, Types.VARCHAR);
                }

                // =====================================================
                // PACKAGE SCHEDULE ID
                // =====================================================

                if (request.getPackageScheduleId() != null) {

                    ps.setLong(
                            9,
                            request.getPackageScheduleId()
                    );

                } else {

                    ps.setNull(9, Types.BIGINT);
                }

                // =====================================================
                // TOUR SCHEDULE ID
                // =====================================================

                if (request.getTourScheduleId() != null) {

                    ps.setLong(
                            10,
                            request.getTourScheduleId()
                    );

                } else {

                    ps.setNull(10, Types.BIGINT);
                }

                // =====================================================
                // STATUS
                // =====================================================

                ps.setLong(
                        11,
                        statusId
                );

                // =====================================================
                // CREATED BY
                // =====================================================

                if (userId != null) {

                    ps.setLong(
                            12,
                            userId
                    );

                } else {

                    ps.setNull(12, Types.BIGINT);
                }

                // =====================================================
                // UPDATED BY
                // =====================================================

                if (userId != null) {

                    ps.setLong(
                            13,
                            userId
                    );

                } else {

                    ps.setNull(13, Types.BIGINT);
                }

                return ps;

            }, keyHolder);

            // =====================================================
            // GENERATED ID
            // =====================================================

            Number generatedId = keyHolder.getKey();

            if (generatedId == null) {

                throw new InternalServerErrorExceptionHandler(
                        "Failed to generate activity schedule id"
                );
            }

            return generatedId.longValue();

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while creating activity schedule: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to create activity schedule"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while creating activity schedule: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while creating activity schedule"
            );
        }
    }

    @Override
    public ActivityScheduleBasicDetailsDTO getActivityScheduleBasicDetails(
            Long activityScheduleId) {

        try {

            return jdbcTemplate.queryForObject(
                    ActivitiesQueries.GET_ACTIVITY_SCHEDULE_BASIC_DETAILS,
                    new Object[]{activityScheduleId},
                    (rs, rowNum) -> ActivityScheduleBasicDetailsDTO.builder()

                            .activityScheduleId(
                                    rs.getLong("activity_schedule_id"))

                            .activityScheduleName(
                                    rs.getString("activity_schedule_name"))

                            .activityId(
                                    rs.getLong("activity_id"))

                            .assumeStartDate(
                                    rs.getDate("assume_start_date"))

                            .assumeEndDate(
                                    rs.getDate("assume_end_date"))

                            .durationHoursStart(
                                    rs.getObject(
                                            "duration_hours_start",
                                            Double.class
                                    ))

                            .durationHoursEnd(
                                    rs.getObject(
                                            "duration_hours_end",
                                            Double.class
                                    ))

                            .specialNotes(
                                    rs.getString("special_note"))

                            .description(
                                    rs.getString("description"))

                            .packageScheduleId(
                                    rs.getObject(
                                            "package_schedule_id",
                                            Long.class
                                    ))

                            .tourScheduleId(
                                    rs.getObject(
                                            "tour_schedule_id",
                                            Long.class
                                    ))

                            .status(
                                    rs.getString("status"))

                            .createdBy(
                                    rs.getObject(
                                            "created_by",
                                            Long.class
                                    ))

                            .createdByName(
                                    rs.getString("created_by_name"))

                            .createdAt(
                                    rs.getTimestamp("created_at"))

                            .updatedAt(
                                    rs.getTimestamp("updated_at"))

                            .updatedBy(
                                    rs.getObject(
                                            "updated_by",
                                            Long.class
                                    ))

                            .updatedByName(
                                    rs.getString("updated_by_name"))

                            .build()
            );

        } catch (EmptyResultDataAccessException ex) {

            LOGGER.error(
                    "No activity schedule found for id: {}",
                    activityScheduleId,
                    ex
            );

            throw new DataNotFoundErrorExceptionHandler(
                    "Activity schedule not found"
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching activity schedule basic details: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activity schedule basic details from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching activity schedule basic details: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching activity schedule basic details"
            );
        }
    }

    @Override
    public void updateActivitySchedule(
            ActivityScheduleUpdateRequest request) {

        try {

            // =====================================================
            // STATUS
            // =====================================================

            Long statusId = statusRepository.getStatusIdByName(
                    request.getStatus()
            );

            if (statusId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Invalid status : " + request.getStatus()
                );
            }

            // =====================================================
            // UPDATE
            // =====================================================

            int updatedRows = jdbcTemplate.update(
                    ActivitiesQueries.UPDATE_ACTIVITY_SCHEDULE,

                    // name
                    request.getActivityScheduleName(),

                    // activity_id
                    request.getActivityId(),

                    // assume_start_date
                    request.getAssumeStartDate() != null
                            ? new java.sql.Date(
                            request.getAssumeStartDate().getTime())
                            : null,

                    // assume_end_date
                    request.getAssumeEndDate() != null
                            ? new java.sql.Date(
                            request.getAssumeEndDate().getTime())
                            : null,

                    // duration_hours_start
                    request.getDurationHoursStart() != null
                            ? BigDecimal.valueOf(
                            request.getDurationHoursStart())
                            : null,

                    // duration_hours_end
                    request.getDurationHoursEnd() != null
                            ? BigDecimal.valueOf(
                            request.getDurationHoursEnd())
                            : null,

                    // special_note
                    request.getSpecialNotes(),

                    // description
                    request.getDescription(),

                    // package_schedule_id
                    request.getPackageScheduleId(),

                    // tour_schedule_id
                    request.getTourScheduleId(),

                    // status
                    statusId,

                    // where id
                    request.getActivityScheduleId()
            );

            // =====================================================
            // NOT FOUND
            // =====================================================

            if (updatedRows == 0) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Activity schedule not found"
                );
            }

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while updating activity schedule: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to update activity schedule"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while updating activity schedule: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while updating activity schedule"
            );
        }
    }

    @Override
    public void terminateActivityScheduleById(CommonIdRequest commonIdRequest) {
        try {
            Long terminatedStatusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.name());
            if (terminatedStatusId == null) {
                throw new DataNotFoundErrorExceptionHandler(
                        "TERMINATED status not found"
                );
            }
            int updatedRows = jdbcTemplate.update(
                    ActivitiesQueries.TERMINATE_ACTIVITY_SCHEDULE_BY_ID,
                    terminatedStatusId,
                    commonIdRequest.getId()
            );


            if (updatedRows == 0) {
                throw new DataNotFoundErrorExceptionHandler(
                        "Activity schedule not found"
                );
            }

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Database error while terminating activity schedule: {}",
                    ex.getMessage(),
                    ex
            );

            throw new UpdateFailedErrorExceptionHandler(
                    "Failed to terminate activity schedule"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while terminating activity schedule: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while terminating activity schedule"
            );
        }
    }

    @Override
    public ActivityCategoryDetailsResponse getActivityCategoryDetailsById(
            CommonIdRequest commonIdRequest) {

        try {
            Long categoryId = commonIdRequest.getId();
            ActivityCategoryDetailsResponse response =
                    jdbcTemplate.queryForObject(
                            ActivitiesQueries.GET_ACTIVITY_CATEGORY_DETAILS_BY_ID,
                            new Object[]{categoryId},
                            (rs, rowNum) -> ActivityCategoryDetailsResponse.builder()

                                    .categoryId(
                                            rs.getLong("id"))

                                    .categoryName(
                                            rs.getString("name"))

                                    .description(
                                            rs.getString("description"))

                                    .color(
                                            rs.getString("color"))

                                    .hoverColor(
                                            rs.getString("hover_color"))

                                    .status(
                                            rs.getString("status"))

                                    .createdAt(
                                            rs.getTimestamp("created_at"))

                                    .createdBy(
                                            rs.getObject(
                                                    "created_by",
                                                    Long.class))

                                    .createdByName(
                                            rs.getString("created_by_name"))

                                    .updatedAt(
                                            rs.getTimestamp("updated_at"))

                                    .updatedBy(
                                            rs.getObject(
                                                    "updated_by",
                                                    Long.class))

                                    .updatedByName(
                                            rs.getString("updated_by_name"))

                                    .terminatedAt(
                                            rs.getTimestamp("terminated_at"))

                                    .terminatedBy(
                                            rs.getObject(
                                                    "terminated_by",
                                                    Long.class))

                                    .build()
                    );

            // =====================================================
            // IMAGES
            // =====================================================

            List<ActivityCategoryDetailsResponse.CategoryImage> images =
                    jdbcTemplate.query(
                            ActivitiesQueries.GET_ACTIVITY_CATEGORY_IMAGES_BY_CATEGORY_ID,
                            new Object[]{categoryId},
                            (rs, rowNum) ->
                                    ActivityCategoryDetailsResponse.CategoryImage
                                            .builder()

                                            .imageId(
                                                    rs.getLong("id"))

                                            .name(
                                                    rs.getString("name"))

                                            .description(
                                                    rs.getString("description"))

                                            .imageUrl(
                                                    rs.getString("image_url"))

                                            .status(
                                                    rs.getString("status"))

                                            .createdAt(
                                                    rs.getTimestamp("created_at"))

                                            .build()
                    );

            // =====================================================
            // PRIMARY ACTIVITIES
            // =====================================================

            List<ActivityCategoryDetailsResponse.Activity> primaryActivities =
                    jdbcTemplate.query(
                            ActivitiesQueries.GET_PRIMARY_ACTIVITIES_BY_CATEGORY_ID,
                            new Object[]{categoryId},
                            (rs, rowNum) ->
                                    ActivityCategoryDetailsResponse.Activity
                                            .builder()

                                            .activityId(
                                                    rs.getLong("id"))

                                            .activityName(
                                                    rs.getString("name"))

                                            .build()
                    );

            // =====================================================
            // OTHER ACTIVITIES
            // =====================================================

            List<ActivityCategoryDetailsResponse.Activity> otherActivities =
                    jdbcTemplate.query(
                            ActivitiesQueries.GET_OTHER_ACTIVITIES_BY_CATEGORY_ID,
                            new Object[]{categoryId},
                            (rs, rowNum) ->
                                    ActivityCategoryDetailsResponse.Activity
                                            .builder()

                                            .activityId(
                                                    rs.getLong("id"))

                                            .activityName(
                                                    rs.getString("name"))

                                            .build()
                    );



            response.setImages(images);
            response.setPrimaryActivities(primaryActivities);
            response.setOtherActivities(otherActivities);

            return response;

        } catch (EmptyResultDataAccessException ex) {

            LOGGER.error(
                    "Activity category not found for id: {}",
                    commonIdRequest.getId(),
                    ex
            );

            throw new DataNotFoundErrorExceptionHandler(
                    "Activity category not found"
            );

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while fetching activity category details: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activity category details from database"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while fetching activity category details: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching activity category details"
            );
        }
    }

    @Override
    public void terminateActivityCategory(
            CommonIdRequest commonIdRequest) {

        try {
            Long terminatedStatusId =
                    statusRepository.getStatusIdByName("TERMINATED");

            if (terminatedStatusId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "TERMINATED status not found"
                );
            }

            int updatedRows = jdbcTemplate.update(
                    ActivitiesQueries.TERMINATE_ACTIVITY_CATEGORY,
                    terminatedStatusId,
                    commonIdRequest.getId()
            );


            if (updatedRows == 0) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Activity category not found"
                );
            }

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while terminating activity category: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to terminate activity category"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while terminating activity category: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while terminating activity category"
            );
        }
    }

    @Override
    public Long insertActivityCategoryBasicDetails(
            ActivityCategoryInsertRequest request) {

        try {

            Long statusId = statusRepository.getStatusIdByName(
                    request.getStatus()
            );

            if (statusId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Invalid status : " + request.getStatus()
                );
            }


            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {

                PreparedStatement ps = connection.prepareStatement(
                        ActivitiesQueries.INSERT_ACTIVITY_CATEGORY,
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, request.getCategoryName());
                ps.setString(2, request.getDescription());
                ps.setString(3, request.getColor());
                ps.setString(4, request.getHoverColor());
                ps.setLong(5, statusId);

                return ps;

            }, keyHolder);

            Number key = keyHolder.getKey();

            if (key == null) {

                throw new InternalServerErrorExceptionHandler(
                        "Failed to generate activity category id"
                );
            }

            return key.longValue();

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while inserting activity category: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to insert activity category"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while inserting activity category: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while inserting activity category"
            );
        }
    }

    @Override
    public void insertActivityCategoryImages(
            Long activityCategoryId,
            List<ActivityCategoryImageRequest> images) {

        try {

            if (activityCategoryId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Activity category id is required"
                );
            }

            if (images == null || images.isEmpty()) {
                return;
            }

            for (ActivityCategoryImageRequest image : images) {

                Long imageStatusId = statusRepository.getStatusIdByName(
                        image.getStatus()
                );

                if (imageStatusId == null) {

                    throw new DataNotFoundErrorExceptionHandler(
                            "Invalid image status : " + image.getStatus()
                    );
                }

                jdbcTemplate.update(
                        ActivitiesQueries.INSERT_ACTIVITY_CATEGORY_IMAGE,

                        activityCategoryId,
                        image.getName(),
                        image.getDescription(),
                        image.getImageUrl(),
                        imageStatusId
                );
            }

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while inserting activity category images: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to insert activity category images"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while inserting activity category images: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while inserting activity category images"
            );
        }
    }


    @Override
    public void addActivityCategoryForActivities(
            Long activityCategoryId,
            List<Long> activityIds) {

        try {

            if (activityCategoryId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Activity category id is required"
                );
            }

            if (activityIds == null || activityIds.isEmpty()) {
                return;
            }

            Long activeStatusId = statusRepository.getStatusIdByName(
                    "ACTIVE"
            );

            if (activeStatusId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "ACTIVE status not found"
                );
            }

            for (Long activityId : activityIds) {

                jdbcTemplate.update(
                        ActivitiesQueries.INSERT_ACTIVITY_CATEGORY_MAP_FOR_CATEGORY,

                        activityId,
                        activityCategoryId,
                        false,
                        activeStatusId
                );
            }

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while mapping activity category for activities: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to map activity category for activities"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while mapping activity category for activities: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while mapping activity category for activities"
            );
        }
    }

    @Override
    public void updateActivityCategorybasicDetails(
            ActivityCategoryUpdateRequest request) {

        try {

            // =====================================================
            // STATUS
            // =====================================================

            Long statusId = statusRepository.getStatusIdByName(
                    request.getStatus()
            );

            if (statusId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Invalid status : " + request.getStatus()
                );
            }

            // =====================================================
            // UPDATE
            // =====================================================

            int updatedRows = jdbcTemplate.update(
                    ActivitiesQueries.UPDATE_ACTIVITY_CATEGORY_BASIC_DETAILS,

                    request.getCategoryName(),
                    request.getDescription(),
                    request.getColor(),
                    request.getHoverColor(),
                    statusId,
                    request.getCategoryId()
            );

            if (updatedRows == 0) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Activity category not found"
                );
            }

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while updating activity category: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to update activity category"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while updating activity category: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while updating activity category"
            );
        }
    }

    @Override
    public void removeActivityCategoryForActivities(
            Long categoryId,
            List<Long> removeActivityIds) {

        try {

            if (categoryId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Category id is required"
                );
            }

            if (removeActivityIds == null ||
                    removeActivityIds.isEmpty()) {

                return;
            }

            for (Long activityId : removeActivityIds) {

                jdbcTemplate.update(
                        ActivitiesQueries.REMOVE_ACTIVITY_CATEGORY_FOR_ACTIVITY,
                        categoryId,
                        activityId
                );
            }

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while removing activity mappings: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to remove activity mappings"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while removing activity mappings: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while removing activity mappings"
            );
        }
    }

    @Override
    public void removeActivityCategoryImages(
            Long categoryId,
            List<Long> removeImageIds) {

        try {

            if (categoryId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Category id is required"
                );
            }

            if (removeImageIds == null ||
                    removeImageIds.isEmpty()) {

                return;
            }

            for (Long imageId : removeImageIds) {

                jdbcTemplate.update(
                        ActivitiesQueries.REMOVE_ACTIVITY_CATEGORY_IMAGE,
                        categoryId,
                        imageId
                );
            }

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while removing activity category images: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to remove activity category images"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while removing activity category images: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while removing activity category images"
            );
        }
    }

    @Override
    public void updateActivityCategoryImages(
            Long categoryId,
            List<ActivityCategoryImageUpdateRequest> updateImages) {

        try {

            if (categoryId == null) {

                throw new DataNotFoundErrorExceptionHandler(
                        "Category id is required"
                );
            }

            if (updateImages == null ||
                    updateImages.isEmpty()) {

                return;
            }

            for (ActivityCategoryImageUpdateRequest image : updateImages) {

                if (image.getImageId() == null) {

                    throw new DataNotFoundErrorExceptionHandler(
                            "Image id is required"
                    );
                }

                Long statusId = statusRepository.getStatusIdByName(
                        image.getStatus()
                );

                if (statusId == null) {

                    throw new DataNotFoundErrorExceptionHandler(
                            "Invalid image status : " + image.getStatus()
                    );
                }

                jdbcTemplate.update(
                        ActivitiesQueries.UPDATE_ACTIVITY_CATEGORY_IMAGE,

                        image.getName(),
                        image.getDescription(),
                        image.getImageUrl(),
                        statusId,
                        image.getImageId(),
                        categoryId
                );
            }

        } catch (DataAccessException ex) {

            LOGGER.error(
                    "Database error while updating activity category images: {}",
                    ex.getMessage(),
                    ex
            );

            throw new DataAccessErrorExceptionHandler(
                    "Failed to update activity category images"
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error while updating activity category images: {}",
                    ex.getMessage(),
                    ex
            );

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while updating activity category images"
            );
        }
    }

    private List<ActivityCategoryDto> getCategoriesByActivityId(Long activityId) {

        return jdbcTemplate.query(
                ActivitiesQueries.ACTIVITY_CATEGORIES_QUERY,
                new Object[]{activityId},
                (rs, rowNum) -> ActivityCategoryDto.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .isPrimary(rs.getBoolean("is_primary"))
                        .build()
        );
    }

    private List<ActivityImageDto> getImagesByActivityId(Long activityId) {

        return jdbcTemplate.query(
                ActivitiesQueries.ACTIVITY_IMAGES_QUERY,
                new Object[]{activityId},
                (rs, rowNum) -> new ActivityImageDto(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getInt("status")
                )
        );
    }

    private String mapSortColumn(String sortBy) {

        if (sortBy == null ||
                !Sortings.ALLOWED_ACTIVITY_SCHEDULE_SORT_COLUMNS.contains(sortBy)) {

            return "a.created_at";
        }

        return switch (sortBy) {

            case "activityName" -> "a.name";
            case "activityScheduleName" -> "s.name";
            case "destinationName" -> "d.name";
            case "durationHours" -> "a.duration_hours";
            case "season" -> "a.season";
            case "status" -> "cs.name";
            case "scheduleAssumeStartDate" -> "s.assume_start_date";
            case "scheduleAssumeEndDate" -> "s.assume_end_date";
            case "createdAt" -> "a.created_at";
            case "updatedAt" -> "a.updated_at";

            default -> "a.created_at";
        };
    }


    private LocalDateTime getLocalDateTime(ResultSet rs, String column) {
        try {
            Timestamp ts = rs.getTimestamp(column);
            return ts != null ? ts.toLocalDateTime() : null;
        } catch (SQLException e) {
            return null;
        }
    }

    private Double getDouble(ResultSet rs, String column) {
        try {
            double value = rs.getDouble(column);
            return rs.wasNull() ? null : value;
        } catch (SQLException e) {
            return null;
        }
    }

    private class ActivityRowMapper implements RowMapper<ActivityResponseDto> {

        @Override
        public ActivityResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {

            ActivityResponseDto activity = new ActivityResponseDto();

            try {
                // Basic fields
                activity.setId(rs.getLong("id"));
                activity.setDestinationId(rs.getInt("destination_id"));
                activity.setDestinationName(rs.getString("destination_name"));
                activity.setName(rs.getString("name"));
                activity.setDescription(rs.getString("description"));
                activity.setDurationHours(rs.getBigDecimal("duration_hours"));
                activity.setAvailableFrom(rs.getTime("available_from"));
                activity.setAvailableTo(rs.getTime("available_to"));
                activity.setPriceLocal(rs.getBigDecimal("price_local"));
                activity.setPriceForeigners(rs.getBigDecimal("price_foreigners"));
                activity.setMinParticipate(rs.getInt("min_participate"));
                activity.setMaxParticipate(rs.getInt("max_participate"));
                activity.setSeason(rs.getString("season"));
                activity.setStatus(rs.getString("status_name"));
                activity.setCreatedAt(rs.getTimestamp("created_at"));
                activity.setUpdatedAt(rs.getTimestamp("updated_at"));

                // Categories
                List<ActivityCategoryDto> categories;
                try {
                    Object obj = rs.getObject("categories");
                    String categoriesJson = obj != null ? obj.toString() : null;

                    categories = (categoriesJson != null && !categoriesJson.equals("[]"))
                            ? objectMapper.readValue(categoriesJson, new TypeReference<List<ActivityCategoryDto>>() {
                    })
                            : List.of();
                } catch (Exception e) {
                    LOGGER.warn("Error parsing categories JSON for activity id {}: {}", rs.getLong("id"), e.getMessage());
                    categories = List.of();
                }
                activity.setActivityCategoryDtos(categories);

// Schedules
                List<ActivityScheduleDto> schedules;
                try {
                    String schedulesJson = rs.getString("schedules");
                    schedules = (schedulesJson != null && !schedulesJson.equals("[]"))
                            ? objectMapper.readValue(schedulesJson, new TypeReference<List<ActivityScheduleDto>>() {
                    })
                            : List.of();
                } catch (Exception e) {
                    LOGGER.warn("Error parsing schedules JSON for activity id {}: {}", rs.getLong("id"), e.getMessage());
                    schedules = List.of();
                }
                activity.setSchedules(schedules);

// Requirements
                List<ActivityRequirementDto> requirements;
                try {
                    String requirementsJson = rs.getString("requirements");
                    requirements = (requirementsJson != null && !requirementsJson.equals("[]"))
                            ? objectMapper.readValue(requirementsJson, new TypeReference<List<ActivityRequirementDto>>() {
                    })
                            : List.of();
                } catch (Exception e) {
                    LOGGER.warn("Error parsing requirements JSON for activity id {}: {}", rs.getLong("id"), e.getMessage());
                    requirements = List.of();
                }
                activity.setRequirements(requirements);

// Images
                List<ActivityImageDto> images;
                try {
                    String imagesJson = rs.getString("images");
                    images = (imagesJson != null && !imagesJson.equals("[]"))
                            ? objectMapper.readValue(imagesJson, new TypeReference<List<ActivityImageDto>>() {
                    })
                            : List.of();
                } catch (Exception e) {
                    LOGGER.warn("Error parsing images JSON for activity id {}: {}", rs.getLong("id"), e.getMessage());
                    images = List.of();
                }
                activity.setImages(images);

            } catch (Exception e) {
                throw new SQLException("Error parsing JSON for activity id: " + activity.getId(), e);
            }

            return activity;
        }
    }

}