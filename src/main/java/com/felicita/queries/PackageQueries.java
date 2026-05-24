package com.felicita.queries;

public class PackageQueries {

    public static final String GET_ALL_PACKAGES = """
                SELECT
                    p.package_id,
                    p.name AS package_name,
                    p.description AS package_description,
                    p.total_price,
                    p.discount_percentage,
                    p.start_date,
                    p.end_date,
                    p.color,
                    p.price_per_person,
                    p.hover_color,
                    p.min_person_count,
                    p.max_person_count,
                    p.price_per_person,
                    p.created_at,
                    p.created_by,
            
                    pt.name AS package_type_name,
                    pt.description AS package_type_description,
                    cs3.name AS package_type_status,
            
                    cs2.name AS package_status,
            
                    t.tour_id,
                    t.name AS tour_name,
                    t.description AS tour_description,
                    t.duration,
                    t.latitude,
                    t.longitude,
                    t.start_location,
                    t.end_location,
                    t.status AS tour_status,
            
                    ps.id AS schedule_id,
                    ps.name AS schedule_name,
                    ps.assume_start_date,
                    ps.assume_end_date,
                    ps.duration_start,
                    ps.duration_end,
                    ps.special_note AS schedule_special_note,
                    ps.description AS schedule_description,
            
                    f.id AS feature_id,
                    f.name AS feature_name,
                    f.value AS feature_value,
                    f.description AS feature_description,
                    f.color AS feature_color,
                    f.special_note AS feature_special_note,
            
                    pi.id AS image_id,
                    pi.name AS image_name,
                    pi.description AS image_description,
                    pi.image_url,
                    pi.color AS image_color
            
                FROM
                    packages p
                    LEFT JOIN common_status cs2 ON p.status = cs2.id
                    LEFT JOIN package_type pt ON pt.id = p.package_type_id
                    LEFT JOIN common_status cs3 ON pt.status = cs3.id
                    LEFT JOIN tour t ON p.tour_id = t.tour_id
                    LEFT JOIN package_schedule ps ON p.package_id = ps.package_id
                    LEFT JOIN features f ON p.package_id = f.package_id
                    LEFT JOIN package_images pi ON p.package_id = pi.package_id
                    LEFT JOIN common_status cs_package ON p.status = cs_package.id
                    LEFT JOIN common_status cs_tour ON t.status = cs_tour.id
            """;


    public static final String GET_PACKAGE_DETAILS_BY_PACKAGE_ID = """
                SELECT
                    p.package_id,
                    p.name AS package_name,
                    p.description AS package_description,
                    p.total_price,
                    p.discount_percentage,
                    p.start_date,
                    p.end_date,
                    p.color,
                    p.price_per_person,
                    p.hover_color,
                    p.min_person_count,
                    p.max_person_count,
                    p.price_per_person,
                    p.created_at,
                    p.created_by,
            
                    pt.name AS package_type_name,
                    pt.description AS package_type_description,
                    cs3.name AS package_type_status,
            
                    cs2.name AS package_status,
            
                    t.tour_id,
                    t.name AS tour_name,
                    t.description AS tour_description,
                    t.duration,
                    t.latitude,
                    t.longitude,
                    t.start_location,
                    t.end_location,
                    t.status AS tour_status,
            
                    ps.id AS schedule_id,
                    ps.name AS schedule_name,
                    ps.assume_start_date,
                    ps.assume_end_date,
                    ps.duration_start,
                    ps.duration_end,
                    ps.special_note AS schedule_special_note,
                    ps.description AS schedule_description,
            
                    f.id AS feature_id,
                    f.name AS feature_name,
                    f.value AS feature_value,
                    f.description AS feature_description,
                    f.color AS feature_color,
                    f.special_note AS feature_special_note,
            
                    pi.id AS image_id,
                    pi.name AS image_name,
                    pi.description AS image_description,
                    pi.image_url,
                    pi.color AS image_color
            
                FROM
                    packages p
                    LEFT JOIN common_status cs2 ON p.status = cs2.id
                    LEFT JOIN package_type pt ON pt.id = p.package_type_id
                    LEFT JOIN common_status cs3 ON pt.status = cs3.id
                    LEFT JOIN tour t ON p.tour_id = t.tour_id
                    LEFT JOIN package_schedule ps ON p.package_id = ps.package_id
                    LEFT JOIN package_features f ON p.package_id = f.package_id
                    LEFT JOIN package_images pi ON p.package_id = pi.package_id
                    LEFT JOIN common_status cs_package ON p.status = cs_package.id
                    LEFT JOIN common_status cs_tour ON t.status = cs_tour.id
                    WHERE p.package_id=?
            """;


    public static final String GET_PACKAGE_ALL_REVIEWS_DETAILS = """
            SELECT
                pr.id AS review_id,
                ps.package_id AS package_id,
                pr.package_schedule_id,
                pr.name AS review_name,
                pr.review,
                pr.rating,
                pr.description,
                cs_pr.name AS review_status,
                pr.number_of_participate,
                pr.created_by AS review_created_by,
                pr.created_at AS review_created_at,
                pr.updated_by AS review_updated_by,
                pr.updated_at AS review_updated_at,
                pri.id AS image_id,
                pri.name AS image_name,
                pri.description AS image_description,
                pri.image_url AS image_url,
                cs_pri.name AS image_status,
                pri.created_by AS image_created_by,
                pri.created_at AS image_created_at,
                prr.id AS review_reaction_id,
                prr.package_review_id AS reaction_review_id,
                prr.user_id AS reaction_user_id,
                u1.username AS reaction_user_name,
                rt.name AS reaction_type,
                cs_prr.name AS review_reaction_status,
                prr.created_at AS reaction_created_at,
                prc.id AS comment_id,
                prc.package_review_id AS comment_review_id,
                prc.user_id AS comment_user_id,
                u2.username AS comment_user_name,
                prc.parent_comment_id,
                prc.comment,
                cs_prc.name AS comment_status,
                prc.created_at AS comment_created_at,
                prc.created_by AS comment_created_by,
                prcr.id AS comment_reaction_id,
                prcr.comment_id AS comment_reaction_comment_id,
                prcr.user_id AS comment_reaction_user_id,
                u3.username AS comment_reaction_user_name,
                rt2.name AS comment_reaction_type,
                cs_prcr.name AS comment_reaction_status,
                prcr.created_by AS comment_reaction_created_by,
                prcr.created_at AS comment_reaction_created_at
            FROM package_review pr
            LEFT JOIN package_schedule ps
                ON pr.package_schedule_id = ps.id
            LEFT JOIN packages p
                ON ps.package_id = p.package_id
            LEFT JOIN common_status cs_pr
                ON cs_pr.id = pr.status
            LEFT JOIN package_review_images pri
                ON pri.package_review_id = pr.id
            LEFT JOIN common_status cs_pri
                ON cs_pri.id = pri.status
            LEFT JOIN package_review_reaction prr
                ON prr.package_review_id = pr.id
            LEFT JOIN reaction_type rt
                ON rt.id = prr.reaction_type_id
            LEFT JOIN user u1
                ON u1.user_id = prr.user_id
            LEFT JOIN common_status cs_prr
                ON cs_prr.id = prr.status
            LEFT JOIN package_review_comment prc
                ON prc.package_review_id = pr.id
            LEFT JOIN user u2
                ON u2.user_id = prc.user_id
            LEFT JOIN common_status cs_prc
                ON cs_prc.id = prc.status
            LEFT JOIN package_review_comment_reaction prcr
                ON prcr.comment_id = prc.id
            LEFT JOIN reaction_type rt2
                ON rt2.id = prcr.reaction_type_id
            LEFT JOIN user u3
                ON u3.user_id = prcr.user_id
            LEFT JOIN common_status cs_prcr
                ON cs_prcr.id = prcr.status
            ORDER BY pr.id, prc.id, prcr.id
            """;

