package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.entities.HistoDetails;

public interface IHistoDetailsService
{
    HistoDetails getActionIdentifierFromSecurityContext(String actionName);
}
