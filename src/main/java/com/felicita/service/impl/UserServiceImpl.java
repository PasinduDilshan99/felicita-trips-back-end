package com.felicita.service.impl;

import com.felicita.email.UserEmailHelperService;
import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.request.BasicUserDetailsRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.PrivilegeNameAndIdResponse;
import com.felicita.model.response.UserBasicDetailsResponse;
import com.felicita.model.response.UsernameAndIdWithoutEmployeesResponse;
import com.felicita.repository.UserRepository;
import com.felicita.service.CommonService;
import com.felicita.service.EmailService;
import com.felicita.service.UserService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final CommonService commonService;
    private final UserValidationService userValidationService;
    private final UserEmailHelperService userEmailHelperService;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CommonService commonService, UserValidationService userValidationService, UserEmailHelperService userEmailHelperService, EmailService emailService) {
        this.userRepository = userRepository;
        this.commonService = commonService;
        this.userValidationService = userValidationService;
        this.userEmailHelperService = userEmailHelperService;
        this.emailService = emailService;
    }

    @Override
    public CommonResponse<List<UsernameAndIdWithoutEmployeesResponse>> getUsernamesAndIdsWithoutEmployees() {
        LOGGER.info("Start fetching usernames and ids from repository");
        try {
            List<UsernameAndIdWithoutEmployeesResponse> usernameAndIdWithoutEmployeesResponses = userRepository.getUsernamesAndIdsWithoutEmployees();
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    usernameAndIdWithoutEmployeesResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching usernames and ids: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch usernames and ids from database");
        } finally {
            LOGGER.info("End fetching usernames and ids from repository");
        }
    }

    @Override
    public CommonResponse<UserBasicDetailsResponse> getUserDetailsByUserId(BasicUserDetailsRequest basicUserDetailsRequest) {
        LOGGER.info("Start fetching get user details by user id from repository");
        try {
            UserBasicDetailsResponse userBasicDetailsResponse = userRepository.getUserDetailsByUserId(basicUserDetailsRequest);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    userBasicDetailsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching get user details by user id : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetching get user details by user id from database");
        } finally {
            LOGGER.info("End fetching get user details by user id from repository");
        }
    }
}
