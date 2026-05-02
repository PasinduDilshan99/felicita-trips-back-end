package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDetailsResponse {
    private Long roleId;
    private String roleName;
    private String roleDescription;
    private String roleStatus;
    private List<Privilege> privileges;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class Privilege{
        private Long privilegeId;
        private String privilegeName;
        private String privilegeDescription;
        private String privilegeStatus;
    }
}
