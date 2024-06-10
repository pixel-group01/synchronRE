package com.pixel.synchronre.sychronremodule.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SimpleEvent<T> extends ApplicationEvent
{
    private String action;
    private T data;

    public SimpleEvent(Object source, String action, T data) {
        super(source);
        this.action = action;
        this.data = data;
    }

}
