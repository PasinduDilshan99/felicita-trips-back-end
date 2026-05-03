package com.felicita.controller;

import com.felicita.model.request.BasicUserDetailsRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.PrivilegeNameAndIdResponse;
import com.felicita.model.response.UserBasicDetailsResponse;
import com.felicita.model.response.UsernameAndIdWithoutEmployeesResponse;
import com.felicita.service.UserService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user-names-and-ids-without-employees")
    public ResponseEntity<CommonResponse<List<UsernameAndIdWithoutEmployeesResponse>>> getUsernamesAndIdsWithoutEmployees() {
        LOGGER.info("{} Start execute get usernames and ids without employees {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<UsernameAndIdWithoutEmployeesResponse>> response = userService.getUsernamesAndIdsWithoutEmployees();
        LOGGER.info("{} End execute get usernames and ids without employees {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/user-basic-details-by-user-id")
    public ResponseEntity<CommonResponse<UserBasicDetailsResponse>> getUserDetailsByUserId(@RequestBody BasicUserDetailsRequest basicUserDetailsRequest) {
        LOGGER.info("{} Start execute get user basic details by user id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UserBasicDetailsResponse> response = userService.getUserDetailsByUserId(basicUserDetailsRequest);
        LOGGER.info("{} End execute get user basic details by user id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
