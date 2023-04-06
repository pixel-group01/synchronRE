package com.pixel.synchronre.authmodule.model.dtos.appuser;

import com.pixel.synchronre.authmodule.controller.repositories.AccountTokenRepo;
import com.pixel.synchronre.authmodule.model.entities.AccountToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneAlreadyUsedToken.NoneAlreadyUsedTokenValidator.class})
@Documented
public @interface NoneAlreadyUsedToken
{
    String message() default "Le lien a déjà été utilisé";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneAlreadyUsedTokenValidator implements ConstraintValidator<NoneAlreadyUsedToken, String>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            AccountToken token = tokenRepo.findByToken(value).orElse(null);
            return value == null ? false : token == null ? false : !token.isAlreadyUsed();
        }
    }
}