    public static final String GET_PACKAGE_REVIEWS_DETAILS_BY_ID = """
            SELECT
                pr.id AS review_id,
                ps.package_id AS package_id,
                pr.package_schedule_id,
                pr.name AS review_name,
                pr.review,
                pr.rating,
                pr.description,
                cs_pr.name AS review_status,
                pr.number_of_participate,
                pr.created_by AS review_created_by,
                pr.created_at AS review_created_at,
                pr.updated_by AS review_updated_by,
                pr.updated_at AS review_updated_at,
                pri.id AS image_id,
                pri.name AS image_name,
                pri.description AS image_description,
                pri.image_url AS image_url,
                cs_pri.name AS image_status,
                pri.created_by AS image_created_by,
                pri.created_at AS image_created_at,
                prr.id AS review_reaction_id,
                prr.package_review_id AS reaction_review_id,
                prr.user_id AS reaction_user_id,
                u1.username AS reaction_user_name,
                rt.name AS reaction_type,
                cs_prr.name AS review_reaction_status,
                prr.created_at AS reaction_created_at,
                prc.id AS comment_id,
                prc.package_review_id AS comment_review_id,
                prc.user_id AS comment_user_id,
                u2.username AS comment_user_name,
                prc.parent_comment_id,
                prc.comment,
                cs_prc.name AS comment_status,
                prc.created_at AS comment_created_at,
                prc.created_by AS comment_created_by,
                prcr.id AS comment_reaction_id,
                prcr.comment_id AS comment_reaction_comment_id,
                prcr.user_id AS comment_reaction_user_id,
                u3.username AS comment_reaction_user_name,
                rt2.name AS comment_reaction_type,
                cs_prcr.name AS comment_reaction_status,
                prcr.created_by AS comment_reaction_created_by,
                prcr.created_at AS comment_reaction_created_at
            FROM package_review pr
            LEFT JOIN package_schedule ps
                ON pr.package_schedule_id = ps.id
            LEFT JOIN packages p
                ON ps.package_id = p.package_id
            LEFT JOIN common_status cs_pr
                ON cs_pr.id = pr.status
            LEFT JOIN package_review_images pri
                ON pri.package_review_id = pr.id
            LEFT JOIN common_status cs_pri
                ON cs_pri.id = pri.status
            LEFT JOIN package_review_reaction prr
                ON prr.package_review_id = pr.id
            LEFT JOIN reaction_type rt
                ON rt.id = prr.reaction_type_id
            LEFT JOIN user u1
                ON u1.user_id = prr.user_id
            LEFT JOIN common_status cs_prr
                ON cs_prr.id = prr.status
            LEFT JOIN package_review_comment prc
                ON prc.package_review_id = pr.id
            LEFT JOIN user u2
                ON u2.user_id = prc.user_id
            LEFT JOIN common_status cs_prc
                ON cs_prc.id = prc.status
            LEFT JOIN package_review_comment_reaction prcr
                ON prcr.comment_id = prc.id
            LEFT JOIN reaction_type rt2
                ON rt2.id = prcr.reaction_type_id
            LEFT JOIN user u3
                ON u3.user_id = prcr.user_id
            LEFT JOIN common_status cs_prcr
                ON cs_prcr.id = prcr.status
            WHERE p.package_id = ?
            ORDER BY pr.id, prc.id, prcr.id
            """;

    public static final String GET_PACKAGE_HISTORY_DETAILS = """
            SELECT
                ph.id AS package_history_id,
                ph.package_schedule_id,
                ps.name AS package_schedule_name,
                ps.assume_start_date,
                ps.assume_end_date,
                ps.duration_start,
                ps.duration_end,
                p.package_id,
                p.name AS package_name,
                p.tour_id,
                ph.number_of_participate,
                ph.rating,
                ph.duration,
                ph.description AS history_description,
                ph.color,
                ph.hover_color,
                ph.start_date,
                ph.end_date,
                ph.created_at AS history_created_at,
                CONCAT(uc.first_name, ' ', IFNULL(uc.middle_name, ''), ' ', IFNULL(uc.last_name, '')) AS created_by_user,
                uc.image_url AS created_by_image,
                ph.updated_at AS history_updated_at,
                CONCAT(uu.first_name, ' ', IFNULL(uu.middle_name, ''), ' ', IFNULL(uu.last_name, '')) AS updated_by_user,
                ph.terminated_at AS history_terminated_at,
                CONCAT(ut.first_name, ' ', IFNULL(ut.middle_name, ''), ' ', IFNULL(ut.last_name, '')) AS terminated_by_user,
                COALESCE(JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'imageId', thi.id,
                        'name', thi.name,
                        'description', thi.description,
                        'imageUrl', thi.image_url,
                        'color', thi.color
                    )
                ), JSON_ARRAY()) AS images
            FROM package_history ph
            JOIN package_schedule ps ON ph.package_schedule_id = ps.id
            JOIN packages p ON ps.package_id = p.package_id
            LEFT JOIN tour_schedule ts ON ts.tour_id = p.tour_id
            LEFT JOIN tour_history_images thi ON thi.tour_schedule_id = ts.id
            LEFT JOIN user uc ON ph.created_by = uc.user_id
            LEFT JOIN user uu ON ph.updated_by = uu.user_id
            LEFT JOIN user ut ON ph.terminated_by = ut.user_id
            GROUP BY ph.id
            ORDER BY ph.created_at DESC
            """;

    public static final String GET_PACKAGE_HISTORY_DETAILS_BY_ID = """
            SELECT
                ph.id AS package_history_id,
                ph.package_schedule_id,
                ps.name AS package_schedule_name,
                ps.assume_start_date,
                ps.assume_end_date,
                ps.duration_start,
                ps.duration_end,
                p.package_id,
                p.name AS package_name,
                p.tour_id,
                ph.number_of_participate,
                ph.rating,
                ph.duration,
                ph.description AS history_description,
                ph.color,
                ph.hover_color,
                ph.start_date,
                ph.end_date,
                ph.created_at AS history_created_at,
                CONCAT(uc.first_name, ' ', IFNULL(uc.middle_name, ''), ' ', IFNULL(uc.last_name, '')) AS created_by_user,
                uc.image_url AS created_by_image,
                ph.updated_at AS history_updated_at,
                CONCAT(uu.first_name, ' ', IFNULL(uu.middle_name, ''), ' ', IFNULL(uu.last_name, '')) AS updated_by_user,
                ph.terminated_at AS history_terminated_at,
                CONCAT(ut.first_name, ' ', IFNULL(ut.middle_name, ''), ' ', IFNULL(ut.last_name, '')) AS terminated_by_user,
                COALESCE(JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'imageId', thi.id,
                        'name', thi.name,
                        'description', thi.description,
                        'imageUrl', thi.image_url,
                        'color', thi.color
                    )
                ), JSON_ARRAY()) AS images
            FROM package_history ph
            JOIN package_schedule ps ON ph.package_schedule_id = ps.id
            JOIN packages p ON ps.package_id = p.package_id
            LEFT JOIN tour_schedule ts ON ts.tour_id = p.tour_id
            LEFT JOIN tour_history_images thi ON thi.tour_schedule_id = ts.id
            LEFT JOIN user uc ON ph.created_by = uc.user_id
            LEFT JOIN user uu ON ph.updated_by = uu.user_id
            LEFT JOIN user ut ON ph.terminated_by = ut.user_id
            GROUP BY ph.id
            HAVING p.package_id = ?
            ORDER BY ph.created_at DESC
            """;

