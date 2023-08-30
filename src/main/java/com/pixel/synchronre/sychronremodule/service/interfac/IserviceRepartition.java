package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculationRepartitionRespDto;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;

public interface IserviceRepartition
{
    RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto) throws UnknownHostException;

    Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, List<String> staCodes, Pageable pageable);

    void deletePlacement(Long repId) throws UnknownHostException;

    List<ParamCessionLegaleListResp> getCesLegParam(Long affId);

    @Transactional
    void transmettrePlacementPourValidation(Long plaId) throws UnknownHostException;

    @Transactional
    void transmettrePlacementPourValidation(List<Long> plaIds) throws UnknownHostException;

    @Transactional
    void retournerPlacement(Long plaId, String motif) throws UnknownHostException;

    @Transactional
    void validerPlacement(Long plaId) throws UnknownHostException;

    @Transactional
    void transmettreNoteDeCession(List<Long> plaId);

    @Transactional
    void transmettreNoteDeCession(Long plaId) throws Exception;

    @Transactional
    void refuserPlacement(Long plaId, String motif) throws UnknownHostException;

    @Transactional
    void annulerPlacement(Long plaId) throws UnknownHostException;

    @Transactional
    void accepterPlacement(Long plaId) throws UnknownHostException;

    void validerPlacement(List<Long> plaIds);
}
