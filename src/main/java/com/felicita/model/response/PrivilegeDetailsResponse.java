package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PrivilegeDetailsResponse {
    private Long privilegeId;
    private String privilegeName;
    private String privilegeDescription;
    private String privilegeStatus;
    private List<Role> roles;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class Role{
        private Long roleId;
        private String roleName;
        private String roleDescription;
        private String roleStatus;
    }
}
