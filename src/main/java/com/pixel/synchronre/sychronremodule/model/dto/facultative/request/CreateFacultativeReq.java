package com.pixel.synchronre.sychronremodule.model.dto.facultative.request;

//import com.pixel.synchronre.sychronremodule.model.dto.affaire.validator.UniqueAffCode;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.ExistingCouId;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.validator.ActiveExercice;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ValidEcheanceDate;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidEcheanceDate
public class CreateFacultativeReq
{
    @NotBlank(message = "Veuillez saisir le nom de l'assuré")
    @NotNull(message = "Veuillez saisir le nom de l'assuré")
    private String affAssure;

    @NotBlank(message = "Veuillez saisir l'activité de l'assuré")
    @NotNull(message = "Veuillez saisir l'activité de l'assuré")
    @Length(message = "L'activité doit contenir au moins deux caractères", min = 2)
    private String affActivite;

    @NotNull(message = "Veuillez saisir la date de prise d'effet de l'affaire")
    @FutureOrPresent(message = "Veuiilez saisir une date ultérieure à aujourd'hui")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate affDateEffet;

    @NotNull(message = "Veuillez saisir la date d'échéance de l'affaire")
    @FutureOrPresent(message = "Veuiilez saisir une date ultérieure à aujourd'hui")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate affDateEcheance;

    @NotNull(message = "Veuillez choisir l'exercice")
    @ActiveExercice
    private Long exeCode;

    @NotNull(message = "Veuillez saisir le numéro de police")
    @NotBlank(message = "Veuillez saisir le numéro de police")
    @Length(message = "Le numéro de police doit contenir au moins deux caractères", min = 2)
    private String facNumeroPolice;

    @NotNull(message = "Veuillez saisir le capital de l'affaire")
    @PositiveOrZero(message = "Le capital de l'affaire doit être un nombre positif")
    private BigDecimal affCapitalInitial;

    @NotNull(message = "Veuillez saisir le montant du Sinistre Maximal Possible (SMP)")
    @Positive(message = "Le montant du Sinistre Maximal Possible (SMP) doit être un nombre strictement positif")
    private BigDecimal facSmpLci;

    @NotNull(message = "Veuillez saisir le montant de la prime")
    @PositiveOrZero(message = "Le montant de la prime doit être un nombre positif")
    private BigDecimal facPrime;

    @NotNull(message = "Veuillez définir le statut de l'affaire")
    @NotBlank(message = "Veuillez définir le statut de l'affaire")
    protected String affStatutCreation;

    @ExistingCedId
    private Long cedId;
    @ExistingCouId
    private Long couvertureId;
    protected String devCode;
}
