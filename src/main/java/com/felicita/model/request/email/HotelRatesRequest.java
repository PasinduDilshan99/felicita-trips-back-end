package com.felicita.model.request.email;

import lombok.Data;

import java.util.List;

@Data
public class HotelRatesRequest {
    private List<String> to;
    private List<String> cc;
    private String subject;
}
