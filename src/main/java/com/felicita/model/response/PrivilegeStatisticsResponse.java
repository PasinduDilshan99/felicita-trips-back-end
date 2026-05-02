package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivilegeStatisticsResponse {

    private PrivilegeDetails privilegeDetails;
    private List<Recent> recentlyUpdates;
    private List<Recent> recentlyCreate;
    private List<Recent> recentlyTerminate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PrivilegeDetails{
        private Integer totalCount;
        private Integer activeCount;
        private Integer inActiveCount;
        private Integer hiddenCount;
        private Integer recentlyUpdateCount;
        private Integer recentlyAddedCount;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Recent{
        private String username;
        private Integer userId;
        private Integer count;
    }
}
