package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateCedLegRepartitionReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidRepCedId.ValidRepCedIdValidator.class})
@Documented
public @interface ValidRepCedId
{
    String message() default "L'ID de la répartition de type part cedante est invalide ou ne correspond pas à l'affaire";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidRepCedIdValidator implements ConstraintValidator<ValidRepCedId, UpdateCedLegRepartitionReq> {
        private final RepartitionRepository repRepo;
        @Override
        public boolean isValid(UpdateCedLegRepartitionReq dto, ConstraintValidatorContext context)
        {
            if (dto == null || dto.getRepId() == null || dto.getAffId() == null) return true;
            return repRepo.existsByRepIdAndAffIdAndTypeRep( dto.getRepId(),dto.getAffId(), "REP_CED");
        }
    }
}