package ru.practicum.dto.hit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class HitCreateDtoValidationTest {

    private Validator validator;

    private HitCreateDto hitCreateDto;

    @BeforeEach
    public void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    public void setUpHitCreateDto() {
        String app = "some app";
        LocalDateTime timestamp = LocalDateTime.now().minusMinutes(1);
        String uri = "/events/1";

        hitCreateDto = new HitCreateDto();
        hitCreateDto.setApp(app);
        hitCreateDto.setTimestamp(timestamp);
        hitCreateDto.setUri(uri);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.1.1.1", "255.255.255.255"})
    public void createHitWhenValidIp_ThenNoValidationException(String validIp) {
        int expectedValidationErrorCount = 0;
        hitCreateDto.setIp(validIp);

        Set<ConstraintViolation<HitCreateDto>> violations = validator.validate(hitCreateDto);
        assertThat(violations).hasSize(expectedValidationErrorCount);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.1.1.1.1", "255.255.255.256"})
    public void createHitWhenInvalidIp_ThenValidationException(String validIp) {
        int expectedValidationErrorCount = 1;
        hitCreateDto.setIp(validIp);

        Set<ConstraintViolation<HitCreateDto>> violations = validator.validate(hitCreateDto);
        assertThat(violations).hasSize(expectedValidationErrorCount);
    }
}
