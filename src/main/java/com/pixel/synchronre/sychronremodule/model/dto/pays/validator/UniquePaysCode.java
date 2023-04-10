package com.pixel.synchronre.sychronremodule.model.dto.pays.validator;

import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.UpdatePaysReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniquePaysCode.UniqueBanCodeValidatorOnCreate.class, UniquePaysCode.UniquePaysCodeValidatorOnUpdate.class})
@Documented
public @interface UniquePaysCode
{
    String message() default "Code du pays déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueBanCodeValidatorOnCreate implements ConstraintValidator<UniquePaysCode, String>
    {

        private final PaysRepository paysRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !paysRepo.alreadyExistsByCode(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniquePaysCodeValidatorOnUpdate implements ConstraintValidator<UniquePaysCode, UpdatePaysReq>
    {
        private final PaysRepository paysRepo;
        @Override
        public boolean isValid(UpdatePaysReq dto, ConstraintValidatorContext context) {
            return !paysRepo.alreadyExistsByCode(dto.getPaysCode(), dto.getPaysId());
        }
    }
}
