package com.pixel.synchronre.authmodule.model.dtos.appprivilege;

import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingOrNullPrivilegeId.ExistingOrNullPrivilegeIdValidator.class})
@Documented
public @interface ExistingOrNullPrivilegeId
{
    String message() default "Invalid privilegeId";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullPrivilegeIdValidator implements ConstraintValidator<ExistingOrNullPrivilegeId, Long>
    {
        private final PrvRepo prvRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return prvRepo.existsById(value) || value == null;
        }
    }
}
