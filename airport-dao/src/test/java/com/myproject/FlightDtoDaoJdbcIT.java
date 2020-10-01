package com.myproject;


import com.myproject.dao.FlightDtoDao;
import com.myproject.model.dto.FlightDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml", "classpath:dao.xml"})
public class FlightDtoDaoJdbcIT {

    @Autowired
    FlightDtoDao flightDtoDao;

    @Test
    public void shouldFindAllWithQuantityPassengers() {
        List<FlightDto> flights = flightDtoDao.findAllWithQuantityPassengers();
        assertNotNull(flights);
        assertTrue(flights.size() > 0);
        assertTrue(flights.get(0).getQuantityPassengers().intValue() > 0);
    }

    @Test
    public void findAllWithQuantityPassengersAndDateFilter() {
        LocalDate dateFrom = LocalDate.now().minusMonths(1);
        LocalDate dateTo = LocalDate.now();
        assertTrue(dateFrom.compareTo(dateTo)<0);
        List<FlightDto> flights = flightDtoDao.findAllWithQuantityPassengersAndDateFilter(dateFrom,dateTo);
        assertNotNull(flights);
        List<FlightDto> flightList = flightDtoDao.findAllWithQuantityPassengersAndDateFilter(dateFrom,dateTo);
        //assertTrue(flightList.size() > 0);

    }

}