    public static final String GET_PACKAGE_HISTORY_IMAGES = """
            SELECT
                thi.id AS image_id,
                thi.name AS image_name,
                thi.description AS image_description,
                thi.image_url,
                thi.color,
                cs.name AS image_status_name,
                ps.id AS package_schedule_id,
                ps.name AS package_schedule_name,
                p.package_id,
                p.name AS package_name,
                p.tour_id,
                thi.created_at AS created_at,
                CONCAT(uc.first_name, ' ', IFNULL(uc.middle_name, ''), ' ', IFNULL(uc.last_name, '')) AS created_by_user,
                uc.image_url AS created_by_image
            FROM tour_history_images thi
            JOIN tour_schedule ts ON thi.tour_schedule_id = ts.id
            JOIN packages p ON ts.tour_id = p.tour_id
            JOIN package_schedule ps ON ps.package_id = p.package_id
            LEFT JOIN common_status cs ON thi.status = cs.id
            LEFT JOIN user uc ON thi.created_by = uc.user_id
            ORDER BY thi.created_at DESC
            """;

    public static final String GET_PACKAGE_HISTORY_IMAGES_BY_ID = """
            SELECT
                thi.id AS image_id,
                thi.name AS image_name,
                thi.description AS image_description,
                thi.image_url,
                thi.color,
                cs.name AS image_status_name,
                ps.id AS package_schedule_id,
                ps.name AS package_schedule_name,
                p.package_id,
                p.name AS package_name,
                p.tour_id,
                thi.created_at AS created_at,
                CONCAT(uc.first_name, ' ', IFNULL(uc.middle_name, ''), ' ', IFNULL(uc.last_name, '')) AS created_by_user,
                uc.image_url AS created_by_image
            FROM tour_history_images thi
            JOIN tour_schedule ts ON thi.tour_schedule_id = ts.id
            JOIN packages p ON ts.tour_id = p.tour_id
            JOIN package_schedule ps ON ps.package_id = p.package_id
            LEFT JOIN common_status cs ON thi.status = cs.id
            LEFT JOIN user uc ON thi.created_by = uc.user_id
            WHERE p.package_id = ?
            ORDER BY thi.created_at DESC
            """;

    public static final String GET_PACKAGE_IDS_WITH_FILTERS = """
            SELECT DISTINCT p.package_id
            FROM packages p
            LEFT JOIN tour t ON p.tour_id = t.tour_id
            LEFT JOIN package_type pt ON pt.id = p.package_type_id
            LEFT JOIN common_status cs_p ON p.status = cs_p.id
            LEFT JOIN common_status cs_t ON t.status = cs_t.id
            WHERE cs_p.name = 'ACTIVE'
              AND cs_t.name = 'ACTIVE'
            
              AND (? IS NULL OR p.name LIKE CONCAT('%', ?, '%'))
              AND (? IS NULL OR p.total_price >= ?)
              AND (? IS NULL OR p.total_price <= ?)
              AND (? IS NULL OR t.duration = ?)
              AND (? IS NULL OR pt.name = ?)
              AND (? IS NULL OR (t.start_location LIKE CONCAT('%', ?, '%')
                   OR t.end_location LIKE CONCAT('%', ?, '%')))
              AND (? = 0 OR p.min_person_count >= ?)
              AND (? = 0 OR p.max_person_count <= ?)
              AND (? IS NULL OR p.start_date >= ?)
              AND (? IS NULL OR p.end_date <= ?)
            LIMIT ? OFFSET ?
            """;

    public static final String COUNT_PACKAGES_WITH_FILTERS = """
                SELECT COUNT(DISTINCT p.package_id)
                FROM packages p
                LEFT JOIN tour t ON p.tour_id = t.tour_id
                LEFT JOIN package_type pt ON pt.id = p.package_type_id
                LEFT JOIN common_status cs_p ON p.status = cs_p.id
                LEFT JOIN common_status cs_t ON t.status = cs_t.id
                WHERE cs_p.name = 'ACTIVE'
                  AND cs_t.name = 'ACTIVE'
            
                  AND (? IS NULL OR p.name LIKE CONCAT('%', ?, '%'))
                  AND (? IS NULL OR p.total_price >= ?)
                  AND (? IS NULL OR p.total_price <= ?)
                  AND (? IS NULL OR t.duration = ?)
                  AND (? IS NULL OR pt.name = ?)
                  AND (? IS NULL OR (t.start_location LIKE CONCAT('%', ?, '%')
                       OR t.end_location LIKE CONCAT('%', ?, '%')))
                  AND (? = 0 OR p.min_person_count >= ?)
                  AND (? = 0 OR p.max_person_count <= ?)
                  AND (? IS NULL OR p.start_date >= ?)
                  AND (? IS NULL OR p.end_date <= ?)
            """;


