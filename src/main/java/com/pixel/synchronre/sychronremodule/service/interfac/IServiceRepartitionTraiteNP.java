package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.model.events.CedanteTraiteEvent;
import com.pixel.synchronre.sychronremodule.model.events.SimpleEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;

public interface IServiceRepartitionTraiteNP
{
    RepartitionTraiteNPResp save(PlacementTraiteNPReq dto);

    Page<RepartitionTraiteNPResp> search(Long traiteNPId, String key, Pageable pageable);

    RepartitionTraiteNPResp create(PlacementTraiteNPReq dto);

    RepartitionTraiteNPResp update(PlacementTraiteNPReq dto);
    void createRepartitionCesLegTraite(CesLeg cesLeg, Long cedTraiId);
    void updateRepartitionCesLegTraite(CesLeg cesLeg, Long cedTraiId);

    void setMontantsPrimes(Long traiteNpId, BigDecimal repTaux, BigDecimal tauxCoutier, BigDecimal tauxCourtierPlaceur, Repartition repartition);

    void desactivateCesLegByTraiteNpIdAndPclId(Long traiteNpId, Long paramCesLegalId);

    void setMontantPrimesForCesLegRep(CesLeg cesLeg, Repartition repartition);
}
