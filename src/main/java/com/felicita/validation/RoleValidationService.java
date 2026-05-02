package com.felicita.validation;

import com.felicita.model.request.RoleInsertRequest;
import com.felicita.model.request.RoleTerminateRequest;
import com.felicita.model.request.RoleUpdateRequest;

public interface RoleValidationService {
    void validateRoleInsertRequest(RoleInsertRequest roleInsertRequest);

    void validateRoleUpdateRequest(RoleUpdateRequest roleUpdateRequest);

    void validateRoleTerminateRequest(RoleTerminateRequest roleTerminateRequest);
}
