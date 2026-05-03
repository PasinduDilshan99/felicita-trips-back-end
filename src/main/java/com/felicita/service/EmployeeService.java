package com.felicita.service;

import com.felicita.model.request.EmployeeBasicDetailsParamRequest;
import com.felicita.model.request.EmployeeFullDetailsRequest;
import com.felicita.model.request.employee.EmployeeCreateRequest;
import com.felicita.model.response.*;

import java.util.List;

public interface EmployeeService {
    CommonResponse<List<EmployeeWithSocialMediaResponse>> getEmployeeWithSocailMedia();

    CommonResponse<List<EmployeeWithSocialMediaResponse>> getALlEmployeeWithSocailMedia();

    CommonResponse<List<EmployeeGuideResponse>> getEmployeeGiudeDetails();

    CommonResponse<TourAssignedEmployeeResponse> getEmployeeAssignToTourId(Long tourId);

    CommonResponse<List<EmployeesForAssignTourResponse>> getEmployeeDetailsForAssignTour();

    CommonResponse<CeoDetailsReponse> getCeoDetails();

    CommonResponse<List<EmployeeBasicDetailsResponse>> getAllEmpmoyeesBasicDetails(EmployeeBasicDetailsParamRequest employeeBasicDetailsParamRequest);

    CommonResponse<EmployeeFullDetailsResponse> getEmployeeFullDetails(EmployeeFullDetailsRequest employeeFullDetailsRequest);

    CommonResponse<EmployeeStatisticsResponse> getEmployeeStatistics();

    CommonResponse<EmployeeBasicDetailsParamsResponse> getAllEmpmoyeesBasicDetailsParams();

    CommonResponse<InsertResponse> createEmployee(EmployeeCreateRequest employeeCreateRequest);

    CommonResponse<EmployeeCreateDataResponse> getCreateEmployeeData();
}
