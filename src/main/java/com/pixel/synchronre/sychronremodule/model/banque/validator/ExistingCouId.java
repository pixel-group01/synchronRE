package com.pixel.synchronre.sychronremodule.model.banque.validator;



import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public @interface ExistingCouId
{
    String message() default "Identitifiant de la couverture est introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCesIdValidator implements ConstraintValidator<ExistingCouId, Long>
    {
        private final CouvertureRepository couvRepo;
        @Override
        public boolean isValid(Long couId, ConstraintValidatorContext context)
        {
            return couvRepo.existsById(couId);
        }
    }
}
