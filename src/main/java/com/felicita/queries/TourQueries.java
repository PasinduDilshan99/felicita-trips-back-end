package com.felicita.queries;

public class TourQueries {

    public static final String GET_ALL_TOURS = """
            SELECT
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration,
                t.latitude,
                t.longitude,
                t.start_location,
                t.end_location,
            
                s.name AS season_name,
                s.description AS season_description,
                cs.name AS status_name,
                (
                    SELECT MIN(p.total_price)
                    FROM packages p
                    WHERE p.tour_id = t.tour_id
                ) AS min_price,
            
                -- Tour Types
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'tourTypeId', tt.id,
                            'tourTypeName', tt.name,
                            'tourTypeDescription', tt.description
                        )
                    )
                    FROM tour_type_map ttm
                    JOIN tour_type tt ON tt.id = ttm.type_id
                    WHERE ttm.tour_id = t.tour_id
                ) AS tour_types,
            
                -- Tour Categories
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'tourCategoryId', tc.id,
                            'tourCategoryName', tc.name,
                            'tourCategoryDescription', tc.description
                        )
                    )
                    FROM tour_category_map tcm
                    JOIN tour_category tc ON tc.id = tcm.category_id
                    WHERE tcm.tour_id = t.tour_id
                ) AS tour_categories,
            
                -- Schedules
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'scheduleId', sch.id,
                            'scheduleName', sch.name,
                            'assumeStartDate', sch.assume_start_date,
                            'assumeEndDate', sch.assume_end_date,
                            'durationStart', sch.duration_start,
                            'durationEnd', sch.duration_end,
                            'specialNote', sch.special_note,
                            'scheduleDescription', sch.description
                        )
                    )
                    FROM tour_schedule sch
                    WHERE sch.tour_id = t.tour_id
                ) AS schedules,
            
                -- Images
                (
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'imageId', img.id,
                            'imageName', img.name,
                            'imageDescription', img.description,
                            'imageUrl', img.image_url
                        )
                    )
                    FROM tour_images img
                    WHERE img.tour_id = t.tour_id
                ) AS images
            
            FROM tour t
            LEFT JOIN seasons s ON t.season = s.id
            LEFT JOIN common_status cs ON t.status = cs.id
            """;

    public static final String GET_PAGINATED_TOUR_IDS = """
            SELECT t.tour_id
            FROM tour t
            LEFT JOIN common_status cs ON t.status = cs.id
            LEFT JOIN seasons s ON t.season = s.id
            LEFT JOIN packages p ON p.tour_id = t.tour_id
            WHERE cs.name = 'ACTIVE'
              AND (? IS NULL OR t.name LIKE CONCAT('%', ?, '%'))
              AND (? IS NULL OR t.duration = ?)
              AND (? IS NULL OR (t.start_location LIKE CONCAT('%', ?, '%')
                   OR t.end_location LIKE CONCAT('%', ?, '%')))
              AND (? IS NULL OR EXISTS (
                   SELECT 1
                   FROM tour_category_map tcm
                   JOIN tour_category tc2 ON tcm.category_id = tc2.id
                   WHERE tcm.tour_id = t.tour_id
                     AND tc2.name = ?
               ))
              AND (? IS NULL OR s.name = ?)
              AND (? IS NULL OR EXISTS (
                   SELECT 1
                   FROM tour_type_map ttm
                   JOIN tour_type tt2 ON ttm.type_id = tt2.id
                   WHERE ttm.tour_id = t.tour_id
                     AND tt2.name = ?
               ))
               AND (
                      (? IS NULL AND ? IS NULL)
                      OR (p.total_price BETWEEN ? AND ?)
                 )
            LIMIT ? OFFSET ?
            """;


    public static final String GET_TOURS_BY_IDS = """
                SELECT
                    t.tour_id,
                    t.name AS tour_name,
                    t.description AS tour_description,
                    t.duration,
                    t.latitude,
                    t.longitude,
                    t.start_location,
                    t.end_location,
            
                    -- Tour Types
                    tt.id AS tour_type_id,
                    tt.name AS tour_type_name,
                    tt.description AS tour_type_description,
            
                    -- Tour Categories
                    tc.id AS tour_category_id,
                    tc.name AS tour_category_name,
                    tc.description AS tour_category_description,
            
                    s.name AS season_name,
                    s.description AS season_description,
                    cs.name AS status_name,
            
                    -- Schedules
                    sch.id AS schedule_id,
                    sch.name AS schedule_name,
                    sch.assume_start_date,
                    sch.assume_end_date,
                    sch.duration_start,
                    sch.duration_end,
                    sch.special_note,
                    sch.description AS schedule_description,
            
                    -- Images
                    img.id AS image_id,
                    img.name AS image_name,
                    img.description AS image_description,
                    img.image_url
            
                FROM tour t
                LEFT JOIN tour_type_map ttm ON t.tour_id = ttm.tour_id
                LEFT JOIN tour_type tt ON ttm.type_id = tt.id
                LEFT JOIN tour_category_map tcm ON t.tour_id = tcm.tour_id
                LEFT JOIN tour_category tc ON tcm.category_id = tc.id
                LEFT JOIN seasons s ON t.season = s.id
                LEFT JOIN common_status cs ON t.status = cs.id
                LEFT JOIN tour_schedule sch ON t.tour_id = sch.tour_id
                LEFT JOIN tour_images img ON t.tour_id = img.tour_id
                WHERE t.tour_id IN (%s)  -- will replace with comma-separated IDs
            """;

