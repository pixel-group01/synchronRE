package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionReq;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServiceLimiteSouscription
{
    LimiteSouscriptionResp save(LimiteSouscriptionReq dto);

    boolean delete(Long limiteSouscriptionId);

    Page<LimiteSouscriptionResp> search(Long traiId, String key, Pageable pageable);

    LimiteSouscriptionResp create(LimiteSouscriptionReq dto);

    LimiteSouscriptionResp update(LimiteSouscriptionReq dto);

    LimiteSouscriptionReq edit(Long limiteSouscriptionId);
}