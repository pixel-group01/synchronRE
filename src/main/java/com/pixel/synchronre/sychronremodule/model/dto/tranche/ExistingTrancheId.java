package com.pixel.synchronre.sychronremodule.model.dto.tranche;

import com.pixel.synchronre.sychronremodule.model.dao.TrancheRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingTrancheId.ExistingTrancheIdValidator.class})
@Documented
public @interface ExistingTrancheId
{
    String message() default "Tranche introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingTrancheIdValidator implements ConstraintValidator<ExistingTrancheId, Long>
    {
        private final TrancheRepository trancheRepo;
        @Override
        public boolean isValid(Long trancheId, ConstraintValidatorContext context)
        {
            if(trancheId == null) return true;
            return trancheRepo.existsById(trancheId);
        }
    }
}