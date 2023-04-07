package com.pixel.synchronre.sychronremodule.model.dto.statut.validator;

import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Objects;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidStaType.ValidStaTypeValidator.class})
@Documented
public @interface ValidStaType
{
    String message() default "Type de statut invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidStaTypeValidator implements ConstraintValidator<ValidStaType, String>
    {
        private final StatutRepository statRepo;
        @Override
        public boolean isValid(String staType, ConstraintValidatorContext context)
        {
            return Arrays.stream(TypeStatut.values()).filter(Objects::nonNull).anyMatch(type->type.name().equals(staType));
        }
    }
}
