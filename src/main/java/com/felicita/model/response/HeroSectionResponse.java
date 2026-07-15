package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroSectionResponse {
    private Integer imageId;
    private String imageName;
    private String imageUrl;
    private String imageTitle;
    private String imageSubTitle;
    private String imageDescription;
    private String imagePrimaryButtonText;
    private String imagePrimaryButtonLink;
    private String imageSecondaryButtonText;
    private String imageSecondaryButtonLink;
    private String imageStatus;
    private Integer imageOrder;
    private LocalDateTime imageCreatedAt;
    private Integer imageCreatedBy;
    private LocalDateTime imageUpdatedAt;
    private Integer imageUpdatedBy;
    private LocalDateTime imageTerminatedAt;
    private Integer imageTerminatedBy;
}
