package com.pixel.synchronre.sychronremodule.model.dto.facultative.validator;

import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingfacId.ExistingCesIdValidator.class})
@Documented
public @interface ExistingfacId
{
    String message() default "Identitifiant du statut introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCesIdValidator implements ConstraintValidator<ExistingfacId, Long>
    {
        private final StatutRepository statRepo;
        @Override
        public boolean isValid(Long staId, ConstraintValidatorContext context)
        {
            if (staId == null) return true;
            return statRepo.existsById(staId);
        }
    }
}
