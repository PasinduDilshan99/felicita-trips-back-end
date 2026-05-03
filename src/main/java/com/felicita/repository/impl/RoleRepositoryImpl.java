package com.felicita.repository.impl;

import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.queries.RoleQueries;
import com.felicita.repository.RoleRepository;
import com.felicita.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;

    @Autowired
    public RoleRepositoryImpl(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    @Override
    public RoleParamResponse getAllRoles(RoleDataParamRequest request) {
        try {
            LOGGER.info("Fetching roles with filters, sorting, pagination");

            StringBuilder sql = new StringBuilder(RoleQueries.GET_ALL_ROLES);
            StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM roles r WHERE r.terminated_at IS NULL ");

            // -------------------------
            // FILTERS
            // -------------------------
            if (request.getName() != null && !request.getName().isEmpty()) {
                sql.append(" AND r.name LIKE '%").append(request.getName()).append("%'");
                countSql.append(" AND r.name LIKE '%").append(request.getName()).append("%'");
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
                sortBy = "r.created_at";
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
            List<RoleResponse> roles = jdbcTemplate.query(
                    sql.toString(),
                    (rs, rowNum) -> RoleResponse.builder()
                            .roleId(rs.getLong("id"))
                            .roleName(rs.getString("name"))
                            .roleDescription(rs.getString("description"))
                            .roleStatus(rs.getString("status"))
                            .build()
            );

            // -------------------------
            // TOTAL COUNT
            // -------------------------
            int total = jdbcTemplate.queryForObject(countSql.toString(), Integer.class);

            LOGGER.info("Roles fetched successfully. Total: {}", total);

            return RoleParamResponse.builder()
                    .roleResponses(roles)
                    .totalResponse(total)
                    .pageNumber(pageNumber)
                    .build();

        } catch (Exception e) {
            LOGGER.error("Error fetching roles", e);
            throw new RuntimeException("Failed to fetch roles");
        }
    }

    @Override
    public List<RoleNameAndIdResponse> getAllRoleNamesAndIds() {
        try {
            LOGGER.info("Fetching role names and IDs");

            return jdbcTemplate.query(RoleQueries.GET_ROLE_NAMES_AND_IDS,
                    (rs, rowNum) -> RoleNameAndIdResponse.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching role names and IDs", e);
            throw new RuntimeException("Failed to fetch role names and IDs");
        }
    }

    @Override
    public RoleDetailsResponse getRoleDetailsById(RoleDetailsRequest request) {
        try {
            LOGGER.info("Fetching role details for ID: {}", request.getRoleId());

            RoleDetailsResponse role = jdbcTemplate.queryForObject(
                    RoleQueries.GET_ROLE_BY_ID,
                    (rs, rowNum) -> RoleDetailsResponse.builder()
                            .roleId(rs.getLong("id"))
                            .roleName(rs.getString("name"))
                            .roleDescription(rs.getString("description"))
                            .roleStatus(rs.getString("status"))
                            .privileges(List.of())
                            .build(),
                    request.getRoleId()
            );

            List<RoleDetailsResponse.Privilege> privileges = jdbcTemplate.query(
                    RoleQueries.GET_PRIVILEGES_BY_ROLE_ID,
                    (rs, rowNum) -> RoleDetailsResponse.Privilege.builder()
                            .privilegeId(rs.getLong("id"))
                            .privilegeName(rs.getString("name"))
                            .privilegeDescription(rs.getString("description"))
                            .privilegeStatus(rs.getString("status"))
                            .build(),
                    request.getRoleId()
            );

            role.setPrivileges(privileges);
            return role;

        } catch (Exception e) {
            LOGGER.error("Error fetching role details", e);
            throw new RuntimeException("Failed to fetch role details");
        }
    }

    @Override
    public RoleResponse getRoleBasicDetailsById(RoleDetailsRequest request) {
        try {
            LOGGER.info("Fetching basic role details for ID: {}", request.getRoleId());

            return jdbcTemplate.queryForObject(
                    RoleQueries.GET_ROLE_BASIC_BY_ID,
                    (rs, rowNum) -> RoleResponse.builder()
                            .roleId(rs.getLong("id"))
                            .roleName(rs.getString("name"))
                            .roleDescription(rs.getString("description"))
                            .roleStatus(rs.getString("status"))
                            .build(),
                    request.getRoleId()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching basic role details for ID: {}", request.getRoleId(), e);
            throw new RuntimeException("Failed to fetch role basic details");
        }
    }

    @Override
    public Long createRole(RoleInsertRequest request, Long userId) {
        try {
            LOGGER.info("Creating role: {}", request.getName());

            Long statusId = statusRepository.getStatusIdByName(CommonStatus.valueOf(request.getStatus()).name());

            jdbcTemplate.update(
                    RoleQueries.INSERT_ROLE,
                    request.getName(),
                    statusId,
                    request.getDescription(),
                    userId
            );

            return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        } catch (Exception e) {
            LOGGER.error("Error creating role", e);
            throw new RuntimeException("Failed to create role");
        }
    }

    @Override
    public void addPrivilegesToRole(Long roleId, List<Long> privilegesIds, Long userId) {
        try {
            LOGGER.info("Adding privileges to role ID: {}, Privileges count: {}", roleId, privilegesIds.size());

            Long statusId = statusRepository.getStatusIdByName(CommonStatus.ACTIVE.name());

            for (Long privilegeId : privilegesIds) {
                jdbcTemplate.update(
                        RoleQueries.INSERT_ROLE_PRIVILEGE,
                        roleId,
                        privilegeId,
                        statusId,
                        userId
                );
            }

            LOGGER.info("Privileges added successfully to role ID: {}", roleId);

        } catch (Exception e) {
            LOGGER.error("Error adding privileges to role", e);
            throw new RuntimeException("Failed to add privileges to role");
        }
    }

    @Override
    public void updateRole(RoleUpdateRequest request, Long userId) {
        try {
            LOGGER.info("Updating role ID: {}", request.getId());

            Long statusId = statusRepository.getStatusIdByName(CommonStatus.valueOf(request.getStatus()).name());

            jdbcTemplate.update(
                    RoleQueries.UPDATE_ROLE,
                    request.getName(),
                    statusId,
                    request.getDescription(),
                    userId,
                    request.getId()
            );

        } catch (Exception e) {
            LOGGER.error("Error updating role", e);
            throw new RuntimeException("Failed to update role");
        }
    }

    @Override
    public void terminatePrivilegesToRole(Long roleId, List<Long> removePrivilegesIds, Long userId) {
        try {
            LOGGER.info("Terminating privileges from role ID: {}, Privileges count: {}", roleId, removePrivilegesIds.size());

            for (Long privilegeId : removePrivilegesIds) {
                jdbcTemplate.update(
                        RoleQueries.TERMINATE_ROLE_PRIVILEGE,
                        userId,
                        roleId,
                        privilegeId
                );
            }

            LOGGER.info("Privileges terminated successfully from role ID: {}", roleId);

        } catch (Exception e) {
            LOGGER.error("Error terminating privileges from role", e);
            throw new RuntimeException("Failed to terminate privileges from role");
        }
    }

    @Override
    public void terminateRole(RoleTerminateRequest request, Long userId) {
        try {
            LOGGER.info("Terminating role ID: {}", request.getRoleId());

            Long statusId = statusRepository.getStatusIdByName(CommonStatus.TERMINATED.name());

            jdbcTemplate.update(
                    RoleQueries.TERMINATE_ROLE,
                    statusId,
                    userId,
                    request.getRoleId()
            );

            // Also terminate all role-privilege associations
            LOGGER.info("statusId" + statusId);
            jdbcTemplate.update(
                    RoleQueries.TERMINATE_ALL_ROLE_PRIVILEGES,
                    statusId,
                    userId,
                    request.getRoleId()
            );
            LOGGER.info("statusId" + statusId);


        } catch (Exception e) {
            LOGGER.error("Error terminating role", e);
            throw new RuntimeException("Failed to terminate role");
        }
    }

    @Override
    public RoleStatisticsResponse.RoleDetails getRoleDetailsStatistics() {
        try {
            LOGGER.info("Fetching role statistics");

            Long activeId = statusRepository.getStatusIdByName(CommonStatus.ACTIVE.name());
            Long inactiveId = statusRepository.getStatusIdByName(CommonStatus.INACTIVE.name());

            return RoleStatisticsResponse.RoleDetails.builder()
                    .totalCount(jdbcTemplate.queryForObject(RoleQueries.TOTAL_COUNT, Integer.class))
                    .activeCount(jdbcTemplate.queryForObject(RoleQueries.ACTIVE_COUNT, Integer.class, activeId))
                    .inActiveCount(jdbcTemplate.queryForObject(RoleQueries.INACTIVE_COUNT, Integer.class, inactiveId))
                    .hiddenCount(jdbcTemplate.queryForObject(RoleQueries.TERMINATED_COUNT, Integer.class))
                    .recentlyAddedCount(jdbcTemplate.queryForObject(RoleQueries.RECENTLY_ADDED, Integer.class))
                    .recentlyUpdateCount(jdbcTemplate.queryForObject(RoleQueries.RECENTLY_UPDATED, Integer.class))
                    .build();

        } catch (Exception e) {
            LOGGER.error("Error fetching role statistics", e);
            throw new RuntimeException("Failed to fetch statistics");
        }
    }

    @Override
    public List<RoleStatisticsResponse.Recent> getRoleRecentlyUpdateStatistics() {
        try {
            LOGGER.info("Fetching recently updated roles");

            return jdbcTemplate.query(
                    RoleQueries.RECENT_UPDATES,
                    (rs, rowNum) -> RoleStatisticsResponse.Recent.builder()
                            .username(rs.getString("username"))
                            .userId(rs.getInt("user_id"))
                            .count(rs.getInt("updated_count"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching recently updated roles", e);
            throw new RuntimeException("Failed to fetch recently updated roles");
        }
    }

    @Override
    public List<RoleStatisticsResponse.Recent> getRoleRecentlyCreateStatistics() {
        try {
            LOGGER.info("Fetching recently created roles");

            return jdbcTemplate.query(
                    RoleQueries.RECENT_CREATED,
                    (rs, rowNum) -> RoleStatisticsResponse.Recent.builder()
                            .username(rs.getString("username"))
                            .userId(rs.getInt("user_id"))
                            .count(rs.getInt("created_count"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching recently created roles", e);
            throw new RuntimeException("Failed to fetch recently created roles");
        }
    }

    @Override
    public List<RoleStatisticsResponse.Recent> getRoleRecentlyTerminateStatistics() {
        try {
            LOGGER.info("Fetching recently terminated roles");

            return jdbcTemplate.query(
                    RoleQueries.RECENT_TERMINATED,
                    (rs, rowNum) -> RoleStatisticsResponse.Recent.builder()
                            .username(rs.getString("username"))
                            .userId(rs.getInt("user_id"))
                            .count(rs.getInt("terminated_count"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching recently terminated roles", e);
            throw new RuntimeException("Failed to fetch recently terminated roles");
        }
    }

    @Override
    public List<RoleStatisticsResponse.RoleUsage> getRoleUsageTerminateStatistics() {
        try {
            LOGGER.info("Fetching role usage statistics");

            return jdbcTemplate.query(
                    RoleQueries.ROLE_USAGE_STATISTICS,
                    (rs, rowNum) -> RoleStatisticsResponse.RoleUsage.builder()
                            .roleId(rs.getLong("role_id"))
                            .roleName(rs.getString("role_name"))
                            .userCount(rs.getInt("user_count"))
                            .build()
            );

        } catch (Exception e) {
            LOGGER.error("Error fetching role usage statistics", e);
            throw new RuntimeException("Failed to fetch role usage statistics");
        }
    }

    @Override
    public void updateRolePrivileges(List<RoleUpdateRequest.UpdatePrivilege> updatePrivileges, Long userId) {
        try {
            LOGGER.info("Updating role privileges for {} privilege(s)", updatePrivileges.size());

            for (RoleUpdateRequest.UpdatePrivilege updatePrivilege : updatePrivileges) {
                Long roleId = updatePrivilege.getRoleId();
                Long privilegeId = updatePrivilege.getPrivilegeId();
                String status = updatePrivilege.getStatus();

                // Get status ID from common_status table
                Long statusId = statusRepository.getStatusIdByName(CommonStatus.valueOf(status).name());

                // Check if the role-privilege association exists and is not terminated
                Integer exists = jdbcTemplate.queryForObject(
                        RoleQueries.CHECK_ROLE_PRIVILEGE_EXISTS,
                        Integer.class,
                        roleId, privilegeId
                );

                if (exists != null && exists > 0) {
                    // Update existing association
                    jdbcTemplate.update(
                            RoleQueries.UPDATE_ROLE_PRIVILEGE_STATUS,
                            statusId,
                            userId,
                            roleId,
                            privilegeId
                    );
                    LOGGER.info("Updated role-privilege association - Role ID: {}, Privilege ID: {}, Status: {}",
                            roleId, privilegeId, status);
                } else {
                    // Create new association if it doesn't exist
                    jdbcTemplate.update(
                            RoleQueries.INSERT_ROLE_PRIVILEGE,
                            roleId,
                            privilegeId,
                            statusId,
                            userId
                    );
                    LOGGER.info("Created new role-privilege association - Role ID: {}, Privilege ID: {}, Status: {}",
                            roleId, privilegeId, status);
                }
            }

            LOGGER.info("Successfully updated all role privileges");

        } catch (Exception e) {
            LOGGER.error("Error updating role privileges", e);
            throw new RuntimeException("Failed to update role privileges: " + e.getMessage());
        }
    }
}