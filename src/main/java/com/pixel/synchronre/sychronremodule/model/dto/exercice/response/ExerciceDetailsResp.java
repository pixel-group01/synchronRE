package com.pixel.synchronre.sychronremodule.model.dto.exercice.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExerciceDetailsResp {
    private Long exeCode;
    private String exeLibelle;
    private String exeCourant;
}
