package com.pixel.synchronre.sychronremodule.model.dto.compte;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrancheCompteDto
{
    private Long trancheId;
    private String trancheLibelle;
    private List<ReadCedanteDTO> cedantes;
    private Long cedIdSelected;
    private List<DetailCompte> detailComptes;
    private List<CompteCessionnaireDto> compteCessionnaires;

    public TrancheCompteDto(Long trancheId, String trancheLibelle) {
        this.trancheId = trancheId;
        this.trancheLibelle = trancheLibelle;
    }
}
