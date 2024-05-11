package com.pixel.synchronre.sychronremodule.model.dto.risquecouvert;

import com.pixel.synchronre.sychronremodule.model.dao.RisqueCouvertRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingRisqueId.ExistingRisqueIdValidator.class})
@Documented
public @interface ExistingRisqueId
{
    String message() default "Risque introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingRisqueIdValidator implements ConstraintValidator<ExistingRisqueId, Long>
    {
        private final RisqueCouvertRepository risqueRepo;
        @Override
        public boolean isValid(Long risqueId, ConstraintValidatorContext context)
        {
            if(risqueId == null) return true;
            return risqueRepo.existsById(risqueId);
        }
    }
}
