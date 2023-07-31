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
@Constraint(validatedBy = {ExistingBanNumCompte.ExistingBanNumCompteValidator.class})
@Documented
public @interface ExistingBanNumCompte
{
    String message() default "Numero de compte introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingBanNumCompteValidator implements ConstraintValidator<ExistingBanNumCompte, String>
    {
        private final BanqueRepository banRepo;
        @Override
        public boolean isValid(String banNumCompte, ConstraintValidatorContext context)
        {
            return banRepo.existsById(banNumCompte);
        }
    }
}
