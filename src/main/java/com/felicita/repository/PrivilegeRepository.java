package com.felicita.repository;

import com.felicita.model.request.*;
import com.felicita.model.response.*;

import java.util.List;

public interface PrivilegeRepository {
    PrivilageParamResponse getAllPrivileges(PrivilegeDataParamRequest privilegeDataParamRequest);

    List<PrivilegeNameAndIdResponse> getAllPrivilegesNamesAndIds();

    PrivilegeDetailsResponse getPrivilegeDetailsById(PrivilegeDetailsRequest privilegeDetailsRequest);

    PrivilegeResponse getPrivilegeBasicDetailsById(PrivilegeDetailsRequest privilegeDetailsRequest);

    Long createPrivilege(PrivilegeInsertRequest privilegeInsertRequest, Long userId);

    void updatePrivilege(PrivilegeUpdateRequest privilegeUpdateRequest, Long userId);

    void terminatePrivilege(PrivilegeTerminateRequest privilegeTerminateRequest, Long userId);

    PrivilegeStatisticsResponse.PrivilegeDetails getPrivilegeDetailsStatistics();

    List<PrivilegeStatisticsResponse.Recent> getPrivilegeRecentlyUpdateStatistics();

    List<PrivilegeStatisticsResponse.Recent> getPrivilegeRecentlyCreateStatistics();

    List<PrivilegeStatisticsResponse.Recent> getPrivilegeRecentlyTerminateStatistics();
}
