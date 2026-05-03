package com.felicita.validation.impl;

import com.felicita.model.request.RoleInsertRequest;
import com.felicita.model.request.RoleTerminateRequest;
import com.felicita.model.request.RoleUpdateRequest;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.RoleValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleValidationServiceImpl implements RoleValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public RoleValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateRoleInsertRequest(RoleInsertRequest roleInsertRequest) {

    }

    @Override
    public void validateRoleUpdateRequest(RoleUpdateRequest roleUpdateRequest) {

    }

    @Override
    public void validateRoleTerminateRequest(RoleTerminateRequest roleTerminateRequest) {

    }
}
