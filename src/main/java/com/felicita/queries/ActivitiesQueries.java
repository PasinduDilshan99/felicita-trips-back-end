package com.felicita.queries;

import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;

public class ActivitiesQueries {

    public static final String GET_ALL_ACTIVITIES = """
                SELECT
                    a.id,
                    a.destination_id,
                    a.name,
                    a.description,
                    a.duration_hours,
                    a.available_from,
                    a.available_to,
                    a.price_local,
                    a.price_foreigners,
                    a.min_participate,
                    a.max_participate,
                    s.name AS season,
                    cs.name AS status_name,
                    a.created_at,
                    a.updated_at,
            
                    /* Categories */
                    (
                        SELECT COALESCE(JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'id', ac.id,
                                'name', ac.name,
                                'description', ac.description,
                                'is_primary', acm.is_primary
                            )
                        ), JSON_ARRAY())
                        FROM activity_category_map acm
                        JOIN activity_category ac ON ac.id = acm.category_id
                        WHERE acm.activity_id = a.id
                          AND acm.terminated_at IS NULL
                          AND ac.terminated_at IS NULL
                    ) AS categories,
            
                    /* Schedules */
                    (
                        SELECT COALESCE(JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'id', asch.id,
                                'name', asch.name,
                                'assume_start_date', asch.assume_start_date,
                                'assume_end_date', asch.assume_end_date,
                                'duration_hours_start', asch.duration_hours_start,
                                'duration_hours_end', asch.duration_hours_end,
                                'special_note', asch.special_note,
                                'description', asch.description,
                                'status', asch.status
                            )
                        ), JSON_ARRAY())
                        FROM activities_schedule asch
                        WHERE asch.activity_id = a.id
                          AND asch.terminated_at IS NULL
                    ) AS schedules,
            
                    /* Requirements */
                    (
                        SELECT COALESCE(JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'id', ar.id,
                                'name', ar.name,
                                'value', ar.value,
                                'description', ar.description,
                                'color', ar.color,
                                'status', ar.status
                            )
                        ), JSON_ARRAY())
                        FROM activities_requirement ar
                        WHERE ar.activity_id = a.id
                          AND ar.terminated_at IS NULL
                    ) AS requirements,
            
                    /* Images */
                    (
                        SELECT COALESCE(JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'id', ai.id,
                                'name', ai.name,
                                'description', ai.description,
                                'image_url', ai.image_url,
                                'status', ai.status
                            )
                        ), JSON_ARRAY())
                        FROM activities_images ai
                        WHERE ai.activity_id = a.id
                          AND ai.terminated_at IS NULL
                    ) AS images
            
                FROM activities a
                LEFT JOIN common_status cs ON a.status = cs.id
                LEFT JOIN seasons s s.id = a.seson_id
                WHERE a.terminated_at IS NULL
                LIMIT 1000
            """;

    public static final String GET_ALL_ACTIVITY_CATEGORIES = """
            SELECT
                ac.id AS category_id,
                ac.name AS category_name,
                ac.description AS category_description,
                ac.created_at AS category_created_at,
                ac.created_by AS category_created_by,
                ac.updated_at AS category_updated_at,
                ac.updated_by AS category_updated_by,
                ac.terminated_at AS category_terminated_at,
                ac.terminated_by AS category_terminated_by,
                ac.color,
                ac.hover_color, 
                cs1.name AS category_status,
            
                IFNULL(act.activity_count, 0) AS activities_count,
            
                aci.id AS image_id,
                aci.name AS image_name,
                aci.description AS image_description,
                aci.image_url,
                cs2.name AS image_status,
                aci.created_at AS image_created_at,
                aci.created_by AS image_created_by,
                aci.updated_at AS image_updated_at,
                aci.updated_by AS image_updated_by,
                aci.terminated_at AS image_terminated_at,
                aci.terminated_by AS image_terminated_by
            
            FROM activity_category ac
            
            LEFT JOIN (
                SELECT
                    acm.category_id,
                    COUNT(acm.activity_id) AS activity_count
                FROM activity_category_map acm
                INNER JOIN activities a
                    ON a.id = acm.activity_id
                WHERE acm.terminated_at IS NULL
                  AND a.terminated_at IS NULL
                GROUP BY acm.category_id
            ) act
                ON act.category_id = ac.id
            
            LEFT JOIN activity_category_images aci
                ON ac.id = aci.activity_category_id
            
            LEFT JOIN common_status cs1
                ON cs1.id = ac.status
            
            LEFT JOIN common_status cs2
                ON cs2.id = aci.status
            
            ORDER BY ac.id, aci.id
            """;


