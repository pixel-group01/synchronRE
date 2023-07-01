package com.pixel.synchronre.sychronremodule.model.dto.pays.response;

import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaysDetailsResp
{
    private String paysCode;
    private String paysIndicatif;
    private String paysNom;
    private String devCode;
}
