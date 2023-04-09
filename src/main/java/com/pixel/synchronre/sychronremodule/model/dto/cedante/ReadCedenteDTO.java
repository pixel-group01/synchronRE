package com.pixel.synchronre.sychronremodule.model.dto.cedante;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadCedenteDTO
{
    private Long cedId;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private String cedTel;
    private String cedEmail;
    private String cedAdressePostale;
    private String cedFax;
    private String cedSituationGeo;
    private String cedStatut;
}
