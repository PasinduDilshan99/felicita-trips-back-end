package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PackageImageInsertRequest {
    private String name;
    private String description;
    private String status;
    private String imageUrl;
    private String color;
}