    public static final String GET_ACTIVITY_REVIEW_DETAILS = """
            SELECT
                ar.id AS review_id,
                ar.activity_schedule_id,
                a.id AS activity_id,
                a.name AS activity_name,
                ar.name AS review_name,
                ar.review,
                ar.rating,
                ar.description,
                cs_ar.name AS review_status,
                ar.number_of_participate,
                ar.created_by AS review_created_by,
                ar.created_at AS review_created_at,
                ar.updated_by AS review_updated_by,
                ar.updated_at AS review_updated_at,
                ari.id AS image_id,
                ari.name AS image_name,
                ari.description AS image_description,
                ari.image_url AS image_url,
                cs_ari.name AS image_status,
                ari.created_by AS image_created_by,
                ari.created_at AS image_created_at,
                arr.id AS review_reaction_id,
                arr.activity_review_id AS reaction_review_id,
                arr.user_id AS reaction_user_id,
                u1.username AS reaction_user_name,
                rt.name AS reaction_type,
                cs_arr.name AS review_reaction_status,
                arr.created_at AS reaction_created_at,
                arc.id AS comment_id,
                arc.activity_review_id AS comment_review_id,
                arc.user_id AS comment_user_id,
                u2.username AS comment_user_name,
                arc.parent_comment_id,
                arc.comment,
                cs_arc.name AS comment_status,
                arc.created_at AS comment_created_at,
                arc.created_by AS comment_created_by,
                arcr.id AS comment_reaction_id,
                arcr.comment_id AS comment_reaction_comment_id,
                arcr.user_id AS comment_reaction_user_id,
                u3.username AS comment_reaction_user_name,
                rt2.name AS comment_reaction_type,
                cs_arcr.name AS comment_reaction_status,
                arcr.created_by AS comment_reaction_created_by,
                arcr.created_at AS comment_reaction_created_at
            FROM activities_review ar
            LEFT JOIN activities_schedule ars
                ON ars.id = ar.activity_schedule_id
            LEFT JOIN activities a
                ON a.id = ars.activity_id
            LEFT JOIN common_status cs_ar
                ON cs_ar.id = ar.status
            LEFT JOIN activities_review_images ari
                ON ari.activities_review_id = ar.id
            LEFT JOIN common_status cs_ari
                ON cs_ari.id = ari.status
            LEFT JOIN activity_review_reaction arr
                ON arr.activity_review_id = ar.id
            LEFT JOIN reaction_type rt
                ON rt.id = arr.reaction_type_id
            LEFT JOIN user u1
                ON u1.user_id = arr.user_id
            LEFT JOIN common_status cs_arr
                ON cs_arr.id = arr.status
            LEFT JOIN activity_review_comment arc
                ON arc.activity_review_id = ar.id
            LEFT JOIN user u2
                ON u2.user_id = arc.user_id
            LEFT JOIN common_status cs_arc
                ON cs_arc.id = arc.status
            LEFT JOIN activity_review_comment_reaction arcr
                ON arcr.comment_id = arc.id
            LEFT JOIN reaction_type rt2
                ON rt2.id = arcr.reaction_type_id
            LEFT JOIN user u3
                ON u3.user_id = arcr.user_id
            LEFT JOIN common_status cs_arcr
                ON cs_arcr.id = arcr.status
            ORDER BY ar.id, arc.id, arcr.id
            """;

    public static final String GET_ACTIVITY_REVIEW_DETAILS_BY_ID = """
            SELECT
                ar.id AS review_id,
                ar.activity_schedule_id,
                a.id AS activity_id,
                a.name AS activity_name,
                ar.name AS review_name,
                ar.review,
                ar.rating,
                ar.description,
                cs_ar.name AS review_status,
                ar.number_of_participate,
                ar.created_by AS review_created_by,
                ar.created_at AS review_created_at,
                ar.updated_by AS review_updated_by,
                ar.updated_at AS review_updated_at,
                ari.id AS image_id,
                ari.name AS image_name,
                ari.description AS image_description,
                ari.image_url AS image_url,
                cs_ari.name AS image_status,
                ari.created_by AS image_created_by,
                ari.created_at AS image_created_at,
                arr.id AS review_reaction_id,
                arr.activity_review_id AS reaction_review_id,
                arr.user_id AS reaction_user_id,
                u1.username AS reaction_user_name,
                rt.name AS reaction_type,
                cs_arr.name AS review_reaction_status,
                arr.created_at AS reaction_created_at,
                arc.id AS comment_id,
                arc.activity_review_id AS comment_review_id,
                arc.user_id AS comment_user_id,
                u2.username AS comment_user_name,
                arc.parent_comment_id,
                arc.comment,
                cs_arc.name AS comment_status,
                arc.created_at AS comment_created_at,
                arc.created_by AS comment_created_by,
                arcr.id AS comment_reaction_id,
                arcr.comment_id AS comment_reaction_comment_id,
                arcr.user_id AS comment_reaction_user_id,
                u3.username AS comment_reaction_user_name,
                rt2.name AS comment_reaction_type,
                cs_arcr.name AS comment_reaction_status,
                arcr.created_by AS comment_reaction_created_by,
                arcr.created_at AS comment_reaction_created_at
            FROM activities_review ar
            LEFT JOIN activities_schedule ars
                ON ars.id = ar.activity_schedule_id
            LEFT JOIN activities a
                ON a.id = ars.activity_id
            LEFT JOIN common_status cs_ar
                ON cs_ar.id = ar.status
            LEFT JOIN activities_review_images ari
                ON ari.activities_review_id = ar.id
            LEFT JOIN common_status cs_ari
                ON cs_ari.id = ari.status
            LEFT JOIN activity_review_reaction arr
                ON arr.activity_review_id = ar.id
            LEFT JOIN reaction_type rt
                ON rt.id = arr.reaction_type_id
            LEFT JOIN user u1
                ON u1.user_id = arr.user_id
            LEFT JOIN common_status cs_arr
                ON cs_arr.id = arr.status
            LEFT JOIN activity_review_comment arc
                ON arc.activity_review_id = ar.id
            LEFT JOIN user u2
                ON u2.user_id = arc.user_id
            LEFT JOIN common_status cs_arc
                ON cs_arc.id = arc.status
            LEFT JOIN activity_review_comment_reaction arcr
                ON arcr.comment_id = arc.id
            LEFT JOIN reaction_type rt2
                ON rt2.id = arcr.reaction_type_id
            LEFT JOIN user u3
                ON u3.user_id = arcr.user_id
            LEFT JOIN common_status cs_arcr
                ON cs_arcr.id = arcr.status
            WHERE a.id = ?
            ORDER BY ar.id, arc.id, arcr.id
            """;

