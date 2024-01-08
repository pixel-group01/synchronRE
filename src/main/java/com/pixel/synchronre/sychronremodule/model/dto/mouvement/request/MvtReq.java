package com.pixel.synchronre.sychronremodule.model.dto.mouvement.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MvtReq
{
    private String action;
    private Long objectId;
    private String staCode;
    private String mvtObservation;
}
//log_details_id_gen
