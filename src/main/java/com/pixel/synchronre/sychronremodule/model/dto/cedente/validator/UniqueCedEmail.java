package com.pixel.synchronre.sychronremodule.model.dto.cedente.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.UpdateCedenteDTO;
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
    class UniqueCesEmailValidatorOnUpdate implements ConstraintValidator<UniqueCedEmail, UpdateCedenteDTO>
    {
        private final CedRepo cedRepo;
        @Override
        public boolean isValid(UpdateCedenteDTO dto, ConstraintValidatorContext context) {
            return !cedRepo.alreadyExistsByEmail(dto.getCedEmail(), dto.getCedId());
        }
    }
}
