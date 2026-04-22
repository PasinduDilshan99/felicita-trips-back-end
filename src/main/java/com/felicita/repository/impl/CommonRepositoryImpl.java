package com.felicita.repository.impl;

import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.response.AllCategoriesResponse;
import com.felicita.model.response.TourForTerminateResponse;
import com.felicita.queries.CommonQueries;
import com.felicita.queries.TourQueries;
import com.felicita.repository.CommonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

}