    public static final String GET_ACTIVITY_DETAILS_BY_ID = """
                SELECT
                    a.id,
                    a.destination_id,
                    a.name,
                    a.description,
                    a.duration_hours,
                    a.available_from,
                    a.available_to,
                    a.price_local,
                    a.price_foreigners,
                    a.min_participate,
                    a.max_participate,
                    s.id AS seasonId,
                    s.name AS season,
                    cs.name AS status_name,
                    a.created_at,
                    a.updated_at,
            
                    -- Categories (FULL OBJECTS)
                    (SELECT COALESCE(JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', ac.id,
                            'name', ac.name,
                            'description', ac.description,
                            'is_primary', acm.is_primary
                        )
                    ), JSON_ARRAY())
                    FROM activity_category_map acm
                    JOIN activity_category ac ON acm.category_id = ac.id
                    WHERE acm.activity_id = a.id
                      AND acm.terminated_at IS NULL
                      AND ac.status = 1
                    ) AS categories,
            
                    -- Schedules
                    (SELECT COALESCE(JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', asch.id,
                            'name', asch.name,
                            'assume_start_date', asch.assume_start_date,
                            'assume_end_date', asch.assume_end_date,
                            'duration_hours_start', asch.duration_hours_start,
                            'duration_hours_end', asch.duration_hours_end,
                            'special_note', asch.special_note,
                            'description', asch.description,
                            'status', asch.status
                        )
                    ), JSON_ARRAY())
                    FROM activities_schedule asch
                    WHERE asch.activity_id = a.id
                      AND asch.terminated_at IS NULL
                    ) AS schedules,
            
                    -- Requirements
                    (SELECT COALESCE(JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', ar.id,
                            'name', ar.name,
                            'value', ar.value,
                            'description', ar.description,
                            'color', ar.color,
                            'status', ar.status
                        )
                    ), JSON_ARRAY())
                    FROM activities_requirement ar
                    WHERE ar.activity_id = a.id
                      AND ar.terminated_at IS NULL
                    ) AS requirements,
            
                    -- Images
                    (SELECT COALESCE(JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', ai.id,
                            'name', ai.name,
                            'description', ai.description,
                            'image_url', ai.image_url,
                            'status', ai.status
                        )
                    ), JSON_ARRAY())
                    FROM activities_images ai
                    WHERE ai.activity_id = a.id
                      AND ai.terminated_at IS NULL
                    ) AS images
            
                FROM activities a
                LEFT JOIN seasons s ON s.id = a.season_id
                LEFT JOIN common_status cs ON a.status = cs.id
                WHERE a.terminated_at IS NULL
                  AND a.id = ?
            """;

