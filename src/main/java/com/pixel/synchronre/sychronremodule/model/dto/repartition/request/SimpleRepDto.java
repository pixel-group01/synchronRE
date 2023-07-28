package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class SimpleRepDto
    {
        private BigDecimal repCapital;
        private BigDecimal repTaux;
        private Long repId;
    }