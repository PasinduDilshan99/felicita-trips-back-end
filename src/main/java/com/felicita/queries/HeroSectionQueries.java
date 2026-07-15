package com.felicita.queries;

public class HeroSectionQueries {

    public static final String GET_ALL_VEHICLE_HERO_SECTION_DATA = """
            SELECT
                vhs.id,
                vhs.name,
                vhs.image_url,
                vhs.title,
                vhs.subtitle,
                vhs.description,
                vhs.primary_button_text,
                vhs.primary_button_link,
                vhs.secondary_button_text,
                vhs.secondary_button_link,
                cs.name AS status,
                vhs.`order`,
                vhs.created_at,
                vhs.created_by,
                vhs.updated_at,
                vhs.updated_by,
                vhs.terminated_at,
                vhs.terminated_by
            FROM vehicle_hero_section vhs
            JOIN common_status cs ON vhs.status = cs.id
            ORDER BY vhs.`order` ASC
            """;
    public static final String GET_ALL_VEHICLE_SPECIFICATION_HERO_SECTION_DATA = """
            SELECT
                vshs.id,
                vshs.name,
                vshs.image_url,
                vshs.title,
                vshs.subtitle,
                vshs.description,
                vshs.primary_button_text,
                vshs.primary_button_link,
                vshs.secondary_button_text,
                vshs.secondary_button_link,
                cs.name AS status,
                vshs.`order`,
                vshs.created_at,
                vshs.created_by,
                vshs.updated_at,
                vshs.updated_by,
                vshs.terminated_at,
                vshs.terminated_by
            FROM vehicle_specification_hero_section vshs
            JOIN common_status cs ON vshs.status = cs.id
            ORDER BY vshs.`order` ASC
            """;
    public static final String GET_ALL_VEHICLE_TYPES_HERO_SECTION_DATA = """
            SELECT
                vths.id,
                vths.name,
                vths.image_url,
                vths.title,
                vths.subtitle,
                vths.description,
                vths.primary_button_text,
                vths.primary_button_link,
                vths.secondary_button_text,
                vths.secondary_button_link,
                cs.name AS status,
                vths.`order`,
                vths.created_at,
                vths.created_by,
                vths.updated_at,
                vths.updated_by,
                vths.terminated_at,
                vths.terminated_by
            FROM vehicle_types_hero_section vths
            JOIN common_status cs ON vths.status = cs.id
            ORDER BY vths.`order` ASC
            """;
    public static final String GET_ALL_SEASON_HERO_SECTION_DATA = """
            SELECT
                shs.id,
                shs.name,
                shs.image_url,
                shs.title,
                shs.subtitle,
                shs.description,
                shs.primary_button_text,
                shs.primary_button_link,
                shs.secondary_button_text,
                shs.secondary_button_link,
                cs.name AS status,
                shs.`order`,
                shs.created_at,
                shs.created_by,
                shs.updated_at,
                shs.updated_by,
                shs.terminated_at,
                shs.terminated_by
            FROM seasons_hero_section shs
            JOIN common_status cs ON shs.status = cs.id
            ORDER BY shs.`order` ASC
            """;

    public static final String GET_HERO_SECTION_DATA_FOR_PARAMS = """
            SELECT DISTINCT
                primary_button_text AS PRIMARY_BUTTON_TEXT,
                secondary_button_text AS SECONDARY_BUTTON_TEXT
            FROM hero_section
            WHERE terminated_at IS NULL
            """;

    private HeroSectionQueries() {
    }

    public static final String GET_ALL_HERO_SECTION_DATA = """
            SELECT
                hs.id AS IMAGE_ID,
                hs.name AS IMAGE_NAME,
                hs.image_url AS IMAGE_URL,
                hs.title AS IMAGE_TITLE,
                hs.subtitle AS IMAGE_SUB_TITLE,
                hs.description AS IMAGE_DESCRIPTION,
                hs.primary_button_text AS IMAGE_PRIMARY_BUTTON_TEXT,
                hs.primary_button_link AS IMAGE_PRIMARY_BUTTON_LINK,
                hs.secondary_button_text AS IMAGE_SECONDARY_BUTTON_TEXT,
                hs.secondary_button_link AS IMAGE_SECONDARY_BUTTON_LINK,
                cs.name AS IMAGE_STATUS,
                hs.`order` AS IMAGE_ORDER,
                hs.created_at AS IMAGE_CREATED_AT,
                hs.created_by AS IMAGE_CREATED_BY,
                hs.updated_at AS IMAGE_UPDATED_AT,
                hs.updated_by AS IMAGE_UPDATED_BY,
                hs.terminated_at AS IMAGE_TERMINATED_AT,
                hs.terminated_by AS IMAGE_TERMINATED_BY
            FROM hero_section hs
            LEFT JOIN common_status cs
                ON hs.status = cs.id
            """;

