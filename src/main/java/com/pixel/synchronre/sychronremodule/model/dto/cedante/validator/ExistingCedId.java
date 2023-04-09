package com.pixel.synchronre.sychronremodule.model.dto.cedante.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public @interface ExistingCedId
{
    String message() default "Identitifiant de cedente introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCesIdValidator implements ConstraintValidator<ExistingCedId, Long>
    {
        private final CedRepo cedRepo;
        @Override
        public boolean isValid(Long cedId, ConstraintValidatorContext context)
        {
            return cedRepo.existsById(cedId);
        }
    }
}
