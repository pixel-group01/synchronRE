package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.CreateSousLimiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IServiceSousLimite {

    SousLimiteDetailsResp create(CreateSousLimiteReq dto) throws UnknownHostException;

    Page<SousLimiteDetailsResp> search(String key, Long fncId, Long userId, Pageable pageable);

    SousLimiteDetailsResp update(UpdateSousLimite dto) throws  UnknownHostException;
}
