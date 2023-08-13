package com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.InterlocuteurRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingIntId.ExistingIntIdValidator.class})
@Documented
public @interface ExistingIntId
{
    String message() default "Identitifiant de l'interlocuteur introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingIntIdValidator implements ConstraintValidator<ExistingIntId, Long>
    {
        private final InterlocuteurRepository intRepo;
        @Override
        public boolean isValid(Long intId, ConstraintValidatorContext context)
        {
            if(intId == null) return true;
            return intRepo.existsById(intId);
        }
    }
}
