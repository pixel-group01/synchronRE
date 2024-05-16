package com.pixel.synchronre.sychronremodule.model.dto.territorialite;

import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class TerritorialiteReq
{
    private Long terrId;
    private String terrLibelle;
    private BigDecimal terrTaux;
    private String terrDescription;
    private List<String> paysCodes;
    private List<String> orgCodes;
    @NotNull(message = "L'ID du traité ne peut être null")
    @ExistingTNPId
    private Long traiteNpId;
}