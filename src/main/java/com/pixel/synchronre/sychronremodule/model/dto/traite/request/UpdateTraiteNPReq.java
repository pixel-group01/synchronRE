package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import com.pixel.synchronre.sychronremodule.model.entities.PERIODICITE;
import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class UpdateTraiteNPReq
{
    private Long traiteNPId;
    @NotNull(message = "La référence unique est obligatoire")
    @NotBlank(message = "La référence unique est obligatoire")
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
    private String traiSourceRef;
    private String natCode;
    private String devCode;
    private String traiCompteDevCode;

    public UpdateTraiteNPReq(Long traiteNPId, String traiReference,
                             String traiNumero, String traiLibelle,
                             String traiAuteur, EXERCICE_RATTACHEMENT traiEcerciceRattachement,
                             LocalDate traiDateEffet, LocalDate traiDateEcheance,
                             BigDecimal traiCoursDevise, PERIODICITE traiPeriodicite,
                             Long traiDelaiEnvoi, Long traiDelaiConfirmation,
                             BigDecimal traiTauxCourtier, BigDecimal traiTauxCourtierPlaceur,
                             String traiSourceRef, String natCode, String devCode,
                             String traiCompteDevCode) {
        this.traiteNPId = traiteNPId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
        this.traiLibelle = traiLibelle;
        this.traiAuteur = traiAuteur;
        this.traiEcerciceRattachement = traiEcerciceRattachement == null ? null : traiEcerciceRattachement.name();
        this.traiDateEffet = traiDateEffet;
        this.traiDateEcheance = traiDateEcheance;
        this.traiCoursDevise = traiCoursDevise;
        this.traiPeriodicite = traiPeriodicite == null ? null : traiPeriodicite.name();
        this.traiDelaiEnvoi = traiDelaiEnvoi;
        this.traiDelaiConfirmation = traiDelaiConfirmation;
        this.traiTauxCourtier = traiTauxCourtier;
        this.traiTauxCourtierPlaceur = traiTauxCourtierPlaceur;
        this.traiSourceRef = traiSourceRef;
        this.natCode = natCode;
        this.devCode = devCode;
        this.traiCompteDevCode = traiCompteDevCode;
    }
}