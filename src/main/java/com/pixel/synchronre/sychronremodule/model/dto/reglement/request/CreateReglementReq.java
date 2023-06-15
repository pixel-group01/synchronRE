package com.pixel.synchronre.sychronremodule.model.dto.reglement.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.validator.SeuilRegMontant;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.validator.SeuilRegMontantSin;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.validator.UniqueReference;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator.ExistingSinId;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRegMontant(groups = {REG_AFF_GROUP.class})
@SeuilRegMontantSin(groups = {REG_SIN_GROUP.class})
public class CreateReglementReq
{
    @NotBlank(message = "Veuillez saisir la réference du paiement")
    @NotNull(message = "Veuillez saisir la réference du paiement")
    @UniqueReference
    private String regReference;

    @NotNull(message = "Veuillez saisir le montant du paiement")
    @PositiveOrZero(message = "Le montant du paiement doit être un nombre positif")
    private BigDecimal regMontant;


    private String regMontantTemp;

    private String regMontantLettre;

    @NotNull(message = "Veuillez saisir la date du paiement")
    @PastOrPresent(message = "Veuiilez saisir une date antérieur à aujourd'hui")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate regDate;

    private MultipartFile regRecu;
    @ExistingAffId(groups = {REG_AFF_GROUP.class})
    @NotNull(message = "Veuillez choisir l'affaire", groups = {REG_AFF_GROUP.class})
    private Long affId;

    @ExistingSinId(groups = {REG_SIN_GROUP.class})
    @NotNull(message = "Veuillez choisir le sinistre", groups = {REG_SIN_GROUP.class})
    private Long sinId;

    @NotBlank(message = "Veuillez selectionner le mode reglement")
    @NotNull(message = "Veuillez selectionner le mode reglement")
    private String regMode;

    private Long cesId;

    private List<RegDocReq> regDocReqs;
}


