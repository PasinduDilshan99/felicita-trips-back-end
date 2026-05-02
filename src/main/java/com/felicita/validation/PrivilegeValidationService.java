package com.felicita.validation;

import com.felicita.model.request.PrivilegeInsertRequest;
import com.felicita.model.request.PrivilegeTerminateRequest;
import com.felicita.model.request.PrivilegeUpdateRequest;

public interface PrivilegeValidationService {
    void validatePrivilegeInsertRequest(PrivilegeInsertRequest privilegeInsertRequest);

    void validatePrivilegeUpdateRequest(PrivilegeUpdateRequest privilegeUpdateRequest);

    void validatePrivilegeTerminateRequest(PrivilegeTerminateRequest privilegeTerminateRequest);
}
