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
    private Long sslimiteTraiteNpId;
    private String sslimiteTraiLibelle;
    private Long sslimiteTrancheId;
    private String sslimiteTrancheLibelle;
    private String sslimiteStaCode;
    private String sslimiteStaLibelle;
    protected String sslimiteUserCreatorEmail;
    protected String sslimiteUserCreatorNomPrenom;
    protected String sslimiteFonCreatorName;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
