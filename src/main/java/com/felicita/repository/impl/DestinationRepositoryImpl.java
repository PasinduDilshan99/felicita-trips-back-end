package com.felicita.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felicita.exception.*;
import com.felicita.model.dto.*;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.DestinationDataRequest;
import com.felicita.model.request.DestinationInsertRequest;
import com.felicita.model.request.DestinationTerminateRequest;
import com.felicita.model.request.DestinationUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.queries.DestinationQueries;
import com.felicita.repository.DestinationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import static com.felicita.queries.DestinationQueries.*;

@Repository
public class DestinationRepositoryImpl implements DestinationRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DestinationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DestinationResponseDto> getAllDestinations() {

        try {

            return jdbcTemplate.query(
                    DestinationQueries.GET_ALL_DESTINATIONS,
                    (rs, rowNum) -> {

                        DestinationResponseDto destination = new DestinationResponseDto();

                        destination.setDestinationId(rs.getLong("destination_id"));
                        destination.setDestinationName(rs.getString("destination_name"));
                        destination.setDestinationDescription(rs.getString("destination_description"));
                        destination.setLocation(rs.getString("location"));
                        destination.setLatitude(rs.getObject("latitude", Double.class));
                        destination.setLongitude(rs.getObject("longitude", Double.class));
                        destination.setRatings(rs.getObject("ratings", Double.class));
                        destination.setStatusName(rs.getString("status_name"));

                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            // Categories
                            String categoryJson = rs.getString("destination_categories");
                            destination.setDestinationCategoryDetailsDtos(
                                    categoryJson != null
                                            ? mapper.readValue(categoryJson,
                                            new TypeReference<List<DestinationCategoryDetailsDto>>() {})
                                            : List.of()
                            );

                            // Activities
                            String activitiesJson = rs.getString("activities");
                            destination.setActivities(
                                    activitiesJson != null
                                            ? mapper.readValue(activitiesJson,
                                            new TypeReference<List<DestinationActivityResponseDto>>() {})
                                            : List.of()
                            );

                            // Images
                            String imagesJson = rs.getString("images");
                            destination.setImages(
                                    imagesJson != null
                                            ? mapper.readValue(imagesJson,
                                            new TypeReference<List<DestionationImageResponseDto>>() {})
                                            : List.of()
                            );

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error parsing destination JSON", e);
                        }

                        return destination;
                    }
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destinations from database");
        }
    }

    @Override
    public List<DestinationCategoryResponseDto> getAllDestinationsCategories() {
        String GET_ALL_DESTINATIONS_CATEGORIES = DestinationQueries.GET_ALL_DESTINATIONS_CATEGORIES;
        try {
            LOGGER.info("Executing query to fetch all destinations categories.");

            return jdbcTemplate.query(GET_ALL_DESTINATIONS_CATEGORIES, rs -> {
                Map<Integer, DestinationCategoryResponseDto> categoryMap = new LinkedHashMap<>();

                while (rs.next()) {
                    int categoryId = rs.getInt("category_id");

                    DestinationCategoryResponseDto category = categoryMap.computeIfAbsent(categoryId, id ->
                            {
                                try {
                                    return new DestinationCategoryResponseDto(
                                            id,
                                            rs.getString("category"),
                                            rs.getString("category_description"),
                                            rs.getString("category_status"),
                                            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                                            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                                            new ArrayList<>()
                                    );
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );

                    int imageId = rs.getInt("image_id");
                    if (imageId > 0) {
                        DestinationsCategoryImageResponseDto image = new DestinationsCategoryImageResponseDto(
                                imageId,
                                rs.getString("image_name"),
                                rs.getString("image_description"),
                                rs.getString("image_url"),
                                rs.getString("image_status"),
                                rs.getTimestamp("image_created_at") != null ? rs.getTimestamp("image_created_at").toLocalDateTime() : null
                        );
                        category.getImages().add(image);
                    }
                }

                return new ArrayList<>(categoryMap.values());
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destinations categories: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destinations categories from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destinations categories: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destinations categories");
        }
    }

    @Override
    public List<PopularDestinationResponseDto> getPopularDestinations() {

        try {
            LOGGER.info("Fetching popular destinations");

            return jdbcTemplate.query(
                    DestinationQueries.GET_POPULAR_DESTINATIONS,
                    (rs, rowNum) -> {

                        PopularDestinationResponseDto dto = new PopularDestinationResponseDto();

                        dto.setPopularId(rs.getInt("popular_id"));
                        dto.setRating(rs.getDouble("rating"));
                        dto.setPopularity(rs.getInt("popularity"));

                        Timestamp createdTs = rs.getTimestamp("popular_created_at");
                        dto.setPopularCreatedAt(
                                createdTs != null ? createdTs.toLocalDateTime() : null
                        );

                        dto.setDestinationId(rs.getInt("destination_id"));
                        dto.setDestinationName(rs.getString("destination_name"));
                        dto.setDestinationDescription(rs.getString("destination_description"));
                        dto.setLocation(rs.getString("location"));
                        dto.setLatitude(rs.getObject("latitude", Double.class));
                        dto.setLongitude(rs.getObject("longitude", Double.class));
                        dto.setDestinationStatus(rs.getString("destination_status"));

                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            // 🔹 Categories
                            String categoryJson = rs.getString("destination_categories");
                            dto.setDestinationCategoryDetailsDtos(
                                    categoryJson != null
                                            ? mapper.readValue(
                                            categoryJson,
                                            new TypeReference<List<DestinationCategoryDetailsDto>>() {})
                                            : List.of()
                            );

                            // 🔹 Images
                            String imagesJson = rs.getString("images");
                            dto.setImages(
                                    imagesJson != null
                                            ? mapper.readValue(
                                            imagesJson,
                                            new TypeReference<List<DestinationImageResponseDto>>() {})
                                            : List.of()
                            );

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error parsing popular destination JSON", e);
                        }

                        return dto;
                    }
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching popular destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch popular destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching popular destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching popular destinations");
        }
    }

    @Override
    public List<TrendingDestinationResponseDto> getTrendingDestinations() {
        try {
            LOGGER.info("Fetching trending destinations...");

            return jdbcTemplate.query(GET_TRENDING_DESTINATIONS, (rs, rowNum) -> {
                TrendingDestinationResponseDto dto = new TrendingDestinationResponseDto();

                dto.setPopularId(rs.getInt("popular_id"));
                dto.setRating(rs.getDouble("rating"));
                dto.setPopularity(rs.getInt("popularity"));

                Timestamp createdTs = rs.getTimestamp("popular_created_at");
                dto.setPopularCreatedAt(createdTs != null ? createdTs.toLocalDateTime() : null);

                dto.setDestinationId(rs.getInt("destination_id"));
                dto.setDestinationName(rs.getString("destination_name"));
                dto.setDestinationDescription(rs.getString("destination_description"));
                dto.setLocation(rs.getString("location"));
                dto.setLatitude(rs.getObject("latitude", Double.class));
                dto.setLongitude(rs.getObject("longitude", Double.class));
                dto.setDestinationStatus(rs.getString("destination_status"));

                ObjectMapper mapper = new ObjectMapper();

                try {
                    // Categories
                    String categoryJson = rs.getString("destination_categories");
                    dto.setDestinationCategoryDetailsDtos(
                            categoryJson != null
                                    ? mapper.readValue(categoryJson, new TypeReference<List<DestinationCategoryDetailsDto>>() {})
                                    : List.of()
                    );

                    // Images
                    String imagesJson = rs.getString("images");
                    dto.setImages(
                            imagesJson != null
                                    ? mapper.readValue(imagesJson, new TypeReference<List<DestinationImageResponseDto>>() {})
                                    : List.of()
                    );

                    // Activities
                    String activitiesJson = rs.getString("activities");
                    dto.setActivities(
                            activitiesJson != null
                                    ? mapper.readValue(activitiesJson, new TypeReference<List<DestinationActivityResponseDto>>() {})
                                    : List.of()
                    );

                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error parsing trending destination JSON", e);
                }

                return dto;
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching trending destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch trending destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching trending destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching trending destinations");
        }
    }

    @Override
    public List<DestinationsForTourMapDto> getDestinationsForTourMap() {
        String sql = DestinationQueries.GET_DESTINATIONS_FOR_TOUR_MAP;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                DestinationsForTourMapDto dto = new DestinationsForTourMapDto();
                dto.setDestinationId(rs.getLong("destination_id"));
                dto.setDestinationName(rs.getString("destination_name"));
                dto.setDestinationDescription(rs.getString("destination_description"));
                dto.setDestinationStatus(rs.getString("destination_status"));

                // Parse destination categories JSON array
                String categoriesJson = rs.getString("destination_categories");
                if (categoriesJson != null) {
                    try {
                        dto.setDestinationCategories(objectMapper.readValue(categoriesJson, new TypeReference<List<String>>() {}));
                    } catch (JsonProcessingException e) {
                        dto.setDestinationCategories(new ArrayList<>());
                    }
                } else {
                    dto.setDestinationCategories(new ArrayList<>());
                }

                dto.setDestinationLocation(rs.getString("destination_location"));
                dto.setDestinationLatitude(rs.getObject("destination_latitude") != null ? rs.getDouble("destination_latitude") : null);
                dto.setDestinationLongitude(rs.getObject("destination_longitude") != null ? rs.getDouble("destination_longitude") : null);
                dto.setDestinationCreatedAt(rs.getTimestamp("destination_created_at") != null
                        ? rs.getTimestamp("destination_created_at").toLocalDateTime()
                        : null);
                dto.setDestinationCreatedBy(rs.getObject("destination_created_by") != null ? rs.getLong("destination_created_by") : null);

                // Parse destination images JSON array
                String destinationImagesJson = rs.getString("destination_images");
                if (destinationImagesJson != null) {
                    try {
                        dto.setDestinationImagesForTourMapDtos(objectMapper.readValue(
                                destinationImagesJson,
                                new TypeReference<List<DestinationImagesForTourMapDto>>() {}
                        ));
                    } catch (JsonProcessingException e) {
                        dto.setDestinationImagesForTourMapDtos(new ArrayList<>());
                    }
                } else {
                    dto.setDestinationImagesForTourMapDtos(new ArrayList<>());
                }

                // Parse category images JSON array
                String categoryImagesJson = rs.getString("destination_category_images");
                if (categoryImagesJson != null) {
                    try {
                        dto.setDestinationCategoryImageForTourMapDtos(objectMapper.readValue(
                                categoryImagesJson,
                                new TypeReference<List<DestinationCategoryImageForTourMapDto>>() {}
                        ));
                    } catch (JsonProcessingException e) {
                        dto.setDestinationCategoryImageForTourMapDtos(new ArrayList<>());
                    }
                } else {
                    dto.setDestinationCategoryImageForTourMapDtos(new ArrayList<>());
                }

                return dto;
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destinations for tour map: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destinations for tour map from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destinations for tour map: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destinations for tour map");
        }
    }

    @Override
    public List<DestinationResponseDto> getDestinationDetailsByTourId(Long tourId) {
        try {
            LOGGER.info("Fetching destinations for tourId: {}", tourId);

            return jdbcTemplate.query(GET_ALL_DESTINATIONS_BY_TOUR_ID, new Object[]{tourId}, rs -> {
                List<DestinationResponseDto> destinations = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();

                while (rs.next()) {
                    DestinationResponseDto dto = new DestinationResponseDto();
                    dto.setDestinationId(rs.getLong("destination_id"));
                    dto.setDestinationName(rs.getString("destination_name"));
                    dto.setDestinationDescription(rs.getString("destination_description"));
                    dto.setLocation(rs.getString("location"));
                    dto.setLatitude(rs.getObject("latitude", Double.class));
                    dto.setLongitude(rs.getObject("longitude", Double.class));
                    dto.setRatings(rs.getObject("ratings", Double.class));
                    dto.setStatusName(rs.getString("status_name"));

                    try {
                        // Destination categories
                        String categoryJson = rs.getString("destination_categories");
                        dto.setDestinationCategoryDetailsDtos(
                                categoryJson != null
                                        ? mapper.readValue(categoryJson, new TypeReference<List<DestinationCategoryDetailsDto>>() {})
                                        : List.of()
                        );

                        // Images
                        String imagesJson = rs.getString("images");
                        dto.setImages(
                                imagesJson != null
                                        ? mapper.readValue(imagesJson, new TypeReference<List<DestionationImageResponseDto>>() {})
                                        : List.of()
                        );

                        // Activities
                        String activitiesJson = rs.getString("activities");
                        dto.setActivities(
                                activitiesJson != null
                                        ? mapper.readValue(activitiesJson, new TypeReference<List<DestinationActivityResponseDto>>() {})
                                        : List.of()
                        );

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error parsing JSON fields for destination", e);
                    }

                    destinations.add(dto);
                }

                return destinations;
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching tour destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch tour destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching tour destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching tour destinations");
        }
    }

    @Override
    public List<DestinationReviewDetailsResponse> getDestinationReviewDetailsById(Long destinationId) {
        String GET_DESTINATIONS_REVIEW_DETAILS_BY_ID = DestinationQueries.GET_DESTINATIONS_REVIEW_DETAILS_BY_ID;

        try {
            LOGGER.info("Executing query to fetch all destinations reviews for destination id : {}", destinationId);
            Map<Integer, DestinationReviewDetailsResponse> reviewMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_DESTINATIONS_REVIEW_DETAILS_BY_ID, new Object[]{destinationId}, rs -> {
                Integer reviewId = rs.getInt("review_id");

                DestinationReviewDetailsResponse review = reviewMap.get(reviewId);
                if (review == null) {
                    review = DestinationReviewDetailsResponse.builder()
                            .reviewId(reviewId)
                            .destinationId(rs.getInt("destination_id"))
                            .destinationName(rs.getString("destination_name"))
                            .reviewUserId(rs.getInt("review_user_id"))
                            .reviewUserName(rs.getString("review_user_name"))
                            .reviewText(rs.getString("review_text"))
                            .reviewRating(rs.getBigDecimal("review_rating"))
                            .reviewStatus(rs.getString("review_status"))
                            .reviewCreatedBy(rs.getInt("review_created_by"))
                            .reviewCreatedAt(rs.getTimestamp("review_created_at").toLocalDateTime())
                            .reviewUpdatedBy(rs.getInt("review_updated_by"))
                            .reviewUpdatedAt(rs.getTimestamp("review_updated_at") != null ?
                                    rs.getTimestamp("review_updated_at").toLocalDateTime() : null)
                            .images(new ArrayList<>())
                            .reactions(new ArrayList<>())
                            .comments(new ArrayList<>())
                            .build();
                    reviewMap.put(reviewId, review);
                }

                Integer imageId = rs.getObject("image_id", Integer.class);
                if (imageId != null && review.getImages().stream().noneMatch(i -> i.getImageId().equals(imageId))) {
                    DestinationReviewDetailsResponse.Image image = DestinationReviewDetailsResponse.Image.builder()
                            .imageId(imageId)
                            .imageName(rs.getString("image_name"))
                            .imageDescription(rs.getString("image_description"))
                            .imageUrl(rs.getString("image_url"))
                            .imageStatus(rs.getString("image_status"))
                            .imageCreatedBy(rs.getInt("image_created_by"))
                            .imageCreatedAt(rs.getTimestamp("image_created_at") != null ?
                                    rs.getTimestamp("image_created_at").toLocalDateTime() : null)
                            .build();
                    review.getImages().add(image);
                }

                Integer reactionId = rs.getObject("review_reaction_id", Integer.class);
                if (reactionId != null && review.getReactions().stream().noneMatch(r -> r.getReviewReactionId().equals(reactionId))) {
                    DestinationReviewDetailsResponse.Reaction reaction = DestinationReviewDetailsResponse.Reaction.builder()
                            .reviewReactionId(reactionId)
                            .reactionReviewId(rs.getInt("reaction_review_id"))
                            .reactionUserId(rs.getInt("reaction_user_id"))
                            .reactionUserName(rs.getString("reaction_user_name"))
                            .reactionType(rs.getString("reaction_type"))
                            .reviewReactionStatus(rs.getString("review_reaction_status"))
                            .reactionCreatedAt(rs.getTimestamp("reaction_created_at") != null ?
                                    rs.getTimestamp("reaction_created_at").toLocalDateTime() : null)
                            .build();
                    review.getReactions().add(reaction);
                }

                Integer commentId = rs.getObject("comment_id", Integer.class);
                DestinationReviewDetailsResponse.Comment comment = null;
                if (commentId != null) {
                    comment = review.getComments().stream()
                            .filter(c -> c.getCommentId().equals(commentId))
                            .findFirst()
                            .orElse(null);

                    if (comment == null) {
                        comment = DestinationReviewDetailsResponse.Comment.builder()
                                .commentId(commentId)
                                .commentReviewId(rs.getInt("comment_review_id"))
                                .commentUserId(rs.getInt("comment_user_id"))
                                .commentUserName(rs.getString("comment_user_name"))
                                .parentCommentId(rs.getObject("parent_comment_id", Integer.class))
                                .commentText(rs.getString("comment_text"))
                                .commentStatus(rs.getString("comment_status"))
                                .commentCreatedAt(rs.getTimestamp("comment_created_at") != null ?
                                        rs.getTimestamp("comment_created_at").toLocalDateTime() : null)
                                .commentCreatedBy(rs.getInt("comment_created_by"))
                                .commentReactions(new ArrayList<>())
                                .build();
                        review.getComments().add(comment);
                    }
                }

                Integer commentReactionId = rs.getObject("comment_reaction_id", Integer.class);
                if (commentReactionId != null && comment != null &&
                        comment.getCommentReactions().stream().noneMatch(cr -> cr.getCommentReactionId().equals(commentReactionId))) {
                    DestinationReviewDetailsResponse.Comment.CommentReaction commentReaction =
                            DestinationReviewDetailsResponse.Comment.CommentReaction.builder()
                                    .commentReactionId(commentReactionId)
                                    .commentReactionCommentId(rs.getInt("comment_reaction_comment_id"))
                                    .commentReactionUserId(rs.getInt("comment_reaction_user_id"))
                                    .commentReactionUserName(rs.getString("comment_reaction_user_name"))
                                    .commentReactionType(rs.getString("comment_reaction_type"))
                                    .commentReactionStatus(rs.getString("comment_reaction_status"))
                                    .commentReactionCreatedBy(rs.getInt("comment_reaction_created_by"))
                                    .commentReactionCreatedAt(rs.getTimestamp("comment_reaction_created_at") != null ?
                                            rs.getTimestamp("comment_reaction_created_at").toLocalDateTime() : null)
                                    .build();
                    comment.getCommentReactions().add(commentReaction);
                }
            });

            return new ArrayList<>(reviewMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destinations");
        }
    }

    @Override
    public List<DestinationReviewDetailsResponse> getAllDestinationsReviewDetails() {
        String GET_DESTINATIONS_REVIEW_DETAILS = DestinationQueries.GET_DESTINATIONS_REVIEW_DETAILS;

        try {
            LOGGER.info("Executing query to fetch all destinations reviews");
            Map<Integer, DestinationReviewDetailsResponse> reviewMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_DESTINATIONS_REVIEW_DETAILS, rs -> {
                Integer reviewId = rs.getInt("review_id");

                DestinationReviewDetailsResponse review = reviewMap.get(reviewId);
                if (review == null) {
                    review = DestinationReviewDetailsResponse.builder()
                            .reviewId(reviewId)
                            .destinationId(rs.getInt("destination_id"))
                            .destinationName(rs.getString("destination_name"))
                            .reviewUserId(rs.getInt("review_user_id"))
                            .reviewUserName(rs.getString("review_user_name"))
                            .reviewText(rs.getString("review_text"))
                            .reviewRating(rs.getBigDecimal("review_rating"))
                            .reviewStatus(rs.getString("review_status"))
                            .reviewCreatedBy(rs.getInt("review_created_by"))
                            .reviewCreatedAt(rs.getTimestamp("review_created_at").toLocalDateTime())
                            .reviewUpdatedBy(rs.getInt("review_updated_by"))
                            .reviewUpdatedAt(rs.getTimestamp("review_updated_at") != null ?
                                    rs.getTimestamp("review_updated_at").toLocalDateTime() : null)
                            .images(new ArrayList<>())
                            .reactions(new ArrayList<>())
                            .comments(new ArrayList<>())
                            .build();
                    reviewMap.put(reviewId, review);
                }

                Integer imageId = rs.getObject("image_id", Integer.class);
                if (imageId != null && review.getImages().stream().noneMatch(i -> i.getImageId().equals(imageId))) {
                    DestinationReviewDetailsResponse.Image image = DestinationReviewDetailsResponse.Image.builder()
                            .imageId(imageId)
                            .imageName(rs.getString("image_name"))
                            .imageDescription(rs.getString("image_description"))
                            .imageUrl(rs.getString("image_url"))
                            .imageStatus(rs.getString("image_status"))
                            .imageCreatedBy(rs.getInt("image_created_by"))
                            .imageCreatedAt(rs.getTimestamp("image_created_at") != null ?
                                    rs.getTimestamp("image_created_at").toLocalDateTime() : null)
                            .build();
                    review.getImages().add(image);
                }

                Integer reactionId = rs.getObject("review_reaction_id", Integer.class);
                if (reactionId != null && review.getReactions().stream().noneMatch(r -> r.getReviewReactionId().equals(reactionId))) {
                    DestinationReviewDetailsResponse.Reaction reaction = DestinationReviewDetailsResponse.Reaction.builder()
                            .reviewReactionId(reactionId)
                            .reactionReviewId(rs.getInt("reaction_review_id"))
                            .reactionUserId(rs.getInt("reaction_user_id"))
                            .reactionUserName(rs.getString("reaction_user_name"))
                            .reactionType(rs.getString("reaction_type"))
                            .reviewReactionStatus(rs.getString("review_reaction_status"))
                            .reactionCreatedAt(rs.getTimestamp("reaction_created_at") != null ?
                                    rs.getTimestamp("reaction_created_at").toLocalDateTime() : null)
                            .build();
                    review.getReactions().add(reaction);
                }

                Integer commentId = rs.getObject("comment_id", Integer.class);
                DestinationReviewDetailsResponse.Comment comment = null;
                if (commentId != null) {
                    comment = review.getComments().stream()
                            .filter(c -> c.getCommentId().equals(commentId))
                            .findFirst()
                            .orElse(null);

                    if (comment == null) {
                        comment = DestinationReviewDetailsResponse.Comment.builder()
                                .commentId(commentId)
                                .commentReviewId(rs.getInt("comment_review_id"))
                                .commentUserId(rs.getInt("comment_user_id"))
                                .commentUserName(rs.getString("comment_user_name"))
                                .parentCommentId(rs.getObject("parent_comment_id", Integer.class))
                                .commentText(rs.getString("comment_text"))
                                .commentStatus(rs.getString("comment_status"))
                                .commentCreatedAt(rs.getTimestamp("comment_created_at") != null ?
                                        rs.getTimestamp("comment_created_at").toLocalDateTime() : null)
                                .commentCreatedBy(rs.getInt("comment_created_by"))
                                .commentReactions(new ArrayList<>())
                                .build();
                        review.getComments().add(comment);
                    }
                }

                Integer commentReactionId = rs.getObject("comment_reaction_id", Integer.class);
                if (commentReactionId != null && comment != null &&
                        comment.getCommentReactions().stream().noneMatch(cr -> cr.getCommentReactionId().equals(commentReactionId))) {
                    DestinationReviewDetailsResponse.Comment.CommentReaction commentReaction =
                            DestinationReviewDetailsResponse.Comment.CommentReaction.builder()
                                    .commentReactionId(commentReactionId)
                                    .commentReactionCommentId(rs.getInt("comment_reaction_comment_id"))
                                    .commentReactionUserId(rs.getInt("comment_reaction_user_id"))
                                    .commentReactionUserName(rs.getString("comment_reaction_user_name"))
                                    .commentReactionType(rs.getString("comment_reaction_type"))
                                    .commentReactionStatus(rs.getString("comment_reaction_status"))
                                    .commentReactionCreatedBy(rs.getInt("comment_reaction_created_by"))
                                    .commentReactionCreatedAt(rs.getTimestamp("comment_reaction_created_at") != null ?
                                            rs.getTimestamp("comment_reaction_created_at").toLocalDateTime() : null)
                                    .build();
                    comment.getCommentReactions().add(commentReaction);
                }
            });

            return new ArrayList<>(reviewMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destinations");
        }
    }

    @Override
    public DestinationResponseDto getDestinationDetailsById(Long destinationId) {
        try {
            LOGGER.info("Fetching destination details for ID: {}", destinationId);

            return jdbcTemplate.query(GET_DESTINATION_DETAILS_BY_ID, new Object[]{destinationId}, rs -> {
                if (!rs.next()) {
                    return null; // Destination not found
                }

                DestinationResponseDto dto = new DestinationResponseDto();
                dto.setDestinationId(rs.getLong("destination_id"));
                dto.setDestinationName(rs.getString("destination_name"));
                dto.setDestinationDescription(rs.getString("destination_description"));
                dto.setLocation(rs.getString("location"));
                dto.setLatitude(rs.getObject("latitude", Double.class));
                dto.setLongitude(rs.getObject("longitude", Double.class));
                dto.setRatings(rs.getObject("ratings", Double.class));
                dto.setStatusName(rs.getString("status_name"));

                ObjectMapper mapper = new ObjectMapper();

                try {
                    // Categories
                    String categoryJson = rs.getString("destination_categories");
                    dto.setDestinationCategoryDetailsDtos(
                            categoryJson != null
                                    ? mapper.readValue(categoryJson, new TypeReference<List<DestinationCategoryDetailsDto>>() {})
                                    : List.of()
                    );

                    // Images
                    String imagesJson = rs.getString("images");
                    dto.setImages(
                            imagesJson != null
                                    ? mapper.readValue(imagesJson, new TypeReference<List<DestionationImageResponseDto>>() {})
                                    : List.of()
                    );

                    // Activities
                    String activitiesJson = rs.getString("activities");
                    dto.setActivities(
                            activitiesJson != null
                                    ? mapper.readValue(activitiesJson, new TypeReference<List<DestinationActivityResponseDto>>() {})
                                    : List.of()
                    );

                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error parsing destination JSON fields", e);
                }

                return dto;
            });

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destination from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destination");
        }
    }

    @Override
    public List<DestinationHistoryDetailsResponse> getAllDestinationHistoryDetails() {
        String GET_DESTINATION_REVIEW_DETAILS = DestinationQueries.GET_DESTINATION_REVIEW_DETAILS;

        try {
            LOGGER.info("Executing query to fetch destination history details.");

            Map<Integer, DestinationHistoryDetailsResponse> historyMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_DESTINATION_REVIEW_DETAILS, rs -> {
                Integer historyId = rs.getInt("history_id");

                historyMap.computeIfAbsent(historyId, id -> {
                    DestinationHistoryDetailsResponse.Destination destination = null;
                    try {
                        destination = new DestinationHistoryDetailsResponse.Destination(
                                rs.getInt("destination_id"),
                                rs.getString("destination_name"),
                                rs.getString("destination_description"),
                                rs.getString("destination_location"),
                                rs.getBigDecimal("latitude"),
                                rs.getBigDecimal("longitude")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    DestinationHistoryDetailsResponse.Status historyStatus = null;
                    try {
                        historyStatus = new DestinationHistoryDetailsResponse.Status(
                                rs.getInt("history_status_id"),
                                rs.getString("history_status_name")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    DestinationHistoryDetailsResponse.UserSummary createdBy = null;
                    try {
                        createdBy = new DestinationHistoryDetailsResponse.UserSummary(
                                nullSafeInteger(rs.getString("history_created_by_username")),
                                rs.getString("history_created_by_username")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    DestinationHistoryDetailsResponse.UserSummary updatedBy = null;
                    try {
                        updatedBy = new DestinationHistoryDetailsResponse.UserSummary(
                                nullSafeInteger(rs.getString("history_updated_by_username")),
                                rs.getString("history_updated_by_username")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    DestinationHistoryDetailsResponse.UserSummary terminatedBy = null;
                    try {
                        terminatedBy = new DestinationHistoryDetailsResponse.UserSummary(
                                nullSafeInteger(rs.getString("history_terminated_by_username")),
                                rs.getString("history_terminated_by_username")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        return DestinationHistoryDetailsResponse.builder()
                                .historyId(historyId)
                                .destination(destination)
                                .title(rs.getString("history_title"))
                                .description(rs.getString("history_description"))
                                .eventDate(rs.getDate("event_date") != null ? rs.getDate("event_date").toLocalDate() : null)
                                .historyStatus(historyStatus)
                                .createdBy(createdBy)
                                .updatedBy(updatedBy)
                                .terminatedBy(terminatedBy)
                                .createdAt(rs.getTimestamp("history_created_at") != null ? rs.getTimestamp("history_created_at").toLocalDateTime() : null)
                                .updatedAt(rs.getTimestamp("history_updated_at") != null ? rs.getTimestamp("history_updated_at").toLocalDateTime() : null)
                                .terminatedAt(rs.getTimestamp("history_terminated_at") != null ? rs.getTimestamp("history_terminated_at").toLocalDateTime() : null)
                                .images(new ArrayList<>())
                                .build();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                Integer imageId = rs.getInt("image_id");
                if (imageId != 0) {
                    DestinationHistoryDetailsResponse.HistoryImage image = new DestinationHistoryDetailsResponse.HistoryImage();
                    image.setImageId(imageId);
                    image.setName(rs.getString("image_name"));
                    image.setDescription(rs.getString("image_description"));
                    image.setImageUrl(rs.getString("image_url"));
                    image.setImageStatus(new DestinationHistoryDetailsResponse.Status(
                            rs.getInt("image_status_id"),
                            rs.getString("image_status_name")
                    ));
                    image.setCreatedBy(new DestinationHistoryDetailsResponse.UserSummary(
                            nullSafeInteger(rs.getString("image_created_by_username")),
                            rs.getString("image_created_by_username")
                    ));
                    image.setUpdatedBy(new DestinationHistoryDetailsResponse.UserSummary(
                            nullSafeInteger(rs.getString("image_updated_by_username")),
                            rs.getString("image_updated_by_username")
                    ));
                    image.setTerminatedBy(new DestinationHistoryDetailsResponse.UserSummary(
                            nullSafeInteger(rs.getString("image_terminated_by_username")),
                            rs.getString("image_terminated_by_username")
                    ));
                    image.setCreatedAt(rs.getTimestamp("image_created_at") != null ? rs.getTimestamp("image_created_at").toLocalDateTime() : null);
                    image.setUpdatedAt(rs.getTimestamp("image_updated_at") != null ? rs.getTimestamp("image_updated_at").toLocalDateTime() : null);
                    image.setTerminatedAt(rs.getTimestamp("image_terminated_at") != null ? rs.getTimestamp("image_terminated_at").toLocalDateTime() : null);

                    historyMap.get(historyId).getImages().add(image);
                }
            });

            return new ArrayList<>(historyMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destinations");
        }
    }

    private Integer nullSafeInteger(String username) {
        return (username != null && !username.isEmpty()) ? 0 : null; // replace 0 if you want a default id
    }

    @Override
    public List<DestinationHistoryDetailsResponse> getDestinationHistoryDetailsById(Long destinationId) {
        String GET_DESTINATION_REVIEW_DETAILS_BY_ID = DestinationQueries.GET_DESTINATION_REVIEW_DETAILS_BY_ID;

        try {
            LOGGER.info("Executing query to fetch destination history for ID: {}", destinationId);

            Map<Integer, DestinationHistoryDetailsResponse> historyMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_DESTINATION_REVIEW_DETAILS_BY_ID, new Object[]{destinationId}, rs -> {
                Integer historyId = rs.getInt("history_id");

                historyMap.computeIfAbsent(historyId, id -> {
                    DestinationHistoryDetailsResponse.Destination destination = null;
                    try {
                        destination = new DestinationHistoryDetailsResponse.Destination(
                                rs.getInt("destination_id"),
                                rs.getString("destination_name"),
                                rs.getString("destination_description"),
                                rs.getString("destination_location"),
                                rs.getBigDecimal("latitude"),
                                rs.getBigDecimal("longitude")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    DestinationHistoryDetailsResponse.Status historyStatus = null;
                    try {
                        historyStatus = new DestinationHistoryDetailsResponse.Status(
                                rs.getInt("history_status_id"),
                                rs.getString("history_status_name")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    DestinationHistoryDetailsResponse.UserSummary createdBy = null;
                    try {
                        createdBy = new DestinationHistoryDetailsResponse.UserSummary(
                                nullSafeInteger(rs.getString("history_created_by_username")),
                                rs.getString("history_created_by_username")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    DestinationHistoryDetailsResponse.UserSummary updatedBy = null;
                    try {
                        updatedBy = new DestinationHistoryDetailsResponse.UserSummary(
                                nullSafeInteger(rs.getString("history_updated_by_username")),
                                rs.getString("history_updated_by_username")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    DestinationHistoryDetailsResponse.UserSummary terminatedBy = null;
                    try {
                        terminatedBy = new DestinationHistoryDetailsResponse.UserSummary(
                                nullSafeInteger(rs.getString("history_terminated_by_username")),
                                rs.getString("history_terminated_by_username")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        return DestinationHistoryDetailsResponse.builder()
                                .historyId(historyId)
                                .destination(destination)
                                .title(rs.getString("history_title"))
                                .description(rs.getString("history_description"))
                                .eventDate(rs.getDate("event_date") != null ? rs.getDate("event_date").toLocalDate() : null)
                                .historyStatus(historyStatus)
                                .createdBy(createdBy)
                                .updatedBy(updatedBy)
                                .terminatedBy(terminatedBy)
                                .createdAt(rs.getTimestamp("history_created_at") != null ? rs.getTimestamp("history_created_at").toLocalDateTime() : null)
                                .updatedAt(rs.getTimestamp("history_updated_at") != null ? rs.getTimestamp("history_updated_at").toLocalDateTime() : null)
                                .terminatedAt(rs.getTimestamp("history_terminated_at") != null ? rs.getTimestamp("history_terminated_at").toLocalDateTime() : null)
                                .images(new ArrayList<>())
                                .build();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                Integer imageId = rs.getInt("image_id");
                if (imageId != 0) {
                    DestinationHistoryDetailsResponse.HistoryImage image = new DestinationHistoryDetailsResponse.HistoryImage();
                    image.setImageId(imageId);
                    image.setName(rs.getString("image_name"));
                    image.setDescription(rs.getString("image_description"));
                    image.setImageUrl(rs.getString("image_url"));
                    image.setImageStatus(new DestinationHistoryDetailsResponse.Status(
                            rs.getInt("image_status_id"),
                            rs.getString("image_status_name")
                    ));
                    image.setCreatedBy(new DestinationHistoryDetailsResponse.UserSummary(
                            nullSafeInteger(rs.getString("image_created_by_username")),
                            rs.getString("image_created_by_username")
                    ));
                    image.setUpdatedBy(new DestinationHistoryDetailsResponse.UserSummary(
                            nullSafeInteger(rs.getString("image_updated_by_username")),
                            rs.getString("image_updated_by_username")
                    ));
                    image.setTerminatedBy(new DestinationHistoryDetailsResponse.UserSummary(
                            nullSafeInteger(rs.getString("image_terminated_by_username")),
                            rs.getString("image_terminated_by_username")
                    ));
                    image.setCreatedAt(rs.getTimestamp("image_created_at") != null ? rs.getTimestamp("image_created_at").toLocalDateTime() : null);
                    image.setUpdatedAt(rs.getTimestamp("image_updated_at") != null ? rs.getTimestamp("image_updated_at").toLocalDateTime() : null);
                    image.setTerminatedAt(rs.getTimestamp("image_terminated_at") != null ? rs.getTimestamp("image_terminated_at").toLocalDateTime() : null);

                    historyMap.get(historyId).getImages().add(image);
                }
            });

            return new ArrayList<>(historyMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination history: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destination history from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination history: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destination history");
        }
    }

    @Override
    public List<DestinationHistoryImageResponse> getAllDestinationHistoryImages() {
        String GET_DESTINATION_HISTORY_IMAGES = DestinationQueries.GET_DESTINATION_HISTORY_IMAGES;

        try {
            LOGGER.info("Executing query to fetch destination history images.");
            return jdbcTemplate.query(GET_DESTINATION_HISTORY_IMAGES, (rs, rowNum) -> {

                DestinationHistoryImageResponse.UserDto imageCreatedBy =
                        new DestinationHistoryImageResponse.UserDto(rs.getString("image_created_by_username"));

                DestinationHistoryImageResponse.UserDto imageUpdatedBy =
                        new DestinationHistoryImageResponse.UserDto(rs.getString("image_updated_by_username"));

                DestinationHistoryImageResponse.UserDto imageTerminatedBy =
                        rs.getString("image_terminated_by_username") != null ?
                                new DestinationHistoryImageResponse.UserDto(rs.getString("image_terminated_by_username")) : null;

                DestinationHistoryImageResponse.HistoryDto history = new DestinationHistoryImageResponse.HistoryDto(
                        rs.getLong("history_id"),
                        rs.getString("history_title"),
                        rs.getString("history_description"),
                        rs.getObject("history_event_date", LocalDate.class),
                        rs.getString("history_status_name")
                );

                DestinationHistoryImageResponse.DestinationDto destination = new DestinationHistoryImageResponse.DestinationDto(
                        rs.getLong("destination_id"),
                        rs.getString("destination_name"),
                        rs.getString("destination_location"),
                        rs.getBigDecimal("latitude"),
                        rs.getBigDecimal("longitude")
                );

                return DestinationHistoryImageResponse.builder()
                        .imageId(rs.getLong("image_id"))
                        .imageName(rs.getString("image_name"))
                        .imageDescription(rs.getString("image_description"))
                        .imageUrl(rs.getString("image_url"))
                        .imageStatusName(rs.getString("image_status_name"))
                        .imageCreatedAt(rs.getObject("image_created_at", LocalDateTime.class))
                        .imageUpdatedAt(rs.getObject("image_updated_at", LocalDateTime.class))
                        .imageTerminatedAt(rs.getObject("image_terminated_at", LocalDateTime.class))
                        .imageCreatedBy(imageCreatedBy)
                        .imageUpdatedBy(imageUpdatedBy)
                        .imageTerminatedBy(imageTerminatedBy)
                        .history(history)
                        .destination(destination)
                        .build();
            });
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination history images: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destination history images from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination history images: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destination history images");
        }
    }

    @Override
    public List<DestinationHistoryImageResponse> getDestinationHistoryImagesById(Long destinationId) {
        String GET_DESTINATION_HISTORY_IMAGES_BY_ID = DestinationQueries.GET_DESTINATION_HISTORY_IMAGES_BY_ID;

        try {
            LOGGER.info("Executing query to fetch destination history images by destination id : {}.", destinationId);
            return jdbcTemplate.query(GET_DESTINATION_HISTORY_IMAGES_BY_ID, new Object[]{destinationId}, (rs, rowNum) -> {

                DestinationHistoryImageResponse.UserDto imageCreatedBy =
                        new DestinationHistoryImageResponse.UserDto(rs.getString("image_created_by_username"));

                DestinationHistoryImageResponse.UserDto imageUpdatedBy =
                        new DestinationHistoryImageResponse.UserDto(rs.getString("image_updated_by_username"));

                DestinationHistoryImageResponse.UserDto imageTerminatedBy =
                        rs.getString("image_terminated_by_username") != null ?
                                new DestinationHistoryImageResponse.UserDto(rs.getString("image_terminated_by_username")) : null;

                DestinationHistoryImageResponse.HistoryDto history = new DestinationHistoryImageResponse.HistoryDto(
                        rs.getLong("history_id"),
                        rs.getString("history_title"),
                        rs.getString("history_description"),
                        rs.getObject("history_event_date", LocalDate.class),
                        rs.getString("history_status_name")
                );

                DestinationHistoryImageResponse.DestinationDto destination = new DestinationHistoryImageResponse.DestinationDto(
                        rs.getLong("destination_id"),
                        rs.getString("destination_name"),
                        rs.getString("destination_location"),
                        rs.getBigDecimal("latitude"),
                        rs.getBigDecimal("longitude")
                );

                return DestinationHistoryImageResponse.builder()
                        .imageId(rs.getLong("image_id"))
                        .imageName(rs.getString("image_name"))
                        .imageDescription(rs.getString("image_description"))
                        .imageUrl(rs.getString("image_url"))
                        .imageStatusName(rs.getString("image_status_name"))
                        .imageCreatedAt(rs.getObject("image_created_at", LocalDateTime.class))
                        .imageUpdatedAt(rs.getObject("image_updated_at", LocalDateTime.class))
                        .imageTerminatedAt(rs.getObject("image_terminated_at", LocalDateTime.class))
                        .imageCreatedBy(imageCreatedBy)
                        .imageUpdatedBy(imageUpdatedBy)
                        .imageTerminatedBy(imageTerminatedBy)
                        .history(history)
                        .destination(destination)
                        .build();
            });
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination history images by ID {}: {}", destinationId, ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destination history images for destination ID " + destinationId);
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination history images by ID {}: {}", destinationId, ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destination history images for destination ID " + destinationId);
        }
    }

    @Override
    public DestinationsWithParamsResponse getDestinationWithParams(
            DestinationDataRequest destinationDataRequest) {

        try {
            LOGGER.info("Executing query to fetch destinations with filters.");

            int offset = (destinationDataRequest.getPageNumber() - 1)
                    * destinationDataRequest.getPageSize();

            // 1️⃣ Get paginated destination IDs
            List<Integer> destinationIds = jdbcTemplate.queryForList(
                    GET_PAGINATED_DESTINATION_IDS,
                    new Object[]{
                            destinationDataRequest.getName(), destinationDataRequest.getName(),
                            destinationDataRequest.getMinPrice(), destinationDataRequest.getMinPrice(),
                            destinationDataRequest.getMaxPrice(), destinationDataRequest.getMaxPrice(),
                            destinationDataRequest.getDuration(), destinationDataRequest.getDuration(),
                            destinationDataRequest.getDestinationCategory(), destinationDataRequest.getDestinationCategory(),
                            destinationDataRequest.getSeason(), destinationDataRequest.getSeason(),
                            destinationDataRequest.getStatus(), destinationDataRequest.getStatus(),
                            destinationDataRequest.getPageSize(), offset
                    },
                    Integer.class
            );

            if (destinationIds.isEmpty()) {
                return new DestinationsWithParamsResponse(0, Collections.emptyList());
            }

            // 2️⃣ Get total count
            Integer totalCount = jdbcTemplate.queryForObject(
                    GET_FILTERED_DESTINATION_COUNT,
                    new Object[]{
                            destinationDataRequest.getName(), destinationDataRequest.getName(),
                            destinationDataRequest.getMinPrice(), destinationDataRequest.getMinPrice(),
                            destinationDataRequest.getMaxPrice(), destinationDataRequest.getMaxPrice(),
                            destinationDataRequest.getDuration(), destinationDataRequest.getDuration(),
                            destinationDataRequest.getDestinationCategory(), destinationDataRequest.getDestinationCategory(),
                            destinationDataRequest.getSeason(), destinationDataRequest.getSeason(),
                            destinationDataRequest.getStatus(), destinationDataRequest.getStatus()
                    },
                    Integer.class
            );

            if (totalCount == null || totalCount == 0) {
                return new DestinationsWithParamsResponse(0, Collections.emptyList());
            }

            // 3️⃣ Fetch full destination details by IDs
            String inSql = String.join(",", Collections.nCopies(destinationIds.size(), "?"));
            String fullQuery = String.format(GET_DESTINATIONS_BY_IDS, inSql);

            List<DestinationResponseDto> destinations = jdbcTemplate.query(
                    fullQuery,
                    destinationIds.toArray(),
                    (rs, rowNum) -> {

                        DestinationResponseDto destination = new DestinationResponseDto();

                        destination.setDestinationId(rs.getLong("destination_id"));
                        destination.setDestinationName(rs.getString("destination_name"));
                        destination.setDestinationDescription(rs.getString("destination_description"));
                        destination.setLocation(rs.getString("location"));
                        destination.setLatitude(rs.getObject("latitude", Double.class));
                        destination.setLongitude(rs.getObject("longitude", Double.class));
                        destination.setRatings(rs.getObject("ratings", Double.class));
                        destination.setStatusName(rs.getString("status_name"));

                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            // 🔹 Categories
                            String categoryJson = rs.getString("destination_categories");
                            destination.setDestinationCategoryDetailsDtos(
                                    categoryJson != null
                                            ? mapper.readValue(categoryJson,
                                            new TypeReference<List<DestinationCategoryDetailsDto>>() {})
                                            : List.of()
                            );

                            // 🔹 Activities
                            String activitiesJson = rs.getString("activities");
                            destination.setActivities(
                                    activitiesJson != null
                                            ? mapper.readValue(activitiesJson,
                                            new TypeReference<List<DestinationActivityResponseDto>>() {})
                                            : List.of()
                            );

                            // 🔹 Images
                            String imagesJson = rs.getString("images");
                            destination.setImages(
                                    imagesJson != null
                                            ? mapper.readValue(imagesJson,
                                            new TypeReference<List<DestionationImageResponseDto>>() {})
                                            : List.of()
                            );

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error parsing destination JSON fields", e);
                        }

                        return destination;
                    }
            );

            return new DestinationsWithParamsResponse(totalCount, destinations);

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destinations");
        }
    }

    @Override
    public void insertDestination(DestinationInsertRequest request, Long userId) {

        String INSERT_DESTINATION = DestinationQueries.INSERT_DESTINATION_REQUEST;
        String INSERT_DESTINATION_IMAGE = INSERT_DESTINATION_IMAGES_REQUEST;

        try {
            LOGGER.error("Start the execute insert destination");

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        INSERT_DESTINATION,
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, request.getName());
                ps.setString(2, request.getDescription());
                ps.setString(3, request.getStatus());
                ps.setString(4, request.getDestinationCategory());
                ps.setString(5, request.getLocation());
                ps.setDouble(6, request.getLatitude());
                ps.setDouble(7, request.getLongitude());
                ps.setLong(8, userId);
                ps.setDouble(9, request.getExtraPrice());
                ps.setString(10, request.getExtraPriceNote());

                return ps;
            }, keyHolder);


            Long destinationId = keyHolder.getKey().longValue();

            if (request.getImages() != null && !request.getImages().isEmpty()) {
                for (DestinationInsertRequest.Image image : request.getImages()) {
                    jdbcTemplate.update(
                            INSERT_DESTINATION_IMAGE,
                            destinationId,
                            image.getName(),
                            image.getDescription(),
                            image.getImageUrl(),
                            image.getStatus(),
                            userId
                    );
                }
            }
        } catch (DataAccessException ife) {
            LOGGER.error(ife.toString());
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to insert destination : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert destination");
        }
    }

    @Override
    public void terminateDestination(DestinationTerminateRequest destinationTerminateRequest, Long userId) {
        String DESTINATION_TERMINATE = DestinationQueries.DESTINATION_TERMINATE;
        try {
            LOGGER.error("Start the execute terminate destination");
            jdbcTemplate.update(DESTINATION_TERMINATE, new Object[]{CommonStatus.TERMINATED.toString(), userId, destinationTerminateRequest.getDestinationId()});
        } catch (DataAccessException tfe) {
            LOGGER.error(tfe.toString());
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to terminate destination : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate destination");
        }
    }

    @Override
    public List<DestinationForTerminateResponse> getDestinationsForTerminate() {
        String GET_ACTIVE_DESTINATIONS_FOR_TERMINATE = DestinationQueries.GET_ACTIVE_DESTINATIONS_FOR_TERMINATE;

        try {
            LOGGER.error("Start the execute get destinations for terminate.");
            return jdbcTemplate.query(
                    GET_ACTIVE_DESTINATIONS_FOR_TERMINATE,
                    new Object[]{CommonStatus.ACTIVE.toString()},
                    (rs, rowNum) -> DestinationForTerminateResponse.builder()
                            .destinationId(rs.getLong("destination_id"))
                            .destinationName(rs.getString("name"))
                            .build()
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch destinations for terminate: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations");
        }
    }

    @Override
    public void updateBasicDestinationDetails(DestinationUpdateRequest destinationUpdateRequest, Long userId) {
        String UPDATE_BASIC_DESTINATION_DETAILS = DestinationQueries.UPDATE_BASIC_DESTINATION_DETAILS;

        try {
            LOGGER.error("Start the execute update destination");
            jdbcTemplate.update(UPDATE_BASIC_DESTINATION_DETAILS, new Object[]{
                    destinationUpdateRequest.getName(),
                    destinationUpdateRequest.getDescription(),
                    destinationUpdateRequest.getStatus(),
                    destinationUpdateRequest.getDestinationCategory(),
                    destinationUpdateRequest.getLocation(),
                    destinationUpdateRequest.getLatitude(),
                    destinationUpdateRequest.getLongitude(),
                    userId,
                    destinationUpdateRequest.getExtraPrice(),
                    destinationUpdateRequest.getExtraPriceNote(),
                    destinationUpdateRequest.getDestinationId()
            });
        } catch (DataAccessException ufe) {
            LOGGER.error(ufe.toString());
            throw new UpdateFailedErrorExceptionHandler(ufe.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to update destination : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to update destination");
        }
    }

    @Override
    public void removeDestinationImages(List<Long> removeImages, Long userId) {
        try {
            LOGGER.error("Start the execute remove destination images");
            jdbcTemplate.batchUpdate(
                    DestinationQueries.DESTINATION_IMAGES_REMOVE,
                    removeImages,
                    removeImages.size(),
                    (ps, imageId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
                        ps.setLong(2, userId);
                        ps.setLong(3, imageId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove destination images", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove destination images : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove destination images");
        }
    }

    @Override
    public void addNewImagesToDestination(List<DestinationInsertRequest.Image> newImages, Long destinationId, Long userId) {
        String INSERT_DESTINATION_IMAGE = DestinationQueries.INSERT_DESTINATION_IMAGES_REQUEST;

        try {
            LOGGER.error("Start the execute add destination images");
            for (DestinationInsertRequest.Image image : newImages) {
                jdbcTemplate.update(
                        INSERT_DESTINATION_IMAGE,
                        destinationId,
                        image.getName(),
                        image.getDescription(),
                        image.getImageUrl(),
                        image.getStatus(),
                        userId
                );
            }
        } catch (DataAccessException ife) {
            LOGGER.error(ife.toString());
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to insert destination image : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert destination image");
        }
    }

    @Override
    public void removeDestinationActivities(List<Long> removeActivities, Long userId) {
        String DESTINATION_ACTIVITIES_REMOVE = DestinationQueries.DESTINATION_ACTIVITIES_REMOVE;

        try {
            LOGGER.error("Start the execute remove destination activities.");
            jdbcTemplate.batchUpdate(
                    DESTINATION_ACTIVITIES_REMOVE,
                    removeActivities,
                    removeActivities.size(),
                    (ps, activityId) -> {
                        ps.setString(1, CommonStatus.TERMINATED.toString());
                        ps.setLong(2, userId);
                        ps.setLong(3, activityId);
                    }
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to remove destination activities", e);
            throw new TerminateFailedErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to remove destination activities : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to remove destination activities");
        }
    }

    @Override
    public void addNewActivitiesToDestination(List<DestinationUpdateRequest.Activity> newActivities, Long destinationId, Long userId) {
        String INSERT_DESTINATION_ACTIVITY = DestinationQueries.INSERT_DESTINATION_ACTIVITY;
        String INSERT_DESTINATION_ACTIVITY_IMAGE = DestinationQueries.INSERT_DESTINATION_ACTIVITY_IMAGE;

        try {
            LOGGER.error("Start the execute add destination activities.");
            KeyHolder keyHolder = new GeneratedKeyHolder();

            for (DestinationUpdateRequest.Activity activity : newActivities) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            INSERT_DESTINATION_ACTIVITY,
                            Statement.RETURN_GENERATED_KEYS
                    );

                    ps.setLong(1, destinationId);
                    ps.setString(2, activity.getName());
                    ps.setString(3, activity.getDescription());
                    ps.setString(4, activity.getActivityCategory());
                    ps.setDouble(5, activity.getDurationHover());
                    ps.setTime(6, Time.valueOf(activity.getAvailableFrom()));
                    ps.setTime(7, Time.valueOf(activity.getAvailableTo()));
                    ps.setDouble(8, activity.getPriceLocal());
                    ps.setDouble(9, activity.getPriceForeigners());
                    ps.setInt(10, activity.getMinParticipate());
                    ps.setInt(11, activity.getMaxParticipate());
                    ps.setString(12, activity.getSeasons().stream().collect(Collectors.joining(",")));
                    ps.setString(13, activity.getStatus());
                    ps.setLong(14, userId);

                    return ps;
                }, keyHolder);

                Long activityId = keyHolder.getKey().longValue();

                if (activity.getActivityImages() != null && !activity.getActivityImages().isEmpty()) {
                    for (DestinationUpdateRequest.Image image : activity.getActivityImages()) {
                        jdbcTemplate.update(
                                INSERT_DESTINATION_ACTIVITY_IMAGE,
                                activityId,
                                image.getName(),
                                image.getDescription(),
                                image.getImageUrl(),
                                image.getStatus(),
                                userId
                        );
                    }
                }
            }
        } catch (DataAccessException ife) {
            LOGGER.error(ife.toString());
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to insert activity : ", e);
            throw new InternalServerErrorExceptionHandler("Failed to insert activity");
        }
    }

    @Override
    public DestinationStatisticsResponse.DestinationDetails getDestinationDetailsStatistics() {
        try {
            LOGGER.info("Executing query to fetch destination details statistics.");

            return jdbcTemplate.queryForObject(
                    DestinationQueries.GET_DESTINATION_DETAILS_STATISTICS,
                    (rs, rowNum) -> DestinationStatisticsResponse.DestinationDetails.builder()
                            .totalDestinationCount(rs.getInt("totalDestinationCount"))
                            .activeDestinations(rs.getInt("activeDestinations"))
                            .inActiveDestinations(rs.getInt("inActiveDestinations"))
                            .hiddenDestinations(rs.getInt("hiddenDestinations"))
                            .recentlyUpdateDestinations(rs.getInt("recentlyUpdateDestinations"))
                            .recentlyAddedDestinations(rs.getInt("recentlyAddedDestinations"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination details statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destination details statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination details statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destination details statistics");
        }
    }

    @Override
    public DestinationStatisticsResponse.WishDetails getDestinationWishStatistics() {
        try {
            LOGGER.info("Executing query to fetch destination wish statistics.");

            return jdbcTemplate.queryForObject(
                    DestinationQueries.GET_DESTINATION_WISH_STATISTICS,
                    (rs, rowNum) -> DestinationStatisticsResponse.WishDetails.builder()
                            .wishListCount(rs.getInt("wishListCount"))
                            .notWishListCount(rs.getInt("notWishListCount"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination wish statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destination wish statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination wish statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destination wish statistics");
        }
    }

    @Override
    public List<DestinationStatisticsResponse.CategoryDetails> getDestinationCategoryStatistics() {
        try {
            LOGGER.info("Executing query to fetch destination category statistics.");

            return jdbcTemplate.query(
                    DestinationQueries.GET_DESTINATION_CATEGORY_STATISTICS,
                    (rs, rowNum) -> DestinationStatisticsResponse.CategoryDetails.builder()
                            .categoryId(rs.getLong("category_id"))
                            .categoryName(rs.getString("category_name"))
                            .count(rs.getInt("destination_count"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination category statistics: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch destination category statistics from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination category statistics: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching destination category statistics");
        }
    }

}
