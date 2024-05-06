package com.pixel.synchronre.sychronremodule.model.dto.territorialite;

import com.pixel.synchronre.sychronremodule.model.dto.organisation.OrganisationDTO;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TerritorialiteResp
{
    private Long terrId;
    private String terrLibelle;
    private BigDecimal terrTaux;
    private String terrDescription;
    private List<PaysListResp> paysList;
    private String organisationList;
    private Long traiteNPId;
    private String traiReference;
    private String traiNumeroPolice;
    private String traiLibelle;
    private String traiAuteur;
}