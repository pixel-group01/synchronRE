package com.pixel.synchronre.sychronremodule.model.events;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class CedanteTraiteEvent extends ApplicationEvent
{
    private String action;
    private CedanteTraite cedanteTraite;
    private List<CesLeg> cesLegDtos;

    public CedanteTraiteEvent(Object source, String action, CedanteTraite cedanteTraite, List<CesLeg> cesLegDtos)
    {
        super(source);
        this.action = action;
        this.cedanteTraite = cedanteTraite;
        this.cesLegDtos = cesLegDtos;
    }
}
