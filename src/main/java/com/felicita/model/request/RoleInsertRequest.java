package com.felicita.model.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleInsertRequest {
    private String name;
    private String status;
    private String description;
    private List<Long> privilegesIds;
}
