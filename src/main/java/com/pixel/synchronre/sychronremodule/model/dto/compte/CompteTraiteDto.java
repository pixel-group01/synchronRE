package com.pixel.synchronre.sychronremodule.model.dto.compte;

import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
import com.pixel.synchronre.sychronremodule.model.enums.PERIODICITE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CompteTraiteDto {
    private Long compteId;
    private Long traiteNpId;
    private Long exeCode;
    private String traiReference;
    private String traiNumero;
    private String natCode;
    private String natLibelle;
    private String traiPeriodicite;
    private String traiEcerciceRattachement;
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
