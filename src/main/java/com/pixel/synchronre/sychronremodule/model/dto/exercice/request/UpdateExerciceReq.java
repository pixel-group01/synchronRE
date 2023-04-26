package com.pixel.synchronre.sychronremodule.model.dto.exercice.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateExerciceReq {
    private Long exeCode;
    private String exeLibelle;
    private boolean exeCourant;
    private String staCode;
}
