package com.pixel.synchronre.sychronremodule.model.dto.couverture.response;


import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CouvertureListResp
{
    private Long couId;
    private String couLibelle;
    private String couLibelleAbrege;
    private Long couParentId;
    private String couParentLibelle;
    private Long branId;
    private String brancheLibelle;
    private String staLibelle;

    public CouvertureListResp(Long couId, String couLibelle, String couLibelleAbrege, Long couParentId, String couParentLibelle, Long branId, String brancheLibelle, String staLibelle) {
        this.couId = couId;
        this.couLibelle = couLibelle;
        this.couLibelleAbrege = couLibelleAbrege;
        this.couParentId = couParentId;
        this.couParentLibelle = couParentLibelle;
        this.branId = branId;
        this.brancheLibelle = brancheLibelle;
        this.staLibelle = staLibelle;
    }

    public CouvertureListResp(Long couId, String couLibelle, String couLibelleAbrege)
    {
        this.couId = couId;
        this.couLibelle = couLibelle;
        this.couLibelleAbrege = couLibelleAbrege;
    }
}
