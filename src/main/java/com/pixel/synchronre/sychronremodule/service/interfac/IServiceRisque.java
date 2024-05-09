package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.CreateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IServiceRisque
{
    RisqueCouvertResp create(CreateRisqueCouvertReq dto) throws UnknownHostException;
    RisqueCouvertResp update(UpdateRisqueCouvertReq dto) throws UnknownHostException;
    Page<RisqueCouvertResp> search(Long traiId, String key, Pageable pageable);

    boolean delete(Long risqueId) throws UnknownHostException;
}