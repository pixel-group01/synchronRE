package com.pixel.synchronre.sychronremodule.model.dto.facultative.request;

//import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.UniqueAffCode;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.ExistingCouId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.NotNullSmpForAffaireRealise;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ValidEcheanceDate;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ValidStatutCreation;
import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;


//@UniqueAffCode(message ="staCode::Ce code statut est déjà utilisé")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidEcheanceDate @NotNullSmpForAffaireRealise
public class UpdateFacultativeReq
{
    @ExistingAffId
    @NotNull(message = "Veuillez choisir une affaire")
    private Long affId;
    @NotBlank(message = "Veuillez saisir le nom de l'assuré")
    @NotNull(message = "Veuillez saisir le nom de l'assuré")
    private String affAssure;

    @NotBlank(message = "Veuillez saisir l'activité de l'assuré")
    @NotNull(message = "Veuillez saisir l'activité de l'assuré")
    @Length(message = "L'activité doit contenir au moins deux caractères", min = 2)
    private String affActivite;

    @NotNull(message = "Veuillez saisir la date de prise d'effet de l'affaire")
    //@FutureOrPresent(message = "Veuiilez saisir une date ultérieure à aujourd'hui")
    private LocalDate affDateEffet;

    @NotNull(message = "Veuillez saisir la date d'échéance de l'affaire")
    //@FutureOrPresent(message = "Veuiilez saisir une date ultérieure à aujourd'hui")
    private LocalDate affDateEcheance;

    //@NotNull(message = "Veuillez saisir le numéro de police")
    //@NotBlank(message = "Veuillez saisir le numéro de police")
    //@Length(message = "Le code de la banque doit contenir au moins deux caractères", min = 2)
    private String facNumeroPolice;

    @NotNull(message = "Veuillez saisir le capital de l'affaire")
    @PositiveOrZero(message = "Le capital de l'affaire doit être un nombre positif")
    private BigDecimal affCapitalInitial;

    //@NotNull(message = "Veuillez saisir le montant du Sinistre Maximal Possible (SMP)")
    @PositiveOrZero(message = "Le montant du Sinistre Maximal Possible (SMP) doit être un nombre positif")
    private BigDecimal facSmpLci;

    @NotNull(message = "Veuillez définir le statut de l'affaire")
    @NotBlank(message = "Veuillez définir le statut de l'affaire")
    @ValidStatutCreation
    protected String affStatutCreation;

    private BigDecimal affCoursDevise;

    @NotNull(message = "Veuillez saisir le montant de la prime")
    @PositiveOrZero(message = "Le montant de la prime doit être un nombre positif")
    private BigDecimal facPrime;
    @ExistingCedId
    private Long cedId;
    @ExistingCouId
    private Long couvertureId;
}
