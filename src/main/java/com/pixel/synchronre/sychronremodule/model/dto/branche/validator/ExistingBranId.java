package com.pixel.synchronre.sychronremodule.model.dto.branche.validator;


import com.pixel.synchronre.sychronremodule.model.dao.BrancheRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public @interface ExistingBranId
{
    String message() default "Identitifiant de la branche introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCesIdValidator implements ConstraintValidator<ExistingBranId, Long>
    {
        private final BrancheRepository branRepo;
        @Override
        public boolean isValid(Long branId, ConstraintValidatorContext context)
        {
            return branRepo.existsById(branId);
        }
    }
}