    public static final String COUNT_TOURS_WITH_FILTER = """
            SELECT COUNT(DISTINCT t.tour_id)
            FROM tour t
            LEFT JOIN common_status cs ON t.status = cs.id
            LEFT JOIN seasons s ON t.season = s.id
            WHERE cs.name = 'ACTIVE'
              AND (? IS NULL OR t.name LIKE CONCAT('%', ?, '%'))
              AND (? IS NULL OR t.duration = ?)
              AND (? IS NULL OR (t.start_location LIKE CONCAT('%', ?, '%')
                   OR t.end_location LIKE CONCAT('%', ?, '%')))
              AND (? IS NULL OR EXISTS (
                   SELECT 1
                   FROM tour_category_map tcm
                   JOIN tour_category tc2 ON tcm.category_id = tc2.id
                   WHERE tcm.tour_id = t.tour_id
                     AND tc2.name = ?
               ))
              AND (? IS NULL OR s.name = ?)
              AND (? IS NULL OR EXISTS (
                   SELECT 1
                   FROM tour_type_map ttm
                   JOIN tour_type tt2 ON ttm.type_id = tt2.id
                   WHERE ttm.tour_id = t.tour_id
                     AND tt2.name = ?
               ))
                AND (
                    (? IS NULL AND ? IS NULL)
                    OR (
                        SELECT MIN(p.total_price)
                        FROM packages p
                        WHERE p.tour_id = t.tour_id
                    ) BETWEEN ? AND ?
                )
            """;


    public static final String GET_TOUR_DETAILS_BY_ID = """
                SELECT
                    t.tour_id,
                    t.name AS tour_name,
                    t.description AS tour_description,
                    t.duration,
                    t.latitude,
                    t.longitude,
                    t.start_location,
                    t.end_location,
            
                    -- Tour Types
                    tt.id AS tour_type_id,
                    tt.name AS tour_type_name,
                    tt.description AS tour_type_description,
            
                    -- Tour Categories
                    tc.id AS tour_category_id,
                    tc.name AS tour_category_name,
                    tc.description AS tour_category_description,
            
                    s.name AS season_name,
                    s.description AS season_description,
                    cs.name AS status_name,
            
                    -- Schedules
                    sch.id AS schedule_id,
                    sch.name AS schedule_name,
                    sch.assume_start_date,
                    sch.assume_end_date,
                    sch.duration_start,
                    sch.duration_end,
                    sch.special_note,
                    sch.description AS schedule_description,
            
                    -- Images
                    img.id AS image_id,
                    img.name AS image_name,
                    img.description AS image_description,
                    img.image_url
            
                FROM tour t
                LEFT JOIN tour_type_map ttm ON t.tour_id = ttm.tour_id
                LEFT JOIN tour_type tt ON ttm.type_id = tt.id
                LEFT JOIN tour_category_map tcm ON t.tour_id = tcm.tour_id
                LEFT JOIN tour_category tc ON tcm.category_id = tc.id
                LEFT JOIN seasons s ON t.season = s.id
                LEFT JOIN common_status cs ON t.status = cs.id
                LEFT JOIN tour_schedule sch ON t.tour_id = sch.tour_id
                LEFT JOIN tour_images img ON t.tour_id = img.tour_id
                WHERE t.tour_id = ?
            """;


    public static final String GET_POPULAR_TOURS = """
              SELECT
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration AS tour_duration,
                t.latitude,
                t.longitude,
                t.start_location,
                t.end_location,
                s.name AS season,
                cs_t.name AS tour_status,
            
                -- Tour Types (one-to-many)
                tt.id AS tour_type_id,
                tt.name AS tour_type_name,
                tt.description AS tour_type_description,
            
                -- Tour Categories (one-to-many)
                tc.id AS tour_category_id,
                tc.name AS tour_category_name,
                tc.description AS tour_category_description,
            
                -- Schedule
                ts.id AS schedule_id,
                ts.name AS schedule_name,
                ts.assume_start_date,
                ts.assume_end_date,
                ts.duration_start,
                ts.duration_end,
                ts.special_note,
                ts.description AS schedule_description,
                cs_ts.name AS schedule_status,
            
                -- Destination
                d.destination_id,
                d.name AS destination_name,
                d.description AS destination_description,
                d.location AS destination_location,
                cs_dest.name AS destination_status,
            
                -- Reviews (rating > 4)
                tr.id AS review_id,
                tr.name AS reviewer_name,
                tr.review,
                tr.rating,
                tr.description AS review_description,
                tr.number_of_participate,
                cs_tr.name AS review_status,
                tr.created_at AS review_created_at,
            
                -- Images
                ti.image_url AS tour_image,
                ti.name AS tour_image_name
            
            FROM tour t
            LEFT JOIN tour_type_map ttm ON t.tour_id = ttm.tour_id
            LEFT JOIN tour_type tt ON ttm.type_id = tt.id
            LEFT JOIN tour_category_map tcm ON t.tour_id = tcm.tour_id
            LEFT JOIN tour_category tc ON tcm.category_id = tc.id
            LEFT JOIN seasons s ON t.season = s.id
            LEFT JOIN common_status cs_t ON t.status = cs_t.id
            LEFT JOIN tour_schedule ts ON t.tour_id = ts.tour_id
            LEFT JOIN common_status cs_ts ON ts.status = cs_ts.id
            LEFT JOIN tour_destination td ON t.tour_id = td.tour_id
            LEFT JOIN destination d ON td.destination_id = d.destination_id
            LEFT JOIN common_status cs_dest ON d.status = cs_dest.id
            LEFT JOIN tour_review tr ON ts.id = tr.tour_schedule_id AND tr.rating > 4.0
            LEFT JOIN common_status cs_tr ON tr.status = cs_tr.id
            LEFT JOIN tour_images ti ON t.tour_id = ti.tour_id
            ORDER BY t.tour_id, tr.rating DESC
            """;


