package com.pixel.synchronre.sychronremodule.model.dto.categorie;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class CategorieReq {
    private String categorieLibelle;
    private List<String> paysCodes;
    private BigDecimal categorieCapacite;
    @NotNull(message = "L'ID du traité ne peut être null")
    private Long traiteNPId;
}
