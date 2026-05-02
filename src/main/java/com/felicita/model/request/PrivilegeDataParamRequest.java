package com.felicita.model.request;

import lombok.Data;

@Data
public class PrivilegeDataParamRequest {
    private String name;
    private String status;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}
