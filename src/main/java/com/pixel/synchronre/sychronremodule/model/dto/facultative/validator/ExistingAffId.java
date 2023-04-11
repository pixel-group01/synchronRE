package com.pixel.synchronre.sychronremodule.model.dto.facultative.validator;

import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingAffId.ExistingAffIdDateValidator.class})
@Documented
public @interface ExistingAffId
{
    String message() default "Affaire introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingAffIdDateValidator implements ConstraintValidator<ExistingAffId, Long>
    {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(Long affId, ConstraintValidatorContext context)
        {
            if (affId == null) return true;
            return affRepo.existsById(affId);
        }
    }
}
