package com.pixel.synchronre.sychronremodule.model.dto.statut.validator;

import com.pixel.synchronre.authmodule.model.dtos.asignation.ExistingAssId;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
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
@Constraint(validatedBy = {ExistingStatCode.ExistingStatCodeValidator.class})
@Documented
public @interface ExistingStatCode
{
    String message() default "Code de statut introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingStatCodeValidator implements ConstraintValidator<ExistingStatCode, String>
    {
        private final StatutRepository statRepo;
        @Override
        public boolean isValid(String staCode, ConstraintValidatorContext context)
        {
            if (staCode == null) return true;
            return statRepo.existsById(staCode);
        }
    }
}
