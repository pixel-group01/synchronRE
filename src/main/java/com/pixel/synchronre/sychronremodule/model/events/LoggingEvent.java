package com.pixel.synchronre.sychronremodule.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LoggingEvent extends ApplicationEvent
{
    private String action;
    private Object oldObject;
    private Object newObject;
    private String tableName;
    public LoggingEvent(Object source, String action, Object oldObject, Object newObject, String tableName) {
        super(source);
        this.action = action;
        this.oldObject = oldObject;
        this.newObject = newObject;
        this.tableName = tableName;
    }

}
