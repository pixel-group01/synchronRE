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
    private Long traiteNpId;
    private String traiReference;
    private String traiNumero;
    private String staCode;
    private String staLibelle;
    private BigDecimal pmdCourtier;
    private BigDecimal pmdCourtierPlaceur;
    private BigDecimal pmdNette;

    private List<CesLeg> cessionsLegales;

    public CedanteTraiteResp(Long cedanteTraiteId, BigDecimal assiettePrime, BigDecimal tauxPrime, BigDecimal pmd,
                             Long cedId, String cedNomFiliale, String cedSigleFiliale, Long traiteNpId,
                             String traiReference, String traiNumero, String staCode, String staLibelle) {
        this.cedanteTraiteId = cedanteTraiteId;
        this.assiettePrime = assiettePrime;
        this.tauxPrime = tauxPrime;
        this.pmd = pmd;
        this.cedId = cedId;
        this.cedNomFiliale = cedNomFiliale;
        this.cedSigleFiliale = cedSigleFiliale;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
        this.staCode = staCode;
        this.staLibelle = staLibelle;
    }

    public CedanteTraiteResp(Long cedanteTraiteId, BigDecimal assiettePrime, BigDecimal tauxPrime, BigDecimal pmd,
                             Long cedId, String cedNomFiliale, String cedSigleFiliale, Long traiteNpId,
                             String traiReference, String traiNumero, String staCode, String staLibelle, BigDecimal pmdCourtier,
                             BigDecimal pmdCourtierPlaceur, BigDecimal pmdNette)
    {
        this.cedanteTraiteId = cedanteTraiteId;
        this.assiettePrime = assiettePrime;
        this.tauxPrime = tauxPrime;
        this.pmd = pmd;
        this.cedId = cedId;
        this.cedNomFiliale = cedNomFiliale;
        this.cedSigleFiliale = cedSigleFiliale;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
        this.staCode = staCode;
        this.staLibelle = staLibelle;
        this.pmdCourtier = pmdCourtier;
        this.pmdCourtierPlaceur = pmdCourtierPlaceur;
        this.pmdNette = pmdNette;
    }
}
