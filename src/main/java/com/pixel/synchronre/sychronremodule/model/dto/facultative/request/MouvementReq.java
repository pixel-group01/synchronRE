package com.pixel.synchronre.sychronremodule.model.dto.facultative.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.statut.validator.ExistingStatCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MouvementReq {
    @ExistingAffId
    @NotNull(message = "Veuillez choisir une affaire")
    private Long affId;
    @NotBlank(message = "Veuillez choisir le statut")
    @NotNull(message = "Veuillez choisir le statut")
    @ExistingStatCode
    private String staCode;
    //private String mvtObservation;
}
