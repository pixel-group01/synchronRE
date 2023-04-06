package com.pixel.synchronre.sychronRe.model.dto.validator;

import com.pixel.synchronre.sychronRe.model.dao.StatutRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneExistingStatutCode.ExistingStatutValidator.class})
@Documented
public @interface NoneExistingStatutCode {
    String message() default "Ce code statut existe deja";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingStatutValidator implements ConstraintValidator<NoneExistingStatutCode,String>
    {
        private final StatutRepository statRepo;
        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

            return !statRepo.existsByStaCode(s);
        }
    }
}

