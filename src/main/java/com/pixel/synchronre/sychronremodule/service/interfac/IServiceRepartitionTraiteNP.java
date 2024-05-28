package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServiceRepartitionTraiteNP
{
    RepartitionTraiteNPResp save(PlacementTraiteNPReq dto);

    Page<RepartitionTraiteNPResp> search(Long traiteNPId, String key, Pageable pageable);

    RepartitionTraiteNPResp create(PlacementTraiteNPReq dto);

    RepartitionTraiteNPResp update(PlacementTraiteNPReq dto);
    void createRepartitionCesLegTraite(CesLeg cesLeg, Long cedTraiId);
    void updateRepartitionCesLegTraite(CesLeg cesLeg, Long cedTraiId);
}
