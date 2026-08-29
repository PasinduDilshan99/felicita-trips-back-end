package com.felicita.repository.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.response.PartnerResponse;
import com.felicita.model.response.ServiceProviderDetailsResponse;
import com.felicita.model.response.ServiceProviderIdsAndNamesReponse;
import com.felicita.model.response.TourForTerminateResponse;
import com.felicita.queries.PartnerQueries;
import com.felicita.queries.ServiceProviderQueries;
import com.felicita.queries.TourQueries;
import com.felicita.repository.ServiceProviderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ServiceProviderRepositoryImpl implements ServiceProviderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProviderRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceProviderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public ServiceProviderDetailsResponse.ServiceProviderDetails getServiceProviderDetails(String serviceProviderId) {
        String GET_SERVICE_PROVIDER_DETAILS = ServiceProviderQueries.GET_SERVICE_PROVIDER_DETAILS;
        try {
            LOGGER.info("Executing query to fetch service provider details for ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.ServiceProviderDetails> results = jdbcTemplate.query(
                    GET_SERVICE_PROVIDER_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> {
                        ServiceProviderDetailsResponse.ServiceProviderDetails details =
                                new ServiceProviderDetailsResponse.ServiceProviderDetails();

                        // --- Basic Info ---
                        details.setServiceProviderId(rs.getInt("service_provider_id"));
                        details.setUserId(rs.getInt("user_id"));
                        details.setServiceProviderTypeId(rs.getInt("service_provider_type_id"));
                        details.setName(rs.getString("name"));
                        details.setDescription(rs.getString("description"));
                        details.setAddress(rs.getString("address"));
                        details.setContactNumber(rs.getString("contact_number"));
                        details.setEmail(rs.getString("email"));
                        details.setWebsiteUrl(rs.getString("website_url"));
                        details.setCheckInTime(rs.getTime("check_in_time") != null
                                ? rs.getTime("check_in_time").toLocalTime() : null);
                        details.setCheckOutTime(rs.getTime("check_out_time") != null
                                ? rs.getTime("check_out_time").toLocalTime() : null);
                        details.setStarRating(rs.getInt("star_rating"));
                        details.setCurrencyId(rs.getInt("currency_id"));
                        details.setCancellationPolicy(rs.getString("cancellation_policy"));
                        details.setMinimumAdvanceBookingHours(rs.getObject("minimum_advance_booking_hours", Integer.class));
                        details.setEstablishmentYear(rs.getObject("establishment_year", Integer.class));
                        details.setTotalRooms(rs.getObject("total_rooms", Integer.class));
                        details.setTotalEmployees(rs.getObject("total_employees", Integer.class));
                        details.setAwardsCertifications(rs.getString("awards_certifications"));
                        details.setLanguagesSpoken(rs.getString("languages_spoken"));
                        details.setParkingFacility(rs.getObject("parking_facility", Boolean.class));
                        details.setParkingCapacity(rs.getObject("parking_capacity", Integer.class));
                        details.setWifiAvailable(rs.getObject("wifi_available", Boolean.class));
                        details.setPetFriendly(rs.getObject("pet_friendly", Boolean.class));
                        details.setCheckInInstructions(rs.getString("check_in_instructions"));
                        details.setSpecialInstructions(rs.getString("special_instructions"));
                        details.setApprovalStatusId(rs.getInt("approval_status_id"));
                        details.setStatusId(rs.getInt("status_id"));
                        details.setCreatedAt(rs.getTimestamp("created_at") != null
                                ? rs.getTimestamp("created_at").toLocalDateTime() : null);
                        details.setCreatedBy(rs.getObject("created_by", Integer.class));
                        details.setUpdatedAt(rs.getTimestamp("updated_at") != null
                                ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                        details.setUpdatedBy(rs.getObject("updated_by", Integer.class));
                        details.setTerminatedAt(rs.getTimestamp("terminated_at") != null
                                ? rs.getTimestamp("terminated_at").toLocalDateTime() : null);
                        details.setTerminatedBy(rs.getObject("terminated_by", Integer.class));

                        // --- Joined Fields ---
                        details.setProviderTypeName(rs.getString("provider_type_name"));
                        details.setCurrencyCode(rs.getString("currency_code"));
                        details.setCurrencySymbol(rs.getString("currency_symbol"));
                        details.setApprovalStatusName(rs.getString("approval_status_name"));
                        details.setStatusName(rs.getString("status_name"));
                        details.setCreatedByUsername(rs.getString("created_by_username"));

                        // --- Approval Info ---
                        details.setApprovalComment(rs.getString("approval_comment"));
                        details.setApprovedAt(rs.getTimestamp("approved_at") != null
                                ? rs.getTimestamp("approved_at").toLocalDateTime() : null);
                        details.setApprovedByUsername(rs.getString("approved_by_username"));

                        // --- Single Row Image ---
                        if (rs.getInt("image_id") != 0 && rs.getString("image_url") != null) {
                            ServiceProviderDetailsResponse.Image image = new ServiceProviderDetailsResponse.Image();
                            image.setImageId(rs.getInt("image_id"));
                            image.setImageUrl(rs.getString("image_url"));
                            image.setImageName(rs.getString("image_name"));
                            image.setImageDescription(rs.getString("image_description"));
                            image.setCaption(rs.getString("caption"));
                            image.setImageStatusId(rs.getInt("image_status_id"));
                            details.setImages(List.of(image));
                        }

                        // --- Single Row Operating Hour ---
                        if (rs.getInt("hours_id") != 0) {
                            ServiceProviderDetailsResponse.OperatingHour hour = new ServiceProviderDetailsResponse.OperatingHour();
                            hour.setHoursId(rs.getInt("hours_id"));
                            hour.setDayOfWeek(rs.getInt("day_of_week"));
                            hour.setOpensAt(rs.getTime("opens_at") != null
                                    ? rs.getTime("opens_at").toLocalTime() : null);
                            hour.setClosesAt(rs.getTime("closes_at") != null
                                    ? rs.getTime("closes_at").toLocalTime() : null);
                            hour.setIs24Hours(rs.getObject("is_24_hours", Boolean.class));
                            hour.setOperatingStatusId(rs.getInt("operating_status_id"));
                            hour.setHoursSpecialNote(rs.getString("hours_special_note"));
                            hour.setOperatingStatusName(rs.getString("operating_status_name"));
                            details.setOperatingHours(List.of(hour));
                        }

                        // --- Single Row Payment Method ---
                        if (rs.getInt("payment_method_id") != 0) {
                            ServiceProviderDetailsResponse.PaymentMethod payment = new ServiceProviderDetailsResponse.PaymentMethod();
                            payment.setPaymentMethodId(rs.getInt("payment_method_id"));
                            payment.setMethodType(rs.getString("method_type"));
                            payment.setMethodDetails(rs.getString("method_details"));
                            payment.setPaymentMethodAvailable(rs.getObject("payment_method_available", Boolean.class));
                            payment.setPaymentMethodStatusId(rs.getInt("payment_method_status_id"));
                            details.setPaymentMethods(List.of(payment));
                        }

                        return details;
                    }
            );

            if (results.isEmpty()) {
                LOGGER.warn("No service provider found with ID: {}", serviceProviderId);
                return null;
            }

            // Optionally handle merging if multiple rows due to joins
            // (if you expect multiple images, hours, etc. — can aggregate later)
            return results.get(0);

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching service provider details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch service provider details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching service provider details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching service provider details");
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.NearbyDestination> getNearByDestiantions(String serviceProviderId) {
        String GET_SERVICE_PROVIDER_NEAR_DESTINATIONS = ServiceProviderQueries.GET_SERVICE_PROVIDER_NEAR_DESTINATIONS;
        try {
            LOGGER.info("Executing query to fetch nearby destinations for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.NearbyDestination> results = jdbcTemplate.query(
                    GET_SERVICE_PROVIDER_NEAR_DESTINATIONS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> {
                        ServiceProviderDetailsResponse.NearbyDestination destination =
                                new ServiceProviderDetailsResponse.NearbyDestination();

                        destination.setDestinationId(rs.getInt("destination_id"));
                        destination.setName(rs.getString("name"));
                        destination.setDescription(rs.getString("description"));
                        destination.setLatitude(rs.getBigDecimal("latitude"));
                        destination.setLongitude(rs.getBigDecimal("longitude"));
                        destination.setLocation(rs.getString("location"));
//                        destination.setDestinationCategory(rs.getString("destination_category"));
                        destination.setStatusName(rs.getString("status_name"));
                        destination.setLinkedDate(rs.getTimestamp("linked_date") != null
                                ? rs.getTimestamp("linked_date").toLocalDateTime()
                                : null);

                        return destination;
                    }
            );

            LOGGER.info("Successfully fetched {} nearby destinations for service provider ID: {}", results.size(), serviceProviderId);
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching nearby destinations: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch nearby destinations from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching nearby destinations: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching nearby destinations");
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.MealDetail> getMealDetails(String serviceProviderId) {
        String GET_SERVICE_PROVIDER_MEAL_DETAILS = ServiceProviderQueries.GET_SERVICE_PROVIDER_MEAL_DETAILS;
        try {
            LOGGER.info("Executing query to fetch meal details for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.MealDetail> results = jdbcTemplate.query(
                    GET_SERVICE_PROVIDER_MEAL_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> {

                        ServiceProviderDetailsResponse.MealDetail meal = new ServiceProviderDetailsResponse.MealDetail();
                        meal.setMealId(rs.getInt("meal_id"));
                        meal.setServiceProviderId(rs.getInt("service_provider_id"));
                        meal.setMealTypeId(rs.getInt("meal_type_id"));
                        meal.setDescription(rs.getString("description"));
                        meal.setLocalPrice(rs.getBigDecimal("local_price"));
                        meal.setForeignPrice(rs.getBigDecimal("foreign_price"));
                        meal.setCurrencyId(rs.getInt("currency_id"));
                        meal.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
                        meal.setDiscountRequirements(rs.getString("discount_requirements"));
                        meal.setServesPeople(rs.getObject("serves_people", Integer.class));
                        meal.setCuisineType(rs.getString("cuisine_type"));
                        meal.setDietaryTags(rs.getString("dietary_tags"));
                        meal.setPreparationTime(rs.getObject("preparation_time", Integer.class));
                        meal.setIsChefSpecial(rs.getObject("is_chef_special", Boolean.class));
                        meal.setIsSpicy(rs.getObject("is_spicy", Boolean.class));
                        meal.setSpiceLevel(rs.getObject("spice_level", Integer.class));
                        meal.setServingSize(rs.getString("serving_size"));
                        meal.setCalories(rs.getObject("calories", Integer.class));
                        meal.setAllergens(rs.getString("allergens"));
                        meal.setAvailable(rs.getObject("available", Boolean.class));
                        meal.setCreatedAt(rs.getTimestamp("created_at") != null
                                ? rs.getTimestamp("created_at").toLocalDateTime() : null);
                        meal.setCreatedBy(rs.getObject("created_by", Integer.class));
                        meal.setUpdatedAt(rs.getTimestamp("updated_at") != null
                                ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                        meal.setUpdatedBy(rs.getObject("updated_by", Integer.class));
                        meal.setTerminatedAt(rs.getTimestamp("terminated_at") != null
                                ? rs.getTimestamp("terminated_at").toLocalDateTime() : null);
                        meal.setTerminatedBy(rs.getObject("terminated_by", Integer.class));

                        meal.setMealTypeName(rs.getString("meal_type_name"));
                        meal.setCurrencyCode(rs.getString("currency_code"));
                        meal.setStatusName(rs.getString("status_name"));

                        if (rs.getString("image_url") != null) {
                            ServiceProviderDetailsResponse.MealImage image =
                                    new ServiceProviderDetailsResponse.MealImage();
                            image.setImageUrl(rs.getString("image_url"));
                            image.setImageName(rs.getString("image_name"));
                            image.setImageDescription(rs.getString("image_description"));
                            image.setImageCaption(rs.getString("image_caption"));

                            meal.setImages(List.of(image));
                        } else {
                            meal.setImages(List.of());
                        }

                        return meal;
                    }
            );

            LOGGER.info("Successfully fetched {} meal details for service provider ID: {}", results.size(), serviceProviderId);
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching meal details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch meal details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching meal details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching meal details");
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.RoomDetail> getRoomDetails(String serviceProviderId) {
        String GET_SERVICE_PROVIDER_ROOM_DETAILS = ServiceProviderQueries.GET_SERVICE_PROVIDER_ROOM_DETAILS;

        try {
            LOGGER.info("Executing query to fetch room details for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.RoomDetail> rawResults = jdbcTemplate.query(
                    GET_SERVICE_PROVIDER_ROOM_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> {

                        ServiceProviderDetailsResponse.RoomDetail room = new ServiceProviderDetailsResponse.RoomDetail();
                        room.setRoomId(rs.getInt("room_id"));
                        room.setServiceProviderId(Integer.parseInt(serviceProviderId));
                        room.setRoomNumber(rs.getString("room_number"));
                        room.setRoomDescription(rs.getString("room_description"));
                        room.setCapacity(rs.getObject("capacity", Integer.class));
                        room.setRoomSize(rs.getBigDecimal("room_size"));
                        room.setBedType(rs.getString("bed_type"));
                        room.setLocalPricePerNight(rs.getBigDecimal("local_price_per_night"));
                        room.setForeignPricePerNight(rs.getBigDecimal("foreign_price_per_night"));
                        room.setRoomTypeName(rs.getString("room_type_name"));
                        room.setCurrencyCode(rs.getString("currency_code"));
                        room.setRoomStatus(rs.getString("room_status"));

                        // --- Room Feature ---
                        if (rs.getString("feature_name") != null) {
                            ServiceProviderDetailsResponse.RoomFeature feature = new ServiceProviderDetailsResponse.RoomFeature();
                            feature.setFeatureName(rs.getString("feature_name"));
                            feature.setFeatureValue(rs.getString("feature_value"));
                            feature.setFeatureDescription(rs.getString("feature_description"));
                            room.setFeatures(List.of(feature));
                        } else {
                            room.setFeatures(List.of());
                        }

                        // --- Room Amenity ---
                        if (rs.getString("amenity_name") != null) {
                            ServiceProviderDetailsResponse.RoomAmenity amenity = new ServiceProviderDetailsResponse.RoomAmenity();
                            amenity.setAmenityName(rs.getString("amenity_name"));
                            amenity.setAmenityCategory(rs.getString("amenity_category"));
                            amenity.setIconClass(rs.getString("icon_class"));
                            amenity.setAmenityNotes(rs.getString("amenity_notes"));
                            room.setAmenities(List.of(amenity));
                        } else {
                            room.setAmenities(List.of());
                        }

                        // --- Room Availability ---
                        if (rs.getDate("availability_date") != null) {
                            ServiceProviderDetailsResponse.RoomAvailability availability =
                                    new ServiceProviderDetailsResponse.RoomAvailability();
                            availability.setAvailabilityDate(rs.getDate("availability_date").toLocalDate());
                            availability.setAvailableRooms(rs.getObject("available_rooms", Integer.class));
                            availability.setBookedRooms(rs.getObject("booked_rooms", Integer.class));
                            availability.setLocalPriceForDate(rs.getBigDecimal("local_price_for_date"));
                            availability.setForeignPriceForDate(rs.getBigDecimal("foreign_price_for_date"));
                            room.setAvailability(List.of(availability));
                        } else {
                            room.setAvailability(List.of());
                        }

                        // --- Room Image ---
                        if (rs.getString("room_image_url") != null) {
                            ServiceProviderDetailsResponse.RoomImage image =
                                    new ServiceProviderDetailsResponse.RoomImage();
                            image.setRoomImageUrl(rs.getString("room_image_url"));
                            image.setRoomImageName(rs.getString("room_image_name"));
                            image.setRoomImageCaption(rs.getString("room_image_caption"));
                            room.setImages(List.of(image));
                        } else {
                            room.setImages(List.of());
                        }

                        return room;
                    }
            );

            // --- Post-process to merge data for rooms that appear in multiple rows ---
            Map<Integer, ServiceProviderDetailsResponse.RoomDetail> roomMap = new LinkedHashMap<>();

            for (ServiceProviderDetailsResponse.RoomDetail r : rawResults) {
                roomMap.compute(r.getRoomId(), (id, existing) -> {
                    if (existing == null) return r;

                    // Merge features
                    List<ServiceProviderDetailsResponse.RoomFeature> mergedFeatures = new ArrayList<>(existing.getFeatures());
                    mergedFeatures.addAll(r.getFeatures().stream()
                            .filter(f -> f.getFeatureName() != null)
                            .toList());
                    existing.setFeatures(mergedFeatures);

                    // Merge amenities
                    List<ServiceProviderDetailsResponse.RoomAmenity> mergedAmenities = new ArrayList<>(existing.getAmenities());
                    mergedAmenities.addAll(r.getAmenities().stream()
                            .filter(a -> a.getAmenityName() != null)
                            .toList());
                    existing.setAmenities(mergedAmenities);

                    // Merge availability
                    List<ServiceProviderDetailsResponse.RoomAvailability> mergedAvailability = new ArrayList<>(existing.getAvailability());
                    mergedAvailability.addAll(r.getAvailability().stream()
                            .filter(av -> av.getAvailabilityDate() != null)
                            .toList());
                    existing.setAvailability(mergedAvailability);

                    // Merge images
                    List<ServiceProviderDetailsResponse.RoomImage> mergedImages = new ArrayList<>(existing.getImages());
                    mergedImages.addAll(r.getImages().stream()
                            .filter(img -> img.getRoomImageUrl() != null)
                            .toList());
                    existing.setImages(mergedImages);

                    return existing;
                });
            }

            List<ServiceProviderDetailsResponse.RoomDetail> mergedResults = new ArrayList<>(roomMap.values());
            LOGGER.info("Successfully fetched {} room details for service provider ID: {}", mergedResults.size(), serviceProviderId);
            return mergedResults;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching room details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch room details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching room details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching room details");
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.PackageDetail> getPackageDetails(String serviceProviderId) {
        String GET_SERVICE_PROVIDER_PACKAGE_DETAILS = ServiceProviderQueries.GET_SERVICE_PROVIDER_PACKAGE_DETAILS;

        try {
            LOGGER.info("Executing query to fetch package details for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.PackageDetail> rawResults = jdbcTemplate.query(
                    GET_SERVICE_PROVIDER_PACKAGE_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> {

                        ServiceProviderDetailsResponse.PackageDetail pkg = new ServiceProviderDetailsResponse.PackageDetail();
                        pkg.setServiceProviderPackageId(rs.getInt("service_provider_package_id"));
                        pkg.setServiceProviderId(Integer.parseInt(serviceProviderId));
                        pkg.setPackageName(rs.getString("package_name"));
                        pkg.setPackageDescription(rs.getString("package_description"));
                        pkg.setLocalPrice(rs.getBigDecimal("local_price"));
                        pkg.setForeignPrice(rs.getBigDecimal("foreign_price"));
                        pkg.setDurationDays(rs.getObject("duration_days", Integer.class));
                        pkg.setMinPersons(rs.getObject("min_persons", Integer.class));
                        pkg.setMaxPersons(rs.getObject("max_persons", Integer.class));
                        pkg.setCurrencyCode(rs.getString("currency_code"));
                        pkg.setPackageStatus(rs.getString("package_status"));

                        // --- Package Feature ---
                        if (rs.getString("feature_name") != null) {
                            ServiceProviderDetailsResponse.PackageFeature feature = new ServiceProviderDetailsResponse.PackageFeature();
                            feature.setFeatureName(rs.getString("feature_name"));
                            feature.setFeatureValue(rs.getString("feature_value"));
                            feature.setFeatureDescription(rs.getString("feature_description"));
                            pkg.setFeatures(List.of(feature));
                        } else {
                            pkg.setFeatures(List.of());
                        }

                        // --- Package Inclusion ---
                        if (rs.getString("inclusion_name") != null) {
                            ServiceProviderDetailsResponse.PackageInclusion inclusion = new ServiceProviderDetailsResponse.PackageInclusion();
                            inclusion.setInclusionName(rs.getString("inclusion_name"));
                            inclusion.setInclusionDescription(rs.getString("inclusion_description"));
                            pkg.setInclusions(List.of(inclusion));
                        } else {
                            pkg.setInclusions(List.of());
                        }

                        // --- Package Image ---
                        if (rs.getString("package_image_url") != null) {
                            ServiceProviderDetailsResponse.PackageImage image = new ServiceProviderDetailsResponse.PackageImage();
                            image.setPackageImageUrl(rs.getString("package_image_url"));
                            image.setPackageImageName(rs.getString("package_image_name"));
                            image.setPackageImageCaption(rs.getString("package_image_caption"));
                            pkg.setImages(List.of(image));
                        } else {
                            pkg.setImages(List.of());
                        }

                        return pkg;
                    }
            );

            // --- Post-process to merge multiple rows per package ---
            Map<Integer, ServiceProviderDetailsResponse.PackageDetail> packageMap = new LinkedHashMap<>();

            for (ServiceProviderDetailsResponse.PackageDetail p : rawResults) {
                packageMap.compute(p.getServiceProviderId(), (id, existing) -> {
                    if (existing == null) return p;

                    // Merge features
                    List<ServiceProviderDetailsResponse.PackageFeature> mergedFeatures = new ArrayList<>(existing.getFeatures());
                    mergedFeatures.addAll(p.getFeatures().stream()
                            .filter(f -> f.getFeatureName() != null)
                            .toList());
                    existing.setFeatures(mergedFeatures);

                    // Merge inclusions
                    List<ServiceProviderDetailsResponse.PackageInclusion> mergedInclusions = new ArrayList<>(existing.getInclusions());
                    mergedInclusions.addAll(p.getInclusions().stream()
                            .filter(i -> i.getInclusionName() != null)
                            .toList());
                    existing.setInclusions(mergedInclusions);

                    // Merge images
                    List<ServiceProviderDetailsResponse.PackageImage> mergedImages = new ArrayList<>(existing.getImages());
                    mergedImages.addAll(p.getImages().stream()
                            .filter(img -> img.getPackageImageUrl() != null)
                            .toList());
                    existing.setImages(mergedImages);

                    return existing;
                });
            }

            List<ServiceProviderDetailsResponse.PackageDetail> mergedResults = new ArrayList<>(packageMap.values());
            LOGGER.info("Successfully fetched {} package details for service provider ID: {}", mergedResults.size(), serviceProviderId);
            return mergedResults;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching package details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch package details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching package details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching package details");
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.AmenityDetail> getAmenityDetails(String serviceProviderId) {
        String GET_SERVICE_PROVIDER_AMENITY_DETAILS = ServiceProviderQueries.GET_SERVICE_PROVIDER_AMENITY_DETAILS;

        try {
            LOGGER.info("Executing query to fetch amenity details for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.AmenityDetail> results = jdbcTemplate.query(
                    GET_SERVICE_PROVIDER_AMENITY_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> {
                        ServiceProviderDetailsResponse.AmenityDetail amenity = new ServiceProviderDetailsResponse.AmenityDetail();
                        amenity.setProviderAmenityId(rs.getInt("provider_amenity_id"));
                        amenity.setAmenityName(rs.getString("amenity_name"));
                        amenity.setAmenityDescription(rs.getString("amenity_description"));
                        amenity.setLocalAdditionalCharge(rs.getBigDecimal("local_additional_charge"));
                        amenity.setForeignAdditionalCharge(rs.getBigDecimal("foreign_additional_charge"));
                        amenity.setIsAvailable(rs.getBoolean("is_available"));
                        amenity.setAmenityTypeName(rs.getString("amenity_type_name"));
                        amenity.setAmenityCategory(rs.getString("amenity_category"));
                        amenity.setIconClass(rs.getString("icon_class"));
                        amenity.setCurrencyCode(rs.getString("currency_code"));
                        amenity.setStatusName(rs.getString("status_name"));
                        return amenity;
                    }
            );

            LOGGER.info("Successfully fetched {} amenity details for service provider ID: {}", results.size(), serviceProviderId);
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching amenity details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch amenity details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching amenity details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching amenity details");
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.FacilityDetail> getFacilityDetails(String serviceProviderId) {
        String GET_SERVICE_PROVIDER_FACILITY_DETAILS = ServiceProviderQueries.GET_SERVICE_PROVIDER_FACILITY_DETAILS;

        try {
            LOGGER.info("Executing query to fetch facility details for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.FacilityDetail> results = jdbcTemplate.query(
                    GET_SERVICE_PROVIDER_FACILITY_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> {
                        ServiceProviderDetailsResponse.FacilityDetail facility = new ServiceProviderDetailsResponse.FacilityDetail();
                        facility.setFacilityId(rs.getInt("facility_id"));
                        facility.setFacilityName(rs.getString("facility_name"));
                        facility.setFacilityDescription(rs.getString("facility_description"));
                        facility.setIsAvailable(rs.getBoolean("is_available"));
                        facility.setSpecialNote(rs.getString("special_note"));

                        // Map facility images
                        ServiceProviderDetailsResponse.FacilityImage image = new ServiceProviderDetailsResponse.FacilityImage();
                        image.setImageUrl(rs.getString("image_url"));
                        image.setImageName(rs.getString("image_name"));
                        image.setImageDescription(rs.getString("image_description"));
                        image.setImageCaption(rs.getString("image_caption"));

                        // Wrap single image in list if exists
                        if (image.getImageUrl() != null || image.getImageName() != null) {
                            facility.setImages(List.of(image));
                        } else {
                            facility.setImages(List.of());
                        }

                        return facility;
                    }
            );

            LOGGER.info("Successfully fetched {} facility details for service provider ID: {}", results.size(), serviceProviderId);
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching facility details: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch facility details from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching facility details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching facility details");
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.SeasonalPricing> getSeasonalPricings(String serviceProviderId) {
        try {
            LOGGER.info("Fetching seasonal pricing details for service provider ID: {}", serviceProviderId);

            return jdbcTemplate.query(
                    ServiceProviderQueries.GET_SERVICE_PROVIDER_SEASONAL_PRICE_DETAILS,
                    ps -> ps.setString(1, serviceProviderId),
                    (rs, rowNum) -> ServiceProviderDetailsResponse.SeasonalPricing.builder()
                            .seasonalPriceId(rs.getInt("seasonal_price_id"))
                            .seasonName(rs.getString("season_name"))
                            .startDate(rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null)
                            .endDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null)
                            .localMultiplier(rs.getBigDecimal("local_multiplier"))
                            .foreignMultiplier(rs.getBigDecimal("foreign_multiplier"))
                            .description(rs.getString("description"))
                            .requirements(rs.getString("requirements"))
                            .specialNote(rs.getString("special_note"))
                            .statusName(rs.getString("status_name"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching seasonal pricing details for service provider ID: {}", serviceProviderId, e);
            return List.of();
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.ContactPerson> getContactPersons(String serviceProviderId) {
        try {
            LOGGER.info("Fetching contact person details for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.ContactPerson> contactPersons = jdbcTemplate.query(
                    ServiceProviderQueries.GET_SERVICE_PROVIDER_CONTACT_PERSON_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> ServiceProviderDetailsResponse.ContactPerson.builder()
                            .contactPersonId(rs.getInt("contact_person_id"))
                            .fullName(rs.getString("full_name"))
                            .designation(rs.getString("designation"))
                            .email(rs.getString("email"))
                            .phone(rs.getString("phone"))
                            .mobile(rs.getString("mobile"))
                            .isPrimary(rs.getBoolean("is_primary"))
                            .statusName(rs.getString("status_name"))
                            .build()
            );

            LOGGER.info("Successfully retrieved {} contact person(s) for service provider {}", contactPersons.size(), serviceProviderId);
            return contactPersons;

        } catch (Exception e) {
            LOGGER.error("Error fetching contact person details for service provider ID: {}", serviceProviderId, e);
            return List.of();
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.BankDetail> getBankDetails(String serviceProviderId) {
        try {
            LOGGER.info("Fetching bank details for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.BankDetail> bankDetails = jdbcTemplate.query(
                    ServiceProviderQueries.GET_SERVICE_PROVIDER_BANK_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> ServiceProviderDetailsResponse.BankDetail.builder()
                            .bankId(rs.getInt("bank_id"))
                            .bankName(rs.getString("bank_name"))
                            .accountHolderName(rs.getString("account_holder_name"))
                            .accountNumber(rs.getString("account_number"))
                            .branchName(rs.getString("branch_name"))
                            .branchCode(rs.getString("branch_code"))
                            .swiftCode(rs.getString("swift_code"))
                            .iban(rs.getString("iban"))
                            .isPrimary(rs.getBoolean("is_primary"))
                            .currencyCode(rs.getString("currency_code"))
                            .statusName(rs.getString("status_name"))
                            .build()
            );

            LOGGER.info("Successfully retrieved {} bank detail(s) for service provider {}", bankDetails.size(), serviceProviderId);
            return bankDetails;

        } catch (Exception e) {
            LOGGER.error("Error fetching bank details for service provider ID: {}", serviceProviderId, e);
            return List.of();
        }
    }


    @Override
    public ServiceProviderDetailsResponse.TaxAndCommissionDetails getTaxAndCommissionDetails(String serviceProviderId) {
        String query = ServiceProviderQueries.GET_SERVICE_PROVIDER_TAX_DETAILS;

        List<ServiceProviderDetailsResponse.TaxAndCommissionDetails> results = jdbcTemplate.query(
                query,
                new Object[]{serviceProviderId},
                (rs, rowNum) -> ServiceProviderDetailsResponse.TaxAndCommissionDetails.builder()
                        .taxId(rs.getInt("tax_id"))
                        .taxName(rs.getString("tax_name"))
                        .taxPercentage(rs.getBigDecimal("tax_percentage"))
                        .taxNumber(rs.getString("tax_number"))
                        .taxActive(rs.getBoolean("tax_active"))
                        .appliesToRooms(rs.getBoolean("applies_to_rooms"))
                        .appliesToMeals(rs.getBoolean("applies_to_meals"))
                        .appliesToPackages(rs.getBoolean("applies_to_packages"))
                        .appliesToAmenities(rs.getBoolean("applies_to_amenities"))
                        .taxStatus(rs.getString("tax_status"))
                        .commissionId(rs.getInt("commission_id"))
                        .commissionPercentage(rs.getBigDecimal("commission_percentage"))
                        .commissionAppliesRooms(rs.getBoolean("commission_applies_rooms"))
                        .commissionAppliesMeals(rs.getBoolean("commission_applies_meals"))
                        .commissionAppliesPackages(rs.getBoolean("commission_applies_packages"))
                        .minimumCommissionAmount(rs.getBigDecimal("minimum_commission_amount"))
                        .commissionStatus(rs.getString("commission_status"))
                        .build()
        );

        // Return first record safely
        return results.isEmpty() ? null : results.get(0);
    }



    @Override
    public List<ServiceProviderDetailsResponse.BookingRestriction> getBookingRestrictions(String serviceProviderId) {
        LOGGER.info("Executing query to fetch booking restrictions for service provider ID: {}", serviceProviderId);

        try {
            String query = ServiceProviderQueries.GET_SERVICE_PROVIDER_BOOKING_RESTRICTIONS_DETAILS;

            List<ServiceProviderDetailsResponse.BookingRestriction> restrictions = jdbcTemplate.query(
                    query,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> ServiceProviderDetailsResponse.BookingRestriction.builder()
                            .restrictionId(rs.getInt("restriction_id"))
                            .restrictionType(rs.getString("restriction_type"))
                            .minStayNights(rs.getObject("min_stay_nights", Integer.class))
                            .maxStayNights(rs.getObject("max_stay_nights", Integer.class))
                            .startDate(rs.getObject("start_date", java.time.LocalDate.class))
                            .endDate(rs.getObject("end_date", java.time.LocalDate.class))
                            .description(rs.getString("description"))
                            .statusName(rs.getString("status_name"))
                            .build()
            );

            LOGGER.info("Successfully fetched {} booking restrictions for service provider ID: {}",
                    restrictions.size(), serviceProviderId);
            return restrictions;
        } catch (Exception ex) {
            LOGGER.error("Error while fetching booking restrictions for service provider ID: {}", serviceProviderId, ex);
            return List.of();
        }
    }


    @Override
    public ServiceProviderDetailsResponse.Statistics getStatistics(String serviceProviderId) {
        try {
            LOGGER.info("Fetching statistics for service provider ID: {}", serviceProviderId);

            return jdbcTemplate.queryForObject(
                    ServiceProviderQueries.GET_SERVICE_PROVIDER_STATISTICS_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> ServiceProviderDetailsResponse.Statistics.builder()
                            .statsId(rs.getInt("stats_id"))
                            .totalBookings(rs.getInt("total_bookings"))
                            .totalRevenue(rs.getBigDecimal("total_revenue"))
                            .averageRating(rs.getBigDecimal("average_rating"))
                            .totalReviews(rs.getInt("total_reviews"))
                            .occupancyRate(rs.getBigDecimal("occupancy_rate"))
                            .lastUpdated(rs.getTimestamp("last_updated") != null
                                    ? rs.getTimestamp("last_updated").toLocalDateTime()
                                    : null)
                            .build()
            );

        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("No statistics found for service provider ID: {}", serviceProviderId);
            return null;
        } catch (Exception e) {
            LOGGER.error("Error fetching statistics for service provider ID: {}", serviceProviderId, e);
            throw new RuntimeException("Failed to fetch service provider statistics", e);
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.SocialMedia> getSocialMedias(String serviceProviderId) {
        try {
            LOGGER.info("Fetching social media details for service provider ID: {}", serviceProviderId);

            return jdbcTemplate.query(
                    ServiceProviderQueries.GET_SERVICE_PROVIDER_SOCIAL_MEDIA_DETAILS,
                    new Object[]{serviceProviderId},
                    (rs, rowNum) -> ServiceProviderDetailsResponse.SocialMedia.builder()
                            .socialId(rs.getInt("social_id"))
                            .platform(rs.getString("platform"))
                            .profileUrl(rs.getString("profile_url"))
                            .verificationStatusName(rs.getString("verification_status_name"))
                            .statusName(rs.getString("status_name"))
                            .build()
            );

        } catch (DataAccessException e) {
            LOGGER.error("Error fetching social media details for service provider ID: {}", serviceProviderId, e);
            return Collections.emptyList();
        }
    }


    @Override
    public List<ServiceProviderDetailsResponse.ReviewDetail> getReviewDetails(String serviceProviderId) {
        try {
            LOGGER.info("Fetching review details for service provider ID: {}", serviceProviderId);

            List<ServiceProviderDetailsResponse.ReviewDetail> reviewDetails = new ArrayList<>();

            Map<Integer, ServiceProviderDetailsResponse.ReviewDetail> reviewMap = new LinkedHashMap<>();

            jdbcTemplate.query(
                    ServiceProviderQueries.GET_SERVICE_PROVIDER_REVIEW_DETAILS,
                    new Object[]{serviceProviderId},
                    rs -> {
                        int reviewId = rs.getInt("review_id");

                        // Check if review already exists
                        ServiceProviderDetailsResponse.ReviewDetail review = reviewMap.get(reviewId);
                        if (review == null) {
                            review = ServiceProviderDetailsResponse.ReviewDetail.builder()
                                    .reviewId(reviewId)
                                    .overallRating(rs.getInt("overall_rating"))
                                    .title(rs.getString("title"))
                                    .comment(rs.getString("comment"))
                                    .reviewDate(rs.getTimestamp("review_date") != null
                                            ? rs.getTimestamp("review_date").toLocalDateTime()
                                            : null)
                                    .isApproved(rs.getBoolean("is_approved"))
                                    .username(rs.getString("username"))
                                    .firstName(rs.getString("first_name"))
                                    .lastName(rs.getString("last_name"))
                                    .reviewStatus(rs.getString("review_status"))
                                    .ratingCategories(new ArrayList<>())
                                    .build();

                            reviewMap.put(reviewId, review);
                        }

                        // Add rating category details if available
                        Integer categoryRating = rs.getObject("category_rating") != null
                                ? rs.getInt("category_rating")
                                : null;
                        String categoryName = rs.getString("category_name");
                        String categoryDescription = rs.getString("category_description");

                        if (categoryName != null) {
                            ServiceProviderDetailsResponse.RatingCategory ratingCategory =
                                    ServiceProviderDetailsResponse.RatingCategory.builder()
                                            .categoryRating(categoryRating)
                                            .categoryName(categoryName)
                                            .categoryDescription(categoryDescription)
                                            .build();
                            review.getRatingCategories().add(ratingCategory);
                        }
                    });

            reviewDetails.addAll(reviewMap.values());
            LOGGER.info("Successfully fetched {} review details for service provider ID: {}", reviewDetails.size(), serviceProviderId);
            return reviewDetails;

        } catch (DataAccessException e) {
            LOGGER.error("Error fetching review details for service provider ID: {}", serviceProviderId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ServiceProviderIdsAndNamesReponse> getServiceProviderNamesAndIds() {
        String GET_SERVICE_PROVIDER_NAMES_AND_IDS = ServiceProviderQueries.GET_SERVICE_PROVIDER_NAMES_AND_IDS;

        try {
            return jdbcTemplate.query(
                    GET_SERVICE_PROVIDER_NAMES_AND_IDS,
                    new Object[]{CommonStatus.ACTIVE.toString()}, // parameter for cs.name = ?
                    (rs, rowNum) -> ServiceProviderIdsAndNamesReponse.builder()
                            .serviceProviderId(rs.getLong("service_provider_id"))
                            .serviceProviderName(rs.getString("name"))
                            .build()
            );
        } catch (DataAccessException e) {
            LOGGER.error("Failed to fetch service provider: ", e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch service provider");
        }
    }

}
