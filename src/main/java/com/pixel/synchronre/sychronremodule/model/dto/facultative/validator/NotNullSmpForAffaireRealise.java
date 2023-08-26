package com.pixel.synchronre.sychronremodule.model.dto.facultative.validator;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
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
@Constraint(validatedBy = {NotNullSmpForAffaireRealise.NotNullSmpForAffaireRealiseValidatorOnCreate.class,
        NotNullSmpForAffaireRealise.NotNullSmpForAffaireRealiseValidatorOnUpdate.class})
@Documented
public @interface NotNullSmpForAffaireRealise
{
    String message() default "La SMP/LCI ne peut être nulle pour une affaire réalisée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class NotNullSmpForAffaireRealiseValidatorOnCreate implements ConstraintValidator<NotNullSmpForAffaireRealise, CreateFacultativeReq>
    {
        @Override
        public boolean isValid(CreateFacultativeReq dto, ConstraintValidatorContext context)
        {
            String affStatutCreation = dto.getAffStatutCreation();
            if (affStatutCreation == null) return true;

            return (affStatutCreation.equals("REALISEE") && dto.getFacSmpLci() != null) || !affStatutCreation.equals("REALISEE");
        }
    }

    @Component
    @RequiredArgsConstructor
    class NotNullSmpForAffaireRealiseValidatorOnUpdate implements ConstraintValidator<NotNullSmpForAffaireRealise, UpdateFacultativeReq>
    {
        @Override
        public boolean isValid(UpdateFacultativeReq dto, ConstraintValidatorContext context)
        {
            String affStatutCreation = dto.getAffStatutCreation();
            if (affStatutCreation == null) return true;

            return (affStatutCreation.equals("REALISEE") && dto.getFacSmpLci() != null) || !affStatutCreation.equals("REALISEE");
        }
    }
}
