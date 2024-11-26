package com.pixel.synchronre.sychronremodule.model.dto.periode;

import com.pixel.synchronre.sychronremodule.model.dao.PeriodeRepo;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingPeriodeId.ExistingPeriodeIdValidator.class})
@Documented
public @interface ExistingPeriodeId
{
    String message() default "Période introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingPeriodeIdValidator implements ConstraintValidator<ExistingPeriodeId, Long>
    {
        private final PeriodeRepo periodeRepo;
        @Override
        public boolean isValid(Long periodeId, ConstraintValidatorContext context)
        {
            if(periodeId == null) return true;
            return periodeRepo.existsById(periodeId);
        }
    }
}