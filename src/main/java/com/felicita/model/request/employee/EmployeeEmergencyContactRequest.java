package com.felicita.model.request.employee;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeEmergencyContactRequest {
    private String contactName;
    private String relationship;
    private String primaryPhone;
    private String secondaryPhone;
    private String email;
    private String address;
    private Boolean isPrimary;
    private String status;
}