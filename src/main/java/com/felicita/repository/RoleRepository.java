package com.felicita.repository;

import com.felicita.model.request.*;
import com.felicita.model.response.*;

import java.util.List;

public interface RoleRepository {
    RoleParamResponse getAllRoles(RoleDataParamRequest roleDataParamRequest);

    List<RoleNameAndIdResponse> getAllRoleNamesAndIds();

    RoleDetailsResponse getRoleDetailsById(RoleDetailsRequest roleDetailsRequest);

    RoleResponse getRoleBasicDetailsById(RoleDetailsRequest roleDetailsRequest);

    Long createRole(RoleInsertRequest roleInsertRequest, Long userId);

    void addPrivilegesToRole(Long roleId, List<Long> privilegesIds, Long userId);

    void updateRole(RoleUpdateRequest roleUpdateRequest, Long userId);

    void terminatePrivilegesToRole(Long id, List<Long> removePrivilegesIds, Long userId);

    void terminateRole(RoleTerminateRequest roleTerminateRequest, Long userId);

    RoleStatisticsResponse.RoleDetails getRoleDetailsStatistics();

    List<RoleStatisticsResponse.Recent> getRoleRecentlyUpdateStatistics();

    List<RoleStatisticsResponse.Recent> getRoleRecentlyCreateStatistics();

    List<RoleStatisticsResponse.Recent> getRoleRecentlyTerminateStatistics();

    List<RoleStatisticsResponse.RoleUsage> getRoleUsageTerminateStatistics();

    void updateRolePrivileges(List<RoleUpdateRequest.UpdatePrivilege> updatePrivileges, Long userId);
}
