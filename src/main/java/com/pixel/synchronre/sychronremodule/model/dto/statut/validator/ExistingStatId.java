package com.pixel.synchronre.sychronremodule.model.dto.statut.validator;

import com.pixel.synchronre.authmodule.model.dtos.asignation.ExistingAssId;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public @interface ExistingStatId
{
    String message() default "Identitifiant du statut introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCesIdValidator implements ConstraintValidator<ExistingStatId, Long>
    {
        private final StatutRepository statRepo;
        @Override
        public boolean isValid(Long staId, ConstraintValidatorContext context)
        {
            return statRepo.existsById(staId);
        }
    }
}