    public static final String GET_PACKAGES_BY_IDS = """
            SELECT
                p.package_id,
                p.name AS package_name,
                p.description AS package_description,
                p.total_price,
                p.discount_percentage,
                p.start_date,
                p.end_date,
                p.color,
                p.price_per_person,
                p.hover_color,
                p.min_person_count,
                p.max_person_count,
                p.created_at,
                p.created_by,
            
                pt.name AS package_type_name,
                pt.description AS package_type_description,
                cs3.name AS package_type_status,
            
                cs2.name AS package_status,
            
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration,
                t.latitude,
                t.longitude,
                t.start_location,
                t.end_location,
                 t.status AS tour_status,
            
                ps.id AS schedule_id,
                ps.name AS schedule_name,
                ps.assume_start_date,
                ps.assume_end_date,
                ps.duration_start,
                ps.duration_end,
                ps.special_note AS schedule_special_note,
                ps.description AS schedule_description,
            
                f.id AS feature_id,
                f.name AS feature_name,
                f.value AS feature_value,
                f.description AS feature_description,
                f.color AS feature_color,
                f.special_note AS feature_special_note,
            
                pi.id AS image_id,
                pi.name AS image_name,
                pi.description AS image_description,
                pi.image_url,
                pi.color AS image_color
            
            FROM packages p
            LEFT JOIN package_type pt ON pt.id = p.package_type_id
            LEFT JOIN common_status cs2 ON p.status = cs2.id
            LEFT JOIN common_status cs3 ON pt.status = cs3.id
            LEFT JOIN tour t ON p.tour_id = t.tour_id
            LEFT JOIN package_schedule ps ON p.package_id = ps.package_id
            LEFT JOIN package_features f ON p.package_id = f.package_id
            LEFT JOIN package_images pi ON p.package_id = pi.package_id
            WHERE p.package_id IN (:packageIds)
            ORDER BY p.package_id
            """;
    public static final String GET_PACKAGE_DAY_TO_DAY_DETAILS_BY_ID = """
            SELECT
                p.package_id,
                p.name AS package_name,
                p.description AS package_description,
                p.total_price,
                p.price_per_person,
                p.discount_percentage,
                p.color,
                p.hover_color,
                pda.package_day_accommodation_id,
                pda.day_number,
                pda.breakfast,
                pda.breakfast_description,
                pda.lunch,
                pda.lunch_description,
                pda.dinner,
                pda.dinner_description,
                pda.morning_tea,
                pda.morning_tea_description,
                pda.evening_tea,
                pda.evening_tea_description,
                pda.snacks,
                pda.snack_note,
                pda.other_notes,
                sp.service_provider_id AS hotel_id,
                sp.name AS hotel_name,
                sp.description AS hotel_description,
                sp.website_url AS hotel_website,
                sp.star_rating AS hotel_category,
                spt.name AS hotel_type,
                sp.address AS hotel_location,
                spl.latitude AS hotel_latitude,
                spl.longitude AS hotel_longitude,
                v.vehicle_id AS transport_id,
                v.registration_number AS vehicle_registration_number,
                vt.name AS vehicle_type_name,
                vs.model AS vehicle_model,
                vs.seat_capacity,
                vs.air_condition
            FROM packages p
            INNER JOIN package_day_accommodation pda
                ON p.package_id = pda.package_id
            LEFT JOIN service_provider sp
                ON pda.hotel_id = sp.service_provider_id
            LEFT JOIN service_provider_type spt
            	ON spt.service_provider_type_id = sp.service_provider_type_id
            LEFT JOIN vehicles v
                ON pda.transport_id = v.vehicle_id
            LEFT JOIN vehicle_specifications vs
                ON v.specification_id = vs.specification_id
            LEFT JOIN vehicle_type vt
                ON vt.vehicle_type_id = v.vehicle_type_id
            LEFT JOIN service_provider_location spl
                ON sp.service_provider_id = spl.service_provider_id
            WHERE p.package_id = ?
            ORDER BY pda.day_number ASC
            """;
    public static final String GET_DAY_TO_PACKAGE_DETAILS_BY_ID = """
            SELECT
            	p.package_id,
                pt.name,
                p.name,
                p.description,
                p.discount_percentage,
                p.total_price,
                p.color,
                p.hover_color,
                p.price_per_person
            FROM packages p
            LEFT JOIN package_type pt
            ON pt.id = p.package_type_id
            WHERE p.tour_id = ?
            """;
    public static final String GET_PACKAGES_ACCOMMODATIONS_BY_IDS = """
            SELECT
                p.package_id,
                pda.package_day_accommodation_id,
                pda.day_number,
                pda.breakfast,
                pda.breakfast_description,
                pda.lunch,
                pda.lunch_description,
                pda.dinner,
                pda.dinner_description,
                pda.morning_tea,
                pda.morning_tea_description,
                pda.evening_tea,
                pda.evening_tea_description,
                pda.snacks,
                pda.snack_note,
                pda.other_notes,
                sp.service_provider_id AS hotel_id,
                sp.name AS hotel_name,
                sp.description AS hotel_description,
                sp.website_url AS hotel_website,
                sp.star_rating AS hotel_category,
                spt.name AS hotel_type,
                sp.address AS hotel_location,
                spl.latitude AS hotel_latitude,
                spl.longitude AS hotel_longitude,
                v.vehicle_id AS transport_id,
                v.registration_number AS vehicle_registration_number,
                vt.vehicle_type_id AS vehicle_type_id,
                vt.name AS vehicle_type_name,
                vs.model AS vehicle_model,
                vs.specification_id AS specification_id,
                vs.seat_capacity,
                vs.air_condition
            FROM packages p
            INNER JOIN package_day_accommodation pda
                ON p.package_id = pda.package_id
            LEFT JOIN service_provider sp
                ON pda.hotel_id = sp.service_provider_id
            LEFT JOIN service_provider_type spt
            	ON spt.service_provider_type_id = sp.service_provider_type_id
            LEFT JOIN vehicles v
                ON pda.transport_id = v.vehicle_id
            LEFT JOIN vehicle_specifications vs
                ON v.specification_id = vs.specification_id
            LEFT JOIN vehicle_type vt
                ON vt.vehicle_type_id = v.vehicle_type_id
            LEFT JOIN service_provider_location spl
                ON sp.service_provider_id = spl.service_provider_id
            WHERE p.package_id IN (%s)
            ORDER BY pda.day_number ASC
            """;

    public static final String GET_PACKAGE_IDS_BY_TOUR_ID = """
            SELECT package_id FROM packages WHERE tour_id = ?
            """;
    public static final String GET_PACKAGE_EXCLUSIONS_BY_PACKAGE_ID = """
            SELECT
                pe.package_exclusion_id,
                pe.package_id,
                pe.exclusion_text,
                pe.display_order,
                cs.name AS status_name,
                pe.created_at,
                pe.created_by,
                pe.updated_at,
                pe.updated_by
            FROM package_exclusion pe
            JOIN common_status cs ON pe.status_id = cs.id
            WHERE pe.package_id = ?
            ORDER BY pe.display_order
            """;
    public static final String GET_PACKAGE_INCLUSIONS_BY_PACKAGE_ID = """
            SELECT
                pi.package_inclusion_id,
                pi.package_id,
                pi.inclusion_text,
                pi.display_order,
                cs.name AS status_name,
                pi.created_at,
                pi.created_by,
                pi.updated_at,
                pi.updated_by
            FROM package_inclusion pi
            JOIN common_status cs ON pi.status_id = cs.id
            WHERE pi.package_id = ?
            ORDER BY pi.display_order
            """;
    public static final String GET_PACKAGE_CONDITIONS_BY_PACKAGE_ID = """
            SELECT
                pc.package_condition_id,
                pc.package_id,
                pc.condition_text,
                pc.display_order,
                cs.name AS status_name,
                pc.created_at,
                pc.created_by,
                pc.updated_at,
                pc.updated_by
            FROM package_condition pc
            JOIN common_status cs ON pc.status_id = cs.id
            WHERE pc.package_id = ?  
            ORDER BY pc.display_order
            """;
    public static final String GET_PACKAGE_TRAVEL_TIPS_BY_PACKAGE_ID = """
            SELECT
                ptt.package_travel_tip_id,
                ptt.package_id,
                ptt.tip_title,
                ptt.tip_description,
                ptt.display_order,
                cs.name AS status_name,
                ptt.created_at,
                ptt.created_by,
                ptt.updated_at,
                ptt.updated_by,
                ptt.terminated_at,
                ptt.terminated_by
            FROM package_travel_tips ptt
            JOIN common_status cs ON ptt.status_id = cs.id
            WHERE ptt.package_id = ?
            ORDER BY ptt.display_order
            """;
    public static final String GET_PACKAGE_SCHEDULE_IDS_BY_TOUR_ID = """
            SELECT
                ps.id
            FROM package_schedule ps
            LEFT JOIN packages p
                ON p.package_id = ps.package_id
            LEFT JOIN tour t
                ON t.tour_id = p.tour_id
            WHERE t.tour_id = ?
            """;

