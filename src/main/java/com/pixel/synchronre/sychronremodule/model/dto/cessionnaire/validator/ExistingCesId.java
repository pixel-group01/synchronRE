package com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCesId.ExistingCesIdValidator.class})
@Documented
public @interface ExistingCesId
{
    String message() default "Identitifiant du cessionnaire introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCesIdValidator implements ConstraintValidator<ExistingCesId, Long>
    {
        private final CessionnaireRepository cesRepo;
        @Override
        public boolean isValid(Long cesId, ConstraintValidatorContext context)
        {
            return cesRepo.existsById(cesId);
        }
    }
}
