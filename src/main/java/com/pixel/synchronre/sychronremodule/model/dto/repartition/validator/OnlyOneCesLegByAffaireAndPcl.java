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
@Constraint(validatedBy = {OnlyOneCesLegByAffaireAndPcl.OnlyOneCesLegByAffaireAndPclValidator.class})
@Documented
public @interface OnlyOneCesLegByAffaireAndPcl
{
    String message() default "paramCesLegalId::Impossible de faire plus d'une répartition sur la même affaire avec les mêmes paramètres de cession légale";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class OnlyOneCesLegByAffaireAndPclValidator implements ConstraintValidator<OnlyOneCesLegByAffaireAndPcl, CreateCesLegReq> {
        private final RepartitionRepository repRepo;
        @Override
        public boolean isValid(CreateCesLegReq dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            if (dto.getAffId() == null) return true;
            return !repRepo.repExistsByAffaireAndPcl(dto.getAffId(), dto.getParamCesLegalId());
        }
    }
}
