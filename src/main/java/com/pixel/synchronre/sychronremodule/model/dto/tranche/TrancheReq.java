package com.pixel.synchronre.sychronremodule.model.dto.tranche;

import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.ExistingRisqueId;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class TrancheReq
{
    private Long trancheId;
    @NotNull(message = "Veuillez selectionner le type de la tranche")
    @NotBlank(message = "Veuillez selectionner le type de la tranche")
    private String trancheType;
    @NotNull(message = "Veuillez saisir le nom de la tranche")
    @NotBlank(message = "Veuillez saisir le nom de la tranche")
    private String trancheLibelle;
    @NotNull(message = "Veuillez saisir la priorité de la tranche")
    private BigDecimal tranchePriorite;
    @NotNull(message = "Veuillez saisir la porté de la tranche")
    private BigDecimal tranchePorte;
    @NotNull(message = "Veuillez saisir le taux de prime")
    @Min(value = 0, message = "Le taux de prime doit être compris entre 0 et 100")
    @Max(value = 100, message = "Le taux de prime doit être compris entre 0 et 100")
    private BigDecimal trancheTauxPrime;
    private Long trancheNumero;
    private List<Long> risqueIds;
    @ExistingTNPId
    private Long traiteNpId;

    private List<Long> categorieIds;

    public TrancheReq(Long trancheId,String trancheType, String trancheLibelle, BigDecimal tranchePriorite, BigDecimal tranchePorte, BigDecimal trancheTauxPrime, Long trancheNumero, Long traiteNpId) {
        this.trancheId = trancheId;
        this.trancheType=trancheType;
        this.trancheLibelle = trancheLibelle;
        this.tranchePriorite = tranchePriorite;
        this.tranchePorte = tranchePorte;
        this.trancheTauxPrime = trancheTauxPrime;
        this.trancheNumero = trancheNumero;
        this.traiteNpId = traiteNpId;
    }
}
