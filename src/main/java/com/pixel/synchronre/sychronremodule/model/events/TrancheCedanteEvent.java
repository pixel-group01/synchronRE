package com.pixel.synchronre.sychronremodule.model.events;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.TrancheCedanteReq;
import com.pixel.synchronre.sychronremodule.model.entities.TrancheCedante;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TrancheCedanteEvent extends ApplicationEvent
{
    private String action;
    private TrancheCedante trancheCedante;
    private TrancheCedanteReq dto;

    public TrancheCedanteEvent(Object source, String action, TrancheCedante trancheCedante, TrancheCedanteReq dto)
    {
        super(source);
        this.action = action;
        this.trancheCedante = trancheCedante;
        this.dto = dto;
    }
}
