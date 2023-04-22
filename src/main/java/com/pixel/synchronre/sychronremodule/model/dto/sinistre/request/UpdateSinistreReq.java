package com.pixel.synchronre.sychronremodule.model.dto.sinistre.request;

import com.pixel.synchronre.authmodule.model.dtos.asignation.CoherentDates;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CREATE_GROUP;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator.ExistingSinId;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator.SeuilSinMontant;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.validator.SinistreNotTooLate;
import com.pixel.synchronre.sychronremodule.model.dto.statut.validator.ExistingStatCode;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilSinMontant(groups = {CREATE_GROUP.class})
@CoherentDates(message = "sinDateSurvenance::La date de survenance ne peut être ultérieure à la date de déclaration")
@SinistreNotTooLate(message = "sinDateSurvenance::La date de survenance du sinistre est n'est pas prise en charge par les termes du contrat")
public class UpdateSinistreReq
{
    @ExistingSinId
    Long sinId;
    private BigDecimal sinMontant100;
    @FutureOrPresent
    private LocalDate sinDateSurvenance;
    @FutureOrPresent
    private LocalDate  sinDateDeclaration;
    private String sinCommentaire;
    @ExistingAffId
    private Long affId;
}
