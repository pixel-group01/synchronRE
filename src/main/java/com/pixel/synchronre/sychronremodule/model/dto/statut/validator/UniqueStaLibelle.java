package com.pixel.synchronre.sychronremodule.model.dto.statut.validator;

import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sychronremodule.model.dao.BanqueRepository;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.banque.validator.ExistingBanId;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.UpdateStatutReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Objects;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueStaLibelle.UniqueStaLibelleValidatorOnCreate.class, UniqueStaLibelle.UniqueStaLibelleValidatorOnUpdate.class})
@Documented
public @interface UniqueStaLibelle
{
    String message() default "staLibelle::Ce libelle existe déjà pour ce type de statut";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueStaLibelleValidatorOnCreate implements ConstraintValidator<UniqueStaLibelle, CreateStatutReq>
    {
        private final StatutRepository staRepo;
        @Override
        public boolean isValid(CreateStatutReq staReq, ConstraintValidatorContext context)
        {
            if( Arrays.stream(TypeStatut.values()).filter(Objects::nonNull).noneMatch(type->type.name().equals(staReq.getStaType()))) return true;
            return !staRepo.alreadyExistsByLibelleAndType(staReq.getStaLibelle(), TypeStatut.valueOf(staReq.getStaType()));
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueStaLibelleValidatorOnUpdate implements ConstraintValidator<UniqueStaLibelle, UpdateStatutReq>
    {
        private final StatutRepository staRepo;
        @Override
        public boolean isValid(UpdateStatutReq staReq, ConstraintValidatorContext context)
        {
            if( Arrays.stream(TypeStatut.values()).filter(Objects::nonNull).noneMatch(type->type.name().equals(staReq.getStaType()))) return true;
            return !staRepo.alreadyExistsByLibelleAndType(staReq.getStaLibelle(), TypeStatut.valueOf(staReq.getStaType()), staReq.getStaCode());
        }
    }
}
