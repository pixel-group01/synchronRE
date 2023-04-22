package com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator;



import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingSinId.ExistingSinIdIdValidator.class})
@Documented
public @interface ExistingSinId
{
    String message() default "Sinsitre introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingSinIdIdValidator implements ConstraintValidator<ExistingSinId, Long>
    {
        private final SinRepo sinRepo;
        @Override
        public boolean isValid(Long sinId, ConstraintValidatorContext context)
        {
            return sinRepo.existsById(sinId);
        }
    }
}
