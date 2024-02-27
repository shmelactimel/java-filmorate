package ru.yandex.practicum.filmorate.utils;

import javax.validation.Constraint;
import javax.validation.constraints.Past;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinDateValidator.class)
@Past
public @interface MinDate {
    String message() default "Дата выхода фильма не может быть раньше {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}
