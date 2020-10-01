package com.myproject.jdbc;

import com.myproject.dao.PassengerDao;
import com.myproject.model.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.myproject.constants.PassengerConstants.*;

public class PassengerDaoJdbc implements PassengerDao {
    @Value("${passenger.select}")
    private String selectSql;

    @Value("${passenger.create}")
    private String createSql;

    @Value("${passenger.findById}")
    private String findByIdSql;

    @Value("${passenger.delete}")
    private String deleteSql;

    @Value("${passenger.update}")
    private String updateSql;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightDaoJdbc.class);

    private final PassengerRowMapper passengerRowMapper = new PassengerRowMapper();

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PassengerDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Passenger> findAll() {
        LOGGER.trace("findAll()");
        return namedParameterJdbcTemplate.query(selectSql, passengerRowMapper);
    }

    @Override
    public Optional<Passenger> findById(Integer passengerId) {
        LOGGER.debug("findById(passenger_id:{})", passengerId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(PASSENGER_ID, passengerId);
        List<Passenger> results = namedParameterJdbcTemplate.query(findByIdSql, namedParameters, passengerRowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    @Override
    public Integer create(Passenger passenger) {
        LOGGER.debug("create(passenger:{})", passenger);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(FIRST_NAME, passenger.getFirstname());
        params.addValue(LAST_NAME, passenger.getLastname());
        params.addValue(FLIGHT_ID, passenger.getFlightId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(createSql, params, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int update(Passenger passenger) {
        LOGGER.debug("update(passenger:{})", passenger);
        Map<String, Object> params = new HashMap<>();
        params.put(FIRST_NAME, passenger.getFirstname());
        params.put(LAST_NAME, passenger.getLastname());
        params.put(FLIGHT_ID, passenger.getFlightId());
        params.put(PASSENGER_ID,passenger.getPassengerId());
        return namedParameterJdbcTemplate.update(updateSql, params);
    }

    @Override
    public int delete(Integer passengerId) {
        LOGGER.debug("delete(passenger_id:{})", passengerId);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(PASSENGER_ID, passengerId);
        return namedParameterJdbcTemplate.update(deleteSql, mapSqlParameterSource);
    }
    private static class PassengerRowMapper implements RowMapper<Passenger> {

        @Override
        public Passenger mapRow(ResultSet resultSet, int i) throws SQLException {
            Passenger passenger = new Passenger();
            passenger.setPassengerId(resultSet.getInt(PASSENGER_ID));
            passenger.setFirstname(resultSet.getString(FIRST_NAME));
            passenger.setLastname(resultSet.getString(LAST_NAME));
            passenger.setFlightId(resultSet.getInt(FLIGHT_ID));
            return passenger;
        }
    }
}
