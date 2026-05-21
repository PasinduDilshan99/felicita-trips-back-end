package com.felicita.model.response.packages.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageTypeAllDetailsResponse {

    private Long typeId;
    private String typeName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;

    private Date createdAt;
    private Long createdBy;
    private String createdByName;

    private Date updatedAt;
    private Long updatedBy;
    private String updatedByName;

    private Date terminatedAt;
    private Long terminatedBy;

    private Integer totalTours;

    private List<PackageTypeImageResponse> images;

    private List<PackageBasicDetails> packageBasicDetails;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageBasicDetails {

        private Long packageId;
        private String packageName;
        private String description;
        private String color;
        private String status;
        private String hoverColor;
        private Date startDate;
        private Date endDate;
        private Boolean primaryType;
    }
}
