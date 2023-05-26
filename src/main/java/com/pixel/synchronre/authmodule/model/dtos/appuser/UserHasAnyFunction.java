package com.pixel.synchronre.authmodule.model.dtos.appuser;

import com.pixel.synchronre.authmodule.controller.repositories.FunctionRepo;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UserHasAnyFunction.UserHasAnyFunctionValidatorOnlogin.class})
@Documented
public @interface UserHasAnyFunction
{
    String message() default "Vous ne disposez d'aucune fonction. Veuillez contacter un administrateur SVP";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UserHasAnyFunctionValidatorOnlogin implements ConstraintValidator<UserHasAnyFunction, LoginDTO>
    {
        private final FunctionRepo fncRepo;
        @Override
        public boolean isValid(LoginDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null || dto.getUsername() == null) return true;
            return fncRepo.userHasAnyAppFunction(dto.getUsername());
        }
    }
}
