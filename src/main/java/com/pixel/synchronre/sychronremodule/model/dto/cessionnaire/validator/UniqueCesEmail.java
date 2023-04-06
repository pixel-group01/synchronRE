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
@Constraint(validatedBy = {UniqueCesEmail.UniqueCesEmailValidatorOnCreate.class, UniqueCesEmail.UniqueCesEmailValidatorOnUpdate.class})
@Documented
public @interface UniqueCesEmail
{
    String message() default "Adresse mail déjà attribuée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueCesEmailValidatorOnCreate implements ConstraintValidator<UniqueCesEmail, String>
    {
        private final CessionnaireRepository cesRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !cesRepo.alreadyExistsByEmail(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueCesEmailValidatorOnUpdate implements ConstraintValidator<UniqueCesEmail, UpdateCessionnaireReq>
    {
        private final CessionnaireRepository cesRepo;
        @Override
        public boolean isValid(UpdateCessionnaireReq dto, ConstraintValidatorContext context) {
            return !cesRepo.alreadyExistsByEmail(dto.getCesEmail(), dto.getCesId());
        }
    }
}
