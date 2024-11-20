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
    private String paysNom;
    private String paysCode;
    private String banNumCompte;
    private String banIban;
    private String banCodeBic;
    private String banLibelle;
    private String banLibelleAbrege;

    public ReadCedanteDTO(Long cedId, String cedNomFiliale, String cedSigleFiliale) {
        this.cedId = cedId;
        this.cedNomFiliale = cedNomFiliale;
        this.cedSigleFiliale = cedSigleFiliale;
    }

    @Getter @Setter @NoArgsConstructor
    public static class ReadCedanteDTOLite
    {
        private Long cedId;
        private String cedNomFiliale;
        private String cedSigleFiliale;

        public ReadCedanteDTOLite(Long cedId, String cedNomFiliale, String cedSigleFiliale) {
            this.cedId = cedId;
            this.cedNomFiliale = cedNomFiliale;
            this.cedSigleFiliale = cedSigleFiliale;
        }
    }
}
