package com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SinistreNotTooLate.SeuilSinMontantValidatorOnCreate.class,
        SinistreNotTooLate.SeuilSinMontantValidatorOnUpdate.class})
@Documented
public @interface SinistreNotTooLate
{
    String message() default "sinDateSurvenance::La date de survenance du sinistre est n'est pas prise en charge par les termes du contrat";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class SeuilSinMontantValidatorOnCreate implements ConstraintValidator<SinistreNotTooLate, CreateSinistreReq>
    {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(CreateSinistreReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            Affaire aff = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire inconnue"));
            LocalDate dateSurvenance = dto.getSinDateSurvenance();
            LocalDate dateEffet = aff.getAffDateEffet();
            LocalDate dateEch = aff.getAffDateEcheance();
            if(dateSurvenance == null) return true;
            if(dateEffet == null && dateEch == null) return true;
            if(dateEffet == null) return dateSurvenance.isBefore(dateEch);
            if(dateEch == null) return dateSurvenance.isAfter(dateEffet);
            return  dateSurvenance.isAfter(dateEffet) && dateSurvenance.isBefore(dateEch);
        }
    }

    @Component
    @RequiredArgsConstructor
    class SeuilSinMontantValidatorOnUpdate implements ConstraintValidator<SinistreNotTooLate, UpdateSinistreReq>
    {
        private final AffaireRepository affRepo;
        @Override
        public boolean isValid(UpdateSinistreReq dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            Affaire aff = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire inconnue"));
            LocalDate dateSurvenance = dto.getSinDateSurvenance();
            LocalDate dateEffet = aff.getAffDateEffet();
            LocalDate dateEch = aff.getAffDateEcheance();
            if(dateSurvenance == null) return true;
            if(dateEffet == null && dateEch == null) return true;
            if(dateEffet == null) return dateSurvenance.isBefore(dateEch);
            if(dateEch == null) return dateSurvenance.isAfter(dateEffet);
            return  dateSurvenance.isAfter(dateEffet) && dateSurvenance.isBefore(dateEch);
        }
    }
}
