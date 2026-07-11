package com.felicita.repository.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.enums.HeroSectionTypes;
import com.felicita.model.request.common.IdWithTypeRequest;
import com.felicita.model.request.heroSection.*;
import com.felicita.model.response.*;
import com.felicita.model.response.heroSection.HeroSectionBasicResponse;
import com.felicita.model.response.heroSection.HeroSectionDataForParamsResponse;
import com.felicita.model.response.heroSection.HeroSectionDetailsResponse;
import com.felicita.queries.HeroSectionQueries;
import com.felicita.repository.HeroSectionRepository;
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
import java.util.List;

@Repository
public class HeroSectionRepositoryImpl implements HeroSectionRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeroSectionRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;

    @Autowired
    public HeroSectionRepositoryImpl(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<HeroSectionResponse> getAllHomeHeroSectionData() {
        String GET_ALL_HERO_SECTION = HeroSectionQueries.GET_ALL_HERO_SECTION_DATA;
        try {
            LOGGER.info("Executing query to fetch home hero section all data");

            List<HeroSectionResponse> results = jdbcTemplate.query(GET_ALL_HERO_SECTION, (rs, rowNum) -> {
                HeroSectionResponse hero = new HeroSectionResponse();

                hero.setImageId(rs.getInt("IMAGE_ID"));
                hero.setImageName(rs.getString("IMAGE_NAME"));
                hero.setImageUrl(rs.getString("IMAGE_URL"));
                hero.setImageTitle(rs.getString("IMAGE_TITLE"));
                hero.setImageSubTitle(rs.getString("IMAGE_SUB_TITLE"));
                hero.setImageDescription(rs.getString("IMAGE_DESCRIPTION"));
                hero.setImagePrimaryButtonText(rs.getString("IMAGE_PRIMARY_BUTTON_TEXT"));
                hero.setImagePrimaryButtonLink(rs.getString("IMAGE_PRIMARY_BUTTON_LINK"));
                hero.setImageSecondaryButtonText(rs.getString("IMAGE_SECONDARY_BUTTON_TEXT"));
                hero.setImageSecondaryButtonLink(rs.getString("IMAGE_SECONDARY_BUTTON_LINK"));
                hero.setImageStatus(rs.getString("IMAGE_STATUS"));
                hero.setImageOrder(rs.getInt("IMAGE_ORDER"));
                hero.setImageCreatedAt(rs.getTimestamp("IMAGE_CREATED_AT") != null ? rs.getTimestamp("IMAGE_CREATED_AT").toLocalDateTime() : null);
                hero.setImageCreatedBy(rs.getInt("IMAGE_CREATED_BY"));
                hero.setImageUpdatedAt(rs.getTimestamp("IMAGE_UPDATED_AT") != null ? rs.getTimestamp("IMAGE_UPDATED_AT").toLocalDateTime() : null);
                hero.setImageUpdatedBy(rs.getInt("IMAGE_UPDATED_BY"));
                hero.setImageTerminatedAt(rs.getTimestamp("IMAGE_TERMINATED_AT") != null ? rs.getTimestamp("IMAGE_TERMINATED_AT").toLocalDateTime() : null);
                hero.setImageTerminatedBy(rs.getInt("IMAGE_TERMINATED_BY"));
                return hero;
            });

            LOGGER.info("Successfully fetched {} home hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching home hero section data : {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch home hero section data from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching home hero section data : {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching home hero section data");
        }
    }

    @Override
    public List<AboutUsHeroSectionResponse> getAboutUsHeroSectionDetails() {
        String GET_ALL_ABOUT_US_HERO_SECTION_DATA = HeroSectionQueries.GET_ALL_ABOUT_US_HERO_SECTION_DATA;
        try {
            LOGGER.info("Executing query to fetch about us hero section data");

            List<AboutUsHeroSectionResponse> results = jdbcTemplate.query(GET_ALL_ABOUT_US_HERO_SECTION_DATA, (rs, rowNum) -> {
                AboutUsHeroSectionResponse hero = AboutUsHeroSectionResponse.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .imageUrl(rs.getString("image_url"))
                        .title(rs.getString("title"))
                        .subtitle(rs.getString("subtitle"))
                        .description(rs.getString("description"))
                        .primaryButtonText(rs.getString("primary_button_text"))
                        .primaryButtonLink(rs.getString("primary_button_link"))
                        .secondaryButtonText(rs.getString("secondary_button_text"))
                        .secondaryButtonLink(rs.getString("secondary_button_link"))
                        .order(rs.getInt("order"))
                        .createdAt(rs.getTimestamp("created_at") != null ?
                                rs.getTimestamp("created_at").toLocalDateTime() : null)
                        .updatedAt(rs.getTimestamp("updated_at") != null ?
                                rs.getTimestamp("updated_at").toLocalDateTime() : null)
                        .statusName(rs.getString("status_name")) // Note: This is from common_status table
                        .build();

                if (rs.wasNull()) {
                    hero.setOrder(null);
                }
                return hero;
            });

            LOGGER.info("Successfully fetched {} about us hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching about us hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch about us hero section data from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching about us hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching about us hero section data");
        }
    }

    @Override
    public List<ContactUsHeroSectionResponse> getContactUsHeroSectionDetails() {

        String GET_ALL_CONTACT_US_HERO_SECTION_DATA =
                HeroSectionQueries.GET_ALL_CONTACT_US_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch contact us hero section data");

            List<ContactUsHeroSectionResponse> results =
                    jdbcTemplate.query(GET_ALL_CONTACT_US_HERO_SECTION_DATA, (rs, rowNum) -> {
                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }
                        return ContactUsHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))
                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))
                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))
                                .order(order)
                                .statusName(rs.getString("status_name"))
                                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                                .build();
                    });

            LOGGER.info("Successfully fetched {} contact us hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching contact us hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch contact us hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching contact us hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching contact us hero section data"
            );
        }
    }

    @Override
    public List<BlogHeroSectionResponse> getBlogHeroSectionDetails() {

        String GET_ALL_BLOG_HERO_SECTION_DATA = HeroSectionQueries.GET_ALL_BLOG_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch blog hero section data");

            List<BlogHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_BLOG_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return BlogHeroSectionResponse.builder()
                                .id(rs.getInt("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))
                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))
                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))
                                .order(order)
                                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                                .statusName(rs.getString("status_name"))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} blog hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching blog hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch blog hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching blog hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching blog hero section data"
            );
        }
    }

    @Override
    public List<FaqHeroSectionResponse> getFAQHeroSectionDetails() {
        String GET_ALL_FAQ_HERO_SECTION_DATA = HeroSectionQueries.GET_ALL_FAQ_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch faq hero section data");

            List<FaqHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_FAQ_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return FaqHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))
                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))
                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))
                                .status(rs.getString("status"))
                                .order(order)
                                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                                .createdBy(rs.getObject("created_by", Integer.class))
                                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                                .updatedBy(rs.getObject("updated_by", Integer.class))
                                .terminatedAt(rs.getTimestamp("terminated_at") != null ? rs.getTimestamp("terminated_at").toLocalDateTime() : null)
                                .terminatedBy(rs.getObject("terminated_by", Integer.class))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} faq hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching faq hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch faq hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching faq hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching faq hero section data"
            );
        }
    }

    @Override
    public List<TourHeroSectionResponse> getTourHeroSectionDetails() {
        String GET_ALL_TOUR_HERO_SECTION_DATA = HeroSectionQueries.GET_ALL_TOUR_HERO_SECTION_DATA;
        try {
            LOGGER.info("Executing query to fetch tour hero section data");
            List<TourHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_TOUR_HERO_SECTION_DATA,
                    (rs, rowNum) -> {
                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }
                        return TourHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))
                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))
                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))
                                .status(rs.getString("status"))
                                .order(order)
                                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                                .createdBy(rs.getObject("created_by", Integer.class))
                                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                                .updatedBy(rs.getObject("updated_by", Integer.class))
                                .terminatedAt(rs.getTimestamp("terminated_at") != null ? rs.getTimestamp("terminated_at").toLocalDateTime() : null)
                                .terminatedBy(rs.getObject("terminated_by", Integer.class))
                                .build();
                    }
            );
            LOGGER.info("Successfully fetched {} tour hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching tour hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch tour hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching tour hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching tour hero section data"
            );
        }
    }

    @Override
    public List<ActivityHeroSectionResponse> getActivityHeroSectionDetails() {
        String GET_ALL_ACTIVITY_HERO_SECTION_DATA =
                HeroSectionQueries.GET_ALL_ACTIVITY_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch activity hero section data");

            List<ActivityHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_ACTIVITY_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return ActivityHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))

                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))

                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))

                                .status(rs.getString("status"))
                                .order(order)

                                .createdAt(rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null)
                                .createdBy(rs.getInt("created_by"))

                                .updatedAt(rs.getTimestamp("updated_at") != null
                                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                                        : null)
                                .updatedBy(rs.getInt("updated_by"))

                                .terminatedAt(rs.getTimestamp("terminated_at") != null
                                        ? rs.getTimestamp("terminated_at").toLocalDateTime()
                                        : null)
                                .terminatedBy(rs.getInt("terminated_by"))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} activity hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching activity hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activity hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching activity hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching activity hero section data"
            );
        }
    }

    @Override
    public List<DestinationHeroSectionResponse> getDestinationHeroSectionDetails() {
        String GET_ALL_DESTINATION_HERO_SECTION_DATA =
                HeroSectionQueries.GET_ALL_DESTINATION_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch destination hero section data");

            List<DestinationHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_DESTINATION_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return DestinationHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))

                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))

                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))

                                .status(rs.getString("status"))
                                .order(order)

                                .createdAt(rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null)
                                .createdBy(rs.getInt("created_by"))

                                .updatedAt(rs.getTimestamp("updated_at") != null
                                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                                        : null)
                                .updatedBy(rs.getInt("updated_by"))

                                .terminatedAt(rs.getTimestamp("terminated_at") != null
                                        ? rs.getTimestamp("terminated_at").toLocalDateTime()
                                        : null)
                                .terminatedBy(rs.getInt("terminated_by"))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} destination hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching destination hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch destination hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching destination hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching destination hero section data"
            );
        }
    }

    @Override
    public List<PackageHeroSectionResponse> getPackageHeroSectionDetails() {
        String GET_ALL_PACKAGE_HERO_SECTION_DATA = HeroSectionQueries.GET_ALL_PACKAGE_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch package hero section data");

            List<PackageHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_PACKAGE_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return PackageHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))

                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))

                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))

                                .status(rs.getString("status"))
                                .order(order)

                                .createdAt(rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null)
                                .createdBy(rs.getInt("created_by"))

                                .updatedAt(rs.getTimestamp("updated_at") != null
                                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                                        : null)
                                .updatedBy(rs.getInt("updated_by"))

                                .terminatedAt(rs.getTimestamp("terminated_at") != null
                                        ? rs.getTimestamp("terminated_at").toLocalDateTime()
                                        : null)
                                .terminatedBy(rs.getInt("terminated_by"))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} package hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch package hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching package hero section data"
            );
        }
    }

    @Override
    public List<PackageScheduleHeroSectionResponse> getPackageScheduleHeroSectionDetails(Long packageScheduleId) {
        String GET_ALL_PACKAGE_SCHEDULE_HERO_SECTION_DATA = HeroSectionQueries.GET_ALL_PACKAGE_SCHEDULE_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch package schedule hero section data");

            List<PackageScheduleHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_PACKAGE_SCHEDULE_HERO_SECTION_DATA,
                    new Object[]{packageScheduleId},
                    (rs, rowNum) -> PackageScheduleHeroSectionResponse.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .imageUrl(rs.getString("image_url"))
                            .color(rs.getString("color"))
                            .build()
            );
            return results;
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch package hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching package hero section data"
            );
        }
    }

    @Override
    public List<BookedTourHeroSectionResponse> getBookedTourHeroSectionDetails(Long bookingId) {
        String GET_BOOKED_TOUR_HERO_SECTION_DATA = HeroSectionQueries.GET_BOOKED_TOUR_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch booked tour hero section data");

            List<BookedTourHeroSectionResponse> results = jdbcTemplate.query(
                    GET_BOOKED_TOUR_HERO_SECTION_DATA,
                    new Object[]{bookingId},
                    (rs, rowNum) -> BookedTourHeroSectionResponse.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .imageUrl(rs.getString("image_url"))
                            .build()
            );
            return results;
        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching booked tour hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch booked tour hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching booked tour hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching booked tour hero section data"
            );
        }
    }

    @Override
    public List<ActivityDetailsHeroSectionResponse> getActivityHeroSectionDetailsByActivityId(Long activityId) {

        try {
            LOGGER.info(
                    "Executing query to fetch activity hero section data by activity id : {}",
                    activityId
            );

            List<ActivityDetailsHeroSectionResponse> results = jdbcTemplate.query(
                    HeroSectionQueries.GET_ACTIVITY_HERO_SECTION_DATA_BY_ACTIVITY_ID,
                    new Object[]{activityId},
                    (rs, rowNum) -> ActivityDetailsHeroSectionResponse.builder()
                            .activityId(rs.getLong("activity_id"))
                            .imageId(rs.getLong("image_id"))
                            .name(rs.getString("name"))
                            .imageUrl(rs.getString("image_url"))
                            .description(rs.getString("description"))
                            .status(rs.getString("status"))
                            .build()
            );

            LOGGER.info(
                    "Successfully fetched {} activity hero section data by activity id : {}.",
                    results.size(),
                    activityId
            );

            return results;

        } catch (DataAccessException ex) {
            LOGGER.error(
                    "Database error while fetching activity hero section data by activity id : {} , {}",
                    activityId, ex.getMessage(), ex
            );
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch activity hero section data by activity id : " + activityId
            );

        } catch (Exception ex) {
            LOGGER.error(
                    "Unexpected error while fetching activity hero section data by activity id : {} , {}",
                    activityId, ex.getMessage(), ex
            );
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching activity hero section data by activity id : " + activityId
            );
        }
    }

    @Override
    public List<VehicleHeroSectionResponse> getVehicleHeroSectionDetails() {
        String GET_ALL_VEHICLE_HERO_SECTION_DATA =
                HeroSectionQueries.GET_ALL_VEHICLE_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch vehicle hero section data");

            List<VehicleHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_VEHICLE_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return VehicleHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))

                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))

                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))

                                .status(rs.getString("status"))
                                .order(order)

                                .createdAt(rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null)
                                .createdBy(rs.getInt("created_by"))

                                .updatedAt(rs.getTimestamp("updated_at") != null
                                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                                        : null)
                                .updatedBy(rs.getInt("updated_by"))

                                .terminatedAt(rs.getTimestamp("terminated_at") != null
                                        ? rs.getTimestamp("terminated_at").toLocalDateTime()
                                        : null)
                                .terminatedBy(rs.getInt("terminated_by"))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} vehicle hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching vehicle hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch vehicle hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching vehicle hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching vehicle hero section data"
            );
        }
    }

    @Override
    public List<VehicleSpecificationHeroSectionResponse> getVehicleSpecificationHeroSectionDetails() {
        String GET_ALL_VEHICLE_SPECIFICATION_HERO_SECTION_DATA =
                HeroSectionQueries.GET_ALL_VEHICLE_SPECIFICATION_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch vehicle specification hero section data");

            List<VehicleSpecificationHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_VEHICLE_SPECIFICATION_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return VehicleSpecificationHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))

                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))

                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))

                                .status(rs.getString("status"))
                                .order(order)

                                .createdAt(rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null)
                                .createdBy(rs.getInt("created_by"))

                                .updatedAt(rs.getTimestamp("updated_at") != null
                                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                                        : null)
                                .updatedBy(rs.getInt("updated_by"))

                                .terminatedAt(rs.getTimestamp("terminated_at") != null
                                        ? rs.getTimestamp("terminated_at").toLocalDateTime()
                                        : null)
                                .terminatedBy(rs.getInt("terminated_by"))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} vehicle specification hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching vehicle specification hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch vehicle specification hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching vehicle specification hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching vehicle specification hero section data"
            );
        }
    }

    @Override
    public List<VehicleTypesHeroSectionResponse> getVehicleTypesHeroSectionDetails() {
        String GET_ALL_VEHICLE_TYPES_HERO_SECTION_DATA =
                HeroSectionQueries.GET_ALL_VEHICLE_TYPES_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch vehicle types hero section data");

            List<VehicleTypesHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_VEHICLE_TYPES_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return VehicleTypesHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))

                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))

                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))

                                .status(rs.getString("status"))
                                .order(order)

                                .createdAt(rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null)
                                .createdBy(rs.getInt("created_by"))

                                .updatedAt(rs.getTimestamp("updated_at") != null
                                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                                        : null)
                                .updatedBy(rs.getInt("updated_by"))

                                .terminatedAt(rs.getTimestamp("terminated_at") != null
                                        ? rs.getTimestamp("terminated_at").toLocalDateTime()
                                        : null)
                                .terminatedBy(rs.getInt("terminated_by"))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} vehicle types hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching vehicle types hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch vehicle types hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching vehicle types hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching vehicle types hero section data"
            );
        }
    }

    @Override
    public List<SeasonsHeroSectionResponse> getSeasonHeroSectionDetails() {
        String GET_ALL_SEASON_HERO_SECTION_DATA =
                HeroSectionQueries.GET_ALL_SEASON_HERO_SECTION_DATA;

        try {
            LOGGER.info("Executing query to fetch seasons hero section data");

            List<SeasonsHeroSectionResponse> results = jdbcTemplate.query(
                    GET_ALL_SEASON_HERO_SECTION_DATA,
                    (rs, rowNum) -> {

                        Integer order = rs.getInt("order");
                        if (rs.wasNull()) {
                            order = null;
                        }

                        return SeasonsHeroSectionResponse.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .imageUrl(rs.getString("image_url"))
                                .title(rs.getString("title"))
                                .subtitle(rs.getString("subtitle"))
                                .description(rs.getString("description"))

                                .primaryButtonText(rs.getString("primary_button_text"))
                                .primaryButtonLink(rs.getString("primary_button_link"))

                                .secondaryButtonText(rs.getString("secondary_button_text"))
                                .secondaryButtonLink(rs.getString("secondary_button_link"))

                                .status(rs.getString("status"))
                                .order(order)

                                .createdAt(rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null)
                                .createdBy(rs.getInt("created_by"))

                                .updatedAt(rs.getTimestamp("updated_at") != null
                                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                                        : null)
                                .updatedBy(rs.getInt("updated_by"))

                                .terminatedAt(rs.getTimestamp("terminated_at") != null
                                        ? rs.getTimestamp("terminated_at").toLocalDateTime()
                                        : null)
                                .terminatedBy(rs.getInt("terminated_by"))
                                .build();
                    }
            );

            LOGGER.info("Successfully fetched {} seasons hero section data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching seasons hero section data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler(
                    "Failed to fetch seasons hero section data from database"
            );

        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching seasons hero section data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching seasons hero section data"
            );
        }
    }

    @Override
    public List<HeroSectionBasicResponse> getHeroSectionBasicResponseForParms(HeroSectionDataRequest request) {

        try {

            String tableName = HeroSectionTypes.valueOf(request.getHeroSectionType())
                    .getTableName();

            StringBuilder sql = new StringBuilder(
                    String.format(HeroSectionQueries.GET_HERO_SECTION_BASIC_DETAILS, tableName));

            List<Object> params = new ArrayList<>();

            if (request.getName() != null && !request.getName().isBlank()) {
                sql.append(" AND hs.name LIKE ?");
                params.add("%" + request.getName() + "%");
            }

            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                sql.append(" AND hs.title LIKE ?");
                params.add("%" + request.getTitle() + "%");
            }

            if (request.getSubTitle() != null && !request.getSubTitle().isBlank()) {
                sql.append(" AND hs.subtitle LIKE ?");
                params.add("%" + request.getSubTitle() + "%");
            }

            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                sql.append(" AND hs.description LIKE ?");
                params.add("%" + request.getDescription() + "%");
            }

            if (request.getPrimaryButtonText() != null && !request.getPrimaryButtonText().isBlank()) {
                sql.append(" AND hs.primary_button_text LIKE ?");
                params.add("%" + request.getPrimaryButtonText() + "%");
            }

            if (request.getSecondaryButtonText() != null && !request.getSecondaryButtonText().isBlank()) {
                sql.append(" AND hs.secondary_button_text LIKE ?");
                params.add("%" + request.getSecondaryButtonText() + "%");
            }

            if (request.getStatus() != null && !request.getStatus().isBlank()) {
                sql.append(" AND cs.name = ?");
                params.add(request.getStatus());
            }

            sql.append(" ORDER BY ")
                    .append(request.getSortBy() == null ? "hs.id" : request.getSortBy())
                    .append(" ")
                    .append(request.getSortDirection() == null ? "DESC" : request.getSortDirection());

            sql.append(" LIMIT ? OFFSET ?");

            params.add(request.getPageSize());
            params.add((request.getPageNumber() - 1) * request.getPageSize());

            return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) ->
                    HeroSectionBasicResponse.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .imageUrl(rs.getString("image_url"))
                            .title(rs.getString("title"))
                            .subtitle(rs.getString("subtitle"))
                            .description(rs.getString("description"))
                            .primaryButtonText(rs.getString("primary_button_text"))
                            .primaryButtonLink(rs.getString("primary_button_link"))
                            .secondaryButtonText(rs.getString("secondary_button_text"))
                            .secondaryButtonLink(rs.getString("secondary_button_link"))
                            .statusId(rs.getLong("status_id"))
                            .status(rs.getString("status"))
                            .order(rs.getInt("order"))
                            .build());

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler("Failed to fetch hero sections.");
        }
    }

    @Override
    public Integer getHeroSectionBasicResponseCountForParms(HeroSectionDataRequest request) {

        try {

            String tableName = HeroSectionTypes.valueOf(request.getHeroSectionType())
                    .getTableName();

            StringBuilder sql = new StringBuilder(
                    String.format(HeroSectionQueries.GET_HERO_SECTION_BASIC_DETAILS_COUNT, tableName));

            List<Object> params = new ArrayList<>();

            if (request.getName() != null && !request.getName().isBlank()) {
                sql.append(" AND hs.name LIKE ?");
                params.add("%" + request.getName() + "%");
            }

            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                sql.append(" AND hs.title LIKE ?");
                params.add("%" + request.getTitle() + "%");
            }

            if (request.getSubTitle() != null && !request.getSubTitle().isBlank()) {
                sql.append(" AND hs.subtitle LIKE ?");
                params.add("%" + request.getSubTitle() + "%");
            }

            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                sql.append(" AND hs.description LIKE ?");
                params.add("%" + request.getDescription() + "%");
            }

            if (request.getPrimaryButtonText() != null && !request.getPrimaryButtonText().isBlank()) {
                sql.append(" AND hs.primary_button_text LIKE ?");
                params.add("%" + request.getPrimaryButtonText() + "%");
            }

            if (request.getSecondaryButtonText() != null && !request.getSecondaryButtonText().isBlank()) {
                sql.append(" AND hs.secondary_button_text LIKE ?");
                params.add("%" + request.getSecondaryButtonText() + "%");
            }

            if (request.getStatus() != null && !request.getStatus().isBlank()) {
                sql.append(" AND cs.name = ?");
                params.add(request.getStatus());
            }

            return jdbcTemplate.queryForObject(
                    sql.toString(),
                    params.toArray(),
                    Integer.class
            );

        } catch (DataAccessException ex) {
            throw new DataAccessErrorExceptionHandler("Failed to fetch hero section count.");
        }
    }

    @Override
    public HeroSectionDataForParamsResponse getDataForRequestParams(HeroSectionTypeRequest request) {

        try {

            String tableName = HeroSectionTypes.valueOf(request.getHeroSectionType())
                    .getTableName();

            String query = String.format(HeroSectionQueries.GET_HERO_SECTION_DATA_FOR_PARAMS, tableName);

            List<String> primaryButtonTexts = new ArrayList<>();
            List<String> secondaryButtonTexts = new ArrayList<>();

            jdbcTemplate.query(query, rs -> {

                String primary = rs.getString("PRIMARY_BUTTON_TEXT");
                if (primary != null && !primary.isBlank() && !primaryButtonTexts.contains(primary)) {
                    primaryButtonTexts.add(primary);
                }

                String secondary = rs.getString("SECONDARY_BUTTON_TEXT");
                if (secondary != null && !secondary.isBlank() && !secondaryButtonTexts.contains(secondary)) {
                    secondaryButtonTexts.add(secondary);
                }
            });

            return HeroSectionDataForParamsResponse.builder()
                    .primaryButtonText(primaryButtonTexts)
                    .secondaryButtonText(secondaryButtonTexts)
                    .build();

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching hero section request params.", ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch hero section request params.");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching hero section request params.", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching hero section request params.");
        }
    }

    @Override
    public HeroSectionDetailsResponse getHeroSectionDetailsById(HeroSectionDetailsDataRequest request) {

        try {

            String tableName = HeroSectionTypes.valueOf(request.getHeroSectionType())
                    .getTableName();

            String query = String.format(
                    HeroSectionQueries.GET_HERO_SECTION_DETAILS_BY_ID,
                    tableName
            );

            LOGGER.info("Executing query to fetch hero section details by id.");

            return jdbcTemplate.queryForObject(
                    query,
                    new Object[]{request.getHeroSectionId()},
                    (rs, rowNum) -> HeroSectionDetailsResponse.builder()
                            .id(rs.getLong("ID"))
                            .name(rs.getString("NAME"))
                            .imageUrl(rs.getString("IMAGE_URL"))
                            .title(rs.getString("TITLE"))
                            .subtitle(rs.getString("SUBTITLE"))
                            .description(rs.getString("DESCRIPTION"))
                            .primaryButtonText(rs.getString("PRIMARY_BUTTON_TEXT"))
                            .primaryButtonLink(rs.getString("PRIMARY_BUTTON_LINK"))
                            .secondaryButtonText(rs.getString("SECONDARY_BUTTON_TEXT"))
                            .secondaryButtonLink(rs.getString("SECONDARY_BUTTON_LINK"))
                            .statusId(rs.getLong("STATUS_ID"))
                            .status(rs.getString("STATUS"))
                            .order(rs.getInt("ORDER"))
                            .createdAt(rs.getTimestamp("CREATED_AT") != null
                                    ? rs.getTimestamp("CREATED_AT").toLocalDateTime()
                                    : null)
                            .createdBy(rs.getLong("CREATED_BY"))
                            .createdByUsername(rs.getString("CREATED_BY_USERNAME"))
                            .updatedAt(rs.getTimestamp("UPDATED_AT") != null
                                    ? rs.getTimestamp("UPDATED_AT").toLocalDateTime()
                                    : null)
                            .updatedBy(rs.getLong("UPDATED_BY"))
                            .updatedByUsername(rs.getString("UPDATED_BY_USERNAME"))
                            .terminatedAt(rs.getTimestamp("TERMINATED_AT") != null
                                    ? rs.getTimestamp("TERMINATED_AT").toLocalDateTime()
                                    : null)
                            .terminatedBy(rs.getObject("TERMINATED_BY") != null
                                    ? rs.getLong("TERMINATED_BY")
                                    : null)
                            .terminatedByUsername(rs.getString("TERMINATED_BY_USERNAME"))
                            .build());

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching hero section details.", ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch hero section details.");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching hero section details.", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching hero section details.");
        }
    }

    @Override
    public Long insertHeroSectionDetails(HeroSectionInsertRequest request, Long userId) {

        try {

            String tableName = HeroSectionTypes.valueOf(request.getHeroSectionType())
                    .getTableName();

            String query = String.format(HeroSectionQueries.INSERT_HERO_SECTION, tableName);

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {

                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, request.getName());
                ps.setString(2, request.getImageUrl());
                ps.setString(3, request.getTitle());
                ps.setString(4, request.getSubtitle());
                ps.setString(5, request.getDescription());
                ps.setString(6, request.getPrimaryButtonText());
                ps.setString(7, request.getPrimaryButtonLink());
                ps.setString(8, request.getSecondaryButtonText());
                ps.setString(9, request.getSecondaryButtonLink());
                ps.setLong(10, request.getStatusId());
                ps.setInt(11, request.getOrder());
                ps.setLong(12, userId);

                return ps;

            }, keyHolder);

            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().longValue();
            }

            throw new InternalServerErrorExceptionHandler("Failed to generate hero section id.");

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while inserting hero section.", ex);
            throw new DataAccessErrorExceptionHandler("Failed to insert hero section.");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while inserting hero section.", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while inserting hero section.");
        }
    }

    @Override
    public void updateBasicHeroSectionDetails(HeroSectionUpdateRequest request, Long userId) {

        try {

            String tableName = HeroSectionTypes.valueOf(request.getHeroSectionType())
                    .getTableName();

            String query = String.format(HeroSectionQueries.UPDATE_HERO_SECTION, tableName);

            int rows = jdbcTemplate.update(
                    query,
                    request.getName(),
                    request.getImageUrl(),
                    request.getTitle(),
                    request.getSubtitle(),
                    request.getDescription(),
                    request.getPrimaryButtonText(),
                    request.getPrimaryButtonLink(),
                    request.getSecondaryButtonText(),
                    request.getSecondaryButtonLink(),
                    request.getStatusId(),
                    request.getOrder(),
                    userId,
                    request.getHeroSectionId()
            );

            if (rows == 0) {
                throw new DataAccessErrorExceptionHandler("Hero section not found.");
            }

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while updating hero section.", ex);
            throw new DataAccessErrorExceptionHandler("Failed to update hero section.");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while updating hero section.", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while updating hero section.");
        }
    }

    @Override
    public void terminateHeroSection(IdWithTypeRequest request, Long userId) {

        try {
            String tableName = HeroSectionTypes.valueOf(request.getType())
                    .getTableName();
            String query = String.format(HeroSectionQueries.TERMINATE_HERO_SECTION, tableName);
            Long terminateStatus = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.toString());
            int rows = jdbcTemplate.update(
                    query,
                    terminateStatus,
                    userId,
                    request.getId()
            );

            if (rows == 0) {
                throw new DataAccessErrorExceptionHandler("Hero section not found.");
            }

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while terminating hero section.", ex);
            throw new DataAccessErrorExceptionHandler("Failed to terminate hero section.");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while terminating hero section.", ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while terminating hero section.");
        }
    }


}
