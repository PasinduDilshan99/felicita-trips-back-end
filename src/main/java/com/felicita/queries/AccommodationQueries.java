package com.felicita.queries;

public class AccommodationQueries {
    public static final String GET_ALL_ACCOMMODATION_DATA = """
            SELECT
            	ac.id AS ACCOMMADATION_ID,
                ac.title AS ACCOMMADATION_TITLE,
                ac.sub_title AS ACCOMMADATION_SUB_TITLE,
                ac.description AS ACCOMMADATION_DESCRIPTION,
                ac.icon_url AS ACCOMMADATION_ICON_URL,
                ac.image_url AS ACCOMMADATION_IMAGE_URL,
                ac.color AS ACCOMMADATION_COLOR,
                ac.hover_color AS ACCOMMADATION_HOVER_COLOR,
                ac.link_url AS ACCOMMADATION_LINK_URL,
                acs.name AS ACCOMMADATION_STATUS,
                cs.name AS ACCOMMADATION_STATUS_STATUS,
            	ac.created_at AS ACCOMMADATION_CREATED_AT,
                ac.created_by AS ACCOMMADATION_CREATED_BY,
                ac.updated_at AS ACCOMMADATION_UPDATED_AT,
                ac.updated_by AS ACCOMMADATION_UPDATED_BY,
                ac.terminated_at AS ACCOMMADATION_TERMINATED_AT,
                ac.terminated_by AS ACCOMMADATION_TERMINATED_BY
            FROM accommodation ac
            LEFT JOIN accommodation_status acs
            ON ac.status_id = acs.id
            LEFT JOIN common_status cs
            ON acs.common_status_id = cs.id
            """;

    public static final String GET_HOTELS_DETAILS_FOR_SECTION = """
            SELECT
                sp.service_provider_id AS hotel_id,
                sp.name AS hotel_name,
                sp.description AS hotel_description,
                sp.address,
                sp.contact_number,
                sp.email,
                sp.website_url,
                sp.check_in_time,
                sp.check_out_time,
                sp.star_rating,
                c.currency_code,
                sp.cancellation_policy,
                sp.total_rooms,
                sp.parking_facility,
                sp.wifi_available,
                sp.pet_friendly,
                spt.name AS hotel_type,
                MAX(spl.latitude) AS latitude,
                MAX(spl.longitude) AS longitude,
                MAX(spl.google_place_id) AS google_place_id,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'image_url', spi.image_url,
                            'caption', spi.caption
                        )
                    )
                    FROM service_provider_image spi
                    WHERE spi.service_provider_id = sp.service_provider_id
                    AND spi.status_id = 1
                ) AS hotel_images,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'meal_type', mt.name,
                            'meal_description', spm.description,
                            'local_price', spm.local_price,
                            'cuisine_type', spm.cuisine_type,
                            'available', spm.available
                        )
                    )
                    FROM service_provider_meal spm
                    LEFT JOIN meal_type mt ON spm.meal_type_id = mt.meal_type_id
                    WHERE spm.service_provider_id = sp.service_provider_id
                    AND spm.available = TRUE
                ) AS meals,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'room_type', srt.name,
                            'room_description', spr.description,
                            'capacity', spr.capacity,
                            'bed_type', spr.bed_type,
                            'local_price_per_night', spr.local_price_per_night,
                            'has_air_conditioning', spr.has_air_conditioning,
                            'has_tv', spr.has_tv,
                            'internet_access', spr.internet_access
                        )
                    )
                    FROM service_provider_room spr
                    LEFT JOIN service_provider_room_type srt ON spr.room_type_id = srt.room_type_id
                    WHERE spr.service_provider_id = sp.service_provider_id
                    AND spr.status_id = 1
                ) AS rooms,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'date', spra.date,
                            'available_rooms', spra.available_rooms
                        )
                    )
                    FROM service_provider_room_availability spra
                    WHERE spra.room_id IN (
                        SELECT room_id FROM service_provider_room
                        WHERE service_provider_id = sp.service_provider_id
                    )
                    AND spra.date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
                ) AS room_availability,
                JSON_OBJECT(
                    'average_rating', ROUND(AVG(sprv.rating), 2),
                    'total_reviews', COUNT(sprv.review_id),
                    'recent_reviews', (
                        SELECT JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'rating', sprv2.rating,
                                'comment', LEFT(sprv2.comment, 100)
                            )
                        )
                        FROM service_provider_review sprv2
                        WHERE sprv2.service_provider_id = sp.service_provider_id
                        AND sprv2.is_approved = TRUE
                        ORDER BY sprv2.created_at DESC
                        LIMIT 5
                    )
                ) AS reviews
            FROM service_provider sp
            LEFT JOIN service_provider_type spt ON sp.service_provider_type_id = spt.service_provider_type_id
            LEFT JOIN currency c ON sp.currency_id = c.currency_id
            LEFT JOIN service_provider_location spl ON sp.service_provider_id = spl.service_provider_id
            LEFT JOIN service_provider_review sprv ON sp.service_provider_id = sprv.service_provider_id
                AND sprv.is_approved = TRUE
            WHERE sp.service_provider_type_id = 1  -- Hotels only
            AND sp.status_id = 1
            -- AND sp.approval_status_id = 35
            GROUP BY
                sp.service_provider_id, sp.name, sp.description, sp.address, sp.contact_number,
                sp.email, sp.website_url, sp.check_in_time, sp.check_out_time, sp.star_rating,
                c.currency_code, sp.cancellation_policy, sp.total_rooms, sp.parking_facility,
                sp.wifi_available, sp.pet_friendly, spt.name
            ORDER BY sp.star_rating DESC, sp.name ASC
            """;

