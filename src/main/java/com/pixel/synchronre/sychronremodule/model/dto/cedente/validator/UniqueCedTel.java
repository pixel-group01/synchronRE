package com.pixel.synchronre.sychronremodule.model.dto.cedente.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.UpdateCedenteDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueCedTel.UniqueCesTelValidatorOnCreate.class, UniqueCedTel.UniqueCesTelValidatorOnUpdate.class})
@Documented
public @interface UniqueCedTel
{
    String message() default "Numéro de téléphone déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueCesTelValidatorOnCreate implements ConstraintValidator<UniqueCedTel, String>
    {
        private final CedRepo cedRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !cedRepo.alreadyExistsByTel(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueCesTelValidatorOnUpdate implements ConstraintValidator<UniqueCedTel, UpdateCedenteDTO>
    {
        private final CedRepo cedRepo;
        @Override
        public boolean isValid(UpdateCedenteDTO dto, ConstraintValidatorContext context) {
            return !cedRepo.alreadyExistsByTel(dto.getCedTel(), dto.getCedId());
        }
    }
}
