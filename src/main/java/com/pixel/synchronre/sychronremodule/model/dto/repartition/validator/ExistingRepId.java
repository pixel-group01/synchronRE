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
@Constraint(validatedBy = {ExistingRepId.ExistingRepIdValidator.class})
@Documented
public @interface ExistingRepId
{
    String message() default "Repartition introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingRepIdValidator implements ConstraintValidator<ExistingRepId, Long>
    {
        private final RepartitionRepository repRepo;
        @Override
        public boolean isValid(Long repId, ConstraintValidatorContext context)
        {
            if(repId == null) return true;
            return repRepo.existsById(repId);
        }
    }
}
