package com.pixel.synchronre.sychronremodule.model.dto.exercice.request;

import com.pixel.synchronre.sychronremodule.model.dto.exercice.validator.UniqueExoCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateExerciceReq {
    @NotNull(message = "Veuillez saisir le code de l'exercice")
    @UniqueExoCode
    private Long exeCode;
    private String exeLibelle;
    private boolean exeCourant;
}
