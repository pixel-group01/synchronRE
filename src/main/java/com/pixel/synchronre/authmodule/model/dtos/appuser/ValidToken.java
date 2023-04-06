package com.pixel.synchronre.authmodule.model.dtos.appuser;

import com.pixel.synchronre.authmodule.controller.repositories.AccountTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidToken.ValidTokenValidator.class, ValidToken.ValidTokenValidatorOnAccountActivation.class, ValidToken.ValidTokenValidatorOnReinitPassword.class})
@Documented
public @interface ValidToken
{
    String message() default "Lien invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidTokenValidator implements ConstraintValidator<ValidToken, String>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return value == null ? false : tokenRepo.existsByToken(value);
        }
    }

    @Component @RequiredArgsConstructor
    class ValidTokenValidatorOnAccountActivation implements ConstraintValidator <ValidToken, ActivateAccountDTO>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(ActivateAccountDTO dto, ConstraintValidatorContext context)
        {
            return tokenRepo.existsByTokenAndUserId(dto.getActivationToken(), dto.getUserId());
        }
    }

    @Component @RequiredArgsConstructor
    class ValidTokenValidatorOnReinitPassword implements ConstraintValidator <ValidToken, ReinitialisePasswordDTO>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(ReinitialisePasswordDTO dto, ConstraintValidatorContext context)
        {
            return tokenRepo.existsByTokenAndUserEmail(dto.getPasswordReinitialisationToken(), dto.getEmail());
        }
    }
}


