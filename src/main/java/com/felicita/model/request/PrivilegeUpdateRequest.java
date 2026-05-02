package com.felicita.model.request;

import lombok.Data;

@Data
public class PrivilegeUpdateRequest {
    private Long id;
    private String name;
    private String status;
    private String description;
}
