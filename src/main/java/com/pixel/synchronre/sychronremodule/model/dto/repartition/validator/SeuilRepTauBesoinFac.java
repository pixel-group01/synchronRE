package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateRepartitionReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SeuilRepTauBesoinFac.SeuilRepTauValidator.class})
@Documented
public @interface SeuilRepTauBesoinFac
{
    String message() default "Le taux de répartition par rapport au besoin fac ne peut exéder 100%";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @RequiredArgsConstructor
    class SeuilRepTauValidator implements ConstraintValidator<SeuilRepTauBesoinFac, Float>
    {
        @Override
        public boolean isValid(Float repTauBesoinFac, ConstraintValidatorContext context)
        {
            return repTauBesoinFac <= 100;
        }
    }
}