    public static final String GET_ACTIVITY_HISTORY_DETAILS = """
                SELECT
                    ah.id AS history_id,
                    a.id AS activity_id,
                    a.name AS activity_name,
                    a.description AS activity_description,
            
                    -- FULL CATEGORY OBJECTS
                    (SELECT COALESCE(JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', ac.id,
                            'name', ac.name,
                            'description', ac.description,
                            'is_primary', acm.is_primary
                        )
                    ), JSON_ARRAY())
                     FROM activity_category_map acm
                     JOIN activity_category ac ON acm.category_id = ac.id
                     WHERE acm.activity_id = a.id 
                       AND acm.terminated_at IS NULL
                       AND ac.status = 1
                    ) AS activity_categories,
            
                    a.duration_hours,
                    a.available_from,
                    a.available_to,
                    a.price_local,
                    a.price_foreigners,
                    a.min_participate,
                    a.max_participate,
                    a.season,
            
                    d.destination_id,
                    d.name AS destination_name,
                    d.description AS destination_description,
                    d.location AS destination_location,
                    d.latitude,
                    d.longitude,
            
                    asch.id AS schedule_id,
                    asch.name AS schedule_name,
                    asch.description AS schedule_description,
                    asch.assume_start_date,
                    asch.assume_end_date,
                    asch.duration_hours_start,
                    asch.duration_hours_end,
                    asch.special_note AS schedule_special_note,
            
                    ah.name AS history_name,
                    ah.description AS history_description,
                    ah.number_of_participate,
                    ah.activity_start,
                    ah.activity_end,
                    ah.rating,
                    ah.special_note AS history_special_note,
                    cs_history.name AS history_status_name,
                    u_created.username AS history_created_by_username,
                    u_updated.username AS history_updated_by_username,
                    u_terminated.username AS history_terminated_by_username,
                    ah.created_at AS history_created_at,
                    ah.updated_at AS history_updated_at,
                    ah.terminated_at AS history_terminated_at,
            
                    ahi.id AS image_id,
                    ahi.name AS image_name,
                    ahi.description AS image_description,
                    ahi.image_url,
                    cs_image.name AS image_status_name,
                    ui_created.username AS image_created_by_username,
                    ui_updated.username AS image_updated_by_username,
                    ui_terminated.username AS image_terminated_by_username,
                    ahi.created_at AS image_created_at,
                    ahi.updated_at AS image_updated_at,
                    ahi.terminated_at AS image_terminated_at
            
                FROM activities_history ah
                JOIN activities_schedule asch ON ah.activity_schedule_id = asch.id
                JOIN activities a ON asch.activity_id = a.id
                LEFT JOIN destination d ON a.destination_id = d.destination_id
                LEFT JOIN common_status cs_history ON ah.status = cs_history.id
                LEFT JOIN user u_created ON ah.created_by = u_created.user_id
                LEFT JOIN user u_updated ON ah.updated_by = u_updated.user_id
                LEFT JOIN user u_terminated ON ah.terminated_by = u_terminated.user_id
                LEFT JOIN activities_history_images ahi ON ahi.activities_history_id = ah.id
                LEFT JOIN common_status cs_image ON ahi.status = cs_image.id
                LEFT JOIN user ui_created ON ahi.created_by = ui_created.user_id
                LEFT JOIN user ui_updated ON ahi.updated_by = ui_updated.user_id
                LEFT JOIN user ui_terminated ON ahi.terminated_by = ui_terminated.user_id
                ORDER BY a.id, asch.id, ah.activity_start, ahi.id
            """;

    public static final String GET_ACTIVITY_HISTORY_DETAILS_BY_ID = """
            SELECT
                ah.id AS history_id,
                a.id AS activity_id,
                a.name AS activity_name,
                a.description AS activity_description,
                (SELECT COALESCE(JSON_ARRAYAGG(ac.name), JSON_ARRAY())
                 FROM activity_category_map acm
                 JOIN activity_category ac ON acm.category_id = ac.id
                 WHERE acm.activity_id = a.id
                 AND acm.terminated_at IS NULL
                 AND ac.status = 1
                ) AS activity_categories,
                a.duration_hours,
                a.available_from,
                a.available_to,
                a.price_local,
                a.price_foreigners,
                a.min_participate,
                a.max_participate,
                a.season,
                d.destination_id,
                d.name AS destination_name,
                d.description AS destination_description,
                d.location AS destination_location,
                d.latitude,
                d.longitude,
                asch.id AS schedule_id,
                asch.name AS schedule_name,
                asch.description AS schedule_description,
                asch.assume_start_date,
                asch.assume_end_date,
                asch.duration_hours_start,
                asch.duration_hours_end,
                asch.special_note AS schedule_special_note,
                ah.name AS history_name,
                ah.description AS history_description,
                ah.number_of_participate,
                ah.activity_start,
                ah.activity_end,
                ah.rating,
                ah.special_note AS history_special_note,
                cs_history.name AS history_status_name,
                u_created.username AS history_created_by_username,
                u_updated.username AS history_updated_by_username,
                u_terminated.username AS history_terminated_by_username,
                ah.created_at AS history_created_at,
                ah.updated_at AS history_updated_at,
                ah.terminated_at AS history_terminated_at,
                ahi.id AS image_id,
                ahi.name AS image_name,
                ahi.description AS image_description,
                ahi.image_url,
                cs_image.name AS image_status_name,
                ui_created.username AS image_created_by_username,
                ui_updated.username AS image_updated_by_username,
                ui_terminated.username AS image_terminated_by_username,
                ahi.created_at AS image_created_at,
                ahi.updated_at AS image_updated_at,
                ahi.terminated_at AS image_terminated_at
            FROM activities_history ah
            JOIN activities_schedule asch ON ah.activity_schedule_id = asch.id
            JOIN activities a ON asch.activity_id = a.id
            LEFT JOIN destination d ON a.destination_id = d.destination_id
            LEFT JOIN common_status cs_history ON ah.status = cs_history.id
            LEFT JOIN user u_created ON ah.created_by = u_created.user_id
            LEFT JOIN user u_updated ON ah.updated_by = u_updated.user_id
            LEFT JOIN user u_terminated ON ah.terminated_by = u_terminated.user_id
            LEFT JOIN activities_history_images ahi ON ahi.activities_history_id = ah.id
            LEFT JOIN common_status cs_image ON ahi.status = cs_image.id
            LEFT JOIN user ui_created ON ahi.created_by = ui_created.user_id
            LEFT JOIN user ui_updated ON ahi.updated_by = ui_updated.user_id
            LEFT JOIN user ui_terminated ON ahi.terminated_by = ui_terminated.user_id
            WHERE a.id = ?
            ORDER BY a.id, asch.id, ah.activity_start, ahi.id
            """;

