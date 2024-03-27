package ru.yandex.practicum.filmorate.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateConstraintValidator implements ConstraintValidator<ReleaseDateAnnotation, LocalDate> {
    @Override
    public void initialize(ReleaseDateAnnotation constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext ctx) {
        return ((date != null) && (date.isAfter(LocalDate.of(1895, 12, 28)))
                && (date.isBefore(LocalDate.now())));
    }
}