    public static final String GET_TOUR_REVIEWS_DETAILS = """
            SELECT
                tr.id AS review_id,
                ts.id AS tour_schedule_id,
                t.tour_id AS tour_id,
                t.name AS tour_name,
                tr.name AS review_name,
                tr.review,
                tr.rating,
                tr.description AS review_description,
                cs_tr.name AS review_status,
                tr.number_of_participate,
                tr.created_by AS review_created_by,
                u_review.username AS review_created_user,
                tr.created_at AS review_created_at,
                tr.updated_by AS review_updated_by,
                tr.updated_at AS review_updated_at,
                tri.id AS image_id,
                tri.name AS image_name,
                tri.description AS image_description,
                tri.image_url AS image_url,
                cs_tri.name AS image_status,
                tri.created_by AS image_created_by,
                tri.created_at AS image_created_at,
                trr.id AS review_reaction_id,
                trr.tour_review_id AS reaction_review_id,
                trr.user_id AS reaction_user_id,
                u_reaction.username AS reaction_user_name,
                rt.name AS reaction_type,
                cs_trr.name AS review_reaction_status,
                trr.created_at AS reaction_created_at,
                trc.id AS comment_id,
                trc.tour_review_id AS comment_review_id,
                trc.user_id AS comment_user_id,
                u_comment.username AS comment_user_name,
                trc.parent_comment_id,
                trc.comment,
                cs_trc.name AS comment_status,
                trc.created_at AS comment_created_at,
                trc.created_by AS comment_created_by,
                trcr.id AS comment_reaction_id,
                trcr.comment_id AS comment_reaction_comment_id,
                trcr.user_id AS comment_reaction_user_id,
                u_comment_react.username AS comment_reaction_user_name,
                rt2.name AS comment_reaction_type,
                cs_trcr.name AS comment_reaction_status,
                trcr.created_by AS comment_reaction_created_by,
                trcr.created_at AS comment_reaction_created_at
            FROM tour_review tr
            LEFT JOIN tour_schedule ts
                ON tr.tour_schedule_id = ts.id
            LEFT JOIN tour t
                ON ts.tour_id = t.tour_id
            LEFT JOIN common_status cs_tr
                ON cs_tr.id = tr.status
            LEFT JOIN tour_review_images tri
                ON tri.tour_review_id = tr.id
            LEFT JOIN common_status cs_tri
                ON cs_tri.id = tri.status
            LEFT JOIN tour_review_reaction trr
                ON trr.tour_review_id = tr.id
            LEFT JOIN reaction_type rt
                ON rt.id = trr.reaction_type_id
            LEFT JOIN user u_reaction
                ON u_reaction.user_id = trr.user_id
            LEFT JOIN common_status cs_trr
                ON cs_trr.id = trr.status
            LEFT JOIN tour_review_comment trc
                ON trc.tour_review_id = tr.id
            LEFT JOIN user u_comment
                ON u_comment.user_id = trc.user_id
            LEFT JOIN common_status cs_trc
                ON cs_trc.id = trc.status
            LEFT JOIN tour_review_comment_reaction trcr
                ON trcr.comment_id = trc.id
            LEFT JOIN reaction_type rt2
                ON rt2.id = trcr.reaction_type_id
            LEFT JOIN user u_comment_react
                ON u_comment_react.user_id = trcr.user_id
            LEFT JOIN common_status cs_trcr
                ON cs_trcr.id = trcr.status
            LEFT JOIN user u_review
                ON u_review.user_id = tr.created_by
            ORDER BY
                tr.id,
                trc.id,
                trcr.id
            """;

