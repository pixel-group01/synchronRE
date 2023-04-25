package com.pixel.synchronre.sychronremodule.model.dto.repartition.validator;

import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
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
@Constraint(validatedBy = {ValidPclId.ValidPclIdValidator.class})
@Documented
public @interface ValidPclId
{
    String message() default "paramCesLegalId::Impossible d'appliquer les paramètres de cession légale d'un pays à une affaire d'un autre pays";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ValidPclIdValidator implements ConstraintValidator<ValidPclId, CreateCesLegReq> {
        private final ParamCessionLegaleRepository pclRepo;
        @Override
        public boolean isValid(CreateCesLegReq dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            if (dto.getAffId() == null) return true;
            return pclRepo.existsByPclIdAndAffaire(dto.getParamCesLegalId(), dto.getAffId());
        }
    }
}