    public static final String GET_RESORTS_DETAILS_FOR_SECTION = """
            SELECT
                sp.service_provider_id AS resort_id,
                sp.name AS resort_name,
                sp.description AS resort_description,
                sp.address,
                sp.contact_number,
                sp.email,
                sp.website_url,
                sp.check_in_time,
                sp.check_out_time,
                sp.star_rating,
                c.currency_code,
                sp.cancellation_policy,
                sp.total_rooms,
                sp.parking_facility,
                sp.wifi_available,
                sp.pet_friendly,
                spt.name AS resort_type,
                MAX(spl.latitude) AS latitude,
                MAX(spl.longitude) AS longitude,
                MAX(spl.google_place_id) AS google_place_id,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'image_url', spi.image_url,
                            'caption', spi.caption
                        )
                    )
                    FROM service_provider_image spi
                    WHERE spi.service_provider_id = sp.service_provider_id
                    AND spi.status_id = 1
                ) AS resort_images,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'meal_type', mt.name,
                            'meal_description', spm.description,
                            'local_price', spm.local_price,
                            'cuisine_type', spm.cuisine_type,
                            'available', spm.available
                        )
                    )
                    FROM service_provider_meal spm
                    LEFT JOIN meal_type mt ON spm.meal_type_id = mt.meal_type_id
                    WHERE spm.service_provider_id = sp.service_provider_id
                    AND spm.available = TRUE
                ) AS dining_options,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'room_type', srt.name,
                            'room_description', spr.description,
                            'capacity', spr.capacity,
                            'bed_type', spr.bed_type,
                            'local_price_per_night', spr.local_price_per_night,
                            'has_air_conditioning', spr.has_air_conditioning,
                            'has_tv', spr.has_tv,
                            'internet_access', spr.internet_access,
                            'has_balcony', spr.has_balcony,
                            'view_type', spr.view_type
                        )
                    )
                    FROM service_provider_room spr
                    LEFT JOIN service_provider_room_type srt ON spr.room_type_id = srt.room_type_id
                    WHERE spr.service_provider_id = sp.service_provider_id
                    AND spr.status_id = 1
                ) AS accommodations,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'date', spra.date,
                            'available_rooms', spra.available_rooms
                        )
                    )
                    FROM service_provider_room_availability spra
                    WHERE spra.room_id IN (
                        SELECT room_id FROM service_provider_room
                        WHERE service_provider_id = sp.service_provider_id
                    )
                    AND spra.date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
                ) AS availability,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'facility_name', spf.facility_name,
                            'description', spf.description,
                            'is_available', spf.is_available
                        )
                    )
                    FROM service_provider_facility spf
                    WHERE spf.service_provider_id = sp.service_provider_id
                    AND spf.is_available = TRUE
                ) AS resort_facilities,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'name', spa.name,
                            'description', spa.description,
                            'local_additional_charge', spa.local_additional_charge
                        )
                    )
                    FROM service_provider_amenity spa
                    WHERE spa.service_provider_id = sp.service_provider_id
                    AND spa.is_available = TRUE
                ) AS amenities,
                JSON_OBJECT(
                    'average_rating', ROUND(AVG(sprv.rating), 2),
                    'total_reviews', COUNT(sprv.review_id),
                    'recent_reviews', (
                        SELECT JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'rating', sprv2.rating,
                                'comment', LEFT(sprv2.comment, 100),
                                'guest_name', (SELECT CONCAT(first_name, ' ', last_name) FROM user WHERE user_id = sprv2.user_id)
                            )
                        )
                        FROM service_provider_review sprv2
                        WHERE sprv2.service_provider_id = sp.service_provider_id
                        AND sprv2.is_approved = TRUE
                        ORDER BY sprv2.created_at DESC
                        LIMIT 5
                    )
                ) AS guest_reviews
            FROM service_provider sp
            LEFT JOIN service_provider_type spt ON sp.service_provider_type_id = spt.service_provider_type_id
            LEFT JOIN currency c ON sp.currency_id = c.currency_id
            LEFT JOIN service_provider_location spl ON sp.service_provider_id = spl.service_provider_id
            LEFT JOIN service_provider_review sprv ON sp.service_provider_id = sprv.service_provider_id
                AND sprv.is_approved = TRUE
            WHERE sp.service_provider_type_id = 2  -- Resorts (assuming 2 is the ID for resorts)
            AND sp.status_id = 1
         --   AND sp.approval_status_id = 1
            GROUP BY
                sp.service_provider_id, sp.name, sp.description, sp.address, sp.contact_number,
                sp.email, sp.website_url, sp.check_in_time, sp.check_out_time, sp.star_rating,
                c.currency_code, sp.cancellation_policy, sp.total_rooms, sp.parking_facility,
                sp.wifi_available, sp.pet_friendly, spt.name
            ORDER BY sp.star_rating DESC, sp.name ASC
            """;