    public static final String GET_TOUR_REVIEW_DETAILS_BY_ID = """
            SELECT
                tr.id AS review_id,
                ts.id AS tour_schedule_id,
                t.tour_id AS tour_id,
                t.name AS tour_name,
                tr.name AS review_name,
                tr.review,
                tr.rating,
                tr.description AS review_description,
                cs_tr.name AS review_status,
                tr.number_of_participate,
                tr.created_by AS review_created_by,
                u_review.username AS review_created_user,
                tr.created_at AS review_created_at,
                tr.updated_by AS review_updated_by,
                tr.updated_at AS review_updated_at,
                tri.id AS image_id,
                tri.name AS image_name,
                tri.description AS image_description,
                tri.image_url AS image_url,
                cs_tri.name AS image_status,
                tri.created_by AS image_created_by,
                tri.created_at AS image_created_at,
                trr.id AS review_reaction_id,
                trr.tour_review_id AS reaction_review_id,
                trr.user_id AS reaction_user_id,
                u_reaction.username AS reaction_user_name,
                rt.name AS reaction_type,
                cs_trr.name AS review_reaction_status,
                trr.created_at AS reaction_created_at,
                trc.id AS comment_id,
                trc.tour_review_id AS comment_review_id,
                trc.user_id AS comment_user_id,
                u_comment.username AS comment_user_name,
                trc.parent_comment_id,
                trc.comment,
                cs_trc.name AS comment_status,
                trc.created_at AS comment_created_at,
                trc.created_by AS comment_created_by,
                trcr.id AS comment_reaction_id,
                trcr.comment_id AS comment_reaction_comment_id,
                trcr.user_id AS comment_reaction_user_id,
                u_comment_react.username AS comment_reaction_user_name,
                rt2.name AS comment_reaction_type,
                cs_trcr.name AS comment_reaction_status,
                trcr.created_by AS comment_reaction_created_by,
                trcr.created_at AS comment_reaction_created_at
            FROM tour_review tr
            LEFT JOIN tour_schedule ts
                ON tr.tour_schedule_id = ts.id
            LEFT JOIN tour t
                ON ts.tour_id = t.tour_id
            LEFT JOIN common_status cs_tr
                ON cs_tr.id = tr.status
            LEFT JOIN tour_review_images tri
                ON tri.tour_review_id = tr.id
            LEFT JOIN common_status cs_tri
                ON cs_tri.id = tri.status
            LEFT JOIN tour_review_reaction trr
                ON trr.tour_review_id = tr.id
            LEFT JOIN reaction_type rt
                ON rt.id = trr.reaction_type_id
            LEFT JOIN user u_reaction
                ON u_reaction.user_id = trr.user_id
            LEFT JOIN common_status cs_trr
                ON cs_trr.id = trr.status
            LEFT JOIN tour_review_comment trc
                ON trc.tour_review_id = tr.id
            LEFT JOIN user u_comment
                ON u_comment.user_id = trc.user_id
            LEFT JOIN common_status cs_trc
                ON cs_trc.id = trc.status
            LEFT JOIN tour_review_comment_reaction trcr
                ON trcr.comment_id = trc.id
            LEFT JOIN reaction_type rt2
                ON rt2.id = trcr.reaction_type_id
            LEFT JOIN user u_comment_react
                ON u_comment_react.user_id = trcr.user_id
            LEFT JOIN common_status cs_trcr
                ON cs_trcr.id = trcr.status
            LEFT JOIN user u_review
                ON u_review.user_id = tr.created_by
            WHERE t.tour_id = ?
            ORDER BY
                tr.id,
                trc.id,
                trcr.id
            """;


    public static final String GET_TOUR_DESTINATIONS_FOR_MAP = """
            SELECT
                d.destination_id AS id,
                d.name,
                d.description,
                d.latitude AS lat,
                d.longitude AS lng,
                di.id AS image_id,
                di.image_url AS image_url,
                di.name AS image_name,
                di.description AS image_description
            FROM
                tour_destination td
            JOIN
                destination d ON td.destination_id = d.destination_id
            LEFT JOIN
                destination_images di ON d.destination_id = di.destination_id
            WHERE
                td.tour_id = ?
            """;


    public static final String GET_ALL_TOUR_HISTORY_DETAILS = """
            SELECT
                th.id AS history_id,
                th.name AS history_name,
                th.description AS history_description,
                th.number_of_participate,
                th.rating,
                th.duration AS history_duration,
                th.start_date,
                th.end_date,
                th.vehicle_number,
                th.driver_id,
                th.guide_id,
                th.color AS history_color,
                th.hover_color,
                th.status AS history_status,
                ts.id AS schedule_id,
                ts.name AS schedule_name,
                ts.assume_start_date,
                ts.assume_end_date,
                ts.duration_start,
                ts.duration_end,
                ts.special_note,
                ts.description AS schedule_description,
                ts.status AS schedule_status,
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration AS tour_duration,
                t.latitude,
                t.longitude,
                t.start_location,
                t.end_location,
                t.status AS tour_status,
                t.tour_type,
                t.tour_category,
                t.season,
                thimg.id AS image_id,
                thimg.name AS image_name,
                thimg.description AS image_description,
                thimg.image_url,
                thimg.color AS image_color,
                thimg.status AS image_status
            FROM tour_history th
            INNER JOIN tour_schedule ts ON th.tour_schedule_id = ts.id
            INNER JOIN tour t ON ts.tour_id = t.tour_id
            LEFT JOIN tour_history_images thimg ON ts.id = thimg.tour_schedule_id
            ORDER BY th.id, thimg.id;
            """;

