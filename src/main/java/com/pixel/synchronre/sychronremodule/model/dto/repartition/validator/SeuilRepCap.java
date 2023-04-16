package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
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
@Constraint(validatedBy = {SeuilRepCap.SeuilRepCapValidatorOnCreate.class,
        SeuilRepCap.SeuilRepCapValidatorOnUpdate.class,
        SeuilRepCap.SeuilRepCapValidatorOnCreateCesLeg.class,
        SeuilRepCap.SeuilRepCapValidatorOnCreatePartCed.class,
        SeuilRepCap.SeuilRepCapValidatorOnCreatePlaRep.class,
        SeuilRepCap.SeuilRepCapValidatorOnCreateCedLeg.class})
@Documented
public @interface SeuilRepCap
{
    String message() default "repCapital::Le montant du capital ne peut excéder le reste à repartir";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreate implements ConstraintValidator<SeuilRepCap, CreateRepartitionReq>
    {
        private final IserviceAffaire affService;
        @Override
        public boolean isValid(CreateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return affService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) == 0 ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreateCesLeg implements ConstraintValidator<SeuilRepCap, CreateCesLegReq>
    {
        private final IserviceAffaire affService;
        @Override
        public boolean isValid(CreateCesLegReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return affService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0 ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreatePlaRep implements ConstraintValidator<SeuilRepCap, CreatePlaRepartitionReq>
    {
        private final IserviceAffaire affService;
        @Override
        public boolean isValid(CreatePlaRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return affService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0 ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreatePartCed implements ConstraintValidator<SeuilRepCap, CreatePartCedRepartitionReq>
    {
        private final IserviceAffaire affService;
        @Override
        public boolean isValid(CreatePartCedRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return affService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0 ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnUpdate implements ConstraintValidator<SeuilRepCap, UpdateRepartitionReq>
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

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreateCedLeg implements ConstraintValidator<SeuilRepCap, CreateCedLegRepartitionReq>
    {
        private final IserviceAffaire affService;
        @Override
        public boolean isValid(CreateCedLegRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return affService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;
        }
    }
}
