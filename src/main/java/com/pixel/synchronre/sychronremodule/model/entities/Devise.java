package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Devise {
    @Id
    private String devCode;
    private String devLibelle;
    private String devLibelleAbrege;
    private String devSymbole;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "devStatut")
    private Statut statut;



    public Devise(String devCode, String devLibelle, String devLibelleAbrege, String devSymbole, Statut statut) {
        this.devCode = devCode;
        this.devLibelle = devLibelle;
        this.devLibelleAbrege = devLibelleAbrege;
        this.devSymbole = devSymbole;
        this.statut = statut;
    }

    public Devise(String devCode) {
        this.devCode = devCode;
    }
}