    public static final String GET_VILLAS_DETAILS_FOR_SECTION = """
            SELECT
                sp.service_provider_id AS villa_id,
                sp.name AS villa_name,
                sp.description AS villa_description,
                sp.address,
                sp.contact_number,
                sp.email,
                sp.website_url,
                sp.check_in_time,
                sp.check_out_time,
                sp.star_rating,
                c.currency_code,
                sp.cancellation_policy,
                sp.total_rooms,
                sp.parking_facility,
                sp.wifi_available,
                sp.pet_friendly,
                spt.name AS property_type,
                MAX(spl.latitude) AS latitude,
                MAX(spl.longitude) AS longitude,
                MAX(spl.google_place_id) AS google_place_id,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'image_url', spi.image_url,
                            'caption', spi.caption
                        )
                    )
                    FROM service_provider_image spi
                    WHERE spi.service_provider_id = sp.service_provider_id
                    AND spi.status_id = 1
                ) AS villa_images,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'room_type', srt.name,
                            'room_description', spr.description,
                            'capacity', spr.capacity,
                            'bed_type', spr.bed_type,
                            'local_price_per_night', spr.local_price_per_night,
                            'has_air_conditioning', spr.has_air_conditioning,
                            'has_tv', spr.has_tv,
                            'internet_access', spr.internet_access,
                            'has_private_pool', spr.has_balcony,  -- Assuming balcony field represents private pool for villas
                            'view_type', spr.view_type
                        )
                    )
                    FROM service_provider_room spr
                    LEFT JOIN service_provider_room_type srt ON spr.room_type_id = srt.room_type_id
                    WHERE spr.service_provider_id = sp.service_provider_id
                    AND spr.status_id = 1
                ) AS villa_units,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'date', spra.date,
                            'available_rooms', spra.available_rooms
                        )
                    )
                    FROM service_provider_room_availability spra
                    WHERE spra.room_id IN (
                        SELECT room_id FROM service_provider_room
                        WHERE service_provider_id = sp.service_provider_id
                    )
                    AND spra.date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
                ) AS availability,
                JSON_OBJECT(
                    'average_rating', ROUND(AVG(sprv.rating), 2),
                    'total_reviews', COUNT(sprv.review_id),
                    'recent_reviews', (
                        SELECT JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'rating', sprv2.rating,
                                'comment', LEFT(sprv2.comment, 100)
                            )
                        )
                        FROM service_provider_review sprv2
                        WHERE sprv2.service_provider_id = sp.service_provider_id
                        AND sprv2.is_approved = TRUE
                        ORDER BY sprv2.created_at DESC
                        LIMIT 5
                    )
                ) AS guest_reviews
            FROM service_provider sp
            LEFT JOIN service_provider_type spt ON sp.service_provider_type_id = spt.service_provider_type_id
            LEFT JOIN currency c ON sp.currency_id = c.currency_id
            LEFT JOIN service_provider_location spl ON sp.service_provider_id = spl.service_provider_id
            LEFT JOIN service_provider_review sprv ON sp.service_provider_id = sprv.service_provider_id
                AND sprv.is_approved = TRUE
            WHERE sp.service_provider_type_id = 3  -- Villas
            AND sp.status_id = 1
           -- AND sp.approval_status_id = 1
            GROUP BY
                sp.service_provider_id, sp.name, sp.description, sp.address, sp.contact_number,
                sp.email, sp.website_url, sp.check_in_time, sp.check_out_time, sp.star_rating,
                c.currency_code, sp.cancellation_policy, sp.total_rooms, sp.parking_facility,
                sp.wifi_available, sp.pet_friendly, spt.name
            ORDER BY sp.star_rating DESC, sp.name ASC
            """;

