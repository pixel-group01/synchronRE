package com.pixel.synchronre.sychronremodule.model.dto.affaire.response;

import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AffaireListResp
{
    private Long staId;
    private String staCode;
    private String staLibelle;
    private String staLibelleLong;
    private String staType;

    public AffaireListResp(Long staId, String staCode, String staLibelle, String staLibelleLong, TypeStatut typeStatut) {
        this.staId = staId;
        this.staCode = staCode;
        this.staLibelle = staLibelle;
        this.staLibelleLong = staLibelleLong;
        this.staType =typeStatut==null ? "" : typeStatut.name();
    }
}
