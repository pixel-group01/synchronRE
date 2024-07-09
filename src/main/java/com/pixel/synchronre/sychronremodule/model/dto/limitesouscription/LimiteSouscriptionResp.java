package com.pixel.synchronre.sychronremodule.model.dto.limitesouscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LimiteSouscriptionResp
{
    private Long limiteSouscriptionId;
    private BigDecimal limSousMontant;
    private Long risqueId;
    private String risqueDescription;
    private Long couId;
    private String couLibelle;
    private String couLibelleAbrege;
    private Long cedanteTraiteId;
    private Long cedId;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private Long traiteNpId;
    private String traiReference;
    private String traiNumero;
}
