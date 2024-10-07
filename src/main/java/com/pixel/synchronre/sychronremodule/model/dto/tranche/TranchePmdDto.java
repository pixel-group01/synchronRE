package com.pixel.synchronre.sychronremodule.model.dto.tranche;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TranchePmdDto
{
    private Long trancheCedanteId;
    private Long trancheId;
    private String trancheLibelle;
    private BigDecimal trancheTauxPrime;
    private BigDecimal pmd;
    private BigDecimal pmdCourtier;
    private BigDecimal pmdCourtierPlaceur;
    private BigDecimal pmdNette;

    public TranchePmdDto(Long trancheId, String trancheLibelle, BigDecimal trancheTauxPrime) {
        this.trancheId = trancheId;
        this.trancheLibelle = trancheLibelle;
        this.trancheTauxPrime = trancheTauxPrime;
    }
}
