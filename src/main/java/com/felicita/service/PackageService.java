package com.felicita.service;

import com.felicita.model.dto.*;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.PackageScheduleStatisticsResponse;
import com.felicita.model.response.statistics.PackageStatisticsResponse;
import com.felicita.model.response.statistics.PackageTypeStatisticsResponse;

import java.util.List;

public interface PackageService {

    CommonResponse<List<PackageResponseDto>> getAllPackages();

    CommonResponse<List<PackageResponseDto>> getActivePackages();

    CommonResponse<PackageResponseDto> getPackageDetailsById(Long packageId);

    CommonResponse<List<PackageReviewResponse>> getAllPackageReviewDetails();

    CommonResponse<List<PackageReviewResponse>> getPackageReviewDetailsById(Long packageId);

    CommonResponse<List<PackageHistoryDetailsResponse>> getAllPackageHistoryDetails();

    CommonResponse<List<PackageHistoryDetailsResponse>> getPackageHistoryDetailsById(Long packageId);

    CommonResponse<List<PackageHistoryImageResponse>> getAllPackageHistoryImages();

    CommonResponse<List<PackageHistoryImageResponse>> getPackageHistoryImagesById(Long packageId);

    CommonResponse<PackageWithParamsResponse> getPackagesWithParams(PackageDataRequest packageDataRequest);

    CommonResponse<List<PackageDayAccommodationResponse>> getDayToPackageDetailsByTourId(Long tourId);

    CommonResponse<List<PackageExtrasResponse>> getPackageExtraDetailsDayByDay(Long tourId);

    CommonResponse<List<PackageScheduleResponse>> getPackageSchedulesByTourId(Long tourId);

    CommonResponse<PackageScheduleDetailsResponse> getPackageSchedulesForId(Long packageId);

    CommonResponse<List<PackageComapreResponse>> getDayToPackageDetailsForComapreByTourId(Long tourId);

    PackageBasicDetailsDto getPackageBasicDetailsByScheduleId(Long packageScheduleId);

    List<PackageActivityPriceDto> getPackageActivityPriceByScheduleId(Long packageScheduleId);

    List<PackageDestinationExtraPriceDto> getPackageDestinationExtraPriceByScheduleId(Long packageScheduleId);

    List<PackageDayAccommodationPriceDto> getPackageDayAccommodationPriceByScheduleId(Long packageScheduleId);

    CommonResponse<List<PackageForTerminateResponse>> getPackagesForTerminate();

    CommonResponse<TerminateResponse> terminatePackage(PackageTerminateRequest packageTerminateRequest);

    CommonResponse<InsertResponse> insertPackage(PackageInsertRequest packageInsertRequest);

    CommonResponse<UpdateResponse> updatePackage(PackageUpdateRequest packageUpdateRequest);

    CommonResponse<PackageAllDetailsResponse> getPackageAllDetailsById(Long packageId);

    CommonResponse<List<PackageIdAndPackageNameResponse>> getPackageIdsAndPackageNames();

    CommonResponse<PackageStatisticsResponse> getPackageStatistics();

    CommonResponse<PackageScheduleStatisticsResponse> getPackageScheduleStatistics();

    CommonResponse<PackageTypeStatisticsResponse> getPackageTypeStatistics();

    CommonResponse<AddPackageParamResponse> getInsertPackageParams(AddPackageParamRequest addPackageParamRequest);
}
