package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CalculationRepartitionReqDto
{
    private Long affId;
    private BigDecimal repCapital;
    private BigDecimal repTaux;
    private Long repId;
    //private SimpleRepDto partCedante;
    private List<Long> pclIds;
    //private List<RepDto> traites;
    private Long repIdToBeModified;

    private BigDecimal retentionCapital;
    private BigDecimal retentionTaux;
    private Long retentionRepId;

    private BigDecimal xlCapital;
    private BigDecimal xlTaux;
    private Long xlRepId;

    private BigDecimal facobCapital;
    private BigDecimal facobTaux;
    private Long facobRepId;
}