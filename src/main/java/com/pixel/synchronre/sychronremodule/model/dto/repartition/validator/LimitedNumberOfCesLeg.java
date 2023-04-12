package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateCesLegReq;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LimitedNumberOfCesLeg.LimitedNumberOfCesLegValidator.class})
@Documented
public @interface LimitedNumberOfCesLeg
{
    String message() default "Impossible de faire plus cessions légales sur cette affaire";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class LimitedNumberOfCesLegValidator implements ConstraintValidator<LimitedNumberOfCesLeg, CreateCesLegReq> {
        private final RepartitionRepository repRepo;
        private final ParamCessionLegaleRepository pclRepo;
        @Override
        public boolean isValid(CreateCesLegReq dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            if (dto.getAffId() == null) return true;
            return repRepo.countCesLegByAffaire(dto.getAffId()) < pclRepo.countByAffId(dto.getAffId());
        }
    }
}
