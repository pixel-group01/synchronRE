package com.pixel.synchronre.sychronRe.service.interfac;

import com.pixel.synchronre.sychronRe.model.dto.request.StatutReq;
import com.pixel.synchronre.sychronRe.model.entities.Statut;

import java.net.UnknownHostException;

public interface StatutIservice {
    void save(StatutReq dto) throws UnknownHostException;
}
