package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface IServiceCedanteTraite
{
    CedanteTraiteResp create(CedanteTraiteReq dto);

    @Transactional
    CedanteTraiteResp update(CedanteTraiteReq dto);

    @Transactional
    CedanteTraiteResp save(CedanteTraiteReq dto);

    Page<CedanteTraiteResp> search(Long traiId, String key, Pageable pageable);
}
