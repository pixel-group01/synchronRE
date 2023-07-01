package com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParamCessionLegaleListResp
{
    private Long paramCesLegId;
    private String paramCesLegLibelle;
    private BigDecimal paramCesLegCapital;
    private BigDecimal paramCesLegTaux;
    private String paysNom;
    private String staLibelle;
    private String paysCode;
    private Long numOrdre;

    public ParamCessionLegaleListResp(Long paramCesLegId, String paramCesLegLibelle, BigDecimal paramCesLegCapital, BigDecimal paramCesLegTaux, String paysNom, String paysCode) {
        this.paramCesLegId = paramCesLegId;
        this.paramCesLegLibelle = paramCesLegLibelle;
        this.paramCesLegCapital = paramCesLegCapital;
        this.paramCesLegTaux = paramCesLegTaux;
        this.paysNom = paysNom;
        this.paysCode = paysCode;
    }
}
