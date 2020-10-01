package com.myproject.web_app.validators;

import com.myproject.model.Passenger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.myproject.constants.PassengerConstants.*;

@Component
public class PassengerValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "firstname", "firstname.empty");
        ValidationUtils.rejectIfEmpty(errors, "lastname", "lastname.empty");
        Passenger  passenger = (Passenger) target;

        if (StringUtils.hasLength(passenger.getFirstname())
                && passenger.getFirstname().length() > FIRST_NAME_SIZE ) {
            errors.rejectValue("firstname", "firstname.maxSize");
        }
        if (StringUtils.hasLength(passenger.getLastname())
                && passenger.getLastname().length() > LAST_NAME_SIZE ) {
            errors.rejectValue("lastname", "lastname.maxSize");
        }
    }
}
