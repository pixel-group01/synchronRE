package com.pixel.synchronre.sychronremodule.model.dto.reglement.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.validator.SeuilRegMontant;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.validator.UniqueReference;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRegMontant(groups = {CREATE_GROUP.class})
public class CreateReglementReq {

    @NotBlank(message = "Veuillez saisir la réference du paiement")
    @NotNull(message = "Veuillez saisir la réference du paiement")
    @UniqueReference
    private String regReference;

    @NotNull(message = "Veuillez saisir le montant du paiement")
    @PositiveOrZero(message = "Le montant du paiement doit être un nombre positif")
    private BigDecimal regMontant;

    @NotNull(message = "Veuillez saisir la date du paiement")
    @FutureOrPresent(message = "Veuiilez saisir une date ultérieure à aujourd'hui")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate regDate;

    //private String typeReglement;

    @ExistingAffId
    private Long affId;
}
