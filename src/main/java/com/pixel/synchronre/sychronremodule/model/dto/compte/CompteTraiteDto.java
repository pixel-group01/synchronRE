package com.pixel.synchronre.sychronremodule.model.dto.compte;

import com.pixel.synchronre.sychronremodule.model.dto.exercice.validator.ExistingExeCode;
import com.pixel.synchronre.sychronremodule.model.dto.periode.ExistingPeriodeId;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.ExistingTrancheId;
import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
import com.pixel.synchronre.sychronremodule.model.enums.PERIODICITE;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée fournie")
public class CompteTraiteDto {
    private Long compteId;
    @NotNull(message = "Veuillez selectionner un traité")
    @ExistingTNPId
    private Long traiteNpId;
    @NotNull(message = "Veuillez selectionner un exercide") @ExistingExeCode
    private Long exeCode;
    private String traiReference;
    private String traiNumero;
    private String natCode;
    private String natLibelle;
    private String traiPeriodicite;
    private String traiEcerciceRattachement;
    @NotNull(message = "Veuillez selectionner une tranche") @ExistingTrancheId
    private Long trancheIdSelected;
    @NotNull(message = "Veuillez selectionner une période")
    @ExistingPeriodeId
    private Long periodeId;
    @NotNull(message = "La liste des tranche ne peut être vide")
    @NotEmpty(message = "La liste des tranche ne peut être vide")
    private List<TrancheCompteDto> trancheCompteDtos;

    public CompteTraiteDto(Long traiteNpId, Long exeCode, String traiReference, String traiNumero, String natCode, String natLibelle, PERIODICITE traiPeriodicite, EXERCICE_RATTACHEMENT traiEcerciceRattachement) {
        this.traiteNpId = traiteNpId;
        this.exeCode = exeCode;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
        this.natCode = natCode;
        this.natLibelle = natLibelle;
        this.traiPeriodicite = traiPeriodicite.getLibelle();
        this.traiEcerciceRattachement = traiEcerciceRattachement.getLibelle();
    }
}
