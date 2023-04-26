package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreatePlaRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdatePlaRepartitionReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CoherentTauxCrtAndScms.CoherentTauxCrtAndScmsValidatorOnCreatePla.class,
        CoherentTauxCrtAndScms.CoherentTauxCrtAndScmsValidatorOnUpdatePla.class})
@Documented
public @interface CoherentTauxCrtAndScms
{
    String message() default "Le taux de la commission de courtage ne peut exéder celui de la commission de réassurance";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


    @Component
    @RequiredArgsConstructor
    class CoherentTauxCrtAndScmsValidatorOnCreatePla implements ConstraintValidator<CoherentTauxCrtAndScms, CreatePlaRepartitionReq>
    {
        @Override
        public boolean isValid(CreatePlaRepartitionReq dto, ConstraintValidatorContext context)
        {
            if (dto.getRepSousCommission() == null) return true;
            if (dto.getRepTauxComCourt() == null) return true;
            return dto.getRepSousCommission().compareTo(dto.getRepTauxComCourt()) >= 0;
        }
    }

    @Component
    @RequiredArgsConstructor
    class CoherentTauxCrtAndScmsValidatorOnUpdatePla implements ConstraintValidator<CoherentTauxCrtAndScms, UpdatePlaRepartitionReq> {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(UpdatePlaRepartitionReq dto, ConstraintValidatorContext context)
        {
            if (dto.getRepSousCommission() == null) return true;
            if (dto.getRepTauxComCourt() == null) return true;
            return dto.getRepSousCommission().compareTo(dto.getRepTauxComCourt()) >= 0;
        }
    }
}
