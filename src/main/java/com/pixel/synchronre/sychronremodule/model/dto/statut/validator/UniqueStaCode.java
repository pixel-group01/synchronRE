package com.pixel.synchronre.sychronremodule.model.dto.statut.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.UpdateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.UpdateStatutReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueStaCode.UniqueStaCodeValidatorOnCreate.class, UniqueStaCode.UniqueStaCodeValidatorOnUpdate.class})
@Documented
public @interface UniqueStaCode
{
    String message() default "Code statut déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueStaCodeValidatorOnCreate implements ConstraintValidator<UniqueStaCode, String>
    {
        private final StatutRepository statRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !statRepo.alreadyExistsByCode(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueStaCodeValidatorOnUpdate implements ConstraintValidator<UniqueStaCode, UpdateStatutReq>
    {
        private final StatutRepository statRepo;
        @Override
        public boolean isValid(UpdateStatutReq dto, ConstraintValidatorContext context) {
            return !statRepo.alreadyExistsByCode(dto.getStaCode());
        }
    }
}
