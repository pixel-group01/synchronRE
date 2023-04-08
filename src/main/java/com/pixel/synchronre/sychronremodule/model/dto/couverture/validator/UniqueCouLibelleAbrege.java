package com.pixel.synchronre.sychronremodule.model.dto.couverture.validator;

import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.UpdateCouvertureReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueCouLibelleAbrege.UniqueCouvLibelleAbregeValidatorOnCreate.class, UniqueCouLibelleAbrege.UniqueCouvLibelleAbregeValidatorOnUpdate.class})
@Documented
public @interface UniqueCouLibelleAbrege
{
    String message() default "Cette abréviation est déjà attribuée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueCouvLibelleAbregeValidatorOnCreate implements ConstraintValidator<UniqueCouLibelleAbrege, String>
    {

        private final CouvertureRepository couvRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !couvRepo.alreadyExistsByCouLibelleAbrege(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueCouvLibelleAbregeValidatorOnUpdate implements ConstraintValidator<UniqueCouLibelleAbrege, UpdateCouvertureReq>
    {

        private final CouvertureRepository couvRepo;
        @Override
        public boolean isValid(UpdateCouvertureReq dto, ConstraintValidatorContext context) {
            return !couvRepo.alreadyExistsByCouLibelleAbrege(dto.getCouLibelleAbrege(), dto.getCouId());
        }
    }
}
