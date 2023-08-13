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
@Constraint(validatedBy = {UniqueIntTel.UniqueIntTelValidatorOnCreate.class, UniqueIntTel.UniqueIntTelValidatorOnUpdate.class})
@Documented
public @interface UniqueIntTel
{
    String message() default "Numéro de téléphone déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueIntTelValidatorOnCreate implements ConstraintValidator<UniqueIntTel, String>
    {
        private final InterlocuteurRepository intRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !intRepo.alreadyExistsByTel(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueIntTelValidatorOnUpdate implements ConstraintValidator<UniqueIntTel, UpdateInterlocuteurReq>
    {
        private final InterlocuteurRepository intRepo;
        @Override
        public boolean isValid(UpdateInterlocuteurReq dto, ConstraintValidatorContext context) {
            return !intRepo.alreadyExistsByTel(dto.getIntTel(), dto.getIntId());
        }
    }
}
