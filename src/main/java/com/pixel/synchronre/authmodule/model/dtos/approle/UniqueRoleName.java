package com.pixel.synchronre.authmodule.model.dtos.approle;

import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import com.pixel.synchronre.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueRoleName.UniqueRoleNameValidatorOnCreate.class})
@Documented
public @interface UniqueRoleName
{
    String message() default "Ce nom de rôle est déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnCreate implements ConstraintValidator<UniqueRoleName, String>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !roleRepo.existsByName(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnUpdate implements ConstraintValidator<UniqueRoleCode, PrvsToRoleDTO>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(PrvsToRoleDTO dto, ConstraintValidatorContext context) {
            return !roleRepo.existsByName(dto.getRoleCode(), dto.getRoleId());
        }
    }
}