    public static final String GET_TOUR_HISTORY_DETAILS_BY_ID = """
            SELECT
                th.id AS history_id,
                th.name AS history_name,
                th.description AS history_description,
                th.number_of_participate,
                th.rating,
                th.duration AS history_duration,
                th.start_date,
                th.end_date,
                th.vehicle_number,
                th.driver_id,
                th.guide_id,
                th.color AS history_color,
                th.hover_color,
                th.status AS history_status,
                ts.id AS schedule_id,
                ts.name AS schedule_name,
                ts.assume_start_date,
                ts.assume_end_date,
                ts.duration_start,
                ts.duration_end,
                ts.special_note,
                ts.description AS schedule_description,
                ts.status AS schedule_status,
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration AS tour_duration,
                t.latitude,
                t.longitude,
                t.start_location,
                t.end_location,
                t.status AS tour_status,
                t.tour_type,
                t.tour_category,
                t.season,
                thimg.id AS image_id,
                thimg.name AS image_name,
                thimg.description AS image_description,
                thimg.image_url,
                thimg.color AS image_color,
                thimg.status AS image_status
            FROM tour_history th
            INNER JOIN tour_schedule ts ON th.tour_schedule_id = ts.id
            INNER JOIN tour t ON ts.tour_id = t.tour_id
            LEFT JOIN tour_history_images thimg ON ts.id = thimg.tour_schedule_id
            WHERE t.tour_id =?
            ORDER BY th.id, thimg.id
            """;

    public static final String GET_ALL_TOUR_HISTORY_IMAGES = """
            SELECT
                thi.id AS image_id,
                thi.name,
                thi.description,
                thi.image_url,
                thi.color,
                cs.name AS status,
                thi.created_at,
                thi.created_by,
                thi.updated_at,
                thi.updated_by,
                thi.terminated_at,
                thi.terminated_by
            FROM tour_history_images thi
            LEFT JOIN common_status cs ON thi.status = cs.id
            ORDER BY thi.created_at DESC
            """;

    public static final String GET_ALL_TOUR_HISTORY_IMAGES_BY_ID = """
            SELECT
                thi.id AS image_id,
                thi.name,
                thi.description,
                thi.image_url,
                thi.color,
                cs.name AS status,
                thi.created_at,
                thi.created_by,
                thi.updated_at,
                thi.updated_by,
                thi.terminated_at,
                thi.terminated_by
            FROM tour_history_images thi
            INNER JOIN tour_schedule ts ON thi.tour_schedule_id = ts.id
            INNER JOIN tour t ON ts.tour_id = t.tour_id
            LEFT JOIN common_status cs ON thi.status = cs.id
            WHERE t.tour_id = ?
            ORDER BY thi.created_at DESC
            """;

    public static final String GET_ALL_TOUR_DAY_DESTINATION_ACTIVITY_IDS = """
            SELECT
                day,
                destination_id,
                GROUP_CONCAT(activities_id ORDER BY activities_id) AS activity_ids
            FROM tour_destination
            WHERE tour_id = ?
            GROUP BY day, destination_id
            ORDER BY day, destination_id
            """;

    public static final String GET_DESTINATIONS_DETAILS_WITH_FOR_DAY_IDS = """
            SELECT
                d.destination_id,
                d.name AS destination_name,
                d.description AS destination_description,
                d.location,
                d.latitude,
                d.longitude,
                CONCAT(u1.first_name, ' ', u1.last_name) AS created_by,
                u1.image_url AS creater_image,
                d.created_at,
                d.updated_at,
                CONCAT(u2.first_name, ' ', u2.last_name) AS updated_by,
                u2.image_url AS updater_image,
                cs.name AS status_name,
                dcm.id AS category_map_id,
                dcm.is_primary AS category_is_primary,
                dc.id AS category_id,
                dc.category AS category_name,
                dc.description AS category_description,
                di.id AS image_id,
                di.name AS image_name,
                di.description AS image_description,
                di.image_url
            FROM destination d
            LEFT JOIN common_status cs ON d.status = cs.id
            LEFT JOIN destination_category_map dcm ON d.destination_id = dcm.destination_id
            LEFT JOIN destination_categories dc ON dcm.category_id = dc.id
            LEFT JOIN destination_images di ON d.destination_id = di.destination_id
            LEFT JOIN user u1 ON u1.user_id = d.created_by
            LEFT JOIN user u2 ON u2.user_id = d.updated_by
            WHERE d.destination_id IN (:destinationIds)
            """;

