package com.felicita.queries;

public class SeasonQueries {

    public static final String GET_SEASON_DETAILS_BY_SEASON_ID = """
            SELECT
                s.id,
                s.name,
                s.standard_name,
                s.local_name,
                s.start_month,
                s.end_month,
                s.monsoon_type,
                s.weather_summary,
                s.temperature_min,
                s.temperature_max,
                s.rainfall_pattern,
                s.is_peak,
                s.display_order,
                s.description,
                s.status,
                s.created_at,
                s.created_by,
                s.updated_at,
                s.updated_by,
            
                si.id AS image_id,
                si.name AS image_name,
                si.description AS image_description,
                si.image_url,
                si.status AS image_status,
                si.created_at AS image_created_at,
                si.created_by AS image_created_by,
                si.updated_at AS image_updated_at,
                si.updated_by AS image_updated_by
            
            FROM seasons s
            LEFT JOIN seasons_images si ON s.id = si.season_id
            WHERE s.id = ?
            AND s.terminated_at IS NULL
            AND (si.terminated_at IS NULL OR si.id IS NULL)
            """;

    public static final String GET_ALL_SEASONS_BASIC = """
            SELECT
                s.id,
                s.name,
                s.standard_name,
                s.local_name,
                s.start_month,
                s.end_month,
                s.is_peak,
                s.display_order,
            
                si.id AS image_id,
                si.name AS image_name,
                si.image_url
            
            FROM seasons s
            LEFT JOIN seasons_images si 
                ON s.id = si.season_id
                AND si.terminated_at IS NULL
                AND si.status = 1
            
            WHERE s.terminated_at IS NULL
            AND s.status = 1
            
            ORDER BY s.display_order ASC
            """;

    public static final String GET_SEASON_ACTIVITY_COUNT =
            """
            SELECT 
                s.id AS season_id,
                s.name AS season_name,
                COUNT(a.id) AS total_activities
            FROM seasons s
            LEFT JOIN activities a ON a.season_id = s.id
            GROUP BY s.id, s.name
            ORDER BY total_activities DESC;
            """;

    public static final String GET_SEASON_TOUR_COUNT =
            """
            SELECT 
                s.id AS season_id,
                s.name AS season_name,
                COUNT(t.tour_id) AS total_tours
            FROM seasons s
            LEFT JOIN tour t ON t.season = s.id
            GROUP BY s.id, s.name
            ORDER BY total_tours DESC;
            """;

    public static final String GET_SEASON_POPULARITY =
            """
            SELECT 
                s.id AS season_id,
                s.name AS season_name,
                COUNT(DISTINCT a.id) AS total_activities,
                COUNT(DISTINCT t.tour_id) AS total_tours,
                (COUNT(DISTINCT a.id) + COUNT(DISTINCT t.tour_id)) AS total_usage
            FROM seasons s
            LEFT JOIN activities a ON a.season_id = s.id
            LEFT JOIN tour t ON t.season = s.id
            GROUP BY s.id, s.name
            ORDER BY total_usage DESC;
            """;

    public static final String GET_PEAK_SEASON_DISTRIBUTION =
            """
            SELECT 
                s.name AS season_name,
                s.is_peak,
                COUNT(DISTINCT a.id) AS activity_count,
                COUNT(DISTINCT t.tour_id) AS tour_count
            FROM seasons s
            LEFT JOIN activities a ON a.season_id = s.id
            LEFT JOIN tour t ON t.season = s.id
            GROUP BY s.id, s.name, s.is_peak
            ORDER BY s.is_peak DESC, activity_count DESC;
            """;

    public static final String GET_SEASON_WEATHER_OVERVIEW =
            """
            SELECT 
                id,
                name,
                temperature_min,
                temperature_max,
                rainfall_pattern,
                weather_summary
            FROM seasons
            WHERE status = 1
            ORDER BY display_order ASC;
            """;

