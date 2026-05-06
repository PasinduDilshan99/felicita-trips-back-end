package com.felicita.controller;

import com.felicita.model.request.EmployeeBasicDetailsParamRequest;
import com.felicita.model.request.EmployeeFullDetailsRequest;
import com.felicita.model.request.employee.EmployeeCreateRequest;
import com.felicita.model.response.*;
import com.felicita.service.EmployeeService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/employee")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/with-social-media-links")
    public ResponseEntity<CommonResponse<List<EmployeeWithSocialMediaResponse>>> getEmployeeWithSocailMedia(){
        LOGGER.info("{} Start execute get employee with social media details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<EmployeeWithSocialMediaResponse>> response = employeeService.getEmployeeWithSocailMedia();
        LOGGER.info("{} End execute get employee with social media details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<CommonResponse<List<EmployeeWithSocialMediaResponse>>> getALlEmployeeWithSocailMedia(){
        LOGGER.info("{} Start execute get all employee with social media details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<EmployeeWithSocialMediaResponse>> response = employeeService.getALlEmployeeWithSocailMedia();
        LOGGER.info("{} End execute get all employee with social media details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-guides")
    public ResponseEntity<CommonResponse<List<EmployeeGuideResponse>>> getEmployeeGiudeDetails(){
        LOGGER.info("{} Start execute get employee guide with social media details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<EmployeeGuideResponse>> response = employeeService.getEmployeeGiudeDetails();
        LOGGER.info("{} End execute get employee guide with social media details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/employee-details/{tourId}")
    public ResponseEntity<CommonResponse<TourAssignedEmployeeResponse>> getEmployeeAssignToTourId(@PathVariable Long tourId){
        LOGGER.info("{} Start execute get employee details assign to tour id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourAssignedEmployeeResponse> response = employeeService.getEmployeeAssignToTourId(tourId);
        LOGGER.info("{} End execute get employee details assign to tour id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/employee-details-for-assign-tour")
    public ResponseEntity<CommonResponse<List<EmployeesForAssignTourResponse>>> getEmployeeDetailsForAssignTour(){
        LOGGER.info("{} Start execute get employee details for assign tour {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<EmployeesForAssignTourResponse>> response = employeeService.getEmployeeDetailsForAssignTour();
        LOGGER.info("{} End execute get employee details assign for tour {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/ceo-details")
    public ResponseEntity<CommonResponse<CeoDetailsReponse>> getCeoDetails(){
        LOGGER.info("{} Start execute get ceo details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<CeoDetailsReponse> response = employeeService.getCeoDetails();
        LOGGER.info("{} End execute get ceo details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/employee-basic-details")
    public ResponseEntity<CommonResponse<List<EmployeeBasicDetailsResponse>>> getAllEmpmoyeesBasicDetails(@RequestBody EmployeeBasicDetailsParamRequest employeeBasicDetailsParamRequest){
        LOGGER.info("{} Start execute get all employee basic details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<EmployeeBasicDetailsResponse>> response = employeeService.getAllEmpmoyeesBasicDetails(employeeBasicDetailsParamRequest);
        LOGGER.info("{} End execute get all employee basic details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/employee-basic-details-params")
    public ResponseEntity<CommonResponse<EmployeeBasicDetailsParamsResponse>> getAllEmpmoyeesBasicDetailsParams(){
        LOGGER.info("{} Start execute get all employee basic details params {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<EmployeeBasicDetailsParamsResponse> response = employeeService.getAllEmpmoyeesBasicDetailsParams();
        LOGGER.info("{} End execute get all employee basic details params {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/employee-full-details")
    public ResponseEntity<CommonResponse<EmployeeFullDetailsResponse>> getEmployeeFullDetails(@RequestBody EmployeeFullDetailsRequest employeeFullDetailsRequest){
        LOGGER.info("{} Start execute get employee full details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<EmployeeFullDetailsResponse> response = employeeService.getEmployeeFullDetails(employeeFullDetailsRequest);
        LOGGER.info("{} End execute get employee full details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/employee-statistics")
    public ResponseEntity<CommonResponse<EmployeeStatisticsResponse>> getEmployeeStatistics() {
        LOGGER.info("{} Start execute get employee statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<EmployeeStatisticsResponse> response = employeeService.getEmployeeStatistics();
        LOGGER.info("{} End execute get employee statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create-employee")
    public ResponseEntity<CommonResponse<InsertResponse>> createEmployee(@RequestBody EmployeeCreateRequest employeeCreateRequest) {
        LOGGER.info("{} Start execute create employee  {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = employeeService.createEmployee(employeeCreateRequest);
        LOGGER.info("{} End execute create employee {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/create-employee-data")
    public ResponseEntity<CommonResponse<EmployeeCreateDataResponse>> getCreateEmployeeData() {
        LOGGER.info("{} Start execute get create employee data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<EmployeeCreateDataResponse> response = employeeService.getCreateEmployeeData();
        LOGGER.info("{} End execute get create employee data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