    public static final String GET_ACTIVITY_HISTORY_IMAGES = """
            SELECT
                ahi.id AS image_id,
                ahi.name AS image_name,
                ahi.description AS image_description,
                ahi.image_url,
                cs_image.name AS image_status_name,
                ui_created.username AS image_created_by_username,
                ui_updated.username AS image_updated_by_username,
                ui_terminated.username AS image_terminated_by_username,
                ahi.created_at AS image_created_at,
                ahi.updated_at AS image_updated_at,
                ahi.terminated_at AS image_terminated_at,
                ah.id AS history_id,
                ah.name AS history_name,
                ah.description AS history_description,
                ah.number_of_participate,
                ah.activity_start,
                ah.activity_end,
                ah.rating,
                ah.special_note AS history_special_note,
                cs_history.name AS history_status_name,
                asch.id AS schedule_id,
                asch.name AS schedule_name,
                asch.description AS schedule_description,
                asch.assume_start_date,
                asch.assume_end_date,
                asch.duration_hours_start,
                asch.duration_hours_end,
                asch.special_note AS schedule_special_note,
                a.id AS activity_id,
                a.name AS activity_name,
                a.description AS activity_description,
                a.activities_category AS activity_category,
                a.duration_hours,
                a.price_local,
                a.price_foreigners,
                a.min_participate,
                a.max_participate
            FROM activities_history_images ahi
            JOIN activities_history ah ON ahi.activities_history_id = ah.id
            JOIN activities_schedule asch ON ah.activity_schedule_id = asch.id
            JOIN activities a ON asch.activity_id = a.id
            LEFT JOIN common_status cs_image ON ahi.status = cs_image.id
            LEFT JOIN common_status cs_history ON ah.status = cs_history.id
            LEFT JOIN user ui_created ON ahi.created_by = ui_created.user_id
            LEFT JOIN user ui_updated ON ahi.updated_by = ui_updated.user_id
            LEFT JOIN user ui_terminated ON ahi.terminated_by = ui_terminated.user_id
            ORDER BY a.id, asch.id, ah.activity_start, ahi.id
            """;

    public static final String GET_ACTIVITY_HISTORY_IMAGES_BY_ID = """
            SELECT
                ahi.id AS image_id,
                ahi.name AS image_name,
                ahi.description AS image_description,
                ahi.image_url,
                cs_image.name AS image_status_name,
                ui_created.username AS image_created_by_username,
                ui_updated.username AS image_updated_by_username,
                ui_terminated.username AS image_terminated_by_username,
                ahi.created_at AS image_created_at,
                ahi.updated_at AS image_updated_at,
                ahi.terminated_at AS image_terminated_at,
                ah.id AS history_id,
                ah.name AS history_name,
                ah.description AS history_description,
                ah.number_of_participate,
                ah.activity_start,
                ah.activity_end,
                ah.rating,
                ah.special_note AS history_special_note,
                cs_history.name AS history_status_name,
                asch.id AS schedule_id,
                asch.name AS schedule_name,
                asch.description AS schedule_description,
                asch.assume_start_date,
                asch.assume_end_date,
                asch.duration_hours_start,
                asch.duration_hours_end,
                asch.special_note AS schedule_special_note,
                a.id AS activity_id,
                a.name AS activity_name,
                a.description AS activity_description,
                a.activities_category AS activity_category,
                a.duration_hours,
                a.price_local,
                a.price_foreigners,
                a.min_participate,
                a.max_participate
            FROM activities_history_images ahi
            JOIN activities_history ah ON ahi.activities_history_id = ah.id
            JOIN activities_schedule asch ON ah.activity_schedule_id = asch.id
            JOIN activities a ON asch.activity_id = a.id
            LEFT JOIN common_status cs_image ON ahi.status = cs_image.id
            LEFT JOIN common_status cs_history ON ah.status = cs_history.id
            LEFT JOIN user ui_created ON ahi.created_by = ui_created.user_id
            LEFT JOIN user ui_updated ON ahi.updated_by = ui_updated.user_id
            LEFT JOIN user ui_terminated ON ahi.terminated_by = ui_terminated.user_id
            WHERE a.id = ?
            ORDER BY a.id, asch.id, ah.activity_start, ahi.id
            """;

    public static final String GET_ACTIVITY_IDS_WITH_FILTERS = """
            SELECT a.id
            FROM activities a
            LEFT JOIN common_status cs ON a.status = cs.id
            WHERE a.terminated_at IS NULL
              AND (? IS NULL OR a.name LIKE CONCAT('%', ?, '%'))
              AND (? IS NULL OR a.price_local >= ?)
              AND (? IS NULL OR a.price_local <= ?)
              AND (? IS NULL OR a.duration_hours = ?)
              AND (? IS NULL OR a.season_id = (SELECT s.id FROM seasons s WHERE s.name = ? ))
              AND (? IS NULL OR cs.name = ?)
              AND (? IS NULL OR EXISTS (
                  SELECT 1 FROM activity_category_map acm
                  JOIN activity_category ac ON acm.category_id = ac.id
                  WHERE acm.activity_id = a.id 
                  AND acm.terminated_at IS NULL
                  AND LOWER(ac.name) = LOWER(?)
              ))
            """;

