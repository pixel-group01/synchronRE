package com.pixel.synchronre.sychronremodule.model.dto.banque.validator;

import com.pixel.synchronre.sychronremodule.model.dto.banque.request.UpdateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dao.BanqueRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueBanNumCompte.UniqueBanCodeValidatorOnCreate.class, UniqueBanNumCompte.UniqueBanCodeValidatorOnUpdate.class})
@Documented
public @interface UniqueBanNumCompte
{
    String message() default "Le numero de compte existe déja";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueBanCodeValidatorOnCreate implements ConstraintValidator<UniqueBanNumCompte, String>
    {
        private final BanqueRepository banRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !banRepo.alreadyExistsByNumCompte(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueBanCodeValidatorOnUpdate implements ConstraintValidator<UniqueBanNumCompte, UpdateBanqueReq>
    {
        private final BanqueRepository banRepo;
        @Override
        public boolean isValid(UpdateBanqueReq dto, ConstraintValidatorContext context) {
            return !banRepo.alreadyExistsByNumCompte(dto.getBanNumCompte());
        }
    }
}
