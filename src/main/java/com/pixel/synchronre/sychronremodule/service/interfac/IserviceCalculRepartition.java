package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CalculationRepartitionReqDto;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculationRepartitionRespDto;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.UnknownHostException;

public interface IserviceCalculRepartition {
    @Transactional
    CalculationRepartitionRespDto saveRep(CalculationRepartitionRespDto dto) throws UnknownHostException;

    CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude);

    CalculRepartitionResp calculateRepByTaux(Long affId, BigDecimal taux, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude);

    BigDecimal calculateSommeCapitauxCessionsLegalesPremierFranc(Long affId);

    BigDecimal calculateCapitauxNetCL(Long affId);

    BigDecimal calculateSommeCapitauxTraites(Long affId);

    BigDecimal calculateBesoinFacBrut(Long affId);

    BigDecimal calculateTauxBesoinFacBrut(Long affId);

    BigDecimal calculateSommeCapitauxCLSimple(Long affId);

    BigDecimal calculateBesoinFacNetCL(Long affId);

    CalculRepartitionResp calculateRepByTauxBesoinFac(Long affId, BigDecimal tauxBesoin, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude);

    CalculationRepartitionRespDto calculateRepByAffId(Long affId);

    CalculationRepartitionRespDto calculateRepByDto(CalculationRepartitionRespDto dto);

    CalculationRepartitionRespDto calculateRepByDto(CalculationRepartitionReqDto dto);
}
