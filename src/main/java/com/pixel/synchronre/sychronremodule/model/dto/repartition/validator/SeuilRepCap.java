package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.constants.PRECISION;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
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
@Constraint(validatedBy = {SeuilRepCap.SeuilRepCapValidatorOnCreate.class,
        SeuilRepCap.SeuilRepCapValidatorOnUpdate.class,
        SeuilRepCap.SeuilRepCapValidatorOnCreateCesLeg.class,
        SeuilRepCap.SeuilRepCapValidatorOnCreatePartCed.class,
        SeuilRepCap.SeuilRepCapValidatorOnCreatePlaRep.class,
        SeuilRepCap.SeuilRepCapValidatorOnCreateCedLeg.class})
@Documented
public @interface SeuilRepCap
{
    String message() default "Le montant du capital ne peut excéder le reste à repartir";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreate implements ConstraintValidator<SeuilRepCap, CreateRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return comptaService.calculateRestARepartir(dto.getAffId()).compareTo(dto.getRepCapital()) == 0 ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreateCesLeg implements ConstraintValidator<SeuilRepCap, CreateCesLegReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreateCesLegReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return SeuilCapitalChecker.checkSeuilCapital(comptaService, dto.getAffId(), dto.getRepCapital());
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreatePlaRep implements ConstraintValidator<SeuilRepCap, CreatePlaRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        private final RepartitionRepository repRepo;
        @Override
        public boolean isValid(CreatePlaRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            if(dto.getRepCapital() == null) return true;
            boolean placementExistsByAffaireAndCesId = repRepo.existsByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
            boolean modeUpdate = dto.getRepId() != null || placementExistsByAffaireAndCesId;

            if(!modeUpdate)
            {
                return SeuilCapitalChecker.checkSeuilCapital(comptaService, dto.getAffId(), dto.getRepCapital());
            }
            else
            {
                BigDecimal capitalToUpdate = dto.getRepId() != null ? repRepo.getRepCapitalByRepId(dto.getRepId()) :
                        repRepo.getRepCapitalByAffIdAndCesId(dto.getAffId(), dto.getCesId());
                capitalToUpdate = capitalToUpdate == null ? BigDecimal.ZERO : capitalToUpdate;
                return SeuilCapitalChecker.checkSeuilCapital(comptaService, dto.getAffId(), dto.getRepCapital(), capitalToUpdate);
            }

        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreatePartCed implements ConstraintValidator<SeuilRepCap, CreatePartCedRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreatePartCedRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return SeuilCapitalChecker.checkSeuilCapital(comptaService, dto.getAffId(), dto.getRepCapital());
        }


    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnUpdate implements ConstraintValidator<SeuilRepCap, UpdateRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(UpdateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return SeuilCapitalChecker.checkSeuilCapital(comptaService, dto.getAffId(), dto.getRepCapital());
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilRepCapValidatorOnCreateCedLeg implements ConstraintValidator<SeuilRepCap, CreateCedLegRepartitionReq>
    {
        private final IServiceCalculsComptables comptaService;
        @Override
        public boolean isValid(CreateCedLegRepartitionReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAffId() == null) return true;
            return SeuilCapitalChecker.checkSeuilCapital(comptaService, dto.getAffId(), dto.getRepCapital());
        }
    }

    class SeuilCapitalChecker
    {
        public static boolean checkSeuilCapital(IServiceCalculsComptables comptaService, Long affId, BigDecimal repCapital) {
            BigDecimal resteARepartir = comptaService.calculateRestARepartir(affId);
            resteARepartir = resteARepartir == null ? BigDecimal.ZERO : resteARepartir;
            BigDecimal futureResteARepartir = resteARepartir.subtract(repCapital);
            return futureResteARepartir.compareTo(BigDecimal.ZERO) >= 0 || futureResteARepartir.abs().compareTo(PRECISION.UN) <=0 ;
        }

        public static boolean checkSeuilCapital(IServiceCalculsComptables comptaService, Long affId, BigDecimal repCapital, BigDecimal capitalToUpdate) {
            BigDecimal resteARepartir = comptaService.calculateRestARepartir(affId);
            resteARepartir = resteARepartir == null ? BigDecimal.ZERO : resteARepartir;
            resteARepartir = resteARepartir.add(capitalToUpdate);
            BigDecimal futureResteARepartir = resteARepartir.subtract(repCapital);
            return futureResteARepartir.compareTo(BigDecimal.ZERO) >= 0 || futureResteARepartir.abs().compareTo(PRECISION.UN) <=0 ;
        }
    }
}