    public static final String GET_ACTIVITIES_DETAILS_BASE = """
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
                a.season_id,
                MAX(cs.name) AS status_name,
                a.created_at,
                a.updated_at,
            
                -- category info from junction table
                acm.id AS activity_category_map_id,
                ac.id AS category_id,
                ac.name AS category_name,
                ac.description AS category_description,
                acm.is_primary AS category_is_primary,
            
                (
                    SELECT COALESCE(
                        JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'id', ar.id,
                                'name', ar.name,
                                'value', ar.value,
                                'description', ar.description,
                                'color', ar.color,
                                'status', ar.status
                            )
                        ),
                        JSON_ARRAY()
                    )
                    FROM activities_requirement ar
                    WHERE ar.activity_id = a.id
                      AND ar.terminated_at IS NULL
                ) AS requirements,
            
                (
                    SELECT COALESCE(
                        JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'id', ai.id,
                                'name', ai.name,
                                'description', ai.description,
                                'image_url', ai.image_url,
                                'status', ai.status
                            )
                        ),
                        JSON_ARRAY()
                    )
                    FROM activities_images ai
                    WHERE ai.activity_id = a.id
                      AND ai.terminated_at IS NULL
                ) AS images
            
            FROM activities a
            LEFT JOIN common_status cs ON a.status = cs.id
            LEFT JOIN activity_category_map acm ON a.id = acm.activity_id
            LEFT JOIN activity_category ac ON acm.category_id = ac.id
            LEFT JOIN seasons s ON s.id = a.season_id
            WHERE a.terminated_at IS NULL
            GROUP BY
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
                a.season,
                a.status,
                a.created_at,
                a.updated_at,
                acm.id,
                ac.id,
                ac.name,
                ac.description,
                acm.is_primary
            """;


    public static final String GET_TOUR_ACCOMMODATIONS_BY_TOUR_ID = """
            SELECT
                tda.tour_day_accommodation_id,
                tda.tour_id,
                tda.day,
                tda.breakfast,
                tda.breakfast_description,
                tda.lunch,
                tda.lunch_description,
                tda.dinner,
                tda.dinner_description,
                tda.morning_tea,
                tda.morning_tea_description,
                tda.evening_tea,
                tda.evening_tea_description,
                tda.snacks,
                tda.snack_note,
                tda.other_notes,
                sp.service_provider_id AS hotel_id,
                sp.name AS hotel_name,
                sp.description AS hotel_description,
                sp.star_rating AS hotel_category,
                sp.address AS hotel_location,
                spl.latitude AS hotel_latitude,
                spl.longitude AS hotel_longitude,
                v.vehicle_id AS transport_id,
                v.registration_number AS vehicle_registration_number,
            	vt.name,
                vs.model AS vehicle_model,
                vs.seat_capacity,
                vs.air_condition
            FROM tour_day_accommodation tda
            LEFT JOIN service_provider sp
                ON tda.hotel_id = sp.service_provider_id
            LEFT JOIN vehicles v
                ON tda.transport_id = v.vehicle_id
            LEFT JOIN vehicle_specifications vs
                ON v.specification_id = vs.specification_id
            LEFT JOIN service_provider_location spl
            	ON sp.service_provider_id = spl.service_provider_id
            LEFT JOIN vehicle_type vt
            	ON vt.vehicle_type_id = v.vehicle_type_id
            WHERE tda.tour_id = ?
            ORDER BY tda.day ASC
            """;
    public static final String GET_TOUR_INCLUSIONS_BY_TOUR_ID = """
            SELECT
                ti.tour_inclusion_id,
                ti.inclusion_text AS description,
                ti.display_order,
                cs.name AS status
            FROM tour_inclusion ti
            JOIN common_status cs ON cs.id = ti.status_id
            WHERE ti.tour_id = ?
              AND cs.name = 'ACTIVE'
            ORDER BY ti.display_order ASC
            """;
    public static final String GET_TOUR_EXCLUSIONS_BY_TOUR_ID = """
            SELECT
                te.tour_exclusion_id,
                te.exclusion_text AS description,
                te.display_order,
                cs.name AS status
            FROM tour_exclusion te
            JOIN common_status cs ON cs.id = te.status_id
            WHERE te.tour_id = ?
              AND cs.name = 'ACTIVE'
            ORDER BY te.display_order ASC
            """;
    public static final String GET_TOUR_CONDITIONS_BY_TOUR_ID = """
            SELECT
                tc.tour_condition_id,
                tc.condition_text AS description,
                tc.display_order,
                cs.name AS status
            FROM tour_condition tc
            JOIN common_status cs ON cs.id = tc.status_id
            WHERE tc.tour_id = ?
              AND cs.name = 'ACTIVE'
            ORDER BY tc.display_order ASC
            """;
    public static final String GET_TOUR_TRAVEL_TIPS_BY_TOUR_ID = """
            SELECT
                ttt.tour_travel_tip_id,
                ttt.tip_title,
                ttt.tip_description,
                ttt.display_order,
                cs.name AS status
            FROM tour_travel_tips ttt
            JOIN common_status cs ON cs.id = ttt.status_id
            WHERE ttt.tour_id = ?
              AND cs.name = 'ACTIVE'
            ORDER BY ttt.display_order ASC
            """;
    public static final String GET_TOUR_SCHEDULES_BY_TOUR_ID = """
            SELECT
                ts.id AS schedule_id,
                ts.name AS schedule_name,
                ts.assume_start_date,
                ts.assume_end_date,
                ts.duration_start,
                ts.duration_end,
                ts.special_note,
                ts.description,
                cs.id AS status_id,
                cs.name AS status_name,
                ts.created_at,
                ts.updated_at
            FROM tour_schedule ts
            JOIN common_status cs ON ts.status = cs.id
            WHERE ts.tour_id = ?
              AND cs.name = 'ACTIVE'
            """;

