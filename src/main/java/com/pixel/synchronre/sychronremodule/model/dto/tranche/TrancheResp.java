package com.pixel.synchronre.sychronremodule.model.dto.tranche;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class TrancheResp
{
    private Long trancheId;
    private String trancheType;
    private String trancheLibelle;
    private BigDecimal tranchePriorite;
    private BigDecimal tranchePorte;
    private BigDecimal trancheTauxPrime;
    private Long trancheNumero;
    private String risques;
    private String couvertures;
    private Long traiteNpId;
    private String traiReference;
    private String traiNumero;

    private List<CategorieResp> categories;
    private List<Long> risqueIds;
    private List<Long> couIds;

    public TrancheResp(Long trancheId, String trancheType, String trancheLibelle, BigDecimal tranchePriorite, BigDecimal tranchePorte, BigDecimal trancheTauxPrime, Long trancheNumero, Long traiteNpId, String traiReference, String traiNumero) {
        this.trancheId = trancheId;
        this.trancheType = trancheType;
        this.trancheLibelle = trancheLibelle;
        this.tranchePriorite = tranchePriorite;
        this.tranchePorte = tranchePorte;
        this.trancheTauxPrime = trancheTauxPrime;
        this.trancheNumero = trancheNumero;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
    }
}