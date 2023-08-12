package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

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
    private BigDecimal mtPartCedante;
    private BigDecimal tauxPartCedante;
    private BigDecimal primePartCedante;
    private List<UpdateCesLegReq> paramCesLegsPremierFranc;
    private BigDecimal capitauxNetCL;

    private BigDecimal conservationCapital;
    private BigDecimal conservationTaux;
    private BigDecimal conservationPrime;
    private Long conservationRepId;

    private BigDecimal xlCapital;
    private BigDecimal xlTaux;
    private BigDecimal xlPrime;
    private Long xlRepId;

    private BigDecimal facobCapital;
    private BigDecimal facobTaux;
    private BigDecimal facobPrime;
    private Long facobRepId;

    private BigDecimal bruteBesoinFac;
    private BigDecimal bruteBesoinFacTaux;
    private BigDecimal bruteBesoinFacPrime;


    private List<UpdateCesLegReq> paramCesLegs;

    private BigDecimal besoinFacNetCL;
    private BigDecimal besoinFacNetCLTaux;
    private BigDecimal besoinFacNetCLPrime;


    private BigDecimal besoinFacRestant;
    private BigDecimal besoinFac;
    private boolean modeUpdate;
}