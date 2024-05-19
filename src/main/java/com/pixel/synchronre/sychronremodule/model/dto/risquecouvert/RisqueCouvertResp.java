package com.pixel.synchronre.sychronremodule.model.dto.risquecouvert;

import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RisqueCouvertResp
{
    private Long risqueId;
    private Long couId;
    private String couLibelle;
    private String description;
    private Long traiteNpId;
    private  String traiReference;
    private String staCode;
    private String staLibelle;
    private List<CouvertureListResp> sousCouvertures;

    public RisqueCouvertResp(Long risqueId, Long couId, String couLibelle, String description, Long traiteNpId, String traiReference, String staCode, String staLibelle) {
        this.risqueId = risqueId;
        this.couId = couId;
        this.couLibelle = couLibelle;
        this.description = description;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.staCode = staCode;
        this.staLibelle = staLibelle;
    }
}
