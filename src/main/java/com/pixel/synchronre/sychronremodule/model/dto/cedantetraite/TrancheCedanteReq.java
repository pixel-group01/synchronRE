package com.pixel.synchronre.sychronremodule.model.dto.cedantetraite;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePrimeDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrancheCedanteReq
{
    private Long traiteNpId;
    @NotNull(message = "Veuillez choisir la cédante")
    @ExistingCedId
    private Long cedId;
    private List<ReadCedanteDTO> cedantes;


    private List<TranchePrimeDto> tranchePrimeDtos;

    public TrancheCedanteReq(Long traiteNpId, Long cedId) {
        this.traiteNpId = traiteNpId;
        this.cedId = cedId;
    }
}
