package com.pixel.synchronre.sychronremodule.model.dto.tranche;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TranchePrimeDto
{
    private Long trancheCedanteId;
    private Long trancheId;
    private String trancheLibelle;
    private BigDecimal assiettePrime;
    private BigDecimal assiettePrimeRealisee;
    private BigDecimal trancheTauxPrime;
    private BigDecimal pmd;
    private BigDecimal pmdCourtier;
    private BigDecimal pmdCourtierPlaceur;
    private BigDecimal pmdNette;
    private Long cedId;
    private Long traiteNpId;
    private boolean changed;
    private List<CesLeg> cessionsLegales;

    public TranchePrimeDto(Long trancheId, String trancheLibelle, BigDecimal assiettePrime, BigDecimal trancheTauxPrime) {
        this.trancheId = trancheId;
        this.trancheLibelle = trancheLibelle;
        this.assiettePrime = assiettePrime;
        this.trancheTauxPrime = trancheTauxPrime;
    }

    public TranchePrimeDto(Long trancheId, String trancheLibelle, BigDecimal trancheTauxPrime) {
        this.trancheId = trancheId;
        this.trancheLibelle = trancheLibelle;
        this.trancheTauxPrime = trancheTauxPrime;
    }
}
