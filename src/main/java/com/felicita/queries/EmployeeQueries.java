package com.felicita.queries;

public class EmployeeQueries {

    public static final String GET_EMPLOYEES_WITH_SOCIAL_MEDIA_LINKS = """
            SELECT
                e.id as employee_id,
                e.employee_code,
                CONCAT(u.first_name, ' ', COALESCE(u.middle_name, ''), ' ', u.last_name) as full_name,
                u.email,
                u.image_url,
                u.mobile_number1 as phone,
                u.date_of_birth,
                et.type_name as employee_type,
                ed.department_name,
                edg.designation_name,
                e.hire_date,
                e.work_location,
                e.salary,
                smp.platform_name,
                esm.username,
                esm.profile_url,
                esm.is_primary,
                esm.is_public,
                esm.verified,
                esm.follower_count
            FROM employees e
            LEFT JOIN user u ON e.user_id = u.user_id
            LEFT JOIN employee_types et ON e.employee_type_id = et.id
            LEFT JOIN employee_departments ed ON e.department_id = ed.id
            LEFT JOIN employee_designations edg ON e.designation_id = edg.id
            LEFT JOIN employee_social_media esm ON e.id = esm.employee_id
            LEFT JOIN social_media_platforms smp ON esm.platform_id = smp.id
            WHERE e.status_id = 1
            """;

    public static final String GET_EMPLOYEES_GUIDE_DETAILS = """ 
            SELECT e.id as employee_id, e.employee_code, CONCAT(u.first_name, ' ', COALESCE(u.middle_name, ''), ' ', u.last_name) as full_name, u.email, u.image_url, u.mobile_number1 as phone, u.date_of_birth, et.type_name as employee_type, ed.department_name, edg.designation_name, e.hire_date, e.work_location, e.salary, smp.platform_name, esm.username, esm.profile_url, esm.is_primary, esm.is_public, esm.verified, esm.follower_count, egs.specialization_type, egs.regions, egs.languages, egs.certifications, egs.experience_years, egs.rating, egs.is_available FROM employees e JOIN user u ON e.user_id = u.user_id JOIN employee_types et ON e.employee_type_id = et.id JOIN employee_departments ed ON e.department_id = ed.id JOIN employee_designations edg ON e.designation_id = edg.id LEFT JOIN employee_social_media esm ON e.id = esm.employee_id LEFT JOIN social_media_platforms smp ON esm.platform_id = smp.id LEFT JOIN employee_guide_specializations egs ON egs.employee_id = e.id WHERE et.type_name = 'guide' AND e.status_id = 1 """;

    public static final String GET_EMPLOYEE_ASSIGNED_TO_TOUR_ID = """
            SELECT
            	u.first_name,
                u.last_name,
                u.image_url,
                u.email,
                u.mobile_number1,
                ed.designation_name,
                t.assign_message
             FROM employees e
             LEFT JOIN user u
            	ON u.user_id = e.user_id
             LEFT JOIN employee_designations ed
            	ON ed.id = e.designation_id
             LEFT JOIN tour t
            	ON t.assign_to = e.id
            WHERE t.tour_id = ?
            """;
    public static final String GET_OTHER_RELATED_TOURS_BY_TOUR_ID = """
            SELECT t.tour_id, t.name
            FROM tour t
            JOIN tour ref ON ref.tour_id = ?
            WHERE t.assign_to = ref.assign_to
            """;
    public static final String GET_EMPLOYEE_IDS_FOR_ASSIGN_TOUR = """
            SELECT e.id
            FROM employees e
            LEFT JOIN employee_departments ed
            	ON ed.id = e.department_id
            WHERE ed.department_name IN ('Sales & Marketing','Executive Management')
            """;
    public static final String GET_EMPLOYEE_DETAILS_FOR_ASSIGN_TOUR = """
            SELECT
            	e.id,
                u.first_name,
                u.last_name,
                u.image_url,
                u.email,
                u.mobile_number1,
                ed.designation_name,
                JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'tour_id', t.tour_id,
                        'name', t.name
                    )
                ) AS tours
            FROM employees e
            LEFT JOIN user u
                ON u.user_id = e.user_id
            LEFT JOIN employee_designations ed
                ON ed.id = e.designation_id
            LEFT JOIN tour t
                ON t.assign_to = e.id
            LEFT JOIN employee_departments ede
            	ON ede.id = e.department_id
            WHERE ede.department_name IN ('Sales & Marketing','Executive Management')
            GROUP BY e.id,u.first_name, u.last_name, u.image_url, u.email, u.mobile_number1, ed.designation_name 
            """;

