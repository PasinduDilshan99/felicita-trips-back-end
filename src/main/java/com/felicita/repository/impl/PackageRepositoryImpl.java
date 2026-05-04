package com.felicita.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felicita.exception.*;
import com.felicita.model.dto.*;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.PackageScheduleStatisticsResponse;
import com.felicita.model.response.statistics.PackageStatisticsResponse;
import com.felicita.model.response.statistics.PackageTypeStatisticsResponse;
import com.felicita.queries.PackageQueries;
import com.felicita.repository.PackageRepository;
import com.felicita.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PackageRepositoryImpl implements PackageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;

    @Autowired
    public PackageRepositoryImpl(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<PackageResponseDto> getAllPackages() {
        try {
            return jdbcTemplate.query(PackageQueries.GET_ALL_PACKAGES, (ResultSet rs) -> {
                Map<Long, PackageResponseDto> packageMap = new HashMap<>();

                while (rs.next()) {
                    Long packageId = rs.getLong("package_id");

                    // Get or create package
                    PackageResponseDto pkg = packageMap.get(packageId);
                    if (pkg == null) {
                        pkg = new PackageResponseDto();
                        pkg.setPackageId(packageId);
                        pkg.setPackageName(rs.getString("package_name"));
                        pkg.setPackageDescription(rs.getString("package_description"));
                        pkg.setTotalPrice(rs.getBigDecimal("total_price"));
                        pkg.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
                        pkg.setStartDate(rs.getObject("start_date", LocalDate.class));
                        pkg.setEndDate(rs.getObject("end_date", LocalDate.class));
                        pkg.setColor(rs.getString("color"));
                        pkg.setHoverColor(rs.getString("hover_color"));
                        pkg.setMinPersonCount(rs.getObject("min_person_count", Integer.class));
                        pkg.setMaxPersonCount(rs.getObject("max_person_count", Integer.class));
                        pkg.setPricePerPerson(rs.getBigDecimal("price_per_person"));
                        pkg.setPackageStatus(rs.getString("package_status"));

                        pkg.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                        pkg.setCreatedBy(rs.getObject("created_by", Integer.class));
                        pkg.setPackageTypeName(rs.getString("package_type_name"));
                        pkg.setPackageTypeDescription(rs.getString("package_type_description"));
                        pkg.setPackageTypeStatus(rs.getString("package_type_status"));

                        // Tour info
                        pkg.setTourId(rs.getLong("tour_id"));
                        pkg.setTourName(rs.getString("tour_name"));
                        pkg.setTourDescription(rs.getString("tour_description"));
                        pkg.setDuration(rs.getObject("duration", Integer.class));
                        pkg.setLatitude(rs.getObject("latitude", Double.class));
                        pkg.setLongitude(rs.getObject("longitude", Double.class));
                        pkg.setStartLocation(rs.getString("start_location"));
                        pkg.setEndLocation(rs.getString("end_location"));
                        pkg.setTourStatus(rs.getString("tour_status"));

                        pkg.setSchedules(new ArrayList<>());
                        pkg.setFeatures(new ArrayList<>());
                        pkg.setImages(new ArrayList<>());
                        packageMap.put(packageId, pkg);
                    }

                    // Schedule
                    int scheduleId = rs.getInt("schedule_id");
                    if (scheduleId != 0 && rs.getString("schedule_name") != null) {
                        PackageScheduleResponseDto schedule = new PackageScheduleResponseDto();
                        schedule.setScheduleId(scheduleId);
                        schedule.setScheduleName(rs.getString("schedule_name"));
                        schedule.setAssumeStartDate(rs.getObject("assume_start_date", LocalDate.class));
                        schedule.setAssumeEndDate(rs.getObject("assume_end_date", LocalDate.class));
                        schedule.setDurationStart(rs.getObject("duration_start", Integer.class));
                        schedule.setDurationEnd(rs.getObject("duration_end", Integer.class));
                        schedule.setSpecialNote(rs.getString("schedule_special_note"));
                        schedule.setScheduleDescription(rs.getString("schedule_description"));

                        if (pkg.getSchedules().stream().noneMatch(s -> s.getScheduleId() == scheduleId)) {
                            pkg.getSchedules().add(schedule);
                        }
                    }

                    // Feature
                    int featureId = rs.getInt("feature_id");
                    if (featureId != 0 && rs.getString("feature_name") != null) {
                        PackageFeatureResponseDto feature = new PackageFeatureResponseDto();
                        feature.setFeatureId(featureId);
                        feature.setFeatureName(rs.getString("feature_name"));
                        feature.setFeatureValue(rs.getString("feature_value"));
                        feature.setFeatureDescription(rs.getString("feature_description"));
                        feature.setColor(rs.getString("feature_color"));
                        feature.setSpecialNote(rs.getString("feature_special_note"));

                        if (pkg.getFeatures().stream().noneMatch(f -> f.getFeatureId() == featureId)) {
                            pkg.getFeatures().add(feature);
                        }
                    }

                    // Image
                    int imageId = rs.getInt("image_id");
                    if (imageId != 0 && rs.getString("image_url") != null) {
                        PackageImageResponseDto image = new PackageImageResponseDto();
                        image.setImageId(imageId);
                        image.setImageName(rs.getString("image_name"));
                        image.setImageDescription(rs.getString("image_description"));
                        image.setImageUrl(rs.getString("image_url"));
                        image.setColor(rs.getString("image_color"));

                        if (pkg.getImages().stream().noneMatch(i -> i.getImageId() == imageId)) {
                            pkg.getImages().add(image);
                        }
                    }
                }

                return new ArrayList<>(packageMap.values());
            });
        } catch (DataAccessException ex) {
            LOGGER.error(ex.toString());
            throw new DataAccessErrorExceptionHandler("Database error while fetching packages");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching packages");
        }
    }

    @Override
    public PackageResponseDto getPackageDetailsById(Long packageId) {
        try {
            return jdbcTemplate.query(PackageQueries.GET_PACKAGE_DETAILS_BY_PACKAGE_ID, new Object[]{packageId}, (ResultSet rs) -> {
                Map<Long, PackageResponseDto> packageMap = new HashMap<>();

                while (rs.next()) {
                    Long pkgId = rs.getLong("package_id");

                    // Get or create package object
                    PackageResponseDto pkg = packageMap.get(pkgId);
                    if (pkg == null) {
                        pkg = new PackageResponseDto();
                        pkg.setPackageId(pkgId);
                        pkg.setPackageName(rs.getString("package_name"));
                        pkg.setPackageDescription(rs.getString("package_description"));
                        pkg.setTotalPrice(rs.getBigDecimal("total_price"));
                        pkg.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
                        pkg.setStartDate(rs.getObject("start_date", LocalDate.class));
                        pkg.setEndDate(rs.getObject("end_date", LocalDate.class));
                        pkg.setColor(rs.getString("color"));
                        pkg.setHoverColor(rs.getString("hover_color"));
                        pkg.setMinPersonCount(rs.getObject("min_person_count", Integer.class));
                        pkg.setMaxPersonCount(rs.getObject("max_person_count", Integer.class));
                        pkg.setPricePerPerson(rs.getBigDecimal("price_per_person"));
                        pkg.setPackageStatus(rs.getString("package_status"));
                        pkg.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                        pkg.setCreatedBy(rs.getObject("created_by", Integer.class));

                        // Package type info
                        pkg.setPackageTypeName(rs.getString("package_type_name"));
                        pkg.setPackageTypeDescription(rs.getString("package_type_description"));
                        pkg.setPackageTypeStatus(rs.getString("package_type_status"));

                        // Tour info
                        pkg.setTourId(rs.getObject("tour_id", Long.class));
                        pkg.setTourName(rs.getString("tour_name"));
                        pkg.setTourDescription(rs.getString("tour_description"));
                        pkg.setDuration(rs.getObject("duration", Integer.class));
                        pkg.setLatitude(rs.getObject("latitude", Double.class));
                        pkg.setLongitude(rs.getObject("longitude", Double.class));
                        pkg.setStartLocation(rs.getString("start_location"));
                        pkg.setEndLocation(rs.getString("end_location"));
                        pkg.setTourStatus(rs.getString("tour_status"));

                        // Initialize nested lists
                        pkg.setSchedules(new ArrayList<>());
                        pkg.setFeatures(new ArrayList<>());
                        pkg.setImages(new ArrayList<>());

                        packageMap.put(pkgId, pkg);
                    }

                    // Schedule
                    int scheduleId = rs.getInt("schedule_id");
                    if (!rs.wasNull() && rs.getString("schedule_name") != null) {
                        if (pkg.getSchedules().stream().noneMatch(s -> s.getScheduleId() == scheduleId)) {
                            PackageScheduleResponseDto schedule = new PackageScheduleResponseDto();
                            schedule.setScheduleId(scheduleId);
                            schedule.setScheduleName(rs.getString("schedule_name"));
                            schedule.setAssumeStartDate(rs.getObject("assume_start_date", LocalDate.class));
                            schedule.setAssumeEndDate(rs.getObject("assume_end_date", LocalDate.class));
                            schedule.setDurationStart(rs.getObject("duration_start", Integer.class));
                            schedule.setDurationEnd(rs.getObject("duration_end", Integer.class));
                            schedule.setSpecialNote(rs.getString("schedule_special_note"));
                            schedule.setScheduleDescription(rs.getString("schedule_description"));
                            pkg.getSchedules().add(schedule);
                        }
                    }

                    // Feature
                    int featureId = rs.getInt("feature_id");
                    if (!rs.wasNull() && rs.getString("feature_name") != null) {
                        if (pkg.getFeatures().stream().noneMatch(f -> f.getFeatureId() == featureId)) {
                            PackageFeatureResponseDto feature = new PackageFeatureResponseDto();
                            feature.setFeatureId(featureId);
                            feature.setFeatureName(rs.getString("feature_name"));
                            feature.setFeatureValue(rs.getString("feature_value"));
                            feature.setFeatureDescription(rs.getString("feature_description"));
                            feature.setColor(rs.getString("feature_color"));
                            feature.setSpecialNote(rs.getString("feature_special_note"));
                            pkg.getFeatures().add(feature);
                        }
                    }

                    // Image
                    int imageId = rs.getInt("image_id");
                    if (!rs.wasNull() && rs.getString("image_url") != null) {
                        if (pkg.getImages().stream().noneMatch(i -> i.getImageId() == imageId)) {
                            PackageImageResponseDto image = new PackageImageResponseDto();
                            image.setImageId(imageId);
                            image.setImageName(rs.getString("image_name"));
                            image.setImageDescription(rs.getString("image_description"));
                            image.setImageUrl(rs.getString("image_url"));
                            image.setColor(rs.getString("image_color"));
                            pkg.getImages().add(image);
                        }
                    }
                }

                // Return the first (and only) package
                return packageMap.values().stream().findFirst().orElse(null);
            });
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package details");
        }
    }

    @Override
    public List<PackageReviewResponse> getAllPackageReviewDetails() {
        String SQL = PackageQueries.GET_PACKAGE_ALL_REVIEWS_DETAILS;

        return jdbcTemplate.query(SQL, rs -> {
            Map<Long, PackageReviewResponse> reviewMap = new LinkedHashMap<>();

            while (rs.next()) {
                Long reviewId = rs.getLong("review_id");

                // ------------------- PACKAGE REVIEW -------------------
                PackageReviewResponse review = reviewMap.get(reviewId);
                if (review == null) {
                    review = PackageReviewResponse.builder()
                            .reviewId(reviewId)
                            .packageId(rs.getLong("package_id"))
                            .packageScheduleId(rs.getLong("package_schedule_id"))
                            .name(rs.getString("review_name"))
                            .review(rs.getString("review"))
                            .rating(rs.getDouble("rating"))
                            .description(rs.getString("description"))
                            .status(rs.getString("review_status"))
                            .numberOfParticipate(rs.getInt("number_of_participate"))
                            .createdBy(rs.getLong("review_created_by"))
                            .createdAt(rs.getTimestamp("review_created_at") != null
                                    ? rs.getTimestamp("review_created_at").toLocalDateTime() : null)
                            .updatedBy(rs.getLong("review_updated_by"))
                            .updatedAt(rs.getTimestamp("review_updated_at") != null
                                    ? rs.getTimestamp("review_updated_at").toLocalDateTime() : null)
                            .images(new ArrayList<>())
                            .reactions(new ArrayList<>())
                            .comments(new ArrayList<>())
                            .build();

                    reviewMap.put(reviewId, review);
                }

                // ------------------- IMAGES -------------------
                Long imageId = rs.getLong("image_id");
                if (imageId != 0 && review.getImages().stream().noneMatch(i -> i.getId().equals(imageId))) {
                    review.getImages().add(
                            PackageReviewResponse.Image.builder()
                                    .id(imageId)
                                    .name(rs.getString("image_name"))
                                    .description(rs.getString("image_description"))
                                    .imageUrl(rs.getString("image_url"))
                                    .status(rs.getString("image_status"))
                                    .createdBy(rs.getLong("image_created_by"))
                                    .createdAt(rs.getTimestamp("image_created_at") != null
                                            ? rs.getTimestamp("image_created_at").toLocalDateTime() : null)
                                    .build()
                    );
                }

                // ------------------- REVIEW REACTIONS -------------------
                Long reactionId = rs.getLong("review_reaction_id");
                if (reactionId != 0 && review.getReactions().stream().noneMatch(r -> r.getId().equals(reactionId))) {
                    review.getReactions().add(
                            PackageReviewResponse.Reaction.builder()
                                    .id(reactionId)
                                    .packageReviewId(rs.getLong("reaction_review_id"))
                                    .userId(rs.getLong("reaction_user_id"))
                                    .userName(rs.getString("reaction_user_name"))
                                    .reactionType(rs.getString("reaction_type"))
                                    .status(rs.getString("review_reaction_status"))
                                    .createdAt(rs.getTimestamp("reaction_created_at") != null
                                            ? rs.getTimestamp("reaction_created_at").toLocalDateTime() : null)
                                    .build()
                    );
                }

                // ------------------- COMMENTS -------------------
                Long commentId = rs.getLong("comment_id");
                PackageReviewResponse.Comment comment = null;
                if (commentId != 0) {
                    Optional<PackageReviewResponse.Comment> existingComment = review.getComments()
                            .stream()
                            .filter(c -> c.getId().equals(commentId))
                            .findFirst();

                    if (existingComment.isPresent()) {
                        comment = existingComment.get();
                    } else {
                        comment = PackageReviewResponse.Comment.builder()
                                .id(commentId)
                                .packageReviewId(rs.getLong("comment_review_id"))
                                .userId(rs.getLong("comment_user_id"))
                                .userName(rs.getString("comment_user_name"))
                                .parentCommentId(rs.getLong("parent_comment_id"))
                                .comment(rs.getString("comment"))
                                .status(rs.getString("comment_status"))
                                .createdAt(rs.getTimestamp("comment_created_at") != null
                                        ? rs.getTimestamp("comment_created_at").toLocalDateTime() : null)
                                .createdBy(rs.getLong("comment_created_by"))
                                .reactions(new ArrayList<>())
                                .build();

                        review.getComments().add(comment);
                    }

                    // ------------------- COMMENT REACTIONS -------------------
                    Long commentReactionId = rs.getLong("comment_reaction_id");
                    if (commentReactionId != 0 && comment.getReactions().stream()
                            .noneMatch(cr -> cr.getId().equals(commentReactionId))) {

                        comment.getReactions().add(
                                PackageReviewResponse.CommentReaction.builder()
                                        .id(commentReactionId)
                                        .commentId(rs.getLong("comment_reaction_comment_id"))
                                        .userId(rs.getLong("comment_reaction_user_id"))
                                        .userName(rs.getString("comment_reaction_user_name"))
                                        .reactionType(rs.getString("comment_reaction_type"))
                                        .status(rs.getString("comment_reaction_status"))
                                        .createdBy(rs.getLong("comment_reaction_created_by"))
                                        .createdAt(rs.getTimestamp("comment_reaction_created_at") != null
                                                ? rs.getTimestamp("comment_reaction_created_at").toLocalDateTime() : null)
                                        .build()
                        );
                    }
                }
            }
            return new ArrayList<>(reviewMap.values());
        });
    }

    @Override
    public List<PackageReviewResponse> getPackageReviewDetailsById(Long packageId) {
        String SQL = PackageQueries.GET_PACKAGE_REVIEWS_DETAILS_BY_ID;

        return jdbcTemplate.query(SQL, new Object[]{packageId}, rs -> {
            Map<Long, PackageReviewResponse> reviewMap = new LinkedHashMap<>();

            while (rs.next()) {
                Long reviewId = rs.getLong("review_id");

                // ------------------- PACKAGE REVIEW -------------------
                PackageReviewResponse review = reviewMap.get(reviewId);
                if (review == null) {
                    review = PackageReviewResponse.builder()
                            .reviewId(reviewId)
                            .packageId(rs.getLong("package_id"))
                            .packageScheduleId(rs.getLong("package_schedule_id"))
                            .name(rs.getString("review_name"))
                            .review(rs.getString("review"))
                            .rating(rs.getDouble("rating"))
                            .description(rs.getString("description"))
                            .status(rs.getString("review_status"))
                            .numberOfParticipate(rs.getInt("number_of_participate"))
                            .createdBy(rs.getLong("review_created_by"))
                            .createdAt(rs.getTimestamp("review_created_at") != null
                                    ? rs.getTimestamp("review_created_at").toLocalDateTime() : null)
                            .updatedBy(rs.getLong("review_updated_by"))
                            .updatedAt(rs.getTimestamp("review_updated_at") != null
                                    ? rs.getTimestamp("review_updated_at").toLocalDateTime() : null)
                            .images(new ArrayList<>())
                            .reactions(new ArrayList<>())
                            .comments(new ArrayList<>())
                            .build();

                    reviewMap.put(reviewId, review);
                }

                // ------------------- IMAGES -------------------
                Long imageId = rs.getLong("image_id");
                if (imageId != 0 && review.getImages().stream().noneMatch(i -> i.getId().equals(imageId))) {
                    review.getImages().add(
                            PackageReviewResponse.Image.builder()
                                    .id(imageId)
                                    .name(rs.getString("image_name"))
                                    .description(rs.getString("image_description"))
                                    .imageUrl(rs.getString("image_url"))
                                    .status(rs.getString("image_status"))
                                    .createdBy(rs.getLong("image_created_by"))
                                    .createdAt(rs.getTimestamp("image_created_at") != null
                                            ? rs.getTimestamp("image_created_at").toLocalDateTime() : null)
                                    .build()
                    );
                }

                // ------------------- REVIEW REACTIONS -------------------
                Long reactionId = rs.getLong("review_reaction_id");
                if (reactionId != 0 && review.getReactions().stream().noneMatch(r -> r.getId().equals(reactionId))) {
                    review.getReactions().add(
                            PackageReviewResponse.Reaction.builder()
                                    .id(reactionId)
                                    .packageReviewId(rs.getLong("reaction_review_id"))
                                    .userId(rs.getLong("reaction_user_id"))
                                    .userName(rs.getString("reaction_user_name"))
                                    .reactionType(rs.getString("reaction_type"))
                                    .status(rs.getString("review_reaction_status"))
                                    .createdAt(rs.getTimestamp("reaction_created_at") != null
                                            ? rs.getTimestamp("reaction_created_at").toLocalDateTime() : null)
                                    .build()
                    );
                }

                // ------------------- COMMENTS -------------------
                Long commentId = rs.getLong("comment_id");
                PackageReviewResponse.Comment comment = null;
                if (commentId != 0) {
                    Optional<PackageReviewResponse.Comment> existingComment = review.getComments()
                            .stream()
                            .filter(c -> c.getId().equals(commentId))
                            .findFirst();

                    if (existingComment.isPresent()) {
                        comment = existingComment.get();
                    } else {
                        comment = PackageReviewResponse.Comment.builder()
                                .id(commentId)
                                .packageReviewId(rs.getLong("comment_review_id"))
                                .userId(rs.getLong("comment_user_id"))
                                .userName(rs.getString("comment_user_name"))
                                .parentCommentId(rs.getLong("parent_comment_id"))
                                .comment(rs.getString("comment"))
                                .status(rs.getString("comment_status"))
                                .createdAt(rs.getTimestamp("comment_created_at") != null
                                        ? rs.getTimestamp("comment_created_at").toLocalDateTime() : null)
                                .createdBy(rs.getLong("comment_created_by"))
                                .reactions(new ArrayList<>())
                                .build();

                        review.getComments().add(comment);
                    }

                    // ------------------- COMMENT REACTIONS -------------------
                    Long commentReactionId = rs.getLong("comment_reaction_id");
                    if (commentReactionId != 0 && comment.getReactions().stream()
                            .noneMatch(cr -> cr.getId().equals(commentReactionId))) {

                        comment.getReactions().add(
                                PackageReviewResponse.CommentReaction.builder()
                                        .id(commentReactionId)
                                        .commentId(rs.getLong("comment_reaction_comment_id"))
                                        .userId(rs.getLong("comment_reaction_user_id"))
                                        .userName(rs.getString("comment_reaction_user_name"))
                                        .reactionType(rs.getString("comment_reaction_type"))
                                        .status(rs.getString("comment_reaction_status"))
                                        .createdBy(rs.getLong("comment_reaction_created_by"))
                                        .createdAt(rs.getTimestamp("comment_reaction_created_at") != null
                                                ? rs.getTimestamp("comment_reaction_created_at").toLocalDateTime() : null)
                                        .build()
                        );
                    }
                }
            }

            return new ArrayList<>(reviewMap.values());
        });
    }

    @Override
    public List<PackageHistoryImageResponse> getPackageHistoryImagesById(Long packageId) {
        try {
            return jdbcTemplate.query(PackageQueries.GET_PACKAGE_HISTORY_IMAGES_BY_ID, ps -> {
                ps.setLong(1, packageId);
            }, (ResultSet rs) -> {
                List<PackageHistoryImageResponse> result = new ArrayList<>();
                while (rs.next()) {
                    PackageHistoryImageResponse.PackageInfo packageInfo = PackageHistoryImageResponse.PackageInfo.builder()
                            .packageId(rs.getInt("package_id"))
                            .packageName(rs.getString("package_name"))
                            .tourId(rs.getInt("tour_id"))
                            .build();

                    PackageHistoryImageResponse.PackageScheduleInfo scheduleInfo = PackageHistoryImageResponse.PackageScheduleInfo.builder()
                            .packageScheduleId(rs.getInt("package_schedule_id"))
                            .packageScheduleName(rs.getString("package_schedule_name"))
                            .build();

                    PackageHistoryImageResponse.UserInfo createdByUser = PackageHistoryImageResponse.UserInfo.builder()
                            .fullName(rs.getString("created_by_user"))
                            .imageUrl(rs.getString("created_by_image"))
                            .build();

                    PackageHistoryImageResponse imageResponse = PackageHistoryImageResponse.builder()
                            .imageId(rs.getInt("image_id"))
                            .imageName(rs.getString("image_name"))
                            .imageDescription(rs.getString("image_description"))
                            .imageUrl(rs.getString("image_url"))
                            .color(rs.getString("color"))
                            .imageStatusName(rs.getString("image_status_name"))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .packageInfo(packageInfo)
                            .packageSchedule(scheduleInfo)
                            .createdByUser(createdByUser)
                            .build();

                    result.add(imageResponse);
                }
                return result;
            });
        } catch (DataAccessException ex) {
            LOGGER.error(ex.toString());
            throw new DataAccessErrorExceptionHandler("Database error while fetching packages");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching packages");
        }
    }


    @Override
    public List<PackageHistoryImageResponse> getAllPackageHistoryImages() {
        try {
            return jdbcTemplate.query(PackageQueries.GET_PACKAGE_HISTORY_IMAGES, (ResultSet rs) -> {
                List<PackageHistoryImageResponse> result = new ArrayList<>();
                while (rs.next()) {
                    PackageHistoryImageResponse.PackageInfo packageInfo = PackageHistoryImageResponse.PackageInfo.builder()
                            .packageId(rs.getInt("package_id"))
                            .packageName(rs.getString("package_name"))
                            .tourId(rs.getInt("tour_id"))
                            .build();

                    PackageHistoryImageResponse.PackageScheduleInfo scheduleInfo = PackageHistoryImageResponse.PackageScheduleInfo.builder()
                            .packageScheduleId(rs.getInt("package_schedule_id"))
                            .packageScheduleName(rs.getString("package_schedule_name"))
                            .build();

                    PackageHistoryImageResponse.UserInfo createdByUser = PackageHistoryImageResponse.UserInfo.builder()
                            .fullName(rs.getString("created_by_user"))
                            .imageUrl(rs.getString("created_by_image"))
                            .build();

                    PackageHistoryImageResponse imageResponse = PackageHistoryImageResponse.builder()
                            .imageId(rs.getInt("image_id"))
                            .imageName(rs.getString("image_name"))
                            .imageDescription(rs.getString("image_description"))
                            .imageUrl(rs.getString("image_url"))
                            .color(rs.getString("color"))
                            .imageStatusName(rs.getString("image_status_name"))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .packageInfo(packageInfo)
                            .packageSchedule(scheduleInfo)
                            .createdByUser(createdByUser)
                            .build();

                    result.add(imageResponse);
                }
                return result;
            });
        } catch (DataAccessException ex) {
            LOGGER.error(ex.toString());
            throw new DataAccessErrorExceptionHandler("Database error while fetching packages");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching packages");
        }
    }


    @Override
    public List<PackageHistoryDetailsResponse> getPackageHistoryDetailsById(Long packageId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return jdbcTemplate.query(PackageQueries.GET_PACKAGE_HISTORY_DETAILS_BY_ID, ps -> {
                ps.setLong(1, packageId);
            }, (ResultSet rs) -> {
                List<PackageHistoryDetailsResponse> result = new ArrayList<>();

                while (rs.next()) {
                    // --- Package info ---
                    PackageHistoryDetailsResponse.PackageInfo packageInfo = PackageHistoryDetailsResponse.PackageInfo.builder()
                            .packageId(rs.getInt("package_id"))
                            .packageName(rs.getString("package_name"))
                            .tourId(rs.getInt("tour_id"))
                            .build();

                    // --- Users ---
                    PackageHistoryDetailsResponse.UserInfo createdByUser = PackageHistoryDetailsResponse.UserInfo.builder()
                            .fullName(rs.getString("created_by_user"))
                            .imageUrl(rs.getString("created_by_image"))
                            .build();

                    PackageHistoryDetailsResponse.UserInfo updatedByUser = PackageHistoryDetailsResponse.UserInfo.builder()
                            .fullName(rs.getString("updated_by_user"))
                            .build();

                    PackageHistoryDetailsResponse.UserInfo terminatedByUser = PackageHistoryDetailsResponse.UserInfo.builder()
                            .fullName(rs.getString("terminated_by_user"))
                            .build();

                    // --- Images ---
                    List<PackageHistoryDetailsResponse.ImageInfo> images = new ArrayList<>();
                    String imagesJson = rs.getString("images");
                    if (imagesJson != null && !imagesJson.isEmpty()) {
                        try {
                            images = objectMapper.readValue(
                                    imagesJson,
                                    new TypeReference<List<PackageHistoryDetailsResponse.ImageInfo>>() {
                                    }
                            );
                        } catch (JsonProcessingException e) {
                            LOGGER.warn("Failed to parse images JSON for packageHistoryId {}: {}",
                                    rs.getInt("package_history_id"), e.getMessage());
                        }
                    }

                    // --- Build response ---
                    PackageHistoryDetailsResponse response = PackageHistoryDetailsResponse.builder()
                            .packageHistoryId(rs.getInt("package_history_id"))
                            .packageScheduleId(rs.getInt("package_schedule_id"))
                            .packageScheduleName(rs.getString("package_schedule_name"))
                            .assumeStartDate(rs.getDate("assume_start_date") != null ? rs.getDate("assume_start_date").toLocalDate() : null)
                            .assumeEndDate(rs.getDate("assume_end_date") != null ? rs.getDate("assume_end_date").toLocalDate() : null)
                            .durationStart(rs.getInt("duration_start"))
                            .durationEnd(rs.getInt("duration_end"))
                            .packageInfo(packageInfo)
                            .numberOfParticipate(rs.getInt("number_of_participate"))
                            .rating(rs.getBigDecimal("rating"))
                            .duration(rs.getInt("duration"))
                            .historyDescription(rs.getString("history_description"))
                            .color(rs.getString("color"))
                            .hoverColor(rs.getString("hover_color"))
                            .startDate(rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null)
                            .endDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null)
                            .historyCreatedAt(rs.getTimestamp("history_created_at") != null ? rs.getTimestamp("history_created_at").toLocalDateTime() : null)
                            .createdByUser(createdByUser)
                            .historyUpdatedAt(rs.getTimestamp("history_updated_at") != null ? rs.getTimestamp("history_updated_at").toLocalDateTime() : null)
                            .updatedByUser(updatedByUser)
                            .historyTerminatedAt(rs.getTimestamp("history_terminated_at") != null ? rs.getTimestamp("history_terminated_at").toLocalDateTime() : null)
                            .terminatedByUser(terminatedByUser)
                            .images(images)
                            .build();

                    result.add(response);
                }

                return result;
            });

        } catch (DataAccessException ex) {
            LOGGER.error("DataAccessException: {}", ex.toString());
            throw new DataAccessErrorExceptionHandler("Database error while fetching packages");
        } catch (Exception ex) {
            LOGGER.error("Exception: {}", ex.toString());
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching packages");
        }
    }

    @Override
    public List<PackageHistoryDetailsResponse> getAllPackageHistoryDetails() {
        try {
            return jdbcTemplate.query(PackageQueries.GET_PACKAGE_HISTORY_DETAILS, (ResultSet rs) -> {
                List<PackageHistoryDetailsResponse> result = new ArrayList<>();
                ObjectMapper objectMapper = new ObjectMapper();

                while (rs.next()) {
                    // Package info
                    PackageHistoryDetailsResponse.PackageInfo packageInfo = PackageHistoryDetailsResponse.PackageInfo.builder()
                            .packageId(rs.getInt("package_id"))
                            .packageName(rs.getString("package_name"))
                            .tourId(rs.getInt("tour_id"))
                            .build();

                    // Users
                    PackageHistoryDetailsResponse.UserInfo createdByUser = PackageHistoryDetailsResponse.UserInfo.builder()
                            .fullName(rs.getString("created_by_user"))
                            .imageUrl(rs.getString("created_by_image"))
                            .build();

                    PackageHistoryDetailsResponse.UserInfo updatedByUser = PackageHistoryDetailsResponse.UserInfo.builder()
                            .fullName(rs.getString("updated_by_user"))
                            .build();

                    PackageHistoryDetailsResponse.UserInfo terminatedByUser = PackageHistoryDetailsResponse.UserInfo.builder()
                            .fullName(rs.getString("terminated_by_user"))
                            .build();

                    // Images JSON array
                    List<PackageHistoryDetailsResponse.ImageInfo> images = new ArrayList<>();
                    String imagesJson = rs.getString("images");
                    if (imagesJson != null && !imagesJson.isEmpty()) {
                        try {
                            images = objectMapper.readValue(
                                    imagesJson,
                                    new TypeReference<List<PackageHistoryDetailsResponse.ImageInfo>>() {
                                    }
                            );
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to parse images JSON", e);
                        }
                    }

                    // Build final response
                    PackageHistoryDetailsResponse history = PackageHistoryDetailsResponse.builder()
                            .packageHistoryId(rs.getInt("package_history_id"))
                            .packageScheduleId(rs.getInt("package_schedule_id"))
                            .packageScheduleName(rs.getString("package_schedule_name"))
                            .assumeStartDate(rs.getDate("assume_start_date") != null ? rs.getDate("assume_start_date").toLocalDate() : null)
                            .assumeEndDate(rs.getDate("assume_end_date") != null ? rs.getDate("assume_end_date").toLocalDate() : null)
                            .durationStart(rs.getInt("duration_start"))
                            .durationEnd(rs.getInt("duration_end"))
                            .packageInfo(packageInfo)
                            .numberOfParticipate(rs.getInt("number_of_participate"))
                            .rating(rs.getBigDecimal("rating"))
                            .duration(rs.getInt("duration"))
                            .historyDescription(rs.getString("history_description"))
                            .color(rs.getString("color"))
                            .hoverColor(rs.getString("hover_color"))
                            .startDate(rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null)
                            .endDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null)
                            .historyCreatedAt(rs.getTimestamp("history_created_at").toLocalDateTime())
                            .createdByUser(createdByUser)
                            .historyUpdatedAt(rs.getTimestamp("history_updated_at") != null ? rs.getTimestamp("history_updated_at").toLocalDateTime() : null)
                            .updatedByUser(updatedByUser)
                            .historyTerminatedAt(rs.getTimestamp("history_terminated_at") != null ? rs.getTimestamp("history_terminated_at").toLocalDateTime() : null)
                            .terminatedByUser(terminatedByUser)
                            .images(images)
                            .build();

                    result.add(history);
                }
                return result;
            });
        } catch (DataAccessException ex) {
            LOGGER.error(ex.toString());
            throw new DataAccessErrorExceptionHandler("Database error while fetching packages");
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching packages");
        }
    }

    @Override
    public PackageWithParamsResponse getPackagesWithParams(PackageDataRequest req) {

        try {
            int offset = (req.getPageNumber() - 1) * req.getPageSize();

            List<Long> packageIds = jdbcTemplate.queryForList(
                    PackageQueries.GET_PACKAGE_IDS_WITH_FILTERS,
                    Long.class,

                    req.getName(), req.getName(),
                    req.getMinPrice(), req.getMinPrice(),
                    req.getMaxPrice(), req.getMaxPrice(),
                    req.getDuration(), req.getDuration(),
                    req.getPackageType(), req.getPackageType(),
                    req.getLocation(), req.getLocation(), req.getLocation(),
                    req.getMinGroupSize(), req.getMinGroupSize(),
                    req.getMaxGroupSize(), req.getMaxGroupSize(),
                    req.getFromDate(), req.getFromDate(),
                    req.getToDate(), req.getToDate(),

                    req.getPageSize(),
                    offset
            );

            if (packageIds.isEmpty()) {
                return null;
            }

            Integer totalCount = jdbcTemplate.queryForObject(
                    PackageQueries.COUNT_PACKAGES_WITH_FILTERS,
                    Integer.class,

                    req.getName(), req.getName(),
                    req.getMinPrice(), req.getMinPrice(),
                    req.getMaxPrice(), req.getMaxPrice(),
                    req.getDuration(), req.getDuration(),
                    req.getPackageType(), req.getPackageType(),
                    req.getLocation(), req.getLocation(), req.getLocation(),
                    req.getMinGroupSize(), req.getMinGroupSize(),
                    req.getMaxGroupSize(), req.getMaxGroupSize(),
                    req.getFromDate(), req.getFromDate(),
                    req.getToDate(), req.getToDate()
            );

            String inSql = String.join(",", Collections.nCopies(packageIds.size(), "?"));

            Map<Long, PackageResponseDto> packageMap = new LinkedHashMap<>();

            jdbcTemplate.query(
                    PackageQueries.GET_PACKAGES_BY_IDS.replace(":packageIds", inSql),
                    packageIds.toArray(),
                    rs -> {

                        Long packageId = rs.getLong("package_id");

                        PackageResponseDto pkg = packageMap.computeIfAbsent(packageId, id -> {
                            PackageResponseDto p = new PackageResponseDto();

                            p.setPackageId(id);
                            try {
                                p.setPackageName(rs.getString("package_name"));
                                p.setPackageDescription(rs.getString("package_description"));
                                p.setTotalPrice(rs.getBigDecimal("total_price"));
                                p.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
                                p.setStartDate(rs.getObject("start_date", LocalDate.class));
                                p.setEndDate(rs.getObject("end_date", LocalDate.class));
                                p.setColor(rs.getString("color"));
                                p.setHoverColor(rs.getString("hover_color"));
                                p.setMinPersonCount(rs.getInt("min_person_count"));
                                p.setMaxPersonCount(rs.getInt("max_person_count"));
                                p.setPricePerPerson(rs.getBigDecimal("price_per_person"));
                                p.setPackageStatus(rs.getString("package_status"));

                                p.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                                p.setCreatedBy(rs.getInt("created_by"));

                                p.setPackageTypeName(rs.getString("package_type_name"));
                                p.setPackageTypeDescription(rs.getString("package_type_description"));
                                p.setPackageTypeStatus(rs.getString("package_type_status"));

                                // Tour
                                p.setTourId(rs.getLong("tour_id"));
                                p.setTourName(rs.getString("tour_name"));
                                p.setTourDescription(rs.getString("tour_description"));
                                p.setDuration(rs.getInt("duration"));
                                p.setLatitude(rs.getDouble("latitude"));
                                p.setLongitude(rs.getDouble("longitude"));
                                p.setStartLocation(rs.getString("start_location"));
                                p.setEndLocation(rs.getString("end_location"));
                                p.setTourStatus(rs.getString("tour_status"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }


                            p.setSchedules(new ArrayList<>());
                            p.setFeatures(new ArrayList<>());
                            p.setImages(new ArrayList<>());

                            return p;
                        });

                        /* ---------- Schedule ---------- */
                        int scheduleId = rs.getInt("schedule_id");
                        if (scheduleId > 0) {
                            if (pkg.getSchedules().stream().noneMatch(s -> s.getScheduleId() == scheduleId)) {
                                PackageScheduleResponseDto s = new PackageScheduleResponseDto();
                                s.setScheduleId(scheduleId);
                                s.setScheduleName(rs.getString("schedule_name"));
                                s.setAssumeStartDate(rs.getObject("assume_start_date", LocalDate.class));
                                s.setAssumeEndDate(rs.getObject("assume_end_date", LocalDate.class));
                                s.setDurationStart(rs.getInt("duration_start"));
                                s.setDurationEnd(rs.getInt("duration_end"));
                                s.setSpecialNote(rs.getString("schedule_special_note"));
                                s.setScheduleDescription(rs.getString("schedule_description"));
                                pkg.getSchedules().add(s);
                            }
                        }

                        /* ---------- Feature ---------- */
                        int featureId = rs.getInt("feature_id");
                        if (featureId > 0) {
                            if (pkg.getFeatures().stream().noneMatch(f -> f.getFeatureId() == featureId)) {
                                PackageFeatureResponseDto f = new PackageFeatureResponseDto();
                                f.setFeatureId(featureId);
                                f.setFeatureName(rs.getString("feature_name"));
                                f.setFeatureValue(rs.getString("feature_value"));
                                f.setFeatureDescription(rs.getString("feature_description"));
                                f.setColor(rs.getString("feature_color"));
                                f.setSpecialNote(rs.getString("feature_special_note"));
                                pkg.getFeatures().add(f);
                            }
                        }

                        /* ---------- Image ---------- */
                        int imageId = rs.getInt("image_id");
                        if (imageId > 0) {
                            if (pkg.getImages().stream().noneMatch(i -> i.getImageId() == imageId)) {
                                PackageImageResponseDto img = new PackageImageResponseDto();
                                img.setImageId(imageId);
                                img.setImageName(rs.getString("image_name"));
                                img.setImageDescription(rs.getString("image_description"));
                                img.setImageUrl(rs.getString("image_url"));
                                img.setColor(rs.getString("image_color"));
                                pkg.getImages().add(img);
                            }
                        }
                    }
            );

            return new PackageWithParamsResponse(totalCount, new ArrayList<>(packageMap.values()));

        } catch (DataAccessException ex) {
            LOGGER.error("DB error", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching packages");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching packages");
        }
    }

    @Override
    public List<PackageDetailsDto> getDayToPackageDetailsById(Long tourId) {
        String sql = PackageQueries.GET_DAY_TO_PACKAGE_DETAILS_BY_ID;
        return jdbcTemplate.query(
                sql,
                new Object[]{tourId}, // query parameter
                (rs, rowNum) -> {
                    PackageDetailsDto dto = new PackageDetailsDto();
                    dto.setPackageId(rs.getLong("package_id"));
                    dto.setPackageName(rs.getString("name")); // assuming pt.name is for type, adjust if needed
                    dto.setPackageDescription(rs.getString("description"));
                    dto.setTotalPrice(rs.getDouble("total_price"));
                    dto.setPricePerPerson(rs.getDouble("price_per_person"));
                    dto.setDiscount(rs.getDouble("discount_percentage"));
                    dto.setColor(rs.getString("color"));
                    dto.setHoverColor(rs.getString("hover_color"));
                    return dto;
                }
        );
    }

    @Override
    public List<PackageDayByDayDto> getPackagesAccoamdationsByIds(List<Long> packageIds) {
        if (packageIds == null || packageIds.isEmpty()) {
            return List.of();
        }

        String placeholders = packageIds.stream().map(id -> "?").collect(Collectors.joining(", "));
        String sql = String.format(PackageQueries.GET_PACKAGES_ACCOMMODATIONS_BY_IDS, placeholders);

        return jdbcTemplate.query(sql, packageIds.toArray(), (rs, rowNum) -> PackageDayByDayDto.builder()
                .packageId(rs.getLong("package_id"))
                .packageDayAccommodationId(rs.getLong("package_day_accommodation_id"))
                .dayNumber(rs.getInt("day_number"))
                .breakfast(rs.getBoolean("breakfast"))
                .breakfastDescription(rs.getString("breakfast_description"))
                .lunch(rs.getBoolean("lunch"))
                .lunchDescription(rs.getString("lunch_description"))
                .dinner(rs.getBoolean("dinner"))
                .dinnerDescription(rs.getString("dinner_description"))
                .morningTea(rs.getBoolean("morning_tea"))
                .morningTeaDescription(rs.getString("morning_tea_description"))
                .eveningTea(rs.getBoolean("evening_tea"))
                .eveningTeaDescription(rs.getString("evening_tea_description"))
                .snacks(rs.getBoolean("snacks"))
                .snackNote(rs.getString("snack_note"))
                .otherNotes(rs.getString("other_notes"))
                .hotelId(rs.getObject("hotel_id") != null ? rs.getLong("hotel_id") : null)
                .hotelName(rs.getString("hotel_name"))
                .hotelDescription(rs.getString("hotel_description"))
                .hotelWebsite(rs.getString("hotel_website"))
                .hotelCategory(rs.getObject("hotel_category") != null ? rs.getInt("hotel_category") : null)
                .hotelType(rs.getString("hotel_type"))
                .hotelLocation(rs.getString("hotel_location"))
                .hotelLatitude(rs.getObject("hotel_latitude") != null ? rs.getDouble("hotel_latitude") : null)
                .hotelLongitude(rs.getObject("hotel_longitude") != null ? rs.getDouble("hotel_longitude") : null)
                .transportId(rs.getObject("transport_id") != null ? rs.getLong("transport_id") : null)
                .vehicleRegistrationNumber(rs.getString("vehicle_registration_number"))
                .vehicleTypeId(rs.getLong("vehicle_type_id"))
                .vehicleTypeName(rs.getString("vehicle_type_name"))
                .vehicleModel(rs.getString("vehicle_model"))
                .vehicleSpecificationId(rs.getLong("specification_id"))
                .seatCapacity(rs.getObject("seat_capacity") != null ? rs.getInt("seat_capacity") : null)
                .airCondition(rs.getObject("air_condition") != null ? rs.getBoolean("air_condition") : null)
                .build()
        );
    }

    @Override
    public List<Long> getPackageIdsByTourId(Long tourId) {
        String GET_PACKAGE_IDS_BY_TOUR_ID = PackageQueries.GET_PACKAGE_IDS_BY_TOUR_ID;
        try {
            return jdbcTemplate.query(GET_PACKAGE_IDS_BY_TOUR_ID, new Object[]{tourId}, (ResultSet rs) -> {
                List<Long> packageIds = new ArrayList<>();
                while (rs.next()) {
                    packageIds.add(rs.getLong("package_id"));
                }
                return packageIds;
            });
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package details");
        }
    }

    @Override
    public List<PackageExtrasResponse.PackageExclusion> getPackageExclusions(Long packageId) {
        String sql = PackageQueries.GET_PACKAGE_EXCLUSIONS_BY_PACKAGE_ID;
        try {
            return jdbcTemplate.query(sql, new Object[]{packageId}, (rs, rowNum) ->
                    PackageExtrasResponse.PackageExclusion.builder()
                            .id(rs.getLong("package_exclusion_id"))
                            .description(rs.getString("exclusion_text"))
                            .displayOrder(rs.getInt("display_order"))
                            .status(rs.getString("status_name"))
                            .build()
            );
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package exclusions", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package exclusions");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package exclusions", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package exclusions");
        }
    }

    @Override
    public List<PackageExtrasResponse.PackageInclusion> getPackageInclusions(Long packageId) {
        String sql = PackageQueries.GET_PACKAGE_INCLUSIONS_BY_PACKAGE_ID;
        try {
            return jdbcTemplate.query(sql, new Object[]{packageId}, (rs, rowNum) ->
                    PackageExtrasResponse.PackageInclusion.builder()
                            .id(rs.getLong("package_inclusion_id"))
                            .description(rs.getString("inclusion_text"))
                            .displayOrder(rs.getInt("display_order"))
                            .status(rs.getString("status_name"))
                            .build()
            );
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package inclusions", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package inclusions");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package inclusions", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package inclusions");
        }
    }

    @Override
    public List<PackageExtrasResponse.PackageCondition> getPackageConditions(Long packageId) {
        String sql = PackageQueries.GET_PACKAGE_CONDITIONS_BY_PACKAGE_ID;
        try {
            return jdbcTemplate.query(sql, new Object[]{packageId}, (rs, rowNum) ->
                    PackageExtrasResponse.PackageCondition.builder()
                            .id(rs.getLong("package_condition_id"))
                            .description(rs.getString("condition_text"))
                            .displayOrder(rs.getInt("display_order"))
                            .status(rs.getString("status_name"))
                            .build()
            );
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package conditions", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package conditions");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package conditions", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package conditions");
        }
    }

    @Override
    public List<PackageExtrasResponse.PackageTravelTip> getPackageTravelTips(Long packageId) {
        String sql = PackageQueries.GET_PACKAGE_TRAVEL_TIPS_BY_PACKAGE_ID;
        try {
            return jdbcTemplate.query(sql, new Object[]{packageId}, (rs, rowNum) ->
                    PackageExtrasResponse.PackageTravelTip.builder()
                            .id(rs.getLong("package_travel_tip_id"))
                            .title(rs.getString("tip_title"))
                            .description(rs.getString("tip_description"))
                            .displayOrder(rs.getInt("display_order"))
                            .status(rs.getString("status_name"))
                            .build()
            );
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package travel tips", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package travel tips");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package travel tips", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package travel tips");
        }
    }

    @Override
    public List<Long> getPackageSchedulesIdsByTourId(Long tourId) {
        String GET_PACKAGE_SCHEDULE_IDS_BY_TOUR_ID = PackageQueries.GET_PACKAGE_SCHEDULE_IDS_BY_TOUR_ID;
        try {
            return jdbcTemplate.query(GET_PACKAGE_SCHEDULE_IDS_BY_TOUR_ID, new Object[]{tourId}, (ResultSet rs) -> {
                List<Long> packageScheduleIds = new ArrayList<>();
                while (rs.next()) {
                    packageScheduleIds.add(rs.getLong("id"));
                }
                return packageScheduleIds;
            });
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package schedule details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package schedule details");
        }
    }

    @Override
    public List<PackageScheduleResponse.PackageScheduleDetails> getPackageSchedulesById(Long packageId) {
        String GET_PACKAGE_SCHEDULE_DETAILS_BY_ID = PackageQueries.GET_PACKAGE_SCHEDULE_DETAILS_BY_ID;
        try {
            return jdbcTemplate.query(GET_PACKAGE_SCHEDULE_DETAILS_BY_ID, new Object[]{packageId}, (ResultSet rs) -> {
                List<PackageScheduleResponse.PackageScheduleDetails> packageScheduleDetailsList = new ArrayList<>();
                while (rs.next()) {
                    PackageScheduleResponse.PackageScheduleDetails packageScheduleDetails =
                            PackageScheduleResponse.PackageScheduleDetails.builder()
                                    .packageScheduleId(rs.getLong("id"))
                                    .packageId(rs.getLong("package_id"))
                                    .name(rs.getString("name"))
                                    .assumeStartDate(rs.getDate("assume_start_date") != null ? rs.getDate("assume_start_date").toLocalDate() : null)
                                    .assumeEndDate(rs.getDate("assume_end_date") != null ? rs.getDate("assume_end_date").toLocalDate() : null)
                                    .description(rs.getString("description"))
                                    .specialNote(rs.getString("special_note"))
                                    .status(rs.getString("status"))
                                    .durationStart(rs.getObject("duration_start", Integer.class))
                                    .durationEnd(rs.getObject("duration_end", Integer.class))
                                    .build();

                    packageScheduleDetailsList.add(packageScheduleDetails);
                }
                return packageScheduleDetailsList;
            });
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package schedule details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package schedule details");
        }
    }

    @Override
    public List<PackageScheduleDetailsResponse.PackageScheduleDetails> getPackageSchedulesForId(Long packageId) {
        String sql = PackageQueries.GET_PACKAGE_SCHEDULE_DETAILS_BY_PACKAGE_ID;

        try {
            return jdbcTemplate.query(sql, new Object[]{packageId}, (ResultSet rs) -> {
                List<PackageScheduleDetailsResponse.PackageScheduleDetails> schedules = new ArrayList<>();

                while (rs.next()) {
                    PackageScheduleDetailsResponse.PackageScheduleDetails schedule =
                            PackageScheduleDetailsResponse.PackageScheduleDetails.builder()
                                    .scheduleId(rs.getLong("schedule_id"))
                                    .scheduleName(rs.getString("schedule_name"))
                                    .assumeStartDate(
                                            rs.getDate("assume_start_date") != null
                                                    ? rs.getDate("assume_start_date").toLocalDate()
                                                    : null
                                    )
                                    .assumeEndDate(
                                            rs.getDate("assume_end_date") != null
                                                    ? rs.getDate("assume_end_date").toLocalDate()
                                                    : null
                                    )
                                    .durationStart(rs.getObject("duration_start", Integer.class))
                                    .durationEnd(rs.getObject("duration_end", Integer.class))
                                    .specialNote(rs.getString("special_note"))
                                    .description(rs.getString("description"))
                                    .status(rs.getString("status_name"))
                                    .createdAt(
                                            rs.getTimestamp("created_at") != null
                                                    ? rs.getTimestamp("created_at").toLocalDateTime()
                                                    : null
                                    )
                                    .updatedAt(
                                            rs.getTimestamp("updated_at") != null
                                                    ? rs.getTimestamp("updated_at").toLocalDateTime()
                                                    : null
                                    )
                                    .build();

                    schedules.add(schedule);
                }
                return schedules;
            });
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package schedule details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package schedule details");
        }
    }


    @Override
    public PackageScheduleDetailsResponse.PackageBasicDetails getPackageBasicDetails(Long packageId) {
        String sql = PackageQueries.GET_PACKAGE_BASIC_DETAILS_BY_PACKAGE_ID;

        try {
            return jdbcTemplate.query(sql, new Object[]{packageId}, (ResultSet rs) -> {

                PackageScheduleDetailsResponse.PackageBasicDetails.PackageBasicDetailsBuilder packageBuilder = null;
                List<PackageScheduleDetailsResponse.PackageImageDetails> images = new ArrayList<>();

                while (rs.next()) {
                    if (packageBuilder == null) {
                        packageBuilder = PackageScheduleDetailsResponse.PackageBasicDetails.builder()
                                .packageId(rs.getLong("package_id"))
                                .packageName(rs.getString("name"))
                                .packageDescription(rs.getString("description"))
                                .totalPrice(rs.getObject("total_price", Double.class))
                                .pricePerPerson(rs.getObject("price_per_person", Double.class))
                                .discount(rs.getObject("discount_percentage", Double.class))
                                .color(rs.getString("color"))
                                .hoverColor(rs.getString("hover_color"))
                                .minPersonCount(rs.getObject("min_person_count", Integer.class))
                                .maxPersonCount(rs.getObject("max_person_count", Integer.class))
                                .status(rs.getString("status"));
                    }

                    // Add image if exists
                    Long imageId = rs.getObject("image_id", Long.class);
                    if (imageId != null) {
                        images.add(
                                PackageScheduleDetailsResponse.PackageImageDetails.builder()
                                        .imageId(imageId)
                                        .imageName(rs.getString("image_name"))
                                        .imageDescription(rs.getString("image_description"))
                                        .imageUrl(rs.getString("image_url"))
                                        .build()
                        );
                    }
                }

                if (packageBuilder == null) {
                    return null; // no package found
                }

                return packageBuilder.images(images).build();
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package basic details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package basic details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package basic details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package basic details");
        }
    }

    @Override
    public List<PackageComapreResponse.PackageImages> getAllPackagesImages(Long tourId) {

        String sql = PackageQueries.GET_ALL_PACKAGES_IMAGES;
        try {
            return jdbcTemplate.query(
                    sql,
                    new Object[]{tourId},
                    (rs, rowNum) -> PackageComapreResponse.PackageImages.builder()
                            .packageId(rs.getLong("package_id"))
                            .imageId(rs.getLong("image_id"))
                            .name(rs.getString("image_name"))
                            .description(rs.getString("image_description"))
                            .url(rs.getString("image_url"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package images for tourId {}", tourId, ex);
            throw new DataAccessErrorExceptionHandler(
                    "Database error while fetching package images"
            );
        }catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package images details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package images details");
        }
    }

    @Override
    public PackageBasicDetailsDto getPackageBasicDetailsByScheduleId(Long packageScheduleId) {
        String QUERY = PackageQueries.GET_PACKAGE_BASIC_DETAILS_BY_PACKAGE_SCHEDULE_ID;

        try {
            return jdbcTemplate.queryForObject(
                    QUERY,
                    new Object[]{packageScheduleId},
                    (rs, rowNum) -> PackageBasicDetailsDto.builder()
                            .packageId(rs.getLong("package_id"))
                            .assumeStartDate(rs.getDate("assume_start_date"))
                            .assumeEndDate(rs.getDate("assume_end_date"))
                            .packageName(rs.getString("name"))
                            .description(rs.getString("description"))
                            .totalPrice(rs.getDouble("total_price"))
                            .pricePerPerson(rs.getDouble("price_per_person"))
                            .discountPercentage(rs.getDouble("discount_percentage"))
                            .color(rs.getString("color"))
                            .hoverColor(rs.getString("hover_color"))
                            .minPersonCount(rs.getInt("min_person_count"))
                            .maxPersonCount(rs.getInt("max_person_count"))
                            .tourId(rs.getLong("tour_id"))
                            .startLocation(rs.getString("start_location"))
                            .endLocation(rs.getString("end_location"))
                            .status(rs.getString("status"))
                            .build()
            );

        } catch (EmptyResultDataAccessException ex) {
            LOGGER.warn("No package found for schedule id {}", packageScheduleId);
            throw new DataNotFoundErrorExceptionHandler("No package found for given schedule id");
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package basic details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package basic details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package basic details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package basic details");
        }
    }

    @Override
    public List<PackageDayAccommodationPriceDto> getPackageDayAccommodationPriceByScheduleId(Long packageScheduleId) {
        String QUERY = PackageQueries.GET_PACKAGE_DAY_ACCOMMODATION_PRICE_BY_PACKAGE_SHECULE_ID;

        try {
            return jdbcTemplate.query(QUERY, new Object[]{packageScheduleId}, rs -> {
                List<PackageDayAccommodationPriceDto> list = new ArrayList<>();

                while (rs.next()) {
                    list.add(PackageDayAccommodationPriceDto.builder()
                            .packageId(rs.getLong("package_id"))
                            .packageDayAccommodationId(rs.getLong("package_day_accommodation_id"))
                            .dayNumber(rs.getInt("day_number"))
                            .hotelId(rs.getLong("hotel_id"))
                            .hotelName(rs.getString("hotel_name"))
                            .vehicleId(rs.getLong("transport_id"))
                            .transportPrice(rs.getDouble("transport_cost"))
                            .localPrice(rs.getObject("local_price", Double.class))
                            .price(rs.getObject("price", Double.class))
                            .discount(rs.getObject("discount", Double.class))
                            .serviceCharge(rs.getObject("service_charge", Double.class))
                            .tax(rs.getObject("tax", Double.class))
                            .extraCharge(rs.getObject("extra_charge", Double.class))
                            .extraChargeNote(rs.getString("extra_charge_note"))
                            .tourName(rs.getString("tour_name"))
                            .tourDescription(rs.getString("tour_description"))
                            .build());
                }
                return list;
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package day accommodation prices", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching package day accommodation prices");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package day accommodation prices", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching package day accommodation prices");
        }
    }

    @Override
    public List<PackageDestinationExtraPriceDto> getPackageDestinationExtraPriceByScheduleId(Long packageScheduleId) {
        String QUERY = PackageQueries.GET_PACKAGE_DESTINATION_EXTRA_PRICE_BY_PACKAGE_SCHEDULE_ID;

        try {
            return jdbcTemplate.query(QUERY, new Object[]{packageScheduleId}, rs -> {
                List<PackageDestinationExtraPriceDto> list = new ArrayList<>();

                while (rs.next()) {
                    list.add(PackageDestinationExtraPriceDto.builder()
                            .packageId(rs.getLong("package_id"))
                            .destinationId(rs.getLong("destination_id"))
                            .extraPrice(rs.getObject("extra_price", Double.class))
                            .extraPriceNote(rs.getString("extra_price_note"))
                            .destinationName(rs.getString("name"))
                            .destinationDescription(rs.getString("description"))
                            .build());
                }
                return list;
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination extra prices", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching destination extra prices");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination extra prices", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching destination extra prices");
        }
    }

    @Override
    public List<PackageActivityPriceDto> getPackageActivityPriceByScheduleId(Long packageScheduleId) {
        String QUERY = PackageQueries.GET_PACKAGE_ACTIVITY_PRICE_BY_PACKAGE_SCHEDULE_ID;

        try {
            return jdbcTemplate.query(QUERY, new Object[]{packageScheduleId}, rs -> {
                List<PackageActivityPriceDto> list = new ArrayList<>();

                while (rs.next()) {
                    // activities_id can be NULL
                    if (rs.getObject("activities_id") != null) {
                        list.add(PackageActivityPriceDto.builder()
                                .packageId(rs.getLong("package_id"))
                                .activityId(rs.getLong("activities_id"))
                                .priceForeigners(rs.getObject("price_foreigners", Double.class))
                                .name(rs.getString("name"))
                                .description(rs.getString("description"))
                                .build());
                    }
                }
                return list;
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity prices", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching activity prices");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity prices", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching activity prices");
        }
    }

    @Override
    public List<PackageForTerminateResponse> getPackagesForTerminate() {
        String GET_ACTIVE_PACKAGES_FOR_TERMINATE = PackageQueries.GET_ACTIVE_PACKAGES_FOR_TERMINATE;

        try {
            return jdbcTemplate.query(
                    GET_ACTIVE_PACKAGES_FOR_TERMINATE,
                    new Object[]{CommonStatus.ACTIVE.toString()}, // parameter for cs.name = ?
                    (rs, rowNum) -> PackageForTerminateResponse.builder()
                            .packageId(rs.getLong("package_id"))
                            .packageName(rs.getString("name"))
                            .build()
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch tours for terminate: ", e);
            throw new DataAccessErrorExceptionHandler("Failed to fetch tours");
        }catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching tours for terminate", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching tours for terminate prices");
        }
    }

    @Override
    public void terminatePackage(PackageTerminateRequest packageTerminateRequest, Long userId) {
        String PACKAGE_TERMINATE = PackageQueries.PACKAGE_TERMINATE;
        try {
            jdbcTemplate.update(PACKAGE_TERMINATE, new Object[]{CommonStatus.TERMINATED.toString(), userId, packageTerminateRequest.getPackageId()});
        } catch (DataAccessException tfe) {
            LOGGER.error(tfe.toString());
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to terminate package : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate package");
        }
    }

    @Override
    public Long insertPackageDeails(PackageInsertRequest request, Long userId) {

        try {
            Long statusId = statusRepository.getStatusIdByName(request.getStatus());

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        PackageQueries.INSERT_PACKAGE_BASIC_DETAILS,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setLong(1, request.getPackageType());
                ps.setLong(2, request.getTourId());
                ps.setString(3, request.getName());
                ps.setString(4, request.getDescription());
                ps.setBigDecimal(5, request.getTotalPrice());
                ps.setBigDecimal(6, request.getDiscountPercentage());
                ps.setObject(7, request.getStartDate());   // LocalDate supported
                ps.setObject(8, request.getEndDate());
                ps.setString(9, request.getColor());
                ps.setLong(10, statusId);
                ps.setString(11, request.getHoverColor());
                ps.setInt(12, request.getMinPersonCount());
                ps.setInt(13, request.getMaxPersonCount());
                ps.setBigDecimal(14, request.getPricePerPerson());
                ps.setLong(15, userId);

                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                throw new InsertFailedErrorExceptionHandler("Failed to generate package ID");
            }

            Long packageId = keyHolder.getKey().longValue();

            return packageId;

        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting package", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to insert package", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert package");
        }
    }

    @Override
    public void insertPackageImages(Long packageId, List<PackageImageInsertRequest> images, Long userId) {

        if (images == null || images.isEmpty()) return;
        try {
            jdbcTemplate.batchUpdate(
                    PackageQueries.INSERT_PACKAGE_IMAGE,
                    images,
                    images.size(),
                    (ps, image) -> {
                        ps.setLong(1, packageId);
                        ps.setString(2, image.getName());
                        ps.setString(3, image.getDescription());
                        ps.setString(4, image.getStatus());   // common_status.name
                        ps.setString(5, image.getImageUrl());
                        ps.setString(6, image.getColor());
                        ps.setLong(7, userId);
                    }
            );
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting package images", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        }
    }

    @Override
    public void insertPackageInclusions(Long packageId,
                                        List<PackageInclusionInsertRequest> inclusions,
                                        Long userId) {

        if (inclusions == null || inclusions.isEmpty()) return;

        jdbcTemplate.batchUpdate(
                PackageQueries.INSERT_PACKAGE_INCLUSION,
                inclusions,
                inclusions.size(),
                (ps, inc) -> {
                    ps.setLong(1, packageId);
                    ps.setString(2, inc.getInclusionText());
                    ps.setInt(3, inc.getDisplayOrder());
                    ps.setString(4, inc.getStatus());
                    ps.setLong(5, userId);
                    ps.setLong(6, userId);
                }
        );
    }

    @Override
    public void insertPackageExclusions(Long packageId,
                                        List<PackageExclusionInsertRequest> exclusions,
                                        Long userId) {

        if (exclusions == null || exclusions.isEmpty()) return;

        jdbcTemplate.batchUpdate(
                PackageQueries.INSERT_PACKAGE_EXCLUSION,
                exclusions,
                exclusions.size(),
                (ps, exc) -> {
                    ps.setLong(1, packageId);
                    ps.setString(2, exc.getExclusionText());
                    ps.setInt(3, exc.getDisplayOrder());
                    ps.setString(4, exc.getStatus());
                    ps.setLong(5, userId);
                    ps.setLong(6, userId);
                }
        );
    }

    @Override
    public void insertPackageConditions(Long packageId,
                                        List<PackageConditionInsertRequest> conditions,
                                        Long userId) {

        if (conditions == null || conditions.isEmpty()) return;

        jdbcTemplate.batchUpdate(
                PackageQueries.INSERT_PACKAGE_CONDITION,
                conditions,
                conditions.size(),
                (ps, con) -> {
                    ps.setLong(1, packageId);
                    ps.setString(2, con.getConditionText());
                    ps.setInt(3, con.getDisplayOrder());
                    ps.setString(4, con.getStatus());
                    ps.setLong(5, userId);
                    ps.setLong(6, userId);
                }
        );
    }

    @Override
    public void insertPackageTravelTips(Long packageId,
                                        List<PackageTravelTipInsertRequest> travelTips,
                                        Long userId) {

        if (travelTips == null || travelTips.isEmpty()) return;

        jdbcTemplate.batchUpdate(
                PackageQueries.INSERT_PACKAGE_TRAVEL_TIP,
                travelTips,
                travelTips.size(),
                (ps, tip) -> {
                    ps.setLong(1, packageId);
                    ps.setString(2, tip.getTipTitle());
                    ps.setString(3, tip.getTipDescription());
                    ps.setInt(4, tip.getDisplayOrder());
                    ps.setString(5, tip.getStatus());
                    ps.setLong(6, userId);
                    ps.setLong(7, userId);
                }
        );
    }

    @Override
    public void insertDayByDayAccommodations(Long packageId,
                                             List<PackageDayAccommodationInsertRequest> dayAccommodations,
                                             Long userId) {

        if (dayAccommodations == null || dayAccommodations.isEmpty()) return;

        try {
            jdbcTemplate.batchUpdate(
                    PackageQueries.INSERT_PACKAGE_DAY_ACCOMMODATION,
                    dayAccommodations,
                    dayAccommodations.size(),
                    (ps, day) -> {
                        ps.setLong(1, packageId);
                        ps.setInt(2, day.getDayNumber());

                        ps.setBoolean(3, Boolean.TRUE.equals(day.getBreakfast()));
                        ps.setString(4, day.getBreakfastDescription());

                        ps.setBoolean(5, Boolean.TRUE.equals(day.getLunch()));
                        ps.setString(6, day.getLunchDescription());

                        ps.setBoolean(7, Boolean.TRUE.equals(day.getDinner()));
                        ps.setString(8, day.getDinnerDescription());

                        ps.setBoolean(9, Boolean.TRUE.equals(day.getMorningTea()));
                        ps.setString(10, day.getMorningTeaDescription());

                        ps.setBoolean(11, Boolean.TRUE.equals(day.getEveningTea()));
                        ps.setString(12, day.getEveningTeaDescription());

                        ps.setBoolean(13, Boolean.TRUE.equals(day.getSnacks()));
                        ps.setString(14, day.getSnackNote());

                        if (day.getHotelId() != null) {
                            ps.setLong(15, day.getHotelId());
                        } else {
                            ps.setNull(15, Types.BIGINT);
                        }

                        if (day.getTransportId() != null) {
                            ps.setLong(16, day.getTransportId());
                        } else {
                            ps.setNull(16, Types.BIGINT);
                        }

                        ps.setString(17, day.getOtherNotes());
                    }
            );
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting day accommodations", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        }
    }

    @Override
    public void updatePackageBasicDetails(
            Long packageId,
            PackageUpdateRequest.PackageBasicDetails packageBasicDetails,
            Long userId
    ) {
        if (packageBasicDetails == null) {
            return;
        }

        try {
            jdbcTemplate.update(
                    PackageQueries.UPDATE_PACKAGE_BASIC_DETAILS,
                    packageBasicDetails.getPackageType(),
                    packageBasicDetails.getTourId(),
                    packageBasicDetails.getName(),
                    packageBasicDetails.getDescription(),
                    packageBasicDetails.getTotalPrice(),
                    packageBasicDetails.getDiscountPercentage(),
                    packageBasicDetails.getStartDate(),
                    packageBasicDetails.getEndDate(),
                    packageBasicDetails.getColor(),
                    packageBasicDetails.getStatus(),
                    packageBasicDetails.getHoverColor(),
                    packageBasicDetails.getMinPersonCount(),
                    packageBasicDetails.getMaxPersonCount(),
                    packageBasicDetails.getPricePerPerson(),
                    userId,
                    packageId
            );

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating package basic details", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to update package basic details", e);
            throw new InternalServerErrorExceptionHandler("Failed to update package");
        }
    }

    @Override
    public void removePackageImages(Long packageId, List<Long> removedImageIds, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());
            jdbcTemplate.batchUpdate(
                    PackageQueries.PACKAGE_IMAGES_REMOVE,
                    removedImageIds,
                    removedImageIds.size(),
                    (ps, imageId) -> {
                        ps.setLong(1, statusId);
                        ps.setLong(2, userId);
                        ps.setLong(3, imageId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove package images", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove package images : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove package images");
        }
    }

    @Override
    public void updatePackageImages(Long packageId, List<PackageImageUpdateRequest> updatedImages, Long userId) {
        if (updatedImages == null || updatedImages.isEmpty()) {
            return;
        }

        try {
            for (PackageImageUpdateRequest image : updatedImages) {

                jdbcTemplate.update(
                        PackageQueries.UPDATE_PACKAGE_IMAGE,
                        image.getImageName(),
                        image.getImageDescription(),
                        image.getImageUrl(),
                        image.getStatus(),
                        userId,
                        image.getImageId(),
                        packageId
                );
            }

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating package images", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to update package images", e);
            throw new InternalServerErrorExceptionHandler("Failed to update package images");
        }
    }

    @Override
    public void removeDayByDayAccommodations(Long packageId, List<Long> removeDayAccommodationIds, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.batchUpdate(
                    PackageQueries.PACKAGE_DAY_ACCOMMODATION_REMOVE,
                    removeDayAccommodationIds,
                    removeDayAccommodationIds.size(),
                    (ps, dayAccommodationId) -> {
                        ps.setLong(1, statusId);
                        ps.setLong(2, userId);
                        ps.setLong(3, dayAccommodationId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove day accommodation", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove day accommodation : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove day accommodation");
        }
    }

    @Override
    public void updateDayByDayAccommodations(Long packageId, List<PackageDayAccommodationUpdateRequest> updatedDayAccommodations, Long userId) {
        if (updatedDayAccommodations == null || updatedDayAccommodations.isEmpty()) {
            return;
        }

        try {
            for (PackageDayAccommodationUpdateRequest req : updatedDayAccommodations) {
                jdbcTemplate.update(
                        PackageQueries.UPDATE_PACKAGE_DAY_ACCOMMODATION,

                        req.getDayNumber(),
                        req.getBreakfast(),
                        req.getBreakfastDescription(),
                        req.getLunch(),
                        req.getLunchDescription(),
                        req.getDinner(),
                        req.getDinnerDescription(),
                        req.getMorningTea(),
                        req.getMorningTeaDescription(),
                        req.getEveningTea(),
                        req.getEveningTeaDescription(),
                        req.getSnacks(),
                        req.getSnackNote(),
                        req.getHotelId(),
                        req.getTransportId(),
                        req.getOtherNotes(),
                        req.getStatus(),
                        userId,
                        req.getPackageDayAccommodationId(),
                        packageId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating package day accommodation", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update package day accommodation", e);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to update package day accommodation"
            );
        }
    }

    @Override
    public void removePcakageInclusions(Long packageId, List<Long> removeInclusionIds, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.batchUpdate(
                    PackageQueries.PACKAGE_INCLUSION_REMOVE,
                    removeInclusionIds,
                    removeInclusionIds.size(),
                    (ps, inclusionId) -> {
                        ps.setLong(1, statusId);
                        ps.setLong(2, userId);
                        ps.setLong(3, inclusionId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove inclusion", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove inclusion : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove inclusion");
        }
    }

    @Override
    public void updatePackageInclusions(Long packageId, List<PackageInclusionUpdateRequest> updatedInclusions, Long userId) {
        if (updatedInclusions == null || updatedInclusions.isEmpty()) return;

        try {
            for (PackageInclusionUpdateRequest inclusion : updatedInclusions) {
                jdbcTemplate.update(
                        PackageQueries.UPDATE_PACKAGE_INCLUSION,
                        inclusion.getInclusionText(),
                        inclusion.getDisplayOrder(),
                        inclusion.getStatus(),
                        userId,
                        inclusion.getPackageInclusionId(),
                        packageId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating package inclusions", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update package inclusions", e);
            throw new InternalServerErrorExceptionHandler("Failed to update package inclusions");
        }
    }

    @Override
    public void removePackageExclusions(Long packageId, List<Long> removeExclusionIds, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.batchUpdate(
                    PackageQueries.PACKAGE_EXCLUSION_REMOVE,
                    removeExclusionIds,
                    removeExclusionIds.size(),
                    (ps, exclusionId) -> {
                        ps.setLong(1,statusId);
                        ps.setLong(2, userId);
                        ps.setLong(3, exclusionId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove exclusion", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove exclusion : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove exclusion");
        }
    }

    @Override
    public void updatePackageExclusions(Long packageId, List<PackageExclusionUpdateRequest> updatedExclusions, Long userId) {
        if (updatedExclusions == null || updatedExclusions.isEmpty()) return;

        try {
            for (PackageExclusionUpdateRequest exclusion : updatedExclusions) {
                jdbcTemplate.update(
                        PackageQueries.UPDATE_PACKAGE_EXCLUSION,
                        exclusion.getExclusionText(),
                        exclusion.getDisplayOrder(),
                        exclusion.getStatus(),
                        userId,
                        exclusion.getPackageExclusionId(),
                        packageId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating package exclusions", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update package exclusions", e);
            throw new InternalServerErrorExceptionHandler("Failed to update package exclusions");
        }
    }

    @Override
    public void removePcakageConditions(Long packageId, List<Long> removeConditionIds, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.batchUpdate(
                    PackageQueries.PACKAGE_CONDITION_REMOVE,
                    removeConditionIds,
                    removeConditionIds.size(),
                    (ps, conditionId) -> {
                        ps.setLong(1, statusId);
                        ps.setLong(2, userId);
                        ps.setLong(3, conditionId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove condition", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove condition : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove condition");
        }
    }

    @Override
    public void updatePackageConditions(Long packageId, List<PackageConditionUpdateRequest> updatedConditions, Long userId) {
        if (updatedConditions == null || updatedConditions.isEmpty()) return;

        try {
            for (PackageConditionUpdateRequest condition : updatedConditions) {
                jdbcTemplate.update(
                        PackageQueries.UPDATE_PACKAGE_CONDITION,
                        condition.getConditionText(),
                        condition.getDisplayOrder(),
                        condition.getStatus(),
                        userId,
                        condition.getPackageConditionId(),
                        packageId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating package conditions", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update package conditions", e);
            throw new InternalServerErrorExceptionHandler("Failed to update package conditions");
        }
    }

    @Override
    public void removePcakageTravelTips(Long packageId, List<Long> removeTravelTipIds, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.batchUpdate(
                    PackageQueries.PACKAGE_TRAVEL_TIPS_REMOVE,
                    removeTravelTipIds,
                    removeTravelTipIds.size(),
                    (ps, travelTipId) -> {
                        ps.setLong(1, statusId);
                        ps.setLong(2, userId);
                        ps.setLong(3, travelTipId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove travel tip ", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove travel tip : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove travel tip");
        }
    }

    @Override
    public void updatePackageTravelTips(Long packageId, List<PackageTravelTipUpdateRequest> updatedTravelTips, Long userId) {
        if (updatedTravelTips == null || updatedTravelTips.isEmpty()) return;

        try {
            for (PackageTravelTipUpdateRequest tip : updatedTravelTips) {
                jdbcTemplate.update(
                        PackageQueries.PACKAGE_TRAVEL_TIP_UPDATE,

                        tip.getTipTitle(),
                        tip.getTipDescription(),
                        tip.getDisplayOrder(),
                        tip.getStatus(),
                        userId,
                        tip.getPackageTipId(),
                        packageId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating package travel tips", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update package travel tips", e);
            throw new InternalServerErrorExceptionHandler("Failed to update travel tips");
        }
    }

    @Override
    public void insertPackageFeatures(Long packageId, List<PackageFeaturesInsertRequest> addFeatures, Long userId) {
        if (addFeatures == null || addFeatures.isEmpty()) return;

        try {
            jdbcTemplate.batchUpdate(
                    PackageQueries.INSERT_PACKAGE_FEATURE,
                    addFeatures,
                    addFeatures.size(),
                    (ps, feature) -> {
                        ps.setLong(1, packageId);
                        ps.setString(2, feature.getFeatureName());
                        ps.setString(3, feature.getFeatureValue());
                        ps.setString(4, feature.getFeatureDescription());

                        ps.setString(5, feature.getStatus());

                        ps.setString(6, feature.getColor());
                        ps.setString(7, feature.getHoverColor());
                        ps.setString(8, feature.getSpecialNote());
                        ps.setLong(9, userId);
                    }
            );
        } catch (DataAccessException dae) {
            throw new RuntimeException("Failed to insert package features", dae);
        }
    }

    @Override
    public void removePackageFeatures(Long packageId, List<Long> removeFeatureIds, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.batchUpdate(
                    PackageQueries.PACKAGE_FEATURE_REMOVE,
                    removeFeatureIds,
                    removeFeatureIds.size(),
                    (ps, featureId) -> {
                        ps.setLong(1, statusId);
                        ps.setLong(2, userId);
                        ps.setLong(3, featureId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove package feature ", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove package feature : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove package feature");
        }
    }

    @Override
    public void updatePackageFeatures(Long packageId, List<PackageFeaturesUpdateRequest> updatedFeatures, Long userId) {
        if (updatedFeatures == null || updatedFeatures.isEmpty()) return;

        try {
            for (PackageFeaturesUpdateRequest feature : updatedFeatures) {
                jdbcTemplate.update(
                        PackageQueries.UPDATE_PACKAGE_FEATURE,
                        feature.getFeatureName(),
                        feature.getFeatureValue(),
                        feature.getFeatureDescription(),
                        feature.getStatus(),
                        feature.getColor(),
                        feature.getHoverColor(),
                        feature.getSpecialNote(),
                        userId,
                        feature.getFeatureId(),
                        packageId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating package feature", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update package feature", e);
            throw new InternalServerErrorExceptionHandler("Failed to update package feature");
        }
    }

    @Override
    public PackageStatisticsResponse.Summary getPackageSummaryStatistics() {
        try {
            LOGGER.info("Executing query to fetch package summary statistics.");

            return jdbcTemplate.queryForObject(
                    PackageQueries.GET_PACKAGE_SUMMARY_STATISTICS,
                    (rs, rowNum) -> PackageStatisticsResponse.Summary.builder()
                            .totalPackages(rs.getLong("total_packages"))
                            .activePackages(rs.getLong("active_packages"))
                            .averagePackageRating(rs.getBigDecimal("average_package_rating"))
                            .totalParticipants(rs.getLong("total_participants"))
                            .averagePackagePrice(rs.getBigDecimal("average_package_price"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package summary statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package summary statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package summary statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package summary statistics");
        }
    }

    @Override
    public List<PackageStatisticsResponse.PackagePopularity> getPackagePopularityStatistics() {
        try {
            LOGGER.info("Executing query to fetch package popularity statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_POPULARITY_STATISTICS,
                    (rs, rowNum) -> PackageStatisticsResponse.PackagePopularity.builder()
                            .packageId(rs.getLong("package_id"))
                            .packageName(rs.getString("package_name"))
                            .totalSchedules(rs.getInt("total_schedules"))
                            .totalParticipants(rs.getInt("total_participants"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package popularity statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package popularity statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package popularity statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package popularity statistics");
        }
    }

    @Override
    public List<PackageStatisticsResponse.PackageRatingOverview> getPackageRatingOverviewStatistics() {
        try {
            LOGGER.info("Executing query to fetch package rating overview statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_RATING_OVERVIEW_STATISTICS,
                    (rs, rowNum) -> PackageStatisticsResponse.PackageRatingOverview.builder()
                            .packageId(rs.getLong("package_id"))
                            .packageName(rs.getString("package_name"))
                            .averageRating(rs.getBigDecimal("average_rating"))
                            .totalReviews(rs.getInt("total_reviews"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package rating overview statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package rating overview statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package rating overview statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package rating overview statistics");
        }
    }

    @Override
    public List<PackageStatisticsResponse.PackageCapacityUtilization> getPackageCapacityUtilizationStatistics() {
        try {
            LOGGER.info("Executing query to fetch package capacity utilization statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_CAPACITY_UTILIZATION_STATISTICS,
                    (rs, rowNum) -> PackageStatisticsResponse.PackageCapacityUtilization.builder()
                            .packageId(rs.getLong("package_id"))
                            .packageName(rs.getString("package_name"))
                            .minPersonCount(rs.getInt("min_person_count"))
                            .maxPersonCount(rs.getInt("max_person_count"))
                            .averageParticipants(rs.getBigDecimal("average_participants"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package capacity utilization statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package capacity utilization statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package capacity utilization statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package capacity utilization statistics");
        }
    }

    @Override
    public List<PackageStatisticsResponse.PackageTypeDistribution> getPackageTypeDistributionStatistics() {
        try {
            LOGGER.info("Executing query to fetch package type distribution statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_TYPE_DISTRIBUTION_STATISTICS,
                    (rs, rowNum) -> PackageStatisticsResponse.PackageTypeDistribution.builder()
                            .packageTypeName(rs.getString("package_type_name"))
                            .totalPackages(rs.getInt("total_packages"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package type distribution statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package type distribution statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package type distribution statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package type distribution statistics");
        }
    }

    @Override
    public List<PackageStatisticsResponse.PackagePriceDistribution> getPackagePriceDistributionStatistics() {
        try {
            LOGGER.info("Executing query to fetch package price distribution statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_PRICE_DISTRIBUTION_STATISTICS,
                    (rs, rowNum) -> PackageStatisticsResponse.PackagePriceDistribution.builder()
                            .packageId(rs.getLong("package_id"))
                            .packageName(rs.getString("package_name"))
                            .totalPrice(rs.getBigDecimal("total_price"))
                            .pricePerPerson(rs.getBigDecimal("price_per_person"))
                            .totalParticipants(rs.getInt("total_participants"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package price distribution statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package price distribution statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package price distribution statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package price distribution statistics");
        }
    }

    @Override
    public PackageScheduleStatisticsResponse.Summary getPackageScheduleSummaryStatistics() {
        try {
            LOGGER.info("Executing query to fetch package schedule summary statistics.");

            return jdbcTemplate.queryForObject(
                    PackageQueries.GET_PACKAGE_SCHEDULE_SUMMARY_STATISTICS,
                    (rs, rowNum) -> PackageScheduleStatisticsResponse.Summary.builder()
                            .totalSchedules(rs.getLong("total_schedules"))
                            .activeSchedules(rs.getLong("active_schedules"))
                            .averageScheduleRating(rs.getBigDecimal("average_schedule_rating"))
                            .totalParticipants(rs.getLong("total_participants"))
                            .averageDuration(rs.getBigDecimal("average_duration"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule summary statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package schedule summary statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule summary statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package schedule summary statistics");
        }
    }

    @Override
    public List<PackageScheduleStatisticsResponse.ScheduleTimeline> getPackageScheduleTimelineStatistics() {
        try {
            LOGGER.info("Executing query to fetch package schedule timeline statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_SCHEDULE_TIMELINE_STATISTICS,
                    (rs, rowNum) -> PackageScheduleStatisticsResponse.ScheduleTimeline.builder()
                            .timeline(rs.getString("timeline"))
                            .totalSchedules(rs.getInt("total_schedules"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule timeline statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package schedule timeline statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule timeline statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package schedule timeline statistics");
        }
    }

    @Override
    public List<PackageScheduleStatisticsResponse.ScheduleStatusDistribution> getPackageScheduleStatusDistributionStatistics() {
        try {
            LOGGER.info("Executing query to fetch package schedule status distribution statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_SCHEDULE_STATUS_DISTRIBUTION_STATISTICS,
                    (rs, rowNum) -> PackageScheduleStatisticsResponse.ScheduleStatusDistribution.builder()
                            .statusId(rs.getInt("status_id"))
                            .totalSchedules(rs.getInt("total_schedules"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule status distribution statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package schedule status distribution statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule status distribution statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package schedule status distribution statistics");
        }
    }

    @Override
    public List<PackageScheduleStatisticsResponse.DurationDistribution> getPackageScheduleDurationDistributionStatistics() {
        try {
            LOGGER.info("Executing query to fetch package schedule duration distribution statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_SCHEDULE_DURATION_DISTRIBUTION_STATISTICS,
                    (rs, rowNum) -> PackageScheduleStatisticsResponse.DurationDistribution.builder()
                            .scheduleId(rs.getLong("schedule_id"))
                            .scheduleName(rs.getString("schedule_name"))
                            .durationStart(rs.getInt("duration_start"))
                            .durationEnd(rs.getInt("duration_end"))
                            .averageDuration(rs.getBigDecimal("average_duration"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule duration distribution statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package schedule duration distribution statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule duration distribution statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package schedule duration distribution statistics");
        }
    }

    @Override
    public List<PackageScheduleStatisticsResponse.ScheduleParticipationPerformance> getPackageScheduleParticipationPerformanceStatistics() {
        try {
            LOGGER.info("Executing query to fetch package schedule participation performance statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_SCHEDULE_PARTICIPATION_PERFORMANCE_STATISTICS,
                    (rs, rowNum) -> PackageScheduleStatisticsResponse.ScheduleParticipationPerformance.builder()
                            .scheduleId(rs.getLong("schedule_id"))
                            .scheduleName(rs.getString("schedule_name"))
                            .totalParticipants(rs.getInt("total_participants"))
                            .averageParticipants(rs.getBigDecimal("average_participants"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule participation performance statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package schedule participation performance statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule participation performance statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package schedule participation performance statistics");
        }
    }

    @Override
    public List<PackageScheduleStatisticsResponse.ScheduleRatingOverview> getPackageScheduleRatingOverviewStatistics() {
        try {
            LOGGER.info("Executing query to fetch package schedule rating overview statistics.");

            return jdbcTemplate.query(
                    PackageQueries.GET_PACKAGE_SCHEDULE_RATING_OVERVIEW_STATISTICS,
                    (rs, rowNum) -> PackageScheduleStatisticsResponse.ScheduleRatingOverview.builder()
                            .scheduleId(rs.getLong("schedule_id"))
                            .scheduleName(rs.getString("schedule_name"))
                            .averageRating(rs.getBigDecimal("average_rating"))
                            .totalReviews(rs.getInt("total_reviews"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package schedule rating overview statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package schedule rating overview statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package schedule rating overview statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package schedule rating overview statistics");
        }
    }

    @Override
    public PackageTypeStatisticsResponse.Summary getPackageTypeSummaryStatistics() {
        try {
            LOGGER.info("Fetching package type summary statistics");

            return jdbcTemplate.queryForObject(
                    PackageQueries.GET_PACKAGE_TYPE_SUMMARY,
                    (rs, rowNum) -> PackageTypeStatisticsResponse.Summary.builder()
                            .totalPackageTypes(rs.getLong("total_package_types"))
                            .mostUsedTypeCount(rs.getLong("total_packages"))
                            .highestRatedTypeRating(rs.getBigDecimal("average_rating"))
                            .highestRevenueTypeValue(rs.getBigDecimal("total_revenue"))
                            .build()
            );

        } catch (Exception ex) {
            LOGGER.error("Error fetching package type summary", ex);
            throw new RuntimeException("Failed to fetch package type summary");
        }
    }

    @Override
    public List<PackageTypeStatisticsResponse.TypeDistribution> getPackageTypesDistributionStatistics() {
        return jdbcTemplate.query(
                PackageQueries.GET_TYPE_DISTRIBUTION,
                (rs, rowNum) -> PackageTypeStatisticsResponse.TypeDistribution.builder()
                        .typeId(rs.getLong("type_id"))
                        .typeName(rs.getString("type_name"))
                        .totalPackages(rs.getLong("total_packages"))
                        .build()
        );
    }

    @Override
    public List<PackageTypeStatisticsResponse.TypeRevenuePerformance> getPackageTypeRevenuePerformanceStatistics() {
        return jdbcTemplate.query(
                PackageQueries.GET_TYPE_REVENUE_PERFORMANCE,
                (rs, rowNum) -> PackageTypeStatisticsResponse.TypeRevenuePerformance.builder()
                        .typeId(rs.getLong("type_id"))
                        .typeName(rs.getString("type_name"))
                        .totalRevenue(rs.getBigDecimal("total_revenue"))
                        .averagePackagePrice(rs.getBigDecimal("avg_price"))
                        .build()
        );
    }

    @Override
    public List<PackageTypeStatisticsResponse.TypeParticipationImpact> getPackageTypeParticipationImpactStatistics() {
        return jdbcTemplate.query(
                PackageQueries.GET_TYPE_PARTICIPATION_IMPACT,
                (rs, rowNum) -> PackageTypeStatisticsResponse.TypeParticipationImpact.builder()
                        .typeName(rs.getString("type_name"))
                        .month(rs.getString("month"))
                        .totalParticipants(rs.getLong("total_participants"))
                        .build()
        );
    }

    @Override
    public List<PackageTypeStatisticsResponse.TypePrimarySecondaryUsage> getPackageTypePrimarySecondaryUsageStatistics() {
        return jdbcTemplate.query(
                PackageQueries.GET_TYPE_PRIMARY_SECONDARY_USAGE,
                (rs, rowNum) -> PackageTypeStatisticsResponse.TypePrimarySecondaryUsage.builder()
                        .typeId(rs.getLong("type_id"))
                        .typeName(rs.getString("type_name"))
                        .primaryCount(rs.getLong("primary_count"))
                        .secondaryCount(rs.getLong("secondary_count"))
                        .build()
        );
    }

    @Override
    public List<PackageTypeStatisticsResponse.TypeBookingPerformance> getPackageTypeBookingPerformanceStatistics() {
        return jdbcTemplate.query(
                PackageQueries.GET_TYPE_BOOKING_PERFORMANCE,
                (rs, rowNum) -> PackageTypeStatisticsResponse.TypeBookingPerformance.builder()
                        .typeId(rs.getLong("type_id"))
                        .typeName(rs.getString("type_name"))
                        .build()
        );
    }

    @Override
    public List<PackageTypeStatisticsResponse.TypeRatingOverview> getPackageTypeRatingOverviewStatistics() {
        return jdbcTemplate.query(
                PackageQueries.GET_TYPE_RATING_OVERVIEW,
                (rs, rowNum) -> PackageTypeStatisticsResponse.TypeRatingOverview.builder()
                        .typeId(rs.getLong("type_id"))
                        .typeName(rs.getString("type_name"))
                        .averageRating(rs.getBigDecimal("avg_rating"))
                        .totalReviews(rs.getLong("total_reviews"))
                        .build()
        );
    }

    @Override
    public void removeAllPackageImages(Long packageId, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.update(
                    PackageQueries.REMOVE_ALL_PACKAGE_IMAGES,
                    statusId,
                    userId,
                    packageId
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove all package images", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove all package images", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove all package images");
        }
    }

    @Override
    public void removeAllPackageFeatures(Long packageId, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.update(
                    PackageQueries.REMOVE_ALL_PACKAGE_FEATURES,
                    statusId,
                    userId,
                    packageId
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove all package features", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove all package features", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove all package features");
        }
    }

    @Override
    public void removeAllPcakageInclusions(Long packageId, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.update(
                    PackageQueries.REMOVE_ALL_PACKAGE_INCLUSIONS,
                    statusId,
                    userId,
                    packageId
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove all package inclusions", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove all package inclusions", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove all package inclusions");
        }
    }

    @Override
    public void removeAllPackageExclusions(Long packageId, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.update(
                    PackageQueries.REMOVE_ALL_PACKAGE_EXCLUSIONS,
                    statusId,
                    userId,
                    packageId
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove all package exclusions", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove all package exclusions", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove all package exclusions");
        }
    }

    @Override
    public void removeAllPcakageConditions(Long packageId, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.update(
                    PackageQueries.REMOVE_ALL_PACKAGE_CONDITIONS,
                    statusId,
                    userId,
                    packageId
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove all package conditions", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove all package conditions", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove all package conditions");
        }
    }

    @Override
    public void removeAllPcakageTravelTips(Long packageId, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.update(
                    PackageQueries.REMOVE_ALL_PACKAGE_TRAVEL_TIPS,
                    statusId,
                    userId,
                    packageId
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove all package travel tips", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove all package travel tips", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove all package travel tips");
        }
    }

    @Override
    public List<HotelsNamesAndIdsDto> getHotelNamesAndIds(AddPackageParamRequest req) {
        try {
            return jdbcTemplate.query(
                    PackageQueries.GET_HOTEL_NAMES_AND_IDS,
                    (rs, rowNum) -> HotelsNamesAndIdsDto.builder()
                            .hotelId(rs.getLong("hotel_id"))
                            .hotelName(rs.getString("hotel_name"))
                            .starRating(rs.getInt("star_rating"))
                            .build()
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch hotels", e);
            throw new DataAccessErrorExceptionHandler("Failed to fetch hotels");
        }
    }

    @Override
    public List<VehicleNumberIdTypeDto> getVehicleNumberIdType(AddPackageParamRequest res) {
        try {
            return jdbcTemplate.query(
                    PackageQueries.GET_VEHICLE_NUMBER_AND_TYPE,
                    (rs, rowNum) -> VehicleNumberIdTypeDto.builder()
                            .vehicleId(rs.getLong("vehicle_id"))
                            .vehicleNumber(rs.getString("registration_number"))
                            .vehicleType(rs.getString("vehicle_type"))
                            .specificationId(rs.getLong("specification_id"))
                            .build()
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch vehicles", e);
            throw new DataAccessErrorExceptionHandler("Failed to fetch vehicles");
        }
    }

    @Override
    public List<String> getTourInclusionsNames(AddPackageParamRequest res) {
        try {
            return jdbcTemplate.query(
                    PackageQueries.GET_TOUR_INCLUSIONS,
                    new Object[]{res.getTourId()},
                    (rs, rowNum) -> rs.getString("inclusion_text")
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch inclusions", e);
            throw new DataAccessErrorExceptionHandler("Failed to fetch inclusions");
        }
    }

    @Override
    public List<String> getTourExclusionsNames(AddPackageParamRequest res) {
        try {
            return jdbcTemplate.query(
                    PackageQueries.GET_TOUR_EXCLUSIONS,
                    new Object[]{res.getTourId()},
                    (rs, rowNum) -> rs.getString("exclusion_text")
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch exclusions", e);
            throw new DataAccessErrorExceptionHandler("Failed to fetch exclusions");
        }
    }

    @Override
    public List<String> getTourConditions(AddPackageParamRequest res) {
        try {
            return jdbcTemplate.query(
                    PackageQueries.GET_TOUR_CONDITIONS,
                    new Object[]{res.getTourId()},
                    (rs, rowNum) -> rs.getString("condition_text")
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch conditions", e);
            throw new DataAccessErrorExceptionHandler("Failed to fetch conditions");
        }
    }

    @Override
    public List<AddPackageParamResponse.TravelTips> getTourTravelTips(AddPackageParamRequest res) {
        try {
            return jdbcTemplate.query(
                    PackageQueries.GET_TOUR_TRAVEL_TIPS,
                    new Object[]{res.getTourId()},
                    (rs, rowNum) -> AddPackageParamResponse.TravelTips.builder()
                            .title(rs.getString("tip_title"))
                            .description(rs.getString("tip_description"))
                            .build()
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch travel tips", e);
            throw new DataAccessErrorExceptionHandler("Failed to fetch travel tips");
        }
    }

    @Override
    public void removeAllDayByDayAccommodations(Long packageId, Long userId) {
        try {
            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());

            jdbcTemplate.update(
                    PackageQueries.REMOVE_ALL_DAY_ACCOMMODATIONS,
                    statusId,
                    userId,
                    packageId
            );

        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove all day accommodations", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove all day accommodations", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove all day accommodations");
        }
    }

}