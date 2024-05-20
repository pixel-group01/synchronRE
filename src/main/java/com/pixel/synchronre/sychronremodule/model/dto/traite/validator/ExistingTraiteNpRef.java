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
@Constraint(validatedBy = {ExistingTraiteNpRef.ExistingTraiteNpRefValidatorOnUpdate.class})
@Documented
public @interface ExistingTraiteNpRef
{
    String message() default "Référence de traité introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingTraiteNpRefValidator implements ConstraintValidator<ExistingTraiteNpRef, String>
    {
        private final TraiteNPRepository tnpRepo;
        @Override
        public boolean isValid(String ref, ConstraintValidatorContext context)
        {
            if(ref == null) return true;
            return tnpRepo.existsByRef(ref.toUpperCase());
        }
    }

    @Component
    @RequiredArgsConstructor
    class ExistingTraiteNpRefValidatorOnUpdate implements ConstraintValidator<ExistingTraiteNpRef, UpdateTraiteNPReq>
    {
        private final TraiteNPRepository tnpRepo;
        @Override
        public boolean isValid(UpdateTraiteNPReq dto, ConstraintValidatorContext context)
        {
            if(dto == null || dto.getTraiReference() == null || dto.getTraiteNpId() == null) return true;

            return tnpRepo.existsByRef(dto.getTraiReference().toUpperCase(), dto.getTraiteNpId());
        }
    }
}