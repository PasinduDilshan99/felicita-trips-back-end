package com.felicita.model.request.packages.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PackageTypeImageUpdateRequest {
    private Long imageId;
    private String name;
    private String description;
    private String imageUrl;
    private String status;
}
