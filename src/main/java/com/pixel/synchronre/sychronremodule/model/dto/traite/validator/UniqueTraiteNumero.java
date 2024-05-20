package com.pixel.synchronre.sychronremodule.model.dto.traite.validator;

import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueTraiteNumero.UniqueTraiteNumeroValidatorOnUpdate.class})
@Documented
public @interface UniqueTraiteNumero
{
    String message() default "Numéro de traité déjà utilisée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueTraiteNumeroValidator implements ConstraintValidator<UniqueTraiteNumero, String>
    {
        private final TraiteNPRepository tnpRepo;
        @Override
        public boolean isValid(String numero, ConstraintValidatorContext context)
        {
            if(numero == null) return true;
            return !tnpRepo.existsByNumero(numero.toUpperCase());
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueTraiteNumeroValidatorOnUpdate implements ConstraintValidator<UniqueTraiteNumero, UpdateTraiteNPReq>
    {
        private final TraiteNPRepository tnpRepo;
        @Override
        public boolean isValid(UpdateTraiteNPReq dto, ConstraintValidatorContext context)
        {
            if(dto == null || dto.getTraiNumero() == null || dto.getTraiNumero() == null) return true;

            return !tnpRepo.existsByNumero(dto.getTraiNumero().toUpperCase(), dto.getTraiteNpId());
        }
    }
}