    public static final String GET_TOUR_BASIC_DETAILS_BY_TOUR_ID = """
            SELECT
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration,
                t.latitude,
                t.longitude,
                t.start_location,
                t.end_location,
                ti.id AS image_id,
                ti.name AS image_name,
                ti.description AS image_description,
                ti.image_url,
                cs_tour.name AS tour_status,
                cs_img.name AS image_status
            FROM tour t
            LEFT JOIN tour_images ti
                   ON t.tour_id = ti.tour_id
                   AND ti.status = (
                       SELECT id FROM common_status WHERE name = 'ACTIVE'
                   )
            JOIN common_status cs_tour
                   ON t.status = cs_tour.id
            LEFT JOIN common_status cs_img
                   ON ti.status = cs_img.id
            WHERE t.tour_id = ?
              AND cs_tour.name = 'ACTIVE'
            """;
    public static final String GET_ALL_TOURS_BASIC_DETAILS = """
            SELECT
                t.tour_id,
                t.name,
                t.description,
                tt.id AS type_id,
                tt.name AS type_name,
                tc.id AS category_id,
                tc.name AS category_name,
                t.duration,
                t.latitude,
                t.longitude,
                t.start_location,
                t.end_location,
                s.name AS season,
                cs.name AS status,
                ti.id AS image_id,
                ti.name AS image_name,
                ti.description AS image_description,
                ti.image_url AS image_url
            FROM tour t
            LEFT JOIN common_status cs
                ON cs.id = t.status
            LEFT JOIN tour_type_map ttm
                ON ttm.tour_id = t.tour_id
            LEFT JOIN tour_type tt
                ON ttm.type_id = tt.id
            LEFT JOIN tour_category_map tcm
                ON tcm.tour_id = t.tour_id
            LEFT JOIN tour_category tc
                ON tcm.category_id = tc.id
            LEFT JOIN seasons s
                ON s.id = t.season
            LEFT JOIN tour_images ti
                ON ti.tour_id = t.tour_id
            WHERE cs.name = 'ACTIVE'
            ORDER BY t.tour_id
            """;
    public static final String GET_ACTIVE_TOURS_FOR_TERMINATE = """
            SELECT
            	t.tour_id,
                t.name
            FROM tour t
            LEFT JOIN common_status cs
            	ON cs.id = t.status
            WHERE cs.name = ?
            """;
    public static final String TOUR_TERMINATE = """
            UPDATE tour
            SET status = (SELECT id FROM common_status WHERE name = ? LIMIT 1),terminated_at = now(), terminated_by = ?
            WHERE tour_id = ?
            """;
    public static final String INSERT_TOUR_BASIC_DETAILS = """
            INSERT INTO tour (name, description, tour_type, tour_category, duration, latitude, longitude, start_location, end_location, season, status, created_by,assign_to,assign_message) 
            VALUES (?,?,?,?,?,?,?,?,?,?,(SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),?,?,?)
            """;

    public static final String INSERT_TOUR_TRAVEL_TIP = """
            INSERT INTO tour_travel_tips
            (tour_id, tip_title, tip_description, display_order, status_id, created_by)
            VALUES (?,?,?,?,(SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),?)
            """;


    public static final String INSERT_TOUR_EXCLUSION = """
            INSERT INTO tour_exclusion
            (tour_id, exclusion_text, display_order, status_id, created_by)
            VALUES (?,?,?,(SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),?)
            """;
    public static final String INSERT_TOUR_CONDITION = """
            INSERT INTO tour_condition
            (tour_id, condition_text, display_order, status_id, created_by)
            VALUES (?,?,?,(SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),?)
            """;

    public static final String INSERT_TOUR_INCLUSION = """
            INSERT INTO tour_inclusion
            (tour_id, inclusion_text, display_order, status_id, created_by)
            VALUES (?,?,?,(SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),?)
            """;


    public static final String INSERT_TOUR_DESTINATION = """
            INSERT INTO tour_destination
            (tour_id, destination_id, activities_id, day)
            VALUES (?,?,?,?)
            """;

    public static final String INSERT_TOUR_IMAGE = """
            INSERT INTO tour_images
            (tour_id, name, description, image_url, status, created_by)
            VALUES (?,?,?,?,(SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),?)
            """;


    public static final String TOUR_DETAILS_FOR_ADD_PACKAGE = """
            SELECT
            	t.tour_id,
                t.name,
                t.description,
                tt.name,
                tc.name,
                t.start_location,
                t.end_location,
            	cs.name,
                s.name,
                u.user_id,
                u.first_name,
                u.last_name,
                u.username,
                t.assign_message,
                td.day,
                d.destination_id,
                d.name,
                d.description,
                a.id AS activity_id,
                a.name,
                a.description
            FROM tour t
            LEFT JOIN tour_type tt
            	ON tt.id = t.tour_type
            LEFT JOIN tour_category tc
            	ON tc.id = t.tour_category
            LEFT JOIN common_status cs
            	ON cs.id = t.status
            LEFT JOIN seasons s
            	ON s.id = t.season
            LEFT JOIN user u
            	ON u.user_id = t.assign_to
            LEFT JOIN tour_destination td
            	ON t.tour_id = td.tour_id
            LEFT JOIN destination d
            	ON d.destination_id = td.destination_id
            LEFt JOIn activities a
            	ON a.id = td.activities_id
            WHERE t.tour_id = ?
            """;

