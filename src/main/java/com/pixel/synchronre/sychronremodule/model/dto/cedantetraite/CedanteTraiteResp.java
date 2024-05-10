package com.pixel.synchronre.sychronremodule.model.dto.cedantetraite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CedanteTraiteResp
{
    private Long cedanteTraiteId;
    private BigDecimal assiettePrime;
    private BigDecimal tauxPrime;
    private BigDecimal pmd;
    private Long cedId;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private Long traiId;
    private String traiReference;
    private String traiNumero;
    private String staCode;
    private String staLibelle;

    private List<CesLeg> cessionsLegales;

}
