package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.UpdateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;

public interface IserviceRepartition {
    RepartitionDetailsResp createRepartition(CreateRepartitionReq dto) throws UnknownHostException;

    //RepartitionDetailsResp createCesLegRepartition(CreateCesLegReq dto) throws UnknownHostException;

    List<RepartitionDetailsResp> createCesLegRepartitions(List<CreateCesLegReq> dtos) throws UnknownHostException;

    RepartitionDetailsResp createPartCedRepartition(CreatePartCedRepartitionReq dto) throws UnknownHostException;

    RepartitionDetailsResp createCedLegRepartition(CreateCedLegRepartitionReq dto) throws UnknownHostException;

    RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto) throws UnknownHostException;

    RepartitionDetailsResp updateRepartition(UpdateRepartitionReq dto) throws UnknownHostException;
    Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, Pageable pageable);

    CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital);
    CalculRepartitionResp calculateRepByTaux(Long affId, BigDecimal taux);
    CalculRepartitionResp calculateRepByTauxBesoinFac(Long affId, BigDecimal tauxBesoin);

    void deletePlacement(Long repId) throws UnknownHostException;
}
