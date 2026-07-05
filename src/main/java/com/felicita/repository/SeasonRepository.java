package com.felicita.repository;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.seasons.SeasonImageInsertRequest;
import com.felicita.model.request.seasons.SeasonImageUpdateRequest;
import com.felicita.model.request.seasons.SeasonInsertRequest;
import com.felicita.model.request.seasons.SeasonUpdateRequest;
import com.felicita.model.response.SeasonBasicResponse;
import com.felicita.model.response.SeasonDetailsResponse;
import com.felicita.model.response.TourTerminateRequest;
import com.felicita.model.response.seasons.SeasonAllDetailsResponse;
import com.felicita.model.response.statistics.SeasonStatisticsResponse;

import java.util.List;

public interface SeasonRepository {
    List<SeasonDetailsResponse> getSeasonDetailsBySeasonId(String seasonId);

    List<SeasonBasicResponse> getActiveSeasonDetails();

    List<SeasonStatisticsResponse.SeasonActivityCount> getSeasonActivityCount();

    List<SeasonStatisticsResponse.SeasonTourCount> getSeasonTourCount();

    List<SeasonStatisticsResponse.SeasonPopularity> getSeasonPopularity();

    List<SeasonStatisticsResponse.PeakSeasonDistribution> getPeakSeasonDistribution();

    List<SeasonStatisticsResponse.SeasonWeatherOverview> getSeasonWeatherOverview();

    SeasonStatisticsResponse.Summary getSeasonSummary();

    SeasonAllDetailsResponse getSeasonAllDetailsById(CommonIdRequest commonIdRequest);

    void terminateSeason(CommonIdRequest seasonTerminateIdRequest);

    void terminateSeasonImages(CommonIdRequest seasonTerminateIdRequest);

    Long insertSeasonBasicDetails(SeasonInsertRequest seasonInsertRequest, Long userId);

    void insertSeasonImages(Long seasonId, List<SeasonImageInsertRequest> imageInsertRequests, Long userId);

    void updateActivitiesSeasonIds(Long seasonId, List<Long> insertActivitiesIds, Long userId);

    void updateToursSeasonIds(Long seasonId, List<Long> insertTourIds, Long userId);

    void updateSeasonBasicDetails(SeasonUpdateRequest seasonUpdateRequest);

    void removeSeasonImages(Long id, List<Long> imageRemoveRequests, Long userId);

    void updateSeasonImages(Long id, List<SeasonImageUpdateRequest> imageUpdateRequests, Long userId);

    void removeActivitiesSeasonIds(Long id, List<Long> removeActivitiesIds, Long userId);

    void removeToursSeasonIds(Long id, List<Long> removeTourIds, Long userId);
}
