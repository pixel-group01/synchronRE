package com.pixel.synchronre.sychronremodule.model.enums.validators;

import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
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
@Constraint(validatedBy = {ValidRattachement.ValidRatachementeValidator.class})
@Documented
public @interface ValidRattachement
{
    String message() default "Type de ratachement invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidRatachementeValidator implements ConstraintValidator<ValidRattachement, String>
    {
        @Override
        public boolean isValid(String rattachement, ConstraintValidatorContext context)
        {
            if(rattachement == null) return true;
            return Arrays.stream(EXERCICE_RATTACHEMENT.values()).anyMatch(p-> Objects.equals(p.name(), rattachement));
        }
    }
}