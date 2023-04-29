package com.pixel.synchronre.sychronremodule.model.dto.devise.validator;



import com.pixel.synchronre.sychronremodule.model.dao.DeviseRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingDevCode.ExistingDevCodeValidator.class})
@Documented
public @interface ExistingDevCode
{
    String message() default "Identitifiant de la devise introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingDevCodeValidator implements ConstraintValidator<ExistingDevCode, String>
    {

        private final DeviseRepository devRepo;
        @Override
        public boolean isValid(String devCode, ConstraintValidatorContext context)
        {
            return devRepo.existsById(devCode);
        }
    }
}
