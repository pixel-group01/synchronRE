package com.pixel.synchronre.sychronremodule.model.dto.organisation;

import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class OrganisationDTO
{
    private String organisationCode;
    private String organisationLibelle;
    private String staCode;
    private String staLibelle;
    private List<String> paysCodes;
    private List<PaysListResp> paysList;

    public OrganisationDTO(String organisationCode, String organisationLibelle, String staCode, String staLibelle) {
        this.organisationCode = organisationCode;
        this.organisationLibelle = organisationLibelle;
        this.staCode = staCode;
        this.staLibelle = staLibelle;
    }
}
