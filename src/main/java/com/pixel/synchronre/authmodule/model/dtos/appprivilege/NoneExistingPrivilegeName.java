package com.pixel.synchronre.authmodule.model.dtos.appprivilege;

import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(value = {ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneExistingPrivilegeName.NoneExistingPrivilegeNameValidator.class})
@Documented
public @interface NoneExistingPrivilegeName
{
    String message() default "Ce nom de privilège est déjà utilisé";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingPrivilegeNameValidator implements ConstraintValidator<NoneExistingPrivilegeName, String>
    {
        private final PrvRepo prvDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return false;//!prvDAO.alreadyExistsByName(value);
        }
    }
}
