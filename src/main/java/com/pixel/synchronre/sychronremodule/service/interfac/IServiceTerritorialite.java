package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IServiceTerritorialite {
    TerritorialiteResp create(TerritorialiteReq dto) throws UnknownHostException;

    TerritorialiteResp update(TerritorialiteReq dto) throws UnknownHostException;

    Page<TerritorialiteResp> search(Long traiId, String key, Pageable pageable);

    TerritorialiteReq edit(Long terrId);
}
