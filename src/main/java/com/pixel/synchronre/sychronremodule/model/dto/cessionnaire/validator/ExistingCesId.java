package com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator;

import com.pixel.synchronre.authmodule.model.dtos.asignation.ExistingAssId;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public @interface ExistingCesId
{
    String message() default "Identitifiant du cessionnaire introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCesIdValidator implements ConstraintValidator<ExistingAssId, Long>
    {
        private final CessionnaireRepository cesRepo;
        @Override
        public boolean isValid(Long cesId, ConstraintValidatorContext context)
        {
            return cesRepo.existsById(cesId);
        }
    }
}
