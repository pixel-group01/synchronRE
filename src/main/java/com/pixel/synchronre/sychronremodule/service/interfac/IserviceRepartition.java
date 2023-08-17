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

public interface IserviceRepartition {
    RepartitionDetailsResp createRepartition(CreateRepartitionReq dto) throws UnknownHostException;

    //RepartitionDetailsResp createCesLegRepartition(CreateCesLegReq dto) throws UnknownHostException;

    List<RepartitionDetailsResp> createCesLegRepartitions(List<CreateCesLegReq> dtos) throws UnknownHostException;

    RepartitionDetailsResp createPartCedRepartition(CreatePartCedRepartitionReq dto) throws UnknownHostException;

    RepartitionDetailsResp createCedLegRepartition(CreateCedLegRepartitionReq dto) throws UnknownHostException;
    RepartitionDetailsResp updateCedLegRepartition(UpdateCedLegRepartitionReq dto) throws UnknownHostException;

    RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto) throws UnknownHostException;

    RepartitionDetailsResp updateRepartition(UpdateRepartitionReq dto) throws UnknownHostException;
    Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, List<String> staCodes, Pageable pageable);

    /*CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude);
    CalculRepartitionResp calculateRepByTaux(Long affId, BigDecimal taux, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude);

    BigDecimal calculateSommeCapitauxCessionsLegalesPremierFranc(Long affId);

    BigDecimal calculateCapitauxNetCL(Long affId);

    BigDecimal calculateSommeCapitauxTraites(Long affId);

    BigDecimal calculateBesoinFacBrut(Long affId);

    BigDecimal calculateTauxBesoinFacBrut(Long affId);

    BigDecimal calculateSommeCapitauxCLSimple(Long affId);

    BigDecimal calculateBesoinFacNetCL(Long affId);

    CalculRepartitionResp calculateRepByTauxBesoinFac(Long affId, BigDecimal tauxBesoin, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude);

    CalculationRepartitionRespDto saveRep(CalculationRepartitionRespDto dto) throws UnknownHostException;

    CalculationRepartitionRespDto calculateRepByAffId(Long affId);

    CalculationRepartitionRespDto calculateRepByDto(CalculationRepartitionRespDto dto);

    CalculationRepartitionRespDto calculateRepByDto(CalculationRepartitionReqDto dto);
    UpdateCedLegRepartitionReq getUpdateCedLegDTO(Long affId);
*/

    void deletePlacement(Long repId) throws UnknownHostException;

    CreateCedLegRepartitionReq getCedLegRepartitionDTO(Long affId);

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
    Repartition modifierPlacement(UpdatePlaRepartitionReq dto) throws UnknownHostException;

    @Transactional
    void accepterPlacement(Long plaId) throws UnknownHostException;

    void validerPlacement(List<Long> plaIds);
}
