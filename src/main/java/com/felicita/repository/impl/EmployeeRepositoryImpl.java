package com.felicita.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.dto.FilterItem;
import com.felicita.model.dto.WelcomeEmployeeDto;
import com.felicita.model.request.EmployeeBasicDetailsParamRequest;
import com.felicita.model.request.EmployeeFullDetailsRequest;
import com.felicita.model.request.employee.*;
import com.felicita.model.response.*;
import com.felicita.queries.CouponQueries;
import com.felicita.queries.EmployeeQueries;
import com.felicita.repository.EmployeeRepository;
import com.felicita.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;

    @Autowired
    public EmployeeRepositoryImpl(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<EmployeeWithSocialMediaResponse> getEmployeeWithSocailMedia() {

        String GET_EMPLOYEES_WITH_SOCIAL_MEDIA_LINKS = EmployeeQueries.GET_EMPLOYEES_WITH_SOCIAL_MEDIA_LINKS;

        try {

            Map<Long, EmployeeWithSocialMediaResponse> employeeMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_EMPLOYEES_WITH_SOCIAL_MEDIA_LINKS, rs -> {

                while (rs.next()) {

                    Long employeeId = rs.getLong("employee_id");

                    EmployeeWithSocialMediaResponse employee = employeeMap.get(employeeId);

                    // ✅ Create employee ONLY once
                    if (employee == null) {

                        java.sql.Date dobDate = rs.getDate("date_of_birth");
                        java.sql.Date hireDate = rs.getDate("hire_date");

                        employee = new EmployeeWithSocialMediaResponse(
                                employeeId,
                                rs.getString("image_url"),
                                rs.getString("employee_code"),
                                rs.getString("full_name"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                dobDate != null ? dobDate.toLocalDate() : null,           // ✅ NULL SAFE
                                rs.getString("employee_type"),
                                rs.getString("department_name"),
                                rs.getString("designation_name"),
                                hireDate != null ? hireDate.toLocalDate() : null,         // ✅ NULL SAFE
                                rs.getString("work_location"),
                                rs.getObject("salary") != null ? rs.getDouble("salary") : null, // ✅ NULL SAFE
                                new ArrayList<>()
                        );

                        employeeMap.put(employeeId, employee);
                    }

                    // ✅ Add social media only if exists
                    String platformName = rs.getString("platform_name");

                    if (platformName != null) {

                        EmployeeWithSocialMediaResponse.SocialMediaProfile socialProfile =
                                new EmployeeWithSocialMediaResponse.SocialMediaProfile(
                                        platformName,
                                        rs.getString("username"),
                                        rs.getString("profile_url"),
                                        rs.getBoolean("is_primary"),
                                        rs.getBoolean("is_public"),
                                        rs.getBoolean("verified"),
                                        rs.getInt("follower_count")
                                );

                        employee.getSocialMediaProfiles().add(socialProfile);
                    }
                }
            });

            return new ArrayList<>(employeeMap.values());

        } catch (Exception ex) {
            LOGGER.error("Error fetching employee with social media details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee with social media details");
        }
    }

    @Override
    public List<EmployeeWithSocialMediaResponse> getALlEmployeeWithSocailMedia() {
        String GET_EMPLOYEES_WITH_SOCIAL_MEDIA_LINKS = EmployeeQueries.GET_EMPLOYEES_WITH_SOCIAL_MEDIA_LINKS;

        try {

            Map<Long, EmployeeWithSocialMediaResponse> employeeMap = new LinkedHashMap<>();

            jdbcTemplate.query(GET_EMPLOYEES_WITH_SOCIAL_MEDIA_LINKS, rs -> {

                while (rs.next()) {

                    Long employeeId = rs.getLong("employee_id");

                    EmployeeWithSocialMediaResponse employee = employeeMap.get(employeeId);

                    // ✅ Create employee ONLY once
                    if (employee == null) {

                        java.sql.Date dobDate = rs.getDate("date_of_birth");
                        java.sql.Date hireDate = rs.getDate("hire_date");

                        employee = new EmployeeWithSocialMediaResponse(
                                employeeId,
                                rs.getString("image_url"),
                                rs.getString("employee_code"),
                                rs.getString("full_name"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                dobDate != null ? dobDate.toLocalDate() : null,           // ✅ NULL SAFE
                                rs.getString("employee_type"),
                                rs.getString("department_name"),
                                rs.getString("designation_name"),
                                hireDate != null ? hireDate.toLocalDate() : null,         // ✅ NULL SAFE
                                rs.getString("work_location"),
                                rs.getObject("salary") != null ? rs.getDouble("salary") : null, // ✅ NULL SAFE
                                new ArrayList<>()
                        );

                        employeeMap.put(employeeId, employee);
                    }

                    // ✅ Add social media only if exists
                    String platformName = rs.getString("platform_name");

                    if (platformName != null) {

                        EmployeeWithSocialMediaResponse.SocialMediaProfile socialProfile =
                                new EmployeeWithSocialMediaResponse.SocialMediaProfile(
                                        platformName,
                                        rs.getString("username"),
                                        rs.getString("profile_url"),
                                        rs.getBoolean("is_primary"),
                                        rs.getBoolean("is_public"),
                                        rs.getBoolean("verified"),
                                        rs.getInt("follower_count")
                                );

                        employee.getSocialMediaProfiles().add(socialProfile);
                    }
                }
            });

            return new ArrayList<>(employeeMap.values());

        } catch (Exception ex) {
            LOGGER.error("Error fetching employee with social media details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee with social media details");
        }
    }

    @Override
    public List<EmployeeGuideResponse> getEmployeeGuideDetails() {

        String GET_EMPLOYEES_GUIDE_DETAILS = EmployeeQueries.GET_EMPLOYEES_GUIDE_DETAILS;

        try {
            return jdbcTemplate.query(GET_EMPLOYEES_GUIDE_DETAILS, rs -> {

                Map<Long, EmployeeGuideResponse> employeeMap = new LinkedHashMap<>();

                while (rs.next()) {

                    Long employeeId = rs.getLong("employee_id");

                    // ================== MAIN EMPLOYEE OBJECT ==================
                    EmployeeGuideResponse employee =
                            employeeMap.computeIfAbsent(employeeId, id -> {
                                try {
                                    return EmployeeGuideResponse.builder()
                                            .employeeId(employeeId)
                                            .employeeCode(rs.getString("employee_code"))
                                            .fullName(rs.getString("full_name"))
                                            .email(rs.getString("email"))
                                            .imageUrl(rs.getString("image_url"))
                                            .phone(rs.getString("phone"))
                                            .dateOfBirth(rs.getDate("date_of_birth") != null
                                                    ? rs.getDate("date_of_birth").toLocalDate()
                                                    : null)
                                            .employeeType(rs.getString("employee_type"))
                                            .departmentName(rs.getString("department_name"))
                                            .designationName(rs.getString("designation_name"))
                                            .hireDate(rs.getDate("hire_date") != null
                                                    ? rs.getDate("hire_date").toLocalDate()
                                                    : null)
                                            .workLocation(rs.getString("work_location"))
                                            .salary(rs.getBigDecimal("salary"))
                                            .guideSpecialization(new ArrayList<>())
                                            .socialMediaAccounts(new ArrayList<>())
                                            .build();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                    // ================== GUIDE SPECIALIZATION (DEDUPED) ==================
                    if (rs.getString("specialization_type") != null) {

                        boolean specializationExists =
                                employee.getGuideSpecialization().stream()
                                        .anyMatch(gs ->
                                                {
                                                    try {
                                                        return gs.getSpecializationType().equals(rs.getString("specialization_type")) &&
                                                                gs.getRegions().equals(rs.getString("regions"));
                                                    } catch (SQLException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                        );

                        if (!specializationExists) {
                            employee.getGuideSpecialization().add(
                                    EmployeeGuideResponse.GuideSpecialization.builder()
                                            .specializationType(rs.getString("specialization_type"))
                                            .regions(rs.getString("regions"))
                                            .languages(rs.getString("languages"))
                                            .certifications(rs.getString("certifications"))
                                            .experienceYears(rs.getObject("experience_years") != null
                                                    ? rs.getInt("experience_years") : null)
                                            .rating(rs.getObject("rating") != null
                                                    ? rs.getDouble("rating") : null)
                                            .isAvailable(rs.getObject("is_available") != null
                                                    ? rs.getBoolean("is_available") : null)
                                            .build()
                            );
                        }
                    }

                    // ================== SOCIAL MEDIA (DEDUPED) ==================
                    if (rs.getString("platform_name") != null) {

                        boolean socialExists =
                                employee.getSocialMediaAccounts().stream()
                                        .anyMatch(sm ->
                                                {
                                                    try {
                                                        return sm.getProfileUrl().equals(rs.getString("profile_url"));
                                                    } catch (SQLException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                        );

                        if (!socialExists) {
                            employee.getSocialMediaAccounts().add(
                                    EmployeeGuideResponse.SocialMedia.builder()
                                            .platformName(rs.getString("platform_name"))
                                            .username(rs.getString("username"))
                                            .profileUrl(rs.getString("profile_url"))
                                            .isPrimary(rs.getObject("is_primary") != null
                                                    ? rs.getBoolean("is_primary") : null)
                                            .isPublic(rs.getObject("is_public") != null
                                                    ? rs.getBoolean("is_public") : null)
                                            .verified(rs.getObject("verified") != null
                                                    ? rs.getBoolean("verified") : null)
                                            .followerCount(rs.getObject("follower_count") != null
                                                    ? rs.getLong("follower_count") : null)
                                            .build()
                            );
                        }
                    }
                }

                return new ArrayList<>(employeeMap.values());
            });

        } catch (Exception ex) {
            LOGGER.error("Error fetching employee guide details: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee guide details");
        }
    }

    @Override
    public TourAssignedEmployeeResponse getEmployeeAssignToTourId(Long tourId) {
        String sql = EmployeeQueries.GET_EMPLOYEE_ASSIGNED_TO_TOUR_ID;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{tourId}, (rs, rowNum) ->
                    TourAssignedEmployeeResponse.builder()
                            .firstName(rs.getString("first_name"))
                            .lastName(rs.getString("last_name"))
                            .imageUrl(rs.getString("image_url"))
                            .email(rs.getString("email"))
                            .mobileNumber(rs.getString("mobile_number1"))
                            .designationName(rs.getString("designation_name"))
                            .assignMessage(rs.getString("assign_message"))
                            .build()
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new DataNotFoundErrorExceptionHandler("No employee assigned to tour found");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching employee assigned to tour", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching employee assigned to tour"
            );
        }
    }

    @Override
    public List<TourAssignedEmployeeResponse.RelatedOtherTours> getOtherRelatedToursByTourId(Long tourId) {
        String sql = EmployeeQueries.GET_OTHER_RELATED_TOURS_BY_TOUR_ID;
        try {
            return jdbcTemplate.query(
                    sql,
                    new Object[]{tourId},
                    (rs, rowNum) -> TourAssignedEmployeeResponse.RelatedOtherTours.builder()
                            .tourId(rs.getLong("tour_id"))
                            .tourName(rs.getString("name"))
                            .build()
            );
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public List<Long> getEmployeeIdsForAssignTour() {
        String GET_EMPLOYEE_IDS_FOR_ASSIGN_TOUR = EmployeeQueries.GET_EMPLOYEE_IDS_FOR_ASSIGN_TOUR;

        try {
            return jdbcTemplate.queryForList(GET_EMPLOYEE_IDS_FOR_ASSIGN_TOUR, Long.class);

        } catch (DataAccessException dae) {
            LOGGER.error("DB error while fetching employee IDs for assign tour", dae);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee IDs");
        }
    }

    @Override
    public List<EmployeesForAssignTourResponse> getEmployeeDetailsForAssignTour() {
        String GET_EMPLOYEE_DETAILS_FOR_ASSIGN_TOUR = EmployeeQueries.GET_EMPLOYEE_DETAILS_FOR_ASSIGN_TOUR;

        try {
            return jdbcTemplate.query(GET_EMPLOYEE_DETAILS_FOR_ASSIGN_TOUR, (rs, rowNum) -> {
                EmployeesForAssignTourResponse employee = new EmployeesForAssignTourResponse();
                employee.setEmployeeId(rs.getLong("id"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setImageUrl(rs.getString("image_url"));
                employee.setEmail(rs.getString("email"));
                employee.setMobileNumber1(rs.getString("mobile_number1"));
                employee.setDesignationName(rs.getString("designation_name"));

                // Parse JSON array from MySQL
                String toursJson = rs.getString("tours");
                if (toursJson != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<EmployeesForAssignTourResponse.Tour> tours = null;
                    try {
                        tours = objectMapper.readValue(
                                toursJson,
                                objectMapper.getTypeFactory().constructCollectionType(
                                        List.class,
                                        EmployeesForAssignTourResponse.Tour.class
                                )
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    employee.setTours(tours);
                }

                return employee;
            });

        } catch (DataAccessException dae) {
            LOGGER.error("DB error while fetching employee details for assign tour", dae);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee details");
        } catch (Exception e) {
            LOGGER.error("Error parsing tours JSON", e);
            throw new InternalServerErrorExceptionHandler("Failed to parse employee tours");
        }
    }

    @Override
    public CeoDetailsReponse getCeoDetails() {
        String sql = EmployeeQueries.GET_CEO_DETAILS;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                List<String> speeches = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    String speech = rs.getString("speech" + i);
                    if (speech != null && !speech.isBlank()) {
                        speeches.add(speech);
                    }
                }

                return CeoDetailsReponse.builder()
                        .userId(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .title(rs.getString("designation"))
                        .imageUrl(rs.getString("profile_image"))
                        .speech(speeches)
                        .build();
            });

        } catch (EmptyResultDataAccessException ex) {
            throw new DataNotFoundErrorExceptionHandler("No ceo details.");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching ceo details.", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching ceo details."
            );
        }
    }

    @Override
    public List<EmployeeBasicDetailsResponse> getAllEmpmoyeesBasicDetails(
            EmployeeBasicDetailsParamRequest request
    ) {
        try {

            StringBuilder query = new StringBuilder(EmployeeQueries.GET_EMPLOYEE_BASIC_DETAILS);
            List<Object> params = new ArrayList<>();

            // ---------------- FILTERS ----------------

            if (request.getName() != null && !request.getName().isEmpty()) {
                query.append("""
                            AND CONCAT(
                                COALESCE(u.first_name, ''),
                                ' ',
                                COALESCE(u.middle_name, ''),
                                ' ',
                                COALESCE(u.last_name, '')
                            ) LIKE ?
                        """);
                params.add("%" + request.getName() + "%");
            }

            if (request.getEmployeeTypeId() != null) {
                query.append(" AND e.employee_type_id = ? ");
                params.add(request.getEmployeeTypeId());
            }

            if (request.getStatus() != null) {
                query.append(" AND cs.name = ? ");
                params.add(request.getStatus());
            }


            if (request.getDepartmentId() != null) {
                query.append(" AND e.department_id = ? ");
                params.add(request.getDepartmentId());
            }

            if (request.getEmploymentType() != null) {
                query.append(" AND e.employment_type = ? ");
                params.add(request.getEmploymentType());
            }

            if (request.getWorkLocation() != null) {
                query.append(" AND e.work_location = ? ");
                params.add(request.getWorkLocation());
            }

            if (request.getEmployeeGrade() != null) {
                query.append(" AND e.employee_grade = ? ");
                params.add(request.getEmployeeGrade());
            }

            if (request.getSupervisorId() != null) {
                query.append(" AND e.supervisor_id = ? ");
                params.add(request.getSupervisorId());
            }

            if (request.getReportingManagerId() != null) {
                query.append(" AND e.reporting_manager_id = ? ");
                params.add(request.getReportingManagerId());
            }

            // ---------------- SORTING (SAFE) ----------------

            String sortBy = request.getSortBy();
            String sortDirection = "DESC";

            if (request.getSortDirection() != null &&
                    request.getSortDirection().equalsIgnoreCase("ASC")) {
                sortDirection = "ASC";
            }

// default
            if (sortBy == null) {
                sortBy = "employeeTypeId";
            }

            switch (sortBy) {

                case "employeeTypeId":
                    sortBy = "e.employee_type_id";
                    break;

                case "status":
                    sortBy = "cs.name";
                    break;

                case "departmentId":
                    sortBy = "e.department_id";
                    break;

                case "employmentType":
                    sortBy = "e.employment_type";
                    break;

                case "workLocation":
                    sortBy = "e.work_location";
                    break;

                case "employeeGrade":
                    sortBy = "e.employee_grade";
                    break;

                case "supervisorId":
                    sortBy = "e.supervisor_id";
                    break;

                case "reportingManagerId":
                    sortBy = "e.reporting_manager_id";
                    break;

                default:
                    sortBy = "e.employee_type_id";
            }

            query.append(" ORDER BY ").append(sortBy).append(" ").append(sortDirection);

            // ---------------- PAGINATION ----------------

            query.append(" LIMIT ? OFFSET ? ");

            int limit = request.getPageSize();
            int offset = request.getPageNumber() * request.getPageSize();

            params.add(limit);
            params.add(offset);

            // ---------------- EXECUTION ----------------

            List<EmployeeBasicDetailsResponse> result = jdbcTemplate.query(
                    query.toString(),
                    params.toArray(),
                    (rs, rowNum) -> EmployeeBasicDetailsResponse.builder()
                            .employeeId(rs.getLong("employeeId"))
                            .employeeCode(rs.getString("employeeCode"))

                            .userId(rs.getLong("userId"))
                            .username(rs.getString("username"))
                            .fullName(rs.getString("fullName"))
                            .email(rs.getString("email"))
                            .mobileNumber(rs.getString("mobileNumber"))
                            .nic(rs.getString("nic"))
                            .imageUrl(rs.getString("imageUrl"))

                            .employeeTypeId(rs.getLong("employeeTypeId"))
                            .employeeType(rs.getString("employeeType"))

                            .departmentId(rs.getLong("departmentId"))
                            .departmentName(rs.getString("departmentName"))

                            .designationId(rs.getLong("designationId"))
                            .designationName(rs.getString("designationName"))

                            .hireDate(
                                    rs.getDate("hireDate") != null
                                            ? rs.getDate("hireDate").toLocalDate()
                                            : null
                            )

                            .employmentType(rs.getString("employmentType"))
                            .workLocation(rs.getString("workLocation"))
                            .employeeGrade(rs.getString("employeeGrade"))
                            .salary(rs.getBigDecimal("salary"))

                            .supervisorId(
                                    rs.getObject("supervisorId") != null
                                            ? rs.getLong("supervisorId")
                                            : null
                            )
                            .supervisorName(rs.getString("supervisorName"))

                            .reportingManagerId(
                                    rs.getObject("reportingManagerId") != null
                                            ? rs.getLong("reportingManagerId")
                                            : null
                            )
                            .reportingManagerName(rs.getString("reportingManagerName"))

                            .status(rs.getString("status"))

                            .createdAt(
                                    rs.getTimestamp("createdAt") != null
                                            ? rs.getTimestamp("createdAt").toLocalDateTime()
                                            : null
                            )
                            .updatedAt(
                                    rs.getTimestamp("updatedAt") != null
                                            ? rs.getTimestamp("updatedAt").toLocalDateTime()
                                            : null
                            )
                            .build()
            );

            if (result.isEmpty()) {
                throw new DataNotFoundErrorExceptionHandler("No employees found");
            }

            return result;

        } catch (DataNotFoundErrorExceptionHandler e){
            throw e;
        }catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching employees", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching employees"
            );
        }
    }

    @Override
    public EmployeeFullDetailsResponse getEmployeeFullDetails(
            EmployeeFullDetailsRequest employeeFullDetailsRequest
    ) {

        try {

            Long employeeId = employeeFullDetailsRequest.getEmployeeId();

            // =========================================
            // Main Employee Details
            // =========================================

            EmployeeFullDetailsResponse response = jdbcTemplate.queryForObject(
                    EmployeeQueries.GET_EMPLOYEE_BY_ID,
                    new Object[]{employeeId},
                    (rs, rowNum) -> EmployeeFullDetailsResponse.builder()

                            .employeeId(rs.getLong("employeeId"))
                            .employeeCode(rs.getString("employeeCode"))

                            .userId(rs.getLong("userId"))
                            .username(rs.getString("username"))
                            .fullName(rs.getString("fullName"))
                            .email(rs.getString("email"))
                            .mobileNumber(rs.getString("mobileNumber"))
                            .nic(rs.getString("nic"))
                            .imageUrl(rs.getString("imageUrl"))

                            .employeeTypeId(rs.getLong("employeeTypeId"))
                            .employeeType(rs.getString("employeeType"))

                            .departmentId(rs.getLong("departmentId"))
                            .departmentName(rs.getString("departmentName"))

                            .designationId(rs.getLong("designationId"))
                            .designationName(rs.getString("designationName"))

                            .hireDate(
                                    rs.getDate("hireDate") != null
                                            ? rs.getDate("hireDate").toLocalDate()
                                            : null
                            )

                            .employmentType(rs.getString("employmentType"))
                            .workLocation(rs.getString("workLocation"))
                            .employeeGrade(rs.getString("employeeGrade"))
                            .salary(rs.getBigDecimal("salary"))

                            .supervisorId(
                                    rs.getObject("supervisorId") != null
                                            ? rs.getLong("supervisorId")
                                            : null
                            )

                            .supervisorName(rs.getString("supervisorName"))

                            .reportingManagerId(
                                    rs.getObject("reportingManagerId") != null
                                            ? rs.getLong("reportingManagerId")
                                            : null
                            )

                            .reportingManagerName(rs.getString("reportingManagerName"))

                            .status(rs.getString("status"))

                            .createdAt(
                                    rs.getTimestamp("createdAt") != null
                                            ? rs.getTimestamp("createdAt").toLocalDateTime()
                                            : null
                            )

                            .updatedAt(
                                    rs.getTimestamp("updatedAt") != null
                                            ? rs.getTimestamp("updatedAt").toLocalDateTime()
                                            : null
                            )

                            .build()
            );

            // =========================================
            // Shift Details
            // =========================================

            List<EmployeeFullDetailsResponse.ShiftDetails> shifts =
                    jdbcTemplate.query(
                            EmployeeQueries.GET_EMPLOYEE_SHIFTS,
                            new Object[]{employeeId},
                            (rs, rowNum) -> EmployeeFullDetailsResponse.ShiftDetails.builder()
                                    .shiftName(rs.getString("shift_name"))

                                    .startTime(
                                            rs.getTime("start_time") != null
                                                    ? rs.getTime("start_time").toLocalTime()
                                                    : null
                                    )

                                    .endTime(
                                            rs.getTime("end_time") != null
                                                    ? rs.getTime("end_time").toLocalTime()
                                                    : null
                                    )

                                    .effectiveFrom(
                                            rs.getDate("effective_from") != null
                                                    ? rs.getDate("effective_from").toLocalDate()
                                                    : null
                                    )

                                    .effectiveTo(
                                            rs.getDate("effective_to") != null
                                                    ? rs.getDate("effective_to").toLocalDate()
                                                    : null
                                    )

                                    .build()
                    );

            response.setShifts(shifts);

            // =========================================
            // Skills
            // =========================================

            List<EmployeeFullDetailsResponse.SkillDetails> skills =
                    jdbcTemplate.query(
                            EmployeeQueries.GET_EMPLOYEE_SKILLS,
                            new Object[]{employeeId},
                            (rs, rowNum) -> EmployeeFullDetailsResponse.SkillDetails.builder()
                                    .skillName(rs.getString("skill_name"))
                                    .skillCategory(rs.getString("skill_category"))
                                    .proficiencyLevel(rs.getString("proficiency_level"))
                                    .certification(rs.getString("certification"))

                                    .certifiedDate(
                                            rs.getDate("certified_date") != null
                                                    ? rs.getDate("certified_date").toLocalDate()
                                                    : null
                                    )

                                    .build()
                    );

            response.setSkills(skills);

            // =========================================
            // Social Media
            // =========================================

            List<EmployeeFullDetailsResponse.SocialMediaDetails> socialMedia =
                    jdbcTemplate.query(
                            EmployeeQueries.GET_EMPLOYEE_SOCIAL_MEDIA,
                            new Object[]{employeeId},
                            (rs, rowNum) -> EmployeeFullDetailsResponse.SocialMediaDetails.builder()
                                    .platformName(rs.getString("platform_name"))
                                    .username(rs.getString("username"))
                                    .profileUrl(rs.getString("profile_url"))
                                    .followerCount(rs.getInt("follower_count"))
                                    .primary(rs.getBoolean("is_primary"))
                                    .verified(rs.getBoolean("verified"))
                                    .build()
                    );

            response.setSocialMedia(socialMedia);

            // =========================================
            // Performance Metrics
            // =========================================

            List<EmployeeFullDetailsResponse.PerformanceMetricDetails> metrics =
                    jdbcTemplate.query(
                            EmployeeQueries.GET_EMPLOYEE_METRICS,
                            new Object[]{employeeId},
                            (rs, rowNum) -> EmployeeFullDetailsResponse.PerformanceMetricDetails.builder()

                                    .metricDate(
                                            rs.getDate("metric_date") != null
                                                    ? rs.getDate("metric_date").toLocalDate()
                                                    : null
                                    )

                                    .metricType(rs.getString("metric_type"))
                                    .metricValue(rs.getBigDecimal("metric_value"))
                                    .targetValue(rs.getBigDecimal("target_value"))
                                    .achievementPercentage(rs.getBigDecimal("achievement_percentage"))

                                    .notes(rs.getString("notes"))

                                    .build()
                    );

            response.setPerformanceMetrics(metrics);

            // =========================================
            // Performance Reviews
            // =========================================

            List<EmployeeFullDetailsResponse.PerformanceReviewDetails> reviews =
                    jdbcTemplate.query(
                            EmployeeQueries.GET_EMPLOYEE_REVIEWS,
                            new Object[]{employeeId},
                            (rs, rowNum) -> EmployeeFullDetailsResponse.PerformanceReviewDetails.builder()

                                    .reviewPeriodStart(
                                            rs.getDate("review_period_start") != null
                                                    ? rs.getDate("review_period_start").toLocalDate()
                                                    : null
                                    )

                                    .reviewPeriodEnd(
                                            rs.getDate("review_period_end") != null
                                                    ? rs.getDate("review_period_end").toLocalDate()
                                                    : null
                                    )

                                    .overallRating(rs.getBigDecimal("overall_rating"))
                                    .teamworkRating(rs.getInt("teamwork_rating"))
                                    .productivityRating(rs.getInt("productivity_rating"))

                                    .comments(rs.getString("comments"))
                                    .status(rs.getString("status"))

                                    .build()
                    );

            response.setPerformanceReviews(reviews);

            // =========================================
            // Emergency Contacts
            // =========================================

            List<EmployeeFullDetailsResponse.EmergencyContactDetails> contacts =
                    jdbcTemplate.query(
                            EmployeeQueries.GET_EMPLOYEE_EMERGENCY_CONTACTS,
                            new Object[]{employeeId},
                            (rs, rowNum) -> EmployeeFullDetailsResponse.EmergencyContactDetails.builder()

                                    .contactName(rs.getString("contact_name"))
                                    .relationship(rs.getString("relationship"))
                                    .primaryPhone(rs.getString("primary_phone"))
                                    .secondaryPhone(rs.getString("secondary_phone"))
                                    .email(rs.getString("email"))
                                    .primary(rs.getBoolean("is_primary"))

                                    .build()
                    );

            response.setEmergencyContacts(contacts);

            // =========================================
            // Assets
            // =========================================

            List<EmployeeFullDetailsResponse.AssetDetails> assets =
                    jdbcTemplate.query(
                            EmployeeQueries.GET_EMPLOYEE_ASSETS,
                            new Object[]{employeeId},
                            (rs, rowNum) -> EmployeeFullDetailsResponse.AssetDetails.builder()

                                    .assetType(rs.getString("asset_type"))
                                    .assetName(rs.getString("asset_name"))
                                    .serialNumber(rs.getString("serial_number"))
                                    .model(rs.getString("model"))

                                    .assignedDate(
                                            rs.getDate("assigned_date") != null
                                                    ? rs.getDate("assigned_date").toLocalDate()
                                                    : null
                                    )

                                    .returnDate(
                                            rs.getDate("return_date") != null
                                                    ? rs.getDate("return_date").toLocalDate()
                                                    : null
                                    )

                                    .build()
                    );

            response.setAssets(assets);

            return response;

        } catch (EmptyResultDataAccessException ex) {

            throw new DataNotFoundErrorExceptionHandler("Employee not found");

        } catch (Exception ex) {

            LOGGER.error("Unexpected error while fetching employee full details", ex);

            throw new InternalServerErrorExceptionHandler(
                    "Unexpected error occurred while fetching employee full details"
            );
        }
    }

    @Override
    public EmployeeStatisticsResponse.KpiSummary getKpiSummeryStatistics() {

        String sql = """
        SELECT
            (SELECT COUNT(*) FROM employees) AS totalEmployees,
            (SELECT COUNT(*) FROM employees e WHERE e.status_id = 1) AS activeEmployees,
            (SELECT COUNT(*) FROM employees e WHERE e.status_id = 2) AS inactiveEmployees,
            (SELECT COUNT(*) FROM employees WHERE supervisor_id IS NULL) AS employeesWithoutSupervisor,
            (SELECT COUNT(*) FROM employees 
                WHERE MONTH(hire_date) = MONTH(CURDATE())
                AND YEAR(hire_date) = YEAR(CURDATE())
            ) AS employeesJoinedThisMonth,
            (SELECT ROUND(AVG(overall_rating),2) FROM employee_performance_reviews) AS averageRating,
            (SELECT COUNT(*) FROM employee_assets) AS totalAssets
        """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.KpiSummary.builder()
                        .totalEmployees(rs.getLong("totalEmployees"))
                        .activeEmployees(rs.getLong("activeEmployees"))
                        .inactiveEmployees(rs.getLong("inactiveEmployees"))
                        .employeesWithoutSupervisor(rs.getLong("employeesWithoutSupervisor"))
                        .employeesJoinedThisMonth(rs.getLong("employeesJoinedThisMonth"))
                        .averageRating(rs.getBigDecimal("averageRating"))
                        .totalAssets(rs.getLong("totalAssets"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.DepartmentWiseEmployees>
    getDepartmentWiseEmployeesListStatistics() {

        String sql = """
        SELECT
            ed.department_name,
            COUNT(e.id) AS employee_count
        FROM employees e
        LEFT JOIN employee_departments ed ON e.department_id = ed.id
        GROUP BY ed.department_name
        ORDER BY employee_count DESC
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.DepartmentWiseEmployees.builder()
                        .departmentName(rs.getString("department_name"))
                        .employeeCount(rs.getLong("employee_count"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.EmployeeTypeDistribution>
    getEmployeeTypeDistributionStatistics() {

        String sql = """
        SELECT
            et.type_name,
            COUNT(e.id) AS employee_count
        FROM employees e
        LEFT JOIN employee_types et ON e.employee_type_id = et.id
        GROUP BY et.type_name
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.EmployeeTypeDistribution.builder()
                        .employeeType(rs.getString("type_name"))
                        .employeeCount(rs.getLong("employee_count"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.WorkLocationDistribution>
    getWorkLocationDistributionStatistics() {

        String sql = """
        SELECT work_location, COUNT(*) AS employee_count
        FROM employees
        GROUP BY work_location
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.WorkLocationDistribution.builder()
                        .workLocation(rs.getString("work_location"))
                        .employeeCount(rs.getLong("employee_count"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.EmployeeGradeDistribution>
    getEmployeeGradeDistributionStatistics() {

        String sql = """
        SELECT employee_grade, COUNT(*) AS employee_count
        FROM employees
        GROUP BY employee_grade
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.EmployeeGradeDistribution.builder()
                        .employeeGrade(rs.getString("employee_grade"))
                        .employeeCount(rs.getLong("employee_count"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.MonthlyHiringTrend>
    getMonthlyHiringTrendStatistics() {

        String sql = """
        SELECT
            DATE_FORMAT(hire_date, '%Y-%m') AS month,
            COUNT(*) AS hired_count
        FROM employees
        GROUP BY DATE_FORMAT(hire_date, '%Y-%m')
        ORDER BY month
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.MonthlyHiringTrend.builder()
                        .month(rs.getString("month"))
                        .hiredCount(rs.getLong("hired_count"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.SalaryByDepartment>
    getSalaryByDepartmentStatistics() {

        String sql = """
        SELECT
            ed.department_name,
            AVG(e.salary) AS avg_salary,
            SUM(e.salary) AS total_salary
        FROM employees e
        LEFT JOIN employee_departments ed ON e.department_id = ed.id
        GROUP BY ed.department_name
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.SalaryByDepartment.builder()
                        .departmentName(rs.getString("department_name"))
                        .averageSalary(rs.getBigDecimal("avg_salary"))
                        .totalSalary(rs.getBigDecimal("total_salary"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.PerformanceRatingDistribution>
    getPerformanceRatingDistributionStatistics() {

        String sql = """
        SELECT
            FLOOR(overall_rating) AS rating_group,
            COUNT(*) AS total_reviews
        FROM employee_performance_reviews
        GROUP BY FLOOR(overall_rating)
        ORDER BY rating_group
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.PerformanceRatingDistribution.builder()
                        .ratingGroup(rs.getInt("rating_group"))
                        .totalReviews(rs.getLong("total_reviews"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.SkillDistribution>
    getSkillDistributionStatistics() {

        String sql = """
        SELECT skill_name, COUNT(*) AS employee_count
        FROM employee_skills
        GROUP BY skill_name
        ORDER BY employee_count DESC
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.SkillDistribution.builder()
                        .skillName(rs.getString("skill_name"))
                        .employeeCount(rs.getLong("employee_count"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.AssetDistribution>
    getAssetDistributionStatistics() {

        String sql = """
        SELECT asset_type, COUNT(*) AS total_assets
        FROM employee_assets
        GROUP BY asset_type
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.AssetDistribution.builder()
                        .assetType(rs.getString("asset_type"))
                        .totalAssets(rs.getLong("total_assets"))
                        .build()
        );
    }

    @Override
    public List<EmployeeStatisticsResponse.ShiftDistribution>
    getShiftDistributionStatistics() {

        String sql = """
        SELECT
            ws.shift_name,
            COUNT(sa.employee_id) AS employee_count
        FROM employee_shifts_assignment sa
        INNER JOIN employee_work_shifts ws ON sa.shift_id = ws.id
        WHERE sa.effective_to IS NULL OR sa.effective_to >= CURDATE()
        GROUP BY ws.shift_name
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                EmployeeStatisticsResponse.ShiftDistribution.builder()
                        .shiftName(rs.getString("shift_name"))
                        .employeeCount(rs.getLong("employee_count"))
                        .build()
        );
    }

    @Override
    public EmployeeBasicDetailsParamsResponse getAllEmpmoyeesBasicDetailsParams() {

        try {

            // ---------------- RESPONSE OBJECT ----------------
            EmployeeBasicDetailsParamsResponse response = new EmployeeBasicDetailsParamsResponse();

            // ---------------- EMPLOYEE TYPES ----------------
            List<FilterItem> employeeTypes =
                    jdbcTemplate.query(
                            """
                            SELECT id, type_name AS label
                            FROM employee_types
                            ORDER BY type_name
                            """,
                            (rs, rowNum) -> FilterItem.builder()
                                    .id(rs.getLong("id"))
                                    .label(rs.getString("label"))
                                    .build()
                    );

            // ---------------- DEPARTMENTS ----------------
            List<FilterItem> departments =
                    jdbcTemplate.query(
                            """
                            SELECT id, department_name AS label
                            FROM employee_departments
                            ORDER BY department_name
                            """,
                            (rs, rowNum) -> FilterItem.builder()
                                    .id(rs.getLong("id"))
                                    .label(rs.getString("label"))
                                    .build()
                    );

            // ---------------- EMPLOYMENT TYPES (STATIC OR TABLE) ----------------
            List<FilterItem> employmentTypes =
                    jdbcTemplate.query(
                            """
                            SELECT DISTINCT employment_type AS label
                            FROM employees
                            WHERE employment_type IS NOT NULL
                            """,
                            (rs, rowNum) -> FilterItem.builder()
                                    .id((long) rowNum)
                                    .label(rs.getString("label"))
                                    .build()
                    );

            // ---------------- WORK LOCATIONS ----------------
            List<FilterItem> workLocations =
                    jdbcTemplate.query(
                            """
                            SELECT DISTINCT work_location AS label
                            FROM employees
                            WHERE work_location IS NOT NULL
                            """,
                            (rs, rowNum) -> FilterItem.builder()
                                    .id((long) rowNum)
                                    .label(rs.getString("label"))
                                    .build()
                    );

            // ---------------- EMPLOYEE GRADES ----------------
            List<FilterItem> employeeGrades =
                    jdbcTemplate.query(
                            """
                            SELECT DISTINCT employee_grade AS label
                            FROM employees
                            WHERE employee_grade IS NOT NULL
                            """,
                            (rs, rowNum) -> FilterItem.builder()
                                    .id((long) rowNum)
                                    .label(rs.getString("label"))
                                    .build()
                    );

            // ---------------- SUPERVISORS ----------------
            List<FilterItem> supervisors =
                    jdbcTemplate.query(
                            """
                            SELECT e.id, CONCAT(u.first_name, ' ', u.last_name) AS label
                            FROM employees e
                            JOIN user u ON e.user_id = u.user_id
                            WHERE e.id IN (
                                SELECT DISTINCT supervisor_id
                                FROM employees
                                WHERE supervisor_id IS NOT NULL
                            )
                            """,
                            (rs, rowNum) -> FilterItem.builder()
                                    .id(rs.getLong("id"))
                                    .label(rs.getString("label"))
                                    .build()
                    );

            // ---------------- REPORTING MANAGERS ----------------
            List<FilterItem> reportingManagers =
                    jdbcTemplate.query(
                            """
                            SELECT e.id, CONCAT(u.first_name, ' ', u.last_name) AS label
                            FROM employees e
                            JOIN user u ON e.user_id = u.user_id
                            WHERE e.id IN (
                                SELECT DISTINCT reporting_manager_id
                                FROM employees
                                WHERE reporting_manager_id IS NOT NULL
                            )
                            """,
                            (rs, rowNum) -> FilterItem.builder()
                                    .id(rs.getLong("id"))
                                    .label(rs.getString("label"))
                                    .build()
                    );

            // ---------------- STATUSES ----------------
            List<FilterItem> statuses =
                    jdbcTemplate.query(
                            """
                            SELECT id, name AS label
                            FROM common_status
                            ORDER BY name
                            """,
                            (rs, rowNum) -> FilterItem.builder()
                                    .id(rs.getLong("id"))
                                    .label(rs.getString("label"))
                                    .build()
                    );

            // ---------------- SET RESPONSE ----------------
            response.setEmployeeTypes(employeeTypes);
            response.setDepartments(departments);
            response.setEmploymentTypes(employmentTypes);
            response.setWorkLocations(workLocations);
            response.setEmployeeGrades(employeeGrades);
            response.setSupervisors(supervisors);
            response.setReportingManagers(reportingManagers);
            response.setStatuses(statuses);

            return response;

        } catch (Exception ex) {
            LOGGER.error("Error while fetching employee filter params", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch employee filter parameters"
            );
        }
    }

    @Override
    public WelcomeEmployeeDto getNewlyAddedEmployeeBasicDetails(Long userId) {

        try {

            String query = """
                    SELECT
                       et.type_name AS employeeType,
                       e.employee_code AS employeeCode,
                       ed.department_name AS departmentName,
                       des.designation_name AS designation,
                       e.employment_type AS employmentType,

                       CAST(e.supervisor_id AS CHAR) AS supervisorId,
                       TRIM(CONCAT(
                           COALESCE(sup_u.first_name, ''),
                           ' ',
                           COALESCE(sup_u.last_name, '')
                       )) AS supervisorName,

                       CAST(e.reporting_manager_id AS CHAR) AS reportingManagerId,
                       TRIM(CONCAT(
                           COALESCE(rm_u.first_name, ''),
                           ' ',
                           COALESCE(rm_u.last_name, '')
                       )) AS reportingManagerName,

                       e.hire_date AS hiringDate,
                       e.probation_period_months AS probationMonths,
                       e.probation_end_date AS probationEndDate

                   FROM employees e

                   LEFT JOIN employee_types et
                       ON e.employee_type_id = et.id

                   LEFT JOIN employee_departments ed
                       ON e.department_id = ed.id

                   LEFT JOIN employee_designations des
                       ON e.designation_id = des.id

                   LEFT JOIN employees sup
                       ON e.supervisor_id = sup.id

                   LEFT JOIN user sup_u
                       ON sup.user_id = sup_u.user_id

                   LEFT JOIN employees rm
                       ON e.reporting_manager_id = rm.id

                   LEFT JOIN user rm_u
                       ON rm.user_id = rm_u.user_id

                   WHERE e.user_id = ?
                """;

            return jdbcTemplate.queryForObject(
                    query,
                    new Object[]{userId},
                    (rs, rowNum) -> WelcomeEmployeeDto.builder()
                            .employeeType(rs.getString("employeeType"))
                            .employeeCode(rs.getString("employeeCode"))
                            .departmentName(rs.getString("departmentName"))
                            .designation(rs.getString("designation"))
                            .employmentType(rs.getString("employmentType"))

                            .supervisorId(rs.getString("supervisorId"))
                            .supervisorName(rs.getString("supervisorName"))

                            .ReportingManagerID(rs.getString("reportingManagerID"))
                            .ReportingManagerName(rs.getString("reportingManagerName"))

                            .hiringDate(
                                    rs.getDate("hiringDate") != null
                                            ? rs.getDate("hiringDate").toLocalDate()
                                            : null
                            )

                            .probationMonths(rs.getObject("probationMonths") != null
                                    ? rs.getInt("probationMonths")
                                    : null)

                            .probationEndDate(
                                    rs.getDate("probationEndDate") != null
                                            ? rs.getDate("probationEndDate").toLocalDate()
                                            : null
                            )

                            .build()
            );

        } catch (EmptyResultDataAccessException ex) {

            throw new DataNotFoundErrorExceptionHandler(
                    "Employee details not found for user id : " + userId
            );

        } catch (Exception ex) {

            LOGGER.error("Error while fetching newly added employee details", ex);

            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch newly added employee details"
            );
        }
    }

    @Override
    public Long insertEmployeebasicDetails(
            EmployeeBasicDetailsRequest basicDetails,
            Long userId) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        EmployeeQueries.INSERT_EMPLOYEE_BASIC_DETAILS,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setLong(1, basicDetails.getUserId());
                ps.setString(2, basicDetails.getEmployeeCode());
                ps.setInt(3, basicDetails.getEmployeeTypeId());
                ps.setInt(4, basicDetails.getDepartmentId());
                ps.setInt(5, basicDetails.getDesignationId());

                ps.setObject(6, basicDetails.getHireDate());
                ps.setString(7, basicDetails.getEmploymentType());

                ps.setObject(8, basicDetails.getSupervisorId());
                ps.setObject(9, basicDetails.getReportingManagerId());

                ps.setBigDecimal(10, basicDetails.getSalary());

                ps.setString(11, basicDetails.getBankAccountNumber());
                ps.setString(12, basicDetails.getBankName());
                ps.setString(13, basicDetails.getBankBranch());
                ps.setString(14, basicDetails.getIfscCode());

                ps.setString(15, basicDetails.getUanNumber());
                ps.setString(16, basicDetails.getPfNumber());
                ps.setString(17, basicDetails.getEsiNumber());

                ps.setObject(18, basicDetails.getProbationPeriodMonths());
                ps.setObject(19, basicDetails.getProbationEndDate());
                ps.setObject(20, basicDetails.getConfirmationDate());
                ps.setObject(21, basicDetails.getExitDate());

                ps.setString(22, basicDetails.getWorkLocation());
                ps.setString(23, basicDetails.getCostCenter());
                ps.setString(24, basicDetails.getEmployeeGrade());

                Long statusId = statusRepository.getStatusIdByName(basicDetails.getStatus());
                ps.setLong(25, statusId != null ? statusId : 1L);

                ps.setLong(26, userId);

                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        } catch (Exception ex) {
            LOGGER.error("Error while inserting employee basic details", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee basic details"
            );
        }
    }

    @Override
    public void insertEmployeeEmergencyContacts(
            Long employeeId,
            List<EmployeeEmergencyContactRequest> emergencyContacts,
            Long userId
    ) {

        try {
            if (emergencyContacts == null || emergencyContacts.isEmpty()) {
                return;
            }
            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_EMERGENCY_CONTACT,
                    emergencyContacts,
                    emergencyContacts.size(),
                    (ps, contact) -> {

                        ps.setLong(1, employeeId);
                        ps.setString(2, contact.getContactName());
                        ps.setString(3, contact.getRelationship());
                        ps.setString(4, contact.getPrimaryPhone());
                        ps.setString(5, contact.getSecondaryPhone());
                        ps.setString(6, contact.getEmail());
                        ps.setString(7, contact.getAddress());

                        ps.setBoolean(8, Boolean.TRUE.equals(contact.getIsPrimary()));
                        Long statusId = statusRepository.getStatusIdByName(contact.getStatus());
                        ps.setLong(9, statusId != null ? statusId : 1L);
                        ps.setLong(10, userId);

                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error while inserting employee emergency contacts", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee emergency contacts"
            );
        }
    }

    @Override
    public void insertEmployeeAssets(
            Long employeeId,
            List<EmployeeAssetRequest> assets,
            Long userId
    ) {

        try {

            if (assets == null || assets.isEmpty()) {
                return;
            }

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_ASSET,
                    assets,
                    assets.size(),
                    (ps, asset) -> {

                        ps.setLong(1, employeeId);
                        ps.setString(2, asset.getAssetType());
                        ps.setString(3, asset.getAssetId());
                        ps.setString(4, asset.getAssetName());
                        ps.setString(5, asset.getSerialNumber());
                        ps.setString(6, asset.getModel());

                        ps.setObject(7, asset.getAssignedDate());
                        ps.setObject(8, asset.getReturnDate());

                        ps.setString(9, asset.getConditionOnAssignment());
                        ps.setString(10, asset.getConditionOnReturn());
                        ps.setString(11, asset.getNotes());

                        ps.setObject(12, asset.getAssignedBy());
                        Long statusId = statusRepository.getStatusIdByName(asset.getStatus());
                        ps.setLong(13, statusId != null ? statusId : 1L);
                        ps.setLong(14, userId);
                    }
            );

        } catch (Exception ex) {

            LOGGER.error("Error while inserting employee assets", ex);

            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee assets"
            );
        }
    }


    @Override
    public void insertEmployeeDocuments(
            Long employeeId,
            List<EmployeeDocumentRequest> documents,
            Long userId
    ) {

        if (documents == null || documents.isEmpty()) {
            return;
        }

        try {

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_DOCUMENT,
                    documents,
                    documents.size(),
                    (ps, doc) -> {

                        ps.setLong(1, employeeId);
                        ps.setString(2, doc.getDocumentType());
                        ps.setString(3, doc.getDocumentName());
                        ps.setString(4, doc.getFilePath());

                        if (doc.getFileSize() != null) {
                            ps.setInt(5, doc.getFileSize());
                        } else {
                            ps.setNull(5, java.sql.Types.INTEGER);
                        }

                        ps.setString(6, doc.getMimeType());

                        if (doc.getExpiryDate() != null) {
                            ps.setDate(7, java.sql.Date.valueOf(doc.getExpiryDate()));
                        } else {
                            ps.setNull(7, java.sql.Types.DATE);
                        }

                        if (doc.getVerified() != null) {
                            ps.setBoolean(8, doc.getVerified());
                        } else {
                            ps.setBoolean(8, false);
                        }

                        if (doc.getVerifiedBy() != null) {
                            ps.setLong(9, doc.getVerifiedBy());
                        } else {
                            ps.setNull(9, java.sql.Types.BIGINT);
                        }

                        if (doc.getVerifiedDate() != null) {
                            ps.setDate(10, java.sql.Date.valueOf(doc.getVerifiedDate()));
                        } else {
                            ps.setNull(10, java.sql.Types.DATE);
                        }

                        ps.setString(11, doc.getNotes());

                        // fallback to current user if uploadedBy not provided
                        if (doc.getUploadedBy() != null) {
                            ps.setLong(12, doc.getUploadedBy());
                        } else {
                            ps.setLong(12, userId);
                        }

                        Long statusId = statusRepository.getStatusIdByName(doc.getStatus());
                        ps.setLong(13, statusId != null ? statusId : 1L);

                        ps.setLong(14, userId);

                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee documents", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee documents"
            );
        }
    }

    @Override
    public void insertEmployeeDriverDetails(
            Long employeeId,
            EmployeeDriverDetailsRequest driverDetails,
            Long userId
    ) {

        if (driverDetails == null) {
            return;
        }

        try {

            jdbcTemplate.update(
                    EmployeeQueries.INSERT_EMPLOYEE_DRIVER_DETAILS,

                    employeeId,
                    driverDetails.getLicenseType(),
                    driverDetails.getLicenseNumber(),

                    driverDetails.getLicenseIssueDate() != null
                            ? java.sql.Date.valueOf(driverDetails.getLicenseIssueDate())
                            : null,

                    driverDetails.getLicenseExpiryDate() != null
                            ? java.sql.Date.valueOf(driverDetails.getLicenseExpiryDate())
                            : null,

                    driverDetails.getVehicleTypes(),

                    driverDetails.getExperienceYears(),
                    driverDetails.getAccidentFreeYears(),

                    driverDetails.getRouteExpertise(),

                    driverDetails.getIsAvailable(),
                    userId
                    );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee driver details", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee driver details"
            );
        }
    }

    @Override
    public void insertEmployeeGuideSpecializations(
            Long employeeId,
            List<EmployeeGuideSpecializationRequest> guideSpecializations,
            Long userId
    ) {

        if (guideSpecializations == null || guideSpecializations.isEmpty()) {
            return;
        }

        try {

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_GUIDE_SPECIALIZATION,
                    guideSpecializations,
                    guideSpecializations.size(),
                    (ps, guide) -> {

                        ps.setLong(1, employeeId);
                        ps.setString(2, guide.getSpecializationType());
                        ps.setString(3, guide.getRegions());
                        ps.setString(4, guide.getLanguages());
                        ps.setString(5, guide.getCertifications());

                        if (guide.getExperienceYears() != null) {
                            ps.setInt(6, guide.getExperienceYears());
                        } else {
                            ps.setNull(6, java.sql.Types.INTEGER);
                        }

                        if (guide.getRating() != null) {
                            ps.setBigDecimal(7, guide.getRating());
                        } else {
                            ps.setNull(7, java.sql.Types.DECIMAL);
                        }

                        if (guide.getIsAvailable() != null) {
                            ps.setBoolean(8, guide.getIsAvailable());
                        } else {
                            ps.setBoolean(8, false);
                        }
                        ps.setLong(9, userId);

                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee guide specializations", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee guide specializations"
            );
        }
    }

    @Override
    public void insertEmployeeIncentives(
            Long employeeId,
            List<EmployeeIncentiveRequest> incentives,
            Long userId
    ) {

        if (incentives == null || incentives.isEmpty()) {
            return;
        }

        try {

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_INCENTIVE,
                    incentives,
                    incentives.size(),
                    (ps, inc) -> {

                        ps.setLong(1, employeeId);

                        if (inc.getIncentiveDate() != null) {
                            ps.setDate(2, java.sql.Date.valueOf(inc.getIncentiveDate()));
                        } else {
                            ps.setNull(2, java.sql.Types.DATE);
                        }

                        ps.setString(3, inc.getIncentiveType());

                        if (inc.getAmount() != null) {
                            ps.setBigDecimal(4, inc.getAmount());
                        } else {
                            ps.setNull(4, java.sql.Types.DECIMAL);
                        }

                        ps.setString(5, inc.getCalculationBasis());
                        ps.setString(6, inc.getReferenceId());
                        ps.setString(7, inc.getPaymentStatus());

                        if (inc.getPaidDate() != null) {
                            ps.setDate(8, java.sql.Date.valueOf(inc.getPaidDate()));
                        } else {
                            ps.setNull(8, java.sql.Types.DATE);
                        }

                        ps.setLong(9, userId);

                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee incentives", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee incentives"
            );
        }
    }

    @Override
    public void insertEmployeeSalaryStructures(
            Long employeeId,
            List<EmployeeSalaryStructureRequest> salaryStructures,
            Long userId
    ) {

        if (salaryStructures == null || salaryStructures.isEmpty()) {
            return;
        }

        try {

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_SALARY_STRUCTURE,
                    salaryStructures,
                    salaryStructures.size(),
                    (ps, salary) -> {

                        ps.setLong(1, employeeId);

                        if (salary.getComponentId() != null) {
                            ps.setInt(2, salary.getComponentId());
                        } else {
                            ps.setNull(2, java.sql.Types.INTEGER);
                        }

                        if (salary.getAmount() != null) {
                            ps.setBigDecimal(3, salary.getAmount());
                        } else {
                            ps.setNull(3, java.sql.Types.DECIMAL);
                        }

                        if (salary.getEffectiveFrom() != null) {
                            ps.setDate(4, java.sql.Date.valueOf(salary.getEffectiveFrom()));
                        } else {
                            ps.setNull(4, java.sql.Types.DATE);
                        }

                        if (salary.getEffectiveTo() != null) {
                            ps.setDate(5, java.sql.Date.valueOf(salary.getEffectiveTo()));
                        } else {
                            ps.setNull(5, java.sql.Types.DATE);
                        }

                        Long statusId = statusRepository.getStatusIdByName(salary.getStatus());
                        ps.setLong(6, statusId != null ? statusId : 1L);

                        ps.setLong(7, userId);
                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee salary structures", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee salary structures"
            );
        }
    }

    @Override
    public void insertEmployeeSkills(
            Long employeeId,
            List<EmployeeSkillRequest> skills,
            Long userId
    ) {

        if (skills == null || skills.isEmpty()) {
            return;
        }

        try {

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_SKILL,
                    skills,
                    skills.size(),
                    (ps, skill) -> {

                        ps.setLong(1, employeeId);
                        ps.setString(2, skill.getSkillName());
                        ps.setString(3, skill.getSkillCategory());
                        ps.setString(4, skill.getProficiencyLevel());
                        ps.setString(5, skill.getCertification());

                        // certified_date
                        if (skill.getCertifiedDate() != null) {
                            ps.setDate(6, java.sql.Date.valueOf(skill.getCertifiedDate()));
                        } else {
                            ps.setNull(6, java.sql.Types.DATE);
                        }

                        // expiry_date
                        if (skill.getExpiryDate() != null) {
                            ps.setDate(7, java.sql.Date.valueOf(skill.getExpiryDate()));
                        } else {
                            ps.setNull(7, java.sql.Types.DATE);
                        }

                        // verified
                        if (skill.getVerified() != null) {
                            ps.setBoolean(8, skill.getVerified());
                        } else {
                            ps.setBoolean(8, false);
                        }

                        // verified_by (Long → BIGINT)
                        if (skill.getVerifiedBy() != null) {
                            ps.setLong(9, skill.getVerifiedBy());
                        } else {
                            ps.setNull(9, java.sql.Types.BIGINT);
                        }

                        // verified_date
                        if (skill.getVerifiedDate() != null) {
                            ps.setDate(10, java.sql.Date.valueOf(skill.getVerifiedDate()));
                        } else {
                            ps.setNull(10, java.sql.Types.DATE);
                        }

                        ps.setLong(11, userId);

                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee skills", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee skills"
            );
        }
    }

    @Override
    public void insertEmployeeWorkHistories(
            Long employeeId,
            List<EmployeeWorkHistoryRequest> workHistories,
            Long userId
    ) {

        if (workHistories == null || workHistories.isEmpty()) {
            return;
        }

        try {

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_WORK_HISTORY,
                    workHistories,
                    workHistories.size(),
                    (ps, wh) -> {

                        ps.setLong(1, employeeId);

                        if (wh.getDesignationId() != null) {
                            ps.setInt(2, wh.getDesignationId());
                        } else {
                            ps.setNull(2, java.sql.Types.INTEGER);
                        }

                        if (wh.getDepartmentId() != null) {
                            ps.setInt(3, wh.getDepartmentId());
                        } else {
                            ps.setNull(3, java.sql.Types.INTEGER);
                        }

                        if (wh.getSalary() != null) {
                            ps.setBigDecimal(4, wh.getSalary());
                        } else {
                            ps.setNull(4, java.sql.Types.DECIMAL);
                        }

                        if (wh.getStartDate() != null) {
                            ps.setDate(5, java.sql.Date.valueOf(wh.getStartDate()));
                        } else {
                            ps.setNull(5, java.sql.Types.DATE);
                        }

                        if (wh.getEndDate() != null) {
                            ps.setDate(6, java.sql.Date.valueOf(wh.getEndDate()));
                        } else {
                            ps.setNull(6, java.sql.Types.DATE);
                        }

                        ps.setString(7, wh.getEmploymentType());
                        ps.setString(8, wh.getReason());
                        ps.setString(9, wh.getNotes());

                        Long statusId = statusRepository.getStatusIdByName(wh.getStatus());
                        ps.setLong(10, statusId != null ? statusId : 1L);

                        ps.setLong(11, userId);

                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee work histories", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee work histories"
            );
        }
    }

    @Override
    public void insertEmployeeShiftAssignments(
            Long employeeId,
            List<EmployeeShiftAssignmentRequest> shiftAssignments,
            Long userId
    ) {

        if (shiftAssignments == null || shiftAssignments.isEmpty()) {
            return;
        }

        try {

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_SHIFT_ASSIGNMENT,
                    shiftAssignments,
                    shiftAssignments.size(),
                    (ps, shift) -> {

                        ps.setLong(1, employeeId);

                        if (shift.getShiftId() != null) {
                            ps.setInt(2, shift.getShiftId());
                        } else {
                            ps.setNull(2, java.sql.Types.INTEGER);
                        }

                        if (shift.getEffectiveFrom() != null) {
                            ps.setDate(3, java.sql.Date.valueOf(shift.getEffectiveFrom()));
                        } else {
                            ps.setNull(3, java.sql.Types.DATE);
                        }

                        if (shift.getEffectiveTo() != null) {
                            ps.setDate(4, java.sql.Date.valueOf(shift.getEffectiveTo()));
                        } else {
                            ps.setNull(4, java.sql.Types.DATE);
                        }

                        if (shift.getAssignedBy() != null) {
                            ps.setLong(5, shift.getAssignedBy());
                        } else {
                            ps.setLong(5, userId); // fallback
                        }

                        Long statusId = statusRepository.getStatusIdByName(shift.getStatus());
                        ps.setLong(6, statusId != null ? statusId : 1L);

                        ps.setLong(7, userId);

                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee shift assignments", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee shift assignments"
            );
        }
    }

    @Override
    public void insertEmployeeSocialMediaAccounts(
            Long employeeId,
            List<EmployeeSocialMediaRequest> socialMediaAccounts,
            Long userId
    ) {

        if (socialMediaAccounts == null || socialMediaAccounts.isEmpty()) {
            return;
        }

        try {

            jdbcTemplate.batchUpdate(
                    EmployeeQueries.INSERT_EMPLOYEE_SOCIAL_MEDIA,
                    socialMediaAccounts,
                    socialMediaAccounts.size(),
                    (ps, sm) -> {

                        ps.setLong(1, employeeId);

                        // platform_id
                        if (sm.getPlatformId() != null) {
                            ps.setInt(2, sm.getPlatformId());
                        } else {
                            ps.setNull(2, java.sql.Types.INTEGER);
                        }

                        ps.setString(3, sm.getUsername());
                        ps.setString(4, sm.getProfileUrl());

                        if (sm.getFollowerCount() != null) {
                            ps.setInt(5, sm.getFollowerCount());
                        } else {
                            ps.setNull(5, java.sql.Types.INTEGER);
                        }

                        if (sm.getIsPrimary() != null) {
                            ps.setBoolean(6, sm.getIsPrimary());
                        } else {
                            ps.setBoolean(6, false);
                        }

                        if (sm.getIsPublic() != null) {
                            ps.setBoolean(7, sm.getIsPublic());
                        } else {
                            ps.setBoolean(7, true);
                        }

                        if (sm.getVerified() != null) {
                            ps.setBoolean(8, sm.getVerified());
                        } else {
                            ps.setBoolean(8, false);
                        }

                        // verified_by
                        if (sm.getVerifiedBy() != null) {
                            ps.setLong(9, sm.getVerifiedBy());
                        } else {
                            ps.setNull(9, java.sql.Types.BIGINT);
                        }

                        // verified_date
                        if (sm.getVerifiedDate() != null) {
                            ps.setDate(10, java.sql.Date.valueOf(sm.getVerifiedDate()));
                        } else {
                            ps.setNull(10, java.sql.Types.DATE);
                        }

                        // last_updated
                        if (sm.getLastUpdated() != null) {
                            ps.setDate(11, java.sql.Date.valueOf(sm.getLastUpdated()));
                        } else {
                            ps.setNull(11, java.sql.Types.DATE);
                        }

                        Long statusId = statusRepository.getStatusIdByName(sm.getStatus());
                        ps.setLong(12, statusId != null ? statusId : 1L);

                        ps.setLong(13, userId);

                    }
            );

        } catch (Exception ex) {
            LOGGER.error("Error inserting employee social media accounts", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to insert employee social media accounts"
            );
        }
    }

    @Override
    public List<FilterItem> getDistnictDesignations() {
        try {
            return jdbcTemplate.query(
                    EmployeeQueries.GET_DISTINCT_DESIGNATIONS,
                    (rs, rowNum) -> FilterItem.builder()
                            .id(rs.getLong("id"))
                            .label(rs.getString("label"))
                            .build()
            );
        } catch (Exception ex) {
            LOGGER.error("Error fetching distinct designations", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch designations"
            );
        }
    }

    @Override
    public List<FilterItem> getActiveEmployeesIdsAndNames() {
        try {
            return jdbcTemplate.query(
                    EmployeeQueries.GET_ACTIVE_EMPLOYEES,
                    (rs, rowNum) -> FilterItem.builder()
                            .id(rs.getLong("id"))
                            .label(rs.getString("label"))
                            .build()
            );
        } catch (Exception ex) {
            LOGGER.error("Error fetching active employees", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch active employees"
            );
        }
    }

    @Override
    public List<FilterItem> getSalaryComponents() {
        try {
            return jdbcTemplate.query(
                    EmployeeQueries.GET_SALARY_COMPONENTS,
                    (rs, rowNum) -> FilterItem.builder()
                            .id(rs.getLong("id"))
                            .label(rs.getString("label"))
                            .build()
            );
        } catch (Exception ex) {
            LOGGER.error("Error fetching salary components", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch salary components"
            );
        }
    }

    @Override
    public List<FilterItem> getSocialMediaPlatforms() {
        try {
            return jdbcTemplate.query(
                    EmployeeQueries.GET_SOCIAL_MEDIA_PLATFORMS,
                    (rs, rowNum) -> FilterItem.builder()
                            .id(rs.getLong("id"))
                            .label(rs.getString("label"))
                            .build()
            );
        } catch (Exception ex) {
            LOGGER.error("Error fetching social media platforms", ex);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch social media platforms"
            );
        }
    }

}
