package ru.yandex.practicum.filmorate.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationConstraintValidator.class)
@Documented
public @interface DurationAnnotation {
    String message() default "must be a valid duration";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}