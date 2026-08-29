package com.felicita.service;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsInsertRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsTerminateRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.common.IdAndNameResponse;
import com.felicita.model.response.statistics.WhyChooseUsStatisticsResponse;
import com.felicita.model.response.whyChooseUs.WhyChooseUsDetailsResponse;

import java.util.List;

public interface WhyChooseUsService {

    CommonResponse<List<WhyChooseUsResponse>> getAllWhyChooseUsData();

    CommonResponse<List<WhyChooseUsResponse>> getActiveWhyChooseUsData();

    CommonResponse<List<IdAndNameResponse>> getWhyChooseUsDataIdsAndNames();

    CommonResponse<WhyChooseUsStatisticsResponse> getWhyChooseUsStatistics();

    CommonResponse<WhyChooseUsDetailsResponse> getWhyChooseUsDetailsById(CommonIdRequest commonIdRequest);

    CommonResponse<InsertResponse> insertWhyChooseUs(WhyChooseUsInsertRequest whyChooseUsInsertRequest);

    CommonResponse<UpdateResponse> updateWhyChooseUs(WhyChooseUsUpdateRequest whyChooseUsUpdateRequest);

    CommonResponse<TerminateResponse> terminateWhyChooseUs(WhyChooseUsTerminateRequest whyChooseUsTerminateRequest);
}
