package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBasicDetailsResponse {

    private Long userId;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String email2;
    private String mobileNumber1;
    private String mobileNumber2;
    private String nic;
    private String passportNumber;
    private String drivingLicenseNumber;
    private String gender;
    private String nationality;
    private LocalDate dateOfBirth;
    private String imageUrl;
    private String userType;
    private String addressNumber;
    private String addressLane1;
    private String addressLane2;
    private String addressCity;
    private String addressDistrict;
    private String addressPostalCode;
    private String addressCountry;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userStatus;

}
