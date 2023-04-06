package com.pixel.synchronre.typemodule.model.dtos;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString //@Entity
@UniqueTypeCode(message = "uniqueCode::Code de type est déjà utilisé")
public class UpdateTypeDTO
{
    @NotNull(message = "L'ID du type à modifier ne peut être nul")
    @ExistingTypeId
    private Long typeId;
    @Pattern(message = "Le groupeCode ne doit contenir d'espace, il doit commencer par TYP_ ou STA_ et contenir entre 6 et 8 caractères", regexp = "(^TYP_|^STA_)\\w{2,4}")
    @NotBlank(message = "Le groupCode ne peut être null")
    @NotNull(message = "Le groupCode ne peut être null")
    private String typeGroup;

    @NotBlank(message = "Le uniqueCode ne peut être null")
    @NotNull(message = "Le uniqueCode ne peut être null")
    private String uniqueCode;

    @Length(message = "Le nom du type doit contenir au moins deux caratères", min = 2)
    @NotBlank(message = "Le nom du type ne peut être nul")
    @NotNull(message = "Le nom du type ne peut être nul")
    private String name;
}