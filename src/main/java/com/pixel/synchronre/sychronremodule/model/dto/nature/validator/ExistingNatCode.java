package com.pixel.synchronre.sychronremodule.model.dto.nature.validator;

import com.pixel.synchronre.sychronremodule.model.dao.NatureRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingNatCode.ExistingNatCodeValidator.class})
@Documented
public @interface ExistingNatCode
{
    String message() default "Nature introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingNatCodeValidator implements ConstraintValidator<ExistingNatCode, String>
    {
        private final NatureRepository natRepo;
        @Override
        public boolean isValid(String natCode, ConstraintValidatorContext context)
        {
            if(natCode == null) return true;
            return natRepo.existsById(natCode.toUpperCase());
        }
    }
}