package com.pixel.synchronre.authmodule.model.dtos.appuser;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConcordantUserIdAndEmail.ConcordantUserIdAndEmailValidatorOnChangingPassword.class})
@Documented
public @interface ConcordantUserIdAndEmail
{
    String message() default "email::L'email fourni ne correspond pas au userId";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /*@Component @RequiredArgsConstructor
    class ConcordantUserIdAndEmailValidatorOnAccountActivation implements ConstraintValidator <ConcordantUserIdAndEmail, ActivateAccountDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(ActivateAccountDTO dto, ConstraintValidatorContext context)
        {
            Long userId = dto.getUserId(); String email = dto.getEmail();
            return userId == null || email == null ? false : userRepo.existsByUserIdAndEmail(userId, email);
        }
    }*/

    @Component @RequiredArgsConstructor
    class ConcordantUserIdAndEmailValidatorOnChangingPassword implements ConstraintValidator <ConcordantUserIdAndEmail, ChangePasswordDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(ChangePasswordDTO dto, ConstraintValidatorContext context)
        {
            Long userId = dto.getUserId(); String email = dto.getEmail();
            return userId == null || email == null ? false : userRepo.existsByUserIdAndEmail(userId, email);
        }
    }
}