    public static final String GET_ACTIVITY_COUNT_WITH_FILTERS = """
            SELECT COUNT(DISTINCT a.id)
            FROM activities a
            LEFT JOIN common_status cs ON a.status = cs.id
            WHERE a.terminated_at IS NULL
              AND (? IS NULL OR a.name LIKE CONCAT('%', ?, '%'))
              AND (? IS NULL OR a.price_local >= ?)
              AND (? IS NULL OR a.price_local <= ?)
              AND (? IS NULL OR a.duration_hours = ?)
              AND (? IS NULL OR a.season_id = (SELECT s.id FROM seasons s WHERE s.name = ? ))
              AND (? IS NULL OR cs.name = ?)
              AND (? IS NULL OR EXISTS (
                  SELECT 1 FROM activity_category_map acm
                  JOIN activity_category ac ON acm.category_id = ac.id
                  WHERE acm.activity_id = a.id 
                  AND acm.terminated_at IS NULL
                  AND ac.name = ?
              ))
            """;


    public static final String GET_ACTIVITIES_BY_IDS = """
            SELECT
                a.id,
                a.destination_id,
                d.name AS destination_name,
                a.name,
                a.description,
                a.duration_hours,
                a.available_from,
                a.available_to,
                a.price_local,
                a.price_foreigners,
                a.min_participate,
                a.max_participate,
                s.name AS season,
                cs.name AS status_name,
                a.created_at,
                a.updated_at,
                -- Get all categories as JSON array
                (SELECT COALESCE(JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'id', ac.id,
                        'name', ac.name,
                        'description', ac.description,
                        'is_primary', acm.is_primary
                    )
                ), JSON_ARRAY())
                FROM activity_category_map acm
                JOIN activity_category ac ON acm.category_id = ac.id
                WHERE acm.activity_id = a.id 
                AND acm.terminated_at IS NULL
                AND ac.status = 1
                ) AS categories,
            
                -- Get schedules as JSON array
                CASE
                    WHEN COUNT(asch.id) > 0 THEN
                        JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'id', asch.id,
                                'name', asch.name,
                                'assume_start_date', asch.assume_start_date,
                                'assume_end_date', asch.assume_end_date,
                                'duration_hours_start', asch.duration_hours_start,
                                'duration_hours_end', asch.duration_hours_end,
                                'special_note', asch.special_note,
                                'description', asch.description,
                                'status', asch.status
                            )
                        )
                    ELSE JSON_ARRAY()
                END AS schedules,
            
                -- Get requirements as JSON array
                (
                    SELECT COALESCE(JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', ar.id,
                            'name', ar.name,
                            'value', ar.value,
                            'description', ar.description,
                            'color', ar.color,
                            'status', ar.status
                        )
                    ), JSON_ARRAY())
                    FROM activities_requirement ar
                    WHERE ar.activity_id = a.id AND ar.terminated_at IS NULL
                ) AS requirements,
            
                -- Get images as JSON array
                (
                    SELECT COALESCE(JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', ai.id,
                            'name', ai.name,
                            'description', ai.description,
                            'image_url', ai.image_url,
                            'status', ai.status
                        )
                    ), JSON_ARRAY())
                    FROM activities_images ai
                    WHERE ai.activity_id = a.id AND ai.terminated_at IS NULL
                ) AS images
            
            FROM activities a
            LEFT JOIN common_status cs ON a.status = cs.id
            LEFT JOIN seasons s ON s.id = a.season_id
            LEFT JOIN destination d ON d.destination_id = a.destination_id
            LEFT JOIN activities_schedule asch ON asch.activity_id = a.id
                AND asch.terminated_at IS NULL
            WHERE a.id IN (%s)
            GROUP BY a.id
            """;

    public static final String GET_ACTIVE_ACTIVITIES_FOR_TERMINATE = """
                        SELECT
            	a.id,
                a.name
            FROM activities a
            LEFT JOIN common_status cs
            	ON cs.id = a.status
            WHERE cs.name = ?
            """;

    public static final String ACTIVITY_TERMINATE = """
            UPDATE activities
            SET status = (SELECT id FROM common_status WHERE name = ? LIMIT 1),terminated_at = now(), terminated_by = ?
            WHERE id = ?
            """;
    public static final String INSERT_ACTIVITY_BASIC_DETAILS = """
                        INSERT INTO activities
                        (destination_id, name, description, activities_category,
                         duration_hours, available_from, available_to,
                         price_local, price_foreigners,
                         min_participate, max_participate,
                         season, status, created_by)
                        VALUES (?, ?, ?,(SELECT ac.id FROM activity_category ac WHERE ac.name = ? LIMIT 1), ?, ?, ?, ?, ?, ?, ?, ?,
                         (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1)
                         , ?)
            """;
    public static final String INSERT_ACTIVITY_IMAGE = """
            INSERT INTO activities_images (activity_id, name, description, image_url, status, created_by) VALUES
            (?,?,?,?, (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),?)""";
    public static final String INSERT_ACTIVITY_REQUIREMENTS = """
            INSERT INTO activities_requirement (activity_id, name, value, description, status, color, created_by) 
            VALUES (?,?,?,?, (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),?,?)
            """;
    public static final String UPDATE_BASIC_ACTIVITY_DETAILS = """
            UPDATE activities
            SET
                destination_id = ?,
                name = ?,
                description = ?,
                activities_category = ?,
                duration_hours = ?,
                available_from = ?,
                available_to = ?,
                price_local = ?,
                price_foreigners = ?,
                min_participate = ?,
                max_participate = ?,
                season = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE id = ?
            """;
    public static final String ACTIVITY_IMAGES_REMOVE = """
            UPDATE activities_images
            SET status = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                terminated_at = now(),
                terminated_by = ?
            WHERE id = ?
            """;
    public static final String ACTIVITY_REQUIREMENTS_REMOVE = """
            UPDATE activities_requirement
            SET status = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                terminated_at = now(),
                terminated_by = ?
            WHERE id = ?
            """;
    public static final String UPDATE_ACTIVITY_IMAGE = """
            UPDATE activities_images
            SET
                name = ?,
                description = ?,
                image_url = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE id = ? AND activity_id = ?
            """;

