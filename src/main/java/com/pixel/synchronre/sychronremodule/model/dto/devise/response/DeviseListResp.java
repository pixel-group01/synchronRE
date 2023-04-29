package com.pixel.synchronre.sychronremodule.model.dto.devise.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviseListResp
{
    private String devCode;
    private String devLibelle;
    private String devLibelleAbrege;
    private String devSymbole;
    private String staLibelle;
}
