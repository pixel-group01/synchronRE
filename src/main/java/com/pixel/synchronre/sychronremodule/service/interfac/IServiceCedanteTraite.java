package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import org.springframework.transaction.annotation.Transactional;

public interface IServiceCedanteTraite {
    CedanteTraiteResp create(CedanteTraiteReq dto);

    @Transactional
    CedanteTraiteResp update(CedanteTraiteReq dto);

    @Transactional
    CedanteTraiteResp save(CedanteTraiteReq dto);
}
