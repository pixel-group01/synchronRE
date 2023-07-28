package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau @CoherentCapitalAndTaux @ValidPclId @ValidRepCesLegId
public class UpdateCesLegReq
{
    @ExistingRepId
    private Long repId;
    @NotNull(message = "Veuillez saisir le capital")
    @PositiveOrZero(message = "Le capital doit être un nombre positif")
    private BigDecimal repCapital;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    private BigDecimal repTaux;

    @ExistingAffId @NotNull(message = "Veuillez choisir l'affaire'")
    private Long affId;
    @ExistingParamCesLegId @NotNull(message = "Veuillez choisir le paramétrage de la cession légale")
    private Long paramCesLegalId;
    private boolean accepte;
    private String paramCesLegLibelle;

    public UpdateCesLegReq(ParamCessionLegaleListResp pcl, Long repId, BigDecimal repCapital, Long affId, boolean  accepte)
    {
        this.repId = repId;
        this.repCapital = repCapital;
        this.affId = affId;
        if(pcl != null) {
            this.repTaux = pcl.getParamCesLegTaux();
            this.paramCesLegalId = pcl.getParamCesLegId();
            this.accepte = accepte;
            this.paramCesLegLibelle = pcl.getParamCesLegLibelle();
        }
    }
}