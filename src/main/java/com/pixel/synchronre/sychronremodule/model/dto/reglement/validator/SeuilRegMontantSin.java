package com.pixel.synchronre.sychronremodule.model.dto.reglement.validator;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateRepartitionReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesSinistre;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SeuilRegMontantSin.SeuilRegMontantValidatorOnCreate.class,
        SeuilRegMontantSin.SeuilRepCapValidatorOnUpdate.class})
@Documented
public @interface SeuilRegMontantSin
{
    String message() default "regMontant::Le montant du règlement ne peut excéder le reste à régler";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class SeuilRegMontantValidatorOnCreate implements ConstraintValidator<SeuilRegMontantSin, CreateReglementReq>
    {
        private final IServiceCalculsComptablesSinistre comptaSinistreService;
        @Override
        public boolean isValid(CreateReglementReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaSinistreService.calculateResteARegleBySin(dto.getSinId()).compareTo(dto.getRegMontant()) <= 0 ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnUpdate implements ConstraintValidator<SeuilRegMontantSin, UpdateRepartitionReq>
    {
        private final IServiceCalculsComptablesSinistre comptaSinistreService;
        @Override
        public boolean isValid(UpdateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaSinistreService.calculateResteARegleBySin(dto.getSinId()).compareTo(dto.getRepCapital()) <= 0;
        }
    }
}
