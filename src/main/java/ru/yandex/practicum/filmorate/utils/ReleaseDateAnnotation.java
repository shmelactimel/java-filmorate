package ru.yandex.practicum.filmorate.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateConstraintValidator.class)
@Documented
public @interface ReleaseDateAnnotation {
    String message() default "must be date after 1895-12-28";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}