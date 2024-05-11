package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServiceTranche
{
    TrancheResp save(TrancheReq dto);

    boolean delete(Long trancheId);

    Page<TrancheResp> search(Long traiId, String key, Pageable pageable);

    TrancheResp create(TrancheReq dto);

    TrancheResp update(TrancheReq dto);
}
