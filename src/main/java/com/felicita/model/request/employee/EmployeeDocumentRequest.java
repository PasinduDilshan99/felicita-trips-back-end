package com.felicita.model.request.employee;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDocumentRequest {

    private String documentType;
    private String documentName;
    private String filePath;
    private Integer fileSize;
    private String mimeType;

    private LocalDate expiryDate;

    private Boolean verified;
    private Long verifiedBy;
    private LocalDate verifiedDate;

    private String notes;

    private Long uploadedBy;
    private String status;
}