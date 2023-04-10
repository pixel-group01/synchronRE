package com.pixel.synchronre.sychronremodule.model.dto.pays.validator;



import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingPaysId.ExistingBanIddValidator.class})
@Documented
public @interface ExistingPaysId
{
    String message() default "L'identifiant du pays est introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingBanIddValidator implements ConstraintValidator<ExistingPaysId, Long>
    {
        private final PaysRepository paysRepo;
        @Override
        public boolean isValid(Long paysId, ConstraintValidatorContext context)
        {
            return paysRepo.existsById(paysId);
        }
    }
}
