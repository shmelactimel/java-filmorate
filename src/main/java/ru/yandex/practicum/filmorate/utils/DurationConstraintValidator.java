package ru.yandex.practicum.filmorate.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class DurationConstraintValidator implements ConstraintValidator<DurationAnnotation, Duration> {
    @Override
    public void initialize(DurationAnnotation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext ctx) {
        return ((duration != null) && (!duration.isNegative()));
    }
}