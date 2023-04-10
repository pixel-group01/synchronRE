package com.pixel.synchronre.sychronremodule.model.dto.mouvement.response;

import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MouvementListResp
{
    private Long mvtId;
    private Long affId;
    private String affAssure;
    private String affActivite;
    private String staLibelle;
    private String staLibelleLong;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private String mvtObservation;
    private LocalDateTime mvtDate;
}
