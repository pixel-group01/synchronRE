package com.pixel.synchronre.sychronremodule.model.dto.tranche;

import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.ExistingRisqueId;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Veuillez saisir le nom de la tranche")
    @NotBlank(message = "Veuillez saisir le nom de la tranche")
    private String trancheLibelle;
    @NotNull(message = "Veuillez saisir la priorité de la tranche")
    private BigDecimal tranchePriorite;
    @NotNull(message = "Veuillez saisir la porté de la tranche")
    private BigDecimal tranchePorte;
    @ExistingRisqueId
    private Long risqueId;
    @ExistingTNPId
    private Long traiteNpId;
    private List<Long> categorieIds;

    public TrancheReq(Long trancheId, String trancheLibelle, BigDecimal tranchePriorite, BigDecimal tranchePorte, Long risqueId, Long traiteNpId) {
        this.trancheId = trancheId;
        this.trancheLibelle = trancheLibelle;
        this.tranchePriorite = tranchePriorite;
        this.tranchePorte = tranchePorte;
        this.risqueId = risqueId;
        this.traiteNpId = traiteNpId;
    }
}
