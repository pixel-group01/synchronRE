package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
//@Builder
public class TraiteProportionnel extends Traite{
    private Long traiCapaciteSous;
    private BigDecimal traiQuotePartCede;
    private BigDecimal traiValeurPlein;
    private Long traiRetention;
    private Long traiNbrPleinCede;
    private BigDecimal traiEngagement;
    private String traiCommissionOriginale;// O ou N
    private BigDecimal traiTauxCommissionFix;
    private BigDecimal traiTauxCommissionMin;
    private BigDecimal traiTauxCommissionMax;
    private BigDecimal traiTauxSinistraliteMin;
    private BigDecimal traiTauxSinistraliteMax;
    private String traiReportSin;
    private BigDecimal traiTauxEpp;
    private BigDecimal traiTauxRpp;
    private BigDecimal traiTauxDepotPrime;
    private BigDecimal traiTauxInteretDepotPrime;
    private BigDecimal traiTauxTaxeDepotPrime;
    private BigDecimal traiModeCalcul;
    private BigDecimal traiTauxProvisionRec;
    private BigDecimal traiTauxEps;
    private BigDecimal traiTauxRps;


}