    public static final String UPDATE_ACTIVITY_REQUIREMENT = """
            UPDATE activities_requirement
            SET
                name = ?,
                value = ?,
                description = ?,
                color = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE id = ? AND activity_id = ?
            """;


    public static final String GET_ACTIVITY_DETAILS_STATISTICS = """
            SELECT
                COUNT(*) AS totalActivityCount,
                SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS activeActivities,
                SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS inActiveActivities,
                SUM(CASE
                    WHEN terminated_at IS NOT NULL THEN 1
                    ELSE 0
                END) AS hiddenActivities,
                SUM(CASE
                    WHEN updated_at >= NOW() - INTERVAL 7 DAY THEN 1
                    ELSE 0
                END) AS recentlyUpdatedActivities,
                SUM(CASE
                    WHEN created_at >= NOW() - INTERVAL 7 DAY THEN 1
                    ELSE 0
                END) AS recentlyAddedActivities
            FROM activities
            """;


    public static final String GET_ACTIVITY_WISH_STATISTICS = """
            SELECT
                COUNT(DISTINCT activity_id) AS wishListCount,
                (SELECT COUNT(*) FROM activities)
                  - COUNT(DISTINCT activity_id) AS notWishListCount
            FROM activity_wishlist
            WHERE status_id = 1
            """;


    public static final String GET_ACTIVITY_CATEGORY_STATISTICS = """
            SELECT
                ac.id AS category_id,
                ac.name AS category_name,
                COUNT(acm.activity_id) AS activity_count
            FROM activity_category ac
            LEFT JOIN activity_category_map acm
                ON ac.id = acm.category_id
                AND acm.status = 1
            LEFT JOIN activities a
                ON a.id = acm.activity_id
                AND a.status = 1
            WHERE ac.status = 1
            GROUP BY ac.id, ac.name
            ORDER BY activity_count DESC
            """;

    public static final String GET_ACTIVITY_SCHEDULE_SUMMARY_STATISTICS = """
                SELECT 
                    (SELECT COUNT(*) 
                     FROM activities 
                     WHERE terminated_at IS NULL) AS total_activities,
            
                    (SELECT COUNT(*) 
                     FROM activities_schedule 
                     WHERE terminated_at IS NULL) AS active_schedules,
            
                    (SELECT COALESCE(SUM(number_of_participate), 0)
                     FROM activities_history 
                     WHERE terminated_at IS NULL) AS total_participants,
            
                    (SELECT ROUND(COALESCE(AVG(rating), 0), 2)
                     FROM activities_review 
                     WHERE terminated_at IS NULL) AS overall_average_rating
            """;


    public static final String GET_ACTIVITY_PARTICIPATION_TREND_STATISTICS = """
                SELECT 
                    DATE(activity_start) AS activity_date,
                    SUM(number_of_participate) AS total_participants
                FROM activities_history
                WHERE terminated_at IS NULL
                GROUP BY DATE(activity_start)
                ORDER BY activity_date
            """;


    public static final String GET_ACTIVITY_RATING_OVERVIEW_STATISTICS = """
                SELECT 
                    a.id AS activity_id,
                    a.name AS activity_name,
                    ROUND(AVG(ar.rating), 2) AS average_rating,
                    COUNT(ar.id) AS total_reviews
                FROM activities a
                INNER JOIN activities_schedule aps 
                    ON aps.activity_id = a.id
                INNER JOIN activities_review ar 
                    ON ar.activity_schedule_id = aps.id
                WHERE 
                    a.terminated_at IS NULL
                    AND aps.terminated_at IS NULL
                    AND ar.terminated_at IS NULL
                GROUP BY a.id, a.name
                ORDER BY average_rating DESC
            """;


    public static final String GET_POPULAR_ACTIVITIES_STATISTICS = """
                SELECT 
                    a.id AS activity_id,
                    a.name AS activity_name,
                    SUM(ah.number_of_participate) AS total_participants
                FROM activities a
                INNER JOIN activities_schedule aps 
                    ON aps.activity_id = a.id
                INNER JOIN activities_history ah 
                    ON ah.activity_schedule_id = aps.id
                WHERE 
                    a.terminated_at IS NULL
                    AND aps.terminated_at IS NULL
                    AND ah.terminated_at IS NULL
                GROUP BY a.id, a.name
                ORDER BY total_participants DESC
                LIMIT 10
            """;


