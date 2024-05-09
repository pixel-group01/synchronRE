package com.pixel.synchronre.sychronremodule.model.dto.risquecouvert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateRisqueCouvertReq
{
    private Long risqueId;
    private Long couId;
    private String description;
    private Long activiteId;
}