package com.felicita.validation.impl;

import com.felicita.model.request.PrivilegeInsertRequest;
import com.felicita.model.request.PrivilegeTerminateRequest;
import com.felicita.model.request.PrivilegeUpdateRequest;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.PrivilegeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeValidationServiceImpl implements PrivilegeValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public PrivilegeValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validatePrivilegeInsertRequest(PrivilegeInsertRequest privilegeInsertRequest) {

    }

    @Override
    public void validatePrivilegeUpdateRequest(PrivilegeUpdateRequest privilegeUpdateRequest) {

    }

    @Override
    public void validatePrivilegeTerminateRequest(PrivilegeTerminateRequest privilegeTerminateRequest) {

    }
}
