package com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.UpdateCessionnaireReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueCesTel.UniqueCesTelValidatorOnCreate.class, UniqueCesTel.UniqueCesTelValidatorOnUpdate.class})
@Documented
public @interface UniqueCesTel
{
    String message() default "Numéro de téléphone déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueCesTelValidatorOnCreate implements ConstraintValidator<UniqueCesTel, String>
    {
        private final CessionnaireRepository cesRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !cesRepo.alreadyExistsByTel(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueCesTelValidatorOnUpdate implements ConstraintValidator<UniqueCesTel, UpdateCessionnaireReq>
    {
        private final CessionnaireRepository cesRepo;
        @Override
        public boolean isValid(UpdateCessionnaireReq dto, ConstraintValidatorContext context) {
            return !cesRepo.alreadyExistsByTel(dto.getCesTelephone(), dto.getCesId());
        }
    }
}
