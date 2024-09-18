package com.pixel.synchronre.sychronremodule.model.dto.limitesouscription;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LimiteSouscriptionResp
{
    private Long limiteSouscriptionId;
    private BigDecimal limSousMontant;
    private Long risqueId;
    private String risqueDescription;
    private Long couId;
    private String couLibelle;
    private String couLibelleAbrege;
    private Long categorieId;
    private String categorieLibelle;
    private BigDecimal categorieCapacite;
    private List<ReadCedanteDTO> cedantes;
    private Long traiteNpId;
    private String traiReference;
    private String traiNumero;

    public LimiteSouscriptionResp(Long limiteSouscriptionId, BigDecimal limSousMontant, Long risqueId, String risqueDescription, Long couId, String couLibelle, String couLibelleAbrege, Long categorieId, String categorieLibelle, BigDecimal categorieCapacite, Long traiteNpId, String traiReference, String traiNumero) {
        this.limiteSouscriptionId = limiteSouscriptionId;
        this.limSousMontant = limSousMontant;
        this.risqueId = risqueId;
        this.risqueDescription = risqueDescription;
        this.couId = couId;
        this.couLibelle = couLibelle;
        this.couLibelleAbrege = couLibelleAbrege;
        this.categorieId = categorieId;
        this.categorieLibelle = categorieLibelle;
        this.categorieCapacite = categorieCapacite;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
    }
}
