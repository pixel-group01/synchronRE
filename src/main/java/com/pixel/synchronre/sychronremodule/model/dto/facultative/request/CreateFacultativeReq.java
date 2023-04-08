package com.pixel.synchronre.sychronremodule.model.dto.facultative.request;

//import com.pixel.synchronre.sychronremodule.model.dto.affaire.validator.UniqueAffCode;
import com.pixel.synchronre.sychronremodule.model.entities.Cedente;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateFacultativeReq
{
    private String affCode;
    private String affAssure;
    private String affActivite;
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private int affEtat;
    private String facNumeroPolice;
    private Long facCapitaux;
    private Long facSmpLci;
    private float facPrime;
    private Long cedenteId;
    private String statutCode;
    protected Long couvertureId;
}
