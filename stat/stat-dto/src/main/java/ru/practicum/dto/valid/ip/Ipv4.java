package ru.practicum.dto.valid.ip;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Ipv4Validator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ipv4 {
    String message() default "Invalid ipv4 address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
