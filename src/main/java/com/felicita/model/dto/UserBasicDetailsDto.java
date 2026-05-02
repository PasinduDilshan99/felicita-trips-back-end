package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicDetailsDto {
    private Long userId;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String nic;
    private String mobileNumber;
}
