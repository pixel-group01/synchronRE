package com.pixel.synchronre.sychronremodule.model.dto.risquecouvert;

import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.ExistingCouId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class UpdateRisqueCouvertReq
{
    @ExistingRisqueId
    private Long risqueId;

    @ExistingCouId
    @NotNull(message = "Veuillez selectionner la couverture")
    private Long couId;
    private String description;
    private List<Long> sousCouIds;

    public UpdateRisqueCouvertReq(Long risqueId, Long couId, String description) {
        this.risqueId = risqueId;
        this.couId = couId;
        this.description = description;
    }
}