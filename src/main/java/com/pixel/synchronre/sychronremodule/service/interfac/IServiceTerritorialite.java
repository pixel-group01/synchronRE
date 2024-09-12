package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IServiceTerritorialite {
    TerritorialiteResp create(TerritorialiteReq dto);

    TerritorialiteResp update(TerritorialiteReq dto);

    Page<TerritorialiteResp> search(Long traiId, String key, Pageable pageable);

    TerritorialiteReq edit(Long terrId);
}
