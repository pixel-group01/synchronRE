package com.pixel.synchronre.sychronremodule.model.events;

import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TrancheEvent extends ApplicationEvent
{
    private String action;
    private Tranche tranche;

    public TrancheEvent(Object source, String action, Tranche tranche)
    {
        super(source);
        this.action = action;
        this.tranche = tranche;
    }
}
