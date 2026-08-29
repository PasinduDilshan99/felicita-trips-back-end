package com.felicita.queries;

public class ServiceProviderQueries {

    public static final String GET_SERVICE_PROVIDER_DETAILS = """
            SELECT
                sp.service_provider_id,
                sp.user_id,
                sp.service_provider_type_id,
                sp.name,
                sp.description,
                sp.address,
                sp.contact_number,
                sp.email,
                sp.website_url,
                sp.check_in_time,
                sp.check_out_time,
                sp.star_rating,
                sp.currency_id,
                sp.cancellation_policy,
                sp.minimum_advance_booking_hours,
                sp.establishment_year,
                sp.total_rooms,
                sp.total_employees,
                sp.awards_certifications,
                sp.languages_spoken,
                sp.parking_facility,
                sp.parking_capacity,
                sp.wifi_available,
                sp.pet_friendly,
                sp.check_in_instructions,
                sp.special_instructions,
                sp.approval_status_id,
                sp.status_id,
                sp.created_at,
                sp.created_by,
                sp.updated_at,
                sp.updated_by,
                sp.terminated_at,
                sp.terminated_by,
                spt.name as provider_type_name,
                c.currency_code,
                c.symbol as currency_symbol,
                cs1.name as approval_status_name,
                cs2.name as status_name,
                u.username as created_by_username,
            	spa.approval_comment,
                spa.approved_at,
                u2.username as approved_by_username,
            	spi.image_id,
                spi.image_url,
                spi.image_name,
                spi.image_description,
                spi.caption,
                spi.status_id as image_status_id,
            	sph.hours_id,
                sph.day_of_week,
                sph.opens_at,
                sph.closes_at,
                sph.is_24_hours,
                sph.operating_status_id,
                sph.special_note as hours_special_note,
                sphos.name as operating_status_name,
            	apm.payment_method_id,
                apm.method_type,
                apm.method_details,
                apm.is_available as payment_method_available,
                apm.status_id as payment_method_status_id
            FROM service_provider sp
            LEFT JOIN service_provider_type spt ON sp.service_provider_type_id = spt.service_provider_type_id
            LEFT JOIN currency c ON sp.currency_id = c.currency_id
            LEFT JOIN common_status cs1 ON sp.approval_status_id = cs1.id
            LEFT JOIN common_status cs2 ON sp.status_id = cs2.id
            LEFT JOIN user u ON sp.created_by = u.user_id
            LEFT JOIN service_provider_approval spa ON sp.service_provider_id = spa.service_provider_id
                AND spa.approval_id = (SELECT MAX(approval_id) FROM service_provider_approval WHERE service_provider_id = sp.service_provider_id)
            LEFT JOIN user u2 ON spa.approved_by = u2.user_id
            LEFT JOIN service_provider_image spi ON sp.service_provider_id = spi.service_provider_id
            LEFT JOIN service_provider_operating_hours sph ON sp.service_provider_id = sph.service_provider_id
            LEFT JOIN service_provider_operating_hours_status sphos ON sph.operating_status_id = sphos.operating_status_id
            LEFT JOIN accepted_payment_methods apm ON sp.service_provider_id = apm.service_provider_id AND apm.is_available = TRUE
            WHERE sp.service_provider_id = ?
            """;

    public static final String GET_SERVICE_PROVIDER_NEAR_DESTINATIONS = """
            SELECT
                d.destination_id,
                d.name,
                d.description,
                d.latitude,
                d.longitude,
                d.location,
               -- dc.category as destination_category,
                cs.name as status_name,
                spd.created_at as linked_date
            FROM service_provider_destination spd
            JOIN destination d ON spd.destination_id = d.destination_id
--            LEFT JOIN destination_categories dc ON d.destination_category = dc.id
            LEFT JOIN common_status cs ON spd.status_id = cs.id
            WHERE spd.service_provider_id = ?
            AND spd.status_id = (SELECT id FROM common_status WHERE name = 'ACTIVE')
            ORDER BY d.name
            """;

