package com.felicita.model.request.activity.category;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ActivityCategoryImageUpdateRequest extends ActivityCategoryImageRequest{
    private Long imageId;
}
