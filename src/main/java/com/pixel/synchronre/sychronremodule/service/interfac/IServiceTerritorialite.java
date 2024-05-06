package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.territorialite.CreateTerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.UpdateTerritorialiteReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceTerritorialite {
    TerritorialiteResp create(CreateTerritorialiteReq dto) throws UnknownHostException;

    TerritorialiteResp update(UpdateTerritorialiteReq dto);

    Page<TerritorialiteResp> search(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, PageRequest of);
}
