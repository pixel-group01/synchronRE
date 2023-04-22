package com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator;

import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.math.BigDecimal;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SeuilSinMontant.SeuilSinMontantValidatorOnCreate.class,
        SeuilSinMontant.SeuilSinMontantValidatorOnUpdate.class})
@Documented
public @interface SeuilSinMontant
{
    String message() default "sinMontant::Le montant du sinistre ne peut excéder le capital de l'affaire";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class SeuilSinMontantValidatorOnCreate implements ConstraintValidator<SeuilSinMontant, CreateSinistreReq>
    {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(CreateSinistreReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            BigDecimal affCapital = affRepo.getCapitalInitial(dto.getAffId());
            if(affCapital == null) return true;
            if(dto.getSinMontant100() == null) return true;
            return  dto.getSinMontant100().compareTo(affCapital)<=0;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilSinMontantValidatorOnUpdate implements ConstraintValidator<SeuilSinMontant, UpdateSinistreReq>
    {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(UpdateSinistreReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            BigDecimal affCapital = affRepo.getCapitalInitial(dto.getAffId());
            if(affCapital == null) return true;
            if(dto.getSinMontant100() == null) return true;
            return  dto.getSinMontant100().compareTo(affCapital)<=0;
        }
    }
}