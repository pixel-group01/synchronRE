package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface IServiceRepartitionTraiteNP
{
    RepartitionTraiteNPResp save(PlacementTraiteNPReq dto);
    Page<RepartitionTraiteNPResp> search(Long traiteNPId, String key, Pageable pageable);
    RepartitionTraiteNPResp create(PlacementTraiteNPReq dto);
    RepartitionTraiteNPResp update(PlacementTraiteNPReq dto);

    @Transactional
    void saveRepartitionCesLegTraite(CesLeg cesLeg);

    void createRepartitionCesLegTraite(CesLeg cesLeg);
    void updateRepartitionCesLegTraite(CesLeg cesLeg);
    void setMontantsPrimes(Long traiteNpId, BigDecimal repTaux, BigDecimal tauxCoutier, BigDecimal tauxCourtierPlaceur, Repartition repartition);
    void desactivateCesLegByTraiteNpIdAndPclId(Long traiteNpId, Long paramCesLegalId);
    void setMontantPrimesForCesLegRep(CesLeg cesLeg, Repartition repartition);

    Repartition recalculateMontantPrimeOnPlacement(PlacementTraiteNPReq dto, Repartition placement);

    void recalculateMontantPrimeForPlacementOnTraite(Long traiteNpId);
}
