package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhyChooseUsResponse {
    private Integer cardId;
    private String cardName;
    private String cardTitle;
    private String cardSubTitle;
    private String cardDescription;
    private String cardImageUrl;
    private String cardIconUrl;
    private String cardClickedUrl;
    private String cardStatus;
    private String cardColor;
    private Integer cardOrder;
    private LocalDateTime cardCreatedAt;
    private Integer cardCreatedBy;
    private LocalDateTime cardUpdatedAt;
    private Integer cardUpdatedBy;
    private LocalDateTime cardTerminatedAt;
    private Integer cardTerminatedBy;
}