    public static final String GET_PACKAGE_SCHEDULE_DETAILS_BY_ID = """
            SELECT
            	ps.id,
                p.package_id,
            	ps.name,
                ps.assume_start_date,
                ps.assume_end_date,
                ps.description,
                ps.special_note,
                cs.name AS status,
                ps.duration_start,
                ps.duration_end
            FROM package_schedule ps
            LEFT JOIN packages p
            	ON p.package_id = ps.package_id
            LEFT JOIN common_status cs
            	ON cs.id = ps.status
            WHERE p.package_id = ?
            """;
    public static final String GET_PACKAGE_SCHEDULE_DETAILS_BY_PACKAGE_ID = """
            SELECT
            	ps.id AS schedule_id,
            	ps.name AS schedule_name,
            	ps.assume_start_date,
            	ps.assume_end_date,
            	ps.duration_start,
            	ps.duration_end,
            	ps.special_note,
            	ps.description,
            	cs.name AS status_name,
            	ps.created_at,
            	ps.updated_at
            FROM package_schedule ps
            JOIN common_status cs ON ps.status = cs.id
            WHERE ps.package_id = ?
              AND cs.name = 'ACTIVE'
            """;
    public static final String GET_PACKAGE_BASIC_DETAILS_BY_PACKAGE_ID = """
            SELECT
            	p.package_id,
                p.name,
                p.description,
                p.total_price,
                p.price_per_person,
                p.discount_percentage,
                p.color,
                p.hover_color,
                p.min_person_count,
                p.max_person_count,
                cs.name AS status,
                pi.id AS image_id,
                pi.name AS image_name,
                pi.description AS image_description,
                pi.image_url
            FROM packages p
            LEFT JOIN common_status cs
            	ON cs.id = p.status
            LEFT JOIN package_images pi
            	ON pi.package_id = p.package_id
            WHERE p.package_id =?
            """;
    public static final String GET_ALL_PACKAGES_IMAGES = """
            SELECT
            	p.package_id AS package_id,
            	pi.id AS image_id,
            	pi.name AS image_name,
                pi.description AS image_description,
                pi.image_url
            FROM package_images pi
            LEFT JOIN packages p
            	ON p.package_id = pi.package_id
            LEFT JOIN tour t
            	ON t.tour_id = p.tour_id
            WHERE t.tour_id = ?
            """;
    public static final String GET_PACKAGE_BASIC_DETAILS_BY_PACKAGE_SCHEDULE_ID = """
            SELECT
            	p.package_id,
            	ps.assume_start_date,
                 ps.assume_end_date,
            	p.name,
            	p.description,
            	p.total_price,
            	p.price_per_person,
            	p.discount_percentage,
            	p.color,
            	p.hover_color,
            	p.min_person_count,
            	p.max_person_count,
                t.tour_id,
                t.start_location,
                t.end_location,
            	cs.name AS status
            FROM package_schedule ps
            LEFT JOIN packages p
            	ON ps.package_id = p.package_id
            LEFT JOIN tour t
            	ON t.tour_id = p.tour_id
            LEFT JOIN common_status cs
            	ON cs.id = p.status
            WHERE ps.id = ?
            """;
    public static final String GET_PACKAGE_DAY_ACCOMMODATION_PRICE_BY_PACKAGE_SHECULE_ID = """
            SELECT DISTINCT
            	p.package_id,
            	pda.package_day_accommodation_id,
            	pda.day_number,
            	pda.hotel_id,
                sp.name AS hotel_name,
            	pda.transport_id,
            	pda.transport_cost,
            	pda.local_price,
            	pda.price,
            	pda.discount,
            	pda.service_charge,
            	pda.tax,
            	pda.extra_charge,
            	pda.extra_charge_note,
                t.name AS tour_name,
                t.description AS tour_description
            FROM package_schedule ps
            JOIN packages p
            	ON ps.package_id = p.package_id
            LEFT JOIN tour t
            	ON t.tour_id = p.tour_id
            LEFT JOIN package_day_accommodation pda
            	ON pda.package_id = p.package_id
            LEFT JOIN service_provider sp
            	ON sp.service_provider_id = pda.hotel_id
            JOIN common_status cs
            	ON cs.id = p.status
                        WHERE ps.id = ?
            """;
    public static final String GET_PACKAGE_DESTINATION_EXTRA_PRICE_BY_PACKAGE_SCHEDULE_ID = """
            SELECT DISTINCT
            	p.package_id,
                td.destination_id,
                d.extra_price,
                d.extra_price_note,
                d.name,
                d.description
            FROM package_schedule ps
            JOIN packages p
                ON ps.package_id = p.package_id
            JOIN tour t
                ON t.tour_id = p.tour_id
            JOIN tour_destination td
                ON td.tour_id = t.tour_id
            JOIN destination d
                ON d.destination_id = td.destination_id
            JOIN common_status cs
                ON cs.id = p.status
            WHERE ps.id = ?
            """;
    public static final String GET_PACKAGE_ACTIVITY_PRICE_BY_PACKAGE_SCHEDULE_ID = """
            SELECT
            	p.package_id,
                td.activities_id,
                a.price_foreigners,
                a.name,
                a.description
            FROM package_schedule ps
            LEFT JOIN packages p
            	ON ps.package_id = p.package_id
            LEFT JOIN tour t
            	ON t.tour_id = p.tour_id
            LEFT JOIN tour_destination td
            	ON td.tour_id = t.tour_id
            LEFT JOIN activities a
            	ON a.id = td.activities_id
            LEFT JOIN common_status cs
            	ON cs.id = p.status
            WHERE ps.id = ?
            """;
    public static final String PACKAGE_TERMINATE = """
            UPDATE packages
            SET status = (SELECT id FROM common_status WHERE name = ? LIMIT 1),terminated_at = now(), terminated_by = ?
            WHERE package_id = ?
            """;
    public static final String GET_ACTIVE_PACKAGES_FOR_TERMINATE = """
            SELECT
            	p.package_id,
                p.name
            FROM packages p
            LEFT JOIN common_status cs
            	ON cs.id = p.status
            WHERE cs.name = ?
            """;

