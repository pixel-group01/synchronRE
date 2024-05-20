package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IServiceCedanteTraite
{
    CedanteTraiteResp create(CedanteTraiteReq dto);

    @Transactional
    CedanteTraiteResp update(CedanteTraiteReq dto);

    @Transactional
    CedanteTraiteResp save(CedanteTraiteReq dto);

    Page<CedanteTraiteResp> search(Long traiId, String key, Pageable pageable);

    @Transactional
    void removeCedanteOnTraite(Long cedanteTraiteId);

    CedanteTraiteReq getEditDto(Long cedanteTraiteId);

    CedanteTraiteReq getEditDto(Long traiteNpId,Long cedId);
    CedanteTraiteReq getEditDto(Long cedanteTraiteId, Long traiteNpId,Long cedId);


    List<CedanteTraiteResp> getCedanteTraitelist(Long traiteNpId);
}