    public static final String GET_CEO_DETAILS = """
                SELECT
                    id,
                    name,
                    designation,
                    profile_image,
                    speech1,
                    speech2,
                    speech3,
                    speech4,
                    speech5,
                    speech6,
                    speech7,
                    speech8,
                    speech9,
                    speech10
                FROM ceo_details
                WHERE status = 1
                LIMIT 1
            """;
    public static final String GET_EMPLOYEE_BASIC_DETAILS = """
                SELECT
                    e.id AS employeeId,
                    e.employee_code AS employeeCode,
            
                    u.user_id AS userId,
                    u.username AS username,
                    CONCAT(
                        COALESCE(u.first_name, ''),
                        ' ',
                        COALESCE(u.middle_name, ''),
                        ' ',
                        COALESCE(u.last_name, '')
                    ) AS fullName,
                    u.email AS email,
                    u.mobile_number1 AS mobileNumber,
                    u.nic AS nic,
            
                    e.employee_type_id AS employeeTypeId,
                    et.type_name AS employeeType,
            
                    e.department_id AS departmentId,
                    ed.department_name AS departmentName,
            
                    e.designation_id AS designationId,
                    des.designation_name AS designationName,
            
                    e.hire_date AS hireDate,
                    e.employment_type AS employmentType,
                    e.work_location AS workLocation,
                    e.employee_grade AS employeeGrade,
                    e.salary AS salary,
            
                    e.supervisor_id AS supervisorId,
                    CONCAT(COALESCE(sup_u.first_name,''),' ',COALESCE(sup_u.last_name,'')) AS supervisorName,
            
                    e.reporting_manager_id AS reportingManagerId,
                    CONCAT(COALESCE(rm_u.first_name,''),' ',COALESCE(rm_u.last_name,'')) AS reportingManagerName,
            
                    cs.name AS status,
                    e.created_at AS createdAt,
                    e.updated_at AS updatedAt
            
                FROM employees e
                INNER JOIN user u ON e.user_id = u.user_id
                LEFT JOIN employee_types et ON e.employee_type_id = et.id
                LEFT JOIN employee_departments ed ON e.department_id = ed.id
                LEFT JOIN employee_designations des ON e.designation_id = des.id
                LEFT JOIN employees sup ON e.supervisor_id = sup.id
                LEFT JOIN user sup_u ON sup.user_id = sup_u.user_id
                LEFT JOIN employees rm ON e.reporting_manager_id = rm.id
                LEFT JOIN user rm_u ON rm.user_id = rm_u.user_id
                LEFT JOIN common_status cs ON e.status_id = cs.id
            
                WHERE 1=1
            """;

    public static final String GET_EMPLOYEE_BY_ID = """
            SELECT
                e.id AS employeeId,
                e.employee_code AS employeeCode,
            
                u.user_id AS userId,
                u.username,
                CONCAT(
                    COALESCE(u.first_name,''),
                    ' ',
                    COALESCE(u.middle_name,''),
                    ' ',
                    COALESCE(u.last_name,'')
                ) AS fullName,
                u.email,
                u.mobile_number1 AS mobileNumber,
                u.nic,
            
                e.employee_type_id AS employeeTypeId,
                et.type_name AS employeeType,
            
                e.department_id AS departmentId,
                ed.department_name AS departmentName,
            
                e.designation_id AS designationId,
                des.designation_name AS designationName,
            
                e.hire_date AS hireDate,
                e.employment_type AS employmentType,
                e.work_location AS workLocation,
                e.employee_grade AS employeeGrade,
                e.salary,
            
                e.supervisor_id AS supervisorId,
                CONCAT(sup_u.first_name,' ',sup_u.last_name) AS supervisorName,
            
                e.reporting_manager_id AS reportingManagerId,
                CONCAT(rm_u.first_name,' ',rm_u.last_name) AS reportingManagerName,
            
                cs.name AS status,
            
                e.created_at AS createdAt,
                e.updated_at AS updatedAt
            
            FROM employees e
            INNER JOIN user u ON e.user_id = u.user_id
            LEFT JOIN employee_types et ON e.employee_type_id = et.id
            LEFT JOIN employee_departments ed ON e.department_id = ed.id
            LEFT JOIN employee_designations des ON e.designation_id = des.id
            LEFT JOIN employees sup ON e.supervisor_id = sup.id
            LEFT JOIN user sup_u ON sup.user_id = sup_u.user_id
            LEFT JOIN employees rm ON e.reporting_manager_id = rm.id
            LEFT JOIN user rm_u ON rm.user_id = rm_u.user_id
            LEFT JOIN common_status cs ON e.status_id = cs.id
            
            WHERE e.id = ?
            """;

