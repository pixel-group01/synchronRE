package com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator;




import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public @interface ExistingParamCesLegId
{
    String message() default "Identitifiant du parametre de la cession légale est introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCesIdValidator implements ConstraintValidator<ExistingParamCesLegId, Long>
    {
        private final ParamCessionLegaleRepository paramRepo;

        @Override
        public boolean isValid(Long paramCesLegId, ConstraintValidatorContext context)
        {
            return paramRepo.existsById(paramCesLegId);
        }
    }
}
