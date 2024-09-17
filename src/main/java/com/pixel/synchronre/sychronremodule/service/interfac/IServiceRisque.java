package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.CreateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceRisque
{
    RisqueCouvertResp create(CreateRisqueCouvertReq dto) throws UnknownHostException;
    RisqueCouvertResp update(UpdateRisqueCouvertReq dto) throws UnknownHostException;
    Page<RisqueCouvertResp> search(Long traiteNpId, String key, Pageable pageable);

    boolean delete(Long risqueId) throws UnknownHostException;

    UpdateRisqueCouvertReq getEditDto(Long risqueId);

    List<RisqueCouvertResp> getRisqueList(Long traiteNpId);
    List<ActivitesResp> getActivites(Long risqueId);

    List<RisqueCouvertResp> getCouvertureParent(Long traiteNpId);
}
