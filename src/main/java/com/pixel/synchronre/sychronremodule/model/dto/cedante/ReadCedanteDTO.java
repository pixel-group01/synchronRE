package com.pixel.synchronre.sychronremodule.model.dto.cedante;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadCedanteDTO
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
    private Long cesId;
    private String cesNom;
    private String cesSigle;
    public ReadCedanteDTO(Long cedId, String cedNomFiliale, String cedSigleFiliale, String cedTel, String cedEmail, String cedAdressePostale, String cedFax, String cedSituationGeo, String cedStatut) {
        this.cedId = cedId;
        this.cedNomFiliale = cedNomFiliale;
        this.cedSigleFiliale = cedSigleFiliale;
        this.cedTel = cedTel;
        this.cedEmail = cedEmail;
        this.cedAdressePostale = cedAdressePostale;
        this.cedFax = cedFax;
        this.cedSituationGeo = cedSituationGeo;
        this.cedStatut = cedStatut;
    }
}
