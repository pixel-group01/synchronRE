package com.pixel.synchronre.sychronremodule.model.dto.traite.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TauxCourtiersResp
{
    private BigDecimal traiTauxCourtier;
    private BigDecimal traiTauxCourtierPlaceur;
}
