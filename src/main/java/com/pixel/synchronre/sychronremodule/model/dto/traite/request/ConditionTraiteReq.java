package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ConditionTraiteReq
{
    private Long conditionId;
    private boolean conditionExclusion;
    private Long traiteId;
    private Long cedId;
    private Long couId;
    private Long typeId;
}