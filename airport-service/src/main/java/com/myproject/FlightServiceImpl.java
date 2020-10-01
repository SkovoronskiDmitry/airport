package com.myproject;

import com.myproject.dao.FlightDao;
import com.myproject.model.Flight;
import com.myproject.serviceApi.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightServiceImpl.class);

    private final FlightDao flightDao;

    public FlightServiceImpl(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> findAll() {
        LOGGER.trace("findAll()");
        return flightDao.findAll();
    }

    @Override
    public Optional<Flight> findById(Integer flightId) {
        LOGGER.debug("findById(flightId:{})", flightId);
        return flightDao.findById(flightId);
    }

    @Override
    public Integer create(Flight flight) {
        LOGGER.debug("create(flight:{})", flight);
        return flightDao.create(flight);
    }

    @Override
    public int update(Flight flight) {
        LOGGER.debug("update(flight:{})", flight);
        return flightDao.update(flight);
    }

    @Override
    public int delete(Integer flightId) {
        LOGGER.debug("update(flightId:{})", flightId);
        return flightDao.delete(flightId);
    }
}
