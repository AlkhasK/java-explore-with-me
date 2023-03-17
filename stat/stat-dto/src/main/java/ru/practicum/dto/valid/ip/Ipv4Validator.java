package ru.practicum.dto.valid.ip;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Ipv4Validator implements ConstraintValidator<Ipv4, String> {

    @Override
    public void initialize(Ipv4 ipv4) {
    }

    @Override
    public boolean isValid(String ip, ConstraintValidatorContext cxt) {
        return ip.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
    }
}
