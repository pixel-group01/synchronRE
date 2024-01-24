package com.pixel.synchronre.sychronremodule.model.dto.facultative.validator;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.RenewFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidEcheanceDate.ValidEcheanceDateValidator.class, ValidEcheanceDate.ValidEcheanceDateValidatorOnUpdate.class, ValidEcheanceDate.ValidEcheanceDateValidatorOnRenew.class})
@Documented
public @interface ValidEcheanceDate
{
    String message() default "affDateEcheance::La date d'échéance doit être ultérieure à la date de prise d'effet";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidEcheanceDateValidator implements ConstraintValidator<ValidEcheanceDate, CreateFacultativeReq>
    {
        @Override
        public boolean isValid(CreateFacultativeReq dto, ConstraintValidatorContext context)
        {
            if (dto.getAffDateEffet() == null || dto.getAffDateEcheance() == null) return true;
            return dto.getAffDateEffet().isEqual(dto.getAffDateEcheance()) || dto.getAffDateEffet().isBefore(dto.getAffDateEcheance());
        }
    }

    @Component
    @RequiredArgsConstructor
    class ValidEcheanceDateValidatorOnUpdate implements ConstraintValidator<ValidEcheanceDate, UpdateFacultativeReq>
    {
        @Override
        public boolean isValid(UpdateFacultativeReq dto, ConstraintValidatorContext context)
        {
            if (dto.getAffDateEffet() == null || dto.getAffDateEcheance() == null) return true;
            return dto.getAffDateEffet().isEqual(dto.getAffDateEcheance()) || dto.getAffDateEffet().isBefore(dto.getAffDateEcheance());
        }
    }

    @Component
    @RequiredArgsConstructor
    class ValidEcheanceDateValidatorOnRenew implements ConstraintValidator<ValidEcheanceDate, RenewFacultativeReq>
    {
        @Override
        public boolean isValid(RenewFacultativeReq dto, ConstraintValidatorContext context)
        {
            if (dto.getAffDateEffet() == null || dto.getAffDateEcheance() == null) return true;
            return dto.getAffDateEffet().isEqual(dto.getAffDateEcheance()) || dto.getAffDateEffet().isBefore(dto.getAffDateEcheance());
        }
    }
}
