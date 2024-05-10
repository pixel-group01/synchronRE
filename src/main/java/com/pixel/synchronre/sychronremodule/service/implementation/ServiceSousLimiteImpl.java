package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.CreateSousLimiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSousLimite;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class ServiceSousLimiteImpl implements IServiceSousLimite {
    @Override
    public SousLimiteDetailsResp create(CreateSousLimiteReq dto) throws UnknownHostException {
        return null;
    }

    @Override
    public Page<SousLimiteDetailsResp> search(String key, Long sslimiteTraiteNPId, String sslimiteRisqueCouvertLibelle, Pageable pageable) {
        return null;
    }

    @Override
    public SousLimiteDetailsResp update(UpdateSousLimite dto) throws UnknownHostException {
        return null;
    }
}
