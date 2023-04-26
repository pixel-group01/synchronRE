package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingPlaId.ExistingPlaIdValidator.class})
@Documented
public @interface ExistingPlaId
{
    String message() default "Placement introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingPlaIdValidator implements ConstraintValidator<ExistingPlaId, Long>
    {
        private final RepartitionRepository repRepo;
        @Override
        public boolean isValid(Long repId, ConstraintValidatorContext context)
        {
            return repRepo.placementExists(repId);
        }
    }
}
