package com.pixel.synchronre.sychronremodule.model.dto.banque.validator;


import com.pixel.synchronre.sychronremodule.model.dao.BanqueRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingBanId.ExistingBanIddValidator.class})
@Documented
public @interface ExistingBanId
{
    String message() default "Identitifiant de la banque introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingBanIddValidator implements ConstraintValidator<ExistingBanId, Long>
    {
        private final BanqueRepository banRepo;
        @Override
        public boolean isValid(Long banId, ConstraintValidatorContext context)
        {
            return banRepo.existsById(banId);
        }
    }
}
