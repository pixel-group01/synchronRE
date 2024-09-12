package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import com.pixel.synchronre.sychronremodule.model.dto.devise.validator.ExistingDevCode;
import com.pixel.synchronre.sychronremodule.model.dto.nature.validator.ExistingNatCode;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTraiteNpRef;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.UniqueTraiteNpRef;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.UniqueTraiteNumero;
import com.pixel.synchronre.sychronremodule.model.enums.PERIODICITE;
import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
import com.pixel.synchronre.sychronremodule.model.enums.validators.ValidPeriodicite;
import com.pixel.synchronre.sychronremodule.model.enums.validators.ValidRattachement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@UniqueTraiteNpRef @UniqueTraiteNumero
@ExistingTraiteNpRef(message = "La référence du traité source est introuvable")
public class UpdateTraiteNPReq
{
    private Long traiteNpId;
    @NotNull(message = "La référence unique est obligatoire")
    @NotBlank(message = "La référence unique est obligatoire")
    private String traiReference;
    @NotNull(message = "Veuillez saisir le numéro du traité")
    @NotBlank(message = "Veuillez saisir le numéro du traité")
    private String traiNumero;
    private String traiLibelle;
    private String traiAuteur;
    @NotNull(message = "Veuillez saisir l'exercice de rattachement")
    @ValidRattachement
    private String traiEcerciceRattachement;
    @NotNull(message = "Veuillez saisir la date d'effet")
    private LocalDate traiDateEffet;
    @NotNull(message = "Veuillez saisir la date d'échéance")
    private LocalDate traiDateEcheance;
    private BigDecimal traiCoursDevise;
    @NotNull(message = "Veuillez saisir la périodicité")
    @ValidPeriodicite
    private String traiPeriodicite;
    private Long traiDelaiEnvoi;
    private Long traiDelaiConfirmation;
    private Long traiDelaiPaiement;
    private BigDecimal traiTauxCourtier;
    private BigDecimal traiTauxCourtierPlaceur;
    private BigDecimal traiTauxAbattement;
    private String traiSourceRef;
    @ExistingNatCode
    private String natCode;
    @ExistingDevCode(message = "La dévise du traité est introuvable")
    @NotNull(message = "Veuillez saisir la divise du traité")
    private String devCode;
    @ExistingDevCode(message = "La dévise des comptes sur le traité est introuvable")
    @NotBlank(message = "Veuillez saisir la divise des comptes")
    private String traiCompteDevCode;
    @NotNull(message = "Veuillez sélectionner le courtier placeur")
    private Long courtierPlaceurId;

    public UpdateTraiteNPReq(Long traiteNpId, String traiReference,
                             String traiNumero, String traiLibelle,
                             EXERCICE_RATTACHEMENT traiEcerciceRattachement,
                             LocalDate traiDateEffet, LocalDate traiDateEcheance,
                             BigDecimal traiCoursDevise, PERIODICITE traiPeriodicite,
                             Long traiDelaiEnvoi, Long traiDelaiConfirmation,Long traiDelaiPaiement,
                             BigDecimal traiTauxCourtier, BigDecimal traiTauxCourtierPlaceur,BigDecimal traiTauxAbattement,
                             String traiSourceRef, String natCode, String devCode,
                             String traiCompteDevCode,Long courtierPlaceurId) {
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
        this.traiLibelle = traiLibelle;
        this.traiEcerciceRattachement = traiEcerciceRattachement == null ? null : traiEcerciceRattachement.name();
        this.traiDateEffet = traiDateEffet;
        this.traiDateEcheance = traiDateEcheance;
        this.traiCoursDevise = traiCoursDevise;
        this.traiPeriodicite = traiPeriodicite == null ? null : traiPeriodicite.name();
        this.traiDelaiEnvoi = traiDelaiEnvoi;
        this.traiDelaiConfirmation = traiDelaiConfirmation;
        this.traiDelaiPaiement=traiDelaiPaiement;
        this.traiTauxCourtier = traiTauxCourtier;
        this.traiTauxCourtierPlaceur = traiTauxCourtierPlaceur;
        this.traiTauxAbattement=traiTauxAbattement;
        this.traiSourceRef = traiSourceRef;
        this.natCode = natCode;
        this.devCode = devCode;
        this.traiCompteDevCode = traiCompteDevCode;
        this.courtierPlaceurId=courtierPlaceurId;
    }
}
