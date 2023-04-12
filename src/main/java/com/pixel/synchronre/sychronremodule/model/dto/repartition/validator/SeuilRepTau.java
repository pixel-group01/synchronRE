package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateRepartitionReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SeuilRepTau.SeuilRepTauValidatorOnCreate.class, SeuilRepTau.SeuilRepTauValidatorOnUpdate.class})
@Documented
public @interface SeuilRepTau
{
    String message() default "repTaux::Le taux de répartition ne peut exéder le taux restant";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnCreate implements ConstraintValidator<SeuilRepTau, CreateRepartitionReq>
    {
        private final IserviceAffaire affService;
        @Override
        public boolean isValid(CreateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return affService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnUpdate implements ConstraintValidator<SeuilRepTau, UpdateRepartitionReq>
    {
        private final IserviceAffaire affService;
        @Override
        public boolean isValid(UpdateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return affService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }
}
