package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesTraite;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SeuilRepTau.SeuilRepTauValidatorOnCreate.class,
        SeuilRepTau.SeuilRepTauValidatorOnUpdate.class, SeuilRepTau.SeuilRepTauValidatorOnCreatePartCed.class,
        SeuilRepTau.SeuilRepTauValidatorOnCreateCesLeg.class,
        SeuilRepTau.SeuilRepTauValidatorOnCreatePlaRep.class,
        SeuilRepTau.SeuilRepTauValidatorOnCreateCedLegRep.class,
        SeuilRepTau.SeuilRepTauValidatorOnTraiteNP.class})
@Documented
public @interface SeuilRepTau
{
    String message() default "Le taux de répartition ne peut exéder le taux restant";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnCreate implements ConstraintValidator<SeuilRepTau, CreateRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }


    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnCreateCesLeg implements ConstraintValidator<SeuilRepTau, CreateCesLegReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreateCesLegReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnCreatePartCed implements ConstraintValidator<SeuilRepTau, CreatePartCedRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreatePartCedRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnCreatePlaRep implements ConstraintValidator<SeuilRepTau, CreatePlaRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreatePlaRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnUpdate implements ConstraintValidator<SeuilRepTau, UpdateRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(UpdateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnCreateCedLegRep implements ConstraintValidator<SeuilRepTau, CreateCedLegRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreateCedLegRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnTraiteNP implements ConstraintValidator<SeuilRepTau, PlacementTraiteNPReq>
    {
        private final IServiceCalculsComptablesTraite comptaService;
        @Override
        public boolean isValid(PlacementTraiteNPReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getTraiNPId() == null) return true;
            return comptaService.calculateTauxRestantARepartir(dto.getTraiNPId()).compareTo(dto.getRepTaux()) >= 0;
        }
    }
}
