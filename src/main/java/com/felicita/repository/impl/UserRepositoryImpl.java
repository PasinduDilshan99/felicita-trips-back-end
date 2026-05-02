package com.felicita.repository.impl;

import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.dto.UserBasicDetailsDto;
import com.felicita.model.request.BasicUserDetailsRequest;
import com.felicita.model.response.UserBasicDetailsResponse;
import com.felicita.model.response.UsernameAndIdWithoutEmployeesResponse;
import com.felicita.repository.StatusRepository;
import com.felicita.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<UsernameAndIdWithoutEmployeesResponse> getUsernamesAndIdsWithoutEmployees() {

        try {

            String query = """
                SELECT
                    u.user_id AS userId,
                    u.username AS username
                FROM user u
                LEFT JOIN employees e
                    ON u.user_id = e.user_id
                WHERE e.user_id IS NULL
                ORDER BY u.username ASC
                """;

            List<UsernameAndIdWithoutEmployeesResponse> response =
                    jdbcTemplate.query(
                            query,
                            (rs, rowNum) -> UsernameAndIdWithoutEmployeesResponse.builder()
                                    .userId(rs.getLong("userId"))
                                    .username(rs.getString("username"))
                                    .build()
                    );

            if (response.isEmpty()) {
                throw new DataNotFoundErrorExceptionHandler(
                        "No users found without employees"
                );
            }

            return response;

        } catch (DataNotFoundErrorExceptionHandler ex) {
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Error while fetching users without employees", ex);

            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch users without employees"
            );
        }
    }

    @Override
    public UserBasicDetailsDto getUserBasicDetailsForEmployeeCreate(Long userId) {

        try {

            String query = """
                SELECT
                    u.user_id AS userId,
                    u.username AS userName,
                    u.email AS email,
                    u.first_name AS firstName,
                    u.last_name AS lastName,
                    u.image_url AS profilePicture,
                    u.nic AS nic,
                    u.mobile_number1 AS mobileNumber
                FROM user u
                WHERE u.user_id = ?
                """;

            return jdbcTemplate.queryForObject(
                    query,
                    new Object[]{userId},
                    (rs, rowNum) -> new UserBasicDetailsDto(
                            rs.getLong("userId"),
                            rs.getString("userName"),
                            rs.getString("email"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("profilePicture"),
                            rs.getString("nic"),
                            rs.getString("mobileNumber")
                    )
            );

        } catch (EmptyResultDataAccessException ex) {

            throw new DataNotFoundErrorExceptionHandler(
                    "User not found for user id : " + userId
            );

        } catch (Exception ex) {

            LOGGER.error("Error while fetching user basic details", ex);

            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch user basic details"
            );
        }
    }


    @Override
    public UserBasicDetailsResponse getUserDetailsByUserId(BasicUserDetailsRequest request) {

        LOGGER.info("Fetching user details for userId: {}", request.getUserId());

        try {

            String sql = """
            SELECT 
                u.user_id,
                u.username,
                u.first_name,
                u.middle_name,
                u.last_name,
                u.email,
                u.email2,
                u.mobile_number1,
                u.mobile_number2,
                u.nic,
                u.passport_number,
                u.driving_license_number,
                g.name AS gender,
                c.name AS nationality,
                u.date_of_birth,
                u.image_url,
                ut.name AS user_type,

                a.number AS address_number,
                a.address_line1,
                a.address_line2,
                a.city,
                a.district,
                a.postal_code,
                ac.name AS address_country,

                u.created_at,
                u.updated_at,

                us.name AS user_status

            FROM user u

            LEFT JOIN gender g
                ON u.gender_id = g.gender_id

            LEFT JOIN country c
                ON u.region_id = c.country_id

            LEFT JOIN user_type ut
                ON u.user_type_id = ut.user_type_id

            LEFT JOIN address a
                ON u.address_id = a.address_id

            LEFT JOIN country ac
                ON a.country_id = ac.country_id

            LEFT JOIN user_status us
                ON u.user_status_id = us.user_status_id

            WHERE u.user_id = ?
        """;

            UserBasicDetailsResponse response = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{request.getUserId()},
                    (rs, rowNum) -> UserBasicDetailsResponse.builder()
                            .userId(rs.getLong("user_id"))
                            .username(rs.getString("username"))
                            .firstName(rs.getString("first_name"))
                            .middleName(rs.getString("middle_name"))
                            .lastName(rs.getString("last_name"))
                            .email(rs.getString("email"))
                            .email2(rs.getString("email2"))
                            .mobileNumber1(rs.getString("mobile_number1"))
                            .mobileNumber2(rs.getString("mobile_number2"))
                            .nic(rs.getString("nic"))
                            .passportNumber(rs.getString("passport_number"))
                            .drivingLicenseNumber(rs.getString("driving_license_number"))

                            .gender(rs.getString("gender"))
                            .nationality(rs.getString("nationality"))

                            .dateOfBirth(
                                    rs.getDate("date_of_birth") != null
                                            ? rs.getDate("date_of_birth").toLocalDate()
                                            : null
                            )

                            .imageUrl(rs.getString("image_url"))

                            .userType(rs.getString("user_type"))

                            .addressNumber(rs.getString("address_number"))
                            .addressLane1(rs.getString("address_line1"))
                            .addressLane2(rs.getString("address_line2"))
                            .addressCity(rs.getString("city"))
                            .addressDistrict(rs.getString("district"))
                            .addressPostalCode(rs.getString("postal_code"))
                            .addressCountry(rs.getString("address_country"))

                            .createdAt(
                                    rs.getTimestamp("created_at") != null
                                            ? rs.getTimestamp("created_at").toLocalDateTime()
                                            : null
                            )

                            .updatedAt(
                                    rs.getTimestamp("updated_at") != null
                                            ? rs.getTimestamp("updated_at").toLocalDateTime()
                                            : null
                            )

                            .userStatus(rs.getString("user_status"))

                            .build()
            );

            LOGGER.info("Successfully fetched user details for userId: {}", request.getUserId());

            return response;

        } catch (Exception e) {

            LOGGER.error(
                    "Error while fetching user details for userId: {}",
                    request.getUserId(),
                    e
            );

            throw new RuntimeException(
                    "Failed to fetch user details for userId: " + request.getUserId(),
                    e
            );
        }
    }

}
