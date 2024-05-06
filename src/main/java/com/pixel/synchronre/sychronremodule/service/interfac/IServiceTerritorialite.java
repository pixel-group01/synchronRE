package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceTerritorialite {
    TerritorialiteResp create(TerritorialiteReq dto) throws UnknownHostException;

    TerritorialiteResp update(TerritorialiteReq dto) throws UnknownHostException;

    Page<TerritorialiteResp> search(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, PageRequest of);
}
