package com.pixel.synchronre.authmodule.model.dtos.approle;

import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingOrNullRoleId.ExistingOrNullRoleIdValidator.class})
@Documented
public @interface ExistingOrNullRoleId
{
    String message() default "Invalid roleId";
    Class<?> [] group() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullRoleIdValidator implements ConstraintValidator<ExistingOrNullRoleId, Long>
    {
        private final RoleRepo roleRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            return roleRepo.existsById(value) || value==null;
        }
    }
}

