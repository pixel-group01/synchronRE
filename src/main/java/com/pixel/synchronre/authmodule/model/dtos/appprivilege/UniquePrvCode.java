package com.pixel.synchronre.authmodule.model.dtos.appprivilege;

import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniquePrvCode.UniqueRoleNameValidatorOnCreate.class})
@Documented
public @interface UniquePrvCode
{
    String message() default "Code de privilège déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnCreate implements ConstraintValidator<UniquePrvCode, String>
    {
        private final PrvRepo prvRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return false;//!prvRepo.alreadyExistsByCode(value);
        }
    }
}