    public static final String INSERT_PACKAGE_BASIC_DETAILS = """
                INSERT INTO packages 
                (package_type_id,tour_id, name, description, total_price, discount_percentage, start_date, end_date,
                 color, status, hover_color, min_person_count, max_person_count, price_per_person, created_by)
                VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String INSERT_PACKAGE_IMAGE = """
            INSERT INTO package_images
            (package_id, name, description, status, image_url, color, created_by)
            VALUES (
               ?, ?, ?, 
               (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
               ?, ?, ?
            )
            """;
    public static final String INSERT_PACKAGE_INCLUSION = """
            INSERT INTO package_inclusion
            (package_id, inclusion_text, display_order, status_id, created_by, updated_by)
            VALUES (
             ?, ?, ?, 
             (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
             ?, ?
            )
            """;

    public static final String INSERT_PACKAGE_EXCLUSION = """
            INSERT INTO package_exclusion
            (package_id, exclusion_text, display_order, status_id, created_by, updated_by)
            VALUES (
             ?, ?, ?, 
             (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
             ?, ?
            )
            """;

    public static final String INSERT_PACKAGE_CONDITION = """
            INSERT INTO package_condition
            (package_id, condition_text, display_order, status_id, created_by, updated_by)
            VALUES (
             ?, ?, ?, 
             (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
             ?, ?
            )
            """;

    public static final String INSERT_PACKAGE_TRAVEL_TIP = """
            INSERT INTO package_travel_tips
            (package_id, tip_title, tip_description, display_order, status_id, created_by, updated_by)
            VALUES (
             ?, ?, ?, ?, 
             (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
             ?, ?
            )
            """;

    public static final String INSERT_PACKAGE_DAY_ACCOMMODATION = """
            INSERT INTO package_day_accommodation (
                package_id, day_number,
                breakfast, breakfast_description,
                lunch, lunch_description,
                dinner, dinner_description,
                morning_tea, morning_tea_description,
                evening_tea, evening_tea_description,
                snacks, snack_note,
                hotel_id,
                transport_id,
                other_notes
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;


    public static final String UPDATE_PACKAGE_BASIC_DETAILS = """
            UPDATE packages
            SET
                package_type_id = ?,
                tour_id = ?,
                name = ?,
                description = ?,
                total_price = ?,
                discount_percentage = ?,
                start_date = ?,
                end_date = ?,
                color = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                hover_color = ?,
                min_person_count = ?,
                max_person_count = ?,
                price_per_person = ?,
                updated_by = ?
            WHERE package_id = ?
            """;
    public static final String PACKAGE_EXCLUSION_REMOVE = """
            UPDATE tour_exclusion
            SET status_id = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE tour_exclusion_id = ?
            """;
    public static final String PACKAGE_CONDITION_REMOVE = """
            UPDATE package_condition
            SET status_id = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_condition_id = ?
            """;
    public static final String PACKAGE_INCLUSION_REMOVE = """
            UPDATE package_inclusion
            SET status_id = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_inclusion_id = ?
            """;
    public static final String PACKAGE_TRAVEL_TIPS_REMOVE = """
            UPDATE package_travel_tips
            SET status_id = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_travel_tip_id = ?
            """;
    public static final String PACKAGE_TRAVEL_TIP_UPDATE = """
            UPDATE package_travel_tips
            SET
                tip_title = ?,
                tip_description = ?,
                display_order = ?,
                status_id = (
                    SELECT cs.id
                    FROM common_status cs
                    WHERE cs.name = ?
                    LIMIT 1
                ),
                updated_by = ?
            WHERE package_travel_tip_id = ? AND package_id = ?
            """;

    public static final String UPDATE_PACKAGE_CONDITION = """
            UPDATE package_condition
            SET
                condition_text = ?,
                display_order = ?,
                status_id = (
                    SELECT cs.id
                    FROM common_status cs
                    WHERE cs.name = ?
                    LIMIT 1
                ),
                updated_by = ?
            WHERE package_condition_id = ? AND package_id = ?
            """;

    public static final String UPDATE_PACKAGE_EXCLUSION = """
            UPDATE package_exclusion
            SET
                exclusion_text = ?,
                display_order = ?,
                status_id = (
                    SELECT cs.id
                    FROM common_status cs
                    WHERE cs.name = ?
                    LIMIT 1
                ),
                updated_by = ?
            WHERE package_exclusion_id = ? AND package_id = ?
            """;

    public static final String UPDATE_PACKAGE_INCLUSION = """
            UPDATE package_inclusion
            SET
                inclusion_text = ?,
                display_order = ?,
                status_id = (
                    SELECT cs.id
                    FROM common_status cs
                    WHERE cs.name = ?
                    LIMIT 1
                ),
                updated_by = ?
            WHERE package_inclusion_id = ? AND package_id = ?
            """;


    public static final String PACKAGE_IMAGES_REMOVE = """
            UPDATE package_images
            SET status = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE id = ?
            """;

    public static final String UPDATE_PACKAGE_IMAGE = """
            UPDATE package_images
            SET
                name = ?,
                description = ?,
                image_url = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE id = ? AND package_id = ?
            """;

    public static final String PACKAGE_DAY_ACCOMMODATION_REMOVE = """
            UPDATE package_day_accommodation
            SET status = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_day_accommodation_id = ?
            """;
    public static final String UPDATE_PACKAGE_DAY_ACCOMMODATION = """
                UPDATE package_day_accommodation
                SET
                    day_number = ?,
                    breakfast = ?,
                    breakfast_description = ?,
                    lunch = ?,
                    lunch_description = ?,
                    dinner = ?,
                    dinner_description = ?,
                    morning_tea = ?,
                    morning_tea_description = ?,
                    evening_tea = ?,
                    evening_tea_description = ?,
                    snacks = ?,
                    snack_note = ?,
                    hotel_id = ?,
                    transport_id = ?,
                    other_notes = ?,
                    status = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                    updated_by = ?
                WHERE
                    package_day_accommodation_id = ?
                    AND package_id = ?
            """;

    public static final String INSERT_PACKAGE_FEATURE = """
            INSERT INTO package_features (
                package_id,
                name,
                value,
                description,
                status,
                color,
                hover_color,
                special_note,
                created_by
            ) VALUES (?, ?, ?, ?, (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1), ?, ?, ?, ?)
            """;

    public static final String PACKAGE_FEATURE_REMOVE = """
            UPDATE package_features
            SET status = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE id = ?
            """;

    public static final String UPDATE_PACKAGE_FEATURE = """
            UPDATE package_features SET
                name = ?,
                value = ?,
                description = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                color = ?,
                hover_color = ?,
                special_note = ?,
                updated_by = ?,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ? AND package_id = ?
            """;

    public static final String GET_PACKAGE_SUMMARY_STATISTICS = """
                SELECT
                    COUNT(DISTINCT p.package_id) AS total_packages,
                    COUNT(DISTINCT CASE WHEN p.status = 1 THEN p.package_id END) AS active_packages,
                    COALESCE(AVG(pr.rating), 0) AS average_package_rating,
                    COALESCE(SUM(ph.number_of_participate), 0) AS total_participants,
                    COALESCE(AVG(p.total_price), 0) AS average_package_price
                FROM packages p
                LEFT JOIN package_schedule ps
                    ON ps.package_id = p.package_id
                LEFT JOIN package_review pr
                    ON pr.package_schedule_id = ps.id
                LEFT JOIN package_history ph
                    ON ph.package_schedule_id = ps.id
                WHERE p.terminated_at IS NULL
            """;

    public static final String GET_PACKAGE_POPULARITY_STATISTICS = """
                SELECT
                    p.package_id,
                    p.name AS package_name,
                    COUNT(DISTINCT ps.id) AS total_schedules,
                    COALESCE(SUM(ph.number_of_participate), 0) AS total_participants
                FROM packages p
                LEFT JOIN package_schedule ps
                    ON ps.package_id = p.package_id
                LEFT JOIN package_history ph
                    ON ph.package_schedule_id = ps.id
                WHERE p.terminated_at IS NULL
                GROUP BY p.package_id, p.name
                ORDER BY total_participants DESC
            """;

    public static final String GET_PACKAGE_RATING_OVERVIEW_STATISTICS = """
                SELECT
                    p.package_id,
                    p.name AS package_name,
                    COALESCE(AVG(pr.rating), 0) AS average_rating,
                    COUNT(pr.id) AS total_reviews
                FROM packages p
                LEFT JOIN package_schedule ps
                    ON ps.package_id = p.package_id
                LEFT JOIN package_review pr
                    ON pr.package_schedule_id = ps.id
                WHERE p.terminated_at IS NULL
                GROUP BY p.package_id, p.name
                ORDER BY average_rating DESC
            """;

    public static final String GET_PACKAGE_CAPACITY_UTILIZATION_STATISTICS = """
                SELECT
                    p.package_id,
                    p.name AS package_name,
                    p.min_person_count,
                    p.max_person_count,
                    COALESCE(AVG(ph.number_of_participate), 0) AS average_participants
                FROM packages p
                LEFT JOIN package_schedule ps
                    ON ps.package_id = p.package_id
                LEFT JOIN package_history ph
                    ON ph.package_schedule_id = ps.id
                WHERE p.terminated_at IS NULL
                GROUP BY
                    p.package_id,
                    p.name,
                    p.min_person_count,
                    p.max_person_count
                ORDER BY average_participants DESC
            """;

    public static final String GET_PACKAGE_TYPE_DISTRIBUTION_STATISTICS = """
                SELECT
                    pt.name AS package_type_name,
                    COUNT(p.package_id) AS total_packages
                FROM package_type pt
                LEFT JOIN packages p
                    ON p.package_type_id = pt.id
                WHERE pt.terminated_at IS NULL
                GROUP BY pt.name
                ORDER BY total_packages DESC
            """;

    public static final String GET_PACKAGE_PRICE_DISTRIBUTION_STATISTICS = """
                SELECT
                    p.package_id,
                    p.name AS package_name,
                    p.total_price,
                    p.price_per_person,
                    COALESCE(SUM(ph.number_of_participate), 0) AS total_participants
                FROM packages p
                LEFT JOIN package_schedule ps
                    ON ps.package_id = p.package_id
                LEFT JOIN package_history ph
                    ON ph.package_schedule_id = ps.id
                WHERE p.terminated_at IS NULL
                GROUP BY
                    p.package_id,
                    p.name,
                    p.total_price,
                    p.price_per_person
                ORDER BY total_participants DESC
            """;

    public static final String GET_PACKAGE_SCHEDULE_SUMMARY_STATISTICS = """
                SELECT
                    COUNT(DISTINCT ps.id) AS total_schedules,
                    COUNT(DISTINCT CASE WHEN ps.status = 1 THEN ps.id END) AS active_schedules,
                    COALESCE(AVG(pr.rating), 0) AS average_schedule_rating,
                    COALESCE(SUM(ph.number_of_participate), 0) AS total_participants,
                    COALESCE(AVG((ps.duration_start + ps.duration_end) / 2), 0) AS average_duration
                FROM package_schedule ps
                LEFT JOIN package_review pr
                    ON pr.package_schedule_id = ps.id
                LEFT JOIN package_history ph
                    ON ph.package_schedule_id = ps.id
                WHERE ps.terminated_at IS NULL
            """;

    public static final String GET_PACKAGE_SCHEDULE_TIMELINE_STATISTICS = """
                SELECT
                    DATE_FORMAT(ps.assume_start_date, '%Y-%m') AS timeline,
                    COUNT(ps.id) AS total_schedules
                FROM package_schedule ps
                WHERE ps.terminated_at IS NULL
                GROUP BY DATE_FORMAT(ps.assume_start_date, '%Y-%m')
                ORDER BY timeline ASC
            """;

    public static final String GET_PACKAGE_SCHEDULE_STATUS_DISTRIBUTION_STATISTICS = """
                SELECT
                    ps.status AS status_id,
                    COUNT(ps.id) AS total_schedules
                FROM package_schedule ps
                WHERE ps.terminated_at IS NULL
                GROUP BY ps.status
                ORDER BY total_schedules DESC
            """;

    public static final String GET_PACKAGE_SCHEDULE_DURATION_DISTRIBUTION_STATISTICS = """
                SELECT
                    ps.id AS schedule_id,
                    ps.name AS schedule_name,
                    ps.duration_start,
                    ps.duration_end,
                    ((ps.duration_start + ps.duration_end) / 2) AS average_duration
                FROM package_schedule ps
                WHERE ps.terminated_at IS NULL
                ORDER BY average_duration DESC
            """;

    public static final String GET_PACKAGE_SCHEDULE_PARTICIPATION_PERFORMANCE_STATISTICS = """
                SELECT
                    ps.id AS schedule_id,
                    ps.name AS schedule_name,
                    COALESCE(SUM(ph.number_of_participate), 0) AS total_participants,
                    COALESCE(AVG(ph.number_of_participate), 0) AS average_participants
                FROM package_schedule ps
                LEFT JOIN package_history ph
                    ON ph.package_schedule_id = ps.id
                WHERE ps.terminated_at IS NULL
                GROUP BY ps.id, ps.name
                ORDER BY total_participants DESC
            """;

    public static final String GET_PACKAGE_SCHEDULE_RATING_OVERVIEW_STATISTICS = """
                SELECT
                    ps.id AS schedule_id,
                    ps.name AS schedule_name,
                    COALESCE(AVG(pr.rating), 0) AS average_rating,
                    COUNT(pr.id) AS total_reviews
                FROM package_schedule ps
                LEFT JOIN package_review pr
                    ON pr.package_schedule_id = ps.id
                WHERE ps.terminated_at IS NULL
                GROUP BY ps.id, ps.name
                ORDER BY average_rating DESC
            """;

    public static final String GET_PACKAGE_TYPE_SUMMARY = """
                SELECT
                    COUNT(DISTINCT pt.id) AS total_package_types,
                    COUNT(p.package_id) AS total_packages,
                    COALESCE(AVG(pr.rating), 0) AS average_rating,
                    COALESCE(SUM(p.total_price), 0) AS total_revenue
                FROM package_type pt
                LEFT JOIN packages p ON p.package_type_id = pt.id
                LEFT JOIN package_schedule ps ON ps.package_id = p.package_id
                LEFT JOIN package_review pr ON pr.package_schedule_id = ps.id
                WHERE pt.terminated_at IS NULL;
            """;

    public static final String GET_TYPE_DISTRIBUTION = """
                SELECT
                    pt.id AS type_id,
                    pt.name AS type_name,
                    COUNT(p.package_id) AS total_packages
                FROM package_type pt
                LEFT JOIN packages p ON p.package_type_id = pt.id
                WHERE pt.terminated_at IS NULL
                GROUP BY pt.id, pt.name
                ORDER BY total_packages DESC;
            """;

    public static final String GET_TYPE_REVENUE_PERFORMANCE = """
                SELECT
                    pt.id AS type_id,
                    pt.name AS type_name,
                    COALESCE(SUM(p.total_price), 0) AS total_revenue,
                    COALESCE(AVG(p.total_price), 0) AS avg_price
                FROM package_type pt
                LEFT JOIN packages p ON p.package_type_id = pt.id
                WHERE pt.terminated_at IS NULL
                GROUP BY pt.id, pt.name
                ORDER BY total_revenue DESC;
            """;

    public static final String GET_TYPE_PARTICIPATION_IMPACT = """
                SELECT
                    pt.name AS type_name,
                    DATE_FORMAT(ph.start_date, '%Y-%m') AS month,
                    COALESCE(SUM(ph.number_of_participate), 0) AS total_participants
                FROM package_type pt
                LEFT JOIN packages p ON p.package_type_id = pt.id
                LEFT JOIN package_schedule ps ON ps.package_id = p.package_id
                LEFT JOIN package_history ph ON ph.package_schedule_id = ps.id
                WHERE pt.terminated_at IS NULL
                GROUP BY pt.name, DATE_FORMAT(ph.start_date, '%Y-%m')
                ORDER BY month;
            """;

    public static final String GET_TYPE_PRIMARY_SECONDARY_USAGE = """
                SELECT
                    pt.id AS type_id,
                    pt.name AS type_name,
                    SUM(CASE WHEN ttm.is_primary = 1 THEN 1 ELSE 0 END) AS primary_count,
                    SUM(CASE WHEN ttm.is_primary = 0 THEN 1 ELSE 0 END) AS secondary_count
                FROM package_type pt
                LEFT JOIN tour_type_map ttm ON ttm.type_id = pt.id
                WHERE pt.terminated_at IS NULL
                GROUP BY pt.id, pt.name
                ORDER BY primary_count DESC;
            """;

    public static final String GET_TYPE_BOOKING_PERFORMANCE = """
                SELECT
                    pt.id AS type_id,
                    pt.name AS type_name,
                    COUNT(ps.id) AS schedule_count
                FROM package_type pt
                LEFT JOIN packages p ON p.package_type_id = pt.id
                LEFT JOIN package_schedule ps ON ps.package_id = p.package_id
                WHERE pt.terminated_at IS NULL
                GROUP BY pt.id, pt.name
                ORDER BY schedule_count DESC;
            """;

    public static final String GET_TYPE_RATING_OVERVIEW = """
                SELECT
                    pt.id AS type_id,
                    pt.name AS type_name,
                    COALESCE(AVG(pr.rating), 0) AS avg_rating,
                    COUNT(pr.id) AS total_reviews
                FROM package_type pt
                LEFT JOIN packages p ON p.package_type_id = pt.id
                LEFT JOIN package_schedule ps ON ps.package_id = p.package_id
                LEFT JOIN package_review pr ON pr.package_schedule_id = ps.id
                WHERE pt.terminated_at IS NULL
                GROUP BY pt.id, pt.name
                ORDER BY avg_rating DESC;
            """;

    public static final String REMOVE_ALL_PACKAGE_IMAGES = """
            UPDATE package_images
            SET status = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_id = ?
            AND terminated_at IS NULL
            """;

    public static final String REMOVE_ALL_PACKAGE_FEATURES = """
            UPDATE package_features
            SET status = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_id = ?
            AND terminated_at IS NULL
            """;

    public static final String REMOVE_ALL_DAY_ACCOMMODATIONS = """
            UPDATE package_day_accommodation
            SET status = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_id = ?
            AND terminated_at IS NULL
            """;

    public static final String REMOVE_ALL_PACKAGE_INCLUSIONS = """
            UPDATE package_inclusion
            SET status_id = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_id = ?
            AND terminated_at IS NULL
            """;

    public static final String REMOVE_ALL_PACKAGE_EXCLUSIONS = """
            UPDATE tour_exclusion
            SET status_id = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE tour_id = ?
            AND terminated_at IS NULL
            """;

    public static final String REMOVE_ALL_PACKAGE_CONDITIONS = """
            UPDATE package_condition
            SET status_id = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_id = ?
            AND terminated_at IS NULL
            """;

    public static final String REMOVE_ALL_PACKAGE_TRAVEL_TIPS = """
            UPDATE package_travel_tips
            SET status_id = ?,
                terminated_at = now(),
                terminated_by = ?
            WHERE package_id = ?
            AND terminated_at IS NULL
            """;

    public static final String GET_HOTEL_NAMES_AND_IDS = """
        SELECT
            service_provider_id AS hotel_id,
            name AS hotel_name,
            star_rating
        FROM service_provider
        WHERE service_provider_type_id = 1
          AND status_id = 1
        """;

    public static final String GET_VEHICLE_NUMBER_AND_TYPE = """
        SELECT
            v.vehicle_id,
            v.registration_number,
            vt.name AS vehicle_type,
            v.specification_id
        FROM vehicles v
        LEFT JOIN vehicle_type vt
            ON v.vehicle_type_id = vt.vehicle_type_id
        WHERE v.terminated_at IS NULL
        """;

    public static final String GET_TOUR_INCLUSIONS = """
        SELECT inclusion_text
        FROM tour_inclusion
        WHERE tour_id = ?
          AND terminated_at IS NULL
        ORDER BY display_order ASC
        """;
    public static final String GET_TOUR_EXCLUSIONS = """
        SELECT exclusion_text
        FROM tour_exclusion
        WHERE tour_id = ?
          AND terminated_at IS NULL
        ORDER BY display_order ASC
        """;

    public static final String GET_TOUR_CONDITIONS = """
        SELECT condition_text
        FROM tour_condition
        WHERE tour_id = ?
          AND terminated_at IS NULL
        ORDER BY display_order ASC
        """;

    public static final String GET_TOUR_TRAVEL_TIPS = """
        SELECT
            tip_title,
            tip_description
        FROM tour_travel_tips
        WHERE tour_id = ?
          AND terminated_at IS NULL
        ORDER BY display_order ASC
        """;


    public static final String BASE_PACKAGE_SCHEDULE_QUERY = """
    SELECT
        ps.id AS package_schedule_id,
        ps.name AS package_schedule_name,

        p.package_id,
        p.name AS package_name,

        ps.assume_start_date,
        ps.assume_end_date,
        ps.duration_start,
        ps.duration_end,

        ps.status,
        ps.special_note,
        ps.description,

        ps.tour_shedule_id,
        ts.name AS tour_schedule_name

    FROM package_schedule ps
    JOIN packages p ON p.package_id = ps.package_id
    LEFT JOIN tour_schedule ts ON ts.id = ps.tour_shedule_id
    WHERE 1=1
""";

    public static final String COUNT_PACKAGE_SCHEDULE_QUERY = """
    SELECT COUNT(*)
    FROM package_schedule ps
    JOIN packages p ON p.package_id = ps.package_id
    LEFT JOIN tour_schedule ts ON ts.id = ps.tour_shedule_id
    WHERE 1=1
""";

}
