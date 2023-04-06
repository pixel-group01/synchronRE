package com.pixel.synchronre.authmodule.model.dtos.approle;

import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueRoleCode.UniqueRoleNameValidatorOnCreate.class})
@Documented
public @interface UniqueRoleCode
{
    String message() default "Code de rôle déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnCreate implements ConstraintValidator<UniqueRoleCode, String>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !roleRepo.existsByCode(value);
        }
    }
}


