package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
//@Builder
public class TraiteNonProportionnel extends  Traite{
    private BigDecimal traiAssiettePrime;
    private BigDecimal traiEngagement;
    private BigDecimal traiPorte;
    private BigDecimal traiTauxPorte;
    private BigDecimal traiPriorite;
    private BigDecimal traiTauxPriorite;
    private BigDecimal traiFranAgre;
    private String traiBaseCalcul;
    private String traiPrimeAdd;
    private String traiClauseStab;
    private Long traiTauxSeuil;
    private BigDecimal traiTauxPrimeMin;
    private BigDecimal traiTauxPrimeMax;
    private BigDecimal traiTauxCotation;
    private String traiNumDocCotation;
    private String traiEtatCotation;
    private String traiTypePrimeNp;
    private LocalDate traiPmdEcheance1;
    private BigDecimal traiTauxPmdEcheance1;
    private LocalDate traiPmdEcheance2;
    private BigDecimal traiTauxPmdEcheance2;
    private LocalDate traiPmdEcheance3;
    private BigDecimal traiTauxPmdEcheance3;
    private LocalDate traiPmdEcheance4;
    private BigDecimal traiTauxPmdEcheance4;

}
