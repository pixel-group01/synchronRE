package com.pixel.synchronre.sychronremodule.model.dto.cedante.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueCedEmail.UniqueCesEmailValidatorOnCreate.class, UniqueCedEmail.UniqueCesEmailValidatorOnUpdate.class})
@Documented
public @interface UniqueCedEmail
{
    String message() default "Adresse mail déjà attribuée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueCesEmailValidatorOnCreate implements ConstraintValidator<UniqueCedEmail, String>
    {
        private final CedRepo cedRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !cedRepo.alreadyExistsByEmail(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueCesEmailValidatorOnUpdate implements ConstraintValidator<UniqueCedEmail, UpdateCedanteDTO>
    {
        private final CedRepo cedRepo;
        @Override
        public boolean isValid(UpdateCedanteDTO dto, ConstraintValidatorContext context) {
            return !cedRepo.alreadyExistsByEmail(dto.getCedEmail(), dto.getCedId());
        }
    }
}
