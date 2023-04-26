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
@Constraint(validatedBy = {UniqueExoCode.UniqueExoCodeValidatorOnCreate.class, UniqueExoCode.UniqueExoCodeValidatorOnUpdate.class})
@Documented
public @interface UniqueExoCode
{
    String message() default "Exercice déjà existant";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueExoCodeValidatorOnCreate implements ConstraintValidator<UniqueExoCode, Long>
    {
        private final ExerciceRepository ExoRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return !ExoRepo.alreadyExistsByCode(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueExoCodeValidatorOnUpdate implements ConstraintValidator<UniqueExoCode, UpdateExerciceReq>
    {
        private final ExerciceRepository ExoRepo;
        @Override
        public boolean isValid(UpdateExerciceReq dto, ConstraintValidatorContext context) {
            return !ExoRepo.alreadyExistsByCode(dto.getExeCode());
        }
    }
}