    public static final String GET_ALL_ABOUT_US_HERO_SECTION_DATA = """
             SELECT
                a.id,
                a.name,
                a.image_url,
                a.title,
                a.subtitle,
                a.description,
                a.primary_button_text,
                a.primary_button_link,
                a.secondary_button_text,
                a.secondary_button_link,
                a.`order`,
                a.created_at,
                a.updated_at,
                s.name AS status_name
            FROM about_us_hero_section a
            LEFT JOIN common_status s ON a.status = s.id
            WHERE a.terminated_at IS NULL
            ORDER BY a.`order` ASC, a.created_at ASC
            """;


    public static final String GET_ALL_CONTACT_US_HERO_SECTION_DATA = """
            SELECT
                c.id,
                c.name,
                c.image_url,
                c.title,
                c.subtitle,
                c.description,
                c.primary_button_text,
                c.primary_button_link,
                c.secondary_button_text,
                c.secondary_button_link,
                c.`order`,
                c.created_at,
                c.updated_at,
                s.name AS status_name
            FROM contact_us_hero_section c
            LEFT JOIN common_status s ON c.status = s.id
            WHERE c.terminated_at IS NULL
            ORDER BY c.`order` ASC, c.created_at ASC
            """;

    public static final String GET_ALL_BLOG_HERO_SECTION_DATA = """
            SELECT
                b.id,
                b.name,
                b.image_url,
                b.title,
                b.subtitle,
                b.description,
                b.primary_button_text,
                b.primary_button_link,
                b.secondary_button_text,
                b.secondary_button_link,
                b.`order`,
                b.created_at,
                b.updated_at,
                s.name AS status_name
            FROM blog_hero_section b
            LEFT JOIN common_status s ON b.status = s.id
            WHERE b.terminated_at IS NULL
            ORDER BY b.`order` ASC, b.created_at ASC
            """;

    public static final String GET_ALL_FAQ_HERO_SECTION_DATA = """
            SELECT
                fhs.id,
                fhs.name,
                fhs.image_url,
                fhs.title,
                fhs.subtitle,
                fhs.description,
                fhs.primary_button_text,
                fhs.primary_button_link,
                fhs.secondary_button_text,
                fhs.secondary_button_link,
                cs.name AS status,
                fhs.`order`,
                fhs.created_at,
                fhs.created_by,
                fhs.updated_at,
                fhs.updated_by,
                fhs.terminated_at,
                fhs.terminated_by
            FROM faq_hero_section fhs
            JOIN common_status cs ON fhs.status = cs.id
            ORDER BY fhs.`order` ASC
            """;

    public static final String GET_ALL_TOUR_HERO_SECTION_DATA = """
            SELECT
                ths.id,
                ths.name,
                ths.image_url,
                ths.title,
                ths.subtitle,
                ths.description,
                ths.primary_button_text,
                ths.primary_button_link,
                ths.secondary_button_text,
                ths.secondary_button_link,
                cs.name AS status,
                ths.`order`,
                ths.created_at,
                ths.created_by,
                ths.updated_at,
                ths.updated_by,
                ths.terminated_at,
                ths.terminated_by
            FROM tour_hero_section ths
            JOIN common_status cs ON ths.status = cs.id
            ORDER BY ths.`order` ASC
            """;

    public static final String GET_ALL_DESTINATION_HERO_SECTION_DATA = """
            SELECT
                dhs.id,
                dhs.name,
                dhs.image_url,
                dhs.title,
                dhs.subtitle,
                dhs.description,
                dhs.primary_button_text,
                dhs.primary_button_link,
                dhs.secondary_button_text,
                dhs.secondary_button_link,
                cs.name AS status,
                dhs.`order`,
                dhs.created_at,
                dhs.created_by,
                dhs.updated_at,
                dhs.updated_by,
                dhs.terminated_at,
                dhs.terminated_by
            FROM destination_hero_section dhs
            JOIN common_status cs ON dhs.status = cs.id
            ORDER BY dhs.`order` ASC
            """;

