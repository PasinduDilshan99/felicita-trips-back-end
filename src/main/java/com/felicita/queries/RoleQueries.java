package com.felicita.queries;

public class RoleQueries {

    public static final String GET_ALL_ROLES = """
            SELECT r.id,
                   r.name,
                   r.description,
                   cs.name AS status
            FROM roles r
            JOIN common_status cs ON r.status = cs.id
            WHERE r.terminated_at IS NULL
            """;

    public static final String GET_ROLE_NAMES_AND_IDS = """
            SELECT id, name
            FROM roles
            WHERE terminated_at IS NULL
            """;

    public static final String GET_ROLE_BY_ID = """
            SELECT r.id,
                   r.name,
                   r.description,
                   cs.name AS status
            FROM roles r
            JOIN common_status cs ON r.status = cs.id
            WHERE r.id = ? AND r.terminated_at IS NULL
            """;

    public static final String GET_ROLE_BASIC_BY_ID = """
            SELECT r.id,
                   r.name,
                   r.description,
                   cs.name AS status
            FROM roles r
            JOIN common_status cs ON r.status = cs.id
            WHERE r.id = ?
            """;

    public static final String GET_PRIVILEGES_BY_ROLE_ID = """
            SELECT p.id,
                   p.name,
                   p.description,
                   cs.name AS status
            FROM role_privileges rp
            JOIN privileges p ON rp.privilege_id = p.id
            JOIN common_status cs ON p.status = cs.id
            WHERE rp.role_id = ? AND rp.terminated_at IS NULL AND p.terminated_at IS NULL
            """;

    public static final String INSERT_ROLE = """
            INSERT INTO roles (name, status, description, created_by)
            VALUES (?, ?, ?, ?)
            """;

    public static final String INSERT_ROLE_PRIVILEGE = """
            INSERT INTO role_privileges (role_id, privilege_id, status, created_by)
            VALUES (?, ?, ?, ?)
            """;

    public static final String UPDATE_ROLE = """
            UPDATE roles
            SET name = ?,
                status = ?,
                description = ?,
                updated_by = ?
            WHERE id = ?
            """;

    public static final String TERMINATE_ROLE = """
            UPDATE roles
            SET status = ?,
                terminated_at = CURRENT_TIMESTAMP,
                terminated_by = ?
            WHERE id = ?
            """;

    public static final String TERMINATE_ROLE_PRIVILEGE = """
            UPDATE role_privileges
            SET terminated_at = CURRENT_TIMESTAMP,
                terminated_by = ?
            WHERE role_id = ? AND privilege_id = ? AND terminated_at IS NULL
            """;

    public static final String TERMINATE_ALL_ROLE_PRIVILEGES = """
            UPDATE role_privileges
            SET terminated_at = CURRENT_TIMESTAMP,
                status = ?,
                terminated_by = ?
            WHERE role_id = ?
            """;

    public static final String TOTAL_COUNT = """
            SELECT COUNT(*)
            FROM roles
            WHERE terminated_at IS NULL
            """;

    public static final String ACTIVE_COUNT = """
            SELECT COUNT(*)
            FROM roles
            WHERE status = ? AND terminated_at IS NULL
            """;

    public static final String INACTIVE_COUNT = """
            SELECT COUNT(*)
            FROM roles
            WHERE status = ? AND terminated_at IS NULL
            """;

    public static final String TERMINATED_COUNT = """
            SELECT COUNT(*)
            FROM roles
            WHERE terminated_at IS NOT NULL
            """;

    public static final String RECENTLY_ADDED = """
            SELECT COUNT(*)
            FROM roles
            WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
            """;

    public static final String RECENTLY_UPDATED = """
            SELECT COUNT(*)
            FROM roles
            WHERE updated_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
            """;

    public static final String RECENT_UPDATES = """
            SELECT
                u.username,
                u.user_id,
                COUNT(*) AS updated_count
            FROM roles r
            JOIN user u ON r.updated_by = u.user_id
            WHERE r.updated_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
            GROUP BY u.username, u.user_id
            """;

    public static final String RECENT_CREATED = """
            SELECT
                u.username,
                u.user_id,
                COUNT(*) AS created_count
            FROM roles r
            LEFT JOIN user u ON r.created_by = u.user_id
            WHERE r.created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
            GROUP BY u.username, u.user_id
            """;

    public static final String RECENT_TERMINATED = """
            SELECT
                u.username,
                u.user_id,
                COUNT(*) AS terminated_count
            FROM roles r
            LEFT JOIN user u ON r.terminated_by = u.user_id
            WHERE r.terminated_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
            GROUP BY u.username, u.user_id
            """;

    public static final String ROLE_USAGE_STATISTICS = """
            SELECT 
                r.id AS role_id,
                r.name AS role_name,
                COUNT(DISTINCT ur.user_id) AS user_count
            FROM roles r
            LEFT JOIN user_roles ur ON r.id = ur.role_id AND ur.terminated_at IS NULL
            WHERE r.terminated_at IS NULL
            GROUP BY r.id, r.name
            ORDER BY user_count DESC
            """;

    public static final String CHECK_ROLE_PRIVILEGE_EXISTS = """
        SELECT COUNT(*)
        FROM role_privileges
        WHERE role_id = ? AND privilege_id = ?
        """;

    public static final String UPDATE_ROLE_PRIVILEGE_STATUS = """
        UPDATE role_privileges
        SET status = ?,
            updated_at = CURRENT_TIMESTAMP,
            updated_by = ?
        WHERE role_id = ? AND privilege_id = ?
        """;
}