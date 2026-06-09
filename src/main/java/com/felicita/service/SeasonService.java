package com.felicita.service;

import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.seasons.SeasonInsertRequest;
import com.felicita.model.request.seasons.SeasonUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.common.SeasonIdAndNameResponse;
import com.felicita.model.response.seasons.SeasonAllDetailsResponse;
import com.felicita.model.response.statistics.SeasonStatisticsResponse;

import java.util.List;

public interface SeasonService {
    CommonResponse<List<SeasonDetailsResponse>> getSeasonDetailsBySeasonId(String seasonId);

    CommonResponse<List<SeasonBasicResponse>> getActiveSeasonDetails();

    CommonResponse<SeasonStatisticsResponse> getSeasonsStatistics();

    CommonResponse<SeasonAllDetailsResponse> getSeasonAllDetailsById(CommonIdRequest commonIdRequest);

    CommonResponse<TerminateResponse> terminateSeason(CommonIdRequest seasonTerminateIdRequest);

    CommonResponse<InsertResponse> insertSeasons(SeasonInsertRequest seasonInsertRequest);

    CommonResponse<UpdateResponse> updateSeasons(SeasonUpdateRequest seasonUpdateRequest);

    CommonResponse<List<SeasonIdAndNameResponse>> getSeasonsIdsAndSeasonsNames();
}