    public static final String GET_SERVICE_PROVIDER_MEAL_DETAILS = """
            SELECT
                spm.meal_id,
                spm.service_provider_id,
                spm.meal_type_id,
                spm.description,
                spm.local_price,
                spm.foreign_price,
                spm.currency_id,
                spm.discount_percentage,
                spm.discount_requirements,
                spm.serves_people,
                spm.cuisine_type,
                spm.dietary_tags,
                spm.preparation_time,
                spm.is_chef_special,
                spm.is_spicy,
                spm.spice_level,
                spm.serving_size,
                spm.calories,
                spm.allergens,
                spm.available,
                spm.created_at,
                spm.created_by,
                spm.updated_at,
                spm.updated_by,
                spm.terminated_at,
                spm.terminated_by,
                mt.name as meal_type_name,
                c.currency_code,
                cs.name as status_name,
                spmi.image_url,
                spmi.image_name,
                spmi.image_description,
                spmi.caption as image_caption
            FROM service_provider_meal spm
            LEFT JOIN meal_type mt ON spm.meal_type_id = mt.meal_type_id
            LEFT JOIN currency c ON spm.currency_id = c.currency_id
            LEFT JOIN common_status cs ON mt.status_id = cs.id
            LEFT JOIN service_provider_meal_image spmi ON spm.meal_id = spmi.meal_id
            WHERE spm.service_provider_id = ?
            AND spm.available = TRUE
            ORDER BY mt.name, spm.is_chef_special DESC
            """;

    public static final String GET_SERVICE_PROVIDER_ROOM_DETAILS = """
            SELECT
                spr.room_id,
                spr.room_number,
                spr.description as room_description,
                spr.capacity,
                spr.room_size,
                spr.bed_type,
                spr.local_price_per_night,
                spr.foreign_price_per_night,
                sprt.name as room_type_name,
                c.currency_code,
                cs.name as room_status,
            	sprf.feature_name,
                sprf.feature_value,
                sprf.description as feature_description,
            	at.name as amenity_name,
                at.category as amenity_category,
                at.icon_class,
                spra.additional_notes as amenity_notes,
            	sprava.date as availability_date,
                sprava.available_rooms,
                sprava.booked_rooms,
                sprava.local_price_for_date,
                sprava.foreign_price_for_date,
            	spri.image_url as room_image_url,
                spri.image_name as room_image_name,
                spri.caption as room_image_caption
            FROM service_provider_room spr
            LEFT JOIN service_provider_room_type sprt ON spr.room_type_id = sprt.room_type_id
            LEFT JOIN currency c ON spr.currency_id = c.currency_id
            LEFT JOIN common_status cs ON spr.status_id = cs.id
            LEFT JOIN service_provider_room_feature sprf ON spr.room_id = sprf.room_id
            LEFT JOIN service_provider_room_amenity spra ON spr.room_id = spra.room_id
            LEFT JOIN amenity_type at ON spra.amenity_type_id = at.amenity_type_id
            LEFT JOIN service_provider_room_availability sprava ON spr.room_id = sprava.room_id
            LEFT JOIN service_provider_room_image spri ON spr.room_id = spri.room_id
            WHERE spr.service_provider_id = ?
            ORDER BY spr.room_type_id, spr.room_number
            """;

    public static final String GET_SERVICE_PROVIDER_PACKAGE_DETAILS = """
            SELECT
                spp.service_provider_package_id,
                spp.name as package_name,
                spp.description as package_description,
                spp.local_price,
                spp.foreign_price,
                spp.duration_days,
                spp.min_persons,
                spp.max_persons,
                c.currency_code,
                cs.name as package_status,
               sppf.feature_name,
                sppf.feature_value,
                sppf.description as feature_description,
               sppi.inclusion_name,
                sppi.description as inclusion_description,
               sppi_img.image_url as package_image_url,
                sppi_img.image_name as package_image_name,
                sppi_img.caption as package_image_caption
            FROM service_provider_package spp
            LEFT JOIN currency c ON spp.currency_id = c.currency_id
            LEFT JOIN common_status cs ON spp.status_id = cs.id
            LEFT JOIN service_provider_package_feature sppf ON spp.service_provider_package_id = sppf.service_provider_package_id
            LEFT JOIN service_provider_package_inclusion sppi ON spp.service_provider_package_id = sppi.service_provider_package_id
            LEFT JOIN service_provider_package_image sppi_img ON spp.service_provider_package_id = sppi_img.service_provider_package_id
            WHERE spp.service_provider_id = ?
            ORDER BY spp.name
            """;

