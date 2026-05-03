package com.felicita.controller;

import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.service.PrivilegeService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/privileges")
public class PrivilegeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeController.class);

    private final PrivilegeService privilegeService;

    @Autowired
    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @PostMapping(path = "/all-privileges")
    public ResponseEntity<CommonResponse<PrivilageParamResponse>> getAllPrivileges(@RequestBody PrivilegeDataParamRequest privilegeDataParamRequest) {
        LOGGER.info("{} Start execute get all privileges {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PrivilageParamResponse> response = privilegeService.getAllPrivileges(privilegeDataParamRequest);
        LOGGER.info("{} End execute get all privileges {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/privileges-names-and-ids")
    public ResponseEntity<CommonResponse<List<PrivilegeNameAndIdResponse>>> getAllPrivilegesNamesAndIds() {
        LOGGER.info("{} Start execute get privilege names and ids {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PrivilegeNameAndIdResponse>> response = privilegeService.getAllPrivilegesNamesAndIds();
        LOGGER.info("{} End execute get privilege names and ids {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/privilege-details-by-id")
    public ResponseEntity<CommonResponse<PrivilegeDetailsResponse>> getPrivilegeDetailsById(@RequestBody PrivilegeDetailsRequest privilegeDetailsRequest) {
        LOGGER.info("{} Start execute get privilege details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PrivilegeDetailsResponse> response = privilegeService.getPrivilegeDetailsById(privilegeDetailsRequest);
        LOGGER.info("{} End execute get privilege details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/privilege-basic-details-by-id")
    public ResponseEntity<CommonResponse<PrivilegeResponse>> getPrivilegeBasicDetailsById(@RequestBody PrivilegeDetailsRequest privilegeDetailsRequest) {
        LOGGER.info("{} Start execute get privilege basic details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PrivilegeResponse> response = privilegeService.getPrivilegeBasicDetailsById(privilegeDetailsRequest);
        LOGGER.info("{} End execute get privilege basic details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create-privilege")
    public ResponseEntity<CommonResponse<InsertResponse>> createPrivilege(@RequestBody PrivilegeInsertRequest privilegeInsertRequest) {
        LOGGER.info("{} Start execute create privilege {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = privilegeService.createPrivilege(privilegeInsertRequest);
        LOGGER.info("{} End execute create privilege {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-privilege")
    public ResponseEntity<CommonResponse<UpdateResponse>> updatePrivilege(@RequestBody PrivilegeUpdateRequest privilegeUpdateRequest) {
        LOGGER.info("{} Start execute update privilege {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = privilegeService.updatePrivilege(privilegeUpdateRequest);
        LOGGER.info("{} End execute update privilege {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-privilege")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminatePrivilege(@RequestBody PrivilegeTerminateRequest privilegeTerminateRequest) {
        LOGGER.info("{} Start execute terminate privilege {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = privilegeService.terminatePrivilege(privilegeTerminateRequest);
        LOGGER.info("{} End execute terminate privilege {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/privileges-statistics")
    public ResponseEntity<CommonResponse<PrivilegeStatisticsResponse>> getPrivilegeStatistics() {
        LOGGER.info("{} Start execute get privilege statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PrivilegeStatisticsResponse> response = privilegeService.getPrivilegeStatistics();
        LOGGER.info("{} End execute get privilege statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
