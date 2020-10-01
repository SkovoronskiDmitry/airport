package com.myproject.jdbc;

import com.myproject.dao.FlightDao;
import com.myproject.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

import static com.myproject.constants.FlightConstants.*;

public class FlightDaoJdbc implements FlightDao {

    @Value("${flight.select}")
    private String selectSql;

    @Value("${flight.create}")
    private String createSql;

    @Value("${flight.findById}")
    private String findByIdSql;

    @Value("${flight.delete}")
    private String deleteSql;

    @Value("${flight.update}")
    private String updateSql;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightDaoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FlightDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public List<Flight> findAll() {
        LOGGER.trace("findAll()");
        return namedParameterJdbcTemplate.query(selectSql,  BeanPropertyRowMapper.newInstance(Flight.class));
    }

    @Override
    public Optional<Flight> findById(Integer flightId) {
        LOGGER.debug("findById(flight_id:{})", flightId);
        SqlParameterSource namedParameters = new MapSqlParameterSource("flight_id", flightId);
        List<Flight> results = namedParameterJdbcTemplate.query(
                findByIdSql, namedParameters, BeanPropertyRowMapper.newInstance(Flight.class));
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    @Override
    public Integer create(Flight flight) {
        LOGGER.debug("create(flight:{})", flight);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(FLIGHT_ID, flight.getFlightId());
        params.addValue(DIRECTION, flight.getDirection());
        params.addValue(DATE_FLIGHT, flight.getDateFlight());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(createSql, params, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int update(Flight flight) {
        LOGGER.debug("update(flight:{})", flight);
        Map<String, Object> params = new HashMap<>();
        params.put(FLIGHT_ID, flight.getFlightId());
        params.put(DIRECTION, flight.getDirection());
        params.put(DATE_FLIGHT, flight.getDateFlight());
        return namedParameterJdbcTemplate.update(updateSql, params);
    }

    @Override
    public int delete(Integer flightId) {
        LOGGER.debug("delete(flightId:{})", flightId);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(FLIGHT_ID, flightId);
        return namedParameterJdbcTemplate.update(deleteSql, mapSqlParameterSource);
    }

}
