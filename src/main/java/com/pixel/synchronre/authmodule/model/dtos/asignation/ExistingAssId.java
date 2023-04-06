package com.pixel.synchronre.authmodule.model.dtos.asignation;

import com.pixel.synchronre.authmodule.controller.repositories.FunctionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingAssId.ExistingAssIdValidator.class})
public @interface ExistingAssId
{
    String message() default "Identitifiant de l'assignation invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingAssIdValidator implements ConstraintValidator<ExistingAssId, Long>
    {
        private final FunctionRepo assRepo;
        @Override
        public boolean isValid(Long assId, ConstraintValidatorContext context)
        {
            return assRepo.existsById(assId);
        }
    }
}
