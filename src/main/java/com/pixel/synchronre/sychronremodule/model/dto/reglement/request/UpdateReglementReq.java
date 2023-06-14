package com.pixel.synchronre.sychronremodule.model.dto.reglement.request;

import com.pixel.synchronre.authmodule.model.dtos.appuser.ExistingUserId;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.UniqueCedTel;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
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
@UniqueReference(message = "regReference::Cette réference existe déja")
public class UpdateReglementReq {

    private Long regId;
    @NotBlank(message = "Veuillez saisir la réference du paiement")
    @NotNull(message = "Veuillez saisir la réference du paiement")
    private String regReference;

    @NotNull(message = "Veuillez saisir le montant du paiement")
    @PositiveOrZero(message = "Le montant du paiement doit être un nombre positif")
    private BigDecimal regMontant;

    private String regMontantLettre;

    @NotNull(message = "Veuillez saisir la date du paiement")
    @FutureOrPresent(message = "Veuiilez saisir une date ultérieure à aujourd'hui")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate regDate;

    @NotBlank(message = "Veuillez selectionner le mode reglement")
    @NotNull(message = "Veuillez selectionner le mode reglement")
    private String regMode;

}