    public static final String GET_ALL_ACTIVITY_HERO_SECTION_DATA = """
            SELECT
                ahs.id,
                ahs.name,
                ahs.image_url,
                ahs.title,
                ahs.subtitle,
                ahs.description,
                ahs.primary_button_text,
                ahs.primary_button_link,
                ahs.secondary_button_text,
                ahs.secondary_button_link,
                cs.name AS status,
                ahs.`order`,
                ahs.created_at,
                ahs.created_by,
                ahs.updated_at,
                ahs.updated_by,
                ahs.terminated_at,
                ahs.terminated_by
            FROM activity_hero_section ahs
            JOIN common_status cs ON ahs.status = cs.id
            ORDER BY ahs.`order` ASC
            """;

    public static final String GET_ALL_PACKAGE_HERO_SECTION_DATA = """
            SELECT
                phs.id,
                phs.name,
                phs.image_url,
                phs.title,
                phs.subtitle,
                phs.description,
                phs.primary_button_text,
                phs.primary_button_link,
                phs.secondary_button_text,
                phs.secondary_button_link,
                cs.name AS status,
                phs.`order`,
                phs.created_at,
                phs.created_by,
                phs.updated_at,
                phs.updated_by,
                phs.terminated_at,
                phs.terminated_by
            FROM package_hero_section phs
            JOIN common_status cs ON phs.status = cs.id
            ORDER BY phs.`order` ASC
            """;

    public static final String GET_ALL_PACKAGE_SCHEDULE_HERO_SECTION_DATA = """
            SELECT
                pi.id,
            	pi.name,
                pi.description,
                pi.image_url,
                pi.color
            FROM package_images pi
            LEFT JOIN packages p
            	ON p.package_id = pi.package_id
            WHERE p.package_id = ?
            """;

    public static final String GET_BOOKED_TOUR_HERO_SECTION_DATA = """
            SELECT
            	ti.id,
            	ti.name,
                ti.description,
                ti.image_url
            FROM bookings b
            LEFT JOIN package_schedule ps
            	ON ps.id = b.package_schedule_id
            LEFT JOIN packages p
            	ON p.package_id =  ps.package_id
            LEFt JOIN tour t
            	ON t.tour_id = p.tour_id
            LEFT JOIN tour_images ti
            	ON ti.tour_id = t.tour_id
            WHERE b.booking_id = ?
            """;

    public static final String GET_ACTIVITY_HERO_SECTION_DATA_BY_ACTIVITY_ID = """
            SELECT
                ai.id AS image_id,
                ai.activity_id AS activity_id,
                ai.name AS name,
                ai.description AS description,
                ai.image_url AS image_url,
                cs.name AS status
            FROM activities_images ai
            LEFT JOIN common_status cs
                ON cs.id = ai.status
            WHERE ai.activity_id = ?;
            """;

    public static final String GET_HERO_SECTION_BASIC_DETAILS = """
            SELECT
                hs.id,
                hs.name,
                hs.image_url,
                hs.title,
                hs.subtitle,
                hs.description,
                hs.primary_button_text,
                hs.primary_button_link,
                hs.secondary_button_text,
                hs.secondary_button_link,
                cs.id AS status_id,
                cs.name AS status,
                hs.`order`
            FROM %s hs
            INNER JOIN common_status cs ON hs.status = cs.id
            WHERE hs.terminated_at IS NULL
            """;

    public static final String GET_HERO_SECTION_BASIC_DETAILS_COUNT = """
            SELECT COUNT(*)
            FROM %s hs
            INNER JOIN common_status cs ON hs.status = cs.id
            WHERE hs.terminated_at IS NULL
            """;

    public static final String GET_HERO_SECTION_DETAILS_BY_ID = """
            SELECT
                hs.id AS ID,
                hs.name AS NAME,
                hs.image_url AS IMAGE_URL,
                hs.title AS TITLE,
                hs.subtitle AS SUBTITLE,
                hs.description AS DESCRIPTION,
                hs.primary_button_text AS PRIMARY_BUTTON_TEXT,
                hs.primary_button_link AS PRIMARY_BUTTON_LINK,
                hs.secondary_button_text AS SECONDARY_BUTTON_TEXT,
                hs.secondary_button_link AS SECONDARY_BUTTON_LINK,
                cs.id AS STATUS_ID,
                cs.name AS STATUS,
                hs.`order` AS `ORDER`,
                hs.created_at AS CREATED_AT,
                hs.created_by AS CREATED_BY,
                uc.username AS CREATED_BY_USERNAME,
                hs.updated_at AS UPDATED_AT,
                hs.updated_by AS UPDATED_BY,
                uu.username AS UPDATED_BY_USERNAME,
                hs.terminated_at AS TERMINATED_AT,
                hs.terminated_by AS TERMINATED_BY,
                ut.username AS TERMINATED_BY_USERNAME
            FROM %s hs
            LEFT JOIN common_status cs
                ON hs.status = cs.id
            LEFT JOIN user uc
                ON hs.created_by = uc.user_id
            LEFT JOIN user uu
                ON hs.updated_by = uu.user_id
            LEFT JOIN user ut
                ON hs.terminated_by = ut.user_id
            WHERE hs.id = ?
            """;

