package com.felicita.queries;

public class CommonQueries {

    public static final String GET_ALL_ACTIVITY_CATEGORIES = """
            SELECT
                ac.id AS activity_category_id,
                ac.name AS activity_category_name,
                ac.description AS activity_category_description,
                ac.color AS activity_category_color,
                ac.hover_color AS activity_category_hover_color,
            
                aci.id AS image_id,
                aci.name AS image_name,
                aci.description AS image_description,
                aci.image_url AS image_url
            
            FROM activity_category ac
            LEFT JOIN activity_category_images aci
                ON ac.id = aci.activity_category_id
                AND aci.status = 1
                AND aci.terminated_at IS NULL
            
            WHERE ac.status = 1
              AND ac.terminated_at IS NULL
            
            ORDER BY ac.id
            """;

    public static final String GET_ALL_DESTINATION_CATEGORIES = """
                SELECT
                    dc.id AS destination_category_id,
                    dc.category AS destination_category_name,
                    dc.description AS destination_category_description,
                    dc.color AS destination_category_color,
                    dc.hover_color AS destination_category_hover_color,
            
                    dci.id AS image_id,
                    dci.name AS image_name,
                    dci.description AS image_description,
                    dci.image_url AS image_url
            
                FROM destination_categories dc
                LEFT JOIN destination_categories_images dci
                    ON dc.id = dci.destination_categories_id
                    AND dci.status = 1
                    AND dci.terminated_at IS NULL
            
                WHERE dc.status = 1
           
            
                ORDER BY dc.id
            """;

    public static final String GET_ALL_PACKAGE_TYPES = """
                SELECT
                    pt.id AS package_category_id,
                    pt.name AS package_category_name,
                    pt.description AS package_category_description,
                    pt.color AS package_category_color,
                    pt.hover_color AS package_category_hover_color,
            
                    pti.id AS image_id,
                    pti.name AS image_name,
                    pti.description AS image_description,
                    pti.image_url AS image_url
            
                FROM package_type pt
                LEFT JOIN package_types_images pti
                    ON pt.id = pti.package_types_id
                    AND pti.status = 1
                    AND pti.terminated_at IS NULL
            
                WHERE pt.status = 1
                  AND pt.terminated_at IS NULL
            
                ORDER BY pt.id
            """;

    public static final String GET_ALL_TOUR_CATEGORIES = """
                SELECT
                    id AS tour_category_id,
                    name AS tour_category_name,
                    description AS tour_category_description,
                    color AS tour_category_color,
                    hover_color AS tour_category_hover_color
            
                FROM tour_category
                WHERE status = 1
                  AND terminated_at IS NULL
                ORDER BY id
            """;

    public static final String GET_ALL_TOUR_TYPES = """
                SELECT
                    id AS tour_type_id,
                    name AS tour_type_name,
                    description AS tour_type_description,
                    color AS tour_type_color,
                    hover_color AS tour_type_hover_color
            
                FROM tour_type
                WHERE status = 1
                  AND terminated_at IS NULL
                ORDER BY id
            """;
    public static final String GET_ALL_SEASONS = """
            SELECT
                s.id AS season_id,
                s.name AS season_name,
                s.standard_name AS season_standard_name,
                s.description AS season_description,
                s.start_month,
                s.end_month,
                s.is_peak,
            
                si.id AS image_id,
                si.image_url,
                si.name AS image_name,
                si.description AS image_description
            
            FROM seasons s
            LEFT JOIN seasons_images si
                ON s.id = si.season_id
                AND si.status = 1
            
            WHERE s.status = 1
            ORDER BY s.display_order ASC, si.id ASC
            """;

    public static final String GET_SUPERVISOR_EMAILS_BY_USER_ID = """
        WITH RECURSIVE supervisor_hierarchy AS (

            -- direct supervisor
            SELECT
                e.supervisor_id,
                u.user_id,
                u.username,
                u.email
            FROM employees e
            JOIN employees s ON e.supervisor_id = s.id
            JOIN user u ON s.user_id = u.user_id
            WHERE e.user_id = ?

            UNION ALL

            -- higher level supervisors
            SELECT
                s.supervisor_id,
                u.user_id,
                u.username,
                u.email
            FROM employees s
            JOIN supervisor_hierarchy sh ON sh.supervisor_id = s.id
            JOIN user u ON s.user_id = u.user_id
            WHERE s.supervisor_id IS NOT NULL
        )

        SELECT DISTINCT
            user_id,
            username,
            email
        FROM supervisor_hierarchy
        WHERE email IS NOT NULL
        """;
}