    public static final String GET_TOUR_INCLUSIONS_NAMES = """
            SELECT ti.inclusion_text
            FROM tour t
            LEFT JOIN tour_inclusion ti
                ON t.tour_id = ti.tour_id
            WHERE t.tour_id = ?
            """;

    public static final String GET_TOUR_EXCLUSIONS_NAMES = """
            SELECT te.exclusion_text
            FROM tour t
            LEFT JOIN tour_exclusion te
                ON t.tour_id = te.tour_id
            WHERE t.tour_id = ?
            """;

    public static final String GET_TOUR_CONDITIONS_NAMES = """
            SELECT tc.condition_text
            FROM tour t
            LEFT JOIN tour_condition tc
                ON tc.tour_id = t.tour_id
            WHERE t.tour_id = ?
            """;

    public static final String GET_TOUR_TRAVEL_TIPS = """
            SELECT ttt.tip_title, ttt.tip_description
            FROM tour t
            LEFT JOIN tour_travel_tips ttt
                ON ttt.tour_id = t.tour_id
            WHERE t.tour_id = ?
            """;


    public static final String UPDATE_TOUR_BASIC_DETAILS = """
            UPDATE tour
            SET
                name = ?,
                description = ?,
                tour_type = ?,
                tour_category = ?,
                duration = ?,
                latitude = ?,
                longitude = ?,
                start_location = ?,
                end_location = ?,
                season = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?,
                updated_at = CURRENT_TIMESTAMP,
                assign_to = ?,
                assign_message = ?
            WHERE tour_id = ?
            """;

    public static final String TOUR_IMAGES_REMOVE = """
            UPDATE tour_images
            SET status = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                terminated_at = now(),
                terminated_by = ?
            WHERE id = ?
            """;
    public static final String TOUR_INCLUSION_REMOVE = """
            UPDATE tour_inclusion
            SET status_id = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                terminated_at = now(),
                terminated_by = ?
            WHERE tour_inclusion_id = ?
            """;
    public static final String TOUR_EXCLUSION_REMOVE = """
            UPDATE tour_exclusion
            SET status_id = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                terminated_at = now(),
                terminated_by = ?
            WHERE tour_exclusion_id = ?
            """;
    public static final String TOUR_CONDITION_REMOVE = """
            UPDATE tour_condition
            SET status_id = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                terminated_at = now(),
                terminated_by = ?
            WHERE tour_condition_id = ?
            """;
    public static final String TOUR_TRAVEL_TIPS_REMOVE = """
            UPDATE tour_travel_tips
            SET status_id = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                terminated_at = now(),
                terminated_by = ?
            WHERE tour_travel_tip_id = ?
            """;
    public static final String TOUR_DESTINATION_REMOVE = """
            UPDATE tour_destination
            SET status = (SELECT id FROM common_status WHERE name = ? LIMIT 1),
                terminated_at = now(),
                terminated_by = ?
            WHERE id = ?
            """;
    public static final String UPDATE_TOUR_IMAGE = """
            UPDATE tour_images
            SET
                name = ?,
                description = ?,
                image_url = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE id = ? AND tour_id = ?
            """;
    public static final String UPDATE_INCLUSION = """
            UPDATE tour_inclusion
            SET
                inclusion_text = ?,
                display_order = ?,
                status_id = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE tour_inclusion_id = ? AND tour_id = ?
            """;
    public static final String UPDATE_EXCLUSION = """
            UPDATE tour_exclusion
            SET
                exclusion_text = ?,
                display_order = ?,
                status_id = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE tour_exclusion_id = ? AND tour_id = ?
            """;

    public static final String UPDATE_CONDITION = """
            UPDATE tour_condition
            SET
                condition_text = ?,
                display_order = ?,
                status_id = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE tour_condition_id = ? AND tour_id = ?
            """;

    public static final String UPDATE_TRAVEL_TIP = """
            UPDATE tour_travel_tips
            SET
                tip_title = ?,
                tip_description = ?,
                display_order = ?,
                status_id = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1),
                updated_by = ?
            WHERE tour_travel_tip_id = ? AND tour_id = ?
            """;

    public static final String UPDATE_TOUR_DESTINATION = """
            UPDATE tour_destination
            SET
                destination_id = ?,
                activities_id = ?,
                day = ?,
                status = (SELECT cs.id FROM common_status cs WHERE cs.name = ? LIMIT 1)
            WHERE id = ? AND tour_id = ?
            """;


    public static final String GET_TOUR_ASSIGN_USER_DETAILS_BY_TOUR_ID = """
            SELECT
            	u.user_id,
                u.username,
                t.assign_message
            FROM tour t
            LEFT JOIN user u
            	ON u.user_id = t.assign_to
            WHERE t.tour_id = ?
            """;

}
