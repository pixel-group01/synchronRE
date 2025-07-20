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
    private Long risqueId;
    private String risqueDescription;
    private Long couId;
    private String couLibelle;
    private String couLibelleAbrege;
    private Long traiteNpId;
    private String traiReference;
    private String traiNumero;
    private List<CategorieResp> categories;

    public TrancheResp(Long trancheId, String trancheType, String trancheLibelle, BigDecimal tranchePriorite, BigDecimal tranchePorte, BigDecimal trancheTauxPrime, Long trancheNumero, Long risqueId, String risqueDescription, Long couId, String couLibelle, String couLibelleAbrege, Long traiteNpId, String traiReference, String traiNumero) {
        this.trancheId = trancheId;
        this.trancheType = trancheType;
        this.trancheLibelle = trancheLibelle;
        this.tranchePriorite = tranchePriorite;
        this.tranchePorte = tranchePorte;
        this.trancheTauxPrime = trancheTauxPrime;
        this.trancheNumero = trancheNumero;
        this.risqueId = risqueId;
        this.risqueDescription = risqueDescription;
        this.couId = couId;
        this.couLibelle = couLibelle;
        this.couLibelleAbrege = couLibelleAbrege;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
    }
}