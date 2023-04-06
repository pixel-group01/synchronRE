package com.pixel.synchronre.authmodule.model.dtos.appuser;

import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingOrNullUserId.ExistingOrNullUserIdValidator.class})
@Documented
public @interface ExistingOrNullUserId
{
    String message() default "Invalid userId";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullUserIdValidator implements ConstraintValidator<ExistingOrNullUserId, Long>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            return userRepo.existsById(value) || value == null;
        }
    }
}


