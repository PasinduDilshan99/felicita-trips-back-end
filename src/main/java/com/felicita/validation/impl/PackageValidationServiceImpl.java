package com.felicita.validation.impl;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.PackageInsertRequest;
import com.felicita.model.request.PackageTerminateRequest;
import com.felicita.model.request.PackageUpdateRequest;
import com.felicita.model.request.packages.schedule.PackageScheduleInsertRequest;
import com.felicita.model.request.packages.schedule.PackageScheduleUpdateRequest;
import com.felicita.model.request.packages.type.PackageTypeInsertRequest;
import com.felicita.model.request.packages.type.PackageTypeUpdateRequest;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.PackageValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageValidationServiceImpl implements PackageValidationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PackageValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public PackageValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateTerminatePackageRequest(PackageTerminateRequest packageTerminateRequest) {

    }

    @Override
    public void validatePackageInsertRequest(PackageInsertRequest packageInsertRequest) {

    }

    @Override
    public void validatePackageUpdateRequest(PackageUpdateRequest packageUpdateRequest) {

    }

    @Override
    public void validateCommonIdRequest(CommonIdRequest commonIdRequest) {

    }

    @Override
    public void validatePackageTypeInsertRequest(PackageTypeInsertRequest packageTypeInsertRequest) {

    }

    @Override
    public void validatePackageTypeUpdateRequest(PackageTypeUpdateRequest packageTypeUpdateRequest) {

    }

    @Override
    public void validatePackageScheduleDataRequest(PackageValidationService packageValidationService) {

    }

    @Override
    public void validatePackageScheduleInsertRequest(PackageScheduleInsertRequest packageScheduleInsertRequest) {

    }

    @Override
    public void validatePackageScheduleUpdateRequest(PackageScheduleUpdateRequest packageScheduleUpdateRequest) {

    }
}
