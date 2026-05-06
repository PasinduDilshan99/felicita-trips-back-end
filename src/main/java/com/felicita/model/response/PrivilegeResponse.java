package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivilegeResponse {
    private Long privilegeId;
    private String privilegeName;
    private String privilegeDescription;
    private String privilegeStatus;
}
