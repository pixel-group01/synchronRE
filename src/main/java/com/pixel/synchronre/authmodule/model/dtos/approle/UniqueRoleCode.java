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
@Constraint(validatedBy = {UniqueRoleCode.UniqueRoleCodeValidatorOnCreate.class, UniqueRoleCode.UniqueRoleCodeValidatorOnUpdate.class})
@Documented
public @interface UniqueRoleCode
{
    String message() default "Code de rôle déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleCodeValidatorOnCreate implements ConstraintValidator<UniqueRoleCode, String>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !roleRepo.existsByCode(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueRoleCodeValidatorOnUpdate implements ConstraintValidator<UniqueRoleCode, PrvsToRoleDTO>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(PrvsToRoleDTO dto, ConstraintValidatorContext context) {
            return !roleRepo.existsByCode(dto.getRoleCode(), dto.getRoleId());
        }
    }
}


