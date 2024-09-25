package com.pixel.synchronre.sychronremodule.model.dto.categorie;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategorieResp
{
    private Long categorieId;
    private String categorieLibelle;
    private BigDecimal categorieCapacite;
    private Long traiteNpId;
    private String traiReference;
    private String traiNumero;
    private String libellesCedantes;
    private List<ReadCedanteDTO> cedantes;

    public CategorieResp(Long categorieId, String categorieLibelle, BigDecimal categorieCapacite, Long traiteNpId, String traiReference, String traiNumero) {
        this.categorieId = categorieId;
        this.categorieLibelle = categorieLibelle;
        this.categorieCapacite = categorieCapacite;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
    }
}
