package com.felicita.repository.impl;

import com.felicita.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StatusRepositoryImpl implements StatusRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatusRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long getStatusIdByName(String statusName) {
        try {

            String sql = """
                    SELECT id
                    FROM common_status
                    WHERE name = ?
                    LIMIT 1
                    """;

            return jdbcTemplate.queryForObject(sql, Long.class, statusName);

        } catch (Exception e) {

            LOGGER.error("Error fetching status id for name: {}", statusName, e);

            throw new RuntimeException("Failed to fetch status id for: " + statusName);
        }
    }

}
