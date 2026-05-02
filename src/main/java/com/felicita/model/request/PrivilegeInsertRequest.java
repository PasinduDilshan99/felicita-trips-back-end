package com.felicita.model.request;

import lombok.Data;

@Data
public class PrivilegeInsertRequest {
    private String name;
    private String status;
    private String description;
}
