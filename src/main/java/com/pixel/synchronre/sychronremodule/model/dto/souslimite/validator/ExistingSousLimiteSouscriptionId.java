package com.pixel.synchronre.sychronremodule.model.dto.souslimite.validator;

import com.pixel.synchronre.sychronremodule.model.dao.SousLimiteRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingSousLimiteSouscriptionId.ExistingSousLimiteSouscriptionValidator.class})
@Documented
public @interface ExistingSousLimiteSouscriptionId
{
    String message() default "Identitifiant de la sous-limite est introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingSousLimiteSouscriptionValidator implements ConstraintValidator<ExistingSousLimiteSouscriptionId, Long>
    {
        private final SousLimiteRepository sslRepo;
        @Override
        public boolean isValid(Long sousLimiteSouscriptionId, ConstraintValidatorContext context)
        {
            if(sousLimiteSouscriptionId == null) return true;
            return sslRepo.existsById(sousLimiteSouscriptionId);
        }
    }
}
