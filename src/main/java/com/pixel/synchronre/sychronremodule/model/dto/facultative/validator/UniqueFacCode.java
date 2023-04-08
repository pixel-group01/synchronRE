//package com.pixel.synchronre.sychronremodule.model.dto.affaire.validator;
//
//import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
//import com.pixel.synchronre.sychronremodule.model.dto.affaire.request.UpdateAffairetReq;
//import jakarta.validation.Constraint;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import jakarta.validation.Payload;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.lang.annotation.*;
//
//@Target({ElementType.FIELD, ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = {UniqueAffCode.UniqueAffCodeValidatorOnCreate.class, UniqueAffCode.UniqueAffCodeValidatorOnUpdate.class})
//@Documented
//public @interface UniqueAffCode
//{
//    String message() default "Code statut déjà attribué";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//
//    @Component
//    @RequiredArgsConstructor
//    class UniqueAffCodeValidatorOnCreate implements ConstraintValidator<UniqueAffCode, String>
//    {
//        private final StatutRepository statRepo;
//        @Override
//        public boolean isValid(String value, ConstraintValidatorContext context) {
//            return !statRepo.alreadyExistsByCode(value);
//        }
//    }
//
//    @Component
//    @RequiredArgsConstructor
//    class UniqueAffCodeValidatorOnUpdate implements ConstraintValidator<UniqueAffCode, UpdateAffairetReq>
//    {
//        private final StatutRepository statRepo;
//        @Override
//        public boolean isValid(UpdateAffairetReq dto, ConstraintValidatorContext context) {
//            return !statRepo.alreadyExistsByCode(dto.getStaCode(), dto.getStaId());
//        }
//    }
//}
