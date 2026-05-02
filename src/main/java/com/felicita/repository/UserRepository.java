package com.felicita.repository;

import com.felicita.model.dto.UserBasicDetailsDto;
import com.felicita.model.request.BasicUserDetailsRequest;
import com.felicita.model.response.UserBasicDetailsResponse;
import com.felicita.model.response.UsernameAndIdWithoutEmployeesResponse;

import java.util.List;

public interface UserRepository {
    List<UsernameAndIdWithoutEmployeesResponse> getUsernamesAndIdsWithoutEmployees();

    UserBasicDetailsDto getUserBasicDetailsForEmployeeCreate(Long userId);

    UserBasicDetailsResponse getUserDetailsByUserId(BasicUserDetailsRequest basicUserDetailsRequest);
}
