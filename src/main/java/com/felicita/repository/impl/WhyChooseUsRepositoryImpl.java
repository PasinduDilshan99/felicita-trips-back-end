package com.felicita.repository.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.response.WhyChooseUsResponse;
import com.felicita.queries.WhyChooseUsQueries;
import com.felicita.repository.WhyChooseUsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class WhyChooseUsRepositoryImpl implements WhyChooseUsRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhyChooseUsRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WhyChooseUsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WhyChooseUsResponse> getAllWhyChooseUsData() {
        String GET_ALL_WHY_CHOOSE_US = WhyChooseUsQueries.GET_ALL_WHY_CHOOSE_US_DATA;
        try {
            LOGGER.info("Executing query to fetch all why choose us data");

            List<WhyChooseUsResponse> results = jdbcTemplate.query(GET_ALL_WHY_CHOOSE_US, (rs, rowNum) -> {
                WhyChooseUsResponse card = new WhyChooseUsResponse();

                card.setCardId(rs.getInt("CARD_ID"));
                card.setCardName(rs.getString("CARD_NAME"));
                card.setCardTitle(rs.getString("CARD_TITLE"));
                card.setCardSubTitle(rs.getString("CARD_SUB_TITLE"));
                card.setCardDescription(rs.getString("CARD_DESCRIPTION"));
                card.setCardImageUrl(rs.getString("CARD_IMAGE_URL"));
                card.setCardIconUrl(rs.getString("CARD_ICON_URL"));
                card.setCardClickedUrl(rs.getString("CARD_CLICKED_URL"));
                card.setCardStatus(rs.getString("CARD_STATUS"));
                card.setCardColor(rs.getString("CARD_COLOR"));
                card.setCardOrder(rs.getInt("CARD_ORDER"));
                card.setCardCreatedAt(rs.getTimestamp("CARD_CREATED_AT") != null ? rs.getTimestamp("CARD_CREATED_AT").toLocalDateTime() : null);
                card.setCardCreatedBy(rs.getInt("CARD_CREATED_BY"));
                card.setCardUpdatedAt(rs.getTimestamp("CARD_UPDATED_AT") != null ? rs.getTimestamp("CARD_UPDATED_AT").toLocalDateTime() : null);
                card.setCardUpdatedBy(rs.getInt("CARD_UPDATED_BY"));
                card.setCardTerminatedAt(rs.getTimestamp("CARD_TERMINATED_AT") != null ? rs.getTimestamp("CARD_TERMINATED_AT").toLocalDateTime() : null);
                card.setCardTerminatedBy(rs.getInt("CARD_TERMINATED_BY"));

                return card;
            });

            LOGGER.info("Successfully fetched {} why choose us data.", results.size());
            return results;

        } catch (DataAccessException ex) {
            LOGGER.error("Database error while fetching why choose us data: {}", ex.getMessage(), ex);
            throw new DataAccessErrorExceptionHandler("Failed to fetch why choose us data from database");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while fetching why choose us data: {}", ex.getMessage(), ex);
            throw new InternalServerErrorExceptionHandler("Unexpected error occurred while fetching why choose us data");
        }
    }

}
