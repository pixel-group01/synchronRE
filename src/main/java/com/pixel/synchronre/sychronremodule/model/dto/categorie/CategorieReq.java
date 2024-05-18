package com.pixel.synchronre.sychronremodule.model.dto.categorie;

import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class CategorieReq
{
    private Long categorieId;
    @NotNull(message = "Veuillez saisir nom de la catégorie")
    private String categorieLibelle;
    private BigDecimal categorieCapacite;
    @NotNull(message = "L'ID du traité ne peut être null") @ExistingTNPId
    private Long traiteNpId;
    private List<Long> cedIds;
}
