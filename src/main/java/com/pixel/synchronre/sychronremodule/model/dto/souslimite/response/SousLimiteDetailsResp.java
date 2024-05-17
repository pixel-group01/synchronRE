package com.pixel.synchronre.sychronremodule.model.dto.souslimite.response;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SousLimiteDetailsResp
{
    private Long sousLimiteSouscriptionId;
    private BigDecimal sousLimMontant;
    private Long sslimiteRisqueCouvertId;
    private String sslimiteRisqueCouvertLibelle;
    private Long traiteNpId;
    private String sslimiteTraiteReference;
    private String sslimiteTraiteNumero;
    private String sslimiteTraiLibelle;
    private Long sslimiteTrancheId;
    private String sslimiteTrancheLibelle;
    private String sslimiteStaCode;
    private String sslimiteStaLibelle;
}
