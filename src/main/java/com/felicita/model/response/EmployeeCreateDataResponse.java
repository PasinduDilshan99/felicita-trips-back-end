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
public class EmployeeCreateDataResponse {

    private List<FilterItem> employeeTypes;
    private List<FilterItem> departments;
    private List<FilterItem> designationTypes;
    private List<FilterItem> employmentTypes;
    private List<FilterItem> bankNames;
    private List<FilterItem> workLocations;
    private List<FilterItem> employeeGrades;
    private List<FilterItem> supervisors;
    private List<FilterItem> reportingManagers;
    private List<FilterItem> statuses;
    private List<FilterItem> salaryComponents;
    private List<FilterItem> shiftTypes;
    private List<FilterItem> socialMediaPlatforms;


}
