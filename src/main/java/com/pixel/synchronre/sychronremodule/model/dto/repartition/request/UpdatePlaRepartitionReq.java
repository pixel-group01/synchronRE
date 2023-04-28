package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau  @CoherentCapitalAndTaux @CoherentTauxCrtAndScms(message = "Le taux de la commission de courtage ne peut exéder celui de la commission de réassurance")
public class UpdatePlaRepartitionReq
{
    @ExistingPlaId
    private Long plaId;
    @NotNull(message = "Veuillez saisir le capital")
    @PositiveOrZero(message = "Le capital doit être un nombre positif")
    private BigDecimal repCapital;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    private BigDecimal repTaux;


    @NotNull(message = "Veuillez saisir la sous commission")
    @PositiveOrZero(message = "La sous commission doit être un nombre positif")
    private BigDecimal repSousCommission; //TODO A Valider

    @NotNull(message = "Veuillez saisir le taux de commission de courtage")
    @PositiveOrZero(message = "Le taux de commission de courtage doit être un nombre positif")
    private BigDecimal repTauxComCourt;

    private MultipartFile avisModificationCession;
}
