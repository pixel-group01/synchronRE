package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.traite.request.CreateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceTraiteNP
{
    TraiteNPResp create(CreateTraiteNPReq dto) throws UnknownHostException;

    Page<TraiteNPResp> search(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable);

    TraiteNPResp update(UpdateTraiteNPReq dto) throws UnknownHostException;

    UpdateTraiteNPReq edit(Long traiId);
}