    public static final String GET_SCHEDULE_TIMELINE_STATISTICS = """
                SELECT 
                    aps.id AS schedule_id,
                    aps.name AS schedule_name,
                    a.name AS activity_name,
                    aps.assume_start_date,
                    aps.assume_end_date,
                    aps.duration_hours_start,
                    aps.duration_hours_end,
                    aps.special_note,
                    aps.status
                FROM activities_schedule aps
                INNER JOIN activities a 
                    ON a.id = aps.activity_id
                WHERE 
                    aps.terminated_at IS NULL
                ORDER BY aps.assume_start_date ASC
            """;


    public static final String GET_ACTIVITY_STATUS_DISTRIBUTION_STATISTICS = """
                SELECT 
                    cs.name AS status_name,
                    COUNT(aps.id) AS total_count
                FROM activities_schedule aps
                INNER JOIN common_status cs 
                    ON cs.id = aps.status
                WHERE 
                    aps.terminated_at IS NULL
                GROUP BY cs.name
                ORDER BY total_count DESC
            """;

    public static final String GET_ACTIVITY_CATEGORY_SUMMARY_STATISTICS = """
                SELECT 
                    (SELECT COUNT(*) 
                     FROM activity_category 
                     WHERE terminated_at IS NULL) AS total_categories,
            
                    (SELECT COUNT(DISTINCT m.activity_id)
                     FROM activity_category_map m
                     WHERE m.terminated_at IS NULL) AS total_activities,
            
                    (SELECT c.name
                     FROM activity_category c
                     INNER JOIN activity_category_map m ON m.category_id = c.id
                     WHERE c.terminated_at IS NULL
                     GROUP BY c.id, c.name
                     ORDER BY COUNT(m.activity_id) DESC
                     LIMIT 1) AS most_used_category,
            
                    (SELECT ROUND(COALESCE(AVG(ar.rating), 0), 2)
                     FROM activities_review ar
                     WHERE ar.terminated_at IS NULL) AS overall_average_rating
            """;

    public static final String GET_CATEGORY_ACTIVITY_COUNT_STATISTICS = """
                SELECT 
                    c.id AS category_id,
                    c.name AS category_name,
                    COUNT(DISTINCT m.activity_id) AS total_activities
                FROM activity_category c
                LEFT JOIN activity_category_map m 
                    ON m.category_id = c.id
                WHERE c.terminated_at IS NULL
                GROUP BY c.id, c.name
                ORDER BY total_activities DESC
            """;

    public static final String GET_CATEGORY_PARTICIPATION_PERFORMANCE_STATISTICS = """
                SELECT 
                    c.id AS category_id,
                    c.name AS category_name,
                    COALESCE(SUM(ah.number_of_participate), 0) AS total_participants
                FROM activity_category c
                INNER JOIN activity_category_map m 
                    ON m.category_id = c.id
                INNER JOIN activities_schedule s 
                    ON s.activity_id = m.activity_id
                LEFT JOIN activities_history ah 
                    ON ah.activity_schedule_id = s.id
                WHERE 
                    c.terminated_at IS NULL
                    AND m.terminated_at IS NULL
                    AND s.terminated_at IS NULL
                    AND (ah.terminated_at IS NULL OR ah.terminated_at IS NULL)
                GROUP BY c.id, c.name
                ORDER BY total_participants DESC
            """;

    public static final String GET_CATEGORY_RATING_OVERVIEW_STATISTICS = """
                SELECT 
                    c.id AS category_id,
                    c.name AS category_name,
                    ROUND(COALESCE(AVG(ar.rating), 0), 2) AS average_rating,
                    COUNT(ar.id) AS total_reviews
                FROM activity_category c
                INNER JOIN activity_category_map m 
                    ON m.category_id = c.id
                INNER JOIN activities_schedule s 
                    ON s.activity_id = m.activity_id
                LEFT JOIN activities_review ar 
                    ON ar.activity_schedule_id = s.id
                WHERE 
                    c.terminated_at IS NULL
                    AND m.terminated_at IS NULL
                    AND s.terminated_at IS NULL
                    AND (ar.terminated_at IS NULL OR ar.terminated_at IS NULL)
                GROUP BY c.id, c.name
                ORDER BY average_rating DESC
            """;

    public static final String GET_CATEGORY_DISTRIBUTION_STATISTICS = """
                SELECT 
                    c.name AS category_name,
                    COUNT(m.activity_id) AS activity_count
                FROM activity_category c
                LEFT JOIN activity_category_map m 
                    ON m.category_id = c.id
                WHERE c.terminated_at IS NULL
                GROUP BY c.name
            """;


    public static final String GET_CATEGORY_PRIMARY_SECONDARY_USAGE_STATISTICS = """
                SELECT 
                    c.name AS category_name,
                    SUM(CASE WHEN m.is_primary = 1 THEN 1 ELSE 0 END) AS primary_count,
                    SUM(CASE WHEN m.is_primary = 0 THEN 1 ELSE 0 END) AS secondary_count
                FROM activity_category c
                INNER JOIN activity_category_map m 
                    ON m.category_id = c.id
                WHERE 
                    c.terminated_at IS NULL
                    AND m.terminated_at IS NULL
                GROUP BY c.name
                ORDER BY primary_count DESC
            """;


}
