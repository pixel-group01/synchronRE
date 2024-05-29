package com.pixel.synchronre.sychronremodule.model.dto.traite.response;

import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
import com.pixel.synchronre.sychronremodule.model.enums.PERIODICITE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TraiteNPResp
{
    private Long traiteNPId;
    private String traiReference;
    private String traiNumero;
    private String traiLibelle;
    private String traiAuteur;
    private String traiEcerciceRattachement;
    private LocalDate traiDateEffet;
    private LocalDate traiDateEcheance;
    private BigDecimal traiCoursDevise;
    private String traiPeriodicite;
    private Long traiDelaiEnvoi;
    private Long traiDelaiConfirmation;
    private BigDecimal traiTauxCourtier;
    private BigDecimal traiTauxCourtierPlaceur;
    private Long exeCode;
    private String traiSourceRef;
    private String traiSourceLibelle;
    private String natCode;
    private String natLibelle;
    private String devCode;
    private String traiCompteDevCode;
    private String traiStaCode;
    private String traiStaLibelle;

    protected String traiUserCreatorEmail;
    protected String traiUserCreatorNomPrenom;
    protected String traiFonCreatorName;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    private BigDecimal traiTauxDejaPlace;
    private BigDecimal traiTauxRestantAPlacer;
    private BigDecimal traiPmd;
    private BigDecimal traiPmdCourtier;
    private BigDecimal traiPmdCourtierPlaceur;
    private BigDecimal traiPmdNette;

    public TraiteNPResp(Long traiteNPId, String traiReference, String traiNumeroPolice) {
        this.traiteNPId = traiteNPId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumeroPolice;
    }

    public TraiteNPResp(Long traiteNPId, String traiReference, String traiNumeroPolice, String traiLibelle,
                        String traiAuteur, EXERCICE_RATTACHEMENT traiEcerciceRattachement,
                        LocalDate traiDateEffet, LocalDate traiDateEcheance,
                        BigDecimal traiCoursDevise, PERIODICITE traiPeriodicite,
                        Long traiDelaiEnvoi, Long traiDelaiConfirmation,
                        BigDecimal traiTauxCourtier, BigDecimal traiTauxCourtierPlaceur,
                        Long exeCode, String traiSourceRef, String traiSourceLibelle,
                        String natCode, String natLibelle, String devCode,
                        String traiCompteDevCode, String traiStaCode, String traiStaLibelle,
                        String traiUserCreatorEmail, String traiUserCreatorNomPrenom,
                        String traiFonCreatorName, LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.traiteNPId = traiteNPId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumeroPolice;
        this.traiLibelle = traiLibelle;
        this.traiAuteur = traiAuteur;
        this.traiEcerciceRattachement = traiEcerciceRattachement == null ? "" : traiEcerciceRattachement.getLibelle();
        this.traiDateEffet = traiDateEffet;
        this.traiDateEcheance = traiDateEcheance;
        this.traiCoursDevise = traiCoursDevise;
        this.traiPeriodicite = traiPeriodicite == null ? "" : traiPeriodicite.getLibelle();
        this.traiDelaiEnvoi = traiDelaiEnvoi;
        this.traiDelaiConfirmation = traiDelaiConfirmation;
        this.traiTauxCourtier = traiTauxCourtier;
        this.traiTauxCourtierPlaceur = traiTauxCourtierPlaceur;
        this.exeCode = exeCode;
        this.traiSourceRef = traiSourceRef;
        this.traiSourceLibelle = traiSourceLibelle;
        this.natCode = natCode;
        this.natLibelle = natLibelle;
        this.devCode = devCode;
        this.traiCompteDevCode = traiCompteDevCode;
        this.traiStaCode = traiStaCode;
        this.traiStaLibelle = traiStaLibelle;
        this.traiUserCreatorEmail = traiUserCreatorEmail;
        this.traiUserCreatorNomPrenom = traiUserCreatorNomPrenom;
        this.traiFonCreatorName = traiFonCreatorName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public TraiteNPResp(Long traiteNPId, String traiReference, String traiNumero, String traiLibelle, String traiAuteur,
                        EXERCICE_RATTACHEMENT traiEcerciceRattachement, LocalDate traiDateEffet, LocalDate traiDateEcheance,
                        BigDecimal traiCoursDevise, PERIODICITE traiPeriodicite, Long traiDelaiEnvoi, Long traiDelaiConfirmation,
                        BigDecimal traiTauxCourtier, BigDecimal traiTauxCourtierPlaceur, Long exeCode, String traiSourceRef,
                        String traiSourceLibelle, String natCode, String natLibelle, String devCode, String traiCompteDevCode,
                        BigDecimal traiPmd, BigDecimal traiPmdCourtier, BigDecimal traiPmdCourtierPlaceur, BigDecimal traiPmdNette)
    {
        this.traiteNPId = traiteNPId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
        this.traiLibelle = traiLibelle;
        this.traiAuteur = traiAuteur;
        this.traiEcerciceRattachement = traiEcerciceRattachement == null ? "" : traiEcerciceRattachement.getLibelle();
        this.traiDateEffet = traiDateEffet;
        this.traiDateEcheance = traiDateEcheance;
        this.traiCoursDevise = traiCoursDevise;
        this.traiPeriodicite = traiPeriodicite == null ? "" : traiPeriodicite.getLibelle();
        this.traiDelaiEnvoi = traiDelaiEnvoi;
        this.traiDelaiConfirmation = traiDelaiConfirmation;
        this.traiTauxCourtier = traiTauxCourtier;
        this.traiTauxCourtierPlaceur = traiTauxCourtierPlaceur;
        this.exeCode = exeCode;
        this.traiSourceRef = traiSourceRef;
        this.traiSourceLibelle = traiSourceLibelle;
        this.natCode = natCode;
        this.natLibelle = natLibelle;
        this.devCode = devCode;
        this.traiCompteDevCode = traiCompteDevCode;
        this.traiPmd = traiPmd;
        this.traiPmdCourtier = traiPmdCourtier;
        this.traiPmdCourtierPlaceur = traiPmdCourtierPlaceur;
        this.traiPmdNette = traiPmdNette;
    }
}
