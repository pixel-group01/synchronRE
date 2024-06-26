package com.pixel.synchronre.sychronremodule.model.dto.couverture.validator;



import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCouParentId.ExistingCouIddValidator.class})
@Documented
public @interface ExistingCouParentId
{
    String message() default "Identitifiant parent introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCouIddValidator implements ConstraintValidator<ExistingCouParentId, Long>
    {
        private final CouvertureRepository couvRepo;
        @Override
        public boolean isValid(Long couId, ConstraintValidatorContext context)
        {
            if(couId == null) return true;
            return couvRepo.existsParentId(couId);
        }
    }
}
