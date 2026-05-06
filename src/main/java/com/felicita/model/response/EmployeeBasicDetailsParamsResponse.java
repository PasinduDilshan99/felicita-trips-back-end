package com.felicita.model.response;

import com.felicita.model.dto.FilterItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeBasicDetailsParamsResponse {

    private List<FilterItem> employeeTypes;
    private List<FilterItem> departments;
    private List<FilterItem> employmentTypes;
    private List<FilterItem> workLocations;
    private List<FilterItem> employeeGrades;
    private List<FilterItem> supervisors;
    private List<FilterItem> reportingManagers;
    private List<FilterItem> statuses;

}
