package com.pixel.synchronre.authmodule.model.dtos.asignation;

import com.pixel.synchronre.authmodule.model.dtos.appfunction.UpdateFncDTO;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CoherentDates.CoherentDatesValidatorOnCreate.class,
        CoherentDates.CoherentDatesValidatorOnUpdate.class,
        CoherentDates.CoherentSinistreDatesValidatorOnCreate.class,
        CoherentDates.CoherentSinistreDatesValidatorOnUpdate.class})
public @interface CoherentDates
{
    String message() default "dates::La date de début ne peut être ultérieure à la date de fin";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnCreate implements ConstraintValidator<CoherentDates, CreateFunctionDTO>
    {
        @Override
        public boolean isValid(CreateFunctionDTO dto, ConstraintValidatorContext context)
        {
            return dto.getStartsAt() == null || dto.getEndsAt() == null ? true : dto.getStartsAt().isBefore(dto.getEndsAt()) || dto.getStartsAt().isEqual(dto.getEndsAt());
        }
    }

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnUpdate implements ConstraintValidator<CoherentDates, UpdateFncDTO>
    {
        @Override
        public boolean isValid(UpdateFncDTO dto, ConstraintValidatorContext context)
        {
            return dto.getStartsAt() == null || dto.getEndsAt() == null ? true : dto.getStartsAt().isBefore(dto.getEndsAt()) || dto.getStartsAt().isEqual(dto.getEndsAt());
        }
    }



    @Component @RequiredArgsConstructor
    class CoherentSinistreDatesValidatorOnCreate implements ConstraintValidator<CoherentDates, CreateSinistreReq>
    {
        @Override
        public boolean isValid(CreateSinistreReq dto, ConstraintValidatorContext context)
        {
            LocalDate dateSurvenance = dto.getSinDateSurvenance();
            LocalDate dateDeclaration = dto.getSinDateDeclaration();
            return dateSurvenance == null || dateDeclaration == null ? true : dateSurvenance.isBefore(dateDeclaration) || dateSurvenance.isEqual(dateDeclaration);
        }
    }

    @Component @RequiredArgsConstructor
    class CoherentSinistreDatesValidatorOnUpdate implements ConstraintValidator<CoherentDates, UpdateSinistreReq>
    {
        @Override
        public boolean isValid(UpdateSinistreReq dto, ConstraintValidatorContext context)
        {
            LocalDate dateSurvenance = dto.getSinDateSurvenance();
            LocalDate dateDeclaration = dto.getSinDateDeclaration();
            return dateSurvenance == null || dateDeclaration == null ? true : dateSurvenance.isBefore(dateDeclaration) || dateSurvenance.isEqual(dateDeclaration);
        }
    }
}
