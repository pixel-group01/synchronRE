package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IServiceTranche
{
    TrancheResp save(TrancheReq dto);

    boolean delete(Long trancheId);

    Page<TrancheResp> search(Long traiId, String key, Pageable pageable);

    TrancheResp create(TrancheReq dto);

    @Transactional
    int getNextTrancheNum(Long traiteNpId);

    TrancheResp update(TrancheReq dto);


    TrancheReq edit(Long trancheId);

    List<TrancheResp> getTrancheList(Long traiteNpId);
}
