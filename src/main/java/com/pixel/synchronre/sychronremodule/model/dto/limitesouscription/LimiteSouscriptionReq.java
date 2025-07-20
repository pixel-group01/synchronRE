package com.pixel.synchronre.sychronremodule.model.dto.limitesouscription;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.ExistingCategorieId;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.ExistingRisqueId;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.ExistingTrancheId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class LimiteSouscriptionReq
{
    private Long limiteSouscriptionId;
    private BigDecimal limSousMontant;
    @ExistingRisqueId
    private Long risqueId;
    @ExistingCategorieId
    private Long categorieId;
    private List<Long> couIds;
}