    public static final String GET_RESTAURANTS_DETAILS_FOR_SECTION = """
            SELECT
                sp.service_provider_id AS restaurant_id,
                sp.name AS restaurant_name,
                sp.description AS restaurant_description,
                sp.address,
                sp.contact_number,
                sp.email,
                sp.website_url,
                sp.star_rating,
                c.currency_code,
                spt.name AS restaurant_type,
                MAX(spl.latitude) AS latitude,
                MAX(spl.longitude) AS longitude,
                MAX(spl.google_place_id) AS google_place_id,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'image_url', spi.image_url,
                            'caption', spi.caption
                        )
                    )
                    FROM service_provider_image spi
                    WHERE spi.service_provider_id = sp.service_provider_id
                    AND spi.status_id = 1
                ) AS restaurant_images,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'meal_type', mt.name,
                            'meal_description', spm.description,
                            'local_price', spm.local_price,
                            'foreign_price', spm.foreign_price,
                            'cuisine_type', spm.cuisine_type,
                            'dietary_tags', spm.dietary_tags,
                            'serves_people', spm.serves_people,
                            'is_chef_special', spm.is_chef_special,
                            'is_spicy', spm.is_spicy,
                            'spice_level', spm.spice_level,
                            'available', spm.available,
                            'preparation_time', spm.preparation_time
                        )
                    )
                    FROM service_provider_meal spm
                    LEFT JOIN meal_type mt ON spm.meal_type_id = mt.meal_type_id
                    WHERE spm.service_provider_id = sp.service_provider_id
                    AND spm.available = TRUE
                ) AS menu_items,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'day_of_week', spoh.day_of_week,
                            'opens_at', TIME_FORMAT(spoh.opens_at, '%H:%i'),
                            'closes_at', TIME_FORMAT(spoh.closes_at, '%H:%i'),
                            'is_24_hours', spoh.is_24_hours,
                            'special_note', spoh.special_note
                        )
                    )
                    FROM service_provider_operating_hours spoh
                    WHERE spoh.service_provider_id = sp.service_provider_id
                    ORDER BY spoh.day_of_week
                ) AS operating_hours,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'facility_name', spf.facility_name,
                            'description', spf.description,
                            'is_available', spf.is_available
                        )
                    )
                    FROM service_provider_facility spf
                    WHERE spf.service_provider_id = sp.service_provider_id
                    AND spf.is_available = TRUE
                ) AS restaurant_facilities,
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'method_type', apm.method_type,
                            'method_details', apm.method_details,
                            'is_available', apm.is_available
                        )
                    )
                    FROM accepted_payment_methods apm
                    WHERE apm.service_provider_id = sp.service_provider_id
                    AND apm.is_available = TRUE
                    AND apm.status_id = 1
                ) AS payment_methods,
                JSON_OBJECT(
                    'average_rating', ROUND(AVG(sprv.rating), 2),
                    'total_reviews', COUNT(sprv.review_id),
                    'recent_reviews', (
                        SELECT JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'rating', sprv2.rating,
                                'title', sprv2.title,
                                'comment', LEFT(sprv2.comment, 150),
                                'created_at', DATE_FORMAT(sprv2.created_at, '%Y-%m-%d')
                            )
                        )
                        FROM service_provider_review sprv2
                        WHERE sprv2.service_provider_id = sp.service_provider_id
                        AND sprv2.is_approved = TRUE
                        ORDER BY sprv2.created_at DESC
                        LIMIT 5
                    )
                ) AS customer_reviews,
                sp.languages_spoken,
                sp.parking_facility,
                sp.parking_capacity,
                sp.wifi_available,
                sp.pet_friendly,
                sp.special_instructions
            FROM service_provider sp
            LEFT JOIN service_provider_type spt ON sp.service_provider_type_id = spt.service_provider_type_id
            LEFT JOIN currency c ON sp.currency_id = c.currency_id
            LEFT JOIN service_provider_location spl ON sp.service_provider_id = spl.service_provider_id
            LEFT JOIN service_provider_review sprv ON sp.service_provider_id = sprv.service_provider_id
                AND sprv.is_approved = TRUE
            WHERE sp.service_provider_type_id = 4  -- Restaurants
            AND sp.status_id = 1
           -- AND sp.approval_status_id = 1
            GROUP BY
                sp.service_provider_id, sp.name, sp.description, sp.address, sp.contact_number,
                sp.email, sp.website_url, sp.star_rating, c.currency_code, spt.name,
                sp.languages_spoken, sp.parking_facility, sp.parking_capacity, sp.wifi_available,
                sp.pet_friendly, sp.special_instructions
            ORDER BY sp.star_rating DESC, sp.name ASC
            """;

