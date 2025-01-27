package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "v_tranche_cedante_nat")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VTrancheCedanteNat
{
    @Id
    private Long rId;
    private Long traiteNpId;
    private Long trancheId;
    private String trancheLibelle;
    private BigDecimal tranchePriorite;
    private BigDecimal tranchePorte;
    private BigDecimal trancheTauxPrime;
    private Long categorieId;
    private String categorieLibelle;
    private Long cedId;
    private String cedNomFiliale;
    private String cedSigleFiliale;
}
