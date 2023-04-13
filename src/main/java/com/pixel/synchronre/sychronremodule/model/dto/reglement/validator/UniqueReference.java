package com.pixel.synchronre.sychronremodule.model.dto.reglement.validator;

import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueReference.UniqueReferenceValidatorOnCreate.class, UniqueReference.ReferenceValidatorOnUpdate.class})
@Documented
public @interface UniqueReference
{
    String message() default "Cette réference existe déja";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueReferenceValidatorOnCreate implements ConstraintValidator<UniqueReference, String>
    {
        private final ReglementRepository regRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !regRepo.alreadyExistsByReference(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class ReferenceValidatorOnUpdate implements ConstraintValidator<UniqueReference, UpdateReglementReq>
    {
        private final ReglementRepository regRepo;
        @Override
        public boolean isValid(UpdateReglementReq dto, ConstraintValidatorContext context) {
            return !regRepo.alreadyExistsByReference(dto.getRegReference());
        }
    }
}
