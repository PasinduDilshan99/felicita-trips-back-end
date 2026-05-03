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
public class RoleParamResponse {
    private List<RoleResponse> roleResponses;
    private int totalResponse;
    private int pageNumber;
}
