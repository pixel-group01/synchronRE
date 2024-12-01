package com.pixel.synchronre.sychronremodule.model.dto.compte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CompteCessionnaireDto
{
    private Long compteCesId;
    private Long cesId;
    private String cesNom;
    private String cesSigle;
    private BigDecimal taux;
    private BigDecimal prime;
    private Long compteCedId;

    public CompteCessionnaireDto(Long cesId, String cesNom, String cesSigle, BigDecimal taux) {
        this.cesId = cesId;
        this.cesNom = cesNom;
        this.cesSigle = cesSigle;
        this.taux = taux;
        this.prime = BigDecimal.ZERO;
    }
}