    public static final String GET_EMPLOYEE_SHIFTS = """
            SELECT
                s.shift_name,
                s.start_time,
                s.end_time,
                sa.effective_from,
                sa.effective_to
            FROM employee_shifts_assignment sa
            JOIN employee_work_shifts s ON sa.shift_id = s.id
            WHERE sa.employee_id = ?
            AND (sa.effective_to IS NULL OR sa.effective_to >= CURDATE())
            """;

    public static final String GET_EMPLOYEE_SKILLS = """
            SELECT
                skill_name,
                skill_category,
                proficiency_level,
                certification,
                certified_date
            FROM employee_skills
            WHERE employee_id = ?
            """;

    public static final String GET_EMPLOYEE_SOCIAL_MEDIA = """
            SELECT
                p.platform_name,
                es.username,
                es.profile_url,
                es.follower_count,
                es.is_primary,
                es.verified
            FROM employee_social_media es
            JOIN social_media_platforms p ON es.platform_id = p.id
            WHERE es.employee_id = ?
            """;

    public static final String GET_EMPLOYEE_REVIEWS = """
            SELECT
                review_period_start,
                review_period_end,
                overall_rating,
                teamwork_rating,
                productivity_rating,
                comments,
                status
            FROM employee_performance_reviews
            WHERE employee_id = ?
            ORDER BY review_date DESC
            """;

    public static final String GET_EMPLOYEE_METRICS = """
            SELECT
                metric_date,
                metric_type,
                metric_value,
                target_value,
                achievement_percentage,
                notes
            FROM employee_performance_metrics
            WHERE employee_id = ?
            ORDER BY metric_date DESC
            """;

    public static final String GET_EMPLOYEE_EMERGENCY_CONTACTS = """
            SELECT
                contact_name,
                relationship,
                primary_phone,
                secondary_phone,
                email,
                is_primary
            FROM employee_emergency_contacts
            WHERE employee_id = ?
            """;

    public static final String GET_EMPLOYEE_ASSETS = """
            SELECT
                asset_type,
                asset_name,
                serial_number,
                model,
                assigned_date,
                return_date
            FROM employee_assets
            WHERE employee_id = ?
            """;


    public static final String INSERT_EMPLOYEE_BASIC_DETAILS = """
            INSERT INTO employees (
                user_id,
                employee_code,
                employee_type_id,
                department_id,
                designation_id,
                hire_date,
                employment_type,
                supervisor_id,
                reporting_manager_id,
                salary,
                bank_account_number,
                bank_name,
                bank_branch,
                ifsc_code,
                uan_number,
                pf_number,
                esi_number,
                probation_period_months,
                probation_end_date,
                confirmation_date,
                exit_date,
                work_location,
                cost_center,
                employee_grade,
                status_id,
                created_by,
                created_at
            )
            VALUES (
                ?, ?, ?, ?, ?,
                ?, ?,
                ?, ?,
                ?,
                ?, ?, ?, ?,
                ?, ?, ?,
                ?, ?, ?, ?,
                ?, ?, ?,
                ?, ?,
                NOW()
            )
            """;