    public static final String GET_SEASON_SUMMARY =
            """
            SELECT 
                (SELECT COUNT(*) FROM seasons WHERE status = 1) AS total_seasons,
                (SELECT COUNT(*) FROM activities) AS total_activities,
                (SELECT COUNT(*) FROM tour) AS total_tours,
    
                (SELECT s.name
                 FROM seasons s
                 LEFT JOIN activities a ON a.season_id = s.id
                 LEFT JOIN tour t ON t.season = s.id
                 GROUP BY s.id
                 ORDER BY (COUNT(a.id) + COUNT(t.tour_id)) DESC
                 LIMIT 1) AS most_used_season,
    
                (SELECT name FROM seasons WHERE is_peak = 1 LIMIT 1) AS peak_season;
            """;

    public static final String GET_SEASON_ALL_DETAILS_BY_ID =
            """
            SELECT 
                s.id,
                s.name,
                s.standard_name,
                s.local_name,
                s.start_month,
                s.end_month,
                s.monsoon_type,
                s.weather_summary,
                s.temperature_min,
                s.temperature_max,
                s.rainfall_pattern,
                s.is_peak,
                s.display_order,
                s.description,
                s.status,
                s.created_at,
                s.created_by,
                s.updated_at,
                s.updated_by,
    
                -- images
                si.id AS image_id,
                si.name AS image_name,
                si.description AS image_description,
                si.image_url,
                si.status AS image_status,
                si.created_at AS image_created_at,
                si.created_by AS image_created_by,
                si.updated_at AS image_updated_at,
                si.updated_by AS image_updated_by,
    
                -- activities
                a.id AS activity_id,
                a.name AS activity_name,
                a.description AS activity_description,
                cs1.name AS activity_status_name,
    
                -- tours
                t.tour_id AS tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                cs2.name AS tour_status_name
    
            FROM seasons s
    
            LEFT JOIN seasons_images si 
                ON si.season_id = s.id
    
            LEFT JOIN activities a 
                ON a.season_id = s.id
    
            LEFT JOIN tour t 
                ON t.season = s.id
    
            LEFT JOIN common_status cs1 
                ON cs1.id = a.status
    
            LEFT JOIN common_status cs2 
                ON cs2.id = t.status
    
            WHERE s.id = ?
            """;


    public static final String TERMINATE_SEASON =
            """
            UPDATE seasons
            SET 
                status = ?,
                terminated_at = NOW()
            WHERE id = ?
            """;

    public static final String TERMINATE_SEASON_IMAGES =
            """
            UPDATE seasons_images
            SET 
                status = ?,
                terminated_at = NOW()
            WHERE id = ?
            """;
    public static final String INSERT_SEASON =
            """
            INSERT INTO seasons (
                name,
                standard_name,
                local_name,
                start_month,
                end_month,
                monsoon_type,
                weather_summary,
                temperature_min,
                temperature_max,
                rainfall_pattern,
                is_peak,
                display_order,
                description,
                status,
                created_at,
                created_by
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)
            """;

    public static final String UPDATE_SEASON =
            """
            UPDATE seasons
            SET 
                name = ?,
                standard_name = ?,
                local_name = ?,
                start_month = ?,
                end_month = ?,
                monsoon_type = ?,
                weather_summary = ?,
                temperature_min = ?,
                temperature_max = ?,
                rainfall_pattern = ?,
                is_peak = ?,
                display_order = ?,
                description = ?,
                status = ?,
                updated_at = NOW()
            WHERE id = ?
            """;

    public static final String REMOVE_SEASON_IMAGES =
            """
            UPDATE seasons_images
            SET 
                status = ?,
                terminated_at = NOW(),
                updated_at = NOW(),
                updated_by = ?
            WHERE id = ? AND season_id = ?
            """;

    public static final String REMOVE_ACTIVITIES_SEASON =
            """
            UPDATE activities
            SET 
                season_id = NULL,
                updated_at = NOW(),
                updated_by = ?
            WHERE id = ? AND season_id = ?
            """;

    public static final String REMOVE_TOURS_SEASON =
            """
            UPDATE tour
            SET 
                season = NULL,
                updated_at = NOW(),
                updated_by = ?
            WHERE tour_id = ? AND season = ?
            """;






}