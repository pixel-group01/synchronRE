package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pixel.synchronre.sychronremodule.model.dto.devise.validator.ExistingDevCode;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.validator.ActiveExercice;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.validator.ExistingExeCode;
import com.pixel.synchronre.sychronremodule.model.dto.nature.validator.ExistingNatCode;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTraiteNpRef;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.UniqueTraiteNpRef;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.UniqueTraiteNumero;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.enums.validators.ValidPeriodicite;
import com.pixel.synchronre.sychronremodule.model.enums.validators.ValidRattachement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class CreateTraiteNPReq
{
    @NotNull(message = "La référence unique est obligatoire")
    @NotBlank(message = "La référence unique est obligatoire")
    @UniqueTraiteNpRef @NotNull(message = "Veuillez saisir la référence du traité")
    private String traiReference;
    @UniqueTraiteNumero
    @NotNull(message = "Veuillez saisir le numéro du traité")
    @NotBlank(message = "Veuillez saisir le numéro du traité")
    private String traiNumero;
    private String traiLibelle;
    private String traiAuteur;
    @NotNull(message = "Veuillez saisir l'exercice de rattachement")
    @NotBlank(message = "Veuillez saisir l'exercice de rattachement")
    @ValidRattachement
    private String traiEcerciceRattachement;
    @NotNull(message = "Veuillez saisir la date d'effet")
    private LocalDate traiDateEffet;
    @NotNull(message = "Veuillez saisir la date d'échéance")
    private LocalDate traiDateEcheance;
    private BigDecimal traiCoursDevise;
    @NotNull(message = "Veuillez saisir la périodicité")
    @NotNull(message = "Veuillez saisir la périodicité")
    @ValidPeriodicite
    private String traiPeriodicite;
    private Long traiDelaiEnvoi;
    private Long traiDelaiConfirmation;
    private Long traiDelaiPaiement;
    private BigDecimal traiTauxCourtier;
    private BigDecimal traiTauxCourtierPlaceur;
    private BigDecimal traiTauxAbattement;
    @NotNull(message = "Veuillez sélectionner la gestion du traité")
    @ExistingExeCode @ActiveExercice
    private Long exeCode;
    @ExistingTraiteNpRef(message = "La référence du traité source est introuvable")
    private String traiSourceRef;
    @ExistingNatCode
    private String natCode;
    @ExistingDevCode(message = "La dévise du traité est introuvable")
    @NotNull(message = "Veuillez saisir la divise du traité")
    @NotNull(message = "Veuillez saisir la divise du traité")
    private String devCode;
    @ExistingDevCode(message = "La dévise des comptes sur le traité est introuvable")
    @NotNull(message = "Veuillez saisir la divise des comptes")
    @NotBlank(message = "Veuillez saisir la divise des comptes")
    private String traiCompteDevCode;
    @NotNull(message = "Veuillez sélectionner le courtier placeur")
    private Long courtierPlaceurId;
}
