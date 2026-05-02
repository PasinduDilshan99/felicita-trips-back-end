package com.felicita.model.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleUpdateComparisonResult {

    private Long roleId;

    private boolean nameChanged;
    private String oldName;
    private String newName;

    private boolean descriptionChanged;
    private String oldDescription;
    private String newDescription;

    private boolean statusChanged;
    private String oldStatus;
    private String newStatus;

    private boolean privilegesAdded;
    private List<Long> addedPrivilegeIds;

    private boolean privilegesRemoved;
    private List<Long> removedPrivilegeIds;

    private boolean hasChanges;
}