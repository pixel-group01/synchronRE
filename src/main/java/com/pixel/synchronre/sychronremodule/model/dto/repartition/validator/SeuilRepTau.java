package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.constants.PRECISION;
import com.pixel.synchronre.sychronremodule.model.dao.CedanteTraiteRepository;
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
import java.math.BigDecimal;

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
            return SeuilTauxChecker.checkSeuilTaux(comptaService, dto.getAffId(), dto.getRepTaux());
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
            return SeuilTauxChecker.checkSeuilTaux(comptaService, dto.getAffId(), dto.getRepTaux());
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
            return SeuilTauxChecker.checkSeuilTaux(comptaService, dto.getAffId(), dto.getRepTaux());
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
            return SeuilTauxChecker.checkSeuilTaux(comptaService, dto.getAffId(), dto.getRepTaux());
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
            return SeuilTauxChecker.checkSeuilTaux(comptaService, dto.getAffId(), dto.getRepTaux());
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
            return SeuilTauxChecker.checkSeuilTaux(comptaService, dto.getAffId(), dto.getRepTaux());
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepTauValidatorOnTraiteNP implements ConstraintValidator<SeuilRepTau, PlacementTraiteNPReq>
    {
        private final IServiceCalculsComptablesTraite comptaService;
        private final CedanteTraiteRepository cedTraiRepo;
        @Override
        public boolean isValid(PlacementTraiteNPReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getCedanteTraiteId() == null) return true;
            Long traiteNPId = cedTraiRepo.getTraiteIdByCedTraiId(dto.getCedanteTraiteId());
            return SeuilTauxChecker.checkSeuilTaux(comptaService, traiteNPId, dto.getRepTaux());
        }
    }
}

class SeuilTauxChecker
{
    public static boolean checkSeuilTaux(IServiceCalculsComptables comptaService, Long affId, BigDecimal repTaux) {
        BigDecimal tauxRestantARepartir = comptaService.calculateTauxRestARepartir(affId);
        tauxRestantARepartir = tauxRestantARepartir == null ? BigDecimal.ZERO : tauxRestantARepartir;
        BigDecimal futureTauxRestantARepartir = tauxRestantARepartir.subtract(repTaux);
        return futureTauxRestantARepartir.compareTo(BigDecimal.ZERO) >= 0 || futureTauxRestantARepartir.abs().compareTo(PRECISION.UN_CHIFFRES) <=0 ;
    }

    public static boolean checkSeuilTaux(IServiceCalculsComptablesTraite comptaService, Long traiteNpId, BigDecimal repTaux) {
        BigDecimal tauxRestantARepartir = comptaService.calculateTauxRestantARepartir(traiteNpId);
        tauxRestantARepartir = tauxRestantARepartir == null ? BigDecimal.ZERO : tauxRestantARepartir;
        BigDecimal futureTauxRestantARepartir = tauxRestantARepartir.subtract(repTaux);
        return futureTauxRestantARepartir.compareTo(BigDecimal.ZERO) >= 0 || futureTauxRestantARepartir.abs().compareTo(PRECISION.UN_CHIFFRES) <=0 ;
    }
}