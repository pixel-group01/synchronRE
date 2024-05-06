package com.pixel.synchronre.sychronremodule.model.dto.territorialite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateTerritorialiteReq
{
    private Long terrId;
    private String terrLibelle;
    private List<String> paysCodes;
    private String orgCode;
    private Long traiteNPId;
}