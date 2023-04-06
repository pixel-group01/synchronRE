package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.request.StatutReq;

import java.net.UnknownHostException;

public interface StatutIservice {
    void save(StatutReq dto) throws UnknownHostException;
}
