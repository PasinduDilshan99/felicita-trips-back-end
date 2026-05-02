package com.felicita.model.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivilegeUpdateComparisonResult {

    private Long privilegeId;

    private boolean nameChanged;
    private String oldName;
    private String newName;

    private boolean descriptionChanged;
    private String oldDescription;
    private String newDescription;

    private boolean statusChanged;
    private String oldStatus;
    private String newStatus;

    private boolean hasChanges;
}