    public static final String INSERT_HERO_SECTION = """
            INSERT INTO %s
            (
                name,
                image_url,
                title,
                subtitle,
                description,
                primary_button_text,
                primary_button_link,
                secondary_button_text,
                secondary_button_link,
                status,
                `order`,
                created_at,
                created_by
            )
            VALUES
            (
                ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?
            )
            """;

    public static final String UPDATE_HERO_SECTION = """
            UPDATE %s
            SET
                name = ?,
                image_url = ?,
                title = ?,
                subtitle = ?,
                description = ?,
                primary_button_text = ?,
                primary_button_link = ?,
                secondary_button_text = ?,
                secondary_button_link = ?,
                status = ?,
                `order` = ?,
                updated_at = NOW(),
                updated_by = ?
            WHERE id = ?
              AND terminated_at IS NULL
            """;

    public static final String TERMINATE_HERO_SECTION = """
            UPDATE %s
            SET
                status = ?,
                terminated_at = NOW(),
                terminated_by = ?
            WHERE id = ?
              AND terminated_at IS NULL
            """;


    public static final String GET_HERO_SECTION_SUMMARY_STATISTICS = """
            SELECT
                COUNT(*) AS TOTAL,
                SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS ACTIVE,
                SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS INACTIVE,
                SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) AS TERMINATED_COUNT,
                SUM(
                    CASE
                        WHEN YEAR(created_at) = YEAR(CURDATE())
                         AND MONTH(created_at) = MONTH(CURDATE())
                        THEN 1
                        ELSE 0
                    END
                ) AS CREATED_THIS_MONTH,
                SUM(
                    CASE
                        WHEN updated_at IS NOT NULL
                         AND YEAR(updated_at) = YEAR(CURDATE())
                         AND MONTH(updated_at) = MONTH(CURDATE())
                        THEN 1
                        ELSE 0
                    END
                ) AS UPDATED_THIS_MONTH
            FROM %s;
            """;
    public static final String GET_HERO_SECTION_STATUS_STATISTICS = """
            SELECT
                cs.id,
                cs.name,
                COUNT(hs.id) COUNT_VALUE
            FROM common_status cs
            LEFT JOIN %s hs
            ON hs.status=cs.id
            GROUP BY cs.id,cs.name
            ORDER BY cs.id
            """;

    public static final String GET_HERO_SECTION_MONTHLY_STATISTICS = """
    SELECT
        YEAR(created_at) AS YEAR_VALUE,
        MONTH(created_at) AS MONTH_VALUE,
        MONTHNAME(created_at) AS MONTH_NAME,
        COUNT(*) AS COUNT_VALUE
    FROM %s
    GROUP BY
        YEAR(created_at),
        MONTH(created_at),
        MONTHNAME(created_at)
    ORDER BY
        YEAR_VALUE,
        MONTH_VALUE
    """;


    public static final String GET_HERO_SECTION_ACTIVITY_STATISTICS = """
        SELECT
            YEAR(created_at) AS YEAR_VALUE,
            MONTH(created_at) AS MONTH_VALUE,
            MONTHNAME(created_at) AS MONTH_NAME,
            COUNT(*) AS createdCount,
            SUM(CASE WHEN updated_at IS NOT NULL THEN 1 ELSE 0 END) AS updatedCount
        FROM %s
        GROUP BY 
            YEAR(created_at),
            MONTH(created_at),
            MONTHNAME(created_at)
        ORDER BY 
            YEAR(created_at),
            MONTH(created_at)
        """;

    public static final String GET_HERO_SECTION_TOP_EDITOR_STATISTICS = """
            SELECT
                u.user_id AS id,
                u.username,
                COUNT(*) updateCount
            FROM %s hs
            JOIN user u
            ON hs.updated_by=u.user_id
            WHERE hs.updated_by IS NOT NULL
            GROUP BY u.user_id,u.username
            ORDER BY updateCount DESC
            LIMIT 10
            """;

}
