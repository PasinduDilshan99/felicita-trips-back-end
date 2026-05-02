package com.felicita.queries;

public class PrivilegeQueries {


    public static final String GET_ALL_PRIVILEGES = """
            SELECT p.id,
                   p.name,
                   p.description,
                   cs.name AS status
            FROM privileges p
            JOIN common_status cs ON p.status = cs.id
            WHERE p.terminated_at IS NULL
            """;

    public static final String GET_PRIVILEGE_NAMES_AND_IDS = """
            SELECT id, name
            FROM privileges
            WHERE terminated_at IS NULL
            """;

    public static final String GET_PRIVILEGE_BY_ID = """
            SELECT p.id,
                   p.name,
                   p.description,
                   cs.name AS status
            FROM privileges p
            JOIN common_status cs ON p.status = cs.id
            WHERE p.id = ? AND p.terminated_at IS NULL
            """;

    public static final String GET_PRIVILEGE_BASIC_BY_ID = """
            SELECT p.id,
                   p.name,
                   p.description,
                   cs.name AS status
            FROM privileges p
            JOIN common_status cs ON p.status = cs.id
            WHERE p.id = ?
            """;

    public static final String GET_ROLES_BY_PRIVILEGE_ID = """
            SELECT r.id,
                   r.name,
                   r.description,
                   cs.name AS status
            FROM role_privileges rp
            JOIN roles r ON rp.role_id = r.id
            JOIN common_status cs ON r.status = cs.id
            WHERE rp.privilege_id = ? AND rp.terminated_at IS NULL
            """;

    public static final String INSERT_PRIVILEGE = """
            INSERT INTO privileges (name, status, description, created_by)
            VALUES (?, ?, ?, ?)
            """;

    public static final String UPDATE_PRIVILEGE = """
            UPDATE privileges
            SET name = ?,
                status = ?,
                description = ?,
                updated_by = ?
            WHERE id = ?
            """;

    public static final String TERMINATE_PRIVILEGE = """
            UPDATE privileges
            SET status = ?,
                terminated_at = CURRENT_TIMESTAMP,
                terminated_by = ?
            WHERE id = ?
            """;


    public static final String TOTAL_COUNT = """
            SELECT COUNT(*)
            FROM privileges
            WHERE terminated_at IS NULL
            """;

    public static final String ACTIVE_COUNT = """
            SELECT COUNT(*)
            FROM privileges
            WHERE status = ? AND terminated_at IS NULL
            """;

    public static final String INACTIVE_COUNT = """
            SELECT COUNT(*)
            FROM privileges
            WHERE status = ? AND terminated_at IS NULL
            """;

    public static final String TERMINATED_COUNT = """
            SELECT COUNT(*)
            FROM privileges
            WHERE terminated_at IS NOT NULL
            """;

    public static final String RECENTLY_ADDED = """
            SELECT COUNT(*)
            FROM privileges
            WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
            """;

    public static final String RECENTLY_UPDATED = """
            SELECT COUNT(*)
            FROM privileges
            WHERE updated_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
            """;

    public static final String RECENT_UPDATES = """
        SELECT
            u.username,
            u.user_id,
            COUNT(*) AS updated_count
        FROM privileges p
        JOIN user u ON p.updated_by = u.user_id
        WHERE p.updated_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
        GROUP BY u.username, u.user_id
        """;
    public static final String RECENT_CREATED = """
        SELECT
            u.username,
            u.user_id,
            COUNT(*) AS created_count
        FROM privileges p
        LEFT JOIN user u ON p.created_by = u.user_id
        WHERE p.created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
        GROUP BY u.username, u.user_id
        """;
    public static final String RECENT_TERMINATED = """
        SELECT
            u.username,
            u.user_id,
            COUNT(*) AS terminated_count
        FROM privileges p
        LEFT JOIN user u ON p.terminated_by = u.user_id
        WHERE p.terminated_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
        GROUP BY u.username, u.user_id
        """;


}