    public static final String INSERT_EMPLOYEE_EMERGENCY_CONTACT = """
                INSERT INTO employee_emergency_contacts (
                    employee_id,
                    contact_name,
                    relationship,
                    primary_phone,
                    secondary_phone,
                    email,
                    address,
                    is_primary,
                    status_id,
                    created_by
                ) VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String INSERT_EMPLOYEE_ASSET = """
                INSERT INTO employee_assets (
                    employee_id,
                    asset_type,
                    asset_id,
                    asset_name,
                    serial_number,
                    model,
                    assigned_date,
                    return_date,
                    condition_on_assignment,
                    condition_on_return,
                    notes,
                    assigned_by,
                    status_id,
                    created_by
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String INSERT_EMPLOYEE_DOCUMENT = """
                INSERT INTO employee_documents (
                    employee_id,
                    document_type,
                    document_name,
                    file_path,
                    file_size,
                    mime_type,
                    expiry_date,
                    verified,
                    verified_by,
                    verified_date,
                    notes,
                    uploaded_by,
                    status_id,
                    created_by
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String INSERT_EMPLOYEE_DRIVER_DETAILS = """
                INSERT INTO employee_driver_details (
                    employee_id,
                    license_type,
                    license_number,
                    license_issue_date,
                    license_expiry_date,
                    vehicle_types,
                    experience_years,
                    accident_free_years,
                    route_expertise,
                    is_available,
                    created_by
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String INSERT_EMPLOYEE_GUIDE_SPECIALIZATION = """
                INSERT INTO employee_guide_specializations (
                    employee_id,
                    specialization_type,
                    regions,
                    languages,
                    certifications,
                    experience_years,
                    rating,
                    is_available,
                    created_by
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String INSERT_EMPLOYEE_INCENTIVE = """
                INSERT INTO employee_incentives (
                    employee_id,
                    incentive_date,
                    incentive_type,
                    amount,
                    calculation_basis,
                    reference_id,
                    payment_status,
                    paid_date,
                    created_by,
                    created_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
            """;

    public static final String INSERT_EMPLOYEE_SALARY_STRUCTURE = """
                INSERT INTO employee_salary_structures (
                    employee_id,
                    component_id,
                    amount,
                    effective_from,
                    effective_to,
                    status_id,
                    created_by,
                    created_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, NOW())
            """;

    public static final String INSERT_EMPLOYEE_SKILL = """
                INSERT INTO employee_skills (
                    employee_id,
                    skill_name,
                    skill_category,
                    proficiency_level,
                    certification,
                    certified_date,
                    expiry_date,
                    verified,
                    verified_by,
                    verified_date,
                    created_by,
                    created_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
            """;

    public static final String INSERT_EMPLOYEE_WORK_HISTORY = """
                INSERT INTO employee_work_history (
                    employee_id,
                    designation_id,
                    department_id,
                    salary,
                    start_date,
                    end_date,
                    employment_type,
                    reason,
                    notes,
                    status_id,
                    created_by,
                    created_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
            """;

    public static final String INSERT_EMPLOYEE_SHIFT_ASSIGNMENT = """
                INSERT INTO employee_shifts_assignment (
                    employee_id,
                    shift_id,
                    effective_from,
                    effective_to,
                    assigned_by,
                    status_id,
                    created_by
                )
                VALUES (?, ?, ?,  ?, ?, ?, ?)
            """;

    public static final String INSERT_EMPLOYEE_SOCIAL_MEDIA = """
                INSERT INTO employee_social_media (
                    employee_id,
                    platform_id,
                    username,
                    profile_url,
                    follower_count,
                    is_primary,
                    is_public,
                    verified,
                    verified_by,
                    verified_date,
                    last_updated,
                    status_id,
                    created_by
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String GET_DISTINCT_DESIGNATIONS = """
                SELECT 
                    id,
                    designation_name AS label
                FROM employee_designations
                WHERE status_id = 1
                ORDER BY designation_name
            """;

    public static final String GET_ACTIVE_EMPLOYEES = """
                SELECT 
                    e.id,
                    u.username AS label
                FROM employees e
                LEFT JOIN user u ON e.user_id = u.user_id
                WHERE status_id = 1
                ORDER BY employee_code
            """;
    public static final String GET_SALARY_COMPONENTS = """
                SELECT 
                    id,
                    component_name AS label
                FROM employee_salary_components
                WHERE status_id = 1
                ORDER BY component_name
            """;

    public static final String GET_SOCIAL_MEDIA_PLATFORMS = """
                SELECT 
                    id,
                    platform_name AS label
                FROM social_media_platforms
                WHERE status_id = 1
                ORDER BY platform_name
            """;


}
