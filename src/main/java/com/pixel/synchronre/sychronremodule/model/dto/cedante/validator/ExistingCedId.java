package com.pixel.synchronre.sychronremodule.model.dto.cedante.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.ExistingCouId;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCedId.ExistingCedIdValidator.class})
@Documented
public @interface ExistingCedId
{
    String message() default "Identitifiant de cedente introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCedIdValidator implements ConstraintValidator<ExistingCedId, Long>
    {
        private final CedRepo cedRepo;
        @Override
        public boolean isValid(Long cedId, ConstraintValidatorContext context)
        {
            return cedRepo.existsById(cedId);
        }
    }
}
