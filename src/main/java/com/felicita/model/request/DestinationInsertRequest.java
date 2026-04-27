package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DestinationInsertRequest {
    private String name;
    private String description;
    private String status;
    private List<Long> destinationCategoriesIdList;
    private String location;
    private Double latitude;
    private Double longitude;
    private Double extraPrice;
    private String extraPriceNote;
    private List<Image> images;

    @Data
    public static class Image {
        private String name;
        private String description;
        private String imageUrl;
        private String status;
    }
}
