package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TraiteReq
{
    private Long tariteId;
    private String traiteCode;
    private String traiteLibelle;
    private BigDecimal traiteTaux;
    private BigDecimal smpMax;
    private BigDecimal smpMin;
    private BigDecimal capitalMax;
    private BigDecimal capitalMin;
    private LocalDate dateEffet;
    private LocalDate dateEcheance;
    //private Long typeId;
    private List<ConditionTraiteReq> conditions;
}
