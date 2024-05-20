package com.pixel.synchronre.sychronremodule.model.dto.exercice.validator;


import com.pixel.synchronre.sychronremodule.model.dao.ExerciceRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingExeCode.ExistingExeCodeValidatorOnCreate.class})
@Documented
public @interface ExistingExeCode
{
    String message() default "Exercice non existant";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingExeCodeValidatorOnCreate implements ConstraintValidator<ExistingExeCode, Long>
    {
        private final ExerciceRepository ExoRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            if (value == null) return true;
            return ExoRepo.alreadyExistsByCode(value);
        }
    }
}
