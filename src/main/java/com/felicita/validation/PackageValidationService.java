package com.felicita.validation;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.PackageInsertRequest;
import com.felicita.model.request.PackageTerminateRequest;
import com.felicita.model.request.PackageUpdateRequest;
import com.felicita.model.request.packages.schedule.PackageScheduleInsertRequest;
import com.felicita.model.request.packages.schedule.PackageScheduleUpdateRequest;
import com.felicita.model.request.packages.type.PackageTypeInsertRequest;
import com.felicita.model.request.packages.type.PackageTypeUpdateRequest;

public interface PackageValidationService {
    void validateTerminatePackageRequest(PackageTerminateRequest packageTerminateRequest);

    void validatePackageInsertRequest(PackageInsertRequest packageInsertRequest);

    void validatePackageUpdateRequest(PackageUpdateRequest packageUpdateRequest);

    void validateCommonIdRequest(CommonIdRequest commonIdRequest);

    void validatePackageTypeInsertRequest(PackageTypeInsertRequest packageTypeInsertRequest);

    void validatePackageTypeUpdateRequest(PackageTypeUpdateRequest packageTypeUpdateRequest);

    void validatePackageScheduleDataRequest(PackageValidationService packageValidationService);

    void validatePackageScheduleInsertRequest(PackageScheduleInsertRequest packageScheduleInsertRequest);

    void validatePackageScheduleUpdateRequest(PackageScheduleUpdateRequest packageScheduleUpdateRequest);
}
