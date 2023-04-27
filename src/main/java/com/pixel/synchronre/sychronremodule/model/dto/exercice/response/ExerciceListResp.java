package com.pixel.synchronre.sychronremodule.model.dto.exercice.response;

import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExerciceListResp {
    private Long exeCode;
    private String exeLibelle;
    private boolean exeCourant;
}
