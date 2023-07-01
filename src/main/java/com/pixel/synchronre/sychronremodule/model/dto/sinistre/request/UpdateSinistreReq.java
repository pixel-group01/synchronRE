package com.pixel.synchronre.sychronremodule.model.dto.sinistre.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pixel.synchronre.authmodule.model.dtos.asignation.CoherentDates;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator.ExistingSinId;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator.SeuilSinMontant;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator.SinistreNotTooLate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilSinMontant
@CoherentDates(message = "sinDateSurvenance::La date de survenance ne peut être ultérieure à la date de déclaration")
@SinistreNotTooLate(message = "sinDateSurvenance::La date de survenance du sinistre est n'est pas prise en charge par les termes du contrat")
public class UpdateSinistreReq
{
    @ExistingSinId
    @NotNull(message = "L'ID du sinistre ne peut être null")
    Long sinId;
    @NotNull(message = "Veuillez saisir le montant du sinistre")
    private BigDecimal sinMontant100;
    private BigDecimal sinMontantHonoraire;
    @PastOrPresent(message = "La date de survenance du sinistre ne peut être future")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate sinDateSurvenance;
    @PastOrPresent(message = "La date de déclaration du sinistre ne peut être future")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate  sinDateDeclaration;
    private String sinCommentaire;
    @ExistingAffId
    private Long affId;
}