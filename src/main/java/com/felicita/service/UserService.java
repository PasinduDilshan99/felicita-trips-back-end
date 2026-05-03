package com.felicita.service;

import com.felicita.model.request.BasicUserDetailsRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.UserBasicDetailsResponse;
import com.felicita.model.response.UsernameAndIdWithoutEmployeesResponse;

import java.util.List;

public interface UserService {
    CommonResponse<List<UsernameAndIdWithoutEmployeesResponse>> getUsernamesAndIdsWithoutEmployees();

    CommonResponse<UserBasicDetailsResponse> getUserDetailsByUserId(BasicUserDetailsRequest basicUserDetailsRequest);
}
