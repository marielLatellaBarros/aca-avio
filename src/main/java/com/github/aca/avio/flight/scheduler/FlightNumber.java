package com.github.aca.avio.flight.scheduler;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FlightNumberValidator.class)
public @interface FlightNumber {
    String message() default "Flight number does not satisfy format";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
