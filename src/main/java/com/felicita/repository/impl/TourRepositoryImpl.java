package com.felicita.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felicita.exception.*;
import com.felicita.model.dto.*;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.queries.TourQueries;
import com.felicita.repository.TourRepository;
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

import static com.felicita.queries.TourQueries.*;

@Repository
public class TourRepositoryImpl implements TourRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TourRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<TourResponseDto> getAllTours() {

        try {

            return jdbcTemplate.query(TourQueries.GET_ALL_TOURS, (rs, rowNum) -> {

                TourResponseDto tour = null;
                try {
                    tour = TourResponseDto.builder()
                            .tourId(rs.getLong("tour_id"))
                            .tourName(rs.getString("tour_name"))
                            .tourDescription(rs.getString("tour_description"))
                            .duration(rs.getObject("duration", Integer.class))
                            .latitude(rs.getObject("latitude", Double.class))
                            .longitude(rs.getObject("longitude", Double.class))
                            .startLocation(rs.getString("start_location"))
                            .endLocation(rs.getString("end_location"))
                            .tourStartingPrice(rs.getDouble("min_price"))
                            .seasonName(rs.getString("season_name"))
                            .seasonDescription(rs.getString("season_description"))
                            .statusName(rs.getString("status_name"))
                            .tourTypeDtos(parseTourTypes(rs.getString("tour_types")))
                            .tourCategoryDto(parseTourCategories(rs.getString("tour_categories")))
                            .schedules(parseSchedules(rs.getString("schedules")))
                            .images(parseImages(rs.getString("images")))
                            .build();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                return tour;
            });

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler("Database error while fetching tours");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching tours");
        }
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<TourTypeDto> parseTourTypes(String json) throws JsonProcessingException {
        if (json == null) return new ArrayList<>();
        return objectMapper.readValue(json, new TypeReference<List<TourTypeDto>>() {
        });
    }

    private List<TourCategoryDto> parseTourCategories(String json) throws JsonProcessingException {
        if (json == null) return new ArrayList<>();
        return objectMapper.readValue(json, new TypeReference<List<TourCategoryDto>>() {
        });
    }

    private List<TourScheduleResponseDto> parseSchedules(String json) throws JsonProcessingException {
        if (json == null) return new ArrayList<>();
        return objectMapper.readValue(json, new TypeReference<List<TourScheduleResponseDto>>() {
        });
    }

    private List<TourImageResponseDto> parseImages(String json) throws JsonProcessingException {
        if (json == null) return new ArrayList<>();
        return objectMapper.readValue(json, new TypeReference<List<TourImageResponseDto>>() {
        });
    }

    @Override
    public List<PopularTourResponseDto> getPopularTours() {
        try {
            return jdbcTemplate.query(TourQueries.GET_POPULAR_TOURS, rs -> {
                Map<Integer, PopularTourResponseDto> tourMap = new LinkedHashMap<>();

                while (rs.next()) {
                    int tourId = rs.getInt("tour_id");

                    PopularTourResponseDto tour = tourMap.computeIfAbsent(tourId, id -> {
                        PopularTourResponseDto t = new PopularTourResponseDto();
                        t.setTourId(tourId);
                        try {
                            t.setTourName(rs.getString("tour_name"));
                            t.setTourDescription(rs.getString("tour_description"));
                            t.setDuration(rs.getObject("tour_duration") != null ? rs.getInt("tour_duration") : null);
                            t.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                            t.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
                            t.setStartLocation(rs.getString("start_location"));
                            t.setEndLocation(rs.getString("end_location"));
                            t.setSeason(rs.getString("season"));
                            t.setTourStatus(rs.getString("tour_status"));
                            t.setCategoryDtos(new ArrayList<>());
                            t.setTypeDtos(new ArrayList<>());
                            t.setImages(new ArrayList<>());
                            t.setSchedules(new ArrayList<>());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        return t;
                    });

                    // --- Handle multiple categories ---
                    Long categoryId = rs.getObject("tour_category_id", Long.class);
                    if (categoryId != null && tour.getCategoryDtos().stream().noneMatch(c -> c.getTourCategoryId().equals(categoryId))) {
                        tour.getCategoryDtos().add(
                                TourCategoryDto.builder()
                                        .tourCategoryId(categoryId)
                                        .tourCategoryName(rs.getString("tour_category_name"))
                                        .tourCategoryDescription(rs.getString("tour_category_description"))
                                        .build()
                        );
                    }

                    // --- Handle multiple types ---
                    Long typeId = rs.getObject("tour_type_id", Long.class);
                    if (typeId != null && tour.getTypeDtos().stream().noneMatch(c -> c.getTourTypeId().equals(typeId))) {
                        tour.getTypeDtos().add(
                                TourTypeDto.builder()
                                        .tourTypeId(typeId)
                                        .tourTypeName(rs.getString("tour_type_name"))
                                        .tourTypeDescription(rs.getString("tour_type_description"))
                                        .build()
                        );
                    }

                    // --- Handle images ---
                    String imageUrl = rs.getString("tour_image");
                    String imageName = rs.getString("tour_image_name");
                    if (imageUrl != null && tour.getImages().stream().noneMatch(img -> img.getImageUrl().equals(imageUrl))) {
                        tour.getImages().add(new PopularTourImagesDto(imageName, imageUrl));
                    }

                    // --- Handle schedules ---
                    int scheduleId = rs.getInt("schedule_id");
                    if (!rs.wasNull()) {
                        popularTourScheduleResponseDto schedule = tour.getSchedules().stream()
                                .filter(s -> s.getScheduleId() == scheduleId)
                                .findFirst()
                                .orElseGet(() -> {
                                    popularTourScheduleResponseDto s = new popularTourScheduleResponseDto();
                                    s.setScheduleId(scheduleId);
                                    try {
                                        s.setScheduleName(rs.getString("schedule_name"));
                                        s.setAssumeStartDate(rs.getObject("assume_start_date") != null ? rs.getDate("assume_start_date").toLocalDate() : null);
                                        s.setAssumeEndDate(rs.getObject("assume_end_date") != null ? rs.getDate("assume_end_date").toLocalDate() : null);
                                        s.setDurationStart(rs.getObject("duration_start") != null ? rs.getInt("duration_start") : null);
                                        s.setDurationEnd(rs.getObject("duration_end") != null ? rs.getInt("duration_end") : null);
                                        s.setSpecialNote(rs.getString("special_note"));
                                        s.setScheduleDescription(rs.getString("schedule_description"));
                                        s.setScheduleStatus(rs.getString("schedule_status"));
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }

                                    s.setDestinations(new ArrayList<>());
                                    s.setReviews(new ArrayList<>());
                                    tour.getSchedules().add(s);
                                    return s;
                                });

                        // --- Handle destinations ---
                        int destinationId = rs.getInt("destination_id");
                        if (!rs.wasNull() && destinationId > 0 &&
                                schedule.getDestinations().stream().noneMatch(d -> d.getDestinationId() == destinationId)) {
                            schedule.getDestinations().add(new TourDestinationResponseDto(
                                    destinationId,
                                    rs.getString("destination_name"),
                                    rs.getString("destination_description"),
                                    rs.getString("destination_location"),
                                    rs.getString("destination_status")
                            ));
                        }

                        // --- Handle reviews ---
                        int reviewId = rs.getInt("review_id");
                        if (!rs.wasNull() && reviewId > 0 &&
                                schedule.getReviews().stream().noneMatch(r -> r.getReviewId() == reviewId)) {
                            schedule.getReviews().add(new TourReviewResponseDto(
                                    reviewId,
                                    rs.getString("reviewer_name"),
                                    rs.getString("review"),
                                    rs.getObject("rating") != null ? rs.getDouble("rating") : null,
                                    rs.getString("review_description"),
                                    rs.getObject("number_of_participate") != null ? rs.getInt("number_of_participate") : null,
                                    rs.getString("review_status"),
                                    rs.getTimestamp("review_created_at") != null ? rs.getTimestamp("review_created_at").toLocalDateTime() : null
                            ));
                        }
                    }
                }

                return new ArrayList<>(tourMap.values());
            });

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler("Database error while fetching tours");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching tours");
        }
    }

    @Override
    public TourResponseDto getTourDetailsById(Long tourId) {
        try {
            return jdbcTemplate.query(TourQueries.GET_TOUR_DETAILS_BY_ID, new Object[]{tourId}, (ResultSet rs) -> {
                TourResponseDto tour = null;

                while (rs.next()) {
                    if (tour == null) {
                        tour = TourResponseDto.builder()
                                .tourId(rs.getLong("tour_id"))
                                .tourName(rs.getString("tour_name"))
                                .tourDescription(rs.getString("tour_description"))
                                .duration(rs.getObject("duration", Integer.class))
                                .latitude(rs.getObject("latitude", Double.class))
                                .longitude(rs.getObject("longitude", Double.class))
                                .startLocation(rs.getString("start_location"))
                                .endLocation(rs.getString("end_location"))
                                .seasonName(rs.getString("season_name"))
                                .seasonDescription(rs.getString("season_description"))
                                .statusName(rs.getString("status_name"))
                                .tourTypeDtos(new ArrayList<>())
                                .tourCategoryDto(new ArrayList<>())
                                .schedules(new ArrayList<>())
                                .images(new ArrayList<>())
                                .build();
                    }

                    // ----- Handle Tour Types -----
                    Long tourTypeId = rs.getLong("tour_type_id");
                    if (tourTypeId != 0 && tour.getTourTypeDtos().stream().noneMatch(tt -> tt.getTourTypeId().equals(tourTypeId))) {
                        tour.getTourTypeDtos().add(
                                TourTypeDto.builder()
                                        .tourTypeId(tourTypeId)
                                        .tourTypeName(rs.getString("tour_type_name"))
                                        .tourTypeDescription(rs.getString("tour_type_description"))
                                        .build()
                        );
                    }

                    // ----- Handle Tour Categories -----
                    Long tourCategoryId = rs.getLong("tour_category_id");
                    if (tourCategoryId != 0 && tour.getTourCategoryDto().stream().noneMatch(tc -> tc.getTourCategoryId().equals(tourCategoryId))) {
                        tour.getTourCategoryDto().add(
                                TourCategoryDto.builder()
                                        .tourCategoryId(tourCategoryId)
                                        .tourCategoryName(rs.getString("tour_category_name"))
                                        .tourCategoryDescription(rs.getString("tour_category_description"))
                                        .build()
                        );
                    }

                    // ----- Handle Schedules -----
                    int scheduleId = rs.getInt("schedule_id");
                    if (!rs.wasNull() && rs.getString("schedule_name") != null) {
                        if (tour.getSchedules().stream().noneMatch(s -> s.getScheduleId() == scheduleId)) {
                            TourScheduleResponseDto schedule = new TourScheduleResponseDto();
                            schedule.setScheduleId(scheduleId);
                            schedule.setScheduleName(rs.getString("schedule_name"));
                            schedule.setAssumeStartDate(rs.getObject("assume_start_date", LocalDate.class));
                            schedule.setAssumeEndDate(rs.getObject("assume_end_date", LocalDate.class));
                            schedule.setDurationStart(rs.getObject("duration_start", Integer.class));
                            schedule.setDurationEnd(rs.getObject("duration_end", Integer.class));
                            schedule.setSpecialNote(rs.getString("special_note"));
                            schedule.setScheduleDescription(rs.getString("schedule_description"));
                            tour.getSchedules().add(schedule);
                        }
                    }

                    // ----- Handle Images -----
                    int imageId = rs.getInt("image_id");
                    if (!rs.wasNull() && rs.getString("image_url") != null) {
                        if (tour.getImages().stream().noneMatch(i -> i.getImageId() == imageId)) {
                            TourImageResponseDto image = new TourImageResponseDto();
                            image.setImageId(imageId);
                            image.setImageName(rs.getString("image_name"));
                            image.setImageDescription(rs.getString("image_description"));
                            image.setImageUrl(rs.getString("image_url"));
                            tour.getImages().add(image);
                        }
                    }
                }

                return tour; // returns null if tourId not found
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching tour details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching tour details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching tour details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching tour details");
        }
    }

    @Override
    public List<TourReviewDetailsResponse> getAllTourReviewDetails() {
        String GET_TOUR_REVIEWS_DETAILS = TourQueries.GET_TOUR_REVIEWS_DETAILS;

        try {
            List<TourReviewDetailsResponse> reviews = jdbcTemplate.query(GET_TOUR_REVIEWS_DETAILS, (ResultSet rs) -> {

                Map<Integer, TourReviewDetailsResponse> reviewMap = new LinkedHashMap<>();

                while (rs.next()) {
                    int reviewId = rs.getInt("review_id");

                    // 🔹 Get or create main review
                    TourReviewDetailsResponse review = reviewMap.computeIfAbsent(reviewId, id ->
                            {
                                try {
                                    return TourReviewDetailsResponse.builder()
                                            .reviewId(rs.getInt("review_id"))
                                            .tourScheduleId(rs.getInt("tour_schedule_id"))
                                            .tourId(rs.getInt("tour_id"))
                                            .tourName(rs.getString("tour_name"))
                                            .reviewName(rs.getString("review_name"))
                                            .review(rs.getString("review"))
                                            .rating(rs.getBigDecimal("rating"))
                                            .reviewDescription(rs.getString("review_description"))
                                            .reviewStatus(rs.getString("review_status"))
                                            .numberOfParticipate(rs.getInt("number_of_participate"))
                                            .reviewCreatedBy(rs.getInt("review_created_by"))
                                            .reviewCreatedUser(rs.getString("review_created_user"))
                                            .reviewCreatedAt(getLocalDateTime(rs, "review_created_at"))
                                            .reviewUpdatedBy(rs.getObject("review_updated_by") != null ? rs.getInt("review_updated_by") : null)
                                            .reviewUpdatedAt(getLocalDateTime(rs, "review_updated_at"))
                                            .images(new ArrayList<>())
                                            .reactions(new ArrayList<>())
                                            .comments(new ArrayList<>())
                                            .build();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );

                    // 🔹 Add image if exists
                    if (rs.getObject("image_id") != null) {
                        boolean imageExists = review.getImages().stream()
                                .anyMatch(i -> {
                                    try {
                                        return i.getImageId().equals(rs.getInt("image_id"));
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                        if (!imageExists) {
                            review.getImages().add(
                                    TourReviewDetailsResponse.Image.builder()
                                            .imageId(rs.getInt("image_id"))
                                            .imageName(rs.getString("image_name"))
                                            .imageDescription(rs.getString("image_description"))
                                            .imageUrl(rs.getString("image_url"))
                                            .imageStatus(rs.getString("image_status"))
                                            .imageCreatedBy(rs.getInt("image_created_by"))
                                            .imageCreatedAt(getLocalDateTime(rs, "image_created_at"))
                                            .build()
                            );
                        }
                    }

                    // 🔹 Add reaction if exists
                    if (rs.getObject("review_reaction_id") != null) {
                        boolean reactionExists = review.getReactions().stream()
                                .anyMatch(r -> {
                                    try {
                                        return r.getReviewReactionId().equals(rs.getInt("review_reaction_id"));
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                        if (!reactionExists) {
                            review.getReactions().add(
                                    TourReviewDetailsResponse.Reaction.builder()
                                            .reviewReactionId(rs.getInt("review_reaction_id"))
                                            .reactionReviewId(rs.getInt("reaction_review_id"))
                                            .reactionUserId(rs.getInt("reaction_user_id"))
                                            .reactionUserName(rs.getString("reaction_user_name"))
                                            .reactionType(rs.getString("reaction_type"))
                                            .reviewReactionStatus(rs.getString("review_reaction_status"))
                                            .reactionCreatedAt(getLocalDateTime(rs, "reaction_created_at"))
                                            .build()
                            );
                        }
                    }

                    // 🔹 Add comment if exists
                    if (rs.getObject("comment_id") != null) {
                        TourReviewDetailsResponse.Comment comment = review.getComments().stream()
                                .filter(c -> {
                                    try {
                                        return c.getCommentId().equals(rs.getInt("comment_id"));
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .findFirst()
                                .orElseGet(() -> {
                                    TourReviewDetailsResponse.Comment newComment = null;
                                    try {
                                        newComment = TourReviewDetailsResponse.Comment.builder()
                                                .commentId(rs.getInt("comment_id"))
                                                .commentReviewId(rs.getInt("comment_review_id"))
                                                .commentUserId(rs.getInt("comment_user_id"))
                                                .commentUserName(rs.getString("comment_user_name"))
                                                .parentCommentId((Integer) rs.getObject("parent_comment_id"))
                                                .comment(rs.getString("comment"))
                                                .commentStatus(rs.getString("comment_status"))
                                                .commentCreatedAt(getLocalDateTime(rs, "comment_created_at"))
                                                .commentCreatedBy(rs.getInt("comment_created_by"))
                                                .commentReactions(new ArrayList<>())
                                                .build();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    review.getComments().add(newComment);
                                    return newComment;
                                });

                        // 🔹 Add comment reaction if exists
                        if (rs.getObject("comment_reaction_id") != null) {
                            boolean commentReactionExists = comment.getCommentReactions().stream()
                                    .anyMatch(cr -> {
                                        try {
                                            return cr.getCommentReactionId().equals(rs.getInt("comment_reaction_id"));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                            if (!commentReactionExists) {
                                comment.getCommentReactions().add(
                                        TourReviewDetailsResponse.CommentReaction.builder()
                                                .commentReactionId(rs.getInt("comment_reaction_id"))
                                                .commentReactionCommentId(rs.getInt("comment_reaction_comment_id"))
                                                .commentReactionUserId(rs.getInt("comment_reaction_user_id"))
                                                .commentReactionUserName(rs.getString("comment_reaction_user_name"))
                                                .commentReactionType(rs.getString("comment_reaction_type"))
                                                .commentReactionStatus(rs.getString("comment_reaction_status"))
                                                .commentReactionCreatedBy(rs.getInt("comment_reaction_created_by"))
                                                .commentReactionCreatedAt(getLocalDateTime(rs, "comment_reaction_created_at"))
                                                .build()
                                );
                            }
                        }
                    }
                }

                return new ArrayList<>(reviewMap.values());
            });

            return reviews;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching tour details", ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching tour details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching tour details", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching tour details");
        }
    }


    @Override
    public List<TourReviewDetailsResponse> getTourReviewDetailsById(Long tourId) {
        String GET_TOUR_REVIEW_DETAILS_BY_ID = TourQueries.GET_TOUR_REVIEW_DETAILS_BY_ID;

        try {
            return jdbcTemplate.query(con -> {
                var ps = con.prepareStatement(GET_TOUR_REVIEW_DETAILS_BY_ID);
                ps.setLong(1, tourId);
                return ps;
            }, rs -> {
                Map<Integer, TourReviewDetailsResponse> reviewMap = new LinkedHashMap<>();

                while (rs.next()) {
                    int reviewId = rs.getInt("review_id");

                    // --- 1️⃣ Create or Get Main Review ---
                    TourReviewDetailsResponse review = reviewMap.computeIfAbsent(reviewId, id ->
                            {
                                try {
                                    return TourReviewDetailsResponse.builder()
                                            .reviewId(rs.getInt("review_id"))
                                            .tourScheduleId(rs.getInt("tour_schedule_id"))
                                            .tourId(rs.getInt("tour_id"))
                                            .tourName(rs.getString("tour_name"))
                                            .reviewName(rs.getString("review_name"))
                                            .review(rs.getString("review"))
                                            .rating(rs.getBigDecimal("rating"))
                                            .reviewDescription(rs.getString("review_description"))
                                            .reviewStatus(rs.getString("review_status"))
                                            .numberOfParticipate(rs.getInt("number_of_participate"))
                                            .reviewCreatedBy(rs.getInt("review_created_by"))
                                            .reviewCreatedUser(rs.getString("review_created_user"))
                                            .reviewCreatedAt(rs.getTimestamp("review_created_at") != null ?
                                                    rs.getTimestamp("review_created_at").toLocalDateTime() : null)
                                            .reviewUpdatedBy(rs.getInt("review_updated_by"))
                                            .reviewUpdatedAt(rs.getTimestamp("review_updated_at") != null ?
                                                    rs.getTimestamp("review_updated_at").toLocalDateTime() : null)
                                            .images(new ArrayList<>())
                                            .reactions(new ArrayList<>())
                                            .comments(new ArrayList<>())
                                            .build();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );

                    // --- 2️⃣ Add Image ---
                    int imageId = rs.getInt("image_id");
                    if (imageId > 0 && review.getImages().stream().noneMatch(i -> i.getImageId().equals(imageId))) {
                        review.getImages().add(TourReviewDetailsResponse.Image.builder()
                                .imageId(imageId)
                                .imageName(rs.getString("image_name"))
                                .imageDescription(rs.getString("image_description"))
                                .imageUrl(rs.getString("image_url"))
                                .imageStatus(rs.getString("image_status"))
                                .imageCreatedBy(rs.getInt("image_created_by"))
                                .imageCreatedAt(rs.getTimestamp("image_created_at") != null ?
                                        rs.getTimestamp("image_created_at").toLocalDateTime() : null)
                                .build());
                    }

                    // --- 3️⃣ Add Reaction ---
                    int reactionId = rs.getInt("review_reaction_id");
                    if (reactionId > 0 && review.getReactions().stream().noneMatch(r -> r.getReviewReactionId().equals(reactionId))) {
                        review.getReactions().add(TourReviewDetailsResponse.Reaction.builder()
                                .reviewReactionId(reactionId)
                                .reactionReviewId(rs.getInt("reaction_review_id"))
                                .reactionUserId(rs.getInt("reaction_user_id"))
                                .reactionUserName(rs.getString("reaction_user_name"))
                                .reactionType(rs.getString("reaction_type"))
                                .reviewReactionStatus(rs.getString("review_reaction_status"))
                                .reactionCreatedAt(rs.getTimestamp("reaction_created_at") != null ?
                                        rs.getTimestamp("reaction_created_at").toLocalDateTime() : null)
                                .build());
                    }

                    // --- 4️⃣ Add Comment & Comment Reactions ---
                    int commentId = rs.getInt("comment_id");
                    if (commentId > 0) {
                        // find existing comment
                        TourReviewDetailsResponse.Comment comment = review.getComments()
                                .stream()
                                .filter(c -> c.getCommentId().equals(commentId))
                                .findFirst()
                                .orElseGet(() -> {
                                    TourReviewDetailsResponse.Comment newComment = null;
                                    try {
                                        newComment = TourReviewDetailsResponse.Comment.builder()
                                                .commentId(commentId)
                                                .commentReviewId(rs.getInt("comment_review_id"))
                                                .commentUserId(rs.getInt("comment_user_id"))
                                                .commentUserName(rs.getString("comment_user_name"))
                                                .parentCommentId(rs.getInt("parent_comment_id"))
                                                .comment(rs.getString("comment"))
                                                .commentStatus(rs.getString("comment_status"))
                                                .commentCreatedAt(rs.getTimestamp("comment_created_at") != null ?
                                                        rs.getTimestamp("comment_created_at").toLocalDateTime() : null)
                                                .commentCreatedBy(rs.getInt("comment_created_by"))
                                                .commentReactions(new ArrayList<>())
                                                .build();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    review.getComments().add(newComment);
                                    return newComment;
                                });

                        // Add comment reaction
                        int commentReactionId = rs.getInt("comment_reaction_id");
                        if (commentReactionId > 0 && comment.getCommentReactions().stream().noneMatch(cr -> cr.getCommentReactionId().equals(commentReactionId))) {
                            comment.getCommentReactions().add(TourReviewDetailsResponse.CommentReaction.builder()
                                    .commentReactionId(commentReactionId)
                                    .commentReactionCommentId(rs.getInt("comment_reaction_comment_id"))
                                    .commentReactionUserId(rs.getInt("comment_reaction_user_id"))
                                    .commentReactionUserName(rs.getString("comment_reaction_user_name"))
                                    .commentReactionType(rs.getString("comment_reaction_type"))
                                    .commentReactionStatus(rs.getString("comment_reaction_status"))
                                    .commentReactionCreatedBy(rs.getInt("comment_reaction_created_by"))
                                    .commentReactionCreatedAt(rs.getTimestamp("comment_reaction_created_at") != null ?
                                            rs.getTimestamp("comment_reaction_created_at").toLocalDateTime() : null)
                                    .build());
                        }
                    }
                }

                return new ArrayList<>(reviewMap.values());
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching tour reviews for ID {}", tourId, ex);
            throw new DataAccessErrorExceptionHandler("Database error while fetching tour reviews");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching tour reviews for ID {}", tourId, ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error while fetching tour reviews");
        }
    }

    @Override
    public List<TourDestinationsForMapResponse> getTourDestinationsForMap(Long tourId) {
        String GET_TOUR_DESTINATIONS_FOR_MAP = TourQueries.GET_TOUR_DESTINATIONS_FOR_MAP;

        try {
            return jdbcTemplate.query(connection -> {
                var ps = connection.prepareStatement(GET_TOUR_DESTINATIONS_FOR_MAP);
                ps.setLong(1, tourId);
                return ps;
            }, (ResultSet rs) -> {

                List<TourDestinationsForMapResponse> destinations = new ArrayList<>();
                Map<Integer, TourDestinationsForMapResponse> destinationMap = new LinkedHashMap<>();

                while (rs.next()) {
                    int destinationId = rs.getInt("id");

                    // Get or create the destination entry
                    TourDestinationsForMapResponse destination = destinationMap.computeIfAbsent(destinationId, id ->
                            TourDestinationsForMapResponse.builder()
                                    .id(id)
                                    .name(getSafeString(rs, "name"))
                                    .description(getSafeString(rs, "description"))
                                    .lat(getSafeDouble(rs, "lat"))
                                    .lng(getSafeDouble(rs, "lng"))
                                    .images(new ArrayList<>())
                                    .build()
                    );

                    // Add image if available
                    int imageId = rs.getInt("image_id");
                    if (imageId > 0) {
                        destination.getImages().add(
                                TourDestinationsForMapResponse.Image.builder()
                                        .id(imageId)
                                        .url(getSafeString(rs, "image_url"))
                                        .name(getSafeString(rs, "image_name"))
                                        .description(getSafeString(rs, "image_description"))
                                        .build()
                        );
                    }
                }

                destinations.addAll(destinationMap.values());
                return destinations;
            });

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler("Database error while fetching tour destinations");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching tour destinations");
        }
    }

    @Override
    public List<TourHistoryResponse> getAllTourHistoryDetails() {
        String GET_ALL_TOUR_HISTORY_DETAILS = TourQueries.GET_ALL_TOUR_HISTORY_DETAILS;

        try {
            return jdbcTemplate.query(GET_ALL_TOUR_HISTORY_DETAILS, rs -> {
                Map<Long, TourHistoryResponse> historyMap = new LinkedHashMap<>();

                while (rs.next()) {
                    Long historyId = rs.getLong("history_id");
                    Long scheduleId = rs.getLong("schedule_id");
                    Long tourId = rs.getLong("tour_id");
                    Long imageId = rs.getLong("image_id");

                    // Get or create TourHistoryResponse
                    TourHistoryResponse history = historyMap.get(historyId);
                    if (history == null) {
                        history = TourHistoryResponse.builder()
                                .historyId(historyId)
                                .historyName(rs.getString("history_name"))
                                .historyDescription(rs.getString("history_description"))
                                .numberOfParticipate(rs.getInt("number_of_participate"))
                                .rating(rs.getBigDecimal("rating"))
                                .historyDuration(rs.getInt("history_duration"))
                                .startDate(rs.getDate("start_date"))
                                .endDate(rs.getDate("end_date"))
                                .vehicleNumber(rs.getString("vehicle_number"))
                                .driverId(rs.getInt("driver_id"))
                                .guideId(rs.getInt("guide_id"))
                                .historyColor(rs.getString("history_color"))
                                .hoverColor(rs.getString("hover_color"))
                                .historyStatus(rs.getInt("history_status"))
                                .build();

                        // Set TourSchedule
                        TourHistoryResponse.TourSchedule schedule = new TourHistoryResponse.TourSchedule();
                        schedule.setScheduleId(scheduleId);
                        schedule.setScheduleName(rs.getString("schedule_name"));
                        schedule.setAssumeStartDate(rs.getDate("assume_start_date"));
                        schedule.setAssumeEndDate(rs.getDate("assume_end_date"));
                        schedule.setDurationStart(rs.getInt("duration_start"));
                        schedule.setDurationEnd(rs.getInt("duration_end"));
                        schedule.setSpecialNote(rs.getString("special_note"));
                        schedule.setScheduleDescription(rs.getString("schedule_description"));
                        schedule.setScheduleStatus(rs.getInt("schedule_status"));

                        // Set Tour
                        TourHistoryResponse.Tour tour = new TourHistoryResponse.Tour();
                        tour.setTourId(tourId);
                        tour.setTourName(rs.getString("tour_name"));
                        tour.setTourDescription(rs.getString("tour_description"));
                        tour.setTourDuration(rs.getInt("tour_duration"));
                        tour.setLatitude(rs.getBigDecimal("latitude"));
                        tour.setLongitude(rs.getBigDecimal("longitude"));
                        tour.setStartLocation(rs.getString("start_location"));
                        tour.setEndLocation(rs.getString("end_location"));
                        tour.setTourStatus(rs.getInt("tour_status"));
                        tour.setTourType(rs.getInt("tour_type"));
                        tour.setTourCategory(rs.getInt("tour_category"));
                        tour.setSeason(rs.getInt("season"));

                        schedule.setTour(tour);
                        schedule.setImages(new ArrayList<>());

                        history.setTourSchedule(schedule);
                        historyMap.put(historyId, history);
                    }

                    // Add image if exists
                    if (imageId != 0) {
                        TourHistoryResponse.TourHistoryImage image = new TourHistoryResponse.TourHistoryImage();
                        image.setImageId(imageId);
                        image.setImageName(rs.getString("image_name"));
                        image.setImageDescription(rs.getString("image_description"));
                        image.setImageUrl(rs.getString("image_url"));
                        image.setImageColor(rs.getString("image_color"));
                        image.setImageStatus(rs.getInt("image_status"));

                        history.getTourSchedule().getImages().add(image);
                    }
                }

                return new ArrayList<>(historyMap.values());
            });

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler("Database error while fetching tours");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching tours");
        }
    }


    @Override
    public List<TourHistoryResponse> getTourHistoryDetailsById(Long tourId) {
        String GET_TOUR_HISTORY_DETAILS_BY_ID = TourQueries.GET_TOUR_HISTORY_DETAILS_BY_ID;

        try {
            return jdbcTemplate.query(GET_TOUR_HISTORY_DETAILS_BY_ID, ps -> ps.setLong(1, tourId), rs -> {
                Map<Long, TourHistoryResponse> historyMap = new LinkedHashMap<>();

                while (rs.next()) {
                    Long historyId = rs.getLong("history_id");
                    Long scheduleId = rs.getLong("schedule_id");
                    Long tId = rs.getLong("tour_id");
                    Long imageId = rs.getLong("image_id");

                    // Get or create TourHistoryResponse
                    TourHistoryResponse history = historyMap.get(historyId);
                    if (history == null) {
                        history = TourHistoryResponse.builder()
                                .historyId(historyId)
                                .historyName(rs.getString("history_name"))
                                .historyDescription(rs.getString("history_description"))
                                .numberOfParticipate(rs.getInt("number_of_participate"))
                                .rating(rs.getBigDecimal("rating"))
                                .historyDuration(rs.getInt("history_duration"))
                                .startDate(rs.getDate("start_date"))
                                .endDate(rs.getDate("end_date"))
                                .vehicleNumber(rs.getString("vehicle_number"))
                                .driverId(rs.getInt("driver_id"))
                                .guideId(rs.getInt("guide_id"))
                                .historyColor(rs.getString("history_color"))
                                .hoverColor(rs.getString("hover_color"))
                                .historyStatus(rs.getInt("history_status"))
                                .build();

                        // Set TourSchedule
                        TourHistoryResponse.TourSchedule schedule = new TourHistoryResponse.TourSchedule();
                        schedule.setScheduleId(scheduleId);
                        schedule.setScheduleName(rs.getString("schedule_name"));
                        schedule.setAssumeStartDate(rs.getDate("assume_start_date"));
                        schedule.setAssumeEndDate(rs.getDate("assume_end_date"));
                        schedule.setDurationStart(rs.getInt("duration_start"));
                        schedule.setDurationEnd(rs.getInt("duration_end"));
                        schedule.setSpecialNote(rs.getString("special_note"));
                        schedule.setScheduleDescription(rs.getString("schedule_description"));
                        schedule.setScheduleStatus(rs.getInt("schedule_status"));

                        // Set Tour
                        TourHistoryResponse.Tour tour = new TourHistoryResponse.Tour();
                        tour.setTourId(tId);
                        tour.setTourName(rs.getString("tour_name"));
                        tour.setTourDescription(rs.getString("tour_description"));
                        tour.setTourDuration(rs.getInt("tour_duration"));
                        tour.setLatitude(rs.getBigDecimal("latitude"));
                        tour.setLongitude(rs.getBigDecimal("longitude"));
                        tour.setStartLocation(rs.getString("start_location"));
                        tour.setEndLocation(rs.getString("end_location"));
                        tour.setTourStatus(rs.getInt("tour_status"));
                        tour.setTourType(rs.getInt("tour_type"));
                        tour.setTourCategory(rs.getInt("tour_category"));
                        tour.setSeason(rs.getInt("season"));

                        schedule.setTour(tour);
                        schedule.setImages(new ArrayList<>());

                        history.setTourSchedule(schedule);
                        historyMap.put(historyId, history);
                    }

                    // Add image if exists
                    if (imageId != 0) {
                        TourHistoryResponse.TourHistoryImage image = new TourHistoryResponse.TourHistoryImage();
                        image.setImageId(imageId);
                        image.setImageName(rs.getString("image_name"));
                        image.setImageDescription(rs.getString("image_description"));
                        image.setImageUrl(rs.getString("image_url"));
                        image.setImageColor(rs.getString("image_color"));
                        image.setImageStatus(rs.getInt("image_status"));

                        history.getTourSchedule().getImages().add(image);
                    }
                }

                return new ArrayList<>(historyMap.values());
            });

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler("Database error while fetching tour history for tourId: " + tourId);
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching tour history for tourId: " + tourId);
        }
    }

    @Override
    public List<TourHistoryImageResponse> getTourHistoryImagesById(Long tourId) {
        String GET_ALL_TOUR_HISTORY_IMAGES_BY_ID = TourQueries.GET_ALL_TOUR_HISTORY_IMAGES_BY_ID;

        try {
            return jdbcTemplate.query(GET_ALL_TOUR_HISTORY_IMAGES_BY_ID, ps -> ps.setLong(1, tourId), rs -> {
                List<TourHistoryImageResponse> images = new ArrayList<>();
                while (rs.next()) {
                    TourHistoryImageResponse image = TourHistoryImageResponse.builder()
                            .imageId(rs.getInt("image_id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .imageUrl(rs.getString("image_url"))
                            .color(rs.getString("color"))
                            .status(rs.getString("status"))
                            .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                            .createdBy(rs.getObject("created_by") != null ? rs.getInt("created_by") : null)
                            .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                            .updatedBy(rs.getObject("updated_by") != null ? rs.getInt("updated_by") : null)
                            .terminatedAt(rs.getTimestamp("terminated_at") != null ? rs.getTimestamp("terminated_at").toLocalDateTime() : null)
                            .terminatedBy(rs.getObject("terminated_by") != null ? rs.getInt("terminated_by") : null)
                            .build();

                    images.add(image);
                }
                return images;
            });

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler(
                    "Database error while fetching tour history for tourId: ");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching tour history for tourId: ");
        }
    }


    @Override
    public List<TourHistoryImageResponse> getAllTourHistoryImages() {
        String GET_ALL_TOUR_HISTORY_IMAGES = TourQueries.GET_ALL_TOUR_HISTORY_IMAGES;

        try {
            return jdbcTemplate.query(GET_ALL_TOUR_HISTORY_IMAGES, (rs) -> {
                List<TourHistoryImageResponse> images = new ArrayList<>();
                while (rs.next()) {
                    TourHistoryImageResponse image = TourHistoryImageResponse.builder()
                            .imageId(rs.getInt("image_id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .imageUrl(rs.getString("image_url"))
                            .color(rs.getString("color"))
                            .status(rs.getString("status"))
                            .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                            .createdBy(rs.getObject("created_by") != null ? rs.getInt("created_by") : null)
                            .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                            .updatedBy(rs.getObject("updated_by") != null ? rs.getInt("updated_by") : null)
                            .terminatedAt(rs.getTimestamp("terminated_at") != null ? rs.getTimestamp("terminated_at").toLocalDateTime() : null)
                            .terminatedBy(rs.getObject("terminated_by") != null ? rs.getInt("terminated_by") : null)
                            .build();

                    images.add(image);
                }
                return images;
            });

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler("Database error while fetching tours");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching tours");
        }
    }

    @Override
    public ToursDetailsWithParamResponse getToursToShowWithParam(TourDataRequest tourDataRequest) {
        LOGGER.info(tourDataRequest.toString());
        try {
            if (tourDataRequest.getMinPrice() == null && tourDataRequest.getMaxPrice() != null){
                tourDataRequest.setMinPrice(0.0);
            }
            int offset = (tourDataRequest.getPageNumber() - 1) * tourDataRequest.getPageSize();
            List<Integer> tourIds = jdbcTemplate.queryForList(GET_PAGINATED_TOUR_IDS,
                    new Object[]{
                            tourDataRequest.getName(), tourDataRequest.getName(),
                            tourDataRequest.getDuration(), tourDataRequest.getDuration(),
                            tourDataRequest.getLocation(), tourDataRequest.getLocation(), tourDataRequest.getLocation(),
                            tourDataRequest.getTourCategory(), tourDataRequest.getTourCategory(),
                            tourDataRequest.getSeason(), tourDataRequest.getSeason(),
                            tourDataRequest.getTourType(), tourDataRequest.getTourType(),
                            tourDataRequest.getMinPrice(), tourDataRequest.getMaxPrice(),
                            tourDataRequest.getMinPrice(), tourDataRequest.getMaxPrice(),
                            tourDataRequest.getPageSize(), offset
                    }, Integer.class);

            Integer totalTours = jdbcTemplate.queryForObject(
                    COUNT_TOURS_WITH_FILTER,
                    new Object[]{
                            tourDataRequest.getName(), tourDataRequest.getName(),
                            tourDataRequest.getDuration(), tourDataRequest.getDuration(),
                            tourDataRequest.getLocation(), tourDataRequest.getLocation(), tourDataRequest.getLocation(),
                            tourDataRequest.getTourCategory(), tourDataRequest.getTourCategory(),
                            tourDataRequest.getSeason(), tourDataRequest.getSeason(),
                            tourDataRequest.getTourType(), tourDataRequest.getTourType(),
                            tourDataRequest.getMinPrice(), tourDataRequest.getMaxPrice(),
                            tourDataRequest.getMinPrice(), tourDataRequest.getMaxPrice()
                    },
                    Integer.class
            );

            if (tourIds.isEmpty()) {
                return null;
            }
            String inSql = String.join(",", Collections.nCopies(tourIds.size(), "?"));
            String fullQuery = String.format(GET_TOURS_BY_IDS, inSql);

            ArrayList<TourResponseDto> query = jdbcTemplate.query(fullQuery, tourIds.toArray(), (ResultSet rs) -> {
                Map<Long, TourResponseDto> tourMap = new HashMap<>();

                while (rs.next()) {
                    Long tourId = rs.getLong("tour_id");
                    TourResponseDto tour = tourMap.get(tourId);

                    Double tourMinPrice = jdbcTemplate.queryForObject(
                            "SELECT MIN(total_price) FROM packages WHERE tour_id = ?",
                            new Object[]{tourId},
                            Double.class
                    );

                    if (tour == null) {
                        tour = TourResponseDto.builder()
                                .tourId(tourId)
                                .tourName(rs.getString("tour_name"))
                                .tourDescription(rs.getString("tour_description"))
                                .duration(rs.getObject("duration", Integer.class))
                                .latitude(rs.getObject("latitude", Double.class))
                                .longitude(rs.getObject("longitude", Double.class))
                                .startLocation(rs.getString("start_location"))
                                .endLocation(rs.getString("end_location"))
                                .tourStartingPrice(tourMinPrice)
                                .seasonName(rs.getString("season_name"))
                                .seasonDescription(rs.getString("season_description"))
                                .statusName(rs.getString("status_name"))
                                .tourTypeDtos(new ArrayList<>())
                                .tourCategoryDto(new ArrayList<>())
                                .schedules(new ArrayList<>())
                                .images(new ArrayList<>())
                                .build();
                        tourMap.put(tourId, tour);
                    }

                    // ----- Handle Tour Types -----
                    Long tourTypeId = rs.getLong("tour_type_id");
                    if (tourTypeId != 0 && tour.getTourTypeDtos().stream().noneMatch(tt -> tt.getTourTypeId().equals(tourTypeId))) {
                        tour.getTourTypeDtos().add(
                                TourTypeDto.builder()
                                        .tourTypeId(tourTypeId)
                                        .tourTypeName(rs.getString("tour_type_name"))
                                        .tourTypeDescription(rs.getString("tour_type_description"))
                                        .build()
                        );
                    }

                    // ----- Handle Tour Categories -----
                    Long tourCategoryId = rs.getLong("tour_category_id");
                    if (tourCategoryId != 0 && tour.getTourCategoryDto().stream().noneMatch(tc -> tc.getTourCategoryId().equals(tourCategoryId))) {
                        tour.getTourCategoryDto().add(
                                TourCategoryDto.builder()
                                        .tourCategoryId(tourCategoryId)
                                        .tourCategoryName(rs.getString("tour_category_name"))
                                        .tourCategoryDescription(rs.getString("tour_category_description"))
                                        .build()
                        );
                    }

                    // ----- Handle Schedules -----
                    int scheduleId = rs.getInt("schedule_id");
                    if (scheduleId != 0 && rs.getString("schedule_name") != null) {
                        TourScheduleResponseDto schedule = new TourScheduleResponseDto();
                        schedule.setScheduleId(scheduleId);
                        schedule.setScheduleName(rs.getString("schedule_name"));
                        schedule.setAssumeStartDate(rs.getObject("assume_start_date", LocalDate.class));
                        schedule.setAssumeEndDate(rs.getObject("assume_end_date", LocalDate.class));
                        schedule.setDurationStart(rs.getObject("duration_start", Integer.class));
                        schedule.setDurationEnd(rs.getObject("duration_end", Integer.class));
                        schedule.setSpecialNote(rs.getString("special_note"));
                        schedule.setScheduleDescription(rs.getString("schedule_description"));

                        if (tour.getSchedules().stream().noneMatch(s -> s.getScheduleId() == scheduleId)) {
                            tour.getSchedules().add(schedule);
                        }
                    }

                    // ----- Handle Images -----
                    int imageId = rs.getInt("image_id");
                    if (imageId != 0 && rs.getString("image_url") != null) {
                        TourImageResponseDto image = new TourImageResponseDto();
                        image.setImageId(imageId);
                        image.setImageName(rs.getString("image_name"));
                        image.setImageDescription(rs.getString("image_description"));
                        image.setImageUrl(rs.getString("image_url"));

                        if (tour.getImages().stream().noneMatch(i -> i.getImageId() == imageId)) {
                            tour.getImages().add(image);
                        }
                    }
                }

                return new ArrayList<>(tourMap.values());
            });
            return new ToursDetailsWithParamResponse(totalTours, query);

        } catch (DataAccessException ex) {
            LOGGER.error(ex.toString());
            throw new DataAccessErrorExceptionHandler("Database error while fetching tours");
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching tours");
        }
    }

    @Override
    public List<TourDayDestinationActivityIdsDto> getTourDayDestinationActivityIds(Long tourId) {

        try {
            return jdbcTemplate.query(
                    TourQueries.GET_ALL_TOUR_DAY_DESTINATION_ACTIVITY_IDS,
                    new Object[]{tourId},
                    (rs, rowNum) -> {

                        List<Integer> activityIds = Arrays.stream(
                                        rs.getString("activity_ids").split(","))
                                .map(Integer::valueOf)
                                .toList();

                        return TourDayDestinationActivityIdsDto.builder()
                                .day(rs.getInt("day"))
                                .destinationId(rs.getInt("destination_id"))
                                .activityIds(activityIds)
                                .build();
                    }
            );

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler(
                    "Database error while fetching tour day-to-day details for tourId: " + tourId);
        } catch (Exception ex) {
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error while fetching tour day-to-day details for tourId: " + tourId);
        }
    }

    @Override
    public List<TourDetailsWithDayToDayResponse.DestinationDetailsPerDay> getDestinationsDetailsByIds(List<Long> destinationIdList) {
        try {
            LOGGER.info("Executing query to fetch all destinations...");
            if (destinationIdList == null || destinationIdList.isEmpty()) {
                return List.of();
            }

            String placeholders = destinationIdList.stream()
                    .map(id -> "?")
                    .collect(Collectors.joining(","));

            String sql = TourQueries.GET_DESTINATIONS_DETAILS_WITH_FOR_DAY_IDS
                    .replace(":destinationIds", placeholders);

            return jdbcTemplate.query(
                    sql,
                    destinationIdList.toArray(),
                    rs -> {
                        Map<Long, TourDetailsWithDayToDayResponse.DestinationDetailsPerDay> destinationMap = new HashMap<>();

                        while (rs.next()) {
                            Long destinationId = rs.getLong("destination_id");

                            // Check if destination already exists
                            TourDetailsWithDayToDayResponse.DestinationDetailsPerDay destination = destinationMap.get(destinationId);
                            if (destination == null) {
                                destination = TourDetailsWithDayToDayResponse.DestinationDetailsPerDay.builder()
                                        .destinationId(destinationId)
                                        .destinationName(rs.getString("destination_name"))
                                        .destinationDescription(rs.getString("destination_description"))
                                        .destinationStatus(rs.getString("status_name"))
                                        .location(rs.getString("location"))
                                        .latitude(rs.getObject("latitude", Double.class))
                                        .longitude(rs.getObject("longitude", Double.class))
                                        .createdBy(rs.getString("created_by"))
                                        .createrImageUrl(rs.getString("creater_image"))
                                        .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                                        .updatedBy(rs.getString("updated_by"))
                                        .updaterImageUrl(rs.getString("updater_image"))
                                        .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                                        .destinationCategoryDetailsDtos(new ArrayList<>())
                                        .images(new ArrayList<>())
                                        .build();

                                destinationMap.put(destinationId, destination);
                            }

                            // Add category if exists
                            Long categoryId = rs.getObject("category_id", Long.class);
                            if (categoryId != null) {
                                boolean exists = destination.getDestinationCategoryDetailsDtos().stream()
                                        .anyMatch(c -> c.getId().equals(categoryId));
                                if (!exists) {
                                    DestinationCategoryDetailsDto categoryDto = DestinationCategoryDetailsDto.builder()
                                            .id(categoryId)
                                            .name(rs.getString("category_name"))
                                            .description(rs.getString("category_description"))
                                            .isPrimary(rs.getBoolean("category_is_primary"))
                                            .build();
                                    destination.getDestinationCategoryDetailsDtos().add(categoryDto);
                                }
                            }

                            // Add image if exists
                            int imageId = rs.getInt("image_id");
                            if (imageId != 0 && rs.getString("image_url") != null) {
                                TourDetailsWithDayToDayResponse.DestinationImagePerDay image = new TourDetailsWithDayToDayResponse.DestinationImagePerDay();
                                image.setImageId(imageId);
                                image.setImageName(rs.getString("image_name"));
                                image.setImageDescription(rs.getString("image_description"));
                                image.setImageUrl(rs.getString("image_url"));

                                if (destination.getImages().stream().noneMatch(i -> i.getImageId() == imageId)) {
                                    destination.getImages().add(image);
                                }
                            }
                        }

                        return new ArrayList<>(destinationMap.values());
                    });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destinations");
        }
    }

    @Override
    public List<TourDetailsWithDayToDayResponse.ActivityPerDayResponse> getActivityDetailsByIds(
            List<Long> activityIdList
    ) {
        if (activityIdList == null || activityIdList.isEmpty()) {
            return List.of();
        }

        // ---- Build dynamic placeholders for IN clause ----
        String placeholders = activityIdList.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql = TourQueries.GET_ACTIVITIES_DETAILS_BASE
                + " AND a.id IN (" + placeholders + ")"
                + " LIMIT 1000";

        ObjectMapper objectMapper = new ObjectMapper();

        return jdbcTemplate.query(sql, activityIdList.toArray(), (ResultSet rs) -> {
            Map<Long, TourDetailsWithDayToDayResponse.ActivityPerDayResponse> activityMap = new HashMap<>();

            while (rs.next()) {
                Long activityId = rs.getLong("id");
                TourDetailsWithDayToDayResponse.ActivityPerDayResponse activity = activityMap.get(activityId);

                if (activity == null) {
                    // Parse JSON arrays
                    List<TourDetailsWithDayToDayResponse.ActivityRequirementPerDay> requirements;
                    try {
                        requirements = objectMapper.readValue(
                                rs.getString("requirements"),
                                new TypeReference<>() {
                                }
                        );
                    } catch (JsonProcessingException e) {
                        requirements = List.of();
                    }

                    List<TourDetailsWithDayToDayResponse.ActivityImagePerDay> images;
                    try {
                        images = objectMapper.readValue(
                                rs.getString("images"),
                                new TypeReference<>() {
                                }
                        );
                    } catch (JsonProcessingException e) {
                        images = List.of();
                    }

                    activity = TourDetailsWithDayToDayResponse.ActivityPerDayResponse.builder()
                            .id(activityId)
                            .destinationId(rs.getLong("destination_id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .activityCategoryDtos(new ArrayList<>()) // start empty, will add below
                            .durationHours(rs.getBigDecimal("duration_hours"))
                            .availableFrom(rs.getTime("available_from"))
                            .availableTo(rs.getTime("available_to"))
                            .priceLocal(rs.getBigDecimal("price_local"))
                            .priceForeigners(rs.getBigDecimal("price_foreigners"))
                            .minParticipate(rs.getInt("min_participate"))
                            .maxParticipate(rs.getInt("max_participate"))
                            .season(rs.getString("season"))
                            .seasonId(rs.getLong("season_id"))
                            .status(rs.getString("status_name"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .requirements(requirements)
                            .images(images)
                            .build();

                    activityMap.put(activityId, activity);
                }

                // Add category if exists
                Long categoryId = rs.getObject("category_id", Long.class);
                if (categoryId != null && activity.getActivityCategoryDtos().stream().noneMatch(c -> c.getId().equals(categoryId))) {
                    activity.getActivityCategoryDtos().add(ActivityCategoryDto.builder()
                            .id(categoryId)
                            .name(rs.getString("category_name"))
                            .description(rs.getString("category_description"))
                            .isPrimary(rs.getBoolean("category_is_primary"))
                            .build());
                }
            }

            return new ArrayList<>(activityMap.values());
        });
    }


    @Override
    public List<TourExtrasResponse.TourInclusion> getTourInclusions(Long tourId) {
        String sql = TourQueries.GET_TOUR_INCLUSIONS_BY_TOUR_ID;

        return jdbcTemplate.query(
                sql,
                new Object[]{tourId}, // Bind tourId to the ?
                (rs, rowNum) -> TourExtrasResponse.TourInclusion.builder()
                        .id(rs.getLong("tour_inclusion_id"))
                        .description(rs.getString("description"))
                        .displayOrder(rs.getInt("display_order"))
                        .status(rs.getString("status"))
                        .build()
        );
    }

    @Override
    public List<TourExtrasResponse.TourExclusion> getTourExclusions(Long tourId) {
        String sql = TourQueries.GET_TOUR_EXCLUSIONS_BY_TOUR_ID;

        return jdbcTemplate.query(
                sql,
                new Object[]{tourId},
                (rs, rowNum) -> TourExtrasResponse.TourExclusion.builder()
                        .id(rs.getLong("tour_exclusion_id"))
                        .description(rs.getString("description"))
                        .displayOrder(rs.getInt("display_order"))
                        .status(rs.getString("status"))
                        .build()
        );
    }

    @Override
    public List<TourExtrasResponse.TourCondition> getTourConditions(Long tourId) {
        String sql = TourQueries.GET_TOUR_CONDITIONS_BY_TOUR_ID;

        return jdbcTemplate.query(
                sql,
                new Object[]{tourId},
                (rs, rowNum) -> TourExtrasResponse.TourCondition.builder()
                        .id(rs.getLong("tour_condition_id"))
                        .description(rs.getString("description"))
                        .displayOrder(rs.getInt("display_order"))
                        .status(rs.getString("status"))
                        .build()
        );
    }

    @Override
    public List<TourExtrasResponse.TourTravelTip> getTourTravelTips(Long tourId) {
        String sql = TourQueries.GET_TOUR_TRAVEL_TIPS_BY_TOUR_ID;

        return jdbcTemplate.query(
                sql,
                new Object[]{tourId},
                (rs, rowNum) -> TourExtrasResponse.TourTravelTip.builder()
                        .id(rs.getLong("tour_travel_tip_id"))
                        .title(rs.getString("tip_title"))
                        .description(rs.getString("tip_description"))
                        .displayOrder(rs.getInt("display_order"))
                        .status(rs.getString("status"))
                        .build()
        );
    }

    @Override
    public List<TourSchedulesResponse.TourScheduleDetails> getTourSchedulesById(Long tourId) {

        String sql = TourQueries.GET_TOUR_SCHEDULES_BY_TOUR_ID;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        TourSchedulesResponse.TourScheduleDetails.builder()
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
                                .durationStart(rs.getInt("duration_start"))
                                .durationEnd(rs.getInt("duration_end"))
                                .specialNote(rs.getString("special_note"))
                                .description(rs.getString("description"))
                                .statusId(rs.getLong("status_id"))
                                .statusName(rs.getString("status_name"))
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
                                .build(),
                tourId
        );
    }


    @Override
    public TourSchedulesResponse.TourBasicDetails getTourBasicDetails(Long tourId) {

        String sql = TourQueries.GET_TOUR_BASIC_DETAILS_BY_TOUR_ID;

        try {
            List<TourSchedulesResponse.TourBasicDetails> result =
                    jdbcTemplate.query(sql, rs -> {

                        TourSchedulesResponse.TourBasicDetails.TourBasicDetailsBuilder tourBuilder = null;
                        List<TourSchedulesResponse.TourImageDetails> images = new ArrayList<>();

                        while (rs.next()) {

                            // Build tour only once
                            if (tourBuilder == null) {
                                tourBuilder = TourSchedulesResponse.TourBasicDetails.builder()
                                        .tourId(rs.getLong("tour_id"))
                                        .tourName(rs.getString("tour_name"))
                                        .tourDescription(rs.getString("tour_description"))
                                        .duration(rs.getInt("duration"))
                                        .latitude(rs.getDouble("latitude"))
                                        .longitude(rs.getDouble("longitude"))
                                        .startLocation(rs.getString("start_location"))
                                        .endLocation(rs.getString("end_location"))
                                        .status(rs.getString("tour_status"));
                            }

                            // Add image if exists
                            Long imageId = rs.getLong("image_id");
                            if (!rs.wasNull()) {
                                images.add(
                                        TourSchedulesResponse.TourImageDetails.builder()
                                                .imageId(imageId)
                                                .imageName(rs.getString("image_name"))
                                                .imageDescription(rs.getString("image_description"))
                                                .imageUrl(rs.getString("image_url"))
                                                .imageStatus(rs.getString("image_status"))
                                                .build()
                                );
                            }
                        }

                        if (tourBuilder == null) {
                            return List.of();
                        }

                        return List.of(
                                tourBuilder.images(images).build()
                        );
                    }, tourId);

            return result.isEmpty() ? null : result.get(0);

        } catch (Exception ex) {
            LOGGER.error("Error fetching tour basic details for tourId={}", tourId, ex);
            return null;
        }
    }

    @Override
    public List<TourBasicDetailsResponse> getAllToursBasicDetails() {

        String sql = TourQueries.GET_ALL_TOURS_BASIC_DETAILS;

        try {
            return jdbcTemplate.query(sql, rs -> {

                Map<Long, TourBasicDetailsResponse> tourMap = new LinkedHashMap<>();

                while (rs.next()) {
                    Long tourId = rs.getLong("tour_id");

                    // Create tour object only once per tour_id
                    TourBasicDetailsResponse response = tourMap.computeIfAbsent(tourId, id -> {
                        TourBasicDetailsResponse.TourBasicDetails tourDetails = null;
                        try {
                            tourDetails = TourBasicDetailsResponse.TourBasicDetails.builder()
                                    .tourId(tourId)
                                    .tourName(rs.getString("name"))
                                    .tourDescription(rs.getString("description"))
                                    .tourTypes(new ArrayList<>())
                                    .tourCategories(new ArrayList<>())
                                    .duration(rs.getObject("duration") != null ? rs.getInt("duration") : null)
                                    .latitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null)
                                    .longitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null)
                                    .startLocation(rs.getString("start_location"))
                                    .endLocation(rs.getString("end_location"))
                                    .season(rs.getString("season"))
                                    .status(rs.getString("status"))
                                    .build();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        return TourBasicDetailsResponse.builder()
                                .tourDetails(tourDetails)
                                .images(new ArrayList<>())
                                .build();
                    });

                    // Add tour type if exists and not already added
                    String typeName = rs.getString("type_name");
                    if (typeName != null && !response.getTourDetails().getTourTypes().contains(typeName)) {
                        response.getTourDetails().getTourTypes().add(typeName);
                    }

                    // Add tour category if exists and not already added
                    String categoryName = rs.getString("category_name");
                    if (categoryName != null && !response.getTourDetails().getTourCategories().contains(categoryName)) {
                        response.getTourDetails().getTourCategories().add(categoryName);
                    }

                    // Add image if exists and not duplicate
                    Long imageId = rs.getLong("image_id");
                    if (!rs.wasNull()) {
                        boolean exists = response.getImages().stream()
                                .anyMatch(img -> img.getImageId().equals(imageId));
                        if (!exists) {
                            TourBasicDetailsResponse.TourImageDetails image = TourBasicDetailsResponse.TourImageDetails.builder()
                                    .imageId(imageId)
                                    .imageName(rs.getString("image_name"))
                                    .imageDescription(rs.getString("image_description"))
                                    .imageUrl(rs.getString("image_url"))
                                    .build();
                            response.getImages().add(image);
                        }
                    }
                }

                return new ArrayList<>(tourMap.values());
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching all tours", ex);
            throw new InternalServerErrorExceptionHandler("Database error while fetching tours");
        }
    }

    @Override
    public List<TourForTerminateResponse> getToursForTerminate() {
        String GET_ACTIVE_TOURS_FOR_TERMINATE = TourQueries.GET_ACTIVE_TOURS_FOR_TERMINATE;

        try {
            return jdbcTemplate.query(
                    GET_ACTIVE_TOURS_FOR_TERMINATE,
                    new Object[]{CommonStatus.ACTIVE.toString()}, // parameter for cs.name = ?
                    (rs, rowNum) -> TourForTerminateResponse.builder()
                            .tourId(rs.getLong("tour_id"))
                            .tourName(rs.getString("name"))
                            .build()
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch tours for terminate: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tours");
        }
    }

    @Override
    public void terminateTour(TourTerminateRequest tourTerminateRequest, Long userId) {
        String TOUR_TERMINATE = TourQueries.TOUR_TERMINATE;
        try {
            jdbcTemplate.update(TOUR_TERMINATE, new Object[]{CommonStatus.TERMINATED.toString(), userId, tourTerminateRequest.getTourId()});
        } catch (DataAccessException tfe) {
            LOGGER.error(tfe.toString());
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to terminate tour : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate tour");
        }
    }

    @Override
    public Long insertTourDetails(TourInsertRequest request, Long userId) {

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        TourQueries.INSERT_TOUR_BASIC_DETAILS,
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, request.getName());
                ps.setString(2, request.getDescription());
                ps.setLong(3, request.getTourType());
                ps.setLong(4, request.getTourCategory());
                ps.setInt(5, request.getDuration());
                ps.setBigDecimal(6, request.getLatitude());
                ps.setBigDecimal(7, request.getLongitude());
                ps.setString(8, request.getStartLocation());
                ps.setString(9, request.getEndLocation());
                ps.setLong(10, request.getSeason());
                ps.setString(11, request.getStatus()); // for subquery
                ps.setLong(12, userId);
                ps.setLong(13, request.getAssignTo());
                ps.setString(14, request.getAssignMessage());

                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                throw new InsertFailedErrorExceptionHandler("Failed to generate tour ID");
            }

            Long tourId = keyHolder.getKey().longValue();


            return tourId;

        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting tour", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to insert tour", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert tour");
        }
    }

    @Override
    public void insertTourDestinations(Long tourId,
                                       List<TourDestinationInsertRequest> destinations,
                                       Long userId) {

        if (destinations == null || destinations.isEmpty()) {
            return;
        }

        try {
            for (TourDestinationInsertRequest destination : destinations) {
                jdbcTemplate.update(
                        TourQueries.INSERT_TOUR_DESTINATION,
                        tourId,
                        destination.getDestinationId(),
                        destination.getActivityId(),
                        destination.getDayNumber()
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting tour destinations", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        }
    }


    @Override
    public void insertTourImages(Long tourId, List<TourImageInsertRequest> images, Long userId) {

        if (images == null || images.isEmpty()) {
            return;
        }

        try {
            for (TourImageInsertRequest image : images) {
                jdbcTemplate.update(
                        TourQueries.INSERT_TOUR_IMAGE,
                        tourId,
                        image.getImageName(),
                        image.getImageDescription(),
                        image.getImageUrl(),
                        image.getStatus(),
                        userId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting tour images", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        }
    }


    @Override
    public void insertTourInclusions(Long tourId,
                                     List<TourInclusionInsertRequest> inclusions,
                                     Long userId) {

        if (inclusions == null || inclusions.isEmpty()) {
            return;
        }

        try {
            for (TourInclusionInsertRequest inclusion : inclusions) {
                jdbcTemplate.update(
                        TourQueries.INSERT_TOUR_INCLUSION,
                        tourId,
                        inclusion.getInclusionText(),
                        inclusion.getDisplayOrder(),
                        inclusion.getStatus(),
                        userId);
            }
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting tour inclusions", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        }
    }


    @Override
    public void insertTourExclusions(Long tourId,
                                     List<TourExclusionInsertRequest> exclusions,
                                     Long userId) {

        if (exclusions == null || exclusions.isEmpty()) {
            return;
        }

        try {
            for (TourExclusionInsertRequest exclusion : exclusions) {
                jdbcTemplate.update(
                        TourQueries.INSERT_TOUR_EXCLUSION,
                        tourId,
                        exclusion.getExclusionText(),
                        exclusion.getDisplayOrder(),
                        exclusion.getStatus(),
                        userId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting tour exclusions", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        }
    }


    @Override
    public void insertTourConditions(Long tourId,
                                     List<TourConditionInsertRequest> conditions,
                                     Long userId) {

        if (conditions == null || conditions.isEmpty()) {
            return;
        }

        try {
            for (TourConditionInsertRequest condition : conditions) {
                jdbcTemplate.update(
                        TourQueries.INSERT_TOUR_CONDITION,
                        tourId,
                        condition.getConditionText(),
                        condition.getDisplayOrder(),
                        condition.getStatus(),
                        userId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting tour conditions", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        }
    }


    @Override
    public void insertTourTravelTips(Long tourId,
                                     List<TourTravelTipInsertRequest> travelTips,
                                     Long userId) {

        if (travelTips == null || travelTips.isEmpty()) {
            return;
        }

        try {
            for (TourTravelTipInsertRequest tip : travelTips) {
                jdbcTemplate.update(
                        TourQueries.INSERT_TOUR_TRAVEL_TIP,
                        tourId,
                        tip.getTipTitle(),
                        tip.getTipDescription(),
                        tip.getDisplayOrder(),
                        tip.getStatus(),
                        userId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while inserting tour travel tips", dae);
            throw new InsertFailedErrorExceptionHandler(dae.getMessage());
        }
    }

    @Override
    public TourDetailsForAddPackageResponse getTourDetailsForAddPackage(Long tourId) {

        String sql = TourQueries.TOUR_DETAILS_FOR_ADD_PACKAGE;

        try {
            return jdbcTemplate.query(sql, rs -> {

                TourDetailsForAddPackageResponse response = null;

                Map<Integer, TourDetailsForAddPackageResponse.TourDay> dayMap = new LinkedHashMap<>();

                while (rs.next()) {

                    // ========== Create Tour only once ==========
                    if (response == null) {
                        response = new TourDetailsForAddPackageResponse();
                        response.setTourId(rs.getLong(1));
                        response.setName(rs.getString(2));
                        response.setDescription(rs.getString(3));
                        response.setTourType(rs.getString(4));
                        response.setTourCategory(rs.getString(5));
                        response.setStartLocation(rs.getString(6));
                        response.setEndLocation(rs.getString(7));
                        response.setStatus(rs.getString(8));
                        response.setSeason(rs.getString(9));
                        response.setAssignMessage(rs.getString(14));

                        TourDetailsForAddPackageResponse.AssignedUser user =
                                new TourDetailsForAddPackageResponse.AssignedUser();
                        user.setUserId(rs.getLong(10));
                        user.setFirstName(rs.getString(11));
                        user.setLastName(rs.getString(12));
                        user.setUsername(rs.getString(13));
                        response.setAssignedUser(user);
                    }

                    // ========== Day ==========
                    int day = rs.getInt(15);
                    TourDetailsForAddPackageResponse.TourDay tourDay =
                            dayMap.computeIfAbsent(day, d -> {
                                TourDetailsForAddPackageResponse.TourDay td =
                                        new TourDetailsForAddPackageResponse.TourDay();
                                td.setDay(d);
                                td.setDestinations(new ArrayList<>());
                                return td;
                            });

                    // ========== Destination ==========
                    Long destinationId = rs.getLong(16);

                    TourDetailsForAddPackageResponse.Destination destination =
                            tourDay.getDestinations()
                                    .stream()
                                    .filter(d -> d.getDestinationId().equals(destinationId))
                                    .findFirst()
                                    .orElseGet(() -> {
                                        TourDetailsForAddPackageResponse.Destination d =
                                                new TourDetailsForAddPackageResponse.Destination();
                                        d.setDestinationId(destinationId);
                                        try {
                                            d.setName(rs.getString(17));
                                            d.setDescription(rs.getString(18));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        d.setActivities(new ArrayList<>());
                                        tourDay.getDestinations().add(d);
                                        return d;
                                    });

                    // ========== Activity ==========
                    Long activityId = rs.getLong(19);
                    if (activityId != null && activityId > 0) {
                        boolean exists = destination.getActivities()
                                .stream()
                                .anyMatch(a -> a.getActivityId().equals(activityId));

                        if (!exists) {
                            TourDetailsForAddPackageResponse.Activity activity =
                                    new TourDetailsForAddPackageResponse.Activity();
                            activity.setActivityId(activityId);
                            activity.setName(rs.getString(20));
                            activity.setDescription(rs.getString(21));
                            destination.getActivities().add(activity);
                        }
                    }
                }

                if (response != null) {
                    response.setDays(new ArrayList<>(dayMap.values()));
                }

                return response;
            }, tourId);

        } catch (DataAccessException tfe) {
            LOGGER.error(tfe.toString());
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed", e);
            throw new InternalServerErrorExceptionHandler("Failed to load tour");
        }
    }

    @Override
    public List<String> getTourInclusionsNamesOnly(Long tourId) {
        try {
            return jdbcTemplate.query(
                    TourQueries.GET_TOUR_INCLUSIONS_NAMES,
                    (rs, rowNum) -> rs.getString("inclusion_text"),
                    tourId
            );
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while fetching tour inclusions", dae);
            return List.of();
        }
    }

    @Override
    public List<String> getTourExclusionsNamesOnly(Long tourId) {
        try {
            return jdbcTemplate.query(
                    TourQueries.GET_TOUR_EXCLUSIONS_NAMES,
                    (rs, rowNum) -> rs.getString("exclusion_text"),
                    tourId
            );
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while fetching tour exclusions", dae);
            return List.of();
        }
    }

    @Override
    public List<String> getTourConditionsNamesOnly(Long tourId) {
        try {
            return jdbcTemplate.query(
                    TourQueries.GET_TOUR_CONDITIONS_NAMES,
                    (rs, rowNum) -> rs.getString("condition_text"),
                    tourId
            );
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while fetching tour conditions", dae);
            return List.of();
        }
    }

    @Override
    public List<TourDetailsForAddPackageResponse.TravelTip> getTourTravelTipsNamesOnly(Long tourId) {
        try {
            return jdbcTemplate.query(
                    TourQueries.GET_TOUR_TRAVEL_TIPS,
                    (rs, rowNum) -> {
                        TourDetailsForAddPackageResponse.TravelTip tip = new TourDetailsForAddPackageResponse.TravelTip();
                        tip.setTipTitle(rs.getString("tip_title"));
                        tip.setTipDescription(rs.getString("tip_description"));
                        return tip;
                    },
                    tourId
            );
        } catch (DataAccessException dae) {
            LOGGER.error("DB error while fetching tour travel tips", dae);
            return List.of();
        }
    }

    @Override
    public void updateTourBasicDetails(Long tourId,
                                       TourUpdateRequest.TourBasicDetails tourBasicDetails,
                                       Long userId) {

        String UPDATE_TOUR_BASIC_DETAILS = TourQueries.UPDATE_TOUR_BASIC_DETAILS;

        try {
            jdbcTemplate.update(
                    UPDATE_TOUR_BASIC_DETAILS,
                    tourBasicDetails.getTourName(),          // 1 name
                    tourBasicDetails.getTourDescription(),   // 2 description
                    tourBasicDetails.getTourType(),          // 3 tour_type
                    tourBasicDetails.getTourCategory(),      // 4 tour_category
                    tourBasicDetails.getDuration(),          // 5 duration
                    tourBasicDetails.getLatitude(),          // 6 latitude
                    tourBasicDetails.getLongitude(),         // 7 longitude
                    tourBasicDetails.getStartLocation(),     // 8 start_location
                    tourBasicDetails.getEndLocation(),       // 9 end_location
                    tourBasicDetails.getSeason(),            // 10 season
                    tourBasicDetails.getStatus(),            // 11 status (name -> subquery)
                    userId,                                  // 12 updated_by
                    tourBasicDetails.getAssignTo(),
                    tourBasicDetails.getAssignMessage(),     // 13 assign_message
                    tourId                                   // 13 where tour_id
            );

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating tour", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to update tour", e);
            throw new InternalServerErrorExceptionHandler("Failed to update tour");
        }
    }


    @Override
    public void removeTourDestinations(Long tourId, List<Long> removeDestinations, Long userId) {
        try {
            jdbcTemplate.batchUpdate(
                    TourQueries.TOUR_DESTINATION_REMOVE,
                    removeDestinations,
                    removeDestinations.size(),
                    (ps, tourDestinationId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
                        ps.setLong(2, userId);
                        ps.setLong(3, tourDestinationId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove tour destination", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove tour destination : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove tour destination");
        }
    }

    @Override
    public void updateTourDestinations(Long tourId, List<TourDestinationUpdateRequest> updateDestinations, Long userId) {
        if (updateDestinations == null || updateDestinations.isEmpty()) {
            return; // nothing to update
        }

        try {
            for (TourDestinationUpdateRequest updateDestination : updateDestinations) {

                jdbcTemplate.update(
                        TourQueries.UPDATE_TOUR_DESTINATION,
                        updateDestination.getDestinationId(),
                        updateDestination.getActivityId(),
                        updateDestination.getDayNumber(),
                        updateDestination.getStatus(),
                        updateDestination.getTourDestinationId(),
                        tourId
                );
            }

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating tour destination", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to update tour destination", e);
            throw new InternalServerErrorExceptionHandler("Failed to update tour destination");
        }
    }


    @Override
    public void removeTourImages(Long tourId, List<Long> removeImages, Long userId) {
        try {
            jdbcTemplate.batchUpdate(
                    TourQueries.TOUR_IMAGES_REMOVE,
                    removeImages,
                    removeImages.size(),
                    (ps, imageId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
                        ps.setLong(2, userId);
                        ps.setLong(3, imageId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove tour images", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove tour images : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove tour images");
        }
    }

    @Override
    public void updateTourImages(Long tourId, List<TourImageUpdateRequest> updateImages, Long userId) {
        if (updateImages == null || updateImages.isEmpty()) {
            return; // nothing to update
        }

        try {
            for (TourImageUpdateRequest image : updateImages) {

                jdbcTemplate.update(
                        TourQueries.UPDATE_TOUR_IMAGE,
                        image.getImageName(),            // 1
                        image.getImageDescription(),     // 2
                        image.getImageUrl(),        // 3
                        image.getStatus(),          // 4 (status name)
                        userId,                     // 5 (updated_by)
                        image.getImageId(),         // 6 (WHERE id)
                        tourId                  // 7 (safety check)
                );
            }

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating tour images", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to update tour images", e);
            throw new InternalServerErrorExceptionHandler("Failed to update tour images");
        }
    }

    @Override
    public void removeTourInclusions(Long tourId, List<Long> removeInclusions, Long userId) {
        try {
            jdbcTemplate.batchUpdate(
                    TourQueries.TOUR_INCLUSION_REMOVE,
                    removeInclusions,
                    removeInclusions.size(),
                    (ps, inclusionId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
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
    public void updateTourInclusions(Long tourId, List<TourInclusionUpdateRequest> updateInclusions, Long userId) {
        if (updateInclusions == null || updateInclusions.isEmpty()) {
            return; // nothing to update
        }

        try {
            for (TourInclusionUpdateRequest updateInclusion : updateInclusions) {

                jdbcTemplate.update(
                        TourQueries.UPDATE_INCLUSION,
                        updateInclusion.getInclusionText(),
                        updateInclusion.getDisplayOrder(),
                        updateInclusion.getStatus(),
                        userId,
                        updateInclusion.getInclusionId(),
                        tourId
                );
            }

        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating tour inclusion", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to update tour inclusion", e);
            throw new InternalServerErrorExceptionHandler("Failed to update tour inclusion");
        }
    }

    @Override
    public void removeTourExclusions(Long tourId, List<Long> removeExclusions, Long userId) {
        try {
            jdbcTemplate.batchUpdate(
                    TourQueries.TOUR_EXCLUSION_REMOVE,
                    removeExclusions,
                    removeExclusions.size(),
                    (ps, exclusionId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
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
    public void removeTourConditions(Long tourId, List<Long> removeConditions, Long userId) {
        try {
            jdbcTemplate.batchUpdate(
                    TourQueries.TOUR_CONDITION_REMOVE,
                    removeConditions,
                    removeConditions.size(),
                    (ps, conditionId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
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
    public void removeTourTravelTips(Long tourId, List<Long> removeTravelTips, Long userId) {
        try {
            jdbcTemplate.batchUpdate(
                    TourQueries.TOUR_TRAVEL_TIPS_REMOVE,
                    removeTravelTips,
                    removeTravelTips.size(),
                    (ps, travelTipId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
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
    public void updateTourExclusions(Long tourId, List<TourExclusionUpdateRequest> updateExclusions, Long userId) {
        if (updateExclusions == null || updateExclusions.isEmpty()) return;

        try {
            for (TourExclusionUpdateRequest exclusion : updateExclusions) {
                jdbcTemplate.update(
                        TourQueries.UPDATE_EXCLUSION,
                        exclusion.getExclusionText(),
                        exclusion.getDisplayOrder(),
                        exclusion.getStatus(),
                        userId,
                        exclusion.getExclusionId(),
                        tourId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating tour exclusions", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update tour exclusions", e);
            throw new InternalServerErrorExceptionHandler("Failed to update tour exclusions");
        }
    }

    @Override
    public void updateTourConditions(Long tourId, List<TourConditionUpdateRequest> updateConditions, Long userId) {
        if (updateConditions == null || updateConditions.isEmpty()) return;

        try {
            for (TourConditionUpdateRequest condition : updateConditions) {
                jdbcTemplate.update(
                        TourQueries.UPDATE_CONDITION,
                        condition.getConditionText(),
                        condition.getDisplayOrder(),
                        condition.getStatus(),
                        userId,
                        condition.getConditionId(),
                        tourId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating tour conditions", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update tour conditions", e);
            throw new InternalServerErrorExceptionHandler("Failed to update tour conditions");
        }
    }

    @Override
    public void updateTourTravelTips(Long tourId, List<TourTravelTipUpdateRequest> updateTravelTips, Long userId) {
        if (updateTravelTips == null || updateTravelTips.isEmpty()) return;

        try {
            for (TourTravelTipUpdateRequest tip : updateTravelTips) {
                jdbcTemplate.update(
                        TourQueries.UPDATE_TRAVEL_TIP,
                        tip.getTipTitle(),
                        tip.getTipDescription(),
                        tip.getDisplayOrder(),
                        tip.getStatus(),
                        userId,
                        tip.getTravelTipId(),
                        tourId
                );
            }
        } catch (DataAccessException dae) {
            LOGGER.error("Database error while updating travel tips", dae);
            throw new UpdateFailedErrorExceptionHandler(dae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to update travel tips", e);
            throw new InternalServerErrorExceptionHandler("Failed to update travel tips");
        }
    }

    @Override
    public TourAssignUserDto getTourAssignUserDetailsByTourId(Long tourId) {
        try {
            return jdbcTemplate.queryForObject(
                    TourQueries.GET_TOUR_ASSIGN_USER_DETAILS_BY_TOUR_ID,
                    new Object[]{tourId},
                    (rs, rowNum) -> TourAssignUserDto.builder()
                            .assignTo(rs.getLong("user_id"))
                            .assignToName(rs.getString("username"))
                            .assignMessage(rs.getString("assign_message"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            LOGGER.error("Database error while fetching tour assign user details", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour assign user details");
        }
    }

    private LocalDateTime getLocalDateTime(ResultSet rs, String columnLabel) {
        try {
            Timestamp timestamp = rs.getTimestamp(columnLabel);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        } catch (SQLException e) {
            return null;
        }
    }

    private String getSafeString(ResultSet rs, String column) {
        try {
            return rs.getString(column);
        } catch (Exception e) {
            return null;
        }
    }

    private Double getSafeDouble(ResultSet rs, String column) {
        try {
            double value = rs.getDouble(column);
            return rs.wasNull() ? null : value;
        } catch (Exception e) {
            return null;
        }
    }

}
