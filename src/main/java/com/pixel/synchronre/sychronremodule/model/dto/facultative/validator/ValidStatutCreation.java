package com.pixel.synchronre.sychronremodule.model.dto.facultative.validator;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidStatutCreation.ValidStatutCreationValidator.class})
@Documented
public @interface ValidStatutCreation
{
    String message() default "Le statut de création de l'affaire doit être 'Réalisé', 'En instance' ou 'Non réalisé'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidStatutCreationValidator implements ConstraintValidator<ValidStatutCreation, String>
    {
        @Override
        public boolean isValid(String statutCreation, ConstraintValidatorContext context)
        {
            if (statutCreation == null) return true;
            return statutCreation.equals("NON_REALISEE") || statutCreation.equals("INSTANCE") || statutCreation.equals("REALISEE");
        }
    }
}
