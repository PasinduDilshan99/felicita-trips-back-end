package com.felicita.service.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.enums.WhyChooseUsItemStatus;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsInsertRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsTerminateRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.common.IdAndNameResponse;
import com.felicita.model.response.statistics.WhyChooseUsStatisticsResponse;
import com.felicita.model.response.whyChooseUs.WhyChooseUsDetailsResponse;
import com.felicita.repository.WhyChooseUsRepository;
import com.felicita.service.WhyChooseUsService;
import com.felicita.util.CommonResponseMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class WhyChooseUsServiceImpl implements WhyChooseUsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhyChooseUsServiceImpl.class);

    private final WhyChooseUsRepository whyChooseUsRepository;

    @Autowired
    public WhyChooseUsServiceImpl(WhyChooseUsRepository whyChooseUsRepository) {
        this.whyChooseUsRepository = whyChooseUsRepository;
    }

    @Override
    public CommonResponse<List<WhyChooseUsResponse>> getAllWhyChooseUsData() {
        LOGGER.info("Start fetching all why choose us data from repository");
        try {
            List<WhyChooseUsResponse> whyChooseUsResponses = whyChooseUsRepository.getAllWhyChooseUsData();

            if (whyChooseUsResponses.isEmpty()) {
                LOGGER.warn("No why choose us data found in database");
                throw new DataNotFoundErrorExceptionHandler("No why choose us data found");
            }

            LOGGER.info("Fetched {} why choose us data successfully", whyChooseUsResponses.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    whyChooseUsResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching why choose us data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch why choose us data from database");
        } finally {
            LOGGER.info("End fetching all why choose us data from repository");
        }
    }

    @Override
    public CommonResponse<List<WhyChooseUsResponse>> getActiveWhyChooseUsData() {
        LOGGER.info("Start fetching active why choose us data from repository");

        try {
            List<WhyChooseUsResponse> whyChooseUsResponses = getAllWhyChooseUsData().getData();

            List<WhyChooseUsResponse> whyChooseUsResponsesList = whyChooseUsResponses.stream()
                    .filter(item -> WhyChooseUsItemStatus.VISIBLE.toString().equalsIgnoreCase(item.getCardStatus()))
                    .toList();

            if (whyChooseUsResponsesList.isEmpty()) {
                LOGGER.warn("No active why choose us data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active why choose us data found");
            }

            LOGGER.info("Fetched {} active why choose us data successfully", whyChooseUsResponsesList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    whyChooseUsResponsesList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active why choose us data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch why choose us data from database");
        } finally {
            LOGGER.info("End fetching active why choose us data from repository");
        }
    }

    @Override
    public CommonResponse<List<IdAndNameResponse>> getWhyChooseUsDataIdsAndNames() {
        return null;
    }

    @Override
    public CommonResponse<WhyChooseUsStatisticsResponse> getWhyChooseUsStatistics() {
        return null;
    }

    @Override
    public CommonResponse<WhyChooseUsDetailsResponse> getWhyChooseUsDetailsById(CommonIdRequest commonIdRequest) {
        return null;
    }

    @Override
    public CommonResponse<InsertResponse> insertWhyChooseUs(WhyChooseUsInsertRequest whyChooseUsInsertRequest) {
        return null;
    }

    @Override
    public CommonResponse<UpdateResponse> updateWhyChooseUs(WhyChooseUsUpdateRequest whyChooseUsUpdateRequest) {
        return null;
    }

    @Override
    public CommonResponse<TerminateResponse> terminateWhyChooseUs(WhyChooseUsTerminateRequest whyChooseUsTerminateRequest) {
        return null;
    }

}
