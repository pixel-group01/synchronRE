package com.pixel.synchronre.sychronremodule.model.dto.souslimite.response;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SousLimiteDetailsResp
{
    private Long sousLimiteSouscriptionId;
    private BigDecimal sousLimMontant;
    private Long sslimiteCouvertId;
    private String sslimiteCouvertLibelle;
    private Long traiteNpId;
    private String sslimiteTraiteReference;
    private String sslimiteTraiteNumero;
    private String sslimiteTraiLibelle;
    private String sslimiteStaCode;
    private String sslimiteStaLibelle;
}
