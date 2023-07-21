package com.pixel.synchronre.sychronremodule.model.dto.reglement.validator;

import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
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
        SeuilRegMontantSin.SeuilRegMontantValidatorOnUpdate.class})
@Documented
public @interface SeuilRegMontantSin
{
    String message() default "Le montant du règlement ne peut excéder le reste à régler";
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
            if(dto.getSinId() == null ) return true;
            if(dto.getCesId() == null) // C'est un reversement
            {
                return comptaSinistreService.calculateMtSinistreEnAttenteDeAReversement(dto.getSinId()).compareTo(dto.getRegMontant()) >= 0;
            }
            return comptaSinistreService.calculateResteAPayerBySin(dto.getSinId()).compareTo(dto.getRegMontant()) >= 0 ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRegMontantValidatorOnUpdate implements ConstraintValidator<SeuilRegMontantSin, UpdateReglementReq>
    {
        private final IServiceCalculsComptablesSinistre comptaSinistreService;
        private final ReglementRepository regRepo;
        @Override
        public boolean isValid(UpdateReglementReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            Reglement reg = regRepo.findById(dto.getRegId()).orElse(null);
            if(reg == null) return true;
            if(reg.getSinId() == null || reg.getCesId() == null) return true;
            return comptaSinistreService.calculateResteAPayerBySinAndCes(reg.getSinId(), reg.getCesId()).compareTo(dto.getRegMontant()) >= 0;
        }
    }
}
