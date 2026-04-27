package com.felicita.model.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldUpdate {
    private String fieldName;
    private Object oldValue;
    private Object newValue;
}
