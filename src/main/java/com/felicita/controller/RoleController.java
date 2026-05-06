package com.felicita.controller;

import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.service.RoleService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/roles")
public class RoleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(path = "/all-roles")
    public ResponseEntity<CommonResponse<RoleParamResponse>> getAllRoles(@RequestBody RoleDataParamRequest roleDataParamRequest) {
        LOGGER.info("{} Start execute get all roles {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<RoleParamResponse> response = roleService.getAllRoles(roleDataParamRequest);
        LOGGER.info("{} End execute get all roles {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/role-names-and-ids")
    public ResponseEntity<CommonResponse<List<RoleNameAndIdResponse>>> getAllRoleNamesAndIds() {
        LOGGER.info("{} Start execute get role names and ids {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<RoleNameAndIdResponse>> response = roleService.getAllRoleNamesAndIds();
        LOGGER.info("{} End execute get role names and ids {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/role-details-by-id")
    public ResponseEntity<CommonResponse<RoleDetailsResponse>> getRoleDetailsById(@RequestBody RoleDetailsRequest roleDetailsRequest) {
        LOGGER.info("{} Start execute get role details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<RoleDetailsResponse> response = roleService.getRoleDetailsById(roleDetailsRequest);
        LOGGER.info("{} End execute get role details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/role-basic-details-by-id")
    public ResponseEntity<CommonResponse<RoleResponse>> getRoleBasicDetailsById(@RequestBody RoleDetailsRequest roleDetailsRequest) {
        LOGGER.info("{} Start execute get role basic details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<RoleResponse> response = roleService.getRoleBasicDetailsById(roleDetailsRequest);
        LOGGER.info("{} End execute get role basic details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create-role")
    public ResponseEntity<CommonResponse<InsertResponse>> createRole(@RequestBody RoleInsertRequest roleInsertRequest) {
        LOGGER.info("{} Start execute create role {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = roleService.createRole(roleInsertRequest);
        LOGGER.info("{} End execute create role {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-role")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        LOGGER.info("{} Start execute update role {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = roleService.updateRole(roleUpdateRequest);
        LOGGER.info("{} End execute update role {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-role")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateRole(@RequestBody RoleTerminateRequest roleTerminateRequest) {
        LOGGER.info("{} Start execute terminate role {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = roleService.terminateRole(roleTerminateRequest);
        LOGGER.info("{} End execute terminate role {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/roles-statistics")
    public ResponseEntity<CommonResponse<RoleStatisticsResponse>> getRoleStatistics() {
        LOGGER.info("{} Start execute get role statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<RoleStatisticsResponse> response = roleService.getRoleStatistics();
        LOGGER.info("{} End execute get role statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
