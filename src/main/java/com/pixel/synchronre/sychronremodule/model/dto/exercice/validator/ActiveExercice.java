package com.pixel.synchronre.sychronremodule.model.dto.exercice.validator;


import com.pixel.synchronre.sychronremodule.model.dao.ExerciceRepository;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.UpdateExerciceReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ActiveExercice.ActiveExerciceValidator.class})
@Documented
public @interface ActiveExercice
{
    String message() default "Exercice inexistant non valide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ActiveExerciceValidator implements ConstraintValidator<ActiveExercice, Long>
    {
        private final ExerciceRepository ExoRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return ExoRepo.exerciceIsActive(value);
        }
    }
}
