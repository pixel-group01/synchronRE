package com.pixel.synchronre.sychronRe.model.dto.request;

import com.pixel.synchronre.sychronRe.model.dto.validator.NoneExistingStatutCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatutReq {
    @NotBlank(message = "Veuillez saisir le code")
    @NotNull(message = "Veuillez saisir le code")
    @Length(message = "Le code doit contenir au moins 3 caractères", min = 3)
    @NoneExistingStatutCode
    private String staCode;

    @NotBlank(message = "Veuillez saisir le libelle")
    @NotNull(message = "Veuillez saisir le libelle")
    @Length(message = "Le libelle doit contenir au moins 3 caractères", min = 3)
    private String staLibelle;

    private String staLibelleAbrege;
}
