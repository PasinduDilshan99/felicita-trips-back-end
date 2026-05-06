package com.felicita.validation.impl;

import com.felicita.model.request.employee.EmployeeCreateRequest;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.EmployeeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeValidationServiceImpl implements EmployeeValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public EmployeeValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateEmployeeCreateRequest(EmployeeCreateRequest employeeCreateRequest) {

    }
}
