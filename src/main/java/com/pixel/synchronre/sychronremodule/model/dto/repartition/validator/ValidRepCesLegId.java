package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateCesLegReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateCesLegReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidRepCesLegId.ValidRepCesIdValidator.class})
@Documented
public @interface ValidRepCesLegId
{
    String message() default "Veuillez selectionner un paramètre de cession légal valide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidRepCesIdValidator implements ConstraintValidator<ValidRepCesLegId, UpdateCesLegReq> {
        private final RepartitionRepository repRepo;
        @Override
        public boolean isValid(UpdateCesLegReq dto, ConstraintValidatorContext context)
        {
            if (dto == null || dto.getAffId() == null) return true;
            return repRepo.existsByRepIdAndAffIdAndTypeRep(dto.getRepId(), dto.getAffId(), "REP_CES_LEG");
        }
    }
}
