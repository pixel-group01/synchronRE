package com.pixel.synchronre.sychronremodule.model.dto.mouvement.response;

import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private Long sinId;
    private String sinCode;
    private BigDecimal sinMontant100;
    private BigDecimal sinMontantHonoraire;
    private LocalDate sinDateSurvenance;
    private LocalDate  sinDateDeclaration;
    private String mvtObservation;
    private String userEmail;
    private String userFullNom;
    private String functionName;
    private LocalDateTime mvtDate;
}
