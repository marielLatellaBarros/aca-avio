package com.github.aca.avio.flight.scheduler;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.regex.Pattern;

public class FlightNumberValidator implements ConstraintValidator<FlightNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (StringUtils.isBlank(value)) {
            context.buildConstraintViolationWithTemplate("Flight number is required")
                    .addConstraintViolation();
            return false;
        }
        if (lengthIsInvalid(value)) {
            context.buildConstraintViolationWithTemplate("Flight number should be between 3 and 7 characters long")
                    .addConstraintViolation();
            return false;
        }

        if (iataAirlineCodeIsInvalid(value)) {
            context.buildConstraintViolationWithTemplate("IATA airline code is invalid")
                    .addConstraintViolation();
            return false;
        }

        if (doesNotHaveDigitsAfterIcaoAirlineCode(value)) {
            context.buildConstraintViolationWithTemplate("IATA airline code must be followed by 1 to 4 digits")
                    .addConstraintViolation();
            return false;
        }

        if (optionalCharacterExistsAndIsInvalid(value)) {
            context.buildConstraintViolationWithTemplate("Optional code must be a alphabetic")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private static boolean lengthIsInvalid(String value) {
        return value.length() < 3 || value.length() > 7;
    }

    private static boolean iataAirlineCodeIsInvalid(String value) {
        String firstTwoCharacters = value.substring(0, 2);
        return Arrays.stream(IataAirlineCode.values()).noneMatch(
                airlineCode -> airlineCode.name().equals(firstTwoCharacters));
    }

    private static boolean doesNotHaveDigitsAfterIcaoAirlineCode(String value) {
        Pattern digitPattern = Pattern.compile("\\d{1,4}$");
        int endOfDigits = Math.min(6, value.length());
        String endOfDigitSubstring = value.substring(2, endOfDigits);
        return !digitPattern.matcher(endOfDigitSubstring).matches();
    }

    private static boolean optionalCharacterExistsAndIsInvalid(String value) {
        return value.length() == 7 && !Character.isLetter(value.charAt(6));
    }
}
