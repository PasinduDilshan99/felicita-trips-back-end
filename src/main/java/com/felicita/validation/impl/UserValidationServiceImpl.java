package com.felicita.validation.impl;

import com.felicita.validation.CommonValidationService;
import com.felicita.validation.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidationServiceImpl implements UserValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public UserValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }
}
