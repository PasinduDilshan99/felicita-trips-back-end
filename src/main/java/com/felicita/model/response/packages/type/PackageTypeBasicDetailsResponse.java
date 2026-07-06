package com.felicita.model.response.packages.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageTypeBasicDetailsResponse {
    private Long typeId;
    private String typeName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;

    private List<PackageTypeImageResponse> images;
}
