package com.pixel.synchronre.sychronremodule.model.enums.validators;

import com.pixel.synchronre.sychronremodule.model.enums.PERIODICITE;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Objects;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidPeriodicite.ValidPeriodiciteValidator.class})
@Documented
public @interface ValidPeriodicite
{
    String message() default "Périodicité invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidPeriodiciteValidator implements ConstraintValidator<ValidPeriodicite, String>
    {
        @Override
        public boolean isValid(String periodicite, ConstraintValidatorContext context)
        {
            if(periodicite == null) return true;
            return Arrays.stream(PERIODICITE.values()).anyMatch(p-> Objects.equals(p.name().toUpperCase(), periodicite.toUpperCase()));
        }
    }
}