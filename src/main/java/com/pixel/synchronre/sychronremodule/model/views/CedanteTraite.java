package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "v_cedante_traite")
@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CedanteTraite
{
    @Id
    private Long rowNum;
    private Long cedId;
    private String cedNomFiliale;
    private Long traiteNpId;
    private String traiNumero;
    
    private BigDecimal assiettePrime;
    private BigDecimal pmd;
    private BigDecimal pmdCourtier;
    private BigDecimal pmdCourtierPlaceur;
    private BigDecimal pmdNette;
}