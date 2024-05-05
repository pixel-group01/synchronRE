package com.pixel.synchronre.sychronremodule.model.dto.organisation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdatePaysOrgDTO
{
    private String organisationLibelle;
    private String orgCode;
    private List<String> paysCodes;
}
