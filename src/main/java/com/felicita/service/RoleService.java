package com.felicita.service;

import com.felicita.model.request.*;
import com.felicita.model.response.*;

import java.util.List;

public interface RoleService {
    CommonResponse<RoleParamResponse> getAllRoles(RoleDataParamRequest roleDataParamRequest);

    CommonResponse<List<RoleNameAndIdResponse>> getAllRoleNamesAndIds();

    CommonResponse<RoleDetailsResponse> getRoleDetailsById(RoleDetailsRequest roleDetailsRequest);

    CommonResponse<RoleResponse> getRoleBasicDetailsById(RoleDetailsRequest roleDetailsRequest);

    CommonResponse<InsertResponse> createRole(RoleInsertRequest roleInsertRequest);

    CommonResponse<UpdateResponse> updateRole(RoleUpdateRequest roleUpdateRequest);

    CommonResponse<TerminateResponse> terminateRole(RoleTerminateRequest roleTerminateRequest);

    CommonResponse<RoleStatisticsResponse> getRoleStatistics();
}
