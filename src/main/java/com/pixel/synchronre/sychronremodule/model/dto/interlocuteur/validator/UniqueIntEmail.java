package com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.InterlocuteurRepository;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.UpdateInterlocuteurReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueIntEmail.UniqueIntEmailValidatorOnCreate.class, UniqueIntEmail.UniqueIntEmailValidatorOnUpdate.class})
@Documented
public @interface UniqueIntEmail
{
    String message() default "Adresse mail déjà attribuée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueIntEmailValidatorOnCreate implements ConstraintValidator<UniqueIntEmail, String>
    {
        private final InterlocuteurRepository intRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !intRepo.alreadyExistsByEmail(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueIntEmailValidatorOnUpdate implements ConstraintValidator<UniqueIntEmail, UpdateInterlocuteurReq>
    {
        private final InterlocuteurRepository intRepo;
        @Override
        public boolean isValid(UpdateInterlocuteurReq dto, ConstraintValidatorContext context) {
            return !intRepo.alreadyExistsByEmail(dto.getIntEmail(), dto.getIntId());
        }
    }
}
