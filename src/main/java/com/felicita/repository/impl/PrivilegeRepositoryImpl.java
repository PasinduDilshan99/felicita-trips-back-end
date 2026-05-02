package com.felicita.repository.impl;

import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.queries.PrivilegeQueries;
import com.felicita.repository.PrivilegeRepository;
import com.felicita.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PrivilegeRepositoryImpl implements PrivilegeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;

    @Autowired
    public PrivilegeRepositoryImpl(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    @Override
    public PrivilageParamResponse getAllPrivileges(PrivilegeDataParamRequest request) {
        try {
            LOGGER.info("Fetching privileges with filters, sorting, pagination");

            StringBuilder sql = new StringBuilder(PrivilegeQueries.GET_ALL_PRIVILEGES);
            StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM privileges p WHERE p.terminated_at IS NULL ");

            // -------------------------
            // FILTERS
            // -------------------------
            if (request.getName() != null && !request.getName().isEmpty()) {
                sql.append(" AND p.name LIKE '%").append(request.getName()).append("%'");
                countSql.append(" AND p.name LIKE '%").append(request.getName()).append("%'");
            }

            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                sql.append(" AND cs.name = '").append(request.getStatus()).append("'");
                countSql.append(" AND cs.name = '").append(request.getStatus()).append("'");
            }

            // -------------------------
            // SORTING
            // -------------------------
            String sortBy = request.getSortBy();
            String sortDirection = request.getSortDirection();

            if (sortBy == null || sortBy.isEmpty()) {
                sortBy = "p.created_at";
            }

            if (sortDirection == null || sortDirection.isEmpty()) {
                sortDirection = "DESC";
            }

            sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortDirection);

            // -------------------------
            // PAGINATION
            // -------------------------
            int pageNumber = request.getPageNumber();
            int pageSize = request.getPageSize();
            int offset = pageNumber * pageSize;

            sql.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(offset);

            // -------------------------
            // QUERY DATA
            // -------------------------
            List<PrivilegeResponse> privileges = jdbcTemplate.query(
                    sql.toString(),
                    (rs, rowNum) -> PrivilegeResponse.builder()
                            .privilegeId(rs.getLong("id"))
                            .privilegeName(rs.getString("name"))
                            .privilegeDescription(rs.getString("description"))
                            .privilegeStatus(rs.getString("status"))
                            .build()
            );

            // -------------------------
            // TOTAL COUNT
            // -------------------------
            int total = jdbcTemplate.queryForObject(countSql.toString(), Integer.class);

            LOGGER.info("Privileges fetched successfully. Total: {}", total);

            return PrivilageParamResponse.builder()
                    .privilegeResponses(privileges)
                    .totalResponse(total)
                    .pageNumber(pageNumber)
                    .build();

        } catch (Exception e) {
            LOGGER.error("Error fetching privileges", e);
            throw new RuntimeException("Failed to fetch privileges");
        }
    }

    @Override
    public List<PrivilegeNameAndIdResponse> getAllPrivilegesNamesAndIds() {
        try {
            LOGGER.info("Fetching privilege names and IDs");

            return jdbcTemplate.query(PrivilegeQueries.GET_PRIVILEGE_NAMES_AND_IDS,
                    (rs, rowNum) -> PrivilegeNameAndIdResponse.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching privilege names and IDs", e);
            throw new RuntimeException("Failed to fetch privilege names and IDs");
        }
    }

    @Override
    public PrivilegeDetailsResponse getPrivilegeDetailsById(PrivilegeDetailsRequest request) {
        try {
            LOGGER.info("Fetching privilege details for ID: {}", request.getPrivilegeId());

            PrivilegeDetailsResponse privilege = jdbcTemplate.queryForObject(
                    PrivilegeQueries.GET_PRIVILEGE_BY_ID,
                    (rs, rowNum) -> PrivilegeDetailsResponse.builder()
                            .privilegeId(rs.getLong("id"))
                            .privilegeName(rs.getString("name"))
                            .privilegeDescription(rs.getString("description"))
                            .privilegeStatus(rs.getString("status"))
                            .roles(List.of())
                            .build(),
                    request.getPrivilegeId()
            );

            List<PrivilegeDetailsResponse.Role> roles = jdbcTemplate.query(
                    PrivilegeQueries.GET_ROLES_BY_PRIVILEGE_ID,
                    (rs, rowNum) -> PrivilegeDetailsResponse.Role.builder()
                            .roleId(rs.getLong("id"))
                            .roleName(rs.getString("name"))
                            .roleDescription(rs.getString("description"))
                            .roleStatus(rs.getString("status"))
                            .build(),
                    request.getPrivilegeId()
            );

            privilege.setRoles(roles);
            return privilege;

        } catch (Exception e) {
            LOGGER.error("Error fetching privilege details", e);
            throw new RuntimeException("Failed to fetch privilege details");
        }
    }

    @Override
    public PrivilegeResponse getPrivilegeBasicDetailsById(PrivilegeDetailsRequest request) {
        try {
            LOGGER.info("Fetching basic privilege details for ID: {}", request.getPrivilegeId());

            return jdbcTemplate.queryForObject(
                    PrivilegeQueries.GET_PRIVILEGE_BASIC_BY_ID,
                    (rs, rowNum) -> PrivilegeResponse.builder()
                            .privilegeId(rs.getLong("id"))
                            .privilegeName(rs.getString("name"))
                            .privilegeDescription(rs.getString("description"))
                            .privilegeStatus(rs.getString("status"))
                            .build(),
                    request.getPrivilegeId()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching basic privilege details for ID: {}", request.getPrivilegeId(), e);
            throw new RuntimeException("Failed to fetch privilege basic details");
        }
    }

    @Override
    public Long createPrivilege(PrivilegeInsertRequest request, Long userId) {
        try {
            LOGGER.info("Creating privilege: {}", request.getName());

            Long statusId = statusRepository.getStatusIdByName(CommonStatus.valueOf(request.getStatus()).name());

            jdbcTemplate.update(
                    PrivilegeQueries.INSERT_PRIVILEGE,
                    request.getName(),
                    statusId,
                    request.getDescription(),
                    userId
            );

            return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        } catch (Exception e) {
            LOGGER.error("Error creating privilege", e);
            throw new RuntimeException("Failed to create privilege");
        }
    }

    @Override
    public void updatePrivilege(PrivilegeUpdateRequest request, Long userId) {
        try {
            LOGGER.info("Updating privilege ID: {}", request.getId());

            Long statusId = statusRepository.getStatusIdByName(CommonStatus.valueOf(request.getStatus()).name());

            jdbcTemplate.update(
                    PrivilegeQueries.UPDATE_PRIVILEGE,
                    request.getName(),
                    statusId,
                    request.getDescription(),
                    userId,
                    request.getId()
            );

        } catch (Exception e) {
            LOGGER.error("Error updating privilege", e);
            throw new RuntimeException("Failed to update privilege");
        }
    }

    @Override
    public void terminatePrivilege(PrivilegeTerminateRequest request, Long userId) {
        try {
            LOGGER.info("Terminating privilege ID: {}", request.getId());

            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.name());

            jdbcTemplate.update(
                    PrivilegeQueries.TERMINATE_PRIVILEGE,
                    statusId,
                    userId,
                    request.getId()
            );

        } catch (Exception e) {
            LOGGER.error("Error terminating privilege", e);
            throw new RuntimeException("Failed to terminate privilege");
        }
    }

    @Override
    public PrivilegeStatisticsResponse.PrivilegeDetails getPrivilegeDetailsStatistics() {
        try {
            LOGGER.info("Fetching privilege statistics");

            Long activeId = statusRepository.getStatusIdByName(CommonStatus.ACTIVE.name());
            Long inactiveId = statusRepository.getStatusIdByName(CommonStatus.INACTIVE.name());

            return PrivilegeStatisticsResponse.PrivilegeDetails.builder()
                    .totalCount(jdbcTemplate.queryForObject(PrivilegeQueries.TOTAL_COUNT, Integer.class))
                    .activeCount(jdbcTemplate.queryForObject(PrivilegeQueries.ACTIVE_COUNT, Integer.class, activeId))
                    .inActiveCount(jdbcTemplate.queryForObject(PrivilegeQueries.INACTIVE_COUNT, Integer.class, inactiveId))
                    .hiddenCount(jdbcTemplate.queryForObject(PrivilegeQueries.TERMINATED_COUNT, Integer.class))
                    .recentlyAddedCount(jdbcTemplate.queryForObject(PrivilegeQueries.RECENTLY_ADDED, Integer.class))
                    .recentlyUpdateCount(jdbcTemplate.queryForObject(PrivilegeQueries.RECENTLY_UPDATED, Integer.class))
                    .build();

        } catch (Exception e) {
            LOGGER.error("Error fetching privilege statistics", e);
            throw new RuntimeException("Failed to fetch statistics");
        }
    }

    @Override
    public List<PrivilegeStatisticsResponse.Recent> getPrivilegeRecentlyUpdateStatistics() {
        try {
            LOGGER.info("Fetching recently updated privileges");

            return jdbcTemplate.query(
                    PrivilegeQueries.RECENT_UPDATES,
                    (rs, rowNum) -> PrivilegeStatisticsResponse.Recent.builder()
                            .username(rs.getString("username"))
                            .userId(rs.getInt("user_id"))
                            .count(rs.getInt("updated_count"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching recently updated privileges", e);
            throw new RuntimeException("Failed to fetch recently updated privileges");
        }
    }

    @Override
    public List<PrivilegeStatisticsResponse.Recent> getPrivilegeRecentlyCreateStatistics() {
        try {
            LOGGER.info("Fetching recently created privileges");

            return jdbcTemplate.query(
                    PrivilegeQueries.RECENT_CREATED,
                    (rs, rowNum) -> PrivilegeStatisticsResponse.Recent.builder()
                            .username(rs.getString("username"))
                            .userId(rs.getInt("user_id"))
                            .count(rs.getInt("created_count"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching recently created privileges", e);
            throw new RuntimeException("Failed to fetch recently created privileges");
        }
    }

    @Override
    public List<PrivilegeStatisticsResponse.Recent> getPrivilegeRecentlyTerminateStatistics() {
        try {
            LOGGER.info("Fetching recently terminated privileges");

            return jdbcTemplate.query(
                    PrivilegeQueries.RECENT_TERMINATED,
                    (rs, rowNum) -> PrivilegeStatisticsResponse.Recent.builder()
                            .username(rs.getString("username"))
                            .userId(rs.getInt("user_id"))
                            .count(rs.getInt("terminated_count"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching recently terminated privileges", e);
            throw new RuntimeException("Failed to fetch recently terminated privileges");
        }
    }



}
