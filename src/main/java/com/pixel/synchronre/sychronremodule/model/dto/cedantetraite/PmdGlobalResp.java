package com.pixel.synchronre.sychronremodule.model.dto.cedantetraite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PmdGlobalResp
{
    private BigDecimal traiPmd;
    private BigDecimal traiPmdCourtier;
    private BigDecimal traiPmdCourtierPlaceur;
    private BigDecimal traiPmdNette;
}
