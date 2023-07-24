package com.pixel.synchronre.sychronremodule.model.dto.reglement.validator;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidDocRegId.ValidDocRegIdValidator.class})
@Documented
public @interface ValidDocRegId
{
    String message() default "Veuillez choisir le type de document";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidDocRegIdValidator implements ConstraintValidator<ValidDocRegId, Long>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(Long typeId, ConstraintValidatorContext context)
        {
            Type typeDoc = typeRepo.findByUniqueCode("DOC_REG").orElseThrow(()->new AppException("Type de document inconnu"));
            return typeRepo.isSousTypeOf(typeDoc.getTypeId(), typeId);
        }
    }
}
