package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateCesLegReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreatePartCedRepartitionReq;
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
import java.math.BigDecimal;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CoherentCapitalAndTaux.CoherentCapitalAndTauxOnCreate.class,
        CoherentCapitalAndTaux.CoherentCapitalAndTauxOnUpdate.class,
        CoherentCapitalAndTaux.CoherentCapitalAndTauxOnCreateCesLeg.class,
        CoherentCapitalAndTaux.CoherentCapitalAndTauxOnCreatePartCed.class})
@Documented
public @interface CoherentCapitalAndTaux
{
    String message() default "repCapital::Le montant du capital et le taux de répartition ne sont pas concordants";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


    @Component
    @RequiredArgsConstructor
    class CoherentCapitalAndTauxOnCreateCesLeg implements ConstraintValidator<CoherentCapitalAndTaux, CreateCesLegReq> {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(CreateCesLegReq dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            if (dto.getAffId() == null) return true;
            return dto.getRepCapital().multiply(new BigDecimal(100)).divide(affRepo.getCapitalInitial(dto.getAffId())).equals(dto.getRepTaux()) ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class CoherentCapitalAndTauxOnCreatePartCed implements ConstraintValidator<CoherentCapitalAndTaux, CreatePartCedRepartitionReq> {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(CreatePartCedRepartitionReq dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            if (dto.getAffId() == null) return true;
            return dto.getRepCapital().multiply(new BigDecimal(100)).divide(affRepo.getCapitalInitial(dto.getAffId())).equals(dto.getRepTaux()) ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class CoherentCapitalAndTauxOnCreate implements ConstraintValidator<CoherentCapitalAndTaux, CreateRepartitionReq> {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(CreateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            if (dto.getAffId() == null) return true;
            return dto.getRepCapital().multiply(new BigDecimal(100)).divide(affRepo.getCapitalInitial(dto.getAffId())).equals(dto.getRepTaux()) ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class CoherentCapitalAndTauxOnUpdate implements ConstraintValidator<CoherentCapitalAndTaux, UpdateRepartitionReq>
    {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(UpdateRepartitionReq dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            if (dto.getAffId() == null) return true;
            return dto.getRepCapital().multiply(new BigDecimal(100)).divide(affRepo.getCapitalInitial(dto.getAffId())).equals(dto.getRepTaux()) ;
        }
    }
}
