package com.felicita.model.request.whyChooseUs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WhyChooseUsInsertRequest {
    private String message;
}
