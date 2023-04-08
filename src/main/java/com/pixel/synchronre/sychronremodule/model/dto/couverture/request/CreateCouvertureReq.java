package com.pixel.synchronre.sychronremodule.model.dto.couverture.request;



import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.UniqueCouLibelleAbrege;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCouvertureReq
{

    @NotBlank(message = "Veuillez saisir le nom de la couverture")
    @NotNull(message = "Veuillez saisir le nom de la couverture")
    private String couLibelle;
    @NotBlank(message = "Veuillez saisir l'abréviation de la couverture")
    @NotNull(message = "Veuillez saisir l'abréviation de la couverture")
    @UniqueCouLibelleAbrege
    private String couLibelleAbrege;
    private Long branId;
}
