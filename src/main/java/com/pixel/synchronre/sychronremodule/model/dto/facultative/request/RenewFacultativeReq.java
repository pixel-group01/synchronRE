package com.pixel.synchronre.sychronremodule.model.dto.facultative.request;

//import com.pixel.synchronre.sychronremodule.model.dto.affaire.validator.UniqueAffCode;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.ExistingCouId;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.validator.ActiveExercice;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidEcheanceDate @NotNullSmpForAffaireRealise
public class RenewFacultativeReq
{
    @ExistingAffId
    private Long affId;
    @NotNull(message = "Veuillez saisir la date de prise d'effet de l'affaire")
    //@FutureOrPresent(message = "La date d'effet de l'affaire doit être une date ultérieure à aujourd'hui")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate affDateEffet;

    @NotNull(message = "Veuillez saisir la date d'échéance de l'affaire")
    //@FutureOrPresent(message = "Veuiilez saisir une date ultérieure à aujourd'hui")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate affDateEcheance;

    //@NotNull(message = "Veuillez saisir le numéro de police")
    //@NotBlank(message = "Veuillez saisir le numéro de police")
    //@Length(message = "Le numéro de police doit contenir au moins deux caractères", min = 2)
    private String facNumeroPolice;

    @NotNull(message = "Veuillez saisir le capital de l'affaire")
    @PositiveOrZero(message = "Le capital de l'affaire doit être un nombre positif")
    private BigDecimal affCapitalInitial;

    //@NotNull(message = "Veuillez saisir le montant du Sinistre Maximal Possible (SMP)")
    @PositiveOrZero(message = "Le montant du Sinistre Maximal Possible (SMP) doit être un nombre positif")
    private BigDecimal facSmpLci;

    @NotNull(message = "Veuillez saisir le montant de la prime")
    @PositiveOrZero(message = "Le montant de la prime doit être un nombre positif")
    private BigDecimal facPrime;

    @NotNull(message = "Veuillez définir le statut de l'affaire")
    @NotBlank(message = "Veuillez définir le statut de l'affaire")
    @ValidStatutCreation
    protected String affStatutCreation;

    protected BigDecimal affCoursDevise;
}
