package com.pixel.synchronre.sychronremodule.model.dto.banque.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateBanqueReq
{
//    @NotBlank(message = "Veuillez saisir le code de la banque")
//    @NotNull(message = "Veuillez saisir le code de la banque")
//    @Length(message = "Le code de la banque doit contenir au moins deux caractères", min = 2)
//    @UniqueBanCode
//    private String banCode;
    @NotBlank(message = "Veuillez saisir le numero de compte")
    @NotNull(message = "Veuillez saisir le numero de compte")
    private String banNumCompte;
    private String banIban;
    private String banCodeBic;
    @NotBlank(message = "Veuillez saisir le nom de la banque")
    @NotNull(message = "Veuillez saisir le nom de la banque")
    private String banLibelle;
    @NotBlank(message = "Veuillez saisir l'abréviation de la banque")
    @NotNull(message = "Veuillez saisir l'abréviation de la banque")
    private String banLibelleAbrege;

}