    public static final String GET_SERVICE_PROVIDER_AMENITY_DETAILS = """
            SELECT
                spa.provider_amenity_id,
                spa.name as amenity_name,
                spa.description as amenity_description,
                spa.local_additional_charge,
                spa.foreign_additional_charge,
                spa.is_available,
                at.name as amenity_type_name,
                at.category as amenity_category,
                at.icon_class,
                c.currency_code,
                cs.name as status_name
            FROM service_provider_amenity spa
            LEFT JOIN amenity_type at ON spa.amenity_type_id = at.amenity_type_id
            LEFT JOIN currency c ON spa.currency_id = c.currency_id
            LEFT JOIN common_status cs ON at.status_id = cs.id
            WHERE spa.service_provider_id = ?
            AND spa.is_available = TRUE
            ORDER BY at.category, at.name
            """;

    public static final String GET_SERVICE_PROVIDER_FACILITY_DETAILS = """
            SELECT
                spf.facility_id,
                spf.facility_name,
                spf.description as facility_description,
                spf.is_available,
                spf.special_note,
                spfi.image_url,
                spfi.image_name,
                spfi.image_description,
                spfi.caption as image_caption
            FROM service_provider_facility spf
            LEFT JOIN service_provider_facility_image spfi ON spf.facility_id = spfi.facility_id
            WHERE spf.service_provider_id = ?
            AND spf.is_available = TRUE
            ORDER BY spf.facility_name
            """;

    public static final String GET_SERVICE_PROVIDER_SEASONAL_PRICE_DETAILS = """
            SELECT
                spsp.seasonal_price_id,
                spsp.name as season_name,
                spsp.start_date,
                spsp.end_date,
                spsp.local_multiplier,
                spsp.foreign_multiplier,
                spsp.description,
                spsp.requirements,
                spsp.special_note,
                cs.name as status_name
            FROM service_provider_seasonal_pricing spsp
            LEFT JOIN common_status cs ON spsp.status_id = cs.id
            WHERE spsp.service_provider_id = ?
            """;
//    AND spsp.end_date >= CURDATE()
//    ORDER BY spsp.start_date

    public static final String GET_SERVICE_PROVIDER_CONTACT_PERSON_DETAILS = """
            SELECT
                spcp.contact_person_id,
                spcp.full_name,
                spcp.designation,
                spcp.email,
                spcp.phone,
                spcp.mobile,
                spcp.is_primary,
                cs.name as status_name
            FROM service_provider_contact_person spcp
            LEFT JOIN common_status cs ON spcp.status_id = cs.id
            WHERE spcp.service_provider_id = ?
            ORDER BY spcp.is_primary DESC, spcp.full_name
            """;

    public static final String GET_SERVICE_PROVIDER_BANK_DETAILS = """
            SELECT
                spb.bank_id,
                spb.bank_name,
                spb.account_holder_name,
                spb.account_number,
                spb.branch_name,
                spb.branch_code,
                spb.swift_code,
                spb.iban,
                spb.is_primary,
                c.currency_code,
                cs.name as status_name
            FROM service_provider_bank spb
            LEFT JOIN currency c ON spb.currency_id = c.currency_id
            LEFT JOIN common_status cs ON spb.status_id = cs.id
            WHERE spb.service_provider_id = ?
            ORDER BY spb.is_primary DESC, spb.bank_name
            """;

