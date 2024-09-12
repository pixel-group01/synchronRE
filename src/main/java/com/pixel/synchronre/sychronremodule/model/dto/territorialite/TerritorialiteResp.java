package com.pixel.synchronre.sychronremodule.model.dto.territorialite;

import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TerritorialiteResp
{
    private Long terrId;
    private String terrLibelle;
    private BigDecimal terrTaux;
    private String terrDescription;
    private List<PaysListResp> paysList;
    private String organisationList;
    private Long traiteNpId;
    private String traiReference;
    private String traiNumeroPolice;
    private String traiLibelle;
    private Long cesNom;

    public TerritorialiteResp(Long terrId, String terrLibelle, BigDecimal terrTaux, String terrDescription, Long traiteNPId, String traiReference) {
        this.terrId = terrId;
        this.terrLibelle = terrLibelle;
        this.terrTaux = terrTaux;
        this.terrDescription = terrDescription;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
    }
}