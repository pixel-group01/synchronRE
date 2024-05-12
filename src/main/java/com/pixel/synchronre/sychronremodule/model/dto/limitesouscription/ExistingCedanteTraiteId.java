package com.pixel.synchronre.sychronremodule.model.dto.limitesouscription;

import com.pixel.synchronre.sychronremodule.model.dao.CedanteTraiteRepository;
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
@Constraint(validatedBy = {ExistingCedanteTraiteId.ExistingCedanteTraiteIdValidator.class})
@Documented
public @interface ExistingCedanteTraiteId
{
    String message() default "Cédante non prise en compte sur ce traité";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCedanteTraiteIdValidator implements ConstraintValidator<ExistingCedanteTraiteId, Long>
    {
        private final CedanteTraiteRepository trancheRepo;
        @Override
        public boolean isValid(Long trancheId, ConstraintValidatorContext context)
        {
            if(trancheId == null) return true;
            return trancheRepo.existsById(trancheId);
        }
    }
}