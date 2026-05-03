package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleUpdateRequest {
    private Long id;
    private String name;
    private String status;
    private String description;
    private List<Long> addPrivilegesIds;
    private List<Long> removePrivilegesIds;
    private List<UpdatePrivilege> updatePrivileges;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdatePrivilege{
        private Long roleId;
        private Long privilegeId;
        private String status;
    }
}
