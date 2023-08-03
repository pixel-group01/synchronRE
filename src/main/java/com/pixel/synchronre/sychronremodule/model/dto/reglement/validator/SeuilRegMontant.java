package com.pixel.synchronre.sychronremodule.model.dto.reglement.validator;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesSinistre;
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
@Constraint(validatedBy = {SeuilRegMontant.SeuilRegMontantValidatorOnCreate.class,
        SeuilRegMontant.SeuilRepCapValidatorOnUpdate.class})
@Documented
public @interface SeuilRegMontant
{
    String message() default "regMontant::Le montant du règlement ne peut excéder le reste à régler";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class SeuilRegMontantValidatorOnCreate implements ConstraintValidator<SeuilRegMontant, CreateReglementReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreateReglementReq dto, ConstraintValidatorContext context)
        {
            String typeReg = dto.getTypeReg();
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            if(typeReg == null) return true;
            if(typeReg.toLowerCase().equals("paiements"))
                return comptaService.calculateRestARegler(dto.getAffId()).compareTo(dto.getRegMontant()) >= 0 ;
            if(typeReg.toLowerCase().equals("reversements"))
                return comptaService.calculateMtEnAttenteDeAReversement(dto.getAffId()).compareTo(dto.getRegMontant()) >= 0 ;
            return true;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnUpdate implements ConstraintValidator<SeuilRegMontant, UpdateReglementReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(UpdateReglementReq dto, ConstraintValidatorContext context)
        {
            /*String typeReg = dto.getTypeReg();
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaService.calculateRestARegler(dto.getAffId()).compareTo(dto.getRepCapital()) >= 0;

            if(typeReg.toLowerCase().equals("paiements"))
                return comptaService.calculateRestARegler(dto.getAffId()).compareTo(dto.getRegMontant()) >= 0 ;
            if(typeReg.toLowerCase().equals("reversements"))
                return comptaService.calculateMtEnAttenteDeAReversement(dto.getAffId()).compareTo(dto.getRegMontant()) >= 0 ;
            */
            return true;

        }
    }
}
