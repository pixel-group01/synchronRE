package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CalculationRepartitionReqDto;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.SimpleRepDto;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateCesLegReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CalculationRepartitionRespDto
{
    private Long affId;
    private BigDecimal repCapital;
    private BigDecimal repTaux;
    private Long repId;
    //private SimpleRepDto partCedante;
    private List<UpdateCesLegReq> paramCesLegs;
    private BigDecimal besoinFac;
    private BigDecimal besoinFacRestant;
    //private List<RepDto> traites;
}