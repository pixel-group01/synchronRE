package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Table(name = "v_stat_compte")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VStatCompte
{
    @Id
    private Long rId;
    private Long cedId;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private Long trancheCedanteId;
    private BigDecimal assiettePrime;
    private BigDecimal pmd;
    private BigDecimal pmdNette;
    private Long trancheId;
    private String trancheLibelle;
    private BigDecimal trancheTauxPrime;
    private BigDecimal pmdSurBaseAssiettePrime;
    private Long periodeId;
    private String periode;
    private String periodeName;
    private Long traiteNpId;
    private BigDecimal primeOrigine;
    private Long compteDetId;
    private Long compteCedId;
    private BigDecimal assiettePrimeExercice;
    private BigDecimal pmdDejaPaye;
    private BigDecimal ecartPmd;
    private BigDecimal primeAjustee;
    private BigDecimal surplusPrime;
    private BigDecimal totalPmdSurBaseAssiettePrime;
    private BigDecimal totalEcartPmd;
    private BigDecimal repartitionSurplusPmd;
    private BigDecimal pmdExervice;
    private BigDecimal repartitionSurplusPmdParCedante;
    private BigDecimal pmdExerviceParCed;
    private Long exeCode;
}
