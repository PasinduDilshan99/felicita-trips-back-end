package com.felicita.service;

import com.felicita.model.request.*;
import com.felicita.model.response.*;

import java.util.List;

public interface PrivilegeService {
    CommonResponse<PrivilageParamResponse> getAllPrivileges(PrivilegeDataParamRequest privilegeDataParamRequest);

    CommonResponse<List<PrivilegeNameAndIdResponse>> getAllPrivilegesNamesAndIds();

    CommonResponse<PrivilegeDetailsResponse> getPrivilegeDetailsById(PrivilegeDetailsRequest privilegeDetailsRequest);

    CommonResponse<PrivilegeResponse> getPrivilegeBasicDetailsById(PrivilegeDetailsRequest privilegeDetailsRequest);

    CommonResponse<InsertResponse> createPrivilege(PrivilegeInsertRequest privilegeInsertRequest);

    CommonResponse<UpdateResponse> updatePrivilege(PrivilegeUpdateRequest privilegeUpdateRequest);

    CommonResponse<TerminateResponse> terminatePrivilege(PrivilegeTerminateRequest privilegeTerminateRequest);

    CommonResponse<PrivilegeStatisticsResponse> getPrivilegeStatistics();
}
