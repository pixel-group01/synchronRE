package com.pixel.synchronre.sychronremodule.model.dto.tranche;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class TrancheResp
{
    private Long trancheId;
    private String trancheLibelle;
    private BigDecimal tranchePriorite;
    private BigDecimal tranchePorte;
    private Long risqueId;
    private String risqueDescription;
    private Long couId;
    private String couLibelle;
    private String couLibelleAbrege;
    private Long traiId;
    private String traiReference;
    private String traiNumero;
}

