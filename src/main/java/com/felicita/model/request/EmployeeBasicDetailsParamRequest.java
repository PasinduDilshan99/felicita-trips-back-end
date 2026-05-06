package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeBasicDetailsParamRequest {
    private String name;
    private Long employeeTypeId;
    private String status;
    private Long departmentId;
    private String employmentType;
    private String workLocation;
    private String employeeGrade;
    private Long supervisorId;
    private Long reportingManagerId;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}
