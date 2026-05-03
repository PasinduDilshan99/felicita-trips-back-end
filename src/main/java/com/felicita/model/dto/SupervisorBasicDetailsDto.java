package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupervisorBasicDetailsDto {
    private Long userId;
    private String username;
    private String email;
}
