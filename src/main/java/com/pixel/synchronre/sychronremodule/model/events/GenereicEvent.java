package com.pixel.synchronre.sychronremodule.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GenereicEvent <T> extends ApplicationEvent
{
    private T data;
    private String action;
    public GenereicEvent(Object source, T data, String action) {
        super(source);
        this.data = data;
        this.action = action;
    }

}