    public static final String GET_SERVICE_PROVIDER_TAX_DETAILS = """
            SELECT
                sptc.tax_id,
                sptc.tax_name,
                sptc.tax_percentage,
                sptc.tax_number,
                sptc.is_active as tax_active,
                sptc.applies_to_rooms,
                sptc.applies_to_meals,
                sptc.applies_to_packages,
                sptc.applies_to_amenities,
                cs1.name as tax_status,
            	spcs.commission_id,
                spcs.commission_percentage,
                spcs.applies_to_rooms as commission_applies_rooms,
                spcs.applies_to_meals as commission_applies_meals,
                spcs.applies_to_packages as commission_applies_packages,
                spcs.minimum_commission_amount,
                cs2.name as commission_status
            FROM service_provider_tax_configuration sptc
            LEFT JOIN common_status cs1 ON sptc.status_id = cs1.id
            LEFT JOIN service_provider_commission_settings spcs ON sptc.service_provider_id = spcs.service_provider_id
            LEFT JOIN common_status cs2 ON spcs.status_id = cs2.id
            WHERE sptc.service_provider_id = ?
            AND sptc.is_active = TRUE
            """;

    public static final String GET_SERVICE_PROVIDER_BOOKING_RESTRICTIONS_DETAILS= """ 
            SELECT
                spbr.restriction_id,
                spbr.restriction_type,
                spbr.min_stay_nights,
                spbr.max_stay_nights,
                spbr.start_date,
                spbr.end_date,
                spbr.description,
                cs.name as status_name
            FROM service_provider_booking_restrictions spbr
            LEFT JOIN common_status cs ON spbr.status_id = cs.id
            WHERE spbr.service_provider_id = ?
            """;
//    AND (spbr.end_date IS NULL OR spbr.end_date >= CURDATE())
//    ORDER BY spbr.start_date

    public static final String GET_SERVICE_PROVIDER_STATISTICS_DETAILS= """
            SELECT
                stats_id,
                total_bookings,
                total_revenue,
                average_rating,
                total_reviews,
                occupancy_rate,
                last_updated
            FROM service_provider_statistics
            WHERE service_provider_id = ?
            """;


    public static final String GET_SERVICE_PROVIDER_SOCIAL_MEDIA_DETAILS= """ 
            SELECT
                sps.social_id,
                sps.platform,
                sps.profile_url,
                spsvs.name as verification_status_name,
                cs.name as status_name
            FROM service_provider_social sps
            LEFT JOIN service_provider_social_verification_status spsvs ON sps.verification_status_id = spsvs.verification_status_id
            LEFT JOIN common_status cs ON sps.status_id = cs.id
            WHERE sps.service_provider_id = ?
            ORDER BY sps.platform""";


    public static final String GET_SERVICE_PROVIDER_REVIEW_DETAILS= """ 
            SELECT
                spr.review_id,
                spr.rating as overall_rating,
                spr.title,
                spr.comment,
                spr.created_at as review_date,
                spr.is_approved,
                u.username,
                u.first_name,
                u.last_name,
                cs.name as review_status,
            	sprrd.rating as category_rating,
                sprrc.name as category_name,
                sprrc.description as category_description
            FROM service_provider_review spr
            LEFT JOIN user u ON spr.user_id = u.user_id
            LEFT JOIN common_status cs ON spr.status_id = cs.id
            LEFT JOIN service_provider_review_rating_detail sprrd ON spr.review_id = sprrd.review_id
            LEFT JOIN service_provider_review_rating_category sprrc ON sprrd.category_id = sprrc.category_id
            WHERE spr.service_provider_id = ?
            AND spr.is_approved = TRUE
            ORDER BY spr.created_at DESC;
            """;

    public static final String GET_SERVICE_PROVIDER_NAMES_AND_IDS = """
            SELECT
            	sp.service_provider_id,
                sp.name
            FROM service_provider sp
            LEFT JOIN common_status cs
            	ON sp.status_id = cs.id
            WHERE cs.name=?
            """ ;
}
