package com.pixel.synchronre.sychronremodule.model.dto.categorie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategorieResp {
    private Long categorieId;
    private String categorieLibelle;
    private BigDecimal categorieCapacite;
    private Long traiteNPId;
}
