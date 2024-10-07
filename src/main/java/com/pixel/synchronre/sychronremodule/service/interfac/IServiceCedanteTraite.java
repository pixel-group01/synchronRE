package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto;
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

    CedanteTraiteReq getEditDto(CedanteTraiteReq dto, int scale);


    List<TranchePmdDto> getTranchePmdDtos(CedanteTraiteReq dto, int scale);

    TranchePmdDto calculatePmds(TranchePmdDto trPmd, CedanteTraiteReq dto, int scale);

    List<CedanteTraiteResp> getCedanteTraitelist(Long traiteNpId);

    List<ReadCedanteDTO> getListCedanteAsaisirSurTraite(Long traiteNpId);
}
