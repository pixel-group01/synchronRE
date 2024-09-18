package com.pixel.synchronre.sychronremodule.model.dto.categorie;

import com.pixel.synchronre.sychronremodule.model.dao.CategorieRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCategorieId.ExistingCategorieIdValidator.class})
@Documented
public @interface ExistingCategorieId
{
    String message() default "Catégorie non prise en compte sur ce traité";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCategorieIdValidator implements ConstraintValidator<ExistingCategorieId, Long>
    {
        private final CategorieRepository catRepo;
        @Override
        public boolean isValid(Long categorieId, ConstraintValidatorContext context)
        {
            if(categorieId == null) return true;
            return catRepo.existsById(categorieId);
        }
    }
}