    public static final String GET_HOSTELS_DETAILS_FOR_SECTION = """
        SELECT
            sp.service_provider_id AS hostel_id,
            sp.name AS hostel_name,
            sp.description AS hostel_description,
            sp.address,
            sp.contact_number,
            sp.email,
            sp.website_url,
            sp.check_in_time,
            sp.check_out_time,
            sp.star_rating,
            c.currency_code,
            sp.cancellation_policy,
            sp.total_rooms,
            sp.parking_facility,
            sp.wifi_available,
            sp.pet_friendly,
            spt.name AS hostel_type,
            MAX(spl.latitude) AS latitude,
            MAX(spl.longitude) AS longitude,
            MAX(spl.google_place_id) AS google_place_id,
            (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'image_url', spi.image_url,
                        'caption', spi.caption
                    )
                )
                FROM service_provider_image spi
                WHERE spi.service_provider_id = sp.service_provider_id
                AND spi.status_id = 1
            ) AS hostel_images,
            (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'room_type', srt.name,
                        'room_description', spr.description,
                        'capacity', spr.capacity,
                        'bed_type', spr.bed_type,
                        'number_of_beds', spr.number_of_beds,
                        'local_price_per_night', spr.local_price_per_night,
                        'foreign_price_per_night', spr.foreign_price_per_night,
                        'has_air_conditioning', spr.has_air_conditioning,
                        'has_tv', spr.has_tv,
                        'internet_access', spr.internet_access,
                        'is_dormitory', (srt.name LIKE '%dorm%' OR srt.name LIKE '%shared%'),
                        'gender_type', CASE
                            WHEN spr.description LIKE '%female%' OR spr.description LIKE '%women%' THEN 'female'
                            WHEN spr.description LIKE '%male%' OR spr.description LIKE '%men%' THEN 'male'
                            ELSE 'mixed'
                        END
                    )
                )
                FROM service_provider_room spr
                LEFT JOIN service_provider_room_type srt ON spr.room_type_id = srt.room_type_id
                WHERE spr.service_provider_id = sp.service_provider_id
                AND spr.status_id = 1
            ) AS room_options,
            (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'date', spra.date,
                        'available_beds', spra.available_rooms,
                        'total_beds', (SELECT SUM(capacity) FROM service_provider_room WHERE service_provider_id = sp.service_provider_id AND status_id = 1)
                    )
                )
                FROM service_provider_room_availability spra
                WHERE spra.room_id IN (
                    SELECT room_id FROM service_provider_room
                    WHERE service_provider_id = sp.service_provider_id
                )
                AND spra.date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
            ) AS bed_availability,
            (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'facility_name', spf.facility_name,
                        'description', spf.description,
                        'is_available', spf.is_available
                    )
                )
                FROM service_provider_facility spf
                WHERE spf.service_provider_id = sp.service_provider_id
                AND spf.is_available = TRUE
            ) AS hostel_facilities,
            (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'meal_type', mt.name,
                        'meal_description', spm.description,
                        'local_price', spm.local_price,
                        'available', spm.available,
                        'serves_people', spm.serves_people
                    )
                )
                FROM service_provider_meal spm
                LEFT JOIN meal_type mt ON spm.meal_type_id = mt.meal_type_id
                WHERE spm.service_provider_id = sp.service_provider_id
                AND spm.available = TRUE
            ) AS food_services,
            (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'name', spa.name,
                        'description', spa.description,
                        'local_additional_charge', spa.local_additional_charge,
                        'is_available', spa.is_available
                    )
                )
                FROM service_provider_amenity spa
                WHERE spa.service_provider_id = sp.service_provider_id
                AND spa.is_available = TRUE
            ) AS hostel_amenities,
            JSON_OBJECT(
                'average_rating', ROUND(AVG(sprv.rating), 2),
                'total_reviews', COUNT(sprv.review_id),
                'recent_reviews', (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'rating', sprv2.rating,
                            'title', sprv2.title,
                            'comment', LEFT(sprv2.comment, 120),
                            'traveler_type', CASE
                                WHEN sprv2.comment LIKE '%solo%' OR sprv2.comment LIKE '%backpacker%' THEN 'solo_traveler'
                                WHEN sprv2.comment LIKE '%group%' OR sprv2.comment LIKE '%friends%' THEN 'group_traveler'
                                WHEN sprv2.comment LIKE '%family%' THEN 'family'
                                ELSE 'other'
                            END
                        )
                    )
                    FROM service_provider_review sprv2
                    WHERE sprv2.service_provider_id = sp.service_provider_id
                    AND sprv2.is_approved = TRUE
                    ORDER BY sprv2.created_at DESC
                    LIMIT 5
                )
            ) AS traveler_reviews,
            sp.languages_spoken
        FROM service_provider sp
        LEFT JOIN service_provider_type spt ON sp.service_provider_type_id = spt.service_provider_type_id
        LEFT JOIN currency c ON sp.currency_id = c.currency_id
        LEFT JOIN service_provider_location spl ON sp.service_provider_id = spl.service_provider_id
        LEFT JOIN service_provider_review sprv ON sp.service_provider_id = sprv.service_provider_id
            AND sprv.is_approved = TRUE
        WHERE sp.service_provider_type_id = 5  -- Hostels
        AND sp.status_id = 1
   --     AND sp.approval_status_id = 1
        GROUP BY
            sp.service_provider_id, sp.name, sp.description, sp.address, sp.contact_number,
            sp.email, sp.website_url, sp.check_in_time, sp.check_out_time, sp.star_rating,
            c.currency_code, sp.cancellation_policy, sp.total_rooms, sp.parking_facility,
            sp.wifi_available, sp.pet_friendly, spt.name, sp.languages_spoken
        ORDER BY sp.star_rating DESC, sp.name ASC
        """;

}
