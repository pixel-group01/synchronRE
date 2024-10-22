package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;

public interface IserviceRepartition
{
    RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto);

    Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, List<String> staCodes, Pageable pageable);

    void deletePlacement(Long repId);

    List<ParamCessionLegaleListResp> getCesLegParam(Long affId);

    @Transactional
    void transmettrePlacementPourValidation(Long plaId);

    @Transactional
    void transmettrePlacementPourValidation(List<Long> plaIds);

    @Transactional
    void retournerPlacement(Long plaId, String motif);

    @Transactional
    void validerPlacement(Long plaId);

    @Transactional
    void transmettreNoteDeCession(List<Long> plaId);

    @Transactional
    void transmettreNoteDeCession(Long plaId) throws Exception;

    @Transactional
    void refuserPlacement(Long plaId, String motif);

    @Transactional
    void annulerPlacement(Long plaId);

    @Transactional
    void accepterPlacement(Long plaId);

    void validerPlacement(List<Long> plaIds);

    @Transactional
    void annulerRepartition(Long repId);

    void doRepartitionSinistre(Affaire aff, Long sinId, CessionnaireListResp ces);

    void saveReserveCourtier(Long affId, BigDecimal facSmp, BigDecimal prime100, BigDecimal reserveCourtier);
}
