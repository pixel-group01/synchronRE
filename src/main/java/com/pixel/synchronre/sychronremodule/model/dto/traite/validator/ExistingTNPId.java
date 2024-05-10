package com.pixel.synchronre.sychronremodule.model.dto.traite.validator;

import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingTNPId.ExistingTNPIdValidator.class})
@Documented
public @interface ExistingTNPId
{
    String message() default "Traité non proportionel introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingTNPIdValidator implements ConstraintValidator<ExistingTNPId, Long>
    {
        private final TraiteNPRepository tnpRepo;
        @Override
        public boolean isValid(Long tnpId, ConstraintValidatorContext context)
        {
            if(tnpId == null) return true;
            return tnpRepo.existsById(tnpId);
        }
    }
}
