package com.myproject.web_app.validators;

import com.myproject.model.Flight;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.myproject.constants.FlightConstants.DIRECTION_MAX_SIZE;

@Component
public class FlightValidator implements Validator  {

    @Override
    public boolean supports(Class<?> clazz) {
        return Flight.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "direction", "direction.empty");
        Flight flight = (Flight) target;

        if (StringUtils.hasLength(flight.getDirection())
                && flight.getDirection().length() > DIRECTION_MAX_SIZE) {
            errors.rejectValue("direction", "direction.maxSize");
        }
    }
}
