package com.pixel.synchronre.sychronremodule.model.dto.banque.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BanqueDetailsResp
{
    private Long banId;
    private String banCode;
    private String banNumCompte;
    private String banIban;
    private String banCodeBic;
    private String banLibelle;
    private String banLibelleAbrege;
}
