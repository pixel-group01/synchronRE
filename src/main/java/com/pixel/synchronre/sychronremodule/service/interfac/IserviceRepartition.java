package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.UpdateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.net.UnknownHostException;

public interface IserviceRepartition {
    RepartitionDetailsResp createRepartition(CreateRepartitionReq dto) throws UnknownHostException;
    RepartitionDetailsResp updateRepartition(UpdateRepartitionReq dto) throws UnknownHostException;
    Page<RepartitionListResp> searchRepartition(String key, Pageable pageable);

    CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital);
    CalculRepartitionResp calculateRepByTaux(Long affId, BigDecimal taux);
    CalculRepartitionResp calculateRepByTauxBesoinFac(Long affId, BigDecimal tauxBesoin);

}
