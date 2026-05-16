package com.felicita.model.request.tour.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TourTypeImageInsertRequest {
    private String name;
    private String description;
    private String imageUrl;
    private String status;
}
