package com.felicita.repository.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.dto.ActivityCategoryImageResponseDto;
import com.felicita.model.dto.ActivityCategoryResponseDto;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.seasons.SeasonImageInsertRequest;
import com.felicita.model.request.seasons.SeasonImageUpdateRequest;
import com.felicita.model.request.seasons.SeasonInsertRequest;
import com.felicita.model.request.seasons.SeasonUpdateRequest;
import com.felicita.model.response.SeasonBasicResponse;
import com.felicita.model.response.SeasonDetailsResponse;
import com.felicita.model.response.TourTerminateRequest;
import com.felicita.model.response.seasons.SeasonAllDetailsResponse;
import com.felicita.model.response.seasons.SeasonImageResponse;
import com.felicita.model.response.statistics.SeasonStatisticsResponse;
import com.felicita.queries.ActivitiesQueries;
import com.felicita.queries.SeasonQueries;
import com.felicita.repository.CommonRepository;
import com.felicita.repository.SeasonRepository;
import com.felicita.repository.StatusRepository;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SeasonRepositoryImpl implements SeasonRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeasonRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;

    @Autowired
    public SeasonRepositoryImpl(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<SeasonDetailsResponse> getSeasonDetailsBySeasonId(String seasonId) {

        try {
            LOGGER.info("Executing query to fetch season details by season id.");

            Map<Long, SeasonDetailsResponse> seasonMap = new LinkedHashMap<>();

            jdbcTemplate.query(
                    SeasonQueries.GET_SEASON_DETAILS_BY_SEASON_ID,
                    new Object[]{seasonId},
                    (rs) -> {

                        Long id = rs.getLong("id");

                        SeasonDetailsResponse season = seasonMap.get(id);

                        if (season == null) {

                            season = SeasonDetailsResponse.builder()
                                    .id(id)
                                    .name(rs.getString("name"))
                                    .standardName(rs.getString("standard_name"))
                                    .localName(rs.getString("local_name"))
                                    .startMonth(rs.getObject("start_month", Integer.class))
                                    .endMonth(rs.getObject("end_month", Integer.class))
                                    .monsoonType(rs.getString("monsoon_type"))
                                    .weatherSummary(rs.getString("weather_summary"))
                                    .temperatureMin(rs.getObject("temperature_min", Integer.class))
                                    .temperatureMax(rs.getObject("temperature_max", Integer.class))
                                    .rainfallPattern(rs.getString("rainfall_pattern"))
                                    .isPeak(rs.getBoolean("is_peak"))
                                    .displayOrder(rs.getObject("display_order", Integer.class))
                                    .description(rs.getString("description"))
                                    .status(rs.getObject("status", Integer.class))
                                    .createdAt(rs.getTimestamp("created_at") != null ?
                                            rs.getTimestamp("created_at").toLocalDateTime() : null)
                                    .createdBy(rs.getObject("created_by", Integer.class))
                                    .updatedAt(rs.getTimestamp("updated_at") != null ?
                                            rs.getTimestamp("updated_at").toLocalDateTime() : null)
                                    .updatedBy(rs.getObject("updated_by", Integer.class))
                                    .seasonImages(new ArrayList<>())
                                    .build();

                            seasonMap.put(id, season);
                        }

                        // Map image (if exists)
                        Long imageId = rs.getObject("image_id", Long.class);

                        if (imageId != null) {

                            SeasonDetailsResponse.SeasonImage image =
                                    SeasonDetailsResponse.SeasonImage.builder()
                                            .id(imageId)
                                            .name(rs.getString("image_name"))
                                            .description(rs.getString("image_description"))
                                            .imageUrl(rs.getString("image_url"))
                                            .status(rs.getObject("image_status", Integer.class))
                                            .createdAt(rs.getTimestamp("image_created_at") != null ?
                                                    rs.getTimestamp("image_created_at").toLocalDateTime() : null)
                                            .createdBy(rs.getObject("image_created_by", Integer.class))
                                            .updatedAt(rs.getTimestamp("image_updated_at") != null ?
                                                    rs.getTimestamp("image_updated_at").toLocalDateTime() : null)
                                            .updatedBy(rs.getObject("image_updated_by", Integer.class))
                                            .build();

                            season.getSeasonImages().add(image);
                        }
                    });

            return new ArrayList<>(seasonMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching season details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch season details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching season details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching season details");
        }
    }

    @Override
    public List<SeasonBasicResponse> getActiveSeasonDetails() {
        try {
            LOGGER.info("Executing query to fetch all seasons with images.");

            Map<Long, SeasonBasicResponse> seasonMap = new LinkedHashMap<>();

            jdbcTemplate.query(
                    SeasonQueries.GET_ALL_SEASONS_BASIC,
                    (rs) -> {

                        Long seasonId = rs.getLong("id");

                        SeasonBasicResponse season = seasonMap.get(seasonId);

                        if (season == null) {

                            season = SeasonBasicResponse.builder()
                                    .id(seasonId)
                                    .name(rs.getString("name"))
                                    .standardName(rs.getString("standard_name"))
                                    .localName(rs.getString("local_name"))
                                    .startMonth(rs.getObject("start_month", Integer.class))
                                    .endMonth(rs.getObject("end_month", Integer.class))
                                    .isPeak(rs.getBoolean("is_peak"))
                                    .displayOrder(rs.getObject("display_order", Integer.class))
                                    .seasonImages(new ArrayList<>())
                                    .build();

                            seasonMap.put(seasonId, season);
                        }

                        Long imageId = rs.getObject("image_id", Long.class);

                        if (imageId != null) {

                            SeasonBasicResponse.SeasonImage image =
                                    SeasonBasicResponse.SeasonImage.builder()
                                            .id(imageId)
                                            .name(rs.getString("image_name"))
                                            .imageUrl(rs.getString("image_url"))
                                            .build();

                            season.getSeasonImages().add(image);
                        }
                    });

            return new ArrayList<>(seasonMap.values());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching seasons: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch seasons from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching seasons: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching seasons");
        }    }

    @Override
    public List<SeasonStatisticsResponse.SeasonActivityCount> getSeasonActivityCount() {

        try {
            LOGGER.info("Fetching season activity count statistics.");

            List<SeasonStatisticsResponse.SeasonActivityCount> result = new ArrayList<>();

            jdbcTemplate.query(SeasonQueries.GET_SEASON_ACTIVITY_COUNT, (rs) -> {

                result.add(
                        SeasonStatisticsResponse.SeasonActivityCount.builder()
                                .seasonId(rs.getLong("season_id"))
                                .seasonName(rs.getString("season_name"))
                                .totalActivities(rs.getInt("total_activities"))
                                .build()
                );
            });

            return result;

        } catch (DataAccessException ex) {
            LOGGER.error("DB error in getSeasonActivityCount: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch season activity count");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error in getSeasonActivityCount: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }

    @Override
    public List<SeasonStatisticsResponse.SeasonTourCount> getSeasonTourCount() {

        try {
            LOGGER.info("Fetching season tour count statistics.");

            List<SeasonStatisticsResponse.SeasonTourCount> result = new ArrayList<>();

            jdbcTemplate.query(SeasonQueries.GET_SEASON_TOUR_COUNT, (rs) -> {

                result.add(
                        SeasonStatisticsResponse.SeasonTourCount.builder()
                                .seasonId(rs.getLong("season_id"))
                                .seasonName(rs.getString("season_name"))
                                .totalTours(rs.getInt("total_tours"))
                                .build()
                );
            });

            return result;

        } catch (DataAccessException ex) {
            LOGGER.error("DB error in getSeasonTourCount: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch season tour count");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error in getSeasonTourCount: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }

    @Override
    public List<SeasonStatisticsResponse.SeasonPopularity> getSeasonPopularity() {

        try {
            LOGGER.info("Fetching season popularity statistics.");

            List<SeasonStatisticsResponse.SeasonPopularity> result = new ArrayList<>();

            jdbcTemplate.query(SeasonQueries.GET_SEASON_POPULARITY, (rs) -> {

                result.add(
                        SeasonStatisticsResponse.SeasonPopularity.builder()
                                .seasonId(rs.getLong("season_id"))
                                .seasonName(rs.getString("season_name"))
                                .totalActivities(rs.getInt("total_activities"))
                                .totalTours(rs.getInt("total_tours"))
                                .totalUsage(rs.getInt("total_usage"))
                                .build()
                );
            });

            return result;

        } catch (DataAccessException ex) {
            LOGGER.error("DB error in getSeasonPopularity: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch season popularity");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error in getSeasonPopularity: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }

    @Override
    public List<SeasonStatisticsResponse.PeakSeasonDistribution> getPeakSeasonDistribution() {

        try {
            LOGGER.info("Fetching peak season distribution statistics.");

            List<SeasonStatisticsResponse.PeakSeasonDistribution> result = new ArrayList<>();

            jdbcTemplate.query(SeasonQueries.GET_PEAK_SEASON_DISTRIBUTION, (rs) -> {

                result.add(
                        SeasonStatisticsResponse.PeakSeasonDistribution.builder()
                                .seasonName(rs.getString("season_name"))
                                .isPeak(rs.getBoolean("is_peak"))
                                .activityCount(rs.getInt("activity_count"))
                                .tourCount(rs.getInt("tour_count"))
                                .build()
                );
            });

            return result;

        } catch (DataAccessException ex) {
            LOGGER.error("DB error in getPeakSeasonDistribution: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch peak season distribution");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error in getPeakSeasonDistribution: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }

    @Override
    public List<SeasonStatisticsResponse.SeasonWeatherOverview> getSeasonWeatherOverview() {

        try {
            LOGGER.info("Fetching season weather overview.");

            List<SeasonStatisticsResponse.SeasonWeatherOverview> result = new ArrayList<>();

            jdbcTemplate.query(SeasonQueries.GET_SEASON_WEATHER_OVERVIEW, (rs) -> {

                result.add(
                        SeasonStatisticsResponse.SeasonWeatherOverview.builder()
                                .seasonId(rs.getLong("id"))
                                .seasonName(rs.getString("name"))
                                .temperatureMin(rs.getObject("temperature_min", Integer.class))
                                .temperatureMax(rs.getObject("temperature_max", Integer.class))
                                .rainfallPattern(rs.getString("rainfall_pattern"))
                                .weatherSummary(rs.getString("weather_summary"))
                                .build()
                );
            });

            return result;

        } catch (DataAccessException ex) {
            LOGGER.error("DB error in getSeasonWeatherOverview: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch weather overview");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error in getSeasonWeatherOverview: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }

    @Override
    public SeasonStatisticsResponse.Summary getSeasonSummary() {

        try {
            LOGGER.info("Fetching season summary KPIs.");

            return jdbcTemplate.queryForObject(
                    SeasonQueries.GET_SEASON_SUMMARY,
                    (rs, rowNum) -> SeasonStatisticsResponse.Summary.builder()
                            .totalSeasons(rs.getInt("total_seasons"))
                            .totalActivities(rs.getInt("total_activities"))
                            .totalTours(rs.getInt("total_tours"))
                            .mostUsedSeason(rs.getString("most_used_season"))
                            .peakSeason(rs.getString("peak_season"))
                            .build()
            );

        } catch (DataAccessException ex) {
            LOGGER.error("DB error in getSeasonSummary: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch season summary");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error in getSeasonSummary: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }

    @Override
    public SeasonAllDetailsResponse getSeasonAllDetailsById(CommonIdRequest request) {

        try {
            LOGGER.info("Fetching full season details by id: {}", request.getId());

            Map<Long, SeasonAllDetailsResponse> seasonMap = new LinkedHashMap<>();

            jdbcTemplate.query(
                    SeasonQueries.GET_SEASON_ALL_DETAILS_BY_ID,
                    new Object[]{request.getId()},
                    (rs) -> {

                        Long seasonId = rs.getLong("id");

                        SeasonAllDetailsResponse season =
                                seasonMap.get(seasonId);

                        if (season == null) {

                            season = SeasonAllDetailsResponse.builder()
                                    .id(seasonId)
                                    .name(rs.getString("name"))
                                    .standardName(rs.getString("standard_name"))
                                    .localName(rs.getString("local_name"))
                                    .startMonth(rs.getObject("start_month", Integer.class))
                                    .endMonth(rs.getObject("end_month", Integer.class))
                                    .monsoonType(rs.getString("monsoon_type"))
                                    .weatherSummary(rs.getString("weather_summary"))
                                    .temperatureMin(rs.getObject("temperature_min", Integer.class))
                                    .temperatureMax(rs.getObject("temperature_max", Integer.class))
                                    .rainfallPattern(rs.getString("rainfall_pattern"))
                                    .isPeak(rs.getBoolean("is_peak"))
                                    .displayOrder(rs.getObject("display_order", Integer.class))
                                    .description(rs.getString("description"))
                                    .status(rs.getObject("status", Integer.class))
                                    .createdAt(rs.getTimestamp("created_at") != null
                                            ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                                    .createdBy(rs.getObject("created_by", Integer.class))
                                    .updatedAt(rs.getTimestamp("updated_at") != null
                                            ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                                    .updatedBy(rs.getObject("updated_by", Integer.class))

                                    .seasonImages(new ArrayList<>())
                                    .activities(new ArrayList<>())
                                    .tours(new ArrayList<>())
                                    .build();

                            seasonMap.put(seasonId, season);
                        }

                        SeasonAllDetailsResponse currentSeason = seasonMap.get(seasonId);

                        // =========================
                        // IMAGES
                        // =========================
                        Long imageId = rs.getObject("image_id", Long.class);

                        if (imageId != null) {

                            SeasonImageResponse image =
                                    SeasonImageResponse.builder()
                                            .id(imageId)
                                            .name(rs.getString("image_name"))
                                            .description(rs.getString("image_description"))
                                            .imageUrl(rs.getString("image_url"))
                                            .status(rs.getObject("image_status", Integer.class))
                                            .createdAt(rs.getTimestamp("image_created_at") != null
                                                    ? rs.getTimestamp("image_created_at").toLocalDateTime() : null)
                                            .createdBy(rs.getObject("image_created_by", Integer.class))
                                            .updatedAt(rs.getTimestamp("image_updated_at") != null
                                                    ? rs.getTimestamp("image_updated_at").toLocalDateTime() : null)
                                            .updatedBy(rs.getObject("image_updated_by", Integer.class))
                                            .build();

                            currentSeason.getSeasonImages().add(image);
                        }

                        // =========================
                        // ACTIVITIES
                        // =========================
                        Long activityId = rs.getObject("activity_id", Long.class);

                        if (activityId != null) {

                            SeasonAllDetailsResponse.SeasonActivity activity =
                                    SeasonAllDetailsResponse.SeasonActivity.builder()
                                            .activityId(activityId)
                                            .activityName(rs.getString("activity_name"))
                                            .activityDescription(rs.getString("activity_description"))
                                            .activityStatus(rs.getString("activity_status_name"))
                                            .build();

                            currentSeason.getActivities().add(activity);
                        }

                        // =========================
                        // TOURS
                        // =========================
                        Long tourId = rs.getObject("tour_id", Long.class);

                        if (tourId != null) {

                            SeasonAllDetailsResponse.SeasonTour tour =
                                    SeasonAllDetailsResponse.SeasonTour.builder()
                                            .tourId(tourId)
                                            .tourName(rs.getString("tour_name"))
                                            .tourDescription(rs.getString("tour_description"))
                                            .tourStatus(rs.getString("tour_status_name"))
                                            .build();

                            currentSeason.getTours().add(tour);
                        }
                    }
            );

            return seasonMap.values().stream().findFirst().orElse(null);

        } catch (DataAccessException ex) {
            LOGGER.error("DB error while fetching season full details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch season full details");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }

    @Override
    public void terminateSeasonImages(CommonIdRequest request) {

        try {
            LOGGER.info("Terminating season image with id: {}", request.getId());

            Long terminatedStatusId = statusRepository.getStatusIdByName("TERMINATED");

            int updated = jdbcTemplate.update(
                    SeasonQueries.TERMINATE_SEASON_IMAGES,
                    terminatedStatusId,
                    request.getId()
            );

            if (updated == 0) {
                throw new DataAccessErrorExceptionHandler("Season image not found for termination");
            }

        } catch (DataAccessException ex) {
            LOGGER.error("DB error while terminating season image: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to terminate season image");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while terminating season image: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }

    @Override
    public Long insertSeasonBasicDetails(SeasonInsertRequest req, Long userId) {

        try {
            LOGGER.info("Inserting season basic details: {}", req.getName());

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {

                PreparedStatement ps = connection.prepareStatement(
                        SeasonQueries.INSERT_SEASON,
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, req.getName());
                ps.setString(2, req.getStandardName());
                ps.setString(3, req.getLocalName());
                ps.setObject(4, req.getStartMonth());
                ps.setObject(5, req.getEndMonth());
                ps.setString(6, req.getMonsoonType());
                ps.setString(7, req.getWeatherSummary());
                ps.setObject(8, req.getTemperatureMin());
                ps.setObject(9, req.getTemperatureMax());
                ps.setString(10, req.getRainfallPattern());
                ps.setBoolean(11, req.getIsPeak() != null ? req.getIsPeak() : false);
                ps.setObject(12, req.getDisplayOrder());
                ps.setString(13, req.getDescription());
                ps.setString(14, req.getStatus());
                ps.setLong(15, userId);

                return ps;

            }, keyHolder);

            if (keyHolder.getKey() == null) {
                throw new InternalServerErrorExceptionHandler("Failed to retrieve generated season id");
            }

            return keyHolder.getKey().longValue();

        } catch (DataAccessException ex) {
            LOGGER.error("DB error while inserting season: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to insert season");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while inserting season: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while inserting season");
        }
    }

    @Override
    public void insertSeasonImages(Long seasonId,
                                   List<SeasonImageInsertRequest> images,
                                   Long userId) {

        try {
            LOGGER.info("Inserting season images for seasonId: {}", seasonId);

            String sql =
                    """
                    INSERT INTO seasons_images (
                        season_id,
                        name,
                        description,
                        image_url,
                        status,
                        created_at,
                        created_by
                    )
                    VALUES (?, ?, ?, ?, ?, NOW(), ?)
                    """;

            jdbcTemplate.batchUpdate(sql, images, images.size(),
                    (ps, img) -> {
                        ps.setLong(1, seasonId);
                        ps.setString(2, img.getName());
                        ps.setString(3, img.getDescription());
                        ps.setString(4, img.getImageUrl());
                        ps.setInt(5, img.getStatus());
                        ps.setLong(6, userId);
                    });

        } catch (Exception ex) {
            LOGGER.error("Error inserting season images: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to insert season images");
        }
    }

    @Override
    public void updateActivitiesSeasonIds(Long seasonId,
                                          List<Long> activityIds,
                                          Long userId) {

        try {
            LOGGER.info("Updating activities seasonId");

            String sql =
                    """
                    UPDATE activities
                    SET season_id = ?,
                        updated_at = NOW(),
                        updated_by = ?
                    WHERE id = ?
                    """;

            jdbcTemplate.batchUpdate(sql, activityIds, activityIds.size(),
                    (ps, activityId) -> {
                        ps.setLong(1, seasonId);
                        ps.setLong(2, userId);
                        ps.setLong(3, activityId);
                    });

        } catch (Exception ex) {
            LOGGER.error("Error updating activities season: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to update activities season");
        }
    }

    @Override
    public void updateToursSeasonIds(Long seasonId,
                                     List<Long> tourIds,
                                     Long userId) {

        try {
            LOGGER.info("Updating tours seasonId");

            String sql =
                    """
                    UPDATE tour
                    SET season = ?,
                        updated_at = NOW(),
                        updated_by = ?
                    WHERE tour_id = ?
                    """;

            jdbcTemplate.batchUpdate(sql, tourIds, tourIds.size(),
                    (ps, tourId) -> {
                        ps.setLong(1, seasonId);
                        ps.setLong(2, userId);
                        ps.setLong(3, tourId);
                    });

        } catch (Exception ex) {
            LOGGER.error("Error updating tours season: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to update tours season");
        }
    }

    @Override
    public void updateSeasonBasicDetails(SeasonUpdateRequest req) {

        try {
            LOGGER.info("Updating season basic details id: {}", req.getId());

            jdbcTemplate.update(
                    SeasonQueries.UPDATE_SEASON,
                    req.getName(),
                    req.getStandardName(),
                    req.getLocalName(),
                    req.getStartMonth(),
                    req.getEndMonth(),
                    req.getMonsoonType(),
                    req.getWeatherSummary(),
                    req.getTemperatureMin(),
                    req.getTemperatureMax(),
                    req.getRainfallPattern(),
                    req.getIsPeak(),
                    req.getDisplayOrder(),
                    req.getDescription(),
                    req.getStatus(),
                    req.getId()
            );

        } catch (Exception ex) {
            LOGGER.error("Error updating season: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to update season");
        }
    }

    @Override
    public void removeSeasonImages(Long seasonId,
                                   List<Long> imageIds,
                                   Long userId) {

        try {
            LOGGER.info("Removing season images for seasonId: {}", seasonId);

            Long terminatedStatusId = statusRepository.getStatusIdByName("TERMINATED");

            String sql = SeasonQueries.REMOVE_SEASON_IMAGES;

            jdbcTemplate.batchUpdate(sql, imageIds, imageIds.size(),
                    (ps, imageId) -> {
                        ps.setLong(1, terminatedStatusId);
                        ps.setLong(2, userId);
                        ps.setLong(3, imageId);
                        ps.setLong(4, seasonId);
                    });

        } catch (Exception ex) {
            LOGGER.error("Error removing season images: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to remove season images");
        }
    }

    @Override
    public void updateSeasonImages(Long seasonId,
                                   List<SeasonImageUpdateRequest> images,
                                   Long userId) {

        try {
            LOGGER.info("Updating season images for seasonId: {}", seasonId);

            String sql =
                    """
                    UPDATE seasons_images
                    SET 
                        name = ?,
                        description = ?,
                        image_url = ?,
                        status = ?,
                        updated_at = NOW(),
                        updated_by = ?
                    WHERE id = ? AND season_id = ?
                    """;

            jdbcTemplate.batchUpdate(sql, images, images.size(),
                    (ps, img) -> {

                        ps.setString(1, img.getName());
                        ps.setString(2, img.getDescription());
                        ps.setString(3, img.getImageUrl());
                        ps.setInt(4, img.getStatus());
                        ps.setLong(5, userId);
                        ps.setLong(6, img.getId());
                        ps.setLong(7, seasonId);
                    });

        } catch (Exception ex) {
            LOGGER.error("Error updating season images: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to update season images");
        }
    }

    @Override
    public void removeActivitiesSeasonIds(Long seasonId,
                                          List<Long> activityIds,
                                          Long userId) {

        try {
            LOGGER.info("Removing season from activities");

            jdbcTemplate.batchUpdate(
                    SeasonQueries.REMOVE_ACTIVITIES_SEASON,
                    activityIds,
                    activityIds.size(),
                    (ps, activityId) -> {
                        ps.setLong(1, userId);
                        ps.setLong(2, activityId);
                        ps.setLong(3, seasonId);
                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error removing season from activities: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to remove activities season mapping");
        }
    }

    @Override
    public void removeToursSeasonIds(Long seasonId,
                                     List<Long> tourIds,
                                     Long userId) {

        try {
            LOGGER.info("Removing season from tours");

            jdbcTemplate.batchUpdate(
                    SeasonQueries.REMOVE_TOURS_SEASON,
                    tourIds,
                    tourIds.size(),
                    (ps, tourId) -> {
                        ps.setLong(1, userId);
                        ps.setLong(2, tourId);
                        ps.setLong(3, seasonId);
                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error removing season from tours: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to remove tours season mapping");
        }
    }

    @Override
    public void terminateSeason(CommonIdRequest request) {

        try {
            LOGGER.info("Terminating season with id: {}", request.getId());

            Long terminatedStatusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.name());

            int updated = jdbcTemplate.update(
                    SeasonQueries.TERMINATE_SEASON,
                    terminatedStatusId,
                    request.getId()
            );

            if (updated == 0) {
                throw new DataAccessErrorExceptionHandler("Season not found for termination");
            }

        } catch (DataAccessException ex) {
            LOGGER.error("DB error while terminating season: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to terminate season");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while terminating season: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred");
        }
    }


}
