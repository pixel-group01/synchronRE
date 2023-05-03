package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.ExistingCesId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.REG_AFF_GROUP;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.REG_SIN_GROUP;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.validator.SeuilRegMontant;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.validator.SeuilRegMontantSin;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.*;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator.ExistingSinId;
import com.pixel.synchronre.typemodule.model.dtos.ExistingTypeId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRegMontant(groups = {REG_AFF_GROUP.class})
@SeuilRegMontantSin(groups = {REG_SIN_GROUP.class})
public class UpdateRepartitionReq
{
    @ExistingRepId
    private Long repId;
    private BigDecimal repCapital;
    private BigDecimal repTaux;
    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    @Max(value = 100)
    @SeuilRepTauBesoinFac
    private BigDecimal repTauxBesoinFac;
    private BigDecimal repSousCommission;
    private String repInterlocuteur;

    @ExistingAffId(groups = {REG_AFF_GROUP.class})
    @NotNull(message = "Veuillez choisir l'affaire", groups = {REG_AFF_GROUP.class})
    private Long affId;

    @ExistingSinId(groups = {REG_SIN_GROUP.class})
    @NotNull(message = "Veuillez choisir le sinistre", groups = {REG_SIN_GROUP.class})
    private Long sinId;

    @ExistingCesId
    private Long cesId;
    @ExistingParamCesLegId
    private Long paramCesLegalId;
    @ExistingTypeId
    @NotNull(message = "Veuillez choisir le type de la répartition")
    private Long typId;
}
