package com.felicita.validation;

import com.felicita.model.request.employee.EmployeeCreateRequest;

public interface EmployeeValidationService {
    void validateEmployeeCreateRequest(EmployeeCreateRequest employeeCreateRequest);
}
