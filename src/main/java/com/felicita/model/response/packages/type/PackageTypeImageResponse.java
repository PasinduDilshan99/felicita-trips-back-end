package com.felicita.model.response.packages.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageTypeImageResponse {

    private Long imageId;
    private String name;
    private String description;
    private String imageUrl;
    private String status;

}
